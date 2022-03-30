package com.petproject.slr.grammar;

import com.petproject.slr.grammar.token.NonTerminal;
import com.petproject.slr.grammar.token.Terminal;
import com.petproject.slr.grammar.token.Token;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Grammar {
    private final Set<Terminal> terminals;
    private final Set<NonTerminal> nonTerminals;
    private final Map<NonTerminal, Set<Token>> productionRules;
}
