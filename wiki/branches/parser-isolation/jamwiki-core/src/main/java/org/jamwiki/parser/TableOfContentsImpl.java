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

import java.util.Enumeration;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jamwiki.Environment;
import org.jamwiki.utils.WikiLogger;
import org.jamwiki.utils.Utilities;
import org.springframework.web.util.HtmlUtils;

/**
 * This class may be used in two ways:
 * <ol>
 * <li>The static addTableOfContents(String) method may be called to automatically
 * add a table of contents on the right side of an article.  This method
 * works with all lexers, because it parses the HTML for headers. However it
 * doesn't care where it is. So if you have a header on the LeftMenu or
 * BottomArea, it will also add a TOC there...</li>
 *
 * <li>The static addTableOfContents(TableOfContents, StringBuffer) method
 * may be called to insert a pre-built TableOfContents object into an
 * article.  This method requires that the parser has added all table of
 * contents headings to the object.  It is a bit more flexible but requires
 * more preperatory work.</li>
 * </ol>
 */
public class TableOfContentsImpl implements TableOfContents {

	private static final WikiLogger logger = WikiLogger.getLogger(TableOfContentsImpl.class.getName());
	private int currentLevel = 0;
	/** Force a TOC to appear */
	private boolean forceTOC = false;
	/** It is possible for a user to include more than one "TOC" tag in a document, so keep count. */
	private int insertTagCount = 0;
	/** Keep track of how many times the parser attempts to insert the TOC (one per "TOC" tag) */
	private int insertionAttempt = 0;
	private int minLevel = 4;
	private final Vector entries = new Vector();
	private int status = STATUS_TOC_UNINITIALIZED;
	/** The minimum number of headings that must be present for a TOC to appear, unless forceTOC is set to true. */
	private static final int MINIMUM_HEADINGS = 4;

	/**
	 * Adds TOC at the beginning as a table on the right side of the page if the
	 * page has any HTML-headers.
	 *
	 * @param text The parsed content into which a table of contents is to be added.
	 * @return The parsed content with a table of contents included in it.
	 */
	public static String addTableOfContents(String text) {
		logger.fine("Start TOC generating...");
		Pattern p = Pattern.compile("<[Hh][123][^>]*>(.*)</[Hh][123][^>]*>");
		Matcher m = p.matcher(text);
		StringBuffer result = new StringBuffer();
		StringBuffer toc = new StringBuffer();
		toc.append("<table align=\"right\" class=\"toc\"><tr><td>");
		int position = 0;
		while (m.find()) {
			result.append(text.substring(position, m.start(1)));
			position = m.start(1);
			result.append("<a class=\"tocheader\" name=\"" + position
					+ "\" id=\"" + position + "\"></a>");
			if (m.group().startsWith("<h1") || m.group().startsWith("<H1")) {
				toc.append("<span class=\"tocheader1\">");
			} else if (m.group().startsWith("<h2") || m.group().startsWith("<H2")) {
				toc.append("<span class=\"tocheader2\">");
			} else {
				toc.append("<span class=\"tocheader3\">");
			}
			toc.append("<li><a href=\"#" + position + "\">" + m.group(1)
					+ "</a></li></span>");
			result.append(text.substring(position, m.end(1)));
			position = m.end(1);
			logger.fine("Adding content: " + m.group(1));
		}
		toc.append("</td></tr></table>");
		result.append(text.substring(position));
		if (position > 0) {
			logger.fine("adding TOC at the beginning!");
			toc.append(result);
		} else {
			toc = result;
		}
		return toc.toString();
	}

	/* (non-Javadoc)
	 * @see org.jamwiki.parser.TableOfContents#addEntry(java.lang.String, java.lang.String, int)
	 */
	public void addEntry(String name, String text, int level) {
		if (this.status != STATUS_NO_TOC && this.status != STATUS_TOC_INITIALIZED) {
			this.setStatus(STATUS_TOC_INITIALIZED);
		}
		TableOfContentsEntry entry = new TableOfContentsEntry(name, text, level);
		entries.add(entry);
		if (level < minLevel) {
			minLevel = level;
		}
	}

	/* (non-Javadoc)
	 * @see org.jamwiki.parser.TableOfContents#attemptTOCInsertion()
	 */
	public String attemptTOCInsertion() {
		this.insertionAttempt++;
		if (this.size() == 0 || (this.size() < MINIMUM_HEADINGS && !this.forceTOC)) {
			// too few headings
			return "";
		}
		if (this.getStatus() == TableOfContents.STATUS_NO_TOC) {
			// TOC disallowed
			return "";
		}
		if (!Environment.getBooleanValue(Environment.PROP_PARSER_TOC)) {
			// TOC turned off for the wiki
			return "";
		}
		if (this.insertionAttempt < this.insertTagCount) {
			// user specified a TOC location, only insert there
			return "";
		}
		return this.toHTML();
	}

	/**
	 * Internal method to close any list tags prior to adding the next entry.
	 */
	private void closeList(int level, StringBuffer text) {
		while (level < currentLevel) {
			// close lists to current level
			text.append("</li></ol>");
			currentLevel--;
		}
	}

	/* (non-Javadoc)
	 * @see org.jamwiki.parser.TableOfContents#getStatus()
	 */
	public int getStatus() {
		return this.status;
	}

	/**
	 * Internal method to open any list tags prior to adding the next entry.
	 */
	private void openList(int level, StringBuffer text) {
		if (level == currentLevel) {
			// same level as previous item, close previous and open new
			text.append("</li><li>");
			return;
		}
		while (level > currentLevel) {
			// open lists to current level
			text.append("<ol><li>");
			currentLevel++;
		}
	}

	/* (non-Javadoc)
	 * @see org.jamwiki.parser.TableOfContents#setForceTOC(boolean)
	 */
	public void setForceTOC(boolean forceTOC) {
		this.forceTOC = forceTOC;
	}

	/* (non-Javadoc)
	 * @see org.jamwiki.parser.TableOfContents#setStatus(int)
	 */
	public void setStatus(int status) {
		if (status == STATUS_TOC_INITIALIZED) {
			// keep track of how many TOC insertion tags are present
			this.insertTagCount++;
		}
		this.status = status;
	}

	/* (non-Javadoc)
	 * @see org.jamwiki.parser.TableOfContents#size()
	 */
	public int size() {
		return this.entries.size();
	}

	/* (non-Javadoc)
	 * @see org.jamwiki.parser.TableOfContents#toHTML()
	 */
	public String toHTML() {
		Enumeration e = entries.elements();
		StringBuffer text = new StringBuffer();
		text.append("<table class=\"toc\"><tr><td>");
		TableOfContentsEntry entry = null;
		int adjustedLevel = 0;
		while (e.hasMoreElements()) {
			entry = (TableOfContentsEntry)e.nextElement();
			// adjusted level determines how far to indent the list
			adjustedLevel = ((entry.level - minLevel) + 1);
			if (adjustedLevel > Environment.getIntValue(Environment.PROP_PARSER_TOC_DEPTH)) {
				// do not display if nested deeper than max
				continue;
			}
			closeList(adjustedLevel, text);
			openList(adjustedLevel, text);
			text.append("<a href=\"#").append(Utilities.encodeForURL(entry.name)).append("\">").append(HtmlUtils.htmlEscape(entry.text)).append("</a>");
		}
		closeList(0, text);
		text.append("</td></tr></table>");
		return text.toString();
	}

	/**
	 * Inner class holds TOC entries until they can be processed for display.
	 */
	class TableOfContentsEntry {

		int level;
		String name;
		String text;

		/**
		 *
		 */
		TableOfContentsEntry(String name, String text, int level) {
			this.name = name;
			this.text = text;
			this.level = level;
		}
	}
}
