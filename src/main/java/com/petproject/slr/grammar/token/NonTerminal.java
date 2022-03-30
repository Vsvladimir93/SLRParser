package com.petproject.slr.grammar.token;

public record NonTerminal(String value) implements Token {
    @Override
    public String getValue() {
        return value;
    }
}
