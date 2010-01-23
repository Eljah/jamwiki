/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itmatter.jamwiki.analyzer;

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
import org.jamwiki.parser.bliki.BlikiProxyParser;
import org.jamwiki.utils.Utilities;
import org.apache.log4j.Logger;

/**
 *
 * @author Daniel.Fisla
 */
public class Main {

    private static Logger logger = Logger.getLogger(Main.class);
    private static final String USAGE = "[-h] [-t <string:topic name> -w <string:wiki name>] -c <string:all,wiki,txt,html>| [-r <string:regex>]";
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

            if (cmd.hasOption("c")) {
                contentType = cmd.getOptionValue("c");
            }

            DataHandler dh = WikiBase.getDataHandler();

            if ((topicName != null) && (wikiName != null) && (contentType != null)) {

                Topic topic = dh.lookupTopic(wikiName, topicName, true, null);
                Locale locale = new Locale("en", "US");

                //TODO: Switch to new BlikiProxyParser
                /*
                WikiModel wikiModel = new WikiModel("${image}", "${title}");

                String wikiContent = topic.getTopicContent();
                String htmlContent = wikiModel.render(wikiContent);
                writeToFile(topicName + ".wiki.txt", wikiContent);
                writeToHtmlFile(topicName + ".html", htmlContent);

                String content = Utilities.stripMarkup(htmlContent);
                writeToFile(topicName + ".clean.txt", StringEscapeUtils.unescapeHtml(content));
                 */

                ParserInput parserInput = new ParserInput();
                parserInput.setContext("/wiki");
                parserInput.setLocale(locale);
                //parserInput.setWikiUser(user);
                parserInput.setTopicName(topicName);
                //parserInput.setUserIpAddress(ServletUtil.getIpAddress(request));
                parserInput.setVirtualWiki(wikiName);
                parserInput.setAllowSectionEdit(true);
                BlikiProxyParser wikiParser = new BlikiProxyParser(parserInput);

                ParserOutput parserOutput = new ParserOutput();

                if (contentType.equalsIgnoreCase("all")) {
                    String wikiContent = topic.getTopicContent();
                    String htmlContent = wikiParser.parseHTML(parserOutput, wikiContent);

                    writeToFile(topicName + ".wiki.txt", wikiContent);
                    writeToHtmlFile(topicName + ".html", htmlContent);

                    String content = Utilities.stripMarkup(htmlContent);
                    writeToFile(topicName + ".clean.txt", StringEscapeUtils.unescapeHtml(content));
                } else if (contentType.equalsIgnoreCase("html")) {
                    String wikiContent = topic.getTopicContent();
                    String htmlContent = wikiParser.parseHTML(parserOutput, wikiContent);

                    writeToHtmlFile(topicName + ".html", htmlContent);
                } else if (contentType.equalsIgnoreCase("wiki")) {
                    String wikiContent = topic.getTopicContent();

                    writeToFile(topicName + ".wiki.txt", wikiContent);
                } else if (contentType.equalsIgnoreCase("txt")) {
                    String wikiContent = topic.getTopicContent();
                    String htmlContent = wikiParser.parseHTML(parserOutput, wikiContent);

                    String content = Utilities.stripMarkup(htmlContent);
                    writeToFile(topicName + ".clean.txt", StringEscapeUtils.unescapeHtml(content));
                }
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
