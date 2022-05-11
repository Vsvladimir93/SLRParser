package com.petproject.slr.parser.collection;

import com.petproject.slr.grammar.token.NonTerminal;
import com.petproject.slr.grammar.token.Rule;
import com.petproject.slr.grammar.token.Token;

import java.util.List;
import java.util.Optional;

public record LRItem(NonTerminal key, List<Token> values, Integer cursorIndex) {
    public Optional<Token> getTokenAfterCursor() {
        if (cursorIndex >= values.size())
            return Optional.empty();

        return Optional.of(values.get(cursorIndex));
    }

    public static LRItem ruleToItem(Rule rule) {
        return new LRItem(rule.key(), rule.value(), 0);
    }

    public static LRItem shiftCursor(LRItem item) {
        return new LRItem(item.key, item.values, item.cursorIndex + 1);
    }

    public List<Token> getTokensAroundCursor() {
        var end = cursorIndex + 1;

        if (end >= values.size())
            end = values.size();

        return values.subList(0, end);
    }
}
