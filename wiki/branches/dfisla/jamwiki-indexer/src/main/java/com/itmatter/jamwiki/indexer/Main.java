/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itmatter.jamwiki.indexer;

import java.util.List;
import org.jamwiki.search.LuceneSearchEngine;
import org.apache.log4j.Logger;
import org.jamwiki.WikiConfiguration;
import org.jamwiki.model.WikiConfigurationObject;
import org.apache.commons.cli.*;

/**
 *
 * @author Daniel.Fisla
 */
public class Main {

    static Logger log = Logger.getLogger(Main.class);
    private static final String USAGE = "[-h] [-d <path> ]";
    private static final String HEADER = "JAM Wiki Lucene Indexer - Rebuilds indexes for all virtual wikis.";
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
            String customDataPath = null;

            if (cmd.hasOption("h")) {
                printUsage(options);
                System.exit(0);
            }

            LuceneSearchEngine se = null;

            if (cmd.hasOption("d")) {
                customDataPath = cmd.getOptionValue("d");
                se = new LuceneSearchEngine(customDataPath);
            }else{
                se = new LuceneSearchEngine();
            }

            if (pageLimit > 0) {
                se.refreshIndex(pageLimit);
            } else {
                se.refreshIndex();
            }

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
