package com.tarasboychuk;

import com.tarasboychuk.search.TextIndexer;
import com.tarasboychuk.search.impl.InMemorySearchEngine;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InMemorySearchEngineTests {
    @Mock
    private TextIndexer textIndexer;

    @InjectMocks
    InMemorySearchEngine searchEngine;

    @Test
    public void testSearchComputesIndexes() {
        String docKey = "docA";
        String docText = "Some text is here";

        searchEngine.indexDocument(docKey, docText);

        verify(textIndexer, times(1)).computeIndexes(docText);
    }

    @Test
    public void testIndexDocumentComputesIndexes() {
        String docKey = "docB";
        String docText = "Some document text";

        searchEngine.indexDocument(docKey, docText);

        verify(textIndexer, times(1)).computeIndexes(docText);
    }

    @Test
    public void testSearchWithEmptyResult() {
        Set<String> indexes = searchEngine.search("XXX");

        assertThat(indexes, is(empty()));
    }

    @Test
    public void testSearchBySingleWord() {
        String docKey = "docC";
        String docText = "The text searching";
        when(textIndexer.computeIndexes(docText)).thenReturn(Set.of("text", "searching"));
        searchEngine.indexDocument(docKey, docText);

        String searchQuery = "searching";
        when(textIndexer.computeIndexes(searchQuery)).thenReturn(Set.of(searchQuery));
        Set<String> foundDocKeys = searchEngine.search(searchQuery);

        assertThat(foundDocKeys, Matchers.contains(docKey));
    }

    @Test
    public void testSearchInTwoDocuments() {
        String searchEngineKey = "search-engine";
        String searchEngineText = "A web search engine is a software system";
        when(textIndexer.computeIndexes(searchEngineText)).thenReturn(Set.of("web", "search", "engine", "software", "system"));
        searchEngine.indexDocument(searchEngineKey, searchEngineText);
        String webCrawlerKey = "web-crawler";
        String webCrawlerText = "A Web crawler, sometimes called a spider";
        when(textIndexer.computeIndexes(webCrawlerText)).thenReturn(Set.of("web", "crawler", "sometimes", "called", "spider"));
        searchEngine.indexDocument(webCrawlerKey, webCrawlerText);

        String searchQuery = "web";
        when(textIndexer.computeIndexes(searchQuery)).thenReturn(Set.of(searchQuery));
        Set<String> foundDocKeys = searchEngine.search(searchQuery);

        assertThat(foundDocKeys, Matchers.containsInAnyOrder(searchEngineKey, webCrawlerKey));
    }

    @Test
    public void testSearchByComplexQueryInMultipleDocs() {
        String docKey1 = "key1";
        String docText1 = "Some text, that contains words search, and engine";
        when(textIndexer.computeIndexes(docText1)).thenReturn(Set.of("some", "text", "contains", "words", "search", "engine"));
        searchEngine.indexDocument(docKey1, docText1);

        String docKey2 = "key2";
        String docText2 = "Another text, that also contains \"search\", and \"engine\"";
        when(textIndexer.computeIndexes(docText2)).thenReturn(Set.of("another", "text", "also", "contains", "words",
                "search", "engine"));
        searchEngine.indexDocument(docKey2, docText2);

        String searchQuery = "search engine";
        when(textIndexer.computeIndexes(searchQuery)).thenReturn(Set.of("search", "engine"));
        Set<String> foundDocKeys = searchEngine.search(searchQuery);

        assertThat(foundDocKeys, Matchers.containsInAnyOrder(docKey1, docKey2));
    }
}
