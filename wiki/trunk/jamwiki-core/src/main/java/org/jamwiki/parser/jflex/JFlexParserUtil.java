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
package org.jamwiki.parser.jflex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.jamwiki.Environment;
import org.jamwiki.parser.ParserException;
import org.jamwiki.parser.ParserInput;
import org.jamwiki.parser.ParserOutput;
import org.jamwiki.utils.LinkUtil;
import org.jamwiki.utils.WikiLink;
import org.jamwiki.utils.WikiLogger;

/**
 * Utility methods used with the Mediawiki lexers.
 */
public class JFlexParserUtil {

	private static final WikiLogger logger = WikiLogger.getLogger(JFlexParserUtil.class.getName());
	private static final String emptyBodyTagPattern = "(br|div|hr|td|th)";
	private static final String nonNestingTagPattern = "(dd|dl|dt|hr|li|ol|table|tbody|td|tfoot|th|thead|tr|ul)";
	private static final String nonTextBodyTagPattern = "(dl|ol|table|tr|ul)";
	private static final String nonInlineTagPattern = "(caption|dd|div|dl|dt|hr|li|ol|p|table|td|th|tr|ul)";
	private static final String nonInlineTagStartPattern = "<" + nonInlineTagPattern + ">.*";
	private static final String nonInlineTagEndPattern = ".*</" + nonInlineTagPattern + ">";
	private static final Pattern EMPTY_BODY_TAG_PATTERN = Pattern.compile(emptyBodyTagPattern, Pattern.CASE_INSENSITIVE);
	/** Pattern to catch script insertions of the form "onsubmit=". */
	private static final Pattern JAVASCRIPT_PATTERN1 = Pattern.compile("( on[^=]{3,}=)+", Pattern.CASE_INSENSITIVE);
	/** Pattern to catch script insertions that use a javascript url. */
	private static final Pattern JAVASCRIPT_PATTERN2 = Pattern.compile("(javascript[ ]*\\:)+", Pattern.CASE_INSENSITIVE);
	private static final Pattern NON_NESTING_TAG_PATTERN = Pattern.compile(nonNestingTagPattern, Pattern.CASE_INSENSITIVE);
	private static final Pattern NON_TEXT_BODY_TAG_PATTERN = Pattern.compile(nonTextBodyTagPattern, Pattern.CASE_INSENSITIVE);
	private static final Pattern NON_INLINE_TAG_PATTERN = Pattern.compile(nonInlineTagPattern, Pattern.CASE_INSENSITIVE);
	private static final Pattern NON_INLINE_TAG_START_PATTERN = Pattern.compile(nonInlineTagStartPattern, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	private static final Pattern NON_INLINE_TAG_END_PATTERN = Pattern.compile(nonInlineTagEndPattern, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	private static final Pattern TAG_PATTERN = Pattern.compile("(<[ ]*[/]?[ ]*)([^\\ />]+)([ ]*(.*?))([/]?[ ]*>)");
	private static final Pattern WIKI_LINK_PATTERN = Pattern.compile("\\[\\[[ ]*(\\:[ ]*)?[ ]*([^\\n\\r\\|]+)([ ]*\\|[ ]*([^\\n\\r]+))?[ ]*\\]\\]([a-z]*)");

	/**
	 *
	 */
	private JFlexParserUtil() {
	}

	/**
	 * An empty body tag is one that contains no content, such as "br".
	 */
	protected static boolean isEmptyBodyTag(String tagType) {
		if (isRootTag(tagType)) {
			return true;
		}
		Matcher matcher = EMPTY_BODY_TAG_PATTERN.matcher(tagType);
		return matcher.matches();
	}

	/**
	 * An inline tag is a tag that does not affect page flow such as
	 * "b" or "i".  A non-inline tag such as "div" is one that creates
	 * its own display box.
	 */
	protected static boolean isInlineTag(String tagType) {
		if (isRootTag(tagType)) {
			return true;
		}
		Matcher matcher = NON_INLINE_TAG_PATTERN.matcher(tagType);
		return !matcher.matches();
	}

	/**
	 * A non-nesting tag is a tag such as "li" which cannot be nested within
	 * another "li" tag.
	 */
	protected static boolean isNonNestingTag(String tagType) {
		Matcher matcher = NON_NESTING_TAG_PATTERN.matcher(tagType);
		return matcher.matches();
	}

	/**
	 *
	 */
	protected static boolean isNonInlineTagEnd(String tagText) {
		Matcher matcher = NON_INLINE_TAG_END_PATTERN.matcher(tagText);
		return matcher.matches();
	}

	/**
	 *
	 */
	protected static boolean isNonInlineTagStart(String tagText) {
		Matcher matcher = NON_INLINE_TAG_START_PATTERN.matcher(tagText);
		return matcher.matches();
	}

	/**
	 * Evaluate the tag to determine whether it is the parser root tag
	 * that indicates the bottom of the parser tag stack.
	 */
	protected static boolean isRootTag(String tagType) {
		return tagType.equals(JFlexTagItem.ROOT_TAG);
	}

	/**
	 * Determine whether the tag allows text body content.  Some tags, such
	 * as "table", allow only tag content and no text content.
	 */
	protected static boolean isTextBodyTag(String tagType) {
		if (isRootTag(tagType)) {
			return true;
		}
		Matcher matcher = NON_TEXT_BODY_TAG_PATTERN.matcher(tagType);
		return !matcher.matches();
	}

	/**
	 * Provide a way to run the pre-processor against a fragment of text, such
	 * as an image caption.  This method should be used sparingly since it is
	 * not very efficient.
	 */
	protected static String parseFragment(ParserInput parserInput, String raw, int mode) throws ParserException {
		if (StringUtils.isBlank(raw)) {
			return raw;
		}
		JFlexParser parser = new JFlexParser(parserInput);
		ParserOutput parserOutput = new ParserOutput();
		return parser.parseFragment(parserOutput, raw, mode);
	}

	/**
	 * Parse a raw Wiki link of the form "[[link|text]]", and return a WikiLink
	 * object representing the link.
	 *
	 * @param raw The raw Wiki link text.
	 * @return A WikiLink object that represents the link.
	 */
	protected static WikiLink parseWikiLink(String raw) {
		if (StringUtils.isBlank(raw)) {
			return new WikiLink();
		}
		Matcher m = WIKI_LINK_PATTERN.matcher(raw.trim());
		if (!m.matches()) {
			return new WikiLink();
		}
		String url = m.group(2);
		WikiLink wikiLink = LinkUtil.parseWikiLink(url);
		wikiLink.setColon((m.group(1) != null));
		wikiLink.setText(m.group(4));
		String suffix = m.group(5);
		if (!StringUtils.isBlank(suffix)) {
			if (StringUtils.isBlank(wikiLink.getText())) {
				wikiLink.setText(wikiLink.getDestination() + suffix);
			} else {
				wikiLink.setText(wikiLink.getText() + suffix);
			}
		}
		return wikiLink;
	}

	/**
	 * Clean up HTML tags to make them XHTML compliant (lowercase, no
	 * unnecessary spaces).
	 */
	protected static String sanitizeHtmlTag(String tag) {
		String result = tag.trim();
		result = StringUtils.remove(result, " ").toLowerCase();
		if (result.endsWith("/>")) {
			// spaces were stripped, so make sure tag is of the form "<br />"
			result = result.substring(0, result.length() - 2) + " />";
		}
		return result;
	}

	/**
	 * Given a tag of the form "<tag>content</tag>", return all content between
	 * the tags.  Consider the following examples:
	 *
	 * "<tag>content</tag>" returns "content".
	 * "<tag />" returns and empty string.
	 * "<tag><sub>content</sub></tag>" returns "<sub>content</sub>".
	 *
	 * @param raw The raw tag content to be analyzed.
	 * @return The content for the tag being analyzed.
	 */
	protected static String tagContent(String raw) {
		int start = raw.indexOf('>') + 1;
		int end = raw.lastIndexOf('<');
		if (start == 0) {
			// no tags
			return raw;
		}
		if (end <= start) {
			return "";
		}
		return raw.substring(start, end);
	}

	/**
	 * Given an HTML tag, split it into its tag type and tag attributes,
	 * cleaning up the attribtues in the process - allowing Javascript
	 * action tags to be used as attributes (onmouseover, etc) is
	 * a bad thing, so clean up HTML tags to remove any such attributes.
	 */
	protected static String[] parseHtmlTag(String tag) {
		Matcher m = TAG_PATTERN.matcher(tag);
		String[] result = new String[4];
		if (!m.find()) {
			logger.severe("Failure while attempting to match html tag for pattern " + tag);
			return result;
		}
		String tagType = m.group(2).toLowerCase().trim();
		String tagAttributes = m.group(3).trim();
		String tagOpen = m.group(1).trim();
		String tagClose = m.group(5).trim();
		if (!StringUtils.isBlank(tagAttributes)) {
			tagAttributes = JFlexParserUtil.validateHtmlTagAttributes(tagAttributes).trim();
		}
		result[0] = tagType;
		result[1] = tagAttributes;
		result[2] = tagOpen;
		result[3] = tagClose;
		return result;
	}

	/**
	 * Allowing Javascript action tags to be used as attributes (onmouseover, etc) is
	 * a bad thing, so clean up HTML tags to remove any such attributes.
	 */
	protected static String validateHtmlTag(String tag) {
		String[] tagInfo = JFlexParserUtil.parseHtmlTag(tag);
		String tagOpen = tagInfo[2];
		String tagKeyword = tagInfo[0];
		String attributes = tagInfo[1];
		String tagClose = tagInfo[3];
		StringBuffer result = new StringBuffer('<');
		if (tagOpen.indexOf('/') != -1) {
			result.append('/');
		}
		result.append(tagKeyword);
		if (!StringUtils.isBlank(attributes)) {
			result.append(' ').append(attributes);
		}
		if (tagClose.indexOf('/') != -1) {
			tagClose = " />";
		}
		result.append(tagClose.trim());
		return result.toString();
	}

	/**
	 * Allowing Javascript action tags to be used as attributes (onmouseover, etc) is
	 * a bad thing, so clean up HTML tags to remove any such attributes.
	 */
	protected static String validateHtmlTagAttributes(String attributes) {
		if (StringUtils.isBlank(attributes)) {
			return attributes;
		}
		if (!Environment.getBooleanValue(Environment.PROP_PARSER_ALLOW_JAVASCRIPT)) {
			// FIXME - can these two patterns be combined into one?
			// pattern requires a space prior to the "onFoo", so make sure one exists
			Matcher m = JAVASCRIPT_PATTERN1.matcher(" " + attributes);
			if (m.find()) {
				logger.warning("Attempt to include Javascript in Wiki syntax " + attributes);
				return "";
			}
			m = JAVASCRIPT_PATTERN2.matcher(attributes);
			if (m.find()) {
				logger.warning("Attempt to include Javascript in Wiki syntax " + attributes);
				return "";
			}
		}
		return attributes;
	}
}
