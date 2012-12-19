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
package org.jamwiki.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.jamwiki.WikiConfiguration;
import org.jamwiki.model.WikiUser;

/**
 * Utility methods for working with dates and time zones.
 */
public class DateUtil {

	private static final WikiLogger logger = WikiLogger.getLogger(DateUtil.class.getName());
	public enum DateFormatType { DATE_ONLY, TIME_ONLY, DATE_AND_TIME };

	/**
	 * Format the given date using the given pattern to return the date
	 * as a formatted string.
	 */
	public static String formatDate(Date date, String pattern, String localeString, String timeZoneString, DateFormatType dateFormatType) {
		Locale locale = DateUtil.stringToLocale(localeString);
		TimeZone tz = DateUtil.stringToTimeZone(timeZoneString);
		SimpleDateFormat sdf = null;
		int style = DateUtil.stringToDateFormatStyle(pattern);
		if (style != -1 && dateFormatType == DateFormatType.DATE_ONLY) {
			sdf = (SimpleDateFormat) DateFormat.getDateInstance(style, locale);
		} else if (style != -1 && dateFormatType == DateFormatType.TIME_ONLY) {
			sdf = (SimpleDateFormat) DateFormat.getTimeInstance(style, locale);
		} else if (style != -1 && dateFormatType == DateFormatType.DATE_AND_TIME) {
			sdf = (SimpleDateFormat) DateFormat.getDateTimeInstance(style, style, locale);
		} else {
			sdf = new SimpleDateFormat(pattern, locale);
		}
		sdf.setTimeZone(tz);
		return sdf.format(date);
	}

	/**
	 * Given a string, return the matching DateFormat style (SHORT, LONG, etc)
	 * or -1 if there is no corresponding style.
	 */
	public static int stringToDateFormatStyle(String format) {
		if (StringUtils.equalsIgnoreCase(format, "SHORT")) {
			return DateFormat.SHORT;
		} else if (StringUtils.equalsIgnoreCase(format, "MEDIUM")) {
			return DateFormat.MEDIUM;
		} else if (StringUtils.equalsIgnoreCase(format, "LONG")) {
			return DateFormat.LONG;
		} else if (StringUtils.equalsIgnoreCase(format, "FULL")) {
			return DateFormat.FULL;
		} else if (StringUtils.equalsIgnoreCase(format, "DEFAULT")) {
			return DateFormat.DEFAULT;
		}
		return -1;
	}

	/**
	 *
	 */
	public static Locale stringToLocale(String localeString) {
		Locale locale = Locale.getDefault();
		if (!StringUtils.isBlank(localeString)) {
			try {
				locale = LocaleUtils.toLocale(localeString);
			} catch (IllegalArgumentException e) {
				logger.warn("Failure while converting string to locale: " + localeString);
			}
		}
		return locale;
	}

	/**
	 * Convert a string to a time zone.  Return the default time zone if the
	 * conversion fails.
	 */
	public static TimeZone stringToTimeZone(String timeZoneString) {
		TimeZone tz = TimeZone.getDefault();
		if (!StringUtils.isBlank(timeZoneString)) {
			try {
				tz = TimeZone.getTimeZone(timeZoneString);
			} catch (IllegalArgumentException e) {
				logger.warn("Failure while converting string to time zone: " + timeZoneString);
			}
		}
		return tz;
	}

	/**
	 * Returns a list of available time zones. The list is used to get the time zone
	 * of a user in the user preferences dialog.
	 *
	 * @return List of time zones
	 */
	public static Map<String, String> getTimeZoneMap() {
		Map<String, String> timeZoneMap = new TreeMap<String, String>();
		for (String timeZoneId : TimeZone.getAvailableIDs()) {
			timeZoneMap.put(timeZoneId, timeZoneId);
		}
		return timeZoneMap;
	}

	/**
	 * Return a map whose entries are the date format pattern and
	 * the current date formatted using the pattern.
	 */
	public static Map<String, String> getDateFormats(WikiUser user) {
		String timeZoneString = user.getPreference(WikiUser.USER_PREFERENCE_TIMEZONE);
		String localeString = user.getPreference(WikiUser.USER_PREFERENCE_DEFAULT_LOCALE);
		Date now = new Date();
		Map<String, String> formats = new LinkedHashMap<String, String>();
		for (String format : WikiConfiguration.getInstance().getDateFormats()) {
			formats.put(format, DateUtil.formatDate(now, format, localeString, timeZoneString, DateFormatType.DATE_ONLY));
		}
		return formats;
	}

	/**
	 * Return a map whose entries are the time format pattern and
	 * the current time formatted using the pattern.
	 */
	public static Map<String, String> getTimeFormats(WikiUser user) {
		String timeZoneString = user.getPreference(WikiUser.USER_PREFERENCE_TIMEZONE);
		String localeString = user.getPreference(WikiUser.USER_PREFERENCE_DEFAULT_LOCALE);
		Date now = new Date();
		Map<String, String> formats = new LinkedHashMap<String, String>();
		for (String format : WikiConfiguration.getInstance().getTimeFormats()) {
			formats.put(format, DateUtil.formatDate(now, format, localeString, timeZoneString, DateFormatType.TIME_ONLY));
		}
		return formats;
	}
}
