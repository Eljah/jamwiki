/**
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, version 2.1, dated February 1999.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the latest version of the GNU Lesser General
 * Public License as published by the Free Software Foundation;
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program (LICENSE.txt); if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.jamwiki.parser.jflex;

import java.io.StringReader;
import org.apache.commons.lang3.StringUtils;
import org.jamwiki.DataAccessException;
import org.jamwiki.parser.AbstractParser;
import org.jamwiki.parser.LinkUtil;
import org.jamwiki.parser.ParserException;
import org.jamwiki.parser.ParserInput;
import org.jamwiki.parser.ParserOutput;
import org.jamwiki.parser.WikiLink;
import org.jamwiki.utils.WikiLogger;

/**
 * Implementation of {@link org.jamwiki.parser.AbstractParser} that uses
 * <a href="http://jflex.de/">JFlex</a> as a lexer to convert Wiki syntax into
 * HTML or other formats.
 */
public class JFlexParser extends AbstractParser {

	private static final WikiLogger logger = WikiLogger.getLogger(JFlexParser.class.getName());

	/** Mode used when parsing edit comments. */
	public static final int MODE_EDIT_COMMENT = 0;
	/** Splice mode is used when inserting an edited topic section back into the full topic content. */
	public static final int MODE_SPLICE = 1;
	/** Slice mode is used when retrieving a section of a topic for editing. */
	public static final int MODE_SLICE = 2;
	/** Minimal mode is used to do a bare minimum of parsing, usually just converting signature tags, prior to saving to the database. */
	public static final int MODE_MINIMAL = 3;
	/** Template mode indicates that the template body is being process for includeonly and similar tags. */
	public static final int MODE_TEMPLATE_BODY = 4;
	/** Template mode indicates that template processing is occurring. */
	public static final int MODE_TEMPLATE = 5;
	/** Template mode indicates that custom tag processing is occurring. */
	public static final int MODE_CUSTOM = 6;
	/** Pre-process mode indicates that that the JFlex pre-processor parser should be run in full. */
	public static final int MODE_PREPROCESS = 7;
	/** Processing mode indicates that the pre-processor and processor should be run, parsing all Wiki syntax into formatted output but NOT parsing paragraph tags. */
	public static final int MODE_PROCESS = 8;
	/** Layout mode indicates that the pre-processor and processor should be run in full, parsing all Wiki syntax into formatted output and adding layout tags such as paragraphs. */
	public static final int MODE_LAYOUT = 9;
	/** Post-process mode indicates that the pre-processor, processor and post-processor should be run in full, parsing all Wiki syntax into formatted output and adding layout tags such as paragraphs and TOC. */
	public static final int MODE_POSTPROCESS = 10;

	/**
	 * The constructor creates a parser instance, initialized with the
	 * specified parser input settings.
	 *
	 * @param parserInput Input configuration settings for this parser
	 *  instance.
	 */
	public JFlexParser(ParserInput parserInput) {
		super(parserInput);
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
	 * Utility method for executing a lexer parse.
	 */
	private String lex(JFlexLexer lexer, String raw, ParserOutput parserOutput, int mode) throws ParserException {
		lexer.init(this.parserInput, parserOutput, mode);
		validate(lexer);
		this.parserInput.incrementDepth();
		String result = null;
		try {
			result = lexer.lex();
		} catch (Exception e) {
			this.parserInput.decrementDepth();
			throw new ParserException("Failure while parsing topic " + this.parserInput.getTopicName(), e);
		}
		this.parserInput.decrementDepth();
		return result;
	}

	/**
	 * After templates are parsed, look for any custom tags.
	 *
	 * @param parserOutput A ParserOutput object containing parser
	 *  metadata output.
	 * @param raw The raw wiki syntax to be parsed.
	 * @return The parsed content.
	 * @throws ParserException Thrown if any error occurs during parsing.
	 */
	private String parseCustom(ParserOutput parserOutput, String raw, int mode) throws ParserException {
		StringReader reader = toStringReader(raw);
		JFlexLexer lexer = new JAMWikiCustomTagLexer(reader);
		int preMode = (mode > JFlexParser.MODE_CUSTOM) ? JFlexParser.MODE_CUSTOM : mode;
		return this.lex(lexer, raw, parserOutput, preMode);
	}

	/**
	 * Parse an edit comment and return HTML for online representation.
	 *
	 * @param parserOutput A ParserOutput object containing parser
	 *  metadata output.
	 * @param raw The raw Wiki syntax to be converted into HTML.
	 * @return The parsed content.
	 * @throws ParserException Thrown if any error occurs during parsing.
	 */
	public String parseEditComment(ParserOutput parserOutput, String raw) throws ParserException {
		StringReader reader = toStringReader(raw);
		JFlexLexer lexer = new JAMWikiEditCommentLexer(reader);
		return this.lex(lexer, raw, parserOutput, MODE_EDIT_COMMENT).trim();
	}

	/**
	 * This method parses content, performing all transformations except for
	 * layout changes such as adding paragraph tags.  It is suitable to be used
	 * when parsing the contents of a link or performing similar internal
	 * manipulation.
	 *
	 * @param parserOutput A ParserOutput object containing parser
	 *  metadata output.
	 * @param raw The raw Wiki syntax to be converted into HTML.
	 * @param mode The parser mode to use when parsing.  Mode affects what
	 *  type of parsing actions are taken when processing raw text.
	 * @return The parsed content.
	 * @throws ParserException Thrown if any error occurs during parsing.
	 */
	public String parseFragment(ParserOutput parserOutput, String raw, int mode) throws ParserException {
		String output = raw;
		// maintain the original output, which has all of the category and link info
		int preMode = (mode > JFlexParser.MODE_TEMPLATE) ? JFlexParser.MODE_TEMPLATE : mode;
		output = this.parseTemplate(parserOutput, output, preMode);
		preMode = (mode > JFlexParser.MODE_CUSTOM) ? JFlexParser.MODE_CUSTOM : mode;
		output = this.parseCustom(parserOutput, output, preMode);
		preMode = (mode > JFlexParser.MODE_PREPROCESS) ? JFlexParser.MODE_PREPROCESS : mode;
		output = this.parsePreProcess(parserOutput, output, preMode);
		// layout should not be done while parsing fragments
		preMode = (mode > JFlexParser.MODE_PROCESS) ? JFlexParser.MODE_PROCESS : mode;
		output = this.parseProcess(parserOutput, output, preMode);
		return output;
	}

	/**
	 * Returns a HTML representation of the given wiki raw text for online representation.
	 *
	 * @param parserOutput A ParserOutput object containing parser
	 *  metadata output.
	 * @param raw The raw Wiki syntax to be converted into HTML.
	 * @return The parsed content.
	 * @throws ParserException Thrown if any error occurs during parsing.
	 */
	public String parseHTML(ParserOutput parserOutput, String raw) throws ParserException {
		long start = System.currentTimeMillis();
		// some parser expressions require that lines end in a newline, so add a newline
		// to the end of the content for good measure
		String output = raw + '\n';
		output = this.parseTemplate(parserOutput, output, JFlexParser.MODE_TEMPLATE);
		output = this.parseCustom(parserOutput, output, JFlexParser.MODE_CUSTOM);
		output = this.parsePreProcess(parserOutput, output, JFlexParser.MODE_PREPROCESS);
		output = this.parseProcess(parserOutput, output, JFlexParser.MODE_LAYOUT);
		output = this.parsePostProcess(parserOutput, output, JFlexParser.MODE_POSTPROCESS);
		if (!StringUtils.isBlank(parserOutput.getRedirect())) {
			// redirects are parsed differently
			output = this.parseRedirect(parserOutput, raw);
		}
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
		output = this.parseTemplate(parserOutput, output, JFlexParser.MODE_TEMPLATE);
		output = this.parseCustom(parserOutput, output, JFlexParser.MODE_CUSTOM);
		output = this.parsePreProcess(parserOutput, output, JFlexParser.MODE_PREPROCESS);
		if (logger.isInfoEnabled()) {
			String topicName = (!StringUtils.isBlank(this.parserInput.getTopicName())) ? this.parserInput.getTopicName() : null;
			logger.info("Parse time (parseMetadata) for " + topicName + " (" + ((System.currentTimeMillis() - start) / 1000.000) + " s.)");
		}
	}

	/**
	 * Perform a bare minimum of parsing as required prior to saving a topic
	 * to the database.  In general this method will simply parse signature
	 * tags are return.
	 *
	 * @param raw The raw Wiki syntax to be converted into HTML.
	 * @return The parsed content.
	 * @throws ParserException Thrown if any error occurs during parsing.
	 */
	public String parseMinimal(String raw) throws ParserException {
		long start = System.currentTimeMillis();
		String output = raw;
		ParserOutput parserOutput = new ParserOutput();
		output = this.parseTemplate(parserOutput, output, JFlexParser.MODE_MINIMAL);
		String topicName = (!StringUtils.isBlank(this.parserInput.getTopicName())) ? this.parserInput.getTopicName() : null;
		logger.info("Parse time (parseHTML) for " + topicName + " (" + ((System.currentTimeMillis() - start) / 1000.000) + " s.)");
		return output;
	}

	/**
	 * First stage of the parser, this method parses templates and signatures.
	 *
	 * @param parserOutput A ParserOutput object containing parser
	 *  metadata output.
	 * @param raw The raw Wiki syntax to be converted into HTML.
	 * @return The parsed content.
	 * @throws ParserException Thrown if any error occurs during parsing.
	 */
	private String parseTemplate(ParserOutput parserOutput, String raw, int mode) throws ParserException {
		StringReader reader = toStringReader(raw);
		JFlexLexer lexer = new JAMWikiTemplateLexer(reader);
		int preMode = (mode > JFlexParser.MODE_TEMPLATE) ? JFlexParser.MODE_TEMPLATE : mode;
		return this.lex(lexer, raw, parserOutput, preMode);
	}

	/**
	 * Second stage of the parser, this method builds metadata.
	 *
	 * @param parserOutput A ParserOutput object containing parser
	 *  metadata output.
	 * @param raw The raw Wiki syntax to be converted into HTML.
	 * @return The parsed content.
	 * @throws ParserException Thrown if any error occurs during parsing.
	 */
	private String parsePreProcess(ParserOutput parserOutput, String raw, int mode) throws ParserException {
		if (mode < JFlexParser.MODE_PREPROCESS) {
			return raw;
		}
		StringReader reader = toStringReader(raw);
		JFlexLexer lexer = new JAMWikiPreLexer(reader);
		int preMode = (mode > JFlexParser.MODE_PREPROCESS) ? JFlexParser.MODE_PREPROCESS : mode;
		return this.lex(lexer, raw, parserOutput, preMode);
	}

	/**
	 * Third stage of the parser, this method parses most Wiki syntax, validates
	 * HTML, and performs the majority of the parser conversion.
	 *
	 * @param parserOutput A ParserOutput object containing parser
	 *  metadata output.
	 * @param raw The raw Wiki syntax to be converted into HTML.
	 * @return The parsed content.
	 * @throws ParserException Thrown if any error occurs during parsing.
	 */
	private String parseProcess(ParserOutput parserOutput, String raw, int mode) throws ParserException {
		if (mode < JFlexParser.MODE_PROCESS) {
			return raw;
		}
		StringReader reader = toStringReader(raw);
		JFlexLexer lexer = new JAMWikiLexer(reader);
		return this.lex(lexer, raw, parserOutput, mode);
	}

	/**
	 * In most cases this method is the final stage of the parser, adding
	 * paragraph tags and other layout elements that for various reasons
	 * cannot be added during the first parsing stage.
	 *
	 * @param parserOutput A ParserOutput object containing parser
	 *  metadata output.
	 * @param raw The raw Wiki syntax to be converted into HTML.
	 * @return The parsed content.
	 * @throws ParserException Thrown if any error occurs during parsing.
	 */
	private String parsePostProcess(ParserOutput parserOutput, String raw, int mode) throws ParserException {
		if (mode < JFlexParser.MODE_POSTPROCESS) {
			return raw;
		}
		StringReader reader = toStringReader(raw);
		JFlexLexer lexer = new JAMWikiPostLexer(reader);
		return this.lex(lexer, raw, parserOutput, mode);
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
		// flush any existing links or categories since this will be re-parsed
		parserOutput.reset();
		// pre-process the text to get the redirect and process metadata
		JFlexParserUtil.parseFragment(this.parserInput, parserOutput, raw, JFlexParser.MODE_PREPROCESS);
		String redirect = parserOutput.getRedirect();
		WikiLink wikiLink = JFlexParserUtil.parseWikiLink(this.parserInput, parserOutput, "[[" + redirect + "]]");
		String style = "redirect";
		try {
			String virtualWiki = this.parserInput.getVirtualWiki();
			// see if the redirect link starts with a virtual wiki
			if (wikiLink.getAltVirtualWiki() != null) {
				virtualWiki = wikiLink.getAltVirtualWiki().getName();
			}
			if (LinkUtil.isExistingArticle(virtualWiki, wikiLink.getDestination()) == null && !wikiLink.isSpecial()) {
				style = "edit redirect";
			}
			return LinkUtil.buildInternalLinkHtml(wikiLink, null, style, null, false);
		} catch (DataAccessException e) {
			throw new ParserException(e);
		}
	}

	/**
	 * This method provides the capability for retrieving a section of Wiki markup
	 * from an existing document.  It is used primarily when editing a section of
	 * a topic.  This method will return all content from the specified section, up
	 * to the either the next section of the same or greater level or the end of the
	 * document.  For example, if the specified section is an &lt;h3&gt;, all content
	 * up to the next &lt;h1&gt;, &lt;h2&gt;, &lt;h3&gt; or the end of the document
	 * will be returned.
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
		JAMWikiSpliceLexer lexer = new JAMWikiSpliceLexer(reader);
		lexer.setTargetSection(targetSection);
		String output = this.lex(lexer, raw, parserOutput, JFlexParser.MODE_SLICE);
		String topicName = (!StringUtils.isBlank(this.parserInput.getTopicName())) ? this.parserInput.getTopicName() : null;
		logger.debug("Parse time (parseSlice) for " + topicName + " (" + ((System.currentTimeMillis() - start) / 1000.000) + " s.)");
		return output;
	}

	/**
	 * This method provides the capability for splicing a section of new content back
	 * into a document.  It is used primarily when editing a section of a topic.  This
	 * method will replace all content in a specified section, up to the either the next
	 * section of the same or greater level or the end of the document.  For example, if
	 * the specified section is an &lt;h3&gt;, all content up to the next &lt;h1&gt;,
	 * &lt;h2&gt;, &lt;h3&gt; or the end of the document will be replaced with the
	 * specified text.
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
		JAMWikiSpliceLexer lexer = new JAMWikiSpliceLexer(reader);
		lexer.setReplacementText(replacementText);
		lexer.setTargetSection(targetSection);
		String output = this.lex(lexer, raw, parserOutput, JFlexParser.MODE_SPLICE);
		String topicName = (!StringUtils.isBlank(this.parserInput.getTopicName())) ? this.parserInput.getTopicName() : null;
		logger.debug("Parse time (parseSplice) for " + topicName + " (" + ((System.currentTimeMillis() - start) / 1000.000) + " s.)");
		return output;
	}

	/**
	 * Convert a string of text to be parsed into a StringReader, performing any
	 * preprocessing, such as removing linefeeds, in the process.
	 */
	private StringReader toStringReader(String raw) {
		return new StringReader(StringUtils.remove(raw, '\r'));
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
		if (lexer.mode == JFlexParser.MODE_SPLICE || lexer.mode == JFlexParser.MODE_SLICE) {
			if (lexer.parserInput.getTopicName() == null) {
				logger.info("Failure while initializing parser: topic name is null.");
				validated = false;
			}
		} else if (lexer.mode == JFlexParser.MODE_POSTPROCESS) {
			if (lexer.parserInput == null) {
				logger.info("Failure while initializing parser: ParserInput is null.");
				validated = false;
			}
			if (lexer.parserInput.getTableOfContents() == null) {
				logger.info("Failure while initializing parser: table of contents object is null.");
				validated = false;
			}
		} else if (lexer.mode >= JFlexParser.MODE_PROCESS && lexer.mode <= JFlexParser.MODE_LAYOUT) {
			if (lexer.parserInput.getTableOfContents() == null) {
				logger.info("Failure while initializing parser: table of contents object is null.");
				validated = false;
			}
			if (lexer.parserInput.getTopicName() == null) {
				logger.info("Failure while initializing parser: topic name is null.");
				validated = false;
			}
			if (lexer.parserInput.getContext() == null) {
				logger.info("Failure while initializing parser: context is null.");
				validated = false;
			}
			if (lexer.parserInput.getVirtualWiki() == null) {
				logger.info("Failure while initializing parser: virtual wiki is null.");
				validated = false;
			}
		} else if (lexer.mode <= JFlexParser.MODE_PREPROCESS && lexer.mode >= JFlexParser.MODE_MINIMAL) {
			if (lexer.parserInput.getVirtualWiki() == null) {
				logger.info("Failure while initializing parser: virtual wiki is null.");
				validated = false;
			}
			if (lexer.parserInput.getTopicName() == null) {
				logger.info("Failure while initializing parser: topic name is null.");
				validated = false;
			}
		}
		if (!validated) {
			throw new ParserException("Parser info not properly initialized");
		}
	}
}