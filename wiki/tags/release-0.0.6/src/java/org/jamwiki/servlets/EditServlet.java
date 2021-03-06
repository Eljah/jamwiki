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

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.jamwiki.Environment;
import org.jamwiki.PseudoTopicHandler;
import org.jamwiki.WikiBase;
import org.jamwiki.model.Topic;
import org.jamwiki.model.TopicVersion;
import org.jamwiki.model.WikiUser;
import org.jamwiki.parser.ParserInfo;
import org.jamwiki.search.SearchEngine;
import org.jamwiki.utils.Utilities;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 */
public class EditServlet extends JAMWikiServlet {

	private static Logger logger = Logger.getLogger(EditServlet.class);

	/**
	 *
	 */
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView next = new ModelAndView("wiki");
		try {
			if (mustLogin(request)) {
				// FIXME - hard coding
				next.addObject("errorMessage", "Editing a topic requires login");
				viewLogin(request, next, JAMWikiServlet.getTopicFromURI(request));
			} else if (isSave(request)) {
				save(request, next);
			} else if (isCancel(request)) {
				cancel(request, next);
			} else if (isPreview(request)) {
				edit(request, next);
			} else {
				edit(request, next);
			}
		} catch (Exception e) {
			viewError(request, next, e);
		}
		loadDefaults(request, next, this.pageInfo);
		return next;
	}

	/**
	 *
	 */
	private void cancel(HttpServletRequest request, ModelAndView next) throws Exception {
		String topicName = JAMWikiServlet.getTopicFromRequest(request);
		String virtualWiki = JAMWikiServlet.getVirtualWikiFromURI(request);
		Topic topic = WikiBase.getHandler().lookupTopic(virtualWiki, topicName);
		try {
			WikiBase.getHandler().unlockTopic(topic);
		} catch (Exception err) {
			// FIXME - hard coding
			throw new Exception("Unable to unlock topic " + virtualWiki + "/" + topicName);
		}
		// FIXME - the caching needs to be simplified
		JAMWikiServlet.removeCachedContents();
		viewTopic(request, next, topic.getName());
	}

	/**
	 *
	 */
	private void edit(HttpServletRequest request, ModelAndView next) throws Exception {
		request.getSession().setMaxInactiveInterval(60 * Environment.getIntValue(Environment.PROP_TOPIC_EDIT_TIME_OUT));
		String topicName = JAMWikiServlet.getTopicFromRequest(request);
		if (!StringUtils.hasText(topicName)) {
			// FIXME - hard coding
			throw new Exception("Invalid or missing topic name");
		}
		if (PseudoTopicHandler.isPseudoTopic(topicName)) {
			throw new Exception(topicName + " " + Utilities.getMessage("edit.exception.pseudotopic", request.getLocale()));
		}
		String virtualWiki = JAMWikiServlet.getVirtualWikiFromURI(request);
		Topic topic = WikiBase.getHandler().lookupTopic(virtualWiki, topicName);
		if (topic == null) {
			topic = new Topic();
			topic.setName(topicName);
		}
		if (topic.getReadOnly()) {
			// FIXME - hard coding
			throw new Exception("The topic " + topicName + " is read only");
		}
		if (topic.getAdminOnly() && !Utilities.isAdmin(request)) {
			// FIXME - hard coding
			next.addObject("errorMessage", "Editing administrative topics requires login");
			viewLogin(request, next, JAMWikiServlet.getTopicFromURI(request));
			return;
		}
		String key = request.getSession().getId();
		if (!WikiBase.getHandler().lockTopic(virtualWiki, topicName, key)) {
			// FIXME - hard coding
			throw new Exception("The topic " + topicName + " is locked");
		}
		String contents = null;
		String editComment = null;
		boolean minorEdit = false;
		String preview = null;
		if (isPreview(request)) {
			JAMWikiServlet.removeCachedContents();
			contents = (String)request.getParameter("contents");
			editComment = (String)request.getParameter("editComment");
			minorEdit = (request.getParameter("minorEdit") != null);
		} else {
			contents = WikiBase.readRaw(virtualWiki, topicName);
		}
		WikiUser user = Utilities.currentUser(request);
		ParserInfo parserInfo = new ParserInfo();
		parserInfo.setContext(request.getContextPath());
		parserInfo.setWikiUser(user);
		parserInfo.setUserIpAddress(request.getRemoteAddr());
		parserInfo.setVirtualWiki(virtualWiki);
		parserInfo.setMode(ParserInfo.MODE_PREVIEW);
		preview = WikiBase.parse(parserInfo, contents, topicName);
		String pageTitle = Utilities.getMessage("edit.title", request.getLocale(), topicName);
		this.pageInfo.setPageTitle(pageTitle);
		this.pageInfo.setTopicName(topicName);
		next.addObject("contents", contents);
		next.addObject("editComment", editComment);
		next.addObject("minorEdit", new Boolean(minorEdit));
		if (isPreview(request)) {
			Topic previewTopic = new Topic();
			previewTopic.setName(topicName);
			previewTopic.setTopicContent(preview);
			next.addObject(JAMWikiServlet.PARAMETER_TOPIC_OBJECT, previewTopic);
			this.pageInfo.setPageAction(JAMWikiServlet.ACTION_PREVIEW);
		} else {
			this.pageInfo.setPageAction(JAMWikiServlet.ACTION_EDIT);
		}
	}

	/**
	 *
	 */
	private boolean isCancel(HttpServletRequest request) {
		return isAction(request, "edit.action.cancel", JAMWikiServlet.ACTION_CANCEL);
	}

	/**
	 *
	 */
	private boolean isPreview(HttpServletRequest request) {
		return isAction(request, "edit.action.preview", JAMWikiServlet.ACTION_PREVIEW);
	}

	/**
	 *
	 */
	private boolean isSave(HttpServletRequest request) {
		return isAction(request, "edit.action.save", JAMWikiServlet.ACTION_SAVE);
	}

	/**
	 *
	 */
	private boolean mustLogin(HttpServletRequest request) {
		return (Environment.getBooleanValue(Environment.PROP_TOPIC_FORCE_USERNAME) && Utilities.currentUser(request) == null);
	}


	/**
	 *
	 */
	private void save(HttpServletRequest request, ModelAndView next) throws Exception {
		String topicName = request.getParameter(JAMWikiServlet.PARAMETER_TOPIC);
		String virtualWiki = JAMWikiServlet.getVirtualWikiFromURI(request);
		if (topicName == null) {
			logger.warn("Attempt to save null topic");
			// FIXME - hard coding
			throw new Exception("Topic must be specified");
		}
		Topic topic = WikiBase.getHandler().lookupTopic(virtualWiki, topicName);
		TopicVersion topicVersion = new TopicVersion();
		if (topic == null) {
			topic = new Topic();
			topic.setName(topicName);
			topic.setVirtualWiki(virtualWiki);
		}
		if (topic.getReadOnly()) {
			logger.warn("The topic " + topicName + " is read only and cannot be saved");
			// FIXME - hard coding
			throw new Exception("The topic " + topicName + " is read only and cannot be saved");
		}
		String key = request.getSession().getId();
		if (!WikiBase.getHandler().holdsLock(virtualWiki, topicName, key)) {
			logger.warn("The lock on " + topicName + " has timed out");
			// FIXME - hard coding
			throw new Exception("The lock on " + topicName + " has timed out");
		}
		String contents = request.getParameter("contents");
		if (contents == null) {
			logger.warn("The topic " + topicName + " has no content");
			// FIXME - hard coding
			throw new Exception("The topic " + topicName + " has no content");
		}
		// parse for signatures and other syntax that should not be saved in raw form
		WikiUser user = Utilities.currentUser(request);
		ParserInfo parserInfo = new ParserInfo();
		parserInfo.setContext(request.getContextPath());
		parserInfo.setWikiUser(user);
		parserInfo.setUserIpAddress(request.getRemoteAddr());
		parserInfo.setVirtualWiki(virtualWiki);
		parserInfo.setMode(ParserInfo.MODE_SAVE);
		contents = WikiBase.parsePreSave(parserInfo, contents);
		topic.setTopicContent(contents);
		topicVersion.setVersionContent(contents);
		topicVersion.setEditComment(request.getParameter("editComment"));
		topicVersion.setAuthorIpAddress(request.getRemoteAddr());
		if (user != null) {
			topicVersion.setAuthorId(new Integer(user.getUserId()));
		}
		WikiBase.getHandler().writeTopic(topic, topicVersion);
		// a save request has been made
		JAMWikiServlet.removeCachedContents();
		SearchEngine sedb = WikiBase.getSearchEngineInstance();
		sedb.indexText(virtualWiki, topicName, request.getParameter("contents"));
		viewTopic(request, next, topicName);
	}
}
