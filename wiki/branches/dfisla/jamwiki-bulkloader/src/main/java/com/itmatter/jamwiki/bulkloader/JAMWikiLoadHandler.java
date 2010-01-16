/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itmatter.jamwiki.bulkloader;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Hashtable;

import org.apache.commons.lang.StringUtils;

import org.jamwiki.utils.NamespaceHandler;

import org.jamwiki.model.Topic;
import org.jamwiki.model.TopicVersion;
import org.jamwiki.model.WikiUser;


import org.apache.log4j.Logger;
import org.xml.sax.SAXParseException;

import java.util.Stack;
import org.jamwiki.DataHandler;
import org.jamwiki.WikiBase;

/**
 *
 * @author Daniel.Fisla
 */
public class JAMWikiLoadHandler extends org.xml.sax.helpers.DefaultHandler {

    static Logger logger = Logger.getLogger(JAMWikiLoadHandler.class.getName());
    private StringBuffer currentElementBuffer = null;
    private final WikiUser user;
    private final String authorIpAddress;
    private String virtualWiki = "en";
    private Integer nsKey = null;
    private String nsVal = null;
    private String pageName = null;
    private String pageText = null;
    private String pageComment = null;
    // namespace related
    private String ns14 = "Category";
    private String ns6 = "Image";
    private Hashtable<String, Object> namespaces = new Hashtable<String, Object>();
    //private Page currentPage = null;
    private Stack<String> STACK = new Stack<String>();
    private Date startDate = new Date();
    private Date endDate = new Date();
    private DataHandler dataHandler = null;

    /**
     *
     * @param virtualWiki
     * @param user
     * @param authorIpAddress
     */
    public JAMWikiLoadHandler(String virtualWiki, WikiUser user, String authorIpAddress) {
        this.currentElementBuffer = new StringBuffer();

        this.virtualWiki = virtualWiki;
        this.authorIpAddress = authorIpAddress;
        this.user = user;

        this.dataHandler = WikiBase.getDataHandler();
    }

    public void startDocument() throws org.xml.sax.SAXException {
        this.startDate = new Date(System.currentTimeMillis());
    }

    public void endDocument() throws org.xml.sax.SAXException {
        this.endDate = new Date(System.currentTimeMillis());

        logger.debug("XML-PARSE_TIME =>: (ms)" + (endDate.getTime() - startDate.getTime()));
    }

    public void startElement(String namespaceURI, String localName, String qName, org.xml.sax.Attributes attributes) throws org.xml.sax.SAXException {

        String eName = localName;
        if ("".equals(eName)) {
            eName = qName;
        }
        this.currentElementBuffer = new StringBuffer();

        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                String aName = attributes.getLocalName(i); // Attr name
                if ("".equals(aName)) {
                    aName = attributes.getQName(i);
                }
            }
        }

        STACK.push(qName);
        //System.out.println("DEPTH: " + STACK.size() + " START-ELEMENT: " + STACK.peek());

//" Attribute found for element :: '" + qName + "'" +
//                    " att qname :: " + attributes.getQName(i) +
//                    " att type :: " + attributes.getType(i) +
//                    " att value :: " + attributes.getValue(i));

        try {
            if ("namespace".equals(eName)) { // mapping of namespaces from imported file
                nsKey = Integer.valueOf(attributes.getValue("key"));
            } else if ("page".equals(eName)) {
                pageName = "";
                pageText = "";
                pageComment = "";
            }
        } catch (Exception ex) {
            logger.error("Character parse Error", ex);
        }
    }

    public void characters(char[] ch, int start, int length) throws org.xml.sax.SAXException {

        //System.out.println("DEPTH: " + STACK.size() + " LENGTH: " + length + " ELEMENT: " + STACK.peek());

        if ((ch != null) && (length > 0)) {
            this.currentElementBuffer.append(ch, start, length);
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws org.xml.sax.SAXException {

        /*
         * We do not reset the current element here as the startElement
         * method should do that at the start of an element
         */

        if (!STACK.peek().equals(qName)) {
            logger.error("STACK-PARSE-ERROR =>: " + STACK.peek() + " NOT-EQUAL " + qName);
            System.exit(1);
        }

        String lastStr = null;
        byte[] contentBytes = null;

        try {
            contentBytes = currentElementBuffer.toString().getBytes("UTF-8");
            lastStr = new String(contentBytes, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            logger.error(uee.getMessage(), uee);
        }

        if ("namespace".equals(qName)) { // mapping of namespaces from imported file
            namespaces.put(lastStr, nsKey);
            //Prepare locale namespaces
            //WikiArticle.addNamespace(nsKey, lastStr.trim());
            if (nsKey.intValue() == 14) {
                ns14 = lastStr;
            }
            if (nsKey.intValue() == 6) {
                ns6 = lastStr;
            }
        } else if ((STACK.size() == 3) && STACK.peek().equals("id")) { // page:id
            int id = Integer.parseInt(lastStr);

        } else if ((STACK.size() == 3) && STACK.peek().equals("title")) { // page:title
            pageName = lastStr;
        } else if ((STACK.size() == 4) && STACK.peek().equals("id")) { // revision:id
            pageComment = "Wikipedia Revision: " + lastStr;
        } else if ((STACK.size() == 4) && STACK.peek().equals("timestamp")) { // revision:timestamp
        } else if ((STACK.size() == 5) && STACK.peek().equals("id")) { // contributor:id
        } else if ((STACK.size() == 5) && STACK.peek().equals("username")) { // revision:username
        } else if ((STACK.size() == 4) && STACK.peek().equals("minor")) { // revision:minor
        } else if ((STACK.size() == 4) && STACK.peek().equals("comment")) { // revision:comment
            //pageComment = lastStr;
        } else if ((STACK.size() == 4) && STACK.peek().equals("text")) { // revision:text
            pageText = lastStr;
        } else if ((STACK.size() == 2) && STACK.peek().equals("page") && ("page".equals(qName))) { // page

            //Create Topic
            String sNamespace = "";
            int namespace = 0;
            // get wiki namespace
            int pos = pageName.indexOf(':');
            if (pos > -1) {
                sNamespace = pageName.substring(0, pos);
                if (namespaces.containsKey(sNamespace)) {
                    namespace = ((Integer) namespaces.get(sNamespace));
                } else { // unknown namespace
                    namespace = -1;
                }
            } else { // main namespace
                namespace = 0;
            }

            try {

                pageText = preprocessText(pageText);

                Topic topic = new Topic();
                topic.setName(convertArticleNameFromWikipediaToJAMWiki(pageName));
                topic.setVirtualWiki(virtualWiki);
                topic.setTopicContent(pageText);
                int charactersChanged = StringUtils.length(pageText);

                TopicVersion topicVersion = new TopicVersion(user, authorIpAddress, pageComment, pageText, charactersChanged);
                // manage mapping between MediaWiki and JAMWiki namespaces
                topic.setTopicType(convertNamespaceFromMediaWikiToJAMWiki(namespace));

                dataHandler.writeTopic(topic, topicVersion, null, null, true, false);

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                //throw new SAXException(e);
            }

        }

        STACK.pop();
    }

    /**
     *
     * @param text
     * @return
     */
    public String preprocessText(String text) {
        String ret = text;
        // convert all namespaces names from MediaWiki to JAMWiki local representation
        ret = StringUtils.replace(ret, "[[category:", "[[" + NamespaceHandler.NAMESPACE_CATEGORY + ":");

        if (!"Category".equals(NamespaceHandler.NAMESPACE_CATEGORY)) {
            ret = StringUtils.replace(ret, "[[Category:", "[[" + NamespaceHandler.NAMESPACE_CATEGORY + ":");
        }

        ret = StringUtils.replace(ret, "[[" + ns14 + ":", "[[" + NamespaceHandler.NAMESPACE_CATEGORY + ":");
        ret = StringUtils.replace(ret, "[[image:", "[[" + NamespaceHandler.NAMESPACE_IMAGE + ":");

        if (!"Image".equals(NamespaceHandler.NAMESPACE_CATEGORY)) {
            ret = StringUtils.replace(ret, "[[Image:", "[[" + NamespaceHandler.NAMESPACE_IMAGE + ":");
        }
        ret = StringUtils.replace(ret, "[[" + ns6 + ":", "[[" + NamespaceHandler.NAMESPACE_IMAGE + ":");

        return ret;
    }

    /**
     *
     */
    private String convertArticleNameFromWikipediaToJAMWiki(String fullName) {
        String ret = fullName;
        String sNamespace = "";
        String sJAMNamespace = "";
        String sTitle = pageName;
        int pos = pageName.indexOf(':');
        if (pos > -1) {
            sNamespace = pageName.substring(0, pos);
            if (namespaces.containsKey(sNamespace)) {
                int namespace = ((Integer) namespaces.get(sNamespace));
                sTitle = pageName.substring(pos + 1);
                sJAMNamespace = getJAMWikiNamespaceById(convertNamespaceFromMediaWikiToJAMWiki(namespace));
                if (sJAMNamespace.length() > 0) {
                    ret = sJAMNamespace + ":" + sTitle;
                } else {//equivalent namespace in JAMWiki not found. Use original name
                    ret = sNamespace + ":" + sTitle;
                }
            } else { //namespace not found
                ret = pageName;
            }
        } else { //main namespace
            ret = pageName;
        }
        return ret;
    }

    /**
     * convert MediaWiki namespace-id to JAMWiki namespace-id
     * @param mediaWikiNamespaceId
     * @return
     */
    private int convertNamespaceFromMediaWikiToJAMWiki(int mediaWikiNamespaceId) {
        int ret = -1;
        switch (mediaWikiNamespaceId) {
            case 0:
                ret = Topic.TYPE_ARTICLE;
                break;
            //case 0: ret = Topic.TYPE_REDIRECT; break; //special hendling for redirects
            case 6:
                ret = Topic.TYPE_IMAGE;
                break;
            case 14:
                ret = Topic.TYPE_CATEGORY;
                break;
            //case 0: ret = Topic.TYPE_FILE; break;
            //case 0: ret = Topic.TYPE_SYSTEM_FILE; break;
            case 10:
                ret = Topic.TYPE_TEMPLATE;
                break;
        }
        return ret;
    }

    /**
     *
     */
    private String getJAMWikiNamespaceById(int jamWikiNamespaceId) {
        String ret = "";
        switch (jamWikiNamespaceId) {
            case Topic.TYPE_IMAGE:
                ret = NamespaceHandler.NAMESPACE_IMAGE;
                break;
            case Topic.TYPE_CATEGORY:
                ret = NamespaceHandler.NAMESPACE_CATEGORY;
                break;
            case Topic.TYPE_TEMPLATE:
                ret = NamespaceHandler.NAMESPACE_TEMPLATE;
                break;
        }
        return ret;
    }

    public void warning(SAXParseException x) {
        logger.warn("Wiki Parser Warning =>: ", x);
    }

    public void error(SAXParseException x) {
        logger.error("Wiki Parser Error =>: ", x);
    }

    public void fatalError(SAXParseException x) {
        logger.error("FATAL ERROR!", x);
    }

}
