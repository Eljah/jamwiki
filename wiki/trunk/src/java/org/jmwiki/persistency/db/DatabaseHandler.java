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
 * along with this program (gpl.txt); if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.jmwiki.persistency.db;

import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.jmwiki.Environment;
import org.jmwiki.persistency.PersistencyHandler;
import org.jmwiki.TopicLock;
import org.jmwiki.WikiBase;
import org.jmwiki.WikiException;
import org.jmwiki.PseudoTopicHandler;
import org.jmwiki.model.Topic;
import org.jmwiki.model.TopicVersion;
import org.jmwiki.utils.DiffUtil;
import org.jmwiki.utils.Utilities;

/**
 *
 */
public class DatabaseHandler implements PersistencyHandler {

	public static final String DB_TYPE_ORACLE = "oracle";
	public static final String DB_TYPE_MYSQL = "mysql";
	private static final Logger logger = Logger.getLogger(DatabaseHandler.class);
	private static Hashtable virtualWikiIdHash = null;
	private static Hashtable virtualWikiNameHash = null;

	private static final String STATEMENT_INSERT_TOPIC =
		"insert into jmw_topic ( "
		+   "topic_id, virtual_wiki_id, topic_name, topic_type, "
		+   "topic_locked_by, topic_lock_date, topic_read_only, topic_content, "
		+   "topic_lock_session_key "
		+ ") values ( "
		+   "?, ?, ?, ?, ?, ?, ?, ?, ?"
		+ ") ";
	private static final String STATEMENT_INSERT_TOPIC_VERSION =
		"insert into jmw_topic_version ("
		+   "topic_version_id, topic_id, edit_comment, version_content, "
		+   "author_id, edit_date, edit_type, author_ip_address "
		+ ") values ( "
		+   "?, ?, ?, ?, ?, ?, ?, ? "
		+ ") ";
	private static final String STATEMENT_INSERT_VIRTUAL_WIKI =
		"insert into jmw_virtual_wiki ("
		+   "virtual_wiki_id, virtual_wiki_name "
		+ ") values ( "
		+   "?, ? "
		+ ") ";
	private static final String STATEMENT_SELECT_TOPIC =
		"select * from jmw_topic "
		+ "where virtual_wiki_id = ? "
		+ "and topic_name = ? ";
	private static final String STATEMENT_SELECT_TOPIC_READ_ONLY =
		"select * from jmw_topic "
		+ "where virtual_wiki_id = ? "
		+ "and topic_read_only = ? ";
	private static final String STATEMENT_SELECT_TOPIC_LOCKED =
		"select * from jmw_topic "
		+ "where virtual_wiki_id = ? "
		+ "and topic_lock_session_key is not null ";
	private static final String STATEMENT_SELECT_TOPIC_SEQUENCE =
		"select nextval('jmw_topic_seq') as topic_id ";
	private static final String STATEMENT_SELECT_TOPIC_VERSION =
		"select * from jmw_topic_version "
		+ "where topic_version_id = ? ";
	private static final String STATEMENT_SELECT_TOPIC_VERSION_COUNT =
		"select count(*) as total from jmw_topic_version "
		+ "where topic_version_id = ? ";
	private static final String STATEMENT_SELECT_TOPIC_VERSIONS =
		"select * from jmw_topic_version "
		+ "where topic_id = ? "
		+ "order by topic_version_id desc ";
	private static final String STATEMENT_SELECT_TOPIC_VERSION_LAST =
		"select max(topic_version_id) as topic_version_id from jmw_topic_version "
		+ "where topic_id = ? ";
	private static final String STATEMENT_SELECT_TOPIC_VERSION_SEQUENCE =
		"select nextval('jmw_topic_version_seq') as topic_version_id ";
	private static final String STATEMENT_SELECT_VIRTUAL_WIKI_SEQUENCE =
		"select nextval('jmw_virtual_wiki_seq') as virtual_wiki_id ";
	private static final String STATEMENT_UPDATE_TOPIC =
		"update jmw_topic set "
		+ "virtual_wiki_id = ?, "
		+ "topic_name = ?, "
		+ "topic_type = ?, "
		+ "topic_locked_by = ?, "
		+ "topic_lock_date = ?, "
		+ "topic_read_only = ?, "
		+ "topic_content = ?, "
		+ "topic_lock_session_key = ? "
		+ "where topic_id = ? ";

	/**
	 *
	 */
	private void addTopic(Topic topic) throws Exception {
		int virtualWikiId = lookupVirtualWikiId(topic.getVirtualWiki());
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DatabaseConnection.getConnection();
			WikiResultSet rs = DatabaseConnection.executeQuery(STATEMENT_SELECT_TOPIC_SEQUENCE, conn);
			topic.setTopicId(rs.getInt("topic_id"));
			stmt = conn.prepareStatement(STATEMENT_INSERT_TOPIC);
			stmt.setInt(1, topic.getTopicId());
			stmt.setInt(2, virtualWikiId);
			stmt.setString(3, topic.getName());
			stmt.setInt(4, topic.getTopicType());
			if (topic.getLockedBy() > 0) {
				stmt.setInt(5, topic.getLockedBy());
			} else {
				stmt.setNull(5, Types.INTEGER);
			}
			stmt.setTimestamp(6, topic.getLockedDate());
			stmt.setBoolean(7, topic.getReadOnly());
			stmt.setString(8, topic.getTopicContent());
			stmt.setString(9, topic.getLockSessionKey());
			stmt.executeUpdate();
		} finally {
			if (conn != null) {
				DatabaseConnection.closeConnection(conn, stmt);
			}
		}
	}

	/**
	 *
	 */
	public void addTopicVersion(String virtualWiki, String topicName, String contents, Date at, String ipAddress) throws Exception {

		// FIXME - DELETE BELOW
		Connection conn = null;
		try {
			conn = DatabaseConnection.getConnection();
			PreparedStatement addStatement;
			if (DatabaseHandler.isOracle()) {
				boolean savedAutoCommit = conn.getAutoCommit();
				conn.setAutoCommit(false);
				addStatement = conn.prepareStatement(STATEMENT_ADD_VERSION_ORACLE1);
				addStatement.setString(1, virtualWiki);
				addStatement.setString(2, topicName);
				addStatement.setTimestamp(3, new DBDate(at).asTimestamp());
				addStatement.execute();
				addStatement.close();
				conn.commit();
				addStatement = conn.prepareStatement(
					STATEMENT_ADD_VERSION_ORACLE2,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE
				);
				addStatement.setString(1, topicName);
				addStatement.setString(2, virtualWiki);
				ResultSet rs = addStatement.executeQuery();
				rs.next();
				OracleClobHelper.setClobValue(rs.getClob(1), contents);
				rs.close();
				addStatement.close();
				conn.commit();
				conn.setAutoCommit(savedAutoCommit);
			} else {
				addStatement = conn.prepareStatement(STATEMENT_ADD_VERSION);
				addStatement.setString(1, virtualWiki);
				addStatement.setString(2, topicName);
				addStatement.setString(3, contents);
				addStatement.setTimestamp(4, new DBDate(at).asTimestamp());
				addStatement.execute();
				addStatement.close();
			}
		} finally {
			DatabaseConnection.closeConnection(conn);
		}
		// FIXME - DELETE ABOVE

		TopicVersion version = new TopicVersion();
		Topic topic = lookupTopic(virtualWiki, topicName);
		if (topic == null) {
			throw new Exception("No topic exists for " + virtualWiki + " / " + topicName);
		}
		version.setTopicId(topic.getTopicId());
		version.setVersionContent(contents);
		version.setAuthorIpAddress(ipAddress);
		addTopicVersion(version);
	}

	/**
	 *
	 */
	private void addTopicVersion(TopicVersion topicVersion) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DatabaseConnection.getConnection();
			WikiResultSet rs = DatabaseConnection.executeQuery(STATEMENT_SELECT_TOPIC_VERSION_SEQUENCE, conn);
			topicVersion.setTopicVersionId(rs.getInt("topic_version_id"));
			stmt = conn.prepareStatement(STATEMENT_INSERT_TOPIC_VERSION);
			stmt.setInt(1, topicVersion.getTopicVersionId());
			stmt.setInt(2, topicVersion.getTopicId());
			stmt.setString(3, topicVersion.getEditComment());
			stmt.setString(4, topicVersion.getVersionContent());
			stmt.setInt(5, topicVersion.getAuthorId());
			// FIXME - it would be better to let this default to CURRENT_TIMESTAMP...
			stmt.setTimestamp(6, topicVersion.getEditDate());
			stmt.setInt(7, topicVersion.getEditType());
			stmt.setString(8, topicVersion.getAuthorIpAddress());
			stmt.executeUpdate();
		} finally {
			if (conn != null) {
				DatabaseConnection.closeConnection(conn, stmt);
			}
		}
	}

	/**
	 *
	 */
	public void addVirtualWiki(String virtualWikiName) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		int virtualWikiId;
		try {
			conn = DatabaseConnection.getConnection();
			WikiResultSet rs = DatabaseConnection.executeQuery(STATEMENT_SELECT_VIRTUAL_WIKI_SEQUENCE, conn);
			virtualWikiId = rs.getInt("virtual_wiki_id");
			stmt = conn.prepareStatement(STATEMENT_INSERT_VIRTUAL_WIKI);
			stmt.setInt(1, virtualWikiId);
			stmt.setString(2, virtualWikiName);
			stmt.executeUpdate();
		} finally {
			if (conn != null) {
				DatabaseConnection.closeConnection(conn, stmt);
			}
		}
		if (virtualWikiNameHash != null) {
			virtualWikiNameHash.put(virtualWikiName, new Integer(virtualWikiId));
		}
		if (virtualWikiIdHash != null) {
			virtualWikiIdHash.put(new Integer(virtualWikiId), virtualWikiName);
		}
	}

	/**
	 *
	 */
	public List getAllVersions(String virtualWiki, String topicName) throws Exception {
		List all = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Topic topic = lookupTopic(virtualWiki, topicName);
		if (topic == null) {
			throw new Exception("No topic exists for " + virtualWiki + " / " + topicName);
		}
		try {
			conn = DatabaseConnection.getConnection();
			stmt = conn.prepareStatement(STATEMENT_SELECT_TOPIC_VERSIONS);
			stmt.setInt(1, topic.getTopicId());
			rs = stmt.executeQuery();
			WikiResultSet wrs = new WikiResultSet(rs);
			while (wrs.next()) {
				all.add(initTopicVersion(wrs));
			}
		} finally {
			DatabaseConnection.closeConnection(conn, stmt, rs);
		}
		return all;
	}

	/**
	 *
	 */
	public int getNumberOfVersions(String virtualWiki, String topicName) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Topic topic = lookupTopic(virtualWiki, topicName);
		if (topic == null) {
			throw new Exception("No topic exists for " + virtualWiki + " / " + topicName);
		}
		try {
			conn = DatabaseConnection.getConnection();
			stmt = conn.prepareStatement(STATEMENT_SELECT_TOPIC_VERSION_COUNT);
			stmt.setInt(1, topic.getTopicId());
			rs = stmt.executeQuery();
			rs.next();
			return rs.getInt("total");
		} finally {
			DatabaseConnection.closeConnection(conn, stmt, rs);
		}
	}

	/**
	 *
	 */
	protected static Topic initTopic(WikiResultSet rs) {
		try {
			int virtualWikiId = rs.getInt("virtual_wiki_id");
			String virtualWiki = lookupVirtualWikiName(virtualWikiId);
			Topic topic = new Topic();
			topic.setAdminOnly(rs.getBoolean("topic_admin_only"));
			topic.setName(rs.getString("topic_name"));
			topic.setVirtualWiki(virtualWiki);
			topic.setTopicContent(rs.getString("topic_content"));
			topic.setTopicId(rs.getInt("topic_id"));
			topic.setLockedBy(rs.getInt("topic_locked_by"));
			topic.setLockedDate(rs.getTimestamp("topic_lock_date"));
			topic.setLockSessionKey(rs.getString("topic_lock_session_key"));
			topic.setReadOnly(rs.getBoolean("topic_read_only"));
			topic.setTopicType(rs.getInt("topic_type"));
			return topic;
		} catch (Exception e) {
			logger.error("Failure while initializing topic", e);
			return null;
		}
	}

	/**
	 *
	 */
	protected static TopicVersion initTopicVersion(WikiResultSet rs) {
		try {
			TopicVersion topicVersion = new TopicVersion();
			topicVersion.setTopicVersionId(rs.getInt("topic_version_id"));
			topicVersion.setTopicId(rs.getInt("topic_id"));
			topicVersion.setEditComment(rs.getString("edit_comment"));
			topicVersion.setVersionContent(rs.getString("version_content"));
			topicVersion.setAuthorId(rs.getInt("author_id"));
			topicVersion.setEditDate(rs.getTimestamp("edit_date"));
			topicVersion.setEditType(rs.getInt("edit_type"));
			topicVersion.setAuthorIpAddress(rs.getString("author_ip_address"));
			return topicVersion;
		} catch (Exception e) {
			logger.error("Failure while initializing topic version", e);
			return null;
		}
	}

	/**
	 *
	 */
	protected static void loadVirtualWikiHashes() throws Exception {
		virtualWikiNameHash = new Hashtable();
		virtualWikiIdHash = new Hashtable();
		String sql = "select * from jmw_virtual_wiki ";
		try {
			WikiResultSet rs = DatabaseConnection.executeQuery(sql);
			while (rs.next()) {
				Integer value = new Integer(rs.getInt("virtual_wiki_id"));
				String key = rs.getString("virtual_wiki_name");
				virtualWikiNameHash.put(key, value);
				virtualWikiIdHash.put(value, key);
			}
		} catch (Exception e) {
			logger.error("Failure while loading virtual wiki hashtable ", e);
			// if there is an error make sure the hashtable is reset since it wasn't
			// properly initialized
			virtualWikiNameHash = null;
			virtualWikiIdHash = null;
			throw e;
		}
	}

	/**
	 *
	 */
	public TopicVersion lookupLastTopicVersion(String virtualWiki, String topicName) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Topic topic = lookupTopic(virtualWiki, topicName);
		try {
			conn = DatabaseConnection.getConnection();
			stmt = conn.prepareStatement(STATEMENT_SELECT_TOPIC_VERSION_LAST);
			stmt.setInt(1, topic.getTopicId());
			rs = stmt.executeQuery();
			if (rs == null) return null;
			rs.next();
			int topicVersionId = rs.getInt("topic_version_id");
			return lookupTopicVersion(virtualWiki, topicName, topicVersionId);
		} finally {
			if (conn != null) {
				DatabaseConnection.closeConnection(conn, stmt, rs);
			}
		}
	}

	/**
	 *
	 */
	public Topic lookupTopic(String virtualWiki, String topicName) throws Exception {
		int virtualWikiId = lookupVirtualWikiId(virtualWiki);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseConnection.getConnection();
			stmt = conn.prepareStatement(STATEMENT_SELECT_TOPIC);
			stmt.setInt(1, virtualWikiId);
			stmt.setString(2, topicName);
			rs = stmt.executeQuery();
			WikiResultSet wrs = new WikiResultSet(rs);
			if (wrs.size() == 0) return null;
			return initTopic(wrs);
		} finally {
			if (conn != null) {
				DatabaseConnection.closeConnection(conn, stmt, rs);
			}
		}
	}

	/**
	 *
	 */
	public TopicVersion lookupTopicVersion(String virtualWiki, String topicName, int topicVersionId) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseConnection.getConnection();
			stmt = conn.prepareStatement(STATEMENT_SELECT_TOPIC_VERSION);
			stmt.setInt(1, topicVersionId);
			rs = stmt.executeQuery();
			WikiResultSet wrs = new WikiResultSet(rs);
			if (wrs.size() == 0) return null;
			return initTopicVersion(wrs);
		} finally {
			if (conn != null) {
				DatabaseConnection.closeConnection(conn, stmt, rs);
			}
		}
	}

	/**
	 *
	 */
	protected static int lookupVirtualWikiId(String virtualWikiName) throws Exception {
		if (virtualWikiNameHash == null) {
			loadVirtualWikiHashes();
		}
		Integer virtualWikiId = (Integer)virtualWikiNameHash.get(virtualWikiName);
		if (virtualWikiId == null) {
			throw new Exception("Virtual wiki " + virtualWikiName + " not found");
		}
		return virtualWikiId.intValue();
	}

	/**
	 *
	 */
	protected static String lookupVirtualWikiName(int virtualWikiId) throws Exception {
		if (virtualWikiIdHash == null) {
			loadVirtualWikiHashes();
		}
		String virtualWikiName = (String)virtualWikiIdHash.get(new Integer(virtualWikiId));
		if (virtualWikiName == null) {
			throw new Exception("Virtual wiki " + virtualWikiId + " not found");
		}
		return virtualWikiName;
	}

	/**
	 *
	 */
	private void updateTopic(Topic topic) throws Exception {
		int virtualWikiId = lookupVirtualWikiId(topic.getVirtualWiki());
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DatabaseConnection.getConnection();
			stmt = conn.prepareStatement(STATEMENT_UPDATE_TOPIC);
			stmt.setInt(1, virtualWikiId);
			stmt.setString(2, topic.getName());
			stmt.setInt(3, topic.getTopicType());
			if (topic.getLockedBy() > 0) {
				stmt.setInt(4, topic.getLockedBy());
			} else {
				stmt.setNull(4, Types.INTEGER);
			}
			stmt.setTimestamp(5, topic.getLockedDate());
			stmt.setBoolean(6, topic.getReadOnly());
			stmt.setString(7, topic.getTopicContent());
			stmt.setString(8, topic.getLockSessionKey());
			stmt.setInt(9, topic.getTopicId());
			stmt.executeUpdate();
		} finally {
			if (conn != null) {
				DatabaseConnection.closeConnection(conn, stmt);
			}
		}
	}

	// ======================================
	// DELETE THE CODE BELOW
	// ======================================


	// FIXME - DELETE BELOW
	protected final static String STATEMENT_UPDATE =
		"UPDATE Topic SET contents = ? WHERE name = ? AND virtualwiki = ?";
	protected final static String STATEMENT_UPDATE_ORACLE1 =
		"UPDATE Topic SET contents = EMPTY_CLOB() WHERE name = ? AND virtualwiki = ?";
	protected final static String STATEMENT_UPDATE_ORACLE2 =
		"SELECT contents FROM Topic WHERE name = ? AND virtualwiki = ? FOR UPDATE";
	protected final static String STATEMENT_INSERT =
		"INSERT INTO Topic( name, contents, virtualwiki ) VALUES ( ?, ?, ? )";
	protected final static String STATEMENT_INSERT_ORACLE =
		"INSERT INTO Topic( name, contents, virtualwiki ) VALUES ( ?, EMPTY_CLOB(), ? )";
	protected final static String STATEMENT_SET_LOCK =
		"INSERT INTO TopicLock( topic, sessionkey, lockat, virtualwiki ) VALUES( ?, ?, ?, ? )";
	protected final static String STATEMENT_CHECK_LOCK =
		"SELECT lockat, sessionkey FROM TopicLock WHERE topic = ? AND virtualwiki = ?";
	protected final static String STATEMENT_REMOVE_LOCK =
		"DELETE FROM TopicLock WHERE topic = ? AND virtualwiki = ?";
	protected final static String STATEMENT_READONLY_INSERT =
		"INSERT INTO TopicReadOnly( topic, virtualwiki ) VALUES ( ?, ? )";
	protected final static String STATEMENT_READONLY_DELETE =
		"DELETE FROM TopicReadOnly WHERE topic = ? AND virtualwiki = ?";
	protected final static String STATEMENT_ADD_VERSION =
		"INSERT INTO TopicVersion (virtualwiki, name, contents, versionat) VALUES( ?, ?, ?, ?)";
	protected final static String STATEMENT_ADD_VERSION_ORACLE1 =
		"INSERT INTO TopicVersion (virtualwiki, name, contents, versionat) VALUES( ?, ?, EMPTY_CLOB(), ?)";
	protected final static String STATEMENT_ADD_VERSION_ORACLE2 =
		"SELECT contents FROM TopicVersion WHERE name = ?  AND virtualwiki = ? ORDER BY versionat DESC FOR UPDATE";
	// FIXME - DELETE ABOVE

	protected final static String STATEMENT_GET_ALL =
		"SELECT versionat FROM TopicVersion WHERE name = ?  AND virtualwiki = ? ORDER BY versionat DESC";
	protected static final String STATEMENT_PURGE_DELETES =
		"DELETE FROM Topic WHERE virtualwiki = ? AND (contents = 'delete\n' or contents = '\n' or contents = '')";
	protected static final String STATEMENT_PURGE_TOPIC =
		"DELETE FROM Topic WHERE virtualwiki = ? AND name = ?";
	protected static final String STATEMENT_TOPICS_TO_PURGE =
		"SELECT name FROM Topic WHERE virtualwiki = ? AND (contents = 'delete\n' or contents = '\n' or contents = '')";
	protected static final String STATEMENT_ALL_TOPICS =
		"SELECT name, contents FROM Topic WHERE virtualwiki = ?";
	protected static final String STATEMENT_ALL_OLDER_TOPICS =
		"SELECT name, contents FROM Topic WHERE virtualwiki = ? AND versionat < ?";
	protected final static String STATEMENT_PURGE_VERSIONS =
		"DELETE FROM TopicVersion WHERE versionat < ? AND virtualwiki = ?";
	protected final static String STATEMENT_VERSION_FIND_ONE =
		"SELECT * FROM TopicVersion WHERE name = ?  AND virtualwiki = ? AND versionAt = ?";

	/**
	 *
	 */
	public DatabaseHandler() throws Exception {
		setDefaults(Locale.ENGLISH);
	}

	/**
	 *
	 */
	public static boolean isMySQL() {
		return Environment.getValue(Environment.PROP_DB_TYPE).equals(DB_TYPE_MYSQL);
	}

	/**
	 *
	 */
	public static boolean isOracle() {
		return Environment.getValue(Environment.PROP_DB_TYPE).equals(DB_TYPE_ORACLE);
	}

	/**
	 *
	 */
	public static String getDatabaseType() {
		return Environment.getValue(Environment.PROP_DB_TYPE);
	}

	/**
	 *
	 */
	private static boolean dbInitialized() {
		String sql = "select 1 from Topic ";
		try {
			WikiResultSet rs = DatabaseConnection.executeQuery(sql);
			return rs.next();
		} catch (Exception e) {
			// thrown if table doesn't exist, so safe to ignore
		}
		return false;
	}

	/**
	 *
	 */
	public void setDefaults(Locale locale) throws Exception {
		logger.debug("Setting defaults");
		// resources for i18n
		ResourceBundle messages = ResourceBundle.getBundle("ApplicationResources", locale);
		if (!DatabaseHandler.dbInitialized()) {
			//set up tables
			DatabaseInit.initialize();
		}
		String sql = null;
		WikiResultSet rs = null;
		sql = "select * from jmw_virtual_wiki ";
		try {
			rs = DatabaseConnection.executeQuery(sql);
		} catch (Exception e) {
			// return, tables not set up yet
			return;
		}
		if (rs.size() == 0) {
			addVirtualWiki(WikiBase.DEFAULT_VWIKI);
		}
		while (rs.next()) {
			String vWiki = rs.getString("virtual_wiki_name");
			// starting points
			setupSpecialPage(vWiki, messages.getString("specialpages.startingpoints"));
			// leftMenu
			setupSpecialPage(vWiki, messages.getString("specialpages.leftMenu"));
			// topArea
			setupSpecialPage(vWiki, messages.getString("specialpages.topArea"));
			// bottomArea
			setupSpecialPage(vWiki, messages.getString("specialpages.bottomArea"));
			// stylesheet
			setupSpecialPage(vWiki, messages.getString("specialpages.stylesheet"));
			// list of topics that only admin is allowed to edit/view by themselves
			setupSpecialPage(vWiki, messages.getString("specialpages.adminonlytopics"));
			if (!exists(vWiki, "SetUsername")) {
				write(vWiki, "", "SetUsername", DatabaseInit.DEFAULT_AUTHOR_IP_ADDRESS);
			}
		}
	}

	/**
	 *
	 */
	private void setupSpecialPage(String vWiki, String specialPage) throws Exception {
		if (!exists(vWiki, specialPage)) {
			logger.debug("Setting up " + specialPage);
			write(vWiki, WikiBase.readDefaultTopic(specialPage), specialPage, DatabaseInit.DEFAULT_AUTHOR_IP_ADDRESS);
		}
	}

	/**
	 *
	 */
	public String read(String virtualWiki, String topicName) throws Exception {
		// FIXME - virtualWiki should never be empty, fix callers
		if (virtualWiki == null || virtualWiki.length() == 0) {
			virtualWiki = WikiBase.DEFAULT_VWIKI;
		}
		Topic topic = lookupTopic(virtualWiki, topicName);
		return (topic == null) ? "" : topic.getTopicContent();
	}

	/**
	 *
	 */
	public void write(String virtualWiki, String contents, String topicName, String ipAddress) throws Exception {

		// FIXME - DELETE BELOW
		Connection conn = null;
		try {
			conn = DatabaseConnection.getConnection();
			if (!exists(virtualWiki, topicName)) {
				logger.debug("Inserting into topic " + topicName + ", " + contents);
				PreparedStatement insertStatement;
				if (DatabaseHandler.isOracle()) {
					boolean savedAutoCommit = conn.getAutoCommit();
					conn.setAutoCommit(false);
					insertStatement = conn.prepareStatement(STATEMENT_INSERT_ORACLE);
					insertStatement.setString(1, topicName);
					insertStatement.setString(2, virtualWiki);
					insertStatement.execute();
					insertStatement.close();
					conn.commit();
					insertStatement = conn.prepareStatement(
						STATEMENT_UPDATE_ORACLE2,
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE
					);
					insertStatement.setString(1, topicName);
					insertStatement.setString(2, virtualWiki);
					ResultSet rs = insertStatement.executeQuery();
					rs.next();
					OracleClobHelper.setClobValue(rs.getClob(1), contents);
					rs.close();
					insertStatement.close();
					conn.setAutoCommit(savedAutoCommit);
				} else {
					insertStatement = conn.prepareStatement(STATEMENT_INSERT);
					insertStatement.setString(1, topicName);
					insertStatement.setString(2, contents);
					insertStatement.setString(3, virtualWiki);
					insertStatement.execute();
					insertStatement.close();
				}
			} else {
				logger.debug("Updating topic " + topicName + " to " + contents);
				PreparedStatement updateStatement;
				if (DatabaseHandler.isOracle()) {
					boolean savedAutoCommit = conn.getAutoCommit();
					conn.setAutoCommit(false);
					updateStatement = conn.prepareStatement(STATEMENT_UPDATE_ORACLE1);
					updateStatement.setString(1, topicName);
					updateStatement.setString(2, virtualWiki);
					updateStatement.execute();
					updateStatement.close();
					conn.commit();
					updateStatement = conn.prepareStatement(
						STATEMENT_UPDATE_ORACLE2,
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE
					);
					updateStatement.setString(1, topicName);
					updateStatement.setString(2, virtualWiki);
					ResultSet rs = updateStatement.executeQuery();
					rs.next();
					OracleClobHelper.setClobValue(rs.getClob(1), contents);
					rs.close();
					updateStatement.close();
					conn.commit();
					conn.setAutoCommit(savedAutoCommit);
				} else {
					updateStatement = conn.prepareStatement(STATEMENT_UPDATE);
					updateStatement.setString(2, topicName);
					updateStatement.setString(1, contents);
					updateStatement.setString(3, virtualWiki);
					updateStatement.execute();
					updateStatement.close();
				}
			}
		} finally {
			DatabaseConnection.closeConnection(conn);
		}
		// FIXME - DELETE ABOVE

		Topic topic = lookupTopic(virtualWiki, topicName);
		if (topic == null) {
			topic = new Topic();
			topic.setName(topicName);
			topic.setVirtualWiki(virtualWiki);
			topic.setTopicContent(contents);
			this.addTopic(topic);
		} else {
			topic.setTopicContent(contents);
			// release any lock that is held by setting lock fields null
			topic.setLockedBy(-1);
			topic.setLockedDate(null);
			topic.setLockSessionKey(null);
			this.updateTopic(topic);
		}
		if (Environment.getBooleanValue(Environment.PROP_TOPIC_VERSIONING_ON)) {
			// write version
			addTopicVersion(virtualWiki, topicName, contents, new DBDate(), ipAddress);
		}
	}

	/**
	 *
	 */
	public boolean exists(String virtualWiki, String topicName) throws Exception {
		return (lookupTopic(virtualWiki, topicName) != null);
	}

	/**
	 *
	 */
	public boolean holdsLock(String virtualWiki, String topicName, String key) throws Exception {
		Topic topic = lookupTopic(virtualWiki, topicName);
		if (topic.getLockSessionKey() == null) {
			return lockTopic(virtualWiki, topicName, key);
		}
		// FIXME - old code included a check to see if last version was made after the time
		// the lock was taken.  that should be impossible with the new code.
		return true;
	}

	/**
	 *
	 */
	public boolean lockTopic(String virtualWiki, String topicName, String key) throws Exception {

		// FIXME - DELETE BELOW
		boolean addLock = true;
		Connection conn = null;
		try {
			conn = DatabaseConnection.getConnection();
			PreparedStatement checkLockStatement = conn.prepareStatement(STATEMENT_CHECK_LOCK);
			checkLockStatement.setString(1, topicName);
			checkLockStatement.setString(2, virtualWiki);
			ResultSet rs = checkLockStatement.executeQuery();
			if (rs.next()) {
				DBDate date = new DBDate(rs.getTimestamp("lockat"));
				DBDate now = new DBDate();
				logger.info("Already locked at " + date);
				long fiveMinutesAgo = now.getTime() - 60000 *
				Environment.getIntValue(Environment.PROP_TOPIC_EDIT_TIME_OUT);
				if (date.getTime() < fiveMinutesAgo) {
					logger.debug("Lock expired");
					PreparedStatement removeLockStatement = conn.prepareStatement(STATEMENT_REMOVE_LOCK);
					removeLockStatement.setString(1, topicName);
					removeLockStatement.setString(2, virtualWiki);
					removeLockStatement.execute();
					removeLockStatement.close();
				} else {
					addLock = false;
					String existingKey = rs.getString("sessionkey");
					rs.close();
					checkLockStatement.close();
					// if the key being locked with is the existing lock, then the user still has
					// the lock, otherwise, it must be locked by someone else
					boolean sameKey = existingKey.equals(key);
					logger.debug("Same key: " + sameKey);
//					return sameKey;
				}
			}
			if (addLock) {
				logger.debug("Setting lock");
				PreparedStatement setLockStatement = conn.prepareStatement(STATEMENT_SET_LOCK);
				setLockStatement.setString(1, topicName);
				setLockStatement.setString(2, key);
				setLockStatement.setTimestamp(3, (new DBDate()).asTimestamp());
				setLockStatement.setString(4, virtualWiki);
				setLockStatement.execute();
				setLockStatement.close();
				rs.close();
				checkLockStatement.close();
			}
		} finally {
			DatabaseConnection.closeConnection(conn);
		}
		// FIXME - DELETE ABOVE

		Topic topic = lookupTopic(virtualWiki, topicName);
		if (topic.getLockSessionKey() != null) {
			// a lock still exists, see if it was taken by the current user
			if (topic.getLockSessionKey().equals(key)) {
				// same user still has the lock, return true
				return true;
			}
			// see if the existing lock has expired
			Timestamp expireDate = new Timestamp(topic.getLockedDate().getTime() + (60000 * Environment.getIntValue(Environment.PROP_TOPIC_EDIT_TIME_OUT)));
			Timestamp now = new Timestamp(System.currentTimeMillis());
			if (now.before(expireDate)) {
				// lock is still valid, return false
				return false;
			}
		}
		topic.setLockSessionKey(key);
		topic.setLockedDate(new Timestamp(System.currentTimeMillis()));
		// FIXME - save author
		//topic.setLockedBy(authorId);
		updateTopic(topic);
		return true;
	}

	/**
	 *
	 */
	public void unlockTopic(String virtualWiki, String topicName) throws Exception {

		// FIXME - DELETE BELOW
		Connection conn = null;
		try {
			conn = DatabaseConnection.getConnection();
			PreparedStatement removeAnyLockStatement = conn.prepareStatement(STATEMENT_REMOVE_LOCK);
			removeAnyLockStatement.setString(1, topicName);
			removeAnyLockStatement.setString(2, virtualWiki);
			removeAnyLockStatement.execute();
			removeAnyLockStatement.close();
		} finally {
			DatabaseConnection.closeConnection(conn);
		}
		// FIXME - DELETE ABOVE

		Topic topic = lookupTopic(virtualWiki, topicName);
		topic.setLockSessionKey(null);
		topic.setLockedDate(null);
		topic.setLockedBy(-1);
		updateTopic(topic);
	}

	/**
	 *
	 */
	public boolean isTopicReadOnly(String virtualWiki, String topicName) throws Exception {
		Topic topic = lookupTopic(virtualWiki, topicName);
		return topic.getReadOnly();
	}

	/**
	 *
	 */
	public Collection getReadOnlyTopics(String virtualWiki) throws Exception {
		Collection all = new ArrayList();
		int virtualWikiId = lookupVirtualWikiId(virtualWiki);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseConnection.getConnection();
			stmt = conn.prepareStatement(STATEMENT_SELECT_TOPIC_READ_ONLY);
			stmt.setInt(1, virtualWikiId);
			stmt.setBoolean(2, true);
			rs = stmt.executeQuery();
			while (rs.next()) {
				all.add(rs.getString("topic_name"));
			}
		} finally {
			if (conn != null) {
				DatabaseConnection.closeConnection(conn, stmt, rs);
			}
		}
		return all;
	}

	/**
	 *
	 */
	public void addReadOnlyTopic(String virtualWiki, String topicName) throws Exception {
		Topic topic = lookupTopic(virtualWiki, topicName);
		topic.setReadOnly(true);
		updateTopic(topic);

		// FIXME - DELETE BELOW
		Connection conn = null;
		try {
			conn = DatabaseConnection.getConnection();
			PreparedStatement addReadOnlyStatement = conn.prepareStatement(STATEMENT_READONLY_INSERT);
			addReadOnlyStatement.setString(1, topicName);
			addReadOnlyStatement.setString(2, virtualWiki);
			addReadOnlyStatement.execute();
			addReadOnlyStatement.close();
		} finally {
			DatabaseConnection.closeConnection(conn);
		}
		// FIXME - DELETE ABOVE

	}

	/**
	 *
	 */
	public void removeReadOnlyTopic(String virtualWiki, String topicName) throws Exception {
		Topic topic = lookupTopic(virtualWiki, topicName);
		topic.setReadOnly(false);
		updateTopic(topic);

		// FIXME - DELETE BELOW
		Connection conn = null;
		try {
			conn = DatabaseConnection.getConnection();
			PreparedStatement deleteReadOnlyStatement = conn.prepareStatement(STATEMENT_READONLY_DELETE);
			deleteReadOnlyStatement.setString(1, topicName);
			deleteReadOnlyStatement.setString(2, virtualWiki);
			deleteReadOnlyStatement.execute();
			deleteReadOnlyStatement.close();
		} finally {
			DatabaseConnection.closeConnection(conn);
		}
		// FIXME - DELETE ABOVE

	}

	/**
	 *
	 */
	public Collection getVirtualWikiList() throws Exception {
		if (virtualWikiNameHash == null) {
			loadVirtualWikiHashes();
		}
		return virtualWikiNameHash.keySet();
	}

	/**
	 *
	 */
	public Collection purgeDeletesOracle(String virtualWiki) throws Exception {
		PreparedStatement stmt;
		ResultSet rs;
		Vector names = new Vector();
		Connection conn = null;
		try {
			conn = DatabaseConnection.getConnection();
			stmt = conn.prepareStatement(STATEMENT_ALL_TOPICS);
			stmt.setString(1, virtualWiki);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String contents = OracleClobHelper.getClobValue(rs.getClob(2));
				if (contents.length() == 0 || contents.equals("delete\n") || contents.equals("\n")) {
					names.add(rs.getString(1));
				}
			}
			rs.close();
			stmt.close();
			stmt = conn.prepareStatement(STATEMENT_PURGE_TOPIC);
			Iterator i = names.iterator();
			while (i.hasNext()) {
				String name = (String) i.next();
				stmt.setString(1, virtualWiki);
				stmt.setString(2, name);
				stmt.execute();
			}
			stmt.close();
		} finally {
			DatabaseConnection.closeConnection(conn);
		}
		return names;
	}

	/**
	 *
	 */
	public Collection purgeDeletes(String virtualWiki) throws Exception {
		if (DatabaseHandler.isOracle()) {
			return purgeDeletesOracle(virtualWiki);
		}
		Collection all = new ArrayList();
		// get list of stuff to be purged
		Connection conn = null;
		try {
			conn = DatabaseConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(STATEMENT_TOPICS_TO_PURGE);
			stmt.setString(1, virtualWiki);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String topicName = rs.getString("name");
				if (!PseudoTopicHandler.getInstance().isPseudoTopic(topicName)) {
					all.add(topicName);
				}
			}
			stmt.close();
			stmt = conn.prepareStatement(STATEMENT_PURGE_DELETES);
			stmt.setString(1, virtualWiki);
			stmt.execute();
			stmt.close();
		} finally {
			DatabaseConnection.closeConnection(conn);
		}
		return all;
	}

	/**
	 *
	 */
	public void purgeVersionsOlderThanOracle(String virtualWiki, DBDate date) throws Exception {
		PreparedStatement stmt;
		ResultSet rs;
		Vector names = new Vector();
		Connection conn = null;
		try {
			conn = DatabaseConnection.getConnection();
			stmt = conn.prepareStatement(STATEMENT_ALL_OLDER_TOPICS);
			stmt.setString(1, virtualWiki);
			stmt.setTimestamp(2, date.asTimestamp());
			rs = stmt.executeQuery();
			while (rs.next()) {
				String contents = OracleClobHelper.getClobValue(rs.getClob(2));
				if (contents.length() == 0 || contents.equals("delete\n") || contents.equals("\n")) {
					names.add(rs.getString(1));
				}
			}
			rs.close();
			stmt.close();
			stmt = conn.prepareStatement(STATEMENT_PURGE_TOPIC);
			Iterator i = names.iterator();
			while (i.hasNext()) {
				String name = (String) i.next();
				stmt.setString(1, virtualWiki);
				stmt.setString(2, name);
				stmt.execute();
			}
			stmt.close();
		} finally {
			DatabaseConnection.closeConnection(conn);
		}
	}

	/**
	 *
	 */
	public void purgeVersionsOlderThan(String virtualWiki, DBDate date) throws Exception {
		if (DatabaseHandler.isOracle()) {
			purgeVersionsOlderThanOracle(virtualWiki, date);
		}
		Connection conn = null;
		try {
			conn = DatabaseConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(STATEMENT_PURGE_VERSIONS);
			stmt.setTimestamp(1, date.asTimestamp());
			stmt.setString(2, virtualWiki);
			stmt.execute();
			stmt.close();
		} finally {
			DatabaseConnection.closeConnection(conn);
		}
	}

	/**
	 *
	 */
	public List getLockList(String virtualWiki) throws Exception {
		List all = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int virtualWikiId = lookupVirtualWikiId(virtualWiki);
		try {
			conn = DatabaseConnection.getConnection();
			stmt = conn.prepareStatement(STATEMENT_SELECT_TOPIC_LOCKED);
			stmt.setInt(1, virtualWikiId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				TopicLock lock = new TopicLock(
					virtualWiki,
					rs.getString("topic_name"),
					new DBDate(rs.getTimestamp("topic_lock_date")),
					rs.getString("topic_lock_session_key")
				);
				all.add(lock);
			}
		} finally {
			DatabaseConnection.closeConnection(conn, stmt, rs);
		}
		return all;
	}

	/**
	 *
	 */
	public String revisionContents(String virtualWiki, String topicName, Timestamp date) throws Exception {
		Connection conn = null;
		String contents;
		try {
			conn = DatabaseConnection.getConnection();
			PreparedStatement versionFindStatementOne = conn.prepareStatement(STATEMENT_VERSION_FIND_ONE);
			versionFindStatementOne.setString(1, topicName);
			versionFindStatementOne.setString(2, virtualWiki);
			versionFindStatementOne.setTimestamp(3, date);
			ResultSet rs = versionFindStatementOne.executeQuery();
			if (!rs.next()) {
				rs.close();
				versionFindStatementOne.close();
				return null;
			}
			if (DatabaseHandler.isOracle()) {
				contents = OracleClobHelper.getClobValue(rs.getClob("contents"));
			} else {
				contents = rs.getString("contents");
			}
			logger.debug("Contents @" + date + ": " + contents);
			rs.close();
			versionFindStatementOne.close();
		} finally {
			DatabaseConnection.closeConnection(conn);
		}
		return contents;
	}

	/**
	 *
	 */
	public String diff(String virtualWiki, String topicName, int topicVersionId1, int topicVersionId2, boolean useHtml) throws Exception {
		TopicVersion version1 = WikiBase.getInstance().getHandler().lookupTopicVersion(virtualWiki, topicName, topicVersionId1);
		TopicVersion version2 = WikiBase.getInstance().getHandler().lookupTopicVersion(virtualWiki, topicName, topicVersionId2);
		String contents1 = version1.getVersionContent();
		String contents2 = version2.getVersionContent();
		if (contents1 == null && contents2 == null) {
			logger.error("No versions found for " + topicVersionId1 + " against " + topicVersionId2);
			return "";
		}
		return DiffUtil.diff(contents1, contents2, useHtml);
	}

	/**
	 *
	 */
	public Date lastRevisionDate(String virtualWiki, String topicName) throws Exception {
		TopicVersion version = WikiBase.getInstance().getHandler().lookupLastTopicVersion(virtualWiki, topicName);
		return version.getEditDate();
	}

	/**
	 *
	 */
	public TopicVersion getTopicVersion(String context, String virtualWiki, String topicName, int topicVersionId) throws Exception {
		TopicVersion version = WikiBase.getInstance().getHandler().lookupTopicVersion(virtualWiki, topicName, topicVersionId);
		String cookedContents = WikiBase.getInstance().cook(
			context,
			virtualWiki,
			new BufferedReader(new StringReader(
				getVersionContents(virtualWiki, topicName, topicVersionId)
			))
		);
		version.setCookedContents(cookedContents);
		return version;
	}

	/**
	 *
	 */
	public String getVersionContents(String virtualWiki, String topicName, int topicVersionId) throws Exception {
		TopicVersion version = WikiBase.getInstance().getHandler().lookupTopicVersion(virtualWiki, topicName, topicVersionId);
		return version.getVersionContent();
	}
}
