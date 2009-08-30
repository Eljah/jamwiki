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
import java.sql.ResultSet;
import java.sql.SQLException;
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
	 * Return a simple query, that if successfully run indicates that JAMWiki
	 * tables have been initialized in the database.
	 *
	 * @return Returns a simple query that, if successfully run, indicates
	 *  that JAMWiki tables have been set up in the database.
	 */
	String existenceValidationQuery();

	/**
	 * Retrieve a ResultSet containing all topic names that exist for a
	 * virtual wiki. This method will not return the names of previously
	 * deleted topics.
	 *
	 * @param virtualWikiId The id of the virtual wiki for which topic names
	 *  are being retrieved.
	 * @return A ResultSet containing the names of all topics for the virtual
	 *  wiki, not including any previously deleted topics.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet getAllTopicNames(int virtualWikiId, Connection conn) throws SQLException;

        ResultSet getAllTopicIdentifiers(int virtualWikiId, Connection conn) throws SQLException;

	/**
	 * Retrieve a ResultSet consisting of all wiki file version information for
	 * a given wiki file.  Version information is sorted by wiki file version id, which
	 * in effect sorts the wiki file versions from newest to oldest.
	 *
	 * @param wikiFile A WikiFile object for which version information is to be retrieved.
	 * @param descending If <code>true</code> then results are sorted newest to
	 *  oldest.
	 * @return A ResultSet containing wiki file version information for all
	 *  versions of the specified wiki file.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet getAllWikiFileVersions(WikiFile wikiFile, boolean descending, Connection conn) throws SQLException;

	/**
	 * Retrieve a ResultSet containing all categories associated with a particular
	 * virtual wiki.  The result set may be limited by specifying the number of results
	 * to retrieve in a Pagination object.
	 *
	 * @param virtualWikiId The virtual wiki id for the virtual wiki from which all
	 *  categories are to be retrieved.
	 * @param pagination A Pagination object that specifies the number of results
	 *  and starting result offset for the result set to be retrieved.
	 * @return A ResultSet containing all categories associated with a particular
	 *  virtual wiki.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet getCategories(int virtualWikiId, Pagination pagination, Connection conn) throws SQLException;

	/**
	 * Retrieve a ResultSet containing all recent changes made to the wiki for a
	 * specific virtual wiki.
	 *
	 * @param virtualWiki The name of the virtual wiki for which changes are being
	 *  retrieved.
	 * @param pagination A Pagination object that specifies the number of results
	 *  and starting result offset for the result set to be retrieved.
	 * @param descending If <code>true</code> then results are sorted newest to
	 *  oldest.
	 * @return A ResultSet containing all recent changes for a particular virtual
	 *  wiki.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet getRecentChanges(String virtualWiki, Pagination pagination, boolean descending, Connection conn) throws SQLException;

	/**
	 * Retrieve a ResultSet containing all recent changes made to the wiki for a
	 * specific topic.
	 *
	 * @param topicId The id of the topic for which recent changes are being
	 *  retrieved.
	 * @param pagination A Pagination object that specifies the number of results
	 *  and starting result offset for the result set to be retrieved.
	 * @param descending If <code>true</code> then results are sorted newest to
	 *  oldest.
	 * @return A ResultSet containing all recent changes for a particular virtual
	 *  wiki.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet getRecentChanges(int topicId, Pagination pagination, boolean descending, Connection conn) throws SQLException;

	/**
	 * Retrieve a ResultSet of user ids, group ids and role names for all
	 * users whose login contains the given login fragment.
	 *
	 * @param loginFragment A value that must be contained with the user's
	 *  login.  This method will return partial matches, so "name" will
	 *  match "name", "firstname" and "namesake".
	 * @return A ResultSet of user ids, group ids and role names for all
	 *  users whose login contains the login fragment.  If no matches are
	 *  found then this method returns an empty ResultSet.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet getRoleMapByLogin(String loginFragment, Connection conn) throws SQLException;

	/**
	 * Retrieve a ResultSet of user ids, group ids and role names for
	 * all users and groups who have been assigned the specified role.
	 *
	 * @param authority The name of the role being queried against.
	 * @return A ResultSet of user ids, group ids and role names for all
	 *  users and groups who have been assigned the specified role, or an
	 *  empty ResultSet if no matches are found.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet getRoleMapByRole(String authority, Connection conn) throws SQLException;

	/**
	 * Retrieve a ResultSet containing all roles assigned to a given group.
	 *
	 * @param groupName The name of the group for whom roles are being retrieved.
	 * @return A ResultSet of role names for the given user, or an empty
	 *  ResultSet if no roles are assigned to the user.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet getRoleMapGroup(String groupName, Connection conn) throws SQLException;

	/**
	 * Retrieve a ResultSet of user ids, group ids and role names for
	 * all groups that have been assigned a role.
	 *
	 * @return A ResultSet of user ids, group ids and role names for all
	 *  groups that have been assigned a role.  If no matches are found then
	 *  this method returns an empty ResultSet.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet getRoleMapGroups(Connection conn) throws SQLException;

	/**
	 * Retrieve a ResultSet containing all roles assigned to a given user.
	 *
	 * @param login The login of the user for whom roles are being retrieved.
	 * @return A ResultSet of role names for the given user, or an empty
	 *  ResultSet if no roles are assigned to the user.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet getRoleMapUser(String login, Connection conn) throws SQLException;

	/**
	 * Retrieve a ResultSet containing all roles that have been defined for
	 * the wiki.
	 *
	 * @return Returns a WikiResult set containing all roles that have been
	 *  defined for the wiki.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet getRoles(Connection conn) throws SQLException;

	/**
	 * Retrieve a ResultSet containing the topic names of all admin-only
	 * topics for the virtual wiki.
	 *
	 * @param virtualWikiId The id of the virtual wiki for which topic names
	 *  are being retrieved.
	 * @param pagination A Pagination object that specifies the number of results
	 *  and starting result offset for the result set to be retrieved.
	 * @return A ResultSet containing the topic names of all admin-only
	 *  topics for the virtual wiki.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet getTopicsAdmin(int virtualWikiId, Pagination pagination, Connection conn) throws SQLException;

	/**
	 * Retrieve a ResultSet containing all recent changes made to the wiki by a
	 * specific user.
	 *
	 * @param virtualWiki The name of the virtual wiki for which user contributions
	 *  are being retrieved.
	 * @param login The login of the user for whom changes are being retrieved.
	 * @param pagination A Pagination object that specifies the number of results
	 *  and starting result offset for the result set to be retrieved.
	 * @param descending If <code>true</code> then results are sorted newest to
	 *  oldest.
	 * @return A ResultSet containing all recent changes made by a particular user.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet getUserContributionsByLogin(String virtualWiki, String login, Pagination pagination, boolean descending, Connection conn) throws SQLException;

	/**
	 * Retrieve a ResultSet containing all recent changes made to the wiki by
	 * searching for matches against the user display field.  This method is
	 * typically used to retrieve contributions made by anonymous users.
	 *
	 * @param virtualWiki The name of the virtual wiki for which user contributions
	 *  are being retrieved.
	 * @param userDisplay The display name of the user, typically the IP address,
	 *  for whom changes are being retrieved.
	 * @param pagination A Pagination object that specifies the number of results
	 *  and starting result offset for the result set to be retrieved.
	 * @param descending If <code>true</code> then results are sorted newest to
	 *  oldest.
	 * @return A ResultSet containing all recent changes made by a particular user.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet getUserContributionsByUserDisplay(String virtualWiki, String userDisplay, Pagination pagination, boolean descending, Connection conn) throws SQLException;


	/**
	 * Retrieve a ResultSet containing all virtual wiki information for all
	 * virtual wikis.
	 *
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @return Returns a WikiResult set containing all virtual wiki information
	 *  for every virtual wiki.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet getVirtualWikis(Connection conn) throws SQLException;

	/**
	 * Retrieve a ResultSet containing the topic ID and topic name for
	 * topics in the user's watchlist.
	 *
	 * @param virtualWikiId The virtual wiki ID for the virtual wiki for the
	 *  watchlist topics.
	 * @param userId The user ID for the user retrieving the watchlist.
	 * @return A ResultSet containing topic ID and topic name for all
	 *  watchlist items.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet getWatchlist(int virtualWikiId, int userId, Connection conn) throws SQLException;

	/**
	 * Retrieve a ResultSet containing all recent changes for topics in the
	 * user's watchlist.
	 *
	 * @param virtualWikiId The virtual wiki ID for the virtual wiki for the
	 *  watchlist topics.
	 * @param userId The user ID for the user retrieving the watchlist.
	 * @param pagination A Pagination object that specifies the number of results
	 *  and starting result offset for the result set to be retrieved.
	 * @return A ResultSet containing recent changes for the watchlist.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet getWatchlist(int virtualWikiId, int userId, Pagination pagination, Connection conn) throws SQLException;

	/**
	 * Add a new category record to the database.  Note that this method will fail
	 * if an existing category of the same name is already associated with the
	 * topic.
	 *
	 * @param category The category record that is being created.
	 * @param virtualWikiId The virtual wiki id for the record that is being added.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void insertCategory(Category category, int virtualWikiId, Connection conn) throws SQLException;

        void insertCategory(Category category, int topicId, int virtualWikiId, Connection conn) throws SQLException;

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
	 * @param groupMemberId A unique ID for the group membership record.
	 * @param username The username for the user being added to the group.
	 * @param groupId The group ID for the group.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void insertGroupMember(int groupMemberId, String username, int groupId, Connection conn) throws SQLException;

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
         * @return Topic identifier, or -1 if failed.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	int insertTopic(Topic topic, int virtualWikiId, Connection conn) throws SQLException;

        /**
	 * Add a new topic version record to the database.  The topic version must
	 * not already exist in the database or else an error will be thrown.
	 *
	 * @param topicVersion The TopicVersion record that is to be added to the database.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
         * @return TopicVersion identifier, or -1 if failed.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	int insertTopicVersion(TopicVersion topicVersion, Connection conn) throws SQLException;

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
	 * Retrieve a result set containing the topic name and sort key for all
	 * topics associated with a category.
	 *
	 * @param virtualWikiId The virtual wiki id for the virtual wiki of the topics
	 *  being retrieved.
	 * @param categoryName The name of the category for which associated topics
	 *  are to be retrieved.
	 * @return A ResultSet containing topic name and sort key for all topics
	 *  associated with a specific category.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet lookupCategoryTopics(int virtualWikiId, String categoryName, Connection conn) throws SQLException;

	/**
	 * Retrieve a ResultSet containing all topic information for a given topic.
	 *
	 * @param virtualWikiId The virtual wiki id for the virtual wiki of the topic
	 *  being retrieved.
	 * @param topicName The name of the topic being retrieved.
	 * @param caseSensitive Set to <code>true</code> if the topic name should be
	 *  searched for in a case-sensitive manner.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @return A ResultSet containing all topic information for the given topic
	 *  name and virtual wiki.  If no matching topic is found an empty result set is
	 *  returned.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet lookupTopic(int virtualWikiId, String topicName, boolean caseSensitive, Connection conn) throws SQLException;
    
        ResultSet lookupTopicById(int virtualWikiId, int topicId, Connection conn) throws SQLException;

        ResultSet lookupTopicMetaDataById(int virtualWikiId, int topicId, Connection conn) throws SQLException;

	/**
	 * Retrieve a result set of all topics of a given type within a virtual wiki.
	 *
	 * @param virtualWikiId The virtual wiki id for the virtual wiki of the topics
	 *  being retrieved.
	 * @param topicType The topic type (image, normal, etc) for the topics to be
	 *  retrieved.
	 * @param pagination A Pagination object that specifies the number of results
	 *  and starting result offset for the result set to be retrieved.
	 * @return A WikiResult set of all non-deleted topics for the given virtual wiki
	 *  of the specified topic type, and within the bounds specified by the pagination
	 *  object.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet lookupTopicByType(int virtualWikiId, int topicType, Pagination pagination, Connection conn) throws SQLException;

	/**
	 * Return a count of all topics, including redirects, comments pages and templates,
	 * currently available on the Wiki.  This method excludes deleted topics.
	 *
	 * @param virtualWikiId The virtual wiki id for the virtual wiki of the topics
	 *  being retrieved.
	 */
	ResultSet lookupTopicCount(int virtualWikiId, Connection conn) throws SQLException;

	/**
	 * Retrieve a result set containing a specific topic version.
	 *
	 * @param topicVersionId The id for the topic version record being retrieved.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @return A ResultSet containing the topic version record, or an empty
	 *  result set if no matching record is found.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet lookupTopicVersion(int topicVersionId, Connection conn) throws SQLException;

	/**
	 * Retrieve a result set containing all wiki file information for a given WikiFile.
	 *
	 * @param virtualWikiId The virtual wiki id for the virtual wiki of the wiki file
	 *  being retrieved.
	 * @param topicId The id of the parent topic for the wiki file being retrieved.
	 * @return A ResultSet containing all wiki file information for the given topic
	 *  id and virtual wiki.  If no matching wiki file is found an empty result set is
	 *  returned.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet lookupWikiFile(int virtualWikiId, int topicId, Connection conn) throws SQLException;

	/**
	 * Return a count of all wiki files currently available on the Wiki.  This
	 * method excludes deleted files.
	 *
	 * @param virtualWikiId The virtual wiki id for the virtual wiki of the files
	 *  being retrieved.
	 */
	ResultSet lookupWikiFileCount(int virtualWikiId, Connection conn) throws SQLException;

	/**
	 * Retrieve a result set containing group information given the name of the group.
	 *
	 * @param groupName The name of the group being retrieved.
	 * @return A ResultSet containing the group information.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet lookupWikiGroup(String groupName, Connection conn) throws SQLException;

	/**
	 * Retrieve a result set containing all user information for a given WikiUser.
	 *
	 * @param userId The id of the user record being retrieved.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @return A ResultSet containing all information for the given user, or
	 *  an empty result set if no matching user exists.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet lookupWikiUser(int userId, Connection conn) throws SQLException;

	/**
	 * Retrieve a result set containing all user information for a given WikiUser.
	 *
	 * @param login The login of the user record being retrieved.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @return A ResultSet containing all information for the given user, or
	 *  an empty result set if no matching user exists.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet lookupWikiUser(String login, Connection conn) throws SQLException;

	/**
	 * Return a count of all wiki users.
	 */
	ResultSet lookupWikiUserCount(Connection conn) throws SQLException;

	/**
	 * Retrieve a result set containing the encrypted password for a user given
	 * the username.
	 *
	 * @param username The name of the user whose enrypted password is being retrieved.
	 * @return A ResultSet containing the encrypted password.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet lookupWikiUserEncryptedPassword(String username, Connection conn) throws SQLException;

	/**
	 * Retrieve a result set of all logins for every wiki user.
	 *
	 * @param pagination A Pagination object that specifies the number of results
	 *  and starting result offset for the result set to be retrieved.
	 * @return A WikiResult set of all logins for all wiki users, within the
	 *  bounds specified by the pagination object.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	ResultSet lookupWikiUsers(Pagination pagination, Connection conn) throws SQLException;

	/**
	 * Retrieve the next available group member id from the group members table.
	 *
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @return The next available group member id from the group members table.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	int nextGroupMemberId(Connection conn) throws SQLException;

	/**
	 * Retrieve the next available virtual wiki id from the virtual wiki table.
	 *
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @return The next available virtual wiki id from the virtual wiki table.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	int nextVirtualWikiId(Connection conn) throws SQLException;

	/**
	 * Retrieve the next available wiki file id from the wiki file table.
	 *
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @return The next available wiki file id from the wiki file table.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	int nextWikiFileId(Connection conn) throws SQLException;

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
	int nextWikiFileVersionId(Connection conn) throws SQLException;

	/**
	 * Retrieve the next available wiki group id from the wiki group table.
	 *
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @return The next available wiki group id from the wiki group table.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	int nextWikiGroupId(Connection conn) throws SQLException;

	/**
	 * Retrieve the next available wiki user id from the wiki user table.
	 *
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @return The next available wiki user id from the wiki user table.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	int nextWikiUserId(Connection conn) throws SQLException;

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


        void updateTopicVersion(TopicVersion topicVersion, int virtualWikiId, Connection conn) throws SQLException;

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
