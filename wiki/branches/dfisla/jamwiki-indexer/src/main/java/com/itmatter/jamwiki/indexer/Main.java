/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itmatter.jamwiki.indexer;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jamwiki.search.LuceneSearchEngine;
import org.apache.log4j.Logger;
import org.apache.commons.cli.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jamwiki.model.WikiUser;

/**
 *
 * @author Daniel.Fisla
 */
public class Main {

    static Logger logger = Logger.getLogger(Main.class);
    private static final String USAGE = "[-h]\n[-d <path> ] [-p <file1,file2,...> -i ]\n[-s <path> -t <path> -m]";
    private static final String HEADER = "JAM Wiki Lucene Indexer - Rebuilds indexes for all virtual wikis. Merges multiple indexes to one.";
    private static final String FOOTER = "";

    private static void printUsage(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(USAGE, HEADER, options, FOOTER);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Options options = new Options();
        options.addOption("h", "help", false, "Print this usage information");
        options.addOption("d", "dir", true, "Location of the root data path - i.e. D:/jamwiki/tomcat-6.0.18/webapps/jamwiki-war/data");
        options.addOption("s", "sdir", true, "Location of the root data path - i.e. ./data/ with index[1-N] subfolders");
        options.addOption("t", "tdir", true, "Location of the root data path - i.e. ./data/merged-index");
        options.addOption("p", "partitions", true, "Partition 1,2,3,4 file name and path");
        options.addOption("i", "index", false, "Build indexes");
        options.addOption("m", "merge", false, "Merge indexes");

        CommandLineParser parser = null;
        CommandLine cmd = null;

        List<String> partitions = null;
        String sourceDir = null;
        String mergeDir = null;
        boolean concurrent = false;
        boolean index = false;
        boolean merge = false;

        try {
            if (args.length == 0) {
                printUsage(options);
                System.exit(0);
            }

            parser = new PosixParser();
            cmd = parser.parse(options, args);

            int pageLimit = Integer.MAX_VALUE;
            String customDataPath = null;

            if (cmd.hasOption("h")) {
                printUsage(options);
                System.exit(0);
            }
            if (cmd.hasOption("i")) {
                logger.debug("INDEX");
                index = true;
            }
            if (cmd.hasOption("m")) {
                logger.debug("MERGE");
                merge = true;
            }

            if (cmd.hasOption("s")) {
                sourceDir = cmd.getOptionValue("s");
                logger.debug("SOURCE-DIR =>: " + sourceDir);
            }
            if (cmd.hasOption("t")) {
                mergeDir = cmd.getOptionValue("t");
                logger.debug("MERGE-DIR =>: " + mergeDir);
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

            LuceneSearchEngine se = null;

            if (cmd.hasOption("d") && (!concurrent) && index) {
                customDataPath = cmd.getOptionValue("d");
                se = new LuceneSearchEngine(customDataPath);
            } else if (cmd.hasOption("d") && (concurrent) && index) {
                customDataPath = cmd.getOptionValue("d");
                WikiUser user = new WikiUser("admin");
                reindexPages("en", 1, user, "127.0.0.1", partitions, customDataPath);
            } else if (cmd.hasOption("s") && cmd.hasOption("t") && merge) {
                se = new LuceneSearchEngine();
                se.merge(sourceDir, mergeDir, 1000, 500);
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private static void reindexPages(String virtualWikiName, int virtualWikiId, WikiUser user, String ip, List<String> partitions, String customDataPath) {
        try {
            int size = partitions.size();

            IndexerPoolExecutor blpe = new IndexerPoolExecutor(size, size);

            int pCount = 0;
            int pSleepRampDown = 7500;

            for (String pName : partitions) {
                List<Integer> pTopicIds = initTopicIds(pName);

                LuceneRebuildHandler rebuildHandler = new LuceneRebuildHandler(virtualWikiName, virtualWikiId, user, ip, pTopicIds, customDataPath);
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
