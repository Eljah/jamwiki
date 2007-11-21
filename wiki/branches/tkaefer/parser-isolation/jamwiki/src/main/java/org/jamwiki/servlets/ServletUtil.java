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

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.Element;

import org.acegisecurity.AuthenticationCredentialsNotFoundException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.WikiException;
import org.jamwiki.WikiMessage;
import org.jamwiki.db.DatabaseConnection;
import org.jamwiki.model.Category;
import org.jamwiki.model.Role;
import org.jamwiki.model.Topic;
import org.jamwiki.model.VirtualWiki;
import org.jamwiki.model.Watchlist;
import org.jamwiki.model.WikiFileVersion;
import org.jamwiki.model.WikiUser;
import org.jamwiki.parser.AbstractParser;
import org.jamwiki.parser.ParserInput;
import org.jamwiki.parser.ParserDocument;
import org.jamwiki.utils.Encryption;
import org.jamwiki.utils.LinkUtil;
import org.jamwiki.utils.NamespaceHandler;
import org.jamwiki.utils.Pagination;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiCache;
import org.jamwiki.utils.WikiLink;
import org.jamwiki.utils.WikiLogger;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * Utility methods useful when processing JAMWiki servlet requests.
 */
public class ServletUtil {

	private static final WikiLogger logger = WikiLogger.getLogger(ServletUtil.class.getName());
	protected static final String JSP_ERROR = "error-display.jsp";
	protected static final String JSP_LOGIN = "login.jsp";
	public static final String PARAMETER_PAGE_INFO = "pageInfo";
	public static final String PARAMETER_TOPIC = "topic";
	public static final String PARAMETER_TOPIC_OBJECT = "topicObject";
	public static final String PARAMETER_USER = "user";
	public static final String PARAMETER_VIRTUAL_WIKI = "virtualWiki";
	public static final String PARAMETER_WATCHLIST = "watchlist";
	private static final String SPRING_REDIRECT_PREFIX = "redirect:";
	public static final String USER_COOKIE = "user-cookie";
	public static final String USER_COOKIE_DELIMITER = "|";
	// FIXME - make configurable
	public static final int USER_COOKIE_EXPIRES = 60 * 60 * 24 * 14; // 14 days

	/**
	 *
	 */
	private ServletUtil() {
	}

	/**
	 * This method ensures that the left menu, logo, and other required values
	 * have been loaded into the session object.
	 *
	 * @param request The servlet request object.
	 * @param next A ModelAndView object corresponding to the page being
	 *  constructed.
	 */
	private static void buildLayout(HttpServletRequest request, ModelAndView next) {
		String virtualWikiName = getVirtualWikiFromURI(request);
		if (virtualWikiName == null) {
			logger.severe("No virtual wiki available for page request " + request.getRequestURI());
			virtualWikiName = WikiBase.DEFAULT_VWIKI;
		}
		VirtualWiki virtualWiki = null;
		String defaultTopic = null;
		try {
			virtualWiki = WikiBase.getDataHandler().lookupVirtualWiki(virtualWikiName);
			defaultTopic = virtualWiki.getDefaultTopicName();
		} catch (Exception e) {}
		if (virtualWiki == null) {
			logger.severe("No virtual wiki found for " + virtualWikiName);
			virtualWikiName = WikiBase.DEFAULT_VWIKI;
			defaultTopic = Environment.getValue(Environment.PROP_BASE_DEFAULT_TOPIC);
		}
		// build the layout contents
		String leftMenu = ServletUtil.cachedContent(request.getContextPath(), request.getLocale(), virtualWikiName, WikiBase.SPECIAL_PAGE_LEFT_MENU, true);
		next.addObject("leftMenu", leftMenu);
		next.addObject("defaultTopic", defaultTopic);
		next.addObject("virtualWiki", virtualWikiName);
		next.addObject("logo", Environment.getValue(Environment.PROP_BASE_LOGO_IMAGE));
		String bottomArea = ServletUtil.cachedContent(request.getContextPath(), request.getLocale(), virtualWikiName, WikiBase.SPECIAL_PAGE_BOTTOM_AREA, true);
		next.addObject("bottomArea", bottomArea);
		next.addObject(ServletUtil.PARAMETER_VIRTUAL_WIKI, virtualWikiName);
	}

	/**
	 * Retrieve the content of a topic from the cache, or if it is not yet in
	 * the cache then add it to the cache.
	 *
	 * @param context The servlet context for the topic being retrieved.  May
	 *  be <code>null</code> if the <code>cook</code> parameter is set to
	 *  <code>false</code>.
	 * @param locale The locale for the topic being retrieved.  May be
	 *  <code>null</code> if the <code>cook</code> parameter is set to
	 *  <code>false</code>.
	 * @param virtualWiki The virtual wiki for the topic being retrieved.
	 * @param topicName The name of the topic being retrieved.
	 * @param cook A parameter indicating whether or not the content should be
	 *  parsed before it is added to the cache.  Stylesheet content (CSS) is not
	 *  parsed, but most other content is parsed.
	 * @return The parsed or unparsed (depending on the <code>cook</code>
	 *  parameter) topic content.
	 */
	protected static String cachedContent(String context, Locale locale, String virtualWiki, String topicName, boolean cook) {
		String content = null;
		String key = WikiCache.key(virtualWiki, topicName);
		Element cacheElement = WikiCache.retrieveFromCache(WikiBase.CACHE_PARSED_TOPIC_CONTENT, key);
		if (cacheElement != null) {
			content = (String)cacheElement.getObjectValue();
			return (content == null) ? null : new String(content);
		}
		try {
			Topic topic = WikiBase.getDataHandler().lookupTopic(virtualWiki, topicName, false, null);
			content = topic.getTopicContent();
			if (cook) {
				ParserInput parserInput = new ParserInput();
				parserInput.setContext(context);
				parserInput.setLocale(locale);
				parserInput.setVirtualWiki(virtualWiki);
				parserInput.setTopicName(topicName);
				content = Utilities.parse(parserInput, null, content);
			}
			WikiCache.addToCache(WikiBase.CACHE_PARSED_TOPIC_CONTENT, key, content);
		} catch (Exception e) {
			logger.warning("error getting cached page " + virtualWiki + " / " + topicName, e);
			return null;
		}
		return content;
	}

	/**
	 * Initialize topic values for a Topic object.  This method will check to
	 * see if a topic with the specified name exists, and if it does exist
	 * then that topic will be returned.  Otherwise a new topic will be
	 * initialized, setting initial parameters such as topic name, virtual
	 * wiki, and topic type.
	 *
	 * @param virtualWiki The virtual wiki name for the topic being
	 *  initialized.
	 * @param topicName The name of the topic being initialized.
	 * @return A new topic object with basic fields initialized, or if a topic
	 *  with the given name already exists then the pre-existing topic is
	 *  returned.
	 * @throws Exception Thrown if any error occurs while retrieving or
	 *  initializing the topic object.
	 */
	protected static Topic initializeTopic(String virtualWiki, String topicName) throws Exception {
		Utilities.validateTopicName(topicName);
		Topic topic = WikiBase.getDataHandler().lookupTopic(virtualWiki, topicName, false, null);
		if (topic != null) {
			return topic;
		}
		topic = new Topic();
		topic.setName(topicName);
		topic.setVirtualWiki(virtualWiki);
		WikiLink wikiLink = LinkUtil.parseWikiLink(topicName);
		String namespace = wikiLink.getNamespace();
		if (namespace != null) {
			if (namespace.equals(NamespaceHandler.NAMESPACE_CATEGORY)) {
				topic.setTopicType(Topic.TYPE_CATEGORY);
			} else if (namespace.equals(NamespaceHandler.NAMESPACE_TEMPLATE)) {
				topic.setTopicType(Topic.TYPE_TEMPLATE);
			}
		}
		return topic;
	}

	/**
	 * Determine if a user has permission to edit a topic.
	 *
	 * @param virtualWiki The virtual wiki name for the topic in question.
	 * @param topicName The name of the topic in question.
	 * @param user The current Wiki user, or <code>null</code> if there is
	 *  no current user.
	 * @return <code>true</code> if the user is allowed to edit the topic,
	 *  <code>false</code> otherwise.
	 */
	protected static boolean isEditable(String virtualWiki, String topicName, WikiUser user) throws Exception {
		if (user == null || !user.hasRole(Role.ROLE_EDIT_EXISTING)) {
			// user does not have appropriate permissions
			return false;
		}
		if (!user.hasRole(Role.ROLE_EDIT_NEW) && WikiBase.getDataHandler().lookupTopic(virtualWiki, topicName, false, null) == null) {
			// user does not have appropriate permissions
			return false;
		}
		Topic topic = WikiBase.getDataHandler().lookupTopic(virtualWiki, topicName, false, null);
		if (topic == null) {
			// new topic, edit away...
			return true;
		}
		if (topic.getAdminOnly() && (user == null || !user.hasRole(Role.ROLE_ADMIN))) {
			return false;
		}
		if (topic.getReadOnly()) {
			return false;
		}
		return true;
	}

	/**
	 * Determine if a user has permission to move a topic.
	 *
	 * @param virtualWiki The virtual wiki name for the topic in question.
	 * @param topicName The name of the topic in question.
	 * @param user The current Wiki user, or <code>null</code> if there is
	 *  no current user.
	 * @return <code>true</code> if the user is allowed to move the topic,
	 *  <code>false</code> otherwise.
	 */
	protected static boolean isMoveable(String virtualWiki, String topicName, WikiUser user) throws Exception {
		if (user == null || !user.hasRole(Role.ROLE_MOVE)) {
			// no permission granted to move pages
			return false;
		}
		Topic topic = WikiBase.getDataHandler().lookupTopic(virtualWiki, topicName, false, null);
		if (topic == null) {
			// cannot move a topic that doesn't exist
			return false;
		}
		if (topic.getReadOnly()) {
			return false;
		}
		if (topic.getAdminOnly() && (user == null || !user.hasRole(Role.ROLE_ADMIN))) {
			return false;
		}
		return true;
	}
	
	/**
	 * Retrieve a topic name from the servlet request.  This method will
	 * retrieve a request parameter matching the PARAMETER_TOPIC value,
	 * and will decode it appropriately.
	 *
	 * @param request The servlet request object.
	 * @return The decoded topic name retrieved from the request.
	 */
	public static String getTopicFromRequest(HttpServletRequest request) throws Exception {
		String topic = null;
		if (request.getMethod().equalsIgnoreCase("GET")) {
			// parameters passed via the URL and URL encoded, so request.getParameter may
			// not interpret non-ASCII characters properly.  This code attempts to work
			// around that issue by manually decoding.  yes, this is ugly and it would be
			// great if someone could eventually make it unnecessary.
			String query = request.getQueryString();
			if (!StringUtils.hasText(query)) {
				return null;
			}
			String prefix = ServletUtil.PARAMETER_TOPIC + "=";
			int pos = query.indexOf(prefix);
			if (pos != -1 && (pos + prefix.length()) < query.length()) {
				topic = query.substring(pos + prefix.length());
				if (topic.indexOf("&") != -1) {
					topic = topic.substring(0, topic.indexOf("&"));
				}
			}
			return Utilities.decodeFromURL(topic);
		}
		topic = request.getParameter(ServletUtil.PARAMETER_TOPIC);
		if (topic == null) {
			topic = (String)request.getAttribute(ServletUtil.PARAMETER_TOPIC);
		}
		if (topic == null) {
			return null;
		}
		return Utilities.decodeFromRequest(topic);
	}
	
	/**
	 * Retrieve a topic name from the request URI.  This method will retrieve
	 * the portion of the URI that follows the virtual wiki and decode it
	 * appropriately.
	 *
	 * @param request The servlet request object.
	 * @return The decoded topic name retrieved from the URI.
	 */
	public static String getTopicFromURI(HttpServletRequest request) {
		// skip one directory, which is the virutal wiki
		String topic = retrieveDirectoriesFromURI(request, 1);
		if (topic == null) {
			logger.warning("No topic in URL: " + request.getRequestURI());
			return null;
		}
		int pos = topic.indexOf('?');
		if (pos != -1) {
			// strip everything after and including '?'
			if (pos == 0) {
				logger.warning("No topic in URL: " + request.getRequestURI());
				return null;
			}
			topic = topic.substring(0, topic.indexOf('?'));
		}
		pos = topic.indexOf('#');
		if (pos != -1) {
			// strip everything after and including '#'
			if (pos == 0) {
				logger.warning("No topic in URL: " + request.getRequestURI());
				return null;
			}
			topic = topic.substring(0, topic.indexOf('#'));
		}
		topic = Utilities.decodeFromURL(topic);
		return topic;
	}
	/**
	 * Examine the request object, and see if the requested topic or page
	 * matches a given value.
	 *
	 * @param request The servlet request object.
	 * @param value The value to match against the current topic or page name.
	 * @return <code>true</code> if the value matches the current topic or
	 *  page name, <code>false</code> otherwise.
	 */
	protected static boolean isTopic(HttpServletRequest request, String value) {
		try {
			String topic = getTopicFromURI(request);
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
	 * Utility method for adding categories associated with the current topic
	 * to the ModelAndView object.  This method adds a hashmap of category
	 * names and sort keys to the session that can then be retrieved for
	 * display during rendering.
	 *
	 * @param next The current ModelAndView object used to return rendering
	 *  information.
	 * @param virtualWiki The virtual wiki name for the topic being rendered.
	 * @param topicName The name of the topic that is being rendered.
	 */
	protected static void loadCategoryContent(ModelAndView next, String virtualWiki, String topicName) throws Exception {
		String categoryName = topicName.substring(NamespaceHandler.NAMESPACE_CATEGORY.length() + NamespaceHandler.NAMESPACE_SEPARATOR.length());
		next.addObject("categoryName", categoryName);
		Collection categoryTopics = WikiBase.getDataHandler().lookupCategoryTopics(virtualWiki, topicName, Topic.TYPE_ARTICLE);
		next.addObject("categoryTopics", categoryTopics);
		next.addObject("numCategoryTopics", new Integer(categoryTopics.size()));
		Collection categoryImages = WikiBase.getDataHandler().lookupCategoryTopics(virtualWiki, topicName, Topic.TYPE_IMAGE);
		next.addObject("categoryImages", categoryImages);
		next.addObject("numCategoryImages", new Integer(categoryImages.size()));
		Collection tempSubcategories = WikiBase.getDataHandler().lookupCategoryTopics(virtualWiki, topicName, Topic.TYPE_CATEGORY);
		LinkedHashMap subCategories = new LinkedHashMap();
		for (Iterator iterator = tempSubcategories.iterator(); iterator.hasNext();) {
			Category category = (Category)iterator.next();
			String value = category.getChildTopicName().substring(NamespaceHandler.NAMESPACE_CATEGORY.length() + NamespaceHandler.NAMESPACE_SEPARATOR.length());
			subCategories.put(category.getChildTopicName(), value);
		}
		next.addObject("subCategories", subCategories);
		next.addObject("numSubCategories", new Integer(subCategories.size()));
	}

	/**
	 * This method ensures that values required for rendering a JSP page have
	 * been loaded into the ModelAndView object.  Examples of values that
	 * may be handled by this method include topic name, username, etc.
	 *
	 * @param request The current servlet request object.
	 * @param next The current ModelAndView object.
	 * @param pageInfo The current WikiPageInfo object, containing basic page
	 *  rendering information.
	 */
	protected static void loadDefaults(HttpServletRequest request, ModelAndView next, WikiPageInfo pageInfo) throws Exception {
		if (next.getViewName() != null && next.getViewName().startsWith(ServletUtil.SPRING_REDIRECT_PREFIX)) {
			// if this is a redirect, no need to load anything
			return;
		}
		// load cached top area, nav bar, etc.
		ServletUtil.buildLayout(request, next);
		// add link to user page and comments page
		WikiUser user = Utilities.currentUser();
		if (user.hasRole(Role.ROLE_USER)) {
			//add user object to model since it is not in session anymore
			next.addObject(PARAMETER_USER, user);
			next.addObject("userpage", NamespaceHandler.NAMESPACE_USER + NamespaceHandler.NAMESPACE_SEPARATOR + user.getUsername());
			next.addObject("usercomments", NamespaceHandler.NAMESPACE_USER_COMMENTS + NamespaceHandler.NAMESPACE_SEPARATOR + user.getUsername());
			next.addObject("adminUser", new Boolean(user.hasRole(Role.ROLE_ADMIN)));
		}
		if (StringUtils.hasText(pageInfo.getTopicName())) {
			String article = Utilities.extractTopicLink(pageInfo.getTopicName());
			String comments = Utilities.extractCommentsLink(pageInfo.getTopicName());
			next.addObject("article", article);
			next.addObject("comments", comments);
			String editLink = "Special:Edit?topic=" + Utilities.encodeForURL(pageInfo.getTopicName());
			if (StringUtils.hasText(request.getParameter("topicVersionId"))) {
				editLink += "&topicVersionId=" + request.getParameter("topicVersionId");
			}
			next.addObject("edit", editLink);
			String virtualWiki = getVirtualWikiFromURI(request);
			pageInfo.setMoveable(ServletUtil.isMoveable(virtualWiki, article, user));
			pageInfo.setEditable(ServletUtil.isEditable(virtualWiki, article, user));
			Watchlist watchlist = currentWatchlist(request, virtualWiki);
			if (watchlist.containsTopic(pageInfo.getTopicName())) {
				pageInfo.setWatched(true);
			}
		}
		if (!StringUtils.hasText(pageInfo.getTopicName())) {
			pageInfo.setTopicName(getTopicFromURI(request));
		}
		next.addObject(ServletUtil.PARAMETER_PAGE_INFO, pageInfo);
	}

	/**
	 * Utility method for parsing a multipart servlet request.  This method returns
	 * an iterator of FileItem objects that corresponds to the request.
	 *
	 * @param request The servlet request containing the multipart request.
	 * @return Returns an iterator of FileItem objects the corresponds to the request.
	 * @throws Exception Thrown if any problems occur while processing the request.
	 */
	protected static Iterator processMultipartRequest(HttpServletRequest request) throws Exception {
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(new File(Environment.getValue(Environment.PROP_FILE_DIR_FULL_PATH)));
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		upload.setSizeMax(Environment.getLongValue(Environment.PROP_FILE_MAX_FILE_SIZE));
		return upload.parseRequest(request).iterator();
	}

	/**
	 * Modify the current ModelAndView object to create a Spring redirect
	 * response, meaning that the view name becomes "redirect:" followed by
	 * the redirection target.
	 *
	 * @param next The current ModelAndView object, which will be reset by
	 *  this method.
	 * @param virtualWiki The virtual wiki name for the page being redirected
	 *  to.
	 * @param destination The topic or page name that is the redirection
	 *  target.  An example might be "Special:Login".
	 */
	protected static void redirect(ModelAndView next, String virtualWiki, String destination) throws Exception {
		String target = LinkUtil.buildInternalLinkUrl(null, virtualWiki, destination);
		String view = ServletUtil.SPRING_REDIRECT_PREFIX + target;
		next.clear();
		next.setViewName(view);
	}

	/**
	 * Utility method used when redirecting to an error page.
	 *
	 * @param request The servlet request object.
	 * @param t The exception that is the source of the error.
	 * @return Returns a ModelAndView object corresponding to the error page display.
	 */
	protected static ModelAndView viewError(HttpServletRequest request, Throwable t) {
		if (!(t instanceof WikiException)) {
			logger.severe("Servlet error", t);
		}
		ModelAndView next = new ModelAndView("wiki");
		WikiPageInfo pageInfo = new WikiPageInfo();
		pageInfo.setPageTitle(new WikiMessage("error.title"));
		pageInfo.setContentJsp(JSP_ERROR);
		pageInfo.setSpecial(true);
		if (t instanceof WikiException) {
			WikiException we = (WikiException)t;
			next.addObject("messageObject", we.getWikiMessage());
		} else {
			next.addObject("messageObject", new WikiMessage("error.unknown", t.toString()));
		}
		try {
			ServletUtil.loadDefaults(request, next, pageInfo);
		} catch (Exception err) {
			logger.severe("Unable to load default layout", err);
		}
		return next;
	}

	/**
	 * Utility method used when redirecting to a login page.
	 *
	 * @param request The servlet request object.
	 * @param pageInfo The current WikiPageInfo object, which contains
	 *  information needed for rendering the final JSP page.
	 * @param topic The topic to be redirected to.  Valid examples are
	 *  "Special:Admin", "StartingPoints", etc.
	 * @param messageObject A WikiMessage object to be displayed on the login
	 *  page.
	 * @return Returns a ModelAndView object corresponding to the login page
	 *  display.
	 * @throws Exception Thrown if any error occurs during processing.
	 */
	protected static ModelAndView viewLogin(HttpServletRequest request, WikiPageInfo pageInfo, String topic, WikiMessage messageObject) throws Exception {
		ModelAndView next = new ModelAndView("wiki");
		pageInfo.reset();
		String virtualWikiName = getVirtualWikiFromURI(request);
		String target = request.getParameter("target");
		if (!StringUtils.hasText(target)) {
			if (!StringUtils.hasText(topic)) {
				VirtualWiki virtualWiki = WikiBase.getDataHandler().lookupVirtualWiki(virtualWikiName);
				topic = virtualWiki.getDefaultTopicName();
			}
			target = topic;
			if (StringUtils.hasText(request.getQueryString())) {
				target += "?" + request.getQueryString();
			}
		}
		next.addObject("target", target);
		pageInfo.setPageTitle(new WikiMessage("login.title"));
		pageInfo.setContentJsp(JSP_LOGIN);
		pageInfo.setSpecial(true);
		if (messageObject != null) {
			next.addObject("messageObject", messageObject);
		}
		return next;
	}

	/**
	 * Utility method used when viewing a topic.
	 *
	 * @param request The current servlet request object.
	 * @param next The current Spring ModelAndView object.
	 * @param pageInfo The current WikiPageInfo object, which contains
	 *  information needed for rendering the final JSP page.
	 * @param topicName The topic being viewed.  This value must be a valid
	 *  topic that can be loaded as a org.jamwiki.model.Topic object.
	 * @throws Exception Thrown if any error occurs during processing.
	 */
	protected static void viewTopic(HttpServletRequest request, ModelAndView next, WikiPageInfo pageInfo, String topicName) throws Exception {
		String virtualWiki = getVirtualWikiFromURI(request);
		if (!StringUtils.hasText(virtualWiki)) {
			virtualWiki = WikiBase.DEFAULT_VWIKI;
		}
		Topic topic = ServletUtil.initializeTopic(virtualWiki, topicName);
		if (topic.getTopicId() <= 0) {
			// topic does not exist, display empty page
			next.addObject("notopic", new WikiMessage("topic.notcreated", topicName));
		}
		WikiMessage pageTitle = new WikiMessage("topic.title", topicName);
		viewTopic(request, next, pageInfo, pageTitle, topic, true);
	}

	/**
	 * Utility method used when viewing a topic.
	 *
	 * @param request The current servlet request object.
	 * @param next The current Spring ModelAndView object.
	 * @param pageInfo The current WikiPageInfo object, which contains
	 *  information needed for rendering the final JSP page.
	 * @param pageTitle The title of the page being rendered.
	 * @param topic The Topic object for the topic being displayed.
	 * @param sectionEdit Set to <code>true</code> if edit links should be displayed
	 *  for each section of the topic.
	 * @throws Exception Thrown if any error occurs during processing.
	 */
	protected static void viewTopic(HttpServletRequest request, ModelAndView next, WikiPageInfo pageInfo, WikiMessage pageTitle, Topic topic, boolean sectionEdit) throws Exception {
		// FIXME - what should the default be for topics that don't exist?
		if (topic == null) {
			throw new WikiException(new WikiMessage("common.exception.notopic"));
		}
		Utilities.validateTopicName(topic.getName());
		if (topic.getTopicType() == Topic.TYPE_REDIRECT && (request.getParameter("redirect") == null || !request.getParameter("redirect").equalsIgnoreCase("no"))) {
			Topic child = Utilities.findRedirectedTopic(topic, 0);
			if (!child.getName().equals(topic.getName())) {
				pageInfo.setRedirectName(topic.getName());
				pageTitle = new WikiMessage("topic.title", child.getName());
				topic = child;
			}
		}
		String virtualWiki = topic.getVirtualWiki();
		String topicName = topic.getName();
		WikiUser user = Utilities.currentUser();
		if (sectionEdit && !ServletUtil.isEditable(virtualWiki, topicName, user)) {
			sectionEdit = false;
		}
		ParserInput parserInput = new ParserInput();
		parserInput.setContext(request.getContextPath());
		parserInput.setLocale(request.getLocale());
		parserInput.setWikiUser(user);
		parserInput.setTopicName(topicName);
		parserInput.setUserIpAddress(request.getRemoteAddr());
		parserInput.setVirtualWiki(virtualWiki);
		parserInput.setAllowSectionEdit(sectionEdit);
		ParserDocument parserDocument = new ParserDocument();
		String content = Utilities.parse(parserInput, parserDocument, topic.getTopicContent());
		// FIXME - the null check should be unnecessary
		if (parserDocument != null && parserDocument.getCategories().size() > 0) {
			LinkedHashMap categories = new LinkedHashMap();
			for (Iterator iterator = parserDocument.getCategories().keySet().iterator(); iterator.hasNext();) {
				String key = (String)iterator.next();
				String value = key.substring(NamespaceHandler.NAMESPACE_CATEGORY.length() + NamespaceHandler.NAMESPACE_SEPARATOR.length());
				categories.put(key, value);
			}
			next.addObject("categories", categories);
		}
		topic.setTopicContent(content);
		if (topic.getTopicType() == Topic.TYPE_CATEGORY) {
			loadCategoryContent(next, virtualWiki, topic.getName());
		}
		if (topic.getTopicType() == Topic.TYPE_IMAGE || topic.getTopicType() == Topic.TYPE_FILE) {
			Collection fileVersions = WikiBase.getDataHandler().getAllWikiFileVersions(virtualWiki, topicName, true);
			for (Iterator iterator = fileVersions.iterator(); iterator.hasNext();) {
				// update version urls to include web root path
				WikiFileVersion fileVersion = (WikiFileVersion)iterator.next();
				String url = FilenameUtils.normalize(Environment.getValue(Environment.PROP_FILE_DIR_RELATIVE_PATH) + "/" + fileVersion.getUrl());
				url = FilenameUtils.separatorsToUnix(url);
				fileVersion.setUrl(url);
			}
			next.addObject("fileVersions", fileVersions);
			if (topic.getTopicType() == Topic.TYPE_IMAGE) {
				next.addObject("topicImage", new Boolean(true));
			} else {
				next.addObject("topicFile", new Boolean(true));
			}
		}
		pageInfo.setSpecial(false);
		pageInfo.setTopicName(topicName);
		next.addObject(ServletUtil.PARAMETER_TOPIC_OBJECT, topic);
		if (pageTitle != null) {
			pageInfo.setPageTitle(pageTitle);
		}
	}
	
	/**
	 * Utility method for setting a cookie.  This method will overwrite an existing
	 * cookie of the same name if such a cookie already exists.
	 *
	 * @param response The servlet response object.
	 * @param cookieName The name of the cookie to be set.
	 * @param cookieValue The value of the cookie to be set.
	 * @param cookieAge The length of time before the cookie expires, specified in seconds.
	 * @throws Exception Thrown if any error occurs while setting cookie values.
	 */
	public static void addCookie(HttpServletResponse response, String cookieName, String cookieValue, int cookieAge) throws Exception {
		Cookie cookie = null;
		// after confirming credentials
		cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(cookieAge);
		response.addCookie(cookie);
	}
	
	/**
	 * Create a pagination object based on parameters found in the current
	 * request.
	 *
	 * @param request The servlet request object.
	 * @param next A ModelAndView object corresponding to the page being
	 *  constructed.
	 * @return A Pagination object constructed from parameters found in the
	 *  request object.
	 */
	public static Pagination buildPagination(HttpServletRequest request, ModelAndView next) {
		int num = Environment.getIntValue(Environment.PROP_RECENT_CHANGES_NUM);
		if (request.getParameter("num") != null) {
			try {
				num = new Integer(request.getParameter("num")).intValue();
			} catch (Exception e) {
				// invalid number
			}
		}
		int offset = 0;
		if (request.getParameter("offset") != null) {
			try {
				offset = new Integer(request.getParameter("offset")).intValue();
			} catch (Exception e) {
				// invalid number
			}
		}
		if (next != null) {
			next.addObject("num", new Integer(num));
			next.addObject("offset", new Integer(offset));
		}
		return new Pagination(num, offset);
	}
	
	
	/**
	 * Retrieve the current logged-in user's watchlist from the session.  If
	 * there is no watchlist return an empty watchlist.
	 *
	 * @param request The servlet request object.
	 * @param virtualWiki The virtual wiki for the watchlist being parsed.
	 * @return The current logged-in user's watchlist, or an empty watchlist
	 *  if there is no watchlist in the session.
	 */
	public static Watchlist currentWatchlist(HttpServletRequest request, String virtualWiki) throws Exception {
		// get watchlist stored in session
		Watchlist watchlist = (Watchlist)request.getSession().getAttribute(ServletUtil.PARAMETER_WATCHLIST);
		if (watchlist != null) {
			return watchlist;
		}
		// no watchlist in session, retrieve from database
		watchlist = new Watchlist();
		WikiUser user = Utilities.currentUser();
		if (!user.hasRole(Role.ROLE_USER)) {
			return watchlist;
		}
		watchlist = WikiBase.getDataHandler().getWatchlist(virtualWiki, user.getUserId());
		request.getSession().setAttribute(ServletUtil.PARAMETER_WATCHLIST, watchlist);
		return watchlist;
	}
	
	/**
	 * Retrieve a virtual wiki name from the servlet request.  This method
	 * will retrieve a request parameter matching the PARAMETER_VIRTUAL_WIKI
	 * value, and will decode it appropriately.
	 *
	 * @param request The servlet request object.
	 * @return The decoded virtual wiki name retrieved from the request.
	 */
	public static String getVirtualWikiFromRequest(HttpServletRequest request) {
		String virtualWiki = request.getParameter(ServletUtil.PARAMETER_VIRTUAL_WIKI);
		if (virtualWiki == null) {
			virtualWiki = (String)request.getAttribute(ServletUtil.PARAMETER_VIRTUAL_WIKI);
		}
		if (virtualWiki == null) {
			return null;
		}
		return Utilities.decodeFromRequest(virtualWiki);
	}

	/**
	 * Retrieve a virtual wiki name from the request URI.  This method will
	 * retrieve the portion of the URI that immediately follows the servlet
	 * context and decode it appropriately.
	 *
	 * @param request The servlet request object.
	 * @return The decoded virtual wiki name retrieved from the URI.
	 */
	public static String getVirtualWikiFromURI(HttpServletRequest request) {
		String uri = retrieveDirectoriesFromURI(request, 0);
		if (uri == null) {
			logger.warning("No virtual wiki found in URL: " + request.getRequestURI());
			return null;
		}
		int slashIndex = uri.indexOf('/');
		if (slashIndex == -1) {
			logger.warning("No virtual wiki found in URL: " + request.getRequestURI());
			return null;
		}
		String virtualWiki = uri.substring(0, slashIndex);
		return Utilities.decodeFromURL(virtualWiki);
	}

	/**
	 * Finds the current WikiUser object in the request and determines
	 * if that user is an admin.
	 *
	 * @param request The current servlet request object.
	 * @return <code>true</code> if the current request contains a valid user
	 *  object and if that user is an admin, <code>false</code> otherwise.
	 */
	public static boolean isAdmin(HttpServletRequest request) throws Exception {
		WikiUser user = Utilities.currentUser();
		return (user.hasRole(Role.ROLE_ADMIN));
	}
	/**
	 * When editing a section of a topic, this method provides a way of slicing
	 * out a given section of the raw topic content.
	 *
	 * @param request The servlet request object.
	 * @param virtualWiki The virtual wiki for the topic being parsed.
	 * @param topicName The name of the topic being parsed.
	 * @param targetSection The section to be sliced and returned.
	 * @return Returns the raw topic content for the target section.
	 * @throws Exception Thrown if a parser error occurs.
	 */
	public static String parseSlice(HttpServletRequest request, String virtualWiki, String topicName, int targetSection) throws Exception {
		Topic topic = WikiBase.getDataHandler().lookupTopic(virtualWiki, topicName, false, null);
		if (topic == null || topic.getTopicContent() == null) {
			return null;
		}
		ParserInput parserInput = new ParserInput();
		parserInput.setContext(request.getContextPath());
		parserInput.setLocale(request.getLocale());
		parserInput.setTopicName(topicName);
		parserInput.setVirtualWiki(virtualWiki);
		AbstractParser parser = Utilities.parserInstance(parserInput);
		ParserDocument parserDocument = new ParserDocument();
		return parser.parseSlice(parserDocument, topic.getTopicContent(), targetSection);
	}

	/**
	 * When editing a section of a topic, this method provides a way of splicing
	 * an edited section back into the raw topic content.
	 *
	 * @param parserDocument A ParserDocument object containing parser
	 *  metadata output.
	 * @param request The servlet request object.
	 * @param virtualWiki The virtual wiki for the topic being parsed.
	 * @param topicName The name of the topic being parsed.
	 * @param targetSection The section to be sliced and returned.
	 * @param replacementText The edited content that is to be spliced back into
	 *  the raw topic.
	 * @return The raw topic content including the new replacement text.
	 * @throws Exception Thrown if a parser error occurs.
	 */
	public static String parseSplice(ParserDocument parserDocument, HttpServletRequest request, String virtualWiki, String topicName, int targetSection, String replacementText) throws Exception {
		Topic topic = WikiBase.getDataHandler().lookupTopic(virtualWiki, topicName, false, null);
		if (topic == null || topic.getTopicContent() == null) {
			return null;
		}
		ParserInput parserInput = new ParserInput();
		parserInput.setContext(request.getContextPath());
		parserInput.setLocale(request.getLocale());
		parserInput.setTopicName(topicName);
		parserInput.setVirtualWiki(virtualWiki);
		AbstractParser parser = Utilities.parserInstance(parserInput);
		return parser.parseSplice(parserDocument, topic.getTopicContent(), targetSection, replacementText);
	}
	/**
	 * Utility method for retrieving values from the URI.  This method
	 * will attempt to properly convert the URI encoding, and then offers a way
	 * to return directories after the initial context directory.  For example,
	 * if the URI is "/context/first/second/third" and this method is called
	 * with a skipCount of 1, the return value is "second/third".
	 *
	 * @param request The servlet request object.
	 * @param skipCount The number of directories to skip.
	 * @return A UTF-8 encoded portion of the URL that skips the web application
	 *  context and skipCount directories, or <code>null</code> if the number of
	 *  directories is less than skipCount.
	 */
	private static String retrieveDirectoriesFromURI(HttpServletRequest request, int skipCount) {
		String uri = request.getRequestURI().trim();
		// FIXME - needs testing on other platforms
		uri = Utilities.convertEncoding(uri, "ISO-8859-1", "UTF-8");
		String contextPath = request.getContextPath().trim();
		if (!StringUtils.hasText(uri) || contextPath == null) {
			return null;
		}
		uri = uri.substring(contextPath.length() + 1);
		int i = 0;
		while (i < skipCount) {
			int slashIndex = uri.indexOf('/');
			if (slashIndex == -1) {
				return null;
			}
			uri = uri.substring(slashIndex + 1);
			i++;
		}
		return uri;
	}
	/**
	 * Users can specify a default locale in their preferences, so determine
	 * if the current user is logged-in and has chosen a locale.  If not, use
	 * the default locale from the request object.
	 *
	 * @param request The request object for the HTTP request.
	 * @return Either the user's default locale (for logged-in users) or the
	 *  locale specified in the request if no default locale is available.
	 */
	public static Locale retrieveUserLocale(HttpServletRequest request) {
		WikiUser user = null;
		try {
			user = Utilities.currentUser();
			if (user.getDefaultLocale() != null) {
				return Utilities.buildLocale(user.getDefaultLocale());
			}
		} catch (AuthenticationCredentialsNotFoundException e) {
			// ignore
		}
		return request.getLocale();
	}

	
	
	/**
	 * Validate that vital system properties, such as database connection settings,
	 * have been specified properly.
	 *
	 * @param props The property object to validate against.
	 * @return A Vector of WikiMessage objects containing any errors encountered,
	 *  or an empty Vector if no errors are encountered.
	 */
	public static Vector validateSystemSettings(Properties props) {
		Vector errors = new Vector();
		// test directory permissions & existence
		WikiMessage baseDirError = Utilities.validateDirectory(props.getProperty(Environment.PROP_BASE_FILE_DIR));
		if (baseDirError != null) {
			errors.add(baseDirError);
		}
		WikiMessage fullDirError = Utilities.validateDirectory(props.getProperty(Environment.PROP_FILE_DIR_FULL_PATH));
		if (fullDirError != null) {
			errors.add(fullDirError);
		}
		String classesDir = null;
		try {
			classesDir = Utilities.getClassLoaderRoot().getPath();
			WikiMessage classesDirError = Utilities.validateDirectory(classesDir);
			if (classesDirError != null) {
				errors.add(classesDirError);
			}
		} catch (Exception e) {
			errors.add(new WikiMessage("error.directorywrite", classesDir, e.getMessage()));
		}
		// test database
		String driver = props.getProperty(Environment.PROP_DB_DRIVER);
		String url = props.getProperty(Environment.PROP_DB_URL);
		String userName = props.getProperty(Environment.PROP_DB_USERNAME);
		String password = Encryption.getEncryptedProperty(Environment.PROP_DB_PASSWORD, props);
		try {
			DatabaseConnection.testDatabase(driver, url, userName, password, false);
		} catch (Exception e) {
			logger.severe("Invalid database settings", e);
			errors.add(new WikiMessage("error.databaseconnection", e.getMessage()));
		}
		// verify valid parser class
		boolean validParser = true;
		String parserClass = props.getProperty(Environment.PROP_PARSER_CLASS);
		String abstractParserClass = "org.jamwiki.parser.AbstractParser";
		if (parserClass == null || parserClass.equals(abstractParserClass)) {
			validParser = false;
		}
		try {
			Class parent = ClassUtils.forName(parserClass);
			Class child = ClassUtils.forName(abstractParserClass);
			if (!child.isAssignableFrom(parent)) {
				validParser = false;
			}
		} catch (Exception e) {
			validParser = false;
		}
		if (!validParser) {
			errors.add(new WikiMessage("error.parserclass", parserClass));
		}
		return errors;
	}

}
