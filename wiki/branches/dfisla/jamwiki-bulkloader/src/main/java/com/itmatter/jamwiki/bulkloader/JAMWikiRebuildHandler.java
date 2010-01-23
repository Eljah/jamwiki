/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itmatter.jamwiki.bulkloader;

import java.util.List;
import java.util.regex.Pattern;
import org.jamwiki.model.WikiUser;

import org.apache.log4j.Logger;

import org.jamwiki.DataAccessException;
import org.jamwiki.DataHandler;
import org.jamwiki.WikiBase;

import org.jamwiki.model.Topic;
import org.jamwiki.model.TopicVersion;
import org.jamwiki.parser.ParserException;
import org.jamwiki.parser.ParserInput;
import org.jamwiki.parser.ParserOutput;
import org.jamwiki.parser.ParserUtil;

/**
 *
 * @author Daniel.Fisla
 */
public class JAMWikiRebuildHandler implements java.lang.Runnable {

    static Logger logger = Logger.getLogger(JAMWikiLoadHandler.class.getName());
    private final WikiUser user;
    private final String authorIpAddress;
    private String virtualWiki = "en";
    private int virtualWikiId = 1;
    private boolean updateCleanContent = false;
    private boolean updateSearchIndex = false;
    private List<Integer> topicIds = null;
    private String fileName = null;
    private DataHandler dataHandler = null;

    /**
     *
     * @param virtualWiki
     * @param virtualWikiId
     * @param user
     * @param authorIpAddress
     * @param ids
     * @throws DataAccessException
     */
    public JAMWikiRebuildHandler(String virtualWiki, int virtualWikiId, WikiUser user, String authorIpAddress, List<Integer> ids) throws DataAccessException {

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

        try {
            logger.info("Retrieved Wiki Topic IDs =>: " + topicIds.size());

            for (Integer topicId : topicIds) {
                logger.debug("TOPIC-LOOKUP-BY-ID =>: " + topicId);

                topic = dataHandler.lookupTopicById(virtualWiki, topicId.intValue());

                if (topic != null) {
                    logger.debug("TOPICVERSION-LOOKUP-BY-ID =>: " + topic.getCurrentVersionId());
                    topicVersion = dataHandler.lookupTopicVersion(topic.getCurrentVersionId());

                    if (topicVersion != null) {
                        topicVersion.setEditComment("Rebuild Comment.");
                        String pageText = topicVersion.getVersionContent();
                        String pageName = topic.getName();

                        logger.debug("TOPIC-NAME =>: " + pageName);

                        ParserInput parserInput = new ParserInput();
                        // FIXME bogus context to avoid errors
                        parserInput.setContext("/wiki");
                        //parserInput.setLocale(request.getLocale());
                        parserInput.setWikiUser(user);
                        parserInput.setTopicName(pageName);
                        //parserInput.setUserIpAddress(ServletUtil.getIpAddress(request));
                        parserInput.setVirtualWiki(virtualWiki);
                        //parserInput.setAllowSectionEdit(sectionEdit);

                        ParserOutput parserOutput = new ParserOutput();

                        String content = null;

                        try {
                            content = ParserUtil.parse(parserInput, parserOutput, pageText);

                            if((content == null) || (content.length() < 2)){
                                logger.error("CONTENT-CLEAN-FAIL =>: " + pageName + " TopicVersion: " + topicVersion.getTopicVersionId());
                            }

                            topicVersion.setVersionContentClean(this.removeHtmlTags(content));
                        } catch (ParserException e) {
                            logger.error(e.getMessage(), e);
                        }

                        // ParserOutput parserOutput = ParserUtil.parserOutput(pageText, virtualWiki, pageName);
                        if ((pageText != null) && (this.updateCleanContent)) {
                            //topicVersion.setVersionContentClean(this.parseCleanArticleContent(pageText, pageName, virtualWiki, user));
                            throw new Exception("Not Implemented!");
                        }

                        if (this.updateSearchIndex) {
                            //WikiBase.getDataHandler().writeTopic(topic, topicVersion, parserOutput.getCategories(), parserOutput.getLinks(), true);
                            dataHandler.updateTopic(topic, topicVersion, parserOutput.getCategories(), parserOutput.getLinks(), true);
                        } else {
                            //dataHandler.updateTopicVersion(topicVersion, parserOutput.getCategories(), pageName, virtualWiki);
                            dataHandler.importTopicVersion(topicVersion, parserOutput.getCategories(), pageName, virtualWiki, 1);
                        }
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

    /**
     *
     * @param text
     * @return
     */
    public String removeHtmlTags(String text){
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
