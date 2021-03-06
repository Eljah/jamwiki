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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import org.apache.commons.lang.ClassUtils;
import org.jamwiki.Environment;
import org.jamwiki.DataAccessException;
import org.jamwiki.WikiBase;
import org.jamwiki.model.Topic;
import org.jamwiki.utils.WikiLogger;

/**
 * This class provides utility methods for use with the parser functions.
 */
public class ParserUtil {

	private static final WikiLogger logger = WikiLogger.getLogger(ParserUtil.class.getName());

	/**
	 * Using the system parser, parse system content.
	 *
	 * @param parserInput A ParserInput object that contains parser
	 *  configuration information.
	 * @param parserOutput A ParserOutput object that will hold metadata
	 *  output.  If this parameter is <code>null</code> then metadata generated
	 *  during parsing will not be available to the calling method.
	 * @param content The raw topic content that is to be parsed.
	 * @return The parsed content.
	 * @throws ParserException Thrown if there are any parsing errors.
	 */
	public static String parse(ParserInput parserInput, ParserOutput parserOutput, String content) throws ParserException {
		if (content == null) {
			return null;
		}
		if (parserOutput == null) {
			parserOutput = new ParserOutput();
		}
		AbstractParser parser = parserInstance(parserInput);
		return parser.parseHTML(parserOutput, content);
	}

	/**
	 * Retrieve a default ParserOutput object for a given topic name.  Note that
	 * the content has almost no parsing performed on it other than to generate
	 * parser output metadata.
	 *
	 * @param content The raw topic content.
	 * @return Returns a minimal ParserOutput object initialized primarily with
	 *  parser metadata such as links.
	 * @throws ParserException Thrown if a parser error occurs.
	 */
	public static ParserOutput parserOutput(String content, String virtualWiki, String topicName) throws ParserException {
		ParserInput parserInput = new ParserInput();
		parserInput.setVirtualWiki(virtualWiki);
		parserInput.setTopicName(topicName);
		parserInput.setAllowSectionEdit(false);
		return ParserUtil.parseMetadata(parserInput, content);
	}

	/**
	 * This method provides a way to parse content and set all output metadata,
	 * such as link values used by the search engine.
	 *
	 * @param parserInput A ParserInput object that contains parser configuration
	 *  information.
	 * @param content The raw topic content that is to be parsed.
	 * @return Returns a ParserOutput object with minimally parsed topic content
	 *  and other parser output fields set.
	 * @throws ParserException Thrown if there are any parsing errors.
	 */
	public static ParserOutput parseMetadata(ParserInput parserInput, String content) throws ParserException {
		AbstractParser parser = parserInstance(parserInput);
		ParserOutput parserOutput = new ParserOutput();
		parser.parseMetadata(parserOutput, content);
		return parserOutput;
	}

	/**
	 * Perform a bare minimum of parsing as required prior to saving a topic
	 * to the database.  In general this method will simply parse signature
	 * tags are return.
	 *
	 * @param parserInput A ParserInput object that contains parser configuration
	 *  information.
	 * @param raw The raw Wiki syntax to be converted into HTML.
	 * @return The parsed content.
	 * @throws ParserException Thrown if any error occurs during parsing.
	 */
	public static String parseMinimal(ParserInput parserInput, String raw) throws ParserException {
		AbstractParser parser = parserInstance(parserInput);
		return parser.parseMinimal(raw);
	}

	/**
	 * Utility method to retrieve an instance of the current system parser.
	 *
	 * @param parserInput A ParserInput object that contains parser configuration
	 *  information.
	 * @return An instance of the system parser.
	 * @throws ParserException Thrown if a parser instance can not be instantiated.
	 */
	private static AbstractParser parserInstance(ParserInput parserInput) throws ParserException {
		String parserClass = Environment.getValue(Environment.PROP_PARSER_CLASS);
		try {
			Class clazz = ClassUtils.getClass(parserClass);
			Class[] parameterTypes = new Class[1];
			parameterTypes[0] = ClassUtils.getClass("org.jamwiki.parser.ParserInput");
			Constructor constructor = clazz.getConstructor(parameterTypes);
			Object[] initArgs = new Object[1];
			initArgs[0] = parserInput;
			return (AbstractParser)constructor.newInstance(initArgs);
		} catch (ClassNotFoundException e) {
			throw new ParserException(e);
		} catch (NoSuchMethodException e) {
			throw new ParserException(e);
		} catch (IllegalAccessException e) {
			throw new ParserException(e);
		} catch (InstantiationException e) {
			throw new ParserException(e);
		} catch (InvocationTargetException e) {
			throw new ParserException(e);
		}
	}

	/**
	 * Given a topic name, return the parser-specific syntax to indicate a page
	 * redirect.
	 *
	 * @param topicName The name of the topic that is being redirected to.
	 * @return A string containing the syntax indicating a redirect.
	 * @throws ParserException Thrown if a parser instance cannot be instantiated or
	 *  if any other parser error occurs.
	 */
	public static String parserRedirectContent(String topicName) throws ParserException {
		AbstractParser parser = parserInstance(null);
		return parser.buildRedirectContent(topicName);
	}

	/**
	 * When editing a section of a topic, this method provides a way of slicing
	 * out a given section of the raw topic content.
	 *
	 * @param context The servlet context.
	 * @param locale The locale for which the content is being parsed.
	 * @param virtualWiki The virtual wiki for the topic being parsed.
	 * @param topicName The name of the topic being parsed.
	 * @param section The section to be sliced and returned.
	 * @return Returns A string array consisting of the section name and the raw topic
	 *  content for the target section.
	 * @throws ParserException Thrown if a parser error occurs.
	 */
	public static String[] parseSlice(String context, Locale locale, String virtualWiki, String topicName, int section) throws ParserException {
		ParserOutput parserOutput = new ParserOutput();
		return ParserUtil.executeSliceOrSplice(parserOutput, context, locale, virtualWiki, topicName, section, null, true);
	}

	/**
	 * When editing a section of a topic, this method provides a way of splicing
	 * an edited section back into the raw topic content.
	 *
	 * @param parserOutput A ParserOutput object containing parser
	 *  metadata output.
	 * @param context The servlet context.
	 * @param locale The locale for which the content is being parsed.
	 * @param virtualWiki The virtual wiki for the topic being parsed.
	 * @param topicName The name of the topic being parsed.
	 * @param targetSection The section to be sliced and returned.
	 * @param replacementText The edited content that is to be spliced back into
	 *  the raw topic.
	 * @return Returns A string array consisting of the section name and the raw topic
	 *  content including the new replacement text.
	 * @throws ParserException Thrown if a parser error occurs.
	 */
	public static String[] parseSplice(ParserOutput parserOutput, String context, Locale locale, String virtualWiki, String topicName, int targetSection, String replacementText) throws ParserException {
		return ParserUtil.executeSliceOrSplice(parserOutput, context, locale, virtualWiki, topicName, targetSection, replacementText, false);
	}

	/**
	 * The slice and splice parser code is very similar, so this method simply consolidates
	 * that code to avoid duplication.
	 */
	private static String[] executeSliceOrSplice(ParserOutput parserOutput, String context, Locale locale, String virtualWiki, String topicName, int targetSection, String replacementText, boolean isSlice) throws ParserException {
		Topic topic = null;
		try {
			topic = WikiBase.getDataHandler().lookupTopic(virtualWiki, topicName, false, null);
		} catch (DataAccessException e) {
			throw new ParserException(e);
		}
		if (topic == null || topic.getTopicContent() == null) {
			return null;
		}
		ParserInput parserInput = new ParserInput();
		parserInput.setContext(context);
		parserInput.setLocale(locale);
		parserInput.setTopicName(topicName);
		parserInput.setVirtualWiki(virtualWiki);
		AbstractParser parser = ParserUtil.parserInstance(parserInput);
		String content = null;
		if (isSlice) {
			content = parser.parseSlice(parserOutput, topic.getTopicContent(), targetSection);
		} else {
			content = parser.parseSplice(parserOutput, topic.getTopicContent(), targetSection, replacementText);
		}
		String sectionName = parserOutput.getSectionName();
		return new String[]{sectionName, content};
	}
}
