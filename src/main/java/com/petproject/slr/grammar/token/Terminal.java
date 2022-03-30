package com.petproject.slr.grammar.token;

public record Terminal(String value) implements Token {
    @Override
    public String getValue() {
        return value;
    }
}
