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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jamwiki.WikiBase;
import org.jamwiki.WikiMessage;
import org.jamwiki.model.Topic;
import org.jamwiki.utils.Pagination;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiLogger;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 */
public class ItemsServlet extends JAMWikiServlet {

	/** Logger for this class and subclasses. */
	private static WikiLogger logger = WikiLogger.getLogger(ItemsServlet.class.getName());

	/**
	 * This method handles the request after its parent class receives control. It gets the topic's name and the
	 * virtual wiki name from the uri, loads the topic and returns a view to the end user.
	 *
	 * @param request - Standard HttpServletRequest object.
	 * @param response - Standard HttpServletResponse object.
	 * @return A <code>ModelAndView</code> object to be handled by the rest of the Spring framework.
	 */
	protected ModelAndView handleJAMWikiRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView next, WikiPageInfo pageInfo) throws Exception {
		if (ServletUtil.isTopic(request, "Special:Imagelist")) {
			viewImages(request, next, pageInfo);
		} else if (ServletUtil.isTopic(request, "Special:Filelist")) {
			viewFiles(request, next, pageInfo);
		} else {
			viewTopics(request, next, pageInfo);
		}
		return next;
	}

	/**
	 *
	 */
	private void viewFiles(HttpServletRequest request, ModelAndView next, WikiPageInfo pageInfo) throws Exception {
		String virtualWiki = Utilities.getVirtualWikiFromURI(request);
		Pagination pagination = Utilities.buildPagination(request, next);
		Collection items = WikiBase.getDataHandler().lookupTopicByType(virtualWiki, Topic.TYPE_FILE, pagination);
		next.addObject("itemCount", new Integer(items.size()));
		next.addObject("items", items);
		next.addObject("rootUrl", "Special:Filelist");
		pageInfo.setPageTitle(new WikiMessage("allfiles.title"));
		pageInfo.setAction(WikiPageInfo.ACTION_FILES);
		pageInfo.setSpecial(true);
	}

	/**
	 *
	 */
	private void viewImages(HttpServletRequest request, ModelAndView next, WikiPageInfo pageInfo) throws Exception {
		String virtualWiki = Utilities.getVirtualWikiFromURI(request);
		Pagination pagination = Utilities.buildPagination(request, next);
		Collection items = WikiBase.getDataHandler().lookupTopicByType(virtualWiki, Topic.TYPE_IMAGE, pagination);
		next.addObject("itemCount", new Integer(items.size()));
		next.addObject("items", items);
		next.addObject("rootUrl", "Special:Imagelist");
		pageInfo.setPageTitle(new WikiMessage("allimages.title"));
		pageInfo.setAction(WikiPageInfo.ACTION_IMAGES);
		pageInfo.setSpecial(true);
	}

	/**
	 *
	 */
	private void viewTopics(HttpServletRequest request, ModelAndView next, WikiPageInfo pageInfo) throws Exception {
		String virtualWiki = Utilities.getVirtualWikiFromURI(request);
		Pagination pagination = Utilities.buildPagination(request, next);
		Collection items = WikiBase.getDataHandler().lookupTopicByType(virtualWiki, Topic.TYPE_ARTICLE, pagination);
		next.addObject("itemCount", new Integer(items.size()));
		next.addObject("items", items);
		next.addObject("rootUrl", "Special:Allpages");
		pageInfo.setPageTitle(new WikiMessage("alltopics.title"));
		pageInfo.setAction(WikiPageInfo.ACTION_ALL_PAGES);
		pageInfo.setSpecial(true);
	}
}