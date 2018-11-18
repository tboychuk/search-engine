package com.tarasboychuk.service;

import com.tarasboychuk.exception.DocumentKeyAlreadyExistsException;
import com.tarasboychuk.exception.DocumentNotFoundException;
import com.tarasboychuk.persistence.DocumentRepository;

import java.util.Set;

/**
 * {@link DocumentService} is a service that implements all document related business logic. It requires an instance of
 * {@link DocumentRepository} in order to store, retrieve and search documents.
 */
public class DocumentService {
    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public void putDocument(String key, String document) {
        if (!documentRepository.putDocument(key, document)) {
            throw new DocumentKeyAlreadyExistsException(String.format("Document with key=%s already exists.", key));
        }
    }

    public String getDocument(String key) {
        return documentRepository.findByKey(key)
                .orElseThrow(() -> new DocumentNotFoundException(String.format("Document not found by key=%s", key)));
    }

    public Set<String> search(String query) {
        return documentRepository.search(query);
    }

    public void clearDocuments() {
        documentRepository.clear();
    }
}
