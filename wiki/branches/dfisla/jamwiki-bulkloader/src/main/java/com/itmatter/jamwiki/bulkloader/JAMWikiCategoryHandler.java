/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itmatter.jamwiki.bulkloader;

import java.util.List;
import org.jamwiki.model.WikiUser;

import org.apache.log4j.Logger;

import org.jamwiki.DataAccessException;
import org.jamwiki.DataHandler;
import org.jamwiki.WikiBase;

import org.jamwiki.model.Topic;
import org.jamwiki.model.TopicVersion;
import org.jamwiki.parser.ParserOutput;
import org.jamwiki.parser.ParserUtil;

/**
 *
 * @author Daniel.Fisla
 */
public class JAMWikiCategoryHandler implements java.lang.Runnable {

    static Logger logger = Logger.getLogger(JAMWikiLoadHandler.class.getName());
    private final WikiUser user;
    private final String authorIpAddress;
    private String virtualWiki = "en";
    private int virtualWikiId = 1;

    private List<Integer> topicIds = null;
    private String fileName = null;
    private DataHandler dataHandler = null;

    public JAMWikiCategoryHandler(String virtualWiki, int virtualWikiId, WikiUser user, String authorIpAddress, List<Integer> ids) throws DataAccessException {

        this.virtualWiki = virtualWiki;
        this.virtualWikiId = virtualWikiId;
        this.authorIpAddress = authorIpAddress;
        this.user = user;
        this.topicIds = ids;

        dataHandler = WikiBase.getDataHandler();

        if (dataHandler == null) {
            throw new DataAccessException("Could not initialize DataHandler!");
        }
    }

    public void run() {

        Topic topic = null;
        TopicVersion topicVersion = null;
        ParserOutput parserOutput = null;

        try {
            logger.debug("Retrieved Wiki Topic IDs =>: " + topicIds.size());

            for (Integer topicId : topicIds) {
                logger.debug("TOPIC-LOOKUP-BY-ID =>: " + topicId);

                topic = dataHandler.lookupTopicMetaDataById(virtualWiki, topicId.intValue());

                if (topic != null) {
                    logger.debug("TOPICVERSION-LOOKUP-BY-ID =>: " + topic.getCurrentVersionId());
                    topicVersion = dataHandler.lookupTopicVersion(topic.getCurrentVersionId());

                    if (topicVersion != null) {
                        String pageText = topicVersion.getVersionContent();
                        String pageName = topic.getName();

                        logger.debug("TOPIC-NAME =>: " + pageName);

                        parserOutput = ParserUtil.parserOutput(pageText, virtualWiki, pageName);

                        dataHandler.buildTopicCategories(topicVersion, parserOutput.getCategories(), pageName, virtualWiki);
                    } else {
                        logger.error("LOOKUP-FAIL-TOPICVERSION-ID =>: " + topic.getCurrentVersionId());
                    }
                } else {
                    logger.error("LOOKUP-FAIL-TOPIC-ID =>: " + topicId);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    public String getVirtualWiki() {
        return virtualWiki;
    }

    public void setVirtualWiki(String virtualWiki) {
        this.virtualWiki = virtualWiki;
    }

    public List<Integer> getTopicIds() {
        return topicIds;
    }

    public void setTopicIds(List<Integer> topicIds) {
        this.topicIds = topicIds;
    }
}
