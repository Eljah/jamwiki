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
package org.jmwiki;

import java.util.Collection;
import java.util.Locale;

/**
 * Stores a list of usernames for each topic page in the JMWiki
 * system, so that an email can be sent to their registered
 * addresses when changes are made to the associated topic page.
 */
public interface Notify {

	/**
	 *
	 */
	public void addMember(String userName) throws Exception;

	/**
	 *
	 */
	public void removeMember(String userName) throws Exception;

	/**
	 *
	 */
	public boolean isMember(String userName) throws Exception;

	/**
	 *
	 */
	public Collection getMembers() throws Exception;

	/**
	 *
	 */
	public boolean sendNotifications(String rootPath, Locale locale) throws Exception;
}