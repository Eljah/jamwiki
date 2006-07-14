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

import java.io.File;
import java.util.Iterator;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.model.Topic;
import org.jamwiki.model.TopicVersion;
import org.jamwiki.model.WikiFile;
import org.jamwiki.model.WikiFileVersion;
import org.jamwiki.model.WikiUser;
import org.jamwiki.utils.Utilities;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 */
public class UploadServlet extends JAMWikiServlet {

	private static Logger logger = Logger.getLogger(UploadServlet.class);

	/**
	 *
	 */
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView next = new ModelAndView("wiki");
		try {
			String contentType = ((request.getContentType() != null) ? request.getContentType().toLowerCase() : "" );
			if (contentType.indexOf("multipart") != -1) {
				upload(request, next);
			} else {
				view(request, next);
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
	private Iterator processMultipartRequest(HttpServletRequest request) throws Exception {
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(new File(Environment.getValue(Environment.PROP_FILE_DIR_FULL_PATH)));
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(Environment.getLongValue(Environment.PROP_FILE_MAX_FILE_SIZE));
		return upload.parseRequest(request).iterator();
	}

	/**
	 *
	 */
	private void upload(HttpServletRequest request, ModelAndView next) throws Exception {
		File file = new File(Environment.getValue(Environment.PROP_FILE_DIR_FULL_PATH));
		if (!file.exists()) {
			// FIXME - hard coding
			throw new Exception("Uploads not supported, no valid directory to upload into");
		}
		String virtualWiki = JAMWikiServlet.getVirtualWikiFromURI(request);
		WikiUser user = Utilities.currentUser(request);
		Iterator iterator = processMultipartRequest(request);
		Topic topic = new Topic();
		topic.setVirtualWiki(virtualWiki);
		topic.setTopicType(Topic.TYPE_IMAGE);
		WikiFile wikiFile = new WikiFile();
		wikiFile.setVirtualWiki(virtualWiki);
		WikiFileVersion wikiFileVersion = new WikiFileVersion();
		wikiFileVersion.setAuthorIpAddress(request.getRemoteAddr());
		TopicVersion topicVersion = new TopicVersion();
		topicVersion.setAuthorIpAddress(request.getRemoteAddr());
		if (user != null) {
			topicVersion.setAuthorId(new Integer(user.getUserId()));
			wikiFileVersion.setAuthorId(new Integer(user.getUserId()));
		}
		String fileName = null;
		String url = null;
		while (iterator.hasNext()) {
			FileItem item = (FileItem)iterator.next();
			String fieldName = item.getFieldName();
			if (item.isFormField()) {
				if (fieldName.equals("description")) {
					wikiFileVersion.setUploadComment(item.getString());
					topicVersion.setEditComment(item.getString());
					// FIXME - these should be parsed
					topicVersion.setVersionContent(item.getString());
					topic.setTopicContent(item.getString());
				}
			} else {
				fieldName = item.getFieldName();
				fileName = item.getName();
				// decode, then encode to ensure that any previously encoded characters
				// aren't encoded twice
				url = Utilities.encodeURL(Utilities.decodeURL(fileName));
				String contentType = item.getContentType();
				boolean isInMemory = item.isInMemory();
				long sizeInBytes = item.getSize();
				File uploadedFile = new File(Environment.getValue(Environment.PROP_FILE_DIR_FULL_PATH), fileName);
				item.write(uploadedFile);
			}
		}
		if (fileName == null) {
			throw new Exception("No file found");
		}
		// FIXME - hard coding
		String topicName = "Image:" + Utilities.decodeURL(fileName);
		topic.setName(topicName);
		wikiFile.setFileName(fileName);
		wikiFile.setUrl(url);
		wikiFileVersion.setUrl(url);
		ServletContext context = request.getSession().getServletContext();
		wikiFile.setMimeType(context.getMimeType(fileName));
		wikiFileVersion.setMimeType(context.getMimeType(fileName));
		if (WikiBase.getHandler().lookupTopic(virtualWiki, topic.getName()) == null) {
			WikiBase.getHandler().writeTopic(topic, topicVersion);
		} else {
			topic = WikiBase.getHandler().lookupTopic(virtualWiki, topic.getName());
		}
		wikiFile.setTopicId(topic.getTopicId());
		WikiBase.getHandler().writeFile(topicName, wikiFile, wikiFileVersion);
		viewTopic(request, next, topicName);
	}

	/**
	 *
	 */
	private void view(HttpServletRequest request, ModelAndView next) throws Exception {
		// FIXME - hard coding
		this.pageInfo.setPageTitle("File Upload");
		this.pageInfo.setPageAction(JAMWikiServlet.ACTION_UPLOAD);
		this.pageInfo.setSpecial(true);
	}
}
