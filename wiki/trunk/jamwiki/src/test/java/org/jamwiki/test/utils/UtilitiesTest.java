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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.Vector;
import junit.framework.TestCase;
import org.jamwiki.WikiException;
import org.jamwiki.WikiMessage;
import org.jamwiki.db.DatabaseUserHandler;
import org.jamwiki.model.Topic;
import org.jamwiki.parser.ParserDocument;
import org.jamwiki.parser.ParserInput;
import org.jamwiki.utils.SortedProperties;
import org.jamwiki.utils.Utilities;

public class UtilitiesTest extends TestCase {
	/**
	 *
	 */
	public void testAddCookie() throws Throwable {
		// TODO
	}

	/**
	 * Test the buildLocale method from a String of form "fr_FR"
	 */
	public void testBuildLocale() throws Throwable {
		Locale result = Utilities.buildLocale("");
		assertNull("locale build from empty String", result);
		result=Utilities.buildLocale("fr_FR");
		assertEquals(Locale.FRANCE,result);
	}

	/**
	 *
	 */
	public void testBuildLocale1() throws Throwable {
		Locale result = Utilities.buildLocale("fr_FR");
		assertEquals("result.getCountry()", "FR", result.getCountry());
	}

	public void testConvertEncoding1() throws Throwable {
		String expected="ça va là?";
		String utf8 = "Ã§a va lÃ ?";
		/* data utf8 opened as Latin1*/
		String result = Utilities.convertEncoding(utf8, "ISO-8859-1", "UTF-8");
		assertEquals(expected, result);

		/* data Latin1 opened as Latin1 */
		result = Utilities.convertEncoding(expected, "ISO-8859-1", "ISO-8859-1");
		assertEquals(expected, result);
	}

	/**
	 *
	 */
	public void testCurrentUser() throws Throwable {
		// TODO
	}

	/**
	 *
	 */
	public void testDataHandlerInstance() throws Throwable {
		// TODO
	}

	/**
	 *
	 */
	public void testDecodeFromRequest() throws Throwable {
		String result = Utilities.decodeFromRequest("Page_requested");
		assertEquals("Page requested", result);
	}

	/**
	 *
	 */
	public void testDecodeFromURL() throws Throwable {
		String result = Utilities.decodeFromURL("\u1342%20");
		assertEquals("\u1342 ", result);
	}

	/**
	 *
	 */
	public void testEncodeForFilename() throws Throwable {
		//TODO
		String result = Utilities.encodeForFilename("testUtilitiesName");
		assertEquals("result", "testUtilitiesName", result);
	}

	/**
	 *
	 */
	public void testEncodeForURL() throws Throwable {
		//TODO
		String result = Utilities.encodeForURL(null);
		assertNull("result", result);
	}

	/**
	 *
	 */
	public void testExtractCommentsLink() throws Throwable {
		// FIXME
//		String result = Utilities.extractCommentsLink("testUtilitiesName");
//		assertEquals("result", "null:testUtilitiesName", result);
	}

	/**
	 *
	 */
	public void testExtractTopicLink() throws Throwable {
		//TODO
		String result = Utilities.extractTopicLink("testUtilitiesName");
		assertSame("result", "testUtilitiesName", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("(");
		assertEquals("result", "(", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation1() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("");
		assertSame("result", "", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation2() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(".:");
		assertEquals("result", ".:", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation3() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("..");
		assertEquals("result", "..", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation4() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("\"[");
		assertEquals("result", "[", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation5() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("2l+.,).,[.");
		assertEquals("result", ".,).,[.", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation6() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(":,");
		assertEquals("result", ":,", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation7() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(";(");
		assertEquals("result", ";(", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation8() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(",HyvFb+$[.r.[");
		assertEquals("result", ".[", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation9() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("[.");
		assertEquals("result", "[.", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation10() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(":");
		assertEquals("result", ":", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation11() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("8>q/U,p9:r%%/ZU+C0KQBOd,ulNw1X8[UvJ._p,");
		assertEquals("result", ",", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation12() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("7EoB  L6z\"zUTVA_hPUQ'z|q*zd#;..");
		assertEquals("result", ";..", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation13() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("A%!2YR?Ols+Lr'6/4;(r4'\";FSG.$T7u<='..;,");
		assertEquals("result", "..;,", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation14() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("-^");
		assertEquals("result", "", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation15() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(";.");
		assertEquals("result", ";.", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation16() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(";[");
		assertEquals("result", ";[", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation17() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(";");
		assertEquals("result", ";", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation18() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("+]");
		assertEquals("result", "]", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation19() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(" ");
		assertEquals("result", "", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation20() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("h&#U1!&B2xv.");
		assertEquals("result", ".", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation21() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(").");
		assertEquals("result", ").", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation22() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(",");
		assertEquals("result", ",", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation23() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(";..:........");
		assertEquals("result", ";..:........", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation24() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(".1..[,..");
		assertEquals("result", "..[,..", result);
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuation25() throws Throwable {
		String result = Utilities.extractTrailingPunctuation("IT$&,[e;");
		assertEquals("result", ";", result);
	}

	/**
	 *
	 */
	public void testFindRedirectedTopic() throws Throwable {
		Topic parent = new Topic();
		parent.setRedirectTo("testUtilitiesRedirectTo");
		parent.setTopicType(2);
		Topic result = Utilities.findRedirectedTopic(parent, 8);
		assertSame("result", parent, result);
	}

	/**
	 *
	 */
	public void testFindRedirectedTopic1() throws Throwable {
		Topic parent = new Topic();
		parent.setTopicType(2);
		Topic result = Utilities.findRedirectedTopic(parent, 100);
		assertSame("result", parent, result);
	}

	/**
	 *
	 */
	public void testGetVirtualWikiFromRequest() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testIsAdmin() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testIsCommentsPage() throws Throwable {
		//TODO
	}

	/**
	 *
	 */
	public void testIsFirstUse() throws Throwable {
		// FIXME
//		boolean result = Utilities.isFirstUse();
//		assertTrue("result", result);
	}

	/**
	 *
	 */
	public void testIsIpAddress() throws Throwable {
		//test length
		assertTrue("1.2.3.4",Utilities.isIpAddress("1.2.3.4"));
		assertTrue("127.0.0.1",Utilities.isIpAddress("127.0.0.1"));
		assertFalse("1.2.3.4.5",Utilities.isIpAddress("1.2.3.4.5"));
		//out of bound values
		assertFalse("out of bound value",Utilities.isIpAddress("256.2.3.4"));
		assertFalse("out of bound value",Utilities.isIpAddress("1.257.3.4"));
		assertFalse("out of bound value",Utilities.isIpAddress("1.2.258.4"));
		assertFalse("out of bound value",Utilities.isIpAddress("1.2.3.259"));
		assertFalse("out of bound value",Utilities.isIpAddress("-1.2.3.4"));
		//empty and null
		assertFalse("empty string",Utilities.isIpAddress(""));
		assertFalse("null string",Utilities.isIpAddress(null));
	}


	/**
	 *
	 */
	public void testIsUpgrade() throws Throwable {
		//TODO
	}

	/**
	 *
	 */
	public void testLogin() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testParse() throws Throwable {
		ParserInput parserInput = getParserInput();
		String result = Utilities.parse(parserInput, null, "testUtilitiesContent");
		//FIXME assertEquals(null,result); //What is the expected behaviour?
	}
	public void testParse1() throws Throwable {
		ParserInput parserInput = getParserInput();
		String result = Utilities.parse(parserInput, null, "testUtilitiesContent");
		assertEquals("paragraph", "<p>testUtilitiesContent\n</p>", result);

	}
	public void testParse2() throws Throwable {
		ParserInput parserInput = getParserInput();
		String result = Utilities.parse(parserInput, null, "''it''");
		assertEquals("italics", "<p><i>it</i>\n</p>", result);
	}
	public void testParse3() throws Throwable {
		ParserInput parserInput = getParserInput();
		String result = Utilities.parse(parserInput, null, "'''bold'''");
		assertEquals("embolden", "<p><b>bold</b>\n</p>", result);
	}

	public void testParse4() throws Throwable {
		ParserInput parserInput = getParserInput();
		String result = Utilities.parse(parserInput, null, "'''''bold it'''''");
		assertEquals("bold+it", "<p><b><i>bold it</i></b>\n</p>", result);
	}

	public void testParse5() throws Throwable {
		ParserInput parserInput = getParserInput();
		String result = Utilities.parse(parserInput, null, "testUtilitiesContent");
		assertEquals("result.getContent()", "<p>testUtilitiesContent\n</p>", result);
	}

	private ParserInput getParserInput() {
		ParserInput parserInput = new ParserInput();
		parserInput.setTopicName("testUtilitiesTopicName");
		parserInput.setVirtualWiki("testUtilitiesVirtualWiki");
		parserInput.setContext("testUtilitiesContext");
		return parserInput;
	}

	/**
	 *
	 */
	public void testParseMetadata() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testParseMinimal() throws Throwable {
		ParserInput parserInput = new ParserInput();
		parserInput.setVirtualWiki("testUtilitiesVirtualWiki");
		parserInput.setTopicName("testUtilitiesTopicName");
		String result = Utilities.parseMinimal(parserInput, "testUtilitiesContent");
		assertEquals("result.getContent()", "testUtilitiesContent", result);
	}

	/**
	 *
	 */
	public void testParseSlice() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testParseSplice() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testParserDocument() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testParserRedirectContent() throws Throwable {
		String result = Utilities.parserRedirectContent("testUtilitiesTopicName");
		assertEquals("result", "#REDIRECT [[testUtilitiesTopicName]]", result);
	}

	/**
	 *
	 */
	public void testUserHandlerInstance() throws Throwable {
		DatabaseUserHandler result = (DatabaseUserHandler) Utilities.userHandlerInstance();
		assertTrue("result.isWriteable()", result.isWriteable());
	}

	/**
	 *
	 */
	public void testValidateDirectory() throws Throwable {
		// FIXME
//		WikiMessage result = Utilities.validateDirectory("");
//		assertEquals("result.getKey()", "error.directorywrite", result.getKey());
	}

	/**
	 *
	 */
	public void testValidateDirectory1() throws Throwable {
		WikiMessage result = Utilities.validateDirectory("testUtilitiesName");
		assertEquals("result.getKey()", "error.directoryinvalid", result.getKey());
	}

	/**
	 *
	 */
	public void testValidateSystemSettings() throws Throwable {
		// FIXME
//		Properties props = new Properties();
//		props.put("homeDir", "testString");
//		props.put("parser", "org.jamwiki.parser.AbstractParser");
//		props.put("file-dir-full-path", "8&=\fCcx[|;o\nlAR*JsUiU1,\\\fH/)5 h{3c4Wxc;s");
//		Vector result = Utilities.validateSystemSettings(props);
//		assertEquals("result.size()", 5, result.size());
	}

	/**
	 *
	 */
	public void testValidateSystemSettings1() throws Throwable {
		Properties props = new Properties(new SortedProperties());
		props.put("file-dir-full-path", ")%2F");
		props.put("homeDir", "testString");
		props.put("parser", "org.jamwiki.parser.AbstractParser");
		props.put("url", "testString");
		Utilities.validateSystemSettings(props);
		assertTrue("Test completed without Exception", true);
		// dependencies on static and environment state led to removal of 1 assertion(s)
	}

	/**
	 *
	 */
	public void testValidateSystemSettings2() throws Throwable {
		Properties props = new Properties();
		props.put("file-dir-full-path", "testString");
		props.put("homeDir", "testString");
		props.put("url", "testString");
		Utilities.validateSystemSettings(props);
		assertTrue("Test completed without Exception", true);
		// dependencies on static and environment state led to removal of 1 assertion(s)
	}

	/**
	 *
	 */
	public void testValidateUserName() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testAddCookieThrowsNullPointerException() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testCurrentWatchlistThrowsNullPointerException() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testExtractCommentsLinkThrowsException() throws Throwable {
		try {
			Utilities.extractCommentsLink("");
			fail("Expected Exception to be thrown");
		} catch (Exception ex) {
			assertEquals("ex.getMessage()", "Empty topic name ", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testExtractTopicLinkThrowsException() throws Throwable {
		try {
			Utilities.extractTopicLink("");
			fail("Expected Exception to be thrown");
		} catch (Exception ex) {
			assertEquals("ex.getMessage()", "Empty topic name ", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testExtractTrailingPunctuationThrowsNullPointerException() throws Throwable {
		try {
			Utilities.extractTrailingPunctuation(null);
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testFindRedirectedTopicThrowsClassNotFoundException() throws Throwable {
		Topic parent = new Topic();
		parent.setRedirectTo("testUtilitiesRedirectTo");
		parent.setTopicType(2);
		parent.setVirtualWiki("testUtilitiesVirtualWiki");
		try {
			Utilities.findRedirectedTopic(parent, 8);
			fail("Expected ClassNotFoundException to be thrown");
		} catch (ClassNotFoundException ex) {
			assertTrue("Test completed without Exception", true);
			// dependencies on static and environment state led to removal of 3 assertion(s)
		}
	}

	/**
	 *
	 */
	public void testFindRedirectedTopicThrowsClassNotFoundException1() throws Throwable {
		Topic parent = new Topic();
		parent.setRedirectTo("testUtilitiesRedirectTo");
		parent.setTopicType(2);
		parent.setVirtualWiki("testUtilitiesVirtualWiki");
		try {
			Utilities.findRedirectedTopic(parent, 9);
			fail("Expected ClassNotFoundException to be thrown");
		} catch (ClassNotFoundException ex) {
			assertTrue("Test completed without Exception", true);
			// dependencies on static and environment state led to removal of 3 assertion(s)
		}
	}

	/**
	 *
	 */
	public void testFindRedirectedTopicThrowsNullPointerException() throws Throwable {
		try {
			Utilities.findRedirectedTopic(null, 100);
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testFindRedirectedTopicThrowsWikiException() throws Throwable {
		Topic parent = new Topic();
		parent.setRedirectTo("testUtilitiesRedirectTo");
		parent.setTopicType(2);
		try {
			Utilities.findRedirectedTopic(parent, 10);
			fail("Expected WikiException to be thrown");
		} catch (WikiException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
			assertEquals("ex.getWikiMessage().getKey()", "topic.redirect.infinite", ex.getWikiMessage().getKey());
		}
	}

	/**
	 *
	 */
	public void testFormatMessageThrowsMissingResourceException() throws Throwable {
		// FIXME
//		try {
//			Utilities.formatMessage("testUtilitiesKey", Locale.JAPAN);
//			fail("Expected MissingResourceException to be thrown");
//		} catch (MissingResourceException ex) {
//			assertEquals("ex.getMessage()", "Can't find bundle for base name ApplicationResources, locale ja_JP", ex.getMessage());
//		}
	}

	/**
	 *
	 */
	public void testFormatMessageThrowsMissingResourceException1() throws Throwable {
		// FIXME
//		Object[] objects = new Object[0];
//		try {
//			Utilities.formatMessage("testUtilitiesKey", Locale.KOREA, objects);
//			fail("Expected MissingResourceException to be thrown");
//		} catch (MissingResourceException ex) {
//			assertEquals("ex.getMessage()", "Can't find bundle for base name ApplicationResources, locale ko_KR", ex.getMessage());
//		}
	}

	/**
	 *
	 */
	public void testFormatMessageThrowsNullPointerException() throws Throwable {
		try {
			Utilities.formatMessage("testUtilitiesKey", null);
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testFormatMessageThrowsNullPointerException1() throws Throwable {
		Object[] objects = new Object[2];
		try {
			Utilities.formatMessage("testUtilitiesKey", null, objects);
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testGetClassLoaderFileThrowsException() throws Throwable {
		try {
			Utilities.getClassLoaderFile("testUtilitiesFilename");
			fail("Expected Exception to be thrown");
		} catch (Exception ex) {
			assertEquals("ex.getMessage()", "Unable to find testUtilitiesFilename", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testGetClassLoaderFileThrowsNullPointerException() throws Throwable {
		// FIXME - this seems to give different results on JDK 1.4 and JDK 1.6
//		try {
//			Utilities.getClassLoaderFile(null);
//			fail("Expected NullPointerException to be thrown");
//		} catch (NullPointerException ex) {
//			assertNull("ex.getMessage()", ex.getMessage()); // Adjusted to other NPE
//		}
	}

	/**
	 *
	 */
	public void testGetClassLoaderRootThrowsException() throws Throwable {
		// FIXME
//		try {
//			Utilities.getClassLoaderRoot();
//			fail("Expected Exception to be thrown");
//		} catch (Exception ex) {
//			assertEquals("ex.getMessage()", "Unable to find ApplicationResources.properties", ex.getMessage());
//		}
	}

	/**
	 *
	 */
	public void testGetTopicFromURIThrowsStringIndexOutOfBoundsException() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testGetVirtualWikiFromURIThrowsStringIndexOutOfBoundsException() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testLoginThrowsStringIndexOutOfBoundsException() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testParseMetadataThrowsException() throws Throwable {
		try {
			Utilities.parseMetadata(new ParserInput(), "testUtilitiesContent");
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
			Utilities.parseMetadata(null, "testUtilitiesContent");
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testParseSliceThrowsClassNotFoundException() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testParseSpliceThrowsClassNotFoundException() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testParseThrowsException() throws Throwable {
		try {
			Utilities.parse(new ParserInput(), null, "testUtilitiesContent");
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
			Utilities.parse(null, null, "testUtilitiesContent");
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testParserDocumentThrowsException() throws Throwable {
		try {
			Utilities.parserDocument("testUtilitiesContent", null, "testUtilitiesTopicName");
			fail("Expected Exception to be thrown");
		} catch (Exception ex) {
			assertEquals("ex.getMessage()", "Parser info not properly initialized", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testReadFileThrowsFileNotFoundException() throws Throwable {
		try {
			Utilities.readFile("fakeFileName");
			fail("Expected FileNotFoundException to be thrown");
		} catch (FileNotFoundException ex) {
			assertTrue("Test completed without Exception", true);
			// dependencies on static and environment state led to removal of 2 assertion(s)
		}
	}

	/**
	 *
	 */
	public void testReadFileThrowsFileNotFoundException1() throws Throwable {
		try {
			Utilities.readFile("testUtilitiesFilename");
			fail("Expected FileNotFoundException to be thrown");
		} catch (FileNotFoundException ex) {
			assertEquals("ex.getClass()", FileNotFoundException.class, ex.getClass());
		}
	}

	/**
	 *
	 */
	public void testReadFileThrowsNullPointerException() throws Throwable {
		try {
			Utilities.readFile(null);
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testReadSpecialPageThrowsFileNotFoundException() throws Throwable {
		try {
			Utilities.readSpecialPage(null, "testUtilitiesPageName");
			fail("Expected FileNotFoundException to be thrown");
		} catch (FileNotFoundException ex) {
			assertEquals("ex.getClass()", FileNotFoundException.class, ex.getClass());
		}
	}

	/**
	 *
	 */
	public void testReadSpecialPageThrowsFileNotFoundException1() throws Throwable {
		try {
			Utilities.readSpecialPage(Locale.GERMAN, "testUtilitiesPageName");
			fail("Expected FileNotFoundException to be thrown");
		} catch (FileNotFoundException ex) {
			assertEquals("ex.getClass()", FileNotFoundException.class, ex.getClass());
		}
	}

	/**
	 *
	 */
	public void testReadSpecialPageThrowsFileNotFoundException2() throws Throwable {
		try {
			Utilities.readSpecialPage(Locale.SIMPLIFIED_CHINESE, "testUtilitiesPageName");
			fail("Expected FileNotFoundException to be thrown");
		} catch (FileNotFoundException ex) {
			assertEquals("ex.getClass()", FileNotFoundException.class, ex.getClass());
		}
	}

	/**
	 *
	 */
	public void testValidateDirectoryThrowsNullPointerException() throws Throwable {
		try {
			Utilities.validateDirectory(null);
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testValidateSystemSettingsThrowsNullPointerException() throws Throwable {
		// FIXME - implement this
	}

	/**
	 *
	 */
	public void testValidateSystemSettingsThrowsNullPointerException1() throws Throwable {
		try {
			Utilities.validateSystemSettings(new Properties());
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testValidateTopicNameThrowsNullPointerException() throws Throwable {
		try {
			Utilities.validateTopicName(null);
			fail("Expected NullPointerException to be thrown");
		} catch (WikiException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}

	/**
	 *
	 */
	public void testValidateTopicNameThrowsWikiException() throws Throwable {
		try {
			Utilities.validateTopicName("");
			fail("Expected WikiException to be thrown");
		} catch (WikiException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
			assertEquals("ex.getWikiMessage().getKey()", "common.exception.notopic", ex.getWikiMessage().getKey());
		}
	}

	/**
	 *
	 */
	public void testValidateTopicNameThrowsWikiException1() throws Throwable {
		try {
			Utilities.validateTopicName("testUtilities\rName");
			fail("Expected WikiException to be thrown");
		} catch (WikiException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
			assertEquals("ex.getWikiMessage().getKey()", "common.exception.name", ex.getWikiMessage().getKey());
		}
	}

	/**
	 *
	 */
	public void testValidateUserNameThrowsWikiException() throws Throwable {
		try {
			Utilities.validateUserName("testUtilities\rName");
			fail("Expected WikiException to be thrown");
		} catch (WikiException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
			assertEquals("ex.getWikiMessage().getKey()", "common.exception.name", ex.getWikiMessage().getKey());
		}
	}

	/**
	 *
	 */
	public void testValidateUserNameThrowsWikiException1() throws Throwable {
		try {
			Utilities.validateUserName("");
			fail("Expected WikiException to be thrown");
		} catch (WikiException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
			assertEquals("ex.getWikiMessage().getKey()", "error.loginempty", ex.getWikiMessage().getKey());
		}
	}
}