package org.jamwiki.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;



public class DateUtil {

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
	 * @return
	 */
	public static String getUserLocalTime(String userTimeZone,String dateFormat,String localeString) {
		TimeZone tz = TimeZone.getDefault();
		if(!StringUtils.isBlank(userTimeZone)) {
			tz = TimeZone.getTimeZone(userTimeZone);

		}
		Locale locale = Locale.getDefault();
		if (!StringUtils.isBlank(localeString)) {
			String[] parts = localeString.split("_");
			switch (parts.length) {
				case 1: {
					locale = new Locale(parts[0]);
					break;
				}
				case 2: {
					locale = new Locale(parts[0],parts[1]);
				}
			}
		}
		DateFormat df = null;
		if(StringUtils.isBlank(dateFormat)) {
			df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		}
		else {
			int style = -1;
			if (StringUtils.equalsIgnoreCase(dateFormat, "SHORT")) {
				style = DateFormat.SHORT;
			} else if (StringUtils.equalsIgnoreCase(dateFormat, "MEDIUM")) {
				style = DateFormat.MEDIUM;
			} else if (StringUtils.equalsIgnoreCase(dateFormat, "LONG")) {
				style = DateFormat.LONG;
			} else if (StringUtils.equalsIgnoreCase(dateFormat, "FULL")) {
				style = DateFormat.FULL;
			}
			if (style != -1) {
				df = DateFormat.getDateTimeInstance(style, style, locale);
			} else {
				df = new SimpleDateFormat(dateFormat,locale);
			}
		}
		df.setTimeZone(tz);
		return df.format(Calendar.getInstance(tz).getTime());
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
}
