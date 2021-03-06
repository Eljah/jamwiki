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
package org.jamwiki.search;

import java.io.File;
import java.io.StringReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLEncoder;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.FSDirectory;
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.utils.WikiLogger;
import org.jamwiki.model.Topic;
import org.jamwiki.model.VirtualWiki;
import org.jamwiki.parser.ParserDocument;
import org.jamwiki.utils.Utilities;
import org.springframework.util.StringUtils;

/**
 * An implementation of {@link org.jamwiki.search.SearchEngine} that uses
 * <a href="http://lucene.apache.org/java/">Lucene</a> to perform searches of
 * Wiki content.
 */
public class LuceneSearchEngine implements SearchEngine {

	/** Where to log to */
	private static final WikiLogger logger = WikiLogger.getLogger(LuceneSearchEngine.class.getName());
	/** Directory for search index files */
	private static final String SEARCH_DIR = "search";
	/** Id stored with documents to indicate the searchable topic name */
	private static final String ITYPE_TOPIC = "topic";
	/** Id stored with documents to indicate the searchable content. */
	private static final String ITYPE_CONTENT = "content";
	/** Id stored with documents to indicate the raw Wiki markup */
	private static final String ITYPE_CONTENT_PLAIN = "content_plain";
	/** Id stored with documents to indicate the topic name. */
	private static final String ITYPE_TOPIC_PLAIN = "topic_plain";
	/** Id stored with the document to indicate the search names of topics linked from the page.  */
	private static final String ITYPE_TOPIC_LINK = "topic_link";

	/**
	 * Add a topic to the search index.
	 *
	 * @param topic The Topic object that is to be added to the index.
	 * @param links A collection containing the topic names for all topics that link
	 *  to the current topic.
	 */
	// FIXME - why is synchronization needed?
	public synchronized void addToIndex(Topic topic, Collection links) {
		String virtualWiki = topic.getVirtualWiki();
		String topicName = topic.getName();
		IndexWriter writer = null;
		try {
			FSDirectory directory = FSDirectory.getDirectory(getSearchIndexPath(virtualWiki));
			try {
				writer = new IndexWriter(directory, new StandardAnalyzer(), false);
				KeywordAnalyzer keywordAnalyzer = new KeywordAnalyzer();
				writer.optimize();
				Document standardDocument = createStandardDocument(topic);
				if (standardDocument != null) {
					writer.addDocument(standardDocument);
				}
				Document keywordDocument = createKeywordDocument(topic, links);
				if (keywordDocument != null) {
					writer.addDocument(keywordDocument, keywordAnalyzer);
				}
			} finally {
				try {
					if (writer != null) {
						writer.optimize();
					}
				} catch (Exception e) {}
				try {
					if (writer != null) {
						writer.close();
					}
				} catch (Exception e) {}
			}
			directory.close();
		} catch (Exception e) {
			logger.severe("Exception while adding topic " + topicName, e);
		}
	}

	/**
	 * Create a basic Lucene document to add to the index that does treats
	 * the topic content as a single keyword and does not tokenize it.
	 */
	private Document createKeywordDocument(Topic topic, Collection links) throws Exception {
		String topicContent = topic.getTopicContent();
		if (topicContent == null) {
			topicContent = "";
		}
		Document doc = new Document();
		// store topic name for later retrieval
		doc.add(new Field(ITYPE_TOPIC_PLAIN, topic.getName(), Field.Store.YES, Field.Index.UN_TOKENIZED));
		if (links == null) {
			links = new Vector();
		}
		// index topic links for search purposes
		for (Iterator iter = links.iterator(); iter.hasNext();) {
			String linkTopic = (String)iter.next();
			doc.add(new Field(ITYPE_TOPIC_LINK, linkTopic, Field.Store.NO, Field.Index.UN_TOKENIZED));
		}
		return doc;
	}

	/**
	 * Create a basic Lucene document to add to the index.  This document
	 * is suitable to be parsed with the StandardAnalyzer.
	 */
	private Document createStandardDocument(Topic topic) throws Exception {
		String topicContent = topic.getTopicContent();
		if (topicContent == null) {
			topicContent = "";
		}
		Document doc = new Document();
		// store topic name and content for later retrieval
		doc.add(new Field(ITYPE_TOPIC_PLAIN, topic.getName(), Field.Store.YES, Field.Index.UN_TOKENIZED));
		doc.add(new Field(ITYPE_CONTENT_PLAIN, topicContent, Field.Store.YES, Field.Index.NO));
		// index topic name and content for search purposes
		doc.add(new Field(ITYPE_TOPIC, new StringReader(topic.getName())));
		doc.add(new Field(ITYPE_CONTENT, new StringReader(topicContent)));
		return doc;
	}

	/**
	 * Remove a topic from the search index.
	 *
	 * @param topic The topic object that is to be removed from the index.
	 */
	// FIXME - why is synchronization needed?
	public synchronized void deleteFromIndex(Topic topic) {
		String virtualWiki = topic.getVirtualWiki();
		String topicName = topic.getName();
		IndexReader reader = null;
		try {
			FSDirectory directory = FSDirectory.getDirectory(getSearchIndexPath(virtualWiki));
			// delete the current document
			try {
				reader = IndexReader.open(directory);
				reader.deleteDocuments(new Term(ITYPE_TOPIC_PLAIN, topicName));
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (Exception e) {}
				}
			}
			directory.close();
		} catch (Exception e) {
			logger.severe("Exception while adding topic " + topicName, e);
		}
	}

	/**
	 * Find all documents that link to a specified topic.
	 *
	 * @param virtualWiki The virtual wiki for the topic.
	 * @param topicName The name of the topic.
	 * @return A collection of SearchResultEntry objects for all documents that
	 *  link to the topic.
	 */
	public Collection findLinkedTo(String virtualWiki, String topicName) {
		Collection results = new Vector();
		IndexSearcher searcher = null;
		try {
			PhraseQuery query = new PhraseQuery();
			Term term = new Term(ITYPE_TOPIC_LINK, topicName);
			query.add(term);
			searcher = new IndexSearcher(FSDirectory.getDirectory(getSearchIndexPath(virtualWiki)));
			// actually perform the search
			Hits hits = searcher.search(query);
			for (int i = 0; i < hits.length(); i++) {
				SearchResultEntry result = new SearchResultEntry();
				result.setRanking(hits.score(i));
				result.setTopic(hits.doc(i).get(ITYPE_TOPIC_PLAIN));
				results.add(result);
			}
		} catch (Exception e) {
			logger.severe("Exception while searching for " + topicName, e);
		} finally {
			if (searcher != null) {
				try {
					searcher.close();
				} catch (Exception e) {}
			}
		}
		return results;
	}

	/**
	 * Find all documents that contain a specific search term, ordered by relevance.
	 * This method supports all Lucene search query syntax.
	 *
	 * @param virtualWiki The virtual wiki for the topic.
	 * @param text The search term being searched for.
	 * @return A collection of SearchResultEntry objects for all documents that
	 *  contain the search term.
	 */
	public Collection findResults(String virtualWiki, String text) {
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Collection results = new Vector();
		logger.fine("search text: " + text);
		IndexSearcher searcher = null;
		try {
			BooleanQuery query = new BooleanQuery();
			QueryParser qp;
			qp = new QueryParser(ITYPE_TOPIC, analyzer);
			query.add(qp.parse(text), Occur.SHOULD);
			qp = new QueryParser(ITYPE_CONTENT, analyzer);
			query.add(qp.parse(text), Occur.SHOULD);
			searcher = new IndexSearcher(FSDirectory.getDirectory(getSearchIndexPath(virtualWiki)));
			// rewrite the query to expand it - required for wildcards to work with highlighter
			Query rewrittenQuery = searcher.rewrite(query);
			// actually perform the search
			Hits hits = searcher.search(rewrittenQuery);
			Highlighter highlighter = new Highlighter(new SimpleHTMLFormatter("<span class=\"highlight\">", "</span>"), new SimpleHTMLEncoder(), new QueryScorer(rewrittenQuery));
			for (int i = 0; i < hits.length(); i++) {
				String summary = retrieveResultSummary(hits.doc(i), highlighter, analyzer);
				SearchResultEntry result = new SearchResultEntry();
				result.setRanking(hits.score(i));
				result.setTopic(hits.doc(i).get(ITYPE_TOPIC_PLAIN));
				result.setSummary(summary);
				results.add(result);
			}
		} catch (Exception e) {
			logger.severe("Exception while searching for " + text, e);
		} finally {
			if (searcher != null) {
				try {
					searcher.close();
				} catch (Exception e) {}
			}
		}
		return results;
	}

	/**
	 * Get the path, which holds all index files
	 */
	private String getSearchIndexPath(String virtualWiki) {
		File parent = new File(Environment.getValue(Environment.PROP_BASE_FILE_DIR), SEARCH_DIR);
		try {
			if (System.getProperty("org.apache.lucene.lockdir") == null) {
				// set the Lucene lock directory.  this defaults to java.io.tmpdir,
				// which may not be writable on some systems.
				System.setProperty("org.apache.lucene.lockdir", parent.getPath());
			}
		} catch (Exception e) {
			// probably a security exception
			logger.warning("Unable to specify Lucene lock directory, default will be used: " + e.getMessage());
		}
		File child = new File(parent.getPath(), "index" + virtualWiki + File.separator);
		if (!child.exists()) {
			child.mkdirs();
			IndexWriter writer = null;
			try {
				// create the search instance
				FSDirectory directory = FSDirectory.getDirectory(getSearchIndexPath(virtualWiki));
				StandardAnalyzer analyzer = new StandardAnalyzer();
				writer = new IndexWriter(directory, analyzer, true);
				directory.close();
			} catch (Exception e) {
				logger.severe("Unable to create search instance " + child.getPath(), e);
			} finally {
				try {
					if (writer != null) {
						writer.close();
					}
				} catch (Exception e) {
					logger.severe("Exception during close", e);
				}
			}
		}
		return child.getPath();
	}

	/**
	 * Refresh the current search index by re-visiting all topic pages.
	 *
	 * @throws Exception Thrown if any error occurs while re-indexing the Wiki.
	 */
	// FIXME - why is synchronization needed?
	public synchronized void refreshIndex() throws Exception {
		Collection allWikis = WikiBase.getDataHandler().getVirtualWikiList(null);
		Topic topic;
		for (Iterator iterator = allWikis.iterator(); iterator.hasNext();) {
			long start = System.currentTimeMillis();
			int count = 0;
			VirtualWiki virtualWiki = (VirtualWiki)iterator.next();
			FSDirectory directory = FSDirectory.getDirectory(this.getSearchIndexPath(virtualWiki.getName()));
			StandardAnalyzer analyzer = new StandardAnalyzer();
			KeywordAnalyzer keywordAnalyzer = new KeywordAnalyzer();
			IndexWriter writer = null;
			try {
				writer = new IndexWriter(directory, analyzer, true);
				Collection topicNames = WikiBase.getDataHandler().getAllTopicNames(virtualWiki.getName());
				for (Iterator iter = topicNames.iterator(); iter.hasNext();) {
					String topicName = (String)iter.next();
					topic = WikiBase.getDataHandler().lookupTopic(virtualWiki.getName(), topicName, false, null);
					Document standardDocument = createStandardDocument(topic);
					if (standardDocument != null) {
						writer.addDocument(standardDocument);
					}
					// FIXME - parsing all documents will be intolerably slow with even a
					// moderately large Wiki
					ParserDocument parserDocument = Utilities.parserDocument(topic.getTopicContent(), virtualWiki.getName(), topicName);
					Document keywordDocument = createKeywordDocument(topic, parserDocument.getLinks());
					if (keywordDocument != null) {
						writer.addDocument(keywordDocument, keywordAnalyzer);
					}
					count++;
				}
			} catch (Exception ex) {
				logger.severe("Failure while refreshing search index", ex);
			} finally {
				try {
					if (writer != null) {
						writer.optimize();
					}
				} catch (Exception e) {
					logger.severe("Exception during optimize", e);
				}
				try {
					if (writer != null) {
						writer.close();
					}
				} catch (Exception e) {
					logger.severe("Exception during close", e);
				}
			}
			directory.close();
			logger.info("Rebuilt search index for " + virtualWiki.getName() + " (" + count + " documents) in " + ((System.currentTimeMillis() - start) / 1000.000) + " seconds");
		}
	}

	/**
	 *
	 */
	private String retrieveResultSummary(Document document, Highlighter highlighter, StandardAnalyzer analyzer) throws Exception {
		String content = document.get(ITYPE_CONTENT_PLAIN);
		TokenStream tokenStream = analyzer.tokenStream(ITYPE_CONTENT_PLAIN, new StringReader(content));
		String summary = highlighter.getBestFragments(tokenStream, content, 3, "...");
		if (!StringUtils.hasText(summary) && StringUtils.hasText(content)) {
			summary = Utilities.escapeHTML(content.substring(0, Math.min(200, content.length())));
			if (Math.min(200, content.length()) == 200) {
				summary += "...";
			}
		}
		return summary;
	}
}
