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
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.acegisecurity.providers.anonymous.AnonymousProcessingFilter;
import org.acegisecurity.userdetails.memory.UserAttribute;
import org.jamwiki.WikiBase;
import org.jamwiki.model.Role;
import org.jamwiki.model.WikiGroup;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiLogger;

/**
 * This class allows anonymous users to be provided default roles from the
 * JAMWiki database.
 */
public class JAMWikiAnonymousProcessingFilter extends AnonymousProcessingFilter {

	/** Standard logger. */
	private static final WikiLogger logger = WikiLogger.getLogger(JAMWikiAnonymousProcessingFilter.class.getName());
	/** Default roles for anonymous users */
	private static Role[] groupRoles = null;

	/**
	 * Set default roles for anonymous users.
	 */
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		this.initRoles();
	}

	/**
	 * Override the parent method to ensure that default roles for anonymous
	 * users have been retrieved.
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (groupRoles == null) {
			// during setup and upgrade roles would not have been initialized, so
			// initialize now.
			this.initRoles();
		}
		super.doFilter(request, response, chain);
	}

	/**
	 * Retrieve the default roles for anonymous users.
	 */
	private void initRoles() {
		try {
			if (Utilities.isFirstUse() || Utilities.isUpgrade()) {
				// wiki is not yet setup
				return;
			}
		} catch (Exception e) {
			logger.info("Failure while determining first use / upgrade status of the wiki", e);
		}
		UserAttribute user = this.getUserAttribute();
		if (user == null) {
			logger.warning("No user attribute available in JAMWikiAnonymousProcessingFilter.  Please verify the Acegi configuration settings.");
		}
		groupRoles = new Role[0];
		try {
			groupRoles = WikiBase.getDataHandler().getRoleMapGroup(WikiGroup.GROUP_ANONYMOUS);
		} catch (Exception e) {
			// FIXME - without default roles bad things happen, so should this throw the
			// error to the calling method?
			logger.severe("Unable to retrieve default roles for " + WikiGroup.GROUP_ANONYMOUS, e);
		}
		for (int i=0; i < groupRoles.length; i++) {
			user.addAuthority(groupRoles[i]);
		}
	}
}
