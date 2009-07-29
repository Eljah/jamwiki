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

import org.apache.log4j.Logger;


/**
 * This class provides a wrapper around the {@link java.util.logging.Logger}
 * class, allowing additional utility methods to be included such as allowing
 * a log message to include a Throwable object.  From an implementation
 * standpoint it would have been much easier to simply sub-class the Logger
 * class, but that class is implemented in such a way to make sub-classes
 * exceedingly difficult to create.
 *
 * @see org.jamwiki.utils.WikiLogFormatter
 */
public class WikiLogger {

    private Logger logger = null;

    protected WikiLogger(String name) {
        logger = Logger.getLogger(name);
    }

    public static WikiLogger getLoggerInstance(String name) {
        return new WikiLogger(name);
    }

    /**
     * Retrieve a named <code>WikiLogger</code> object.
     *
     * @param name The name of the log object to retrieve or create.
     * @return A logger instance for the given name.
     */
    public static WikiLogger getLogger(String name) {
        return new WikiLogger(name);
    }

    /**
     * Log a message at the {@link java.util.logging.Level#FINE} level,
     * provided that the current log level is {@link java.util.logging.Level#FINE}
     * or greater.
     *
     * @param msg The message to be written to the log.
     */
    public void fine(String msg) {
        this.logger.debug(msg);
    }

    /**
     * Log a message and an exception at the {@link java.util.logging.Level#FINE}
     * level, provided that the current log level is {@link java.util.logging.Level#FINE}
     * or greater.
     *
     * @param msg The message to be written to the log.
     * @param thrown An exception to be written to the log.
     */
    public void fine(String msg, Throwable thrown) {
        this.logger.debug(msg, thrown);
    }

    /**
     * Log a message at the {@link java.util.logging.Level#FINER} level,
     * provided that the current log level is {@link java.util.logging.Level#FINER}
     * or greater.
     *
     * @param msg The message to be written to the log.
     */
    public void finer(String msg) {
        this.logger.debug(msg);
    }

    /**
     * Log a message and an exception at the {@link java.util.logging.Level#FINER}
     * level, provided that the current log level is {@link java.util.logging.Level#FINER}
     * or greater.
     *
     * @param msg The message to be written to the log.
     * @param thrown An exception to be written to the log.
     */
    public void finer(String msg, Throwable thrown) {
        this.logger.debug(msg, thrown);
    }

    /**
     * Log a message at the {@link java.util.logging.Level#FINEST} level,
     * provided that the current log level is {@link java.util.logging.Level#FINEST}
     * or greater.
     *
     * @param msg The message to be written to the log.
     */
    public void finest(String msg) {
        this.logger.trace(msg);
    }

    /**
     * Log a message and an exception at the {@link java.util.logging.Level#FINEST}
     * level, provided that the current log level is {@link java.util.logging.Level#FINEST}
     * or greater.
     *
     * @param msg The message to be written to the log.
     * @param thrown An exception to be written to the log.
     */
    public void finest(String msg, Throwable thrown) {
        this.logger.trace(msg, thrown);
    }

    /**
     * Return <code>true</code> if a log message of level CONFIG can be logged.
     */
    public boolean isConfigEnabled() {
        return true;
    }

    /**
     * Return <code>true</code> if a log message of level FINE can be logged.
     */
    public boolean isFineEnabled() {
        return this.logger.isDebugEnabled();
    }

    /**
     * Return <code>true</code> if a log message of level FINER can be logged.
     */
    public boolean isFinerEnabled() {
        return this.logger.isDebugEnabled();
    }

    /**
     * Return <code>true</code> if a log message of level FINEST can be logged.
     */
    public boolean isFinestEnabled() {
        return this.logger.isTraceEnabled();
    }

    /**
     * Return <code>true</code> if a log message of level INFO can be logged.
     */
    public boolean isInfoEnabled() {
        return this.logger.isInfoEnabled();
    }

    /**
     * Log a message at the {@link java.util.logging.Level#SEVERE} level,
     * provided that the current log level is {@link java.util.logging.Level#SEVERE}
     * or greater.
     *
     * @param msg The message to be written to the log.
     */
    public void severe(String msg) {
        this.logger.fatal(msg);
    }

    /**
     * Log a message and an exception at the {@link java.util.logging.Level#SEVERE}
     * level, provided that the current log level is {@link java.util.logging.Level#SEVERE}
     * or greater.
     *
     * @param msg The message to be written to the log.
     * @param thrown An exception to be written to the log.
     */
    public void severe(String msg, Throwable thrown) {
        this.logger.fatal(msg, thrown);
    }

    /**
     * Log a message at the {@link java.util.logging.Level#WARNING} level,
     * provided that the current log level is {@link java.util.logging.Level#WARNING}
     * or greater.
     *
     * @param msg The message to be written to the log.
     */
    public void warning(String msg) {
        this.logger.warn(msg);
    }

    /**
     * Log a message and an exception at the {@link java.util.logging.Level#WARNING}
     * level, provided that the current log level is {@link java.util.logging.Level#WARNING}
     * or greater.
     *
     * @param msg The message to be written to the log.
     * @param thrown An exception to be written to the log.
     */
    public void warning(String msg, Throwable thrown) {
        this.logger.warn(msg, thrown);
    }

    //------------------------------------- LOG4J METHODS
    public void error(String msg) {
        this.logger.error(msg);
    }

    public void error(String msg, Throwable thrown) {
        this.logger.error(msg, thrown);
    }

    public void debug(String msg) {
        this.logger.debug(msg);
    }

    public void debug(String msg, Throwable thrown) {
        this.logger.debug(msg, thrown);
    }

    public void fatal(String msg) {
        this.logger.fatal(msg);
    }

    public void fatal(String msg, Throwable thrown) {
        this.logger.fatal(msg, thrown);
    }

   /**
     * Log a message at the {@link java.util.logging.Level#INFO} level,
     * provided that the current log level is {@link java.util.logging.Level#INFO}
     * or greater.
     *
     * @param msg The message to be written to the log.
     */
    public void info(String msg) {
        this.logger.info(msg);
    }

    /**
     * Log a message and an exception at the {@link java.util.logging.Level#INFO}
     * level, provided that the current log level is {@link java.util.logging.Level#INFO}
     * or greater.
     *
     * @param msg The message to be written to the log.
     * @param thrown An exception to be written to the log.
     */
    public void info(String msg, Throwable thrown) {
        this.logger.info(msg, thrown);
    }
}
