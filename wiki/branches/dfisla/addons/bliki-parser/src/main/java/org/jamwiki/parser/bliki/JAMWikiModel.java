package org.jamwiki.parser.bliki;

import info.bliki.htmlcleaner.ContentToken;
import info.bliki.htmlcleaner.TagNode;
import info.bliki.wiki.filter.TemplateParser;
import info.bliki.wiki.filter.WikipediaParser;
import info.bliki.wiki.model.AbstractWikiModel;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.ImageFormat;
import info.bliki.wiki.tags.WPATag;
import info.bliki.wiki.tags.util.TagStack;

import info.bliki.wiki.namespaces.INamespace;
import java.util.HashSet;

import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.jamwiki.DataAccessException;
import org.jamwiki.WikiBase;
import org.jamwiki.model.Topic;
import org.jamwiki.parser.ParserInput;
import org.jamwiki.parser.ParserOutput;
import org.jamwiki.utils.LinkUtil;
import org.jamwiki.utils.NamespaceHandler;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiLink;
import org.jamwiki.parser.jflex.MagicWordUtil;

/**
 * An IWikiModel model implementation for JAMWiki
 * 
 */
public class JAMWikiModel extends AbstractWikiModel {

    private static final Logger logger = Logger.getLogger(JAMWikiModel.class.getName());
    protected String fContextPath;
    protected ParserInput fParserInput;
    protected ParserOutput fParserOutput;
    protected Set<String> links = null;
    protected Set<String> templates = null;

    static {
        TagNode.addAllowedAttribute("style");
    }

    @Override
    public void setUp() {
        super.setUp();
        links = new HashSet<String>();
        templates = new HashSet<String>();
    }

    /**
     *
     * @param parserInput
     * @param document
     * @param contextPath
     */
    public JAMWikiModel(ParserInput parserInput, ParserOutput document, String contextPath) {
        super(Configuration.DEFAULT_CONFIGURATION);

        fParserInput = parserInput;
        fParserOutput = document;
        fContextPath = contextPath;
    }

    public void parseInternalImageLink(String imageNamespace, String name) {
        // see JAMHTMLConverter#imageNodeToText() for the real HTML conversion
        // routine!!!
        ImageFormat imageFormat = ImageFormat.getImageFormat(name, imageNamespace);

        int pxWidth = imageFormat.getWidth();
        String caption = imageFormat.getCaption();
        TagNode divTagNode = new TagNode("div");
        divTagNode.addAttribute("id", "image", false);
        // divTagNode.addAttribute("href", hrefImageLink, false);
        // divTagNode.addAttribute("src", srcImageLink, false);
        divTagNode.addObjectAttribute("wikiobject", imageFormat);
        if (pxWidth != -1) {
            divTagNode.addAttribute("style", "width:" + pxWidth + "px", false);
        }
        pushNode(divTagNode);

        if (caption != null && caption.length() > 0) {

            TagNode captionTagNode = new TagNode("div");
            String clazzValue = "caption";
            String type = imageFormat.getType();
            if (type != null) {
                clazzValue = type + clazzValue;
            }
            captionTagNode.addAttribute("class", clazzValue, false);

            TagStack localStack = WikipediaParser.parseRecursive(caption, this, true, true);
            captionTagNode.addChildren(localStack.getNodeList());
            String altAttribute = captionTagNode.getBodyString();
            imageFormat.setAlt(altAttribute);
            pushNode(captionTagNode);
            // WikipediaParser.parseRecursive(caption, this);
            popNode();
        }

        popNode(); // div

    }

    @Override
    public void appendInternalLink(String topic, String hashSection, String topicDescription, String cssClass, boolean parseRecursive) {
        try {
            WikiLink wikiLink;
            if (hashSection != null) {
                wikiLink = LinkUtil.parseWikiLink(topic + "#" + hashSection);
            } else {
                wikiLink = LinkUtil.parseWikiLink(topic);
            }
            String destination = wikiLink.getDestination();
            String virtualWiki = fParserInput.getVirtualWiki();
            String section = wikiLink.getSection();
            String query = wikiLink.getQuery();
            String href = LinkUtil.buildTopicUrlNoEdit(fContextPath, virtualWiki, destination, section, query);
            String style = "";
            if (StringUtils.isBlank(topic) && !StringUtils.isBlank(section)) {
                // do not check existence for section links
            } else {
                String articleName = topic.replace('_', ' ');
                if (!LinkUtil.isExistingArticle(virtualWiki, articleName)) {
                    style = "edit";
                    href = LinkUtil.buildEditLinkUrl(fContextPath, virtualWiki, topic, query, -1);
                }
            }
            WPATag aTagNode = new WPATag();
            aTagNode.addAttribute("href", href, true);
            aTagNode.addAttribute("class", style, true);
            aTagNode.addObjectAttribute("wikilink", topic);

            pushNode(aTagNode);
            if (parseRecursive) {
                WikipediaParser.parseRecursive(topicDescription.trim(), this, false, true);
            } else {
                aTagNode.addChild(new ContentToken(topicDescription));
            }
            popNode();
        } catch (DataAccessException e1) {
            logger.severe(e1.getMessage() + " TRACE: " + e1.getStackTrace());
            append(new ContentToken(topicDescription));
        }
    }

    @Override
    public void addCategory(String categoryName, String sortKey) {
        fParserOutput.addCategory(getCategoryNamespace() + NamespaceHandler.NAMESPACE_SEPARATOR + categoryName, sortKey);
    }

    @Override
    public void addLink(String topic) {

        if (!this.links.contains(topic)) {
            this.links.add(topic);
            fParserOutput.addLink(topic);
        }
    }

    @Override
    public void addTemplate(String template) {
        if (!this.templates.contains(template)) {
            this.templates.add(template);
            fParserOutput.addTemplate(template);
        }
    }

    public String getRawWikiContentNew(String namespace, String templateName, Map<String, String> templateParameters) {
        // String name = Encoder.encodeTitleUrl(articleName);
        String rv = null;
        if (isTemplateNamespace(namespace)) {
            /*
            String magicWord = templateName;
            String parameter = "";
            int index = magicWord.indexOf(':');
            if (index > 0) {
                parameter = magicWord.substring(index + 1).trim();
                magicWord = magicWord.substring(0, index);
            }
            if (MagicWord.isMagicWord(magicWord)) {
                MagicWordUtil mUtil = new MagicWordUtil();
                rv = MagicWord.processMagicWord(magicWord, parameter, this);
                logger.debug("RAW-WIKI-CONTENT MAGIC-WORD: " + magicWord + " MAGIC-RESULT: " + rv);
            }
            */
            try{
                if(MagicWordUtil.isMagicWord(templateName)){
                    rv = MagicWordUtil.processMagicWord(fParserInput, templateName);
                }
            }
            catch(Exception ex){
                logger.severe(ex.getMessage() + " TRACE: " + ex.getStackTrace());
            }
        }
        return rv;
    }

    @Override
    public String getRawWikiContent(String namespace, String topicName, Map<String, String> templateParameters) {
        logger.fine(String.format("RAW-WIKI-CONTENT NS: %s TOPIC-NAME: %s", namespace, topicName));

        if (isTemplateNamespace(namespace)) {
            logger.fine("RAW-WIKI-CONTENT IS TEMPLATE-NAME SPACE!");
        } else {
            logger.fine("RAW-WIKI-CONTENT IS NOT TEMPLATE-NAME SPACE!");
        }
        String result = getRawWikiContentNew(namespace, topicName, templateParameters);
        logger.fine("RAW-WIKI-CONTENT RESULT: " + result);
        if (result != null) {
            return result;
        }
        try {
            topicName = topicName.replaceAll("_", " ");
            Topic topic = WikiBase.getDataHandler().lookupTopic(fParserInput.getVirtualWiki(), namespace + ':' + topicName, false, null);
            if (topic == null) {
                return null;
            }
            return topic.getTopicContent();
        } catch (Exception ex) {
            logger.severe(ex.getMessage() + " TRACE: " + ex.getStackTrace());
        }
        return result;
    }

    @Override
    public void buildEditLinkUrl(int section) {
        if (fParserInput.getAllowSectionEdit()) {
            TagNode divTagNode = new TagNode("div");
            divTagNode.addAttribute("style", "font-size:90%;float:right;margin-left:5px;", false);
            divTagNode.addChild(new ContentToken("["));
            append(divTagNode);

            String url = "";
            try {
                // Use correct section number.
                // Bliki starts with offset 0 so it must be "section+1"
                url = LinkUtil.buildEditLinkUrl(fParserInput.getContext(), fParserInput.getVirtualWiki(), fParserInput.getTopicName(),
                        null, section + 1);
            } catch (Exception e) {
                logger.severe("Failure while building link for topic " + fParserInput.getVirtualWiki() + " / "
                        + fParserInput.getTopicName() + " MSG: " + e.getMessage());
            }
            TagNode aTagNode = new TagNode("a");
            aTagNode.addAttribute("href", url, false);
            aTagNode.addChild(new ContentToken(Utilities.formatMessage("common.sectionedit", fParserInput.getLocale())));
            divTagNode.addChild(aTagNode);
            divTagNode.addChild(new ContentToken("]"));
        }
    }

    @Override
    public boolean parseBBCodes() {
        return false;
    }

    @Override
    public boolean replaceColon() {
        return false;
    }

    @Override
    public String getCategoryNamespace() {
        return NamespaceHandler.NAMESPACE_CATEGORY;
    }

    @Override
    public String getImageNamespace() {
        return NamespaceHandler.NAMESPACE_IMAGE;
    }

    @Override
    public String getTemplateNamespace() {
        return NamespaceHandler.NAMESPACE_TEMPLATE;
    }

    public Set<String> getLinks() {
        if (fParserOutput != null) {
            return (Set<String>) fParserOutput.getLinks();
        } else {
            return null;
        }
    }

    public Map<String, String> getCategories() {
        if (fParserOutput != null) {
            return fParserOutput.getCategories();
        } else {
            return null;
        }
    }

    public Set<String> getTemplates() {
        if (fParserOutput != null) {
            return (Set<String>) fParserOutput.getTemplates();
        } else {
            return null;
        }
    }

    @Override
    public void appendInterWikiLink(String namespace, String title, String topicDescription) {
        String hrefLink = getInterwikiMap().get(namespace.toLowerCase());
        if (hrefLink != null) {
            WikiLink wikiLink = LinkUtil.parseWikiLink(namespace + NamespaceHandler.NAMESPACE_SEPARATOR + title + "|" + topicDescription);
            String destination = wikiLink.getDestination();
            destination = destination.substring(wikiLink.getNamespace().length() + NamespaceHandler.NAMESPACE_SEPARATOR.length());
            hrefLink = hrefLink.replace("${title}", Utilities.encodeAndEscapeTopicName(title));
            TagNode aTagNode = new TagNode("a");
            aTagNode.addAttribute("href", hrefLink, true);
            aTagNode.addAttribute("class", "interwiki", false);
            aTagNode.addAttribute("rel", "nofollow", false);

            pushNode(aTagNode);
            WikipediaParser.parseRecursive(topicDescription.trim(), this, false, true);
            popNode();

        } else {
            append(new ContentToken(topicDescription));
        }
    }

    @Override
    public boolean isTemplateTopic() {
        String topicName = fParserInput.getTopicName();
        int index = topicName.indexOf(':');
        if (index > 0) {
            String namespace = topicName.substring(0, index);
            if (isTemplateNamespace(namespace)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMathtranRenderer() {
        return true;
    }

    @Override
    public String parseTemplates(String rawWikiText, boolean parseOnlySignature) {
        if (rawWikiText == null) {
            return "";
        }
        if (!parseOnlySignature) {
            initialize();
        }
        StringBuilder buf = new StringBuilder(rawWikiText.length() + rawWikiText.length() / 10);
        try {
            TemplateParser.parse(rawWikiText, this, buf, parseOnlySignature, true);
        } catch (Exception ex) {
            logger.severe(ex.getMessage() + " TRACE: " + ex.getStackTrace());
            buf.append("<span class=\"error\">TemplateParser exception: " + ex.getClass().getSimpleName() + "</span>");
        }
        return buf.toString();
    }

    /**
     *
     * @return
     */
    public INamespace getNamespace() {
        return fNamespace;
    }
}
