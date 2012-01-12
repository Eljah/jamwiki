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
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.io.IOUtils;
import org.jamwiki.DataAccessException;
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.model.ImageData;
import org.jamwiki.model.WikiFile;
import org.jamwiki.utils.WikiLogger;

/**
 * Get image requests handler.
 */
public class ImageServlet extends JAMWikiServlet {

	private static final WikiLogger logger = WikiLogger.getLogger(ImageServlet.class.getName());

	/**
	 * This servlet requires slightly different initialization parameters from most
	 * servlets.
	 */
	public ImageServlet() {
		this.layout = false;
	}

	/**
	 * Handle image requests, returning the binary image data.  This method
	 * can be invoked either via the Special:Image path, for database files,
	 * or via a path that appears to end users as any other image request but
	 * that is actually a servlet request that will serve a file from the
	 * filesystem.
	 */
	public ModelAndView handleJAMWikiRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView next, WikiPageInfo pageInfo) throws ServletException, IOException {
		if (ServletUtil.isTopic(request, "Special:Image")) {
			this.streamFileFromDatabase(request, response);
		} else {
			this.streamFileFromFileSystem(request, response);
		}
		return null;
	}

	/**
	 * Serve a file from the database.  In some cases users may choose to store
	 * files directly in the database, and this method provides a way of serving
	 * those files.
	 */
	private void streamFileFromDatabase(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fileId = request.getParameter("fileId");
		String fileVersionId = request.getParameter("fileVersionId");
		String resized = request.getParameter("resized");
		if (resized == null) {
			resized = "0";
		}
		// TODO - consider implementing caching
		ImageData imageData;
		try {
			if (fileVersionId != null) {
				imageData = WikiBase.getDataHandler().getImageVersionData(Integer.parseInt(fileVersionId), Integer.parseInt(resized));
			} else {
				imageData = WikiBase.getDataHandler().getImageData(Integer.parseInt(fileId), Integer.parseInt(resized));
			}
		} catch (NumberFormatException nfe) {
			throw new ServletException(nfe);
		} catch (DataAccessException dae) {
			throw new ServletException(dae);
		}
		if (imageData == null) {
			logger.info("Data does not exist: " + (fileVersionId != null ? ("file_version_id=" + fileVersionId) : ("file_id=" + fileId)));
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		response.setContentType(imageData.mimeType);
		response.setContentLength(imageData.data.length);
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			os.write(imageData.data);
		} finally {
			IOUtils.closeQuietly(os);
		}
	}

	/**
	 * Serve a file from the filesystem.  This is less efficient than serving the file
	 * directly via Tomcat or Apache, but allows files to be stored outside of the
	 * webapp and thus keeps wiki data (files) separate from application code.
	 */
	private void streamFileFromFileSystem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletOutputStream out = null;
		InputStream in = null;
		String filename = request.getRequestURI().substring(request.getContextPath().length());
		File file = new File(Environment.getValue(Environment.PROP_BASE_FILE_DIR), filename);
		if (!file.exists() || file.isDirectory() || !file.canRead()) {
			logger.info("File does not exist: " + file.getAbsolutePath());
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		String mimeType = getServletContext().getMimeType(file.getAbsolutePath());
		if (mimeType == null) {
			mimeType = WikiFile.UNKNOWN_MIME_TYPE;
		}
		try {
			response.setContentType(mimeType);
			response.setContentLength((int)file.length());
			out = response.getOutputStream();
			in = new FileInputStream(file);
			IOUtils.copy(in, out);
			out.flush();
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}
}
