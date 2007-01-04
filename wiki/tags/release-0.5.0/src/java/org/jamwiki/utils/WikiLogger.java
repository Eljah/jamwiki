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

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides a wrapper around the java.util.logging.Logger class,
 * allowing additional utility methods to be included such as allowing a log
 * message to include a Throwable object.  From an implementation standpoint
 * it would have been much easier to simply sub-class the Logger class, but
 * that class is implemented in such a way to make sub-classes exceedingly
 * difficult to create.
 */
public class WikiLogger {

	private Logger logger = null;

	private static FileHandler DEFAULT_LOG_HANDLER = null;
	private static Level DEFAULT_LOG_LEVEL = null;
	/** Log configuration property file. */
	public final static String LOG_PROPERTIES_FILENAME = "logging.properties";

	static {
		initializeLogParams();
	}

	/**
	 *
	 */
	private WikiLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 *
	 */
	public static WikiLogger getLogger(String name) {
		Logger logger = Logger.getLogger(name);
		if (WikiLogger.DEFAULT_LOG_HANDLER != null) {
			logger.addHandler(WikiLogger.DEFAULT_LOG_HANDLER);
			logger.setLevel(DEFAULT_LOG_LEVEL);
		}
		return new WikiLogger(logger);
	}

	/**
	 *
	 */
	private static void initializeLogParams() {
		FileInputStream stream = null;
		try {
			File propertyFile = WikiLogger.loadProperties();
			stream = new FileInputStream(propertyFile);
			Properties properties = new Properties();
			properties.load(stream);
			String pattern = properties.getProperty("org.jamwiki.pattern");
			int limit = new Integer(properties.getProperty("org.jamwiki.limit")).intValue();
			int count = new Integer(properties.getProperty("org.jamwiki.count")).intValue();
			boolean append = new Boolean(properties.getProperty("org.jamwiki.append")).booleanValue();
			String datePattern = properties.getProperty("org.jamwiki.timestamp");
			DEFAULT_LOG_LEVEL = Level.parse(properties.getProperty("org.jamwiki.level"));
			WikiLogger.DEFAULT_LOG_HANDLER = new FileHandler(pattern, limit, count, append);
			DEFAULT_LOG_HANDLER.setFormatter(new WikiLogFormatter(datePattern));
			DEFAULT_LOG_HANDLER.setLevel(DEFAULT_LOG_LEVEL);
			// test the logger to verify permissions are OK
			Logger logger = Logger.getLogger(WikiLogger.class.getName());
			logger.addHandler(WikiLogger.DEFAULT_LOG_HANDLER);
			logger.setLevel(DEFAULT_LOG_LEVEL);
			logger.config("JAMWiki log initialized from " + propertyFile.getPath() + " with pattern " + pattern);
		} catch (Exception e) {
			System.out.println("WARNING: Unable to load custom JAMWiki logging configuration, using system default " + e.getMessage());
			WikiLogger.DEFAULT_LOG_HANDLER = null;
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (Exception ex) {}
			}
		}
	}

	/**
	 *
	 */
	private static File loadProperties() throws Exception {
		URL url = WikiLogger.getClassLoader().getResource(LOG_PROPERTIES_FILENAME);
		if (url == null) {
			throw new Exception("Log initialization file " + LOG_PROPERTIES_FILENAME + " could not be found");
		}
		File propertyFile = new File(URLDecoder.decode(url.getFile()));
		if (!propertyFile.exists()) {
			throw new Exception("Log initialization file " + LOG_PROPERTIES_FILENAME + " could not be found");
		}
		return propertyFile;
	}

	/**
	 *
	 */
	private static ClassLoader getClassLoader() {
		try {
			return Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			// ignore
		}
		return WikiLogger.class.getClassLoader();
	}

	/**
	 *
	 */
	public void config(String msg) {
		this.logger.config(msg);
	}

	/**
	 *
	 */
	public void config(String msg, Throwable thrown) {
		this.logger.log(Level.CONFIG, msg, thrown);
	}

	/**
	 *
	 */
	public void fine(String msg) {
		this.logger.fine(msg);
	}

	/**
	 *
	 */
	public void fine(String msg, Throwable thrown) {
		this.logger.log(Level.FINE, msg, thrown);
	}

	/**
	 *
	 */
	public void finer(String msg) {
		this.logger.finer(msg);
	}

	/**
	 *
	 */
	public void finer(String msg, Throwable thrown) {
		this.logger.log(Level.FINER, msg, thrown);
	}

	/**
	 *
	 */
	public void finest(String msg) {
		this.logger.finest(msg);
	}

	/**
	 *
	 */
	public void finest(String msg, Throwable thrown) {
		this.logger.log(Level.FINEST, msg, thrown);
	}

	/**
	 *
	 */
	public void info(String msg) {
		this.logger.info(msg);
	}

	/**
	 *
	 */
	public void info(String msg, Throwable thrown) {
		this.logger.log(Level.INFO, msg, thrown);
	}

	/**
	 *
	 */
	public void severe(String msg) {
		this.logger.severe(msg);
	}

	/**
	 *
	 */
	public void severe(String msg, Throwable thrown) {
		this.logger.log(Level.SEVERE, msg, thrown);
	}

	/**
	 *
	 */
	public void warning(String msg) {
		this.logger.warning(msg);
	}

	/**
	 *
	 */
	public void warning(String msg, Throwable thrown) {
		this.logger.log(Level.WARNING, msg, thrown);
	}
}
