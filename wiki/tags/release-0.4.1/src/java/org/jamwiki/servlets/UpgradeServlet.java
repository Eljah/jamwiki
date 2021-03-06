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
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.WikiException;
import org.jamwiki.WikiMessage;
import org.jamwiki.WikiVersion;
import org.jamwiki.db.DatabaseHandler;
import org.jamwiki.db.DatabaseUpgrades;
import org.jamwiki.file.FileHandler;
import org.jamwiki.model.VirtualWiki;
import org.jamwiki.model.WikiUser;
import org.jamwiki.utils.LinkUtil;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiLink;
import org.jamwiki.utils.WikiLogger;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * The <code>AdminServlet</code> servlet is the servlet which allows the administrator
 * to perform administrative actions on the wiki.
 */
public class UpgradeServlet extends JAMWikiServlet {

	private static WikiLogger logger = WikiLogger.getLogger(UpgradeServlet.class.getName());

	/**
	 * This method handles the request after its parent class receives control.
	 *
	 * @param request - Standard HttpServletRequest object.
	 * @param response - Standard HttpServletResponse object.
	 * @return A <code>ModelAndView</code> object to be handled by the rest of the Spring framework.
	 */
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView next = new ModelAndView("upgrade");
		WikiPageInfo pageInfo = new WikiPageInfo();
		try {
			if (!Utilities.isUpgrade()) {
				throw new WikiException(new WikiMessage("upgrade.error.notrequired"));
			}
			String function = request.getParameter("function");
			if (!StringUtils.hasText(function)) {
				view(request, next, pageInfo);
			} else if (function.equals("upgrade")) {
				upgrade(request, next, pageInfo);
			}
		} catch (Exception e) {
			return viewError(request, e);
		}
		return next;
	}

	/**
	 *
	 */
	private boolean login(HttpServletRequest request) throws Exception {
		String password = request.getParameter("password");
		String username = request.getParameter("username");
		WikiUser user = null;
		if (Environment.getValue(Environment.PROP_BASE_PERSISTENCE_TYPE).equals("FILE")) {
			FileHandler handler = new FileHandler();
			user = handler.lookupWikiUser(username, password, false);
		} else {
			user = WikiBase.getHandler().lookupWikiUser(username, password, false);
		}
		if (user != null) {
			request.getSession().setAttribute(JAMWikiServlet.PARAMETER_USER, user);
			return true;
		}
		return false;
	}

	/**
	 *
	 */
	private void upgrade(HttpServletRequest request, ModelAndView next, WikiPageInfo pageInfo) throws Exception {
		if (!this.login(request)) {
			next.addObject("error", new WikiMessage("error.login"));
			pageInfo.setAction(WikiPageInfo.ACTION_UPGRADE);
			pageInfo.setSpecial(true);
			pageInfo.setPageTitle(new WikiMessage("upgrade.title"));
			return;
		}
		Vector messages = new Vector();
		boolean success = true;
		WikiVersion oldVersion = new WikiVersion(Environment.getValue(Environment.PROP_BASE_WIKI_VERSION));
		if (oldVersion.before(0, 2, 0)) {
			messages.add(new WikiMessage("upgrade.error.oldversion", WikiVersion.CURRENT_WIKI_VERSION, "0.2.0"));
			success = false;
		} else {
			// first perform database upgrades
			if (!Environment.getValue(Environment.PROP_BASE_PERSISTENCE_TYPE).equals("FILE")) {
				try {
					if (oldVersion.before(0, 3, 0)) {
						messages = DatabaseUpgrades.upgrade030(messages);
					}
					if (oldVersion.before(0, 3, 1)) {
						messages = DatabaseUpgrades.upgrade031(messages);
					}
				} catch (Exception e) {
					// FIXME - hard coding
					String msg = "Unable to complete upgrade to new JAMWiki version.";
					logger.severe(msg, e);
					messages.add(msg + ": " + e.getMessage());
					success = false;
				}
			}
			// then perform other needed upgrades
			boolean stylesheet = false;
			// the 0.4.0 file conversion needs to happen first
			if (oldVersion.before(0, 4, 0)) {
				if (!upgrade040(request, messages)) success = false;
			}
			if (oldVersion.before(0, 3, 0)) {
				stylesheet = true;
			}
			if (oldVersion.before(0, 3, 1)) {
				stylesheet = true;
			}
			if (oldVersion.before(0, 3, 5)) {
				stylesheet = true;
			}
			if (stylesheet) {
				// upgrade stylesheet
				if (!upgradeStyleSheet(request, messages)) success = false;
			}
			Vector errors = Utilities.validateSystemSettings(Environment.getInstance());
			if (errors.size() > 0) {
				next.addObject("errors", errors);
				success = false;
			}
		}
		if (success) {
			Environment.setValue(Environment.PROP_BASE_WIKI_VERSION, WikiVersion.CURRENT_WIKI_VERSION);
			Environment.saveProperties();
			VirtualWiki virtualWiki = WikiBase.getHandler().lookupVirtualWiki(WikiBase.DEFAULT_VWIKI);
			WikiLink wikiLink = new WikiLink();
			wikiLink.setDestination(virtualWiki.getDefaultTopicName());
			String htmlLink = LinkUtil.buildInternalLinkHtml(request.getContextPath(), virtualWiki.getName(), wikiLink, virtualWiki.getDefaultTopicName(), null, true);
			WikiMessage wm = new WikiMessage("upgrade.caption.upgradecomplete");
			// do not escape the HTML link
			wm.setParamsWithoutEscaping(new String[]{htmlLink});
			next.addObject("message", wm);
		} else {
			next.addObject("error", new WikiMessage("upgrade.caption.upgradefailed"));
			next.addObject("failure", "true");
		}
		next.addObject("messages", messages);
		pageInfo.setAction(WikiPageInfo.ACTION_UPGRADE);
		pageInfo.setSpecial(true);
		pageInfo.setPageTitle(new WikiMessage("upgrade.title"));
	}

	/**
	 *
	 */
	private boolean upgrade040(HttpServletRequest request, Vector messages) {
		try {
			if (Environment.getValue(Environment.PROP_BASE_PERSISTENCE_TYPE).equals("FILE")) {
				// convert file to default database
				DatabaseHandler.setupDefaultDatabase(Environment.getInstance());
				WikiBase.reset(request.getLocale(), Utilities.currentUser(request));
				Environment.saveProperties();
				FileHandler fromHandler = new FileHandler();
				messages.addAll(DatabaseHandler.convertFromFile(Utilities.currentUser(request), request.getLocale(), fromHandler, WikiBase.getHandler()));
			}
			if (Environment.getValue(Environment.PROP_PARSER_CLASS) != null && Environment.getValue(Environment.PROP_PARSER_CLASS).equals("org.jamwiki.parser.JAMWikiParser")) {
				Environment.setValue(Environment.PROP_PARSER_CLASS, "org.jamwiki.parser.jflex.JFlexParser");
				Environment.saveProperties();
			}
			return true;
		} catch (Exception e) {
			// FIXME - hard coding
			String msg = "Unable to complete upgrade to new JAMWiki version.";
			logger.severe(msg, e);
			messages.add(msg + ": " + e.getMessage());
			return false;
		}
	}

	/**
	 *
	 */
	private boolean upgradeStyleSheet(HttpServletRequest request, Vector messages) throws Exception {
		try {
			Collection virtualWikis = WikiBase.getHandler().getVirtualWikiList();
			for (Iterator iterator = virtualWikis.iterator(); iterator.hasNext();) {
				VirtualWiki virtualWiki = (VirtualWiki)iterator.next();
				WikiBase.getHandler().updateSpecialPage(request.getLocale(), virtualWiki.getName(), WikiBase.SPECIAL_PAGE_STYLESHEET, Utilities.currentUser(request), request.getRemoteAddr());
				messages.add("Updated stylesheet for virtual wiki " + virtualWiki.getName());
			}
			return true;
		} catch (Exception e) {
			// FIXME - hard coding
			String msg = "Unable to complete upgrade to new JAMWiki version.";
			logger.severe(msg, e);
			messages.add(msg + ": " + e.getMessage());
			return false;
		}
	}

	/**
	 *
	 */
	private void view(HttpServletRequest request, ModelAndView next, WikiPageInfo pageInfo) throws Exception {
		WikiVersion oldVersion = new WikiVersion(Environment.getValue(Environment.PROP_BASE_WIKI_VERSION));
		if (oldVersion.before(0, 1, 0)) {
			Vector errors = new Vector();
			errors.add(new WikiMessage("upgrade.error.oldversion", WikiVersion.CURRENT_WIKI_VERSION, "0.1.0"));
			next.addObject("errors", errors);
		}
		pageInfo.setAction(WikiPageInfo.ACTION_UPGRADE);
		pageInfo.setSpecial(true);
		pageInfo.setPageTitle(new WikiMessage("upgrade.title"));
	}
}
