package com.tarasboychuk.persistence;

import java.util.Optional;
import java.util.Set;

/**
 * {@link DocumentRepository} is an document service related API that specifies all required methods for document
 * persistence and search.
 */
public interface DocumentRepository {
    boolean putDocument(String key, String document);

    Optional<String> findByKey(String key);

    Set<String> search(String query);

    void clear();
}
