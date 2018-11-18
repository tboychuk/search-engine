package com.tarasboychuk.controller;

import com.tarasboychuk.exception.EmptyDocumentException;
import com.tarasboychuk.exception.EmptyKeyException;
import com.tarasboychuk.service.DocumentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
@RequestMapping("/docs")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/add")
    public String getDocumentForm() {
        return "form";
    }

    @PostMapping
    public String saveDocument(@RequestParam String key, @RequestParam String document, Model model) {
        validate(key, document);
        documentService.putDocument(key, document);
        model.addAttribute("success", true);
        return "form";
    }

    private void validate(String key, String document) {
        if (key.trim().isEmpty()) {
            throw new EmptyKeyException("Key cannot be empty.");
        }
        if (document.trim().isEmpty()) {
            throw new EmptyDocumentException("Document cannot be empty.");
        }
    }

    @GetMapping
    public String getDocumentByKey(@RequestParam(required = false) String key, Model model) {
        if (key != null) {
            String document = documentService.getDocument(key);
            model.addAttribute("document", document);
        }
        return "document";
    }

    @GetMapping("/search")
    public String searchDocuments(@RequestParam String query, Model model) {
        Set<String> foundDocumentKeys = documentService.search(query);
        model.addAttribute("foundKeys", foundDocumentKeys);
        return "searchResult";
    }
}
