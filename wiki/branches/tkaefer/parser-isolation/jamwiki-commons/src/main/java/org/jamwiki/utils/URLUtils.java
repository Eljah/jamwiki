package org.jamwiki.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;

public class URLUtils {
	/** Logger */
	public static final WikiLogger logger = WikiLogger.getLogger(URLUtils.class.getName());

	/**
	 * Decode a value that has been retrieved from a servlet request.  This
	 * method will replace any underscores with spaces.
	 *
	 * @param url The encoded value that is to be decoded.
	 * @return A decoded value.
	 */
	public static String decodeFromRequest(String url) {
		// convert underscores to spaces
		return StringUtils.replace(url, "_", " ");
	}

	/**
	 * Decode a value that has been retrieved directly from a URL or file
	 * name.  This method will URL decode the value and then replace any
	 * underscores with spaces.  Note that this method SHOULD NOT be called
	 * for values retrieved using request.getParameter(), but only values
	 * taken directly from a URL.
	 *
	 * @param url The encoded value that is to be decoded.
	 * @return A decoded value.
	 */
	public static String decodeFromURL(String url) {
		String result = url;
		try {
			result = URLDecoder.decode(result, "UTF-8");
		} catch (Exception e) {
			logger.info("Failure while decoding url " + url + " with charset UTF-8");
		}
		return decodeFromRequest(result);
	}

	/**
	 * Convert a topic name or other value into a value suitable for use as a
	 * file name.  This method replaces spaces with underscores, and then URL
	 * encodes the value.
	 *
	 * @param name The value that is to be encoded for use as a file name.
	 * @return The encoded value.
	 */
	public static String encodeForFilename(String name) {
		// replace spaces with underscores
		String result = StringUtils.replace(name, " ", "_");
		// URL encode the rest of the name
		try {
			result = URLEncoder.encode(result, "UTF-8");
		} catch (Exception e) {
			logger.warning("Failure while encoding " + name + " with charset UTF-8");
		}
		return result;
	}

	/**
	 * Encode a topic name for use in a URL.  This method will replace spaces
	 * with underscores and URL encode the value, but it will not URL encode
	 * colons.
	 *
	 * @param url The topic name to be encoded for use in a URL.
	 * @return The encoded topic name value.
	 */
	public static String encodeForURL(String url) {
		String result = encodeForFilename(url);
		// un-encode colons
		result = StringUtils.replace(result, "%3A", ":");
		// un-encode forward slashes
		result = StringUtils.replace(result, "%2F", "/");
		return result;
	}

}
