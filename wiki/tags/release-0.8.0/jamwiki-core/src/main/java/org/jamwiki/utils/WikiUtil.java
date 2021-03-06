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
package org.jamwiki.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jamwiki.DataAccessException;
import org.jamwiki.DataHandler;
import org.jamwiki.Environment;
import org.jamwiki.SearchEngine;
import org.jamwiki.WikiBase;
import org.jamwiki.WikiException;
import org.jamwiki.WikiMessage;
import org.jamwiki.WikiVersion;
import org.jamwiki.model.Role;
import org.jamwiki.model.Topic;
import org.jamwiki.model.VirtualWiki;

/**
 * This class provides a variety of general utility methods for handling
 * wiki-specific functionality such as retrieving topics from the URL.
 */
public class WikiUtil {

	private static final WikiLogger logger = WikiLogger.getLogger(WikiUtil.class.getName());

	/** webapp context path, initialized from JAMWikiFilter. */
	public static String WEBAPP_CONTEXT_PATH = null;
	private static Pattern INVALID_ROLE_NAME_PATTERN = null;
	private static Pattern INVALID_TOPIC_NAME_PATTERN = null;
	private static Pattern VALID_USER_LOGIN_PATTERN = null;
	public static final String PARAMETER_TOPIC = "topic";
	public static final String PARAMETER_VIRTUAL_WIKI = "virtualWiki";
	public static final String PARAMETER_WATCHLIST = "watchlist";

	static {
		try {
			INVALID_ROLE_NAME_PATTERN = Pattern.compile(Environment.getValue(Environment.PROP_PATTERN_INVALID_ROLE_NAME));
			INVALID_TOPIC_NAME_PATTERN = Pattern.compile(Environment.getValue(Environment.PROP_PATTERN_INVALID_TOPIC_NAME));
			VALID_USER_LOGIN_PATTERN = Pattern.compile(Environment.getValue(Environment.PROP_PATTERN_VALID_USER_LOGIN));
		} catch (PatternSyntaxException e) {
			logger.severe("Unable to compile pattern", e);
		}
	}

	/**
	 * Create a pagination object based on parameters found in the current
	 * request.
	 *
	 * @param request The servlet request object.
	 * @return A Pagination object constructed from parameters found in the
	 *  request object.
	 */
	public static Pagination buildPagination(HttpServletRequest request) {
		int num = Environment.getIntValue(Environment.PROP_RECENT_CHANGES_NUM);
		if (request.getParameter("num") != null) {
			try {
				num = Integer.parseInt(request.getParameter("num"));
			} catch (NumberFormatException e) {
				// invalid number
			}
		}
		int offset = 0;
		if (request.getParameter("offset") != null) {
			try {
				offset = Integer.parseInt(request.getParameter("offset"));
			} catch (NumberFormatException e) {
				// invalid number
			}
		}
		return new Pagination(num, offset);
	}

	/**
	 * Utility method to retrieve an instance of the current data handler.
	 *
	 * @return An instance of the current data handler.
	 * @throws IOException Thrown if a data handler instance can not be
	 *  instantiated.
	 */
	public static DataHandler dataHandlerInstance() throws IOException {
		if (Environment.getValue(Environment.PROP_DB_TYPE) == null) {
			// this is a problem, but it should never occur
			logger.warning("WikiUtil.dataHandlerInstance called without a valid PROP_DB_TYPE value");
		}
		String dataHandlerClass = Environment.getValue(Environment.PROP_DB_TYPE);
		try {
			return (DataHandler)Utilities.instantiateClass(dataHandlerClass);
		} catch (ClassCastException e) {
			throw new IllegalStateException("Data handler specified in jamwiki.properties does not implement org.jamwiki.DataHandler: " + dataHandlerClass);
		}
	}

	/**
	 * Convert a topic name or other value into a value suitable for use as a
	 * file name.  This method replaces spaces with underscores, and then URL
	 * encodes the value.
	 *
	 * @param name The value that is to be encoded for use as a file name.
	 * @return The encoded value.
	 */
	public static String encodeForFilename(String name) {
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("File name not specified in encodeForFilename");
		}
		// replace spaces with underscores
		String result = Utilities.encodeTopicName(name);
		// URL encode the rest of the name
		try {
			result = URLEncoder.encode(result, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// this should never happen
			throw new IllegalStateException("Unsupporting encoding UTF-8");
		}
		return result;
	}

	/**
	 * Given an article name, return the appropriate comments topic article name.
	 * For example, if the article name is "Topic" then the return value is
	 * "Comments:Topic".
	 *
	 * @param name The article name from which a comments article name is to
	 *  be constructed.
	 * @return The comments article name for the article name.
	 */
	public static String extractCommentsLink(String name) {
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("Topic name must not be empty in extractCommentsLink");
		}
		WikiLink wikiLink = LinkUtil.parseWikiLink(name);
		if (StringUtils.isBlank(wikiLink.getNamespace())) {
			return NamespaceHandler.NAMESPACE_COMMENTS + NamespaceHandler.NAMESPACE_SEPARATOR + name;
		}
		String namespace = wikiLink.getNamespace();
		String commentsNamespace = NamespaceHandler.getCommentsNamespace(namespace);
		return (!StringUtils.isBlank(commentsNamespace)) ? commentsNamespace + NamespaceHandler.NAMESPACE_SEPARATOR + wikiLink.getArticle() : NamespaceHandler.NAMESPACE_COMMENTS + NamespaceHandler.NAMESPACE_SEPARATOR + wikiLink.getArticle();
	}

	/**
	 * Given an article name, extract an appropriate topic article name.  For
	 * example, if the article name is "Comments:Topic" then the return value
	 * is "Topic".
	 *
	 * @param name The article name from which a topic article name is to be
	 *  constructed.
	 * @return The topic article name for the article name.
	 */
	public static String extractTopicLink(String name) {
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("Topic name must not be empty in extractTopicLink");
		}
		WikiLink wikiLink = LinkUtil.parseWikiLink(name);
		if (StringUtils.isBlank(wikiLink.getNamespace())) {
			return name;
		}
		String namespace = wikiLink.getNamespace();
		String mainNamespace = NamespaceHandler.getMainNamespace(namespace);
		return (!StringUtils.isBlank(mainNamespace)) ? mainNamespace + NamespaceHandler.NAMESPACE_SEPARATOR + wikiLink.getArticle() : wikiLink.getArticle();
	}

	/**
	 * Determine the URL for the default virtual wiki topic, not including the application server context.
	 */
	public static String findDefaultVirtualWikiUrl(String virtualWikiName) {
		if (StringUtils.isBlank(virtualWikiName)) {
			virtualWikiName = WikiBase.DEFAULT_VWIKI;
		}
		String target = Environment.getValue(Environment.PROP_BASE_DEFAULT_TOPIC);
		try {
			VirtualWiki virtualWiki = WikiBase.getDataHandler().lookupVirtualWiki(virtualWikiName);
			target = virtualWiki.getDefaultTopicName();
		} catch (DataAccessException e) {
			logger.warning("Unable to retrieve default topic for virtual wiki", e);
		}
		return "/" + virtualWikiName + "/" + target;
	}

	/**
	 * Given a topic type, determine the namespace name.
	 *
	 * @param topicType The topic type.
	 * @return The namespace that matches the topic type.
	 */
	public static String findNamespaceForTopicType(int topicType) {
		switch (topicType) {
			case Topic.TYPE_IMAGE:
			case Topic.TYPE_FILE:
				return NamespaceHandler.NAMESPACE_IMAGE;
			case Topic.TYPE_CATEGORY:
				return NamespaceHandler.NAMESPACE_CATEGORY;
			case Topic.TYPE_SYSTEM_FILE:
				return NamespaceHandler.NAMESPACE_JAMWIKI;
			case Topic.TYPE_TEMPLATE:
				return NamespaceHandler.NAMESPACE_TEMPLATE;
			default:
				return "";
		}
	}

	/**
	 * Given a topic, if that topic is a redirect find the target topic of the redirection.
	 *
	 * @param parent The topic being queried.  If this topic is a redirect then the redirect
	 *  target will be returned, otherwise the topic itself is returned.
	 * @param attempts The maximum number of child topics to follow.  This parameter prevents
	 *  infinite loops if topics redirect back to one another.
	 * @return If the parent topic is a redirect then this method returns the target topic that
	 *  is being redirected to, otherwise the parent topic is returned.
	 * @throws DataAccessException Thrown if any error occurs while retrieving data.
	 */
	public static Topic findRedirectedTopic(Topic parent, int attempts) throws DataAccessException {
		int count = attempts;
		String target = parent.getRedirectTo();
		if (parent.getTopicType() != Topic.TYPE_REDIRECT || StringUtils.isBlank(target)) {
			logger.severe("getRedirectTarget() called for non-redirect topic " + parent.getName());
			return parent;
		}
		// avoid infinite redirection
		count++;
		if (count > 10) {
			//TODO throw new WikiException(new WikiMessage("topic.redirect.infinite"));
			return parent;
		}
		String virtualWiki = parent.getVirtualWiki();
		WikiLink wikiLink = LinkUtil.parseWikiLink(target);
		if (!StringUtils.isBlank(wikiLink.getNamespace()) && WikiBase.getDataHandler().lookupVirtualWiki(wikiLink.getNamespace()) != null) {
			virtualWiki = wikiLink.getNamespace();
			wikiLink.setDestination(wikiLink.getDestination().substring(virtualWiki.length() + NamespaceHandler.NAMESPACE_SEPARATOR.length()));
		}
		// get the topic that is being redirected to
		Topic child = WikiBase.getDataHandler().lookupTopic(virtualWiki, wikiLink.getDestination(), false, null);
		if (child == null) {
			// child being redirected to doesn't exist, return parent
			return parent;
		}
		if (StringUtils.isBlank(child.getRedirectTo())) {
			// found a topic that is not a redirect, return
			return child;
		}
		// child is a redirect, keep looking
		return findRedirectedTopic(child, count);
	}

	/**
	 * Given a namespace name, determine the topic type.
	 *
	 * @param namespace The namespace name.
	 * @return The topic type that matches the namespace.
	 */
	public static int findTopicTypeForNamespace(String namespace) {
		if (namespace != null) {
			if (namespace.equals(NamespaceHandler.NAMESPACE_CATEGORY)) {
				return Topic.TYPE_CATEGORY;
			}
			if (namespace.equals(NamespaceHandler.NAMESPACE_TEMPLATE)) {
				return Topic.TYPE_TEMPLATE;
			}
			if (namespace.equals(NamespaceHandler.NAMESPACE_JAMWIKI)) {
				return Topic.TYPE_SYSTEM_FILE;
			}
			if (namespace.equals(NamespaceHandler.NAMESPACE_IMAGE)) {
				// FIXME - handle TYPE_FILE
				return Topic.TYPE_IMAGE;
			}
		}
		return Topic.TYPE_ARTICLE;
	}

	/**
	 * Return the URL of the index page for the wiki.
	 *
	 * @throws DataAccessException Thrown if any error occurs while retrieving data.
	 */
	public static String getBaseUrl() throws DataAccessException {
		String url = Environment.getValue(Environment.PROP_SERVER_URL);
		url += LinkUtil.buildTopicUrl(WEBAPP_CONTEXT_PATH, WikiBase.DEFAULT_VWIKI, Environment.getValue(Environment.PROP_BASE_DEFAULT_TOPIC), true);
		return url;
	}

	/**
	 * Retrieve a parameter from the servlet request.  This method works around
	 * some issues encountered when retrieving non-ASCII values from URL
	 * parameters.
	 *
	 * @param request The servlet request object.
	 * @param name The parameter name to be retrieved.
	 * @param decodeUnderlines Set to <code>true</code> if underlines should
	 *  be automatically converted to spaces.
	 * @return The decoded parameter value retrieved from the request.
	 */
	public static String getParameterFromRequest(HttpServletRequest request, String name, boolean decodeUnderlines) {
		String value = null;
		if (request.getMethod().equalsIgnoreCase("GET")) {
			// parameters passed via the URL are URL encoded, so request.getParameter may
			// not interpret non-ASCII characters properly.  This code attempts to work
			// around that issue by manually decoding.  yes, this is ugly and it would be
			// great if someone could eventually make it unnecessary.
			String query = request.getQueryString();
			if (StringUtils.isBlank(query)) {
				return null;
			}
			String prefix = name + "=";
			int pos = query.indexOf(prefix);
			if (pos != -1 && (pos + prefix.length()) < query.length()) {
				value = query.substring(pos + prefix.length());
				if (value.indexOf('&') != -1) {
					value = value.substring(0, value.indexOf('&'));
				}
			}
			return Utilities.decodeAndEscapeTopicName(value, decodeUnderlines);
		}
		value = request.getParameter(name);
		if (value == null) {
			value = (String)request.getAttribute(name);
		}
		if (value == null) {
			return null;
		}
		return Utilities.decodeTopicName(value, decodeUnderlines);
	}

	/**
	 * Retrieve a file that represents a "tmp" directory within the wiki system directory.
	 * The caller should test directory.exists() to verify that the directory is available.
	 */
	public static File getTempDirectory() {
		String subdirectory = "tmp";
		File directory = new File(Environment.getValue(Environment.PROP_BASE_FILE_DIR), subdirectory);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		return directory;
	}

	/**
	 * Retrieve a topic name from the servlet request.  This method will
	 * retrieve a request parameter matching the PARAMETER_TOPIC value,
	 * and will decode it appropriately.
	 *
	 * @param request The servlet request object.
	 * @return The decoded topic name retrieved from the request.
	 */
	public static String getTopicFromRequest(HttpServletRequest request) {
		return WikiUtil.getParameterFromRequest(request, WikiUtil.PARAMETER_TOPIC, true);
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
		int pos = topic.indexOf('#');
		if (pos != -1) {
			// strip everything after and including '#'
			if (pos == 0) {
				logger.warning("No topic in URL: " + request.getRequestURI());
				return null;
			}
			topic = topic.substring(0, pos);
		}
		pos = topic.indexOf('?');
		if (pos != -1) {
			// strip everything after and including '?'
			if (pos == 0) {
				logger.warning("No topic in URL: " + request.getRequestURI());
				return null;
			}
			topic = topic.substring(0, pos);
		}
		pos = topic.indexOf(';');
		if (pos != -1) {
			// some servlet containers return parameters of the form ";jsessionid=1234" when getRequestURI is called.
			if (pos == 0) {
				logger.warning("No topic in URL: " + request.getRequestURI());
				return null;
			}
			topic = topic.substring(0, pos);
		}
		if (!StringUtils.isBlank(topic)) {
			topic = Utilities.decodeAndEscapeTopicName(topic, true);
		}
		return topic;
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
		String virtualWiki = request.getParameter(WikiUtil.PARAMETER_VIRTUAL_WIKI);
		if (virtualWiki == null) {
			virtualWiki = (String)request.getAttribute(WikiUtil.PARAMETER_VIRTUAL_WIKI);
		}
		if (virtualWiki == null) {
			return null;
		}
		return Utilities.decodeTopicName(virtualWiki, true);
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
		if (StringUtils.isBlank(uri)) {
			logger.info("No virtual wiki found in URL: " + request.getRequestURI());
			return null;
		}
		// default the virtual wiki to the URI since the user may have accessed a URL of
		// the form /context/virtualwiki with no trailing slash
		String virtualWiki = uri;
		int slashIndex = uri.indexOf('/');
		if (slashIndex != -1) {
			virtualWiki = uri.substring(0, slashIndex);
		}
		return Utilities.decodeAndEscapeTopicName(virtualWiki, true);
	}

	/**
	 * Given a topic name, determine if that name corresponds to a comments
	 * page.
	 *
	 * @param topicName The topic name (non-null) to examine to determine if it
	 *  is a comments page or not.
	 * @return <code>true</code> if the page is a comments page, <code>false</code>
	 *  otherwise.
	 */
	public static boolean isCommentsPage(String topicName) {
		WikiLink wikiLink = LinkUtil.parseWikiLink(topicName);
		if (StringUtils.isBlank(wikiLink.getNamespace())) {
			return false;
		}
		String namespace = wikiLink.getNamespace();
		if (namespace.equals(NamespaceHandler.NAMESPACE_SPECIAL)) {
			return false;
		}
		String commentNamespace = NamespaceHandler.getCommentsNamespace(namespace);
		return namespace.equals(commentNamespace);
	}

	/**
	 * Determine if the system properties file exists and has been initialized.
	 * This method is primarily used to determine whether or not to display
	 * the system setup page or not.
	 *
	 * @return <code>true</code> if the properties file has NOT been initialized,
	 *  <code>false</code> otherwise.
	 */
	public static boolean isFirstUse() {
		return !Environment.getBooleanValue(Environment.PROP_BASE_INITIALIZED);
	}

	/**
	 * Determine if the system code has been upgraded from the configured system
	 * version.  Thus if the system is upgraded, this method returns <code>true</code>
	 *
	 * @return <code>true</code> if the system has been upgraded, <code>false</code>
	 *  otherwise.
	 */
	public static boolean isUpgrade() {
		if (WikiUtil.isFirstUse()) {
			return false;
		}
		WikiVersion oldVersion = new WikiVersion(Environment.getValue(Environment.PROP_BASE_WIKI_VERSION));
		WikiVersion currentVersion = new WikiVersion(WikiVersion.CURRENT_WIKI_VERSION);
		return oldVersion.before(currentVersion);
	}

	/**
	 * Utility method for reading special topic values from files and returning
	 * the file contents.
	 *
	 * @param locale The locale for the user viewing the special page.
	 * @param pageName The name of the special page being retrieved.
	 */
	public static String readSpecialPage(Locale locale, String pageName) throws IOException {
		String contents = null;
		String filename = null;
		String language = null;
		String country = null;
		if (locale != null) {
			language = locale.getLanguage();
			country = locale.getCountry();
		}
		String subdirectory = "";
		if (!StringUtils.isBlank(language) && !StringUtils.isBlank(country)) {
			try {
				subdirectory = new File(WikiBase.SPECIAL_PAGE_DIR, language + "_" + country).getPath();
				filename = new File(subdirectory, WikiUtil.encodeForFilename(pageName) + ".txt").getPath();
				contents = Utilities.readFile(filename);
			} catch (IOException e) {
				logger.info("File " + filename + " does not exist");
			}
		}
		if (contents == null && !StringUtils.isBlank(language)) {
			try {
				subdirectory = new File(WikiBase.SPECIAL_PAGE_DIR, language).getPath();
				filename = new File(subdirectory, WikiUtil.encodeForFilename(pageName) + ".txt").getPath();
				contents = Utilities.readFile(filename);
			} catch (IOException e) {
				logger.info("File " + filename + " does not exist");
			}
		}
		if (contents == null) {
			try {
				subdirectory = new File(WikiBase.SPECIAL_PAGE_DIR).getPath();
				filename = new File(subdirectory, WikiUtil.encodeForFilename(pageName) + ".txt").getPath();
				contents = Utilities.readFile(filename);
			} catch (IOException e) {
				logger.warning("File " + filename + " could not be read", e);
				throw e;
			}
		}
		return contents;
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
		if (StringUtils.isBlank(uri) || contextPath == null) {
			return null;
		}
		// make sure there are no instances of "//" in the URL
		uri = uri.replaceAll("(/){2,}", "/");
		if (uri.length() <= contextPath.length()) {
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
	 * If a blacklist or whitelist of allowed file upload types is being used,
	 * retrieve the list from the properties file and return as a List object.
	 * If no such list is being used then return an empty List object.
	 *
	 * @return A list consisting of lowercase versions of all file extensions
	 *  for the whitelist/blacklist.  Entries in the list are of the form
	 *  "txt", not ".txt".
	 */
	public static List retrieveUploadFileList() {
		List<String> list = new ArrayList<String>();
		int blacklistType = Environment.getIntValue(Environment.PROP_FILE_BLACKLIST_TYPE);
		String listString = "";
		if (blacklistType == WikiBase.UPLOAD_BLACKLIST) {
			listString = Environment.getValue(Environment.PROP_FILE_BLACKLIST);
		} else if (blacklistType == WikiBase.UPLOAD_WHITELIST) {
			listString = Environment.getValue(Environment.PROP_FILE_WHITELIST);
		}
		String[] tokens = listString.split("[\\s,\\.]");
		for (int i = 0; i < tokens.length; i++) {
			String token = tokens[i];
			if (StringUtils.isBlank(token)) {
				continue;
			}
			list.add(token.toLowerCase());
		}
		return list;
	}

	/**
	 * Utility method to retrieve an instance of the current search engine.
	 *
	 * @return An instance of the current search engine.
	 */
	public static SearchEngine searchEngineInstance() {
		String searchEngineClass = Environment.getValue(Environment.PROP_BASE_SEARCH_ENGINE);
		try {
			return (SearchEngine)Utilities.instantiateClass(searchEngineClass);
		} catch (ClassCastException e) {
			throw new IllegalStateException("Search engine specified in jamwiki.properties does not implement org.jamwiki.SearchEngine: " + searchEngineClass);
		}
	}

	/**
	 * Verify that a directory exists and is writable.
	 *
	 * @param name The full name (including the path) for the directory being tested.
	 * @return A WikiMessage object containing any error encountered, otherwise
	 *  <code>null</code>.
	 */
	public static WikiMessage validateDirectory(String name) {
		File directory = new File(name);
		if (!directory.exists() || !directory.isDirectory()) {
			return new WikiMessage("error.directoryinvalid", name);
		}
		String filename = "jamwiki-test-" + System.currentTimeMillis() + ".txt";
		File file = new File(name, filename);
		String text = "Testing";
		String read = null;
		try {
			// attempt to write a temp file to the directory
			FileUtils.writeStringToFile(file, text, "UTF-8");
		} catch (IOException e) {
			return new WikiMessage("error.directorywrite", name, e.getMessage());
		}
		try {
			// verify that the file was correctly written
			read = FileUtils.readFileToString(file, "UTF-8");
			if (read == null || !text.equals(read)) {
				throw new IOException();
			}
		} catch (IOException e) {
			return new WikiMessage("error.directoryread", name, e.getMessage());
		}
		try {
			// attempt to delete the file
			FileUtils.forceDelete(file);
		} catch (IOException e) {
			return new WikiMessage("error.directorydelete", name, e.getMessage());
		}
		return null;
	}

	/**
	 * Utility method for determining if the parameters of a Role are valid
	 * or not.
	 *
	 * @param role The Role to validate.
	 * @throws WikiException Thrown if the role is invalid.
	 */
	public static void validateRole(Role role) throws WikiException {
		Matcher m = WikiUtil.INVALID_ROLE_NAME_PATTERN.matcher(role.getAuthority());
		if (!m.matches()) {
			throw new WikiException(new WikiMessage("roles.error.name", role.getAuthority()));
		}
		if (!StringUtils.isBlank(role.getDescription()) && role.getDescription().length() > 200) {
			throw new WikiException(new WikiMessage("roles.error.description"));
		}
		// FIXME - throw a user-friendly error if the role name is already in use
	}

	/**
	 * Utility method for determining if a topic name is valid for use on the Wiki,
	 * meaning that it is not empty and does not contain any invalid characters.
	 *
	 * @param name The topic name to validate.
	 * @throws WikiException Thrown if the user name is invalid.
	 */
	public static void validateTopicName(String name) throws WikiException {
		if (StringUtils.isBlank(name)) {
			throw new WikiException(new WikiMessage("common.exception.notopic"));
		}
		if (PseudoTopicHandler.isPseudoTopic(name)) {
			throw new WikiException(new WikiMessage("common.exception.pseudotopic", name));
		}
		WikiLink wikiLink = LinkUtil.parseWikiLink(name);
		String namespace = StringUtils.trimToNull(wikiLink.getNamespace());
		String article = StringUtils.trimToNull(wikiLink.getArticle());
		if (StringUtils.startsWith(namespace, "/") || StringUtils.startsWith(article, "/")) {
			throw new WikiException(new WikiMessage("common.exception.name", name));
		}
		if (namespace != null && namespace.toLowerCase().equals(NamespaceHandler.NAMESPACE_SPECIAL.toLowerCase())) {
			throw new WikiException(new WikiMessage("common.exception.name", name));
		}
		Matcher m = WikiUtil.INVALID_TOPIC_NAME_PATTERN.matcher(name);
		if (m.find()) {
			throw new WikiException(new WikiMessage("common.exception.name", name));
		}
	}

	/**
	 * Utility method for determining if a password is valid for use on the wiki.
	 *
	 * @param password The password value.
	 * @param confirmPassword Passwords must be entered twice to avoid tying errors.
	 *  This field represents the confirmed password entry.
	 */
	public static void validatePassword(String password, String confirmPassword) throws WikiException {
		if (StringUtils.isBlank(password)) {
			throw new WikiException(new WikiMessage("error.newpasswordempty"));
		}
		if (StringUtils.isBlank(confirmPassword)) {
			throw new WikiException(new WikiMessage("error.passwordconfirm"));
		}
		if (!password.equals(confirmPassword)) {
			throw new WikiException(new WikiMessage("admin.message.passwordsnomatch"));
		}
	}

	/**
	 * Utility method for determining if a username is valid for use on the Wiki,
	 * meaning that it is not empty and does not contain any invalid characters.
	 *
	 * @param name The username to validate.
	 * @throws WikiException Thrown if the user name is invalid.
	 */
	public static void validateUserName(String name) throws WikiException {
		if (StringUtils.isBlank(name)) {
			throw new WikiException(new WikiMessage("error.loginempty"));
		}
		Matcher m = WikiUtil.VALID_USER_LOGIN_PATTERN.matcher(name);
		if (!m.matches()) {
			throw new WikiException(new WikiMessage("common.exception.name", name));
		}
	}
}
