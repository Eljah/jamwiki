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
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.sf.ehcache.Element;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.WikiException;
import org.jamwiki.WikiMessage;
import org.jamwiki.authentication.JAMWikiAuthenticationConstants;
import org.jamwiki.authentication.WikiUserDetails;
import org.jamwiki.db.DatabaseConnection;
import org.jamwiki.model.Category;
import org.jamwiki.model.Role;
import org.jamwiki.model.Topic;
import org.jamwiki.model.VirtualWiki;
import org.jamwiki.model.Watchlist;
import org.jamwiki.model.WikiFileVersion;
import org.jamwiki.model.WikiUser;
import org.jamwiki.parser.ParserInput;
import org.jamwiki.parser.ParserOutput;
import org.jamwiki.parser.ParserUtil;
import org.jamwiki.utils.Encryption;
import org.jamwiki.utils.LinkUtil;
import org.jamwiki.utils.NamespaceHandler;
import org.jamwiki.utils.Pagination;
import org.jamwiki.utils.SpamFilter;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiCache;
import org.jamwiki.utils.WikiLink;
import org.jamwiki.utils.WikiLogger;
import org.jamwiki.utils.WikiUtil;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationCredentialsNotFoundException;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;

/**
 * Utility methods useful when processing JAMWiki servlet requests.
 */
public class ServletUtil {

	private static final WikiLogger logger = WikiLogger.getLogger(ServletUtil.class.getName());
	/** The name of the JSP file used to render the servlet output for logins. */
	protected static final String JSP_LOGIN = "login.jsp";
	/** The name of the output parameter used to store page information. */
	public static final String PARAMETER_PAGE_INFO = "pageInfo";
	/** The name of the output parameter used to store topic information. */
	public static final String PARAMETER_TOPIC_OBJECT = "topicObject";
	/** The name of the output parameter used to indicate that Spring should redirect to another servlet. */
	protected static final String SPRING_REDIRECT_PREFIX = "redirect:";

	/**
	 *
	 */
	private ServletUtil() {
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
				content = ParserUtil.parse(parserInput, null, content);
			}
			WikiCache.addToCache(WikiBase.CACHE_PARSED_TOPIC_CONTENT, key, content);
		} catch (Exception e) {
			logger.warning("error getting cached page " + virtualWiki + " / " + topicName, e);
			return null;
		}
		return content;
	}

	/**
	 * This is a utility method that will check topic content for spam, and
	 * return <code>null</code> if no matching values are found, or if a spam
	 * pattern is found then that pattern will be returned.  It will also log
	 * information about the offending spam and user to the logs.
	 *
	 * @param request The current servlet request.
	 * @param topicName The name of the current topic being edited.
	 * @param contents The text for the current topic that the user is trying to
	 *  add.
	 * @return <code>null</code> if nothing in the topic content matches a current
	 *  spam pattern, or the text that matches a spam pattern if one is found.
	 */
	protected static String checkForSpam(HttpServletRequest request, String topicName, String contents) throws Exception {
		String result = SpamFilter.containsSpam(contents);
		if (StringUtils.isBlank(result)) {
			return null;
		}
		String message = "SPAM found in topic " + topicName + " (";
		WikiUserDetails user = ServletUtil.currentUserDetails();
		if (!user.hasRole(Role.ROLE_ANONYMOUS)) {
			message += user.getUsername() + " / ";
		}
		message += ServletUtil.getIpAddress(request) + "): " + result;
		logger.info(message);
		return result;
	}

	/**
	 * Retrieve the current <code>WikiUserDetails</code> from Spring Security
	 * <code>SecurityContextHolder</code>.  If the current user is not
	 * logged-in then this method will return an empty <code>WikiUserDetails</code>
	 * object.
	 *
	 * @return The current logged-in <code>WikiUserDetails</code>, or an empty
	 *  <code>WikiUserDetails</code> if there is no user currently logged in.
	 *  This method will never return <code>null</code>.
	 * @throws AuthenticationCredentialsNotFoundException If authentication
	 *  credentials are unavailable.
	 */
	public static WikiUserDetails currentUserDetails() throws AuthenticationCredentialsNotFoundException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return WikiUserDetails.initWikiUserDetails(auth);
	}

	/**
	 * Retrieve the current <code>WikiUser</code> using the <code>WikiUserDetails</code>
	 * from Spring Security <code>SecurityContextHolder</code>.  If there is no current
	 * user (the user is not logged in) then this method will return an empty WikiUser.
	 * The method will never return <code>null</code>.
	 *
	 * @return The current logged-in <code>WikiUser</code>, or an empty WikiUser if
	 *  there is no user currently logged in.
	 */
	public static WikiUser currentWikiUser() throws AuthenticationCredentialsNotFoundException {
		WikiUserDetails userDetails = ServletUtil.currentUserDetails();
		WikiUser user = new WikiUser();
		String username = userDetails.getUsername();
		if (username.equals(WikiUserDetails.ANONYMOUS_USER_USERNAME)) {
			return user;
		}
		if (!WikiUtil.isFirstUse() && !WikiUtil.isUpgrade()) {
			try {
				// FIXME - do not lookup the user every time this method is called, that will kill performance
				user = WikiBase.getDataHandler().lookupWikiUser(username);
				if (user == null) {
					// invalid user.  someone has either spoofed a cookie or the user account is no longer in
					// the database.
					logger.warning("No user exists for principal found in security context authentication: " + username);
					SecurityContextHolder.clearContext();
					throw new AuthenticationCredentialsNotFoundException("Invalid user credentials found - username " + username + " does not exist in this wiki installation");
				}
			} catch (Exception e) {
				logger.severe("Failure while retrieving user from database with login: " + username, e);
			}
		}
		return user;
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
		// try to get watchlist stored in session
		if (request.getSession(false) != null) {
			Watchlist watchlist = (Watchlist)request.getSession(false).getAttribute(WikiUtil.PARAMETER_WATCHLIST);
			if (watchlist != null) {
				return watchlist;
			}
		}
		// no watchlist in session, retrieve from database
		WikiUserDetails userDetails = ServletUtil.currentUserDetails();
		Watchlist watchlist = new Watchlist();
		if (userDetails.hasRole(Role.ROLE_ANONYMOUS)) {
			return watchlist;
		}
		WikiUser user = ServletUtil.currentWikiUser();
		watchlist = WikiBase.getDataHandler().getWatchlist(virtualWiki, user.getUserId());
		if (request.getSession(false) != null) {
			// add watchlist to session
			request.getSession(false).setAttribute(WikiUtil.PARAMETER_WATCHLIST, watchlist);
		}
		return watchlist;
	}

	/**
	 * Duplicate the functionality of the request.getRemoteAddr() method, but
	 * for IPv6 addresses strip off any local interface information (anything
	 * following a "%").
	 *
	 * @param request the HTTP request object.
	 * @return The IP address that the request originated from, or 0.0.0.0 if
	 *  the originating address cannot be determined.
	 */
	public static String getIpAddress(HttpServletRequest request) {
		if (request == null) {
			throw new IllegalArgumentException("Request object cannot be null");
		}
		String ipAddress = request.getRemoteAddr();
		int pos = ipAddress.indexOf("%");
		if (pos != -1) {
			ipAddress = ipAddress.substring(0, pos);
		}
		if (!Utilities.isIpAddress(ipAddress)) {
			logger.info("Invalid IP address found in request: " + ipAddress);
			ipAddress = "0.0.0.0";
		}
		return ipAddress;
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
		WikiUtil.validateTopicName(topicName);
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
	protected static boolean isEditable(String virtualWiki, String topicName, WikiUserDetails user) throws Exception {
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
		if (topic.getAdminOnly() && !user.hasRole(Role.ROLE_ADMIN)) {
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
	protected static boolean isMoveable(String virtualWiki, String topicName, WikiUserDetails user) throws Exception {
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
		if (topic.getAdminOnly() && !user.hasRole(Role.ROLE_ADMIN)) {
			return false;
		}
		return true;
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
			String topic = WikiUtil.getTopicFromURI(request);
			if (StringUtils.isBlank(topic)) {
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
		List categoryTopics = WikiBase.getDataHandler().lookupCategoryTopics(virtualWiki, topicName);
		List categoryImages = new Vector();
		LinkedHashMap subCategories = new LinkedHashMap();
		int i = 0;
		// loop through the results and split out images and sub-categories
		while (i < categoryTopics.size()) {
			Category category = (Category)categoryTopics.get(i);
			if (category.getTopicType() == Topic.TYPE_IMAGE) {
				categoryTopics.remove(i);
				categoryImages.add(category);
				continue;
			}
			if (category.getTopicType() == Topic.TYPE_CATEGORY) {
				categoryTopics.remove(i);
				String value = category.getChildTopicName().substring(NamespaceHandler.NAMESPACE_CATEGORY.length() + NamespaceHandler.NAMESPACE_SEPARATOR.length());
				subCategories.put(category.getChildTopicName(), value);
				continue;
			}
			i++;
		}
		next.addObject("categoryTopics", categoryTopics);
		next.addObject("numCategoryTopics", new Integer(categoryTopics.size()));
		next.addObject("categoryImages", categoryImages);
		next.addObject("numCategoryImages", new Integer(categoryImages.size()));
		next.addObject("subCategories", subCategories);
		next.addObject("numSubCategories", new Integer(subCategories.size()));
	}

	/**
	 * Create a Pagination object and load all necessary values into the
	 * request for processing by a JSP.
	 *
	 * @param request The servlet request object.
	 * @param next A ModelAndView object corresponding to the page being
	 *  constructed.
	 * @return A Pagination object constructed from parameters found in the
	 *  request object.
	 */
	public static Pagination loadPagination(HttpServletRequest request, ModelAndView next) {
		if (next == null) {
			throw new IllegalArgumentException("A non-null ModelAndView object must be specified when loading pagination values");
		}
		Pagination pagination = WikiUtil.buildPagination(request);
		next.addObject("num", new Integer(pagination.getNumResults()));
		next.addObject("offset", new Integer(pagination.getOffset()));
		return pagination;
	}

	/**
	 * Utility method for parsing a multipart servlet request.  This method returns
	 * an iterator of FileItem objects that corresponds to the request.
	 *
	 * @param request The servlet request containing the multipart request.
	 * @param uploadDirectory The directory into which files will be uploaded.
	 * @param maxFileSize The maximum allowed file size in bytes.
	 * @return Returns an iterator of FileItem objects the corresponds to the request.
	 * @throws Exception Thrown if any problems occur while processing the request.
	 */
	public static Iterator processMultipartRequest(HttpServletRequest request, String uploadDirectory, long maxFileSize) throws Exception {
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(new File(uploadDirectory));
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		upload.setSizeMax(maxFileSize);
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
		String target = LinkUtil.buildTopicUrl(null, virtualWiki, destination, true);
		String view = ServletUtil.SPRING_REDIRECT_PREFIX + target;
		next.clear();
		next.setViewName(view);
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
		try {
			WikiUser user = ServletUtil.currentWikiUser();
			if (user.getDefaultLocale() != null) {
				return LocaleUtils.toLocale(user.getDefaultLocale());
			}
		} catch (AuthenticationCredentialsNotFoundException e) {
			// ignore
		}
		return request.getLocale();
	}

	/**
	 * Given a virtual wiki name, return a <code>VirtualWiki</code> object.
	 * If there is no virtual wiki available with the given name then the
	 * default virtual wiki is returned.
	 *
	 * @param virtualWikiName The name of the virtual wiki that is being
	 *  retrieved.
	 * @return A <code>VirtualWiki</code> object.  If there is no virtual
	 *  wiki available with the given name then the default virtual wiki is
	 *  returned.
	 */
	public static VirtualWiki retrieveVirtualWiki(String virtualWikiName) {
		VirtualWiki virtualWiki = null;
		if (virtualWikiName == null) {
			virtualWikiName = WikiBase.DEFAULT_VWIKI;
		}
		// FIXME - the check here for initialized properties is due to this
		// change being made late in a release cycle.  Revisit in a future
		// release & clean this up.
		if (Environment.getBooleanValue(Environment.PROP_BASE_INITIALIZED)) {
			try {
				virtualWiki = WikiBase.getDataHandler().lookupVirtualWiki(virtualWikiName);
			} catch (Exception e) {}
		}
		if (virtualWiki == null) {
			logger.severe("No virtual wiki found for " + virtualWikiName);
			virtualWiki = new VirtualWiki();
			virtualWiki.setName(WikiBase.DEFAULT_VWIKI);
			virtualWiki.setDefaultTopicName(Environment.getValue(Environment.PROP_BASE_DEFAULT_TOPIC));
		}
		return virtualWiki;
	}

	/**
	 * Validate that vital system properties, such as database connection settings,
	 * have been specified properly.
	 *
	 * @param props The property object to validate against.
	 * @return A Vector of WikiMessage objects containing any errors encountered,
	 *  or an empty Vector if no errors are encountered.
	 */
	protected static Vector validateSystemSettings(Properties props) {
		Vector errors = new Vector();
		// test directory permissions & existence
		WikiMessage baseDirError = WikiUtil.validateDirectory(props.getProperty(Environment.PROP_BASE_FILE_DIR));
		if (baseDirError != null) {
			errors.add(baseDirError);
		}
		WikiMessage fullDirError = WikiUtil.validateDirectory(props.getProperty(Environment.PROP_FILE_DIR_FULL_PATH));
		if (fullDirError != null) {
			errors.add(fullDirError);
		}
		String classesDir = null;
		try {
			classesDir = Utilities.getClassLoaderRoot().getPath();
			WikiMessage classesDirError = WikiUtil.validateDirectory(classesDir);
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
			Class parent = ClassUtils.getClass(parserClass);
			Class child = ClassUtils.getClass(abstractParserClass);
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
		String virtualWikiName = pageInfo.getVirtualWikiName();
		String target = request.getParameter(JAMWikiAuthenticationConstants.SPRING_SECURITY_LOGIN_TARGET_URL_FIELD_NAME);
		if (StringUtils.isBlank(target)) {
			if (StringUtils.isBlank(topic)) {
				VirtualWiki virtualWiki = WikiBase.getDataHandler().lookupVirtualWiki(virtualWikiName);
				topic = virtualWiki.getDefaultTopicName();
			}
			target = "/" + virtualWikiName + "/" + topic;
			if (!StringUtils.isBlank(request.getQueryString())) {
				target += "?" + request.getQueryString();
			}
		}
		next.addObject("springSecurityTargetUrlField", JAMWikiAuthenticationConstants.SPRING_SECURITY_LOGIN_TARGET_URL_FIELD_NAME);
		HttpSession session = request.getSession(false);
		if (request.getRequestURL().indexOf(request.getRequestURI()) != -1 && (session == null || session.getAttribute(JAMWikiAuthenticationConstants.SPRING_SECURITY_SAVED_REQUEST_SESSION_KEY) == null)) {
			// Only add a target URL if Spring Security has not saved a request in the session.  The request
			// URL vs URI check is needed due to the fact that the first time a user is redirected by Spring
			// Security to the login page the saved request attribute is not yet available in the session
			// due to weirdness and magic which I've thus far been unable to track down, so comparing the URI
			// to the URL provides a way of determining if the user was redirected.  Anyone who can create
			// a check that reliably captures whether or not Spring Security has a saved request should
			// feel free to modify the conditional above.
			next.addObject("springSecurityTargetUrl", target);
		}
		String springSecurityLoginUrl = "/" + virtualWikiName + JAMWikiAuthenticationConstants.SPRING_SECURITY_LOGIN_URL;
		next.addObject("springSecurityLoginUrl", springSecurityLoginUrl);
		next.addObject("springSecurityUsernameField", JAMWikiAuthenticationConstants.SPRING_SECURITY_LOGIN_USERNAME_FIELD_NAME);
		next.addObject("springSecurityPasswordField", JAMWikiAuthenticationConstants.SPRING_SECURITY_LOGIN_PASSWORD_FIELD_NAME);
		next.addObject("springSecurityRememberMeField", JAMWikiAuthenticationConstants.SPRING_SECURITY_LOGIN_REMEMBER_ME_FIELD_NAME);
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
		WikiUtil.validateTopicName(topic.getName());
		if (topic.getTopicType() == Topic.TYPE_REDIRECT && (request.getParameter("redirect") == null || !request.getParameter("redirect").equalsIgnoreCase("no"))) {
			Topic child = WikiUtil.findRedirectedTopic(topic, 0);
			if (!child.getName().equals(topic.getName())) {
				String redirectUrl = LinkUtil.buildTopicUrl(request.getContextPath(), topic.getVirtualWiki(), topic.getName(), true);
				// FIXME - hard coding
				redirectUrl += LinkUtil.appendQueryParam("", "redirect", "no");
				String redirectName = topic.getName();
				pageInfo.setRedirectInfo(redirectUrl, redirectName);
				pageTitle = new WikiMessage("topic.title", child.getName());
				topic = child;
				// update the page info's virtual wiki in case this redirect is to another virtual wiki
				pageInfo.setVirtualWikiName(topic.getVirtualWiki());
			}
		}
		String virtualWiki = topic.getVirtualWiki();
		String topicName = topic.getName();
		WikiUserDetails userDetails = ServletUtil.currentUserDetails();
		if (sectionEdit && !ServletUtil.isEditable(virtualWiki, topicName, userDetails)) {
			sectionEdit = false;
		}
		WikiUser user = ServletUtil.currentWikiUser();
		ParserInput parserInput = new ParserInput();
		parserInput.setContext(request.getContextPath());
		parserInput.setLocale(request.getLocale());
		parserInput.setWikiUser(user);
		parserInput.setTopicName(topicName);
		parserInput.setUserIpAddress(ServletUtil.getIpAddress(request));
		parserInput.setVirtualWiki(virtualWiki);
		parserInput.setAllowSectionEdit(sectionEdit);
		ParserOutput parserOutput = new ParserOutput();
		String content = ParserUtil.parse(parserInput, parserOutput, topic.getTopicContent());
		if (parserOutput.getCategories().size() > 0) {
			LinkedHashMap categories = new LinkedHashMap();
			for (Iterator iterator = parserOutput.getCategories().keySet().iterator(); iterator.hasNext();) {
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
}