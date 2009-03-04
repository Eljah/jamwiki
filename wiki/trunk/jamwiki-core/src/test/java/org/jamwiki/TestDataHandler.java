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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import org.apache.commons.lang.StringUtils;
import org.jamwiki.model.Category;
import org.jamwiki.model.RecentChange;
import org.jamwiki.model.Role;
import org.jamwiki.model.RoleMap;
import org.jamwiki.model.Topic;
import org.jamwiki.model.TopicVersion;
import org.jamwiki.model.VirtualWiki;
import org.jamwiki.model.Watchlist;
import org.jamwiki.model.WikiFile;
import org.jamwiki.model.WikiFileVersion;
import org.jamwiki.model.WikiGroup;
import org.jamwiki.model.WikiUser;
import org.jamwiki.utils.Pagination;
import org.jamwiki.utils.WikiLogger;

/**
 *
 */
public class TestDataHandler implements DataHandler {

	private static final WikiLogger logger = WikiLogger.getLogger(TestDataHandler.class.getName());

	/**
	 *
	 */
	public boolean authenticate(String username, String password) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public boolean canMoveTopic(Topic fromTopic, String destination) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public void deleteTopic(Topic topic, TopicVersion topicVersion, boolean userVisible) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public List<Category> getAllCategories(String virtualWiki, Pagination pagination) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public List<Role> getAllRoles() throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public List<String> getAllTopicNames(String virtualWiki) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public List<WikiFileVersion> getAllWikiFileVersions(String virtualWiki, String topicName, boolean descending) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public List<RecentChange> getRecentChanges(String virtualWiki, Pagination pagination, boolean descending) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public List<RecentChange> getRecentChanges(String virtualWiki, String topicName, Pagination pagination, boolean descending) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public Collection<RoleMap> getRoleMapByLogin(String loginFragment) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public Collection<RoleMap> getRoleMapByRole(String roleName) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public Role[] getRoleMapGroup(String groupName) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public Collection<RoleMap> getRoleMapGroups() throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public Role[] getRoleMapUser(String login) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public List<String> getTopicsAdmin(String virtualWiki, Pagination pagination) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public List<RecentChange> getUserContributions(String virtualWiki, String userString, Pagination pagination, boolean descending) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public List<VirtualWiki> getVirtualWikiList() throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public Watchlist getWatchlist(String virtualWiki, int userId) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public List<RecentChange> getWatchlist(String virtualWiki, int userId, Pagination pagination) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public List<Category> lookupCategoryTopics(String virtualWiki, String categoryName) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public Topic lookupTopic(String virtualWiki, String topicName, boolean deleteOK, Object transactionObject) throws Exception {
		String content = TestFileUtil.retrieveFileContent(TestFileUtil.TEST_TOPICS_DIR, topicName);
		if (content == null) {
			return null;
		}
		Topic topic = new Topic();
		topic.setName(topicName);
		topic.setVirtualWiki(virtualWiki);
		topic.setTopicContent(content);
		return topic;
	}

	/**
	 *
	 */
	public int lookupTopicCount(String virtualWiki) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public List<String> lookupTopicByType(String virtualWiki, int topicType, Pagination pagination) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public TopicVersion lookupTopicVersion(int topicVersionId) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public VirtualWiki lookupVirtualWiki(String virtualWikiName) throws Exception {
		if (!StringUtils.equals(virtualWikiName, "en")) {
			// test handler, so hard-code "en" as the only valid virtual wiki
			return null;
		}
		VirtualWiki virtualWiki = new VirtualWiki();
		virtualWiki.setName(virtualWikiName);
		return virtualWiki;
	}

	/**
	 *
	 */
	public WikiFile lookupWikiFile(String virtualWiki, String fileName) throws Exception {
		String content = TestFileUtil.retrieveFileContent(TestFileUtil.TEST_TOPICS_DIR, fileName);
		if (content == null) {
			return null;
		}
		WikiFile wikiFile = new WikiFile();
		wikiFile.setFileName(fileName);
		wikiFile.setVirtualWiki(virtualWiki);
		wikiFile.setUrl(StringUtils.trim(content));
		return wikiFile;
	}

	/**
	 *
	 */
	public int lookupWikiFileCount(String virtualWiki) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public WikiGroup lookupWikiGroup(String groupName) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public WikiUser lookupWikiUser(int userId) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public WikiUser lookupWikiUser(String username) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public int lookupWikiUserCount() throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public String lookupWikiUserEncryptedPassword(String username) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public List<String> lookupWikiUsers(Pagination pagination) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public void moveTopic(Topic fromTopic, TopicVersion fromVersion, String destination) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public void reloadRecentChanges() throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public void setup(Locale locale, WikiUser user, String username, String encryptedPassword) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public void setupSpecialPages(Locale locale, WikiUser user, VirtualWiki virtualWiki) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public void undeleteTopic(Topic topic, TopicVersion topicVersion, boolean userVisible) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public void updateSpecialPage(Locale locale, String virtualWiki, String topicName, String ipAddress) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public void writeFile(WikiFile wikiFile, WikiFileVersion wikiFileVersion) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public void writeRole(Role role, boolean update) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public void writeRoleMapGroup(int groupId, List<String> roles) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public void writeRoleMapUser(String username, List<String> roles) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public void writeTopic(Topic topic, TopicVersion topicVersion, LinkedHashMap categories, Vector links, boolean userVisible) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public void writeVirtualWiki(VirtualWiki virtualWiki) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public void writeWatchlistEntry(Watchlist watchlist, String virtualWiki, String topicName, int userId) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public void writeWikiGroup(WikiGroup group) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public void writeWikiUser(WikiUser user, String username, String encryptedPassword) throws Exception {
		throw new UnsupportedOperationException();
	}
}
