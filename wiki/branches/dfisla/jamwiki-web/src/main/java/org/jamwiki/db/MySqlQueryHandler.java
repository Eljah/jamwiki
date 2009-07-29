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
package org.jamwiki.db;

import java.util.Properties;
import org.jamwiki.Environment;
import org.apache.log4j.Logger;

/**
 * MySQL-specific implementation of the QueryHandler interface.  This class implements
 * MySQL-specific methods for instances where MySQL does not support the default
 * ASCII SQL syntax.
 */
public class MySqlQueryHandler extends AnsiQueryHandler {

	private static final Logger logger = Logger.getLogger(MySqlQueryHandler.class.getName());
	private static final String SQL_PROPERTY_FILE_NAME = "sql.mysql.properties";
	private static Properties props = null;
	private static Properties defaults = null;

	/**
	 *
	 */
	protected MySqlQueryHandler() {
		defaults = Environment.loadProperties(AnsiQueryHandler.SQL_PROPERTY_FILE_NAME);
		props = Environment.loadProperties(SQL_PROPERTY_FILE_NAME, defaults);
		super.init(props);
	}
}
