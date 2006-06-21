package org.jmwiki.persistency.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.jmwiki.utils.Encryption;

/**
 * Convert a VQWiki 2.7.8 database to JMWiki format.
 */
public class ConvertVQWiki {

	private static final Logger logger = Logger.getLogger(ConvertVQWiki.class);

	private static final String DATABASE_DB2 = "db2";
	private static final String DATABASE_MYSQL = "mysql";
	private static final String DATABASE_ORACLE = "oracle";
	private static final String DATABASE_POSTGRES = "postgres";
	private static final String INIT_SCRIPT_ANSI = "create_ansi.sql";
	private static final String INIT_SCRIPT_ORACLE = "create_oracle.sql";

	// FIXME - move to more logical location
	private static final String DEFAULT_PASSWORD = "password";
	private static final String DEFAULT_AUTHOR_LOGIN = "unknown_author";
	private static final String DEFAULT_AUTHOR_NAME = "Unknown Author";
	private static final int TOPIC_TYPE_DEFAULT = 1;
	private static final int EDIT_TYPE_DEFAULT = 1;

	/**
	 *
	 */
	protected static void convertFromDatabase() throws Exception {
		Connection conn = null;
		String sql = null;
		try {
			conn = DatabaseConnection.getConnection();
			Statement st = conn.createStatement();
			Statement results = conn.createStatement();
			ResultSet rs = null;
			// jmw_virtual_wiki
			sql = "INSERT INTO jmw_virtual_wiki ("
				+   "virtual_wiki_name "
				+ ") "
				+ "SELECT name FROM VirtualWiki ";
			st.executeUpdate(sql);
			// jmw_author
			sql = "INSERT INTO jmw_author ( "
				+   "login, virtual_wiki_id, confirmation_key, email, display_name, "
				+   "encoded_password, initial_ip_address, last_ip_address "
				+ ") "
				+ "SELECT WikiMember.wikiuser, jmw_virtual_wiki.virtual_wiki_id, "
				+ "WikiMember.userkey, WikiMember.email, WikiMember.wikiuser, "
				+ "'" + Encryption.encrypt(DEFAULT_PASSWORD) + "', "
				+ "'127.0.0.1', '127.0.0.1' "
				+ "FROM WikiMember, jmw_virtual_wiki "
				+ "WHERE WikiMember.virtualwiki = jmw_virtual_wiki.virtual_wiki_name ";
			st.executeUpdate(sql);
			sql = "INSERT INTO jmw_author ( "
				+   "login, virtual_wiki_id, display_name, "
				+   "encoded_password, initial_ip_address, last_ip_address "
				+ ") "
				+ "SELECT DISTINCT TopicChange.username, jmw_virtual_wiki.virtual_wiki_id, "
				+ "TopicChange.username, "
				+ "'" + Encryption.encrypt(DEFAULT_PASSWORD) + "', "
				+ "'127.0.0.1', '127.0.0.1' "
				+ "FROM TopicChange, jmw_virtual_wiki "
				+ "WHERE TopicChange.virtualwiki = jmw_virtual_wiki.virtual_wiki_name "
				+ "AND NOT EXISTS ( "
				+   "SELECT * FROM WikiMember "
				+   "WHERE TopicChange.username = WikiMember.wikiuser "
				+   "AND TopicChange.virtualwiki = WikiMember.virtualwiki "
				+ ") ";
			st.executeUpdate(sql);
			// jmw_topic
			sql = "insert into jmw_topic ( "
				+   "virtual_wiki_id, topic_name, topic_type "
				+ ") "
				+ "SELECT jmw_virtual_wiki.virtual_wiki_id, Topic.name, "
				+ DatabaseInit.TOPIC_TYPE_DEFAULT + " "
				+ "FROM Topic, jmw_virtual_wiki "
				+ "WHERE Topic.virtualwiki = jmw_virtual_wiki.virtual_wiki_name ";
			st.executeUpdate(sql);
			sql = "SELECT topic, virtualwiki FROM TopicReadOnly ";
			rs = results.executeQuery(sql);
			while (rs.next()) {
				sql = "UPDATE jmw_topic set topic_read_only = TRUE "
					+ "WHERE topic_name = '" + rs.getString("topic") + "' "
					+ "AND virtual_wiki_id = ( "
					+   "SELECT virtual_wiki_id FROM jmw_virtual_wiki "
					+   "WHERE virtual_wiki_name = '" + rs.getString("virtualwiki") + "' "
					+ ") ";
				st.executeUpdate(sql);
			}
			// jmw_topic_version
			// create default author for edits with no author
			sql = "INSERT INTO jmw_author ( "
				+   "login, virtual_wiki_id, display_name, "
				+   "encoded_password, initial_ip_address, last_ip_address "
				+ ") "
				+ "SELECT '" + DatabaseInit.DEFAULT_AUTHOR_LOGIN + "', "
				+ "virtual_wiki_id, '" + DatabaseInit.DEFAULT_AUTHOR_NAME + "', "
				+ "'" + Encryption.encrypt(DEFAULT_PASSWORD) + "', "
				+ "'127.0.0.1', '127.0.0.1' "
				+ "FROM jmw_virtual_wiki ";
			st.executeUpdate(sql);
			// get default author (will update later)
			sql = "SELECT author_id FROM jmw_author "
				+ "WHERE login = '" + DatabaseInit.DEFAULT_AUTHOR_LOGIN + "' ";
			rs  = results.executeQuery(sql);
			int authorId = 0;
			while (rs.next()) {
				authorId = rs.getInt("author_id");
			}
			sql = "INSERT INTO jmw_topic_version ( "
				+   "topic_id, version_content, author_id, "
				+   "edit_date, edit_type "
				+ ") "
				+ "SELECT jmw_topic.topic_id, TopicVersion.contents, "
				+ authorId + ", "
				+ "TopicVersion.versionat, "
				+ DatabaseInit.EDIT_TYPE_DEFAULT + " "
				+ "FROM TopicVersion, jmw_topic, jmw_virtual_wiki "
				+ "WHERE jmw_topic.topic_name = TopicVersion.name "
				+ "AND jmw_topic.virtual_wiki_id = jmw_virtual_wiki.virtual_wiki_id "
				+ "AND TopicVersion.virtualwiki = jmw_virtual_wiki.virtual_wiki_name ";
			st.executeUpdate(sql);
			// add links to authors
			sql = "SELECT jmw_author.author_id, jmw_topic_version.topic_version_id "
				+ "FROM jmw_author, TopicChange, jmw_topic_version, "
				+ "jmw_virtual_wiki, jmw_topic "
				+ "WHERE jmw_author.login = TopicChange.username "
				+ "AND jmw_author.virtual_wiki_id = jmw_virtual_wiki.virtual_wiki_id "
				+ "AND jmw_virtual_wiki.virtual_wiki_name = TopicChange.virtualwiki "
				+ "AND (TopicChange.changeat - jmw_topic_version.edit_date) < INTERVAL '5 seconds' "
				+ "AND jmw_topic_version.topic_id = jmw_topic.topic_id "
				+ "AND jmw_topic.virtual_wiki_id = jmw_virtual_wiki.virtual_wiki_id "
				+ "AND jmw_topic.topic_name = TopicChange.topic ";
			rs  = results.executeQuery(sql);
			while (rs.next()) {
				sql = "UPDATE jmw_topic_version "
					+ "SET author_id = " + rs.getInt("author_id") + " "
					+ "WHERE topic_version_id = " + rs.getInt("topic_version_id") + " ";
				st.executeUpdate(sql);
			}
			// update articles still using default author
			sql = "SELECT author_id, topic_id "
				+ "FROM jmw_author, jmw_topic "
				+ "WHERE jmw_author.login = '" + DatabaseInit.DEFAULT_AUTHOR_LOGIN + "' "
				+ "AND jmw_author.virtual_wiki_id = jmw_topic.virtual_wiki_id ";
			rs  = results.executeQuery(sql);
			while (rs.next()) {
				// FIXME - not all topics need to be updated, so this is inefficient
				sql = "UPDATE jmw_topic_version "
					+ "SET author_id = " + rs.getInt("author_id") + " "
					+ "WHERE author_id = " + authorId + " "
					+ "AND topic_id = " + rs.getInt("topic_id") + " ";
				st.executeUpdate(sql);
			}
			// jmw_notification
			sql = "INSERT INTO jmw_notification ( "
				+   "author_id, topic_id "
				+ ") "
				+ "SELECT jmw_author.author_id, jmw_topic.topic_id "
				+ "FROM jmw_author, jmw_topic, jmw_virtual_wiki, Notification "
				+ "WHERE jmw_topic.topic_name = Notification.topic "
				+ "AND jmw_virtual_wiki.virtual_wiki_name = Notification.virtualwiki "
				+ "AND jmw_virtual_wiki.virtual_wiki_id = jmw_topic.virtual_wiki_id "
				+ "AND jmw_author.login = Notification.wikiuser "
				+ "AND jmw_author.virtual_wiki_id = jmw_virtual_wiki.virtual_wiki_id ";
			st.executeUpdate(sql);
			// jmw_recent_change
			sql = "INSERT INTO jmw_recent_change ( "
				+   "topic_version_id, topic_id, "
				+   "topic_name, edit_date, author_id, display_name, "
				+   "edit_type, virtual_wiki_id, virtual_wiki_name "
				+ ") "
				+ "SELECT "
				+   "jmw_topic_version.topic_version_id, jmw_topic.topic_id, "
				+   "jmw_topic.topic_name, jmw_topic_version.edit_date, "
				+   "jmw_topic_version.author_id, jmw_author.display_name, "
				+   "jmw_topic_version.edit_type, jmw_virtual_wiki.virtual_wiki_id, "
				+   "jmw_virtual_wiki.virtual_wiki_name "
				+ "FROM jmw_topic, jmw_topic_version, jmw_author, jmw_virtual_wiki "
				+ "WHERE jmw_topic.topic_id = jmw_topic_version.topic_id "
				+ "AND jmw_topic_version.author_id = jmw_author.author_id "
				+ "AND jmw_topic.virtual_wiki_id = jmw_virtual_wiki.virtual_wiki_id ";
			st.executeUpdate(sql);
			st.close();
		} catch (Exception e) {
			if (sql != null) {
				e = new Exception("Failure while executing SQL: " + sql, e);
			}
			throw e;
		} finally {
			DatabaseConnection.closeConnection(conn);
		}
	}

	/**
	 * Optionally purge obsolete data from the database.
	 */
	private void purgeDatabase() throws Exception {
		Connection conn = null;
		String sql = null;
		try {
			conn = DatabaseConnection.getConnection();
			Statement st = conn.createStatement();
			sql = "DROP TABLE WikiMember";
			st.executeUpdate(sql);
			sql = "DROP TABLE Notification";
			st.executeUpdate(sql);
			sql = "DROP TABLE VirtualWiki";
			st.executeUpdate(sql);
			sql = "DROP TABLE TopicReadOnly";
			st.executeUpdate(sql);
			sql = "DROP TABLE TopicLock";
			st.executeUpdate(sql);
			sql = "DROP TABLE TopicChange";
			st.executeUpdate(sql);
			sql = "DROP TABLE TopicVersion";
			st.executeUpdate(sql);
			sql = "DROP TABLE Topic";
			st.executeUpdate(sql);
			st.close();
		} catch (Exception e) {
			if (sql != null) {
				throw new Exception("Failure while executing SQL: " + sql, e);
			}
			throw e;
		} finally {
			DatabaseConnection.closeConnection(conn);
		}
	}
}
