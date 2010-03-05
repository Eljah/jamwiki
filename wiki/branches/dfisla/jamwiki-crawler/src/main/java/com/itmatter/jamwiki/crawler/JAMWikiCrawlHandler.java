/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itmatter.jamwiki.crawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;
import org.jamwiki.model.WikiUser;

import org.apache.log4j.Logger;

import org.jamwiki.DataAccessException;
import org.jamwiki.DataHandler;
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.WikiException;
import org.jamwiki.WikiMessage;
import org.jamwiki.model.ParsedTopic;
import org.jamwiki.model.Topic;
import org.jamwiki.model.WikiFileVersion;
import org.jamwiki.parser.ParserInput;
import org.jamwiki.parser.ParserOutput;
import org.jamwiki.parser.bliki.BlikiProxyParser;
import org.jamwiki.utils.NamespaceHandler;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiUtil;

/**
 *
 * @author Daniel.Fisla
 */
public class JAMWikiCrawlHandler implements java.lang.Runnable {

    static Logger logger = Logger.getLogger(JAMWikiCrawlHandler.class.getName());
    private final WikiUser user;
    private final String authorIpAddress;
    private String virtualWiki = "en";
    private int virtualWikiId = 1;
    private List<String> topicNames = null;
    private DataHandler dataHandler = null;
    private String appUrl = null;
    private String fetchMode = "web";
    private String parserLogger = null;
    private BufferedWriter pLogger = null;
    private long threadId = -1;

    /**
     *
     * @param virtualWiki
     * @param virtualWikiId
     * @param user
     * @param authorIpAddress
     * @param ids
     * @throws DataAccessException
     */
    public JAMWikiCrawlHandler(String mode, String logFile, String virtualWiki, int virtualWikiId, WikiUser user, String authorIpAddress, List<String> topics) throws DataAccessException {

        this.virtualWiki = virtualWiki;
        this.virtualWikiId = virtualWikiId;
        this.authorIpAddress = authorIpAddress;
        this.user = user;
        this.topicNames = topics;
        this.fetchMode = mode;

        dataHandler = WikiBase.getDataHandler();

        if (dataHandler == null) {
            throw new DataAccessException("Could not initialize DataHandler!");
        }

        this.threadId = Thread.currentThread().getId();
        this.parserLogger = this.threadId + "-" + logFile;
    }

    /*
     *
     */
    public boolean fetchFromDb(String virtualWiki, String topicName) {

        ParsedTopic parsedTopic = null;
        ParserInput parserInput = null;
        ParserOutput parserOutput = null;
        BlikiProxyParser wikiParser = null;
        Topic topic = null;

        boolean hasErrors = false;

        try {

            WikiUtil.validateTopicName(topicName);

            topic = dataHandler.lookupTopic(virtualWiki, topicName, true, null);
            Locale locale = new Locale("en", "US");

            //String cacheKey = WikiCache.key(virtualWiki, topicName);
            //Element cacheElement = WikiCache.retrieveFromCache(WikiBase.CACHE_PARSED_TOPIC_DATA, cacheKey);

            parsedTopic = dataHandler.lookupParsedTopic(virtualWiki, topicName, null);

            //if (cacheElement == null) {
            if (parsedTopic == null) {
                parsedTopic = new ParsedTopic();

                parserInput = new ParserInput();
                parserInput.setContext("");
                parserInput.setLocale(locale);
                //parserInput.setWikiUser(user);
                parserInput.setTopicName(topicName);
                //parserInput.setUserIpAddress(ServletUtil.getIpAddress(request));
                parserInput.setVirtualWiki(virtualWiki);
                parserInput.setAllowSectionEdit(false);
                wikiParser = new BlikiProxyParser(parserInput);

                parserOutput = new ParserOutput();

                boolean timeLimited = false;
                String content = null;

                //content = ParserUtil.parse(parserInput, parserOutput, topic.getTopicContent());
                String wikiContent = topic.getTopicContent();
                content = wikiParser.parseHTML(parserOutput, wikiContent);

                if ((content != null) && (content.startsWith("<span class=\"error\">"))) {
                    hasErrors = true;
                }

                if ((content != null) && (content.startsWith("<span class=\"error\">TIME"))) {
                    timeLimited = true;

                    String reloadLinkHtml = String.format("<a href=\"/%s/%s\" class=\"%s\">%s</a>", virtualWiki, topicName, "edit", "Click here to try again.");

                    StringBuffer html = new StringBuffer();
                    html.append(content);
                    html.append("<br/><br/>");
                    html.append(reloadLinkHtml);
                    html.append("<br/>");

                    content = html.toString();
                }

                // ADD-CATEGORIES
                if (parserOutput.getCategories().size() > 0) {
                    LinkedHashMap<String, String> categories = new LinkedHashMap<String, String>();
                    for (String key : parserOutput.getCategories().keySet()) {
                        String value = key.substring(NamespaceHandler.NAMESPACE_CATEGORY.length() + NamespaceHandler.NAMESPACE_SEPARATOR.length());
                        categories.put(key, value);
                    }
                    parsedTopic.categories = categories;
                }

                topic.setTopicContent(content);

                // ADD IMAGE/FILE TOPIC DATA
                if (topic.getTopicType() == Topic.TYPE_IMAGE || topic.getTopicType() == Topic.TYPE_FILE) {
                    List<WikiFileVersion> fileVersions = null;
                    try {
                        fileVersions = WikiBase.getDataHandler().getAllWikiFileVersions(virtualWiki, topicName, true);
                    } catch (DataAccessException e) {
                        throw new WikiException(new WikiMessage("error.unknown", e.getMessage()), e);
                    }
                    for (WikiFileVersion fileVersion : fileVersions) {
                        // update version urls to include web root path
                        String url = FilenameUtils.normalize(Environment.getValue(Environment.PROP_FILE_DIR_RELATIVE_PATH) + "/" + fileVersion.getUrl());
                        url = FilenameUtils.separatorsToUnix(url);
                        fileVersion.setUrl(url);
                    }
                    // Topic ADD FILE-VERSIONS & FILE/IMAGE
                    if (topic.getTopicType() == Topic.TYPE_IMAGE) {
                        parsedTopic.topicImage = true;
                    } else {
                        parsedTopic.topicFile = true;
                    }
                }
                parsedTopic.initialize(topic);

                // Let's make it safe!
                if ((topicName != null) && (content != null)
                        && (!topicName.equals(WikiBase.SPECIAL_PAGE_STYLESHEET))
                        && (!topicName.equals(WikiBase.SPECIAL_PAGE_LEFT_MENU))
                        && (!topicName.equals(WikiBase.SPECIAL_PAGE_BOTTOM_AREA))
                        && (!timeLimited)
                        && (!content.startsWith("<span class=\"error\">ERROR"))) {
                    dataHandler.writeParsedTopic(parsedTopic);
                    //WikiCache.addToCache(WikiBase.CACHE_PARSED_TOPIC_DATA, cacheKey, parsedTopic.toString());
                    logger.debug("CACHE_PARSED_TOPIC_CONTENT WRITE =>: TOPIC-NAME: " + parsedTopic.getName() + " CONTENT: " + parsedTopic.getTopicContent());
                }

            } else {

                //logger.debug("PARSEDTOPIC-CONTENT: " + parsedTopic.getTopicContent());
                //parsedTopic = new ParsedTopic((String) cacheElement.getObjectValue());
                topic = (Topic) parsedTopic;
                logger.debug("WikiBase.CACHE_PARSED_TOPIC_CONTENT GET <=: TOPIC-NAME: " + topic.getName() + " CONTENT: " + topic.getTopicContent());
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            parsedTopic = null;
            parserInput = null;
            parserOutput = null;
            wikiParser = null;
            topic = null;

        }

        return hasErrors;
    }

    public void fetchFromWeb(String path) {

        URL u;
        InputStream is = null;
        BufferedReader br = null;
        String s;

        try {

            u = new URL(path);
            is = u.openStream();
            br = new BufferedReader(new InputStreamReader(is));

            while ((s = br.readLine()) != null) {
                s = null;
            }

        } catch (MalformedURLException mue) {
            logger.error("Ouch - a MalformedURLException happened.", mue);
        } catch (IOException ioe) {
            logger.error("Oops- an IOException happened.", ioe);
        } finally {
            //System.out.println(s)
            try {
                br.close();
                is.close();
            } catch (IOException ioe) {
                // just going to ignore this one
            }
        }

    }

    public void run() {

        try {
            logger.info("Retrieved Wiki Names =>: " + topicNames.size());

            
            pLogger = new BufferedWriter(new FileWriter(this.parserLogger));

            for (String topicName : topicNames) {

                //WebClient webClient = new WebClient(BrowserVersion.FIREFOX_3);
                long startTime = System.currentTimeMillis();
                //HtmlPage page = webClient.getPage(requestUrl);
                if (this.fetchMode.equalsIgnoreCase("web")) {
                    String requestUrl = String.format("%s/%s/%s", appUrl, virtualWiki, Utilities.encodeAndEscapeTopicName(topicName));
                    logger.info(String.format("Thread[%d] Requesting Web Page: %s", threadId, requestUrl));
                    fetchFromWeb(requestUrl);
                    long endTime = System.currentTimeMillis();
                    //webClient.closeAllWindows();

                    long fetchTime = endTime - startTime;
                    logger.info(String.format("Thread[%d] Retrieved Web Page: %s in: %s ms", threadId, requestUrl, fetchTime));
                    System.out.println(String.format("Thread[%d] Retrieved Web Page: %s in: %s ms", threadId, requestUrl, fetchTime));
                } else if (this.fetchMode.equalsIgnoreCase("db")) {
                    logger.info("Thread[%d] Requesting Topic: " + topicName);
                    boolean rv = fetchFromDb(virtualWiki, topicName);
                    long endTime = System.currentTimeMillis();

                    long fetchTime = endTime - startTime;

                    if (!rv) {
                        pLogger.write("OK|" + threadId + "|" + topicName + "|" + fetchTime + "\n");
                    } else {
                        pLogger.write("ERROR|" + threadId + "|" + topicName + "|" + fetchTime + "\n");
                    }

                    pLogger.flush();
                    logger.info(String.format("Thread[%d] Retrieved Topic: %s in: %s ms", threadId, topicName, fetchTime));
                    System.out.println(String.format("Thread[%d] Retrieved Topic: %s in: %s ms", threadId, topicName, fetchTime));
                } else {
                    logger.error("Unknow fetch mode!");
                    return;
                }


            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                pLogger.flush();
                pLogger.close();
            } catch (Exception ex) {
            }
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

    public String getFetchMode() {
        return fetchMode;
    }

    public void setFetchMode(String fetchMode) {
        this.fetchMode = fetchMode;
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
    public List<String> getTopicNames() {
        return topicNames;
    }

    /**
     *
     * @param topicIds
     */
    public void setTopicNames(List<String> topics) {
        this.topicNames = topics;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }
}
