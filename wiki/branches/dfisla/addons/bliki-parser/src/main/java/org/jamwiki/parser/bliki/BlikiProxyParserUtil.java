/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jamwiki.parser.bliki;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jamwiki.Environment;
import org.jamwiki.parser.ParserInput;
import org.jamwiki.utils.LinkUtil;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiLink;

/**
 *
 * @author dfisla
 */
public class BlikiProxyParserUtil {

    private static final Logger logger = Logger.getLogger(BlikiProxyParserUtil.class.getName());
    private static final Pattern WIKI_LINK_PATTERN = Pattern.compile("\\[\\[[ ]*(\\:[ ]*)?[ ]*([^\\n\\r\\|]+)([ ]*\\|[ ]*([^\\n\\r]+))?[ ]*\\]\\]([a-z]*)");
    /** Pattern to determine if the topic is a redirect. */
    private static final Pattern REDIRECT_PATTERN = Pattern.compile(".*#REDIRECT[ ]*\\[\\[(.*)\\]\\].*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    /** Pattern to detect sidebar */
    private static final Pattern SIDEBAR_PATTERN = Pattern.compile("\\{\\{[^{}]*sidebar+\\}\\}", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    /** Pattern to detect co-ordinates */
    private static final Pattern COORDINATES_PATTERN = Pattern.compile("\\{\\{coord[^{}]*\\}\\}", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    /** Pattern to detect citation(s) */
    private static final Pattern CITATIONS_PATTERN = Pattern.compile("\\{\\{citation[^{}]*\\}\\}", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    /** MediaWiki variables with i.e. {{NAMESPACE}} suffix pattern */
    private static final Pattern MEDIAWIKI_VAR_PAGENAME = Pattern.compile("\\{\\{PAGENAME\\}\\}", Pattern.DOTALL);
    /** MediaWiki variables with i.e. {{NAMESPACE}} suffix pattern */
    private static final Pattern UNSUPPORTED_MEDIAWIKI_PATTERN1 = Pattern.compile("\\{\\{[A-Z]*\\}\\}", Pattern.DOTALL);
    /** MediaWiki variables with {{... :page name}} suffix pattern */
    private static final Pattern UNSUPPORTED_MEDIAWIKI_PATTERN2 = Pattern.compile("\\{\\{[A-Z]*:page name\\}\\}", Pattern.DOTALL);
    /** MediaWiki variables with __NOTOC__,... suffix pattern */
    private static final Pattern UNSUPPORTED_MEDIAWIKI_PATTERN3 = Pattern.compile("__[A-Z]+__", Pattern.DOTALL);
    /** Revision MediaWiki variables */
    private static final Pattern UNSUPPORTED_MEDIAWIKI_PATTERN_REVISION = Pattern.compile("\\{\\{REVISION[A-Z,0-9]*\\}\\}", Pattern.DOTALL);
    private static final Pattern PROTECTED_PATTERN = Pattern.compile("\\{\\{pp-[^{}]*\\}\\}", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern UNPARSED_ERROR_PATTERN = Pattern.compile("\\{\\{[^{}]*\\}\\}", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern HTML_COMMENTS = Pattern.compile("(?s)<!--.*?-->", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    /**
     *
     * @param raw
     * @return 
     */
    public static String isRedirect(String raw) {
        if (StringUtils.isBlank(raw)) {
            return null;
        }

        Matcher m = REDIRECT_PATTERN.matcher(raw);
        return (m.matches()) ? Utilities.decodeAndEscapeTopicName(m.group(1).trim(), true) : null;
    }

    /**
     * Removes Mediawiki markup that is unsupported/broken
     * @param parserInput
     * @param text
     * @return
     */
    public static String parseUnsupportedMediaWikiMarkup(ParserInput parserInput, String text) {

        if (!Environment.getBooleanValue(Environment.PROP_PARSER_PARSE_SIDEBAR)) {
            text = SIDEBAR_PATTERN.matcher(text).replaceAll("");

        }
        if (!Environment.getBooleanValue(Environment.PROP_PARSER_PARSE_COORD)) {
            text = COORDINATES_PATTERN.matcher(text).replaceAll("");
        }

        text = CITATIONS_PATTERN.matcher(text).replaceAll("");

        text = HTML_COMMENTS.matcher(text).replaceAll("");
        text = PROTECTED_PATTERN.matcher(text).replaceAll("");

        return text;
    }

    /**
     *
     * @param parserInput
     * @param text
     * @return
     */
    public static String parseMediaWikiVariables(ParserInput parserInput, String text) {

        if((parserInput != null) && (parserInput.getTopicName() != null)){
        text = MEDIAWIKI_VAR_PAGENAME.matcher(text).replaceAll(parserInput.getTopicName());
        }else{
            text = MEDIAWIKI_VAR_PAGENAME.matcher(text).replaceAll("");
        }
        text = UNSUPPORTED_MEDIAWIKI_PATTERN1.matcher(text).replaceAll("");
        text = UNSUPPORTED_MEDIAWIKI_PATTERN2.matcher(text).replaceAll("");
        text = UNSUPPORTED_MEDIAWIKI_PATTERN3.matcher(text).replaceAll("");
        text = UNSUPPORTED_MEDIAWIKI_PATTERN_REVISION.matcher(text).replaceAll("");
        return text;
    }

    /**
     *
     * @param parserInput
     * @param text
     * @return
     */
    public static String cleanupHtmlParserError(ParserInput parserInput, String text) {

        // first try this for URL
        text = text.replace("Special:Edit?topic=PAGENAME", parserInput.getTopicName());

        // then also this for URL title
        if (!UNPARSED_ERROR_PATTERN.matcher(text).find()) {
            text = text.replaceAll("PAGENAME", parserInput.getTopicName());
            return text;
        }

        text = UNPARSED_ERROR_PATTERN.matcher(text).replaceAll("");

        return cleanupHtmlParserError(parserInput,text);
    }

    /**
     * Parse Mediawiki Link
     * @param raw
     * @return WikiLink object
     */
    public static WikiLink parseWikiLink(String raw) {
        if (StringUtils.isBlank(raw)) {
            return new WikiLink();
        }
        Matcher m = WIKI_LINK_PATTERN.matcher(raw.trim());
        if (!m.matches()) {
            return new WikiLink();
        }
        String url = m.group(2);
        WikiLink wikiLink = LinkUtil.parseWikiLink(url);
        wikiLink.setColon((m.group(1) != null));
        wikiLink.setText(m.group(4));
        String suffix = m.group(5);
        if (!StringUtils.isBlank(suffix)) {
            if (StringUtils.isBlank(wikiLink.getText())) {
                wikiLink.setText(wikiLink.getDestination() + suffix);
            } else {
                wikiLink.setText(wikiLink.getText() + suffix);
            }
        }
        return wikiLink;
    }
}
