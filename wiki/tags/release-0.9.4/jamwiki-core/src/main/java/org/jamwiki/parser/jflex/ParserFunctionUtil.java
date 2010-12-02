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

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jamwiki.DataAccessException;
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.model.Namespace;
import org.jamwiki.parser.ParserException;
import org.jamwiki.parser.ParserInput;
import org.jamwiki.parser.ParserOutput;
import org.jamwiki.utils.ImageUtil;
import org.jamwiki.utils.LinkUtil;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiLogger;

/**
 * Process parser functions.  See http://www.mediawiki.org/wiki/Help:Magic_words#Parser_functions.
 */
public class ParserFunctionUtil {

	private static final WikiLogger logger = WikiLogger.getLogger(ParserFunctionUtil.class.getName());
	private static final String PARSER_FUNCTION_ANCHOR_ENCODE = "anchorencode:";
	private static final String PARSER_FUNCTION_FILE_PATH = "filepath:";
	private static final String PARSER_FUNCTION_FULL_URL = "fullurl:";
	private static final String PARSER_FUNCTION_EXPR = "#expr:";
	private static final String PARSER_FUNCTION_IF = "#if:";
	private static final String PARSER_FUNCTION_IF_EQUAL = "#ifeq:";
	private static final String PARSER_FUNCTION_LOCAL_URL = "localurl:";
	private static final String PARSER_FUNCTION_LOWER_CASE = "lc:";
	private static final String PARSER_FUNCTION_LOWER_CASE_FIRST = "lcfirst:";
	private static final String PARSER_FUNCTION_NAMESPACE = "ns:";
	private static final String PARSER_FUNCTION_NAMESPACE_ESCAPED = "nse:";
	private static final String PARSER_FUNCTION_UPPER_CASE = "uc:";
	private static final String PARSER_FUNCTION_UPPER_CASE_FIRST = "ucfirst:";
	private static final String PARSER_FUNCTION_URL_ENCODE = "urlencode:";
	private static final String MAGIC_DISPLAY_TITLE = "DISPLAYTITLE:";
	private static List<String> PARSER_FUNCTIONS = new ArrayList<String>();

	static {
		// parser functions
		PARSER_FUNCTIONS.add(PARSER_FUNCTION_ANCHOR_ENCODE);
		PARSER_FUNCTIONS.add(PARSER_FUNCTION_FILE_PATH);
		PARSER_FUNCTIONS.add(PARSER_FUNCTION_FULL_URL);
		PARSER_FUNCTIONS.add(PARSER_FUNCTION_EXPR);
		PARSER_FUNCTIONS.add(PARSER_FUNCTION_IF);
		PARSER_FUNCTIONS.add(PARSER_FUNCTION_IF_EQUAL);
		PARSER_FUNCTIONS.add(PARSER_FUNCTION_LOCAL_URL);
		PARSER_FUNCTIONS.add(PARSER_FUNCTION_LOWER_CASE);
		PARSER_FUNCTIONS.add(PARSER_FUNCTION_LOWER_CASE_FIRST);
		PARSER_FUNCTIONS.add(PARSER_FUNCTION_NAMESPACE);
		PARSER_FUNCTIONS.add(PARSER_FUNCTION_NAMESPACE_ESCAPED);
		PARSER_FUNCTIONS.add(PARSER_FUNCTION_UPPER_CASE);
		PARSER_FUNCTIONS.add(PARSER_FUNCTION_UPPER_CASE_FIRST);
		PARSER_FUNCTIONS.add(PARSER_FUNCTION_URL_ENCODE);
		PARSER_FUNCTIONS.add(MAGIC_DISPLAY_TITLE);
	}

	/**
	 * Determine if a template name corresponds to a parser function requiring
	 * special handling.  See http://meta.wikimedia.org/wiki/Help:Magic_words
	 * for a list of Mediawiki parser functions.  If the template name is a parser
	 * function then return the parser function name and argument.
	 */
	protected static String[] parseParserFunctionInfo(String name) {
		int pos = name.indexOf(':');
		if (pos == -1 || (pos + 2) > name.length()) {
			return null;
		}
		String parserFunction = name.substring(0, pos + 1).trim();
		String parserFunctionArguments = name.substring(pos + 1).trim();
		if (!PARSER_FUNCTIONS.contains(parserFunction)) {
			return null;
		}
		return new String[]{parserFunction, parserFunctionArguments};
	}

	/**
	 * Process a parser function, returning the value corresponding to the parser
	 * function result.  See http://meta.wikimedia.org/wiki/Help:Magic_words for a
	 * list of Mediawiki parser functions.
	 */
	protected static String processParserFunction(ParserInput parserInput, ParserOutput parserOutput, String parserFunction, String parserFunctionArguments) throws DataAccessException, ParserException {
		String[] parserFunctionArgumentArray = ParserFunctionUtil.parseParserFunctionArgumentArray(parserFunctionArguments);
		if (parserFunction.equals(PARSER_FUNCTION_ANCHOR_ENCODE)) {
			return Utilities.encodeAndEscapeTopicName(parserFunctionArgumentArray[0]);
		}
		if (parserFunction.equals(PARSER_FUNCTION_FILE_PATH)) {
			return ParserFunctionUtil.parseFilePath(parserInput, parserFunctionArgumentArray);
		}
		if (parserFunction.equals(PARSER_FUNCTION_FULL_URL)) {
			return ParserFunctionUtil.parseFileUrl(parserInput, parserFunctionArgumentArray);
		}
		if (parserFunction.equals(PARSER_FUNCTION_EXPR)) {
			return ParserFunctionUtil.parseExpr(parserInput, parserFunctionArgumentArray);
		}
		if (parserFunction.equals(PARSER_FUNCTION_IF)) {
			return ParserFunctionUtil.parseIf(parserInput, parserFunctionArgumentArray);
		}
		if (parserFunction.equals(PARSER_FUNCTION_IF_EQUAL)) {
			return ParserFunctionUtil.parseIfEqual(parserInput, parserFunctionArgumentArray);
		}
		if (parserFunction.equals(PARSER_FUNCTION_LOCAL_URL)) {
			return ParserFunctionUtil.parseLocalUrl(parserInput, parserFunctionArgumentArray);
		}
		if (parserFunction.equals(PARSER_FUNCTION_LOWER_CASE)) {
			return ParserFunctionUtil.parseLowerCase(parserInput, parserFunctionArgumentArray);
		}
		if (parserFunction.equals(PARSER_FUNCTION_LOWER_CASE_FIRST)) {
			return ParserFunctionUtil.parseLowerCaseFirst(parserInput, parserFunctionArgumentArray);
		}
		if (parserFunction.equals(PARSER_FUNCTION_NAMESPACE)) {
			return ParserFunctionUtil.parseNamespace(parserInput, parserFunctionArgumentArray, false);
		}
		if (parserFunction.equals(PARSER_FUNCTION_NAMESPACE_ESCAPED)) {
			return ParserFunctionUtil.parseNamespace(parserInput, parserFunctionArgumentArray, true);
		}
		if (parserFunction.equals(PARSER_FUNCTION_UPPER_CASE)) {
			return ParserFunctionUtil.parseUpperCase(parserInput, parserFunctionArgumentArray);
		}
		if (parserFunction.equals(PARSER_FUNCTION_UPPER_CASE_FIRST)) {
			return ParserFunctionUtil.parseUpperCaseFirst(parserInput, parserFunctionArgumentArray);
		}
		if (parserFunction.equals(PARSER_FUNCTION_URL_ENCODE)) {
			return ParserFunctionUtil.parseUrlEncode(parserInput, parserFunctionArgumentArray);
		}
		if (parserFunction.equals(MAGIC_DISPLAY_TITLE)) {
			return ParserFunctionUtil.parseDisplayTitle(parserInput, parserOutput, parserFunctionArgumentArray);
		}
		return null;
	}

	/**
	 * Parse the {{filepath}} parser function.
	 */
	private static String parseFilePath(ParserInput parserInput, String[] parserFunctionArgumentArray) throws DataAccessException {
		// pre-pend the image namespace to the file name
		String filename = Namespace.namespace(Namespace.FILE_ID).getLabel(parserInput.getVirtualWiki()) + Namespace.SEPARATOR + parserFunctionArgumentArray[0];
		String result = ImageUtil.buildImageFileUrl(parserInput.getContext(), parserInput.getVirtualWiki(), filename);
		if (result == null) {
			return "";
		}
		result = LinkUtil.normalize(Environment.getValue(Environment.PROP_FILE_SERVER_URL) + result);
		if (parserFunctionArgumentArray.length > 1 && parserFunctionArgumentArray[1].equalsIgnoreCase("nowiki")) {
			// add nowiki tags so that the next round of parsing does not convert to an HTML link
			result = "<nowiki>" + result + "</nowiki>";
		}
		return result;
	}

	/**
	 * Parse the {{fileurl:}} parser function.
	 */
	private static String parseFileUrl(ParserInput parserInput, String[] parserFunctionArgumentArray) throws DataAccessException {
		String result = LinkUtil.buildTopicUrl(parserInput.getContext(), parserInput.getVirtualWiki(), parserFunctionArgumentArray[0], false);
		result = LinkUtil.normalize(Environment.getValue(Environment.PROP_SERVER_URL) + result);
		if (parserFunctionArgumentArray.length > 1 && !StringUtils.isBlank(parserFunctionArgumentArray[1])) {
			result += "?" + parserFunctionArgumentArray[1];
		}
		return result;
	}

	/**
	 * Parse the {{#expr:}} parser function.  Usage: {{#expr: expression }}.
	 */
	private static String parseExpr(ParserInput parserInput, String[] parserFunctionArgumentArray) throws DataAccessException,  ParserException {
		String expr = parserFunctionArgumentArray[0];
		return ParserFunctionUtil.evaluateExpression(expr);
	}

	/**
	 *
	 */
	private static String evaluateExpression(String expr) {
		if (StringUtils.isBlank(expr)) {
			return "";
		}
		StringBuilder expression = new StringBuilder(expr);
		List<BigDecimal> stack = new ArrayList<BigDecimal>();
		String next = "";
		char part;
		int i = 0;
		while (i < expression.length()) {
			part = expression.charAt(i);
			if (Character.isDigit(part) || part == '.') {
				next += part;
			} else if (part == ')') {
				// unmatched closing parentheses
				return ((i > 0) ? expression.substring(0, i) : "") + "<strong class=\"error\">" + part + "</strong>" + ((i < expression.length() - 1) ? expression.substring(i + 1) : "");
			} else if (part == '(') {
				int end = Utilities.findMatchingEndTag(expression, i, "(", ")");
				if (end == -1) {
					// unmatched opening parentheses
					return ((i > 0) ? expression.substring(0, i) : "") + "<strong class=\"error\">" + part + "</strong>" + ((i < expression.length() - 1) ? expression.substring(i + 1) : "");
				}
				String result = ParserFunctionUtil.evaluateExpression(expression.substring(i + 1, end - 1));
				expression = expression.replace(i, end, "");
				try {
					stack.add(new BigDecimal(result));
				} catch (NumberFormatException e) {
					return result;
				}
			} else if (!StringUtils.isBlank(next)) {
				stack.add(new BigDecimal(next));
				next = "";
			}
			i++;
		}
		if (!StringUtils.isBlank(next)) {
			stack.add(new BigDecimal(next));
		}
		BigDecimal a, b;
		for (i = 0; i < expression.length(); i++) {
			part = expression.charAt(i);
			try {
				switch (part) {
					case '+':
						a = stack.remove(0);
						b = stack.remove(0);
						stack.add(0, a.add(b));
						break;
					case '-':
						a = stack.remove(0);
						b = stack.remove(0);
						stack.add(0, a.subtract(b));
						break;
					case '*':
						a = stack.remove(0);
						b = stack.remove(0);
						stack.add(0, a.multiply(b));
						break;
					case '/':
						a = stack.remove(0);
						b = stack.remove(0);
						stack.add(0, a.divide(b));
						break;
				}
			} catch (ArithmeticException e) {
				return "<strong class=\"error\">" + expr + "</strong>";
			} catch (IndexOutOfBoundsException e) {
				return "<strong class=\"error\">" + expr + "</strong>";
			}
		}
		double result = stack.get(0).doubleValue();
		// strip any trailing zeroes
		return ((Math.round(result) == result) ? Long.toString(Math.round(result)) : Double.toString(result));
	}

	/**
	 * Parse the {{#if:}} parser function.  Usage: {{#if: test | true | false}}.
	 */
	private static String parseIf(ParserInput parserInput, String[] parserFunctionArgumentArray) throws DataAccessException,  ParserException {
		boolean condition = ((parserFunctionArgumentArray.length >= 1) ? !StringUtils.isBlank(parserFunctionArgumentArray[0]) : false);
		// parse to handle any embedded templates
		if (condition) {
			return (parserFunctionArgumentArray.length >= 2) ? JFlexParserUtil.parseFragment(parserInput, parserFunctionArgumentArray[1], JFlexParser.MODE_PREPROCESS) : "";
		} else {
			return (parserFunctionArgumentArray.length >= 3) ? JFlexParserUtil.parseFragment(parserInput, parserFunctionArgumentArray[2], JFlexParser.MODE_PREPROCESS) : "";
		}
	}

	/**
	 * Parse the {{#ifeq:}} parser function.  Usage: {{#ifeq: value1 | value2 | true | false}}.
	 */
	private static String parseIfEqual(ParserInput parserInput, String[] parserFunctionArgumentArray) throws DataAccessException,  ParserException {
		String arg1 = ((parserFunctionArgumentArray.length >= 1) ? parserFunctionArgumentArray[0] : "");
		String arg2 = ((parserFunctionArgumentArray.length >= 2) ? parserFunctionArgumentArray[1] : "");
		String result1 = ((parserFunctionArgumentArray.length >= 3) ? parserFunctionArgumentArray[2] : "");
		String result2 = ((parserFunctionArgumentArray.length >= 4) ? parserFunctionArgumentArray[3] : "");
		boolean equals = StringUtils.equals(arg1, arg2);
		if (!equals && NumberUtils.isNumber(arg1) && NumberUtils.isNumber(arg2)) {
			// compare numerically
			BigDecimal bigDecimal1 = new BigDecimal(arg1);
			BigDecimal bigDecimal2 = new BigDecimal(arg2);
			equals = (bigDecimal1.compareTo(bigDecimal2) == 0);
		}
		// parse to handle any embedded templates
		if (equals) {
			return JFlexParserUtil.parseFragment(parserInput, result1, JFlexParser.MODE_PREPROCESS);
		} else {
			return JFlexParserUtil.parseFragment(parserInput, result2, JFlexParser.MODE_PREPROCESS);
		}
	}

	/**
	 * Parse the {{localurl:}} parser function.
	 */
	private static String parseLocalUrl(ParserInput parserInput, String[] parserFunctionArgumentArray) throws DataAccessException {
		String result = LinkUtil.buildTopicUrl(parserInput.getContext(), parserInput.getVirtualWiki(), parserFunctionArgumentArray[0], false);
		if (parserFunctionArgumentArray.length > 1 && !StringUtils.isBlank(parserFunctionArgumentArray[1])) {
			result += "?" + parserFunctionArgumentArray[1];
		}
		return result;
	}

	/**
	 * Parse the {{lc:}} parser function.
	 */
	private static String parseLowerCase(ParserInput parserInput, String[] parserFunctionArgumentArray) {
		return StringUtils.lowerCase(parserFunctionArgumentArray[0]);
	}

	/**
	 * Parse the {{lcfirst:}} parser function.
	 */
	private static String parseLowerCaseFirst(ParserInput parserInput, String[] parserFunctionArgumentArray) {
		return StringUtils.uncapitalize(parserFunctionArgumentArray[0]);
	}

	/**
	 * Parse the {{ns:}} and {{nse:}} parser functions.
	 */
	private static String parseNamespace(ParserInput parserInput, String[] parserFunctionArgumentArray, boolean escape) throws DataAccessException {
		int namespaceId = NumberUtils.toInt(parserFunctionArgumentArray[0], -10);
		Namespace namespace = null;
		if (namespaceId != -10) {
			namespace = WikiBase.getDataHandler().lookupNamespaceById(namespaceId);
		} else {
			namespace = WikiBase.getDataHandler().lookupNamespace(parserInput.getVirtualWiki(), Utilities.decodeAndEscapeTopicName(parserFunctionArgumentArray[0], true));
		}
		String result = ((namespace == null) ? "" : namespace.getLabel(parserInput.getVirtualWiki()));
		if (StringUtils.isBlank(result)) {
			return "";
		}
		return (escape) ? Utilities.encodeAndEscapeTopicName(result) : result;
	}

	/**
	 * Parse the {{uc:}} parser function.
	 */
	private static String parseUpperCase(ParserInput parserInput, String[] parserFunctionArgumentArray) {
		return StringUtils.upperCase(parserFunctionArgumentArray[0]);
	}

	/**
	 * Parse the {{ucfirst:}} parser function.
	 */
	private static String parseUpperCaseFirst(ParserInput parserInput, String[] parserFunctionArgumentArray) {
		return StringUtils.capitalize(parserFunctionArgumentArray[0]);
	}

	/**
	 * Parse the {{urlencode:}} parser function.
	 */
	private static String parseUrlEncode(ParserInput parserInput, String[] parserFunctionArgumentArray) {
		try {
			return URLEncoder.encode(parserFunctionArgumentArray[0], "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// this should never happen
			throw new IllegalStateException("Unsupporting encoding UTF-8");
		}
	}

	/**
	 *
	 */
	private static String parseDisplayTitle(ParserInput parserInput, ParserOutput parserOutput, String[] parserFunctionArgumentArray) {
		String pageTitle = parserFunctionArgumentArray[0];
		if (pageTitle != null) {
			if (StringUtils.equals(Utilities.decodeAndEscapeTopicName(pageTitle, true), parserInput.getTopicName())) {
				parserOutput.setPageTitle(parserFunctionArgumentArray[0]);
			}
		}
		return "";
	}

	/**
	 * Parse parser function arguments of the form "arg1|arg2", trimming excess whitespace
	 * and returning an array of results.
	 */
	private static String[] parseParserFunctionArgumentArray(String parserFunctionArguments) {
		if (StringUtils.isBlank(parserFunctionArguments)) {
			return new String[0];
		}
		List<String> parserFunctionArgumentList = JFlexParserUtil.tokenizeParamString(parserFunctionArguments);
		String[] parserFunctionArgumentArray = new String[parserFunctionArgumentList.size()];
		// trim results and store in array
		int i = 0;
		for (String argument : parserFunctionArgumentList) {
			parserFunctionArgumentArray[i++] = argument.trim();
		}
		return parserFunctionArgumentArray;
	}
}
