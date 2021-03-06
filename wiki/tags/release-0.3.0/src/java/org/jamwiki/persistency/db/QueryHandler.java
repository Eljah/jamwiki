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
package org.jamwiki.persistency.db;

import java.sql.Connection;
import org.jamwiki.model.RecentChange;
import org.jamwiki.model.Topic;
import org.jamwiki.model.TopicVersion;
import org.jamwiki.model.VirtualWiki;
import org.jamwiki.model.WikiFile;
import org.jamwiki.model.WikiFileVersion;
import org.jamwiki.model.WikiUser;

/**
 *
 */
public interface QueryHandler {

	/**
	 *
	 */
	public void createTables(Connection conn) throws Exception;

	/**
	 *
	 */
	public void deleteRecentChanges(int topicId, Connection conn) throws Exception;

	/**
	 *
	 */
	public void deleteTopicCategories(int childTopicId, Connection conn) throws Exception;

	/**
	 *
	 */
	public void dropTables(Connection conn);

	/**
	 *
	 */
	public WikiResultSet getAllTopicNames(int virtualWikiId) throws Exception;

	/**
	 *
	 */
	public WikiResultSet getAllTopicVersions(Topic topic, boolean descending) throws Exception;

	/**
	 *
	 */
	public WikiResultSet getAllWikiFileTopicNames(int virtualWikiId) throws Exception;

	/**
	 *
	 */
	public WikiResultSet getAllWikiFileVersions(WikiFile wikiFile, boolean descending) throws Exception;

	/**
	 *
	 */
	public WikiResultSet getAllWikiUserLogins() throws Exception;

	/**
	 *
	 */
	public WikiResultSet getCategories(int virtualWikiId) throws Exception;

	/**
	 *
	 */
	public WikiResultSet getRecentChanges(String virtualWiki, int num, boolean descending) throws Exception;

	/**
	 *
	 */
	public WikiResultSet getUserContributions(String virtualWiki, String userString, int num, boolean descending) throws Exception;

	/**
	 *
	 */
	public WikiResultSet getVirtualWikis() throws Exception;

	/**
	 *
	 */
	public void insertCategory(int childTopicId, String categoryName, String sortKey, Connection conn) throws Exception;

	/**
	 *
	 */
	public void insertRecentChange(RecentChange change, int virtualWikiId, Connection conn) throws Exception;

	/**
	 *
	 */
	public void insertTopic(Topic topic, int virtualWikiId, Connection conn) throws Exception;

	/**
	 *
	 */
	public void insertTopicVersion(TopicVersion topicVersion, Connection conn) throws Exception;

	/**
	 *
	 */
	public void insertVirtualWiki(VirtualWiki virtualWiki, Connection conn) throws Exception;

	/**
	 *
	 */
	public void insertWikiFile(WikiFile wikiFile, int virtualWikiId, Connection conn) throws Exception;

	/**
	 *
	 */
	public void insertWikiFileVersion(WikiFileVersion wikiFileVersion, Connection conn) throws Exception;

	/**
	 *
	 */
	public void insertWikiUser(WikiUser user, Connection conn) throws Exception;

	/**
	 *
	 */
	public void insertWikiUserInfo(WikiUser user, Connection conn) throws Exception;

	/**
	 *
	 */
	public WikiResultSet lookupCategoryTopics(int virtualWikiId, String categoryName, int topicType) throws Exception;

	/**
	 *
	 */
	public WikiResultSet lookupLastTopicVersion(Topic topic) throws Exception;

	/**
	 *
	 */
	public WikiResultSet lookupLastTopicVersion(Topic topic, Connection conn) throws Exception;

	/**
	 *
	 */
	public WikiResultSet lookupTopic(int virtualWikiId, String topicName) throws Exception;

	/**
	 *
	 */
	public WikiResultSet lookupTopic(int virtualWikiId, String topicName, Connection conn) throws Exception;

	/**
	 *
	 */
	public WikiResultSet lookupTopicByType(int virtualWikiId, int topicType) throws Exception;

	/**
	 *
	 */
	public WikiResultSet lookupTopicVersion(int topicVersionId) throws Exception;

	/**
	 *
	 */
	public WikiResultSet lookupTopicVersion(int topicVersionId, Connection conn) throws Exception;

	/**
	 *
	 */
	public WikiResultSet lookupWikiFile(int virtualWikiId, int topicId) throws Exception;

	/**
	 *
	 */
	public WikiResultSet lookupWikiUser(int userId) throws Exception;

	/**
	 *
	 */
	public WikiResultSet lookupWikiUser(int userId, Connection conn) throws Exception;

	/**
	 *
	 */
	public WikiResultSet lookupWikiUser(String login) throws Exception;

	/**
	 *
	 */
	public WikiResultSet lookupWikiUser(String login, String password) throws Exception;

	/**
	 *
	 */
	public int nextTopicId(Connection conn) throws Exception;

	/**
	 *
	 */
	public int nextTopicVersionId(Connection conn) throws Exception;

	/**
	 *
	 */
	public int nextVirtualWikiId(Connection conn) throws Exception;

	/**
	 *
	 */
	public int nextWikiFileId(Connection conn) throws Exception;

	/**
	 *
	 */
	public int nextWikiFileVersionId(Connection conn) throws Exception;

	/**
	 *
	 */
	public int nextWikiUserId(Connection conn) throws Exception;

	/**
	 *
	 */
	public void reloadRecentChanges(Connection conn) throws Exception;

	/**
	 *
	 */
	public void updateTopic(Topic topic, int virtualWikiId, Connection conn) throws Exception;

	/**
	 *
	 */
	public void updateVirtualWiki(VirtualWiki virtualWiki, Connection conn) throws Exception;

	/**
	 *
	 */
	public void updateWikiFile(WikiFile wikiFile, int virtualWikiId, Connection conn) throws Exception;

	/**
	 *
	 */
	public void updateWikiUser(WikiUser user, Connection conn) throws Exception;

	/**
	 *
	 */
	public void updateWikiUserInfo(WikiUser user, Connection conn) throws Exception;
}
