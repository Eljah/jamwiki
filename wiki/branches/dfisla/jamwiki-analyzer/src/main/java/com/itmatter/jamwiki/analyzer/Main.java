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
import java.util.regex.Pattern;
import org.apache.commons.cli.*;
import org.apache.commons.lang.*;
import org.jamwiki.DataHandler;
import org.jamwiki.WikiBase;
import org.jamwiki.model.Topic;

import org.jamwiki.utils.HtmlUtil;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiLogger;

/**
 *
 * @author Daniel.Fisla
 */
public class Main {

    private static final WikiLogger logger = WikiLogger.getLogger(Main.class.getName());
    private static final String USAGE = "[-h] [-t <string:topic name> -w <string:wiki name>] | [-r <string:regex>]";
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

        logger.fine("Started...");
        Options options = new Options();
        options.addOption("h", "help", false, "Print this usage information");
        options.addOption("w", "wiki", true, "Wiki name");
        options.addOption("t", "topic", true, "Topic name");

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

            if ((topicName != null) && (wikiName != null)) {

                DataHandler dh = WikiBase.getDataHandler();
                Topic topic = dh.lookupTopic(wikiName, topicName, true, null);
                Locale locale = new Locale("en", "US");

                String wikiContent = topic.getTopicContent();
                String htmlContent = HtmlUtil.parseToHtml(wikiContent, topic.getName(), "/wiki", locale, wikiName);
                writeToHtmlFile(topicName + ".wiki.txt", wikiContent);
                writeToHtmlFile(topicName + ".html", htmlContent);

                //content = HtmlUtil.removeTemplateTags(htmlContent);
                String content = Utilities.stripMarkup(htmlContent); //HtmlUtil.removeHtmlTags(content);

                //writeToFile(topicName + ".txt", content);
                writeToFile(topicName + ".clean.txt", StringEscapeUtils.unescapeHtml(content));
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
