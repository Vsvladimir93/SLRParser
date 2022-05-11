package com.petproject.slr.grammar.token;

public record AcceptToken(String value) implements Token {
    @Override
    public String getValue() {
        return value;
    }
}
