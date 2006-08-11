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
package org.jamwiki.persistency;

import java.io.File;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.model.RecentChange;
import org.jamwiki.model.Topic;
import org.jamwiki.model.TopicVersion;
import org.jamwiki.model.VirtualWiki;
import org.jamwiki.model.WikiFile;
import org.jamwiki.model.WikiFileVersion;
import org.jamwiki.model.WikiUser;
import org.jamwiki.persistency.db.DatabaseHandler;
import org.jamwiki.persistency.file.FileHandler;
import org.jamwiki.search.LuceneSearchEngine;
import org.jamwiki.servlets.WikiException;
import org.jamwiki.servlets.WikiMessage;
import org.jamwiki.utils.DiffUtil;
import org.jamwiki.utils.Encryption;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiCacheMap;
import org.springframework.util.StringUtils;

/**
 *
 */
public abstract class PersistencyHandler {

	private static Logger logger = Logger.getLogger(PersistencyHandler.class);
	// FIXME - possibly make this a property, or configurable based on number of topics in the system
	private static int MAX_CACHED_LIST_SIZE = 2000;
	/** For performance reasons, keep a (small) list of recently looked-up topics around in memory. */
	private static WikiCacheMap cachedTopicsList = new WikiCacheMap(MAX_CACHED_LIST_SIZE);
	/** For performance reasons, keep a (small) list of recently looked-up non-topics around in memory. */
	private static WikiCacheMap cachedNonTopicsList = new WikiCacheMap(MAX_CACHED_LIST_SIZE);
	/** For performance reasons, keep a (small) list of recently looked-up user logins and ids around in memory. */
	private static WikiCacheMap cachedUserLoginHash = new WikiCacheMap(MAX_CACHED_LIST_SIZE);
	protected static Hashtable virtualWikiIdHash = null;
	protected static Hashtable virtualWikiNameHash = null;

	/**
	 *
	 */
	protected abstract void addRecentChange(RecentChange change, Object[] params) throws Exception;

	/**
	 *
	 */
	protected abstract void addTopic(Topic topic, Object[] params) throws Exception;

	/**
	 *
	 */
	protected abstract void addTopicVersion(String virtualWiki, String topicName, TopicVersion topicVersion, Object[] params) throws Exception;

	/**
	 *
	 */
	protected abstract void addVirtualWiki(VirtualWiki virtualWiki, Object[] params) throws Exception;

	/**
	 *
	 */
	protected abstract void addWikiFile(String topicName, WikiFile wikiFile, Object[] params) throws Exception;

	/**
	 *
	 */
	protected abstract void addWikiFileVersion(String virtualWiki, String topicName, WikiFileVersion wikiFileVersion, Object[] params) throws Exception;

	/**
	 *
	 */
	protected abstract void addWikiUser(WikiUser user, Object[] params) throws Exception;

	/**
	 * Convert all data from one handler to another.  Note: this method will
	 * delete all data in the destination handler.  Thus, when converting from
	 * file to database, the database is completely re-initialized and all data
	 * erased.  Do not use this method unless you know what you are doing.
	 */
	public Vector convert(WikiUser user, Locale locale, PersistencyHandler fromHandler, PersistencyHandler toHandler) throws Exception {
		Object params[] = null;
		try {
			params = toHandler.initParams();
			WikiBase.setHandler(fromHandler);
			this.resetCache();
			// FIXME - hard coding of messages
			Vector messages = new Vector();
			// purge EVERYTHING from the destination handler
			toHandler.purgeData(params);
			if (toHandler instanceof DatabaseHandler) {
				messages.add("Deleting all database data.");
			} else {
				messages.add("Deleting all files from " + Environment.getValue(Environment.PROP_BASE_FILE_DIR));
			}
			// users
			Collection userNames = fromHandler.getAllWikiUserLogins();
			for (Iterator userIterator = userNames.iterator(); userIterator.hasNext();) {
				String userName = (String)userIterator.next();
				try {
					WikiUser wikiUser = fromHandler.lookupWikiUser(userName);
					toHandler.addWikiUser(wikiUser, params);
					messages.add("Added user " + userName);
				} catch (Exception e) {
					String msg = "Unable to convert user: " + userName;
					logger.error(msg, e);
					messages.add(msg + ": " + e.getMessage());
				}
			}
			Collection virtualWikis = fromHandler.getVirtualWikiList();
			for (Iterator virtualWikiIterator = virtualWikis.iterator(); virtualWikiIterator.hasNext();) {
				VirtualWiki virtualWiki = (VirtualWiki)virtualWikiIterator.next();
				try {
					toHandler.addVirtualWiki(virtualWiki, params);
					fromHandler.loadVirtualWikiHashes();
					messages.add("Added virtual wiki " + virtualWiki.getName());
				} catch (Exception e) {
					String msg = "Unable to convert virtual wiki " + virtualWiki.getName();
					logger.error(msg, e);
					messages.add(msg + ": " + e.getMessage());
				}
				// topics
				Collection topicNames = fromHandler.getAllTopicNames(virtualWiki.getName());
				for (Iterator topicIterator = topicNames.iterator(); topicIterator.hasNext();) {
					String topicName = (String)topicIterator.next();
					try {
						Topic topic = fromHandler.lookupTopic(virtualWiki.getName(), topicName);
						toHandler.addTopic(topic, params);
						messages.add("Added topic " + virtualWiki.getName() + " / " + topicName);
					} catch (Exception e) {
						String msg = "Unable to convert topic: " + virtualWiki.getName() + " / " + topicName;
						logger.error(msg, e);
						messages.add(msg + ": " + e.getMessage());
					}
				}
				// topic versions
				for (Iterator topicIterator = topicNames.iterator(); topicIterator.hasNext();) {
					String topicName = (String)topicIterator.next();
					List versions = fromHandler.getAllTopicVersions(virtualWiki.getName(), topicName, false);
					for (Iterator topicVersionIterator = versions.iterator(); topicVersionIterator.hasNext();) {
						TopicVersion topicVersion = (TopicVersion)topicVersionIterator.next();
						try {
							toHandler.addTopicVersion(virtualWiki.getName(), topicName, topicVersion, params);
							messages.add("Added topic version " + virtualWiki.getName() + " / " + topicName + " / " + topicVersion.getTopicVersionId());
						} catch (Exception e) {
							String msg = "Unable to convert topic version: " + virtualWiki.getName() + " / " + topicName + " / " + topicVersion.getTopicVersionId();
							logger.error(msg, e);
							messages.add(msg + ": " + e.getMessage());
						}
					}
				}
				// read-only topics
				Collection readOnlys = fromHandler.getReadOnlyTopics(virtualWiki.getName());
				for (Iterator readOnlyIterator = readOnlys.iterator(); readOnlyIterator.hasNext();) {
					String topicName = (String)readOnlyIterator.next();
					try {
						toHandler.writeReadOnlyTopic(virtualWiki.getName(), topicName, params);
						messages.add("Added read-only topic " + virtualWiki.getName() + " / " + topicName);
					} catch (Exception e) {
						String msg = "Unable to convert read-only topic: " + virtualWiki.getName() + " / " + topicName;
						logger.error(msg, e);
						messages.add(msg + ": " + e.getMessage());
					}
				}
				// wiki files
				Collection wikiFileNames = fromHandler.getAllWikiFileTopicNames(virtualWiki.getName());
				for (Iterator wikiFileIterator = wikiFileNames.iterator(); wikiFileIterator.hasNext();) {
					String topicName = (String)wikiFileIterator.next();
					try {
						WikiFile wikiFile = fromHandler.lookupWikiFile(virtualWiki.getName(), topicName);
						toHandler.addWikiFile(topicName, wikiFile, params);
						messages.add("Added wiki file " + virtualWiki.getName() + " / " + topicName);
					} catch (Exception e) {
						String msg = "Unable to convert wiki file: " + virtualWiki.getName() + " / " + topicName;
						logger.error(msg, e);
						messages.add(msg + ": " + e.getMessage());
					}
				}
				// wiki file versions
				for (Iterator topicIterator = wikiFileNames.iterator(); topicIterator.hasNext();) {
					String topicName = (String)topicIterator.next();
					List versions = fromHandler.getAllWikiFileVersions(virtualWiki.getName(), topicName, false);
					for (Iterator wikiFileVersionIterator = versions.iterator(); wikiFileVersionIterator.hasNext();) {
						WikiFileVersion wikiFileVersion = (WikiFileVersion)wikiFileVersionIterator.next();
						try {
							toHandler.addWikiFileVersion(virtualWiki.getName(), topicName, wikiFileVersion, params);
							messages.add("Added wiki file version " + virtualWiki.getName() + " / " + topicName + " / " + wikiFileVersion.getFileVersionId());
						} catch (Exception e) {
							String msg = "Unable to convert wiki file version: " + virtualWiki.getName() + " / " + topicName;
							logger.error(msg, e);
							messages.add(msg + ": " + e.getMessage());
						}
					}
				}
				// recent changes
				Collection changes = fromHandler.getRecentChanges(virtualWiki.getName(), 1000, false);
				for (Iterator changeIterator = changes.iterator(); changeIterator.hasNext();) {
					RecentChange change = (RecentChange)changeIterator.next();
					try {
						toHandler.addRecentChange(change, params);
						messages.add("Added recent change " + virtualWiki.getName() + " / " + change.getTopicName());
					} catch (Exception e) {
						String msg = "Unable to convert recent change: " + virtualWiki.getName() + " / " + change.getTopicName();
						logger.error(msg, e);
						messages.add(msg + ": " + e.getMessage());
					}
				}
			}
			return messages;
		} catch (Exception e) {
			toHandler.handleErrors(params);
			throw e;
		} finally {
			toHandler.releaseParams(params);
			WikiBase.reset(locale, user);
		}
	}

	/**
	 *
	 */
	public void deleteReadOnlyTopic(String virtualWiki, String topicName) throws Exception {
		Object params[] = null;
		try {
			params = this.initParams();
			Topic topic = lookupTopic(virtualWiki, topicName);
			topic.setReadOnly(false);
			updateTopic(topic, params);
		} catch (Exception e) {
			this.handleErrors(params);
			throw e;
		} finally {
			this.releaseParams(params);
		}
	}

	/**
	 *
	 */
	protected abstract void deleteRecentChanges(Topic topic, Object params[]) throws Exception;

	/**
	 *
	 */
	public void deleteTopic(Topic topic, TopicVersion topicVersion) throws Exception {
		Object params[] = null;
		try {
			params = this.initParams();
			// delete old recent changes
			deleteRecentChanges(topic, params);
			// update topic to indicate deleted, add delete topic version
			writeTopic(topic, topicVersion, params);
			// reset topic existence vector
			cachedTopicsList = new WikiCacheMap(MAX_CACHED_LIST_SIZE);
		} catch (Exception e) {
			this.handleErrors(params);
			throw e;
		} finally {
			this.releaseParams(params);
		}
	}

	/**
	 *
	 */
	public String diff(String virtualWiki, String topicName, int topicVersionId1, int topicVersionId2, boolean useHtml, Locale locale) throws Exception {
		TopicVersion version1 = lookupTopicVersion(virtualWiki, topicName, topicVersionId1);
		TopicVersion version2 = lookupTopicVersion(virtualWiki, topicName, topicVersionId2);
		if (version1 == null && version2 == null) {
			String msg = "Versions " + topicVersionId1 + " and " + topicVersionId2 + " not found for " + topicName + " / " + virtualWiki;
			logger.error(msg);
			throw new Exception(msg);
		}
		String contents1 = null;
		if (version1 != null) {
			contents1 = version1.getVersionContent();
		}
		String contents2 = null;
		if (version2 != null) {
			contents2 = version2.getVersionContent();
		}
		if (contents1 == null && contents2 == null) {
			String msg = "No versions found for " + topicVersionId1 + " against " + topicVersionId2;
			logger.error(msg);
			throw new Exception(msg);
		}
		return DiffUtil.diff(contents1, contents2, useHtml, locale);
	}

	/**
	 * See if a topic exists and if it has not been deleted.
	 */
	public boolean exists(String virtualWiki, String topicName) throws Exception {
		if (!StringUtils.hasText(virtualWiki) || !StringUtils.hasText(topicName)) {
			return false;
		}
		// first check a cache of recently looked-up topics for performance reasons
		String key = virtualWiki + "/" + topicName;
		if (cachedTopicsList.containsKey(key)) {
			return true;
		}
		if (cachedNonTopicsList.containsKey(key)) {
			return false;
		}
		Topic topic = lookupTopic(virtualWiki, topicName);
		if (topic == null || topic.getDeleted()) {
			cachedNonTopicsList.put(key, null);
			return false;
		}
		cachedTopicsList.put(key, null);
		return true;
	}

	/**
	 *
	 */
	protected abstract List getAllTopicVersions(String virtualWiki, String topicName, boolean descending) throws Exception;

	/**
	 *
	 */
	public abstract List getAllTopicNames(String virtualWiki) throws Exception;

	/**
	 *
	 */
	protected abstract List getAllWikiFileTopicNames(String virtualWiki) throws Exception;

	/**
	 *
	 */
	public abstract List getAllWikiFileVersions(String virtualWiki, String topicName, boolean descending) throws Exception;

	/**
	 *
	 */
	public abstract List getAllWikiUserLogins() throws Exception;

	/**
	 *
	 */
	public abstract Collection getReadOnlyTopics(String virtualWiki) throws Exception;

	/**
	 *
	 */
	public abstract Collection getRecentChanges(String virtualWiki, int numChanges, boolean descending) throws Exception;

	/**
	 *
	 */
	public Vector getRecentChanges(String virtualWiki, String topicName, boolean descending) throws Exception {
		Vector results = new Vector();
		Collection versions = getAllTopicVersions(virtualWiki, topicName, descending);
		for (Iterator iterator = versions.iterator(); iterator.hasNext();) {
			TopicVersion version = (TopicVersion)iterator.next();
			RecentChange change = new RecentChange();
			Integer authorId = version.getAuthorId();
			change.setAuthorId(authorId);
			if (authorId != null) {
				String login = lookupWikiUserLogin(authorId);
				change.setAuthorName(login);
			} else {
				change.setAuthorName(version.getAuthorIpAddress());
			}
			change.setEditComment(version.getEditComment());
			change.setEditDate(version.getEditDate());
			change.setEditType(version.getEditType());
			// NOTE! previous topic version not set!
			change.setTopicId(version.getTopicId());
			// NOTE! topic name not set!
			change.setTopicVersionId(version.getTopicVersionId());
			change.setVirtualWiki(virtualWiki);
			results.add(change);
		}
		return results;
	}

	/**
	 *
	 */
	public abstract Collection getUserContributions(String virtualWiki, String userString, int num, boolean descending) throws Exception;

	/**
	 *
	 */
	public Collection getVirtualWikiList() throws Exception {
		if (virtualWikiNameHash == null) {
			loadVirtualWikiHashes();
		}
		return virtualWikiNameHash.values();
	}

	/**
	 *
	 */
	protected abstract void handleErrors(Object[] params);

	/**
	 * Set up defaults if necessary
	 */
	public void initialize(Locale locale, WikiUser user) throws Exception {
		if (!Environment.getBooleanValue(Environment.PROP_BASE_INITIALIZED)) {
			return;
		}
		Object params[] = null;
		try {
			this.resetCache();
			params = this.initParams();
			setupDefaultVirtualWiki();
			setupAdminUser(user, params);
			setupSpecialPages(locale, user, params);
			this.loadVirtualWikiHashes();
		} catch (Exception e) {
			this.handleErrors(params);
			throw e;
		} finally {
			this.releaseParams(params);
		}
	}

	/**
	 *
	 */
	protected abstract Object[] initParams() throws Exception;

	/**
	 * Return <code>true</code> if the handler is initialized and ready to
	 * retrieve and save data.
	 */
	public abstract boolean isInitialized();

	/**
	 *
	 */
	protected abstract void loadVirtualWikiHashes() throws Exception;

	/**
	 *
	 */
	public abstract TopicVersion lookupLastTopicVersion(String virtualWiki, String topicName) throws Exception;

	/**
	 *
	 */
	public abstract TopicVersion lookupLastTopicVersion(String virtualWiki, String topicName, Object[] params) throws Exception;

	/**
	 *
	 */
	public abstract Topic lookupTopic(String virtualWiki, String topicName) throws Exception;

	/**
	 *
	 */
	public abstract Topic lookupTopic(String virtualWiki, String topicName, Object[] params) throws Exception;

	/**
	 *
	 */
	public abstract TopicVersion lookupTopicVersion(String virtualWiki, String topicName, int topicVersionId) throws Exception;

	/**
	 *
	 */
	public abstract TopicVersion lookupTopicVersion(String virtualWiki, String topicName, int topicVersionId, Object[] params) throws Exception;

	/**
	 *
	 */
	public VirtualWiki lookupVirtualWiki(String virtualWikiName) throws Exception {
		if (virtualWikiNameHash == null) {
			loadVirtualWikiHashes();
		}
		return (VirtualWiki)virtualWikiNameHash.get(virtualWikiName);
	}

	/**
	 *
	 */
	public int lookupVirtualWikiId(String virtualWikiName) throws Exception {
		if (virtualWikiNameHash == null) {
			this.loadVirtualWikiHashes();
		}
		VirtualWiki virtualWiki = (VirtualWiki)virtualWikiNameHash.get(virtualWikiName);
		return (virtualWiki != null) ? virtualWiki.getVirtualWikiId() : -1;
	}

	/**
	 *
	 */
	public String lookupVirtualWikiName(int virtualWikiId) throws Exception {
		if (virtualWikiIdHash == null) {
			this.loadVirtualWikiHashes();
		}
		VirtualWiki virtualWiki = (VirtualWiki)virtualWikiIdHash.get(new Integer(virtualWikiId));
		return (virtualWiki != null) ? virtualWiki.getName() : null;
	}

	/**
	 *
	 */
	public abstract WikiFile lookupWikiFile(String virtualWiki, String topicName) throws Exception;

	/**
	 *
	 */
	public abstract WikiUser lookupWikiUser(int userId) throws Exception;

	/**
	 *
	 */
	protected abstract WikiUser lookupWikiUser(int userId, Object[] params) throws Exception;

	/**
	 *
	 */
	public abstract WikiUser lookupWikiUser(String login, String password, boolean encrypted) throws Exception;

	/**
	 *
	 */
	public abstract WikiUser lookupWikiUser(String login) throws Exception;

	/**
	 *
	 */
	private String lookupWikiUserLogin(Integer authorId) throws Exception {
		String login = (String)cachedUserLoginHash.get(authorId);
		if (login != null) {
			return login;
		}
		WikiUser user = lookupWikiUser(authorId.intValue());
		login = user.getLogin();
		if (login != null) {
			cachedUserLoginHash.put(authorId, login);
		}
		return login;
	}

	/**
	 * This method causes all existing data to be deleted from the Wiki.  Use only
	 * when totally re-initializing a system.  To reiterate: CALLING THIS METHOD WILL
	 * DELETE ALL WIKI DATA!
	 */
	protected abstract void purgeData(Object[] params) throws Exception;

	/**
	 * Utility method for reading default topic values from files and returning
	 * the file contents.
	 */
	private static String readSpecialPage(Locale locale, String topicName) throws Exception {
		String contents = null;
		String filename = null;
		String language = null;
		String country = null;
		if (locale != null) {
			language = locale.getLanguage();
			country = locale.getCountry();
		}
		String subdirectory = WikiBase.SPECIAL_PAGE_DIR + File.separator;
		if (StringUtils.hasText(language) && StringUtils.hasText(country)) {
			try {
				filename = subdirectory + Utilities.encodeURL(topicName + "_" + language + "_" + country) + ".txt";
				contents = Utilities.readFile(filename);
			} catch (Exception e) {
				logger.info("File " + filename + " does not exist");
			}
		}
		if (contents == null && StringUtils.hasText(language)) {
			try {
				filename = subdirectory + Utilities.encodeURL(topicName + "_" + language) + ".txt";
				contents = Utilities.readFile(filename);
			} catch (Exception e) {
				logger.info("File " + filename + " does not exist");
			}
		}
		if (contents == null) {
			try {
				filename = subdirectory + Utilities.encodeURL(topicName) + ".txt";
				contents = Utilities.readFile(filename);
			} catch (Exception e) {
				logger.info("File " + filename + " could not be read", e);
				throw e;
			}
		}
		return contents;
	}

	/**
	 *
	 */
	protected abstract void releaseParams(Object[] params) throws Exception;

	/**
	 *
	 */
	public void reloadRecentChanges() throws Exception {
		Object params[] = null;
		try {
			params = this.initParams();
			this.reloadRecentChanges(params);
		} catch (Exception e) {
			this.handleErrors(params);
			throw e;
		} finally {
			this.releaseParams(params);
		}
	}

	/**
	 *
	 */
	protected abstract void reloadRecentChanges(Object[] params) throws Exception;

	/**
	 *
	 */
	protected void resetCache() {
		PersistencyHandler.virtualWikiIdHash = null;
		PersistencyHandler.virtualWikiNameHash = null;
		PersistencyHandler.cachedTopicsList = new WikiCacheMap(MAX_CACHED_LIST_SIZE);
		PersistencyHandler.cachedNonTopicsList = new WikiCacheMap(MAX_CACHED_LIST_SIZE);
		PersistencyHandler.cachedUserLoginHash = new WikiCacheMap(MAX_CACHED_LIST_SIZE);
	}

	/**
	 *
	 */
	private void setupAdminUser(WikiUser user, Object[] params) throws Exception {
		if (user == null) {
			throw new Exception("Admin user not specified");
		}
		if (lookupWikiUser(user.getUserId(), params) != null) {
			logger.info("Admin user already exists");
		}
		addWikiUser(user, params);
	}

	/**
	 *
	 */
	private void setupDefaultVirtualWiki() throws Exception {
		if (lookupVirtualWiki(WikiBase.DEFAULT_VWIKI) != null) {
			logger.info("Default virtual wiki already exists");
			return;
		}
		VirtualWiki virtualWiki = new VirtualWiki();
		virtualWiki.setName(WikiBase.DEFAULT_VWIKI);
		virtualWiki.setDefaultTopicName(Environment.getValue(Environment.PROP_BASE_DEFAULT_TOPIC));
		writeVirtualWiki(virtualWiki);
	}

	/**
	 *
	 */
	private void setupSpecialPage(Locale locale, String virtualWiki, String topicName, WikiUser user, boolean adminOnly, Object[] params) throws Exception {
		if (exists(virtualWiki, topicName)) {
			logger.warn("Special page " + virtualWiki + " / " + topicName + " already exists");
			return;
		}
		logger.info("Setting up special page " + virtualWiki + " / " + topicName);
		String contents = PersistencyHandler.readSpecialPage(locale, topicName);
		Topic topic = new Topic();
		topic.setName(topicName);
		topic.setVirtualWiki(virtualWiki);
		topic.setTopicContent(contents);
		topic.setAdminOnly(adminOnly);
		TopicVersion topicVersion = new TopicVersion();
		topicVersion.setVersionContent(contents);
		topicVersion.setAuthorIpAddress(user.getLastLoginIpAddress());
		topicVersion.setAuthorId(new Integer(user.getUserId()));
		// FIXME - hard coding
		topicVersion.setEditComment("Automatically created by system setup");
		writeTopic(topic, topicVersion, params);
	}

	/**
	 *
	 */
	public void setupSpecialPages(Locale locale, WikiUser user, VirtualWiki virtualWiki) throws Exception {
		Object params[] = null;
		try {
			params = this.initParams();
			// create the default topics
			setupSpecialPage(locale, virtualWiki.getName(), WikiBase.SPECIAL_PAGE_STARTING_POINTS, user, false, params);
			setupSpecialPage(locale, virtualWiki.getName(), WikiBase.SPECIAL_PAGE_LEFT_MENU, user, true, params);
			setupSpecialPage(locale, virtualWiki.getName(), WikiBase.SPECIAL_PAGE_BOTTOM_AREA, user, true, params);
			setupSpecialPage(locale, virtualWiki.getName(), WikiBase.SPECIAL_PAGE_STYLESHEET, user, true, params);
			setupSpecialPage(locale, virtualWiki.getName(), WikiBase.SPECIAL_PAGE_ADMIN_ONLY_TOPICS, user, true, params);
		} catch (Exception e) {
			this.handleErrors(params);
			throw e;
		} finally {
			this.releaseParams(params);
		}
	}

	/**
	 *
	 */
	private void setupSpecialPages(Locale locale, WikiUser user, Object[] params) throws Exception {
		Collection all = getVirtualWikiList();
		for (Iterator iterator = all.iterator(); iterator.hasNext();) {
			VirtualWiki virtualWiki = (VirtualWiki)iterator.next();
			// create the default topics
			setupSpecialPage(locale, virtualWiki.getName(), WikiBase.SPECIAL_PAGE_STARTING_POINTS, user, false, params);
			setupSpecialPage(locale, virtualWiki.getName(), WikiBase.SPECIAL_PAGE_LEFT_MENU, user, true, params);
			setupSpecialPage(locale, virtualWiki.getName(), WikiBase.SPECIAL_PAGE_BOTTOM_AREA, user, true, params);
			setupSpecialPage(locale, virtualWiki.getName(), WikiBase.SPECIAL_PAGE_STYLESHEET, user, true, params);
			setupSpecialPage(locale, virtualWiki.getName(), WikiBase.SPECIAL_PAGE_ADMIN_ONLY_TOPICS, user, true, params);
		}
	}

	/**
	 *
	 */
	public void updateSpecialPage(Locale locale, String virtualWiki, String topicName, WikiUser user) throws Exception {
		logger.info("Updating special page " + virtualWiki + " / " + topicName);
		Object params[] = null;
		try {
			params = this.initParams();
			String contents = PersistencyHandler.readSpecialPage(locale, topicName);
			Topic topic = this.lookupTopic(virtualWiki, topicName);
			topic.setTopicContent(contents);
			TopicVersion topicVersion = new TopicVersion();
			topicVersion.setVersionContent(contents);
			topicVersion.setAuthorIpAddress(user.getLastLoginIpAddress());
			topicVersion.setAuthorId(new Integer(user.getUserId()));
			// FIXME - hard coding
			topicVersion.setEditComment("Automatically updated by system upgrade");
			writeTopic(topic, topicVersion, params);
		} catch (Exception e) {
			this.handleErrors(params);
			throw e;
		} finally {
			this.releaseParams(params);
		}
	}

	/**
	 *
	 */
	protected abstract void updateTopic(Topic topic, Object[] params) throws Exception;

	/**
	 *
	 */
	protected abstract void updateVirtualWiki(VirtualWiki virtualWiki, Object[] params) throws Exception;

	/**
	 *
	 */
	protected abstract void updateWikiFile(String topicName, WikiFile wikiFile, Object[] params) throws Exception;

	/**
	 *
	 */
	protected abstract void updateWikiUser(WikiUser user, Object[] params) throws Exception;

	/**
	 *
	 */
	public synchronized void writeFile(String topicName, WikiFile wikiFile, WikiFileVersion wikiFileVersion) throws Exception {
		Object params[] = null;
		try {
			params = this.initParams();
			if (wikiFile.getFileId() <= 0) {
				addWikiFile(topicName, wikiFile, params);
			} else {
				updateWikiFile(topicName, wikiFile, params);
			}
			wikiFileVersion.setFileId(wikiFile.getFileId());
			if (Environment.getBooleanValue(Environment.PROP_TOPIC_VERSIONING_ON)) {
				// write version
				addWikiFileVersion(wikiFile.getVirtualWiki(), wikiFile.getFileName(), wikiFileVersion, params);
			}
		} catch (Exception e) {
			this.handleErrors(params);
			throw e;
		} finally {
			this.releaseParams(params);
		}
	}

	/**
	 *
	 */
	public void writeReadOnlyTopic(String virtualWiki, String topicName) throws Exception {
		Object params[] = null;
		try {
			params = this.initParams();
			this.writeReadOnlyTopic(virtualWiki, topicName, params);
		} catch (Exception e) {
			this.handleErrors(params);
			throw e;
		} finally {
			this.releaseParams(params);
		}
	}

	/**
	 *
	 */
	public void writeReadOnlyTopic(String virtualWiki, String topicName, Object[] params) throws Exception {
		Topic topic = lookupTopic(virtualWiki, topicName, params);
		topic.setReadOnly(true);
		updateTopic(topic, params);
	}

	/**
	 *
	 */
	public synchronized void writeTopic(Topic topic, TopicVersion topicVersion) throws Exception {
		Object params[] = null;
		try {
			params = this.initParams();
			WikiUser user = null;
			if (topicVersion.getAuthorId() != null) {
				user = lookupWikiUser(topicVersion.getAuthorId().intValue(), params);
			}
			this.writeTopic(topic, topicVersion, params);
			LuceneSearchEngine.deleteFromIndex(topic);
			LuceneSearchEngine.addToIndex(topic);
		} catch (Exception e) {
			this.handleErrors(params);
			throw e;
		} finally {
			this.releaseParams(params);
		}
	}

	/**
	 *
	 */
	protected synchronized void writeTopic(Topic topic, TopicVersion topicVersion, Object[] params) throws Exception {
		if (!Utilities.validateName(topic.getName())) {
			throw new WikiException(new WikiMessage("common.exception.name", topic.getName()));
		}
		if (topic.getTopicId() <= 0) {
			addTopic(topic, params);
		} else {
			updateTopic(topic, params);
		}
		if (topicVersion.getPreviousTopicVersionId() == null) {
			TopicVersion tmp = lookupLastTopicVersion(topic.getVirtualWiki(), topic.getName(), params);
			if (tmp != null) topicVersion.setPreviousTopicVersionId(new Integer(tmp.getTopicVersionId()));
		}
		// reset topic non-existence vector
		cachedNonTopicsList = new WikiCacheMap(MAX_CACHED_LIST_SIZE);
		topicVersion.setTopicId(topic.getTopicId());
		if (Environment.getBooleanValue(Environment.PROP_TOPIC_VERSIONING_ON)) {
			// write version
			addTopicVersion(topic.getVirtualWiki(), topic.getName(), topicVersion, params);
		}
		String authorName = topicVersion.getAuthorIpAddress();
		if (topicVersion.getAuthorId() != null) {
			WikiUser user = lookupWikiUser(topicVersion.getAuthorId().intValue(), params);
			authorName = user.getLogin();
			if (!StringUtils.hasText(authorName)) authorName = user.getLogin();
		}
		RecentChange change = new RecentChange();
		change.setTopicId(topic.getTopicId());
		change.setTopicName(topic.getName());
		change.setTopicVersionId(topicVersion.getTopicVersionId());
		change.setPreviousTopicVersionId(topicVersion.getPreviousTopicVersionId());
		change.setAuthorId(topicVersion.getAuthorId());
		change.setAuthorName(authorName);
		change.setEditComment(topicVersion.getEditComment());
		change.setEditDate(topicVersion.getEditDate());
		change.setEditType(topicVersion.getEditType());
		change.setVirtualWiki(topic.getVirtualWiki());
		addRecentChange(change, params);
	}

	/**
	 *
	 */
	public void writeVirtualWiki(VirtualWiki virtualWiki) throws Exception {
		if (!Utilities.validateName(virtualWiki.getName())) {
			throw new WikiException(new WikiMessage("common.exception.name", virtualWiki.getName()));
		}
		Object params[] = null;
		try {
			params = this.initParams();
			if (virtualWiki.getVirtualWikiId() <= 0) {
				this.addVirtualWiki(virtualWiki, params);
			} else {
				this.updateVirtualWiki(virtualWiki, params);
			}
		} catch (Exception e) {
			this.handleErrors(params);
			throw e;
		} finally {
			this.releaseParams(params);
		}
		// update the hashtable AFTER the commit
		this.loadVirtualWikiHashes();
	}

	/**
	 *
	 */
	public void writeWikiUser(WikiUser user) throws Exception {
		if (!Utilities.validateName(user.getLogin())) {
			throw new WikiException(new WikiMessage("common.exception.name", user.getLogin()));
		}
		Object params[] = null;
		try {
			params = this.initParams();
			if (user.getUserId() <= 0) {
				this.addWikiUser(user, params);
			} else {
				this.updateWikiUser(user, params);
			}
		} catch (Exception e) {
			this.handleErrors(params);
			throw e;
		} finally {
			this.releaseParams(params);
		}
	}
}
