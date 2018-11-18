package com.tarasboychuk.config;

import com.tarasboychuk.persistence.DocumentRepository;
import com.tarasboychuk.persistence.impl.InMemoryDocumentRepository;
import com.tarasboychuk.search.SearchEngine;
import com.tarasboychuk.search.TextIndexer;
import com.tarasboychuk.search.impl.InMemorySearchEngine;
import com.tarasboychuk.search.impl.SimpleTextIndexer;
import com.tarasboychuk.service.DocumentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class RootConfig {
    @Value("${search.engine.stop-words}")
    private Set<String> stopWords;

    @Bean
    public DocumentService documentService() {
        return new DocumentService(documentRepository());
    }

    @Bean
    public DocumentRepository documentRepository() {
        return new InMemoryDocumentRepository(searchEngine());
    }

    @Bean
    public SearchEngine searchEngine() {
        return new InMemorySearchEngine(textIndexer());
    }

    @Bean
    public TextIndexer textIndexer() {
        return new SimpleTextIndexer(stopWords);
    }
}
