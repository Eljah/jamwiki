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
import java.util.List;
import org.jamwiki.authentication.WikiUserDetails;
import org.jamwiki.model.Category;
import org.jamwiki.model.LogItem;
import org.jamwiki.model.RecentChange;
import org.jamwiki.model.Role;
import org.jamwiki.model.RoleMap;
import org.jamwiki.model.Topic;
import org.jamwiki.model.TopicVersion;
import org.jamwiki.model.VirtualWiki;
import org.jamwiki.model.WikiFile;
import org.jamwiki.model.WikiFileVersion;
import org.jamwiki.model.WikiGroup;
import org.jamwiki.model.WikiUser;
import org.jamwiki.utils.Pagination;

/**
 * This interface provides all methods needed for retrieving, inserting, or updating
 * data from the database.
 */
public interface QueryHandler {

	/**
	 * Retrieve a result set containing all user information for a given WikiUser.
	 *
	 * @param login The login of the user record being retrieved.
	 * @param encryptedPassword The encrypted password for the user record being
	 *  retrieved.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @return <code>true</code> if the login and password matches an existing
	 *  user, <code>false</code> otherwise.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	boolean authenticateUser(String login, String encryptedPassword, Connection conn) throws SQLException;

	/**
	 * Some databases support automatically incrementing primary key values without the
	 * need to explicitly specify a value, thus improving performance.  This method provides
	 * a way for the a query handler to specify whether or not auto-incrementing is supported.
	 *
	 * @return <code>true</code> if the query handler supports auto-incrementing primary keys.
	 */
	boolean autoIncrementPrimaryKeys();

	/**
	 * Returns the simplest possible query that can be used to validate
	 * whether or not a database connection is valid.  Note that the query
	 * returned MUST NOT query any JAMWiki tables since it will be used prior
	 * to setting up the JAMWiki tables.
	 *
	 * @return Returns a simple query that can be used to validate a database
	 *  connection.
	 */
	String connectionValidationQuery();

	/**
	 * Method called to set up all JAMWiki system tables, indexes, and other
	 * required database objects.  If a failure occurs during object creation
	 * then this method will not attempt to clean up any objects that were
	 * created prior to the failure.
	 *
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void createTables(Connection conn) throws SQLException;

	/**
	 * Delete all authorities for a specific group.
	 *
	 * @param groupId The group id for which authorities are being deleted.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void deleteGroupAuthorities(int groupId, Connection conn) throws SQLException;

	/**
	 * Delete all records from the recent changes table for a specific topic.
	 *
	 * @param topicId The topic id for which recent changes are being deleted.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void deleteRecentChanges(int topicId, Connection conn) throws SQLException;

	/**
	 * Delete all categories associated with a topic.
	 *
	 * @param topicId The topic for which category association records are being
	 *  deleted.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void deleteTopicCategories(int topicId, Connection conn) throws SQLException;

	/**
	 * Delete all authorities for a specific user.
	 *
	 * @param username The username for which authorities are being deleted.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void deleteUserAuthorities(String username, Connection conn) throws SQLException;

	/**
	 * Delete a user's watchlist entry using the topic name to determine which
	 * entry to remove.
	 *
	 * @param virtualWikiId The id of the virtual wiki for which the watchlist
	 *  entry is being deleted.
	 * @param topicName The topic name for which the watchlist entry is being
	 *  deleted.
	 * @param userId The user for which the watchlist entry is being deleted.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void deleteWatchlistEntry(int virtualWikiId, String topicName, int userId, Connection conn) throws SQLException;

	/**
	 * Drop all JAMWiki database objects.  This method drops tables, indexes, and
	 * any database objects, as well as all data in those objects.  Note that if
	 * a failure occurs while deleting any one object the method will continue
	 * trying to delete any remaining objects.
	 *
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 */
	void dropTables(Connection conn);

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
	 * Return a simple query, that if successfully run indicates that JAMWiki
	 * tables have been initialized in the database.
	 *
	 * @return Returns a simple query that, if successfully run, indicates
	 *  that JAMWiki tables have been set up in the database.
	 */
	String existenceValidationQuery();

	/**
	 * Retrieve a list of all topic names that exist for a virtual wiki. This method
	 * will not return the names of previously deleted topics.
	 *
	 * @param virtualWikiId The id of the virtual wiki for which topic names
	 *  are being retrieved.
	 * @return A list of the names of all topics for the virtual wiki, not including
	 *  any previously deleted topics.  If no topics are found an empty list is
	 *  returned.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	List<String> getAllTopicNames(int virtualWikiId) throws SQLException;

	/**
	 * Retrieve a list of all wiki file version information for a given wiki file.
	 * Version information is sorted by wiki file version id, which in effect sorts
	 * the wiki file versions from newest to oldest.
	 *
	 * @param wikiFile A WikiFile object for which version information is to be retrieved.
	 * @param descending If <code>true</code> then results are sorted newest to
	 *  oldest.
	 * @return A list of all wiki file versions for the file, or an empty list if no
	 *  versions exist.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	List<WikiFileVersion> getAllWikiFileVersions(WikiFile wikiFile, boolean descending) throws SQLException;

	/**
	 * Retrieve a list of all categories associated with a particular virtual wiki.  The
	 * list may be limited by specifying the number of results to retrieve in a Pagination
	 * object.
	 *
	 * @param virtualWikiId The virtual wiki id for the virtual wiki from which all
	 *  categories are to be retrieved.
	 * @param virtualWikiName The name of the virtual wiki for which results are being
	 *  retrieved.
	 * @param pagination A Pagination object that specifies the number of results
	 *  and starting result offset for the result set to be retrieved.
	 * @return A list of all categories associated with a particular virtual wiki, or
	 *  an empty list if no categories exist for the virtual wiki.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	List<Category> getCategories(int virtualWikiId, String virtualWikiName, Pagination pagination) throws SQLException;

	/**
	 * Retrieve a list of all recent log items for a specific virtual wiki.
	 *
	 * @param virtualWikiId The id of the virtual wiki for which log items
	 *  are being retrieved.
	 * @param virtualWikiName The name of the virtual wiki for which results are being
	 *  retrieved.
	 * @param logType Set to <code>-1</code> if all log items should be returned,
	 *  otherwise set the log type for items to retrieve.
	 * @param pagination A Pagination object that specifies the number of results
	 *  and starting result offset for the result set to be retrieved.
	 * @param descending If <code>true</code> then results are sorted newest to
	 *  oldest.
	 * @return A list of LogItems, or an empty list if no log items are available.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	public List<LogItem> getLogItems(int virtualWikiId, String virtualWikiName, int logType, Pagination pagination, boolean descending) throws SQLException;

	/**
	 * Retrieve a list of all recent changes made to the wiki for a
	 * specific virtual wiki.
	 *
	 * @param virtualWiki The name of the virtual wiki for which results are being
	 *  retrieved.
	 * @param pagination A Pagination object that specifies the number of results
	 *  and starting result offset for the result set to be retrieved.
	 * @param descending If <code>true</code> then results are sorted newest to
	 *  oldest.
	 * @return A list of recent change results for the virtual wiki and pagination,
	 *  or an empty list if no results are found.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	List<RecentChange> getRecentChanges(String virtualWiki, Pagination pagination, boolean descending) throws SQLException;

	/**
	 * Retrieve a list of user ids, group ids and role names for all users whose
	 * login contains the given login fragment.
	 *
	 * @param loginFragment A value that must be contained with the user's
	 *  login.  This method will return partial matches, so "name" will
	 *  match "name", "firstname" and "namesake".
	 * @return A list of user ids, group ids and role names for all users whose
	 *  login contains the login fragment.  If no matches are found then this
	 *  method returns an empty list.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	List<RoleMap> getRoleMapByLogin(String loginFragment) throws SQLException;

	/**
	 * Retrieve a list of user ids, group ids and role names for all users and
	 * groups who have been assigned the specified role.
	 *
	 * @param authority The name of the role being queried against.
	 * @return A list of user ids, group ids and role names for all users and
	 *  groups who have been assigned the specified role, or an empty list if
	 *  no matches are found.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	List<RoleMap> getRoleMapByRole(String authority) throws SQLException;

	/**
	 * Retrieve a list of all roles assigned to a given group.
	 *
	 * @param groupName The name of the group for whom roles are being retrieved.
	 * @return A list of roles for the given group, or an empty list if no roles
	 *  are assigned to the group.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	List<Role> getRoleMapGroup(String groupName) throws SQLException;

	/**
	 * Retrieve a list of user ids, group ids and role names for all groups that
	 * have been assigned a role.
	 *
	 * @return A list of user ids, group ids and role names for all groups that
	 *  have been assigned a role.  If no matches are found then this method
	 *  returns an empty list.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	List<RoleMap> getRoleMapGroups() throws SQLException;

	/**
	 * Retrieve a list of all roles assigned to a given user.
	 *
	 * @param login The login of the user for whom roles are being retrieved.
	 * @return A list of roles for the given user, or an empty list if no roles
	 *  are assigned to the user.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	List<Role> getRoleMapUser(String login) throws SQLException;

	/**
	 * Retrieve a list of all roles that have been defined for the wiki.
	 *
	 * @return Returns a list of all roles that have been defined for the wiki,
	 *  or an empty list if no roles exist.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	List<Role> getRoles() throws SQLException;

	/**
	 * Retrieve a list of all history for a specific topic.
	 *
	 * @param topicId The id of the topic for which recent changes are being
	 *  retrieved.
	 * @param pagination A Pagination object that specifies the number of results
	 *  and starting result offset for the result set to be retrieved.
	 * @param descending If <code>true</code> then results are sorted newest to
	 *  oldest.
	 * @return A list of recent change objects, or an empty list if not topic
	 *  history exists.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	List<RecentChange> getTopicHistory(int topicId, Pagination pagination, boolean descending) throws SQLException;

	/**
	 * Retrieve a list containing the topic names of all admin-only topics for
	 * the virtual wiki.
	 *
	 * @param virtualWikiId The id of the virtual wiki for which topic names
	 *  are being retrieved.
	 * @param pagination A Pagination object that specifies the number of results
	 *  and starting result offset for the result set to be retrieved.
	 * @return A list containing the topic names of all admin-only topics for the
	 *  virtual wiki, or an empty list if there are no admin-only topics.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	List<String> getTopicsAdmin(int virtualWikiId, Pagination pagination) throws SQLException;

	/**
	 * Retrieve a list of all recent changes made to the wiki by a specific user.
	 *
	 * @param virtualWiki The name of the virtual wiki for which user contributions
	 *  are being retrieved.
	 * @param login The login of the user for whom changes are being retrieved.
	 * @param pagination A Pagination object that specifies the number of results
	 *  and starting result offset for the result set to be retrieved.
	 * @param descending If <code>true</code> then results are sorted newest to
	 *  oldest.
	 * @return A list of recent changes corresponding to the user's contributions,
	 *  or an empty list if no contributions are found.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	List<RecentChange> getUserContributionsByLogin(String virtualWiki, String login, Pagination pagination, boolean descending) throws SQLException;

	/**
	 * Retrieve a list of all recent changes made to the wiki by searching for matches
	 * against the user display field.  This method is typically used to retrieve
	 * contributions made by anonymous users.
	 *
	 * @param virtualWiki The name of the virtual wiki for which user contributions
	 *  are being retrieved.
	 * @param userDisplay The display name of the user, typically the IP address,
	 *  for whom changes are being retrieved.
	 * @param pagination A Pagination object that specifies the number of results
	 *  and starting result offset for the result set to be retrieved.
	 * @param descending If <code>true</code> then results are sorted newest to
	 *  oldest.
	 * @return A list of recent changes corresponding to the user's contributions,
	 *  or an empty list if no contributions are found.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	List<RecentChange> getUserContributionsByUserDisplay(String virtualWiki, String userDisplay, Pagination pagination, boolean descending) throws SQLException;

	/**
	 * Retrieve a list of all virtual wiki information for all virtual wikis.
	 *
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @return Returns a list of VirtualWiki objects for every virtual wiki or an
	 *  empty list if no virtual wikis are found.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	List<VirtualWiki> getVirtualWikis(Connection conn) throws SQLException;

	/**
	 * Retrieve a list of topic names for topics in the user's watchlist.
	 *
	 * @param virtualWikiId The virtual wiki ID for the virtual wiki for the
	 *  watchlist topics.
	 * @param userId The user ID for the user retrieving the watchlist.
	 * @return A list of topic names for topics in the user's watchlist.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	List<String> getWatchlist(int virtualWikiId, int userId) throws SQLException;

	/**
	 * Retrieve a list of all recent changes for topics in the user's watchlist.
	 *
	 * @param virtualWikiId The virtual wiki ID for the virtual wiki for the
	 *  watchlist topics.
	 * @param userId The user ID for the user retrieving the watchlist.
	 * @param pagination A Pagination object that specifies the number of results
	 *  and starting result offset for the result set to be retrieved.
	 * @return A list of recent changes for the watchlist, or an empty list if
	 *  there are no entries in the watchlist.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	List<RecentChange> getWatchlist(int virtualWikiId, int userId, Pagination pagination) throws SQLException;

	/**
	 * Add a new category record to the database.  Note that this method will fail
	 * if an existing category of the same name is already associated with the
	 * topic.
	 *
	 * @param category The category record that is being created.
	 * @param virtualWikiId The virtual wiki id for the record that is being added.
	 * @param topicId The ID of the topic record to which this category is being added.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void insertCategory(Category category, int virtualWikiId, int topicId, Connection conn) throws SQLException;

	/**
	 * Add a new authority for a specified group.  The group must not already have
	 * this authority or else an error will be thrown.
	 *
	 * @param groupId The group id for the group being assigned a role, or -1
	 *  if a user is being assigned a role.
	 * @param authority The authority being assigned.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void insertGroupAuthority(int groupId, String authority, Connection conn) throws SQLException;

	/**
	 * Add a user to a group.
	 *
	 * @param username The username for the user being added to the group.
	 * @param groupId The group ID for the group.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void insertGroupMember(String username, int groupId, Connection conn) throws SQLException;

	/**
	 * Add a new log item record to the database.
	 *
	 * @param logItem The LogItem record that is to be added to the database.
	 * @param virtualWikiId The virtual wiki id for the record that is being added.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void insertLogItem(LogItem logItem, int virtualWikiId, Connection conn) throws SQLException;

	/**
	 * Add a new recent change record to the database.
	 *
	 * @param change The RecentChange record that is to be added to the database.
	 * @param virtualWikiId The virtual wiki id for the record that is being added.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void insertRecentChange(RecentChange change, int virtualWikiId, Connection conn) throws SQLException;

	/**
	 * Add a new role record to the database.  The role must not already exist
	 * in the database or else an error will be thrown.
	 *
	 * @param role The Role record that is to be added to the database.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void insertRole(Role role, Connection conn) throws SQLException;

	/**
	 * Add a new topic record to the database.  The topic must not already exist
	 * in the database or else an error will be thrown.
	 *
	 * @param topic The Topic record that is to be added to the database.
	 * @param virtualWikiId The virtual wiki id for the record that is being added.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void insertTopic(Topic topic, int virtualWikiId, Connection conn) throws SQLException;

	/**
	 * Add a new topic version record to the database.  The topic version must
	 * not already exist in the database or else an error will be thrown.
	 *
	 * @param topicVersion The TopicVersion record that is to be added to the database.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void insertTopicVersion(TopicVersion topicVersion, Connection conn) throws SQLException;

	/**
	 * Add a new user authentication credential to the database.  The user authentication
	 * credential must not already exist in the database or else an error will be thrown.
	 *
	 * @param userDetails The user authentication credential that is to be added to the database.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void insertUserDetails(WikiUserDetails userDetails, Connection conn) throws SQLException;

	/**
	 * Add a new authority for a specified user.  The user must not already have
	 * this authority or else an error will be thrown.
	 *
	 * @param username The username for the user being assigned a role, or null
	 *  if a group is being assigned a role.
	 * @param authority The authority being assigned.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void insertUserAuthority(String username, String authority, Connection conn) throws SQLException;

	/**
	 * Add a new virtual wiki record to the database.  The virtual wiki must
	 * not already exist in the database or else an error will be thrown.
	 *
	 * @param virtualWiki The VirtualWiki record that is to be added to the database.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void insertVirtualWiki(VirtualWiki virtualWiki, Connection conn) throws SQLException;

	/**
	 * Add a new watchlist entry record to the database.  An identical entry
	 * must not already exist or else an exception will be thrown.
	 *
	 * @param virtualWikiId The virtual wiki id for the watchlist entry being
	 *  inserted.
	 * @param topicName The name of the topic for the watchlist entry.  This
	 *  value should be set only for topics that do not yet exist, and should
	 *  be set to <code>null</code> for existing topics.
	 * @param userId The ID of the user for the watchlist entry.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void insertWatchlistEntry(int virtualWikiId, String topicName, int userId, Connection conn) throws SQLException;

	/**
	 * Add a new wiki file record to the database.  The wiki file must not
	 * already exist in the database or else an error will be thrown.
	 *
	 * @param wikiFile The WikiFile record that is to be added to the database.
	 * @param virtualWikiId The virtual wiki id for the record that is being added.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void insertWikiFile(WikiFile wikiFile, int virtualWikiId, Connection conn) throws SQLException;

	/**
	 * Add a new wiki file version record to the database.  The wiki file
	 * version must not already exist in the database or else an error will
	 * be thrown.
	 *
	 * @param wikiFileVersion The WikiFileVersion record that is to be added
	 *  to the database.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void insertWikiFileVersion(WikiFileVersion wikiFileVersion, Connection conn) throws SQLException;

	/**
	 * Add a new group record to the database.  The group must not already exist
	 * in the database or else an error will be thrown.
	 *
	 * @param group The WikiGroup record that is to be added to the database.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void insertWikiGroup(WikiGroup group, Connection conn) throws SQLException;

	/**
	 * Add a new user record to the database.  The user must not already exist
	 * in the database or else an error will be thrown.
	 *
	 * @param user The WikiUser record that is to be added to the database.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void insertWikiUser(WikiUser user, Connection conn) throws SQLException;

	/**
	 * Retrieve a list of all topics in a category.
	 *
	 * @param virtualWikiId The virtual wiki id for the virtual wiki of the topics
	 *  being retrieved.
	 * @param virtualWikiName The name of the virtual wiki for the virtual wiki of
	 *  the topic being retrieved.
	 * @param categoryName The name of the category for which associated topics
	 *  are to be retrieved.
	 * @return A list of all topics associated with a specific category.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	List<Category> lookupCategoryTopics(int virtualWikiId, String virtualWikiName, String categoryName) throws SQLException;

	/**
	 * Retrieve a topic that matches a given name and virtual wiki.
	 *
	 * @param virtualWikiId The virtual wiki id for the virtual wiki of the topic
	 *  being retrieved.
	 * @param virtualWikiName The name of the virtual wiki for the virtual wiki of
	 *  the topic being retrieved.
	 * @param topicName The name of the topic being retrieved.
	 * @param caseSensitive Set to <code>true</code> if the topic name should be
	 *  searched for in a case-sensitive manner.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @return A topic containing all topic information for the given topic
	 *  name and virtual wiki.  If no matching topic is found <code>null</code> is
	 *  returned.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	Topic lookupTopic(int virtualWikiId, String virtualWikiName, String topicName, boolean caseSensitive, Connection conn) throws SQLException;

	/**
	 * Retrieve a list of all topic names of a given type within a virtual wiki.
	 *
	 * @param virtualWikiId The virtual wiki id for the virtual wiki of the topics
	 *  being retrieved.
	 * @param topicType1 The topic type (image, normal, etc) for the topics to be
	 *  retrieved.
	 * @param topicType2 The topic type (image, normal, etc) for the topics to be
	 *  retrieved.  Set to the same value as topicType1 if only one topic type is
	 *  needed.
	 * @param pagination A Pagination object that specifies the number of results
	 *  and starting result offset for the result set to be retrieved.
	 * @return A list of all topic names of a given type within a virtual wiki, and
	 *  within the bounds specified by the pagination object.  If no results are
	 *  found then an empty list is returned.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	List<String> lookupTopicByType(int virtualWikiId, int topicType1, int topicType2, Pagination pagination) throws SQLException;

	/**
	 * Return a count of all topics, including redirects, comments pages and templates,
	 * currently available on the Wiki.  This method excludes deleted topics.
	 *
	 * @param virtualWikiId The virtual wiki id for the virtual wiki of the topics
	 *  being retrieved.
	 * @return The total number of topics for the virtual wiki.
	 */
	int lookupTopicCount(int virtualWikiId) throws SQLException;

	/**
	 * Retrieve a result set containing a specific topic version.
	 *
	 * @param topicVersionId The id for the topic version record being retrieved.
	 * @return A TopicVersion record, or <code>null</code> if no matching record is found.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	TopicVersion lookupTopicVersion(int topicVersionId) throws SQLException;

	/**
	 * Retrieve a result set containing all wiki file information for a given WikiFile.
	 *
	 * @param virtualWikiId The virtual wiki id for the virtual wiki of the wiki file
	 *  being retrieved.
	 * @param virtualWikiName The name of the virtual wiki for the virtual wiki of
	 *  the topic being retrieved.
	 * @param topicId The id of the parent topic for the wiki file being retrieved.
	 * @return A WikeFile containing all wiki file information for the given topic
	 *  id and virtual wiki.  If no matching wiki file <code>null</code> is returned.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	WikiFile lookupWikiFile(int virtualWikiId, String virtualWikiName, int topicId) throws SQLException;

	/**
	 * Return a count of all wiki files currently available on the Wiki.  This
	 * method excludes deleted files.
	 *
	 * @param virtualWikiId The virtual wiki id for the virtual wiki of the files
	 *  being retrieved.
	 * @return The total number of files for the specified virtual wiki.
	 */
	int lookupWikiFileCount(int virtualWikiId) throws SQLException;

	/**
	 * Retrieve a result set containing group information given the name of the group.
	 *
	 * @param groupName The name of the group being retrieved.
	 * @return The WikiGroup matching the group name, or <code>null</code> if no
	 *  match is found.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	WikiGroup lookupWikiGroup(String groupName) throws SQLException;

	/**
	 * Retrieve a result set containing all user information for a given WikiUser.
	 *
	 * @param userId The id of the user record being retrieved.
	 * @return A WikiUser containing all information for the given user, or
	 *  <code>null</code> if no matching user exists.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	WikiUser lookupWikiUser(int userId) throws SQLException;

	/**
	 * Retrieve a the user id that matches the given login.
	 *
	 * @param login The login of the user record being retrieved.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @return The user id that matches the given login, or -1 if no match is found.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	int lookupWikiUser(String login, Connection conn) throws SQLException;

	/**
	 * Return a count of all wiki users.
	 *
	 * @return a count of the total number of wiki users.
	 */
	int lookupWikiUserCount() throws SQLException;

	/**
	 * Retrieve the encrypted password for a user given the username.
	 *
	 * @param username The name of the user whose enrypted password is being retrieved.
	 * @return The encrypted password, or <code>null</code> if no matching username is
	 *  found.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	String lookupWikiUserEncryptedPassword(String username) throws SQLException;

	/**
	 * Retrieve a list of all logins for every wiki user.
	 *
	 * @param pagination A Pagination object that specifies the number of results
	 *  and starting result offset for the result set to be retrieved.
	 * @return A list of all logins for all wiki users, within the bounds specified
	 *  by the pagination object, or an empty list if no logins are available.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	List<String> lookupWikiUsers(Pagination pagination) throws SQLException;

	/**
	 * Utility method used when importing to updating the previous topic version ID field
	 * of topic versions, as well as the current version ID field for the topic record.
	 *
	 * @param topic The topic record to update.
	 * @param virtualWikiId The virtual wiki id for the record that is being updated.
	 * @param topicVersionIdList A list of all topic version IDs for the topic, sorted
	 *  chronologically from oldest to newest.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	public void orderTopicVersions(Topic topic, int virtualWikiId, List<Integer> topicVersionIdList) throws SQLException;

	/**
	 * Refresh the log entries by rebuilding the data based on topic versions,
	 * file uploads, and user information.
	 *
	 * @param virtualWikiId The virtual wiki id for which log items are being
	 *  reloaded.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void reloadLogItems(int virtualWikiId, Connection conn) throws SQLException;

	/**
	 * Refresh the recent changes content by reloading the recent changes table.
	 *
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void reloadRecentChanges(Connection conn) throws SQLException;

	/**
	 * Update a role record in the database.
	 *
	 * @param role The Role record that is to be updated in the database.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void updateRole(Role role, Connection conn) throws SQLException;

	/**
	 * Update a topic record in the database.
	 *
	 * @param topic The Topic record that is to be updated in the database.
	 * @param virtualWikiId The virtual wiki id for the record that is being updated.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void updateTopic(Topic topic, int virtualWikiId, Connection conn) throws SQLException;

	/**
	 * Update user authentication credentials.
	 *
	 * @param userDetails The user authentication credentials to update.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	public void updateUserDetails(WikiUserDetails userDetails, Connection conn) throws SQLException;

	/**
	 * Update a virtual wiki record in the database.
	 *
	 * @param virtualWiki The VirtualWiki record that is to be updated in the database.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void updateVirtualWiki(VirtualWiki virtualWiki, Connection conn) throws SQLException;

	/**
	 * Update a wiki file record in the database.
	 *
	 * @param wikiFile The WikiFile record that is to be updated in the database.
	 * @param virtualWikiId The virtual wiki id for the record that is being updated.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void updateWikiFile(WikiFile wikiFile, int virtualWikiId, Connection conn) throws SQLException;

	/**
	 * Update a group record in the database.
	 *
	 * @param group The WikiGroup record that is to be updated in the database.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void updateWikiGroup(WikiGroup group, Connection conn) throws SQLException;

	/**
	 * Update a wiki user record in the database.
	 *
	 * @param user The WikiUser record that is to be updated in the database.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void updateWikiUser(WikiUser user, Connection conn) throws SQLException;
}
