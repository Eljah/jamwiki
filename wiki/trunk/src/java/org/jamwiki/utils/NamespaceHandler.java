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

import java.util.Enumeration;
import java.util.Hashtable;
import org.jamwiki.WikiConfiguration;
import org.springframework.util.StringUtils;

/**
 *
 */
public class NamespaceHandler {

	/** Logger */
	private static final WikiLogger logger = WikiLogger.getLogger(NamespaceHandler.class.getName());

	public static final String NAMESPACE_SEPARATOR = ":";
	public static final String NAMESPACE_SPECIAL = initializeNamespace("special", false);
	public static final String NAMESPACE_COMMENTS = initializeNamespace("main", true);
	public static final String NAMESPACE_IMAGE = initializeNamespace("image", false);
	public static final String NAMESPACE_IMAGE_COMMENTS = initializeNamespace("image", true);
	public static final String NAMESPACE_CATEGORY = initializeNamespace("category", false);
	public static final String NAMESPACE_CATEGORY_COMMENTS = initializeNamespace("category", true);
	public static final String NAMESPACE_JAMWIKI = initializeNamespace("jamwiki", false);
	public static final String NAMESPACE_JAMWIKI_COMMENTS = initializeNamespace("jamwiki", true);
	public static final String NAMESPACE_TEMPLATE = initializeNamespace("template", false);
	public static final String NAMESPACE_TEMPLATE_COMMENTS = initializeNamespace("template", true);
	public static final String NAMESPACE_USER = initializeNamespace("user", false);
	public static final String NAMESPACE_USER_COMMENTS = initializeNamespace("user", true);
	public static final String NAMESPACE_HELP = initializeNamespace("help", false);
	public static final String NAMESPACE_HELP_COMMENTS = initializeNamespace("help", true);

	/**
	 *
	 */
	public static String getCommentsNamespace(String namespace) {
		if (!StringUtils.hasText(namespace)) {
			// main namespace
			return NAMESPACE_COMMENTS;
		}
		Hashtable namespaces = WikiConfiguration.getNamespaces();
		Enumeration keys = namespaces.keys();
		while (keys.hasMoreElements()) {
			String key = (String)keys.nextElement();
			String[] values = (String[])namespaces.get(key);
			String main = values[0];
			String comments = values[1];
			if (namespace.equals(NAMESPACE_SPECIAL)) {
				return NAMESPACE_SPECIAL;
			} else if (namespace.equals(main) || (comments != null && namespace.equals(comments))) {
				return comments;
			}
		}
		// unrecognized namespace
		return NAMESPACE_COMMENTS + NAMESPACE_SEPARATOR + namespace;
	}

	/**
	 *
	 */
	public static String getMainNamespace(String namespace) {
		if (!StringUtils.hasText(namespace)) {
			// main namespace
			return "";
		}
		Hashtable namespaces = WikiConfiguration.getNamespaces();
		Enumeration keys = namespaces.keys();
		while (keys.hasMoreElements()) {
			String key = (String)keys.nextElement();
			String[] values = (String[])namespaces.get(key);
			String main = values[0];
			String comments = values[1];
			if (namespace.equals(main) || (comments != null && namespace.equals(comments))) {
				return main;
			}
		}
		// unrecognized namespace
		return namespace;
	}

	/**
	 *
	 */
	private static final String initializeNamespace(String name, boolean isComments) {
		Hashtable namespaces = WikiConfiguration.getNamespaces();
		Enumeration keys = namespaces.keys();
		while (keys.hasMoreElements()) {
			String key = (String)keys.nextElement();
			String[] values = (String[])namespaces.get(key);
			String main = values[0];
			String comments = values[1];
			if (name.equals(key)) {
				return (isComments) ? comments : main;
			}
		}
		logger.warning("Namespace not found in configuration: " + name);
		return null;
	}
}
