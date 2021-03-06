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
 *
 * Based on code generated by Agitar build: Agitator Version 1.0.2.000071 (Build date: Jan 12, 2007) [1.0.2.000071]
 */
package org.jamwiki.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Vector;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import org.jamwiki.model.Topic;
import org.jamwiki.model.TopicVersion;

public class TiddlyWikiParserTest extends TestCase {

	private static final String NAME = "MyTopic";
	private static final String DATUM  = "200701020304";
	private static final String CONTENT  = "Content"; 

	public void testParse() throws Exception {
		Handler[] h = Logger.getLogger("").getHandlers();
		for (int i = 0; i < h.length; i++) {
			h[i].setLevel(Level.ALL);
		}
		Logger.getLogger("").setLevel(Level.ALL);
		WikiBaseMock mock = new WikiBaseMock();
		TiddlyWikiParser parser = new TiddlyWikiParser("myvirtual", null, "", mock);
		String testLine = "<div tiddler=\""+ NAME + "\" modified=\"" + DATUM + "\">" + CONTENT+ "</div>";
		parser.parse(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(testLine.getBytes()))));
		assertEquals(1, mock.topics.size());
		Topic result =  (Topic) mock.topics.get(0);
		assertEquals(NAME, result.getName());
		assertEquals(CONTENT, result.getTopicContent());
		assertEquals(1, mock.versions.size());
		TopicVersion version = (TopicVersion) mock.versions.get(0);
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmm");
		Timestamp t = new Timestamp(fmt.parse(DATUM).getTime());
		assertEquals(t, version.getEditDate());
	}

	private class WikiBaseMock implements TiddlyWikiParser.WikiBaseFascade {
		public ArrayList topics = new ArrayList();
		public ArrayList versions = new ArrayList();

		public void writeTopic(Topic topic, TopicVersion topicVersion, LinkedHashMap categories, Vector links, boolean userVisible, Object transactionObject) throws Exception {
			topics.add(topic);
			versions.add(topicVersion);
		}
	}
}
