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
 */
package org.jamwiki.utils;

import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.jamwiki.Environment;
import org.jamwiki.WikiException;

/**
 * Provide capability for encrypting and decrypting values.  Inspired by an
 * example from http://www.devx.com/assets/sourcecode/10387.zip.
 */
public class Encryption {

	private static final WikiLogger logger = WikiLogger.getLogger(Encryption.class.getName());
	private static final Charset UTF8 = Charset.forName("UTF-8");
	private static final Random SALT_RANDOM = new Random(System.currentTimeMillis());
	// Form at most two matching groups: 1st optional is MessageDigest name, 2nd is real salt
	private static final Pattern PATTERN_SALT = Pattern.compile("(?:\\$([\\p{Print}&&[^\\$]]+))?(?:\\$([\\p{Digit}\\p{Alpha}&&[^\\$]]+))\\$");
	public static final String DES_ALGORITHM = "DES";
	public static final String ENCRYPTION_KEY = "JAMWiki Key 12345";
	public static final int DEFAULT_SALT_LENGTH = 8;

	/**
	 * Hide the constructor by making it private.
	 */
	private Encryption() {
	}

	/**
	 * Encrypt a String value using the DES encryption algorithm.
	 *
	 * @param unencryptedBytes The unencrypted String value that is to be encrypted.
	 * @return An encrypted version of the String that was passed to this method.
	 */
	private static String encrypt64(byte[] unencryptedBytes) throws GeneralSecurityException {
		if (unencryptedBytes == null || unencryptedBytes.length == 0) {
			throw new IllegalArgumentException("Cannot encrypt a null or empty byte array");
		}
		SecretKey key = createKey();
		Cipher cipher = Cipher.getInstance(key.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encryptedBytes = Base64.encodeBase64(cipher.doFinal(unencryptedBytes));
		return new String(encryptedBytes, UTF8);
	}

	/**
	 * Encrypt the password after it's been hashed and 'salted' (if provided).
	 *
	 * @param unencryptedString The plain text password to hash and encrypt.
	 * @param salt
	 *            The salt to use for strengthen the hash, if not 'null' or
	 *            an empty String.<br/>
	 *            The format is supposed to be "'$' + salt + '$'". If
	 *            there's an optional text in format "'$' + value" preceding
	 *            the salt, it is considered to be the {@link MessageDigest}
	 *            name to use. <br/>
	 *            If format pattern cannot be found, the given String is
	 *            trimmed and used as salt as provided.
	 * @return The hashed and encrypted password.<br/>
	 *             If a salt was provided the salt and used hashing algorithm
	 *             is prepended using '$' as delimiting character.
	 */
	public static String encrypt(String unencryptedString, String salt) {
		if (StringUtils.isBlank(unencryptedString)) {
			throw new IllegalArgumentException("Cannot encrypt a null or empty string");
		}
		String realSalt = null;
		MessageDigest md = null;
		String encryptionAlgorithm = Environment.getValue(Environment.PROP_ENCRYPTION_ALGORITHM);
		// If a salt is provided we need to extract the used 'md' and the salt itself
		// to reproduce the same encryption in case we're called to authenticate a user 
		// provided password
		if (!StringUtils.isBlank(salt)) {
			// pessimistic approach: pretend we haven't found 'our syntax' for salt storage, 
			// consider everything provided as salt to use (e.g. initial password encryption for registration, etc.)
			realSalt = salt.trim();
			// test if storage pattern for MessageDigest and salt matches and overrule pessimistic assumption
			Matcher m = PATTERN_SALT.matcher(realSalt);
			if (m.matches()) {
				// it might be no MD-algorithm is stored, but only the salt
				if (null != m.group(1)) {
					encryptionAlgorithm = m.group(1);
					if (logger.isDebugEnabled()) {
							logger.debug("Extracted MessageDigest name '" + encryptionAlgorithm + "'");
					}
				}
				// second regexp matching group (between '$'s) is the wanted salt
				realSalt = m.group(2);
				if (logger.isDebugEnabled()) {
					logger.debug("Extracted salt '" + realSalt + "'");
				}
			}
		}
		try {
			md = MessageDigest.getInstance(encryptionAlgorithm);
		} catch (NoSuchAlgorithmException e) {
			logger.warn("JDK does not support the " + encryptionAlgorithm + " encryption algorithm.  Weaker encryption will be attempted.");
		}
		if (md == null) {
			// fallback to weaker encryption algorithm if nothing better is available
			try {
				md = MessageDigest.getInstance("SHA-1");
			} catch (NoSuchAlgorithmException e) {
				throw new UnsupportedOperationException("JDK does not support the SHA-1 or SHA-512 encryption algorithms");
			}
			// save the algorithm so that if the user upgrades the JDK they can
			// still use passwords encrypted with the weaker algorithm
			Environment.setValue(Environment.PROP_ENCRYPTION_ALGORITHM, "SHA-1");
			try {
				Environment.saveConfiguration();
			} catch (WikiException e) {
				// FIXME - shouldn't this be better handled ???
				logger.info("Failure while saving encryption algorithm property", e);
			}
		}
		try {
			// multi step result String creation done using a StringBuilder
			StringBuilder sb = new StringBuilder((unencryptedString.length() * 2));
			// hash the password itself
			md.update(unencryptedString.getBytes(UTF8));
			
			// if a salt should be used ...
			if (!StringUtils.isBlank(realSalt)) {
				sb.append('$'); // indicator we're not pure hashed password
				sb.append(md.getAlgorithm()).append('$'); // append MessageDigest name
				md.update(realSalt.getBytes(UTF8)); // salt the password hash
				sb.append(realSalt).append('$'); // append used salt
			}
			// finally, weather salt or not, encrypt the password hash and add to result
			sb.append(encrypt64(md.digest()));

			return sb.toString();
		} catch (GeneralSecurityException e) {
			logger.error("Encryption failure", e);
			throw new IllegalStateException("Failure while encrypting value");
		}
	}

	/**
	 * Unencrypt a String value using the DES encryption algorithm.
	 *
	 * @param encryptedString The encrypted String value that is to be unencrypted.
	 * @return An unencrypted version of the String that was passed to this method.
	 */
	private static String decrypt64(String encryptedString) throws GeneralSecurityException {
		if (StringUtils.isBlank(encryptedString)) {
			return encryptedString;
		}
		SecretKey key = createKey();
		Cipher cipher = Cipher.getInstance(key.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] encryptedBytes = encryptedString.getBytes(UTF8);
		byte[] unencryptedBytes = cipher.doFinal(Base64.decodeBase64(encryptedBytes));
		// FIXME The API needs verification; Assumption decrypted bytes form a String is very optimistic
		return new String(unencryptedBytes, UTF8);
	}

	/**
	 * Create the encryption key value.
	 *
	 * @return An encryption key value implementing the DES encryption algorithm.
	 */
	private static SecretKey createKey() throws GeneralSecurityException {
		byte[] bytes = ENCRYPTION_KEY.getBytes(UTF8);
		DESKeySpec spec = new DESKeySpec(bytes);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES_ALGORITHM);
		return keyFactory.generateSecret(spec);
	}

	/**
	 * If a property value is encrypted, return the unencrypted value.  Note that if this
	 * method finds an un-encrypted value it will automatically encrypt it and re-save it to
	 * the property file.
	 *
	 * @param name The name of the encrypted property being retrieved.
	 * @return The unencrypted value of the property.
	 */
	public static String getEncryptedProperty(String name, Properties props) {
		try {
			if (props != null) {
				return Encryption.decrypt64(props.getProperty(name));
			}
			return Encryption.decrypt64(Environment.getValue(name));
		} catch (GeneralSecurityException e) {
			String value = Environment.getValue(name);
			if (props != null || StringUtils.isBlank(value)) {
				logger.error("Encryption failure or no value available for property: " + name, e);
				throw new IllegalStateException("Failure while retrieving encrypted property: " + name);
			}
			// the property might have been unencrypted in the property file, so encrypt, save, and return the value
			logger.warn("Found unencrypted property file value: " + name + ".  Assuming that this value manually un-encrypted in the property file so re-encrypting and re-saving.");
			Encryption.setEncryptedProperty(name, value, null);
			try {
				Environment.saveConfiguration();
			} catch (WikiException ex) {
				logger.error("Failure while saving properties", ex);
				throw new IllegalStateException("Failure while saving properties");
			}
			return value;
		}
	}

	/**
	 * Encrypt and set a property value.
	 *
	 * @param name The name of the encrypted property being retrieved.
	 * @param value The unenencrypted value of the property.
	 * @param props The property object in which the property is being set.
	 */
	public static void setEncryptedProperty(String name, String value, Properties props) {
		String encrypted = "";
		if (!StringUtils.isBlank(value)) {
			byte[] unencryptedBytes = null;
			try {
				unencryptedBytes = value.getBytes(UTF8);
				encrypted = Encryption.encrypt64(unencryptedBytes);
			} catch (GeneralSecurityException e) {
				logger.error("Encryption failure", e);
				throw new IllegalStateException("Failure while encrypting value");
			}
		}
		if (props == null) {
			Environment.setValue(name, encrypted);
		} else {
			props.setProperty(name, encrypted);
		}
	}
	
	/**
	 * Genrates a random salt, usually used for password hashing.
	 * 
	 * The generated salt will have a length of {@value #DEFAULT_SALT_LENGTH} characters.
	 * 
	 * @return The generated salt.
	 * @see Encryption#generateSalt(int)
	 */
	public static String generateSalt() {
		return generateSalt(DEFAULT_SALT_LENGTH);
	}

	/**
	 * Generates a random salt, usually used for password hashing.
	 * 
	 * @param length
	 *            The requested salts length. If zero or a negative value is
	 *            passed the default length of 8 characters is used.
	 * @return The generated salt.
	 */
	public static String generateSalt(final int length) {
		final int saltLen = 0 < length ? length : DEFAULT_SALT_LENGTH;

		// As we filter out a lot of bytes a non-letter/digit fetch more
		// multiplier 5 is determined empirically 
		byte[] tmp = new byte[saltLen * 5];
		// initialize StringBuilder with "a little more than necessary" number
		// of characters as we might have more 'valid' bytes than needed 
		StringBuilder sb = new StringBuilder(saltLen * 2);
		while (sb.length() < saltLen) {
			SALT_RANDOM.nextBytes(tmp);
			// manually "decode" byte for valid US-ASCII letter or digit
			for (int i = 0; i < tmp.length; ++i) {
				if ((0x30 <= tmp[i] && 0x39 >= tmp[i]) || 
						(0x40 <= tmp[i] && 0x5a >= tmp[i]) || 
						(0x61 <= tmp[i] && 0x7a >= tmp[i])) {
					sb.append((char)tmp[i]);
				}
			}
		}
		return sb.substring(0, saltLen);
	}
	
	/**
	 * Extracts the salting part of stored password.
	 *
	 * Everything up to the last '$' character is considered to be part of
	 * the salt.
	 * This can include any preceding - also '$' delimited - text, which 
	 * might form other password hashing hints, e.g. a MessageDigest name.
	 * @see Encryption#encrypt(String, String)
	 *
	 * @param encryptedPassword
	 *            the encrypted password String, eventually containing the
	 *            salt.
	 * @return the extracted salt, or 'null' if non found.
	 */
	public static String extractSalt(final String encryptedPassword) {
		if (StringUtils.isBlank(encryptedPassword)) {
			return null;
		}
		String result = null;
		
		int pos = encryptedPassword.lastIndexOf('$');
		if (-1 < pos) {
			if (encryptedPassword.indexOf('$') != pos) {
				result = encryptedPassword.substring(0, ++pos);
				if (logger.isDebugEnabled()) {
					logger.debug("Extracted salt '" + result + "'");
				}
			}
		}
		
		return result;
	}
}
