package org.jamwiki.model;

import java.sql.Timestamp;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.GrantedAuthority;

public interface User extends UserDetails {

	/**
	 *
	 */
	public abstract Timestamp getCreateDate();

	/**
	 *
	 */
	public abstract void setCreateDate(Timestamp createDate);

	/**
	 *
	 */
	public abstract String getCreateIpAddress();

	/**
	 *
	 */
	public abstract void setCreateIpAddress(String createIpAddress);

	/**
	 *
	 */
	public abstract String getDefaultLocale();

	/**
	 *
	 */
	public abstract void setDefaultLocale(String defaultLocale);

	/**
	 *
	 */
	public abstract String getDisplayName();

	/**
	 *
	 */
	public abstract void setDisplayName(String displayName);

	/**
	 *
	 */
	public abstract Timestamp getLastLoginDate();

	/**
	 *
	 */
	public abstract void setLastLoginDate(Timestamp lastLoginDate);

	/**
	 *
	 */
	public abstract String getLastLoginIpAddress();

	/**
	 *
	 */
	public abstract void setLastLoginIpAddress(String lastLoginIpAddress);

	/**
	 *
	 */
	public abstract int getUserId();

	/**
	 *
	 */
	public abstract void setUserId(int userId);

	/**
	 * Returns granted authorites.
	 *
	 * @return authorites, never null.
	 */
	public abstract GrantedAuthority[] getAuthorities();

	/**
	 *
	 */
	public abstract String getPassword();

	/**
	 *
	 */
	public abstract void setPassword(String password);

	/**
	 *
	 */
	public abstract String getUsername();

	/**
	 *
	 */
	public abstract boolean isAccountNonExpired();

	/**
	 *
	 */
	public abstract boolean isAccountNonLocked();

	/**
	 *
	 */
	public abstract boolean isCredentialsNonExpired();

	/**
	 *
	 */
	public abstract boolean isEnabled();

	/**
	 * Convenience method for determining if a user has been assigned a role
	 * without the need to examine an array of Role objects.
	 *
	 * @param role If the user has been assigned this role then the method will
	 *  return <code>true</code>.
	 * @return <code>true</code> if the user has been assigned the specified
	 *  role, <code>false</code> otherwise.
	 */
	public abstract boolean hasRole(Role role);

}