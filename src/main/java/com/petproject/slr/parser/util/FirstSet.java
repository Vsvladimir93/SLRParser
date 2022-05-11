package com.petproject.slr.parser.util;

import com.petproject.slr.grammar.Grammar;
import com.petproject.slr.grammar.token.Terminal;
import com.petproject.slr.grammar.token.Token;

import java.util.HashSet;
import java.util.Set;

public class FirstSet {
    public Set<Terminal> calculate(Token token, Grammar grammar) {
        if (token instanceof Terminal)
            return Set.of((Terminal) token);

        Set<Terminal> set = new HashSet<>();

        /* If exists rule where N -> epsilon, add epsilon to Set */

        var epsilonRuleExists = grammar.rules().stream()
                .filter(r -> r.key().equals(token))
                .anyMatch(r -> r.value().get(0).equals(grammar.epsilonToken()));

        if (epsilonRuleExists)
            set.add(grammar.epsilonToken());


        /// Get all first K of terminals from N rules

        

        return null;
    }
}
