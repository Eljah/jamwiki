package org.jamwiki.model;

import org.acegisecurity.GrantedAuthority;

public interface Role extends GrantedAuthority {
	public static final String ROLE_ADMIN = new String("ROLE_ADMIN");
	/** ROLE_ANONYMOUS is not store in the database but is instead automatically assigned to all non-logged in users. */
	public static final String ROLE_ANONYMOUS = new String("ROLE_ANONYMOUS");
	public static final String ROLE_DELETE = new String("ROLE_DELETE");
	public static final String ROLE_EDIT_EXISTING = new String(
			"ROLE_EDIT_EXISTING");
	public static final String ROLE_EDIT_NEW = new String("ROLE_EDIT_NEW");
	public static final String ROLE_MOVE = new String("ROLE_MOVE");
	public static final String ROLE_TRANSLATE = new String("ROLE_TRANSLATE");
	public static final String ROLE_UPLOAD = new String("ROLE_UPLOAD");
	/** ROLE_USER is not store in the database but is instead automatically assigned to all logged in users. */
	public static final String ROLE_USER = new String("ROLE_USER");
	public static final String ROLE_VIEW = new String("ROLE_VIEW");

	/**
	 *
	 */
	public abstract String getDescription();

	/**
	 *
	 */
	public abstract void setDescription(String description);

	/**
	 * Two roles are equal if the role names are the same.
	 */
	public abstract boolean equals(Role role);

}