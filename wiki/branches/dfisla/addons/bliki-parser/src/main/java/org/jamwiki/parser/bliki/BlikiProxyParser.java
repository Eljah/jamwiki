/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jamwiki.parser.bliki;

import info.bliki.wiki.model.WikiModel;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.jamwiki.parser.AbstractParser;
import org.jamwiki.parser.ParserException;
import org.jamwiki.parser.ParserInput;
import org.jamwiki.parser.ParserOutput;
import org.apache.log4j.Logger;

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
    /** Pattern to determine if the topic is a redirect. */
    private static final Pattern REDIRECT_PATTERN = Pattern.compile("#REDIRECT[ ]+\\[\\[([^\\n\\r\\]]+)\\]\\]", Pattern.CASE_INSENSITIVE);
    private static final Pattern SIDEBAR_PATTERN = Pattern.compile("\\{\\{.*sidebar+\\}\\}", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    //private static final Pattern IFERROR_PATTERN = Pattern.compile("\\(?\\s*\\{\\{.*iferror+.*\\}\\}\\s*\\)?", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    //private static final Pattern IFERROR_2_PATTERN = Pattern.compile("\\(\\s*\\{\\{.*iferror+.*\\}\\}\\s*\\)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final boolean SECTION_EDIT = false;

    /**
     * The constructor creates a parser instance, initialized with the
     * specified parser input settings.
     *
     * @param parserInput Input configuration settings for this parser
     *  instance.
     */
    public BlikiProxyParser(ParserInput parserInput) {
        super(parserInput);
        this.parserInput.setAllowSectionEdit(SECTION_EDIT);
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
        return "#REDIRECT [[" + topicName + "]]";
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

        raw = this.removeUnsupportedMediaWikiMarkup(raw);
        
        JAMWikiModel wikiModel = new JAMWikiModel(parserInput, parserOutput, "");
        output = wikiModel.parseTemplates(raw, true);

        //output = this.removeUnsupportedHtml(output);
        
        output = output == null ? "" : output;
        String topicName = (!StringUtils.isBlank(this.parserInput.getTopicName())) ? this.parserInput.getTopicName() : null;
        logger.info("Parse time (parseMinimal) for " + topicName + " (" + ((System.currentTimeMillis() - start) / 1000.000) + " s.)");
        return output;
    }

    public String parseFragment(ParserOutput parserOutput, String raw, int mode) throws ParserException {

        long start = System.currentTimeMillis();
        // some parser expressions require that lines end in a newline, so add a newline
        // to the end of the content for good measure
        String output = raw + '\n';

        //if (!StringUtils.isBlank(parserOutput.getRedirect())) {
        // redirects are parsed differently
        //output = this.parseRedirect(parserOutput, raw);
        //}
        raw = this.removeUnsupportedMediaWikiMarkup(raw);

        WikiModel wikiModel = new WikiModel("${image}", "${title}");
        output = wikiModel.render(raw);

        //output = this.removeUnsupportedHtml(output);

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

        String topicName = (!StringUtils.isBlank(this.parserInput.getTopicName())) ? this.parserInput.getTopicName() : null;
        logger.info("Parse time (parseHTML) for " + topicName + " (" + ((System.currentTimeMillis() - start) / 1000.000) + " s.)");
        return output;
    }

    public String removeUnsupportedMediaWikiMarkup(String text) {
        text = SIDEBAR_PATTERN.matcher(text).replaceAll("");
        return text;
    }
/*
    public String removeUnsupportedHtml(String text) {
        text = IFERROR_PATTERN.matcher(text).replaceAll("");
        return text;
    }
*/
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
        //if (!StringUtils.isBlank(this.isRedirect(parserInput, raw, JFlexParser.MODE_LAYOUT))) {
        // redirects are parsed differently
        //    output = this.parseRedirect(parserOutput, raw);
        //} else {
        String context = parserInput.getContext();
        if (context == null) {
            context = "";
        }
        JAMWikiModel wikiModel = new JAMWikiModel(parserInput, parserOutput, context);

        raw = this.removeUnsupportedMediaWikiMarkup(raw);

        output = wikiModel.render(new JAMHTMLConverter(parserInput), raw);
        //output = this.removeUnsupportedHtml(output);
        output = output == null ? "" : output;

        //}
        String topicName = (!StringUtils.isBlank(this.parserInput.getTopicName())) ? this.parserInput.getTopicName() : null;
        logger.info("Parse time (parseHTML) for " + topicName + " (" + ((System.currentTimeMillis() - start) / 1000.000) + " s.)");
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
        // FIXME - set a bogus context value to avoid parser errors
        if (this.parserInput.getContext() == null) {
            this.parserInput.setContext("/wiki");
        }
        // some parser expressions require that lines end in a newline, so add a newline
        // to the end of the content for good measure
        String output = raw + '\n';

        raw = this.removeUnsupportedMediaWikiMarkup(raw);

        WikiModel wikiModel = new WikiModel("${image}", "${title}");
        wikiModel.render(raw);

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

        String topicName = (!StringUtils.isBlank(this.parserInput.getTopicName())) ? this.parserInput.getTopicName() : null;
        logger.info("Parse time (parseMetadata) for " + topicName + " (" + ((System.currentTimeMillis() - start) / 1000.000) + " s.)");
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
        return "";
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
        return "";
    }
}