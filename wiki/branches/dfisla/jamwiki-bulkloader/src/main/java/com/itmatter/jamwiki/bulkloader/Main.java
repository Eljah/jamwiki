/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itmatter.jamwiki.bulkloader;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.cli.*;
import org.jamwiki.model.WikiUser;

import org.apache.log4j.*;
import org.xml.sax.InputSource;

/**
 *
 * @author Daniel.Fisla
 */
public class Main {

    static Logger logger = Logger.getLogger(Main.class.getName());
    private static final String USAGE = "[-h] [-f <file> -l] [-p <file1,file2,...> -r | -c]";
    private static final String HEADER = "JAM Wiki Bulk Loader - Imports MediaWiki wikipedia XML exports to JAMWiki.";
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

        Options options = new Options();
        options.addOption("h", "help", false, "Print this usage information");
        options.addOption("r", "rebuild", false, "Rebuild all topics, parses content, generates clean/txt content, partitions must be specified");
        options.addOption("c", "category-rebuild", false, "Rebuild all topic categories only, no topic loading, partitions must be specified");
        options.addOption("l", "load", false, "Load all topics only, no category building");
        options.addOption("n", "number", true, "Limit the number of pages to import");
        options.addOption("f", "file", true, "File name and path of XML file");
        options.addOption("p", "partitions", true, "Partition [1,2,3,...] file names, where each p contains a list of '\\n' delimited Topic IDs");

        CommandLineParser parser = null;
        CommandLine cmd = null;

        try {
            if (args.length == 0) {
                printUsage(options);
                System.exit(0);
            }

            parser = new PosixParser();
            cmd = parser.parse(options, args);

            int pageLimit = Integer.MAX_VALUE;
            String fileName = null;

            List<String> partitions = null;

            boolean rebuild = false;
            boolean load = false;
            boolean category = false;
            boolean concurrent = false;

            // NOTE: when checking Option Value, always use short form in code as below
            if (cmd.hasOption("h")) {
                printUsage(options);
                System.exit(0);
            }

            if (cmd.hasOption("n")) {
                pageLimit = Integer.parseInt(cmd.getOptionValue("n"));
                logger.debug("PAGE-LIMIT =>: " + pageLimit);
            }
            if (cmd.hasOption("f")) {
                fileName = cmd.getOptionValue("f");
                logger.debug("XML-FILE =>: " + fileName);
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

            if (cmd.hasOption("r")) {
                logger.debug("REBUILD-ALL");
                rebuild = true;
            }
            if (cmd.hasOption("l")) {
                logger.debug("LOAD-ALL");
                load = true;
            }
            if (cmd.hasOption("c")) {
                logger.debug("CATEGORY-ALL");
                category = true;
            }

            if ((fileName != null) && (load)) {
                logger.info("Starting Loading Process...");

                WikiUser user = new WikiUser("admin");
                parseXml("en", 1, user, "127.0.0.1", fileName);
            } else if ((concurrent) && (rebuild)) {
                logger.info("Starting Rebuilding Process...");

                WikiUser user = new WikiUser("admin");
                rebuildPages("en", 1, user, "127.0.0.1", partitions);
            } else if ((concurrent) && (category)) {
                logger.info("Starting Category Rebuilding Process...");

                WikiUser user = new WikiUser("admin");
                rebuildCategories("en", 1, user, "127.0.0.1", partitions);
            } else {
                printUsage(options);
                System.exit(1);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private static void parseXml(String virtualWikiName, int virtualWikiId, WikiUser user, String ip, String file) {

        SAXParserFactory spf = null;
        try {

            spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            InputSource is = new InputSource(new java.io.FileInputStream(file));

            JAMWikiLoadHandler loadHandler = new JAMWikiLoadHandler(virtualWikiName, user, ip);
            sp.parse(is, loadHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void rebuildPages(String virtualWikiName, int virtualWikiId, WikiUser user, String ip, List<String> partitions) {
        try {
            int size = partitions.size();

            BulkLoaderPoolExecutor blpe = new BulkLoaderPoolExecutor(size, size);

            int pCount = 0;
            int pSleepRampDown = 7500;

            for (String pName : partitions) {
                List<Integer> pTopicIds = initTopicIds(pName);

                JAMWikiRebuildHandler rebuildHandler = new JAMWikiRebuildHandler(virtualWikiName, virtualWikiId, user, ip, pTopicIds);
                rebuildHandler.setUpdateCleanContent(false);
                rebuildHandler.setUpdateSearchIndex(false);
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

    private static void rebuildCategories(String virtualWikiName, int virtualWikiId, WikiUser user, String ip, List<String> partitions) {
        try {
            int size = partitions.size();

            BulkLoaderPoolExecutor blpe = new BulkLoaderPoolExecutor(size, size);

            int pCount = 0;
            int pSleepRampDown = 7500;

            for (String pName : partitions) {
                List<Integer> pTopicIds = initTopicIds(pName);

                JAMWikiCategoryHandler rebuildHandler = new JAMWikiCategoryHandler(virtualWikiName, virtualWikiId, user, ip, pTopicIds);
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

    private static List<Integer> initTopicIds(String fileName) {

        List<Integer> topicIds = null;

        try {
            logger.info("LOADING-IDs ...");
            topicIds = new ArrayList<Integer>();

            FileInputStream fstream = new FileInputStream(fileName);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            //Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                topicIds.add(Integer.parseInt(strLine));
            }
            //Close the input stream
            in.close();
            logger.info("LOADED-IDs =>: " + topicIds.size());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return topicIds;
    }
}
