package com.petproject.slr;

import com.petproject.slr.grammar.GrammarParser;
import com.petproject.slr.grammar.token.NonTerminal;
import lombok.extern.slf4j.Slf4j;

import java.util.AbstractMap;
import java.util.stream.Collectors;

@Slf4j
public class SLRParserApplication {
    public static void main(String[] args) {
        var grammar = GrammarParser.parseGrammar("grammar");

        log.debug("Grammar: {}", grammar);

        grammar.rules()
                .forEach(rule -> {
                    log.debug("Key: {}", rule.key() != null);
                    log.debug("Values: {}", rule.value().stream()
                            .map(t -> new AbstractMap.SimpleEntry<>(t.getValue(), t instanceof NonTerminal))
                            .collect(Collectors.toList()));
                });
    }
}
