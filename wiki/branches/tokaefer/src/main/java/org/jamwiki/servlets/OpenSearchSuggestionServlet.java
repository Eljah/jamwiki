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

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.jamwiki.WikiBase;
import org.jamwiki.utils.WikiUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Implements OpenSearch api. A JSON array is returned
 *
 * @author Tobias Kaefer
 * @since 2007-11-22
 */
public class OpenSearchSuggestionServlet extends AbstractController {

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
    	throws Exception {

		String searchName = request.getParameter("query");
		Vector result = new Vector();
		Collection topics = getTopics(request);
		if ( topics.size() > 0 )
		{
			result.add(searchName);
			result.add(topics);
		}
		JSONArray jsonArray = JSONArray.fromObject(result);
		response.getWriter().print(jsonArray.toString());
		return null;
	}

	private Collection getTopics(HttpServletRequest request) throws Exception {
		String virtualWiki = WikiUtil.getVirtualWikiFromURI(request);
		String searchName = request.getParameter("query").toLowerCase();
		Collection items = WikiBase.getDataHandler().getAllTopicNames(virtualWiki);
		Vector result = new Vector();
		for (Iterator iterator = items.iterator(); iterator.hasNext();) {
			String topicName = (String) iterator.next();
			if( topicName.toLowerCase().startsWith(searchName) )	{
				result.add(topicName);
			}
		}
		return result;
	}


}
