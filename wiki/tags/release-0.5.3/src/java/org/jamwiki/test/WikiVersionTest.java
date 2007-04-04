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
package org.jamwiki.test;

import junit.framework.TestCase;
import org.jamwiki.WikiVersion;

public class WikiVersionTest extends TestCase {

	/**
	 *
	 */
	public void testConstructor() throws Throwable {
		WikiVersion wikiVersion = new WikiVersion("0.5.1");
		assertFalse("Constructor failed to properly set up version", wikiVersion.before(0, 5, 1));
		assertFalse("Constructor failed to properly set up version", wikiVersion.before(0, 5, 0));
		assertTrue("Constructor failed to properly set up version", wikiVersion.before(0, 5, 2));
	}

	/**
	 *
	 */
	public void testBefore() throws Throwable {
		WikiVersion version = new WikiVersion("0.5.1");
		boolean result = version.before(version);
		assertFalse("result", result);
	}

	/**
	 *
	 */
	public void testBefore1() throws Throwable {
		boolean result = new WikiVersion("0.5.1").before(100, 1000, 0);
		assertTrue("result", result);
	}

	/**
	 *
	 */
	public void testBefore2() throws Throwable {
		boolean result = new WikiVersion("0.5.1").before(0, 100, 1000);
		assertTrue("result", result);
	}

	/**
	 *
	 */
	public void testBefore3() throws Throwable {
		boolean result = new WikiVersion("0.5.1").before(0, 5, 100);
		assertTrue("result", result);
	}

	/**
	 *
	 */
	public void testBefore4() throws Throwable {
		boolean result = new WikiVersion("0.5.1").before(0, -1, 100);
		assertFalse("result", result);
	}

	/**
	 *
	 */
	public void testConstructorThrowsException() throws Throwable {
		try {
			new WikiVersion(".LLx]FUAw,.4LvE$e$XNmc 0<P.SL;nQfRQO.4UXw");
			fail("Expected Exception to be thrown");
		} catch (Exception ex) {
			assertNotNull("Invalid exception message", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testConstructorThrowsException1() throws Throwable {
		try {
			new WikiVersion("0.5.u1");
			fail("Expected Exception to be thrown");
		} catch (Exception ex) {
			assertNotNull("Invalid exception message", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testConstructorThrowsException2() throws Throwable {
		try {
			new WikiVersion("X\u0006Fl\\s\u000E6T\u000Ec>\fiS.\u00049gk\u00050'n$<y%](o\u0000!@2\u001A\u0016P%");
			fail("Expected Exception to be thrown");
		} catch (Exception ex) {
			assertNotNull("Invalid exception message", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testConstructorThrowsException3() throws Throwable {
		try {
			new WikiVersion("");
			fail("Expected Exception to be thrown");
		} catch (Exception ex) {
			assertNotNull("Invalid exception message", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testConstructorThrowsNumberFormatException() throws Throwable {
		try {
			new WikiVersion("')_.g|R.?'bx$||7,v`FvmpuUBNy,$C,/mvi^M[}@");
			fail("Expected NumberFormatException to be thrown");
		} catch (NumberFormatException ex) {
			assertNotNull("Invalid exception message", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testConstructorThrowsNumberFormatException1() throws Throwable {
		try {
			new WikiVersion("0.B5.1");
			fail("Expected NumberFormatException to be thrown");
		} catch (NumberFormatException ex) {
			assertNotNull("Invalid exception message", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testBeforeThrowsNullPointerException() throws Throwable {
		try {
			new WikiVersion("0.5.1").before(null);
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			// FIXME - do something here
		}
	}
}