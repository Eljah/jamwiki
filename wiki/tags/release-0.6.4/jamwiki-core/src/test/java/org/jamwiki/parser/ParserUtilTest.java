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
package org.jamwiki.parser;

import junit.framework.TestCase;

public class ParserUtilTest extends TestCase {

	/**
	 *
	 */
	public void testParse() throws Throwable {
		ParserInput parserInput = getParserInput();
		String result = ParserUtil.parse(parserInput, null, "testParserUtilContent");
		//FIXME assertEquals(null,result); //What is the expected behaviour?
	}

	public void testParse1() throws Throwable {
		ParserInput parserInput = getParserInput();
		String result = ParserUtil.parse(parserInput, null, "testParserUtilContent");
		assertEquals("paragraph", "<p>testParserUtilContent\n</p>", result);
	}

	public void testParse2() throws Throwable {
		ParserInput parserInput = getParserInput();
		String result = ParserUtil.parse(parserInput, null, "''it''");
		assertEquals("italics", "<p><i>it</i>\n</p>", result);
	}

	public void testParse3() throws Throwable {
		ParserInput parserInput = getParserInput();
		String result = ParserUtil.parse(parserInput, null, "'''bold'''");
		assertEquals("embolden", "<p><b>bold</b>\n</p>", result);
	}

	public void testParse4() throws Throwable {
		ParserInput parserInput = getParserInput();
		String result = ParserUtil.parse(parserInput, null, "'''''bold it'''''");
		assertEquals("bold+it", "<p><b><i>bold it</i></b>\n</p>", result);
	}

	public void testParse5() throws Throwable {
		ParserInput parserInput = getParserInput();
		String result = ParserUtil.parse(parserInput, null, "testParserUtilContent");
		assertEquals("result.getContent()", "<p>testParserUtilContent\n</p>", result);
	}

	private ParserInput getParserInput() {
		ParserInput parserInput = new ParserInput();
		parserInput.setTopicName("testParserUtilTopicName");
		parserInput.setVirtualWiki("testParserUtilVirtualWiki");
		parserInput.setContext("testParserUtilContext");
		return parserInput;
	}

	/**
	 *
	 */
	public void testParseMinimal() throws Throwable {
		ParserInput parserInput = new ParserInput();
		parserInput.setVirtualWiki("testParserUtilVirtualWiki");
		parserInput.setTopicName("testParserUtilTopicName");
		String result = ParserUtil.parseMinimal(parserInput, "testParserUtilContent");
		assertEquals("result.getContent()", "testParserUtilContent", result);
	}

	/**
	 *
	 */
	public void testParserRedirectContent() throws Throwable {
		String result = ParserUtil.parserRedirectContent("testParserUtilTopicName");
		assertEquals("result", "#REDIRECT [[testParserUtilTopicName]]", result);
	}

	/**
	 *
	 */
	public void testParseMetadataThrowsException() throws Throwable {
		try {
			ParserUtil.parseMetadata(new ParserInput(), "testParserUtilContent");
			fail("Expected Exception to be thrown");
		} catch (Exception ex) {
			assertEquals("ex.getMessage()", "Parser info not properly initialized", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testParseMetadataThrowsNullPointerException() throws Throwable {
		try {
			ParserUtil.parseMetadata(null, "testParserUtilContent");
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testParseThrowsException() throws Throwable {
		try {
			ParserUtil.parse(new ParserInput(), null, "testParserUtilContent");
			fail("Expected Exception to be thrown");
		} catch (Exception ex) {
			assertEquals("ex.getMessage()", "Parser info not properly initialized", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testParseThrowsNullPointerException() throws Throwable {
		try {
			ParserUtil.parse(null, null, "testParserUtilContent");
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testParserOutputThrowsException() throws Throwable {
		try {
			ParserUtil.parserOutput("testParserUtilContent", null, "testParserUtilTopicName");
			fail("Expected Exception to be thrown");
		} catch (Exception ex) {
			assertEquals("ex.getMessage()", "Parser info not properly initialized", ex.getMessage());
		}
	}
}