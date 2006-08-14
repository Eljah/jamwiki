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

import java.util.Stack;
import org.apache.log4j.Logger;

/**
 * This interface can be implemented in any way you like, it doesn't have to be for
 * a JLex generated lexer. As long as the implementing class implements a constructor
 * that takes a single InputStream or Reader parameter and returns one token at a
 * time from the yylex() method, it will work.
 */
public abstract class AbstractLexer {

	private static final Logger logger = Logger.getLogger(AbstractLexer.class);

	/** Member variable used to keep track of the state history for the lexer. */
	protected Stack states = new Stack();
	/** Parser configuration information. */
	protected ParserInfo parserInfo = null;

	/**
	 * Begin a new state and store the old state onto the stack.
	 */
	protected void beginState(int state) {
		// store current state
		Integer current = new Integer(yystate());
		states.push(current);
		// switch to new state
		yybegin(state);
	}

	/**
	 * End processing of a state and switch to the previous state.
	 */
	protected void endState() {
		// revert to previous state
		if (states.empty()) {
			logger.warn("Attempt to call endState for an empty stack with text: " + yytext());
			return;
		}
		int next = ((Integer)states.pop()).intValue();
		yybegin(next);
	}

	/**
	 * Set the parser settings.  This method should also validate that
	 * all settings required for the parser have been set, and if not it
	 * should throw an exception.
	 */
	public abstract void setParserInfo(ParserInfo parserInfo) throws Exception;

	/**
	 *
	 */
	public abstract void yybegin(int newState);

	/**
	 *
	 */
	public abstract String yylex() throws Exception;

	/**
	 *
	 */
	public abstract int yystate();

	/**
	 *
	 */
	public abstract String yytext();
}
