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
package org.jamwiki.servlets;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.jamwiki.DataAccessException;
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.WikiMessage;
import org.jamwiki.model.Namespace;
import org.jamwiki.model.VirtualWiki;
import org.jamwiki.utils.LinkUtil;
import org.jamwiki.utils.WikiLink;
import org.jamwiki.utils.WikiLogger;
import org.jamwiki.utils.WikiUtil;

/**
 * The <code>WikiPageInfo</code> class provides an object containing common
 * data used for generating wiki page display.
 */
public class WikiPageInfo {

	private static final WikiLogger logger = WikiLogger.getLogger(WikiPageInfo.class.getName());
	/** The name of the JSP file used to render the servlet output. */
	protected static final String JSP_TOPIC = "topic.jsp";
	private boolean admin = false;
	private String canonicalUrl = null;
	private String contentJsp = JSP_TOPIC;
	private WikiMessage pageTitle = null;
	private WikiMessage exception = null;
	private String redirectName = null;
	private String redirectUrl = null;
	private String selectedTab = null;
	private boolean special = false;
	private LinkedHashMap<String, WikiMessage> tabMenu = new LinkedHashMap<String, WikiMessage>();
	private String topicName = "";
	private LinkedHashMap<String, WikiMessage> userMenu = new LinkedHashMap<String, WikiMessage>();
	private String virtualWikiName = null;

	/**
	 *
	 */
	protected WikiPageInfo(HttpServletRequest request) {
		this.virtualWikiName = WikiUtil.getVirtualWikiFromURI(request);
		if (this.virtualWikiName == null) {
			logger.error("No virtual wiki available for page request " + request.getRequestURI());
			this.virtualWikiName = VirtualWiki.defaultVirtualWiki().getName();
		}
	}

	/**
	 * Reset all parameters of the current <code>WikiPageInfo</code> object
	 * to default values.
	 */
	protected void reset() {
		this.admin = false;
		this.contentJsp = JSP_TOPIC;
		this.exception = null;
		this.pageTitle = null;
		this.redirectName = null;
		this.selectedTab = null;
		this.special = false;
		this.tabMenu = new LinkedHashMap<String, WikiMessage>();
		this.topicName = "";
		this.userMenu = new LinkedHashMap<String, WikiMessage>();
	}

	/**
	 * If a page is a part of the admin tool then this method will return
	 * <code>true</code>.
	 *
	 * @return <code>true</code> if a page is part of the admin tool,
	 *  <code>false</code> otherwise.
	 */
	public boolean getAdmin() {
		return this.admin;
	}

	/**
	 * Set a flag indicating whether or not the page being displayed is a part
	 * of the admin tool.
	 *
	 * @param admin <code>true</code> if a page is part of the admin tool,
	 *  <code>false</code> otherwise.
	 */
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	/**
	 * The canonical URL is used with search engines to indicate the primary
	 * URL for a topic.  For redirects and shared images this is the target
	 * URL.
	 *
	 * @return The canonical URL is used with search engines to indicate the primary
	 * URL for a topic.
	 */
	public String getCanonicalUrl() {
		return this.canonicalUrl;
	}

	/**
	 * The canonical URL is used with search engines to indicate the primary
	 * URL for a topic.  For redirects and shared images this is the target
	 * URL.
	 *
	 * @param canonicalUrl The canonical URL is used with search engines to indicate
	 * the primary URL for a topic.
	 */
	public void setCanonicalUrl(String canonicalUrl) {
		this.canonicalUrl = canonicalUrl;
	}

	/**
	 * Retrieve the name of the JSP page that will be used to display the
	 * results of this page request.
	 *
	 * @return The name of the JSP page that will be used to display the
	 *  results of the page request.
	 */
	public String getContentJsp() {
		return this.contentJsp;
	}

	/**
	 * Set the JSP page that will display the results of this page request.
	 * If no value is specified then the default is to display the request
	 * using the topic display JSP.
	 *
	 * @param contentJsp The JSP page that should be used to display the
	 *  results of the page request.
	 */
	public void setContentJsp(String contentJsp) {
		this.contentJsp = contentJsp;
	}

	/**
	 * Return the property representing the date pattern to use for dates
	 * that include date and time.
	 *
	 * @return The property representing the date pattern to use for dates
	 * that include date and time.
	 */
	public String getDatePatternDateAndTime() {
		return Environment.getDatePatternValue(Environment.PROP_DATE_PATTERN_DATE_AND_TIME, true, true);
	}

	/**
	 * Return the property representing the date pattern to use for date-only
	 * dates.
	 *
	 * @return The property representing the date pattern to use for date-only
	 * dates.
	 */
	public String getDatePatternDateOnly() {
		return Environment.getDatePatternValue(Environment.PROP_DATE_PATTERN_DATE_ONLY, true, false);
	}

	/**
	 * Return the property representing the date pattern to use for time-only
	 * dates.
	 *
	 * @return The property representing the date pattern to use for time-only
	 * dates.
	 */
	public String getDatePatternTimeOnly() {
		return Environment.getDatePatternValue(Environment.PROP_DATE_PATTERN_TIME_ONLY, false, true);
	}

	/**
	 * If a fatal error occurs it will be stored for display on the front
	 * end.
	 *
	 * @return The WikiMessage object representing the error.
	 */
	public WikiMessage getException() {
		return this.exception;
	}

	/**
	 * If a fatal error occurs it will be stored for display on the front
	 * end.
	 *
	 * @param exception The WikiMessage object representing the error.
	 */
	public void setException(WikiMessage exception) {
		this.exception = exception;
	}

	/**
	 * Return a description for the current page that can be used in an HTML
	 * meta tag.
	 *
	 * @return A description for the current page that can be used in an HTML
	 *  meta tag.
	 */
	public String getMetaDescription() {
		String pattern = this.getVirtualWiki().getMetaDescription();
		if (StringUtils.isBlank(pattern)) {
			return "";
		}
		MessageFormat formatter = new MessageFormat(pattern);
		Object params[] = new Object[1];
		params[0] = (this.topicName == null) ? "" : this.topicName;
		return formatter.format(params);
	}

	/**
	 * Return a map whose keys are virtual wikis, and whose values is a mapping of
	 * namespace id and value for the virtual wiki.
	 */
	public Map<String, Map<String, String>> getNamespaces() throws DataAccessException {
		Map<String, Map<String, String>> results = new HashMap<String, Map<String, String>>();
		List<Namespace> namespaces = WikiBase.getDataHandler().lookupNamespaces();
		List<VirtualWiki> virtualWikis = WikiBase.getDataHandler().getVirtualWikiList();
		for (VirtualWiki virtualWiki : virtualWikis) {
			Map<String, String> namespaceMap = new HashMap<String, String>();
			for (Namespace namespace : namespaces) {
				namespaceMap.put(namespace.getDefaultLabel(), namespace.getLabel(virtualWiki.getName()));
			}
			results.put(virtualWiki.getName(), namespaceMap);
		}
		return results;
	}

	/**
	 * Return the title for the current page.
	 *
	 * @return The title for the current page.
	 */
	public WikiMessage getPageTitle() {
		return this.pageTitle;
	}

	/**
	 * Set the title for the current page.
	 *
	 * @param pageTitle A <code>WikiMessage</code> object that contains a
	 *  translatable page title value.
	 */
	public void setPageTitle(WikiMessage pageTitle) {
		this.pageTitle = pageTitle;
	}

	/**
	 * If printable pages should open in a new window then this method will
	 * return the HTML target "_blank", otherwise this method returns an
	 * empty String.
	 *
	 * @return The HTML target "_blank" if printable pages should open in a
	 *  new window, otherwise an empty String.
	 */
	public String getPrintTarget() {
		return (Environment.getBooleanValue(Environment.PROP_PRINT_NEW_WINDOW)) ? "_blank" : "";
	}

	/**
	 * If the topic currently being displayed is the result of a redirect from
	 * another topic, return the name of the topic that is being redirected
	 * from.
	 *
	 * @return The name of the topic being redirected from, or
	 *  <code>null</code> if the current page is not the result of a redirect.
	 */
	public String getRedirectName() {
		return this.redirectName;
	}

	/**
	 * If the topic currently being displayed is the result of a redirect from
	 * another topic, return the full (relative) URL back to the redirection
	 * topic.
	 *
	 * @return The full (relative) URL of the topic being redirected from, or
	 *  <code>null</code> if the current page is not the result of a redirect.
	 */
	public String getRedirectUrl() {
		return this.redirectUrl;
	}

	/**
	 * If the topic currently being displayed is the result of a redirect from
	 * another topic, set the name and full (relative) URL of the topic that is
	 * being redirected from.
	 *
	 * @param redirectUrl The full (relative) URL of the topic being redirected
	 *  from, or <code>null</code> if the current page is not the result of a redirect.
	 * @param redirectName The name of the topic being redirected from, or
	 *  <code>null</code> if the current page is not the result of a redirect.
	 */
	public void setRedirectInfo(String redirectUrl, String redirectName) {
		this.redirectName = redirectName;
		this.redirectUrl = redirectUrl;
	}

	/**
	 * Return the base title used with RSS feeds.
	 *
	 * @return The base title used with RSS feeds.
	 */
	public String getRSSTitle() {
		return Environment.getValue(Environment.PROP_RSS_TITLE);
	}

	/**
	 * Return the page topic (example: "Special:Admin") for the page that is
	 * currently active.
	 *
	 * @return The page topic (example: "Special:Admin") for the page that is
	 * currently active.
	 */
	public String getSelectedTab() {
		return this.selectedTab;
	}

	/**
	 * Set the page topic (example: "Special:Admin") for the page that is
	 * currently active.
	 *
	 * @param selectedTab The page topic (example: "Special:Admin") for the
	 *  page that is currently active.
	 */
	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

	/**
	 * Return the property value set for the site name.  This value is appended to
	 * page titles and used in XML exports.
	 *
	 * @return The property value corresponding to the configured name for the
	 *  wiki.  This value is configurable through the Special:Admin interface.
	 */
	public String getSiteName() {
		return this.getVirtualWiki().getSiteName();
	}

	/**
	 * Return a flag indicating whether or not the current page is a "Special:"
	 * page, as opposed to a standard topic.
	 *
	 * @return <code>true</code> if the current page is a "Special:" page,
	 *  <code>false</code> otherwise.
	 */
	public boolean getSpecial() {
		return this.special;
	}

	/**
	 * Set a flag indicating whether or not the current page is a "Special:"
	 * page, as opposed to a standard topic.
	 *
	 * @param special Set to <code>true</code> if the current page is a
	 *  "Special:" page, <code>false</code> otherwise.
	 */
	public void setSpecial(boolean special) {
		this.special = special;
	}

	/**
	 * Return a LinkedHashMap containing the topic and text for all links
	 * that should appear for the tab menu.
	 *
	 * @return A LinkedHashMap containing the topic and text for all links
	 *  that should appear for the tab menu.
	 */
	public LinkedHashMap<String, WikiMessage> getTabMenu() {
		return this.tabMenu;
	}

	/**
	 * Set a LinkedHashMap containing the topic and text for all links
	 * that should appear for the tab menu.
	 *
	 * @param tabMenu A LinkedHashMap containing the topic and text for all
	 *  links that should appear for the tab menu.
	 */
	public void setTabMenu(LinkedHashMap<String, WikiMessage> tabMenu) {
		this.tabMenu = tabMenu;
	}

	/**
	 * Return the name of the topic being displayed by the current page.
	 *
	 * @return The name of the topic being displayed by the current page.
	 */
	public String getTopicName() {
		return this.topicName;
	}

	/**
	 * Utility method for returning a URL encoded version of the current
	 * topic name.
	 *
	 * @return A URL encoded version of the name of the topic being displayed
	 *  by the current page.
	 */
	public String getTopicNameUrlEncoded() throws UnsupportedEncodingException {
		return (this.topicName != null) ? URLEncoder.encode(this.topicName, "UTF-8") : null;
	}

	/**
	 * Set the name of the topic being displayed by the current page.
	 *
	 * @param topicName The name of the topic being displayed by the current
	 *  page.
	 */
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	/**
	 * Return a LinkedHashMap containing the topic and text for all links
	 * that should appear for the user menu.
	 *
	 * @return A LinkedHashMap containing the topic and text for all links
	 *  that should appear for the user menu.
	 */
	public LinkedHashMap<String, WikiMessage> getUserMenu() {
		return this.userMenu;
	}

	/**
	 * Set a LinkedHashMap containing the topic and text for all links
	 * that should appear for the user menu.
	 *
	 * @param userMenu A LinkedHashMap containing the topic and text for all
	 *  links that should appear for the user menu.
	 */
	public void setUserMenu(LinkedHashMap<String, WikiMessage> userMenu) {
		this.userMenu = userMenu;
	}

	/**
	 * Utility method for retrieving a VirtualWiki object given the virtual wiki
	 * name.
	 */
	public VirtualWiki getVirtualWiki() {
		VirtualWiki virtualWiki = VirtualWiki.defaultVirtualWiki();
		try {
			virtualWiki = WikiBase.getDataHandler().lookupVirtualWiki(this.getVirtualWikiName());
		} catch (DataAccessException e) {
			logger.error("Failure while retrieving virtual wiki: " + this.getVirtualWikiName(), e);
		}
		return virtualWiki;
	}

	/**
	 * Return the name of the virtual wiki associated with the page info being
	 * created.  This will normally be taken directly from the request and default
	 * to the wiki default virtual wiki, although in rare cases (such as redirects
	 * to other virtual wikis) it may differ.
	 */
	public String getVirtualWikiName() {
		return this.virtualWikiName;
	}

	/**
	 * Return the name of the virtual wiki associated with the page info being
	 * created.  This will normally be taken directly from the request and default
	 * to the wiki default virtual wiki, although in rare cases (such as redirects
	 * to other virtual wikis) it may differ.
	 *
	 * @param virtualWikiName The name of the virtual wiki to set.
	 */
	public void setVirtualWikiName(String virtualWikiName) {
		if (StringUtils.isBlank(virtualWikiName)) {
			throw new IllegalArgumentException("Cannot pass a null or empty virtual wiki name");
		}
		this.virtualWikiName = virtualWikiName;
	}

	/**
	 * If the page currently being viewed is a user page or a user comments
	 * page return <code>true</code>
	 *
	 * @return <code>true</code> if the page currently being viewed is a
	 *  user page, otherwise <code>false</code>.
	 */
	public boolean isUserPage() {
		WikiLink wikiLink = LinkUtil.parseWikiLink(this.virtualWikiName, this.getTopicName());
		return (wikiLink.getNamespace().getId().equals(Namespace.USER_ID) || wikiLink.getNamespace().getId().equals(Namespace.USER_COMMENTS_ID));
	}
}
