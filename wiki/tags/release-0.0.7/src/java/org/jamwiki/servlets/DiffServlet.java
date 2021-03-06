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

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.jamwiki.WikiBase;
import org.jamwiki.model.Topic;
import org.jamwiki.utils.Utilities;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 */
public class DiffServlet extends JAMWikiServlet {

	private static Logger logger = Logger.getLogger(DiffServlet.class);

	/**
	 *
	 */
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView next = new ModelAndView("wiki");
		try {
			diff(request, next);
		} catch (Exception e) {
			viewError(request, next, e);
		}
		loadDefaults(request, next, this.pageInfo);
		return next;
	}

	/**
	 *
	 */
	protected void diff(HttpServletRequest request, ModelAndView next) throws Exception {
		String virtualWiki = JAMWikiServlet.getVirtualWikiFromURI(request);
		String topicName = JAMWikiServlet.getTopicFromRequest(request);
		try {
			String diffType = request.getParameter("type");
			if (diffType != null && diffType.equals("arbitrary")) {
				int firstVersion = -1;
				int secondVersion = -1;
				Enumeration e = request.getParameterNames();
				while (e.hasMoreElements()) {
					String name = (String) e.nextElement();
					if (name.startsWith("diff:")) {
						int version = Integer.parseInt(name.substring(name.indexOf(":") + 1));
						if (firstVersion >= 0) {
							secondVersion = version;
						} else {
							firstVersion = version;
						}
					}
				}
				if (firstVersion == -1 || secondVersion == -1) {
					next.addObject("badinput", "true");
				} else {
					String diff = WikiBase.getHandler().diff(virtualWiki, topicName, Math.max(firstVersion, secondVersion), Math.min(firstVersion, secondVersion), true, request.getLocale());
					next.addObject("diff", diff);
				}
			} else {
				int topicVersionId1 = 0;
				if (StringUtils.hasText(request.getParameter("version1"))) {
					topicVersionId1 = new Integer(request.getParameter("version1")).intValue();
				}
				int topicVersionId2 = 0;
				if (StringUtils.hasText(request.getParameter("version2"))) {
					topicVersionId2 = new Integer(request.getParameter("version2")).intValue();
				}
				String diff = WikiBase.getHandler().diff(virtualWiki, topicName, topicVersionId1, topicVersionId2, true, request.getLocale());
				next.addObject("diff", diff);
			}
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		String message = Utilities.getMessage("diff.title", request.getLocale(), topicName);
		this.pageInfo.setPageTitle(message);
		this.pageInfo.setTopicName(topicName);
		this.pageInfo.setPageAction(JAMWikiServlet.ACTION_DIFF);
	}
}
