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
package org.jamwiki.model;

import org.acegisecurity.GrantedAuthorityImpl;

/**
 * Provides an object representing a Wiki role and implementing the Acegi
 * <code>GrantedAuthority</code> interface.
 */
public class Role  extends GrantedAuthorityImpl  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7995231871249892674L;
	private String description = null;

	/**
	 *
	 */
	public Role(String role) {
		super((role == null) ? null : role.toUpperCase());
	}

	/* (non-Javadoc)
	 * @see org.jamwiki.model.Role#getDescription()
	 */
	public String getDescription() {
		return this.description;
	}

	/* (non-Javadoc)
	 * @see org.jamwiki.model.Role#setDescription(java.lang.String)
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see org.jamwiki.model.Role#equals(org.jamwiki.model.WikiRole)
	 */
	public boolean equals(Role role) {
		if (this.getAuthority() == null && role != null && role.getAuthority() == null) {
			return true;
		}
		return (this.getAuthority() != null && role != null && this.getAuthority().equals(role.getAuthority()));
	}
}