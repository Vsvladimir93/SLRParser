package com.petproject.slr.parser;

import com.petproject.slr.grammar.Grammar;
import com.petproject.slr.grammar.token.Token;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class Lexer {
    public static List<Token> splitByTokens(String input, Grammar grammar) {
        var sb = new StringBuilder(input.replaceAll(" ", ""));
        List<Token> tokens = new ArrayList<>();
        var position = 0;

        var terminalAccepted = false;
        while (!sb.isEmpty()) {
            terminalAccepted = false;
            for (var terminal : grammar.terminals()) {
                // Check for RegExp
                if (sb.toString().startsWith(terminal.getValue())) {
                    tokens.add(terminal);
                    sb.delete(0, terminal.getValue().length());
                    position += terminal.getValue().length();
                    terminalAccepted = true;
                    break;
                }
            }
            if (!terminalAccepted) {
                throw new RuntimeException(format("Can't split input by tokens. Position: %d Rest of the input: %s",
                        position, sb));
            }
        }

        return tokens;
    }
}
