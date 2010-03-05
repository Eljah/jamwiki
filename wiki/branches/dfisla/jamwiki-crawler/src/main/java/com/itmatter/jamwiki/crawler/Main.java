/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itmatter.jamwiki.crawler;

import java.io.BufferedReader;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.*;
import org.jamwiki.model.WikiUser;

import org.apache.log4j.*;

/**
 *
 * @author Daniel.Fisla
 */
public class Main {

    static Logger logger = Logger.getLogger(Main.class.getName());
    private static final String USAGE = "[-h] [-f <file>] [-l <file>] [-p <file1,file2,...> -c]";
    private static final String HEADER = "JAM Wiki Web Crawler - Imports MediaWiki wikipedia XML exports to JAMWiki.";
    private static final String FOOTER = "";

    private static void printUsage(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(USAGE, HEADER, options, FOOTER);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        logger.info("...start...");

        System.out.println(new Timestamp(System.currentTimeMillis()));

        Options options = new Options();
        options.addOption("h", "help", false, "Print this usage information");
        options.addOption("c", "crawl", false, "Rebuild all cached topic and topic version content");
        options.addOption("m", "mode", true, "Fetch using 'web' or 'db' modes.");
        options.addOption("f", "file", true, "File name and path");
        options.addOption("l", "log", true, "File name and path");
        options.addOption("u", "url", true, "Application Root URL Prefix");
        options.addOption("p", "partitions", true, "Partition 1,2,3,4 file name and path");

        CommandLineParser parser = null;
        CommandLine cmd = null;

        try {
            if (args.length == 0) {
                printUsage(options);
                System.exit(0);
            }

            parser = new PosixParser();
            cmd = parser.parse(options, args);

            String fileName = null;
            String appUrl = null;
            String fetchMode = "web";
            String logFile = "parser-log.txt";

            List<String> partitions = null;

            boolean crawl = false;
            boolean concurrent = false;

            // NOTE: when checking Option Value, always use short form in code as below
            if (cmd.hasOption("h")) {
                printUsage(options);
                System.exit(0);
            }

            if (cmd.hasOption("f")) {
                fileName = cmd.getOptionValue("f");
                logger.debug("XML-FILE =>: " + fileName);
            }

            if (cmd.hasOption("u")) {
                appUrl = cmd.getOptionValue("u");
                logger.debug("URL-PREFIX =>: " + appUrl);
            }

            if (cmd.hasOption("m")) {
                fetchMode = cmd.getOptionValue("m");
                logger.debug("FETCH-MODE =>: " + fetchMode);
            }

            if (cmd.hasOption("l")) {
                logFile = cmd.getOptionValue("l");
                logger.debug("LOG-FILE =>: " + logFile);
            }

            if (cmd.hasOption("p")) {
                String pfileNames = cmd.getOptionValue("p");
                logger.info("PARTITION-LIST =>: " + pfileNames);

                if (pfileNames != null) {
                    String[] parts = pfileNames.split(",");
                    partitions = new ArrayList<String>();

                    for (int x = 0; x < parts.length; x++) {
                        partitions.add(parts[x]);
                    }

                    concurrent = true;
                }
            }

            if (cmd.hasOption("c")) {
                logger.debug("CRAWL-TOPICS-ALL");
                crawl = true;
            }

            if ((concurrent) && (crawl) && ((appUrl != null) || fetchMode.equalsIgnoreCase("db"))) {
                logger.info("Starting Topic Crawling Process...");

                WikiUser user = new WikiUser("admin");
                crawlContent(fetchMode, logFile, "en", 1, user, "127.0.0.1", partitions, appUrl);
            } else {
                printUsage(options);
                System.exit(1);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private static void crawlContent(String fetchMode, String logFile, String virtualWikiName, int virtualWikiId, WikiUser user, String ip, List<String> partitions, String url) {
        try {
            int size = partitions.size();

            BulkLoaderPoolExecutor blpe = new BulkLoaderPoolExecutor(size, 50);

            int pCount = 0;
            int pSleepRampDown = 7500;

            for (String pName : partitions) {
                List<String> pTopicIds = initTopicNames(pName);

                JAMWikiCrawlHandler rebuildHandler = new JAMWikiCrawlHandler(fetchMode, logFile, virtualWikiName, virtualWikiId, user, ip, pTopicIds);
                rebuildHandler.setAppUrl(url);
                blpe.runTask(rebuildHandler);

                int pSleepTime = pSleepRampDown - (2500 * pCount);
                if (pSleepTime > 0) {
                    Thread.sleep(pSleepTime);
                }
                pCount++;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private static List<String> initTopicNames(String fileName) {

        List<String> topicNames = null;

        try {
            logger.info("LOADING-NAMEs ...");
            topicNames = new ArrayList<String>();

            FileInputStream fstream = new FileInputStream(fileName);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            //Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                topicNames.add(strLine);
            }
            //Close the input stream
            in.close();
            logger.info("LOADED-NAMEs =>: " + topicNames.size());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return topicNames;
    }
}
