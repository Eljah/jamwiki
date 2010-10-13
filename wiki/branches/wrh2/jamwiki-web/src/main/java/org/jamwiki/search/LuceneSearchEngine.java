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
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLEncoder;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.jamwiki.Environment;
import org.jamwiki.SearchEngine;
import org.jamwiki.WikiBase;
import org.jamwiki.model.SearchResultEntry;
import org.jamwiki.model.Topic;
import org.jamwiki.model.VirtualWiki;
import org.jamwiki.utils.WikiLogger;

/**
 * An implementation of {@link org.jamwiki.SearchEngine} that uses
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
	/** Lucene compatibility version. */
	private static final Version USE_LUCENE_VERSION = Version.LUCENE_30;
	/** Maximum number of results to return per search. */
	// FIXME - make this configurable
	private static final int MAXIMUM_RESULTS_PER_SEARCH = 200;
	/** Store Searchers (once opened) for re-use for performance reasons. */
	private Map<String, Searcher> searchers = new HashMap<String, Searcher>();
	/** Store Writers (once opened) for re-use for performance reasons. */
	private Map<String, IndexWriter> indexWriters = new HashMap<String, IndexWriter>();

	/**
	 * Add a topic to the search index.
	 *
	 * @param topic The Topic object that is to be added to the index.
	 */
	public void addToIndex(Topic topic) {
		try {
			long start = System.currentTimeMillis();
			IndexWriter writer = this.retrieveIndexWriter(topic.getVirtualWiki(), false);
			this.addToIndex(writer, topic);
			writer.commit();
			if (logger.isDebugEnabled()) {
				logger.debug("Add to search index for topic " + topic.getName() + " in " + ((System.currentTimeMillis() - start) / 1000.000) + " s.");
			}
		} catch (Exception e) {
			logger.error("Exception while adding topic " + topic.getName(), e);
		}
	}

	/**
	 * Add a topic to the search index.
	 *
	 * @param topic The Topic object that is to be added to the index.
	 */
	private void addToIndex(IndexWriter writer, Topic topic) throws IOException {
		Document standardDocument = createStandardDocument(topic);
		writer.addDocument(standardDocument);
		this.resetIndexSearcher(topic.getVirtualWiki());
	}

	/**
	 * Create a basic Lucene document to add to the index.  This document
	 * is suitable to be parsed with the StandardAnalyzer.
	 */
	private Document createStandardDocument(Topic topic) {
		String topicContent = topic.getTopicContent();
		if (topicContent == null) {
			topicContent = "";
		}
		Document doc = new Document();
		// store topic name and content for later retrieval
		doc.add(new Field(ITYPE_TOPIC_PLAIN, topic.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
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
	public void deleteFromIndex(Topic topic) {
		try {
			long start = System.currentTimeMillis();
			// delete the current document
			IndexWriter writer = this.retrieveIndexWriter(topic.getVirtualWiki(), false);
			this.deleteFromIndex(writer, topic);
			writer.commit();
			if (logger.isDebugEnabled()) {
				logger.debug("Delete from search index for topic " + topic.getName() + " in " + ((System.currentTimeMillis() - start) / 1000.000) + " s.");
			}
		} catch (Exception e) {
			logger.error("Exception while adding topic " + topic.getName(), e);
		}
	}

	/**
	 * Remove a topic from the search index.
	 *
	 * @param topic The topic object that is to be removed from the index.
	 */
	private void deleteFromIndex(IndexWriter writer, Topic topic) throws IOException {
		writer.deleteDocuments(new Term(ITYPE_TOPIC_PLAIN, topic.getName()));
		this.resetIndexSearcher(topic.getVirtualWiki());
	}

	/**
	 * Find all documents that contain a specific search term, ordered by relevance.
	 * This method supports all Lucene search query syntax.
	 *
	 * @param virtualWiki The virtual wiki for the topic.
	 * @param text The search term being searched for.
	 * @return A list of SearchResultEntry objects for all documents that
	 *  contain the search term.
	 */
	public List<SearchResultEntry> findResults(String virtualWiki, String text) {
		StandardAnalyzer analyzer = new StandardAnalyzer(USE_LUCENE_VERSION);
		List<SearchResultEntry> results = new ArrayList<SearchResultEntry>();
		logger.trace("search text: " + text);
		try {
			BooleanQuery query = new BooleanQuery();
			QueryParser qp;
			qp = new QueryParser(USE_LUCENE_VERSION, ITYPE_TOPIC, analyzer);
			query.add(qp.parse(text), Occur.SHOULD);
			qp = new QueryParser(USE_LUCENE_VERSION, ITYPE_CONTENT, analyzer);
			query.add(qp.parse(text), Occur.SHOULD);
			Searcher searcher = this.retrieveIndexSearcher(virtualWiki);
			// rewrite the query to expand it - required for wildcards to work with highlighter
			Query rewrittenQuery = searcher.rewrite(query);
			// actually perform the search
			TopScoreDocCollector collector = TopScoreDocCollector.create(MAXIMUM_RESULTS_PER_SEARCH, true);
			searcher.search(rewrittenQuery, collector);
			Highlighter highlighter = new Highlighter(new SimpleHTMLFormatter("<span class=\"highlight\">", "</span>"), new SimpleHTMLEncoder(), new QueryScorer(rewrittenQuery));
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			for (int i = 0; i < hits.length; i++) {
				int docId = hits[i].doc;
				Document doc = searcher.doc(docId);
				String summary = retrieveResultSummary(doc, highlighter, analyzer);
				SearchResultEntry result = new SearchResultEntry();
				result.setRanking(hits[i].score);
				result.setTopic(doc.get(ITYPE_TOPIC_PLAIN));
				result.setSummary(summary);
				results.add(result);
			}
		} catch (Exception e) {
			logger.error("Exception while searching for " + text, e);
		}
		return results;
	}

	/**
	 * Get the path, which holds all index files
	 */
	private File getSearchIndexPath(String virtualWiki) throws IOException {
		File parent = new File(Environment.getValue(Environment.PROP_BASE_FILE_DIR), SEARCH_DIR);
		try {
			if (System.getProperty("org.apache.lucene.lockdir") == null) {
				// set the Lucene lock directory.  this defaults to java.io.tmpdir,
				// which may not be writable on some systems.
				System.setProperty("org.apache.lucene.lockdir", parent.getPath());
			}
		} catch (Exception e) {
			// probably a security exception
			logger.warn("Unable to specify Lucene lock directory, default will be used: " + e.getMessage());
		}
		File child = new File(parent.getPath(), "index" + virtualWiki + File.separator);
		if (!child.exists()) {
			// create the search instance
			child.mkdirs();
			IndexWriter writer = new IndexWriter(FSDirectory.open(child), new StandardAnalyzer(USE_LUCENE_VERSION), true, IndexWriter.MaxFieldLength.LIMITED);
			writer.close();
		}
		return child;
	}

	/**
	 * Refresh the current search index by re-visiting all topic pages.
	 *
	 * @throws Exception Thrown if any error occurs while re-indexing the Wiki.
	 */
	public void refreshIndex() throws Exception {
		List<VirtualWiki> allWikis = WikiBase.getDataHandler().getVirtualWikiList();
		Topic topic;
		for (VirtualWiki virtualWiki : allWikis) {
			long start = System.currentTimeMillis();
			int count = 0;
			IndexWriter writer = null;
			try {
				writer = this.retrieveIndexWriter(virtualWiki.getName(), true);
				List<String> topicNames = WikiBase.getDataHandler().getAllTopicNames(virtualWiki.getName(), false);
				// FIXME - parsing all documents will be intolerably slow with even a
				// moderately large Wiki
				for (String topicName : topicNames) {
					topic = WikiBase.getDataHandler().lookupTopic(virtualWiki.getName(), topicName, false, null);
					if (topic == null) {
						logger.info("Unable to rebuild search index for topic: " + topicName);
						continue;
					}
					// note: no delete is necessary since a new index is being created
					this.addToIndex(writer, topic);
					count++;
				}
			} catch (Exception ex) {
				logger.error("Failure while refreshing search index", ex);
			} finally {
				try {
					if (writer != null) {
						writer.optimize();
					}
				} catch (Exception e) {
					logger.error("Exception during optimize", e);
				}
				try {
					if (writer != null) {
						writer.close();
					}
				} catch (Exception e) {
					logger.error("Exception during close", e);
				}
			}
			if (logger.isInfoEnabled()) {
				logger.info("Rebuilt search index for " + virtualWiki.getName() + " (" + count + " documents) in " + ((System.currentTimeMillis() - start) / 1000.000) + " seconds");
			}
		}
	}

	/**
	 * Call this method after a search index is updated to reset the searcher.
	 */
	private void resetIndexSearcher(String virtualWiki) throws IOException {
		Searcher searcher = searchers.get(virtualWiki);
		if (searcher != null) {
			searchers.remove(virtualWiki);
			searcher.close();
		}
	}

	/**
	 * For performance reasons cache the IndexSearcher for re-use.
	 */
	private Searcher retrieveIndexSearcher(String virtualWiki) throws IOException {
		Searcher searcher = searchers.get(virtualWiki);
		if (searcher == null) {
			searcher = new IndexSearcher(this.retrieveIndexWriter(virtualWiki, false).getReader());
			searchers.put(virtualWiki, searcher);
		}
		return searcher;
	}

	/**
	 * For performance reasons create a cache of writers.  Since writers are not being
	 * re-initialized then commit() must be called to explicitly flush data to the index,
	 * otherwise it will be flushed on a programmatic basis by Lucene.
	 */
	private IndexWriter retrieveIndexWriter(String virtualWiki, boolean create) throws IOException {
		IndexWriter indexWriter = indexWriters.get(virtualWiki);
		if (create) {
			// if the writer is going to blow away the existing index and create a new one then it
			// should not be cached.  instead, close any open writer, create a new one, and return.
			if (indexWriter != null) {
				indexWriter.close();
				indexWriters.remove(virtualWiki);
			}
			indexWriter = new IndexWriter(FSDirectory.open(getSearchIndexPath(virtualWiki)), new StandardAnalyzer(USE_LUCENE_VERSION), create, IndexWriter.MaxFieldLength.LIMITED);
		} else if (indexWriter == null) {
			indexWriter = new IndexWriter(FSDirectory.open(getSearchIndexPath(virtualWiki)), new StandardAnalyzer(USE_LUCENE_VERSION), create, IndexWriter.MaxFieldLength.LIMITED);
			indexWriters.put(virtualWiki, indexWriter);
		}
		return indexWriter;
	}

	/**
	 *
	 */
	private String retrieveResultSummary(Document document, Highlighter highlighter, StandardAnalyzer analyzer) throws InvalidTokenOffsetsException, IOException {
		String content = document.get(ITYPE_CONTENT_PLAIN);
		TokenStream tokenStream = analyzer.tokenStream(ITYPE_CONTENT_PLAIN, new StringReader(content));
		String summary = highlighter.getBestFragments(tokenStream, content, 3, "...");
		if (StringUtils.isBlank(summary) && !StringUtils.isBlank(content)) {
			summary = StringEscapeUtils.escapeHtml(content.substring(0, Math.min(200, content.length())));
			if (Math.min(200, content.length()) == 200) {
				summary += "...";
			}
		}
		return summary;
	}

	/**
	 *
	 */
	public void updateInIndex(Topic topic) {
		try {
			long start = System.currentTimeMillis();
			IndexWriter writer = this.retrieveIndexWriter(topic.getVirtualWiki(), false);
			this.deleteFromIndex(writer, topic);
			this.addToIndex(writer, topic);
			writer.commit();
			if (logger.isDebugEnabled()) {
				logger.debug("Update search index for topic " + topic.getName() + " in " + ((System.currentTimeMillis() - start) / 1000.000) + " s.");
			}
		} catch (Exception e) {
			logger.error("Exception while updating topic " + topic.getName(), e);
		}
	}
}
