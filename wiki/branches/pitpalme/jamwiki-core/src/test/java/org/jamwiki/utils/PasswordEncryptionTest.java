package org.jamwiki.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collection;

import org.jamwiki.JAMWikiUnitTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class PasswordEncryptionTest {
	// Static password used for all tests
	private static final String TEST_PASSWORD = "password";
	// 'template' for tests using variations of salts
	private static final String TEST_SALT = "12345678";

	@Parameter(0)
	public String testSalt;
	@Parameter(1)
	public String expectation;
	
	/**
	 * Provides test input and assertion data.
	 */
	@Parameters(name = "salt = \"{0}\"")
	public static Collection<Object[]> data() {
		return Arrays.asList((new Object[][] {
			// no salt, default hashing
			{null, "HXelSWb4gdI13Z6Qjd6jLgxZnw9dFFBLikjSURfR54w0fkzpkEs8lyiA88ReCVW5giX8AfrNjHw9XobpsAQVpzdEvUzqWnQo"}
			// empty salt, default hashing
			, {"", "HXelSWb4gdI13Z6Qjd6jLgxZnw9dFFBLikjSURfR54w0fkzpkEs8lyiA88ReCVW5giX8AfrNjHw9XobpsAQVpzdEvUzqWnQo"}
			// blank only salt, default hashing
			, {"   ", "HXelSWb4gdI13Z6Qjd6jLgxZnw9dFFBLikjSURfR54w0fkzpkEs8lyiA88ReCVW5giX8AfrNjHw9XobpsAQVpzdEvUzqWnQo"}
			// pure salt, full blown result
			, {TEST_SALT, "$SHA-512$12345678$sxSVgcB5QVZZnPKq5dpzNcWfyxHLu33NrC0uR7vAkCIwgmvEjg/N70ZhURZPUSxy1dztdj/EaLiAkEPEbzT1NDdEvUzqWnQo"}
			// '$' delimited - salt only
			, {"$" + TEST_SALT + "$", "$SHA-512$12345678$sxSVgcB5QVZZnPKq5dpzNcWfyxHLu33NrC0uR7vAkCIwgmvEjg/N70ZhURZPUSxy1dztdj/EaLiAkEPEbzT1NDdEvUzqWnQo"}
			// full blown test, including default MessageDigest name and salt
			, {"$SHA-512$" + TEST_SALT + "$", "$SHA-512$12345678$sxSVgcB5QVZZnPKq5dpzNcWfyxHLu33NrC0uR7vAkCIwgmvEjg/N70ZhURZPUSxy1dztdj/EaLiAkEPEbzT1NDdEvUzqWnQo"}
			// full blown test, including non-default MessageDigest name and salt
			, {"$SHA-1$" + TEST_SALT + "$", "$SHA-1$12345678$N/uJVMTwbGsozrOnEt+Y4FMhNiJD7vqW"}
		}));
	}
	
	/**
	 * Tests password encryption.
	 *
	 * Parameterized input data cover use cases without and with salt - 
	 * the latter without and with MessageDigest.
	 */
	@Test
	public void testPasswordEncryption() {
		String result = Encryption.encrypt(TEST_PASSWORD, testSalt);
		assertNotNull(result);
		assertEquals("Salt " + testSalt, expectation, result);
	}
}
