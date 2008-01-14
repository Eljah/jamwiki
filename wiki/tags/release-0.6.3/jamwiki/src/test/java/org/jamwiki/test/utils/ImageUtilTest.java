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
 *
 * Based on code generated by Agitar build: Agitator Version 1.0.2.000071 (Build date: Jan 12, 2007) [1.0.2.000071]
 */
package org.jamwiki.test.utils;

import junit.framework.TestCase;
import org.jamwiki.model.WikiImage;
import org.jamwiki.utils.ImageUtil;

/**
 *
 */
public class ImageUtilTest extends TestCase {

	/**
	 *
	 */
	public void testInitializeImageThrowsNullPointerException() throws Throwable {
		try {
			ImageUtil.initializeImage(null, 0);
			fail("Expected IllegalArgumentException to be thrown");
		} catch (IllegalArgumentException ex) {
			assertNotNull(ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testIsImageThrowsNullPointerException() throws Throwable {
		try {
			ImageUtil.isImage(null);
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}
}

