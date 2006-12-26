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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.model.RecentChange;
import org.jamwiki.utils.Pagination;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiLogger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.SyndFeedOutput;

/**
 * Provides an RSS or Atom feed for recent changes.
 *
 * Feed generation can be influenced through following request parameters:
 * <ul>
 * <li><code>feedType</code>: RSS or Atom (see
 * {@link #setDefaultFeedType(String)}) </li>
 * <li><code>minorEdits</code>: set to 'true' to include minor edits </li>
 * <li><code>num</code>: number of entries to return (see
 * {@link Utilities#buildPagination(HttpServletRequest, ModelAndView)})</li>
 * </ul>
 *
 * @author Rainer Schmitz
 * @since 22.12.2006
 */
public class RecentChangesFeedServlet extends AbstractController {

	private static final WikiLogger logger = WikiLogger.getLogger(RecentChangesFeedServlet.class.getName());
	private static final String MIME_TYPE = "application/xml; charset=UTF-8";
	private static final String FEED_ENCODING = "UTF-8";
	private static final String DEFAULT_FEED_TYPE = "rss_2.0";

	private String defaultFeedType = DEFAULT_FEED_TYPE;
	private boolean defaultIncludeMinorEdits = false;

	/**
	 * Sets the default feed type.
	 *
	 * Valid values are rss_0.92, rss_0.93, rss_0.94, rss_1.0, rss_2.0,
	 * atom_0.3, and atom_1.0.
	 *
	 * This value is used if no feed type is given in the request (parameter
	 * 'feedType'). Default is rss_2.0.
	 *
	 * @param defaultFeedType The defaultFeedType to set.
	 */
	public void setDefaultFeedType(String defaultFeedType) {
		this.defaultFeedType = defaultFeedType;
	}

	/**
	 * Sets wether minor edits are included in generated feed.
	 *
	 * This value can be overriden by the request parameter 'minorEdits'.
	 * Default is false.
	 *
	 * @param includeMinorEdits <code>true</code> if minor edits shall be
	 *  included in feed.
	 */
	public void setDefaultIncludeMinorEdits(boolean includeMinorEdits) {
		this.defaultIncludeMinorEdits = includeMinorEdits;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *	  javax.servlet.http.HttpServletResponse)
	 */
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String feedType = ServletRequestUtils.getStringParameter(request, "feedType", defaultFeedType);
			logger.fine("Serving xml feed of type " + feedType);
			SyndFeed feed = getFeed(request);
			feed.setFeedType(feedType);
			response.setContentType(MIME_TYPE);
			SyndFeedOutput output = new SyndFeedOutput();
			output.output(feed, response.getWriter());
		} catch (Exception e) {
			logger.severe("Could not generate feed: " + e.getMessage(), e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not generate feed: " + e.getMessage());
		}
		return null;
	}

	/**
	 * TODO cache feed to avoid high load caused by RSS aggregators
	 *
	 * @throws Exception
	 */
	private SyndFeed getFeed(HttpServletRequest request) throws Exception {
		Collection changes = getChanges(request);
		SyndFeed feed = new SyndFeedImpl();
		feed.setEncoding(FEED_ENCODING);
		feed.setTitle(Environment.getValue(Environment.PROP_RSS_TITLE));
		StringBuffer requestURL = request.getRequestURL();
		String feedURL = requestURL.substring(0, requestURL.length() - Utilities.getTopicFromURI(request).length());
		feed.setLink(feedURL);
		feed.setDescription("List of the last " + changes.size() + " changed wiki pages.");
		boolean includeMinorEdits = ServletRequestUtils.getBooleanParameter(request, "minorEdits", defaultIncludeMinorEdits);
		feed.setEntries(getFeedEntries(changes, includeMinorEdits, feedURL));
		return feed;
	}

	/**
	 *
	 */
	private List getFeedEntries(Collection changes, boolean includeMinorEdits, String feedURL) {
		List entries = new ArrayList();
		SyndEntry entry;
		SyndContent description;
		for (Iterator iter = changes.iterator(); iter.hasNext();) {
			RecentChange change = (RecentChange)iter.next();
			if (includeMinorEdits || (!change.getMinor())) {
				entry = new SyndEntryImpl();
				entry.setTitle(change.getTopicName());
				entry.setAuthor(change.getAuthorName());
				entry.setPublishedDate(change.getEditDate());
				description = new SyndContentImpl();
				description.setType("text/plain");
				StringBuffer descr = new StringBuffer();
				descr.append(change.getEditComment());
				if (change.getDelete()) {
					descr.append(" (deleted)");
				} else {
					entry.setLink(feedURL + Utilities.encodeForURL(change.getTopicName()));
				}
				if (change.getUndelete()) {
					descr.append(" (undeleted)");
				}
				if (change.getMinor()) {
					descr.append(" (minor)");
				}
				description.setValue(descr.toString());
				entry.setDescription(description);
				entries.add(entry);
			}
		}
		return entries;
	}

	/**
	 *
	 */
	private Collection getChanges(HttpServletRequest request) throws Exception {
		String virtualWiki = Utilities.getVirtualWikiFromURI(request);
		Pagination pagination = Utilities.buildPagination(request, null);
		return WikiBase.getDataHandler().getRecentChanges(virtualWiki, pagination, true);
	}

}
