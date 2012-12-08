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

package org.jamwiki.web.utils;

import java.util.Locale;
import java.util.Map;
import java.util.LinkedHashMap;
import org.jamwiki.DataAccessException;
import org.jamwiki.WikiBase;
import org.jamwiki.WikiConfiguration;
import org.jamwiki.model.WikiUser;
import org.jamwiki.utils.DateUtil;
import org.jamwiki.utils.WikiLogger;

public class UserPreferencesUtil {

	private static final WikiLogger logger = WikiLogger.getLogger(UserPreferencesUtil.class.getName());
	private WikiUser user = null;
	private static LinkedHashMap<String, Map<String, String>> defaults = null;
	private static LinkedHashMap<String, Map<String, UserPreferenceItem>> groups = null;

	// This is a workaround. It should be possible to get the signature preview directly
	// from a method...
	public static String signaturePreview = null;

	/**
	 *
	 */
	public UserPreferencesUtil(WikiUser user) {
		this.user = user;
		try {
			defaults = (LinkedHashMap<String, Map<String, String>>)WikiBase.getDataHandler().getUserPreferencesDefaults();
		} catch(DataAccessException e) {
			logger.error(e.toString());
		}
	}

	/**
	 * Convenience method to retrieve the UserPreferenceItem for a single preference.
	 * @param preferenceKey
	 * @return
	 */
	public UserPreferenceItem getPreference(String preferenceKey) {
		LinkedHashMap<String, Map<String, UserPreferenceItem>> map = (LinkedHashMap<String, Map<String, UserPreferenceItem>>)getGroups();
		for (String group : map.keySet()) {
			for (String key : map.get(group).keySet()) {
				if (preferenceKey.equals(key)) {
					return map.get(group).get(key);
				}
			}
		}
		return null;
	}

	/**
	 * The method return a map with the following structure:
	 * pref_group_key -> Map(pref_key -> Instance of UserPreferenceItem for pref_key)
	 * UserPreferenceItems implements the getters necessary to automate the display of
	 * user preferences choices in JSTL.
	 * @return
	 */
	public Map<String, Map<String, UserPreferenceItem>> getGroups() {
		if (defaults == null) {
			try {
				defaults = (LinkedHashMap<String, Map<String, String>>)WikiBase.getDataHandler().getUserPreferencesDefaults();
			} catch(DataAccessException e) {
				logger.error(e.toString());
			}
		}
		if (groups == null) {
			groups = new LinkedHashMap<String, Map<String, UserPreferenceItem>>();
		}
		String lastKey = null;
		LinkedHashMap<String, UserPreferenceItem> items = null;
		for (String group : defaults.keySet()) {
			if (lastKey == null || !lastKey.equals(group)) {
				items = new LinkedHashMap<String, UserPreferenceItem>();
			}
			for (String item : defaults.get(group).keySet()) {
				items.put(item, new UserPreferenceItem(item));
			}
			groups.put(group, items);
		}
		return groups;
	}

	/**
	 *
	 */
	public void setSignaturePreview(String signature) {
		// This is a workaround. It should be possible to get the signature preview directly
		// from a method...
		signaturePreview = signature;
	}

	/**
	 *
	 */
	public class UserPreferenceItem {
		String prefName = null;

		/**
		 *
		 */
		public UserPreferenceItem(String prefName) {
			this.prefName = prefName;
		}

		/**
		 *
		 */
		public String getKey() {
			return this.prefName;
		}

		/**
		 * This must match an entry in the ApplicationResources language file.
		 */
		public String getLabel() {
			return prefName + ".label";
		}

		/**
		 * This must match an entry in the ApplicationResources language file.
		 */
		public String getHelp() {
			return prefName + ".help";
		}

		/**
		 * Add an if statement if a new property must fill a drop down box with
		 * a list of key/value pairs. The key is the value stored in the database for
		 * the user, while value is used to display the content in the dropdown box.
		 */
		public Map getMap() {
			if (prefName.equals(WikiUser.USER_PREFERENCE_TIMEZONE)) {
				return DateUtil.getTimeZoneMap();
			} else if (prefName.equals(WikiUser.USER_PREFERENCE_DEFAULT_LOCALE)) {
				LinkedHashMap<String, String> locales = new LinkedHashMap<String, String>();
				Locale[] localeArray = Locale.getAvailableLocales();
				for (int i = 0; i < localeArray.length; i++) {
					String key = localeArray[i].toString();
					String value = key + " - " + localeArray[i].getDisplayName(localeArray[i]);
					locales.put(key, value);
				}
				return locales;
			} else if (prefName.equals(WikiUser.USER_PREFERENCE_PREFERRED_EDITOR)) {
				return WikiConfiguration.getInstance().getEditors();
			} else if (prefName.equals(WikiUser.USER_PREFERENCE_DATE_FORMAT)) {
				return DateUtil.getDateFormats(user);
			} else if (prefName.equals(WikiUser.USER_PREFERENCE_TIME_FORMAT)) {
				return DateUtil.getTimeFormats(user);
			}
			return null;
		}

		/**
		 * Add an if statement if a property must display a checkbox.
		 */
		public boolean getCheckbox() {
			return false;
		}

		/**
		 * Add an if statement if a property has a preview to display on screen.
		 */
		public String getPreview() {
			return (prefName.equals(WikiUser.USER_PREFERENCE_SIGNATURE)) ? UserPreferencesUtil.signaturePreview : null;
		}
	}
}
