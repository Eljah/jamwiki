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

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jamwiki.DataAccessException;
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.utils.UserPreferencesUtil;
import org.jamwiki.utils.WikiLogger;

/**
 * Provides an object representing Wiki-specific information about a user of
 * the Wiki.
 */
public class WikiUser implements Serializable {

	private Timestamp createDate = new Timestamp(System.currentTimeMillis());
	private String createIpAddress = "0.0.0.0";
	private String email;
	private Timestamp lastLoginDate = new Timestamp(System.currentTimeMillis());
	private String lastLoginIpAddress = "0.0.0.0";
	private final String username;
	private int userId = -1;
	private String displayName = null;
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDefaultLocale() {
		return preferences.get(UserPreferencesUtil.USER_PREFERENCE_DEFAULT_LOCALE);
	}
	
	public void setDefaultLocale(String defaultLocale) {
		preferences.put(UserPreferencesUtil.USER_PREFERENCE_DEFAULT_LOCALE, defaultLocale);
	}
	
	private HashMap<String, String> preferences = new HashMap<String, String>();

	/**
	 *
	 */
	public WikiUser(String username) {
		this.username = username;
	}

	/**
	 *
	 */
	public Timestamp getCreateDate() {
		return this.createDate;
	}

	/**
	 *
	 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	/**
	 *
	 */
	public String getCreateIpAddress() {
		return this.createIpAddress;
	}

	/**
	 *
	 */
	public void setCreateIpAddress(String createIpAddress) {
		this.createIpAddress = createIpAddress;
	}
	
	/**
	 *
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 *
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 *
	 */
	public Timestamp getLastLoginDate() {
		return this.lastLoginDate;
	}

	/**
	 *
	 */
	public void setLastLoginDate(Timestamp lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	/**
	 *
	 */
	public String getLastLoginIpAddress() {
		return this.lastLoginIpAddress;
	}

	/**
	 *
	 */
	public void setLastLoginIpAddress(String lastLoginIpAddress) {
		this.lastLoginIpAddress = lastLoginIpAddress;
	}

	/**
	 *
	 */
	public int getUserId() {
		return this.userId;
	}

	/**
	 *
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 *
	 */
	public String getUsername() {
		return username;
	}
	
	public HashMap<String, String> getPreferences() {
		return preferences;
	}
	
	public void setPreferences(HashMap<String, String> preferences) {
		this.preferences = preferences;
	}

	/**
	 * Shortcut to get the user signature
	 */
	public String getSignature() {
		return preferences.get(UserPreferencesUtil.USER_PREFERENCE_SIGNATURE);
	}
	
	public void setSignature(String signature) {
		preferences.put(UserPreferencesUtil.USER_PREFERENCE_SIGNATURE, signature);
	}
	
	/**
	 * Helper method to get a specific preference
	 */
	public String getPreference(String preferenceKey) {
		return preferences.get(preferenceKey);
	}
	
	/**
	 * Helper method to set a specific preference
	 */
	public void setPreference(String preferenceKey, String preferenceValue) {
		preferences.put(preferenceKey, preferenceValue);
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer("WikiUser ID " + userId  + ": " + username + "; displayName: " + displayName);
		sb.append("; preferences: ");
		for(String key : preferences.keySet()) {
			sb.append(key + "=" + preferences.get(key) + "; ");
		}
		return sb.toString();
	}
}
