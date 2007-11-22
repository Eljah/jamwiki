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


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.jamwiki.utils.WikiLogger;
import org.jamwiki.utils.WikiUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Implements OpenSearch api. A RSS feed is returned as result set
 *
 * Feed generation can be influenced through following request parameters:
 * <ul>
 * <li><code>feedType</code>: RSS or Atom (see
 * {@link #setDefaultFeedType(String)}) </li>
 * <li><code></code>: set to 'true' to include minor edits </li>
 * </ul>
 *
 * @author Tobias Kaefer
 * @since 2007-11-21
 */
public class OpenSearchDescriptionServlet extends AbstractController {
	private static final WikiLogger logger = WikiLogger.getLogger(RecentChangesFeedServlet.class.getName());
    private static final String MIME_TYPE = "application/xml";
    private static final String FEED_ENCODING = "UTF-8";


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

        logger.fine("Serving OpenSearch xml description");
        response.setContentType(MIME_TYPE);
        response.setCharacterEncoding(FEED_ENCODING);
        StringBuffer requestURL = request.getRequestURL();
        String feedURL = requestURL.substring(0, requestURL.length() - WikiUtil.getTopicFromURI(request).length());
        try{
			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			XMLStreamWriter writer = factory.createXMLStreamWriter(response.getWriter());
			writer.writeStartDocument();
				writer.writeStartElement("OpenSearchDescription");
					writer.writeAttribute("xmlns", "http://a9.com/-/spec/opensearch/1.1/");
					writer.writeStartElement("ShortName");
						writer.writeCharacters("JAMWiki");
					writer.writeEndElement();
					writer.writeStartElement("LongName");
						writer.writeCharacters("JAMWiki Search");
					writer.writeEndElement();
					writer.writeStartElement("Description");
						writer.writeCharacters("JAMWiki Search");
					writer.writeEndElement();
					writer.writeStartElement("Tags");
						writer.writeCharacters("JAMWiki wiki");
					writer.writeEndElement();
					writer.writeStartElement("Contact");
						//TODO retrieve standard contact
					writer.writeEndElement();
					writer.writeStartElement("Url");
						writer.writeAttribute("type", "application/rss+xml");
						writer.writeAttribute("template", feedURL + "Special:OpenSearchResultFeed?query={searchTerms}");
					writer.writeEndElement();
					writer.writeStartElement("Url");
						writer.writeAttribute("type", "text/html");
						writer.writeAttribute("method", "get");
						writer.writeAttribute("template", feedURL + "Special:Search?text={searchTerms}");
					writer.writeEndElement();
					writer.writeStartElement("Url");
						writer.writeAttribute("type", "application/x-suggestions+json");
						writer.writeAttribute("method", "get");
						writer.writeAttribute("template", feedURL + "Special:OpenSearchSuggestion?query={searchTerms}");
					writer.writeEndElement();
				writer.writeEndElement();
			writer.writeEndDocument();
			writer.close();
        } catch (Exception e) {
            logger.severe("Could not generate OpenSearch xml description: " + e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
            		"Could not generate OpenSearch xml description: " + e.getMessage());
		}
		return null;
	}

}
