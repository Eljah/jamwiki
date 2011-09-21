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
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * This class provides utilities for working with classpath resources
 * such as property files.
 */
public abstract class ResourceUtil {

	private static final WikiLogger logger = WikiLogger.getLogger(ResourceUtil.class.getName());

	/**
	 * This method is a wrapper for Class.forName that will attempt to load a
	 * class from both the current thread context class loader and the default
	 * class loader.
	 *
	 * @param className The full class name that is to be initialized with the
	 *  <code>Class.forName</code> call.
	 * @throws ClassNotFoundException Thrown if the class cannot be initialized
	 *  from any class loader.
	 */
	public static void forName(String className) throws ClassNotFoundException {
		try {
			// first try using the current thread's class loader
			Class.forName(className, true, Thread.currentThread().getContextClassLoader());
			return;
		} catch (ClassNotFoundException e) {
			logger.info("Unable to load class " + className + " using the thread class loader, now trying the default class loader");
		}
		Class.forName(className);
	}

	/**
	 * Return the current ClassLoader.  First try to get the current thread's
	 * ClassLoader, and if that fails return the ClassLoader that loaded this
	 * class instance.
	 *
	 * @return An instance of the current ClassLoader.
	 */
	private static ClassLoader getClassLoader() {
		ClassLoader loader = null;
		try {
			loader = Thread.currentThread().getContextClassLoader();
		} catch (SecurityException e) {
			logger.debug("Unable to retrieve thread class loader, trying default");
		}
		if (loader == null) {
			loader = ResourceUtil.class.getClassLoader();
		}
		return loader;
	}

	/**
	 * Given a file name for a file that is located somewhere in the application
	 * classpath, return a File object representing the file.
	 *
	 * @param filename The name of the file (relative to the classpath) that is
	 *  to be retrieved.
	 * @return A file object representing the requested filename.  Note that the
	 *  file name is not guaranteed to match the filename passed to this method
	 *  since (for example) the file might be found in a JAR file and thus will
	 *  need to be copied to a temporary location for reading.
	 * @throws FileNotFoundException Thrown if the classloader can not be found or if
	 *  the file can not be found in the classpath.
	 */
	public static File getClassLoaderFile(String filename) throws FileNotFoundException {
		// note that this method is used when initializing logging, so it must
		// not attempt to log anything.
		File file = null;
		ClassLoader loader = ResourceUtil.getClassLoader();
		// Windows machines will have "\" in the path, convert to "/"
		filename = filename.replace('\\', '/');
		URL url = loader.getResource(filename);
		if (url == null) {
			url = ClassLoader.getSystemResource(filename);
		}
		if (url == null) {
			throw new FileNotFoundException("Unable to find " + filename);
		}
		file = FileUtils.toFile(url);
		if (file == null || !file.exists()) {
			InputStream is = null;
			FileOutputStream os = null;
			try {
				// url exists but file cannot be read, so perhaps it's not a "file:" url (an example
				// would be a "jar:" url).  as a workaround, copy the file to a temp file and return
				// the temp file.
				String tempFilename = RandomStringUtils.randomAlphanumeric(20);
				file = File.createTempFile(tempFilename, null);
				is = loader.getResourceAsStream(filename);
				os = new FileOutputStream(file);
				IOUtils.copy(is, os);
			} catch (IOException e) {
				throw new FileNotFoundException("Unable to load file with URL " + url);
			} finally {
				IOUtils.closeQuietly(is);
				IOUtils.closeQuietly(os);
			}
		}
		return file;
	}

	/**
	 * Attempt to get the class loader root directory.  This method works
	 * by searching for a file that MUST exist in the class loader root
	 * and then returning its parent directory.
	 *
	 * @return Returns a file indicating the directory of the class loader.
	 * @throws FileNotFoundException Thrown if the class loader can not be found,
	 *  which may occur if this class is deployed without the jamwiki-war package.
	 */
	public static File getClassLoaderRoot() throws FileNotFoundException {
		// The file hard-coded here MUST exist in the class loader directory.
		File file = ResourceUtil.getClassLoaderFile("sql/sql.ansi.properties");
		if (!file.exists()) {
			throw new FileNotFoundException("Unable to find class loader root");
		}
		return file.getParentFile().getParentFile();
	}

	/**
	 * Given a String representation of a class name (for example, org.jamwiki.db.AnsiDataHandler)
	 * return an instance of the class.  The constructor for the class being instantiated must
	 * not take any arguments.
	 *
	 * @param className The name of the class being instantiated.
	 * @return A Java Object representing an instance of the specified class.
	 */
	public static Object instantiateClass(String className) {
		if (StringUtils.isBlank(className)) {
			throw new IllegalArgumentException("Cannot call instantiateClass with an empty class name");
		}
		logger.debug("Instantiating class: " + className);
		try {
			Class clazz = ClassUtils.getClass(className);
			Class[] parameterTypes = new Class[0];
			Constructor constructor = clazz.getConstructor(parameterTypes);
			Object[] initArgs = new Object[0];
			return constructor.newInstance(initArgs);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Invalid class name specified: " + className, e);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException("Specified class does not have a valid constructor: " + className, e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Specified class does not have a valid constructor: " + className, e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException("Specified class does not have a valid constructor: " + className, e);
		} catch (InstantiationException e) {
			throw new IllegalStateException("Specified class could not be instantiated: " + className, e);
		}
	}

	/**
	 * Utility method for reading a file from a classpath directory and returning
	 * its contents as a String.
	 *
	 * @param filename The name of the file to be read, either as an absolute file
	 *  path or relative to the classpath.
	 * @return A string representation of the file contents.
	 * @throws FileNotFoundException Thrown if the file cannot be found or if an I/O exception
	 *  occurs.
	 */
	public static String readFile(String filename) throws IOException {
		File file = new File(filename);
		if (file.exists()) {
			// file passed in as full path
			return FileUtils.readFileToString(file, "UTF-8");
		}
		// look for file in resource directories
		file = getClassLoaderFile(filename);
		return FileUtils.readFileToString(file, "UTF-8");
	}
}
