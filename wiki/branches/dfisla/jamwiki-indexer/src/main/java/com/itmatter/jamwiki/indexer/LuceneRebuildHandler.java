/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itmatter.jamwiki.indexer;

import java.util.List;
import java.util.regex.Pattern;
import org.jamwiki.model.WikiUser;

import org.apache.log4j.Logger;

import org.jamwiki.DataAccessException;
import org.jamwiki.DataHandler;
import org.jamwiki.WikiBase;

import org.jamwiki.search.LuceneSearchEngine;

/**
 *
 * @author Daniel.Fisla
 */
public class LuceneRebuildHandler implements java.lang.Runnable {

    static Logger logger = Logger.getLogger(LuceneRebuildHandler.class.getName());
    private final WikiUser user;
    private final String authorIpAddress;
    private String virtualWiki = "en";
    private int virtualWikiId = 1;
    private boolean updateCleanContent = false;
    private boolean updateSearchIndex = false;
    private List<Integer> topicIds = null;
    private String customDataPath = null;
    private DataHandler dataHandler = null;

    /**
     *
     * @param virtualWiki
     * @param virtualWikiId
     * @param user
     * @param authorIpAddress
     * @param ids
     * @param dataPath
     * @throws DataAccessException
     */
    public LuceneRebuildHandler(String virtualWiki, int virtualWikiId, WikiUser user, String authorIpAddress, List<Integer> ids, String dataPath) throws DataAccessException {

        this.virtualWiki = virtualWiki;
        this.virtualWikiId = virtualWikiId;
        this.authorIpAddress = authorIpAddress;
        this.user = user;
        this.topicIds = ids;
        this.customDataPath = dataPath;

        dataHandler = WikiBase.getDataHandler();

        if (dataHandler == null) {
            throw new DataAccessException("Could not initialize DataHandler!");
        }
    }

    public void run() {

        try {
            logger.info("Retrieved Wiki Topic IDs =>: " + topicIds.size());
            LuceneSearchEngine se = null;
            se = new LuceneSearchEngine(customDataPath, topicIds);
            se.refreshIndex();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    /**
     *
     * @param text
     * @return
     */
    public String removeHtmlTags(String text) {
        String regex = "\\<.*?>";

        Pattern p = Pattern.compile(regex, Pattern.DOTALL);

        return p.matcher(text).replaceAll("");
    }

    /**
     *
     * @return
     */
    public boolean isUpdateCleanContent() {
        return updateCleanContent;
    }

    /**
     *
     * @param updateCleanContent
     */
    public void setUpdateCleanContent(boolean updateCleanContent) {
        this.updateCleanContent = updateCleanContent;
    }

    /**
     * 
     * @return
     */
    public boolean isUpdateSearchIndex() {
        return updateSearchIndex;
    }

    /**
     *
     * @param updateSearchIndex
     */
    public void setUpdateSearchIndex(boolean updateSearchIndex) {
        this.updateSearchIndex = updateSearchIndex;
    }

    /**
     *
     * @return
     */
    public String getVirtualWiki() {
        return virtualWiki;
    }

    /**
     *
     * @param virtualWiki
     */
    public void setVirtualWiki(String virtualWiki) {
        this.virtualWiki = virtualWiki;
    }

    /**
     *
     * @return
     */
    public List<Integer> getTopicIds() {
        return topicIds;
    }

    /**
     *
     * @param topicIds
     */
    public void setTopicIds(List<Integer> topicIds) {
        this.topicIds = topicIds;
    }
}
