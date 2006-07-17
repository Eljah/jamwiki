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

import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.model.Topic;
import org.jamwiki.model.WikiUser;
import org.jamwiki.parser.ParserInfo;
import org.jamwiki.utils.Utilities;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 */
public abstract class JAMWikiServlet extends AbstractController {

	private static final Logger logger = Logger.getLogger(JAMWikiServlet.class);
	// constants used as the action parameter in calls to this servlet
	public static final String ACTION_ADMIN = "action_admin";
	public static final String ACTION_ADMIN_DELETE = "action_admin_delete";
	public static final String ACTION_ADMIN_UPGRADE = "action_admin_upgrade";
	public static final String ACTION_CANCEL = "Cancel";
	public static final String ACTION_CONTRIBUTIONS = "action_contributions";
	public static final String ACTION_DIFF = "action_diff";
	public static final String ACTION_EDIT = "action_edit";
	public static final String ACTION_EDIT_RESOLVE = "action_edit_resolve";
	public static final String ACTION_ERROR = "action_error";
	public static final String ACTION_HISTORY = "action_history";
	public static final String ACTION_LOGIN = "action_login";
	public static final String ACTION_LOGOUT = "action_logout";
	public static final String ACTION_REGISTER = "action_member";
	public static final String ACTION_PRINT = "action_printable";
	public static final String ACTION_RSS = "RSS";
	public static final String ACTION_SAVE_USER = "action_save_user";
	public static final String ACTION_SEARCH = "action_search";
	public static final String ACTION_SETUP = "action_setup";
	public static final String ACTION_UNLOCK = "action_unlock";
	public static final String ACTION_UPLOAD = "action_upload";
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
	public static final String PARAMETER_TOPIC_OBJECT = "topicObject";
	public static final String PARAMETER_USER = "user";
	public static final String PARAMETER_VIRTUAL_WIKI = "virtualWiki";
	public static final String USER_COOKIE = "user-cookie";
	public static final String USER_COOKIE_DELIMITER = "|";
	// FIXME - make configurable
	public static final int USER_COOKIE_EXPIRES = 60 * 60 * 24 * 14; // 14 days
	private static HashMap cachedContents = new HashMap();
	WikiPageInfo pageInfo = new WikiPageInfo();

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
	private static void buildLayout(HttpServletRequest request, ModelAndView next) throws Exception {
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
		if (!StringUtils.hasText(uri) || contextPath == null) {
			return null;
		}
		uri = uri.substring(contextPath.length() + 1);
		int slashIndex = uri.indexOf('/');
		if (slashIndex == -1) {
			return null;
		}
		virtualWiki = uri.substring(0, slashIndex);
		return virtualWiki;
	}

	/**
	 *
	 */
	protected static boolean isAction(HttpServletRequest request, String key, String constant) {
		String action = request.getParameter(JAMWikiServlet.PARAMETER_ACTION);
		if (!StringUtils.hasText(action)) {
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
			if (!StringUtils.hasText(topic)) {
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
				if (!StringUtils.hasText(baseFileDir)) {
					// system not set up yet, just read the default file
					// FIXME - filename should be set better
					content = Utilities.readFile(topicName + ".txt");
				} else {
					Topic topic = WikiBase.getHandler().lookupTopic(virtualWiki, topicName);
					content = topic.getTopicContent();
				}
				if (cook) {
					ParserInfo parserInfo = new ParserInfo();
					parserInfo.setContext(context);
					parserInfo.setVirtualWiki(virtualWiki);
					content = WikiBase.parse(parserInfo, content, topicName);
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
	 *
	 */
	protected void loadDefaults(HttpServletRequest request, ModelAndView next, WikiPageInfo pageInfo) {
		// load cached top area, nav bar, etc.
		try {
			this.buildLayout(request, next);
		} catch (Exception e) {
			logger.error("Unable to build default page layout", e);
		}
		// add link to user page and comments page
		WikiUser user = Utilities.currentUser(request);
		if (user != null) {
			next.addObject("userpage", WikiBase.NAMESPACE_USER + user.getLogin());
			next.addObject("usercomments", WikiBase.NAMESPACE_USER_COMMENTS + user.getLogin());
			next.addObject("adminUser", new Boolean(user.getAdmin()));
		}
		if (!this.pageInfo.getSpecial()) {
			// FIXME - this is really ugly
			String article = this.pageInfo.getTopicName();
			String comments = WikiBase.NAMESPACE_COMMENTS + article;
			if (article != null && article.startsWith(WikiBase.NAMESPACE_COMMENTS)) {
				int pos = WikiBase.NAMESPACE_COMMENTS.length();
				article = article.substring(pos);
				comments = WikiBase.NAMESPACE_COMMENTS + article;
			} else if (article != null && article.startsWith(WikiBase.NAMESPACE_SPECIAL)) {
				int pos = WikiBase.NAMESPACE_SPECIAL.length();
				article = article.substring(pos);
				comments = WikiBase.NAMESPACE_COMMENTS + article;
			} else if (article != null && article.startsWith(WikiBase.NAMESPACE_USER_COMMENTS)) {
				int pos = WikiBase.NAMESPACE_USER_COMMENTS.length();
				comments = article;
				article = WikiBase.NAMESPACE_USER + article.substring(pos);
			} else if (article != null && article.startsWith(WikiBase.NAMESPACE_USER)) {
				int pos = WikiBase.NAMESPACE_USER.length();
				comments = WikiBase.NAMESPACE_USER_COMMENTS + article.substring(pos);
			} else if (article != null && article.startsWith(WikiBase.NAMESPACE_IMAGE_COMMENTS)) {
				int pos = WikiBase.NAMESPACE_IMAGE_COMMENTS.length();
				comments = article;
				article = WikiBase.NAMESPACE_IMAGE + article.substring(pos);
			} else if (article != null && article.startsWith(WikiBase.NAMESPACE_IMAGE)) {
				int pos = WikiBase.NAMESPACE_IMAGE.length();
				comments = WikiBase.NAMESPACE_IMAGE_COMMENTS + article.substring(pos);
			}
			next.addObject("article", article);
			next.addObject("comments", comments);
		}
		next.addObject(JAMWikiServlet.PARAMETER_TOPIC, this.pageInfo.getTopicName());
		if (!StringUtils.hasText(this.pageInfo.getTopicName())) {
			try {
				next.addObject(JAMWikiServlet.PARAMETER_TOPIC, JAMWikiServlet.getTopicFromURI(request));
			} catch (Exception e) {
				logger.error("Unable to load topic value in JAMWikiServlet", e);
			}
		}
		next.addObject(JAMWikiServlet.PARAMETER_SPECIAL, new Boolean(this.pageInfo.getSpecial()));
		next.addObject(JAMWikiServlet.PARAMETER_TITLE, "JAMWiki - " + this.pageInfo.getPageTitle());
		next.addObject(JAMWikiServlet.PARAMETER_ACTION, this.pageInfo.getPageAction());
		// reset pageInfo object - seems not to reset with each servlet call
		this.pageInfo = new WikiPageInfo();
	}

	/**
	 * Clears cached contents including the top area, left nav, bottom area, etc.
	 * This method should be called when the contents of these areas may have been
	 * modified.
	 */
	public static void removeCachedContents() {
		cachedContents.clear();
	}

	/**
	 * Action used when redirecting to an error page.
	 *
	 * @param request The servlet request object.
	 * @param next The Spring ModelAndView object.
	 * @param e The exception that is the source of the error.
	 */
	protected void viewError(HttpServletRequest request, ModelAndView next, Exception e) {
		logger.error("Servlet error", e);
		this.pageInfo = new WikiPageInfo();
		this.pageInfo.setPageTitle("System Error");
		this.pageInfo.setPageAction(JAMWikiServlet.ACTION_ERROR);
		this.pageInfo.setSpecial(true);
		next.addObject("errorMessage", e.getMessage());
	}

	/**
	 * Action used when redirecting to a login page.
	 *
	 * @param request The servlet request object.
	 * @param next The Spring ModelAndView object.
	 * @param topic The topic to be redirected to.  Valid examples are "Special:Admin",
	 *  "StartingPoints", etc.
	 */
	protected void viewLogin(HttpServletRequest request, ModelAndView next, String topic) throws Exception {
		this.pageInfo = new WikiPageInfo();
		String virtualWiki = JAMWikiServlet.getVirtualWikiFromURI(request);
		String redirect = request.getParameter("redirect");
		if (!StringUtils.hasText(redirect)) {
			if (!StringUtils.hasText(topic)) {
				topic = Environment.getValue(Environment.PROP_BASE_DEFAULT_TOPIC);
			}
			redirect = Utilities.buildInternalLink(request.getContextPath(), virtualWiki, topic);
			if (request.getQueryString() != null) {
				redirect += "?" + request.getQueryString();
			}
		}
		next.addObject("redirect", redirect);
		this.pageInfo.setPageTitle(Utilities.getMessage("login.title", request.getLocale()));
		this.pageInfo.setPageAction(JAMWikiServlet.ACTION_LOGIN);
		this.pageInfo.setSpecial(true);
	}

	/**
	 * Action used when viewing a topic.
	 *
	 * @param request The servlet request object.
	 * @param next The Spring ModelAndView object.
	 * @param topicName The topic being viewed.  This value must be a valid topic that
	 *  can be loaded as a org.jamwiki.model.Topic object.
	 */
	protected void viewTopic(HttpServletRequest request, ModelAndView next, String topicName) throws Exception {
		String virtualWiki = JAMWikiServlet.getVirtualWikiFromURI(request);
		if (!StringUtils.hasText(virtualWiki)) {
			virtualWiki = WikiBase.DEFAULT_VWIKI;
		}
		Topic topic = WikiBase.getHandler().lookupTopic(virtualWiki, topicName);
		// FIXME - what should the default be for topics that don't exist?
		String contents = "";
		if (topic != null) {
			String displayName = request.getRemoteAddr();
			WikiUser user = Utilities.currentUser(request);
			ParserInfo parserInfo = new ParserInfo();
			parserInfo.setContext(request.getContextPath());
			parserInfo.setWikiUser(user);
			parserInfo.setUserIpAddress(request.getRemoteAddr());
			parserInfo.setVirtualWiki(virtualWiki);
			contents = WikiBase.parse(parserInfo, topic.getTopicContent(), topicName);
			// search servlet highlights search terms, so add that here
			contents = Utilities.highlightHTML(contents, request.getParameter("highlight"));
			topic.setTopicContent(contents);
			if (topic.getTopicType() == Topic.TYPE_IMAGE) {
				List fileVersions = WikiBase.getHandler().getAllWikiFileVersions(virtualWiki, topicName, true);
				next.addObject("fileVersions", fileVersions);
			}
		}
		next.addObject(JAMWikiServlet.PARAMETER_TOPIC_OBJECT, topic);
		this.pageInfo.setPageTitle(topicName);
		this.pageInfo.setTopicName(topicName);
	}
}
