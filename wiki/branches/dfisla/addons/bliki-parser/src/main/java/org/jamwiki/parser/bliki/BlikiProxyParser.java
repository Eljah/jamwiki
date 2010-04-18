/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jamwiki.parser.bliki;

import java.io.StringReader;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.jamwiki.parser.AbstractParser;
import org.jamwiki.parser.ParserException;
import org.jamwiki.parser.ParserInput;
import org.jamwiki.parser.ParserOutput;
import org.jamwiki.DataAccessException;
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.parser.jflex.JAMWikiSpliceProcessor;
import org.jamwiki.parser.jflex.JFlexLexer;
import org.jamwiki.parser.jflex.MagicWordUtil;
import org.jamwiki.utils.LinkUtil;
import org.jamwiki.utils.NamespaceHandler;
import org.jamwiki.utils.WikiLink;

/**
 *
 * @author dfisla
 */
public class BlikiProxyParser extends AbstractParser {

    private static final Logger logger = Logger.getLogger(BlikiProxyParser.class.getName());
    /** Splice mode is used when inserting an edited topic section back into the full topic content. */
    protected static final int MODE_SPLICE = 1;
    /** Slice mode is used when retrieving a section of a topic for editing. */
    protected static final int MODE_SLICE = 2;
    /** Minimal mode is used to do a bare minimum of parsing, usually just converting signature tags, prior to saving to the database. */
    protected static final int MODE_MINIMAL = 3;
    /** Pre-process mode is currently equivalent to metadata mode and indicates that that the JFlex pre-processor parser should be run in full. */
    protected static final int MODE_PREPROCESS = 4;
    /** Processing mode indicates that the pre-processor and processor should be run, parsing all Wiki syntax into formatted output but NOT parsing paragraph tags. */
    protected static final int MODE_PROCESS = 5;
    /** Layout mode indicates that the pre-processor and processor should be run in full, parsing all Wiki syntax into formatted output and adding layout tags such as paragraphs. */
    protected static final int MODE_LAYOUT = 6;
    /** Post-process mode indicates that the pre-processor, processor and post-processor should be run in full, parsing all Wiki syntax into formatted output and adding layout tags such as paragraphs and TOC. */
    protected static final int MODE_POSTPROCESS = 7;

    /**
     * The constructor creates a parser instance, initialized with the
     * specified parser input settings.
     *
     * @param parserInput Input configuration settings for this parser
     *  instance.
     */
    public BlikiProxyParser(ParserInput parserInput) {
        super(parserInput);
        this.parserInput.setAllowSectionEdit(Environment.getBooleanValue(Environment.PROP_PARSER_SECTION_EDIT));
    }

    /**
     * Return a parser-specific value that can be used as the content of a
     * topic representing a redirect.  For the Mediawiki syntax parser the
     * value returned would be of the form "#REDIRECT [[Topic]]".
     *
     * @param topicName The name of the topic to redirect to.
     * @return A parser-specific value that can be used as the content of a
     *  topic representing a redirect.
     */
    public String buildRedirectContent(String topicName) {

        String output = "";
        try {
            output = this.parseRedirect(new ParserOutput(), "#REDIRECT [[" + topicName + "]]");
        } catch (Exception ex) {
            logger.severe(ex.getMessage() + " TRACE: " + ex.getStackTrace());
        }
        return output;
    }

    /**
     * Perform a bare minimum of parsing as required prior to saving a topic to
     * the database. In general this method will simply parse signature tags are
     * return.
     * 
     * @param raw
     *          The raw Wiki syntax to be converted into HTML.
     * @return The parsed content.
     * @throws ParserException
     *           Thrown if any error occurs during parsing.
     */
    public String parseMinimal(String raw) throws ParserException {

        long start = System.currentTimeMillis();
        String output = raw;
        ParserOutput parserOutput = new ParserOutput();

        JAMWikiModel wikiModel = new JAMWikiModel(parserInput, parserOutput, "");
        output = wikiModel.parseTemplates(raw, true);

        if (output == null) {
            output = "";
        }


        String topicName = (!StringUtils.isBlank(this.parserInput.getTopicName())) ? this.parserInput.getTopicName() : null;
        logger.fine("Parse time (parseMinimal) for " + topicName + " (" + ((System.currentTimeMillis() - start) / 1000.000) + " s.)");

        return output;
    }

    /**
     *
     *
     * @param raw
     *          The raw Wiki syntax to be converted into HTML.
     * @return The parsed content.
     * @throws ParserException
     *           Thrown if any error occurs during parsing.
     */
    public String parseFragment(ParserOutput parserOutput, String raw, int mode) throws ParserException {

        long start = System.currentTimeMillis();
        // some parser expressions require that lines end in a newline, so add a newline
        // to the end of the content for good measure
        String output = raw + '\n';

        String redirectTo = BlikiProxyParserUtil.isRedirect(raw);

        if (redirectTo != null) {

            parserOutput.setRedirect(redirectTo);
            // redirects are parsed differently
            return this.parseRedirect(parserOutput, raw);
        }

        try {
            parserInput.setTopicName(MagicWordUtil.processMagicWord(parserInput, parserInput.getTopicName()));
        } catch (Exception ex) {
        }

        if (Environment.getBooleanValue(Environment.PROP_PARSER_REMOVE_UNSUPPORTED)) {
            raw = BlikiProxyParserUtil.parseUnsupportedMediaWikiMarkup(parserInput, raw);
        }

        JAMWikiModel wikiModel = new JAMWikiModel(parserInput, parserOutput, "");
        output = wikiModel.render(new JAMHTMLConverter(parserInput), raw);

        if (output == null) {
            output = "";
        } else {

            if (Environment.getBooleanValue(Environment.PROP_PARSER_CLEAN_HTML)) {
                output = BlikiProxyParserUtil.cleanupHtmlParserError(parserInput, output);
            }
        }
        /*
        for (String link : wikiModel.getLinks()) {
        parserOutput.addLink(link);
        logger.debug("PARSER-OUTPUT-LINK: " + link);
        }


        for (String template : wikiModel.getTemplates()) {
        parserOutput.addTemplate(template);
        logger.debug("PARSER-OUTPUT-TEMPLATE: " + template);
        }

        Map<String, String> cats = wikiModel.getCategories();

        if ((cats != null) && (!cats.isEmpty())) {
        for (String key : cats.keySet()) {
        parserOutput.addCategory(key, cats.get(key));
        logger.debug("PARSER-OUTPUT-CATEGORY: " + key);
        }
        }
         */

        String topicName = (!StringUtils.isBlank(this.parserInput.getTopicName())) ? this.parserInput.getTopicName() : null;
        logger.fine("Parse time (parseHTML) for " + topicName + " (" + ((System.currentTimeMillis() - start) / 1000.000) + " s.)");

        return output;
    }

    /**
     * Returns a HTML representation of the given wiki raw text for online
     * representation.
     *
     * @param parserOutput
     *          A ParserOutput object containing parser metadata output.
     * @param raw
     *          The raw Wiki syntax to be converted into HTML.
     * @return The parsed content.
     * @throws ParserException
     *           Thrown if any error occurs during parsing.
     */
    public String parseHTML(ParserOutput parserOutput, String raw) throws ParserException {

        long start = System.currentTimeMillis();

        String output = null;

        String redirectTo = BlikiProxyParserUtil.isRedirect(raw);

        if (redirectTo != null) {

            // redirects are parsed differently
            logger.fine("PARSE-REDIRECT");
            output = this.parseRedirect(parserOutput, raw);

            parserOutput.setRedirect(redirectTo);
        } else {
            logger.fine("PARSE-NON-REDIRECT");
            String context = parserInput.getContext();
            if (context == null) {
                context = "";
            }
            parserInput.setContext(context);
            try {
                parserInput.setTopicName(MagicWordUtil.processMagicWord(parserInput, parserInput.getTopicName()));
            } catch (Exception ex) {
            }

            JAMWikiModel wikiModel = new JAMWikiModel(parserInput, parserOutput, context);

            if (Environment.getBooleanValue(Environment.PROP_PARSER_REMOVE_UNSUPPORTED)) {
                raw = BlikiProxyParserUtil.parseUnsupportedMediaWikiMarkup(parserInput, raw);
            }

            output = wikiModel.render(new JAMHTMLConverter(parserInput), raw);

            if (output == null) {
                output = "";
            } else {

                if (Environment.getBooleanValue(Environment.PROP_PARSER_CLEAN_HTML)) {
                    output = BlikiProxyParserUtil.cleanupHtmlParserError(parserInput, output);
                }
            }
        }

        String topicName = (!StringUtils.isBlank(this.parserInput.getTopicName())) ? this.parserInput.getTopicName() : null;
        logger.fine("Parse time (parseHTML) for " + topicName + " (" + ((System.currentTimeMillis() - start) / 1000.000) + " s.)");

        return output;
    }

    /**
     * This method provides a way to parse content and set all output metadata,
     * such as link values used by the search engine.
     *
     * @param parserOutput A ParserOutput object containing results of the parsing process.
     * @param raw The raw Wiki syntax to be converted into HTML.
     */
    public void parseMetadata(ParserOutput parserOutput, String raw) throws ParserException {
        long start = System.currentTimeMillis();

        String context = parserInput.getContext();
        if (context == null) {
            context = "";
        }
        parserInput.setContext(context);
        try {
            parserInput.setTopicName(MagicWordUtil.processMagicWord(parserInput, parserInput.getTopicName()));
        } catch (Exception ex) {
        }

        String redirectTo = BlikiProxyParserUtil.isRedirect(raw);
        if (redirectTo != null) {
            parserOutput.setRedirect(redirectTo);
        }

        if (Environment.getBooleanValue(Environment.PROP_PARSER_REMOVE_UNSUPPORTED)) {
            raw = BlikiProxyParserUtil.parseUnsupportedMediaWikiMarkup(parserInput, raw);
        }

        JAMWikiModel wikiModel = new JAMWikiModel(parserInput, parserOutput, context);
        wikiModel.render(raw);

        /*
        logger.debug("============== LINKS =============");
        for (String link : parserOutput.getLinks()) {
        logger.debug("LINK: " + link);
        }

        logger.debug("============== TEMPLATES =============");
        for (String template : parserOutput.getTemplates()) {
        logger.debug("TEMPLATE: " + template);
        }

        LinkedHashMap<String, String> categories = parserOutput.getCategories();
        logger.debug("============== CATEGORIES =============");
        for (String key : categories.keySet()) {
        logger.debug("CATEGORY: " + categories.get(key));
        }
         */


        String topicName = (!StringUtils.isBlank(this.parserInput.getTopicName())) ? this.parserInput.getTopicName() : null;
        logger.fine("Parse time (parseMetadata) for " + topicName + " (" + ((System.currentTimeMillis() - start) / 1000.000) + " s.)");

    }

    /**
     * When making a section edit this function provides the capability to retrieve
     * all text within a specific heading level.  For example, if targetSection is
     * specified as five, and the sixth heading is an &lt;h2&gt;, then this method
     * will return the heading tag and all text up to either the next &lt;h2&gt;,
     * &lt;h1&gt;, or the end of the document, whichever comes first.
     *
     * @param parserOutput A ParserOutput object containing parser
     *  metadata output.
     * @param raw The raw Wiki text that is to be parsed.
     * @param targetSection The section (counted from zero) that is to be returned.
     * @return Returns the raw topic content for the target section.
     * @throws ParserException Thrown if any error occurs during parsing.
     */
    public String parseSlice(ParserOutput parserOutput, String raw, int targetSection) throws ParserException {
        long start = System.currentTimeMillis();
        StringReader reader = toStringReader(raw);
        JAMWikiSpliceProcessor lexer = new JAMWikiSpliceProcessor(reader);
        lexer.setTargetSection(targetSection);
        String output = this.lex(lexer, raw, parserOutput, MODE_SLICE);

        String topicName = (!StringUtils.isBlank(this.parserInput.getTopicName())) ? this.parserInput.getTopicName() : null;
        logger.fine("Parse time (parseSlice) for " + topicName + " (" + ((System.currentTimeMillis() - start) / 1000.000) + " s.)");

        return output;
    }

    /**
     * This method provides the capability for re-integrating a section edit back
     * into the main topic.  The text to be re-integrated is provided along with the
     * full Wiki text and a targetSection.  All of the content of targetSection
     * is then replaced with the new text.
     *
     * @param parserOutput A ParserOutput object containing parser
     *  metadata output.
     * @param raw The raw Wiki text that is to be parsed.
     * @param targetSection The section (counted from zero) that is to be returned.
     * @param replacementText The text to replace the target section text with.
     * @return The raw topic content including the new replacement text.
     * @throws ParserException Thrown if any error occurs during parsing.
     */
    public String parseSplice(ParserOutput parserOutput, String raw, int targetSection, String replacementText) throws ParserException {
        long start = System.currentTimeMillis();
        StringReader reader = toStringReader(raw);
        JAMWikiSpliceProcessor lexer = new JAMWikiSpliceProcessor(reader);
        lexer.setReplacementText(replacementText);
        lexer.setTargetSection(targetSection);
        String output = this.lex(lexer, raw, parserOutput, MODE_SPLICE);

        String topicName = (!StringUtils.isBlank(this.parserInput.getTopicName())) ? this.parserInput.getTopicName() : null;
        logger.fine("Parse time (parseSplice) for " + topicName + " (" + ((System.currentTimeMillis() - start) / 1000.000) + " s.)");

        return output;
    }

    /**
     * Parse a topic that is a redirect.  Ordinarily the contents of the redirected
     * topic would be displayed, but in some cases (such as when explicitly viewing
     * a redirect) the redirect page contents need to be displayed.
     *
     * @param parserOutput A ParserOutput object containing parser
     *  metadata output.
     * @param raw The raw Wiki syntax to be converted into HTML.
     * @return The parsed content.
     * @throws ParserException Thrown if any error occurs during parsing.
     */
    protected String parseRedirect(ParserOutput parserOutput, String raw) throws ParserException {
        String redirect = BlikiProxyParserUtil.isRedirect(raw);
        WikiLink wikiLink = BlikiProxyParserUtil.parseWikiLink("[[" + redirect + "]]");
        String style = "redirect";
        String virtualWiki = this.parserInput.getVirtualWiki();
        try {
            // see if the redirect link starts with a virtual wiki
            if (wikiLink.getColon() && !StringUtils.isBlank(wikiLink.getNamespace())) {
                if (WikiBase.getDataHandler().lookupVirtualWiki(wikiLink.getNamespace()) != null) {
                    virtualWiki = wikiLink.getNamespace();
                    wikiLink.setDestination(wikiLink.getDestination().substring(virtualWiki.length() + NamespaceHandler.NAMESPACE_SEPARATOR.length()));
                }
            }
            if (!LinkUtil.isExistingArticle(virtualWiki, wikiLink.getDestination())) {
                style = "edit redirect";
            }
            return LinkUtil.buildInternalLinkHtml(this.parserInput.getContext(), virtualWiki, wikiLink, null, style, null, false);
        } catch (DataAccessException e) {
            throw new ParserException(e);
        }
    }

    /**
     * Convert a string of text to be parsed into a StringReader, performing any
     * preprocessing, such as removing linefeeds, in the process.
     */
    private StringReader toStringReader(String raw) {
        return new StringReader(StringUtils.remove(raw, '\r'));
    }

    /**
     * Utility method for executing a lexer parse.
     */
    private String lex(JFlexLexer lexer, String raw, ParserOutput parserOutput, int mode) throws ParserException {
        lexer.init(this.parserInput, parserOutput, mode);
        validate(lexer);
        this.parserInput.incrementDepth();
        // avoid infinite loops
        if (this.parserInput.getDepth() > 100) {
            String topicName = (!StringUtils.isBlank(this.parserInput.getTopicName())) ? this.parserInput.getTopicName() : null;
            throw new ParserException("Infinite parsing loop - over " + this.parserInput.getDepth() + " parser iterations while parsing topic " + topicName);
        }
        try {
            while (true) {
                String line = lexer.yylex();
                if (line == null) {
                    break;
                }
                lexer.append(line);
            }
        } catch (Exception e) {
            throw new ParserException(e);
        }
        this.parserInput.decrementDepth();
        return lexer.popAllTags();
    }

    /**
     * Validate that all settings required for the parser have been set, and if
     * not throw an exception.
     *
     * @throws ParserException Thrown if the parser is not initialized properly,
     *  usually due to a parser input field not being set.
     */
    private static void validate(JFlexLexer lexer) throws ParserException {
        // validate parser settings
        boolean validated = true;
        if (lexer.getMode() == MODE_SPLICE || lexer.getMode() == MODE_SLICE) {
            if (lexer.getParserInput().getTopicName() == null) {
                logger.warning("Failure while initializing parser: topic name is null.");
                validated = false;
            }
        }
        if (!validated) {
            throw new ParserException("Parser info not properly initialized");
        }
    }
}
