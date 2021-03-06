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
import org.apache.commons.lang.StringUtils;
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.WikiException;
import org.jamwiki.WikiMessage;
import org.jamwiki.model.Topic;
import org.jamwiki.model.TopicVersion;
import org.jamwiki.model.WikiFile;
import org.jamwiki.model.WikiFileVersion;
import org.jamwiki.model.WikiUser;
import org.jamwiki.parser.ParserOutput;
import org.jamwiki.parser.ParserUtil;
import org.jamwiki.utils.ImageUtil;
import org.jamwiki.utils.NamespaceHandler;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiLogger;
import org.jamwiki.utils.WikiUtil;
import org.springframework.web.servlet.ModelAndView;

/**
 * Used to handle file uploads.
 */
public class UploadServlet extends JAMWikiServlet {

	private static final WikiLogger logger = WikiLogger.getLogger(UploadServlet.class.getName());
	/** The name of the JSP file used to render the servlet output. */
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
	private static String buildUploadUrl(String filename) throws WikiException {
		String url = filename;
		if (StringUtils.isBlank(url)) {
			throw new WikiException(new WikiMessage("upload.error.filename"));
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
		int pos = url.lastIndexOf('.');
		url = (pos == -1) ? url + suffix : url.substring(0, pos) + suffix + url.substring(pos);
		// now pre-pend the file system directory
		String subdirectory = UploadServlet.buildFileSubdirectory();
		File directory = new File(Environment.getValue(Environment.PROP_FILE_DIR_FULL_PATH), subdirectory);
		if (!directory.exists() && !directory.mkdirs()) {
			throw new WikiException(new WikiMessage("upload.error.directorycreate", directory.getAbsolutePath()));
		}
		return url;
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
		if (StringUtils.isBlank(extension)) {
			// FIXME - should non-extensions be represented in the whitelist/blacklist?
			return true;
		}
		extension = extension.toLowerCase();
		List list = WikiUtil.retrieveUploadFileList();
		if (blacklistType == WikiBase.UPLOAD_BLACKLIST) {
			return !list.contains(extension);
		}
		if (blacklistType == WikiBase.UPLOAD_WHITELIST) {
			return list.contains(extension);
		}
		return false;
	}

	/**
	 *
	 */
	private String processDestinationFilename(String destinationFilename, String filename) {
		if (StringUtils.isBlank(destinationFilename)) {
			return destinationFilename;
		}
		if (!StringUtils.isBlank(FilenameUtils.getExtension(filename)) && StringUtils.isBlank(FilenameUtils.getExtension(destinationFilename))) {
			// if original has an extension, the renamed version must as well
			destinationFilename += (!destinationFilename.endsWith(".") ? "." : "") + FilenameUtils.getExtension(filename);
		}
		// if the user entered a file name of the form "Image:Foo.jpg" strip the namespace
		return StringUtils.removeStart(destinationFilename, NamespaceHandler.NAMESPACE_IMAGE + NamespaceHandler.NAMESPACE_SEPARATOR);
	}

	/**
	 *
	 */
	private static String sanitizeFilename(String filename) {
		if (StringUtils.isBlank(filename)) {
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
		Iterator iterator = ServletUtil.processMultipartRequest(request, Environment.getValue(Environment.PROP_FILE_DIR_FULL_PATH), Environment.getLongValue(Environment.PROP_FILE_MAX_FILE_SIZE));
		String filename = null;
		String destinationFilename = null;
		String url = null;
		String contentType = null;
		long fileSize = 0;
		String contents = null;
		boolean isImage = true;
		File uploadedFile = null;
		while (iterator.hasNext()) {
			FileItem item = (FileItem)iterator.next();
			String fieldName = item.getFieldName();
			if (item.isFormField()) {
				if (fieldName.equals("description")) {
					// FIXME - these should be parsed
					contents = item.getString("UTF-8");
				} else if (fieldName.equals("destination")) {
					destinationFilename = item.getString("UTF-8");
				}
				continue;
			}
			// file name can have encoding issues, so manually convert
			filename = item.getName();
			if (filename == null) {
				throw new WikiException(new WikiMessage("upload.error.filename"));
			}
			filename = UploadServlet.sanitizeFilename(filename);
			String extension = FilenameUtils.getExtension(filename);
			if (!isFileTypeAllowed(extension)) {
				throw new WikiException(new WikiMessage("upload.error.filetype", extension));
			}
			url = UploadServlet.buildUploadUrl(filename);
			fileSize = item.getSize();
			contentType = item.getContentType();
			uploadedFile = new File(Environment.getValue(Environment.PROP_FILE_DIR_FULL_PATH), url);
			item.write(uploadedFile);
			isImage = ImageUtil.isImage(uploadedFile);
		}
		if (uploadedFile == null) {
			throw new WikiException(new WikiMessage("upload.error.filenotfound"));
		}
		String virtualWiki = pageInfo.getVirtualWikiName();
		String topicName = NamespaceHandler.NAMESPACE_IMAGE + NamespaceHandler.NAMESPACE_SEPARATOR;
		destinationFilename = processDestinationFilename(destinationFilename, filename);
		topicName += Utilities.decodeAndEscapeTopicName((!StringUtils.isEmpty(destinationFilename) ? destinationFilename : filename), true);
		if (this.handleSpam(request, next, topicName, contents)) {
			// delete the spam file
			uploadedFile.delete();
			this.view(request, next, pageInfo);
			next.addObject("contents", contents);
			return;
		}
		if (!StringUtils.isEmpty(destinationFilename)) {
			filename = UploadServlet.sanitizeFilename(destinationFilename);
			url = UploadServlet.buildUploadUrl(filename);
			File renamedFile = new File(Environment.getValue(Environment.PROP_FILE_DIR_FULL_PATH), url);
			if (!uploadedFile.renameTo(renamedFile)) {
				throw new WikiException(new WikiMessage("upload.error.filerename", destinationFilename));
			}
		}
		Topic topic = WikiBase.getDataHandler().lookupTopic(virtualWiki, topicName, false, null);
		int charactersChanged = 0;
		if (topic == null) {
			topic = new Topic();
			topic.setVirtualWiki(virtualWiki);
			topic.setName(topicName);
			topic.setTopicContent(contents);
			charactersChanged = StringUtils.length(contents);
		}
		if (isImage) {
			topic.setTopicType(Topic.TYPE_IMAGE);
		} else {
			topic.setTopicType(Topic.TYPE_FILE);
		}
		WikiFileVersion wikiFileVersion = new WikiFileVersion();
		wikiFileVersion.setUploadComment(contents);
		wikiFileVersion.setAuthorDisplay(ServletUtil.getIpAddress(request));
		WikiUser user = ServletUtil.currentWikiUser();
		Integer authorId = null;
		if (user.getUserId() > 0) {
			authorId = user.getUserId();
		}
		wikiFileVersion.setAuthorId(authorId);
		TopicVersion topicVersion = new TopicVersion(user, ServletUtil.getIpAddress(request), contents, topic.getTopicContent(), charactersChanged);
		topicVersion.setEditType(TopicVersion.EDIT_UPLOAD);
		WikiFile wikiFile = WikiBase.getDataHandler().lookupWikiFile(virtualWiki, topicName);
		if (wikiFile == null) {
			wikiFile = new WikiFile();
			wikiFile.setVirtualWiki(virtualWiki);
		}
		wikiFile.setFileName(filename);
		wikiFile.setUrl(url);
		wikiFileVersion.setUrl(url);
		wikiFileVersion.setMimeType(contentType);
		wikiFile.setMimeType(contentType);
		wikiFileVersion.setFileSize(fileSize);
		wikiFile.setFileSize(fileSize);
		ParserOutput parserOutput = ParserUtil.parserOutput(topic.getTopicContent(), virtualWiki, topicName);
		WikiBase.getDataHandler().writeTopic(topic, topicVersion, parserOutput.getCategories(), parserOutput.getLinks());
		wikiFile.setTopicId(topic.getTopicId());
		WikiBase.getDataHandler().writeFile(wikiFile, wikiFileVersion);
		ServletUtil.redirect(next, virtualWiki, topicName);
	}

	/**
	 *
	 */
	private void view(HttpServletRequest request, ModelAndView next, WikiPageInfo pageInfo) throws Exception {
		pageInfo.setPageTitle(new WikiMessage("upload.title"));
		pageInfo.setContentJsp(JSP_UPLOAD);
		next.addObject("uploadDestination", request.getParameter("topic"));
		pageInfo.setSpecial(true);
	}
}
