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
import java.util.Properties;
// FIXME - remove this import
import org.apache.commons.pool.impl.GenericObjectPool;
import org.jamwiki.Environment;
import org.jamwiki.utils.SortedProperties;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiLogger;

/**
 * Provides access to JAMWiki property values.
 */
public class WikiEnvironment implements Environment {
	private static final WikiLogger logger = WikiLogger.getLogger(WikiEnvironment.class.getName());

	private static final String PROPERTY_FILE_NAME = "jamwiki.properties";

	private static Properties defaults = null;
	private static Environment instance = null; // NOPMD instanciated and used
	private static SortedProperties props = null;

	// initialize the singleton instance
	static {
		instance = new WikiEnvironment();
	}

	/**
	 * The constructor loads property values from the property file.
	 */
	private WikiEnvironment() {
		try {
			initDefaultProperties();
			logger.fine("Default properties initialized: " + defaults.toString());
			props = loadProperties(PROPERTY_FILE_NAME, defaults);
			logger.fine("JAMWiki properties initialized: " + props.toString());
		} catch (Exception e) {
			logger.severe("Failure while initializing property values", e);
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
		return WikiEnvironment.retrievePropertyFile(filename);
	}

	/**
	 * Initialize the default property values.
	 */
	private static void initDefaultProperties() {
		defaults = new Properties();
		defaults.setProperty(PROP_BASE_COOKIE_EXPIRE, "31104000");
		defaults.setProperty(PROP_BASE_DEFAULT_TOPIC, "StartingPoints");
		defaults.setProperty(PROP_BASE_FILE_DIR, "");
		defaults.setProperty(PROP_BASE_INITIALIZED, Boolean.FALSE.toString());
		defaults.setProperty(PROP_BASE_LOGO_IMAGE, "logo_oliver.gif");
		defaults.setProperty(PROP_BASE_META_DESCRIPTION, "");
		defaults.setProperty(PROP_BASE_PERSISTENCE_TYPE, WikiBase.PERSISTENCE_INTERNAL);
		defaults.setProperty(PROP_BASE_USER_HANDLER, WikiBase.USER_HANDLER_DATABASE);
		defaults.setProperty(PROP_BASE_WIKI_VERSION, "0.0.0");
		defaults.setProperty(PROP_CACHE_INDIVIDUAL_SIZE, "500");
		defaults.setProperty(PROP_CACHE_MAX_AGE, "300");
		defaults.setProperty(PROP_CACHE_MAX_IDLE_AGE, "150");
		defaults.setProperty(PROP_CACHE_TOTAL_SIZE, "1000");
		defaults.setProperty(PROP_DB_DRIVER, "org.postgresql.Driver");
		defaults.setProperty(PROP_DB_PASSWORD, "");
		defaults.setProperty(PROP_DB_TYPE, WikiBase.DATA_HANDLER_ANSI);
		defaults.setProperty(PROP_DB_URL, "jdbc:postgresql://localhost:5432/database");
		defaults.setProperty(PROP_DB_USERNAME, "");
		defaults.setProperty(PROP_DBCP_MAX_ACTIVE, "10");
		defaults.setProperty(PROP_DBCP_MAX_IDLE, "3");
		defaults.setProperty(PROP_DBCP_MIN_EVICTABLE_IDLE_TIME, "600");
		defaults.setProperty(PROP_DBCP_NUM_TESTS_PER_EVICTION_RUN, "5");
		defaults.setProperty(PROP_DBCP_TEST_ON_BORROW, Boolean.TRUE.toString());
		defaults.setProperty(PROP_DBCP_TEST_ON_RETURN, Boolean.TRUE.toString());
		defaults.setProperty(PROP_DBCP_TEST_WHILE_IDLE, Boolean.TRUE.toString());
		defaults.setProperty(PROP_DBCP_TIME_BETWEEN_EVICTION_RUNS, "120");
		defaults.setProperty(PROP_DBCP_WHEN_EXHAUSTED_ACTION, String.valueOf(GenericObjectPool.WHEN_EXHAUSTED_GROW));
		defaults.setProperty(PROP_EMAIL_REPLY_ADDRESS, "");
		defaults.setProperty(PROP_EMAIL_SMTP_HOST, "");
		defaults.setProperty(PROP_EMAIL_SMTP_PASSWORD, "");
		defaults.setProperty(PROP_EMAIL_SMTP_USERNAME, "");
		defaults.setProperty(PROP_ENCRYPTION_ALGORITHM, "SHA-512");
		defaults.setProperty(PROP_EXTERNAL_LINK_NEW_WINDOW, Boolean.FALSE.toString());
		defaults.setProperty(PROP_FILE_BLACKLIST, "bat,bin,exe,htm,html,js,jsp,php,sh");
		defaults.setProperty(PROP_FILE_BLACKLIST_TYPE, String.valueOf(WikiBase.UPLOAD_BLACKLIST));
		defaults.setProperty(PROP_FILE_DIR_FULL_PATH, "");
		defaults.setProperty(PROP_FILE_DIR_RELATIVE_PATH, "");
		// size is in bytes
		defaults.setProperty(PROP_FILE_MAX_FILE_SIZE, "2000000");
		defaults.setProperty(PROP_FILE_WHITELIST, "bmp,gif,jpeg,jpg,pdf,png,properties,svg,txt,zip");
		defaults.setProperty(PROP_IMAGE_RESIZE_INCREMENT, "100");
		defaults.setProperty(PROP_LDAP_CONTEXT, "ou=users,dc=mycompany,dc=com");
		defaults.setProperty(PROP_LDAP_FACTORY_CLASS, "com.sun.jndi.ldap.LdapCtxFactory");
		defaults.setProperty(PROP_LDAP_FIELD_EMAIL, "mail");
		defaults.setProperty(PROP_LDAP_FIELD_FIRST_NAME, "givenName");
		defaults.setProperty(PROP_LDAP_FIELD_LAST_NAME, "sn");
		defaults.setProperty(PROP_LDAP_FIELD_USERID, "uid");
		defaults.setProperty(PROP_LDAP_LOGIN, "");
		defaults.setProperty(PROP_LDAP_PASSWORD, "");
		defaults.setProperty(PROP_LDAP_SECURITY_AUTHENTICATION, "DIGEST-MD5");
		defaults.setProperty(PROP_LDAP_URL, "ldap://localhost:389");
		defaults.setProperty(PROP_PARSER_ALLOW_HTML, Boolean.TRUE.toString());
		defaults.setProperty(PROP_PARSER_ALLOW_JAVASCRIPT, Boolean.FALSE.toString());
		defaults.setProperty(PROP_PARSER_ALLOW_TEMPLATES, Boolean.TRUE.toString());
		defaults.setProperty(PROP_PARSER_CLASS, "org.jamwiki.parser.jflex.JFlexParser");
		defaults.setProperty(PROP_PARSER_SIGNATURE_DATE_PATTERN, "dd-MMM-yyyy HH:mm zzz");
		defaults.setProperty(PROP_PARSER_SIGNATURE_USER_PATTERN, "[[{0}|{4}]]");
		defaults.setProperty(PROP_PARSER_TOC, Boolean.TRUE.toString());
		defaults.setProperty(PROP_PARSER_TOC_DEPTH, "5");
		defaults.setProperty(PROP_PRINT_NEW_WINDOW, Boolean.FALSE.toString());
		defaults.setProperty(PROP_RECENT_CHANGES_NUM, "100");
		defaults.setProperty(PROP_RSS_ALLOWED, Boolean.TRUE.toString());
		defaults.setProperty(PROP_RSS_TITLE, "Wiki Recent Changes");
		defaults.setProperty(PROP_TOPIC_SPAM_FILTER, Boolean.TRUE.toString());
		defaults.setProperty(PROP_TOPIC_USE_PREVIEW, Boolean.TRUE.toString());
		defaults.setProperty(PROP_TOPIC_WYSIWYG, Boolean.TRUE.toString());
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
		String value = getValue(name);
		try {
			return Boolean.valueOf(value).booleanValue(); //NOPMD there is already a FIX ME
		} catch (Exception e) {
			logger.severe("Invalid boolean property " + name + " with value " + value);
		}
		// FIXME - should this otherwise indicate an invalid property?
		return false;
	}

	/**
	 * Return an instance of the current properties object.  The property instance
	 * returned should not be directly modified.
	 *
	 * @return Returns an instance of the current system properties.
	 */
	public static Properties getInstance() {
		return props;
	}

	/**
	 * Get the value of an integer property.
	 *
	 * @param name The name of the property whose value is to be retrieved.
	 * @return The value of the property.
	 */
	public static int getIntValue(String name) {
		String value = getValue(name);
		try {
			 return Integer.parseInt(value); //NOPMD
		} catch (Exception e) {
			logger.warning("Invalid integer property " + name + " with value " + value);
		}
		// FIXME - should this otherwise indicate an invalid property?
		return -1;
	}

	/**
	 * Get the value of a long property.
	 *
	 * @param name The name of the property whose value is to be retrieved.
	 * @return The value of the property.
	 */
	public static long getLongValue(String name) {
		String value = getValue(name);
		try {
			 return Long.parseLong(value); //NOPMD
		} catch (Exception e) {
			logger.warning("Invalid long property " + name + " with value " + value);
		}
		// FIXME - should this otherwise indicate an invalid property?
		return -1;
	}

	/**
	 * Returns the value of a property.
	 *
	 * @param name The name of the property whose value is to be retrieved.
	 * @return The value of the property.
	 */
	public static String getValue(String name) {
		return props.getProperty(name);
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
		try {
			file = findProperties(propertyFile);
			if (file == null) {
				logger.warning("Property file " + propertyFile + " does not exist");
			} else if (!file.exists()) {
				logger.warning("Property file " + file.getPath() + " does not exist");
			} else {
				logger.config("Loading properties from " + file.getPath());
				properties.load(new FileInputStream(file));
			}
		} catch (Exception e) {
			logger.severe("Failure while trying to load properties file " + file.getPath(), e);
		}
		return properties;
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
		File file = null;
		try {
			file = Utilities.getClassLoaderFile(filename);
			return file; //NOPMD
		} catch (Exception e) {
			// NOPMD file might not exist
		}
		try {
			file = new File(Utilities.getClassLoaderRoot(), filename);
			return file; //NOPMD
		} catch (Exception e) {
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
		WikiEnvironment.saveProperties(PROPERTY_FILE_NAME, props, null);
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
	public static void saveProperties(String propertyFile, SortedProperties properties, String comments) throws IOException {
		File file = findProperties(propertyFile);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			properties.store(out, comments);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
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
		props.setProperty(name, Boolean.toString(value));
	}

	/**
	 * Sets a new integer value for the given property name.
	 *
	 * @param name The name of the property whose value is to be set.
	 * @param value The value of the property being set.
	 */
	public static void setIntValue(String name, int value) {
		props.setProperty(name, Integer.toString(value));
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
			value = ""; //NOPMD
		}
		props.setProperty(name, value);
	}
}