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
package org.jamwiki.servlets;

import org.jamwiki.Environment;
import org.jamwiki.JAMWikiUnitTest;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import static org.junit.Assert.*;

public class WikiPageInfoTest extends JAMWikiUnitTest {

	/**
	 *
	 */
	@Test
	public void testGetVirtualWiki() {
		MockHttpServletRequest mockRequest = this.getMockHttpServletRequest("/virtual/Topic");
		WikiPageInfo p = new WikiPageInfo(mockRequest);
		assertEquals(p.getVirtualWikiName(), "virtual", p.getVirtualWikiName());
		p.setVirtualWikiName("en");
		assertEquals(p.getVirtualWikiName(), "en", p.getVirtualWikiName());
		mockRequest = this.getMockHttpServletRequest("/");
		p = new WikiPageInfo(mockRequest);
		assertEquals(p.getVirtualWikiName(), Environment.getValue(Environment.PROP_VIRTUAL_WIKI_DEFAULT), p.getVirtualWikiName());
	}

	/**
	 *
	 */
	private MockHttpServletRequest getMockHttpServletRequest(String url) {
		MockServletContext mockContext = new MockServletContext("context");
		return new MockHttpServletRequest(mockContext, "GET", url);
	}
}
