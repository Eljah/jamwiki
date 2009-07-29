/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jamwiki.search;

import org.jamwiki.model.WikiUser;
import org.jamwiki.servlets.XMLTopicFactory;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Daniel.Fisla
 */
public class StripHtmlTagsTest {

    @Test
	public void testStrimhtmltags() throws Throwable {
		XMLTopicFactory xmlTopicFactory = new XMLTopicFactory("testXMLTopicFactoryVirtualWiki", new WikiUser(""), "testXMLTopicFactoryAuthorIpAddress");

        assertTrue(xmlTopicFactory.removeHtmlTags("<em>").length() == 0);
        assertTrue(xmlTopicFactory.removeHtmlTags("<b>bold</b>").length() == 4);
        assertEquals(xmlTopicFactory.removeHtmlTags("This is a link <a href=\"yahoo.com/foo/bar\">to Yahoo</a> portal."), "This is a link to Yahoo portal.");
	}
}
