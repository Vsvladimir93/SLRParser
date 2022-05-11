package com.petproject.slr.parser.collection;

import com.petproject.slr.grammar.token.Token;

import java.util.List;

public record LRCCState(Token token, List<LRItem> items, Integer stateIndex, LRItem fromItem) {
}
