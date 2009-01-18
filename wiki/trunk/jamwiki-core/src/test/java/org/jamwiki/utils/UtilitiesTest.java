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

import java.io.FileNotFoundException;
import junit.framework.TestCase;

public class UtilitiesTest extends TestCase {

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
	public void testdecodeTopicName() throws Throwable {
		String result = Utilities.decodeTopicName("Page_requested", true);
		assertEquals("Page requested", result);
		result = Utilities.decodeTopicName("Page_requested", false);
		assertEquals("Page_requested", result);
	}

	/**
	 *
	 */
	public void testdecodeAndEscapeTopicName() throws Throwable {
		String result = Utilities.decodeAndEscapeTopicName("\u1342%20", true);
		assertEquals("\u1342 ", result);
	}

	/**
	 *
	 */
	public void testdecodeAndEscapeTopicName2() throws Throwable {
		String result = Utilities.decodeAndEscapeTopicName(null, true);
		assertNull(result);
	}

	/**
	 *
	 */
	public void testdecodeAndEscapeTopicName3() throws Throwable {
		String result = Utilities.decodeAndEscapeTopicName(" ", true);
		assertEquals(" ", result);
	}

	/**
	 *
	 */
	public void testEncodeAndEscapeTopicName() throws Throwable {
		String result = Utilities.encodeAndEscapeTopicName(null);
		assertNull(result);
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
	public void testExtractTrailingPunctuation26() throws Throwable {
		String result = Utilities.extractTrailingPunctuation(null);
		assertEquals("result", "", result);
	}

	/**
	 *
	 */
	public void testIsIpAddress() throws Throwable {
		//test ipv4 addresses
		assertTrue("0.0.0.0", Utilities.isIpAddress("0.0.0.0"));
		assertTrue("1.2.3.4", Utilities.isIpAddress("1.2.3.4"));
		assertTrue("127.0.0.1", Utilities.isIpAddress("127.0.0.1"));
		assertTrue("255.255.255.255", Utilities.isIpAddress("255.255.255.255"));
		assertTrue("240.0.199.200", Utilities.isIpAddress("240.0.199.200"));
		assertFalse("too many numbers", Utilities.isIpAddress("1.2.3.4.5"));
		assertFalse("too many numbers", Utilities.isIpAddress("1.2.3.4."));
		assertFalse("too few numbers", Utilities.isIpAddress("256.1.1"));
		assertFalse("too few numbers", Utilities.isIpAddress("256.1.1."));
		assertFalse("256 is out of range", Utilities.isIpAddress("256.1.1.1"));
		assertFalse("letters instead of numbers", Utilities.isIpAddress("a.b.c.d"));
		assertFalse("extra period", Utilities.isIpAddress("1.2.3..4"));
		//test ipv6 addresses
		assertTrue("0:0:0:0:0:0:0:0", Utilities.isIpAddress("0:0:0:0:0:0:0:0"));
		assertTrue("1234:abcd:eFe9:5500:30:a:99aa:5542", Utilities.isIpAddress("1234:abcd:eFe9:5500:30:a:99aa:5542"));
		assertFalse("too many entries", Utilities.isIpAddress("1234:abcd:eFe9:5500:30:a:99aa:0:0"));
		assertFalse("too many entries", Utilities.isIpAddress("1234:abcd:eFe9:5500:30:a:99aa:0:"));
		assertFalse("too few entries", Utilities.isIpAddress("1234:abcd:eFe9:5500:30:a:99aa"));
		assertFalse("too few entries", Utilities.isIpAddress("1234:abcd:eFe9:5500:30:a:99aa:"));
		assertFalse("g is out of range", Utilities.isIpAddress("123g:abcd:eFe9:5500:30:a:99aa:0"));
		assertFalse("% is out of range", Utilities.isIpAddress("1234:abcd:eFe9:5500:30:a:99aa:0%"));
		assertFalse("extra colon", Utilities.isIpAddress("1234::abcd:eFe9:5500:30:a:99aa:0"));
		//empty and null
		assertFalse("empty string", Utilities.isIpAddress(""));
		assertFalse("null string", Utilities.isIpAddress(null));
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
}