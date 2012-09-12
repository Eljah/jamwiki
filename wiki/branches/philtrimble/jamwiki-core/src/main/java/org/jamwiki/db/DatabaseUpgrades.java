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
import java.util.Locale;
import java.util.TimeZone;

import org.jamwiki.DataAccessException;
import org.jamwiki.WikiBase;
import org.jamwiki.WikiException;
import org.jamwiki.WikiMessage;
import org.jamwiki.model.WikiUser;
import org.jamwiki.utils.WikiLogger;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * This class simply contains utility methods for upgrading database schemas
 * (if needed) between JAMWiki versions.  These methods are typically called automatically
 * by the UpgradeServlet when an upgrade is detected and will automatically upgrade the
 * database schema without the need for manual intervention from the user.
 *
 * In general upgrade methods will only be maintained for two major releases and then
 * deleted - for example, JAMWiki version 0.9.0 will not support upgrading from versions
 * prior to 0.7.0.
 */
public class DatabaseUpgrades {

	private static final WikiLogger logger = WikiLogger.getLogger(DatabaseUpgrades.class.getName());

	/**
	 *
	 */
	private DatabaseUpgrades() {
	}

	/**
	 *
	 */
	private static TransactionDefinition getTransactionDefinition() {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		return def;
	}

	/**
	 * Per http://hsqldb.sourceforge.net/doc/2.0/guide/deployment-chapt.html#deployment_upgrade-sect
	 * ("It is strongly recommended to execute SHUTDOWN COMPACT after an automatic
	 * upgrade from previous versions") after upgrading from HSQL from 1.8 to 2.2
	 * run a "SHUTDOWN COMPACT" to ensure that file data is successfully upgraded.
	 */
	public static void upgradeHsql22(List<WikiMessage> messages) throws WikiException {
		TransactionStatus status = null;
		try {
			status = DatabaseConnection.startTransaction(getTransactionDefinition());
			Connection conn = DatabaseConnection.getConnection();
			logger.info("Compacting HSQL database after upgrade to HSQL 2.2");
			WikiBase.getDataHandler().queryHandler().executeUpgradeUpdate("UPGRADE_110_HSQL_SHUTDOWN_COMPACT", conn);
		} catch (SQLException e) {
			DatabaseConnection.rollbackOnException(status, e);
			logger.error("Database failure during upgrade", e);
			throw new WikiException(new WikiMessage("upgrade.error.fatal", e.getMessage()));
		}
		DatabaseConnection.commit(status);
		try {
			// database has been shut down, so close the current connection pool to
			// refresh all database connections.
			DatabaseConnection.closeConnectionPool();
		} catch (SQLException e) {
			logger.error("Unable to close database connection pool", e);
			throw new WikiException(new WikiMessage("upgrade.error.fatal", e.getMessage()));
		}
	}

	/**
	 * Perform the required database upgrade steps when upgrading from versions
	 * older than JAMWiki 1.1.
	 */
	public static void upgrade110(List<WikiMessage> messages) throws WikiException {
		TransactionStatus status = null;
		try {
			status = DatabaseConnection.startTransaction(getTransactionDefinition());
			Connection conn = DatabaseConnection.getConnection();
			// add the log_sub_type column to the jam_log and jam_recent_change tables
			WikiBase.getDataHandler().queryHandler().executeUpgradeUpdate("UPGRADE_110_ADD_LOG_SUB_TYPE", conn);
			messages.add(new WikiMessage("upgrade.message.db.column.added", "log_sub_type", "jam_log"));
			WikiBase.getDataHandler().queryHandler().executeUpgradeUpdate("UPGRADE_110_ADD_RECENT_CHANGE_LOG_SUB_TYPE", conn);
			messages.add(new WikiMessage("upgrade.message.db.column.added", "log_sub_type", "jam_recent_change"));
			// add the jam_user_block table
			WikiBase.getDataHandler().queryHandler().executeUpgradeUpdate("STATEMENT_CREATE_USER_BLOCK_TABLE", conn);
			messages.add(new WikiMessage("upgrade.message.db.table.added", "jam_user_block"));
			// drop & re-add the jam_topic_links table
			try {
				WikiBase.getDataHandler().queryHandler().executeUpgradeUpdate("STATEMENT_DROP_TOPIC_LINKS_TABLE", conn);
				WikiBase.getDataHandler().queryHandler().executeUpgradeUpdate("UPGRADE_110_DROP_TOPIC_LINKS_INDEX", conn);
				messages.add(new WikiMessage("upgrade.message.db.table.dropped", "jam_topic_links"));
			} catch (SQLException e) {
				// table may not exist if upgrading from prior to JAMWiki 1.0
			}
			WikiBase.getDataHandler().queryHandler().executeUpgradeUpdate("STATEMENT_CREATE_TOPIC_LINKS_TABLE", conn);
			messages.add(new WikiMessage("upgrade.message.db.table.added", "jam_topic_links"));
		} catch (SQLException e) {
			DatabaseConnection.rollbackOnException(status, e);
			logger.error("Database failure during upgrade", e);
			throw new WikiException(new WikiMessage("upgrade.error.fatal", e.getMessage()));
		}
		DatabaseConnection.commit(status);
		try {
			// perform a separate transaction to update existing data.  this code is in its own
			// transaction since if it fails the upgrade can still be considered successful.
			status = DatabaseConnection.startTransaction(getTransactionDefinition());
			Connection conn = DatabaseConnection.getConnection();
			// set the log_sub_type value in the jam_log table
			WikiBase.getDataHandler().queryHandler().executeUpgradeUpdate("UPGRADE_110_UPDATE_LOG_SUB_TYPE_UNDELETE", conn);
			WikiBase.getDataHandler().queryHandler().executeUpgradeUpdate("UPGRADE_110_UPDATE_LOG_SUB_TYPE_DELETE", conn);
			messages.add(new WikiMessage("upgrade.message.db.data.updated", "jam_log"));
			// set the log_sub_type value in the jam_recent_change table
			WikiBase.getDataHandler().queryHandler().executeUpgradeUpdate("UPGRADE_110_UPDATE_RECENT_CHANGE_LOG_SUB_TYPE_UNDELETE", conn);
			WikiBase.getDataHandler().queryHandler().executeUpgradeUpdate("UPGRADE_110_UPDATE_RECENT_CHANGE_LOG_SUB_TYPE_DELETE", conn);
			messages.add(new WikiMessage("upgrade.message.db.data.updated", "jam_recent_change"));
			// add an index to the jam_topic_links table
			WikiBase.getDataHandler().queryHandler().executeUpgradeUpdate("STATEMENT_CREATE_TOPIC_LINKS_INDEX", conn);
			messages.add(new WikiMessage("upgrade.message.db.data.updated", "jam_topic_links"));
		} catch (SQLException e) {
			messages.add(new WikiMessage("upgrade.error.nonfatal", e.getMessage()));
			// do not throw this error and halt the upgrade process - populating the field
			// is not required for existing systems.
			logger.warn("Non-fatal error while upgrading.", e);
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
	}

	/**
	 * Perform the required database upgrade steps when upgrading from versions
	 * older than JAMWiki 1.2.
	 */
	public static void upgrade120(List<WikiMessage> messages) throws WikiException {
		TransactionStatus status = null;
		try {
			status = DatabaseConnection.startTransaction(getTransactionDefinition());
			Connection conn = DatabaseConnection.getConnection();
			// initialize sequences
			if (WikiBase.getDataHandler().queryHandler().executeUpgradeUpdate("STATEMENT_CREATE_SEQUENCES", conn)) {
				messages.add(new WikiMessage("upgrade.message.db.object.added", "sequences"));
			}
			// create ROLE_REGISTER
			WikiBase.getDataHandler().queryHandler().executeUpgradeUpdate("UPGRADE_120_ADD_ROLE_REGISTER", conn);
			messages.add(new WikiMessage("upgrade.message.db.data.updated", "jam_role"));
			WikiBase.getDataHandler().queryHandler().executeUpgradeUpdate("UPGRADE_120_ADD_ROLE_REGISTER_TO_ANONYMOUS", conn);
			messages.add(new WikiMessage("upgrade.message.db.data.updated", "jam_group_authorities"));
			// add the jam_file_data table
			WikiBase.getDataHandler().queryHandler().executeUpgradeUpdate("STATEMENT_CREATE_FILE_DATA_TABLE", conn);
			messages.add(new WikiMessage("upgrade.message.db.table.added", "jam_file_data"));
		} catch (SQLException e) {
			DatabaseConnection.rollbackOnException(status, e);
			logger.error("Database failure during upgrade", e);
			throw new WikiException(new WikiMessage("upgrade.error.fatal", e.getMessage()));
		}
		DatabaseConnection.commit(status);
	}

	/**
	 * Perform the required database upgrade steps when upgrading from versions
	 * older than JAMWiki 1.3.
	 */
	public static void upgrade130(List<WikiMessage> messages) throws WikiException {
		TransactionStatus status = null;
		try {
			status = DatabaseConnection.startTransaction(getTransactionDefinition());
			Connection conn = DatabaseConnection.getConnection();
			// initialize sequences
			if (WikiBase.getDataHandler().queryHandler().executeUpgradeUpdate("STATEMENT_CREATE_SEQUENCES", conn)) {
				messages.add(new WikiMessage("upgrade.message.db.object.added", "sequences"));
			}
			// New tables as of JAMWiki 1.3
			WikiBase.getDataHandler().queryHandler().executeUpgradeUpdate("STATEMENT_CREATE_USER_PREFERENCES_DEFAULTS_TABLE", conn);
			messages.add(new WikiMessage("upgrade.message.db.table.added", "jam_user_preferences_defaults"));
			WikiBase.getDataHandler().queryHandler().executeUpgradeUpdate("STATEMENT_CREATE_USER_PREFERENCES_TABLE", conn);
			messages.add(new WikiMessage("upgrade.message.db.table.added", "jam_user_preferences"));
			WikiBase.getDataHandler().writeUserPreferenceDefault(WikiUser.USER_PREFERENCE_DEFAULT_LOCALE, Locale.getDefault().getLanguage(),WikiUser.USER_PREFERENCES_GROUP_INTERNATIONALIZATION,1);
			WikiBase.getDataHandler().writeUserPreferenceDefault(WikiUser.USER_PREFERENCE_TIMEZONE, TimeZone.getDefault().getID(),WikiUser.USER_PREFERENCES_GROUP_INTERNATIONALIZATION,2);
			WikiBase.getDataHandler().writeUserPreferenceDefault(WikiUser.USER_PREFERENCE_DATETIME_FORMAT, null,WikiUser.USER_PREFERENCES_GROUP_INTERNATIONALIZATION,3);
			WikiBase.getDataHandler().writeUserPreferenceDefault(WikiUser.USER_PREFERENCE_PREFERRED_EDITOR, "toolbar",WikiUser.USER_PREFERENCES_GROUP_EDITING,1);
			WikiBase.getDataHandler().writeUserPreferenceDefault(WikiUser.USER_PREFERENCE_SIGNATURE, null,WikiUser.USER_PREFERENCES_GROUP_EDITING,2);
			// Create default values for user preferences.
			messages.add(new WikiMessage("upgrade.message.db.data.updated", "jam_user_preferences_defaults"));
			// Migrate existing user preferences to new tables
			WikiBase.getDataHandler().queryHandler().executeUpgradeUpdate("UPGRADE_130_MIGRATE_USER_PREFERENCES_DEFAULT_LOCALE", conn);
			WikiBase.getDataHandler().queryHandler().executeUpgradeUpdate("UPGRADE_130_MIGRATE_USER_PREFERENCES_EDITOR", conn);
			WikiBase.getDataHandler().queryHandler().executeUpgradeUpdate("UPGRADE_130_MIGRATE_USER_PREFERENCES_SIGNATURE", conn);
			messages.add(new WikiMessage("upgrade.message.db.data.updated", "jam_user_preferences"));
			// Drop old user preference columns from jam_wiki_user
			WikiBase.getDataHandler().queryHandler().executeUpgradeUpdate("UPGRADE_130_REMOVE_WIKI_USER_TABLE_COLUMN_DEFAULT_LOCALE", conn);
			messages.add(new WikiMessage("upgrade.message.db.data.updated", "jam_wiki_user"));
			WikiBase.getDataHandler().queryHandler().executeUpgradeUpdate("UPGRADE_130_REMOVE_WIKI_USER_TABLE_COLUMN_EDITOR", conn);
			messages.add(new WikiMessage("upgrade.message.db.data.updated", "jam_wiki_user"));
			WikiBase.getDataHandler().queryHandler().executeUpgradeUpdate("UPGRADE_130_REMOVE_WIKI_USER_TABLE_COLUMN_SIGNATURE", conn);
			messages.add(new WikiMessage("upgrade.message.db.data.updated", "jam_wiki_user"));
		} catch (DataAccessException e) {
			DatabaseConnection.rollbackOnException(status, e);
			logger.error("Database failure during upgrade", e);
			throw new WikiException(new WikiMessage("upgrade.error.fatal", e.getMessage()));
		} catch (SQLException e) {
			DatabaseConnection.rollbackOnException(status, e);
			logger.error("Database failure during upgrade", e);
			throw new WikiException(new WikiMessage("upgrade.error.fatal", e.getMessage()));
		}
		DatabaseConnection.commit(status);
	}
}
