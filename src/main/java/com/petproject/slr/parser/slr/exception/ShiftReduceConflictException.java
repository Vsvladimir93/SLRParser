package com.petproject.slr.parser.slr.exception;

public class ShiftReduceConflictException extends RuntimeException {
    public ShiftReduceConflictException(String message) {
        super(message);
    }

    public ShiftReduceConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
