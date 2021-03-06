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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.jamwiki.model.Category;
import org.jamwiki.model.Interwiki;
import org.jamwiki.model.LogItem;
import org.jamwiki.model.Namespace;
import org.jamwiki.model.RecentChange;
import org.jamwiki.model.Role;
import org.jamwiki.model.RoleMap;
import org.jamwiki.model.Topic;
import org.jamwiki.model.TopicType;
import org.jamwiki.model.TopicVersion;
import org.jamwiki.model.UserBlock;
import org.jamwiki.model.VirtualWiki;
import org.jamwiki.model.Watchlist;
import org.jamwiki.model.WikiFile;
import org.jamwiki.model.WikiFileVersion;
import org.jamwiki.model.WikiGroup;
import org.jamwiki.model.WikiUser;
import org.jamwiki.utils.Pagination;

/**
 * This interface provides all methods needed when retrieving or modifying
 * Wiki data.  Any database or other persistency class must implement
 * this interface, and there should also be a corresponding
 * &lt;data-handler&gt; entry for the class in the
 * <code>jamwiki-configuration.xml</code> file.
 *
 * @see org.jamwiki.WikiBase#getDataHandler
 */
public interface DataHandler {

	/** Ansi data handler class */
	public static final String DATA_HANDLER_ANSI = "org.jamwiki.db.AnsiDataHandler";
	/** DB2 data handler class */
	public static final String DATA_HANDLER_DB2 = "org.jamwiki.db.DB2DataHandler";
	/** DB2/400 data handler class */
	public static final String DATA_HANDLER_DB2400 = "org.jamwiki.db.DB2400DataHandler";
	/** HSql data handler class */
	public static final String DATA_HANDLER_HSQL = "org.jamwiki.db.HSqlDataHandler";
	/** MSSql data handler class */
	public static final String DATA_HANDLER_MSSQL = "org.jamwiki.db.MSSqlDataHandler";
	/** MySql data handler class */
	public static final String DATA_HANDLER_MYSQL = "org.jamwiki.db.MySqlDataHandler";
	/** Oracle data handler class */
	public static final String DATA_HANDLER_ORACLE = "org.jamwiki.db.OracleDataHandler";
	/** Postgres data handler class */
	public static final String DATA_HANDLER_POSTGRES = "org.jamwiki.db.PostgresDataHandler";
	/** Sybase ASA data handler class */
	public static final String DATA_HANDLER_ASA = "org.jamwiki.db.SybaseASADataHandler";
	/** Intersystems Cache data handler class */
	public static final String DATA_HANDLER_CACHE = "org.jamwiki.db.CacheDataHandler";

	/**
	 * Determine if a value matching the given username and password exists in
	 * the data store.
	 *
	 * @param username The username that is being validated against.
	 * @param password The password that is being validated against.
	 * @return <code>true</code> if the username / password combination matches
	 *  an existing record in the data store, <code>false</code> otherwise.
	 * @throws DataAccessException Thrown if an error occurs while accessing the data
	 *  store.
	 */
	boolean authenticate(String username, String password) throws DataAccessException;

	/**
	 * Determine if a topic can be moved to a new location.  If the
	 * destination is not an existing topic, is a topic that has been deleted,
	 * or is a topic that redirects to the source topic then this method
	 * should return <code>true</code>.
	 *
	 * @param fromTopic The Topic that is being moved.
	 * @param destination The new name for the topic.
	 * @return <code>true</code> if the topic can be moved to the destination,
	 *  <code>false</code> otherwise.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	boolean canMoveTopic(Topic fromTopic, String destination) throws DataAccessException;

	/**
	 * Delete an interwiki record from the interwiki table.
	 *
	 * @param interwiki The Interwiki record to be deleted.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	void deleteInterwiki(Interwiki interwiki) throws DataAccessException;

	/**
	 * Mark a topic deleted by setting its delete date to a non-null value.
	 * Prior to calling this method the topic content should also be set
	 * empty.  This method will also delete recent changes for the topic,
	 * and a new TopicVersion should be supplied reflecting the topic deletion
	 * event.
	 *
	 * @param topic The Topic object that is being deleted.
	 * @param topicVersion A TopicVersion object that indicates the delete
	 *  date, author, and other parameters for the topic.  If this value is
	 *  <code>null</code> then no version is saved, nor is any recent change
	 *  entry created.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 * @throws WikiException Thrown if the topic information is invalid.
	 */
	void deleteTopic(Topic topic, TopicVersion topicVersion) throws DataAccessException, WikiException;

	/**
	 * This method should be called only during upgrades and provides the capability
	 * to execute a SQL query from a QueryHandler-specific property file.
	 *
	 * @param prop The name of the SQL property file value to execute.
	 * @param conn The SQL connection to use when executing the SQL.
	 * @throws SQLException Thrown if any error occurs during execution.
	 */
	void executeUpgradeQuery(String prop, Connection conn) throws SQLException;

	/**
	 * This method should be called only during upgrades and provides the capability
	 * to execute update SQL from a QueryHandler-specific property file.
	 *
	 * @param prop The name of the SQL property file value to execute.
	 * @param conn The SQL connection to use when executing the SQL.
	 * @throws SQLException Thrown if any error occurs during execution.
	 */
	void executeUpgradeUpdate(String prop, Connection conn) throws SQLException;

	/**
	 * Return a List of all Category objects for a given virtual wiki.
	 *
	 * @param virtualWiki The virtual wiki for which categories are being
	 *  retrieved.
	 * @param pagination A Pagination object indicating the total number of
	 *  results and offset for the results to be retrieved.
	 * @return A List of all Category objects for a given virutal wiki.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	List<Category> getAllCategories(String virtualWiki, Pagination pagination) throws DataAccessException;

	/**
	 * Return a List of all Role objects for the wiki.
	 *
	 * @return A List of all Role objects for the wiki.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	List<Role> getAllRoles() throws DataAccessException;

	/**
	 * Return a List of all topic names for all topics that exist for
	 * the virtual wiki.
	 *
	 * @param virtualWiki The virtual wiki for which topics are being
	 *  retrieved.
	 * @param includeDeleted Set to <code>true</code> if deleted topics
	 *  should be included in the results.
	 * @return A List of all topic names for all non-deleted topics that
	 *  exist for the virtual wiki.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	List<String> getAllTopicNames(String virtualWiki, boolean includeDeleted) throws DataAccessException;

	/**
	 * Retrieve a List of all TopicVersions for a given topic, sorted
	 * chronologically.
	 *
	 * @param virtualWiki The virtual wiki for the topic being queried.
	 * @param topicName The name of the topic being queried.
	 * @param descending Set to <code>true</code> if the results should be
	 *  sorted with the most recent version first, <code>false</code> if the
	 *  results should be sorted with the oldest versions first.
	 * @return A List of all TopicVersion objects for the given topic.
	 *  If no matching topic exists then an exception is thrown.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	List<WikiFileVersion> getAllWikiFileVersions(String virtualWiki, String topicName, boolean descending) throws DataAccessException;

	/**
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
	public List<LogItem> getLogItems(String virtualWiki, int logType, Pagination pagination, boolean descending) throws DataAccessException;

	/**
	 * Retrieve a List of all RecentChange objects for a given virtual
	 * wiki, sorted chronologically.
	 *
	 * @param virtualWiki The virtual wiki for which recent changes are being
	 *  retrieved.
	 * @param pagination A Pagination object indicating the total number of
	 *  results and offset for the results to be retrieved.
	 * @param descending Set to <code>true</code> if the results should be
	 *  sorted with the most recent changes first, <code>false</code> if the
	 *  results should be sorted with the oldest changes first.
	 * @return A List of all RecentChange objects for a given virtual
	 *  wiki, sorted chronologically.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	List<RecentChange> getRecentChanges(String virtualWiki, Pagination pagination, boolean descending) throws DataAccessException;

	/**
	 * Retrieve a List of RoleMap objects for all users whose login
	 * contains the given login fragment.
	 *
	 * @param loginFragment A value that must be contained with the user's
	 *  login.  This method will return partial matches, so "name" will
	 *  match "name", "firstname" and "namesake".
	 * @return A list of RoleMap objects containing all roles for all
	 *  users whose login contains the login fragment.  If no matches are
	 *  found then this method returns an empty List.  This method will
	 *  never return <code>null</code>.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	List<RoleMap> getRoleMapByLogin(String loginFragment) throws DataAccessException;

	/**
	 * Retrieve a list of RoleMap objects for all users and groups who
	 * have been assigned the specified role.
	 *
	 * @param roleName The name of the role being queried against.
	 * @return A list of RoleMap objects containing all roles for all
	 *  users and groups who have been assigned the specified role.  If no
	 *  matches are found then this method returns an empty List.  This
	 *  method will never return <code>null</code>.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	List<RoleMap> getRoleMapByRole(String roleName) throws DataAccessException;

	/**
	 * Retrieve all roles assigned to a given group.
	 *
	 * @param groupName The name of the group for whom roles are being retrieved.
	 * @return An array of Role objects for the given group, or an empty
	 *  array if no roles are assigned to the group.  This method will
	 *  never return <code>null</code>.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	List<Role> getRoleMapGroup(String groupName) throws DataAccessException;

	/**
	 * Retrieve a list of RoleMap objects for all groups.
	 *
	 * @return A list of RoleMap objects containing all roles for all
	 *  groups.  If no matches are found then this method returns an empty
	 *  List.  This method will never return <code>null</code>.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	List<RoleMap> getRoleMapGroups() throws DataAccessException;

	/**
	 * Retrieve all roles assigned to a given user.
	 *
	 * @param login The login of the user for whom roles are being retrieved.
	 * @return A list of Role objects for the given user, or an empty
	 *  array if no roles are assigned to the user.  This method will
	 *  never return <code>null</code>.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	List<Role> getRoleMapUser(String login) throws DataAccessException;

	/**
	 * Retrieve a List of RecentChange objects representing a topic's history,
	 * sorted chronologically.
	 *
	 * @param topic The topic whose history is being retrieved.  Note that revisions
	 *  will be returned even if the topic is currently deleted.
	 * @param pagination A Pagination object indicating the total number of
	 *  results and offset for the results to be retrieved.
	 * @param descending Set to <code>true</code> if the results should be
	 *  sorted with the most recent changes first, <code>false</code> if the
	 *  results should be sorted with the oldest changes first.
	 * @return A List of all RecentChange objects representing a topic's history,
	 *  sorted chronologically.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	List<RecentChange> getTopicHistory(Topic topic, Pagination pagination, boolean descending) throws DataAccessException;

	/**
	 * Retrieve a List of topic names for all admin-only topics, sorted
	 * alphabetically.
	 *
	 * @param virtualWiki The virtual wiki for which admin-only topics are
	 *  being retrieved.
	 * @param pagination A Pagination object indicating the total number of
	 *  results and offset for the results to be retrieved.
	 * @return A List of topic names for all admin-only topics, sorted
	 *  alphabetically.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	List<String> getTopicsAdmin(String virtualWiki, Pagination pagination) throws DataAccessException;

	/**
	 * Return a map of all active user blocks, where the key is the ip or user id
	 * of the blocked user and the value is the UserBlock object.
	 *
	 * @return A map of all active user blocks, where the key is the ip or user id
	 * of the blocked user and the value is the UserBlock object.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	Map<Object, UserBlock> getUserBlocks() throws DataAccessException;

	/**
	 * Retrieve a List of RecentChange objects corresponding to all
	 * changes made by a particular user.
	 *
	 * @param virtualWiki The virtual wiki for which changes are being
	 *  retrieved.
	 * @param userString Either a user display, which is typically an IP
	 *  address (for anonymous users) or the user login corresponding to
	 *  the user for whom contributions are being retrieved.
	 * @param pagination A Pagination object indicating the total number of
	 *  results and offset for the results to be retrieved.
	 * @param descending Set to <code>true</code> if the results should be
	 *  sorted with the most recent changes first, <code>false</code> if the
	 *  results should be sorted with the oldest changes first.
	 * @return A List of RecentChange objects corresponding to all
	 *  changes made by a particular user.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	List<RecentChange> getUserContributions(String virtualWiki, String userString, Pagination pagination, boolean descending) throws DataAccessException;

	/**
	 * Return a List of all VirtualWiki objects that exist for the wiki.
	 *
	 * @return A List of all VirtualWiki objects that exist for the
	 *  wiki.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	List<VirtualWiki> getVirtualWikiList() throws DataAccessException;

	/**
	 * Retrieve a user's watchlist.
	 *
	 * @param virtualWiki The virtual wiki for which a watchlist is being
	 *  retrieved.
	 * @param userId The ID of the user whose watchlist is being retrieved.
	 * @return The Watchlist object for the user.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	Watchlist getWatchlist(String virtualWiki, int userId) throws DataAccessException;

	/**
	 * Retrieve a List of RecentChange objects corresponding to a user's
	 * watchlist.  This method is primarily used to display a user's watchlist
	 * on the Special:Watchlist page.
	 *
	 * @param virtualWiki The virtual wiki for which a watchlist is being
	 *  retrieved.
	 * @param userId The ID of the user whose watchlist is being retrieved.
	 * @param pagination A Pagination object indicating the total number of
	 *  results and offset for the results to be retrieved.
	 * @return A List of RecentChange objects corresponding to a user's
	 *  watchlist.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	List<RecentChange> getWatchlist(String virtualWiki, int userId, Pagination pagination) throws DataAccessException;

	/**
	 * Retrieve a List of Category objects corresponding to all topics
	 * that belong to the category, sorted by either the topic name, or
	 * category sort key (if specified).
	 *
	 * @param virtualWiki The virtual wiki for the category being queried.
	 * @param categoryName The name of the category being queried.
	 * @return A List of all Category objects corresponding to all
	 *  topics that belong to the category, sorted by either the topic name,
	 *  or category sort key (if specified).
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	List<Category> lookupCategoryTopics(String virtualWiki, String categoryName) throws DataAccessException;

	/**
	 * Return a map of key-value pairs corresponding to all configuration values
	 * currently set up for the system.
	 *
	 * @return A map of key-value pairs corresponding to all configuration values
	 * currently set up for the system.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	public Map<String, String> lookupConfiguration() throws DataAccessException;

	/**
	 * Given an interwiki prefix, return the Interwiki that corresponds to that prefix,
	 * or <code>null</code> if no match exists.
	 *
	 * @param interwikiPrefix The value to query to see if a matching interwiki record
	 *  exists.
	 * @return The matching Interwiki object, or <code>null</code> if no match is found.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	Interwiki lookupInterwiki(String interwikiPrefix) throws DataAccessException;

	/**
	 * Return all interwiki records currently available for the wiki.
	 *
	 * @return A list of all Interwiki records currently available for the wiki.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	List<Interwiki> lookupInterwikis() throws DataAccessException;

	/**
	 * Given a namespace string, return the namespace that corresponds to that string,
	 * or <code>null</code> if no match exists.
	 *
	 * @param virtualWiki The virtual wiki for the namespace being queried.
	 * @param namespaceString The value to query to see if a matching namespace exists.
	 * @return The matching Namespace object, or <code>null</code> if no match is found.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	Namespace lookupNamespace(String virtualWiki, String namespaceString) throws DataAccessException;

	/**
	 * Given a namespace ID return the corresponding namespace, or <code>null</code>
	 * if no match exists.
	 *
	 * @param namespaceId The ID for the namespace being retrieved.
	 * @return The matching Namespace object, or <code>null</code> if no match is found.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	Namespace lookupNamespaceById(int namespaceId) throws DataAccessException;

	/**
	 * Return all namespaces currently available for the wiki.
	 *
	 * @return A list of all Namespace objects currently available for the wiki.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	List<Namespace> lookupNamespaces() throws DataAccessException;

	/**
	 * Retrieve a Topic object that matches the given virtual wiki and topic
	 * name.  Note that when a shared image repository is in use this method
	 * should first try to retrieve images from the specified virtual wiki,
	 * but if that search fails then a second search should be performed
	 * against the shared repository.
	 *
	 * @param virtualWiki The virtual wiki for the topic being queried.
	 * @param topicName The name of the topic being queried.
	 * @param deleteOK Set to <code>true</code> if deleted topics can be
	 *  retrieved, <code>false</code> otherwise.
	 * @return A Topic object that matches the given virtual wiki and topic
	 *  name, or <code>null</code> if no matching topic exists.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	Topic lookupTopic(String virtualWiki, String topicName, boolean deleteOK) throws DataAccessException;

	/**
	 * Retrieve a Topic object that matches the given topic id and virtual wiki.  Note
	 * that this method can return deleted topics.
	 *
	 * @param virtualWiki The virtual wiki for the topic being queried.
	 * @param topicId The identifier of the topic being queried.
	 * @return A Topic object that matches the given virtual wiki and topic
	 * id, or <code>null</code> if no matching topic exists.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	Topic lookupTopicById(String virtualWiki, int topicId) throws DataAccessException;

	/**
	 * Return a count of all topics, including redirects, comments pages and
	 * templates, for the given virtual wiki.  Deleted topics are not included
	 * in the count.
	 *
	 * @param virtualWiki The virtual wiki for which the total topic count is
	 *  being returned.
	 * @param namespaceId An optional parameter to specify that results should only
	 *  be from the specified namespace.  If this value is <code>null</code> then
	 *  results will be returned from all namespaces.
	 * @return A count of all topics, including redirects, comments pages and
	 *  templates, for the given virtual wiki and (optionally) namespace.  Deleted
	 *  topics are not included in the count.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	int lookupTopicCount(String virtualWiki, Integer namespaceId) throws DataAccessException;

	/**
	 * Return a List of topic names for all non-deleted topics in the
	 * virtual wiki that match a specific topic type.
	 *
	 * @param virtualWiki The virtual wiki for the topics being queried.
	 * @param topicType1 The type of topics to return.
	 * @param topicType2 The type of topics to return.  Set to the same value
	 *  as topicType1 if only one type is needed.
	 * @param namespaceId An optional parameter to specify that results should only
	 *  be from the specified namespace.  If this value is <code>null</code> then
	 *  results will be returned from all namespaces.
	 * @param pagination A Pagination object indicating the total number of
	 *  results and offset for the results to be retrieved.
	 * @return A map of topic id and topic name for all non-deleted topics in the
	 *  virtual wiki that match a specific topic type.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	Map<Integer, String> lookupTopicByType(String virtualWiki, TopicType topicType1, TopicType topicType2, Integer namespaceId, Pagination pagination) throws DataAccessException;

	/**
	 * This method is used primarily to determine if a topic with a given name exists,
	 * taking as input a topic name and virtual wiki and returning the corresponding
	 * topic name, or <code>null</code> if no matching topic exists.  This method will
	 * return only non-deleted topics and performs better for cases where a caller only
	 * needs to know if a topic exists, but does not need a full Topic object.
	 *
	 * @param virtualWiki The virtual wiki for the topic being queried.
	 * @param topicName The name of the topic being queried.
	 * @return The name of the Topic object that matches the given virtual wiki and topic
	 * name, or <code>null</code> if no matching topic exists.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	String lookupTopicName(String virtualWiki, String topicName) throws DataAccessException;

	/**
	 * Retrieve a TopicVersion object for a given topic version ID.
	 *
	 * @param topicVersionId The ID of the topic version being retrieved.
	 * @return A TopicVersion object matching the given topic version ID,
	 *  or <code>null</code> if no matching topic version is found.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	TopicVersion lookupTopicVersion(int topicVersionId) throws DataAccessException;

	/**
	 * Retrieve the next topic version ID chronologically for a given topic
	 * version, or <code>null</code> if there is no next topic version ID.
	 *
	 * @param topicVersionId The ID of the topic version whose next topic version
	 *  ID is being retrieved.
	 * @return The next topic version ID chronologically for a given topic
	 * version, or <code>null</code> if there is no next topic version ID.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	Integer lookupTopicVersionNextId(int topicVersionId) throws DataAccessException;

	/**
	 * Find the names for all topics that link to a specified topic.
	 *
	 * @param virtualWiki The virtual wiki for the topic.
	 * @param topicName The name of the topic.
	 * @return A list of topic names for all topics that link to the
	 *  specified topic.  If no results are found then an empty list is
	 *  returned.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	List<String> lookupTopicLinks(String virtualWiki, String topicName) throws DataAccessException;

	/**
	 * Find the names for all un-linked topics in the main namespace.
	 *
	 * @param virtualWiki The virtual wiki to query against.
	 * @param namespaceId The ID for the namespace being retrieved.
	 * @return A list of topic names for all topics that are not linked to by
	 *  any other topic.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	List<String> lookupTopicLinkOrphans(String virtualWiki, int namespaceId) throws DataAccessException;

	/**
	 * Find any active user block for the given user or IP address.
	 *
	 * @param wikiUserId The wiki user ID, or <code>null</code> if the search is
	 *  by IP address.
	 * @param ipAddress The IP address, or <code>null</code> if the search is by
	 *  user ID.
	 * @return A currently-active user block for the ID or IP address, or
	 *  <code>null</code> if no block is currently active.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	UserBlock lookupUserBlock(Integer wikiUserId, String ipAddress) throws DataAccessException;

	/**
	 * Given a virtual wiki name, return the corresponding VirtualWiki object.
	 *
	 * @param virtualWikiName The name of the VirtualWiki object that is
	 *  being retrieved.
	 * @return The VirtualWiki object that corresponds to the virtual wiki
	 *  name being queried, or <code>null</code> if no matching VirtualWiki
	 *  can be found.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	VirtualWiki lookupVirtualWiki(String virtualWikiName) throws DataAccessException;

	/**
	 * Retrieve a WikiFile object for a given virtual wiki and topic name.
	 *
	 * @param virtualWiki The virtual wiki for the file being queried.
	 * @param topicName The topic name for the file being queried.
	 * @return The WikiFile object for the given virtual wiki and topic name,
	 *  or <code>null</code> if no matching WikiFile exists.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	WikiFile lookupWikiFile(String virtualWiki, String topicName) throws DataAccessException;

	/**
	 * Return a count of all wiki files for the given virtual wiki.  Deleted
	 * files are not included in the count.
	 *
	 * @param virtualWiki The virtual wiki for which the total file count is
	 *  being returned.
	 * @return A count of all wiki files for the given virtual wiki.  Deleted
	 *  files are not included in the count.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	int lookupWikiFileCount(String virtualWiki) throws DataAccessException;

	/**
	 * Retrieve a WikiGroup object for a given group name.
	 *
	 * @param groupName The group name for the group being queried.
	 * @return The WikiGroup object for the given group name, or
	 *  <code>null</code> if no matching group exists.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	WikiGroup lookupWikiGroup(String groupName) throws DataAccessException;

	/**
	 * Retrieve a WikiUser object matching a given user ID.
	 *
	 * @param userId The ID of the WikiUser being retrieved.
	 * @return The WikiUser object matching the given user ID, or
	 *  <code>null</code> if no matching WikiUser exists.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	WikiUser lookupWikiUser(int userId) throws DataAccessException;

	/**
	 * Retrieve a WikiUser object matching a given username.
	 *
	 * @param username The username of the WikiUser being retrieved.
	 * @return The WikiUser object matching the given username, or
	 *  <code>null</code> if no matching WikiUser exists.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	WikiUser lookupWikiUser(String username) throws DataAccessException;

	/**
	 * Return a count of all wiki users.
	 *
	 * @return A count of all wiki users.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	int lookupWikiUserCount() throws DataAccessException;

	/**
	 * Retrieve a WikiUser object matching a given username.
	 *
	 * @param username The username of the WikiUser being retrieved.
	 * @return The encrypted password for the given user name, or
	 *  <code>null</code> if no matching WikiUser exists.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	String lookupWikiUserEncryptedPassword(String username) throws DataAccessException;

	/**
	 * Return a List of user logins for all wiki users.
	 *
	 * @param pagination A Pagination object indicating the total number of
	 *  results and offset for the results to be retrieved.
	 * @return A List of user logins for all wiki users.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	List<String> lookupWikiUsers(Pagination pagination) throws DataAccessException;

	/**
	 * Move a topic to a new name, creating a redirect topic in the old
	 * topic location.  An exception will be thrown if the topic cannot be
	 * moved for any reason.
	 *
	 * @param fromTopic The Topic object that is being moved.
	 * @param destination The new name for the topic.
	 * @param user The WikiUser who will be credited in the topic version
	 *  associated with this topic move as having performed the move.
	 * @param ipAddress The IP address of the user making the topic move.
	 * @param moveComment The edit comment to associate with the topic
	 *  move.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 * @throws WikiException Thrown if the topic information is invalid.
	 */
	void moveTopic(Topic fromTopic, String destination, WikiUser user, String ipAddress, String moveComment) throws DataAccessException, WikiException;

	/**
	 * Utility method used when importing to updating the previous topic version ID field
	 * of topic versions, as well as the current version ID field for the topic record.
	 *
	 * @param topic The topic record to update.
	 * @param topicVersionIdList A list of all topic version IDs for the topic, sorted
	 *  chronologically from oldest to newest.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	void orderTopicVersions(Topic topic, List<Integer> topicVersionIdList) throws DataAccessException;

	/**
	 * Remove a topic version from the database.  This action deletes the record
	 * entirely, including references in other tables, and cannot be undone.
	 *
	 * @param virtualWiki The virtual wiki for the version being deleted.
	 * @param topicVersionId The ID of the topic version being deleted.
	 * @param user The WikiUser who will be credited in the log record
	 *  associated with this action.
	 * @param ipAddress The IP address of the user deleting the topic version.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	void purgeTopicVersion(String virtualWiki, int topicVersionId, WikiUser user, String ipAddress) throws DataAccessException, WikiException;

	/**
	 * Delete all existing log entries and reload the log item table based
	 * on the most recent topic versions, uploads, and user signups.
	 *
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	void reloadLogItems() throws DataAccessException;

	/**
	 * Delete all existing recent changes and reload the recent changes based
	 * on the most recent topic versions.
	 *
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	void reloadRecentChanges() throws DataAccessException;

	/**
	 * Perform any required setup steps for the DataHandler instance.
	 *
	 * @param locale The locale to be used when setting up the data handler
	 *  instance.  This parameter will affect any messages or defaults used
	 *  for the DataHandler.
	 * @param user The admin user to use when creating default topics and
	 *  other DataHandler parameters.
	 * @param username The admin user's username (login).
	 * @param encryptedPassword The admin user's encrypted password.  This value
	 *  is only required when creating a new admin user.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 * @throws WikiException Thrown if a setup failure occurs.
	 */
	void setup(Locale locale, WikiUser user, String username, String encryptedPassword) throws DataAccessException, WikiException;

	/**
	 * Create the special pages used on the wiki, such as the left menu and
	 * default stylesheet.
	 *
	 * @param locale The locale to be used when setting up special pages such
	 *  as the left menu and default stylesheet.  This parameter will affect
	 *  the language used when setting up these pages.
	 * @param user The admin user to use when creating the special pages.
	 * @param virtualWiki The VirtualWiki for which special pages are being
	 *  created.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 * @throws WikiException Thrown if a setup failure occurs.
	 */
	// FIXME - move this to another location
	void setupSpecialPages(Locale locale, WikiUser user, VirtualWiki virtualWiki) throws DataAccessException, WikiException;

	/**
	 * Undelete a previously deleted topic by setting its delete date to a
	 * null value.  Prior to calling this method the topic content should be
	 * restored to its previous value.  A new TopicVersion should be supplied
	 * reflecting the topic undeletion event.
	 *
	 * @param topic The Topic object that is being undeleted.
	 * @param topicVersion A TopicVersion object that indicates the undelete
	 *  date, author, and other parameters for the topic.  If this value is
	 *  <code>null</code> then no version is saved, nor is any recent change
	 *  entry created.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 * @throws WikiException Thrown if the topic information is invalid.
	 */
	void undeleteTopic(Topic topic, TopicVersion topicVersion) throws DataAccessException, WikiException;

	/**
	 * Update a special page used on the wiki, such as the left menu or
	 * default stylesheet.
	 *
	 * @param locale The locale to be used when updating a special page such
	 *  as the left menu and default stylesheet.  This parameter will affect
	 *  the language used when updating up the page.
	 * @param virtualWiki The VirtualWiki for which the special page are being
	 *  updated.
	 * @param topicName The name of the special page topic that is being
	 *  updated.
	 * @param userDisplay A display name for the user updating special pages,
	 *  typically the IP address.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 * @throws WikiException Thrown if the topic information is invalid.
	 */
	// FIXME - move this to another location
	void updateSpecialPage(Locale locale, String virtualWiki, String topicName, String userDisplay) throws DataAccessException, WikiException;

	/**
	 * Replace the existing configuration records with a new set of values.  This
	 * method will delete all existing records and replace them with the records
	 * specified.
	 *
	 * @param configuration A map of key-value pairs corresponding to the new
	 *  configuration information.  These values will replace all existing
	 *  configuration values in the system.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 * @throws WikiException Thrown if the configuration information is invalid.
	 */
	public void writeConfiguration(Map<String, String> configuration) throws DataAccessException, WikiException;

	/**
	 * Add or update a WikiFile object.  This method will add a new record if
	 * the WikiFile does not have a file ID, otherwise it will perform an update.
	 * A WikiFileVersion object will also be created to capture the author, date,
	 * and other parameters for the file.
	 *
	 * @param wikiFile The WikiFile to add or update.  If the WikiFile does not
	 *  have a file ID then a new record is created, otherwise an update is
	 *  performed.
	 * @param wikiFileVersion A WikiFileVersion containing the author, date, and
	 *  other information about the version being added.
	 * @param imageData Image data or null
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 * @throws WikiException Thrown if the file information is invalid.
	 */
	void writeFile(WikiFile wikiFile, WikiFileVersion wikiFileVersion, ImageData imageData) throws DataAccessException, WikiException;

	/**
	 * Add or update an Interwiki record.  This method will first delete any
	 * existing method with the same prefix and then add the new record.
	 *
	 * @param interwiki The Interwiki record to add or update.  If a record
	 *  already exists with the same prefix then that record will be deleted.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 * @throws WikiException Thrown if the interwiki information is invalid.
	 */
	void writeInterwiki(Interwiki interwiki) throws DataAccessException, WikiException;

	/**
	 * Add or update a namespace.  This method will add a new record if the
	 * namespace does not already exist, otherwise it will update the existing
	 * record.
	 *
	 * @param mainNamespace The namespace object to add to the database.
	 * @param commentsNamespace The comments namespace object to add to the database
	 *  for the corresponding main namespace.  This argument can be <code>null</code>
	 *  if there is no comments namespace.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 * @throws WikiException Thrown if the namespace information is invalid.
	 */
	void writeNamespace(Namespace mainNamespace, Namespace commentsNamespace) throws DataAccessException, WikiException;

	/**
	 * Add or update virtual-wiki specific labels for a namespace.  This method will
	 * remove existing records for the virtual wiki and add the new ones.
	 *
	 * @param namespaces The namespace translation records to add/update.
	 * @param virtualWiki The virtual wiki for which namespace translations are
	 *  being added or updated.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 * @throws WikiException Thrown if the namespace information is invalid.
	 */
	void writeNamespaceTranslations(List<Namespace> namespaces, String virtualWiki) throws DataAccessException, WikiException;

	/**
	 * Add or update a Role object.  This method will add a new record if
	 * the role does not yet exist, otherwise the role will be updated.
	 *
	 * @param role The Role to add or update.  If the Role does not yet
	 *  exist then a new record is created, otherwise an update is
	 *  performed.
	 * @param update A boolean value indicating whether this transaction is
	 *  updating an existing role or not.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 * @throws WikiException Thrown if the role information is invalid.
	 */
	// FIXME - the update flag should not be necessary
	void writeRole(Role role, boolean update) throws DataAccessException, WikiException;

	/**
	 * Add a set of group role mappings.  This method will first delete all
	 * existing role mappings for the specified group, and will then create
	 * a mapping for each specified role.
	 *
	 * @param groupId The group id for whom role mappings are being modified.
	 * @param roles A List of String role names for all roles that are
	 *  to be assigned to this group.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 * @throws WikiException Thrown if the role information is invalid.
	 */
	void writeRoleMapGroup(int groupId, List<String> roles) throws DataAccessException, WikiException;

	/**
	 * Add a set of user role mappings.  This method will first delete all
	 * existing role mappings for the specified user, and will then create
	 * a mapping for each specified role.
	 *
	 * @param username The username for whom role mappings are being modified.
	 * @param roles A List of String role names for all roles that are
	 *  to be assigned to this user.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 * @throws WikiException Thrown if the role information is invalid.
	 */
	void writeRoleMapUser(String username, List<String> roles) throws DataAccessException, WikiException;

	/**
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
	void writeTopic(Topic topic, TopicVersion topicVersion, LinkedHashMap<String, String> categories, List<String> links) throws DataAccessException, WikiException;

	/**
	 * This method exists for performance reasons for scenarios such as topic
	 * imports where many versions may be added without the need to update the
	 * topic record.  In general {@link #writeTopic} should be used instead.
	 *
	 * @param topic The Topic for the versions being added.  The topic must already
	 *  exist.
	 * @param topicVersions A list of TopicVersion objects, each containing the
	 *  author, date, and other information about the version being added.  If
	 *  this value is <code>null</code> or empty then no versions are saved.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 * @throws WikiException Thrown if the topic version information is invalid.
	 */
	public void writeTopicVersions(Topic topic, List<TopicVersion> topicVersions) throws DataAccessException, WikiException;

	/**
	 * Add or update a user block.  This method will add a new record if the
	 * UserBlock object does not have an ID, otherwise it will perform an
	 * update.
	 *
	 * @param userBlock The UserBlock record to add or update.  If the
	 *  UserBlock does not have an ID then a new record is created, otherwise
	 *  an update is performed.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 * @throws WikiException Thrown if the user block information is invalid.
	 */
	public void writeUserBlock(UserBlock userBlock) throws DataAccessException, WikiException;

	/**
	 * Add or update a VirtualWiki object.  This method will add a new record
	 * if the VirtualWiki does not have a virtual wiki ID, otherwise it will
	 * perform an update.
	 *
	 * @param virtualWiki The VirtualWiki to add or update.  If the
	 *  VirtualWiki does not have a virtual wiki ID then a new record is
	 *  created, otherwise an update is performed.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 * @throws WikiException Thrown if the virtual wiki information is invalid.
	 */
	void writeVirtualWiki(VirtualWiki virtualWiki) throws DataAccessException, WikiException;

	/**
	 * Add or delete an item from a user's watchlist.  If the topic is
	 * already in the user's watchlist it will be deleted, otherwise it will
	 * be added.
	 *
	 * @param watchlist The user's current Watchlist.
	 * @param virtualWiki The virtual wiki name for the current virtual wiki.
	 * @param topicName The name of the topic being added or removed from
	 *  the watchlist.
	 * @param userId The ID of the user whose watchlist is being updated.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 * @throws WikiException Thrown if the watchlist information is invalid.
	 */
	void writeWatchlistEntry(Watchlist watchlist, String virtualWiki, String topicName, int userId) throws DataAccessException, WikiException;

	/**
	 * Add or update a WikiGroup object.  This method will add a new record if
	 * the group does not have a group ID, otherwise it will perform an update.
	 *
	 * @param group The WikiGroup to add or update.  If the group does not have
	 *  a group ID then a new record is created, otherwise an update is
	 *  performed.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 * @throws WikiException Thrown if the group information is invalid.
	 */
	void writeWikiGroup(WikiGroup group) throws DataAccessException, WikiException;

	/**
	 * Add or update a WikiUser object.  This method will add a new record
	 * if the WikiUser does not have a user ID, otherwise it will perform an
	 * update.
	 *
	 * @param user The WikiUser being added or updated.  If the WikiUser does
	 *  not have a user ID then a new record is created, otherwise an update
	 *  is performed.
	 * @param username The user's username (login).
	 * @param encryptedPassword The user's encrypted password.  Required only when the
	 *  password is being updated.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 * @throws WikiException Thrown if the user information is invalid.
	 */
	void writeWikiUser(WikiUser user, String username, String encryptedPassword) throws DataAccessException, WikiException;

	/**
	 * Add new image or other data to database.
	 *
	 * @param imageData The image and it's arrtibutes to store.
	 * @param resized Must be true when inserting resized version of image and false otherwise.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	public void insertImage(ImageData imageData, boolean resized) throws DataAccessException;

	/**
	 * Get info of latest version of image.
	 *
	 * @param fileId File identifier.
	 * @param resized Image width or zero for original.
	 * @return The image info or null if image not found. Result's width and height components must
	 * be negative when data are not an image. Result's data component may be null.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	public ImageData getImageInfo(int fileId, int resized) throws DataAccessException;

	/**
	 * Get latest version of image.
	 *
	 * @param fileId File identifier.
	 * @param resized Image width or zero for original.
	 * @return The image data or null if image not found.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	public ImageData getImageData(int fileId, int resized) throws DataAccessException;

	/**
	 * Get desired version of image.
	 *
	 * @param fileVersionId File identifier.
	 * @param resized Image width or zero for original.
	 * @return The image data or null if image version not found.
	 * @throws DataAccessException Thrown if any error occurs during method execution.
	 */
	public ImageData getImageVersionData(int fileVersionId, int resized) throws DataAccessException;
}
