package com.petproject.slr.grammar;

import com.petproject.slr.grammar.token.NonTerminal;
import com.petproject.slr.grammar.token.Rule;
import com.petproject.slr.grammar.token.Terminal;

import java.util.List;
import java.util.Set;

public record Grammar(Set<Terminal> terminals,
                      Set<NonTerminal> nonTerminals,
                      List<Rule> rules) {
}
