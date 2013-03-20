package org.jamwiki.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.jamwiki.JAMWikiUnitTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests for salt generation.
 * FIXME implement a test for salt containing only salt-valid characters
 */
@RunWith(Parameterized.class)
public class SaltGenerationTest {
	@Parameter(0)
	public int saltRequestLength;
	@Parameter(1)
	public int saltExpectedLength;

	/**
	 * Provides test input and assertion data.
	 */
	@Parameters(name = "request \"{0}\" = result \"{1}\"")
	public static Collection<Object[]> data() {
		return Arrays.asList((new Object[][]{
			{0, 8} // zero length requested, default 8 returned
			, {-1, 8} // negative length requested, default 8 returned
			, {1, 1} // length one requested, length one returned
			, {8, 8} // length eight requested, length eight returned
			, {100, 100} // length 100 requested, length 100 returned
		}));
	}

	/**
	 * Tests salt generation.
	 *
	 * As generated salt is random the only - so far implemented - test is 
	 * for it's length, which should depend on the provided input parameter.
	 * If this is less than or equal to zero the returned salt length should 
	 * be of default length.
	 * Otherwise the length as 'number of characters' should be the same as 
	 * input number.
	 */
	@Test
	public void testGenerateSaltLength() {
		String salt = Encryption.generateSalt(saltRequestLength);
		assertNotNull(salt);
		assertTrue("Default salt length (" + saltRequestLength + ")", 
				saltExpectedLength == salt.length());
	}
}
