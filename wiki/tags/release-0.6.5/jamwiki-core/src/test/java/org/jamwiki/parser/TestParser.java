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
package org.jamwiki.parser;

import junit.framework.TestCase;
import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;
import org.jamwiki.TestFileUtil;
import org.jamwiki.utils.WikiLogger;

/**
 *
 */
public abstract class TestParser extends TestCase {

	private static final WikiLogger logger = WikiLogger.getLogger(TestParser.class.getName());

	/**
	 *
	 */
	public TestParser(String name) {
		super(name);
	}

	/**
	 *
	 */
	private String parse(String topicName, String raw) throws Exception {
		// set dummy values for parser input
		ParserInput parserInput = new ParserInput();
		parserInput.setContext("/wiki");
		parserInput.setLocale(LocaleUtils.toLocale("en_US"));
		parserInput.setWikiUser(null);
		parserInput.setTopicName(topicName);
		parserInput.setUserIpAddress("0.0.0.0");
		parserInput.setVirtualWiki("en");
		parserInput.setAllowSectionEdit(true);
		ParserOutput parserOutput = new ParserOutput();
		return ParserUtil.parse(parserInput, parserOutput, raw);
	}

	/**
	 *
	 */
	protected void executeParserTest(String topicName) throws Exception {
		String parserResult = this.parserResult(topicName);
		String expectedResult = this.expectedResult(topicName);
		assertEquals(parserResult, expectedResult);
	}

	/**
	 *
	 */
	private String expectedResult(String topicName) throws Exception {
		String raw = TestFileUtil.retrieveFileContent(TestFileUtil.TEST_TOPICS_DIR, topicName);
		String output = this.parse(topicName, raw);
		// FIXME - the sanitize should be unnecessary
		return this.sanitize(output);
	}

	/**
	 *
	 */
	private String parserResult(String topicName) throws Exception {
		String result = TestFileUtil.retrieveFileContent(TestFileUtil.TEST_RESULTS_DIR, topicName);
		// FIXME - the sanitize should be unnecessary
		return this.sanitize(result);
	}

	/**
	 *
	 */
	private String sanitize(String value) {
		return StringUtils.remove(value, '\r').trim();
	}
}
