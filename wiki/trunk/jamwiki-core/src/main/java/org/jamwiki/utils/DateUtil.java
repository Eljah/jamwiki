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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.jamwiki.Environment;
import org.jamwiki.model.WikiUser;

/**
 * Utility methods for working with dates and time zones.
 */
public class DateUtil {

	private static final WikiLogger logger = WikiLogger.getLogger(DateUtil.class.getName());

	private static final String[] dateFormats = new String[]{ "SHORT",
															  "MEDIUM",
															  "LONG",
															  "FULL",
															  "dd.MM.yyyy HH:mm z" };

	/**
	 * The method returns the current date and/or time for a specific time zone.
	 * The value is returned as a date formatted String, based on the formatting
	 * rules given in the parameter dateFormat.
	 *
	 * Format rules can be found in the javadoc of <a href="http://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html">java.text.SimpleDateFormat</a>
	 *
	 * @param userTimeZone The timezone, whose current time is needed. If the
	 * timezone does not exist, the method returns GMT/UTC, i.e. Greenwich time.
	 * @param dateFormat The format to use for the String. See above for a description
	 * of the existing fields.
	 * @param localeString a String representation of a Locale. This can be the ISO 2 digit code for
	 * the language (e.g. "en") or the combination of language and ISO 2 digit country code (e.g. "en_US").
	 * A value of null or of a non existing locale will force the method to use the default locale of
	 * the server.
	 * @throws IllegalArgumentException Thrown if the dateFormat or localeString is invalid.
	 * @return
	 */
	public static String getUserLocalTime(String userTimeZone, String dateFormat, String localeString) throws IllegalArgumentException {
		TimeZone tz = TimeZone.getDefault();
		if (!StringUtils.isBlank(userTimeZone)) {
			tz = TimeZone.getTimeZone(userTimeZone);
		}
		Locale locale = Locale.getDefault();
		if (!StringUtils.isBlank(localeString)) {
			try {
				locale = LocaleUtils.toLocale(localeString);
			} catch (IllegalArgumentException e) {
				logger.warn("Failure while initializing locale from string: " + localeString);
			}
		}
		String dateFormatString = dateFormat;
		if (dateFormatString == null) {
			dateFormatString = Environment.getDatePatternValue(Environment.PROP_PARSER_SIGNATURE_DATE_PATTERN, true, true);
		}
		SimpleDateFormat df = DateUtil.stringToDateFormat(dateFormatString, locale);
		df.setTimeZone(tz);
		return df.format(Calendar.getInstance(tz).getTime());
	}

	/**
	 *
	 */
	private static SimpleDateFormat stringToDateFormat(String dateFormatString, Locale locale) throws IllegalArgumentException {
		if (StringUtils.isBlank(dateFormatString)) {
			return (SimpleDateFormat)DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		}
		int style = -1;
		if (StringUtils.equalsIgnoreCase(dateFormatString, "SHORT")) {
			style = DateFormat.SHORT;
		} else if (StringUtils.equalsIgnoreCase(dateFormatString, "MEDIUM")) {
			style = DateFormat.MEDIUM;
		} else if (StringUtils.equalsIgnoreCase(dateFormatString, "LONG")) {
			style = DateFormat.LONG;
		} else if (StringUtils.equalsIgnoreCase(dateFormatString, "FULL")) {
			style = DateFormat.FULL;
		} else if (StringUtils.equalsIgnoreCase(dateFormatString, "DEFAULT")) {
			style = DateFormat.DEFAULT;
		}
		if (style != -1) {
			SimpleDateFormat sdf = (SimpleDateFormat)DateFormat.getDateTimeInstance(style, style, locale);
			String pattern = sdf.toPattern();
			if (pattern.indexOf('z') < 0) {
				sdf = new SimpleDateFormat(pattern + " z",locale);
			}
			return sdf;
		} else {
			return new SimpleDateFormat(dateFormatString, locale);
		}
	}

	/**
	 * Returns a list of available time zones. The list is used to get the time zone
	 * of a user in the user preferences dialog.
	 *
	 * @return List of time zones
	 */
	public static String[] getTimeZoneIDs() {
		return TimeZone.getAvailableIDs();
	}

	/**
	 *
	 */
	public static Map<String, String> getDatetimeFormats(WikiUser user) {
		String timezone   = user.getPreference(WikiUser.USER_PREFERENCE_TIMEZONE);
		String locale     = user.getPreference(WikiUser.USER_PREFERENCE_DEFAULT_LOCALE);
		String sysDefault = Environment.getDatePatternValue(Environment.PROP_PARSER_SIGNATURE_DATE_PATTERN, true, true);
		HashMap<String, String> formats = new HashMap<String, String>();
		formats.put("", getUserLocalTime(timezone,sysDefault,locale));
		for (String format : dateFormats) {
			formats.put(format, getUserLocalTime(timezone,format,locale));
		}
		return formats;
	}
}
