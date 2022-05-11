package com.petproject.slr.parser.slr.exception;

public class ReduceReduceConflictException extends RuntimeException {
    public ReduceReduceConflictException(String message) {
        super(message);
    }

    public ReduceReduceConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
