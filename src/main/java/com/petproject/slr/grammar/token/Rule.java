package com.petproject.slr.grammar.token;

import java.util.List;

public record Rule(NonTerminal key, List<Token> value) {}
