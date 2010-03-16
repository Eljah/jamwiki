/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itmatter.jamwiki.analyzer;

import info.bliki.wiki.filter.HTMLConverter;
import info.bliki.wiki.filter.PlainTextConverter;
import info.bliki.wiki.model.WikiModel;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang.StringEscapeUtils;
import org.jamwiki.DataHandler;
import org.jamwiki.WikiBase;
import org.jamwiki.model.Topic;

import org.jamwiki.parser.ParserInput;
import org.jamwiki.parser.ParserOutput;
import org.jamwiki.utils.Utilities;
import org.apache.log4j.Logger;
import org.jamwiki.Environment;
import org.jamwiki.parser.bliki.BlikiProxyParserUtil;
import org.jamwiki.parser.bliki.JAMHTMLConverter;
import org.jamwiki.parser.bliki.JAMWikiModel;
import org.jamwiki.parser.jflex.JFlexParser;

/**
 *
 * @author Daniel.Fisla
 */
public class Main {

    private static Logger logger = Logger.getLogger(Main.class);
    private static final String USAGE = "[-h] [-t <string:topic name> -w <string:wiki name>] -c <string:all,wiki,txt,html> -p <bliki,blikiproxy,jflex>| [-r <string:regex>]";
    private static final String HEADER = "JAM Wiki Content Analyzer - Parses and Transforms article content.";
    private static final String FOOTER = "";

    private static void printUsage(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        //helpFormatter.setWidth(120);
        helpFormatter.printHelp(USAGE, HEADER, options, FOOTER);
    }

    private static void writeToFile(String fileName, String content) {

        try {

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF8"));
            out.write(content);
            out.close();

        } catch (UnsupportedEncodingException ue) {
            logger.error(ue.getMessage(), ue);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static void writeToHtmlFile(String fileName, String content) {

        try {
            String prefix = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; Charset=UTF-8\" /></head><body>";
            String suffix = "</body></html>";

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF8"));
            out.write(prefix + content + suffix);
            out.close();

        } catch (UnsupportedEncodingException ue) {
            logger.error(ue.getMessage(), ue);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        logger.debug("Started...");
        Options options = new Options();
        options.addOption("h", "help", false, "Print this usage information");
        options.addOption("w", "wiki", true, "Wiki name");
        options.addOption("t", "topic", true, "Topic name");
        options.addOption("f", "file", true, "Source file");
        options.addOption("p", "parser", true, "Parser type");
        options.addOption("c", "content", true, "Content type");

        CommandLineParser parser = null;

        CommandLine cmd = null;

        try {
            if (args.length == 0) {
                printUsage(options);
                System.exit(0);
            }

            parser = new PosixParser();
            cmd = parser.parse(options, args);

            String wikiName = null;
            String topicName = null;
            String sourceFile = null;
            String parserType = new String("blikiproxy");
            String contentType = new String("all");

            if (cmd.hasOption("h")) {
                printUsage(options);
                System.exit(0);
            }

            if (cmd.hasOption("w")) {
                wikiName = cmd.getOptionValue("w");
            }

            if (cmd.hasOption("t")) {
                topicName = cmd.getOptionValue("t");
            }

            if (cmd.hasOption("f")) {
                sourceFile = cmd.getOptionValue("f");
            }

            if (cmd.hasOption("c")) {
                contentType = cmd.getOptionValue("c");
            }

            if (cmd.hasOption("p")) {
                parserType = cmd.getOptionValue("p");
            }

            DataHandler dh = WikiBase.getDataHandler();

            if ((topicName != null) && (wikiName != null) && (contentType != null)) {

                Topic topic = dh.lookupTopic(wikiName, topicName, true, null);
                Locale locale = new Locale("en", "US");

                if ((parserType != null) & (parserType.equalsIgnoreCase("blikiproxy"))) {

                    ParserInput parserInput = new ParserInput();
                    parserInput.setContext("");
                    parserInput.setLocale(locale);
                    //parserInput.setWikiUser(user);
                    parserInput.setTopicName(topicName);
                    //parserInput.setUserIpAddress(ServletUtil.getIpAddress(request));
                    parserInput.setVirtualWiki(wikiName);
                    parserInput.setAllowSectionEdit(false);

                    ParserOutput parserOutput = new ParserOutput();

                    if (contentType.equalsIgnoreCase("all")) {
                        String wikiContent = topic.getTopicContent();

                        JAMWikiModel wikiModel = new JAMWikiModel(parserInput, parserOutput, "");

                        if (Environment.getBooleanValue(Environment.PROP_PARSER_REMOVE_UNSUPPORTED)) {
                            wikiContent = BlikiProxyParserUtil.parseUnsupportedMediaWikiMarkup(parserInput, wikiContent);
                        }

                        String htmlContent = wikiModel.render(new JAMHTMLConverter(parserInput), wikiContent);
                        if (htmlContent == null) {
                            htmlContent = "";
                        } else {

                            if (Environment.getBooleanValue(Environment.PROP_PARSER_CLEAN_HTML)) {
                                htmlContent = BlikiProxyParserUtil.cleanupHtmlParserError(parserInput, htmlContent);
                            }
                        }

                        writeToFile(topicName + ".wiki.txt", wikiContent);
                        writeToHtmlFile(topicName + ".blikiproxy.html", htmlContent);

                        String content = Utilities.stripMarkup(htmlContent);
                        writeToFile(topicName + ".clean.txt", StringEscapeUtils.unescapeHtml(content));
                    } else if (contentType.equalsIgnoreCase("html")) {
                        String wikiContent = topic.getTopicContent();

                        JAMWikiModel wikiModel = new JAMWikiModel(parserInput, parserOutput, "");
                        String htmlContent = wikiModel.render(new JAMHTMLConverter(parserInput), wikiContent);

                        writeToHtmlFile(topicName + ".blikiproxy.html", htmlContent);
                    } else if (contentType.equalsIgnoreCase("wiki")) {
                        String wikiContent = topic.getTopicContent();

                        writeToFile(topicName + ".wiki.txt", wikiContent);
                    } else if (contentType.equalsIgnoreCase("txt")) {
                        String wikiContent = topic.getTopicContent();

                        JAMWikiModel wikiModel = new JAMWikiModel(parserInput, parserOutput, "");
                        String htmlContent = wikiModel.render(new JAMHTMLConverter(parserInput), wikiContent);

                        String content = Utilities.stripMarkup(htmlContent);
                        writeToFile(topicName + ".clean.txt", StringEscapeUtils.unescapeHtml(content));
                    }
                } else if ((parserType != null) & (parserType.equalsIgnoreCase("bliki"))) {

                    if (contentType.equalsIgnoreCase("all")) {
                        String wikiContent = topic.getTopicContent();
                        WikiModel wikiModel = new WikiModel("${image}", "${title}");
                        String htmlContent = wikiModel.render(new HTMLConverter(), wikiContent);

                        writeToFile(topicName + ".wiki.txt", wikiContent);
                        writeToHtmlFile(topicName + ".bliki.html", htmlContent);

                        String plainContent = wikiModel.render(new PlainTextConverter(), wikiContent);
                        writeToFile(topicName + ".clean.txt", plainContent);
                    } else if (contentType.equalsIgnoreCase("html")) {
                        String wikiContent = topic.getTopicContent();
                        WikiModel wikiModel = new WikiModel("${image}", "${title}");
                        String htmlContent = wikiModel.render(new HTMLConverter(), wikiContent);

                        writeToHtmlFile(topicName + ".bliki.html", htmlContent);
                    } else if (contentType.equalsIgnoreCase("wiki")) {
                        String wikiContent = topic.getTopicContent();

                        writeToFile(topicName + ".wiki.txt", wikiContent);
                    } else if (contentType.equalsIgnoreCase("txt")) {
                        String wikiContent = topic.getTopicContent();
                        WikiModel wikiModel = new WikiModel("${image}", "${title}");

                        String plainContent = wikiModel.render(new PlainTextConverter(), wikiContent);
                        writeToFile(topicName + ".clean.txt", plainContent);
                    }
                } else if ((parserType != null) & (parserType.equalsIgnoreCase("jflex"))) {

                    ParserInput parserInput = new ParserInput();
                    parserInput.setContext("");
                    parserInput.setLocale(locale);
                    //parserInput.setWikiUser(user);
                    parserInput.setTopicName(topicName);
                    //parserInput.setUserIpAddress(ServletUtil.getIpAddress(request));
                    parserInput.setVirtualWiki(wikiName);

                    JFlexParser wikiParser = new JFlexParser(parserInput);

                    ParserOutput parserOutput = new ParserOutput();

                    if (contentType.equalsIgnoreCase("all")) {
                        String wikiContent = topic.getTopicContent();
                        String htmlContent = wikiParser.parseHTML(parserOutput, wikiContent);

                        writeToFile(topicName + ".wiki.txt", wikiContent);
                        writeToHtmlFile(topicName + ".jflex.html", htmlContent);

                        String content = Utilities.stripMarkup(htmlContent);
                        writeToFile(topicName + ".clean.txt", StringEscapeUtils.unescapeHtml(content));
                    } else if (contentType.equalsIgnoreCase("html")) {
                        String wikiContent = topic.getTopicContent();
                        String htmlContent = wikiParser.parseHTML(parserOutput, wikiContent);

                        writeToHtmlFile(topicName + ".jflex.html", htmlContent);
                    } else if (contentType.equalsIgnoreCase("wiki")) {
                        String wikiContent = topic.getTopicContent();

                        writeToFile(topicName + ".wiki.txt", wikiContent);
                    } else if (contentType.equalsIgnoreCase("txt")) {
                        String wikiContent = topic.getTopicContent();
                        String htmlContent = wikiParser.parseHTML(parserOutput, wikiContent);

                        String content = Utilities.stripMarkup(htmlContent);
                        writeToFile(topicName + ".clean.txt", StringEscapeUtils.unescapeHtml(content));
                    } else {
                        logger.error("Incorrect parser type!");
                    }
                }
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
