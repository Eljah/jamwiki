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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.WikiException;
import org.jamwiki.WikiMessage;
import org.jamwiki.model.Topic;
import org.jamwiki.model.TopicVersion;
import org.jamwiki.model.WikiFile;
import org.jamwiki.model.WikiFileVersion;
import org.jamwiki.model.WikiUser;
import org.jamwiki.utils.ImageUtil;
import org.jamwiki.utils.NamespaceHandler;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiLogger;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * Used to handle file uploads.
 */
public class UploadServlet extends JAMWikiServlet {

	private static final WikiLogger logger = WikiLogger.getLogger(UploadServlet.class.getName());
	protected static final String JSP_UPLOAD = "upload.jsp";

	/**
	 *
	 */
	protected ModelAndView handleJAMWikiRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView next, WikiPageInfo pageInfo) throws Exception {
		String contentType = ((request.getContentType() != null) ? request.getContentType().toLowerCase() : "" );
		if (contentType.indexOf("multipart") == -1) {
			view(request, next, pageInfo);
		} else {
			upload(request, next, pageInfo);
		}
		return next;
	}

	/**
	 *
	 */
	private static String buildFileSubdirectory() {
		// subdirectory is composed of year/month
		GregorianCalendar cal = new GregorianCalendar();
		String year = Integer.toString(cal.get(Calendar.YEAR));
		String month = Integer.toString(cal.get(Calendar.MONTH) + 1);
		return "/" + year + "/" + month;
	}

	/**
	 *
	 */
	private static String buildUniqueFileName(String fileName) {
		if (!StringUtils.hasText(fileName)) {
			return null;
		}
		// file is appended with a timestamp of DDHHMMSS
		GregorianCalendar cal = new GregorianCalendar();
		String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
		if (day.length() == 1) {
			day = "0" + day;
		}
		String hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
		if (hour.length() == 1) {
			hour = "0" + hour;
		}
		String minute = Integer.toString(cal.get(Calendar.MINUTE));
		if (minute.length() == 1) {
			minute = "0" + minute;
		}
		String second = Integer.toString(cal.get(Calendar.SECOND));
		if (second.length() == 1) {
			second = "0" + second;
		}
		String suffix = "-" + day + hour + minute + second;
		int pos = fileName.lastIndexOf('.');
		if (pos == -1) {
			fileName = fileName + suffix;
		} else {
			fileName = fileName.substring(0, pos) + suffix + fileName.substring(pos);
		}
		return fileName;
	}

	/**
	 *
	 */
	private boolean isFileTypeAllowed(String extension) {
		int blacklistType = Environment.getIntValue(Environment.PROP_FILE_BLACKLIST_TYPE);
		if (blacklistType == WikiBase.UPLOAD_ALL) {
			return true;
		}
		if (blacklistType == WikiBase.UPLOAD_NONE) {
			return false;
		}
		if (!StringUtils.hasText(extension)) {
			// FIXME - should non-extensions be represented in the whitelist/blacklist?
			return true;
		}
		extension = extension.toLowerCase();
		List list = Utilities.retrieveUploadFileList();
		if (blacklistType == WikiBase.UPLOAD_BLACKLIST) {
			return !list.contains(extension);
		} else if (blacklistType == WikiBase.UPLOAD_WHITELIST) {
			return list.contains(extension);
		}
		return false;
	}

	/**
	 *
	 */
	private static String sanitizeFilename(String filename) {
		if (!StringUtils.hasText(filename)) {
			return null;
		}
		// some browsers set the full path, so strip to just the file name
		filename = FilenameUtils.getName(filename);
		filename = StringUtils.replace(filename.trim(), " ", "_");
		return filename;
	}

	/**
	 *
	 */
	private void upload(HttpServletRequest request, ModelAndView next, WikiPageInfo pageInfo) throws Exception {
		// FIXME - this method is a mess and needs to be split up.
		File file = new File(Environment.getValue(Environment.PROP_FILE_DIR_FULL_PATH));
		if (!file.exists()) {
			throw new WikiException(new WikiMessage("upload.error.nodirectory"));
		}
		Iterator iterator = ServletUtil.processMultipartRequest(request);
		String fileName = null;
		String url = null;
		String contentType = null;
		long fileSize = 0;
		String contents = null;
		boolean isImage = true;
		while (iterator.hasNext()) {
			FileItem item = (FileItem)iterator.next();
			String fieldName = item.getFieldName();
			if (item.isFormField()) {
				if (fieldName.equals("description")) {
					// FIXME - these should be parsed
					contents = item.getString("UTF-8");
				}
			} else {
				// file name can have encoding issues, so manually convert
				fileName = item.getName();
				if (fileName == null) {
					throw new WikiException(new WikiMessage("upload.error.filename"));
				}
				fileName = UploadServlet.sanitizeFilename(fileName);
				String extension = FilenameUtils.getExtension(fileName);
				if (!isFileTypeAllowed(extension)) {
					throw new WikiException(new WikiMessage("upload.error.filetype", extension));
				}
				url = UploadServlet.buildUniqueFileName(fileName);
				String subdirectory = UploadServlet.buildFileSubdirectory();
				fileSize = item.getSize();
				File directory = new File(Environment.getValue(Environment.PROP_FILE_DIR_FULL_PATH), subdirectory);
				if (!directory.exists() && !directory.mkdirs()) {
					throw new WikiException(new WikiMessage("upload.error.directorycreate",  directory.getAbsolutePath()));
				}
				contentType = item.getContentType();
				url = subdirectory + "/" + url;
				File uploadedFile = new File(Environment.getValue(Environment.PROP_FILE_DIR_FULL_PATH), url);
				item.write(uploadedFile);
				isImage = ImageUtil.isImage(uploadedFile);
			}
		}
		String virtualWiki = Utilities.getVirtualWikiFromURI(request);
		String topicName = NamespaceHandler.NAMESPACE_IMAGE + NamespaceHandler.NAMESPACE_SEPARATOR + Utilities.decodeFromURL(fileName);
		Topic topic = WikiBase.getDataHandler().lookupTopic(virtualWiki, topicName, false, null);
		if (topic == null) {
			topic = new Topic();
			topic.setVirtualWiki(virtualWiki);
			topic.setName(topicName);
			topic.setTopicContent(contents);
		}
		if (isImage) {
			topic.setTopicType(Topic.TYPE_IMAGE);
		} else {
			topic.setTopicType(Topic.TYPE_FILE);
		}
		WikiFileVersion wikiFileVersion = new WikiFileVersion();
		wikiFileVersion.setUploadComment(contents);
		wikiFileVersion.setAuthorIpAddress(request.getRemoteAddr());
		WikiUser user = Utilities.currentUser();
		Integer authorId = null;
		if (user.getUserId() > 0) {
			authorId = new Integer(user.getUserId());
		}
		wikiFileVersion.setAuthorId(authorId);
		TopicVersion topicVersion = new TopicVersion(user, request.getRemoteAddr(), contents, topic.getTopicContent());
		if (fileName == null) {
			throw new WikiException(new WikiMessage("upload.error.filenotfound"));
		}
		WikiFile wikiFile = WikiBase.getDataHandler().lookupWikiFile(virtualWiki, topicName);
		if (wikiFile == null) {
			wikiFile = new WikiFile();
			wikiFile.setVirtualWiki(virtualWiki);
		}
		wikiFile.setFileName(fileName);
		wikiFile.setUrl(url);
		wikiFileVersion.setUrl(url);
		wikiFileVersion.setMimeType(contentType);
		wikiFile.setMimeType(contentType);
		wikiFileVersion.setFileSize(fileSize);
		wikiFile.setFileSize(fileSize);
		WikiBase.getDataHandler().writeTopic(topic, topicVersion, Utilities.parserDocument(topic.getTopicContent(), virtualWiki, topicName), true, null);
		wikiFile.setTopicId(topic.getTopicId());
		WikiBase.getDataHandler().writeFile(wikiFile, wikiFileVersion, null);
		ServletUtil.redirect(next, virtualWiki, topicName);
	}

	/**
	 *
	 */
	private void view(HttpServletRequest request, ModelAndView next, WikiPageInfo pageInfo) throws Exception {
		pageInfo.setPageTitle(new WikiMessage("upload.title"));
		pageInfo.setContentJsp(JSP_UPLOAD);
		pageInfo.setSpecial(true);
	}
}
