package com.petproject.slr.parser.util;

import com.petproject.slr.grammar.Grammar;
import com.petproject.slr.grammar.exception.GrammarParseException;
import com.petproject.slr.grammar.token.*;

import java.util.*;

public class Util {
    public static Grammar augmentGrammar(Grammar grammar) {
        var primaryRule = grammar.rules().stream().findFirst();

        if (primaryRule.isEmpty())
            throw new GrammarParseException("Can't augment grammar. Rules is empty.");

        var primaryKey = primaryRule.get().key();
        var augmentedPrimaryKey = new NonTerminal(primaryKey.value().concat("'"));

        List<NonTerminal> augmentedKeys = new ArrayList<>();
        augmentedKeys.add(augmentedPrimaryKey);
        augmentedKeys.addAll(grammar.nonTerminals());

        var rule = new Rule(augmentedPrimaryKey, List.of(primaryKey));
        List<Rule> augmentedRules = new ArrayList<>();
        augmentedRules.add(rule);
        augmentedRules.addAll(grammar.rules());

        return new Grammar(grammar.terminals(), augmentedKeys, augmentedRules, true, grammar.acceptToken(),
                grammar.epsilonToken());
    }

    public static Set<Token> findFollowOf(NonTerminal token, Grammar augmentedGrammar) {
        return findFollowOfConstraints(token, augmentedGrammar, 0);
    }

    private static Set<Token> findFollowOfConstraints(NonTerminal token, Grammar grammar, Integer repeats) {
        Set<Token> set = new HashSet<>();

        if (token.equals(grammar.getPrimaryRule().key())) {
            set.add(grammar.acceptToken());
        }

        if (repeats > 10)
            return set;

        for (int i = 0; i < grammar.rules().size(); i++) {
            Rule rule = grammar.rules().get(i);
            for (int j = 0; j < rule.value().size(); j++) {
                Token token1 = rule.value().get(j);
                if (token1.equals(token)) {
                    if (rule.value().size() <= j + 1) {
                        set.addAll(findFollowOfConstraints(rule.key(), grammar, repeats + 1));
                    } else if (rule.value().get(j + 1) instanceof Terminal) {
                        set.add(rule.value().get(j + 1));
                    } else {
                        set.addAll(findFirstOf(rule.value().get(j + 1), grammar));
                    }
                }
            }
        }
        return set;
    }

    public static Set<Token> findFirstOf(Token token, Grammar grammar) {
        Set<Token> set = new HashSet<>();

        if (token instanceof Terminal) {
            set.add(token);
            return set;
        }

        var rules = grammar.rules().stream()
                .filter(r -> !r.value().get(0).equals(r.key()))
                .filter(r -> r.key().equals(token))
                .toList();

        for (var rule : rules) {
            set.addAll(findFirstOf(rule.value().get(0), grammar));
        }

        return set;
    }
}
