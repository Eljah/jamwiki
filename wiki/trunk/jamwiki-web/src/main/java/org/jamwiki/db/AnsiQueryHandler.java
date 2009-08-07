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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.jamwiki.Environment;
import org.jamwiki.authentication.WikiUserDetails;
import org.jamwiki.model.Category;
import org.jamwiki.model.RecentChange;
import org.jamwiki.model.Role;
import org.jamwiki.model.Topic;
import org.jamwiki.model.TopicVersion;
import org.jamwiki.model.VirtualWiki;
import org.jamwiki.model.WikiFile;
import org.jamwiki.model.WikiFileVersion;
import org.jamwiki.model.WikiGroup;
import org.jamwiki.model.WikiUser;
import org.jamwiki.utils.Pagination;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiLogger;

/**
 * Default implementation of the QueryHandler implementation for retrieving, inserting,
 * and updating data in the database.  This method uses ANSI SQL and should therefore
 * work with any fully ANSI-compliant database.
 */
public class AnsiQueryHandler implements QueryHandler {

	private static final WikiLogger logger = WikiLogger.getLogger(AnsiQueryHandler.class.getName());
	protected static final String SQL_PROPERTY_FILE_NAME = "sql.ansi.properties";

	protected static String STATEMENT_CONNECTION_VALIDATION_QUERY = null;
	protected static String STATEMENT_CREATE_AUTHORITIES_TABLE = null;
	protected static String STATEMENT_CREATE_CATEGORY_TABLE = null;
	protected static String STATEMENT_CREATE_GROUP_AUTHORITIES_TABLE = null;
	protected static String STATEMENT_CREATE_GROUP_MEMBERS_TABLE = null;
	protected static String STATEMENT_CREATE_GROUP_TABLE = null;
	protected static String STATEMENT_CREATE_RECENT_CHANGE_TABLE = null;
	protected static String STATEMENT_CREATE_ROLE_TABLE = null;
	protected static String STATEMENT_CREATE_TOPIC_CURRENT_VERSION_CONSTRAINT = null;
	protected static String STATEMENT_CREATE_TOPIC_TABLE = null;
	protected static String STATEMENT_CREATE_TOPIC_VERSION_TABLE = null;
	protected static String STATEMENT_CREATE_USERS_TABLE = null;
	protected static String STATEMENT_CREATE_VIRTUAL_WIKI_TABLE = null;
	protected static String STATEMENT_CREATE_WATCHLIST_TABLE = null;
	protected static String STATEMENT_CREATE_WIKI_FILE_TABLE = null;
	protected static String STATEMENT_CREATE_WIKI_FILE_VERSION_TABLE = null;
	protected static String STATEMENT_CREATE_WIKI_USER_TABLE = null;
	protected static String STATEMENT_CREATE_WIKI_USER_LOGIN_INDEX = null;
	protected static String STATEMENT_DELETE_AUTHORITIES = null;
	protected static String STATEMENT_DELETE_GROUP_AUTHORITIES = null;
	protected static String STATEMENT_DELETE_RECENT_CHANGES = null;
	protected static String STATEMENT_DELETE_RECENT_CHANGES_TOPIC = null;
	protected static String STATEMENT_DELETE_TOPIC_CATEGORIES = null;
	protected static String STATEMENT_DELETE_WATCHLIST_ENTRY = null;
	protected static String STATEMENT_DROP_AUTHORITIES_TABLE = null;
	protected static String STATEMENT_DROP_CATEGORY_TABLE = null;
	protected static String STATEMENT_DROP_GROUP_AUTHORITIES_TABLE = null;
	protected static String STATEMENT_DROP_GROUP_MEMBERS_TABLE = null;
	protected static String STATEMENT_DROP_GROUP_TABLE = null;
	protected static String STATEMENT_DROP_RECENT_CHANGE_TABLE = null;
	protected static String STATEMENT_DROP_ROLE_TABLE = null;
	protected static String STATEMENT_DROP_TOPIC_CURRENT_VERSION_CONSTRAINT = null;
	protected static String STATEMENT_DROP_TOPIC_TABLE = null;
	protected static String STATEMENT_DROP_TOPIC_VERSION_TABLE = null;
	protected static String STATEMENT_DROP_USERS_TABLE = null;
	protected static String STATEMENT_DROP_VIRTUAL_WIKI_TABLE = null;
	protected static String STATEMENT_DROP_WATCHLIST_TABLE = null;
	protected static String STATEMENT_DROP_WIKI_FILE_TABLE = null;
	protected static String STATEMENT_DROP_WIKI_FILE_VERSION_TABLE = null;
	protected static String STATEMENT_DROP_WIKI_USER_TABLE = null;
	protected static String STATEMENT_DROP_WIKI_USER_LOGIN_INDEX = null;
	protected static String STATEMENT_INSERT_AUTHORITY = null;
	protected static String STATEMENT_INSERT_CATEGORY = null;
	protected static String STATEMENT_INSERT_GROUP = null;
	protected static String STATEMENT_INSERT_GROUP_AUTHORITY = null;
	protected static String STATEMENT_INSERT_GROUP_MEMBER = null;
	protected static String STATEMENT_INSERT_RECENT_CHANGE = null;
	protected static String STATEMENT_INSERT_RECENT_CHANGES = null;
	protected static String STATEMENT_INSERT_ROLE = null;
	protected static String STATEMENT_INSERT_TOPIC = null;
	protected static String STATEMENT_INSERT_TOPIC_VERSION = null;
	protected static String STATEMENT_INSERT_USER = null;
	protected static String STATEMENT_INSERT_VIRTUAL_WIKI = null;
	protected static String STATEMENT_INSERT_WATCHLIST_ENTRY = null;
	protected static String STATEMENT_INSERT_WIKI_FILE = null;
	protected static String STATEMENT_INSERT_WIKI_FILE_VERSION = null;
	protected static String STATEMENT_INSERT_WIKI_USER = null;
	protected static String STATEMENT_SELECT_AUTHORITIES_AUTHORITY = null;
	protected static String STATEMENT_SELECT_AUTHORITIES_LOGIN = null;
	protected static String STATEMENT_SELECT_AUTHORITIES_USER = null;
	protected static String STATEMENT_SELECT_CATEGORIES = null;
	protected static String STATEMENT_SELECT_CATEGORY_TOPICS = null;
	protected static String STATEMENT_SELECT_GROUP = null;
	protected static String STATEMENT_SELECT_GROUP_AUTHORITIES = null;
	protected static String STATEMENT_SELECT_GROUPS_AUTHORITIES = null;
	protected static String STATEMENT_SELECT_GROUP_MEMBERS_SEQUENCE = null;
	protected static String STATEMENT_SELECT_GROUP_SEQUENCE = null;
	protected static String STATEMENT_SELECT_RECENT_CHANGES = null;
	protected static String STATEMENT_SELECT_RECENT_CHANGES_TOPIC = null;
	protected static String STATEMENT_SELECT_ROLES = null;
	protected static String STATEMENT_SELECT_TOPIC_BY_TYPE = null;
	protected static String STATEMENT_SELECT_TOPIC_COUNT = null;
	protected static String STATEMENT_SELECT_TOPIC = null;
	protected static String STATEMENT_SELECT_TOPIC_LOWER = null;
	protected static String STATEMENT_SELECT_TOPICS = null;
	protected static String STATEMENT_SELECT_TOPICS_ADMIN = null;
	protected static String STATEMENT_SELECT_TOPIC_SEQUENCE = null;
	protected static String STATEMENT_SELECT_TOPIC_VERSION = null;
	protected static String STATEMENT_SELECT_TOPIC_VERSION_SEQUENCE = null;
	protected static String STATEMENT_SELECT_USERS_AUTHENTICATION = null;
	protected static String STATEMENT_SELECT_VIRTUAL_WIKIS = null;
	protected static String STATEMENT_SELECT_VIRTUAL_WIKI_SEQUENCE = null;
	protected static String STATEMENT_SELECT_WATCHLIST = null;
	protected static String STATEMENT_SELECT_WATCHLIST_CHANGES = null;
	protected static String STATEMENT_SELECT_WIKI_FILE = null;
	protected static String STATEMENT_SELECT_WIKI_FILE_COUNT = null;
	protected static String STATEMENT_SELECT_WIKI_FILE_SEQUENCE = null;
	protected static String STATEMENT_SELECT_WIKI_FILE_VERSION_SEQUENCE = null;
	protected static String STATEMENT_SELECT_WIKI_FILE_VERSIONS = null;
	protected static String STATEMENT_SELECT_WIKI_USER = null;
	protected static String STATEMENT_SELECT_WIKI_USER_CHANGES_ANONYMOUS = null;
	protected static String STATEMENT_SELECT_WIKI_USER_CHANGES_LOGIN = null;
	protected static String STATEMENT_SELECT_WIKI_USER_COUNT = null;
	protected static String STATEMENT_SELECT_WIKI_USER_DETAILS_PASSWORD = null;
	protected static String STATEMENT_SELECT_WIKI_USER_LOGIN = null;
	protected static String STATEMENT_SELECT_WIKI_USER_SEQUENCE = null;
	protected static String STATEMENT_SELECT_WIKI_USERS = null;
	protected static String STATEMENT_UPDATE_GROUP = null;
	protected static String STATEMENT_UPDATE_ROLE = null;
	protected static String STATEMENT_UPDATE_TOPIC = null;
	protected static String STATEMENT_UPDATE_TOPIC_CURRENT_VERSION = null;
	protected static String STATEMENT_UPDATE_USER = null;
	protected static String STATEMENT_UPDATE_VIRTUAL_WIKI = null;
	protected static String STATEMENT_UPDATE_WIKI_FILE = null;
	protected static String STATEMENT_UPDATE_WIKI_USER = null;
	private Properties props = null;

	/**
	 *
	 */
	protected AnsiQueryHandler() {
		props = Environment.loadProperties(SQL_PROPERTY_FILE_NAME);
		this.init(props);
	}

	/**
	 *
	 */
	public boolean authenticateUser(String username, String encryptedPassword, Connection conn) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_USERS_AUTHENTICATION);
		stmt.setString(1, username);
		stmt.setString(2, encryptedPassword);
		return (stmt.executeQuery(conn).size() != 0);
	}

	/**
	 *
	 */
	public String connectionValidationQuery() {
		return STATEMENT_CONNECTION_VALIDATION_QUERY;
	}

	/**
	 *
	 */
	public void createTables(Connection conn) throws SQLException {
		DatabaseConnection.executeUpdate(STATEMENT_CREATE_VIRTUAL_WIKI_TABLE, conn);
		DatabaseConnection.executeUpdate(STATEMENT_CREATE_USERS_TABLE, conn);
		DatabaseConnection.executeUpdate(STATEMENT_CREATE_WIKI_USER_TABLE, conn);
		DatabaseConnection.executeUpdate(STATEMENT_CREATE_WIKI_USER_LOGIN_INDEX, conn);
		DatabaseConnection.executeUpdate(STATEMENT_CREATE_TOPIC_TABLE, conn);
		DatabaseConnection.executeUpdate(STATEMENT_CREATE_TOPIC_VERSION_TABLE, conn);
		DatabaseConnection.executeUpdate(STATEMENT_CREATE_TOPIC_CURRENT_VERSION_CONSTRAINT, conn);
		DatabaseConnection.executeUpdate(STATEMENT_CREATE_WIKI_FILE_TABLE, conn);
		DatabaseConnection.executeUpdate(STATEMENT_CREATE_WIKI_FILE_VERSION_TABLE, conn);
		DatabaseConnection.executeUpdate(STATEMENT_CREATE_CATEGORY_TABLE, conn);
		DatabaseConnection.executeUpdate(STATEMENT_CREATE_GROUP_TABLE, conn);
		DatabaseConnection.executeUpdate(STATEMENT_CREATE_GROUP_MEMBERS_TABLE, conn);
		DatabaseConnection.executeUpdate(STATEMENT_CREATE_ROLE_TABLE, conn);
		DatabaseConnection.executeUpdate(STATEMENT_CREATE_AUTHORITIES_TABLE, conn);
		DatabaseConnection.executeUpdate(STATEMENT_CREATE_GROUP_AUTHORITIES_TABLE, conn);
		DatabaseConnection.executeUpdate(STATEMENT_CREATE_RECENT_CHANGE_TABLE, conn);
		DatabaseConnection.executeUpdate(STATEMENT_CREATE_WATCHLIST_TABLE, conn);
	}

	/**
	 *
	 */
	public void deleteGroupAuthorities(int groupId, Connection conn) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_DELETE_GROUP_AUTHORITIES);
		stmt.setInt(1, groupId);
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public void deleteRecentChanges(int topicId, Connection conn) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_DELETE_RECENT_CHANGES_TOPIC);
		stmt.setInt(1, topicId);
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public void deleteTopicCategories(int childTopicId, Connection conn) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_DELETE_TOPIC_CATEGORIES);
		stmt.setInt(1, childTopicId);
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public void deleteUserAuthorities(String username, Connection conn) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_DELETE_AUTHORITIES);
		stmt.setString(1, username);
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public void deleteWatchlistEntry(int virtualWikiId, String topicName, int userId, Connection conn) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_DELETE_WATCHLIST_ENTRY);
		stmt.setInt(1, virtualWikiId);
		stmt.setString(2, topicName);
		stmt.setInt(3, userId);
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public void dropTables(Connection conn) {
		// note that this method is called during creation failures, so be careful to
		// catch errors that might result from a partial failure during install.  also
		// note that the coding style violation here is intentional since it makes the
		// actual work of the method more obvious.
		try {
			DatabaseConnection.executeUpdate(STATEMENT_DROP_WATCHLIST_TABLE, conn);
		} catch (SQLException e) { logger.severe(e.getMessage()); }
		try {
			DatabaseConnection.executeUpdate(STATEMENT_DROP_RECENT_CHANGE_TABLE, conn);
		} catch (SQLException e) { logger.severe(e.getMessage()); }
		try {
			DatabaseConnection.executeUpdate(STATEMENT_DROP_GROUP_AUTHORITIES_TABLE, conn);
		} catch (SQLException e) { logger.severe(e.getMessage()); }
		try {
			DatabaseConnection.executeUpdate(STATEMENT_DROP_AUTHORITIES_TABLE, conn);
		} catch (SQLException e) { logger.severe(e.getMessage()); }
		try {
			DatabaseConnection.executeUpdate(STATEMENT_DROP_ROLE_TABLE, conn);
		} catch (SQLException e) { logger.severe(e.getMessage()); }
		try {
			DatabaseConnection.executeUpdate(STATEMENT_DROP_GROUP_MEMBERS_TABLE, conn);
		} catch (SQLException e) { logger.severe(e.getMessage()); }
		try {
			DatabaseConnection.executeUpdate(STATEMENT_DROP_GROUP_TABLE, conn);
		} catch (SQLException e) { logger.severe(e.getMessage()); }
		try {
			DatabaseConnection.executeUpdate(STATEMENT_DROP_CATEGORY_TABLE, conn);
		} catch (SQLException e) { logger.severe(e.getMessage()); }
		try {
			DatabaseConnection.executeUpdate(STATEMENT_DROP_WIKI_FILE_VERSION_TABLE, conn);
		} catch (SQLException e) { logger.severe(e.getMessage()); }
		try {
			DatabaseConnection.executeUpdate(STATEMENT_DROP_WIKI_FILE_TABLE, conn);
		} catch (SQLException e) { logger.severe(e.getMessage()); }
		try {
			DatabaseConnection.executeUpdate(STATEMENT_DROP_TOPIC_CURRENT_VERSION_CONSTRAINT, conn);
		} catch (SQLException e) { logger.severe(e.getMessage()); }
		try {
			DatabaseConnection.executeUpdate(STATEMENT_DROP_TOPIC_VERSION_TABLE, conn);
		} catch (SQLException e) { logger.severe(e.getMessage()); }
		try {
			DatabaseConnection.executeUpdate(STATEMENT_DROP_TOPIC_TABLE, conn);
		} catch (SQLException e) { logger.severe(e.getMessage()); }
		try {
			DatabaseConnection.executeUpdate(STATEMENT_DROP_WIKI_USER_LOGIN_INDEX, conn);
		} catch (SQLException e) { logger.severe(e.getMessage()); }
		try {
			DatabaseConnection.executeUpdate(STATEMENT_DROP_WIKI_USER_TABLE, conn);
		} catch (SQLException e) { logger.severe(e.getMessage()); }
		try {
			DatabaseConnection.executeUpdate(STATEMENT_DROP_USERS_TABLE, conn);
		} catch (SQLException e) { logger.severe(e.getMessage()); }
		try {
			DatabaseConnection.executeUpdate(STATEMENT_DROP_VIRTUAL_WIKI_TABLE, conn);
		} catch (SQLException e) { logger.severe(e.getMessage()); }
	}

	/**
	 *
	 */
	public void executeUpgradeUpdate(String prop, Connection conn) throws SQLException {
		String sql = this.props.getProperty(prop);
		if (sql == null) {
			throw new SQLException("No property found for " + prop);
		}
		WikiPreparedStatement stmt = new WikiPreparedStatement(sql);
		stmt.executeUpdate(conn);
	}

	/**
	 * Return a simple query, that if successfully run indicates that JAMWiki
	 * tables have been initialized in the database.
	 *
	 * @return Returns a simple query that, if successfully run, indicates
	 *  that JAMWiki tables have been set up in the database.
	 */
	public String existenceValidationQuery() {
		return STATEMENT_SELECT_VIRTUAL_WIKIS;
	}

	/**
	 *
	 */
	public WikiResultSet getAllTopicNames(int virtualWikiId) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_TOPICS);
		stmt.setInt(1, virtualWikiId);
		return stmt.executeQuery();
	}

	/**
	 *
	 */
	public WikiResultSet getAllWikiFileVersions(WikiFile wikiFile, boolean descending) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_WIKI_FILE_VERSIONS);
		// FIXME - sort order ignored
		stmt.setInt(1, wikiFile.getFileId());
		return stmt.executeQuery();
	}

	/**
	 *
	 */
	public WikiResultSet getCategories(int virtualWikiId, Pagination pagination) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_CATEGORIES);
		stmt.setInt(1, virtualWikiId);
		stmt.setInt(2, pagination.getNumResults());
		stmt.setInt(3, pagination.getOffset());
		return stmt.executeQuery();
	}

	/**
	 *
	 */
	public WikiResultSet getRecentChanges(String virtualWiki, Pagination pagination, boolean descending) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_RECENT_CHANGES);
		stmt.setString(1, virtualWiki);
		stmt.setInt(2, pagination.getNumResults());
		stmt.setInt(3, pagination.getOffset());
		// FIXME - sort order ignored
		return stmt.executeQuery();
	}

	/**
	 *
	 */
	public WikiResultSet getRecentChanges(int topicId, Pagination pagination, boolean descending) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_RECENT_CHANGES_TOPIC);
		stmt.setInt(1, topicId);
		stmt.setInt(2, pagination.getNumResults());
		stmt.setInt(3, pagination.getOffset());
		// FIXME - sort order ignored
		return stmt.executeQuery();
	}

	/**
	 *
	 */
	public WikiResultSet getRoleMapByLogin(String loginFragment) throws SQLException {
		if (StringUtils.isBlank(loginFragment)) {
			return new WikiResultSet();
		}
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_AUTHORITIES_LOGIN);
		loginFragment = '%' + loginFragment.toLowerCase() + '%';
		stmt.setString(1, loginFragment);
		return stmt.executeQuery();
	}

	/**
	 *
	 */
	public WikiResultSet getRoleMapByRole(String authority) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_AUTHORITIES_AUTHORITY);
		stmt.setString(1, authority);
		stmt.setString(2, authority);
		return stmt.executeQuery();
	}

	/**
	 *
	 */
	public WikiResultSet getRoleMapGroup(String groupName) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_GROUP_AUTHORITIES);
		stmt.setString(1, groupName);
		return stmt.executeQuery();
	}

	/**
	 *
	 */
	public WikiResultSet getRoleMapGroups() throws SQLException {
		return DatabaseConnection.executeQuery(STATEMENT_SELECT_GROUPS_AUTHORITIES);
	}

	/**
	 *
	 */
	public WikiResultSet getRoleMapUser(String login) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_AUTHORITIES_USER);
		stmt.setString(1, login);
		return stmt.executeQuery();
	}

	/**
	 *
	 */
	public WikiResultSet getRoles() throws SQLException {
		return DatabaseConnection.executeQuery(STATEMENT_SELECT_ROLES);
	}

	/**
	 *
	 */
	public WikiResultSet getTopicsAdmin(int virtualWikiId, Pagination pagination) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_TOPICS_ADMIN);
		stmt.setInt(1, virtualWikiId);
		stmt.setInt(2, pagination.getNumResults());
		stmt.setInt(3, pagination.getOffset());
		return stmt.executeQuery();
	}

	/**
	 *
	 */
	public WikiResultSet getUserContributionsByLogin(String virtualWiki, String login, Pagination pagination, boolean descending) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_WIKI_USER_CHANGES_LOGIN);
		stmt.setString(1, virtualWiki);
		stmt.setString(2, login);
		stmt.setInt(3, pagination.getNumResults());
		stmt.setInt(4, pagination.getOffset());
		// FIXME - sort order ignored
		return stmt.executeQuery();
	}

	/**
	 *
	 */
	public WikiResultSet getUserContributionsByUserDisplay(String virtualWiki, String userDisplay, Pagination pagination, boolean descending) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_WIKI_USER_CHANGES_ANONYMOUS);
		stmt.setString(1, virtualWiki);
		stmt.setString(2, userDisplay);
		stmt.setInt(3, pagination.getNumResults());
		stmt.setInt(4, pagination.getOffset());
		// FIXME - sort order ignored
		return stmt.executeQuery();
	}

	/**
	 *
	 */
	public WikiResultSet getVirtualWikis(Connection conn) throws SQLException {
		return DatabaseConnection.executeQuery(STATEMENT_SELECT_VIRTUAL_WIKIS, conn);
	}

	/**
	 *
	 */
	public WikiResultSet getWatchlist(int virtualWikiId, int userId) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_WATCHLIST);
		stmt.setInt(1, virtualWikiId);
		stmt.setInt(2, userId);
		return stmt.executeQuery();
	}

	/**
	 *
	 */
	public WikiResultSet getWatchlist(int virtualWikiId, int userId, Pagination pagination) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_WATCHLIST_CHANGES);
		stmt.setInt(1, virtualWikiId);
		stmt.setInt(2, userId);
		stmt.setInt(3, pagination.getNumResults());
		stmt.setInt(4, pagination.getOffset());
		return stmt.executeQuery();
	}

	/**
	 *
	 */
	protected void init(Properties properties) {
		this.props = properties;
		STATEMENT_CONNECTION_VALIDATION_QUERY    = props.getProperty("STATEMENT_CONNECTION_VALIDATION_QUERY");
		STATEMENT_CREATE_GROUP_TABLE             = props.getProperty("STATEMENT_CREATE_GROUP_TABLE");
		STATEMENT_CREATE_ROLE_TABLE              = props.getProperty("STATEMENT_CREATE_ROLE_TABLE");
		STATEMENT_CREATE_VIRTUAL_WIKI_TABLE      = props.getProperty("STATEMENT_CREATE_VIRTUAL_WIKI_TABLE");
		STATEMENT_CREATE_WIKI_USER_TABLE         = props.getProperty("STATEMENT_CREATE_WIKI_USER_TABLE");
		STATEMENT_CREATE_WIKI_USER_LOGIN_INDEX   = props.getProperty("STATEMENT_CREATE_WIKI_USER_LOGIN_INDEX");
		STATEMENT_CREATE_TOPIC_CURRENT_VERSION_CONSTRAINT = props.getProperty("STATEMENT_CREATE_TOPIC_CURRENT_VERSION_CONSTRAINT");
		STATEMENT_CREATE_TOPIC_TABLE             = props.getProperty("STATEMENT_CREATE_TOPIC_TABLE");
		STATEMENT_CREATE_TOPIC_VERSION_TABLE     = props.getProperty("STATEMENT_CREATE_TOPIC_VERSION_TABLE");
		STATEMENT_CREATE_USERS_TABLE             = props.getProperty("STATEMENT_CREATE_USERS_TABLE");
		STATEMENT_CREATE_WIKI_FILE_TABLE         = props.getProperty("STATEMENT_CREATE_WIKI_FILE_TABLE");
		STATEMENT_CREATE_WIKI_FILE_VERSION_TABLE = props.getProperty("STATEMENT_CREATE_WIKI_FILE_VERSION_TABLE");
		STATEMENT_CREATE_AUTHORITIES_TABLE       = props.getProperty("STATEMENT_CREATE_AUTHORITIES_TABLE");
		STATEMENT_CREATE_CATEGORY_TABLE          = props.getProperty("STATEMENT_CREATE_CATEGORY_TABLE");
		STATEMENT_CREATE_GROUP_AUTHORITIES_TABLE = props.getProperty("STATEMENT_CREATE_GROUP_AUTHORITIES_TABLE");
		STATEMENT_CREATE_GROUP_MEMBERS_TABLE     = props.getProperty("STATEMENT_CREATE_GROUP_MEMBERS_TABLE");
		STATEMENT_CREATE_RECENT_CHANGE_TABLE     = props.getProperty("STATEMENT_CREATE_RECENT_CHANGE_TABLE");
		STATEMENT_CREATE_WATCHLIST_TABLE         = props.getProperty("STATEMENT_CREATE_WATCHLIST_TABLE");
		STATEMENT_DELETE_AUTHORITIES             = props.getProperty("STATEMENT_DELETE_AUTHORITIES");
		STATEMENT_DELETE_GROUP_AUTHORITIES       = props.getProperty("STATEMENT_DELETE_GROUP_AUTHORITIES");
		STATEMENT_DELETE_RECENT_CHANGES          = props.getProperty("STATEMENT_DELETE_RECENT_CHANGES");
		STATEMENT_DELETE_RECENT_CHANGES_TOPIC    = props.getProperty("STATEMENT_DELETE_RECENT_CHANGES_TOPIC");
		STATEMENT_DELETE_TOPIC_CATEGORIES        = props.getProperty("STATEMENT_DELETE_TOPIC_CATEGORIES");
		STATEMENT_DELETE_WATCHLIST_ENTRY         = props.getProperty("STATEMENT_DELETE_WATCHLIST_ENTRY");
		STATEMENT_DROP_AUTHORITIES_TABLE         = props.getProperty("STATEMENT_DROP_AUTHORITIES_TABLE");
		STATEMENT_DROP_CATEGORY_TABLE            = props.getProperty("STATEMENT_DROP_CATEGORY_TABLE");
		STATEMENT_DROP_GROUP_AUTHORITIES_TABLE   = props.getProperty("STATEMENT_DROP_GROUP_AUTHORITIES_TABLE");
		STATEMENT_DROP_GROUP_MEMBERS_TABLE       = props.getProperty("STATEMENT_DROP_GROUP_MEMBERS_TABLE");
		STATEMENT_DROP_GROUP_TABLE               = props.getProperty("STATEMENT_DROP_GROUP_TABLE");
		STATEMENT_DROP_RECENT_CHANGE_TABLE       = props.getProperty("STATEMENT_DROP_RECENT_CHANGE_TABLE");
		STATEMENT_DROP_ROLE_TABLE                = props.getProperty("STATEMENT_DROP_ROLE_TABLE");
		STATEMENT_DROP_TOPIC_CURRENT_VERSION_CONSTRAINT = props.getProperty("STATEMENT_DROP_TOPIC_CURRENT_VERSION_CONSTRAINT");
		STATEMENT_DROP_TOPIC_TABLE               = props.getProperty("STATEMENT_DROP_TOPIC_TABLE");
		STATEMENT_DROP_TOPIC_VERSION_TABLE       = props.getProperty("STATEMENT_DROP_TOPIC_VERSION_TABLE");
		STATEMENT_DROP_USERS_TABLE               = props.getProperty("STATEMENT_DROP_USERS_TABLE");
		STATEMENT_DROP_VIRTUAL_WIKI_TABLE        = props.getProperty("STATEMENT_DROP_VIRTUAL_WIKI_TABLE");
		STATEMENT_DROP_WATCHLIST_TABLE           = props.getProperty("STATEMENT_DROP_WATCHLIST_TABLE");
		STATEMENT_DROP_WIKI_USER_LOGIN_INDEX     = props.getProperty("STATEMENT_DROP_WIKI_USER_LOGIN_INDEX");
		STATEMENT_DROP_WIKI_USER_TABLE           = props.getProperty("STATEMENT_DROP_WIKI_USER_TABLE");
		STATEMENT_DROP_WIKI_FILE_TABLE           = props.getProperty("STATEMENT_DROP_WIKI_FILE_TABLE");
		STATEMENT_DROP_WIKI_FILE_VERSION_TABLE   = props.getProperty("STATEMENT_DROP_WIKI_FILE_VERSION_TABLE");
		STATEMENT_INSERT_AUTHORITY               = props.getProperty("STATEMENT_INSERT_AUTHORITY");
		STATEMENT_INSERT_CATEGORY                = props.getProperty("STATEMENT_INSERT_CATEGORY");
		STATEMENT_INSERT_GROUP                   = props.getProperty("STATEMENT_INSERT_GROUP");
		STATEMENT_INSERT_GROUP_AUTHORITY         = props.getProperty("STATEMENT_INSERT_GROUP_AUTHORITY");
		STATEMENT_INSERT_GROUP_MEMBER            = props.getProperty("STATEMENT_INSERT_GROUP_MEMBER");
		STATEMENT_INSERT_RECENT_CHANGE           = props.getProperty("STATEMENT_INSERT_RECENT_CHANGE");
		STATEMENT_INSERT_RECENT_CHANGES          = props.getProperty("STATEMENT_INSERT_RECENT_CHANGES");
		STATEMENT_INSERT_ROLE                    = props.getProperty("STATEMENT_INSERT_ROLE");
		STATEMENT_INSERT_TOPIC                   = props.getProperty("STATEMENT_INSERT_TOPIC");
		STATEMENT_INSERT_TOPIC_VERSION           = props.getProperty("STATEMENT_INSERT_TOPIC_VERSION");
		STATEMENT_INSERT_USER                    = props.getProperty("STATEMENT_INSERT_USER");
		STATEMENT_INSERT_VIRTUAL_WIKI            = props.getProperty("STATEMENT_INSERT_VIRTUAL_WIKI");
		STATEMENT_INSERT_WATCHLIST_ENTRY         = props.getProperty("STATEMENT_INSERT_WATCHLIST_ENTRY");
		STATEMENT_INSERT_WIKI_FILE               = props.getProperty("STATEMENT_INSERT_WIKI_FILE");
		STATEMENT_INSERT_WIKI_FILE_VERSION       = props.getProperty("STATEMENT_INSERT_WIKI_FILE_VERSION");
		STATEMENT_INSERT_WIKI_USER               = props.getProperty("STATEMENT_INSERT_WIKI_USER");
		STATEMENT_SELECT_AUTHORITIES_AUTHORITY   = props.getProperty("STATEMENT_SELECT_AUTHORITIES_AUTHORITY");
		STATEMENT_SELECT_AUTHORITIES_LOGIN       = props.getProperty("STATEMENT_SELECT_AUTHORITIES_LOGIN");
		STATEMENT_SELECT_AUTHORITIES_USER        = props.getProperty("STATEMENT_SELECT_AUTHORITIES_USER");
		STATEMENT_SELECT_CATEGORIES              = props.getProperty("STATEMENT_SELECT_CATEGORIES");
		STATEMENT_SELECT_CATEGORY_TOPICS         = props.getProperty("STATEMENT_SELECT_CATEGORY_TOPICS");
		STATEMENT_SELECT_GROUP                   = props.getProperty("STATEMENT_SELECT_GROUP");
		STATEMENT_SELECT_GROUP_AUTHORITIES       = props.getProperty("STATEMENT_SELECT_GROUP_AUTHORITIES");
		STATEMENT_SELECT_GROUPS_AUTHORITIES      = props.getProperty("STATEMENT_SELECT_GROUPS_AUTHORITIES");
		STATEMENT_SELECT_GROUP_MEMBERS_SEQUENCE  = props.getProperty("STATEMENT_SELECT_GROUP_MEMBERS_SEQUENCE");
		STATEMENT_SELECT_GROUP_SEQUENCE          = props.getProperty("STATEMENT_SELECT_GROUP_SEQUENCE");
		STATEMENT_SELECT_RECENT_CHANGES          = props.getProperty("STATEMENT_SELECT_RECENT_CHANGES");
		STATEMENT_SELECT_RECENT_CHANGES_TOPIC    = props.getProperty("STATEMENT_SELECT_RECENT_CHANGES_TOPIC");
		STATEMENT_SELECT_ROLES                   = props.getProperty("STATEMENT_SELECT_ROLES");
		STATEMENT_SELECT_TOPIC_BY_TYPE           = props.getProperty("STATEMENT_SELECT_TOPIC_BY_TYPE");
		STATEMENT_SELECT_TOPIC_COUNT             = props.getProperty("STATEMENT_SELECT_TOPIC_COUNT");
		STATEMENT_SELECT_TOPIC                   = props.getProperty("STATEMENT_SELECT_TOPIC");
		STATEMENT_SELECT_TOPIC_LOWER             = props.getProperty("STATEMENT_SELECT_TOPIC_LOWER");
		STATEMENT_SELECT_TOPICS                  = props.getProperty("STATEMENT_SELECT_TOPICS");
		STATEMENT_SELECT_TOPICS_ADMIN            = props.getProperty("STATEMENT_SELECT_TOPICS_ADMIN");
		STATEMENT_SELECT_TOPIC_SEQUENCE          = props.getProperty("STATEMENT_SELECT_TOPIC_SEQUENCE");
		STATEMENT_SELECT_TOPIC_VERSION           = props.getProperty("STATEMENT_SELECT_TOPIC_VERSION");
		STATEMENT_SELECT_TOPIC_VERSION_SEQUENCE  = props.getProperty("STATEMENT_SELECT_TOPIC_VERSION_SEQUENCE");
		STATEMENT_SELECT_USERS_AUTHENTICATION    = props.getProperty("STATEMENT_SELECT_USERS_AUTHENTICATION");
		STATEMENT_SELECT_VIRTUAL_WIKIS           = props.getProperty("STATEMENT_SELECT_VIRTUAL_WIKIS");
		STATEMENT_SELECT_VIRTUAL_WIKI_SEQUENCE   = props.getProperty("STATEMENT_SELECT_VIRTUAL_WIKI_SEQUENCE");
		STATEMENT_SELECT_WATCHLIST               = props.getProperty("STATEMENT_SELECT_WATCHLIST");
		STATEMENT_SELECT_WATCHLIST_CHANGES       = props.getProperty("STATEMENT_SELECT_WATCHLIST_CHANGES");
		STATEMENT_SELECT_WIKI_FILE               = props.getProperty("STATEMENT_SELECT_WIKI_FILE");
		STATEMENT_SELECT_WIKI_FILE_COUNT         = props.getProperty("STATEMENT_SELECT_WIKI_FILE_COUNT");
		STATEMENT_SELECT_WIKI_FILE_SEQUENCE      = props.getProperty("STATEMENT_SELECT_WIKI_FILE_SEQUENCE");
		STATEMENT_SELECT_WIKI_FILE_VERSION_SEQUENCE = props.getProperty("STATEMENT_SELECT_WIKI_FILE_VERSION_SEQUENCE");
		STATEMENT_SELECT_WIKI_FILE_VERSIONS      = props.getProperty("STATEMENT_SELECT_WIKI_FILE_VERSIONS");
		STATEMENT_SELECT_WIKI_USER               = props.getProperty("STATEMENT_SELECT_WIKI_USER");
		STATEMENT_SELECT_WIKI_USER_CHANGES_ANONYMOUS = props.getProperty("STATEMENT_SELECT_WIKI_USER_CHANGES_ANONYMOUS");
		STATEMENT_SELECT_WIKI_USER_CHANGES_LOGIN = props.getProperty("STATEMENT_SELECT_WIKI_USER_CHANGES_LOGIN");
		STATEMENT_SELECT_WIKI_USER_COUNT         = props.getProperty("STATEMENT_SELECT_WIKI_USER_COUNT");
		STATEMENT_SELECT_WIKI_USER_DETAILS_PASSWORD = props.getProperty("STATEMENT_SELECT_WIKI_USER_DETAILS_PASSWORD");
		STATEMENT_SELECT_WIKI_USER_LOGIN         = props.getProperty("STATEMENT_SELECT_WIKI_USER_LOGIN");
		STATEMENT_SELECT_WIKI_USER_SEQUENCE      = props.getProperty("STATEMENT_SELECT_WIKI_USER_SEQUENCE");
		STATEMENT_SELECT_WIKI_USERS              = props.getProperty("STATEMENT_SELECT_WIKI_USERS");
		STATEMENT_UPDATE_GROUP                   = props.getProperty("STATEMENT_UPDATE_GROUP");
		STATEMENT_UPDATE_ROLE                    = props.getProperty("STATEMENT_UPDATE_ROLE");
		STATEMENT_UPDATE_TOPIC                   = props.getProperty("STATEMENT_UPDATE_TOPIC");
		STATEMENT_UPDATE_TOPIC_CURRENT_VERSION   = props.getProperty("STATEMENT_UPDATE_TOPIC_CURRENT_VERSION");
		STATEMENT_UPDATE_USER                    = props.getProperty("STATEMENT_UPDATE_USER");
		STATEMENT_UPDATE_VIRTUAL_WIKI            = props.getProperty("STATEMENT_UPDATE_VIRTUAL_WIKI");
		STATEMENT_UPDATE_WIKI_FILE               = props.getProperty("STATEMENT_UPDATE_WIKI_FILE");
		STATEMENT_UPDATE_WIKI_USER               = props.getProperty("STATEMENT_UPDATE_WIKI_USER");
	}

	/**
	 *
	 */
	public void insertCategory(Category category, int virtualWikiId, Connection conn) throws SQLException {
		// FIXME - clean this code up
		WikiResultSet rs = this.lookupTopic(virtualWikiId, category.getChildTopicName(), false, conn);
		int topicId = -1;
		while (rs.next()) {
			if (rs.getTimestamp("delete_date") == null) {
				topicId = rs.getInt(AnsiDataHandler.DATA_TOPIC_ID);
				break;
			}
		}
		if (topicId == -1) {
			throw new SQLException("Unable to find child topic " + category.getChildTopicName() + " for category " + category.getName());
		}
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_INSERT_CATEGORY);
		stmt.setInt(1, rs.getInt(AnsiDataHandler.DATA_TOPIC_ID));
		stmt.setString(2, category.getName());
		stmt.setString(3, category.getSortKey());
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public void insertGroupAuthority(int groupId, String authority, Connection conn) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_INSERT_GROUP_AUTHORITY);
		stmt.setInt(1, groupId);
		stmt.setString(2, authority);
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public void insertGroupMember(String username, int groupId, Connection conn) throws SQLException {
		int groupMemberId = this.nextGroupMemberId(conn);
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_INSERT_GROUP_MEMBER);
		stmt.setInt(1, groupMemberId);
		stmt.setString(2, username);
		stmt.setInt(3, groupId);
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public void insertRecentChange(RecentChange change, int virtualWikiId, Connection conn) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_INSERT_RECENT_CHANGE);
		stmt.setInt(1, change.getTopicVersionId());
		if (change.getPreviousTopicVersionId() == null) {
			stmt.setNull(2, Types.INTEGER);
		} else {
			stmt.setInt(2, change.getPreviousTopicVersionId());
		}
		stmt.setInt(3, change.getTopicId());
		stmt.setString(4, change.getTopicName());
		stmt.setTimestamp(5, change.getEditDate());
		stmt.setString(6, change.getEditComment());
		if (change.getAuthorId() == null) {
			stmt.setNull(7, Types.INTEGER);
		} else {
			stmt.setInt(7, change.getAuthorId());
		}
		stmt.setString(8, change.getAuthorName());
		stmt.setInt(9, change.getEditType());
		stmt.setInt(10, virtualWikiId);
		stmt.setString(11, change.getVirtualWiki());
		stmt.setInt(12, change.getCharactersChanged());
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public void insertRole(Role role, Connection conn) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_INSERT_ROLE);
		stmt.setString(1, role.getAuthority());
		stmt.setString(2, role.getDescription());
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public void insertTopic(Topic topic, int virtualWikiId, Connection conn) throws SQLException {
		if (topic.getTopicId() < 1) {
			int topicId = this.nextTopicId(conn);
			topic.setTopicId(topicId);
		}
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_INSERT_TOPIC);
		stmt.setInt(1, topic.getTopicId());
		stmt.setInt(2, virtualWikiId);
		stmt.setString(3, topic.getName());
		stmt.setInt(4, topic.getTopicType());
		stmt.setInt(5, (topic.getReadOnly() ? 1 : 0));
		if (topic.getCurrentVersionId() == null) {
			stmt.setNull(6, Types.INTEGER);
		} else {
			stmt.setInt(6, topic.getCurrentVersionId());
		}
		stmt.setTimestamp(7, topic.getDeleteDate());
		stmt.setInt(8, (topic.getAdminOnly() ? 1 : 0));
		stmt.setString(9, topic.getRedirectTo());
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public void insertTopicVersion(TopicVersion topicVersion, Connection conn) throws SQLException {
		if (topicVersion.getTopicVersionId() < 1) {
			int topicVersionId = this.nextTopicVersionId(conn);
			topicVersion.setTopicVersionId(topicVersionId);
		}
		if (topicVersion.getEditDate() == null) {
			Timestamp editDate = new Timestamp(System.currentTimeMillis());
			topicVersion.setEditDate(editDate);
		}
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_INSERT_TOPIC_VERSION);
		stmt.setInt(1, topicVersion.getTopicVersionId());
		stmt.setInt(2, topicVersion.getTopicId());
		stmt.setString(3, topicVersion.getEditComment());
		stmt.setString(4, topicVersion.getVersionContent());
		if (topicVersion.getAuthorId() == null) {
			stmt.setNull(5, Types.INTEGER);
		} else {
			stmt.setInt(5, topicVersion.getAuthorId());
		}
		stmt.setInt(6, topicVersion.getEditType());
		stmt.setString(7, topicVersion.getAuthorDisplay());
		stmt.setTimestamp(8, topicVersion.getEditDate());
		if (topicVersion.getPreviousTopicVersionId() == null) {
			stmt.setNull(9, Types.INTEGER);
		} else {
			stmt.setInt(9, topicVersion.getPreviousTopicVersionId());
		}
		stmt.setInt(10, topicVersion.getCharactersChanged());
		stmt.executeUpdate(conn);
		stmt = new WikiPreparedStatement(STATEMENT_UPDATE_TOPIC_CURRENT_VERSION);
		stmt.setInt(1, topicVersion.getTopicId());
		stmt.setInt(2, topicVersion.getTopicId());
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public void insertUserDetails(WikiUserDetails userDetails, Connection conn) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_INSERT_USER);
		stmt.setString(1, userDetails.getUsername());
		stmt.setString(2, userDetails.getPassword());
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public void insertUserAuthority(String username, String authority, Connection conn) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_INSERT_AUTHORITY);
		stmt.setString(1, username);
		stmt.setString(2, authority);
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public void insertVirtualWiki(VirtualWiki virtualWiki, Connection conn) throws SQLException {
		if (virtualWiki.getVirtualWikiId() < 1) {
			int virtualWikiId = this.nextVirtualWikiId(conn);
			virtualWiki.setVirtualWikiId(virtualWikiId);
		}
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_INSERT_VIRTUAL_WIKI);
		stmt.setInt(1, virtualWiki.getVirtualWikiId());
		stmt.setString(2, virtualWiki.getName());
		stmt.setString(3, virtualWiki.getDefaultTopicName());
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public void insertWatchlistEntry(int virtualWikiId, String topicName, int userId, Connection conn) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_INSERT_WATCHLIST_ENTRY);
		stmt.setInt(1, virtualWikiId);
		stmt.setString(2, topicName);
		stmt.setInt(3, userId);
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public void insertWikiFile(WikiFile wikiFile, int virtualWikiId, Connection conn) throws SQLException {
		if (wikiFile.getFileId() < 1) {
			int fileId = this.nextWikiFileId(conn);
			wikiFile.setFileId(fileId);
		}
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_INSERT_WIKI_FILE);
		stmt.setInt(1, wikiFile.getFileId());
		stmt.setInt(2, virtualWikiId);
		stmt.setString(3, wikiFile.getFileName());
		stmt.setString(4, wikiFile.getUrl());
		stmt.setString(5, wikiFile.getMimeType());
		stmt.setInt(6, wikiFile.getTopicId());
		stmt.setTimestamp(7, wikiFile.getDeleteDate());
		stmt.setInt(8, (wikiFile.getReadOnly() ? 1 : 0));
		stmt.setInt(9, (wikiFile.getAdminOnly() ? 1 : 0));
		stmt.setInt(10, wikiFile.getFileSize());
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public void insertWikiFileVersion(WikiFileVersion wikiFileVersion, Connection conn) throws SQLException {
		if (wikiFileVersion.getFileVersionId() < 1) {
			int fileVersionId = this.nextWikiFileVersionId(conn);
			wikiFileVersion.setFileVersionId(fileVersionId);
		}
		if (wikiFileVersion.getUploadDate() == null) {
			Timestamp uploadDate = new Timestamp(System.currentTimeMillis());
			wikiFileVersion.setUploadDate(uploadDate);
		}
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_INSERT_WIKI_FILE_VERSION);
		stmt.setInt(1, wikiFileVersion.getFileVersionId());
		stmt.setInt(2, wikiFileVersion.getFileId());
		stmt.setString(3, wikiFileVersion.getUploadComment());
		stmt.setString(4, wikiFileVersion.getUrl());
		if (wikiFileVersion.getAuthorId() == null) {
			stmt.setNull(5, Types.INTEGER);
		} else {
			stmt.setInt(5, wikiFileVersion.getAuthorId());
		}
		stmt.setString(6, wikiFileVersion.getAuthorDisplay());
		stmt.setTimestamp(7, wikiFileVersion.getUploadDate());
		stmt.setString(8, wikiFileVersion.getMimeType());
		stmt.setInt(9, wikiFileVersion.getFileSize());
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public void insertWikiGroup(WikiGroup group, Connection conn) throws SQLException {
		if (group.getGroupId() < 1) {
			int groupId = this.nextWikiGroupId(conn);
			group.setGroupId(groupId);
		}
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_INSERT_GROUP);
		stmt.setInt(1, group.getGroupId());
		stmt.setString(2, group.getName());
		stmt.setString(3, group.getDescription());
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public void insertWikiUser(WikiUser user, Connection conn) throws SQLException {
		if (user.getUserId() < 1) {
			int nextUserId = this.nextWikiUserId(conn);
			user.setUserId(nextUserId);
		}
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_INSERT_WIKI_USER);
		stmt.setInt(1, user.getUserId());
		stmt.setString(2, user.getUsername());
		stmt.setString(3, user.getDisplayName());
		stmt.setTimestamp(4, user.getCreateDate());
		stmt.setTimestamp(5, user.getLastLoginDate());
		stmt.setString(6, user.getCreateIpAddress());
		stmt.setString(7, user.getLastLoginIpAddress());
		stmt.setString(8, user.getDefaultLocale());
		stmt.setString(9, user.getEmail());
		stmt.setString(10, user.getEditor());
		stmt.setString(11, user.getSignature());
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public WikiResultSet lookupCategoryTopics(int virtualWikiId, String categoryName) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_CATEGORY_TOPICS);
		// category name must be lowercase since search is case-insensitive
		categoryName = categoryName.toLowerCase();
		stmt.setInt(1, virtualWikiId);
		stmt.setString(2, categoryName);
		return stmt.executeQuery();
	}

	/**
	 *
	 */
	public WikiResultSet lookupTopic(int virtualWikiId, String topicName, boolean caseSensitive, Connection conn) throws SQLException {
		WikiPreparedStatement stmt = null;
		if (caseSensitive) {
			stmt = new WikiPreparedStatement(STATEMENT_SELECT_TOPIC);
		} else {
			topicName = topicName.toLowerCase();
			stmt = new WikiPreparedStatement(STATEMENT_SELECT_TOPIC_LOWER);
		}
		stmt.setInt(1, virtualWikiId);
		stmt.setString(2, topicName);
		return stmt.executeQuery(conn);
	}

	/**
	 *
	 */
	public WikiResultSet lookupTopicByType(int virtualWikiId, int topicType, Pagination pagination) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_TOPIC_BY_TYPE);
		stmt.setInt(1, virtualWikiId);
		stmt.setInt(2, topicType);
		stmt.setInt(3, pagination.getNumResults());
		stmt.setInt(4, pagination.getOffset());
		return stmt.executeQuery();
	}

	/**
	 * Return a count of all topics, including redirects, comments pages and templates,
	 * currently available on the Wiki.  This method excludes deleted topics.
	 *
	 * @param virtualWikiId The virtual wiki id for the virtual wiki of the topics
	 *  being retrieved.
	 */
	public WikiResultSet lookupTopicCount(int virtualWikiId) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_TOPIC_COUNT);
		stmt.setInt(1, virtualWikiId);
		return stmt.executeQuery();
	}

	/**
	 *
	 */
	public WikiResultSet lookupTopicVersion(int topicVersionId, Connection conn) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_TOPIC_VERSION);
		stmt.setInt(1, topicVersionId);
		return stmt.executeQuery(conn);
	}

	/**
	 *
	 */
	public WikiResultSet lookupWikiFile(int virtualWikiId, int topicId) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_WIKI_FILE);
		stmt.setInt(1, virtualWikiId);
		stmt.setInt(2, topicId);
		return stmt.executeQuery();
	}

	/**
	 * Return a count of all wiki files currently available on the Wiki.  This
	 * method excludes deleted files.
	 *
	 * @param virtualWikiId The virtual wiki id for the virtual wiki of the files
	 *  being retrieved.
	 */
	public WikiResultSet lookupWikiFileCount(int virtualWikiId) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_WIKI_FILE_COUNT);
		stmt.setInt(1, virtualWikiId);
		return stmt.executeQuery();
	}

	/**
	 *
	 */
	public WikiResultSet lookupWikiGroup(String groupName) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_GROUP);
		stmt.setString(1, groupName);
		return stmt.executeQuery();
	}

	/**
	 *
	 */
	public WikiResultSet lookupWikiUser(int userId, Connection conn) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_WIKI_USER);
		stmt.setInt(1, userId);
		return stmt.executeQuery(conn);
	}

	/**
	 *
	 */
	public WikiResultSet lookupWikiUser(String username, Connection conn) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_WIKI_USER_LOGIN);
		stmt.setString(1, username);
		return stmt.executeQuery(conn);
	}

	/**
	 * Return a count of all wiki users.
	 */
	public WikiResultSet lookupWikiUserCount() throws SQLException {
		return DatabaseConnection.executeQuery(STATEMENT_SELECT_WIKI_USER_COUNT);
	}

	/**
	 *
	 */
	public WikiResultSet lookupWikiUserEncryptedPassword(String username) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_WIKI_USER_DETAILS_PASSWORD);
		stmt.setString(1, username);
		return stmt.executeQuery();
	}

	/**
	 *
	 */
	public WikiResultSet lookupWikiUsers(Pagination pagination) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_WIKI_USERS);
		stmt.setInt(1, pagination.getNumResults());
		stmt.setInt(2, pagination.getOffset());
		return stmt.executeQuery();
	}

	/**
	 * Retrieve the next available group member id from the group members table.
	 *
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @return The next available group member id from the group members table.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	private int nextGroupMemberId(Connection conn) throws SQLException {
		WikiResultSet rs = DatabaseConnection.executeQuery(STATEMENT_SELECT_GROUP_MEMBERS_SEQUENCE, conn);
		int nextId = 0;
		if (rs.size() > 0) {
			nextId = rs.getInt("id");
		}
		// note - this returns the last id in the system, so add one
		return nextId + 1;
	}

	/**
	 * Retrieve the next available topic id from the topic table.
	 *
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @return The next available topic id from the topic table.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	private int nextTopicId(Connection conn) throws SQLException {
		WikiResultSet rs = DatabaseConnection.executeQuery(STATEMENT_SELECT_TOPIC_SEQUENCE, conn);
		int nextId = 0;
		if (rs.size() > 0) {
			nextId = rs.getInt(AnsiDataHandler.DATA_TOPIC_ID);
		}
		// note - this returns the last id in the system, so add one
		return nextId + 1;
	}

	/**
	 * Retrieve the next available topic version id from the topic version table.
	 *
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @return The next available topic version id from the topic version table.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	private int nextTopicVersionId(Connection conn) throws SQLException {
		WikiResultSet rs = DatabaseConnection.executeQuery(STATEMENT_SELECT_TOPIC_VERSION_SEQUENCE, conn);
		int nextId = 0;
		if (rs.size() > 0) {
			nextId = rs.getInt("topic_version_id");
		}
		// note - this returns the last id in the system, so add one
		return nextId + 1;
	}

	/**
	 * Retrieve the next available virtual wiki id from the virtual wiki table.
	 *
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @return The next available virtual wiki id from the virtual wiki table.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	private int nextVirtualWikiId(Connection conn) throws SQLException {
		WikiResultSet rs = DatabaseConnection.executeQuery(STATEMENT_SELECT_VIRTUAL_WIKI_SEQUENCE, conn);
		int nextId = 0;
		if (rs.size() > 0) {
			nextId = rs.getInt("virtual_wiki_id");
		}
		// note - this returns the last id in the system, so add one
		return nextId + 1;
	}

	/**
	 * Retrieve the next available wiki file id from the wiki file table.
	 *
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @return The next available wiki file id from the wiki file table.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	private int nextWikiFileId(Connection conn) throws SQLException {
		WikiResultSet rs = DatabaseConnection.executeQuery(STATEMENT_SELECT_WIKI_FILE_SEQUENCE, conn);
		int nextId = 0;
		if (rs.size() > 0) {
			nextId = rs.getInt("file_id");
		}
		// note - this returns the last id in the system, so add one
		return nextId + 1;
	}

	/**
	 * Retrieve the next available wiki file version id from the wiki file
	 * version table.
	 *
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @return The next available wiki file version id from the wiki file
	 *  version table.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	private int nextWikiFileVersionId(Connection conn) throws SQLException {
		WikiResultSet rs = DatabaseConnection.executeQuery(STATEMENT_SELECT_WIKI_FILE_VERSION_SEQUENCE, conn);
		int nextId = 0;
		if (rs.size() > 0) {
			nextId = rs.getInt("file_version_id");
		}
		// note - this returns the last id in the system, so add one
		return nextId + 1;
	}

	/**
	 * Retrieve the next available wiki group id from the wiki group table.
	 *
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @return The next available wiki group id from the wiki group table.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	private int nextWikiGroupId(Connection conn) throws SQLException {
		WikiResultSet rs = DatabaseConnection.executeQuery(STATEMENT_SELECT_GROUP_SEQUENCE, conn);
		int nextId = 0;
		if (rs.size() > 0) {
			nextId = rs.getInt(AnsiDataHandler.DATA_GROUP_ID);
		}
		// note - this returns the last id in the system, so add one
		return nextId + 1;
	}

	/**
	 * Retrieve the next available wiki user id from the wiki user table.
	 *
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @return The next available wiki user id from the wiki user table.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	private int nextWikiUserId(Connection conn) throws SQLException {
		WikiResultSet rs = DatabaseConnection.executeQuery(STATEMENT_SELECT_WIKI_USER_SEQUENCE, conn);
		int nextId = 0;
		if (rs.size() > 0) {
			nextId = rs.getInt(AnsiDataHandler.DATA_WIKI_USER_ID);
		}
		// note - this returns the last id in the system, so add one
		return nextId + 1;
	}

	/**
	 *
	 */
	public void reloadRecentChanges(Connection conn) throws SQLException {
		DatabaseConnection.executeUpdate(STATEMENT_DELETE_RECENT_CHANGES, conn);
		DatabaseConnection.executeUpdate(STATEMENT_INSERT_RECENT_CHANGES, conn);
	}

	/**
	 *
	 */
	public void updateRole(Role role, Connection conn) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_UPDATE_ROLE);
		stmt.setString(1, role.getDescription());
		stmt.setString(2, role.getAuthority());
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public void updateTopic(Topic topic, int virtualWikiId, Connection conn) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_UPDATE_TOPIC);
		stmt.setInt(1, virtualWikiId);
		stmt.setString(2, topic.getName());
		stmt.setInt(3, topic.getTopicType());
		stmt.setInt(4, (topic.getReadOnly() ? 1 : 0));
		if (topic.getCurrentVersionId() == null) {
			stmt.setNull(5, Types.INTEGER);
		} else {
			stmt.setInt(5, topic.getCurrentVersionId());
		}
		stmt.setTimestamp(6, topic.getDeleteDate());
		stmt.setInt(7, (topic.getAdminOnly() ? 1 : 0));
		stmt.setString(8, topic.getRedirectTo());
		stmt.setInt(9, topic.getTopicId());
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public void updateUserDetails(WikiUserDetails userDetails, Connection conn) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_UPDATE_USER);
		stmt.setString(1, userDetails.getPassword());
		stmt.setInt(2, 1);
		stmt.setString(3, userDetails.getUsername());
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public void updateVirtualWiki(VirtualWiki virtualWiki, Connection conn) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_UPDATE_VIRTUAL_WIKI);
		stmt.setString(1, virtualWiki.getDefaultTopicName());
		stmt.setInt(2, virtualWiki.getVirtualWikiId());
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public void updateWikiFile(WikiFile wikiFile, int virtualWikiId, Connection conn) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_UPDATE_WIKI_FILE);
		stmt.setInt(1, virtualWikiId);
		stmt.setString(2, wikiFile.getFileName());
		stmt.setString(3, wikiFile.getUrl());
		stmt.setString(4, wikiFile.getMimeType());
		stmt.setInt(5, wikiFile.getTopicId());
		stmt.setTimestamp(6, wikiFile.getDeleteDate());
		stmt.setInt(7, (wikiFile.getReadOnly() ? 1 : 0));
		stmt.setInt(8, (wikiFile.getAdminOnly() ? 1 : 0));
		stmt.setInt(9, wikiFile.getFileSize());
		stmt.setInt(10, wikiFile.getFileId());
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public void updateWikiGroup(WikiGroup group, Connection conn) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_UPDATE_GROUP);
		stmt.setString(1, group.getName());
		stmt.setString(2, group.getDescription());
		stmt.setInt(3, group.getGroupId());
		stmt.executeUpdate(conn);
	}

	/**
	 *
	 */
	public void updateWikiUser(WikiUser user, Connection conn) throws SQLException {
		WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_UPDATE_WIKI_USER);
		stmt.setString(1, user.getUsername());
		stmt.setString(2, user.getDisplayName());
		stmt.setTimestamp(3, user.getLastLoginDate());
		stmt.setString(4, user.getLastLoginIpAddress());
		stmt.setString(5, user.getDefaultLocale());
		stmt.setString(6, user.getEmail());
		stmt.setString(7, user.getEditor());
		stmt.setString(8, user.getSignature());
		stmt.setInt(9, user.getUserId());
		stmt.executeUpdate(conn);
	}
}
