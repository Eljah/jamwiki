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
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.jamwiki.utils.WikiLogger;
import org.jamwiki.utils.WikiUtil;

/**
 * This class provides an additional filter that is added to the Spring Security
 * configuration for adding messages to the session about why a login is
 * required.
 */
public class JAMWikiExceptionTranslationFilter implements Filter, InitializingBean {

	private static final WikiLogger logger = WikiLogger.getLogger(JAMWikiExceptionTranslationFilter.class.getName());
	private JAMWikiErrorMessageProvider errorMessageProvider;

	/**
	 *
	 */
	public void afterPropertiesSet() {
		if (errorMessageProvider == null) {
			throw new IllegalArgumentException("errorMessageProvider must be specified");
		}
    }

	/**
	 *
	 */
	public void destroy() {
	}

	/**
	 *
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (!(request instanceof HttpServletRequest)) {
			throw new ServletException("HttpServletRequest required");
		}
		try {
			chain.doFilter(request, response);
		} catch (ServletException ex) {
			handleException(request, response, (Exception)ex.getRootCause());
		} catch (Exception ex) {
			handleException(request, response, ex);
			throw new ServletException(ex);
		}
	}

	/**
	 *
	 */
	public JAMWikiErrorMessageProvider getErrorMessageProvider() {
		return this.errorMessageProvider;
	}

	/**
	 *
	 */
	private boolean handleException(ServletRequest servletRequest, ServletResponse servletResponse, Exception exception) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		if (exception instanceof AccessDeniedException) {
			request.getSession().setAttribute(JAMWikiAuthenticationConstants.JAMWIKI_ACCESS_DENIED_ERROR_KEY, this.getErrorMessageProvider().getErrorMessageKey(request));
			request.getSession().setAttribute(JAMWikiAuthenticationConstants.JAMWIKI_ACCESS_DENIED_URI_KEY, WikiUtil.getTopicFromURI(request));
			String virtualWiki = WikiUtil.getVirtualWikiFromURI(request);
			String accessDeniedRedirectUri = "/" + virtualWiki + "/Special:Login";
			request.getSession().setAttribute(JAMWikiAuthenticationConstants.JAMWIKI_ACCESS_DENIED_REDIRECT_URI, accessDeniedRedirectUri);
			return true;
		}
		if (exception instanceof AuthenticationException) {
			request.getSession().setAttribute(JAMWikiAuthenticationConstants.JAMWIKI_AUTHENTICATION_REQUIRED_KEY, this.getErrorMessageProvider().getErrorMessageKey(request));
			request.getSession().setAttribute(JAMWikiAuthenticationConstants.JAMWIKI_AUTHENTICATION_REQUIRED_URI_KEY, WikiUtil.getTopicFromURI(request));
			return true;
		}
		return false;
	}

	/**
	 *
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	/**
	 *
	 */
	public void setErrorMessageProvider(JAMWikiErrorMessageProvider errorMessageProvider) {
		this.errorMessageProvider = errorMessageProvider;
	}
}
