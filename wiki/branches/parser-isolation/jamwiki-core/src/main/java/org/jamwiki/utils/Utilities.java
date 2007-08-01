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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationCredentialsNotFoundException;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.io.FileUtils;
import org.jamwiki.DataHandler;
import org.jamwiki.Environment;
import org.jamwiki.UserHandler;
import org.jamwiki.WikiBase;
import org.jamwiki.WikiException;
import org.jamwiki.WikiMessage;
import org.jamwiki.WikiVersion;
import org.jamwiki.model.Role;
import org.jamwiki.model.Topic;
import org.jamwiki.model.WikiUser;
import org.jamwiki.parser.AbstractParser;
import org.jamwiki.parser.ParserDocument;
import org.jamwiki.parser.ParserInput;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * This class provides a variety of utility methods.
 */
public class Utilities {

	private static final WikiLogger logger = WikiLogger.getLogger(Utilities.class.getName());
	private static Pattern INVALID_ROLE_NAME_PATTERN = null;
	private static Pattern INVALID_TOPIC_NAME_PATTERN = null;
	private static Pattern VALID_USER_LOGIN_PATTERN = null;

	static {
		try {
			INVALID_ROLE_NAME_PATTERN = Pattern.compile("([A-Za-z0-9_]+)");
			INVALID_TOPIC_NAME_PATTERN = Pattern.compile("([\\n\\r\\\\<>\\[\\]?#]+)");
			VALID_USER_LOGIN_PATTERN = Pattern.compile("([A-Za-z0-9_]+)");
		} catch (Exception e) {
			logger.severe("Unable to compile pattern", e);
		}
	}

	/**
	 *
	 */
	private Utilities() {
	}



	/**
	 * Given a string of the form "en_US" or "en_us", build an appropriate
	 * Locale object.  Note that the Java constructor apparently has some
	 * problems that cause breakage with Spring request handling.
	 *
	 * @param localeString The name of the locale for which an object is being
	 *  created.
	 * @return A Locale object matching the given string, or <code>null</code>
	 *  if a Locale object cannot be created.
	 */
	public static Locale buildLocale(String localeString) {
		if (!StringUtils.hasText(localeString)) {
			return null;
		}
		// use spring voodoo to avoid some weird locale initialization issues
		LocaleEditor localeEditor = new LocaleEditor();
		localeEditor.setAsText(localeString);
		return (Locale)localeEditor.getValue();
	}



	/**
	 * Convert a string value from one encoding to another.
	 *
	 * @param text The string that is to be converted.
	 * @param fromEncoding The encoding that the string is currently encoded in.
	 * @param toEncoding The encoding that the string is to be encoded to.
	 * @return The encoded string.
	 */
	public static String convertEncoding(String text, String fromEncoding, String toEncoding) {
		if (!StringUtils.hasText(text)) {
			return text;
		}
		if (!StringUtils.hasText(fromEncoding)) {
			logger.warning("No character encoding specified to convert from, using UTF-8");
			fromEncoding = "UTF-8";
		}
		if (!StringUtils.hasText(toEncoding)) {
			logger.warning("No character encoding specified to convert to, using UTF-8");
			toEncoding = "UTF-8";
		}
		try {
			text = new String(text.getBytes(fromEncoding), toEncoding);
		} catch (Exception e) {
			// bad encoding
			logger.warning("Unable to convert value " + text + " from " + fromEncoding + " to " + toEncoding, e);
		}
		return text;
	}

	/**
	 * Retrieve the current <code>WikiUser</code> from Acegi
	 * <code>SecurityContextHolder</code>.  If the current user is not
	 * logged-in then this method will return an empty <code>WikiUser</code>
	 * object.
	 *
	 * @return The current logged-in <code>WikiUser</code>, or an empty
	 *  <code>WikiUser</code> if there is no user currently logged in.
	 *  This method will never return <code>null</code>.
	 * @throws AuthenticationCredentialsNotFoundException If authentication
	 *  credentials are unavailable.
	 */
	public static WikiUser currentUser() throws AuthenticationCredentialsNotFoundException {
		SecurityContext ctx = SecurityContextHolder.getContext();
		Authentication auth = ctx.getAuthentication();
		return WikiUser.initWikiUser(auth);
	}



	/**
	 * Utility method to retrieve an instance of the current data handler.
	 *
	 * @return An instance of the current data handler.
	 * @throws Exception Thrown if a data handler instance can not be
	 *  instantiated.
	 */
	public static DataHandler dataHandlerInstance() throws Exception {
		// FIXME - remove this conditional after the ability to upgrade to
		// 0.5.0 is removed.
		if (Environment.getValue(Environment.PROP_DB_TYPE) == null) {
			// this is a problem, but it should never occur
			logger.warning("Utilities.dataHandlerInstance called without a valid PROP_DB_TYPE value");
		} else if (Environment.getValue(Environment.PROP_DB_TYPE).equals("ansi")) {
			Environment.setValue(Environment.PROP_DB_TYPE, WikiBase.DATA_HANDLER_ANSI);
			Environment.saveProperties();
		} else if (Environment.getValue(Environment.PROP_DB_TYPE).equals("hsql")) {
			Environment.setValue(Environment.PROP_DB_TYPE, WikiBase.DATA_HANDLER_HSQL);
			Environment.saveProperties();
		} else if (Environment.getValue(Environment.PROP_DB_TYPE).equals("mssql")) {
			Environment.setValue(Environment.PROP_DB_TYPE, WikiBase.DATA_HANDLER_MSSQL);
			Environment.saveProperties();
		} else if (Environment.getValue(Environment.PROP_DB_TYPE).equals("mysql")) {
			Environment.setValue(Environment.PROP_DB_TYPE, WikiBase.DATA_HANDLER_MYSQL);
			Environment.saveProperties();
		} else if (Environment.getValue(Environment.PROP_DB_TYPE).equals("oracle")) {
			Environment.setValue(Environment.PROP_DB_TYPE, WikiBase.DATA_HANDLER_ORACLE);
			Environment.saveProperties();
		} else if (Environment.getValue(Environment.PROP_DB_TYPE).equals("postgres")) {
			Environment.setValue(Environment.PROP_DB_TYPE, WikiBase.DATA_HANDLER_POSTGRES);
			Environment.saveProperties();
		} else if (Environment.getValue(Environment.PROP_DB_TYPE).equals("db2")) {
			Environment.setValue(Environment.PROP_DB_TYPE, WikiBase.DATA_HANDLER_DB2);
			Environment.saveProperties();
		} else if (Environment.getValue(Environment.PROP_DB_TYPE).equals("db2/400")) {
			Environment.setValue(Environment.PROP_DB_TYPE, WikiBase.DATA_HANDLER_DB2400);
			Environment.saveProperties();
		}
		String dataHandlerClass = Environment.getValue(Environment.PROP_DB_TYPE);
		logger.fine("Using data handler: " + dataHandlerClass);
		Class clazz = ClassUtils.forName(dataHandlerClass);
		Class[] parameterTypes = new Class[0];
		Constructor constructor = clazz.getConstructor(parameterTypes);
		Object[] initArgs = new Object[0];
		return (DataHandler)constructor.newInstance(initArgs);
	}

	/**
	 * Decode a value that has been retrieved from a servlet request.  This
	 * method will replace any underscores with spaces.
	 *
	 * @param url The encoded value that is to be decoded.
	 * @return A decoded value.
	 */
	public static String decodeFromRequest(String url) {
		// convert underscores to spaces
		return StringUtils.replace(url, "_", " ");
	}

	/**
	 * Decode a value that has been retrieved directly from a URL or file
	 * name.  This method will URL decode the value and then replace any
	 * underscores with spaces.  Note that this method SHOULD NOT be called
	 * for values retrieved using request.getParameter(), but only values
	 * taken directly from a URL.
	 *
	 * @param url The encoded value that is to be decoded.
	 * @return A decoded value.
	 */
	public static String decodeFromURL(String url) {
		String result = url;
		try {
			result = URLDecoder.decode(result, "UTF-8");
		} catch (Exception e) {
			logger.info("Failure while decoding url " + url + " with charset UTF-8");
		}
		return Utilities.decodeFromRequest(result);
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
		// replace spaces with underscores
		String result = StringUtils.replace(name, " ", "_");
		// URL encode the rest of the name
		try {
			result = URLEncoder.encode(result, "UTF-8");
		} catch (Exception e) {
			logger.warning("Failure while encoding " + name + " with charset UTF-8");
		}
		return result;
	}

	/**
	 * Encode a topic name for use in a URL.  This method will replace spaces
	 * with underscores and URL encode the value, but it will not URL encode
	 * colons.
	 *
	 * @param url The topic name to be encoded for use in a URL.
	 * @return The encoded topic name value.
	 */
	public static String encodeForURL(String url) {
		String result = Utilities.encodeForFilename(url);
		// un-encode colons
		result = StringUtils.replace(result, "%3A", ":");
		// un-encode forward slashes
		result = StringUtils.replace(result, "%2F", "/");
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
	public static String extractCommentsLink(String name) throws Exception {
		if (!StringUtils.hasText(name)) {
			throw new Exception("Empty topic name " + name);
		}
		WikiLink wikiLink = LinkUtil.parseWikiLink(name);
		if (!StringUtils.hasText(wikiLink.getNamespace())) {
			return NamespaceHandler.NAMESPACE_COMMENTS + NamespaceHandler.NAMESPACE_SEPARATOR + name;
		}
		String namespace = wikiLink.getNamespace();
		String commentsNamespace = NamespaceHandler.getCommentsNamespace(namespace);
		return (StringUtils.hasText(commentsNamespace)) ? commentsNamespace + NamespaceHandler.NAMESPACE_SEPARATOR + wikiLink.getArticle() : NamespaceHandler.NAMESPACE_COMMENTS + NamespaceHandler.NAMESPACE_SEPARATOR + wikiLink.getArticle();
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
	public static String extractTopicLink(String name) throws Exception {
		if (!StringUtils.hasText(name)) {
			throw new Exception("Empty topic name " + name);
		}
		WikiLink wikiLink = LinkUtil.parseWikiLink(name);
		if (!StringUtils.hasText(wikiLink.getNamespace())) {
			return name;
		}
		String namespace = wikiLink.getNamespace();
		String mainNamespace = NamespaceHandler.getMainNamespace(namespace);
		return (StringUtils.hasText(mainNamespace)) ? mainNamespace + NamespaceHandler.NAMESPACE_SEPARATOR + wikiLink.getArticle() : wikiLink.getArticle();
	}

	/**
	 * Returns any trailing period, comma, semicolon, or colon characters
	 * from the given string.  This method is useful when parsing raw HTML
	 * links, in which case trailing punctuation must be removed.
	 *
	 * @param text The text from which trailing punctuation should be returned.
	 * @return Any trailing punctuation from the given text, or an empty string
	 *  otherwise.
	 */
	public static String extractTrailingPunctuation(String text) {
		StringBuffer buffer = new StringBuffer();
		for (int i = text.length() - 1; i >= 0; i--) {
			char c = text.charAt(i);
			if (c == '.' || c == ';' || c == ',' || c == ':' || c == ')' || c == '(' || c == ']' || c == '[') {
				buffer.append(c);
			} else {
				break;
			}
		}
		if (buffer.length() == 0) {
			return "";
		}
		buffer = buffer.reverse();
		return buffer.toString();
	}

	/**
	 *
	 */
	public static Topic findRedirectedTopic(Topic parent, int attempts) throws Exception {
		int count = attempts;
		if (parent.getTopicType() != Topic.TYPE_REDIRECT || !StringUtils.hasText(parent.getRedirectTo())) {
			logger.severe("getRedirectTarget() called for non-redirect topic " + parent.getName());
			return parent;
		}
		// avoid infinite redirection
		count++;
		if (count > 10) {
			throw new WikiException(new WikiMessage("topic.redirect.infinite"));
		}
		// get the topic that is being redirected to
		Topic child = WikiBase.getDataHandler().lookupTopic(parent.getVirtualWiki(), parent.getRedirectTo(), false, null);
		if (child == null) {
			// child being redirected to doesn't exist, return parent
			return parent;
		}
		if (!StringUtils.hasText(child.getRedirectTo())) {
			// found a topic that is not a redirect, return
			return child;
		}
		if (WikiBase.getDataHandler().lookupTopic(child.getVirtualWiki(), child.getRedirectTo(), false, null) == null) {
			// child is a redirect, but its target does not exist
			return child;
		}
		// topic is a redirect, keep looking
		return Utilities.findRedirectedTopic(child, count);
	}

	/**
	 * Given a message key and locale return a locale-specific message.
	 *
	 * @param key The message key that corresponds to the formatted message
	 *  being retrieved.
	 * @param locale The locale for the message that is to be retrieved.
	 * @return A formatted message string that is specific to the locale.
	 */
	public static String formatMessage(String key, Locale locale) {
		ResourceBundle messages = ResourceBundle.getBundle("ApplicationResources", locale);
		return messages.getString(key);
	}

	/**
	 * Given a message key, locale, and formatting parameters, return a
	 * locale-specific message.
	 *
	 * @param key The message key that corresponds to the formatted message
	 *  being retrieved.
	 * @param locale The locale for the message that is to be retrieved.
	 * @param params An array of formatting parameters to use in the message
	 *  being returned.
	 * @return A formatted message string that is specific to the locale.
	 */
	public static String formatMessage(String key, Locale locale, Object[] params) {
		MessageFormat formatter = new MessageFormat("");
		formatter.setLocale(locale);
		String message = Utilities.formatMessage(key, locale);
		formatter.applyPattern(message);
		return formatter.format(params);
	}

	/**
	 * Given a file name for a file that is located somewhere in the application
	 * classpath, return a File object representing the file.
	 *
	 * @param filename The name of the file (relative to the classpath) that is
	 *  to be retrieved.
	 * @return A file object representing the requested filename
	 * @throws Exception Thrown if the classloader can not be found or if
	 *  the file can not be found in the classpath.
	 */
	public static File getClassLoaderFile(String filename) throws Exception {
		// note that this method is used when initializing logging, so it must
		// not attempt to log anything.
		File file = null;
		ClassLoader loader = ClassUtils.getDefaultClassLoader();
		URL url = loader.getResource(filename);
		if (url == null) {
			url = ClassLoader.getSystemResource(filename);
			if (url == null) {
				throw new Exception("Unable to find " + filename);
			}
			file = FileUtils.toFile(url);
		} else {
			file = FileUtils.toFile(url);
		}
		if (file == null || !file.exists()) {
			throw new Exception("Found invalid class loader root " + file);
		}
		return file;
	}

	/**
	 * Attempt to get the class loader root directory.  This method works
	 * by searching for a file that MUST exist in the class loader root
	 * and then returning its parent directory.
	 *
	 * @return Returns a file indicating the directory of the class loader.
	 * @throws Exception Thrown if the class loader can not be found.
	 */
	public static File getClassLoaderRoot() throws Exception {
		File file = Utilities.getClassLoaderFile("ApplicationResources.properties");
		if (!file.exists()) {
			throw new Exception("Unable to find class loader root");
		}
		return file.getParentFile();
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
		if (!StringUtils.hasText(wikiLink.getNamespace())) {
			return false;
		}
		String namespace = wikiLink.getNamespace();
		if (namespace.equals(NamespaceHandler.NAMESPACE_SPECIAL)) {
			return false;
		}
		String commentNamespace = NamespaceHandler.getCommentsNamespace(namespace);
		return (namespace.equals(commentNamespace));
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
	public static boolean isUpgrade() throws Exception {
		if (Utilities.isFirstUse()) {
			return false;
		}
		WikiVersion oldVersion = new WikiVersion(Environment.getValue(Environment.PROP_BASE_WIKI_VERSION));
		WikiVersion currentVersion = new WikiVersion(WikiVersion.CURRENT_WIKI_VERSION);
		return (oldVersion.before(currentVersion));
	}

	/**
	 * Determine if the given string is an IP address.  This method uses pattern
	 * matching to see if the given string could be a valid IP address.
	 *
	 * @param ipAddress A string that is to be examined to verify whether or not
	 *  it could be a valid IP address.
	 * @return <code>true</code> if the string is a value that is a valid IP address,
	 *  <code>false</code> otherwise.
	 */
	public static boolean isIpAddress(String ipAddress) {
		// note that a regular expression would be the easiest way to handle
		// this, but regular expressions don't handle things like "number between
		// 0 and 255" very well, so use a heavier approach
		// if no text, obviously not valid
		if (!StringUtils.hasText(ipAddress)) {
			return false;
		}
		// must contain three periods
		if (StringUtils.countOccurrencesOf(ipAddress, ".") != 3) {
			return false;
		}
		// ip addresses must be between seven and 15 characters long
		if (ipAddress.length() < 7 || ipAddress.length() > 15) {
			return false;
		}
		// verify that the string is "0-255.0-255.0-255.0-255".
		StringTokenizer tokens = new StringTokenizer(ipAddress, ".");
		String token = null;
		int number = -1;
		while (tokens.hasMoreTokens()) {
			token = tokens.nextToken();
			try {
				number = Integer.parseInt(token);
				if (number < 0 || number > 255) {
					return false;
				}
			} catch (Exception e) {
				// not a number
				return false;
			}
		}
		// all tests passed, it's an IP address
		return true;
	}

	/**
	 * Using the system parser, parse system content.
	 *
	 * @param parserInput A ParserInput object that contains parser
	 *  configuration information.
	 * @param parserDocument A ParserDocument object that will hold metadata
	 *  output.  If this parameter is <code>null</code> then metadata generated
	 *  during parsing will not be available to the calling method.
	 * @param content The raw topic content that is to be parsed.
	 * @return The parsed content.
	 * @throws Exception Thrown if there are any parsing errors.
	 */
	public static String parse(ParserInput parserInput, ParserDocument parserDocument, String content) throws Exception {
		if (content == null) {
			return null;
		}
		if (parserDocument == null) {
			parserDocument = new ParserDocument();
		}
		AbstractParser parser = parserInstance(parserInput);
		return parser.parseHTML(parserDocument, content);
	}

	/**
	 * This method provides a way to parse content and set all output metadata,
	 * such as link values used by the search engine.
	 *
	 * @param parserInput A ParserInput object that contains parser configuration
	 *  information.
	 * @param content The raw topic content that is to be parsed.
	 * @return Returns a ParserDocument object with minimally parsed topic content
	 *  and other parser output fields set.
	 * @throws Exception Thrown if there are any parsing errors.
	 */
	public static ParserDocument parseMetadata(ParserInput parserInput, String content) throws Exception {
		AbstractParser parser = parserInstance(parserInput);
		ParserDocument parserDocument = new ParserDocument();
		parser.parseMetadata(parserDocument, content);
		return parserDocument;
	}

	/**
	 * Perform a bare minimum of parsing as required prior to saving a topic
	 * to the database.  In general this method will simply parse signature
	 * tags are return.
	 *
	 * @param parserInput A ParserInput object that contains parser configuration
	 *  information.
	 * @param raw The raw Wiki syntax to be converted into HTML.
	 * @return The parsed content.
	 * @throws Exception Thrown if any error occurs during parsing.
	 */
	public static String parseMinimal(ParserInput parserInput, String raw) throws Exception {
		AbstractParser parser = parserInstance(parserInput);
		return parser.parseMinimal(raw);
	}

	/**
	 * Retrieve a default ParserDocument object for a given topic name.  Note that
	 * the content has almost no parsing performed on it other than to generate
	 * parser output metadata.
	 *
	 * @param content The raw topic content.
	 * @return Returns a minimal ParserDocument object initialized primarily with
	 *  parser metadata such as links.
	 * @throws Exception Thrown if a parser error occurs.
	 */
	public static ParserDocument parserDocument(String content, String virtualWiki, String topicName) throws Exception {
		ParserInput parserInput = new ParserInput();
		parserInput.setVirtualWiki(virtualWiki);
		parserInput.setTopicName(topicName);
		parserInput.setAllowSectionEdit(false);
		return Utilities.parseMetadata(parserInput, content);
	}

	/**
	 * Utility method to retrieve an instance of the current system parser.
	 *
	 * @param parserInput A ParserInput object that contains parser configuration
	 *  information.
	 * @return An instance of the system parser.
	 * @throws Exception Thrown if a parser instance can not be instantiated.
	 */
	public static AbstractParser parserInstance(ParserInput parserInput) throws Exception {
		String parserClass = Environment.getValue(Environment.PROP_PARSER_CLASS);
		logger.fine("Using parser: " + parserClass);
		Class clazz = ClassUtils.forName(parserClass);
		Class[] parameterTypes = new Class[1];
		parameterTypes[0] = ClassUtils.forName("org.jamwiki.parser.ParserInput");
		Constructor constructor = clazz.getConstructor(parameterTypes);
		Object[] initArgs = new Object[1];
		initArgs[0] = parserInput;
		return (AbstractParser)constructor.newInstance(initArgs);
	}

	/**
	 * Given a topic name, return the parser-specific syntax to indicate a page
	 * redirect.
	 *
	 * @param topicName The name of the topic that is being redirected to.
	 * @return A string containing the syntax indicating a redirect.
	 * @throws Exception Thrown if a parser instance cannot be instantiated or
	 *  if any other parser error occurs.
	 */
	public static String parserRedirectContent(String topicName) throws Exception {
		AbstractParser parser = parserInstance(null);
		return parser.buildRedirectContent(topicName);
	}


	/**
	 * Utility method for reading a file from a classpath directory and returning
	 * its contents as a String.
	 *
	 * @param filename The name of the file to be read, either as an absolute file
	 *  path or relative to the classpath.
	 * @return A string representation of the file contents.
	 * @throws Exception Thrown if the file cannot be found or if an I/O exception
	 *  occurs.
	 */
	public static String readFile(String filename) throws Exception {
		File file = new File(filename);
		if (file.exists()) {
			// file passed in as full path
			return FileUtils.readFileToString(file, "UTF-8");
		}
		// look for file in resource directories
		ClassLoader loader = ClassUtils.getDefaultClassLoader();
		URL url = loader.getResource(filename);
		file = FileUtils.toFile(url);
		if (file == null || !file.exists()) {
			throw new FileNotFoundException("File " + filename + " is not available for reading");
		}
		return FileUtils.readFileToString(file, "UTF-8");
	}

	/**
	 * Utility method for reading special topic values from files and returning
	 * the file contents.
	 *
	 * @param locale The locale for the user viewing the special page.
	 * @param pageName The name of the special page being retrieved.
	 */
	public static String readSpecialPage(Locale locale, String pageName) throws Exception {
		String contents = null;
		String filename = null;
		String language = null;
		String country = null;
		if (locale != null) {
			language = locale.getLanguage();
			country = locale.getCountry();
		}
		String subdirectory = "";
		if (StringUtils.hasText(language) && StringUtils.hasText(country)) {
			try {
				subdirectory = new File(WikiBase.SPECIAL_PAGE_DIR, language + "_" + country).getPath();
				filename = new File(subdirectory, Utilities.encodeForFilename(pageName) + ".txt").getPath();
				contents = Utilities.readFile(filename);
			} catch (Exception e) {
				logger.info("File " + filename + " does not exist");
			}
		}
		if (contents == null && StringUtils.hasText(language)) {
			try {
				subdirectory = new File(WikiBase.SPECIAL_PAGE_DIR, language).getPath();
				filename = new File(subdirectory, Utilities.encodeForFilename(pageName) + ".txt").getPath();
				contents = Utilities.readFile(filename);
			} catch (Exception e) {
				logger.info("File " + filename + " does not exist");
			}
		}
		if (contents == null) {
			try {
				subdirectory = new File(WikiBase.SPECIAL_PAGE_DIR).getPath();
				filename = new File(subdirectory, Utilities.encodeForFilename(pageName) + ".txt").getPath();
				contents = Utilities.readFile(filename);
			} catch (Exception e) {
				logger.warning("File " + filename + " could not be read", e);
				throw e;
			}
		}
		return contents;
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
		List list = new Vector();
		int blacklistType = Environment.getIntValue(Environment.PROP_FILE_BLACKLIST_TYPE);
		String listString = "";
		if (blacklistType == WikiBase.UPLOAD_BLACKLIST) {
			listString = Environment.getValue(Environment.PROP_FILE_BLACKLIST);
		} else if (blacklistType == WikiBase.UPLOAD_WHITELIST) {
			listString = Environment.getValue(Environment.PROP_FILE_WHITELIST);
		}
		StringTokenizer tokens = new StringTokenizer(listString, "\n\r ,.");
		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			if (!StringUtils.hasText(token)) {
				continue;
			}
			list.add(token.toLowerCase());
		}
		return list;
	}


	/**
	 * Utility method to retrieve an instance of the current user handler.
	 *
	 * @return An instance of the current user handler.
	 * @throws Exception Thrown if a user handler instance can not be
	 *  instantiated.
	 */
	public static UserHandler userHandlerInstance() throws Exception {
		String userHandlerClass = Environment.getValue(Environment.PROP_BASE_USER_HANDLER);
		logger.fine("Using user handler: " + userHandlerClass);
		Class clazz = ClassUtils.forName(userHandlerClass);
		Class[] parameterTypes = new Class[0];
		Constructor constructor = clazz.getConstructor(parameterTypes);
		Object[] initArgs = new Object[0];
		return (UserHandler)constructor.newInstance(initArgs);
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
		} catch (Exception e) {
			return new WikiMessage("error.directorywrite", name, e.getMessage());
		}
		try {
			// verify that the file was correctly written
			read = FileUtils.readFileToString(file, "UTF-8");
			if (read == null || !text.equals(read)) {
				throw new IOException();
			}
		} catch (Exception e) {
			return new WikiMessage("error.directoryread", name, e.getMessage());
		}
		try {
			// attempt to delete the file
			FileUtils.forceDelete(file);
		} catch (Exception e) {
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
		Matcher m = INVALID_ROLE_NAME_PATTERN.matcher(role.getAuthority());
		if (!m.matches()) {
			throw new WikiException(new WikiMessage("roles.error.name", role.getAuthority()));
		}
		if (StringUtils.hasText(role.getDescription()) && role.getDescription().length() > 200) {
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
		if (!StringUtils.hasText(name)) {
			throw new WikiException(new WikiMessage("common.exception.notopic"));
		}
		if (PseudoTopicHandler.isPseudoTopic(name)) {
			throw new WikiException(new WikiMessage("common.exception.pseudotopic", name));
		}
		WikiLink wikiLink = LinkUtil.parseWikiLink(name);
		String namespace = wikiLink.getNamespace();
		if (namespace != null && namespace.toLowerCase().trim().equals(NamespaceHandler.NAMESPACE_SPECIAL.toLowerCase())) {
			throw new WikiException(new WikiMessage("common.exception.name", name));
		}
		Matcher m = INVALID_TOPIC_NAME_PATTERN.matcher(name);
		if (m.find()) {
			throw new WikiException(new WikiMessage("common.exception.name", name));
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
		if (!StringUtils.hasText(name)) {
			throw new WikiException(new WikiMessage("error.loginempty"));
		}
		Matcher m = VALID_USER_LOGIN_PATTERN.matcher(name);
		if (!m.matches()) {
			throw new WikiException(new WikiMessage("common.exception.name", name));
		}
	}
}
