package com.tarasboychuk.search.impl;

import com.tarasboychuk.search.SearchEngine;
import com.tarasboychuk.search.TextIndexer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@link InMemorySearchEngine} is a {@link HashMap} based implementation of {@link SearchEngine}. In order to perform
 * text searches, it requires an instance of {@link TextIndexer}
 */
public class InMemorySearchEngine implements SearchEngine {
    private final Map<String, Set<String>> docKeyToIndexesMap;
    private final TextIndexer textIndexer;

    public InMemorySearchEngine(TextIndexer textIndexer) {
        this.textIndexer = textIndexer;
        this.docKeyToIndexesMap = new HashMap<>();
    }

    /**
     * Computes indexes for an input {@link String} query using {@link TextIndexer}. Then uses indexes to find keys
     * of all documents which indexes contains whole set of an input query indexes.
     *
     * @param query a text to search
     * @return a set of keys of documents that contain a search query
     */
    @Override
    public Set<String> search(String query) {
        Set<String> queryIndexes = textIndexer.computeIndexes(query);

        return docKeyToIndexesMap.keySet().stream()
                .filter(key -> docKeyToIndexesMap.get(key).containsAll(queryIndexes))
                .collect(Collectors.toSet());
    }

    public void indexDocument(String key, String document) {
        Set<String> indexes = textIndexer.computeIndexes(document);
        docKeyToIndexesMap.put(key, indexes);
    }

    @Override
    public void clear() {
        docKeyToIndexesMap.clear();
    }
}
