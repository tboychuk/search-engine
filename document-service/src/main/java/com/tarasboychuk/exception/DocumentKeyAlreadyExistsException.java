package com.tarasboychuk.exception;

public class DocumentKeyAlreadyExistsException extends RuntimeException {
    public DocumentKeyAlreadyExistsException(String message) {
        super(message);
    }
}
