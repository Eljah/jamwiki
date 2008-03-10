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

import java.util.Properties;
import junit.framework.TestCase;

/**
 *
 */
public class EncryptionTest extends TestCase {

	/**
	 *
	 */
	public void testDecrypt64() throws Throwable {
		String result = Encryption.decrypt64("");
		assertEquals("result", "", result);
	}

	/**
	 *
	 */
	public void testDecrypt641() throws Throwable {
		String result = Encryption.decrypt64(null);
		assertNull("result", result);
	}

	/**
	 *
	 */
	public void testEncrypt64() throws Throwable {
		String result = Encryption.encrypt64((String) null);
		assertNull("result", result);
	}

	/**
	 *
	 */
	public void testEncrypt641() throws Throwable {
		String result = Encryption.encrypt64("");
		assertEquals("result", "", result);
	}

	/**
	 *
	 */
	public void testGetEncryptedProperty() throws Throwable {
		Encryption.getEncryptedProperty("", null);
		assertTrue("Test completed without Exception", true);
		// dependencies on static and environment state led to removal of 1 assertion(s)
	}

	/**
	 *
	 */
	public void testGetEncryptedProperty1() throws Throwable {
		Properties props = new SortedProperties();
		props.put("", "");
		String result = Encryption.getEncryptedProperty("", props);
		assertSame("result", "", result);
	}

	/**
	 *
	 */
	public void testGetEncryptedProperty2() throws Throwable {
		String result = Encryption.getEncryptedProperty("testEncryptionName", new Properties());
		assertNull("result", result);
	}

	/**
	 *
	 */
	public void testSetEncryptedProperty() throws Throwable {
		Encryption.setEncryptedProperty("testEncryptionName", "", null);
		assertTrue("Test completed without Exception", true);
	}

	/**
	 *
	 */
	public void testSetEncryptedProperty1() throws Throwable {
		Encryption.setEncryptedProperty("testEncryptionName", null, null);
		assertTrue("Test completed without Exception", true);
	}

	/**
	 *
	 */
	public void testSetEncryptedProperty2() throws Throwable {
		Properties props = new Properties();
		Encryption.setEncryptedProperty("testEncryptionName", "", props);
		assertEquals("props.size()", 1, props.size());
		assertEquals("props.get(\"testEncryptionName\")", "", props.get("testEncryptionName"));
	}

	/**
	 *
	 */
	public void testSetEncryptedProperty3() throws Throwable {
		Properties props = new SortedProperties();
		Encryption.setEncryptedProperty("testEncryptionName", null, props);
		assertEquals("(SortedProperties) props.size()", 1, props.size());
		assertEquals("(SortedProperties) props.get(\"testEncryptionName\")", "", props.get("testEncryptionName"));
	}

	/**
	 *
	 */
	public void testEncrypt64ThrowsNullPointerException() throws Throwable {
		try {
			Encryption.encrypt64((byte[]) null);
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			// FIXME - do something here
		}
	}

	/**
	 *
	 */
	public void testEncryptThrowsNullPointerException() throws Throwable {
		try {
			Encryption.encrypt(null);
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			// FIXME - do something here
		}
	}
}

