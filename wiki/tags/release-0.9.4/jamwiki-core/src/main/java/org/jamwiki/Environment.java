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
package org.jamwiki;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import org.apache.commons.lang.math.NumberUtils;
// FIXME - remove this import
import org.apache.commons.pool.impl.GenericObjectPool;
import org.jamwiki.utils.SortedProperties;
import org.jamwiki.utils.WikiLogger;
import org.jamwiki.utils.Utilities;

/**
 * The <code>Environment</code> class is instantiated as a singleton to
 * provides access to JAMWiki property values stored in the
 * <code>jamwiki.properties</code> file.
 */
public class Environment {
	private static final WikiLogger logger = WikiLogger.getLogger(Environment.class.getName());

	public static final String PROP_BASE_COOKIE_EXPIRE = "cookie-expire";
	public static final String PROP_BASE_DEFAULT_TOPIC = "default-topic";
	public static final String PROP_BASE_FILE_DIR = "homeDir";
	public static final String PROP_BASE_INITIALIZED = "props-initialized";
	public static final String PROP_BASE_LOGO_IMAGE = "logo-image";
	public static final String PROP_BASE_META_DESCRIPTION = "meta-description";
	public static final String PROP_BASE_PERSISTENCE_TYPE = "persistenceType";
	public static final String PROP_BASE_SEARCH_ENGINE = "search-engine";
	public static final String PROP_BASE_WIKI_VERSION = "wiki-version";
	public static final String PROP_CACHE_INDIVIDUAL_SIZE = "cache-individual-size";
	public static final String PROP_CACHE_MAX_AGE = "cache-max-age";
	public static final String PROP_CACHE_MAX_IDLE_AGE = "cache-max-idle-age";
	public static final String PROP_CACHE_TOTAL_SIZE = "cache-total-size";
	public static final String PROP_DB_DRIVER = "driver";
	public static final String PROP_DB_PASSWORD = "db-password";
	public static final String PROP_DB_TYPE = "database-type";
	public static final String PROP_DB_URL = "url";
	public static final String PROP_DB_USERNAME = "db-user";
	public static final String PROP_DBCP_MAX_ACTIVE = "dbcp-max-active";
	public static final String PROP_DBCP_MAX_IDLE = "dbcp-max-idle";
	public static final String PROP_DBCP_MIN_EVICTABLE_IDLE_TIME = "dbcp-min-evictable-idle-time";
	public static final String PROP_DBCP_NUM_TESTS_PER_EVICTION_RUN = "dbcp-num-tests-per-eviction-run";
	public static final String PROP_DBCP_TEST_ON_BORROW = "dbcp-test-on-borrow";
	public static final String PROP_DBCP_TEST_ON_RETURN = "dbcp-test-on-return";
	public static final String PROP_DBCP_TEST_WHILE_IDLE = "dbcp-test-while-idle";
	public static final String PROP_DBCP_TIME_BETWEEN_EVICTION_RUNS = "dbcp-time-between-eviction-runs";
	public static final String PROP_DBCP_WHEN_EXHAUSTED_ACTION = "dbcp-when-exhausted-action";
	public static final String PROP_EMAIL_REPLY_ADDRESS = "reply-address";
	public static final String PROP_EMAIL_SMTP_HOST = "smtp-host";
	public static final String PROP_EMAIL_SMTP_PASSWORD = "smtp-password";
	public static final String PROP_EMAIL_SMTP_USERNAME = "smtp-username";
	public static final String PROP_ENCRYPTION_ALGORITHM = "encryption-algorithm";
	public static final String PROP_EXTERNAL_LINK_NEW_WINDOW = "external-link-new-window";
	public static final String PROP_FILE_BLACKLIST = "file-blacklist";
	public static final String PROP_FILE_BLACKLIST_TYPE = "file-blacklist-type";
	public static final String PROP_FILE_DIR_FULL_PATH = "file-dir-full-path";
	public static final String PROP_FILE_DIR_RELATIVE_PATH = "file-dir-relative-path";
	public static final String PROP_FILE_MAX_FILE_SIZE = "max-file-size";
	public static final String PROP_FILE_SERVER_URL = "file-server-url";
	public static final String PROP_FILE_WHITELIST = "file-whitelist";
	public static final String PROP_IMAGE_RESIZE_INCREMENT = "image-resize-increment";
	public static final String PROP_MAX_TOPIC_VERSION_EXPORT = "max-topic-version-export";
	public static final String PROP_PARSER_ALLOW_HTML = "allowHTML";
	public static final String PROP_PARSER_ALLOW_JAVASCRIPT = "allow-javascript";
	public static final String PROP_PARSER_ALLOW_TEMPLATES = "allow-templates";
	public static final String PROP_PARSER_CLASS = "parser";
	/** Maximum number of template inclusions allowed on a page. */
	public static final String PROP_PARSER_MAX_INCLUSIONS = "parser-max-inclusions";
	/** Maximum number of parser iterations allowed for a single parsing run. */
	public static final String PROP_PARSER_MAX_PARSER_ITERATIONS = "parser-max-iterations";
	/** Maximum depth to which templates can be included for a single parsing run. */
	public static final String PROP_PARSER_MAX_TEMPLATE_DEPTH = "parser-max-template-depth";
	public static final String PROP_PARSER_SIGNATURE_DATE_PATTERN = "signature-date";
	public static final String PROP_PARSER_SIGNATURE_USER_PATTERN = "signature-user";
	public static final String PROP_PARSER_TOC = "allow-toc";
	public static final String PROP_PARSER_TOC_DEPTH = "toc-depth";
	public static final String PROP_PATTERN_INVALID_NAMESPACE_NAME = "pattern-namespace-name-invalid";
	public static final String PROP_PATTERN_INVALID_ROLE_NAME = "pattern-role-name-invalid";
	public static final String PROP_PATTERN_INVALID_TOPIC_NAME = "pattern-topic-name-invalid";
	public static final String PROP_PATTERN_VALID_USER_LOGIN = "pattern-login-valid";
	public static final String PROP_PATTERN_VALID_VIRTUAL_WIKI = "pattern-virtualwiki-valid";
	public static final String PROP_PRINT_NEW_WINDOW = "print-new-window";
	public static final String PROP_RECENT_CHANGES_NUM = "recent-changes-days";
	public static final String PROP_RSS_ALLOWED = "rss-allowed";
	public static final String PROP_RSS_TITLE = "rss-title";
	public static final String PROP_SERVER_URL = "server-url";
	public static final String PROP_SITE_NAME = "site-name";
	public static final String PROP_TOPIC_EDITOR = "default-editor";
	public static final String PROP_TOPIC_SPAM_FILTER = "use-spam-filter";
	public static final String PROP_TOPIC_USE_PREVIEW = "use-preview";
	public static final String PROP_TOPIC_USE_SHOW_CHANGES = "use-show-changes";
	public static final String PROP_VIRTUAL_WIKI_DEFAULT = "virtual-wiki-default";
	// Lookup properties file location from system properties first.
	private static final String PROPERTY_FILE_NAME = System.getProperty("jamwiki.property.file", "jamwiki.properties");

	private static Environment ENVIRONMENT_INSTANCE = null;
	private Properties defaults = null;
	private SortedProperties props = null;

	/**
	 * The constructor loads property values from the property file.
	 */
	private Environment() {
		this.initDefaultProperties();
		logger.fine("Default properties initialized: " + this.defaults.toString());
		this.props = loadProperties(PROPERTY_FILE_NAME, this.defaults);
		if ("true".equals(System.getProperty("jamwiki.override.file.properties"))) {
			overrideFromSystemProperties();
		}
		logger.fine("JAMWiki properties initialized: " + this.props.toString());
	}

	/**
	* Overrides file properties from system properties. Iterates over all properties
	* and checks if application server has defined overriding property. System wide
	* properties are prefixed with "jamwiki". These properties may be used to define
	* dynamic runtime properties (eg. upload path depends on environment).
	*/
	private void overrideFromSystemProperties() {
		logger.info("Overriding file properties with system properties.");
		Enumeration properties = this.props.propertyNames();
		while (properties.hasMoreElements()) {
			String property = String.valueOf(properties.nextElement());
			String value = System.getProperty("jamwiki." + property);
			if (value != null) {
				this.props.setProperty(property, value);
				logger.info("Replaced property " + property + " with value: " + value);
			}
		}
	}

	/**
	 * Load a property file.  First check for the file in the path from which
	 * the application was started, then check other classpath locations.
	 *
	 * @param filename The name of the property file to be loaded.  This name can be
	 *  either absolute or relative; if relative then the file will be loaded from
	 *  the class path or from the directory from which the JVM was loaded.
	 * @return A File object containing the properties file instance.
	 * @throws FileNotFoundException Thrown if the specified property file cannot
	 *  be located.
	 */
	private static File findProperties(String filename) throws FileNotFoundException {
		// read in properties file
		File file = new File(filename);
		if (file.exists()) {
			return file; //NOPMD
		}
		// search for file in class loader path
		return Environment.retrievePropertyFile(filename);
	}

	/**
	 * Initialize the default property values.
	 */
	private void initDefaultProperties() {
		this.defaults = new Properties();
		this.defaults.setProperty(PROP_BASE_COOKIE_EXPIRE, "31104000");
		this.defaults.setProperty(PROP_BASE_DEFAULT_TOPIC, "StartingPoints");
		this.defaults.setProperty(PROP_BASE_FILE_DIR, "");
		this.defaults.setProperty(PROP_BASE_INITIALIZED, Boolean.FALSE.toString());
		this.defaults.setProperty(PROP_BASE_LOGO_IMAGE, "logo_oliver.gif");
		this.defaults.setProperty(PROP_BASE_META_DESCRIPTION, "");
		this.defaults.setProperty(PROP_BASE_PERSISTENCE_TYPE, WikiBase.PERSISTENCE_INTERNAL);
		this.defaults.setProperty(PROP_BASE_SEARCH_ENGINE, SearchEngine.SEARCH_ENGINE_LUCENE);
		this.defaults.setProperty(PROP_BASE_WIKI_VERSION, "0.0.0");
		this.defaults.setProperty(PROP_CACHE_INDIVIDUAL_SIZE, "1500");
		this.defaults.setProperty(PROP_CACHE_MAX_AGE, "300");
		this.defaults.setProperty(PROP_CACHE_MAX_IDLE_AGE, "150");
		this.defaults.setProperty(PROP_CACHE_TOTAL_SIZE, "3000");
		this.defaults.setProperty(PROP_DB_DRIVER, "");
		this.defaults.setProperty(PROP_DB_PASSWORD, "");
		this.defaults.setProperty(PROP_DB_TYPE, "");
		this.defaults.setProperty(PROP_DB_URL, "");
		this.defaults.setProperty(PROP_DB_USERNAME, "");
		this.defaults.setProperty(PROP_DBCP_MAX_ACTIVE, "15");
		this.defaults.setProperty(PROP_DBCP_MAX_IDLE, "5");
		this.defaults.setProperty(PROP_DBCP_MIN_EVICTABLE_IDLE_TIME, "600");
		this.defaults.setProperty(PROP_DBCP_NUM_TESTS_PER_EVICTION_RUN, "5");
		this.defaults.setProperty(PROP_DBCP_TEST_ON_BORROW, Boolean.TRUE.toString());
		this.defaults.setProperty(PROP_DBCP_TEST_ON_RETURN, Boolean.TRUE.toString());
		this.defaults.setProperty(PROP_DBCP_TEST_WHILE_IDLE, Boolean.FALSE.toString());
		this.defaults.setProperty(PROP_DBCP_TIME_BETWEEN_EVICTION_RUNS, "120");
		this.defaults.setProperty(PROP_DBCP_WHEN_EXHAUSTED_ACTION, String.valueOf(GenericObjectPool.WHEN_EXHAUSTED_GROW));
		this.defaults.setProperty(PROP_EMAIL_REPLY_ADDRESS, "");
		this.defaults.setProperty(PROP_EMAIL_SMTP_HOST, "");
		this.defaults.setProperty(PROP_EMAIL_SMTP_PASSWORD, "");
		this.defaults.setProperty(PROP_EMAIL_SMTP_USERNAME, "");
		this.defaults.setProperty(PROP_ENCRYPTION_ALGORITHM, "SHA-512");
		this.defaults.setProperty(PROP_EXTERNAL_LINK_NEW_WINDOW, Boolean.FALSE.toString());
		this.defaults.setProperty(PROP_FILE_BLACKLIST, "bat,bin,exe,htm,html,js,jsp,php,sh");
		this.defaults.setProperty(PROP_FILE_BLACKLIST_TYPE, String.valueOf(WikiBase.UPLOAD_BLACKLIST));
		// size is in bytes
		this.defaults.setProperty(PROP_FILE_MAX_FILE_SIZE, "2000000");
		this.defaults.setProperty(PROP_FILE_SERVER_URL, "");
		this.defaults.setProperty(PROP_FILE_WHITELIST, "bmp,gif,jpeg,jpg,pdf,png,properties,svg,txt,zip");
		this.defaults.setProperty(PROP_IMAGE_RESIZE_INCREMENT, "100");
		this.defaults.setProperty(PROP_MAX_TOPIC_VERSION_EXPORT, "200");
		this.defaults.setProperty(PROP_PARSER_ALLOW_HTML, Boolean.TRUE.toString());
		this.defaults.setProperty(PROP_PARSER_ALLOW_JAVASCRIPT, Boolean.FALSE.toString());
		this.defaults.setProperty(PROP_PARSER_ALLOW_TEMPLATES, Boolean.TRUE.toString());
		this.defaults.setProperty(PROP_PARSER_CLASS, "org.jamwiki.parser.jflex.JFlexParser");
		this.defaults.setProperty(PROP_PARSER_MAX_INCLUSIONS, "250");
		this.defaults.setProperty(PROP_PARSER_MAX_PARSER_ITERATIONS, "100");
		this.defaults.setProperty(PROP_PARSER_MAX_TEMPLATE_DEPTH, "100");
		this.defaults.setProperty(PROP_PARSER_SIGNATURE_DATE_PATTERN, "dd-MMM-yyyy HH:mm zzz");
		this.defaults.setProperty(PROP_PARSER_SIGNATURE_USER_PATTERN, "[[{0}|{4}]]");
		this.defaults.setProperty(PROP_PARSER_TOC, Boolean.TRUE.toString());
		this.defaults.setProperty(PROP_PARSER_TOC_DEPTH, "5");
		this.defaults.setProperty(PROP_PATTERN_INVALID_NAMESPACE_NAME, "([\\n\\r\\\\<>\\[\\]\\:_%/?&#]+)");
		this.defaults.setProperty(PROP_PATTERN_INVALID_ROLE_NAME, "([A-Za-z0-9_]+)");
		this.defaults.setProperty(PROP_PATTERN_INVALID_TOPIC_NAME, "([\\n\\r\\\\<>\\[\\]?#]+)");
		this.defaults.setProperty(PROP_PATTERN_VALID_USER_LOGIN, "([A-Za-z0-9_]+)");
		this.defaults.setProperty(PROP_PATTERN_VALID_VIRTUAL_WIKI, "([A-Za-z0-9_]+)");
		this.defaults.setProperty(PROP_PRINT_NEW_WINDOW, Boolean.FALSE.toString());
		this.defaults.setProperty(PROP_RECENT_CHANGES_NUM, "100");
		this.defaults.setProperty(PROP_RSS_ALLOWED, Boolean.TRUE.toString());
		this.defaults.setProperty(PROP_RSS_TITLE, "Wiki Recent Changes");
		this.defaults.setProperty(PROP_SERVER_URL, "");
		this.defaults.setProperty(PROP_SITE_NAME, "JAMWiki");
		// FIXME - hard coding
		this.defaults.setProperty(PROP_TOPIC_EDITOR, "toolbar");
		this.defaults.setProperty(PROP_TOPIC_SPAM_FILTER, Boolean.TRUE.toString());
		this.defaults.setProperty(PROP_TOPIC_USE_PREVIEW, Boolean.TRUE.toString());
		this.defaults.setProperty(PROP_TOPIC_USE_SHOW_CHANGES, Boolean.TRUE.toString());
		this.defaults.setProperty(PROP_VIRTUAL_WIKI_DEFAULT, "en");
		this.processDefaultUploadDirectories();
	}

	/**
	 * Get the value of a boolean property.
	 * Returns <code>true</code> if the property is equal, ignoring case,
	 * to the string "true".
	 * Returns false in all other cases (eg: "false", "yes", "1")
	 *
	 * @param name The name of the property whose value is to be retrieved.
	 * @return The value of the property.
	 */
	public static boolean getBooleanValue(String name) {
		return Boolean.valueOf(getValue(name));
	}

	/**
	 * Return an instance of the current properties object.  The property instance
	 * returned should not be directly modified.
	 *
	 * @return Returns an instance of the current system properties.
	 */
	public static Properties getInstance() {
		if (ENVIRONMENT_INSTANCE == null) {
			// initialize the singleton instance
			ENVIRONMENT_INSTANCE = new Environment();
		}
		return ENVIRONMENT_INSTANCE.props;
	}

	/**
	 * Get the value of an integer property.
	 *
	 * @param name The name of the property whose value is to be retrieved.
	 * @return The value of the property.
	 */
	public static int getIntValue(String name) {
		int value = NumberUtils.toInt(getValue(name), -1);
		if (value == -1) {
			logger.warning("Invalid integer property " + name + " with value " + value);
		}
		// FIXME - should this otherwise indicate an invalid property?
		return value;
	}

	/**
	 * Get the value of a long property.
	 *
	 * @param name The name of the property whose value is to be retrieved.
	 * @return The value of the property.
	 */
	public static long getLongValue(String name) {
		long value = NumberUtils.toLong(getValue(name), -1);
		if (value == -1) {
			logger.warning("Invalid long property " + name + " with value " + value);
		}
		// FIXME - should this otherwise indicate an invalid property?
		return value;
	}

	/**
	 * Returns the value of a property.
	 *
	 * @param name The name of the property whose value is to be retrieved.
	 * @return The value of the property.
	 */
	public static String getValue(String name) {
		return getInstance().getProperty(name);
	}

	/**
	 * Given a property file name, load the property file and return an object
	 * representing the property values.
	 *
	 * @param propertyFile The name of the property file to load.
	 * @return The loaded SortedProperties object.
	 */
	public static SortedProperties loadProperties(String propertyFile) {
		return loadProperties(propertyFile, null);
	}

	/**
	 * Given a property file name, load the property file and return an object
	 * representing the property values.
	 *
	 * @param propertyFile The name of the property file to load.
	 * @param def Default property values, or <code>null</code> if there are no defaults.
	 * @return The loaded SortedProperties object.
	 */
	public static SortedProperties loadProperties(String propertyFile, Properties def) {
		SortedProperties properties = new SortedProperties();
		if (def != null) {
			properties = new SortedProperties(def);
		}
		File file = null;
		FileInputStream fis = null;
		try {
			file = findProperties(propertyFile);
			if (file == null) {
				logger.warning("Property file " + propertyFile + " does not exist");
			} else if (!file.exists()) {
				logger.warning("Property file " + file.getPath() + " does not exist");
			} else {
				logger.config("Loading properties from " + file.getPath());
				fis = new FileInputStream(file);
				properties.load(fis);
			}
		} catch (IOException e) {
			logger.severe("Failure while trying to load properties file " + file.getPath(), e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// NOPMD
				}
			}
		}
		return properties;
	}

	/**
	 * Set values related to file uploads.  The file upload directory is the default
	 * folder into which uploads are stored, such as /home/user/wiki/upload.  The
	 * relative upload directory is a prefix that will be added to upload URLs that
	 * corresponds to the file upload directory, so in the previous example if files
	 * are being uploaded to /home/user/wiki/upload then the relative uploaded
	 * directory would be /wiki/upload/.
	 */
	private void processDefaultUploadDirectories() {
		String defaultUploadDirectory = "";
		String defaultRelativeUploadDirectory = "";
		try {
			File webAppRoot = Utilities.getClassLoaderRoot();
			// the class loader root should be /WEB-INF/classes, but if deployed as anything
			// other than a WAR then it might just be the temp directory.
			if (webAppRoot.getParentFile() != null && webAppRoot.getName().toLowerCase().equals("classes")) {
				webAppRoot = webAppRoot.getParentFile();
				if (webAppRoot.getParentFile() != null && webAppRoot.getName().toLowerCase().equals("web-inf")) {
					webAppRoot = webAppRoot.getParentFile();
				}
			}
			defaultRelativeUploadDirectory = "/" + webAppRoot.getName() + "/upload/";
			defaultUploadDirectory = new File(webAppRoot, "upload").getPath();
		} catch (Throwable t) {
			logger.severe("Failure while setting file upload defaults", t);
		}
		this.defaults.setProperty(PROP_FILE_DIR_FULL_PATH, defaultUploadDirectory);
		this.defaults.setProperty(PROP_FILE_DIR_RELATIVE_PATH, defaultRelativeUploadDirectory);
	}

	/**
	 * Utility methods for retrieving property files from the class path, based on
	 * code from the org.apache.log4j.helpers.Loader class.
	 *
	 * @param filename Given a filename return a File object for the file.  The filename
	 *  may be relative to the class path or the directory from which the JVM was
	 *  initialized.
	 * @return Returns a file representing the filename, or <code>null</code> if
	 *  the file cannot be found.
	 */
	private static File retrievePropertyFile(String filename) {
		try {
			return Utilities.getClassLoaderFile(filename);
		} catch (FileNotFoundException e) {
			// NOPMD file might not exist
		}
		try {
			return new File(Utilities.getClassLoaderRoot(), filename);
		} catch (FileNotFoundException e) {
			logger.severe("Error while searching for resource " + filename, e);
		}
		return null;
	}

	/**
	 * Save the current Wiki system properties to the filesystem.
	 *
	 * @throws IOException Thrown if the file cannot be found or if an I/O
	 *  error occurs.
	 */
	public static void saveProperties() throws IOException {
		Environment.saveProperties(PROPERTY_FILE_NAME, getInstance(), null);
	}

	/**
	 * Save the specified property values to the filesystem.
	 *
	 * @param propertyFile The name of the property file to save.
	 * @param properties The properties object that is to be saved.
	 * @param comments A comment to save in the properties file.
	 * @throws IOException Thrown if the file cannot be found or if an I/O
	 *  error occurs.
	 */
	public static void saveProperties(String propertyFile, Properties properties, String comments) throws IOException {
		File file = findProperties(propertyFile);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			properties.store(out, comments);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// NOPMD ignore, unimportant if a close fails
				}
			}
		}
	}

	/**
	 * Set a new boolean value for the given property name.
	 *
	 * @param name The name of the property whose value is to be set.
	 * @param value The value of the property being set.
	 */
	public static void setBooleanValue(String name, boolean value) {
		getInstance().setProperty(name, Boolean.toString(value));
	}

	/**
	 * Sets a new integer value for the given property name.
	 *
	 * @param name The name of the property whose value is to be set.
	 * @param value The value of the property being set.
	 */
	public static void setIntValue(String name, int value) {
		getInstance().setProperty(name, Integer.toString(value));
	}

	/**
	 * Sets a new value for the given property name.
	 *
	 * @param name The name of the property whose value is to be set.
	 * @param value The value of the property being set.
	 */
	public static void setValue(String name, String value) {
		// it is invalid to set a property value null, so convert to empty string
		if (value == null) {
			value = "";
		}
		getInstance().setProperty(name, value);
	}
}
