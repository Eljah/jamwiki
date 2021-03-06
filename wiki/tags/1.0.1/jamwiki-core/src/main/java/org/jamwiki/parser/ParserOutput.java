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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * This class represents the output from the JAMWiki parser.  It holds parsed
 * output text as well as metadata that is generated by the parser.
 */
public class ParserOutput implements Serializable {

	private boolean cacheable = true;
	private final LinkedHashMap<String, String> categories = new LinkedHashMap<String, String>();
	private final List<String> interwikiLinks = new ArrayList<String>();
	private final List<String> links = new ArrayList<String>();
	private final List<String> virtualWikiLinks = new ArrayList<String>();
	private String pageTitle = null;
	private String redirect = null;
	private String sectionName = null;
	private final List<String> templates = new ArrayList<String>();

	/**
	 *
	 */
	public ParserOutput() {
	}

	/**
	 * When a document contains a token indicating that the document belongs
	 * to a specific category this method should be called to add that
	 * category to the output metadata.
	 *
	 * @param categoryName The name of the category that the document belongs
	 *  to.
	 * @param sortKey The sort key for the category, or <code>null</code> if
	 *  no sort key has been specified.  The sort key determines what order
	 *  categories are sorted on category index pages, so a category for
	 *  "John Doe" might be given a sort key of "Doe, John".
	 */
	public void addCategory(String categoryName, String sortKey) {
		this.categories.put(categoryName, sortKey);
	}

	/**
	 * Add a fully-formatted interwiki link to the list of available
	 * interwiki links for a document.  These links can then be rendered
	 * separately from the document, for example in an "Other Sites"
	 * toolbox.
	 *
	 * @param interwikiLink The fully-formatted HTML interwiki link.
	 */
	public void addInterwikiLink(String interwikiLink) {
		if (!this.interwikiLinks.contains(interwikiLink)) {
			this.interwikiLinks.add(interwikiLink);
		}
	}

	/**
	 * When a document contains a token indicating that the document links
	 * to another Wiki topic this method should be called to add that
	 * topic link to the output metadata.
	 *
	 * @param topicName The name of the topic that is linked to.
	 */
	public void addLink(String topicName) {
		this.links.add(topicName);
	}

	/**
	 * When a document contains a token indicating that the document includes
	 * a Wiki template this method should be called to add that template
	 * to the output metadata.
	 *
	 * @param template The name of the template that is being included.
	 */
	public void addTemplate(String template) {
		this.templates.add(template);
	}

	/**
	 * Add a fully-formatted virtual wiki link to the list of available
	 * virtual wiki links for a document.  These links can then be rendered
	 * separately from the document, for example in an "Other Languages"
	 * toolbox.
	 *
	 * @param virtualWikiLink The fully-formatted HTML interwiki link.
	 */
	public void addVirtualWikiLink(String virtualWikiLink) {
		if (!this.virtualWikiLinks.contains(virtualWikiLink)) {
			this.virtualWikiLinks.add(virtualWikiLink);
		}
	}

	/**
	 * Return a flag indicating whether or not the current ParserOutput
	 * object can be cached.  If the document contains user-specific,
	 * time-specific or other non-cacheable content then this method should
	 * return <code>false</code>.
	 *
	 * @return <code>true</code> if the current ParserOutput is cacheable,
	 *  <code>false</code> if it contains any non-cacheable content.
	 */
	public boolean getCacheable() {
		return this.cacheable;
	}

	/**
	 * Sets a flag indicating whether or not the current ParserOutput
	 * object can be cached.  If the document contains user-specific,
	 * time-specific or other non-cacheable content then the cacheable flag
	 * should be set to <code>false</code>.
	 *
	 * @param cacheable Set to <code>true</code> if the current ParserOutput
	 *  is cacheable, <code>false</code> if it contains any non-cacheable
	 *  content.
	 */
	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}

	/**
	 * Return the current mapping of categories associated with the document
	 * being parsed.  The mapping contains key-value pairs with the category
	 * name as the key and the sort key (if any) as the value.
	 *
	 * @return A mapping of categories and their associated sort keys (if any)
	 *  for all categories that are associated with the document being parsed.
	 */
	public LinkedHashMap<String, String> getCategories() {
		return this.categories;
	}

	/**
	 * For the document being parsed, return the current list of interwiki
	 * links for all interwiki links specified for the current document.
	 *
	 * @return A list of all interwiki links for all interwiki links
	 *  specified for the current document.
	 */
	public List<String> getInterwikiLinks() {
		return this.interwikiLinks;
	}

	/**
	 * For the document being parsed, return the current list of topic
	 * names for all topics that are linked to from the current document.
	 *
	 * @return A list of all topic names that are linked to from the
	 *  current document.
	 */
	public List<String> getLinks() {
		return this.links;
	}

	/**
	 * If a parser element supports setting an alternate page title then this
	 * field provides a way to do so.  If no alternate page title is specified
	 * then this method should return <code>null</code>.
	 *
	 * @return An alternate page title as indicated by a parsing element, or
	 *  <code>null</code> if no alternate page title is specified.
	 */
	public String getPageTitle() {
		return this.pageTitle;
	}

	/**
	 * If a parser element supports setting an alternate page title then this
	 * field provides a way to do so.  If no alternate page title is specified
	 * then this method should return <code>null</code>.
	 *
	 * @param pageTitle An alternate page title as indicated by a parsing
	 *  element, or <code>null</code> if no alternate page title is in use.
	 */
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	/**
	 * When editing or parsing a section of a document, get the name of
	 * the heading for that section.
	 *
	 * @return The name of the heading for a section of a document being
	 *  parsed, or <code>null</code> if a section is not being parsed. If not
	 *  <code>null</code> then the section name should be encoded for use in a
	 *  URL.
	 */
	public String getSectionName() {
		return this.sectionName;
	}

	/**
	 * When editing or parsing a section of a document, set the name of
	 * the heading for that section.
	 *
	 * @param sectionName The name of the heading for a section of a document
	 *  being parsed, or <code>null</code> if a section is not being parsed.
	 *  If not <code>null</code> then the section name should be encoded for
	 *  use in a URL.
	 */
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	/**
	 * For the document being parsed, return the current list of
	 * templates names for all templates that are included in the current
	 * document.
	 *
	 * @return A list of all template names that are included in the
	 *  current document.
	 */
	public List<String> getTemplates() {
		return this.templates;
	}

	/**
	 * If a document being parsed represents a redirect, return the name of
	 * the topic that this document redirects to.
	 *
	 * @return The name of the topic that this document redirects to, or
	 *  <code>null</code> if the document does not represent a redirect.
	 */
	public String getRedirect() {
		return this.redirect;
	}

	/**
	 * If a document being parsed represents a redirect, set the name of
	 * the topic that this document redirects to.
	 *
	 * @param redirect The name of the topic that this document redirects to,
	 *  or <code>null</code> if the document does not represent a redirect.
	 */
	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	/**
	 * For the document being parsed, return the current list of virtual wiki
	 * links for all virtual wiki links specified for the current document.
	 *
	 * @return A list of all virtual wiki links for all virtual wiki links
	 *  specified for the current document.
	 */
	public List<String> getVirtualWikiLinks() {
		return this.virtualWikiLinks;
	}
}
