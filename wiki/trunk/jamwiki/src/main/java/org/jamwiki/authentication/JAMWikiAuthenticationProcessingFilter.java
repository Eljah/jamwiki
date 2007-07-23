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
package org.jamwiki.authentication;

import java.io.IOException;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.acegisecurity.ui.webapp.AuthenticationProcessingFilter;
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.model.VirtualWiki;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiLogger;
import org.springframework.util.StringUtils;

/**
 * This class is a hack implemented to work around the fact that the default
 * Acegi classes can only redirect to a single, hard-coded URL.  Due to the
 * fact that JAMWiki may have multiple virtual wikis this class overrides some
 * of the default Acegi behavior to allow additional flexibility.  Hopefully
 * future versions of Acegi will add additional flexibility and this class
 * can be removed.
 */
public class JAMWikiAuthenticationProcessingFilter extends AuthenticationProcessingFilter {

	/** Standard logger. */
	private static final WikiLogger logger = WikiLogger.getLogger(JAMWikiAuthenticationProcessingFilter.class.getName());

	/**
	 * Indicates whether this filter should attempt to process a login request
	 * for the current invocation.
	 *
	 * It strips any parameters from the "path" section of the request URL
	 * (such as the jsessionid parameter in
	 * http://host/myapp/index.html;jsessionid=blah) before matching against
	 * the filterProcessesUrl property.
	 *
	 * FIXME - This method is needed due to the fact that different virtual
	 * wikis may be used.
	 */
	protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
		String uri = request.getRequestURI();
		int pathParamIndex = uri.indexOf(';');
		if (pathParamIndex > 0) {
			// strip everything after the first semi-colon
			uri = uri.substring(0, pathParamIndex);
		}
		String virtualWiki = Utilities.getVirtualWikiFromURI(request);
		return uri.endsWith(request.getContextPath() + "/" + virtualWiki + this.getFilterProcessesUrl());
	}

	/**
	 * Allow subclasses to modify the redirection message.
	 *
	 * @param request the request
	 * @param response the response
	 * @param url the URL to redirect to
	 * @throws IOException in the event of any failure
	 */
	protected void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
		// FIXME - this method is a mess.  clean it up.
		if (!url.equals(this.getAuthenticationFailureUrl()) && !url.equals("/DEFAULT_VIRTUAL_WIKI")) {
			// if Acegi has saved a redirect URL then use that
			super.sendRedirect(request, response, url);
			return;
		}
		String target = request.getParameter("target");
		String targetUrl = url;
		if (url.equals("/DEFAULT_VIRTUAL_WIKI")) {
			// ugly, but a hard-coded constant seems to be the only way to
			// allow a dynamic url value
			String virtualWikiName = Utilities.getVirtualWikiFromURI(request);
			if (!StringUtils.hasText(virtualWikiName)) {
				virtualWikiName = WikiBase.DEFAULT_VWIKI;
			}
			if (!StringUtils.hasText(target)) {
				target = Environment.getValue(Environment.PROP_BASE_DEFAULT_TOPIC);
				try {
					VirtualWiki virtualWiki = WikiBase.getDataHandler().lookupVirtualWiki(virtualWikiName);
					target = virtualWiki.getDefaultTopicName();
				} catch (Exception e) {
					logger.warning("Unable to retrieve default topic for virtual wiki", e);
				}
			}
			targetUrl = request.getContextPath() + "/" + virtualWikiName + "/" + target;
		} else if (!url.startsWith("http://") && !url.startsWith("https://")) {
			String virtualWiki = Utilities.getVirtualWikiFromURI(request);
			targetUrl = request.getContextPath() + "/" + virtualWiki + url;
			if (StringUtils.hasText(target)) {
				targetUrl += (url.indexOf('?') == -1) ? "?" : "&";
				targetUrl += "target=" + URLEncoder.encode(target, "UTF-8");
			}
		}
		response.sendRedirect(response.encodeRedirectURL(targetUrl));
	}
}
