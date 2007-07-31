package org.jamwiki.servlets;

import java.util.Properties;

import org.jamwiki.utils.Utilities;

import junit.framework.TestCase;

public class ServeletUtilTestCase extends TestCase {
	public void testUserHandlerInstance() throws Throwable {
		DatabaseUserHandler result = (DatabaseUserHandler) Utilities.userHandlerInstance();
		assertTrue("result.isWriteable()", result.isWriteable());
	}
	
	public void testValidateSystemSettings2() throws Throwable {
		Properties props = new Properties();
		props.put("file-dir-full-path", "testString");
		props.put("homeDir", "testString");
		props.put("url", "testString");
		Utilities.validateSystemSettings(props);
		assertTrue("Test completed without Exception", true);
		// dependencies on static and environment state led to removal of 1 assertion(s)
	}
	
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
	
	public void testValidateSystemSettingsThrowsNullPointerException1() throws Throwable {
		try {
			Utilities.validateSystemSettings(new Properties());
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());
		}
	}
}
