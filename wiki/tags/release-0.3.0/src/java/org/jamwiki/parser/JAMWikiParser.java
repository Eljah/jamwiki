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
package org.jamwiki.parser;

import java.io.StringReader;
import org.apache.log4j.Logger;
import org.jamwiki.Environment;

/**
 * Parser used to implement MediaWiki syntax.
 */
public class JAMWikiParser extends AbstractParser {

	private static final Logger logger = Logger.getLogger(JAMWikiParser.class);

	/**
	 * Sets the basics for this parser.
	 *
	 * @param parserInput General information about this parser.
	 */
	public JAMWikiParser(ParserInput parserInput) {
		super(parserInput);
	}

	/**
	 * Parse text for online display.
	 */
	public ParserOutput parseHTML(String rawtext, String topicName) throws Exception {
		long start = System.currentTimeMillis();
		// some parser expressions require that lines end in a newline, so add a newline
		// to the end of the content for good measure
		rawtext += '\n';
		StringReader raw = new StringReader(rawtext);
		// maintain the original output, which has all of the category and link info
		ParserOutput original = this.parsePreProcess(raw);
		if (this.parserInput.getMode() != ParserInput.MODE_NORMAL) {
			// save or preview mode, add pre-save processor
			ParserOutput parserOutput = this.parsePreSave(original.getContent());
			original.setContent(parserOutput.getContent());
		}
		raw = new StringReader(original.getContent());
		ParserOutput parserOutput = this.parsePostProcess(raw);
		original.setContent(parserOutput.getContent());
		logger.info("Parse time (parseHTML) for " + topicName + "(" + ((System.currentTimeMillis() - start) / 1000.000) + " s.)");
		return original;
	}

	/**
	 * First stage of the parser, this method parses most Wiki syntax, validates
	 * HTML, and performs the majority of the parser conversion.
	 *
	 * @param raw The raw Wiki syntax to be converted into HTML.
	 * @return A ParserOutput object containing results of the parsing process.
	 */
	protected ParserOutput parsePreProcess(StringReader raw) throws Exception {
		JAMWikiPreProcessor lexer = new JAMWikiPreProcessor(raw);
		lexer.setParserInput(this.parserInput);
		return this.lex(lexer);
	}

	/**
	 * Parse MediaWiki signatures and other tags that should not be
	 * saved as part of the topic source.  This method is usually only called
	 * during edits.
	 *
	 * @param raw The raw Wiki syntax to be converted into HTML.
	 * @return A ParserOutput object containing results of the parsing process.
	 */
	public ParserOutput parsePreSave(String contents) throws Exception {
		StringReader raw = new StringReader(contents);
		JAMWikiPreSaveProcessor lexer = new JAMWikiPreSaveProcessor(raw);
		lexer.setParserInput(this.parserInput);
		return this.lex(lexer);
	}

	/**
	 * In most cases this method is the second and final stage of the parser,
	 * adding paragraph tags and other layout elements that for various reasons
	 * cannot be added during the first parsing stage.
	 *
	 * @param raw The raw Wiki syntax to be converted into HTML.
	 * @return A ParserOutput object containing results of the parsing process.
	 */
	private ParserOutput parsePostProcess(StringReader raw) throws Exception {
		JAMWikiPostProcessor lexer = new JAMWikiPostProcessor(raw);
		lexer.setParserInput(this.parserInput);
		return this.lex(lexer);
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
	 * @param rawtext The raw Wiki syntax from which a section is to be retrieved.
	 * @param topicName The name of the topic that is being parsed.
	 * @param targetSection The section of the document to be replaced (first section is 1).
	 * @return All markup from the target section, contained within a ParserOutput
	 *  object.
	 */
	public ParserOutput parseSlice(String rawtext, String topicName, int targetSection) throws Exception {
		long start = System.currentTimeMillis();
		StringReader raw = new StringReader(rawtext);
		JAMWikiSpliceProcessor lexer = new JAMWikiSpliceProcessor(raw);
		lexer.setParserInput(this.parserInput);
		lexer.setTargetSection(targetSection);
		ParserOutput parserOutput = this.lex(lexer);
		logger.debug("Parse time (parseSlice) for " + topicName + "(" + ((System.currentTimeMillis() - start) / 1000.000) + " s.)");
		return parserOutput;
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
	 * @param rawtext The raw Wiki syntax from which a section is to be replaced.
	 * @param topicName The name of the topic that is being parsed.
	 * @param targetSection The section of the document to be replaced (first section is 1).
	 * @param replacementText The text to replace the specified section text with.
	 * @return The new topic markup, contained within a ParserOutput object.
	 */
	public ParserOutput parseSplice(String rawtext, String topicName, int targetSection, String replacementText) throws Exception {
		long start = System.currentTimeMillis();
		StringReader raw = new StringReader(rawtext);
		JAMWikiSpliceProcessor lexer = new JAMWikiSpliceProcessor(raw);
		lexer.setParserInput(this.parserInput);
		lexer.setReplacementText(replacementText);
		lexer.setTargetSection(targetSection);
		ParserOutput parserOutput = this.lex(lexer);
		logger.debug("Parse time (parseSplice) for " + topicName + "(" + ((System.currentTimeMillis() - start) / 1000.000) + " s.)");
		return parserOutput;
	}
}
