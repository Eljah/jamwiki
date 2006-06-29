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
 * along with this program (gpl.txt); if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.jamwiki.persistency.file;

import java.io.File;
import java.util.Collection;
import java.util.TreeSet;
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.WikiException;
import org.jamwiki.search.AbstractSearchEngine;
import org.jamwiki.utils.TextFileFilter;
import org.jamwiki.utils.Utilities;

/**
 *
 */
public class FileSearchEngine extends AbstractSearchEngine {

	/** A pointer to self. Used for the singleton pattern */
	private static FileSearchEngine instance = null;

	/**
	 * Create the new FileSearchEnginge. The constructor is private, so that
	 * it cannot be instanciated from outside thsi class. Use getInstance()
	 * instead.
	 *
	 * @throws java.lang.Exception
	 */
	private FileSearchEngine() throws Exception {
	}

	/**
	 * Create an instance of the FileSearchEngine
	 * @return a reference to the only FileSearchEngine existing
	 * @throws java.lang.Exception
	 */
	public static synchronized FileSearchEngine getInstance() throws Exception {
		String baseFileDir = Environment.getValue(Environment.PROP_BASE_FILE_DIR);
		if (baseFileDir == null || baseFileDir.length() == 0) {
			// system not initialized yet
			return null;
		}
		if (instance == null) {
			instance = new FileSearchEngine();
			instance.initSearchEngine();
		}
		return instance;
	}

	/**
	 * find the base directory to use for a virtual wiki
	 * @param virtualWiki the virtual wiki to find the base directory for
	 * @return base directory for a particular virtual wiki
	 */
	protected String dir(String virtualWiki) {
		return FileHandler.fileBase(virtualWiki);
	}

	/**
	 * Return a list of all topic names for one given virtual Wiki
	 *
	 * @param virtualWiki The virtual viki, for which the topic names are requested
	 *
	 * @return A List of Topic Names (Collection of Strings).
	 *
	 * @throws Exception Exception during search
	 * @throws WikiException Virtual wiki cannot be found
	 */
	public Collection getAllTopicNames(String virtualWiki) throws Exception {
		Collection all = new TreeSet();
		if (virtualWiki.equals(WikiBase.DEFAULT_VWIKI)) {
			virtualWiki = "";
		}
		File file = new File(dir(virtualWiki));
		if (!file.exists()) {
			throw new WikiException("Directory can not be accessed: " + file);
		}
		File[] files = file.listFiles(new TextFileFilter());
		for (int i = 0; i < files.length; i++) {
			String fileName = Utilities.decodeSafeFileName(files[i].getName());
			all.add(fileName.substring(0, fileName.length() - FileHandler.EXT.length()));
		}
		return all;
	}

	/**
	 * Get the filename of a topic file.
	 * @see jamwiki.AbstractSearchEngine#getFilename(java.lang.String, java.lang.String)
	 */
	protected String getFilename(String currentWiki, String topic) {
		return FileHandler.getPathFor(currentWiki, null, topic + FileHandler.EXT).getName();
	}
}
