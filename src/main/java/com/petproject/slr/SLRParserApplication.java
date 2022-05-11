package com.petproject.slr;

import com.petproject.slr.grammar.GrammarParser;
import com.petproject.slr.parser.slr.SLRParser;
import com.petproject.slr.parser.util.Util;
import com.petproject.slr.parser.collection.LRCCGenerator;
import com.petproject.slr.parser.collection.LRCCState;
import com.petproject.slr.parser.slr.SLRParsingTable;
import com.petproject.slr.shared.Mapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class SLRParserApplication {
    public static void main(String[] args) {
        var grammar = GrammarParser.parseGrammar("example");

        grammar = Util.augmentGrammar(grammar);

        log.debug("Grammar: {}", grammar);

        List<LRCCState> canonicalCollection = LRCCGenerator.generate(grammar);

        var parsingTable = new SLRParsingTable(grammar, canonicalCollection);

        log.info("Parsing table: {}", Mapper.writeValueAsString(parsingTable));

        SLRParser parser = new SLRParser(grammar, parsingTable);

        parser.parse("(1123123123+3)-3+2");


    }
}
