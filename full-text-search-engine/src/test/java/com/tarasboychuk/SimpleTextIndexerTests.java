package com.tarasboychuk;

import com.tarasboychuk.search.impl.SimpleTextIndexer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class SimpleTextIndexerTests {
    private Set<String> stopWords = Set.of("i", "a", "about", "an", "are", "as", "at", "be", "by", "com", "for", "from",
            "how", "in", "is", "it", "of", "on", "or", "that", "the", "this", "to", "was", "what", "when", "where", "who",
            "will", "with");
    private SimpleTextIndexer textIndexer = new SimpleTextIndexer(stopWords);

    @Test
    public void testRemovePunctuation() {
        String text = "1. 2? 3! 4, 5; 6: 7 - 8[ 9] 10{ 11} 12( 13) 14' ";

        Set<String> indexes = textIndexer.computeIndexes(text);

        assertThat(indexes,
                containsInAnyOrder("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"));
    }

    @Test
    public void testIgnoresDuplication() {
        String text = "Why do we indexes? Because indexes help to improve the performance.";

        Set<String> indexes = textIndexer.computeIndexes(text);

        assertThat(indexes, hasItem("indexes"));
    }

    @Test
    public void testMakeAllLowercase() {
        String text = "Some text that use both lower and UPPER case";

        Set<String> indexes = textIndexer.computeIndexes(text);

        assertThat(indexes, hasItems("some", "upper"));
        assertThat(indexes, not(hasItems("Some", "UPPER")));
    }

    @Test
    public void testRemoveStopWords() {
        String text = "Indexed search. When the search area is large, the reasonable solution is to create an index of" +
                " search terms beforehand. Treat it like a glossary with the numbers of the pages where the term is" +
                " mentioned, which you may notice at the end of some books or papers. So full text search consists of" +
                " two stages. On the first stage, the algorithm forms this kind of index, or more accurate to say a" +
                " concordance as it contains the term along with the referring to find them in the text (like" +
                " “Sentence 3, character number 125”. After this index is built, the search algorithm scans the index" +
                " instead of the original set of documents and exposes the results. As you noticed, this approach" +
                " demands a lot of time to create an index, but then it is much faster to search for information in" +
                " the documents using index than simple string search methods. An important part of indexing is" +
                " normalization. It is word processing, which brings the source text into a standard canonical form." +
                " It means that stop words and articles are removed, diacritical marks (like in words “pâté”, “naïve”," +
                " “złoty”) are removed or replaced with standard alphabet signs. Also, a single case is chosen (only" +
                " upper or lower). Another important part of normalization is stemming. It’s a process of reducing a" +
                " word to a stem form, or base form. For example, for words “eating”, “ate”, “eaten” stem form is" +
                " “eat”. Like so search request “vegans eating meat pâté caught on tape” transforms into “vegan eat" +
                " meat pate tape”. In addition, it’s very important to specify the language for the algorithm to work," +
                " and even spelling (e. g. English, American, Australian, South African etc.).";

        Set<String> indexes = textIndexer.computeIndexes(text);

        assertThat(indexes, everyItem(not(isIn(stopWords))));

    }

}
