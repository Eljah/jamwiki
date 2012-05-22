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
package org.jamwiki.utils;

import org.jamwiki.JAMWikiUnitTest;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class PaginationTest extends JAMWikiUnitTest {

	/**
	 *
	 */
	@Test
	public void testConstructor() throws Throwable {
		Pagination pagination = new Pagination(100, 1000);
		assertEquals("pagination.getNumResults()", 100, pagination.getNumResults());
		assertEquals("pagination.getOffset()", 1000, pagination.getOffset());
	}

	/**
	 *
	 */
	@Test
	public void testGetEnd() throws Throwable {
		int result = new Pagination(0, 0).getEnd();
		assertEquals("result", 0, result);
	}

	/**
	 *
	 */
	@Test
	public void testGetEnd1() throws Throwable {
		int result = new Pagination(100, 1000).getEnd();
		assertEquals("result", 1100, result);
	}

	/**
	 *
	 */
	@Test
	public void testGetNumResults() throws Throwable {
		int result = new Pagination(0, 100).getNumResults();
		assertEquals("result", 0, result);
	}

	/**
	 *
	 */
	@Test
	public void testGetNumResults1() throws Throwable {
		int result = new Pagination(100, 1000).getNumResults();
		assertEquals("result", 100, result);
	}

	/**
	 *
	 */
	@Test
	public void testGetOffset() throws Throwable {
		int result = new Pagination(100, 0).getOffset();
		assertEquals("result", 0, result);
	}

	/**
	 *
	 */
	@Test
	public void testGetOffset1() throws Throwable {
		int result = new Pagination(100, 1000).getOffset();
		assertEquals("result", 1000, result);
	}

	/**
	 *
	 */
	@Test
	public void testGetStart() throws Throwable {
		int result = new Pagination(100, 1000).getStart();
		assertEquals("result", 1000, result);
	}

	/**
	 *
	 */
	@Test
	public void testGetStart1() throws Throwable {
		int result = new Pagination(100, 0).getStart();
		assertEquals("result", 0, result);
	}
}

