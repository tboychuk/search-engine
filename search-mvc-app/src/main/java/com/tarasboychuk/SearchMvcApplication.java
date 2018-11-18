package com.tarasboychuk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * {@link SearchMvcApplication} is an entry point into a Spring Boot MVC application that provides capabilities to
 * put new document by key, get a document by key and search a document by a search query.
 * <p>
 * It couples together {@link com.tarasboychuk.service.DocumentService},
 * {@link com.tarasboychuk.persistence.impl.InMemoryDocumentRepository} ,
 * {@link com.tarasboychuk.search.impl.InMemorySearchEngine}, and {@link com.tarasboychuk.search.impl.SimpleTextIndexer}
 * in order implement all required service, persistence and full text search logic.
 * <p>
 * A configuration that wires up all required beans is located in {@link com.tarasboychuk.config.RootConfig}
 * <p>
 * A simple MVC application that provides an UI for the document service is implemented using Thymeleaf templates
 * and a single {@link com.tarasboychuk.controller.DocumentController}
 */
@SpringBootApplication
public class SearchMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchMvcApplication.class, args);
    }
}
