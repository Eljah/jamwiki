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
 * along with this program (gpl.txt); if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.jamwiki.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import org.apache.log4j.Logger;

import org.jamwiki.parser.ParserInfo;

/**
 * Abstract class to be used when implementing new lexers.  New lexers
 * should extend this class and override any methods that need to be
 * implemented differently.
 */
public abstract class AbstractParser {

	private static final Logger logger = Logger.getLogger(AbstractParser.class);
	private ParserInfo parserInfo;

	/**
	 * Sets the basics for this parser.
	 *
	 * @param parserInfo General information about this parser.
	 */
	protected AbstractParser(ParserInfo parserInfo) {
		this.parserInfo = parserInfo;
	}

	/**
	 * For getting general information about this parser.
	 *
	 * @return General information about this parser.
	 */
	public ParserInfo getParserInfo() {
		return parserInfo;
	}

	/**
	 * Returns a HTML representation of the given wiki raw text for online representation.
	 *
	 * @param raw The raw Wiki syntax to be converted into HTML.
	 * @param virtualwiki A virtual wiki prefix (if any).
	 * @return HTML representation of the text for online.
	 */
	public abstract String parseHTML(String context, String virtualWiki, String raw) throws IOException;
}