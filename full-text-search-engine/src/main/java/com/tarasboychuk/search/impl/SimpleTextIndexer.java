package com.tarasboychuk.search.impl;

import com.tarasboychuk.search.TextIndexer;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link SimpleTextIndexer} is a simple implementation of a {@link TextIndexer} that uses three step algorithm to compute
 * text indexes. The first step removes all punctuation. The second step changes all words to lowercase. The third step
 * removes all stop words. In order to perform the algorithm it requires a {@link Set} of stop words.
 */
public class SimpleTextIndexer implements TextIndexer {
    private final static String PUNCTUATION_REGEX = "\\W+";
    private final Set<String> stopWords;

    public SimpleTextIndexer(Set<String> stopWords) {
        this.stopWords = stopWords;
    }

    @Override
    public Set<String> computeIndexes(String text) {
        return Stream.of(text.split(PUNCTUATION_REGEX))
                .map(String::toLowerCase)
                .filter(this::isNotStopWord)
                .collect(Collectors.toSet());
    }

    private boolean isNotStopWord(String word) {
        return !stopWords.contains(word);
    }
}
