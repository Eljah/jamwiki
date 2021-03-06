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
 * along with this program (gpl.txt); if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.jamwiki.servlets;

import java.util.HashMap;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.model.Topic;
import org.jamwiki.utils.Utilities;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 */
public abstract class JAMWikiServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(JAMWikiServlet.class);
	// constants used as the action parameter in calls to this servlet
	public static final String ACTION_ADMIN = "action_admin";
	public static final String ACTION_ADMIN_DELETE = "action_admin_delete";
	public static final String ACTION_ADMIN_UPGRADE = "action_admin_upgrade";
	public static final String ACTION_ATTACH = "action_attach";
	public static final String ACTION_CANCEL = "Cancel";
	public static final String ACTION_DIFF = "action_diff";
	public static final String ACTION_EDIT = "action_edit";
	public static final String ACTION_HISTORY = "action_history";
	public static final String ACTION_LOGIN = "action_login";
	public static final String ACTION_LOGOUT = "action_logout";
	public static final String ACTION_MEMBER = "action_member";
	public static final String ACTION_NOTIFY = "action_notify";
	public static final String ACTION_PRINT = "action_printable";
	public static final String ACTION_RSS = "RSS";
	public static final String ACTION_SAVE_USER = "action_save_user";
	public static final String ACTION_SEARCH = "action_search";
	public static final String ACTION_SETUP = "action_setup";
	public static final String ACTION_UNLOCK = "action_unlock";
	public static final String ACTION_VIEW_ATTACHMENT = "action_view_attachment";
	public static final String ACTION_ALL_TOPICS = "all_topics";
	public static final String ACTION_EDIT_USER = "edit_user";
	public static final String ACTION_LOCKLIST = "locklist";
	public static final String ACTION_ORPHANED_TOPICS = "orphaned_topics";
	public static final String ACTION_PREVIEW = "preview";
	public static final String ACTION_RECENT_CHANGES = "recent_changes";
	public static final String ACTION_SAVE = "save";
	public static final String ACTION_SEARCH_RESULTS = "search_results";
	public static final String ACTION_TODO_TOPICS = "todo_topics";
	public static final String ACTION_DELETE = "action_delete";
	public static final String ACTION_VIRTUAL_WIKI_LIST = "action_virtual_wiki_list";
	public static final String PARAMETER_ACTION = "action";
	public static final String PARAMETER_SPECIAL = "special";
	public static final String PARAMETER_TITLE = "title";
	public static final String PARAMETER_TOPIC = "topic";
	public static final String PARAMETER_VIRTUAL_WIKI = "virtualWiki";
	private static HashMap cachedContents = new HashMap();

	/**
	 *
	 */
	protected void redirect(String destination, HttpServletResponse response) {
		String url = response.encodeRedirectURL(destination);
		try {
			response.sendRedirect(url);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 *
	 */
	public static void buildLayout(HttpServletRequest request, ModelAndView next) throws Exception {
		String virtualWiki = JAMWikiServlet.getVirtualWikiFromURI(request);
		if (virtualWiki == null) {
			throw new Exception("Invalid virtual wiki");
		}
		String topic = JAMWikiServlet.getTopicFromRequest(request);
		if (topic == null) {
			topic = JAMWikiServlet.getTopicFromURI(request);
		}
		next.addObject(PARAMETER_TOPIC, topic);
		// build the layout contents
		String leftMenu = JAMWikiServlet.getCachedContent(
			request.getContextPath(),
			virtualWiki,
			Utilities.getMessage("specialpages.leftMenu", request.getLocale()),
			true
		);
		next.addObject("leftMenu", leftMenu);
		String topArea = JAMWikiServlet.getCachedContent(
			request.getContextPath(),
			virtualWiki,
			Utilities.getMessage("specialpages.topArea", request.getLocale()),
			true
		);
		next.addObject("topArea", topArea);
		String bottomArea = JAMWikiServlet.getCachedContent(
			request.getContextPath(),
			virtualWiki,
			Utilities.getMessage("specialpages.bottomArea", request.getLocale()),
			true
		);
		next.addObject("bottomArea", bottomArea);
		String styleSheet = JAMWikiServlet.getCachedContent(
			request.getContextPath(),
			virtualWiki,
			Utilities.getMessage("specialpages.stylesheet", request.getLocale()),
			false
		);
		next.addObject("StyleSheet", styleSheet);
		next.addObject(PARAMETER_VIRTUAL_WIKI, virtualWiki);
	}

	/**
	 *
	 */
	public static String getTopicFromURI(HttpServletRequest request) throws Exception {
		String uri = request.getRequestURI().trim();
		if (uri == null || uri.length() <= 0) {
			throw new Exception("URI string is empty");
		}
		int slashIndex = uri.lastIndexOf('/');
		if (slashIndex == -1) {
			throw new Exception("No topic in URL: " + uri);
		}
		String topic = uri.substring(slashIndex + 1);
		topic = Utilities.decodeURL(topic);
		logger.info("Retrieved topic from URI as: " + topic);
		return topic;
	}

	/**
	 *
	 */
	public static String getTopicFromRequest(HttpServletRequest request) throws Exception {
		String topic = request.getParameter(JAMWikiServlet.PARAMETER_TOPIC);
		if (topic == null) {
			topic = (String)request.getAttribute(JAMWikiServlet.PARAMETER_TOPIC);
		}
		if (topic == null) return null;
		topic = Utilities.decodeURL(topic);
		return topic;
	}

	/**
	 *
	 */
	public static String getVirtualWikiFromURI(HttpServletRequest request) {
		String uri = request.getRequestURI().trim();
		String contextPath = request.getContextPath().trim();
		String virtualWiki = null;
		if (uri == null || uri.length() == 0 || contextPath == null) {
			return null;
		}
		uri = uri.substring(contextPath.length() + 1);
		int slashIndex = uri.indexOf('/');
		if (slashIndex == -1) {
			return null;
		}
		virtualWiki = uri.substring(0, slashIndex);
		logger.info("Retrieved virtual wiki from URI as: " + virtualWiki);
		return virtualWiki;
	}

	/**
	 *
	 */
	protected static boolean isAction(HttpServletRequest request, String key, String constant) {
		String action = request.getParameter(JAMWikiServlet.PARAMETER_ACTION);
		if (action == null || action.length() == 0) {
			return false;
		}
		if (key != null &&  action.equals(Utilities.getMessage(key, request.getLocale()))) {
			return true;
		}
		if (constant != null && action.equals(constant)) {
			return true;
		}
		return false;
	}

	/**
	 *
	 */
	protected static boolean isTopic(HttpServletRequest request, String value) {
		try {
			String topic = JAMWikiServlet.getTopicFromURI(request);
			if (topic == null || topic.length() == 0) {
				return false;
			}
			if (value != null &&  topic.equals(value)) {
				return true;
			}
		} catch (Exception e) {}
		return false;
	}

	/**
	 *
	 */
	public static String getCachedContent(String context, String virtualWiki, String topicName, boolean cook) {
		String content = (String)cachedContents.get(virtualWiki + "-" + topicName);
		if (content == null) {
			try {
				String baseFileDir = Environment.getValue(Environment.PROP_BASE_FILE_DIR);
				if (baseFileDir == null || baseFileDir.length() == 0) {
					// system not set up yet, just read the default file
					// FIXME - filename should be set better
					content = Utilities.readFile(topicName + ".txt");
				} else {
					Topic topic = WikiBase.getInstance().getHandler().lookupTopic(virtualWiki, topicName);
					content = topic.getTopicContent();
				}
				if (cook) {
					content = WikiBase.getInstance().cook(context, virtualWiki, content);
				}
				synchronized (cachedContents) {
					cachedContents.put(virtualWiki + "-" + topicName, content);
				}
			} catch (Exception e) {
				logger.warn("error getting cached page " + virtualWiki + " / " + topicName, e);
				return null;
			}
		}
		return content;
	}

	/**
	 * Clears cached contents including the top area, left nav, bottom area, etc.
	 * This method should be called when the contents of these areas may have been
	 * modified.
	 */
	public static void removeCachedContents() {
		cachedContents.clear();
	}
}
