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

import java.io.IOException;
import java.util.Locale;
import org.jamwiki.model.WikiGroup;
import org.jamwiki.model.WikiUser;
import org.jamwiki.utils.WikiUtil;
import org.jamwiki.utils.WikiCache;
import org.jamwiki.utils.WikiLogger;

/**
 * <code>WikiBase</code> is loaded as a singleton class and provides access
 * to all core wiki structures.  In addition this class provides utility methods
 * for resetting core structures including caches and permissions.
 *
 * @see org.jamwiki.DataHandler
 * @see org.jamwiki.SearchEngine
 */
public class WikiBase {

	/** Standard logger. */
	private static final WikiLogger logger = WikiLogger.getLogger(WikiBase.class.getName());
	/** The singleton instance of this class. */
	private static WikiBase instance = null;
	/** The data handler that looks after read/write operations. */
	private static DataHandler dataHandler = null;
	/** The search engine instance. */
	private static SearchEngine searchEngine = null;

	/** Cache name for the cache of parsed topic content. */
	public static final String CACHE_PARSED_TOPIC_CONTENT = "org.jamwiki.WikiBase.CACHE_PARSED_TOPIC_CONTENT";

        public static final String CACHE_PARSED_TOPIC_DATA = "org.jamwiki.WikiBase.CACHE_PARSED_TOPIC_DATA";

	/** Name of the default wiki */
	// FIXME - make this configurable
	public static final String DEFAULT_VWIKI = "en";
	/** Default group for registered users. */
	private static WikiGroup GROUP_REGISTERED_USER = null;
	/** Data stored using an external database */
	public static final String PERSISTENCE_EXTERNAL = "DATABASE";
	/** Data stored using an internal copy of the HSQL database */
	public static final String PERSISTENCE_INTERNAL = "INTERNAL";
	/** Root directory within the WAR distribution that contains the default topic pages. */
	public static final String SPECIAL_PAGE_DIR = "pages";
	/** Name of the default starting points topic. */
	public static final String SPECIAL_PAGE_STARTING_POINTS = "StartingPoints";
	/** Name of the default left menu topic. */
	public static final String SPECIAL_PAGE_LEFT_MENU = "LeftMenu";
	/** Name of the default footer topic. */
	public static final String SPECIAL_PAGE_BOTTOM_AREA = "BottomArea";
	/** Name of the default jamwiki.css topic. */
	public static final String SPECIAL_PAGE_STYLESHEET = "StyleSheet";
	/** Allow file uploads of any file type. */
	public static final int UPLOAD_ALL = 0;
	/** Use a blacklist to determine what file types can be uploaded. */
	public static final int UPLOAD_BLACKLIST = 2;
	/** Disable all file uploads. */
	public static final int UPLOAD_NONE = 1;
	/** Use a whitelist to determine what file types can be uploaded. */
	public static final int UPLOAD_WHITELIST = 3;

	static {
		try {
			WikiBase.instance = new WikiBase();
		} catch (Exception e) {
			logger.severe("Failure while initializing WikiBase", e);
		}
	}

	/**
	 * Creates an instance of <code>WikiBase</code>, initializing the default
	 * data handler instance and search engine instance.
	 *
	 * @throws IOException If the instance cannot be instantiated.
	 */
	private WikiBase() throws IOException {
		this.reload();
	}

	/**
	 * Get an instance of the current data handler.
	 *
	 * @return The current data handler instance, or <code>null</code>
	 *  if the handler has not yet been initialized.
	 */
	public static DataHandler getDataHandler() {
		return WikiBase.dataHandler;
	}

	/**
	 *
	 */
	public static WikiGroup getGroupRegisteredUser() {
		if (WikiUtil.isFirstUse() || WikiUtil.isUpgrade()) {
			throw new IllegalStateException("Cannot retrieve group information prior to completing setup/upgrade");
		}
		if (WikiBase.GROUP_REGISTERED_USER == null) {
			try {
				WikiBase.GROUP_REGISTERED_USER = WikiBase.getDataHandler().lookupWikiGroup(WikiGroup.GROUP_REGISTERED_USER);
			} catch (Exception e) {
				throw new RuntimeException("Unable to retrieve registered users group", e);
			}
		}
		return WikiBase.GROUP_REGISTERED_USER;
	}

	/**
	 * Get an instance of the current search engine.
	 *
	 * @return The current search engine instance.
	 */
	public static SearchEngine getSearchEngine() {
		return WikiBase.searchEngine;
	}

	/**
	 * Reload the data handler, user handler, and other basic wiki
	 * data structures.
	 */
	public static void reload() throws IOException {
		WikiBase.dataHandler = WikiUtil.dataHandlerInstance();
		WikiBase.searchEngine = WikiUtil.searchEngineInstance();
	}

	/**
	 * Reset the WikiBase object, re-initializing the data handler and
	 * other values.
	 *
	 * @param locale The locale to be used if any system pages need to be set up
	 *  as a part of the initialization process.
	 * @param user A sysadmin user to be used in case any system pages need to
	 *  be created as a part of the initialization process.
	 * @param username The admin user's username (login).
	 * @param encryptedPassword The admin user's encrypted password.  This value
	 *  is only required when creating a new admin user.
	 * @throws DataAccessException Thrown if an error occurs during re-initialization.
	 * @throws IOException Thrown if an error occurs during re-initialization.
	 * @throws WikiException Thrown if an error occurs during re-initialization.
	 */
	public static void reset(Locale locale, WikiUser user, String username, String encryptedPassword) throws DataAccessException, IOException, WikiException {
		WikiBase.instance = new WikiBase();
		WikiCache.initialize();
		WikiBase.dataHandler.setup(locale, user, username, encryptedPassword);
	}
}
