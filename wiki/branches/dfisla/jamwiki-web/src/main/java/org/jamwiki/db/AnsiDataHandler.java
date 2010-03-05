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

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import net.sf.ehcache.Element;
import org.apache.commons.lang.StringUtils;
import org.jamwiki.DataAccessException;
import org.jamwiki.DataHandler;
import org.jamwiki.WikiBase;
import org.jamwiki.WikiException;
import org.jamwiki.WikiMessage;
import org.jamwiki.authentication.JAMWikiAuthenticationConfiguration;
import org.jamwiki.authentication.WikiUserDetails;
import org.jamwiki.model.Category;
import org.jamwiki.model.RecentChange;
import org.jamwiki.model.Role;
import org.jamwiki.model.RoleMap;
import org.jamwiki.model.Topic;
import org.jamwiki.model.LogItem;
import org.jamwiki.model.TopicVersion;
import org.jamwiki.model.VirtualWiki;
import org.jamwiki.model.Watchlist;
import org.jamwiki.model.WikiFile;
import org.jamwiki.model.WikiFileVersion;
import org.jamwiki.model.WikiGroup;
import org.jamwiki.model.WikiUser;
import org.jamwiki.parser.ParserException;
import org.jamwiki.parser.ParserOutput;
import org.jamwiki.parser.ParserUtil;
import org.jamwiki.utils.Encryption;
import org.jamwiki.utils.LinkUtil;
import org.jamwiki.utils.NamespaceHandler;
import org.jamwiki.utils.Pagination;
import org.jamwiki.utils.WikiCache;
import org.jamwiki.utils.WikiLink;
import org.jamwiki.Environment;
import org.jamwiki.authentication.RoleImpl;
import org.jamwiki.model.ParsedTopic;
import org.jamwiki.utils.WikiUtil;
import org.springframework.transaction.TransactionStatus;
import org.jamwiki.utils.WikiLogger;

/**
 * Default implementation of the {@link org.jamwiki.DataHandler} interface for
 * ANSI SQL compatible databases.
 */
public class AnsiDataHandler implements DataHandler {

    private static final String CACHE_TOPICS = "org.jamwiki.db.AnsiDataHandler.CACHE_TOPICS";
    private static final String CACHE_TOPIC_VERSIONS = "org.jamwiki.db.AnsiDataHandler.CACHE_TOPIC_VERSIONS";
    private static final String CACHE_VIRTUAL_WIKI = "org.jamwiki.db.AnsiDataHandler.CACHE_VIRTUAL_WIKI";
    private static final WikiLogger logger = WikiLogger.getLogger(AnsiDataHandler.class.getName());
// some constants
    public static final String DATA_TOPIC_NAME = "topic_name";
    public static final String DATA_WIKI_USER_ID = "wiki_user_id";
    public static final String DATA_GROUP_ID = "group_id";
    public static final String DATA_CATEGORY_NAME = "category_name";
    public static final String DATA_TOPIC_ID = "topic_id";
    private final QueryHandler queryHandler = new AnsiQueryHandler();

    /**
     *
     */
    private void addCategory(Category category, Connection conn) throws DataAccessException, WikiException {
        int virtualWikiId = this.lookupVirtualWikiId(category.getVirtualWiki());
        this.validateCategory(category);
        try {
            this.queryHandler().insertCategory(category, virtualWikiId, conn);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    private void addCategory(Category category, int topicId, Connection conn) throws DataAccessException, WikiException {
        int virtualWikiId = this.lookupVirtualWikiId(category.getVirtualWiki());
        this.validateCategory(category);
        try {
            this.queryHandler().insertCategory(category, topicId, virtualWikiId, conn);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    private void addGroupMember(String username, int groupId, Connection conn) throws DataAccessException {
        try {
            int groupMemberId = this.queryHandler().nextGroupMemberId(conn);
            this.queryHandler().insertGroupMember(groupMemberId, username, groupId, conn);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    private void addRecentChange(RecentChange change, Connection conn) throws DataAccessException, WikiException {
        int virtualWikiId = this.lookupVirtualWikiId(change.getVirtualWiki());
        this.validateRecentChange(change);
        try {
            this.queryHandler().insertRecentChange(change, virtualWikiId, conn);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    private int addTopic(Topic topic, Connection conn) throws DataAccessException, WikiException {
        int virtualWikiId = this.lookupVirtualWikiId(topic.getVirtualWiki());
        int topicId = -1;
        try {
            this.validateTopic(topic);
            topicId = this.queryHandler().insertTopic(topic, virtualWikiId, conn);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        return topicId;
    }

    /**
     *
     */
    private int addTopicVersion(TopicVersion topicVersion, Connection conn) throws DataAccessException, WikiException {
        int topicVersionId = -1;
        try {
            if (topicVersion.getEditDate() == null) {
                Timestamp editDate = new Timestamp(System.currentTimeMillis());
                topicVersion.setEditDate(editDate);
            }
            this.validateTopicVersion(topicVersion);
            topicVersionId = this.queryHandler().insertTopicVersion(topicVersion, conn);
        } catch (SQLException e) {
            logger.severe("WRITE-TOPIC-FAILED, TOPIC-ID: " + topicVersion.getTopicId() + " TOPIC-VERSION-ID: " + topicVersionId + " CONTENT: " + topicVersion.getVersionContent());
            throw new DataAccessException(e);
        }
        return topicVersionId;
    }

    /**
     * EXPERIMENTAL
     */
    private int addParsedTopic(ParsedTopic parsedTopic, Connection conn) throws DataAccessException, WikiException {
        int virtualWikiId = this.lookupVirtualWikiId(parsedTopic.getVirtualWiki());
        int rv = -1;
        try {
            rv = this.queryHandler().insertParsedTopic(parsedTopic, virtualWikiId, conn);
        } catch (SQLException e) {
            logger.severe("WRITE-PARSED-TOPIC-FAILED, TOPIC-ID: " + parsedTopic.getTopicId() + " CONTENT: " + parsedTopic.getTopicContent());
            throw new DataAccessException(e);
        }
        return rv;
    }

    /**
     *
     */
    private void addUserDetails(WikiUserDetails userDetails, Connection conn) throws DataAccessException, WikiException {
        this.validateUserDetails(userDetails);
        try {
            this.queryHandler().insertUserDetails(userDetails, conn);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    private void addVirtualWiki(VirtualWiki virtualWiki, Connection conn) throws DataAccessException, WikiException {
        try {
            if (virtualWiki.getVirtualWikiId() < 1) {
                int virtualWikiId = this.queryHandler().nextVirtualWikiId(conn);
                virtualWiki.setVirtualWikiId(virtualWikiId);
            }
            this.validateVirtualWiki(virtualWiki);
            this.queryHandler().insertVirtualWiki(virtualWiki, conn);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    private void addWatchlistEntry(int virtualWikiId, String topicName, int userId, Connection conn) throws DataAccessException, WikiException {
        this.validateWatchlistEntry(topicName);
        try {
            this.queryHandler().insertWatchlistEntry(virtualWikiId, topicName, userId, conn);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    private void addWikiFile(WikiFile wikiFile, Connection conn) throws DataAccessException, WikiException {
        try {
            if (wikiFile.getFileId() < 1) {
                int fileId = this.queryHandler().nextWikiFileId(conn);
                wikiFile.setFileId(fileId);
            }
            int virtualWikiId = this.lookupVirtualWikiId(wikiFile.getVirtualWiki());
            this.validateWikiFile(wikiFile);
            this.queryHandler().insertWikiFile(wikiFile, virtualWikiId, conn);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    private void addWikiFileVersion(WikiFileVersion wikiFileVersion, Connection conn) throws DataAccessException, WikiException {
        try {
            if (wikiFileVersion.getFileVersionId() < 1) {
                int fileVersionId = this.queryHandler().nextWikiFileVersionId(conn);
                wikiFileVersion.setFileVersionId(fileVersionId);
            }
            if (wikiFileVersion.getUploadDate() == null) {
                Timestamp uploadDate = new Timestamp(System.currentTimeMillis());
                wikiFileVersion.setUploadDate(uploadDate);
            }
            this.validateWikiFileVersion(wikiFileVersion);
            this.queryHandler().insertWikiFileVersion(wikiFileVersion, conn);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    private void addWikiGroup(WikiGroup group, Connection conn) throws DataAccessException, WikiException {
        try {
            if (group.getGroupId() < 1) {
                int groupId = this.queryHandler().nextWikiGroupId(conn);
                group.setGroupId(groupId);
            }
            this.validateWikiGroup(group);
            this.queryHandler().insertWikiGroup(group, conn);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    private void addWikiUser(WikiUser user, Connection conn) throws DataAccessException, WikiException {
        try {
            if (user.getUserId() < 1) {
                int nextUserId = this.queryHandler().nextWikiUserId(conn);
                user.setUserId(nextUserId);
            }
            this.validateWikiUser(user);
            this.queryHandler().insertWikiUser(user, conn);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    public boolean authenticate(String username, String password) throws DataAccessException {
        boolean result = false;
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();

            // password is stored encrypted, so encrypt password
            if (!StringUtils.isBlank(password)) {
                String encryptedPassword = Encryption.encrypt(password);
                result = this.queryHandler().authenticateUser(username, encryptedPassword, conn);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        } finally {
            DatabaseConnection.closeConnection(conn);
        }

        return result;
    }

    /**
     *
     */
    public boolean canMoveTopic(Topic fromTopic, String destination) throws DataAccessException {
        Topic toTopic = this.lookupTopic(fromTopic.getVirtualWiki(), destination, false, null);
        if (toTopic == null || toTopic.getDeleteDate() != null) {
            // destination doesn't exist or is deleted, so move is OK
            return true;
        }
        if (toTopic.getRedirectTo() != null && toTopic.getRedirectTo().equals(fromTopic.getName())) {
            // source redirects to destination, so move is OK
            return true;
        }
        return false;
    }

    /**
     *
     */
    private static void checkLength(String value, int maxLength) throws WikiException {
        if (value != null && value.length() > maxLength) {
            logger.severe("error.fieldlength value =>: " + value + " limit: " + Integer.valueOf(maxLength).toString());
            throw new WikiException(new WikiMessage("error.fieldlength", value, Integer.valueOf(maxLength).toString()));
        }
    }

    /**
     *
     */
    private void deleteRecentChanges(Topic topic, Connection conn) throws DataAccessException {
        try {
            this.queryHandler().deleteRecentChanges(topic.getTopicId(), conn);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     * JAMWIKI-NEW
     */
     public void deleteTopic(Topic topic, TopicVersion topicVersion) throws DataAccessException, WikiException {
         deleteTopic(topic, topicVersion, true);
     }
    
    /**
     *
     */
    public void deleteTopic(Topic topic, TopicVersion topicVersion, boolean userVisible) throws DataAccessException, WikiException {
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            if (userVisible) {
                // delete old recent changes
                deleteRecentChanges(topic, conn);
            }
            // update topic to indicate deleted, add delete topic version.  parser output
            // should be empty since nothing to add to search engine.
            ParserOutput parserOutput = new ParserOutput();
            topic.setDeleteDate(new Timestamp(System.currentTimeMillis()));
            this.writeTopic(topic, topicVersion, parserOutput.getCategories(), parserOutput.getLinks(), userVisible);

            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } catch (WikiException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }

    }

    /**
     *
     */
    private void deleteTopicCategories(Topic topic, Connection conn) throws DataAccessException {
        try {
            this.queryHandler().deleteTopicCategories(topic.getTopicId(), conn);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    private void deleteTopicCategories(int topicId, Connection conn) throws DataAccessException {
        try {
            this.queryHandler().deleteTopicCategories(topicId, conn);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    private void deleteWatchlistEntry(int virtualWikiId, String topicName, int userId, Connection conn) throws DataAccessException {
        try {
            this.queryHandler().deleteWatchlistEntry(virtualWikiId, topicName, userId, conn);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

     /** JAMWIKI-NEW
     * This method should be called only during upgrades and provides the capability
     * to execute a SQL query from a QueryHandler-specific property file.
     *
     * @param prop The name of the SQL property file value to execute.
     * @param conn The SQL connection to use when executing the SQL.
     * @throws SQLException Thrown if any error occurs during execution.
     */
    public void executeUpgradeQuery(String prop, Connection conn) throws SQLException{
        throw new SQLException("Not Implemented!");
    }

    /** JAMWIKI-NEW
     * This method should be called only during upgrades and provides the capability
     * to execute update SQL from a QueryHandler-specific property file.
     *
     * @param prop The name of the SQL property file value to execute.
     * @param conn The SQL connection to use when executing the SQL.
     * @throws SQLException Thrown if any error occurs during execution.
     */
    public void executeUpgradeUpdate(String prop, Connection conn) throws SQLException{
        throw new SQLException("Not Implemented!");
    }
    
    /**
     *
     */
    public List<Category> getAllCategories(String virtualWiki, Pagination pagination) throws DataAccessException {
        List<Category> results = new ArrayList<Category>();
        int virtualWikiId = this.lookupVirtualWikiId(virtualWiki);

        ResultSet rs = null;
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            rs = this.queryHandler().getCategories(virtualWikiId, pagination, conn);
            while (rs.next()) {
                Category category = new Category();
                category.setName(rs.getString(DATA_CATEGORY_NAME));
                // child topic name not initialized since it is not needed
                category.setVirtualWiki(virtualWiki);
                category.setSortKey(rs.getString("sort_key"));
                // topic type not initialized since it is not needed
                results.add(category);
            }
            
            DatabaseConnection.commit(status);
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return results;
    }

    /**
     *
     */
    public List<Role> getAllRoles() throws DataAccessException {
        List<Role> results = new ArrayList<Role>();
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            rs = this.queryHandler().getRoles(conn);

            while (rs.next()) {
                results.add(this.initRole(rs));
            }

        } catch (SQLException e) {
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }
        return results;
    }

    /**
     *
     */
    public List<String> getAllTopicNames(String virtualWiki) throws DataAccessException {
        List<String> all = new ArrayList<String>();
        int virtualWikiId = this.lookupVirtualWikiId(virtualWiki);
        ResultSet rs = null;
        TransactionStatus status = null;
        Connection conn = null;

        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            rs = this.queryHandler().getAllTopicNames(virtualWikiId, conn);
            while (rs.next()) {
                all.add(rs.getString(DATA_TOPIC_NAME));
            }

            DatabaseConnection.commit(status);
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return all;
    }

    public List<Integer> getAllTopicIdentifiers(String virtualWiki) throws DataAccessException {

        List<Integer> all = new ArrayList<Integer>();

        int virtualWikiId = this.lookupVirtualWikiId(virtualWiki);
        logger.info("VIRTUAL-WIKI-ID =>: " + virtualWikiId);
        ResultSet rs = null;
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            rs = this.queryHandler().getAllTopicIdentifiers(virtualWikiId, conn);
            while (rs.next()) {
                all.add(rs.getInt(DATA_TOPIC_ID));
            }

            DatabaseConnection.commit(status);
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return all;
    }

    /**
     *
     */
    public List<WikiFileVersion> getAllWikiFileVersions(String virtualWiki, String topicName, boolean descending) throws DataAccessException {
        List<WikiFileVersion> all = new ArrayList<WikiFileVersion>();
        WikiFile wikiFile = lookupWikiFile(virtualWiki, topicName);
        if (wikiFile == null) {
            throw new DataAccessException("No topic exists for " + virtualWiki + " / " + topicName);
        }
        ResultSet rs = null;
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            rs = this.queryHandler().getAllWikiFileVersions(wikiFile, descending, conn);
            while (rs.next()) {
                all.add(initWikiFileVersion(rs));
            }
            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return all;
    }

    
    /** JAMWIKI-NEW
     * Retrieve a List of all LogItem objects for a given virtual wiki, sorted
     * chronologically.
     *
     * @param virtualWiki The virtual wiki for which log items are being
     *  retrieved.
     * @param logType Set to <code>-1</code> if all log items should be returned,
     *  otherwise set the log type for items to retrieve.
     * @param pagination A Pagination object indicating the total number of
     *  results and offset for the results to be retrieved.
     * @param descending Set to <code>true</code> if the results should be
     *  sorted with the most recent log items first, <code>false</code> if the
     *  results should be sorted with the oldest items first.
     * @return A List of LogItem objects for a given virtual wiki, sorted
     *  chronologically.
     * @throws DataAccessException Thrown if any error occurs during method execution.
     */
    public List<LogItem> getLogItems(String virtualWiki, int logType, Pagination pagination, boolean descending) throws DataAccessException{
        throw new DataAccessException("Not Implemented!");
    }

    /**
     *
     */
    public List<RecentChange> getRecentChanges(String virtualWiki, Pagination pagination, boolean descending) throws DataAccessException {
        List<RecentChange> all = new ArrayList<RecentChange>();
        ResultSet rs = null;
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            rs = this.queryHandler().getRecentChanges(virtualWiki, pagination, descending, conn);

            while (rs.next()) {
                RecentChange change = initRecentChange(rs);
                all.add(change);
            }

            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return all;
    }

    /**
     *
     */
    public List<RecentChange> getRecentChanges(String virtualWiki, String topicName, Pagination pagination, boolean descending) throws DataAccessException {
        List<RecentChange> all = new ArrayList<RecentChange>();
        Topic topic = this.lookupTopic(virtualWiki, topicName, true, null);
        if (topic == null) {
            return all;
        }
        ResultSet rs = null;
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            rs = this.queryHandler().getRecentChanges(topic.getTopicId(), pagination, descending, conn);

            while (rs.next()) {
                RecentChange change = initRecentChange(rs);
                all.add(change);
            }

            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return all;
    }

    /**
     *
     */
    public List<RoleMap> getRoleMapByLogin(String loginFragment) throws DataAccessException {
        LinkedHashMap<Integer, RoleMap> roleMaps = new LinkedHashMap<Integer, RoleMap>();
        ResultSet rs = null;
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            rs = this.queryHandler().getRoleMapByLogin(loginFragment, conn);
            while (rs.next()) {
                Integer userId = rs.getInt(DATA_WIKI_USER_ID);
                RoleMap roleMap = new RoleMap();
                if (roleMaps.containsKey(userId)) {
                    roleMap = roleMaps.get(userId);
                } else {
                    roleMap.setUserId(userId);
                    roleMap.setUserLogin(rs.getString("username"));
                }
                roleMap.addRole(rs.getString("authority"));
                roleMaps.put(userId, roleMap);
            }

            DatabaseConnection.commit(status);
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return new ArrayList<RoleMap>(roleMaps.values());
    }

    /**
     *
     * @param authority
     */
    public List<RoleMap> getRoleMapByRole(String authority) throws DataAccessException {
        LinkedHashMap<String, RoleMap> roleMaps = new LinkedHashMap<String, RoleMap>();
        ResultSet rs = null;
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            rs = this.queryHandler().getRoleMapByRole(authority, conn);
            while (rs.next()) {
                int userId = rs.getInt(DATA_WIKI_USER_ID);
                int groupId = rs.getInt(DATA_GROUP_ID);
                RoleMap roleMap = new RoleMap();
                String key = userId + "|" + groupId;
                if (roleMaps.containsKey(key)) {
                    roleMap = roleMaps.get(key);
                } else {
                    if (userId > 0) {
                        roleMap.setUserId(userId);
                        roleMap.setUserLogin(rs.getString("username"));
                    }
                    if (groupId > 0) {
                        roleMap.setGroupId(groupId);
                        roleMap.setGroupName(rs.getString("group_name"));
                    }
                }
                roleMap.addRole(rs.getString("authority"));
                roleMaps.put(key, roleMap);
            }

            DatabaseConnection.commit(status);
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return new ArrayList<RoleMap>(roleMaps.values());
    }

    /**
     *
     */
    public List<Role> getRoleMapGroup(String groupName) throws DataAccessException {
        List<Role> results = new ArrayList<Role>();
        ResultSet rs = null;
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            rs = this.queryHandler().getRoleMapGroup(groupName, conn);

            while (rs.next()) {
                Role role = this.initRole(rs);
                results.add(role);
            }

            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        //return results.toArray(new Role[0]);
        return results;
    }

    /**
     *
     */
    public List<RoleMap> getRoleMapGroups() throws DataAccessException {
        LinkedHashMap<Integer, RoleMap> roleMaps = new LinkedHashMap<Integer, RoleMap>();
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();

            rs = this.queryHandler().getRoleMapGroups(conn);
            while (rs.next()) {
                Integer groupId = rs.getInt(DATA_GROUP_ID);
                RoleMap roleMap = new RoleMap();
                if (roleMaps.containsKey(groupId)) {
                    roleMap = roleMaps.get(groupId);
                } else {
                    roleMap.setGroupId(groupId);
                    roleMap.setGroupName(rs.getString("group_name"));
                }
                roleMap.addRole(rs.getString("authority"));
                roleMaps.put(groupId, roleMap);
            }

        } catch (SQLException e) {
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return new ArrayList<RoleMap>(roleMaps.values());
    }

    /**
     *
     */
    public List<Role> getRoleMapUser(String login) throws DataAccessException {
        List<Role> results = new ArrayList<Role>();
        ResultSet rs = null;
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            rs = this.queryHandler().getRoleMapUser(login, conn);

            while (rs.next()) {
                Role role = this.initRole(rs);
                results.add(role);
            }

            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        //return results.toArray(new Role[0]);
        return results;
    }


    /** JAMWIKI-NEW
     * Retrieve a List of RecentChange objects representing a topic's history,
     * sorted chronologically.
     *
     * @param virtualWiki The virtual wiki for the topic being queried.
     * @param topicName The name of the topic being queried.
     * @param pagination A Pagination object indicating the total number of
     *  results and offset for the results to be retrieved.
     * @param descending Set to <code>true</code> if the results should be
     *  sorted with the most recent changes first, <code>false</code> if the
     *  results should be sorted with the oldest changes first.
     * @return A List of all RecentChange objects representing a topic's history,
     *  sorted chronologically.
     * @throws DataAccessException Thrown if any error occurs during method execution.
     */
    public List<RecentChange> getTopicHistory(String virtualWiki, String topicName, Pagination pagination, boolean descending) throws DataAccessException{
        throw new DataAccessException("Not Implemented");
    }

    /**
     *
     */
    public List<String> getTopicsAdmin(String virtualWiki, Pagination pagination) throws DataAccessException {
        List<String> all = new ArrayList<String>();
        int virtualWikiId = this.lookupVirtualWikiId(virtualWiki);
        ResultSet rs = null;
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            rs = this.queryHandler().getTopicsAdmin(virtualWikiId, pagination, conn);
            while (rs.next()) {
                String topicName = rs.getString(DATA_TOPIC_NAME);
                all.add(topicName);
            }

            DatabaseConnection.commit(status);
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return all;
    }

    /**
     *
     */
    public List<RecentChange> getUserContributions(String virtualWiki, String userString, Pagination pagination, boolean descending) throws DataAccessException {
        List<RecentChange> all = new ArrayList<RecentChange>();
        ResultSet rs = null;
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            if (this.lookupWikiUser(userString) != null) {
                rs = this.queryHandler().getUserContributionsByLogin(virtualWiki, userString, pagination, descending, conn);
            } else {
                rs = this.queryHandler().getUserContributionsByUserDisplay(virtualWiki, userString, pagination, descending, conn);
            }

            while (rs.next()) {
                RecentChange change = initRecentChange(rs);
                all.add(change);
            }

            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return all;
    }

    /**
     * Return a List of all VirtualWiki objects that exist for the Wiki.
     */
    public List<VirtualWiki> getVirtualWikiList() throws DataAccessException {
        List<VirtualWiki> results = new ArrayList<VirtualWiki>();
        TransactionStatus status = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();
            rs = this.queryHandler().getVirtualWikis(conn);
            while (rs.next()) {
                VirtualWiki virtualWiki = initVirtualWiki(rs);
                results.add(virtualWiki);
            }

            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return results;
    }

    /**
     * Retrieve a watchlist containing a List of topic ids and topic
     * names that can be used to determine if a topic is in a user's current
     * watchlist.
     */
    public Watchlist getWatchlist(String virtualWiki, int userId) throws DataAccessException {
        List<String> all = new ArrayList<String>();
        int virtualWikiId = this.lookupVirtualWikiId(virtualWiki);
        ResultSet rs = null;
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            rs = this.queryHandler().getWatchlist(virtualWikiId, userId, conn);
            while (rs.next()) {
                String topicName = rs.getString(DATA_TOPIC_NAME);
                all.add(topicName);
            }

            DatabaseConnection.commit(status);
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return new Watchlist(virtualWiki, all);
    }

    /**
     * Retrieve a watchlist containing a List of RecentChanges objects
     * that can be used for display on the Special:Watchlist page.
     */
    public List<RecentChange> getWatchlist(String virtualWiki, int userId, Pagination pagination) throws DataAccessException {
        List<RecentChange> all = new ArrayList<RecentChange>();
        int virtualWikiId = this.lookupVirtualWikiId(virtualWiki);
        ResultSet rs = null;
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            rs = this.queryHandler().getWatchlist(virtualWikiId, userId, pagination, conn);
            while (rs.next()) {
                RecentChange change = initRecentChange(rs);
                all.add(change);
            }

            DatabaseConnection.commit(status);
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return all;
    }

    /**
     *
     */
    private RecentChange initRecentChange(ResultSet rs) throws DataAccessException {
        try {
            RecentChange change = new RecentChange();
            change.setTopicVersionId(rs.getInt("topic_version_id"));
            int previousTopicVersionId = rs.getInt("previous_topic_version_id");
            if (previousTopicVersionId > 0) {
                change.setPreviousTopicVersionId(previousTopicVersionId);
            }
            change.setTopicId(rs.getInt(DATA_TOPIC_ID));
            change.setTopicName(rs.getString(DATA_TOPIC_NAME));
            change.setCharactersChanged(rs.getInt("characters_changed"));
            change.setEditDate(rs.getTimestamp("edit_date"));
            change.setEditComment(rs.getString("edit_comment"));
            int userId = rs.getInt(DATA_WIKI_USER_ID);
            if (userId > 0) {
                change.setAuthorId(userId);
            }
            change.setAuthorName(rs.getString("display_name"));
            change.setEditType(rs.getInt("edit_type"));
            change.setVirtualWiki(rs.getString("virtual_wiki_name"));
            return change;
        } catch (SQLException e) {
            logger.severe("Failure while initializing recent change", e);
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    private Role initRole(ResultSet rs) throws DataAccessException {
        try {
            Role role = new RoleImpl(rs.getString("role_name"));
            role.setDescription(rs.getString("role_description"));
            return role;
        } catch (SQLException e) {
            logger.severe("Failure while initializing role", e);
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    private Topic initTopic(ResultSet rs) throws DataAccessException {
        try {
            // if a topic by this name has been deleted then there will be
            // multiple results.  the first will be a non-deleted topic (if
            // one exists), otherwise the last is the most recently deleted
            // topic.
            // FIXME
            //if (rs.size() > 1 && rs.getTimestamp("delete_date") != null) {
            // go to the last result
            //rs.last();
            //}
            int virtualWikiId = rs.getInt("virtual_wiki_id");
            String virtualWiki = this.lookupVirtualWikiName(virtualWikiId);
            Topic topic = new Topic();
            topic.setAdminOnly(rs.getInt("topic_admin_only") != 0);
            topic.setName(rs.getString(DATA_TOPIC_NAME));
            topic.setVirtualWiki(virtualWiki);
            int currentVersionId = rs.getInt("current_version_id");
            if (currentVersionId > 0) {
                topic.setCurrentVersionId(currentVersionId);
            }

            byte[] buffer = null;
            int bzType = rs.getInt("bz_type");
            try {
                buffer = (byte[]) rs.getObject("version_content");

                if (Environment.getBooleanValue(Environment.PROP_DB_COMPRESSION) && (bzType == DataCompression.PROP_DB_COMPRESSION_DEFAULT)) {
                    buffer = DataCompression.decompressByteArray(buffer);
                }

                if (buffer != null) {
                    topic.setTopicContent(new String(buffer, "UTF-8"));
                }
            } catch (java.io.UnsupportedEncodingException uee) {
                logger.severe(uee.getMessage(), uee);
            } catch (java.io.IOException ioe) {
                logger.severe(ioe.getMessage(), ioe);
            }

            try {
                buffer = (byte[]) rs.getObject("version_content_clean");
                if (buffer != null) {
                    topic.setTopicContentClean(new String(buffer, "UTF-8"));
                }
            } catch (java.io.UnsupportedEncodingException uee) {
                logger.severe(uee.getMessage(), uee);
            }
            //topic.setTopicContentClean(rs.getString("version_content_clean"));
            topic.setTopicContentShort(rs.getString("version_content_short"));
            // FIXME - Oracle cannot store an empty string - it converts them
            // to null - so add a hack to work around the problem.
            if (topic.getTopicContent() == null) {
                topic.setTopicContent("");
            }
            topic.setTopicId(rs.getInt(DATA_TOPIC_ID));
            topic.setReadOnly(rs.getInt("topic_read_only") != 0);
            topic.setDeleteDate(rs.getTimestamp("delete_date"));
            topic.setTopicType(rs.getInt("topic_type"));
            topic.setRedirectTo(rs.getString("redirect_to"));
            return topic;
        } catch (SQLException e) {
            logger.severe("Failure while initializing topic", e);
            throw new DataAccessException(e);
        }
    }

    private Topic initTopicMetaData(ResultSet rs) throws DataAccessException {
        try {
            // if a topic by this name has been deleted then there will be
            // multiple results.  the first will be a non-deleted topic (if
            // one exists), otherwise the last is the most recently deleted
            // topic.
            // FIXME
            //if (rs.size() > 1 && rs.getTimestamp("delete_date") != null) {
            // go to the last result
            //    rs.last();
            //}
            int virtualWikiId = rs.getInt("virtual_wiki_id");
            String virtualWiki = this.lookupVirtualWikiName(virtualWikiId);
            Topic topic = new Topic();
            topic.setAdminOnly(rs.getInt("topic_admin_only") != 0);
            topic.setName(rs.getString(DATA_TOPIC_NAME));
            topic.setVirtualWiki(virtualWiki);
            int currentVersionId = rs.getInt("current_version_id");
            if (currentVersionId > 0) {
                topic.setCurrentVersionId(currentVersionId);
            }

            topic.setTopicId(rs.getInt(DATA_TOPIC_ID));
            topic.setReadOnly(rs.getInt("topic_read_only") != 0);
            topic.setDeleteDate(rs.getTimestamp("delete_date"));
            topic.setTopicType(rs.getInt("topic_type"));
            topic.setRedirectTo(rs.getString("redirect_to"));
            return topic;
        } catch (SQLException e) {
            logger.severe("Failure while initializing topic", e);
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    private TopicVersion initTopicVersion(ResultSet rs) throws DataAccessException {
        try {
            TopicVersion topicVersion = new TopicVersion();
            topicVersion.setTopicVersionId(rs.getInt("topic_version_id"));
            topicVersion.setTopicId(rs.getInt(DATA_TOPIC_ID));
            topicVersion.setEditComment(rs.getString("edit_comment"));

            byte[] buffer = null;
            int bzType = rs.getInt("bz_type");
            try {
                buffer = (byte[]) rs.getObject("version_content");
                if (buffer != null) {

                    if (Environment.getBooleanValue(Environment.PROP_DB_COMPRESSION) && (bzType == DataCompression.PROP_DB_COMPRESSION_DEFAULT)) {
                        //DataCompression dc = new DataCompression();
                        buffer = DataCompression.decompressByteArray(buffer);
                    }

                    topicVersion.setVersionContent(new String(buffer, "UTF-8"));
                }
            } catch (java.io.UnsupportedEncodingException uee) {
                logger.severe(uee.getMessage(), uee);
            }

            try {
                buffer = (byte[]) rs.getObject("version_content_clean");
                if (buffer != null) {

                    if (Environment.getBooleanValue(Environment.PROP_DB_COMPRESSION) && (bzType == DataCompression.PROP_DB_COMPRESSION_DEFAULT)) {
                        //DataCompression dc = new DataCompression();
                        buffer = DataCompression.decompressByteArray(buffer);
                    }

                    topicVersion.setVersionContentClean(new String(buffer, "UTF-8"));
                }
            } catch (java.io.UnsupportedEncodingException uee) {
                logger.severe(uee.getMessage(), uee);
            }

            topicVersion.setVersionContentShort(rs.getString("version_content_short"));
            // FIXME - Oracle cannot store an empty string - it converts them
            // to null - so add a hack to work around the problem.
            if (topicVersion.getVersionContent() == null) {
                topicVersion.setVersionContent("");
            }
            int previousTopicVersionId = rs.getInt("previous_topic_version_id");
            if (previousTopicVersionId > 0) {
                topicVersion.setPreviousTopicVersionId(previousTopicVersionId);
            }
            int userId = rs.getInt(DATA_WIKI_USER_ID);
            if (userId > 0) {
                topicVersion.setAuthorId(userId);
            }
            topicVersion.setCharactersChanged(rs.getInt("characters_changed"));
            topicVersion.setEditDate(rs.getTimestamp("edit_date"));
            topicVersion.setEditType(rs.getInt("edit_type"));
            topicVersion.setAuthorDisplay(rs.getString("wiki_user_display"));
            topicVersion.setBzType(rs.getInt("bz_type"));
            return topicVersion;
        } catch (SQLException e) {
            logger.severe("Failure while initializing topic version", e);
            throw new DataAccessException(e);
        }
    }


    /**
     * EXPERIMENTAL
     */
    private ParsedTopic initParsedTopic(ResultSet rs) throws DataAccessException {
        try {
            ParsedTopic parsedTopic = null;

            byte[] buffer = null;

            try {
                buffer = (byte[]) rs.getObject("data");
                if (buffer != null) {
                    parsedTopic = new ParsedTopic(new String(buffer));
                }
            } catch (Exception uee) {
                logger.severe(uee.getMessage(), uee);
            }

            logger.info("PARSEDTOPIC-CONTENT: " + parsedTopic.getTopicContent());
            return parsedTopic;
        } catch (Exception e) {
            logger.severe("Failure while initializing parsed topic", e);
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    private VirtualWiki initVirtualWiki(ResultSet rs) throws DataAccessException {
        try {
            VirtualWiki virtualWiki = new VirtualWiki();
            virtualWiki.setVirtualWikiId(rs.getInt("virtual_wiki_id"));
            virtualWiki.setName(rs.getString("virtual_wiki_name"));
            virtualWiki.setDefaultTopicName(rs.getString("default_topic_name"));
            return virtualWiki;
        } catch (SQLException e) {
            logger.severe("Failure while initializing virtual wiki", e);
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    private WikiFile initWikiFile(ResultSet rs) throws DataAccessException {
        try {
            int virtualWikiId = rs.getInt("virtual_wiki_id");
            String virtualWiki = this.lookupVirtualWikiName(virtualWikiId);
            WikiFile wikiFile = new WikiFile();
            wikiFile.setFileId(rs.getInt("file_id"));
            wikiFile.setAdminOnly(rs.getInt("file_admin_only") != 0);
            wikiFile.setFileName(rs.getString("file_name"));
            wikiFile.setVirtualWiki(virtualWiki);
            wikiFile.setUrl(rs.getString("file_url"));
            wikiFile.setTopicId(rs.getInt(DATA_TOPIC_ID));
            wikiFile.setReadOnly(rs.getInt("file_read_only") != 0);
            wikiFile.setDeleteDate(rs.getTimestamp("delete_date"));
            wikiFile.setMimeType(rs.getString("mime_type"));
            wikiFile.setFileSize(rs.getInt("file_size"));
            return wikiFile;
        } catch (SQLException e) {
            logger.severe("Failure while initializing file", e);
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    private WikiFileVersion initWikiFileVersion(ResultSet rs) throws DataAccessException {
        try {
            WikiFileVersion wikiFileVersion = new WikiFileVersion();
            wikiFileVersion.setFileVersionId(rs.getInt("file_version_id"));
            wikiFileVersion.setFileId(rs.getInt("file_id"));
            wikiFileVersion.setUploadComment(rs.getString("upload_comment"));
            wikiFileVersion.setUrl(rs.getString("file_url"));
            int userId = rs.getInt(DATA_WIKI_USER_ID);
            if (userId > 0) {
                wikiFileVersion.setAuthorId(userId);
            }
            wikiFileVersion.setUploadDate(rs.getTimestamp("upload_date"));
            wikiFileVersion.setMimeType(rs.getString("mime_type"));
            wikiFileVersion.setAuthorDisplay(rs.getString("wiki_user_display"));
            wikiFileVersion.setFileSize(rs.getInt("file_size"));
            return wikiFileVersion;
        } catch (SQLException e) {
            logger.severe("Failure while initializing wiki file version", e);
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    private WikiGroup initWikiGroup(ResultSet rs) throws DataAccessException {
        try {
            WikiGroup wikiGroup = new WikiGroup();
            wikiGroup.setGroupId(rs.getInt("group_id"));
            wikiGroup.setName(rs.getString("group_name"));
            wikiGroup.setDescription(rs.getString("group_description"));
            return wikiGroup;
        } catch (SQLException e) {
            logger.severe("Failure while initializing group", e);
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    private WikiUser initWikiUser(ResultSet rs) throws DataAccessException {
        try {
            String username = rs.getString("login");
            WikiUser user = new WikiUser(username);
            user.setUserId(rs.getInt(DATA_WIKI_USER_ID));
            user.setDisplayName(rs.getString("display_name"));
            user.setCreateDate(rs.getTimestamp("create_date"));
            user.setLastLoginDate(rs.getTimestamp("last_login_date"));
            user.setCreateIpAddress(rs.getString("create_ip_address"));
            user.setLastLoginIpAddress(rs.getString("last_login_ip_address"));
            user.setDefaultLocale(rs.getString("default_locale"));
            user.setEmail(rs.getString("email"));
            user.setEditor(rs.getString("editor"));
            user.setSignature(rs.getString("signature"));
            return user;
        } catch (SQLException e) {
            logger.severe("Failure while initializing user", e);
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    public List<Category> lookupCategoryTopics(String virtualWiki, String categoryName) throws DataAccessException {
        List<Category> results = new ArrayList<Category>();
        int virtualWikiId = this.lookupVirtualWikiId(virtualWiki);
        ResultSet rs = null;
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            rs = this.queryHandler().lookupCategoryTopics(virtualWikiId, categoryName, conn);
            while (rs.next()) {
                Category category = new Category();
                category.setName(categoryName);
                category.setVirtualWiki(virtualWiki);
                category.setChildTopicName(rs.getString(DATA_TOPIC_NAME));
                category.setSortKey(rs.getString("sort_key"));
                category.setTopicType(rs.getInt("topic_type"));
                results.add(category);
            }

            DatabaseConnection.commit(status);
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return results;
    }

    /**
     *
     */
    public Topic lookupTopic(String virtualWiki, String topicName, boolean deleteOK, Object transactionObject) throws DataAccessException {
        if (StringUtils.isBlank(virtualWiki) || StringUtils.isBlank(topicName)) {
            return null;
        }
        String key = WikiCache.key(virtualWiki, topicName);
        if (transactionObject == null) {
            // retrieve topic from the cache only if this call is not currently a part
            // of a transaction to avoid retrieving data that might have been updated
            // as part of this transaction and would thus now be out of date
            Element cacheElement = WikiCache.retrieveFromCache(CACHE_TOPICS, key);
            if (cacheElement != null) {
                Topic cacheTopic = (Topic) cacheElement.getObjectValue();
                return (cacheTopic == null || (!deleteOK && cacheTopic.getDeleteDate() != null)) ? null : new Topic(cacheTopic);
            }
        }
        WikiLink wikiLink = LinkUtil.parseWikiLink(topicName);
        String namespace = wikiLink.getNamespace();
        boolean caseSensitive = true;
        if (namespace != null) {
            if (namespace.equals(NamespaceHandler.NAMESPACE_SPECIAL)) {
                // invalid namespace
                return null;
            }
            if (namespace.equals(NamespaceHandler.NAMESPACE_TEMPLATE) || namespace.equals(NamespaceHandler.NAMESPACE_USER) || namespace.equals(NamespaceHandler.NAMESPACE_CATEGORY)) {
                // user/template/category namespaces are case-insensitive
                caseSensitive = false;
            }
        }
        Topic topic = null;
        TransactionStatus status = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();
            int virtualWikiId = this.lookupVirtualWikiId(virtualWiki);
            rs = this.queryHandler().lookupTopic(virtualWikiId, topicName, caseSensitive, conn);
            if (rs.next()) {
                topic = initTopic(rs);
            }
            if (transactionObject == null) {
                // add topic to the cache only if it is not currently a part of a transaction
                // to avoid caching something that might need to be rolled back
                Topic cacheTopic = (topic == null) ? null : new Topic(topic);
                WikiCache.addToCache(CACHE_TOPICS, key, cacheTopic);
            }

            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return (topic == null || (!deleteOK && topic.getDeleteDate() != null)) ? null : topic;
    }

    /**
     * Return a count of all topics, including redirects, comments pages and templates,
     * currently available on the Wiki.  This method excludes deleted topics.
     *
     * @param virtualWiki The virtual wiki for which the total topic count is being returned
     *  for.
     */
    public int lookupTopicCount(String virtualWiki) throws DataAccessException {
        int virtualWikiId = this.lookupVirtualWikiId(virtualWiki);
        ResultSet rs = null;
        TransactionStatus status = null;
        Connection conn = null;
        int result = 0;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            rs = this.queryHandler().lookupTopicCount(virtualWikiId, conn);
            result = rs.getInt("topic_count");

            DatabaseConnection.commit(status);
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return result;
    }

    /** JAMWIKI-NEW
     * Return a List of topic names for all non-deleted topics in the
     * virtual wiki that match a specific topic type.
     *
     * @param virtualWiki The virtual wiki for the topics being queried.
     * @param topicType1 The type of topics to return.
     * @param topicType2 The type of topics to return.  Set to the same value
     *  as topicType1 if only one type is needed.
     * @param pagination A Pagination object indicating the total number of
     *  results and offset for the results to be retrieved.
     * @return A List of topic names for all non-deleted topics in the
     *  virtual wiki that match a specific topic type.
     * @throws DataAccessException Thrown if any error occurs during method execution.
     */
    public List<String> lookupTopicByType(String virtualWiki, int topicType1, int topicType2, Pagination pagination) throws DataAccessException{
        throw new DataAccessException("Not Implemented!");
    }
    

    /**
     *
     */
    public List<String> lookupTopicByType(String virtualWiki, int topicType, Pagination pagination) throws DataAccessException {
        List<String> results = new ArrayList<String>();
        int virtualWikiId = this.lookupVirtualWikiId(virtualWiki);
        TransactionStatus status = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            rs = this.queryHandler().lookupTopicByType(virtualWikiId, topicType, pagination, conn);
            while (rs.next()) {
                results.add(rs.getString(DATA_TOPIC_NAME));
            }

            DatabaseConnection.commit(status);
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return results;
    }

    // Daniel
    public Topic lookupTopicById(String virtualWiki, int topicId) throws DataAccessException {
        Element cacheElement = WikiCache.retrieveFromCache(CACHE_TOPICS, topicId);
        if (cacheElement != null) {
            return (Topic) cacheElement.getObjectValue();
        }

        int virtualWikiId = this.lookupVirtualWikiId(virtualWiki);

        Topic result = null;
        TransactionStatus status = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();
            rs = this.queryHandler().lookupTopicById(virtualWikiId, topicId, conn);
            if (rs.next()) {
                result = this.initTopic(rs);
            }

            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        WikiCache.addToCache(CACHE_TOPICS, topicId, result);
        return result;
    }

    public Topic lookupTopicMetaDataById(String virtualWiki, int topicId) throws DataAccessException {
        Element cacheElement = WikiCache.retrieveFromCache(CACHE_TOPICS, topicId);
        if (cacheElement != null) {
            return (Topic) cacheElement.getObjectValue();
        }

        int virtualWikiId = this.lookupVirtualWikiId(virtualWiki);

        Topic result = null;
        TransactionStatus status = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();
            rs = this.queryHandler().lookupTopicMetaDataById(virtualWikiId, topicId, conn);
            if (rs.next()) {
                result = this.initTopicMetaData(rs);
            }

            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        WikiCache.addToCache(CACHE_TOPICS, topicId, result);
        return result;
    }

    /**
     *
     */
    public TopicVersion lookupTopicVersion(int topicVersionId) throws DataAccessException {
        Element cacheElement = WikiCache.retrieveFromCache(CACHE_TOPIC_VERSIONS, topicVersionId);
        if (cacheElement != null) {
            return (TopicVersion) cacheElement.getObjectValue();
        }
        TopicVersion result = null;
        TransactionStatus status = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();
            rs = this.queryHandler().lookupTopicVersion(topicVersionId, conn);
            if (rs.next()) {
                result = this.initTopicVersion(rs);
            }

            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        WikiCache.addToCache(CACHE_TOPIC_VERSIONS, topicVersionId, result);
        return result;
    }

    /**
     *
     */
    public VirtualWiki lookupVirtualWiki(String virtualWikiName) throws DataAccessException {
        Element cacheElement = WikiCache.retrieveFromCache(CACHE_VIRTUAL_WIKI, virtualWikiName);
        if (cacheElement != null) {
            return (VirtualWiki) cacheElement.getObjectValue();
        }
        List<VirtualWiki> virtualWikis = this.getVirtualWikiList();
        for (VirtualWiki virtualWiki : virtualWikis) {
            if (virtualWiki.getName().equals(virtualWikiName)) {
                WikiCache.addToCache(CACHE_VIRTUAL_WIKI, virtualWikiName, virtualWiki);
                return virtualWiki;
            }
        }
        WikiCache.addToCache(CACHE_VIRTUAL_WIKI, virtualWikiName, null);
        return null;
    }

    /**
     *
     */
    private int lookupVirtualWikiId(String virtualWikiName) throws DataAccessException {
        VirtualWiki virtualWiki = this.lookupVirtualWiki(virtualWikiName);
        WikiCache.addToCache(CACHE_VIRTUAL_WIKI, virtualWikiName, virtualWiki);
        return (virtualWiki == null) ? -1 : virtualWiki.getVirtualWikiId();
    }

    /**
     *
     */
    private String lookupVirtualWikiName(int virtualWikiId) throws DataAccessException {
        Element cacheElement = WikiCache.retrieveFromCache(CACHE_VIRTUAL_WIKI, virtualWikiId);
        if (cacheElement != null) {
            VirtualWiki virtualWiki = (VirtualWiki) cacheElement.getObjectValue();
            return (virtualWiki == null) ? null : virtualWiki.getName();
        }
        List<VirtualWiki> virtualWikis = this.getVirtualWikiList();
        for (VirtualWiki virtualWiki : virtualWikis) {
            if (virtualWiki.getVirtualWikiId() == virtualWikiId) {
                WikiCache.addToCache(CACHE_VIRTUAL_WIKI, virtualWikiId, virtualWiki);
                return virtualWiki.getName();
            }
        }
        WikiCache.addToCache(CACHE_VIRTUAL_WIKI, virtualWikiId, null);
        return null;
    }

    /**
     *
     */
    public WikiFile lookupWikiFile(String virtualWiki, String topicName) throws DataAccessException {
        Topic topic = this.lookupTopic(virtualWiki, topicName, false, null);
        if (topic == null) {
            return null;
        }
        int virtualWikiId = this.lookupVirtualWikiId(virtualWiki);
        ResultSet rs = null;
        WikiFile wikiFile = null;
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            rs = this.queryHandler().lookupWikiFile(virtualWikiId, topic.getTopicId(), conn);
            if (rs.next()) {
                wikiFile = initWikiFile(rs);
            }

            DatabaseConnection.commit(status);
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return wikiFile;
    }

    /**
     * Return a count of all wiki files currently available on the Wiki.  This
     * method excludes deleted files.
     *
     * @param virtualWiki The virtual wiki of the files being retrieved.
     */
    public int lookupWikiFileCount(String virtualWiki) throws DataAccessException {
        int virtualWikiId = this.lookupVirtualWikiId(virtualWiki);
        TransactionStatus status = null;
        Connection conn = null;
        int result = 0;
        ResultSet rs = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            rs = this.queryHandler().lookupWikiFileCount(virtualWikiId, conn);
            result = rs.getInt("file_count");

            DatabaseConnection.commit(status);
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return result;
    }

    /**
     *
     */
    public WikiGroup lookupWikiGroup(String groupName) throws DataAccessException {
        ResultSet rs = null;
        WikiGroup wikiGroup = null;
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            rs = this.queryHandler().lookupWikiGroup(groupName, conn);
            if (rs.next()) {
                wikiGroup = initWikiGroup(rs);
            }

            DatabaseConnection.commit(status);
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }
        return wikiGroup;
    }

    /**
     *
     */
    public WikiUser lookupWikiUser(int userId) throws DataAccessException {
        WikiUser result = null;
        TransactionStatus status = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();
            rs = this.queryHandler().lookupWikiUser(userId, conn);
            if (rs.next()) {
                result = initWikiUser(rs);
            }

            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return result;
    }

    /**
     *
     */
    public WikiUser lookupWikiUser(String username) throws DataAccessException {
        WikiUser result = null;
        TransactionStatus status = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            rs = this.queryHandler().lookupWikiUser(username, conn);
            if (!rs.next()) {
                result = null;
            } else {
                int userId = rs.getInt(DATA_WIKI_USER_ID);
                result = lookupWikiUser(userId);
            }

            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally{
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return result;
    }

    /**
     * Return a count of all wiki users.
     */
    public int lookupWikiUserCount() throws DataAccessException {
        Connection conn = null;
        int rv;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            rs = this.queryHandler().lookupWikiUserCount(conn);
            rv = rs.getInt("user_count");

        } catch (SQLException e) {
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }
        return rv;
    }

    /**
     *
     */
    public String lookupWikiUserEncryptedPassword(String username) throws DataAccessException {
        TransactionStatus status = null;
        String result = null;
        Connection conn = null;
        ResultSet rs = null;

        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            rs = this.queryHandler().lookupWikiUserEncryptedPassword(username, conn);

            if (rs.next()) {
                result = rs.getString("password");
            }

            DatabaseConnection.commit(status);
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return result;
    }

    /**
     *
     */
    public List<String> lookupWikiUsers(Pagination pagination) throws DataAccessException {
        List<String> results = new ArrayList<String>();
        TransactionStatus status = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            rs = this.queryHandler().lookupWikiUsers(pagination, conn);
            while (rs.next()) {
                results.add(rs.getString("login"));
            }
            rs.close();
            DatabaseConnection.commit(status);
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return results;
    }

    /**
     *
     */
    public void moveTopic(Topic fromTopic, TopicVersion fromVersion, String destination) throws DataAccessException, WikiException {
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            if (!this.canMoveTopic(fromTopic, destination)) {
                throw new WikiException(new WikiMessage("move.exception.destinationexists", destination));
            }
            Topic toTopic = this.lookupTopic(fromTopic.getVirtualWiki(), destination, false, conn);
            boolean detinationExistsFlag = (toTopic != null && toTopic.getDeleteDate() == null);
            if (detinationExistsFlag) {
                // if the target topic is a redirect to the source topic then the
                // target must first be deleted.
                this.deleteTopic(toTopic, null, false);
            }
            // first rename the source topic with the new destination name
            String fromTopicName = fromTopic.getName();
            fromTopic.setName(destination);
            ParserOutput fromParserOutput = ParserUtil.parserOutput(fromTopic.getTopicContent(), fromTopic.getVirtualWiki(), fromTopic.getName());
            writeTopic(fromTopic, fromVersion, fromParserOutput.getCategories(), fromParserOutput.getLinks(), true);
            // now either create a new topic that is a redirect with the
            // source topic's old name, or else undelete the new topic and
            // rename.
            if (detinationExistsFlag) {
                // target topic was deleted, so rename and undelete
                toTopic.setName(fromTopicName);
                writeTopic(toTopic, null, null, null, false);
                this.undeleteTopic(toTopic, null, false);
            } else {
                // create a new topic that redirects to the destination
                toTopic = fromTopic;
                toTopic.setTopicId(-1);
                toTopic.setName(fromTopicName);
            }
            String content = ParserUtil.parserRedirectContent(destination);
            toTopic.setRedirectTo(destination);
            toTopic.setTopicType(Topic.TYPE_REDIRECT);
            toTopic.setTopicContent(content);
            TopicVersion toVersion = fromVersion;
            toVersion.setTopicVersionId(-1);
            toVersion.setVersionContent(content);
            ParserOutput toParserOutput = ParserUtil.parserOutput(toTopic.getTopicContent(), toTopic.getVirtualWiki(), toTopic.getName());
            writeTopic(toTopic, toVersion, toParserOutput.getCategories(), toParserOutput.getLinks(), true);

            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } catch (ParserException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } catch (WikiException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }

    }


    /** JAMWIKI-NEW
     * Utility method used when importing to updating the previous topic version ID field
     * of topic versions, as well as the current version ID field for the topic record.
     *
     * @param topic The topic record to update.
     * @param topicVersionIdList A list of all topic version IDs for the topic, sorted
     *  chronologically from oldest to newest.
     * @throws DataAccessException Thrown if any error occurs during method execution.
     */
    public void orderTopicVersions(Topic topic, List<Integer> topicVersionIdList) throws DataAccessException {
        throw new DataAccessException("Not Implemented!");
    }


    /**
     *
     * @return
     */
    protected QueryHandler queryHandler() {
        return this.queryHandler;
    }


    /** JAMWIKI-NEW
     * Delete all existing log entries and reload the log item table based
     * on the most recent topic versions, uploads, and user signups.
     *
     * @throws DataAccessException Thrown if any error occurs during method execution.
     */
    public void reloadLogItems() throws DataAccessException {
        throw new DataAccessException("Not Implemented!");
    }


    /**
     *
     */
    public void reloadRecentChanges() throws DataAccessException {
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();
            this.queryHandler().reloadRecentChanges(conn);
            DatabaseConnection.commit(status);
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            DatabaseConnection.closeConnection(conn);
        }

    }

    /**
     *
     */
    public void setup(Locale locale, WikiUser user, String username, String encryptedPassword) throws DataAccessException, WikiException {
        WikiDatabase.initialize();
        // determine if database exists
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            DatabaseConnection.executeQuery(WikiDatabase.getExistenceValidationQuery(), conn);
            DatabaseConnection.commit(status);
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            DatabaseConnection.closeConnection(conn);
        }

        WikiDatabase.setup(locale, user, username, encryptedPassword);
    }

    /**
     *
     */
    public void setupSpecialPages(Locale locale, WikiUser user, VirtualWiki virtualWiki) throws DataAccessException, WikiException {
        TransactionStatus status = null;
        try {
            status = DatabaseConnection.startTransaction();
            // create the default topics
            WikiDatabase.setupSpecialPage(locale, virtualWiki.getName(), WikiBase.SPECIAL_PAGE_STARTING_POINTS, user, false);
            WikiDatabase.setupSpecialPage(locale, virtualWiki.getName(), WikiBase.SPECIAL_PAGE_LEFT_MENU, user, true);
            WikiDatabase.setupSpecialPage(locale, virtualWiki.getName(), WikiBase.SPECIAL_PAGE_BOTTOM_AREA, user, true);
            WikiDatabase.setupSpecialPage(locale, virtualWiki.getName(), WikiBase.SPECIAL_PAGE_STYLESHEET, user, true);
            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } catch (WikiException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } finally {
            //DatabaseConnection.closeConnection(conn);
        }

    }

    /**
     *
     */
    public void undeleteTopic(Topic topic, TopicVersion topicVersion, boolean userVisible) throws DataAccessException, WikiException {
        TransactionStatus status = null;
        try {
            status = DatabaseConnection.startTransaction();
            // update topic to indicate deleted, add delete topic version.  if
            // topic has categories or other metadata then parser document is
            // also needed.
            ParserOutput parserOutput = ParserUtil.parserOutput(topic.getTopicContent(), topic.getVirtualWiki(), topic.getName());
            topic.setDeleteDate(null);
            this.writeTopic(topic, topicVersion, parserOutput.getCategories(), parserOutput.getLinks(), userVisible);
            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } catch (ParserException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } catch (WikiException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } finally {
            //DatabaseConnection.closeConnection(conn);
        }

    }


    /**
     * JAMWIKI-NEW
     */
    public void undeleteTopic(Topic topic, TopicVersion topicVersion) throws DataAccessException, WikiException {
        
        undeleteTopic(topic, topicVersion, true);
    }

    /**
     *
     */
    public void updateSpecialPage(Locale locale, String virtualWiki, String topicName, String userDisplay) throws DataAccessException, WikiException {
        logger.info("Updating special page " + virtualWiki + " / " + topicName);
        TransactionStatus status = null;
        Connection conn = null;
        try {

            String key = WikiCache.key(virtualWiki, topicName);
            WikiCache.removeFromCache(WikiBase.CACHE_PARSED_TOPIC_CONTENT, key);

            

            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();
            String contents = WikiUtil.readSpecialPage(locale, topicName);
            Topic topic = this.lookupTopic(virtualWiki, topicName, false, conn);
            int charactersChanged = StringUtils.length(contents) - StringUtils.length(topic.getTopicContent());
            topic.setTopicContent(contents);
            // FIXME - hard coding
            TopicVersion topicVersion = new TopicVersion(null, userDisplay, "Automatically updated by system upgrade", contents, charactersChanged);
            ParserOutput parserOutput = ParserUtil.parserOutput(topic.getTopicContent(), virtualWiki, topicName);
            writeTopic(topic, topicVersion, parserOutput.getCategories(), parserOutput.getLinks(), true);
            DatabaseConnection.commit(status);

            if(topic != null){
                ParsedTopic parsedTopic = new ParsedTopic();
                parsedTopic.setVirtualWiki(virtualWiki);
                parsedTopic.setTopicId(topic.getTopicId());
                parsedTopic.setCurrentVersionId(topic.getCurrentVersionId());
                this.deleteParsedTopic(parsedTopic);
            }
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (IOException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        //} catch (ParserException e) {
          //  DatabaseConnection.rollbackOnException(status, e);
          //  throw new DataAccessException(e);
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } catch (WikiException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }

    }

    /**
     *
     */
    public void updateTopic(Topic topic) throws DataAccessException, WikiException {
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            String key = WikiCache.key(topic.getVirtualWiki(), topic.getName());

            WikiCache.removeFromCache(WikiBase.CACHE_PARSED_TOPIC_CONTENT, key);
            WikiCache.removeFromCache(CACHE_TOPICS, key);

            conn = DatabaseConnection.getConnection();
            updateTopic(topic, conn);
            DatabaseConnection.commit(status);

             if(topic != null){
                ParsedTopic parsedTopic = new ParsedTopic();
                parsedTopic.setVirtualWiki(topic.getVirtualWiki());
                parsedTopic.setTopicId(topic.getTopicId());
                parsedTopic.setCurrentVersionId(topic.getCurrentVersionId());
                this.deleteParsedTopic(parsedTopic);
            }
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } catch (WikiException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }

    }

    /**
     *
     */
    private void updateTopic(Topic topic, Connection conn) throws DataAccessException, WikiException {
        int virtualWikiId = this.lookupVirtualWikiId(topic.getVirtualWiki());
        this.validateTopic(topic);
        try {
            this.queryHandler().updateTopic(topic, virtualWikiId, conn);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    public void updateTopic(Topic topic, TopicVersion topicVersion, LinkedHashMap<String, String> categories, List<String> links) throws DataAccessException, WikiException {

        updateTopic(topic, topicVersion, categories, links, false);
    }

    /**
     * Updates TopicVersion with support for Topic search indexing, and Topic cache update
     */
    public void updateTopic(Topic topic, TopicVersion topicVersion, LinkedHashMap<String, String> categories, List<String> links, boolean updateSearchIndex) throws DataAccessException, WikiException {
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            String key = WikiCache.key(topic.getVirtualWiki(), topic.getName());
            WikiCache.removeFromCache(WikiBase.CACHE_PARSED_TOPIC_CONTENT, key);
            WikiCache.removeFromCache(CACHE_TOPICS, key);

            conn = DatabaseConnection.getConnection();
            WikiUtil.validateTopicName(topic.getName());

            int topicId = topic.getTopicId();
            int topicVersionId = topicVersion.getTopicVersionId();

            if ((topicId <= 0) || (topicVersionId <= 0)) {
                throw new DataAccessException("Topic OR TopicVersion Ids not initialized!");
            } else {
                updateTopic(topic, conn);
                updateTopicVersion(topic, topicVersion, conn);
            }

            if (categories != null) {
                // add / remove categories associated with the topic
                this.deleteTopicCategories(topic, conn);
                for (String categoryName : categories.keySet()) {
                    Category category = new Category();
                    category.setName(categoryName);
                    category.setSortKey(categories.get(categoryName));
                    category.setVirtualWiki(topic.getVirtualWiki());
                    category.setChildTopicName(topic.getName());
                    this.addCategory(category, conn);
                }
            }
            DatabaseConnection.commit(status);
            if ((links != null) && (updateSearchIndex)) {
                WikiBase.getSearchEngine().deleteFromIndex(topic);
                WikiBase.getSearchEngine().addToIndex(topic, links);
            }

             if(topic != null){
                ParsedTopic parsedTopic = new ParsedTopic();
                parsedTopic.setVirtualWiki(topic.getVirtualWiki());
                parsedTopic.setTopicId(topic.getTopicId());
                parsedTopic.setCurrentVersionId(topic.getCurrentVersionId());
                this.deleteParsedTopic(parsedTopic);
            }
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } catch (WikiException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }

    }

    /**
     * Updates TopicVersion only, no support for search indexing, and no Topic cache update
     */
    public void updateTopicVersion(TopicVersion topicVersion, LinkedHashMap<String, String> categories, String topicName, String virtualWiki) throws DataAccessException, WikiException {
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            int virtualWikiId = this.lookupVirtualWikiId(virtualWiki);
            int topicId = topicVersion.getTopicId();
            int topicVersionId = topicVersion.getTopicVersionId();

            if ((topicId <= 0) || (topicVersionId <= 0)) {
                throw new DataAccessException("Topic OR TopicVersion Ids not initialized!");
            } else {
                updateTopicVersion(topicVersion, virtualWikiId, conn);
            }

            if (categories != null) {
                // add / remove categories associated with the topic
                this.deleteTopicCategories(topicId, conn);
                for (String categoryName : categories.keySet()) {
                    Category category = new Category();
                    category.setName(categoryName);
                    category.setSortKey(categories.get(categoryName));
                    category.setVirtualWiki(virtualWiki);
                    category.setChildTopicName(topicName);
                    this.addCategory(category, conn);
                }
            }
            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } catch (WikiException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }

    }

    public void importTopicVersion(TopicVersion topicVersion, LinkedHashMap<String, String> categories, String topicName, String virtualWiki, int virtualWikiId) throws DataAccessException, WikiException {

        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            int topicId = topicVersion.getTopicId();
            int topicVersionId = topicVersion.getTopicVersionId();

            if ((topicId <= 0) || (topicVersionId <= 0)) {
                throw new DataAccessException("Topic OR TopicVersion Ids not initialized!");
            } else {
                updateTopicVersion(topicVersion, virtualWikiId, conn);
            }

            if (categories != null) {
                // add / remove categories associated with the topic
                //this.deleteTopicCategories(topicId, conn);
                for (String categoryName : categories.keySet()) {
                    Category category = new Category();
                    category.setName(categoryName);
                    category.setSortKey(categories.get(categoryName));
                    category.setVirtualWiki(virtualWiki);
                    category.setChildTopicName(topicName);
                    this.addCategory(category, conn);
                }
            }
            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            throw e;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        } catch (WikiException e) {
            throw e;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }

    }

    public void buildTopicCategories(TopicVersion topicVersion, LinkedHashMap<String, String> categories, String topicName, int topicId, String virtualWiki) throws DataAccessException, WikiException {

        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            if (categories != null) {
                // add / remove categories associated with the topic
                //this.deleteTopicCategories(topicId, conn);
                for (String categoryName : categories.keySet()) {
                    Category category = new Category();
                    category.setName(categoryName);
                    category.setSortKey(categories.get(categoryName));
                    category.setVirtualWiki(virtualWiki);
                    category.setChildTopicName(topicName);
                    this.addCategory(category, topicId, conn);
                }
            }
            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            throw e;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        } catch (WikiException e) {
            throw e;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }

    }

    /**
     * Updates TopicVersion with no support for search indexing, and with Topic cache update
     */
    public void updateTopicVersion(Topic topic, TopicVersion topicVersion) throws DataAccessException, WikiException {
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();

            String key = WikiCache.key(topic.getVirtualWiki(), topic.getName());
            WikiCache.removeFromCache(WikiBase.CACHE_PARSED_TOPIC_CONTENT, key);
            WikiCache.removeFromCache(CACHE_TOPICS, key);
            
            conn = DatabaseConnection.getConnection();

            updateTopicVersion(topic, topicVersion, conn);
            DatabaseConnection.commit(status);

             if(topic != null){
                ParsedTopic parsedTopic = new ParsedTopic();
                parsedTopic.setVirtualWiki(topic.getVirtualWiki());
                parsedTopic.setTopicId(topic.getTopicId());
                parsedTopic.setCurrentVersionId(topic.getCurrentVersionId());
                this.deleteParsedTopic(parsedTopic);
            }
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } catch (WikiException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }

    }

    /**
     *
     */
    private void updateTopicVersion(Topic topic, TopicVersion topicVersion, Connection conn) throws DataAccessException, WikiException {
        int virtualWikiId = this.lookupVirtualWikiId(topic.getVirtualWiki());
        //this.validateTopic(topic);
        try {
            this.queryHandler().updateTopicVersion(topicVersion, virtualWikiId, conn);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    private void updateTopicVersion(TopicVersion topicVersion, int virtualWikiId, Connection conn) throws DataAccessException, WikiException {
        try {
            this.queryHandler().updateTopicVersion(topicVersion, virtualWikiId, conn);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    private void updateUserDetails(WikiUserDetails userDetails, Connection conn) throws DataAccessException, WikiException {
        this.validateUserDetails(userDetails);
        try {
            this.queryHandler().updateUserDetails(userDetails, conn);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    private void updateVirtualWiki(VirtualWiki virtualWiki, Connection conn) throws DataAccessException, WikiException {
        this.validateVirtualWiki(virtualWiki);
        try {
            this.queryHandler().updateVirtualWiki(virtualWiki, conn);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    private void updateWikiFile(WikiFile wikiFile, Connection conn) throws DataAccessException, WikiException {
        int virtualWikiId = this.lookupVirtualWikiId(wikiFile.getVirtualWiki());
        this.validateWikiFile(wikiFile);
        try {
            this.queryHandler().updateWikiFile(wikiFile, virtualWikiId, conn);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    private void updateWikiGroup(WikiGroup group, Connection conn) throws DataAccessException, WikiException {
        this.validateWikiGroup(group);
        try {
            this.queryHandler().updateWikiGroup(group, conn);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     *
     */
    private void updateWikiUser(WikiUser user, Connection conn) throws DataAccessException, WikiException {
        this.validateWikiUser(user);
        try {
            this.queryHandler().updateWikiUser(user, conn);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     *
     * @param role
     * @throws WikiException
     */
    protected void validateAuthority(String role) throws WikiException {
        checkLength(role, 30);
    }

    /**
     *
     * @param category
     * @throws WikiException
     */
    protected void validateCategory(Category category) throws WikiException {
        checkLength(category.getName(), 255);
        checkLength(category.getSortKey(), 255);
    }

    /**
     *
     * @param change
     * @throws WikiException
     */
    protected void validateRecentChange(RecentChange change) throws WikiException {
        checkLength(change.getTopicName(), 200);
        checkLength(change.getAuthorName(), 200);
        checkLength(change.getVirtualWiki(), 100);
        change.setEditComment(StringUtils.substring(change.getEditComment(), 0, 200));
    }

    /**
     *
     * @param role 
     * @throws WikiException
     */
    protected void validateRole(Role role) throws WikiException {
        checkLength(role.getAuthority(), 30);
        role.setDescription(StringUtils.substring(role.getDescription(), 0, 200));
    }

    /**
     *
     * @param topic 
     * @throws WikiException
     */
    protected void validateTopic(Topic topic) throws WikiException {
        checkLength(topic.getName(), 200);
        checkLength(topic.getRedirectTo(), 200);
    }

    /**
     *
     * @param topicVersion
     * @throws WikiException
     */
    protected void validateTopicVersion(TopicVersion topicVersion) throws WikiException {
        checkLength(topicVersion.getAuthorDisplay(), 100);
        topicVersion.setEditComment(StringUtils.substring(topicVersion.getEditComment(), 0, 200));
    }

    /**
     *
     * @param userDetails
     * @throws WikiException
     */
    protected void validateUserDetails(WikiUserDetails userDetails) throws WikiException {
        checkLength(userDetails.getUsername(), 100);
        // do not throw exception containing password info
        if (userDetails.getPassword() != null && userDetails.getPassword().length() > 100) {
            throw new WikiException(new WikiMessage("error.fieldlength", "-", "100"));
        }
    }

    /**
     *
     * @param virtualWiki 
     * @throws WikiException
     */
    protected void validateVirtualWiki(VirtualWiki virtualWiki) throws WikiException {
        checkLength(virtualWiki.getName(), 100);
        checkLength(virtualWiki.getDefaultTopicName(), 200);
    }

    /**
     *
     * @param topicName 
     * @throws WikiException
     */
    protected void validateWatchlistEntry(String topicName) throws WikiException {
        checkLength(topicName, 200);
    }

    /**
     *
     * @param wikiFile 
     * @throws WikiException
     */
    protected void validateWikiFile(WikiFile wikiFile) throws WikiException {
        checkLength(wikiFile.getFileName(), 200);
        checkLength(wikiFile.getUrl(), 200);
        checkLength(wikiFile.getMimeType(), 100);
    }

    /**
     *
     * @param wikiFileVersion 
     * @throws WikiException
     */
    protected void validateWikiFileVersion(WikiFileVersion wikiFileVersion) throws WikiException {
        checkLength(wikiFileVersion.getUrl(), 200);
        checkLength(wikiFileVersion.getMimeType(), 100);
        checkLength(wikiFileVersion.getAuthorDisplay(), 100);
        wikiFileVersion.setUploadComment(StringUtils.substring(wikiFileVersion.getUploadComment(), 0, 200));
    }

    /**
     *
     * @param group
     * @throws WikiException
     */
    protected void validateWikiGroup(WikiGroup group) throws WikiException {
        checkLength(group.getName(), 30);
        group.setDescription(StringUtils.substring(group.getDescription(), 0, 200));
    }

    /**
     *
     * @param user 
     * @throws WikiException
     */
    protected void validateWikiUser(WikiUser user) throws WikiException {
        checkLength(user.getUsername(), 100);
        checkLength(user.getDisplayName(), 100);
        checkLength(user.getCreateIpAddress(), 39);
        checkLength(user.getLastLoginIpAddress(), 39);
        checkLength(user.getDefaultLocale(), 8);
        checkLength(user.getEmail(), 100);
        checkLength(user.getEditor(), 50);
        checkLength(user.getSignature(), 255);
    }

    /**
     *
     */
    public void writeFile(WikiFile wikiFile, WikiFileVersion wikiFileVersion) throws DataAccessException, WikiException {
        TransactionStatus status = null;
        Connection conn = null;

        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();
            WikiUtil.validateTopicName(wikiFile.getFileName());
            if (wikiFile.getFileId() <= 0) {
                addWikiFile(wikiFile, conn);
            } else {
                updateWikiFile(wikiFile, conn);
            }
            wikiFileVersion.setFileId(wikiFile.getFileId());
            // write version
            addWikiFileVersion(wikiFileVersion, conn);
            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } catch (WikiException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }

    }

    /**
     *
     */
    public void writeRole(Role role, boolean update) throws DataAccessException, WikiException {
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();
            this.validateRole(role);
            if (update) {
                this.queryHandler().updateRole(role, conn);
            } else {
                this.queryHandler().insertRole(role, conn);
            }
            DatabaseConnection.commit(status);
            // FIXME - add caching
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } catch (WikiException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }

    }

    /**
     *
     */
    public void writeRoleMapGroup(int groupId, List<String> roles) throws DataAccessException, WikiException {
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();
            this.queryHandler().deleteGroupAuthorities(groupId, conn);
            for (String authority : roles) {
                this.validateAuthority(authority);
                this.queryHandler().insertGroupAuthority(groupId, authority, conn);
            }
            // refresh the current role requirements
            JAMWikiAuthenticationConfiguration.resetJamwikiAnonymousAuthorities();
            JAMWikiAuthenticationConfiguration.resetDefaultGroupRoles();
            DatabaseConnection.commit(status);
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } catch (WikiException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }

    }

    /**
     *
     */
    public void writeRoleMapUser(String username, List<String> roles) throws DataAccessException, WikiException {
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();
            this.queryHandler().deleteUserAuthorities(username, conn);
            for (String authority : roles) {
                this.validateAuthority(authority);
                this.queryHandler().insertUserAuthority(username, authority, conn);
            }
            DatabaseConnection.commit(status);
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } catch (WikiException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }

    }

    /** JAMWIKI-NEW
     * Add or update a Topic object.  This method will add a new record if
     * the Topic does not have a topic ID, otherwise it will perform an update.
     * A TopicVersion object will also be created to capture the author, date,
     * and other parameters for the topic.
     *
     * @param topic The Topic to add or update.  If the Topic does not have
     *  a topic ID then a new record is created, otherwise an update is
     *  performed.
     * @param topicVersion A TopicVersion containing the author, date, and
     *  other information about the version being added.  If this value is <code>null</code>
     *  then no version is saved and no recent change record is created.
     * @param categories A mapping of categories and their associated sort keys (if any)
     *  for all categories that are associated with the current topic.
     * @param links A List of all topic names that are linked to from the
     *  current topic.  These will be passed to the search engine to create
     *  searchable metadata.
     * @throws DataAccessException Thrown if any error occurs during method execution.
     * @throws WikiException Thrown if the topic information is invalid.
     */
    public void writeTopic(Topic topic, TopicVersion topicVersion, LinkedHashMap<String, String> categories, List<String> links) throws DataAccessException, WikiException{
        writeTopic(topic, topicVersion, categories, links, true, true);
    }


    /**
     * Commit changes to a topic (and its version) to the database or
     * filesystem.
     *
     * @param topic The topic object that is to be committed.  If the topic
     *  id is empty or less than zero then the topic is added, otherwise an
     *  update is performed.
     * @param topicVersion The version associated with the topic that is
     *  being added.  This parameter should never be null UNLESS the change is
     *  not user visible, such as when deleting a topic temporarily during
     *  page moves.
     * @param categories A mapping of categories and their associated sort keys (if any)
     *  for all categories that are associated with the current topic.
     * @param links A List of all topic names that are linked to from the
     *  current topic.  These will be passed to the search engine to create
     *  searchable metadata.
     * @param userVisible A flag indicating whether or not this change should
     *  be visible to Wiki users.  This flag should be true except in rare
     *  cases, such as when temporarily deleting a topic during page moves.
     */
    public void writeTopic(Topic topic, TopicVersion topicVersion, LinkedHashMap<String, String> categories, List<String> links, boolean userVisible) throws DataAccessException, WikiException {

        writeTopic(topic, topicVersion, categories, links, userVisible, true);
    }

    /**
     * Commit changes to a topic (and its version) to the database or
     * filesystem.
     *
     * @param topic The topic object that is to be committed.  If the topic
     *  id is empty or less than zero then the topic is added, otherwise an
     *  update is performed.
     * @param topicVersion The version associated with the topic that is
     *  being added.  This parameter should never be null UNLESS the change is
     *  not user visible, such as when deleting a topic temporarily during
     *  page moves.
     * @param categories A mapping of categories and their associated sort keys (if any)
     *  for all categories that are associated with the current topic.
     * @param links A List of all topic names that are linked to from the
     *  current topic.  These will be passed to the search engine to create
     *  searchable metadata.
     * @param userVisible A flag indicating whether or not this change should
     *  be visible to Wiki users.  This flag should be true except in rare
     *  cases, such as when temporarily deleting a topic during page moves.
     * @param updateSearchIndex A flag indicating whether or not this change should
     *  be synchronized with search index.  This flag should be true except in rare
     *  cases, such as when importing large number of pages.
     */
    public void writeTopic(Topic topic, TopicVersion topicVersion, LinkedHashMap<String, String> categories, List<String> links, boolean userVisible, boolean updateSearchIndex) throws DataAccessException, WikiException {
        TransactionStatus status = null;
        Connection conn = null;
        int topicId = -1;
        int topicVersionId = -1;

        try {
            status = DatabaseConnection.startTransaction();
            String key = WikiCache.key(topic.getVirtualWiki(), topic.getName());
            WikiCache.removeFromCache(WikiBase.CACHE_PARSED_TOPIC_CONTENT, key);
            WikiCache.removeFromCache(CACHE_TOPICS, key);
            
            conn = DatabaseConnection.getConnection();
            WikiUtil.validateTopicName(topic.getName());

            topicId = topic.getTopicId();

            if (topicId <= 0) {
                topicId = addTopic(topic, conn);
                logger.info("TOPIC-ID =>: " + topicId);
                topic.setTopicId(topicId);
            } else {
                updateTopic(topic, conn);
            }
            if (userVisible) {
                if (topicVersion.getPreviousTopicVersionId() == null && topic.getCurrentVersionId() != null) {
                    topicVersion.setPreviousTopicVersionId(topic.getCurrentVersionId());
                }

                topicVersion.setTopicId(topic.getTopicId());
                // write version
                topicVersionId = addTopicVersion(topicVersion, conn);
                topicVersion.setTopicVersionId(topicVersionId);
                String authorName = topicVersion.getAuthorDisplay();
                logger.info("TOPIC-VERSION-ID =>: " + topicVersionId);

                Integer authorId = topicVersion.getAuthorId();
                if (authorId != null) {
                    WikiUser user = this.lookupWikiUser(topicVersion.getAuthorId());
                    authorName = user.getUsername();
                }
                RecentChange change = new RecentChange(topic, topicVersion, authorName);
                this.addRecentChange(change, conn);
            }
            if (categories != null) {
                // add / remove categories associated with the topic
                this.deleteTopicCategories(topic, conn);
                for (String categoryName : categories.keySet()) {
                    Category category = new Category();
                    category.setName(categoryName);
                    category.setSortKey(categories.get(categoryName));
                    category.setVirtualWiki(topic.getVirtualWiki());
                    category.setChildTopicName(topic.getName());
                    this.addCategory(category, conn);
                }
            }
            DatabaseConnection.commit(status);
            if ((links != null) && (updateSearchIndex)) {
                WikiBase.getSearchEngine().deleteFromIndex(topic);
                WikiBase.getSearchEngine().addToIndex(topic, links);
            }

        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } catch (WikiException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }

    }

    /**
     *
     */
    public void writeVirtualWiki(VirtualWiki virtualWiki) throws DataAccessException, WikiException {
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();
            WikiUtil.validateTopicName(virtualWiki.getName());
            if (virtualWiki.getVirtualWikiId() <= 0) {
                this.addVirtualWiki(virtualWiki, conn);
            } else {
                this.updateVirtualWiki(virtualWiki, conn);
            }
            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } catch (WikiException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }

        // update the cache AFTER the commit
        WikiCache.removeFromCache(CACHE_VIRTUAL_WIKI, virtualWiki.getName());
        WikiCache.removeFromCache(CACHE_VIRTUAL_WIKI, virtualWiki.getVirtualWikiId());
        WikiCache.addToCache(CACHE_VIRTUAL_WIKI, virtualWiki.getName(), virtualWiki);
        WikiCache.addToCache(CACHE_VIRTUAL_WIKI, virtualWiki.getVirtualWikiId(), virtualWiki);
    }

    /**
     *
     */
    public void writeWatchlistEntry(Watchlist watchlist, String virtualWiki, String topicName, int userId) throws DataAccessException, WikiException {
        TransactionStatus status = null;
        Connection conn = null;

        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();
            int virtualWikiId = this.lookupVirtualWikiId(virtualWiki);
            String article = WikiUtil.extractTopicLink(topicName);
            String comments = WikiUtil.extractCommentsLink(topicName);
            if (watchlist.containsTopic(topicName)) {
                // remove from watchlist
                this.deleteWatchlistEntry(virtualWikiId, article, userId, conn);
                this.deleteWatchlistEntry(virtualWikiId, comments, userId, conn);
                watchlist.remove(article);
                watchlist.remove(comments);
            } else {
                // add to watchlist
                this.addWatchlistEntry(virtualWikiId, article, userId, conn);
                this.addWatchlistEntry(virtualWikiId, comments, userId, conn);
                watchlist.add(article);
                watchlist.add(comments);
            }
            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } catch (WikiException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }

    }

    /**
     *
     */
    public void writeWikiGroup(WikiGroup group) throws DataAccessException, WikiException {
        TransactionStatus status = null;
        Connection conn = null;

        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();
            if (group.getGroupId() <= 0) {
                this.addWikiGroup(group, conn);
            } else {
                this.updateWikiGroup(group, conn);
            }
            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } catch (WikiException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }

    }

    /**
     *
     */
    public void writeWikiUser(WikiUser user, String username, String encryptedPassword) throws DataAccessException, WikiException {
        WikiUtil.validateUserName(user.getUsername());
        TransactionStatus status = null;
        Connection conn = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();
            if (user.getUserId() <= 0) {
                WikiUserDetails userDetails = new WikiUserDetails(username, encryptedPassword, true, true, true, true, JAMWikiAuthenticationConfiguration.getDefaultGroupRoles());
                this.addUserDetails(userDetails, conn);
                this.addWikiUser(user, conn);
                // add all users to the registered user group
                this.addGroupMember(user.getUsername(), WikiBase.getGroupRegisteredUser().getGroupId(), conn);
            } else {
                if (!StringUtils.isBlank(encryptedPassword)) {
                    WikiUserDetails userDetails = new WikiUserDetails(username, encryptedPassword, true, true, true, true, JAMWikiAuthenticationConfiguration.getDefaultGroupRoles());
                    this.updateUserDetails(userDetails, conn);
                }
                this.updateWikiUser(user, conn);
            }
            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } catch (WikiException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        }finally {
            DatabaseConnection.closeConnection(conn);
        }
        
    }

    // EXPERIMENTAL
    public void deleteParsedTopic(ParsedTopic parsedTopic) throws DataAccessException, WikiException{

        TransactionStatus status = null;
        Connection conn = null;

        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            int virtualWikiId = this.lookupVirtualWikiId(parsedTopic.getVirtualWiki());
            this.queryHandler().deleteParsedTopic(virtualWikiId, parsedTopic.getTopicId(), parsedTopic.getCurrentVersionId(), conn);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    // EXPERIMENTAL
    public ParsedTopic lookupParsedTopic(String virtualWiki, String topicName, Object transactionObject) throws DataAccessException {

        ParsedTopic result = null;
        TransactionStatus status = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            int virtualWikiId = this.lookupVirtualWikiId(virtualWiki);
            rs = this.queryHandler().lookupParsedTopic(virtualWikiId, topicName, conn);
            // Grab the first result
            if (rs.next()) {
                result = this.initParsedTopic(rs);
            }
            rs.close();
            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return result;
    }

    // EXPERIMENTAL
    public ParsedTopic lookupParsedTopic(String virtualWiki, int topicId, int topicVersionId, Object transactionObject) throws DataAccessException {

        ParsedTopic result = null;
        TransactionStatus status = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            int virtualWikiId = this.lookupVirtualWikiId(virtualWiki);
            rs = this.queryHandler().lookupParsedTopic(virtualWikiId, topicId, topicVersionId, conn);
            if (rs.next()) {
                result = this.initParsedTopic(rs);
            }
            rs.close();
            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception ex){
                    logger.warning("Could not close ResultSet!", ex);
                }
            }
            DatabaseConnection.closeConnection(conn);
        }

        return result;
    }

    // EXPERIMENTAL
    public void writeParsedTopic(ParsedTopic parsedTopic) throws DataAccessException, WikiException {

        TransactionStatus status = null;
        Connection conn = null;
        int topicId = -1;

        try {
            status = DatabaseConnection.startTransaction();
            conn = DatabaseConnection.getConnection();

            topicId = addParsedTopic(parsedTopic, conn);
            logger.info("PARSED-TOPIC-ID =>: " + topicId);

            DatabaseConnection.commit(status);
        } catch (DataAccessException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } catch (SQLException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw new DataAccessException(e);
        } catch (WikiException e) {
            DatabaseConnection.rollbackOnException(status, e);
            throw e;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }
}
