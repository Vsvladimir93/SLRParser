package com.petproject.slr.grammar.token;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Terminal implements Token {

    private final String value;

    @Override
    public String getValue() {
        return value;
    }
}
