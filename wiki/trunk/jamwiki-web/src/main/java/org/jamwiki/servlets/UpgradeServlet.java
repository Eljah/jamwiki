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
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.apache.commons.lang.StringUtils;
import org.jamwiki.DataAccessException;
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.WikiException;
import org.jamwiki.WikiMessage;
import org.jamwiki.WikiVersion;
import org.jamwiki.db.DatabaseUpgrades;
import org.jamwiki.db.WikiDatabase;
import org.jamwiki.model.VirtualWiki;
import org.jamwiki.utils.LinkUtil;
import org.jamwiki.utils.WikiLink;
import org.jamwiki.utils.WikiLogger;
import org.jamwiki.utils.WikiUtil;
import org.springframework.web.servlet.ModelAndView;

/**
 * Used to automatically handle JAMWiki upgrades, including configuration and
 * data modifications.
 *
 * @see org.jamwiki.servlets.SetupServlet
 */
public class UpgradeServlet extends JAMWikiServlet {

	private static final WikiLogger logger = WikiLogger.getLogger(UpgradeServlet.class.getName());
	/** The name of the JSP file used to render the servlet output. */
	protected static final String JSP_UPGRADE = "upgrade.jsp";
	private static final int MAX_TOPICS_FOR_AUTOMATIC_SEARCH_REBUILD = 1000;

	/**
	 * This method handles the request after its parent class receives control.
	 *
	 * @param request - Standard HttpServletRequest object.
	 * @param response - Standard HttpServletResponse object.
	 * @return A <code>ModelAndView</code> object to be handled by the rest of the Spring framework.
	 */
	protected ModelAndView handleJAMWikiRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView next, WikiPageInfo pageInfo) throws Exception {
		if (!WikiUtil.isUpgrade()) {
			throw new WikiException(new WikiMessage("upgrade.error.notrequired"));
		}
		String function = request.getParameter("function");
		pageInfo.setPageTitle(new WikiMessage("upgrade.title", Environment.getValue(Environment.PROP_BASE_WIKI_VERSION), WikiVersion.CURRENT_WIKI_VERSION));
		if (!StringUtils.isBlank(function) && function.equals("upgrade")) {
			upgrade(request, next, pageInfo);
		} else {
			view(request, next, pageInfo);
		}
		return next;
	}

	/**
	 *
	 */
	protected void initParams() {
		this.layout = false;
		this.displayJSP = "upgrade";
	}

	/**
	 *
	 */
	private void upgrade(HttpServletRequest request, ModelAndView next, WikiPageInfo pageInfo) {
		List<WikiMessage> messages = new ArrayList<WikiMessage>();
		try {
			WikiVersion oldVersion = new WikiVersion(Environment.getValue(Environment.PROP_BASE_WIKI_VERSION));
			if (oldVersion.before(0, 9, 0)) {
				throw new WikiException(new WikiMessage("upgrade.error.oldversion", WikiVersion.CURRENT_WIKI_VERSION, "0.9.0"));
			}
			// first perform database upgrades
			this.upgradeDatabase(true, messages);
			// upgrade the search index if required & possible
			this.upgradeSearchIndex(true, messages);
			// perform any additional upgrades required
			try {
				int topicCount = WikiBase.getDataHandler().lookupTopicCount(VirtualWiki.defaultVirtualWiki().getName(), null);
				if (oldVersion.before(1, 0, 0)) {
					if (topicCount < 1000) {
						// populate the jam_topic_links table
						WikiDatabase.rebuildTopicMetadata();
						messages.add(new WikiMessage("upgrade.message.db.data.added", "jam_topic_links"));
					} else {
						// print a message telling the user to do this step manually
						messages.add(new WikiMessage("upgrade.message.100.topic.links"));
					}
				}
			} catch (DataAccessException e) {
				logger.warn("Failure during upgrade while generating topic link records.  Please use the tools on the Special:Maintenance page to complete this step.", e);
				messages.add(new WikiMessage("upgrade.error.nonfatal", e.getMessage()));
			}
			// upgrade stylesheet
			if (this.upgradeStyleSheetRequired()) {
				this.upgradeStyleSheet(request, messages);
			}
			pageInfo.getErrors().addAll(ServletUtil.validateSystemSettings(Environment.getInstance()));
			try {
				Environment.setValue(Environment.PROP_BASE_WIKI_VERSION, WikiVersion.CURRENT_WIKI_VERSION);
				Environment.saveConfiguration();
				// reset data handler and other instances.  this probably hides a bug
				// elsewhere since no reset should be needed, but it's anyone's guess
				// where that might be...
				WikiBase.reload();
			} catch (Exception e) {
				logger.error("Failure during upgrade while saving properties and executing WikiBase.reload()", e);
				throw new WikiException(new WikiMessage("upgrade.error.nonfatal", e.toString()));
			}
		} catch (WikiException e) {
			pageInfo.addError(e.getWikiMessage());
		}
		if (!pageInfo.getErrors().isEmpty()) {
			pageInfo.addError(new WikiMessage("upgrade.caption.upgradefailed"));
			next.addObject("failure", "true");
		} else {
			handleUpgradeSuccess(request, next, pageInfo);
		}
		next.addObject("messages", messages);
		this.view(request, next, pageInfo);
	}
		
	/**
	 *
	 */
	private void handleUpgradeSuccess(HttpServletRequest request, ModelAndView next, WikiPageInfo pageInfo) {
		WikiMessage wm = new WikiMessage("upgrade.caption.upgradecomplete");
		VirtualWiki virtualWiki = VirtualWiki.defaultVirtualWiki();
		WikiLink wikiLink = new WikiLink();
		wikiLink.setDestination(virtualWiki.getRootTopicName());
		try {
			String htmlLink = LinkUtil.buildInternalLinkHtml(request.getContextPath(), virtualWiki.getName(), wikiLink, virtualWiki.getRootTopicName(), null, null, true);
			// do not escape the HTML link
			wm.setParamsWithoutEscaping(new String[]{htmlLink});
		} catch (DataAccessException e) {
			// building a link to the start page shouldn't fail, but if it does display a message
			wm = new WikiMessage("upgrade.error.nonfatal", e.toString());
			logger.warn("Upgrade complete, but unable to build redirect link to the start page.", e);
		}
		next.addObject("successMessage", wm);
		// force logout to ensure current user will be re-validated.  this is
		// necessary because the upgrade may have changed underlying data structures.
		SecurityContextHolder.clearContext();
	}

	/**
	 *
	 */
	private boolean upgradeDatabase(boolean performUpgrade, List<WikiMessage> messages) throws WikiException {
		boolean upgradeRequired = false;
		WikiVersion oldVersion = new WikiVersion(Environment.getValue(Environment.PROP_BASE_WIKI_VERSION));
		if (oldVersion.before(1, 0, 0)) {
			upgradeRequired = true;
			if (performUpgrade) {
				messages = DatabaseUpgrades.upgrade100(messages);
			}
		}
		if (oldVersion.before(1, 1, 0)) {
			upgradeRequired = true;
			if (performUpgrade) {
				messages = DatabaseUpgrades.upgrade110(messages);
			}
		}
		return upgradeRequired;
	}

	/**
	 *
	 */
	private boolean upgradeSearchIndex(boolean performUpgrade, List<WikiMessage> messages) {
		boolean upgradeRequired = false;
		WikiVersion oldVersion = new WikiVersion(Environment.getValue(Environment.PROP_BASE_WIKI_VERSION));
		if (oldVersion.before(1, 1, 0)) {
			upgradeRequired = true;
			if (performUpgrade) {
				try {
					int topicCount = WikiBase.getDataHandler().lookupTopicCount(VirtualWiki.defaultVirtualWiki().getName(), null);
					if (oldVersion.before(1, 1, 0)) {
						if (topicCount < MAX_TOPICS_FOR_AUTOMATIC_SEARCH_REBUILD) {
							// refresh search engine
							WikiBase.getSearchEngine().refreshIndex();
							messages.add(new WikiMessage("upgrade.message.search.refresh"));
						} else {
							// print a message telling the user to do this step manually
							WikiMessage searchWikiMessage = new WikiMessage("upgrade.error.search.refresh");
							searchWikiMessage.addWikiLinkParam("Special:Maintenance");
							messages.add(searchWikiMessage);
						}
					}
				} catch (Exception e) {
					logger.warn("Failure during upgrade while rebuilding search index.  Please use the tools on the Special:Maintenance page to complete this step.", e);
					messages.add(new WikiMessage("upgrade.error.nonfatal", e.getMessage()));
				}
			}
		}
		return upgradeRequired;
	}

	/**
	 *
	 */
	private boolean upgradeStyleSheet(HttpServletRequest request, List<WikiMessage> messages) {
		try {
			List<VirtualWiki> virtualWikis = WikiBase.getDataHandler().getVirtualWikiList();
			for (VirtualWiki virtualWiki : virtualWikis) {
				WikiBase.getDataHandler().updateSpecialPage(request.getLocale(), virtualWiki.getName(), WikiBase.SPECIAL_PAGE_STYLESHEET, ServletUtil.getIpAddress(request));
				messages.add(new WikiMessage("upgrade.message.stylesheet.success", virtualWiki.getName()));
			}
			return true;
		} catch (WikiException e) {
			logger.warn("Failure while updating JAMWiki stylesheet", e);
			messages.add(e.getWikiMessage());
			messages.add(new WikiMessage("upgrade.message.stylesheet.failure",  e.getMessage()));
			return false;
		} catch (DataAccessException e) {
			logger.warn("Failure while updating JAMWiki stylesheet", e);
			messages.add(new WikiMessage("upgrade.message.stylesheet.failure",  e.getMessage()));
			return false;
		}
	}

	/**
	 *
	 */
	private boolean upgradeStyleSheetRequired() {
		WikiVersion oldVersion = new WikiVersion(Environment.getValue(Environment.PROP_BASE_WIKI_VERSION));
		return (oldVersion.before(1, 1, 0));
	}

	/**
	 *
	 */
	private void view(HttpServletRequest request, ModelAndView next, WikiPageInfo pageInfo) {
		WikiVersion oldVersion = new WikiVersion(Environment.getValue(Environment.PROP_BASE_WIKI_VERSION));
		if (oldVersion.before(0, 9, 0)) {
			pageInfo.addError(new WikiMessage("upgrade.error.oldversion", WikiVersion.CURRENT_WIKI_VERSION, "0.9.0"));
		}
		List<WikiMessage> upgradeDetails = new ArrayList<WikiMessage>();
		try {
			if (this.upgradeDatabase(false, null)) {
				upgradeDetails.add(new WikiMessage("upgrade.caption.database"));
			}
		} catch (Exception e) {
			// never thrown when the first parameter is false
		}
		if (this.upgradeSearchIndex(false, null)) {
			WikiMessage searchWikiMessage = new WikiMessage("upgrade.caption.search");
			searchWikiMessage.addParam(Integer.toString(MAX_TOPICS_FOR_AUTOMATIC_SEARCH_REBUILD));
			searchWikiMessage.addWikiLinkParam("Special:Maintenance");
			upgradeDetails.add(searchWikiMessage);
		}
		if (oldVersion.before(1, 0, 0)) {
			upgradeDetails.add(new WikiMessage("upgrade.message.100.reparse"));
		}
		if (this.upgradeStyleSheetRequired()) {
			upgradeDetails.add(new WikiMessage("upgrade.caption.stylesheet"));
		}
		upgradeDetails.add(new WikiMessage("upgrade.caption.releasenotes"));
		upgradeDetails.add(new WikiMessage("upgrade.caption.manual"));
		next.addObject("upgradeDetails", upgradeDetails);
		pageInfo.setContentJsp(JSP_UPGRADE);
		pageInfo.setSpecial(true);
	}
}
