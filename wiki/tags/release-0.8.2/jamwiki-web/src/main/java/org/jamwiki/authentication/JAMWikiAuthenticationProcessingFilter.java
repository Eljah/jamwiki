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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.AuthenticationException;
import org.springframework.security.ui.webapp.AuthenticationProcessingFilter;
import org.apache.commons.lang.StringUtils;
import org.jamwiki.WikiBase;
import org.jamwiki.utils.WikiLogger;
import org.jamwiki.utils.WikiUtil;

/**
 * This class is a hack implemented to work around the fact that the default
 * Spring Security classes can only redirect to a single, hard-coded URL.  Due to the
 * fact that JAMWiki may have multiple virtual wikis this class overrides some
 * of the default Spring Security behavior to allow additional flexibility.  Hopefully
 * future versions of Spring Security will add additional flexibility and this class
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
		// FIXME - move the "strip after semicolon" code to WikiUtil
		int pathParamIndex = uri.indexOf(';');
		if (pathParamIndex > 0) {
			// strip everything after the first semi-colon
			uri = uri.substring(0, pathParamIndex);
		}
		String virtualWiki = WikiUtil.getVirtualWikiFromURI(request);
		return uri.endsWith(request.getContextPath() + "/" + virtualWiki + this.getFilterProcessesUrl());
	}

	/**
	 *
	 */
	protected String determineFailureUrl(HttpServletRequest request, AuthenticationException failed) {
		String virtualWikiName = WikiUtil.getVirtualWikiFromURI(request);
		if (StringUtils.isBlank(virtualWikiName)) {
			virtualWikiName = WikiBase.DEFAULT_VWIKI;
		}
		String targetUrl = "/" + virtualWikiName + this.getAuthenticationFailureUrl();
		String target = request.getParameter(JAMWikiAuthenticationConstants.SPRING_SECURITY_LOGIN_TARGET_URL_FIELD_NAME);
		if (!StringUtils.isBlank(target)) {
			targetUrl += (targetUrl.indexOf('?') == -1) ? "?" : "&";
			try {
				targetUrl += JAMWikiAuthenticationConstants.SPRING_SECURITY_LOGIN_TARGET_URL_FIELD_NAME + "=" + URLEncoder.encode(target, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// this should never happen
				throw new IllegalStateException("Unsupporting encoding UTF-8");
			}
		}
		return targetUrl;
	}
}
