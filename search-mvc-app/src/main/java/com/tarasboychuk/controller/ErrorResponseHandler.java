package com.tarasboychuk.controller;

import com.tarasboychuk.exception.DocumentKeyAlreadyExistsException;
import com.tarasboychuk.exception.DocumentNotFoundException;
import com.tarasboychuk.exception.EmptyDocumentException;
import com.tarasboychuk.exception.EmptyKeyException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorResponseHandler {

    @ExceptionHandler(DocumentNotFoundException.class)
    public String documentNotFound(DocumentNotFoundException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "document";
    }

    @ExceptionHandler(DocumentKeyAlreadyExistsException.class)
    public String documentKeyAlreadyExists(DocumentKeyAlreadyExistsException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "form";
    }

    @ExceptionHandler(EmptyDocumentException.class)
    public String emptyDocument(EmptyDocumentException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "form";
    }

    @ExceptionHandler(EmptyKeyException.class)
    public String emptyKey(EmptyKeyException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "form";
    }
}
