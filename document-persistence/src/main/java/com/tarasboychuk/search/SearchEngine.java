package com.tarasboychuk.search;

import java.util.Set;

/**
 * {@link SearchEngine} is a simple API for full text search engine.
 */
public interface SearchEngine {
    Set<String> search(String query);

    void indexDocument(String key, String document);

    void clear();
}
