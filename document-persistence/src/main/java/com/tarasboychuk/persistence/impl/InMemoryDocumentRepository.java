package com.tarasboychuk.persistence.impl;

import com.tarasboychuk.persistence.DocumentRepository;
import com.tarasboychuk.search.SearchEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * {@link InMemoryDocumentRepository} is a {@link HashMap} based implementation of {@link DocumentRepository}. In order
 * to provide full text search capabilities it requires an instance of {@link SearchEngine}
 */
public class InMemoryDocumentRepository implements DocumentRepository {
    private final Map<String, String> keysToDocumentsMap;
    private final SearchEngine searchEngine;

    public InMemoryDocumentRepository(SearchEngine searchEngine) {
        this.searchEngine = searchEngine;
        this.keysToDocumentsMap = new HashMap<>();
    }

    @Override
    public boolean putDocument(String key, String document) {
        if (!keysToDocumentsMap.containsKey(key)) {
            keysToDocumentsMap.put(key, document);
            searchEngine.indexDocument(key, document);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<String> findByKey(String key) {
        return Optional.ofNullable(keysToDocumentsMap.get(key));
    }

    @Override
    public Set<String> search(String query) {
        return searchEngine.search(query);
    }

    @Override
    public void clear() {
        keysToDocumentsMap.clear();
        searchEngine.clear();
    }
}
