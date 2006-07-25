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
package org.jamwiki.servlets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.model.Topic;
import org.jamwiki.model.WikiUser;
import org.jamwiki.persistency.PersistencyHandler;
import org.jamwiki.persistency.db.DatabaseConnection;
import org.jamwiki.persistency.db.DatabaseHandler;
import org.jamwiki.persistency.file.FileHandler;
import org.jamwiki.utils.Encryption;
import org.jamwiki.utils.Utilities;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * The <code>AdminServlet</code> servlet is the servlet which allows the administrator
 * to perform administrative actions on the wiki.
 */
public class AdminServlet extends JAMWikiServlet {

	private static Logger logger = Logger.getLogger(AdminServlet.class.getName());

	/**
	 * This method handles the request after its parent class receives control.
	 *
	 * @param request - Standard HttpServletRequest object.
	 * @param response - Standard HttpServletResponse object.
	 * @return A <code>ModelAndView</code> object to be handled by the rest of the Spring framework.
	 */
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView next = new ModelAndView("wiki");
		try {
			String function = request.getParameter("function");
			if (function == null) function = "";
			// FIXME - hard coding of "function" values
			if (!Utilities.isAdmin(request)) {
				String redirect = "Special:Admin";
				if (isTopic(request, "Special:Convert")) {
					redirect = "Special:Convert";
				} else if (isTopic(request, "Special:Delete")) {
					redirect = "Special:Delete";
				}
				next.addObject("errorMessage", "Only administrators may access this page");
				viewLogin(request, next, redirect);
				loadDefaults(request, next, this.pageInfo);
				return next;
			}
			if (isTopic(request, "Special:Convert")) {
				// FIXME - hard coding of "function" values
				if (function.equals("Convert to File")) {
					convertToFile(request, next);
				} else if (function.equals("Convert to Database")) {
					convertToDatabase(request, next);
				} else {
					convertView(request, next);
				}
				loadDefaults(request, next, this.pageInfo);
				return next;
			}
			if (isTopic(request, "Special:Delete")) {
				if (function.equals("Delete")) {
					delete(request, next);
				} else {
					deleteView(request, next);
				}
				loadDefaults(request, next, this.pageInfo);
				return next;
			}
			if (!StringUtils.hasText(function)) {
				view(request, next);
			}
			if (function.equals("refreshIndex")) {
				refreshIndex(request, next);
			}
			if (function.equals("properties")) {
				properties(request, next);
			}
			if (function.equals("addVirtualWiki")) {
				addVirtualWiki(request, next);
			}
			if (function.equals("recentChanges")) {
				recentChanges(request, next);
			}
			if (function.equals("readOnly")) {
				readOnly(request, next);
			}
			// FIXME - remove this
			readOnlyList(request, next);
		} catch (Exception e) {
			viewError(request, next, e);
		}
		loadDefaults(request, next, this.pageInfo);
		return next;
	}

	/**
	 *
	 */
	private void addVirtualWiki(HttpServletRequest request, ModelAndView next) throws Exception {
		String newWiki = request.getParameter("newVirtualWiki");
		this.pageInfo.setPageAction(JAMWikiServlet.ACTION_ADMIN);
		this.pageInfo.setSpecial(true);
		this.pageInfo.setPageTitle("Special:Admin");
		WikiUser user = Utilities.currentUser(request);
		try {
			logger.debug("Adding new Wiki: " + newWiki);
			WikiBase.getHandler().writeVirtualWiki(newWiki);
			String message = Utilities.getMessage("admin.message.virtualwikiadded", request.getLocale());
			next.addObject("message", message);
			WikiBase.initialise(request.getLocale(), user);
		} catch (Exception e) {
			logger.error("Failure while adding virtual wiki " + newWiki, e);
			String message = "Failure while adding virtual wiki " + newWiki + ": " + e.getMessage();
			next.addObject("message", message);
		}
	}

	/**
	 *
	 */
	private void convertToDatabase(HttpServletRequest request, ModelAndView next) throws Exception {
		try {
			FileHandler fromHandler = new FileHandler();
			DatabaseHandler toHandler = new DatabaseHandler();
			Vector messages = WikiBase.getHandler().convert(fromHandler, toHandler);
			// FIXME - hard coding
			next.addObject("message", "Database values successfully written to files.  You may need to logout and re-login.");
			next.addObject("messages", messages);
		} catch (Exception e) {
			// FIXME - hard coding
			logger.error("Failure while executing database-to-file conversion", e);
			next.addObject("errorMessage", "Failure while executing database-to-file-conversion: " + e.getMessage());
		}
		this.pageInfo.setPageAction(JAMWikiServlet.ACTION_ADMIN_CONVERT);
		this.pageInfo.setSpecial(true);
		this.pageInfo.setPageTitle("Special:Convert");
	}

	/**
	 *
	 */
	private void convertToFile(HttpServletRequest request, ModelAndView next) throws Exception {
		try {
			FileHandler toHandler = new FileHandler();
			DatabaseHandler fromHandler = new DatabaseHandler();
			Vector messages = WikiBase.getHandler().convert(fromHandler, toHandler);
			// FIXME - hard coding
			next.addObject("message", "Database values successfully written to files.  You may need to logout and re-login.");
			next.addObject("messages", messages);
		} catch (Exception e) {
			// FIXME - hard coding
			logger.error("Failure while executing database-to-file conversion", e);
			next.addObject("errorMessage", "Failure while executing database-to-file-conversion: " + e.getMessage());
		}
		this.pageInfo.setPageAction(JAMWikiServlet.ACTION_ADMIN_CONVERT);
		this.pageInfo.setSpecial(true);
		this.pageInfo.setPageTitle("Special:Convert");
	}

	/**
	 *
	 */
	private void convertView(HttpServletRequest request, ModelAndView next) throws Exception {
		this.pageInfo.setPageAction(JAMWikiServlet.ACTION_ADMIN_CONVERT);
		this.pageInfo.setSpecial(true);
		this.pageInfo.setPageTitle("Special:Convert");
	}

	/**
	 *
	 */
	private void delete(HttpServletRequest request, ModelAndView next) throws Exception {
		String topicName = JAMWikiServlet.getTopicFromRequest(request);
		String virtualWiki = JAMWikiServlet.getVirtualWikiFromURI(request);
		this.pageInfo.setSpecial(true);
		this.pageInfo.setTopicName(topicName);
		this.pageInfo.setPageAction(JAMWikiServlet.ACTION_ADMIN_DELETE);
		this.pageInfo.setPageTitle("Delete " + topicName);
		try {
			if (topicName == null) {
				next.addObject("errorMessage", "No topic found");
				return;
			}
			Topic topic = WikiBase.getHandler().lookupTopic(virtualWiki, topicName);
			WikiBase.getHandler().deleteTopic(topic);
			// FIXME - hard coding
			next.addObject("message", "Topic " + topicName + " deleted successfully");
		} catch (Exception e) {
			// FIXME - hard coding
			logger.error("Failure while deleting topic " + topicName, e);
			next.addObject("errorMessage", "Failure while deleting topic " + topicName + ": " + e.getMessage());
		}
	}

	/**
	 *
	 */
	private void deleteView(HttpServletRequest request, ModelAndView next) throws Exception {
		String topicName = JAMWikiServlet.getTopicFromRequest(request);
		if (topicName == null) {
			next.addObject("errorMessage", "No topic found");
		}
		this.pageInfo.setTopicName(topicName);
		this.pageInfo.setPageAction(JAMWikiServlet.ACTION_ADMIN_DELETE);
		this.pageInfo.setPageTitle("Delete " + topicName);
		this.pageInfo.setSpecial(true);
	}

	/**
	 *
	 */
	private void properties(HttpServletRequest request, ModelAndView next) throws Exception {
		this.pageInfo.setPageAction(JAMWikiServlet.ACTION_ADMIN);
		this.pageInfo.setSpecial(true);
		this.pageInfo.setPageTitle("Special:Admin");
		try {
			Environment.setValue(
				Environment.PROP_RECENT_CHANGES_DAYS,
				request.getParameter(Environment.PROP_RECENT_CHANGES_DAYS)
			);
			Environment.setIntValue(
				Environment.PROP_SEARCH_INDEX_REFRESH_INTERVAL,
				Integer.parseInt(request.getParameter(Environment.PROP_SEARCH_INDEX_REFRESH_INTERVAL))
			);
			Environment.setValue(
				Environment.PROP_EMAIL_SMTP_HOST,
				request.getParameter(Environment.PROP_EMAIL_SMTP_HOST)
			);
			Environment.setValue(
				Environment.PROP_EMAIL_SMTP_USERNAME,
				request.getParameter(Environment.PROP_EMAIL_SMTP_USERNAME)
			);
			if (StringUtils.hasText(request.getParameter(Environment.PROP_EMAIL_SMTP_PASSWORD))) {
				Encryption.setEncryptedProperty(
					Environment.PROP_EMAIL_SMTP_PASSWORD,
					request.getParameter(Environment.PROP_EMAIL_SMTP_PASSWORD)
				);
				next.addObject("smtpPassword", request.getParameter(Environment.PROP_EMAIL_SMTP_PASSWORD));
			}
			Environment.setValue(
				Environment.PROP_EMAIL_REPLY_ADDRESS,
				request.getParameter(Environment.PROP_EMAIL_REPLY_ADDRESS)
			);
			Environment.setBooleanValue(
				Environment.PROP_TOPIC_VERSIONING_ON,
				request.getParameter(Environment.PROP_TOPIC_VERSIONING_ON) != null
			);
			Environment.setBooleanValue(
				Environment.PROP_PARSER_ALLOW_HTML,
				request.getParameter(Environment.PROP_PARSER_ALLOW_HTML) != null
			);
			Environment.setBooleanValue(
				Environment.PROP_TOPIC_FORCE_USERNAME,
				request.getParameter(Environment.PROP_TOPIC_FORCE_USERNAME) != null
			);
			Environment.setBooleanValue(
				Environment.PROP_TOPIC_ALLOW_VWIKI_LIST,
				request.getParameter(Environment.PROP_TOPIC_ALLOW_VWIKI_LIST) != null
			);
			Environment.setBooleanValue(
				Environment.PROP_SEARCH_EXTLINKS_INDEXING_ENABLED,
				request.getParameter(Environment.PROP_SEARCH_EXTLINKS_INDEXING_ENABLED) != null
			);
			Environment.setValue(
				Environment.PROP_BASE_FILE_DIR,
				request.getParameter(Environment.PROP_BASE_FILE_DIR)
			);
			int persistenceType = Integer.parseInt(request.getParameter(Environment.PROP_BASE_PERSISTENCE_TYPE));
			if (persistenceType == WikiBase.FILE) {
				Environment.setValue(Environment.PROP_BASE_PERSISTENCE_TYPE, "FILE");
			} else if (persistenceType == WikiBase.DATABASE) {
				Environment.setValue(Environment.PROP_BASE_PERSISTENCE_TYPE, "DATABASE");
			}
			if (request.getParameter(Environment.PROP_DB_DRIVER) != null) {
				Environment.setValue(
					Environment.PROP_DB_DRIVER,
					request.getParameter(Environment.PROP_DB_DRIVER)
				);
				Environment.setValue(
					Environment.PROP_DB_URL,
					request.getParameter(Environment.PROP_DB_URL)
				);
				Environment.setValue(
					Environment.PROP_DB_USERNAME,
					request.getParameter(Environment.PROP_DB_USERNAME)
				);
				if (StringUtils.hasText(request.getParameter(Environment.PROP_DB_PASSWORD))) {
					Encryption.setEncryptedProperty(
						Environment.PROP_DB_PASSWORD,
						request.getParameter(Environment.PROP_DB_PASSWORD)
					);
					next.addObject("dbPassword", request.getParameter(Environment.PROP_DB_PASSWORD));
				}
				Environment.setIntValue(
					Environment.PROP_DBCP_MAX_ACTIVE,
					Integer.parseInt(request.getParameter(Environment.PROP_DBCP_MAX_ACTIVE))
				);
				Environment.setIntValue(
					Environment.PROP_DBCP_MAX_IDLE,
					Integer.parseInt(request.getParameter(Environment.PROP_DBCP_MAX_IDLE))
				);
				Environment.setBooleanValue(
					Environment.PROP_DBCP_TEST_ON_BORROW,
					request.getParameter(Environment.PROP_DBCP_TEST_ON_BORROW) != null
				);
				Environment.setBooleanValue(
					Environment.PROP_DBCP_TEST_ON_RETURN,
					request.getParameter(Environment.PROP_DBCP_TEST_ON_RETURN) != null
				);
				Environment.setBooleanValue(
					Environment.PROP_DBCP_TEST_WHILE_IDLE,
					request.getParameter(Environment.PROP_DBCP_TEST_WHILE_IDLE) != null
				);
				Environment.setIntValue(
					Environment.PROP_DBCP_MIN_EVICTABLE_IDLE_TIME,
					Integer.parseInt(request.getParameter(Environment.PROP_DBCP_MIN_EVICTABLE_IDLE_TIME))
				);
				Environment.setIntValue(
					Environment.PROP_DBCP_TIME_BETWEEN_EVICTION_RUNS,
					Integer.parseInt(request.getParameter(Environment.PROP_DBCP_TIME_BETWEEN_EVICTION_RUNS))
				);
				Environment.setIntValue(
					Environment.PROP_DBCP_NUM_TESTS_PER_EVICTION_RUN,
					Integer.parseInt(request.getParameter(Environment.PROP_DBCP_NUM_TESTS_PER_EVICTION_RUN))
				);
				Environment.setIntValue(
					Environment.PROP_DBCP_WHEN_EXHAUSTED_ACTION,
					Integer.parseInt(request.getParameter(Environment.PROP_DBCP_WHEN_EXHAUSTED_ACTION))
				);
				Environment.setValue(
					Environment.PROP_DBCP_VALIDATION_QUERY,
					request.getParameter(Environment.PROP_DBCP_VALIDATION_QUERY)
				);
				Environment.setBooleanValue(
					Environment.PROP_DBCP_REMOVE_ABANDONED,
					request.getParameter(Environment.PROP_DBCP_REMOVE_ABANDONED) != null
				);
				Environment.setIntValue(
					Environment.PROP_DBCP_REMOVE_ABANDONED_TIMEOUT,
					Integer.parseInt(request.getParameter(Environment.PROP_DBCP_REMOVE_ABANDONED_TIMEOUT))
				);
				Environment.setBooleanValue(
					Environment.PROP_DBCP_LOG_ABANDONED,
					request.getParameter(Environment.PROP_DBCP_LOG_ABANDONED) != null
				);
			}
			Environment.setBooleanValue(
				Environment.PROP_TOPIC_USE_PREVIEW,
				request.getParameter(Environment.PROP_TOPIC_USE_PREVIEW) != null
			);
			Environment.setValue(
				Environment.PROP_BASE_DEFAULT_TOPIC,
				request.getParameter(Environment.PROP_BASE_DEFAULT_TOPIC)
			);
			Environment.setValue(
				Environment.PROP_PARSER_CLASS,
				request.getParameter(Environment.PROP_PARSER_CLASS)
			);
			int maxFileSizeInKB = Integer.parseInt(request.getParameter(Environment.PROP_FILE_MAX_FILE_SIZE));
			Environment.setIntValue(
				Environment.PROP_FILE_MAX_FILE_SIZE,
				maxFileSizeInKB * 1000
			);
			Environment.setValue(
				Environment.PROP_FILE_DIR_FULL_PATH,
				request.getParameter(Environment.PROP_FILE_DIR_FULL_PATH)
			);
			Environment.setValue(
				Environment.PROP_FILE_DIR_RELATIVE_PATH,
				request.getParameter(Environment.PROP_FILE_DIR_RELATIVE_PATH)
			);
			if (request.getParameter(Environment.PROP_DB_TYPE) != null) {
				Environment.setValue(
					Environment.PROP_DB_TYPE,
					request.getParameter(Environment.PROP_DB_TYPE)
				);
			}
			Environment.setBooleanValue(
				Environment.PROP_PARSER_TOC,
				request.getParameter(Environment.PROP_PARSER_TOC) != null
			);
			/*
			int membershipType = Integer.parseInt(request.getParameter(Environment.PROP_USERGROUP_TYPE));
			String usergroupType;
			if (membershipType == WikiBase.LDAP) {
				usergroupType = "LDAP";
			} else if (membershipType == WikiBase.DATABASE) {
				usergroupType = "DATABASE";
			} else {
				usergroupType = "0";
			}
			Environment.setValue(Environment.PROP_USERGROUP_TYPE, usergroupType);
			String[] autoFill = {
				Environment.PROP_USERGROUP_FACTORY,
				Environment.PROP_USERGROUP_URL,
				Environment.PROP_USERGROUP_USERNAME,
				Environment.PROP_USERGROUP_PASSWORD,
				Environment.PROP_USERGROUP_BASIC_SEARCH,
				Environment.PROP_USERGROUP_SEARCH_RESTRICTIONS,
				Environment.PROP_USERGROUP_USERID_FIELD,
				Environment.PROP_USERGROUP_FULLNAME_FIELD,
				Environment.PROP_USERGROUP_MAIL_FIELD,
				Environment.PROP_USERGROUP_DETAILVIEW
			};
			for (int i = 0; i < autoFill.length; i++) {
				if (request.getParameter(autoFill[i]) != null) {
					if (autoFill[i].equals(Environment.PROP_USERGROUP_PASSWORD) && StringUtils.hasText(request.getParameter(autoFill[i]))) {
						Encryption.setEncryptedProperty(
							Environment.PROP_USERGROUP_PASSWORD,
							request.getParameter(autoFill[i])
						);
						next.addObject("userGroupPassword", request.getParameter(autoFill[i]));
					} else {
						Environment.setValue(autoFill[i], request.getParameter(autoFill[i]));
					}
				}
			}
			*/
			if (Environment.getValue(Environment.PROP_BASE_FILE_DIR) == null) {
				// if home directory set empty, use system home directory
				String dir = System.getProperty("user.home") + System.getProperty("file.separator") + "wiki";
				Environment.setValue(Environment.PROP_BASE_FILE_DIR, dir);
			}
			if (WikiBase.getPersistenceType() == WikiBase.DATABASE) {
				// initialize connection pool in its own try-catch to avoid an error
				// causing property values not to be saved.
				try {
					DatabaseConnection.setPoolInitialized(false);
				} catch (Exception e) {
					String message = e.getMessage();
					next.addObject("message", message);
				}
			}
			Environment.saveProperties();
			WikiBase.initialise(request.getLocale(), null);
			String message = Utilities.getMessage("admin.message.changessaved", request.getLocale());
			next.addObject("message", message);
		} catch (Exception e) {
			// FIXME - hard coding
			logger.error("Failure while processing property values", e);
			String message = "Failure while processing property values: " + e.getMessage();
			next.addObject("message", message);
		}
	}

	/**
	 *
	 */
	private void readOnly(HttpServletRequest request, ModelAndView next) throws Exception {
		this.pageInfo.setPageAction(JAMWikiServlet.ACTION_ADMIN);
		this.pageInfo.setSpecial(true);
		this.pageInfo.setPageTitle("Special:Admin");
		String virtualWiki = JAMWikiServlet.getVirtualWikiFromURI(request);
		if (request.getParameter("addReadOnly") != null) {
			String topicName = request.getParameter("readOnlyTopic");
			WikiBase.getHandler().writeReadOnlyTopic(virtualWiki, topicName);
		}
		if (request.getParameter("removeReadOnly") != null) {
			String[] topics = request.getParameterValues("markRemove");
			for (int i = 0; i < topics.length; i++) {
				String topicName = topics[i];
				WikiBase.getHandler().deleteReadOnlyTopic(virtualWiki, topicName);
			}
		}
	}

	/**
	 *
	 */
	private void readOnlyList(HttpServletRequest request, ModelAndView next) throws Exception {
		String virtualWiki = JAMWikiServlet.getVirtualWikiFromURI(request);
		Collection readOnlyTopics = new ArrayList();
		try {
			readOnlyTopics = WikiBase.getHandler().getReadOnlyTopics(virtualWiki);
			next.addObject("readOnlyTopics", readOnlyTopics);
		} catch (Exception e) {
			// Ignore database error - probably just an invalid setting, the
			// user may not have config'd yet
		}
	}

	/**
	 *
	 */
	private void recentChanges(HttpServletRequest request, ModelAndView next) throws Exception {
		try {
			// FIXME - database specific
			if (WikiBase.getHandler() instanceof DatabaseHandler) {
				WikiBase.getHandler().reloadRecentChanges();
				// FIXME - hard coding
				next.addObject("message", "Recent changes successfully loaded");
			} else {
				next.addObject("message", "Recent changes can only be reloaded while in database persistency mode");
			}
		} catch (Exception e) {
			logger.error("Failure while loading recent changes", e);
			next.addObject("errorMessage", "Failure while loading recent changes: " + e.getMessage());
		}
		this.pageInfo.setPageAction(JAMWikiServlet.ACTION_ADMIN);
		this.pageInfo.setSpecial(true);
		this.pageInfo.setPageTitle("Special:Admin");
	}

	/**
	 *
	 */
	private void refreshIndex(HttpServletRequest request, ModelAndView next) throws Exception {
		this.pageInfo.setPageAction(JAMWikiServlet.ACTION_ADMIN);
		this.pageInfo.setSpecial(true);
		this.pageInfo.setPageTitle("Special:Admin");
		try {
			WikiBase.getSearchEngineInstance().refreshIndex();
			String message = Utilities.getMessage("admin.message.indexrefreshed", request.getLocale());
			next.addObject("message", message);
		} catch (Exception e) {
			// FIXME - hard coding
			logger.error("Failure while refreshing search index", e);
			String message = "Failure while refreshing search index: " + e.getMessage();
			next.addObject("message", message);
		}
	}

	/**
	 *
	 */
	private void view(HttpServletRequest request, ModelAndView next) throws Exception {
		this.pageInfo.setPageAction(JAMWikiServlet.ACTION_ADMIN);
		this.pageInfo.setSpecial(true);
		this.pageInfo.setPageTitle("Special:Admin");
	}
}
