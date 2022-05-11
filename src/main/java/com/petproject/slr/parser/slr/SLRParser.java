package com.petproject.slr.parser.slr;

import com.petproject.slr.grammar.Grammar;
import com.petproject.slr.grammar.token.Rule;
import com.petproject.slr.grammar.token.Token;
import com.petproject.slr.parser.AST;
import com.petproject.slr.parser.Lexer;
import com.petproject.slr.parser.Parser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class SLRParser implements Parser {

    private final Grammar grammar;
    private final SLRParsingTable table;

    @Override
    public AST parse(String input) {
        var tokens = Lexer.splitByTokens(input, grammar);
        var pointer = 0;
        log.info("Lexer. Split input by tokens: {}", tokens);

        Stack<Object> stack = new Stack<>();
        Queue<Token> inputTokens = new LinkedList<>(tokens);
        inputTokens.add(grammar.acceptToken());

        stack.add(0);

        log.debug("Start parsing");

        while (true) {
            int state = (int) stack.peek();
            Action action = table.actionTable.get(inputTokens.peek())[state];
            if (action == null) {
                log.error("Input is incorrect. Error at position: {} of input string: {}", pointer,
                        new StringBuilder(input).insert(pointer, "!"));
                break;
            }

            log.debug("{} for state: {} token: {}", action.getType(), state, inputTokens.peek());

            if (action instanceof ShiftAction) {
                var token = inputTokens.remove();
                pointer += token.getValue().length();
                stack.add(token);
                stack.add(action.getIndex());
            } else if (action instanceof ReduceAction) {
                Rule rule = grammar.rules().get(action.getIndex());
                for (int i = 0; i < rule.value().size() * 2; i++) {
                    stack.pop();
                }
                int prevState = (int) stack.peek();
                stack.push(rule.key());
                stack.push(table.goToTable.get(rule.key())[prevState]);
            } else if (action instanceof AcceptAction) {
                log.info("Input accepted");
                break;
            }
        }

        return null;
    }
}
