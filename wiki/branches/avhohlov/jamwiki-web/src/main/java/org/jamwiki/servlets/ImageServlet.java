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
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.jamwiki.DataAccessException;
import org.jamwiki.ImageData;
import org.jamwiki.WikiBase;

/**
 * Get image requests handler.
 */
public class ImageServlet extends JAMWikiServlet {
	protected ModelAndView handleJAMWikiRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView next, WikiPageInfo pageInfo) throws ServletException, IOException {
		ImageData imageData;

		try
		{
			String imageName = request.getParameter("url");
			imageData = WikiBase.getDataHandler().getImageData(imageName);
		}
		catch (DataAccessException dae)
		{
			throw new ServletException(dae);
		}

		response.setContentType  (imageData.mimeType);
		response.setContentLength(imageData.data.length);
		OutputStream os = response.getOutputStream();
		os.write(imageData.data);
		os.close();

		return null;
	} 
}

