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
package org.jamwiki;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import org.jamwiki.model.Topic;
import org.jamwiki.persistency.PersistencyHandler;
import org.jamwiki.persistency.db.DatabaseHandler;
import org.jamwiki.persistency.db.DatabaseNotify;
import org.jamwiki.persistency.db.DatabaseSearchEngine;
import org.jamwiki.persistency.db.DatabaseWikiMembers;
import org.jamwiki.persistency.file.FileHandler;
import org.jamwiki.persistency.file.FileNotify;
import org.jamwiki.persistency.file.FileSearchEngine;
import org.jamwiki.persistency.file.FileWikiMembers;
import org.jamwiki.parser.AbstractParser;
import org.jamwiki.parser.alt.BackLinkLex;
import org.jamwiki.search.SearchEngine;
import org.jamwiki.search.SearchRefreshThread;
import org.jamwiki.servlets.JAMWikiServlet;
import org.jamwiki.users.LdapUsergroup;
import org.jamwiki.users.NoUsergroup;
import org.jamwiki.users.Usergroup;
import org.jamwiki.utils.Utilities;

/**
 * This class represents the core of JAMWiki. It has some central methods, like parsing the URI, and keeps an
 * instance of the <code>Environment</code> class.
 */
public class WikiBase {

	// FIXME - remove this
	public final static String WIKI_VERSION = "0.0.1";
	private static WikiBase instance;					   /** An instance to myself. Singleton pattern. */
	public static final int FILE = 0;					   /** The topics are stored in a flat file */
	public static final int DATABASE = 1;				   /** The topics are stored in a database */
	public static final int LDAP = 2;					   /** Members are retrieved from LDAP */
	public static final String DEFAULT_VWIKI = "en";	   /** Name of the default wiki */
	public static final String PLUGINS_DIR = "plugins";	 /** Name of the Plugins-Directory */
	private static final Logger logger = Logger.getLogger(WikiBase.class);  /** Log output */
	protected PersistencyHandler handler;				   /** The handler that looks after read/write operations for a persitence type */
	private List topicListeners;							/** Listeners for topic changes */
	private int virtualWikiCount;						   /** Number of virtual wikis */
	private static HashMap cachedContents = new HashMap();

	/**
	 * Creates an instance of <code>WikiBase</code> with a specified persistency sub-system.
	 *
	 * @param persistencyType
	 * @throws Exception If the handler cannot be instanciated.
	 */
	private WikiBase(int persistencyType) throws Exception {
		switch (persistencyType) {
			case FILE:
				this.handler = new FileHandler();
				break;
			case DATABASE:
				this.handler = new DatabaseHandler();
				break;
		}

		new SearchRefreshThread(
			Environment.getIntValue(Environment.PROP_SEARCH_INDEX_REFRESH_INTERVAL)
		);

//		PluginManager.getInstance().installAll();
//		this.topicListeners = new ArrayList();
//		this.topicListeners.addAll(PluginManager.getInstance().getTopicListeners());
	}


	/**
	 * Singleton. Retrieves an intance of <code>WikiBase</code> and creates one if it doesn't exist yet.
	 *
	 * @return Instance of this class
	 * @throws Exception If the storage produces errors
	 */
	public static WikiBase getInstance() throws Exception {
		int persistenceType = -1;
		String type = Environment.getValue(Environment.PROP_BASE_PERSISTENCE_TYPE);
		if (type != null && type.equals("DATABASE")) {
			persistenceType = WikiBase.DATABASE;
		} else {
			persistenceType = WikiBase.FILE;
		}
		if (instance == null) {
			instance = new WikiBase(persistenceType);
		}
		return instance;
	}


	/**
	 * Get an instance to the search enginge.
	 *
	 * @return Reference to the SearchEngine
	 * @throws Exception the current search engine
	 */
	public SearchEngine getSearchEngineInstance() throws Exception {
		switch (WikiBase.getPersistenceType()) {
			case FILE:
				return FileSearchEngine.getInstance();
			case DATABASE:
				return DatabaseSearchEngine.getInstance();
			default:
				return FileSearchEngine.getInstance();
		}
	}

	/**
	 *
	 */
	public static int getPersistenceType() {
		if (Environment.getValue(Environment.PROP_BASE_PERSISTENCE_TYPE).equals("DATABASE")) {
			return WikiBase.DATABASE;
		} else {
			return WikiBase.FILE;
		}
	}

	/**
	 * Get an instance of the user group
	 *
	 * @return Reference to the SearchEngine
	 * @throws Exception the current search engine
	 */
	public Usergroup getUsergroupInstance() throws Exception {
		switch (Usergroup.getUsergroupType()) {
			case LDAP:
				return LdapUsergroup.getInstance();
			//TODO case DATABASE:
			//  return DatabaseUsergroup.getInstance();
			default:
				return NoUsergroup.getInstance();
		}
	}

	/**
	 * TODO: DOCUMENT ME!
	 *
	 * @param virtualWiki TODO: DOCUMENT ME!
	 * @param topic	   TODO: DOCUMENT ME!
	 * @return TODO: DOCUMENT ME!
	 * @throws Exception TODO: DOCUMENT ME!
	 */
	public Notify getNotifyInstance(String virtualWiki, String topic) throws Exception {
		switch (WikiBase.getPersistenceType()) {
			case FILE:
				return new FileNotify(virtualWiki, topic);
			case DATABASE:
				return new DatabaseNotify(virtualWiki, topic);
			default:
				return new FileNotify();
		}
	}

	/**
	 * TODO: DOCUMENT ME!
	 *
	 * @param virtualWiki TODO: DOCUMENT ME!
	 * @return TODO: DOCUMENT ME!
	 * @throws Exception TODO: DOCUMENT ME!
	 */
	public WikiMembers getWikiMembersInstance(String virtualWiki) throws Exception {
		switch (WikiBase.getPersistenceType()) {
			case FILE:
				return new FileWikiMembers(virtualWiki);
			case DATABASE:
				return new DatabaseWikiMembers(virtualWiki);
			default:
				return new FileWikiMembers(virtualWiki);
		}
	}

	/**
	 * Reads a file and returns the raw contents. Used for the editing version.
	 */
	public synchronized String readRaw(String virtualWiki, String topicName) throws Exception {
		Topic topic = WikiBase.getInstance().getHandler().lookupTopic(virtualWiki, topicName);
		// FIXME - return null or empty?
		if (topic == null) return "";
		return topic.getTopicContent();
	}

	/**
	 * TODO: DOCUMENT ME!
	 *
	 * @param virtualWiki TODO: DOCUMENT ME!
	 * @param topicName   TODO: DOCUMENT ME!
	 * @return TODO: DOCUMENT ME!
	 * @throws Exception TODO: DOCUMENT ME!
	 */
	public synchronized boolean exists(String virtualWiki, String topicName) throws Exception {
		if (PseudoTopicHandler.getInstance().isPseudoTopic(topicName)) {
			return true;
		}
		return handler.exists(virtualWiki, topicName);
	}

	/**
	 * TODO: DOCUMENT ME!
	 *
	 * @param in		  TODO: DOCUMENT ME!
	 * @param virtualWiki TODO: DOCUMENT ME!
	 * @return				TODO: DOCUMENT ME!
	 * @throws Exception	TODO: DOCUMENT ME!
	 */
	public synchronized String cook(String context, String virtualWiki, String content) throws Exception {
		if (content == null) {
			// FIXME - return empty or something else?
			return "";
		}
		BufferedReader in = new BufferedReader(new StringReader(content));
		return cook(context, virtualWiki, in);
	}

	/**
	 * TODO: DOCUMENT ME!
	 *
	 * @param in		  TODO: DOCUMENT ME!
	 * @param virtualWiki TODO: DOCUMENT ME!
	 * @return				TODO: DOCUMENT ME!
	 * @throws Exception	TODO: DOCUMENT ME!
	 */
	public synchronized String cook(String context, String virtualWiki, BufferedReader in) throws Exception {
		String parserClass = Environment.getValue(Environment.PROP_PARSER_CLASS);
		logger.debug("Using parser: " + parserClass);
		Class clazz = Class.forName(parserClass);
		Class[] parameterTypes = null;
		Constructor constructor = clazz.getConstructor(parameterTypes);
		Object[] initArgs = null;
		AbstractParser parser = (AbstractParser)constructor.newInstance(initArgs);
		String line;
		StringBuffer raw = new StringBuffer();
		while ((line = in.readLine()) != null) {
			raw.append(line).append("\n");
		}
		return parser.parseHTML(context, virtualWiki, raw.toString());
	}

	/**
	 * TODO: DOCUMENT ME!
	 *
	 * @throws Exception TODO: DOCUMENT ME!
	 */
	public static void initialise(Locale locale) throws Exception {
		int persistenceType = WikiBase.getPersistenceType();
		WikiMail.init();
		instance = new WikiBase(persistenceType);
		instance.getHandler().initialize(locale);
		WikiBase.removeCachedContents();
	}

	/**
	 * Find all topics without links to them
	 */
	public Collection getOrphanedTopics(String virtualWiki) throws Exception {
		Collection results = new HashSet();
		Collection all = getHandler().getAllTopicNames(virtualWiki);
		for (Iterator iterator = all.iterator(); iterator.hasNext();) {
			String topicName = (String) iterator.next();
			Collection matches = getSearchEngineInstance().findLinkedTo(
				virtualWiki,
				topicName
			);
			logger.debug(topicName + ": " + matches.size() + " matches");
			if (matches.size() == 0) {
				results.add(topicName);
			}
		}
		logger.debug(results.size() + " orphaned topics found");
		return results;
	}

	/**
	 * Find all topics that haven't been written but are linked to
	 */
	public Collection getToDoWikiTopics(String virtualWiki) throws Exception {
		Collection results = new TreeSet();
		Collection all = getHandler().getAllTopicNames(virtualWiki);
		Set topicNames = new HashSet();
		for (Iterator iterator = all.iterator(); iterator.hasNext();) {
			String topicName = (String) iterator.next();
			Topic topic = WikiBase.getInstance().getHandler().lookupTopic(virtualWiki, topicName);
			String content = topic.getTopicContent();
			StringReader reader = new StringReader(content);
			BackLinkLex lexer = new BackLinkLex(reader);
			while (lexer.yylex() != null) {
				;
			}
			reader.close();
			topicNames.addAll(lexer.getLinks());
		}
		for (Iterator iterator = topicNames.iterator(); iterator.hasNext();) {
			String topicName = (String) iterator.next();
			if (!PseudoTopicHandler.getInstance().isPseudoTopic(topicName)
				&& !handler.exists(virtualWiki, topicName)
				&& !"\\\\\\\\link\\\\\\\\".equals(topicName)) {
				results.add(topicName);
			}
		}
		logger.debug(results.size() + " todo topics found");
		return results;
	}

	/**
	 * Return a list of all virtual wikis on the server
	 */
	public Collection getVirtualWikiList() throws Exception {
		return this.handler.getVirtualWikiList();
	}

	/**
	 * Add virtual wiki
	 */
	public void addVirtualWiki(String virtualWiki) throws Exception {
		this.handler.addVirtualWiki(virtualWiki);
	}

	/**
	 * get a count of the number of virtual wikis in the system
	 *
	 * @return the number of virtual wikis
	 */
	public int getVirtualWikiCount() {
		if (virtualWikiCount == 0) {
			try {
				virtualWikiCount = getVirtualWikiList().size();
			} catch (Exception e) {
				logger.warn(e);
			}
		}
		return virtualWikiCount;
	}

	/**
	 * return the current handler instance
	 *
	 * @return the current handler instance
	 */
	public PersistencyHandler getHandler() {
		return handler;
	}

	/**
	 * Return true if the given topic is marked as "admin only", i.e. it is present in the admin only topics topic
	 *
	 * @param virtualWiki
	 * @param topicName
	 * @return
	 */
	public boolean isAdminOnlyTopic(Locale locale, String virtualWiki, String topicName) throws Exception {
		String adminOnlyTopics = readRaw(virtualWiki, JAMWikiServlet.getMessage("specialpages.adminonlytopics", locale));
		StringTokenizer tokenizer = new StringTokenizer(adminOnlyTopics);
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if (token.equals(topicName)) {
				return true;
			}
		}
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
	 * Clears the cached content
	 * This method is called when a "edit-save" or "edit-cancel" is invoked.
	 * <p/>
	 * Clearing all cached contents forces to reload.
	 */
	public static void removeCachedContents() {
		logger.debug(
			"Removing Cached Contents; " +
			"cachedContents.size() = " + cachedContents.size()
		);
		cachedContents.clear();
	}
}
