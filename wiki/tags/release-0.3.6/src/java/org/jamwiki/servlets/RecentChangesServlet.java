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
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.utils.Pagination;
import org.jamwiki.utils.WikiLogger;
import org.jamwiki.WikiMessage;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 */
public class RecentChangesServlet extends JAMWikiServlet {

	private static final WikiLogger logger = WikiLogger.getLogger(RecentChangesServlet.class.getName());

	/**
	 *
	 */
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView next = new ModelAndView("wiki");
		WikiPageInfo pageInfo = new WikiPageInfo();
		try {
			recentChanges(request, next, pageInfo);
		} catch (Exception e) {
			return viewError(request, e);
		}
		loadDefaults(request, next, pageInfo);
		return next;
	}

	/**
	 *
	 */
	private void recentChanges(HttpServletRequest request, ModelAndView next, WikiPageInfo pageInfo) throws Exception {
		String virtualWiki = JAMWikiServlet.getVirtualWikiFromURI(request);
		Pagination pagination = JAMWikiServlet.buildPagination(request, next);
		Collection changes = WikiBase.getHandler().getRecentChanges(virtualWiki, pagination, true);
		next.addObject("changes", changes);
		next.addObject("numChanges", new Integer(changes.size()));
		pageInfo.setPageTitle(new WikiMessage("recentchanges.title"));
		pageInfo.setAction(WikiPageInfo.ACTION_RECENT_CHANGES);
		pageInfo.setSpecial(true);
	}
}
