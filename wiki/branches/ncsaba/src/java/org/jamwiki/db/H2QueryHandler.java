package org.jamwiki.db;

import org.jamwiki.utils.WikiLogger;
import org.jamwiki.Environment;

import java.util.Properties;

/**
 * H2DB-specific implementation of the QueryHandler interface.  This class implements
 * H2DB-specific methods for instances where H2DB does not support the default
 * ASCII SQL syntax.
 */
public class H2QueryHandler extends AnsiQueryHandler {

    private static WikiLogger logger = WikiLogger.getLogger(H2QueryHandler.class.getName());
    private static final String SQL_PROPERTY_FILE_NAME = "sql.h2.properties";
    private static Properties props = null;
    private static Properties defaults = null;

    /**
     *
     */
    protected H2QueryHandler() {
        H2QueryHandler.defaults = Environment.loadProperties(AnsiQueryHandler.SQL_PROPERTY_FILE_NAME);
        H2QueryHandler.props = Environment.loadProperties(H2QueryHandler.SQL_PROPERTY_FILE_NAME, H2QueryHandler.defaults);
        super.init(H2QueryHandler.props);
    }
}
