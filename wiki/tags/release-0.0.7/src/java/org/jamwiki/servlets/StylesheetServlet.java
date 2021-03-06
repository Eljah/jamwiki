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

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.jamwiki.utils.Utilities;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 */
public class StylesheetServlet extends JAMWikiServlet {

	private static Logger logger = Logger.getLogger(StylesheetServlet.class.getName());

	/**
	 *
	 */
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		try {
			String virtualWiki = JAMWikiServlet.getVirtualWikiFromURI(request);
			String topicName = JAMWikiServlet.getTopicFromURI(request);
			String stylesheet = JAMWikiServlet.getCachedContent(request.getContextPath(), virtualWiki, Utilities.getMessage("specialpages.stylesheet", request.getLocale()), false);
			response.setContentType("text/css");
			PrintWriter out = response.getWriter();
			out.print(stylesheet);
			out.close();
		} catch (Exception e) {
			ModelAndView next = new ModelAndView("wiki");
			viewError(request, next, e);
		}
		// do not load defaults or redirect - return as raw CSS
		return null;
	}
}