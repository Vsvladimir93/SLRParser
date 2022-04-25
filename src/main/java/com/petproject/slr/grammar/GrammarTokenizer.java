package com.petproject.slr.grammar;

import com.petproject.slr.grammar.exception.GrammarParseException;
import com.petproject.slr.grammar.token.NonTerminal;
import com.petproject.slr.grammar.token.Rule;
import com.petproject.slr.grammar.token.Terminal;
import com.petproject.slr.grammar.token.Token;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Text to grammar tokens
 */
@Slf4j
class GrammarTokenizer {

    private static final String KEY_VALUE_SEPARATOR = ":=";
    private static final String RULE_KEY_SEPARATOR = "->";
    private static final String RULE_VALUE_SEPARATOR = "|";

    enum TokenType {Terminals, NonTerminals, Rule}

    public Grammar parseGrammar(List<String> grammarDefinitions) {
        log.debug("Parse grammar from definitions {}", grammarDefinitions);

        var terminals = parseTerminals(grammarDefinitions);
        var nonTerminals = parseNonTerminals(grammarDefinitions);
        var rules = parseRules(grammarDefinitions, terminals, nonTerminals);

        log.debug("Terminals: {}", terminals);
        log.debug("NonTerminals: {}", nonTerminals);
        log.debug("Rules: {}", rules);

        return new Grammar(terminals, nonTerminals, rules);
    }

    private Set<Terminal> parseTerminals(List<String> grammarDefinitions) {
        return parseToken(grammarDefinitions, TokenType.Terminals, Terminal.class);
    }

    private Set<NonTerminal> parseNonTerminals(List<String> grammarDefinitions) {
        return parseToken(grammarDefinitions, TokenType.NonTerminals, NonTerminal.class);
    }

    private List<Rule> parseRules(List<String> grammarDefinitions, Set<Terminal> terminals, Set<NonTerminal> nonTerminals) {
        return grammarDefinitions.stream()
                .filter(d -> d.startsWith(TokenType.Rule.name()))
                .map(d -> d.split(KEY_VALUE_SEPARATOR)[1])
                .map(r -> r.split(RULE_KEY_SEPARATOR))
                .map(r -> parseRule(r, terminals, nonTerminals))
                .collect(Collectors.toList());
    }

    private <T extends Token> Set<T> parseToken(List<String> grammarDefinitions, TokenType tokenType, Class<T> clazz) {
        return grammarDefinitions.stream()
                .filter(d -> d.startsWith(tokenType.name()))
                .map(d -> d.split(KEY_VALUE_SEPARATOR)[1].split(" "))
                .map(d -> Arrays.stream(d).collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .map(t -> {
                    try {
                        return clazz.getConstructor(String.class).newInstance(t);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Rule parseRule(String[] ruleTuple, Set<Terminal> terminals, Set<NonTerminal> nonTerminals) {
        var ruleValue = new StringBuilder(ruleTuple[1]);
        List<Token> tokens = new ArrayList<>();
        tokens.addAll(terminals);
        tokens.addAll(nonTerminals);

        List<Token> result = new ArrayList<>();

        while(ruleValue.length() > 0) {
            var token = tokens.stream()
                    .filter(t -> ruleValue.toString().startsWith(t.getValue()))
                    .findFirst();

            if (token.isEmpty())
                throw new GrammarParseException(
                        "Can't parse rule: " + Arrays.toString(ruleTuple) + " Can't find token " + ruleValue);

            result.add(token.get());

            ruleValue.delete(0, token.get().getValue().length());
        }

        return new Rule(new NonTerminal(ruleTuple[0]), result);
    }

}
