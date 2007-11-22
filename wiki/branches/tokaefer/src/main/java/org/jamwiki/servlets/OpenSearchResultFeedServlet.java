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
import org.jamwiki.search.SearchResultEntry;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiLogger;
import org.jamwiki.utils.WikiUtil;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.sun.syndication.feed.atom.Link;
import com.sun.syndication.feed.module.opensearch.OpenSearchModule;
import com.sun.syndication.feed.module.opensearch.entity.OSQuery;
import com.sun.syndication.feed.module.opensearch.impl.OpenSearchModuleImpl;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.SyndFeedOutput;
/**
 * Implements OpenSearch api. A RSS feed is returned as result set
 *
 * Feed generation can be influenced through following request parameters:
 * <ul>
 * <li><code>feedType</code>: RSS or Atom (see
 * {@link #setDefaultFeedType(String)}) </li>
 * </ul>
 *
 * @author Tobias Kaefer
 * @since 2007-11-20
 */
public class OpenSearchResultFeedServlet extends AbstractController {
    private static final WikiLogger logger = WikiLogger.getLogger(RecentChangesFeedServlet.class.getName());
    private static final String MIME_TYPE = "application/xml";
    private static final String FEED_ENCODING = "UTF-8";
    private static final String DEFAULT_FEED_TYPE = "rss_2.0";
    private static final String FEED_TYPE = "feedType";
    private String defaultFeedType = DEFAULT_FEED_TYPE;
    private String feedUrlPrefix = "";

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            String feedType = ServletRequestUtils.getStringParameter(request, FEED_TYPE, defaultFeedType);
            logger.fine("Serving xml feed of type " + feedType);
            SyndFeed feed = getFeed(request);
            feed.setFeedType(feedType);
            response.setContentType(MIME_TYPE);
            response.setCharacterEncoding(FEED_ENCODING);
            SyndFeedOutput output = new SyndFeedOutput();
            output.output(feed, response.getWriter());
        } catch (Exception e) {
            logger.severe("Could not generate feed: " + e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not generate feed: "
                    + e.getMessage());
        }
        return null;
    }

    /**
     * get a feed as the result of a search query
     * @throws Exception
     */
    private SyndFeed getFeed(HttpServletRequest request) throws Exception {
        Collection results = getResults(request);
        SyndFeed feed = new SyndFeedImpl();
        StringBuffer requestURL = request.getRequestURL();
        String feedURL = feedUrlPrefix + requestURL.substring(0, requestURL.length() - WikiUtil.getTopicFromURI(request).length());

        // Add the OpenSearch module, you would get information like totalResults from the
        // return results of your search
        List mods = feed.getModules();

        // instantiate OpenSearchModule for use with SyndFeed
        OpenSearchModule osm = new OpenSearchModuleImpl();
        osm.setItemsPerPage(1);
        osm.setStartIndex(1);
        osm.setTotalResults(1024);
        osm.setItemsPerPage(50);

        OSQuery query = new OSQuery();
        query.setRole("superset");
        query.setSearchTerms("Java Syndication");
        query.setStartPage(1);
        osm.addQuery(query);

        Link link = new Link();
        link.setHref(feedURL + "/OpenSearchDescription");
        link.setType("application/opensearchdescription+xml");
        osm.setLink(link);
        mods.add(osm);

        feed.setModules(mods);
        // end add module

        feed.setEncoding(FEED_ENCODING);
        feed.setTitle(Environment.getValue(Environment.PROP_RSS_TITLE));
        feed.setLink(feedURL);
        feed.setDescription("Search Results");

        feed.setEntries(getFeedEntries(results, feedURL));
        return feed;
    }


    /**
     * performs search on JAMWiki via instance of search engine
     * @param request
     * @return Collection of SearchResultEntry
     */
    private Collection getResults(HttpServletRequest request){
        String virtualWiki = WikiUtil.getVirtualWikiFromURI(request);
        String searchField = request.getParameter("query");
        // grab search engine instance and find
        return WikiBase.getSearchEngine().findResults(virtualWiki, searchField);
    }

    /**
     * convert the results into syndication entries
     * @param results
     * @param feedUrl
     * @return List of SyndEntry
     */
    private List getFeedEntries(Collection results, String feedUrl) {
		List entries = new ArrayList();
		for (Iterator iter = results.iterator(); iter.hasNext();) {
			SearchResultEntry result = (SearchResultEntry)iter.next();
			entries.add(getFeedEntry(result, feedUrl));
		}
		return entries;
	}

    /**
     * convert a SearchResultEntry item into SyndEntry item
     * @param result
     * @param feedURL
     * @return SyndEntry for SearchResultEntry
     */
	private SyndEntry getFeedEntry(SearchResultEntry result, String feedURL) {
		SyndContent description;
		SyndEntry entry = new SyndEntryImpl();
		entry.setTitle(result.getTopic());
		// entry.setAuthor(result.getAuthorName());
		// entry.setPublishedDate(result.getEditDate());
		description = new SyndContentImpl();
		description.setType("text/plain");
		description.setValue(result.getSummary());
		entry.setDescription(description);
		entry.setLink(feedURL + Utilities.encodeForURL(result.getTopic()));

		// URI is used as GUID in RSS 2.0 and should therefore contain the
		// version id
		entry.setUri(feedURL + Utilities.encodeForURL(result.getTopic()));

		return entry;
	}
}
