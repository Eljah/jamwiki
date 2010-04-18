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

import org.jamwiki.utils.DataCompression;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import org.jamwiki.model.ParsedTopic;
import org.jamwiki.model.WikiImageResource;
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
    protected static String STATEMENT_INSERT_TOPIC_AUTO_SEQ = null;
    protected static String STATEMENT_INSERT_TOPIC_VERSION_AUTO_SEQ = null;
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
    protected static String STATEMENT_SELECT_TOPIC_BY_ID = null;
    protected static String STATEMENT_SELECT_TOPIC_METADATA_BY_ID = null;
    protected static String STATEMENT_SELECT_TOPIC_BY_TYPE = null;
    protected static String STATEMENT_SELECT_TOPIC_COUNT = null;
    protected static String STATEMENT_SELECT_TOPIC = null;
    protected static String STATEMENT_SELECT_TOPIC_LOWER = null;
    protected static String STATEMENT_SELECT_TOPICS = null;
    protected static String STATEMENT_SELECT_TOPIC_IDS = null;
    protected static String STATEMENT_SELECT_TOPICS_ADMIN = null;
    protected static String STATEMENT_SELECT_TOPIC_VERSION = null;
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
    protected static String STATEMENT_UPDATE_TOPIC_VERSION = null;
    protected static String STATEMENT_UPDATE_TOPIC_CURRENT_VERSION = null;
    protected static String STATEMENT_UPDATE_TOPIC_CURRENT_VERSIONS = null;
    protected static String STATEMENT_UPDATE_TOPIC_CURRENT_VERSION_AUTO_SEQ = null;
    protected static String STATEMENT_UPDATE_TOPIC_CURRENT_VERSIONS_AUTO_SEQ = null;
    protected static String STATEMENT_UPDATE_USER = null;
    protected static String STATEMENT_UPDATE_VIRTUAL_WIKI = null;
    protected static String STATEMENT_UPDATE_WIKI_FILE = null;
    protected static String STATEMENT_UPDATE_WIKI_USER = null;

    protected static String STATEMENT_CREATE_TOPIC_CACHE_TABLE = null;
    protected static String STATEMENT_DELETE_TOPIC_VERSION_CACHE = null;
    protected static String STATEMENT_INSERT_TOPIC_VERSION_CACHE = null;
    protected static String STATEMENT_SELECT_TOPIC_VERSION_CACHE_BY_NAME = null;
    protected static String STATEMENT_SELECT_TOPIC_VERSION_CACHE = null;

    protected static String STATEMENT_SELECT_WP_IMAGE = null;
    protected static String STATEMENT_SELECT_WP_IMAGE_BY_PARENT = null;
    protected static String STATEMENT_INSERT_WP_IMAGE_AUTO_SEQ = null;

    private static Properties props = null;

    /**
     *
     */
    protected AnsiQueryHandler() {
        props = Environment.loadProperties(SQL_PROPERTY_FILE_NAME);
        this.init(props);
    }

    /**
     *
     * @param username
     */
    public boolean authenticateUser(String username, String encryptedPassword, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_USERS_AUTHENTICATION);
        stmt.setString(1, username);
        stmt.setString(2, encryptedPassword);
        boolean rv = (stmt.executeQuery().next());
        DatabaseConnection.closeStatement(stmt);
        return rv;
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
        /*
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
        */
    }

    /**
     *
     */
    public void deleteGroupAuthorities(int groupId, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_DELETE_GROUP_AUTHORITIES);
        stmt.setInt(1, groupId);
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    /**
     *
     */
    public void deleteRecentChanges(int topicId, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_DELETE_RECENT_CHANGES_TOPIC);
        stmt.setInt(1, topicId);
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    /**
     *
     * @param childTopicId
     */
    public void deleteTopicCategories(int childTopicId, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_DELETE_TOPIC_CATEGORIES);
        stmt.setInt(1, childTopicId);
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    /**
     *
     */
    public void deleteUserAuthorities(String username, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_DELETE_AUTHORITIES);
        stmt.setString(1, username);
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    /**
     *
     */
    public void deleteWatchlistEntry(int virtualWikiId, String topicName, int userId, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_DELETE_WATCHLIST_ENTRY);
        stmt.setInt(1, virtualWikiId);
        stmt.setString(2, topicName);
        stmt.setInt(3, userId);
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    /**
     *
     */
    public void dropTables(Connection conn) {
        // note that this method is called during creation failures, so be careful to
        // catch errors that might result from a partial failure during install.  also
        // note that the coding style violation here is intentional since it makes the
        // actual work of the method more obvious.
        /*
        try {
            DatabaseConnection.executeUpdate(STATEMENT_DROP_WATCHLIST_TABLE, conn);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        try {
            DatabaseConnection.executeUpdate(STATEMENT_DROP_RECENT_CHANGE_TABLE, conn);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        try {
            DatabaseConnection.executeUpdate(STATEMENT_DROP_GROUP_AUTHORITIES_TABLE, conn);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        try {
            DatabaseConnection.executeUpdate(STATEMENT_DROP_AUTHORITIES_TABLE, conn);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        try {
            DatabaseConnection.executeUpdate(STATEMENT_DROP_ROLE_TABLE, conn);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        try {
            DatabaseConnection.executeUpdate(STATEMENT_DROP_GROUP_MEMBERS_TABLE, conn);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        try {
            DatabaseConnection.executeUpdate(STATEMENT_DROP_GROUP_TABLE, conn);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        try {
            DatabaseConnection.executeUpdate(STATEMENT_DROP_CATEGORY_TABLE, conn);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        try {
            DatabaseConnection.executeUpdate(STATEMENT_DROP_WIKI_FILE_VERSION_TABLE, conn);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        try {
            DatabaseConnection.executeUpdate(STATEMENT_DROP_WIKI_FILE_TABLE, conn);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        try {
            DatabaseConnection.executeUpdate(STATEMENT_DROP_TOPIC_CURRENT_VERSION_CONSTRAINT, conn);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        try {
            DatabaseConnection.executeUpdate(STATEMENT_DROP_TOPIC_VERSION_TABLE, conn);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        try {
            DatabaseConnection.executeUpdate(STATEMENT_DROP_TOPIC_TABLE, conn);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        try {
            DatabaseConnection.executeUpdate(STATEMENT_DROP_WIKI_USER_LOGIN_INDEX, conn);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        try {
            DatabaseConnection.executeUpdate(STATEMENT_DROP_WIKI_USER_TABLE, conn);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        try {
            DatabaseConnection.executeUpdate(STATEMENT_DROP_USERS_TABLE, conn);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        try {
            DatabaseConnection.executeUpdate(STATEMENT_DROP_VIRTUAL_WIKI_TABLE, conn);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        */
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
    public ResultSet getAllTopicNames(int virtualWikiId, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_TOPICS);
        stmt.setInt(1, virtualWikiId);
        return stmt.executeQuery();
    }

    /**
     *
     * @param virtualWikiId
     * @return
     * @throws SQLException
     */
    public ResultSet getAllTopicIdentifiers(int virtualWikiId, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_TOPIC_IDS);
        stmt.setInt(1, virtualWikiId);
        return stmt.executeQuery();
    }

    /**
     *
     */
    public ResultSet getAllWikiFileVersions(WikiFile wikiFile, boolean descending, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_WIKI_FILE_VERSIONS);
        // FIXME - sort order ignored
        stmt.setInt(1, wikiFile.getFileId());
        return stmt.executeQuery();
    }

    /**
     *
     */
    public ResultSet getCategories(int virtualWikiId, Pagination pagination, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_CATEGORIES);
        stmt.setInt(1, virtualWikiId);
        stmt.setInt(2, pagination.getNumResults());
        stmt.setInt(3, pagination.getOffset());
        return stmt.executeQuery();
    }

    /**
     *
     */
    public ResultSet getRecentChanges(String virtualWiki, Pagination pagination, boolean descending, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_RECENT_CHANGES);
        stmt.setString(1, virtualWiki);
        stmt.setInt(2, pagination.getNumResults());
        stmt.setInt(3, pagination.getOffset());
        // FIXME - sort order ignored
        return stmt.executeQuery();
    }

    /**
     *
     */
    public ResultSet getRecentChanges(int topicId, Pagination pagination, boolean descending, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_RECENT_CHANGES_TOPIC);
        stmt.setInt(1, topicId);
        stmt.setInt(2, pagination.getNumResults());
        stmt.setInt(3, pagination.getOffset());
        // FIXME - sort order ignored
        return stmt.executeQuery();
    }

    /**
     *
     */
    public ResultSet getRoleMapByLogin(String loginFragment, Connection conn) throws SQLException {
        if (StringUtils.isBlank(loginFragment)) {
            return null; //new ResultSet();
        }
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_AUTHORITIES_LOGIN);
        loginFragment = '%' + loginFragment.toLowerCase() + '%';
        stmt.setString(1, loginFragment);
        return stmt.executeQuery();
    }

    /**
     *
     */
    public ResultSet getRoleMapByRole(String authority, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_AUTHORITIES_AUTHORITY);
        stmt.setString(1, authority);
        stmt.setString(2, authority);
        return stmt.executeQuery();
    }

    /**
     *
     */
    public ResultSet getRoleMapGroup(String groupName, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_GROUP_AUTHORITIES);
        stmt.setString(1, groupName);
        return stmt.executeQuery();
    }

    /**
     *
     */
    public ResultSet getRoleMapGroups(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_GROUPS_AUTHORITIES);
        return stmt.executeQuery();
    }

    /**
     *
     */
    public ResultSet getRoleMapUser(String login, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_AUTHORITIES_USER);
        stmt.setString(1, login);
        return stmt.executeQuery();
    }

    /**
     *
     */
    public ResultSet getRoles(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_ROLES);
        return stmt.executeQuery();
    }

    /**
     *
     */
    public ResultSet getTopicsAdmin(int virtualWikiId, Pagination pagination, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_TOPICS_ADMIN);
        stmt.setInt(1, virtualWikiId);
        stmt.setInt(2, pagination.getNumResults());
        stmt.setInt(3, pagination.getOffset());
        return stmt.executeQuery();
    }

    /**
     *
     */
    public ResultSet getUserContributionsByLogin(String virtualWiki, String login, Pagination pagination, boolean descending, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_WIKI_USER_CHANGES_LOGIN);
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
    public ResultSet getUserContributionsByUserDisplay(String virtualWiki, String userDisplay, Pagination pagination, boolean descending, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_WIKI_USER_CHANGES_ANONYMOUS);
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
    public ResultSet getVirtualWikis(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_VIRTUAL_WIKIS);
        return stmt.executeQuery();
    }

    /**
     *
     */
    public ResultSet getWatchlist(int virtualWikiId, int userId, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_WATCHLIST);
        stmt.setInt(1, virtualWikiId);
        stmt.setInt(2, userId);
        return stmt.executeQuery();
    }

    /**
     *
     */
    public ResultSet getWatchlist(int virtualWikiId, int userId, Pagination pagination, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_WATCHLIST_CHANGES);
        stmt.setInt(1, virtualWikiId);
        stmt.setInt(2, userId);
        stmt.setInt(3, pagination.getNumResults());
        stmt.setInt(4, pagination.getOffset());
        return stmt.executeQuery();
    }

    /**
     *
     * @param props
     */
    protected void init(Properties props) {
        STATEMENT_CONNECTION_VALIDATION_QUERY = props.getProperty("STATEMENT_CONNECTION_VALIDATION_QUERY");
        STATEMENT_CREATE_GROUP_TABLE = props.getProperty("STATEMENT_CREATE_GROUP_TABLE");
        STATEMENT_CREATE_ROLE_TABLE = props.getProperty("STATEMENT_CREATE_ROLE_TABLE");
        STATEMENT_CREATE_VIRTUAL_WIKI_TABLE = props.getProperty("STATEMENT_CREATE_VIRTUAL_WIKI_TABLE");
        STATEMENT_CREATE_WIKI_USER_TABLE = props.getProperty("STATEMENT_CREATE_WIKI_USER_TABLE");
        STATEMENT_CREATE_WIKI_USER_LOGIN_INDEX = props.getProperty("STATEMENT_CREATE_WIKI_USER_LOGIN_INDEX");
        STATEMENT_CREATE_TOPIC_CURRENT_VERSION_CONSTRAINT = props.getProperty("STATEMENT_CREATE_TOPIC_CURRENT_VERSION_CONSTRAINT");
        STATEMENT_CREATE_TOPIC_TABLE = props.getProperty("STATEMENT_CREATE_TOPIC_TABLE");
        STATEMENT_CREATE_TOPIC_VERSION_TABLE = props.getProperty("STATEMENT_CREATE_TOPIC_VERSION_TABLE");
        STATEMENT_CREATE_USERS_TABLE = props.getProperty("STATEMENT_CREATE_USERS_TABLE");
        STATEMENT_CREATE_WIKI_FILE_TABLE = props.getProperty("STATEMENT_CREATE_WIKI_FILE_TABLE");
        STATEMENT_CREATE_WIKI_FILE_VERSION_TABLE = props.getProperty("STATEMENT_CREATE_WIKI_FILE_VERSION_TABLE");
        STATEMENT_CREATE_AUTHORITIES_TABLE = props.getProperty("STATEMENT_CREATE_AUTHORITIES_TABLE");
        STATEMENT_CREATE_CATEGORY_TABLE = props.getProperty("STATEMENT_CREATE_CATEGORY_TABLE");
        STATEMENT_CREATE_GROUP_AUTHORITIES_TABLE = props.getProperty("STATEMENT_CREATE_GROUP_AUTHORITIES_TABLE");
        STATEMENT_CREATE_GROUP_MEMBERS_TABLE = props.getProperty("STATEMENT_CREATE_GROUP_MEMBERS_TABLE");
        STATEMENT_CREATE_RECENT_CHANGE_TABLE = props.getProperty("STATEMENT_CREATE_RECENT_CHANGE_TABLE");
        STATEMENT_CREATE_WATCHLIST_TABLE = props.getProperty("STATEMENT_CREATE_WATCHLIST_TABLE");
        STATEMENT_DELETE_AUTHORITIES = props.getProperty("STATEMENT_DELETE_AUTHORITIES");
        STATEMENT_DELETE_GROUP_AUTHORITIES = props.getProperty("STATEMENT_DELETE_GROUP_AUTHORITIES");
        STATEMENT_DELETE_RECENT_CHANGES = props.getProperty("STATEMENT_DELETE_RECENT_CHANGES");
        STATEMENT_DELETE_RECENT_CHANGES_TOPIC = props.getProperty("STATEMENT_DELETE_RECENT_CHANGES_TOPIC");
        STATEMENT_DELETE_TOPIC_CATEGORIES = props.getProperty("STATEMENT_DELETE_TOPIC_CATEGORIES");
        STATEMENT_DELETE_WATCHLIST_ENTRY = props.getProperty("STATEMENT_DELETE_WATCHLIST_ENTRY");
        STATEMENT_DROP_AUTHORITIES_TABLE = props.getProperty("STATEMENT_DROP_AUTHORITIES_TABLE");
        STATEMENT_DROP_CATEGORY_TABLE = props.getProperty("STATEMENT_DROP_CATEGORY_TABLE");
        STATEMENT_DROP_GROUP_AUTHORITIES_TABLE = props.getProperty("STATEMENT_DROP_GROUP_AUTHORITIES_TABLE");
        STATEMENT_DROP_GROUP_MEMBERS_TABLE = props.getProperty("STATEMENT_DROP_GROUP_MEMBERS_TABLE");
        STATEMENT_DROP_GROUP_TABLE = props.getProperty("STATEMENT_DROP_GROUP_TABLE");
        STATEMENT_DROP_RECENT_CHANGE_TABLE = props.getProperty("STATEMENT_DROP_RECENT_CHANGE_TABLE");
        STATEMENT_DROP_ROLE_TABLE = props.getProperty("STATEMENT_DROP_ROLE_TABLE");
        STATEMENT_DROP_TOPIC_CURRENT_VERSION_CONSTRAINT = props.getProperty("STATEMENT_DROP_TOPIC_CURRENT_VERSION_CONSTRAINT");
        STATEMENT_DROP_TOPIC_TABLE = props.getProperty("STATEMENT_DROP_TOPIC_TABLE");
        STATEMENT_DROP_TOPIC_VERSION_TABLE = props.getProperty("STATEMENT_DROP_TOPIC_VERSION_TABLE");
        STATEMENT_DROP_USERS_TABLE = props.getProperty("STATEMENT_DROP_USERS_TABLE");
        STATEMENT_DROP_VIRTUAL_WIKI_TABLE = props.getProperty("STATEMENT_DROP_VIRTUAL_WIKI_TABLE");
        STATEMENT_DROP_WATCHLIST_TABLE = props.getProperty("STATEMENT_DROP_WATCHLIST_TABLE");
        STATEMENT_DROP_WIKI_USER_LOGIN_INDEX = props.getProperty("STATEMENT_DROP_WIKI_USER_LOGIN_INDEX");
        STATEMENT_DROP_WIKI_USER_TABLE = props.getProperty("STATEMENT_DROP_WIKI_USER_TABLE");
        STATEMENT_DROP_WIKI_FILE_TABLE = props.getProperty("STATEMENT_DROP_WIKI_FILE_TABLE");
        STATEMENT_DROP_WIKI_FILE_VERSION_TABLE = props.getProperty("STATEMENT_DROP_WIKI_FILE_VERSION_TABLE");
        STATEMENT_INSERT_AUTHORITY = props.getProperty("STATEMENT_INSERT_AUTHORITY");
        STATEMENT_INSERT_CATEGORY = props.getProperty("STATEMENT_INSERT_CATEGORY");
        STATEMENT_INSERT_GROUP = props.getProperty("STATEMENT_INSERT_GROUP");
        STATEMENT_INSERT_GROUP_AUTHORITY = props.getProperty("STATEMENT_INSERT_GROUP_AUTHORITY");
        STATEMENT_INSERT_GROUP_MEMBER = props.getProperty("STATEMENT_INSERT_GROUP_MEMBER");
        STATEMENT_INSERT_RECENT_CHANGE = props.getProperty("STATEMENT_INSERT_RECENT_CHANGE");
        STATEMENT_INSERT_RECENT_CHANGES = props.getProperty("STATEMENT_INSERT_RECENT_CHANGES");
        STATEMENT_INSERT_ROLE = props.getProperty("STATEMENT_INSERT_ROLE");
        STATEMENT_INSERT_TOPIC = props.getProperty("STATEMENT_INSERT_TOPIC");
        STATEMENT_INSERT_TOPIC_VERSION = props.getProperty("STATEMENT_INSERT_TOPIC_VERSION");

        STATEMENT_INSERT_TOPIC_AUTO_SEQ = props.getProperty("STATEMENT_INSERT_TOPIC_AUTO_SEQ");
        STATEMENT_INSERT_TOPIC_VERSION_AUTO_SEQ = props.getProperty("STATEMENT_INSERT_TOPIC_VERSION_AUTO_SEQ");

        STATEMENT_INSERT_USER = props.getProperty("STATEMENT_INSERT_USER");
        STATEMENT_INSERT_VIRTUAL_WIKI = props.getProperty("STATEMENT_INSERT_VIRTUAL_WIKI");
        STATEMENT_INSERT_WATCHLIST_ENTRY = props.getProperty("STATEMENT_INSERT_WATCHLIST_ENTRY");
        STATEMENT_INSERT_WIKI_FILE = props.getProperty("STATEMENT_INSERT_WIKI_FILE");
        STATEMENT_INSERT_WIKI_FILE_VERSION = props.getProperty("STATEMENT_INSERT_WIKI_FILE_VERSION");
        STATEMENT_INSERT_WIKI_USER = props.getProperty("STATEMENT_INSERT_WIKI_USER");
        STATEMENT_SELECT_AUTHORITIES_AUTHORITY = props.getProperty("STATEMENT_SELECT_AUTHORITIES_AUTHORITY");
        STATEMENT_SELECT_AUTHORITIES_LOGIN = props.getProperty("STATEMENT_SELECT_AUTHORITIES_LOGIN");
        STATEMENT_SELECT_AUTHORITIES_USER = props.getProperty("STATEMENT_SELECT_AUTHORITIES_USER");
        STATEMENT_SELECT_CATEGORIES = props.getProperty("STATEMENT_SELECT_CATEGORIES");
        STATEMENT_SELECT_CATEGORY_TOPICS = props.getProperty("STATEMENT_SELECT_CATEGORY_TOPICS");
        STATEMENT_SELECT_GROUP = props.getProperty("STATEMENT_SELECT_GROUP");
        STATEMENT_SELECT_GROUP_AUTHORITIES = props.getProperty("STATEMENT_SELECT_GROUP_AUTHORITIES");
        STATEMENT_SELECT_GROUPS_AUTHORITIES = props.getProperty("STATEMENT_SELECT_GROUPS_AUTHORITIES");
        STATEMENT_SELECT_GROUP_MEMBERS_SEQUENCE = props.getProperty("STATEMENT_SELECT_GROUP_MEMBERS_SEQUENCE");
        STATEMENT_SELECT_GROUP_SEQUENCE = props.getProperty("STATEMENT_SELECT_GROUP_SEQUENCE");
        STATEMENT_SELECT_RECENT_CHANGES = props.getProperty("STATEMENT_SELECT_RECENT_CHANGES");
        STATEMENT_SELECT_RECENT_CHANGES_TOPIC = props.getProperty("STATEMENT_SELECT_RECENT_CHANGES_TOPIC");
        STATEMENT_SELECT_ROLES = props.getProperty("STATEMENT_SELECT_ROLES");
        STATEMENT_SELECT_TOPIC_BY_TYPE = props.getProperty("STATEMENT_SELECT_TOPIC_BY_TYPE");
        STATEMENT_SELECT_TOPIC_COUNT = props.getProperty("STATEMENT_SELECT_TOPIC_COUNT");
        STATEMENT_SELECT_TOPIC = props.getProperty("STATEMENT_SELECT_TOPIC");
        STATEMENT_SELECT_TOPIC_BY_ID = props.getProperty("STATEMENT_SELECT_TOPIC_BY_ID");
        STATEMENT_SELECT_TOPIC_METADATA_BY_ID = props.getProperty("STATEMENT_SELECT_TOPIC_METADATA_BY_ID");
        STATEMENT_SELECT_TOPIC_LOWER = props.getProperty("STATEMENT_SELECT_TOPIC_LOWER");
        STATEMENT_SELECT_TOPICS = props.getProperty("STATEMENT_SELECT_TOPICS");
        STATEMENT_SELECT_TOPIC_IDS = props.getProperty("STATEMENT_SELECT_TOPIC_IDS");
        STATEMENT_SELECT_TOPICS_ADMIN = props.getProperty("STATEMENT_SELECT_TOPICS_ADMIN");
        STATEMENT_SELECT_TOPIC_VERSION = props.getProperty("STATEMENT_SELECT_TOPIC_VERSION");
        STATEMENT_SELECT_USERS_AUTHENTICATION = props.getProperty("STATEMENT_SELECT_USERS_AUTHENTICATION");
        STATEMENT_SELECT_VIRTUAL_WIKIS = props.getProperty("STATEMENT_SELECT_VIRTUAL_WIKIS");
        STATEMENT_SELECT_VIRTUAL_WIKI_SEQUENCE = props.getProperty("STATEMENT_SELECT_VIRTUAL_WIKI_SEQUENCE");
        STATEMENT_SELECT_WATCHLIST = props.getProperty("STATEMENT_SELECT_WATCHLIST");
        STATEMENT_SELECT_WATCHLIST_CHANGES = props.getProperty("STATEMENT_SELECT_WATCHLIST_CHANGES");
        STATEMENT_SELECT_WIKI_FILE = props.getProperty("STATEMENT_SELECT_WIKI_FILE");
        STATEMENT_SELECT_WIKI_FILE_COUNT = props.getProperty("STATEMENT_SELECT_WIKI_FILE_COUNT");
        STATEMENT_SELECT_WIKI_FILE_SEQUENCE = props.getProperty("STATEMENT_SELECT_WIKI_FILE_SEQUENCE");
        STATEMENT_SELECT_WIKI_FILE_VERSION_SEQUENCE = props.getProperty("STATEMENT_SELECT_WIKI_FILE_VERSION_SEQUENCE");
        STATEMENT_SELECT_WIKI_FILE_VERSIONS = props.getProperty("STATEMENT_SELECT_WIKI_FILE_VERSIONS");
        STATEMENT_SELECT_WIKI_USER = props.getProperty("STATEMENT_SELECT_WIKI_USER");
        STATEMENT_SELECT_WIKI_USER_CHANGES_ANONYMOUS = props.getProperty("STATEMENT_SELECT_WIKI_USER_CHANGES_ANONYMOUS");
        STATEMENT_SELECT_WIKI_USER_CHANGES_LOGIN = props.getProperty("STATEMENT_SELECT_WIKI_USER_CHANGES_LOGIN");
        STATEMENT_SELECT_WIKI_USER_COUNT = props.getProperty("STATEMENT_SELECT_WIKI_USER_COUNT");
        STATEMENT_SELECT_WIKI_USER_DETAILS_PASSWORD = props.getProperty("STATEMENT_SELECT_WIKI_USER_DETAILS_PASSWORD");
        STATEMENT_SELECT_WIKI_USER_LOGIN = props.getProperty("STATEMENT_SELECT_WIKI_USER_LOGIN");
        STATEMENT_SELECT_WIKI_USER_SEQUENCE = props.getProperty("STATEMENT_SELECT_WIKI_USER_SEQUENCE");
        STATEMENT_SELECT_WIKI_USERS = props.getProperty("STATEMENT_SELECT_WIKI_USERS");
        STATEMENT_UPDATE_GROUP = props.getProperty("STATEMENT_UPDATE_GROUP");
        STATEMENT_UPDATE_ROLE = props.getProperty("STATEMENT_UPDATE_ROLE");
        STATEMENT_UPDATE_TOPIC = props.getProperty("STATEMENT_UPDATE_TOPIC");
        STATEMENT_UPDATE_TOPIC_VERSION = props.getProperty("STATEMENT_UPDATE_TOPIC_VERSION");

        STATEMENT_UPDATE_TOPIC_CURRENT_VERSION = props.getProperty("STATEMENT_UPDATE_TOPIC_CURRENT_VERSION");
        STATEMENT_UPDATE_TOPIC_CURRENT_VERSIONS = props.getProperty("STATEMENT_UPDATE_TOPIC_CURRENT_VERSIONS");

        STATEMENT_UPDATE_TOPIC_CURRENT_VERSION_AUTO_SEQ = props.getProperty("STATEMENT_UPDATE_TOPIC_CURRENT_VERSION_AUTO_SEQ");
        STATEMENT_UPDATE_TOPIC_CURRENT_VERSIONS_AUTO_SEQ = props.getProperty("STATEMENT_UPDATE_TOPIC_CURRENT_VERSIONS_AUTO_SEQ");

        STATEMENT_UPDATE_USER = props.getProperty("STATEMENT_UPDATE_USER");
        STATEMENT_UPDATE_VIRTUAL_WIKI = props.getProperty("STATEMENT_UPDATE_VIRTUAL_WIKI");
        STATEMENT_UPDATE_WIKI_FILE = props.getProperty("STATEMENT_UPDATE_WIKI_FILE");
        STATEMENT_UPDATE_WIKI_USER = props.getProperty("STATEMENT_UPDATE_WIKI_USER");

        STATEMENT_CREATE_TOPIC_CACHE_TABLE = props.getProperty("STATEMENT_CREATE_TOPIC_CACHE_TABLE");
        STATEMENT_DELETE_TOPIC_VERSION_CACHE = props.getProperty("STATEMENT_DELETE_TOPIC_VERSION_CACHE");
        STATEMENT_INSERT_TOPIC_VERSION_CACHE = props.getProperty("STATEMENT_INSERT_TOPIC_VERSION_CACHE");
        STATEMENT_SELECT_TOPIC_VERSION_CACHE = props.getProperty("STATEMENT_SELECT_TOPIC_VERSION_CACHE");
        STATEMENT_SELECT_TOPIC_VERSION_CACHE_BY_NAME = props.getProperty("STATEMENT_SELECT_TOPIC_VERSION_CACHE_BY_NAME");
        
        STATEMENT_SELECT_WP_IMAGE = props.getProperty("STATEMENT_SELECT_WP_IMAGE");
        STATEMENT_SELECT_WP_IMAGE_BY_PARENT = props.getProperty("STATEMENT_SELECT_WP_IMAGE_BY_PARENT");
        STATEMENT_INSERT_WP_IMAGE_AUTO_SEQ = props.getProperty("STATEMENT_INSERT_WP_IMAGE_AUTO_SEQ");
    }

    /**
     *
     */
    public void insertCategory(Category category, int virtualWikiId, Connection conn) throws SQLException {
        // FIXME - clean this code up
        ResultSet rs = this.lookupTopic(virtualWikiId, category.getChildTopicName(), false, conn);
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
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_INSERT_CATEGORY);
        stmt.setInt(1, rs.getInt(AnsiDataHandler.DATA_TOPIC_ID));
        stmt.setString(2, category.getName());
        stmt.setString(3, category.getSortKey());
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);

        if (rs != null) {
            try {
                rs.close();
            } catch (Exception ex) {
                logger.warning("Could not close ResultSet!", ex);
            }
        }
    }

    /**
     *
     * @param category 
     * @param topicId
     * @param conn
     * @param virtualWikiId
     * @throws SQLException
     */
    public void insertCategory(Category category, int topicId, int virtualWikiId, Connection conn) throws SQLException {

        if (topicId == -1) {
            throw new SQLException("Unassigned topic identifier " + category.getChildTopicName() + " for category " + category.getName());
        }
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_INSERT_CATEGORY);
        stmt.setInt(1, topicId);
        stmt.setString(2, category.getName());
        stmt.setString(3, category.getSortKey());
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    /**
     *
     */
    public void insertGroupAuthority(int groupId, String authority, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_INSERT_GROUP_AUTHORITY);
        stmt.setInt(1, groupId);
        stmt.setString(2, authority);
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    /**
     *
     */
    public void insertGroupMember(int groupMemberId, String username, int groupId, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_INSERT_GROUP_MEMBER);
        stmt.setInt(1, groupMemberId);
        stmt.setString(2, username);
        stmt.setInt(3, groupId);
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    /**
     *
     */
    public void insertRecentChange(RecentChange change, int virtualWikiId, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_INSERT_RECENT_CHANGE);
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

        //stmt.logParams();

        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    /**
     *
     */
    public void insertRole(Role role, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_INSERT_ROLE);
        stmt.setString(1, role.getAuthority());
        stmt.setString(2, role.getDescription());
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    public int insertTopic(Topic topic, int virtualWikiId, Connection conn) throws SQLException {

        int rv = -1;
        int topicId = -1;

        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(STATEMENT_INSERT_TOPIC_AUTO_SEQ, Statement.RETURN_GENERATED_KEYS);

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

            //stmt.logParams();
            logger.fine("SQL-QUERY-STRING =>: " + stmt.toString());
            rv = stmt.executeUpdate();

            if (rv > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    topicId = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            logger.severe(e.getMessage(), e);
            throw e;
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn, stmt, rs);
        }
        return topicId;
    }

    /**
     *
     * 
     */
    public int insertTopicVersion(TopicVersion topicVersion, Connection conn) throws SQLException {

        int rv = -1;
        int topicVersionId = -1;
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(STATEMENT_INSERT_TOPIC_VERSION_AUTO_SEQ, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, topicVersion.getTopicId());
            stmt.setString(2, topicVersion.getEditComment());

            byte[] buffer = null;
            String topicContent = topicVersion.getVersionContent();

            try {
                if ((topicContent != null) && (Environment.getBooleanValue(Environment.PROP_DB_COMPRESSION))) {
                    buffer = (byte[]) topicContent.getBytes("UTF-8");
                    buffer = DataCompression.compressByteArray(buffer);
                    stmt.setBytes(3, buffer);
                    stmt.setInt(12, DataCompression.PROP_DB_COMPRESSION_DEFAULT);
                } else {
                    stmt.setBytes(3, topicContent.getBytes("UTF-8"));
                    stmt.setInt(12, DataCompression.PROP_DB_COMPRESSION_NONE);
                }

            } catch (Exception e) {
                logger.severe(e.getMessage(), e);
            }
            if (topicVersion.getAuthorId() == null) {
                stmt.setNull(4, Types.INTEGER);
            } else {
                stmt.setInt(4, topicVersion.getAuthorId());
            }
            stmt.setInt(5, topicVersion.getEditType());
            stmt.setString(6, topicVersion.getAuthorDisplay());
            stmt.setTimestamp(7, topicVersion.getEditDate());
            if (topicVersion.getPreviousTopicVersionId() == null) {
                stmt.setNull(8, Types.INTEGER);
            } else {
                stmt.setInt(8, topicVersion.getPreviousTopicVersionId());
            }
            stmt.setInt(9, topicVersion.getCharactersChanged());
            stmt.setString(10, topicVersion.getVersionContentClean());
            stmt.setString(11, topicVersion.getVersionContentShort());

            //stmt.logParams();
            logger.fine("SQL-QUERY-STRING =>: " + stmt.toString());

            rv = stmt.executeUpdate();

            if (rv > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    topicVersionId = rs.getInt(1);
                }

                stmt = conn.prepareStatement(STATEMENT_UPDATE_TOPIC_CURRENT_VERSION_AUTO_SEQ);
                stmt.setInt(1, topicVersionId);
                stmt.setInt(2, topicVersion.getTopicId());

                //stmt.logParams();

                rv = stmt.executeUpdate();
            }
        } catch (SQLException e) {
            logger.severe(e.getMessage(), e);
            throw e;
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn, stmt, rs);
        }
        return topicVersionId;
    }
    
   

    /**
     *
     */
    public void insertUserDetails(WikiUserDetails userDetails, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_INSERT_USER);
        stmt.setString(1, userDetails.getUsername());
        stmt.setString(2, userDetails.getPassword());
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    /**
     *
     */
    public void insertUserAuthority(String username, String authority, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_INSERT_AUTHORITY);
        stmt.setString(1, username);
        stmt.setString(2, authority);
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    /**
     *
     */
    public void insertVirtualWiki(VirtualWiki virtualWiki, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_INSERT_VIRTUAL_WIKI);
        stmt.setInt(1, virtualWiki.getVirtualWikiId());
        stmt.setString(2, virtualWiki.getName());
        stmt.setString(3, virtualWiki.getDefaultTopicName());
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    /**
     *
     */
    public void insertWatchlistEntry(int virtualWikiId, String topicName, int userId, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_INSERT_WATCHLIST_ENTRY);
        stmt.setInt(1, virtualWikiId);
        stmt.setString(2, topicName);
        stmt.setInt(3, userId);
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    /**
     *
     */
    public void insertWikiFile(WikiFile wikiFile, int virtualWikiId, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_INSERT_WIKI_FILE);
        stmt.setInt(1, wikiFile.getFileId());
        stmt.setInt(2, virtualWikiId);
        stmt.setString(3, wikiFile.getFileName());
        stmt.setString(4, wikiFile.getUrl());
        stmt.setString(5, wikiFile.getMimeType());
        stmt.setInt(6, wikiFile.getTopicId());
        stmt.setTimestamp(7, wikiFile.getDeleteDate());
        stmt.setInt(8, (wikiFile.getReadOnly() ? 1 : 0));
        stmt.setInt(9, (wikiFile.getAdminOnly() ? 1 : 0));
        stmt.setLong(10, wikiFile.getFileSize());
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    /**
     *
     */
    public void insertWikiFileVersion(WikiFileVersion wikiFileVersion, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_INSERT_WIKI_FILE_VERSION);
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
        stmt.setLong(9, wikiFileVersion.getFileSize());
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    /**
     *
     */
    public void insertWikiGroup(WikiGroup group, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_INSERT_GROUP);
        stmt.setInt(1, group.getGroupId());
        stmt.setString(2, group.getName());
        stmt.setString(3, group.getDescription());
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    /**
     *
     */
    public void insertWikiUser(WikiUser user, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_INSERT_WIKI_USER);
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
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    /**
     *
     */
    public ResultSet lookupCategoryTopics(int virtualWikiId, String categoryName, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_CATEGORY_TOPICS);
        // category name must be lowercase since search is case-insensitive
        categoryName = categoryName.toLowerCase();
        stmt.setInt(1, virtualWikiId);
        stmt.setString(2, categoryName);
        return stmt.executeQuery();
    }

    /**
     *
     */
    public ResultSet lookupTopic(int virtualWikiId, String topicName, boolean caseSensitive, Connection conn) throws SQLException {
        PreparedStatement stmt = null;
        if (caseSensitive) {
            stmt = conn.prepareStatement(STATEMENT_SELECT_TOPIC);
        } else {
            topicName = topicName.toLowerCase();
            stmt = conn.prepareStatement(STATEMENT_SELECT_TOPIC_LOWER);
        }
        stmt.setInt(1, virtualWikiId);
        stmt.setString(2, topicName);
        return stmt.executeQuery();
    }

    /**
     *
     * @param virtualWikiId
     * @param topicId
     * @param conn
     * @return
     * @throws SQLException
     */
    public ResultSet lookupTopicById(int virtualWikiId, int topicId, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_TOPIC_BY_ID);
        stmt.setInt(1, virtualWikiId);
        stmt.setInt(2, topicId);
        return stmt.executeQuery();
    }

    /**
     *
     * @param virtualWikiId
     * @param topicId
     * @param conn
     * @return
     * @throws SQLException
     */
    public ResultSet lookupTopicMetaDataById(int virtualWikiId, int topicId, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_TOPIC_METADATA_BY_ID);
        stmt.setInt(1, virtualWikiId);
        stmt.setInt(2, topicId);
        return stmt.executeQuery();
    }

    /**
     *
     */
    public ResultSet lookupTopicByType(int virtualWikiId, int topicType, Pagination pagination, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_TOPIC_BY_TYPE);
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
     * @return
     * @throws SQLException
     */
    public ResultSet lookupTopicCount(int virtualWikiId, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_TOPIC_COUNT);
        stmt.setInt(1, virtualWikiId);
        return stmt.executeQuery();
    }

    /**
     *
     */
    public ResultSet lookupTopicVersion(int topicVersionId, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_TOPIC_VERSION);
        stmt.setInt(1, topicVersionId);
        return stmt.executeQuery();
    }

    /**
     *
     */
    public ResultSet lookupWikiFile(int virtualWikiId, int topicId, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_WIKI_FILE);
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
     * @return
     * @throws SQLException
     */
    public ResultSet lookupWikiFileCount(int virtualWikiId, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_WIKI_FILE_COUNT);
        stmt.setInt(1, virtualWikiId);
        return stmt.executeQuery();
    }

    /**
     *
     */
    public ResultSet lookupWikiGroup(String groupName, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_GROUP);
        stmt.setString(1, groupName);
        return stmt.executeQuery();
    }

    /**
     *
     */
    public ResultSet lookupWikiUser(int userId, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_WIKI_USER);
        stmt.setInt(1, userId);
        return stmt.executeQuery();
    }

    /**
     *
     * @param username
     */
    public ResultSet lookupWikiUser(String username, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_WIKI_USER_LOGIN);
        stmt.setString(1, username);
        return stmt.executeQuery();
    }

    /**
     * Return a count of all wiki users.
     * @return
     * @throws SQLException
     */
    public ResultSet lookupWikiUserCount(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_WIKI_USER_COUNT);
        return stmt.executeQuery();
    }

    /**
     *
     */
    public ResultSet lookupWikiUserEncryptedPassword(String username, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_WIKI_USER_DETAILS_PASSWORD);
        stmt.setString(1, username);
        return stmt.executeQuery();
    }

    /**
     *
     */
    public ResultSet lookupWikiUsers(Pagination pagination, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_WIKI_USERS);
        stmt.setInt(1, pagination.getNumResults());
        stmt.setInt(2, pagination.getOffset());
        return stmt.executeQuery();
    }

    /**
     *
     */
    public int nextGroupMemberId(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_GROUP_MEMBERS_SEQUENCE);
        ResultSet rs = stmt.executeQuery();
        int nextId = 0;
        if (rs.next()) {
            nextId = rs.getInt("id");
        }
        rs.close();
        // note - this returns the last id in the system, so add one
        return nextId + 1;
    }

    /**
     *
     */
    public int nextVirtualWikiId(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_VIRTUAL_WIKI_SEQUENCE);
        ResultSet rs = stmt.executeQuery();
        int nextId = 0;
        if (rs.next()) {
            nextId = rs.getInt("virtual_wiki_id");
        }
        rs.close();
        // note - this returns the last id in the system, so add one
        return nextId + 1;
    }

    /**
     *
     */
    public int nextWikiFileId(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_WIKI_FILE_SEQUENCE);
        ResultSet rs = stmt.executeQuery();
        int nextId = 0;
        if (rs.next()) {
            nextId = rs.getInt("file_id");
        }
        rs.close();
        // note - this returns the last id in the system, so add one
        return nextId + 1;
    }

    /**
     *
     */
    public int nextWikiFileVersionId(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_WIKI_FILE_VERSION_SEQUENCE);
        ResultSet rs = stmt.executeQuery();
        int nextId = 0;
        if (rs.next()) {
            nextId = rs.getInt("file_version_id");
        }
        rs.close();
        // note - this returns the last id in the system, so add one
        return nextId + 1;
    }

    /**
     *
     */
    public int nextWikiGroupId(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_GROUP_SEQUENCE);
        ResultSet rs = stmt.executeQuery();
        int nextId = 0;
        if (rs.next()) {
            nextId = rs.getInt(AnsiDataHandler.DATA_GROUP_ID);
        }
        rs.close();
        // note - this returns the last id in the system, so add one
        return nextId + 1;
    }

    /**
     *
     */
    public int nextWikiUserId(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_WIKI_USER_SEQUENCE);
        ResultSet rs = stmt.executeQuery();
        int nextId = 0;
        if (rs.next()) {
            nextId = rs.getInt(AnsiDataHandler.DATA_WIKI_USER_ID);
        }
        rs.close();
        // note - this returns the last id in the system, so add one
        return nextId + 1;
    }

    /**
     *
     */
    public void reloadRecentChanges(Connection conn) throws SQLException {
        /*
        DatabaseConnection.executeUpdate(STATEMENT_DELETE_RECENT_CHANGES, conn);
        DatabaseConnection.executeUpdate(STATEMENT_INSERT_RECENT_CHANGES, conn);
         */
    }

    /**
     *
     */
    public void updateRole(Role role, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_UPDATE_ROLE);
        stmt.setString(1, role.getDescription());
        stmt.setString(2, role.getAuthority());
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    /**
     *
     */
    public void updateTopic(Topic topic, int virtualWikiId, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_UPDATE_TOPIC);
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
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    /**
     *
     * @param topicVersion
     * @param virtualWikiId
     * @param conn
     * @throws SQLException
     */
    public void updateTopicVersion(TopicVersion topicVersion, int virtualWikiId, Connection conn) throws SQLException {

        PreparedStatement stmt = conn.prepareStatement(STATEMENT_UPDATE_TOPIC_VERSION);
        stmt.setInt(1, topicVersion.getTopicId());
        stmt.setString(2, topicVersion.getEditComment());

        byte[] buffer = null;
        String topicContent = topicVersion.getVersionContent();

        try {
            if (topicContent == null) {
                stmt.setNull(3, Types.BLOB);
            } else if ((topicContent != null) && (Environment.getBooleanValue(Environment.PROP_DB_COMPRESSION))) {
                buffer = (byte[]) topicContent.getBytes("UTF-8");
                buffer = DataCompression.compressByteArray(buffer);
                stmt.setBytes(3, buffer);
            } else if ((topicContent != null) && (!Environment.getBooleanValue(Environment.PROP_DB_COMPRESSION))) {
                stmt.setBytes(3, (byte[]) topicContent.getBytes("UTF-8"));
            }
        } catch (Exception e) {
            logger.severe(e.getMessage(), e);
        }
        if (topicVersion.getAuthorId() == null) {
            stmt.setNull(4, Types.INTEGER);
        } else {
            stmt.setInt(4, topicVersion.getAuthorId());
        }
        stmt.setString(5, topicVersion.getAuthorDisplay());
        stmt.setTimestamp(6, topicVersion.getEditDate());
        stmt.setInt(7, topicVersion.getEditType());

        if (topicVersion.getPreviousTopicVersionId() == null) {
            stmt.setNull(8, Types.INTEGER);
        } else {
            stmt.setInt(8, topicVersion.getPreviousTopicVersionId());
        }
        stmt.setInt(9, topicVersion.getCharactersChanged());
        //stmt.setString(10, topicVersion.getVersionContentClean());
        String topicContentClean = topicVersion.getVersionContentClean();

        try {
            if (topicContentClean == null) {
                stmt.setNull(10, Types.BLOB);
            } else if ((topicContentClean != null) && (Environment.getBooleanValue(Environment.PROP_DB_COMPRESSION))) {
                buffer = (byte[]) topicContentClean.getBytes("UTF-8");
                buffer = DataCompression.compressByteArray(buffer);
                stmt.setBytes(10, buffer);
            } else if ((topicContentClean != null) && (!Environment.getBooleanValue(Environment.PROP_DB_COMPRESSION))) {
                stmt.setBytes(10, (byte[]) topicContentClean.getBytes("UTF-8"));
            }
        } catch (Exception e) {
            logger.severe(e.getMessage(), e);
        }

        stmt.setString(11, topicVersion.getVersionContentShort());

        if (Environment.getBooleanValue(Environment.PROP_DB_COMPRESSION)) {
            stmt.setInt(12, DataCompression.PROP_DB_COMPRESSION_DEFAULT);
        } else {
            stmt.setInt(12, DataCompression.PROP_DB_COMPRESSION_NONE);
        }

        stmt.setInt(13, topicVersion.getTopicVersionId());
        //stmt.logParams();

        int rv = stmt.executeUpdate();
        rv = stmt.executeUpdate();

        if (rv < 1) {
            logger.severe("Failed to update TopicVersion =>: " + topicVersion.getTopicVersionId());
        }
        DatabaseConnection.closeStatement(stmt);
    }

    /**
     *
     */
    public void updateUserDetails(WikiUserDetails userDetails, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_UPDATE_USER);
        stmt.setString(1, userDetails.getPassword());
        stmt.setInt(2, 1);
        stmt.setString(3, userDetails.getUsername());
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    /**
     *
     */
    public void updateVirtualWiki(VirtualWiki virtualWiki, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_UPDATE_VIRTUAL_WIKI);
        stmt.setString(1, virtualWiki.getDefaultTopicName());
        stmt.setInt(2, virtualWiki.getVirtualWikiId());
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    /**
     *
     */
    public void updateWikiFile(WikiFile wikiFile, int virtualWikiId, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_UPDATE_WIKI_FILE);
        stmt.setInt(1, virtualWikiId);
        stmt.setString(2, wikiFile.getFileName());
        stmt.setString(3, wikiFile.getUrl());
        stmt.setString(4, wikiFile.getMimeType());
        stmt.setInt(5, wikiFile.getTopicId());
        stmt.setTimestamp(6, wikiFile.getDeleteDate());
        stmt.setInt(7, (wikiFile.getReadOnly() ? 1 : 0));
        stmt.setInt(8, (wikiFile.getAdminOnly() ? 1 : 0));
        stmt.setLong(9, wikiFile.getFileSize());
        stmt.setInt(10, wikiFile.getFileId());
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    /**
     *
     */
    public void updateWikiGroup(WikiGroup group, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_UPDATE_GROUP);
        stmt.setString(1, group.getName());
        stmt.setString(2, group.getDescription());
        stmt.setInt(3, group.getGroupId());
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    /**
     *
     */
    public void updateWikiUser(WikiUser user, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_UPDATE_WIKI_USER);
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getDisplayName());
        stmt.setTimestamp(3, user.getLastLoginDate());
        stmt.setString(4, user.getLastLoginIpAddress());
        stmt.setString(5, user.getDefaultLocale());
        stmt.setString(6, user.getEmail());
        stmt.setString(7, user.getEditor());
        stmt.setString(8, user.getSignature());
        stmt.setInt(9, user.getUserId());
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    // EXPERIMENTAL
    public void deleteParsedTopic(int virtualWikiId, int topicId, int topicVersionId, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(STATEMENT_DELETE_TOPIC_VERSION_CACHE);
        stmt.setInt(1, virtualWikiId);
        stmt.setInt(2, topicId);
        stmt.setInt(3, topicVersionId);
        stmt.executeUpdate();
        DatabaseConnection.closeStatement(stmt);
    }

    // EXPERIMENTAL
    public int insertParsedTopic(ParsedTopic parsedTopic, int virtualWikiId, Connection conn) throws SQLException {
        int rv = -1; // number of records inserted NOT GENERATED KEY

        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(STATEMENT_INSERT_TOPIC_VERSION_CACHE);

            stmt.setInt(1, parsedTopic.getTopicId());
            stmt.setInt(2, parsedTopic.getCurrentVersionId());
            stmt.setInt(3, virtualWikiId);
            stmt.setString(4, parsedTopic.getName());
            stmt.setBytes(5, parsedTopic.toString().getBytes("UTF-8"));
       
            //stmt.logParams();
            logger.fine("SQL-QUERY-STRING =>: " + stmt.toString());
            rv = stmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe(e.getMessage(), e);
            throw e;
        } catch(UnsupportedEncodingException uee){
            logger.severe(uee.getMessage(), uee);
        }
        finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn, stmt, rs);
        }
        return rv;
    }

    // EXPERIMENTAL
    public int insertWikipediaImage(WikiImageResource imgResource, Connection conn) throws SQLException{
        int rv = -1;
        int imageId = -1;

        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(STATEMENT_INSERT_WP_IMAGE_AUTO_SEQ , Statement.RETURN_GENERATED_KEYS);

            //  thumb,path,parent_name,image_name,size
            stmt.setInt(1, imgResource.getIsThumb());
            stmt.setString(2, imgResource.getPath());
            stmt.setString(3, imgResource.getParentName());
            stmt.setString(4, imgResource.getName());
            stmt.setInt(5, imgResource.getSize());

            //stmt.logParams();
            logger.fine("SQL-QUERY-STRING =>: " + stmt.toString());
            rv = stmt.executeUpdate();

            if (rv > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    imageId = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            logger.severe(e.getMessage(), e);
            throw e;
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn, stmt, rs);
        }
        return imageId;
    }

    // EXPERIMENTAL
    public ResultSet lookupParsedTopic(int virtualWikiId, int topicId, int topicVersionId, Connection conn) throws SQLException {

        PreparedStatement stmt = null;
        stmt = conn.prepareStatement(STATEMENT_SELECT_TOPIC_VERSION_CACHE);

        stmt.setInt(1, virtualWikiId);
        stmt.setInt(2, topicId);
        stmt.setInt(3, topicVersionId);
        return stmt.executeQuery();
    }

    // EXPERIMENTAL
    public ResultSet lookupParsedTopic(int virtualWikiId, String topicName, Connection conn) throws SQLException {

        PreparedStatement stmt = null;
        stmt = conn.prepareStatement(STATEMENT_SELECT_TOPIC_VERSION_CACHE_BY_NAME);

        stmt.setInt(1, virtualWikiId);
        stmt.setString(2, topicName);
        return stmt.executeQuery();
    }

    /**
     *
     */
    public ResultSet lookupWikipediaImage(String imageName, String parentName, Connection conn) throws SQLException {
        PreparedStatement stmt = null;

        if((parentName != null) && (!parentName.isEmpty())){
            stmt = conn.prepareStatement(STATEMENT_SELECT_WP_IMAGE_BY_PARENT);
            stmt.setString(1, parentName);
        }else{
            stmt = conn.prepareStatement(STATEMENT_SELECT_WP_IMAGE);
            stmt.setString(1, imageName);
        }
        
        return stmt.executeQuery();
    }
}
