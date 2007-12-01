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

import java.util.Stack;
import org.jamwiki.parser.ParserDocument;
import org.jamwiki.parser.ParserInput;
import org.jamwiki.parser.ParserTag;
import org.jamwiki.utils.WikiLogger;

/**
 * Abstract class that is extended by the JFlex lexers.  This class primarily
 * contains utility methods useful during parsing.
 */
public abstract class AbstractLexer {

	private static final WikiLogger logger = WikiLogger.getLogger(AbstractLexer.class.getName());

	/** Member variable used to keep track of the state history for the lexer. */
	protected Stack states = new Stack();
	/** Parser configuration information. */
	protected ParserInput parserInput = null;
	/** Parser parsing results. */
	protected ParserDocument parserDocument = null;
	/** Parser mode, which provides input to the parser about what steps to take. */
	protected int mode = JFlexParser.MODE_LAYOUT;

	/**
	 * Begin a new parser state and store the old state onto the stack.
	 *
	 * @param state The new parsing state that is being entered.
	 */
	protected void beginState(int state) {
		// store current state
		Integer current = new Integer(yystate());
		states.push(current);
		// switch to new state
		yybegin(state);
	}

	/**
	 * End processing of a parser state and switch to the previous parser state.
	 */
	protected void endState() {
		// revert to previous state
		if (states.empty()) {
			logger.warning("Attempt to call endState for an empty stack with text: " + yytext());
			return;
		}
		int next = ((Integer)states.pop()).intValue();
		yybegin(next);
	}

	/**
	 * This method is used to set the ParserDocument field, which is used to retrieve
	 * parsed information from the parser.
	 *
	 * @return Parsed information generated by the parser
	 */
	public ParserDocument getParserDocument() {
		return this.parserDocument;
	}

	/**
	 * Initialize the parser settings.  This functionality should be done
	 * from the constructor, but since JFlex generates code it is not possible
	 * to modify the constructor parameters.
	 *
	 * @param parserInput The ParserInput object containing parser parameters
	 *  required for successful parsing.
	 * @param parserDocument The current parsed document.  When parsing is done
	 *  in multiple stages that output values are also built in stages.
	 * @param mode The parser mode to use when parsing.  Mode affects what
	 *  type of parsing actions are taken when processing raw text.
	 */
	public final void init(ParserInput parserInput, ParserDocument parserDocument, int mode) {
		this.parserInput = parserInput;
		this.parserDocument = parserDocument;
		this.mode = mode;
	}

	/**
	 * Parse a token using the specified parser tag handler.  If an error
	 * occurs during processing then this method will return the raw text
	 * that was passed to it.
	 *
	 * @param raw The raw token text that is to be parsed.
	 * @param parserTag The parser tag handler to use when parsing the token.
	 * @return Returns the parsed text, or if an error occurs returns the raw
	 *  text that was passed to this method.
	 */
	protected String parseToken(String raw, ParserTag parserTag) {
		try {
			return parserTag.parse(this.parserInput, this.parserDocument, this.mode, raw);
		} catch (Throwable t) {
			logger.info("Unable to parse " + raw, t);
			return raw;
		}
	}

	/**
	 * JFlex internal method used to change the lexer state values.
	 */
	public abstract void yybegin(int newState);

	/**
	 * JFlex internal method used to parse the next token.
	 */
	public abstract String yylex() throws Exception;

	/**
	 * JFlex internal method used to retrieve the current lexer state value.
	 */
	public abstract int yystate();

	/**
	 * JFlex internal method used to retrieve the current text matched by the
	 * yylex() method.
	 */
	public abstract String yytext();
}