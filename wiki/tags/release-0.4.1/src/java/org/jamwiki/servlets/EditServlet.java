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

import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.WikiException;
import org.jamwiki.WikiMessage;
import org.jamwiki.model.Topic;
import org.jamwiki.model.TopicVersion;
import org.jamwiki.model.WikiUser;
import org.jamwiki.parser.ParserInput;
import org.jamwiki.parser.ParserDocument;
import org.jamwiki.utils.DiffUtil;
import org.jamwiki.utils.LinkUtil;
import org.jamwiki.utils.NamespaceHandler;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiLink;
import org.jamwiki.utils.WikiLogger;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 */
public class EditServlet extends JAMWikiServlet {

	private static WikiLogger logger = WikiLogger.getLogger(EditServlet.class.getName());

	/**
	 *
	 */
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView next = new ModelAndView("wiki");
		WikiPageInfo pageInfo = new WikiPageInfo();
		try {
			ModelAndView loginRequired = loginRequired(request);
			if (loginRequired != null) {
				return loginRequired;
			}
			if (isSave(request)) {
				save(request, next, pageInfo);
			} else {
				edit(request, next, pageInfo);
			}
		} catch (Exception e) {
			return viewError(request, e);
		}
		loadDefaults(request, next, pageInfo);
		return next;
	}

	/**
	 *
	 */
	private void edit(HttpServletRequest request, ModelAndView next, WikiPageInfo pageInfo) throws Exception {
		String topicName = JAMWikiServlet.getTopicFromRequest(request);
		String virtualWiki = JAMWikiServlet.getVirtualWikiFromURI(request);
		Topic topic = loadTopic(virtualWiki, topicName);
		// topic name might be updated by loadTopic
		topicName = topic.getName();
		int lastTopicVersionId = retrieveLastTopicVersionId(request, virtualWiki, topicName);
		next.addObject("lastTopicVersionId", new Integer(lastTopicVersionId));
		loadEdit(request, next, pageInfo, virtualWiki, topicName, true);
		String contents = null;
		if (isPreview(request)) {
			preview(request, next, pageInfo);
			return;
		}
		pageInfo.setAction(WikiPageInfo.ACTION_EDIT);
		if (StringUtils.hasText(request.getParameter("topicVersionId"))) {
			// editing an older version
			int topicVersionId = Integer.parseInt(request.getParameter("topicVersionId"));
			TopicVersion topicVersion = WikiBase.getHandler().lookupTopicVersion(topicName, topicVersionId);
			if (topicVersion == null) {
				throw new WikiException(new WikiMessage("common.exception.notopic"));
			}
			contents = topicVersion.getVersionContent();
			if (lastTopicVersionId != topicVersionId) {
				next.addObject("topicVersionId", new Integer(topicVersionId));
			}
		} else if (StringUtils.hasText(request.getParameter("section"))) {
			// editing a section of a topic
			int section = (new Integer(request.getParameter("section"))).intValue();
			ParserDocument parserDocument = Utilities.parseSlice(request, virtualWiki, topicName, section);
			contents = parserDocument.getContent();
		} else {
			// editing a full new or existing topic
			contents = (topic == null) ? "" : topic.getTopicContent();
		}
		next.addObject("contents", contents);
	}

	/**
	 *
	 */
	private boolean isPreview(HttpServletRequest request) {
		return StringUtils.hasText(request.getParameter("preview"));
	}

	/**
	 *
	 */
	private boolean isSave(HttpServletRequest request) {
		return StringUtils.hasText(request.getParameter("save"));
	}

	/**
	 *
	 */
	private void loadEdit(HttpServletRequest request, ModelAndView next, WikiPageInfo pageInfo, String virtualWiki, String topicName, boolean useSection) throws Exception {
		pageInfo.setPageTitle(new WikiMessage("edit.title", topicName));
		pageInfo.setTopicName(topicName);
		WikiLink wikiLink = LinkUtil.parseWikiLink(topicName);
		String namespace = wikiLink.getNamespace();
		if (namespace != null && namespace.equals(NamespaceHandler.NAMESPACE_CATEGORY)) {
			loadCategoryContent(next, virtualWiki, topicName);
		}
		if (request.getParameter("editComment") != null) {
			next.addObject("editComment", request.getParameter("editComment"));
		}
		if (useSection && request.getParameter("section") != null) {
			next.addObject("section", request.getParameter("section"));
		}
		next.addObject("minorEdit", new Boolean(request.getParameter("minorEdit") != null));
	}

	/**
	 * Initialize topic values for the topic being edited.  If a topic with
	 * the specified name already exists then it will be initialized,
	 * otherwise a new topic is created.
	 */
	private Topic loadTopic(String virtualWiki, String topicName) throws Exception {
		Topic topic = this.initializeTopic(virtualWiki, topicName);
		if (topic.getReadOnly()) {
			throw new WikiException(new WikiMessage("error.readonly"));
		}
		return topic;
	}

	/**
	 *
	 */
	private ModelAndView loginRequired(HttpServletRequest request) throws Exception {
		String topicName = JAMWikiServlet.getTopicFromRequest(request);
		String virtualWiki = JAMWikiServlet.getVirtualWikiFromURI(request);
		if (!StringUtils.hasText(topicName) || !StringUtils.hasText(virtualWiki)) {
			return null;
		}
		if (Environment.getBooleanValue(Environment.PROP_TOPIC_FORCE_USERNAME) && Utilities.currentUser(request) == null) {
			WikiMessage errorMessage = new WikiMessage("edit.exception.login");
			return viewLogin(request, JAMWikiServlet.getTopicFromURI(request), errorMessage);
		}
		Topic topic = WikiBase.getHandler().lookupTopic(virtualWiki, topicName);
		if (topic != null && topic.getAdminOnly() && !Utilities.isAdmin(request)) {
			WikiMessage errorMessage = new WikiMessage("edit.exception.loginadmin", topicName);
			return viewLogin(request, JAMWikiServlet.getTopicFromURI(request), errorMessage);
		}
		return null;
	}

	/**
	 *
	 */
	private void preview(HttpServletRequest request, ModelAndView next, WikiPageInfo pageInfo) throws Exception {
		String topicName = JAMWikiServlet.getTopicFromRequest(request);
		String virtualWiki = JAMWikiServlet.getVirtualWikiFromURI(request);
		String contents = (String)request.getParameter("contents");
		Topic previewTopic = new Topic();
		previewTopic.setName(topicName);
		previewTopic.setTopicContent(contents);
		previewTopic.setVirtualWiki(virtualWiki);
		pageInfo.setAction(WikiPageInfo.ACTION_EDIT_PREVIEW);
		next.addObject("contents", contents);
		viewTopic(request, next, pageInfo, null, previewTopic, false);
	}

	/**
	 *
	 */
	private void resolve(HttpServletRequest request, ModelAndView next, WikiPageInfo pageInfo) throws Exception {
		String topicName = JAMWikiServlet.getTopicFromRequest(request);
		String virtualWiki = JAMWikiServlet.getVirtualWikiFromURI(request);
		TopicVersion version = WikiBase.getHandler().lookupLastTopicVersion(virtualWiki, topicName);
		String contents1 = version.getVersionContent();
		String contents2 = request.getParameter("contents");
		next.addObject("lastTopicVersionId", new Integer(version.getTopicVersionId()));
		next.addObject("contents", contents1);
		next.addObject("contentsResolve", contents2);
		Vector diffs = DiffUtil.diff(contents1, contents2);
		next.addObject("diffs", diffs);
		loadEdit(request, next, pageInfo, virtualWiki, topicName, false);
		pageInfo.setAction(WikiPageInfo.ACTION_EDIT_RESOLVE);
	}

	/**
	 *
	 */
	private int retrieveLastTopicVersionId(HttpServletRequest request, String virtualWiki, String topicName) throws Exception {
		int lastTopicVersionId = 0;
		if (request.getParameter("lastTopicVersionId") == null) {
			TopicVersion version = WikiBase.getHandler().lookupLastTopicVersion(virtualWiki, topicName);
			if (version != null) lastTopicVersionId = version.getTopicVersionId();
		} else {
			lastTopicVersionId = new Integer(request.getParameter("lastTopicVersionId")).intValue();
		}
		return lastTopicVersionId;
	}

	/**
	 *
	 */
	private void save(HttpServletRequest request, ModelAndView next, WikiPageInfo pageInfo) throws Exception {
		String topicName = JAMWikiServlet.getTopicFromRequest(request);
		String virtualWiki = JAMWikiServlet.getVirtualWikiFromURI(request);
		Topic topic = loadTopic(virtualWiki, topicName);
		TopicVersion lastTopicVersion = WikiBase.getHandler().lookupLastTopicVersion(virtualWiki, topicName);
		if (lastTopicVersion != null && lastTopicVersion.getTopicVersionId() != retrieveLastTopicVersionId(request, virtualWiki, topicName)) {
			// someone else has edited the topic more recently
			resolve(request, next, pageInfo);
			return;
		}
		String contents = request.getParameter("contents");
		if (StringUtils.hasText(request.getParameter("section"))) {
			// load section of topic
			int section = (new Integer(request.getParameter("section"))).intValue();
			ParserDocument parserDocument = Utilities.parseSplice(request, virtualWiki, topicName, section, contents);
			contents = parserDocument.getContent();
		}
		if (contents == null) {
			logger.warning("The topic " + topicName + " has no content");
			throw new WikiException(new WikiMessage("edit.exception.nocontent", topicName));
		}
		if (lastTopicVersion != null && lastTopicVersion.getVersionContent().equals(contents)) {
			viewTopic(request, next, pageInfo, topicName);
			return;
		}
		// parse for signatures and other syntax that should not be saved in raw form
		WikiUser user = Utilities.currentUser(request);
		ParserInput parserInput = new ParserInput();
		parserInput.setContext(request.getContextPath());
		parserInput.setLocale(request.getLocale());
		parserInput.setWikiUser(user);
		parserInput.setTopicName(topicName);
		parserInput.setUserIpAddress(request.getRemoteAddr());
		parserInput.setVirtualWiki(virtualWiki);
		ParserDocument parserDocument = Utilities.parseSave(parserInput, contents);
		contents = parserDocument.getContent();
		topic.setTopicContent(contents);
		if (StringUtils.hasText(parserDocument.getRedirect())) {
			// set up a redirect
			topic.setRedirectTo(parserDocument.getRedirect());
			topic.setTopicType(Topic.TYPE_REDIRECT);
		} else if (topic.getTopicType() == Topic.TYPE_REDIRECT) {
			// no longer a redirect
			topic.setRedirectTo(null);
			topic.setTopicType(Topic.TYPE_ARTICLE);
		}
		TopicVersion topicVersion = new TopicVersion(user, request.getRemoteAddr(), request.getParameter("editComment"), contents);
		if (request.getParameter("minorEdit") != null) {
			topicVersion.setEditType(TopicVersion.EDIT_MINOR);
		}
		WikiBase.getHandler().writeTopic(topic, topicVersion, parserDocument);
		// a save request has been made
		JAMWikiServlet.removeCachedContents();
		viewTopic(request, next, pageInfo, topicName);
	}
}
