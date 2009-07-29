/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jamwiki.utils;

import java.util.Locale;
import java.util.regex.Pattern;
import org.jamwiki.parser.ParserException;
import org.jamwiki.parser.ParserInput;
import org.jamwiki.parser.ParserOutput;
import org.jamwiki.parser.ParserUtil;

/**
 *
 * @author Daniel.Fisla
 */
public class HtmlUtil {

    public static String removeTemplateTags(String text) {
        String regex = "\\<a.*?>\\s*Template\\s*:.*\\</\\s*a\\s*>";

        Pattern p = Pattern.compile(regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        return p.matcher(text).replaceAll("");
    }

    public static String removeHtmlTags(String text) {
        String regex = "\\<.*?>";

        Pattern p = Pattern.compile(regex, Pattern.DOTALL);

        return p.matcher(text).replaceAll("");
    }

    public static String evalRegEx(String text, String regex) {
        
        Pattern p = Pattern.compile(regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE );

        return p.matcher(text).replaceAll("");
    }

    public static String parseToHtml(String topicContent, String topicName, String context, Locale locale, String virtualWiki) throws ParserException {

        //WikiUserDetails userDetails = ServletUtil.currentUserDetails();
        String ret = "";

        ParserInput parserInput = new ParserInput();

        parserInput.setContext(context);
        parserInput.setLocale(locale);
        //parserInput.setWikiUser(user);
        parserInput.setTopicName(topicName);
        //parserInput.setUserIpAddress(ServletUtil.getIpAddress(request));
        parserInput.setVirtualWiki(virtualWiki);
        parserInput.setAllowSectionEdit(false);

        ParserOutput parserOutput = new ParserOutput();

        try {
            ret = ParserUtil.parse(parserInput, parserOutput, topicContent);
        } catch (ParserException e) {
            throw new ParserException(e);
        }

        return ret;
    }
}
