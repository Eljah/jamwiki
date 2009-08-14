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
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jamwiki.DataAccessException;
import org.jamwiki.DataHandler;
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.WikiException;
import org.jamwiki.WikiMessage;
import org.jamwiki.WikiVersion;
import org.jamwiki.model.WikiGroup;
import org.jamwiki.utils.Encryption;
import org.jamwiki.utils.WikiLogger;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * This class simply contains utility methods for upgrading database schemas
 * (if needed) between JAMWiki versions.  In general upgrade methods will only
 * be maintained for a few versions and then deleted - for example, JAMWiki version 10.0.0
 * does not need to keep the upgrade methods from JAMWiki 0.0.1 around.
 */
public class DatabaseUpgrades {

	private static final WikiLogger logger = WikiLogger.getLogger(DatabaseUpgrades.class.getName());

	/**
	 *
	 */
	private DatabaseUpgrades() {
	}

	private static TransactionDefinition getTransactionDefinition() {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		return def;
	}

	/**
	 * Special login method - it cannot be assumed that the database schema
	 * is unchanged, so do not use standard methods.
	 */
	public static boolean login(String username, String password) throws WikiException {
		WikiVersion oldVersion = new WikiVersion(Environment.getValue(Environment.PROP_BASE_WIKI_VERSION));
		if (!oldVersion.before(0, 7, 0)) {
			try {
				return (WikiBase.getDataHandler().authenticate(username, password));
			} catch (DataAccessException e) {
				logger.severe("Unable to authenticate user during upgrade", e);
				throw new WikiException(new WikiMessage("upgrade.error.fatal", e.getMessage()));
			}
		}
		try {
			Connection conn = DatabaseConnection.getConnection();
			WikiPreparedStatement stmt = new WikiPreparedStatement("select 1 from jam_wiki_user_info where login = ? and encoded_password = ?");
			if (!StringUtils.isBlank(password)) {
				password = Encryption.encrypt(password);
			}
			stmt.setString(1, username);
			stmt.setString(2, password);
			WikiResultSet rs = stmt.executeQuery(conn);
			return (rs.size() > 0);
		} catch (SQLException e) {
			logger.severe("Database failure while authenticating user", e);
			throw new WikiException(new WikiMessage("upgrade.error.fatal", e.getMessage()));
		}
	}

	/**
	 *
	 */
	public static List<WikiMessage> upgrade061(List<WikiMessage> messages) throws WikiException {
		TransactionStatus status = null;
		try {
			status = DatabaseConnection.startTransaction(getTransactionDefinition());
			String sql = null;
			Connection conn = DatabaseConnection.getConnection();
			// delete ROLE_DELETE
			sql = "delete from jam_role_map where role_name = 'ROLE_DELETE'";
			DatabaseConnection.executeUpdate(sql, conn);
			sql = "delete from jam_role where role_name = 'ROLE_DELETE'";
			DatabaseConnection.executeUpdate(sql, conn);
			messages.add(new WikiMessage("upgrade.message.db.data.deleted", "jam_role_map"));
			messages.add(new WikiMessage("upgrade.message.db.data.deleted", "jam_role"));
		} catch (SQLException e) {
			DatabaseConnection.rollbackOnException(status, e);
			logger.severe("Database failure during upgrade", e);
			throw new WikiException(new WikiMessage("upgrade.error.fatal", e.getMessage()));
		}
		DatabaseConnection.commit(status);
		return messages;
	}

	/**
	 *
	 */
	public static List<WikiMessage> upgrade063(List<WikiMessage> messages) {
		TransactionStatus status = null;
		try {
			status = DatabaseConnection.startTransaction(getTransactionDefinition());
			String sql = null;
			Connection conn = DatabaseConnection.getConnection();
			// increase the size of ip address columns
			String dbType = Environment.getValue(Environment.PROP_DB_TYPE);
			if (dbType.equals(DataHandler.DATA_HANDLER_DB2) || dbType.equals(DataHandler.DATA_HANDLER_DB2400)) {
				sql = "alter table jam_wiki_user alter column create_ip_address set data type varchar(39) ";
				DatabaseConnection.executeUpdate(sql, conn);
				sql = "alter table jam_wiki_user alter column last_login_ip_address set data type varchar(39) ";
				DatabaseConnection.executeUpdate(sql, conn);
			} else if (dbType.equals(DataHandler.DATA_HANDLER_MYSQL) || dbType.equals(DataHandler.DATA_HANDLER_ORACLE)) {
				sql = "alter table jam_wiki_user modify create_ip_address varchar(39) not null ";
				DatabaseConnection.executeUpdate(sql, conn);
				sql = "alter table jam_wiki_user modify last_login_ip_address varchar(39) not null ";
				DatabaseConnection.executeUpdate(sql, conn);
			} else if (dbType.equals(DataHandler.DATA_HANDLER_POSTGRES)) {
				sql = "alter table jam_wiki_user alter column create_ip_address type varchar(39) ";
				DatabaseConnection.executeUpdate(sql, conn);
				sql = "alter table jam_wiki_user alter column last_login_ip_address type varchar(39) ";
				DatabaseConnection.executeUpdate(sql, conn);
			} else {
				sql = "alter table jam_wiki_user alter column create_ip_address varchar(39) not null ";
				DatabaseConnection.executeUpdate(sql, conn);
				sql = "alter table jam_wiki_user alter column last_login_ip_address varchar(39) not null ";
				DatabaseConnection.executeUpdate(sql, conn);
			}
			messages.add(new WikiMessage("upgrade.message.db.column.modified", "create_ip_address", "jam_wiki_user"));
			messages.add(new WikiMessage("upgrade.message.db.column.modified", "last_login_ip_address", "jam_wiki_user"));
		} catch (SQLException e) {
			// do not throw this error and halt the upgrade process - changing the column size
			// is not required for systems that have already been successfully installed, it
			// is simply being done to keep new installs consistent with existing installs.
			messages.add(new WikiMessage("upgrade.error.nonfatal", e.getMessage()));
			logger.warning("Failure while updating database for IPv6 support.  See UPGRADE.txt for instructions on how to manually complete this optional step.", e);
			DatabaseConnection.rollbackOnException(status, e);
			status = null;	// so we do not try to commit
		}
		if (status != null) {
			DatabaseConnection.commit(status);
		}
		return messages;
	}

	/**
	 *
	 */
	public static List<WikiMessage> upgrade070(List<WikiMessage> messages) throws WikiException {
		TransactionStatus status = null;
		String dbType = Environment.getValue(Environment.PROP_DB_TYPE);
		try {
			status = DatabaseConnection.startTransaction(getTransactionDefinition());
			String sql = null;
			Connection conn = DatabaseConnection.getConnection();
			// add characters_changed column to jam_topic_version
			if (dbType.equals(DataHandler.DATA_HANDLER_ORACLE)) {
				sql = "alter table jam_topic_version add (characters_changed INTEGER) ";
			} else if (dbType.equals(DataHandler.DATA_HANDLER_MSSQL)) {
				sql = "alter table jam_topic_version add [characters_changed] int ";
			} else {
				sql = "alter table jam_topic_version add column characters_changed INTEGER ";
			}
			DatabaseConnection.executeUpdate(sql, conn);
			messages.add(new WikiMessage("upgrade.message.db.column.added", "characters_changed", "jam_topic_version"));
			// add characters_changed column to jam_recent_change
			if (dbType.equals(DataHandler.DATA_HANDLER_ORACLE)) {
				sql = "alter table jam_recent_change add (characters_changed INTEGER) ";
			} else if (dbType.equals(DataHandler.DATA_HANDLER_MSSQL)) {
				sql = "alter table jam_recent_change add [characters_changed] int ";
			} else {
				sql = "alter table jam_recent_change add column characters_changed INTEGER ";
			}
			DatabaseConnection.executeUpdate(sql, conn);
			messages.add(new WikiMessage("upgrade.message.db.column.added", "characters_changed", "jam_recent_change"));
			// copy columns from jam_wiki_user_info into jam_wiki_user
			if (dbType.equals(DataHandler.DATA_HANDLER_ORACLE)) {
				sql = "alter table jam_wiki_user add (email VARCHAR(100)) ";
			} else if (dbType.equals(DataHandler.DATA_HANDLER_MSSQL)) {
				sql = "alter table jam_wiki_user add email VARCHAR(100) ";
			} else {
				sql = "alter table jam_wiki_user add column email VARCHAR(100) ";
			}
			DatabaseConnection.executeUpdate(sql, conn);
			sql = "update jam_wiki_user set email = ( "
			    +   "select email "
			    +   "from jam_wiki_user_info "
			    +   "where jam_wiki_user.wiki_user_id = jam_wiki_user_info.wiki_user_id "
			    + ") ";
			DatabaseConnection.executeUpdate(sql, conn);
			messages.add(new WikiMessage("upgrade.message.db.column.added", "email", "jam_wiki_user"));
			// add new columns to jam_wiki_user
			if (dbType.equals(DataHandler.DATA_HANDLER_ORACLE)) {
				sql = "alter table jam_wiki_user add (editor VARCHAR(50)) ";
			} else if (dbType.equals(DataHandler.DATA_HANDLER_MSSQL)) {
				sql = "alter table jam_wiki_user add editor VARCHAR(50) ";
			} else {
				sql = "alter table jam_wiki_user add column editor VARCHAR(50) ";
			}
			DatabaseConnection.executeUpdate(sql, conn);
			messages.add(new WikiMessage("upgrade.message.db.column.added", "editor", "jam_wiki_user"));
			if (dbType.equals(DataHandler.DATA_HANDLER_ORACLE)) {
				sql = "alter table jam_wiki_user add (signature VARCHAR(255)) ";
			} else if (dbType.equals(DataHandler.DATA_HANDLER_MSSQL)) {
				sql = "alter table jam_wiki_user add signature VARCHAR(255) ";
			} else {
				sql = "alter table jam_wiki_user add column signature VARCHAR(255) ";
			}
			DatabaseConnection.executeUpdate(sql, conn);
			messages.add(new WikiMessage("upgrade.message.db.column.added", "signature", "jam_wiki_user"));
			if (dbType.equals(DataHandler.DATA_HANDLER_HSQL)) {
				DatabaseConnection.executeUpdate(HSqlQueryHandler.STATEMENT_CREATE_USERS_TABLE, conn);
			} else {
				DatabaseConnection.executeUpdate(AnsiQueryHandler.STATEMENT_CREATE_USERS_TABLE, conn);
			}
			sql = "insert into jam_users ( "
			    +    "username, password "
				+ ") "
				+ "select login, encoded_password "
				+ "from jam_wiki_user_info ";
			DatabaseConnection.executeUpdate(sql, conn);
			messages.add(new WikiMessage("upgrade.message.db.table.added", "jam_users"));
			sql = "alter table jam_wiki_user drop column remember_key";
			DatabaseConnection.executeUpdate(sql, conn);
			messages.add(new WikiMessage("upgrade.message.db.column.dropped", "remember_key", "jam_wiki_user"));
			if (dbType.equals(DataHandler.DATA_HANDLER_HSQL)) {
				DatabaseConnection.executeUpdate(HSqlQueryHandler.STATEMENT_CREATE_AUTHORITIES_TABLE, conn);
			} else if (dbType.equals(DataHandler.DATA_HANDLER_MYSQL)) {
				DatabaseConnection.executeUpdate(MySqlQueryHandler.STATEMENT_CREATE_AUTHORITIES_TABLE, conn);
			} else {
				DatabaseConnection.executeUpdate(AnsiQueryHandler.STATEMENT_CREATE_AUTHORITIES_TABLE, conn);
			}
			sql = "insert into jam_authorities ( "
			    +    "username, authority "
				+ ") "
				+ "select jam_wiki_user.login, jam_role_map.role_name "
				+ "from jam_wiki_user, jam_role_map "
				+ "where jam_wiki_user.wiki_user_id = jam_role_map.wiki_user_id ";
			DatabaseConnection.executeUpdate(sql, conn);
			messages.add(new WikiMessage("upgrade.message.db.table.added", "jam_authorities"));
			if (dbType.equals(DataHandler.DATA_HANDLER_HSQL)) {
				DatabaseConnection.executeUpdate(HSqlQueryHandler.STATEMENT_CREATE_GROUP_AUTHORITIES_TABLE, conn);
			} else if (dbType.equals(DataHandler.DATA_HANDLER_MYSQL)) {
				DatabaseConnection.executeUpdate(MySqlQueryHandler.STATEMENT_CREATE_GROUP_AUTHORITIES_TABLE, conn);
			} else {
				DatabaseConnection.executeUpdate(AnsiQueryHandler.STATEMENT_CREATE_GROUP_AUTHORITIES_TABLE, conn);
			}
			sql = "insert into jam_group_authorities ( "
			    +    "group_id, authority "
				+ ") "
				+ "select jam_group.group_id, jam_role_map.role_name "
				+ "from jam_group, jam_role_map "
				+ "where jam_group.group_id = jam_role_map.group_id ";
			DatabaseConnection.executeUpdate(sql, conn);
			messages.add(new WikiMessage("upgrade.message.db.table.added", "jam_group_authorities"));
			sql = "drop table jam_role_map ";
			DatabaseConnection.executeUpdate(sql, conn);
			messages.add(new WikiMessage("upgrade.message.db.table.dropped", "jam_role_map"));
			if (dbType.equals(DataHandler.DATA_HANDLER_HSQL)) {
				DatabaseConnection.executeUpdate(MySqlQueryHandler.STATEMENT_CREATE_GROUP_MEMBERS_TABLE, conn);
			} else {
				DatabaseConnection.executeUpdate(AnsiQueryHandler.STATEMENT_CREATE_GROUP_MEMBERS_TABLE, conn);
			}
			sql = "select group_id from jam_group where group_name = '" + WikiGroup.GROUP_REGISTERED_USER + "'";
			WikiResultSet rs = DatabaseConnection.executeQuery(sql, conn);
			int groupId = rs.getInt("group_id");
			sql = "select username from jam_users ";
			rs = DatabaseConnection.executeQuery(sql, conn);
			int id = 1;
			while (rs.next()) {
				sql = "insert into jam_group_members ( "
				    +   "id, username, group_id "
				    + ") values ( "
				    +   id + ", '" + StringEscapeUtils.escapeSql(rs.getString("username")) + "', " + groupId
				    + ") ";
				DatabaseConnection.executeUpdate(sql, conn);
				id++;
			}
			messages.add(new WikiMessage("upgrade.message.db.table.added", "jam_group_members"));
			sql = "drop table jam_wiki_user_info";
			DatabaseConnection.executeUpdate(sql, conn);
			messages.add(new WikiMessage("upgrade.message.db.table.dropped", "jam_wiki_user_info"));
		} catch (SQLException e) {
			DatabaseConnection.rollbackOnException(status, e);
			try {
				DatabaseConnection.executeUpdate(AnsiQueryHandler.STATEMENT_DROP_GROUP_MEMBERS_TABLE);
			} catch (Exception ex) {}
			try {
				DatabaseConnection.executeUpdate(AnsiQueryHandler.STATEMENT_DROP_GROUP_AUTHORITIES_TABLE);
			} catch (Exception ex) {}
			try {
				DatabaseConnection.executeUpdate(AnsiQueryHandler.STATEMENT_DROP_AUTHORITIES_TABLE);
			} catch (Exception ex) {}
			try {
				DatabaseConnection.executeUpdate(AnsiQueryHandler.STATEMENT_DROP_USERS_TABLE);
			} catch (Exception ex) {}
			logger.severe("Database failure during upgrade", e);
			throw new WikiException(new WikiMessage("upgrade.error.fatal", e.getMessage()));
		}
		DatabaseConnection.commit(status);
		try {
			// perform a second transaction to populate the new columns.  this code is in its own
			// transaction since if it fails the upgrade can still be considered successful.
			status = DatabaseConnection.startTransaction(getTransactionDefinition());
			String sql = null;
			if (dbType.equals(DataHandler.DATA_HANDLER_ORACLE)) {
				sql = "update jam_topic_version set characters_changed = ( "
				       +   "select (dbms_lob.getlength(current_version.version_content) - dbms_lob.getlength(previous_version.version_content)) "
				       +   "from jam_topic_version current_version "
				       +   "left outer join jam_topic_version previous_version "
				       +   "on current_version.previous_topic_version_id = previous_version.topic_version_id "
				       +   "where jam_topic_version.topic_version_id = current_version.topic_version_id "
				       + ") ";
			} else {
				sql = "update jam_topic_version set characters_changed = ( "
				       +   "select (char_length(current_version.version_content) - char_length(coalesce(previous_version.version_content, ''))) "
				       +   "from jam_topic_version current_version "
				       +   "left outer join jam_topic_version previous_version "
				       +   "on current_version.previous_topic_version_id = previous_version.topic_version_id "
				       +   "where jam_topic_version.topic_version_id = current_version.topic_version_id "
				       + ") ";
			}
			Connection conn = DatabaseConnection.getConnection();
			DatabaseConnection.executeUpdate(sql, conn);
			messages.add(new WikiMessage("upgrade.message.db.column.populated", "characters_changed", "jam_topic_version"));
		} catch (SQLException e) {
			messages.add(new WikiMessage("upgrade.error.nonfatal", e.getMessage()));
			// do not throw this error and halt the upgrade process - populating the field
			// is not required for existing systems.
			logger.warning("Failure while populating characters_changed colum in jam_topic_version.  See UPGRADE.txt for instructions on how to manually complete this optional step.", e);
			try {
				DatabaseConnection.rollbackOnException(status, e);
			} catch (Exception ex) {
				// ignore
			}
			status = null; // so we do not try to commit
		}
		if (status != null) {
			DatabaseConnection.commit(status);
		}
		try {
			WikiBase.getDataHandler().reloadRecentChanges();
		} catch (DataAccessException e) {
			logger.warning("Failure during upgrade while reloading recent changes.  Please use the Special:Maintenance page to complete this step.", e);
			messages.add(new WikiMessage("upgrade.error.nonfatal", e.getMessage()));
		}
		messages.add(new WikiMessage("upgrade.message.db.column.populated", "characters_changed", "jam_recent_change"));
		return messages;
	}

	/**
	 *
	 */
	public static List<WikiMessage> upgrade080(List<WikiMessage> messages) throws WikiException {
		String dbType = Environment.getValue(Environment.PROP_DB_TYPE);
		TransactionStatus status = null;
		try {
			status = DatabaseConnection.startTransaction(getTransactionDefinition());
			String sql = null;
			Connection conn = DatabaseConnection.getConnection();
			// add wiki_user_display column to jam_topic_version
			if (dbType.equals(DataHandler.DATA_HANDLER_ORACLE)) {
				sql = "alter table jam_topic_version add (wiki_user_display VARCHAR(100)) ";
			} else if (dbType.equals(DataHandler.DATA_HANDLER_MSSQL)) {
				sql = "alter table jam_topic_version add wiki_user_display VARCHAR(100) ";
			} else {
				sql = "alter table jam_topic_version add column wiki_user_display VARCHAR(100) ";
			}
			DatabaseConnection.executeUpdate(sql, conn);
			sql = "update jam_topic_version set wiki_user_display = wiki_user_ip_address ";
			DatabaseConnection.executeUpdate(sql, conn);
			sql = "alter table jam_topic_version drop column wiki_user_ip_address ";
			DatabaseConnection.executeUpdate(sql, conn);
			messages.add(new WikiMessage("upgrade.message.db.column.added", "wiki_user_display", "jam_topic_version"));
			messages.add(new WikiMessage("upgrade.message.db.column.dropped", "wiki_user_ip_address", "jam_topic_version"));
			// add wiki_user_display column to jam_file_version
			if (dbType.equals(DataHandler.DATA_HANDLER_ORACLE)) {
				sql = "alter table jam_file_version add (wiki_user_display VARCHAR(100)) ";
			} else if (dbType.equals(DataHandler.DATA_HANDLER_MSSQL)) {
				sql = "alter table jam_file_version add wiki_user_display VARCHAR(100) ";
			} else {
				sql = "alter table jam_file_version add column wiki_user_display VARCHAR(100) ";
			}
			DatabaseConnection.executeUpdate(sql, conn);
			sql = "update jam_file_version set wiki_user_display = wiki_user_ip_address ";
			DatabaseConnection.executeUpdate(sql, conn);
			sql = "alter table jam_file_version drop column wiki_user_ip_address ";
			DatabaseConnection.executeUpdate(sql, conn);
			messages.add(new WikiMessage("upgrade.message.db.column.added", "wiki_user_display", "jam_file_version"));
			messages.add(new WikiMessage("upgrade.message.db.column.dropped", "wiki_user_ip_address", "jam_file_version"));
		} catch (SQLException e) {
			DatabaseConnection.rollbackOnException(status, e);
			logger.severe("Database failure during upgrade", e);
			throw new WikiException(new WikiMessage("upgrade.error.fatal", e.getMessage()));
		}
		DatabaseConnection.commit(status);
		try {
			// perform a second transaction to assign ROLE_IMPORT.  this code is in its own
			// transaction since if it fails the upgrade can still be considered successful.
			status = DatabaseConnection.startTransaction(getTransactionDefinition());
			Connection conn = DatabaseConnection.getConnection();
			String sql = "INSERT into jam_role (role_name) values ('ROLE_IMPORT') ";
			DatabaseConnection.executeUpdate(sql, conn);
			messages.add(new WikiMessage("upgrade.message.db.data.added", "jam_role"));
			sql = "INSERT into jam_authorities (authority, username) "
			    + "select 'ROLE_IMPORT', username "
			    + "from jam_authorities where authority = 'ROLE_ADMIN' ";
			DatabaseConnection.executeUpdate(sql, conn);
			messages.add(new WikiMessage("upgrade.message.db.data.added", "jam_authorities"));
		} catch (SQLException e) {
			messages.add(new WikiMessage("upgrade.error.nonfatal", e.getMessage()));
			// do not throw this error and halt the upgrade process - populating the field
			// is not required for existing systems.
			logger.warning("Failure while populating characters_changed colum in jam_topic_version.  See UPGRADE.txt for instructions on how to manually complete this optional step.", e);
			try {
				DatabaseConnection.rollbackOnException(status, e);
			} catch (Exception ex) {
				// ignore
			}
			status = null; // so we do not try to commit
		}
		if (status != null) {
			DatabaseConnection.commit(status);
		}
		return messages;
	}
}