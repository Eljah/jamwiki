/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itmatter.jamwiki.loader;

import java.io.File;
import org.apache.commons.cli.*;
import org.jamwiki.model.WikiUser;
import org.jamwiki.servlets.XMLTopicFactory;
//import org.jamwiki.utils.WikiLogger;
import org.apache.log4j.*;

/**
 *
 * @author Daniel.Fisla
 */
public class Main {

    static Logger logger = Logger.getLogger(Main.class.getName());
    private static final String USAGE = "[-h] [-f <file> | -n <number>]";
    private static final String HEADER = "JAM Wiki Loader - Imports MediaWiki wikipedia XML exports to JAMWiki.";
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
        options.addOption("n", "number", true, "Number of pages to import");
        options.addOption("f", "file", true, "File name and path");

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
            }else{
                printUsage(options);
                System.exit(1);
            }

            if (fileName != null) {
                WikiUser user = new WikiUser("admin");

                XMLTopicFactory importFactory = new XMLTopicFactory("en", user, "127.0.0.1");

                importFactory.setPageLimit(pageLimit);
                importFactory.setUpdateSearchIndex(false);
                importFactory.setUpdateCleanContent(false);
                importFactory.importWikiXml(new File(fileName));
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
