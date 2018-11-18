package com.tarasboychuk.search;

import java.util.Set;

/**
 * {@link TextIndexer} is an API that is used to compute indexes of a text document represented as {@link String} object.
 * Computed indexes are used to improve the performance of a text search.
 */
public interface TextIndexer {
    Set<String> computeIndexes(String text);
}
