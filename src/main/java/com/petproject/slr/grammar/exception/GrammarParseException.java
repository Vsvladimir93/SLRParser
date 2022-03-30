package com.petproject.slr.grammar.exception;

public class GrammarParseException extends RuntimeException {
    public GrammarParseException(String message) {
        super(message);
    }

    public GrammarParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
