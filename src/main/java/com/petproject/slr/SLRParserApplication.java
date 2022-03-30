package com.petproject.slr;

import com.petproject.slr.grammar.Grammar;
import com.petproject.slr.grammar.GrammarParser;
import com.petproject.slr.grammar.token.NonTerminal;
import com.petproject.slr.grammar.token.Terminal;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;

@Slf4j
public class SLRParserApplication {
    public static void main(String[] args) {
        GrammarParser.parseGrammar("grammar");

    }

}
