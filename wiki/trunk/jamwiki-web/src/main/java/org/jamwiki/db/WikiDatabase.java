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
package org.jamwiki.db;

import java.io.File;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Vector;
import org.apache.commons.lang.StringUtils;
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.model.Role;
import org.jamwiki.model.Topic;
import org.jamwiki.model.TopicVersion;
import org.jamwiki.model.VirtualWiki;
import org.jamwiki.model.WikiGroup;
import org.jamwiki.model.WikiUser;
import org.jamwiki.model.WikiUserInfo;
import org.jamwiki.utils.WikiLogger;
import org.jamwiki.utils.WikiUtil;
import org.springframework.transaction.TransactionStatus;

/**
 * This class contains general database utility methods that are useful for a
 * variety of JAMWiki database functions, including setup and upgrades.
 */
public class WikiDatabase {

	private static String CONNECTION_VALIDATION_QUERY = null;
	private static String EXISTENCE_VALIDATION_QUERY = null;
	private static final WikiLogger logger = WikiLogger.getLogger(WikiDatabase.class.getName());

	/**
	 *
	 */
	private WikiDatabase() {
	}

	/**
	 * Dump the database to a CSV file.  This is an HSQL-specific method useful
	 * for individuals who want to convert from HSQL to another database.
	 */
	public static void exportToCsv() throws Exception {
		if (!(WikiBase.getDataHandler() instanceof HSqlDataHandler)) {
			throw new IllegalStateException("Exporting to CSV is allowed only when the wiki is configured to use the internal database setting.");
		}
		WikiPreparedStatement stmt = null;
		String sql = null;
		String exportTableName = null;
		String[] tableNames = {
				"jam_category",
				"jam_group",
				"jam_recent_change",
				"jam_role_map",
				"jam_role",
				"jam_topic",
				"jam_topic_version",
				"jam_virtual_wiki",
				"jam_watchlist",
				"jam_file",
				"jam_file_version",
				"jam_wiki_user",
				"jam_wiki_user_info"
		};
		String csvDirectory = new File(Environment.getValue(Environment.PROP_BASE_FILE_DIR), "database").getPath();
		File csvFile = null;
		TransactionStatus status = DatabaseConnection.startTransaction();
		try {
			// make sure CSV files are encoded UTF-8
			// TODO: this does not seem to be working currently - HSQL bug?
			sql = "set property \"textdb.encoding\" 'UTF-8'";
			stmt = new WikiPreparedStatement(sql);
			stmt.executeUpdate();
			for (int i=0; i < tableNames.length; i++) {
				exportTableName = tableNames[i] + "_export";
				// first drop any pre-existing CSV database files.
				sql = "drop table " + exportTableName + " if exists";
				stmt = new WikiPreparedStatement(sql);
				stmt.executeUpdate();
				// now delete the CSV file if it exists
				csvFile = new File(csvDirectory, exportTableName + ".csv");
				if (csvFile.exists()) {
					if (csvFile.delete()) {
						logger.info("Deleted existing CSV file: " + csvFile.getPath());
					} else {
						logger.warning("Could not delete existing CSV file: " + csvFile.getPath());
					}
				}
				// create the CSV files
				sql = "select * into text " + exportTableName + " from " + tableNames[i];
				stmt = new WikiPreparedStatement(sql);
				stmt.executeUpdate();
			}
			// rebuild the data files to make sure everything is committed to disk
			sql = "checkpoint";
			stmt = new WikiPreparedStatement(sql);
			stmt.executeUpdate();
		} catch (Exception e) {
			DatabaseConnection.rollbackOnException(status, e);
			throw e;
		} catch (Error err) {
			DatabaseConnection.rollbackOnException(status, err);
			throw err;
		}
		DatabaseConnection.commit(status);
	}

	/**
	 *
	 */
	protected static String getConnectionValidationQuery() {
		return (!StringUtils.isBlank(CONNECTION_VALIDATION_QUERY)) ? CONNECTION_VALIDATION_QUERY : null;
	}

	/**
	 *
	 */
	protected static String getExistenceValidationQuery() {
		return (!StringUtils.isBlank(EXISTENCE_VALIDATION_QUERY)) ? EXISTENCE_VALIDATION_QUERY : null;
	}

	/**
	 *
	 */
	public synchronized static void initialize() {
		try {
			WikiDatabase.CONNECTION_VALIDATION_QUERY = WikiDatabase.queryHandler().connectionValidationQuery();
			WikiDatabase.EXISTENCE_VALIDATION_QUERY = WikiDatabase.queryHandler().existenceValidationQuery();
			// initialize connection pool in its own try-catch to avoid an error
			// causing property values not to be saved.
			// this clears out any existing connection pool, so that a new one will be created on first access
			DatabaseConnection.closeConnectionPool();
		} catch (Exception e) {
			logger.severe("Unable to initialize database", e);
		}
	}

	public synchronized static void shutdown() {
		try {
			DatabaseConnection.closeConnectionPool();
		} catch (Exception e) {
			logger.severe("Unable to close the connection pool on shutdown", e);
		}
	}

	/**
	 * This method causes all existing data to be deleted from the Wiki.  Use only
	 * when totally re-initializing a system.  To reiterate: CALLING THIS METHOD WILL
	 * DELETE ALL WIKI DATA!
	 */
	protected static void purgeData(Connection conn) throws Exception {
		// BOOM!  Everything gone...
		WikiDatabase.queryHandler().dropTables(conn);
		try {
			// re-create empty tables
			WikiDatabase.queryHandler().createTables(conn);
		} catch (Exception e) {
			// creation failure, don't leave tables half-committed
			WikiDatabase.queryHandler().dropTables(conn);
		}
	}

	/**
	 *
	 */
	protected static QueryHandler queryHandler() throws Exception {
		// FIXME - this is ugly
		if (WikiBase.getDataHandler() instanceof AnsiDataHandler) {
			AnsiDataHandler dataHandler = (AnsiDataHandler)WikiBase.getDataHandler();
			return dataHandler.queryHandler();
		}
		throw new Exception("Unable to determine query handler");
	}

	/**
	 *
	 */
	protected static void releaseConnection(Connection conn, Object transactionObject) throws Exception {
		if (transactionObject instanceof Connection) {
			// transaction objects will be released elsewhere
			return;
		}
		WikiDatabase.releaseConnection(conn);
	}

	/**
	 *
	 */
	private static void releaseConnection(Connection conn) throws Exception {
		if (conn == null) {
			return;
		}
		try {
			conn.commit();
		} finally {
			DatabaseConnection.closeConnection(conn);
		}
	}

	/**
	 *
	 */
	protected static void setup(Locale locale, WikiUser user) throws Exception {
		TransactionStatus status = DatabaseConnection.startTransaction();
		try {
			Connection conn = DatabaseConnection.getConnection();
			// set up tables
			WikiDatabase.queryHandler().createTables(conn);

			WikiDatabase.setupDefaultVirtualWiki(conn);
			WikiDatabase.setupRoles(conn);
			WikiDatabase.setupGroups(conn);
			WikiDatabase.setupAdminUser(user, conn);
			WikiDatabase.setupSpecialPages(locale, user, conn);

		} catch (Exception e) {

			DatabaseConnection.rollbackOnException(status, e);

			logger.severe("Unable to set up database tables", e);
			// clean up anything that might have been created
			try {
				Connection conn = DatabaseConnection.getConnection();
				WikiDatabase.queryHandler().dropTables(conn);
			} catch (Exception e2) {}

			throw e;
		} catch (Error err) {
			DatabaseConnection.rollbackOnException(status, err);
			throw err;
		}
		DatabaseConnection.commit(status);		
	}

	/**
	 *
	 */
	private static void setupAdminUser(WikiUser user, Connection conn) throws Exception {
		if (user == null) {
			throw new IllegalArgumentException("Cannot pass null or anonymous WikiUser object to setupAdminUser");
		}
		if (WikiBase.getDataHandler().lookupWikiUser(user.getUserId(), conn) != null) {
			logger.warning("Admin user already exists");
		}
		WikiUserInfo userInfo = null;
		if (WikiBase.getUserHandler().isWriteable()) {
			userInfo = new WikiUserInfo();
			userInfo.setEncodedPassword(user.getPassword());
			userInfo.setUsername(user.getUsername());
			userInfo.setUserId(user.getUserId());
		}
		WikiBase.getDataHandler().writeWikiUser(user, userInfo, conn);
		Vector roles = new Vector();
		roles.add(Role.ROLE_ADMIN.getAuthority());
		roles.add(Role.ROLE_SYSADMIN.getAuthority());
		roles.add(Role.ROLE_TRANSLATE.getAuthority());
		WikiBase.getDataHandler().writeRoleMapUser(user.getUserId(), roles, conn);
	}

	/**
	 *
	 */
	public static void setupDefaultDatabase(Properties props) {
		props.setProperty(Environment.PROP_DB_DRIVER, "org.hsqldb.jdbcDriver");
		props.setProperty(Environment.PROP_DB_TYPE, WikiBase.DATA_HANDLER_HSQL);
		props.setProperty(Environment.PROP_DB_USERNAME, "sa");
		props.setProperty(Environment.PROP_DB_PASSWORD, "");
		File file = new File(props.getProperty(Environment.PROP_BASE_FILE_DIR), "database");
		if (!file.exists()) {
			file.mkdirs();
		}
		String url = "jdbc:hsqldb:file:" + new File(file.getPath(), "jamwiki").getPath() + ";shutdown=true";
		props.setProperty(Environment.PROP_DB_URL, url);
	}

	/**
	 *
	 */
	private static void setupDefaultVirtualWiki(Connection conn) throws Exception {
		VirtualWiki virtualWiki = new VirtualWiki();
		virtualWiki.setName(WikiBase.DEFAULT_VWIKI);
		virtualWiki.setDefaultTopicName(Environment.getValue(Environment.PROP_BASE_DEFAULT_TOPIC));
		WikiBase.getDataHandler().writeVirtualWiki(virtualWiki, conn);
	}

	/**
	 *
	 */
	protected static void setupGroups(Connection conn) throws Exception {
		WikiGroup group = new WikiGroup();
		group.setName(WikiGroup.GROUP_ANONYMOUS);
		// FIXME - use message key
		group.setDescription("All non-logged in users are automatically assigned to the anonymous group.");
		WikiBase.getDataHandler().writeWikiGroup(group, conn);
		List anonymousRoles = new Vector();
		anonymousRoles.add(Role.ROLE_EDIT_EXISTING.getAuthority());
		anonymousRoles.add(Role.ROLE_EDIT_NEW.getAuthority());
		anonymousRoles.add(Role.ROLE_UPLOAD.getAuthority());
		anonymousRoles.add(Role.ROLE_VIEW.getAuthority());
		WikiBase.getDataHandler().writeRoleMapGroup(group.getGroupId(), anonymousRoles, conn);
		group = new WikiGroup();
		group.setName(WikiGroup.GROUP_REGISTERED_USER);
		// FIXME - use message key
		group.setDescription("All logged in users are automatically assigned to the registered user group.");
		WikiBase.getDataHandler().writeWikiGroup(group, conn);
		List userRoles = new Vector();
		userRoles.add(Role.ROLE_EDIT_EXISTING.getAuthority());
		userRoles.add(Role.ROLE_EDIT_NEW.getAuthority());
		userRoles.add(Role.ROLE_MOVE.getAuthority());
		userRoles.add(Role.ROLE_UPLOAD.getAuthority());
		userRoles.add(Role.ROLE_VIEW.getAuthority());
		WikiBase.getDataHandler().writeRoleMapGroup(group.getGroupId(), userRoles, conn);
	}

	/**
	 *
	 */
	protected static void setupRoles(Connection conn) throws Exception {
		Role role = Role.ROLE_ADMIN;
		// FIXME - use message key
		role.setDescription("Provides the ability to perform wiki maintenance tasks not available to normal users.");
		WikiBase.getDataHandler().writeRole(role, conn, false);
		role = Role.ROLE_EDIT_EXISTING;
		// FIXME - use message key
		role.setDescription("Allows a user to edit an existing topic.");
		WikiBase.getDataHandler().writeRole(role, conn, false);
		role = Role.ROLE_EDIT_NEW;
		// FIXME - use message key
		role.setDescription("Allows a user to create a new topic.");
		WikiBase.getDataHandler().writeRole(role, conn, false);
		role = Role.ROLE_MOVE;
		// FIXME - use message key
		role.setDescription("Allows a user to move a topic to a different name.");
		WikiBase.getDataHandler().writeRole(role, conn, false);
		role = Role.ROLE_SYSADMIN;
		// FIXME - use message key
		role.setDescription("Allows access to set database parameters, modify parser settings, and set other wiki system settings.");
		WikiBase.getDataHandler().writeRole(role, conn, false);
		role = Role.ROLE_TRANSLATE;
		// FIXME - use message key
		role.setDescription("Allows access to the translation tool used for modifying the values of message keys used to display text on the wiki.");
		WikiBase.getDataHandler().writeRole(role, conn, false);
		role = Role.ROLE_UPLOAD;
		// FIXME - use message key
		role.setDescription("Allows a user to upload a file to the wiki.");
		WikiBase.getDataHandler().writeRole(role, conn, false);
		role = Role.ROLE_VIEW;
		// FIXME - use message key
		role.setDescription("Allows a user to view topics on the wiki.");
		WikiBase.getDataHandler().writeRole(role, conn, false);
	}

	/**
	 *
	 */
	protected static void setupSpecialPage(Locale locale, String virtualWiki, String topicName, WikiUser user, boolean adminOnly, Connection conn) throws Exception {
		logger.info("Setting up special page " + virtualWiki + " / " + topicName);
		if (user == null) {
			throw new IllegalArgumentException("Cannot pass null WikiUser object to setupSpecialPage");
		}
		String contents = WikiUtil.readSpecialPage(locale, topicName);
		Topic topic = new Topic();
		topic.setName(topicName);
		topic.setVirtualWiki(virtualWiki);
		topic.setTopicContent(contents);
		topic.setAdminOnly(adminOnly);
		// FIXME - hard coding
		TopicVersion topicVersion = new TopicVersion(user, user.getLastLoginIpAddress(), "Automatically created by system setup", contents);
		// FIXME - it is not connection-safe to parse for metadata since we are already holding a connection
		// ParserOutput parserOutput = ParserUtil.parserOutput(topic.getTopicContent(), virtualWiki, topicName);
		// WikiBase.getDataHandler().writeTopic(topic, topicVersion, parserOutput.getCategories(), parserOutput.getLinks(), true, conn);
		WikiBase.getDataHandler().writeTopic(topic, topicVersion, null, null, true, conn);
	}

	/**
	 *
	 */
	private static void setupSpecialPages(Locale locale, WikiUser user, Connection conn) throws Exception {
		List all = WikiBase.getDataHandler().getVirtualWikiList(conn);
		for (Iterator iterator = all.iterator(); iterator.hasNext();) {
			VirtualWiki virtualWiki = (VirtualWiki)iterator.next();
			// create the default topics
			setupSpecialPage(locale, virtualWiki.getName(), WikiBase.SPECIAL_PAGE_STARTING_POINTS, user, false, conn);
			setupSpecialPage(locale, virtualWiki.getName(), WikiBase.SPECIAL_PAGE_LEFT_MENU, user, true, conn);
			setupSpecialPage(locale, virtualWiki.getName(), WikiBase.SPECIAL_PAGE_BOTTOM_AREA, user, true, conn);
			setupSpecialPage(locale, virtualWiki.getName(), WikiBase.SPECIAL_PAGE_STYLESHEET, user, true, conn);
		}
	}
}
