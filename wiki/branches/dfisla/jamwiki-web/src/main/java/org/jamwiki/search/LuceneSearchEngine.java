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
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocCollector;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLEncoder;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.FSDirectory;
import org.jamwiki.Environment;
import org.jamwiki.SearchEngine;
import org.jamwiki.WikiBase;
import org.jamwiki.model.SearchResultEntry;
import org.jamwiki.model.Topic;
import org.jamwiki.model.VirtualWiki;
import org.jamwiki.parser.ParserOutput;
import org.jamwiki.parser.ParserUtil;
import org.apache.log4j.Logger;
import org.apache.lucene.store.Directory;
import org.jamwiki.DataHandler;

/**
 * An implementation of {@link org.jamwiki.SearchEngine} that uses
 * <a href="http://lucene.apache.org/java/">Lucene</a> to perform searches of
 * Wiki content.
 */
public class LuceneSearchEngine implements SearchEngine {

    /** Where to log to */
    private static final Logger logger = Logger.getLogger(LuceneSearchEngine.class.getName());
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
    /** Maximum number of results to return per search. */
    // FIXME - make this configurable
    private static final int MAXIMUM_RESULTS_PER_SEARCH = 200;
    private static boolean SEARCH_DIR_OVERRIDE = false;
    private static String CUSTOM_DATA_PATH = null;
    private List<Integer> topicIds = null;

    /**
     * Default Constructor
     */
    public LuceneSearchEngine() {
    }

    /**
     * Constructor
     *
     * @param searchIndexPath
     */
    public LuceneSearchEngine(String searchIndexPath) {
        CUSTOM_DATA_PATH = searchIndexPath;
        SEARCH_DIR_OVERRIDE = true;
    }

    /**
     * Constructor
     *
     * @param searchIndexPath
     * @param
     */
    public LuceneSearchEngine(String searchIndexPath, List<Integer> ids) {
        CUSTOM_DATA_PATH = searchIndexPath;
        SEARCH_DIR_OVERRIDE = true;
        this.topicIds = ids;
    }

    /**
     * Add a topic to the search index.
     *
     * @param topic The Topic object that is to be added to the index.
     * @param links A list containing the topic names for all topics that link
     *  to the current topic.
     */
    public synchronized void addToIndex(Topic topic, List<String> links) {
        String virtualWiki = topic.getVirtualWiki();
        String topicName = topic.getName();
        String topicContent = topic.getTopicContent();

        IndexWriter writer = null;

        if ((topicContent != null) &&
                (topicContent.startsWith("#REDIRECT"))) {
            return;
        }
        try {
            FSDirectory directory = FSDirectory.getDirectory(getSearchIndexPath(virtualWiki));
            // FIXME - move synchronization to the writer instance for this directory
            try {
                writer = new IndexWriter(directory, new StandardAnalyzer(), false, IndexWriter.MaxFieldLength.LIMITED);
                KeywordAnalyzer keywordAnalyzer = new KeywordAnalyzer();
                writer.optimize();
                Document standardDocument = createStandardDocument(topic);
                writer.addDocument(standardDocument);
                Document keywordDocument = createKeywordDocument(topic, links);
                writer.addDocument(keywordDocument, keywordAnalyzer);
            } finally {
                try {
                    if (writer != null) {
                        writer.optimize();
                    }
                } catch (Exception e) {
                }
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (Exception e) {
                }
            }
            directory.close();
        } catch (Exception e) {
            logger.fatal("Exception while adding topic " + topicName, e);
        }
    }

    /**
     * Create a basic Lucene document to add to the index that does treats
     * the topic content as a single keyword and does not tokenize it.
     */
    private Document createKeywordDocument(Topic topic, List<String> links) throws Exception {
        String topicContent = topic.getTopicContent();
        if (topicContent == null) {
            topicContent = "";
        }
        Document doc = new Document();
        // store topic name for later retrieval
        doc.add(new Field(ITYPE_TOPIC_PLAIN, topic.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        if (links == null) {
            links = new ArrayList<String>();
        }
        // index topic links for search purposes
        for (String linkTopic : links) {
            doc.add(new Field(ITYPE_TOPIC_LINK, linkTopic, Field.Store.NO, Field.Index.NOT_ANALYZED));
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
    public synchronized void deleteFromIndex(Topic topic) {
        String virtualWiki = topic.getVirtualWiki();
        String topicName = topic.getName();
        IndexWriter writer = null;
        try {
            FSDirectory directory = FSDirectory.getDirectory(getSearchIndexPath(virtualWiki));
            // delete the current document
            // FIXME - move synchronization to the writer instance for this directory
            try {
                writer = new IndexWriter(directory, new StandardAnalyzer(), false, IndexWriter.MaxFieldLength.LIMITED);
                writer.deleteDocuments(new Term(ITYPE_TOPIC_PLAIN, topicName));
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (Exception e) {
                    }
                }
            }
            directory.close();
        } catch (Exception e) {
            logger.fatal("Exception while adding topic " + topicName, e);
        }
    }

    /**
     * Find all documents that link to a specified topic.
     *
     * @param virtualWiki The virtual wiki for the topic.
     * @param topicName The name of the topic.
     * @return A list of SearchResultEntry objects for all documents that
     *  link to the topic.
     */
    public List<SearchResultEntry> findLinkedTo(String virtualWiki, String topicName) {
        List<SearchResultEntry> results = new ArrayList<SearchResultEntry>();
        IndexSearcher searcher = null;
        try {
            PhraseQuery query = new PhraseQuery();
            Term term = new Term(ITYPE_TOPIC_LINK, topicName);
            query.add(term);
            searcher = new IndexSearcher(FSDirectory.getDirectory(getSearchIndexPath(virtualWiki)));
            // actually perform the search
            TopDocCollector collector = new TopDocCollector(MAXIMUM_RESULTS_PER_SEARCH);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            for (int i = 0; i < hits.length; i++) {
                int docId = hits[i].doc;
                Document doc = searcher.doc(docId);
                SearchResultEntry result = new SearchResultEntry();
                result.setRanking(hits[i].score);
                result.setTopic(doc.get(ITYPE_TOPIC_PLAIN));
                results.add(result);
            }
        } catch (Exception e) {
            logger.fatal("Exception while searching for " + topicName, e);
        } finally {
            if (searcher != null) {
                try {
                    searcher.close();
                } catch (Exception e) {
                }
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
     * @return A list of SearchResultEntry objects for all documents that
     *  contain the search term.
     */
    public List<SearchResultEntry> findResults(String virtualWiki, String text) {
        StandardAnalyzer analyzer = new StandardAnalyzer();
        List<SearchResultEntry> results = new ArrayList<SearchResultEntry>();
        logger.debug("search text: " + text);
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
            TopDocCollector collector = new TopDocCollector(MAXIMUM_RESULTS_PER_SEARCH);
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
            logger.fatal("Exception while searching for " + text, e);
        } finally {
            if (searcher != null) {
                try {
                    searcher.close();
                } catch (Exception e) {
                }
            }
        }
        return results;
    }

    /**
     * Get the path, which holds all index files
     */
    private String getSearchIndexPath(String virtualWiki) {

        File parent = null;

        if (SEARCH_DIR_OVERRIDE) {
            parent = new File(CUSTOM_DATA_PATH, SEARCH_DIR);
        } else {
            parent = new File(Environment.getValue(Environment.PROP_BASE_FILE_DIR), SEARCH_DIR);
        }

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

        long threadId = Thread.currentThread().getId();

        File child = null;
        if (this.topicIds == null) {
            child = new File(parent.getPath(), "index" + virtualWiki + File.separator);
        } else {
            child = new File(parent.getPath(), String.format("index-%s-%s", threadId, virtualWiki) + File.separator);
        }
        if (!child.exists()) {
            child.mkdirs();
            IndexWriter writer = null;
            try {
                // create the search instance
                FSDirectory directory = FSDirectory.getDirectory(getSearchIndexPath(virtualWiki));
                writer = new IndexWriter(directory, new StandardAnalyzer(), true, IndexWriter.MaxFieldLength.LIMITED);
                directory.close();
            } catch (Exception e) {
                logger.fatal("Unable to create search instance " + child.getPath(), e);
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (Exception e) {
                    logger.fatal("Exception during close", e);
                }
            }
        }
        return child.getPath();
    }

    /**
     *
     * @param sourceDir
     * @param mergeDir
     * @param mergeFactor
     * @param bufferSizeMB
     */
    public synchronized void merge(String sourceDir, String mergeDir, int mergeFactor, int bufferSizeMB) {

        File indexesDirectory = new File(sourceDir);
        File mergeDirectory = new File(mergeDir);

        if (!mergeDirectory.exists()) {
            mergeDirectory.mkdir();
        }

        Date start = new Date();

        try {
            //IndexWriter writer = new IndexWriter(INDEX_DIR, new StandardAnalyzer(), true, IndexWriter.MaxFieldLength.UNLIMITED);
            IndexWriter writer = new IndexWriter(mergeDirectory, new StandardAnalyzer(), true);
            writer.setMergeFactor(mergeFactor);
            writer.setRAMBufferSizeMB(bufferSizeMB);

            Directory indexes[] = new Directory[indexesDirectory.list().length];

            for (int i = 0; i < indexesDirectory.list().length; i++) {
                logger.info("Adding: " + indexesDirectory.list()[i]);
                String index = indexesDirectory.getAbsolutePath() + "/" + indexesDirectory.list()[i];

                logger.info("Found Index: " + index);
                indexes[i] = FSDirectory.getDirectory(index);
            }

            logger.info("Merging added indexes...");
            writer.addIndexesNoOptimize(indexes);
            logger.info("done");

            logger.info("Optimizing index...");
            writer.optimize();
            writer.close();
            logger.info("done");

            Date end = new Date();
            logger.info("It took: " + ((end.getTime() - start.getTime()) / 1000) + "\"");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void refreshIndex() throws Exception {
        refreshIndex(Integer.MAX_VALUE);
    }

    /**
     * Refresh the current search index by re-visiting all topic pages.
     *
     * @param wikiTopicLimit
     * @throws Exception Thrown if any error occurs while re-indexing the Wiki.
     */
    public synchronized void refreshIndex(int wikiTopicLimit) throws Exception {
        List<VirtualWiki> allWikis = WikiBase.getDataHandler().getVirtualWikiList();

        for (VirtualWiki virtualWiki : allWikis) {
            long start = System.currentTimeMillis();
            int count = 0;
            FSDirectory directory = FSDirectory.getDirectory(this.getSearchIndexPath(virtualWiki.getName()));
            KeywordAnalyzer keywordAnalyzer = new KeywordAnalyzer();
            IndexWriter writer = null;

            // FIXME - move synchronization to the writer instance for this directory
            try {
                Topic topic = null;

                String wikiName = virtualWiki.getName();
                DataHandler handler = WikiBase.getDataHandler();
                writer = new IndexWriter(directory, new StandardAnalyzer(), true, IndexWriter.MaxFieldLength.LIMITED);

                if (this.topicIds == null) {
                    this.topicIds = handler.getAllTopicIdentifiers(wikiName);
                }
                int wikiTopicCount = 0;
                logger.info("Retrieved WIki Topic IDs =>: " + this.topicIds.size());

                for (Integer topicId : this.topicIds) {

                    if (!(wikiTopicCount < wikiTopicLimit)) {
                        break;
                    }
                    //topic = WikiBase.getDataHandler().lookupTopic(virtualWiki.getName(), topicName, false, null);
                    topic = handler.lookupTopicById(wikiName, topicId.intValue());

                    String topicContent = topic.getTopicContent();

                    if ((topicContent != null) && (!topicContent.startsWith("#REDIRECT"))) {

                        Document standardDocument = createStandardDocument(topic);
                        writer.addDocument(standardDocument);

                        ParserOutput parserOutput = ParserUtil.parserOutput(topic.getTopicContent(), virtualWiki.getName(), topic.getName());
                        Document keywordDocument = createKeywordDocument(topic, parserOutput.getLinks());
                        writer.addDocument(keywordDocument, keywordAnalyzer);
                        count++;
                        logger.info("TOPIC-COUNT: " + wikiTopicCount);
                        wikiTopicCount++;
                    }
                }
            } catch (Exception ex) {
                logger.fatal("Failure while refreshing search index", ex);
            } finally {
                try {
                    if (writer != null) {
                        writer.optimize();
                    }
                } catch (Exception e) {
                    logger.fatal("Exception during optimize", e);
                }
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (Exception e) {
                    logger.fatal("Exception during close", e);
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
        if (StringUtils.isBlank(summary) && !StringUtils.isBlank(content)) {
            summary = StringEscapeUtils.escapeHtml(content.substring(0, Math.min(200, content.length())));
            if (Math.min(200, content.length()) == 200) {
                summary += "...";
            }
        }
        return summary;
    }
}
