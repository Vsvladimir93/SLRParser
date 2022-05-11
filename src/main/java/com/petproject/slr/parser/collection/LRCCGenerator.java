package com.petproject.slr.parser.collection;

import com.petproject.slr.grammar.Grammar;
import com.petproject.slr.grammar.token.NonTerminal;
import com.petproject.slr.grammar.token.Token;
import com.petproject.slr.shared.Mapper;
import lombok.NoArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
public final class LRCCGenerator {


    public static List<LRCCState> generate(Grammar grammar) {
        var initialState = generateInitialState(grammar);
        var nextIndex = new AtomicInteger(initialState.stateIndex() + 1);

        List<LRCCState> collection = new ArrayList<>();
        collection.add(initialState);

        var processedStates = new HashSet<Integer>();
        processedStates.add(initialState.stateIndex());

        populateCollectionWithGoToOnState(initialState, collection, grammar, nextIndex);

        var hasUnprocessedStates = true;
        Predicate<LRCCState> unprocessedState = l -> !processedStates.contains(l.stateIndex());

        do {
            var newStates = collection.stream()
                    .filter(unprocessedState)
                    .toList();

            for (var state : newStates) {
                processedStates.add(state.stateIndex());
                populateCollectionWithGoToOnState(state, collection, grammar, nextIndex);
            }

            hasUnprocessedStates = hasUnprocessedStates(collection, processedStates);
        } while (hasUnprocessedStates);

        log.debug("Processed states: {}", processedStates);

        log.debug("States size: {}", collection.size());

        log.debug("States: {}", Mapper.writeValueAsString(collection.stream()
                .collect(Collectors.toMap(LRCCState::stateIndex, s -> s))));

        return collection;
    }

    private static boolean hasUnprocessedStates(List<LRCCState> collection, Set<Integer> processedStates) {
        Predicate<LRCCState> unprocessedState = l -> !processedStates.contains(l.stateIndex());
        return collection.stream().anyMatch(unprocessedState);
    }

    private static void populateCollectionWithGoToOnState(
            LRCCState state, List<LRCCState> collection, Grammar grammar, AtomicInteger nextIndex) {
        log.debug("populateCollectionWithGoToOnState number : {} new state number : {}", state.stateIndex(), nextIndex.get() + 1);

        var alreadyProcessedTokens = new HashSet<Token>();

        for (var item : state.items()) {
            if (collection.subList(1, collection.size()).stream().anyMatch(s -> s.fromItem().getTokensAroundCursor()
                    .equals(item.getTokensAroundCursor()))) {
                log.warn("Item already calculated: {}", item);

                var tokensAroungCursor = item.getTokensAroundCursor();
                Object asd = null;

                continue;
            }
//            if (collection.stream().anyMatch(s -> s.fromItem().equals(item))) {
//                log.warn("Item already calculated: {}", item);
//                continue;
//            }

            if (item.getTokenAfterCursor().isEmpty()) {
                log.warn("Token after cursor isEmpty for initial items! Item: {}", item);
                continue;
            }

            var tokenAfterCursor = item.getTokenAfterCursor().get();

            if (alreadyProcessedTokens.contains(tokenAfterCursor)) {
                log.debug("Item with token: {} already processed for state number: {}",
                        tokenAfterCursor, state.stateIndex());
                continue;
            }

            var newState = goTo(
                    nextIndex.getAndIncrement(),
                    tokenAfterCursor,
                    state, item);

            newState = closure(newState, grammar);

            collection.add(newState);
            alreadyProcessedTokens.add(tokenAfterCursor);
        }
    }

    private static LRCCState generateInitialState(Grammar grammar) {
        var initialItems = grammar.rules().stream()
                .map(rule -> new LRItem(rule.key(), rule.value(), 0))
                .toList();

        return new LRCCState(grammar.getPrimaryRule().key(), initialItems, 0, initialItems.get(0));
    }

    private static LRCCState closure(LRCCState state, Grammar grammar) {
        var items = new ArrayList<>(state.items());

        // If has NonTerminal after cursor - add items for them
        var additionalItems = findAdditionalItems(items, Collections.emptyList(), grammar);

        // Repeat while have new NonTerminals after cursor
        while (!additionalItems.isEmpty()) {
            items.addAll(additionalItems);

            additionalItems = findAdditionalItems(additionalItems, items, grammar);
        }

        return new LRCCState(state.token(), items, state.stateIndex(), state.fromItem());
    }

    private static LRCCState goTo(Integer nextIndex, Token token, LRCCState state, LRItem fromItem) {
        // Get all items from state where the "token" is followed by the cursor
        // Shift cursor by one position to the right
        // Return new state

        var itemsWhereTokenFollowedByCursor = state.items().stream()
                .filter(item -> item.getTokenAfterCursor().isPresent())
                .filter(item -> item.getTokenAfterCursor().get().equals(token))
                .map(LRItem::shiftCursor)
                .toList();
        log.debug("Check item number : {} it has : {} items where token followed by the cursor",
                state.stateIndex(), itemsWhereTokenFollowedByCursor.size());
        return new LRCCState(token, itemsWhereTokenFollowedByCursor, nextIndex, fromItem);
    }

    private static List<LRItem> findAdditionalItems(List<LRItem> items, List<LRItem> except, Grammar grammar) {
        Predicate<LRItem> notPresentIn = r -> except.stream().noneMatch(ex -> ex.equals(r));

        return items.stream()
                .map(LRItem::getTokenAfterCursor)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(t -> t instanceof NonTerminal)
                .flatMap(t -> grammar.getRulesByKey((NonTerminal) t)
                        .stream()
                        .map(LRItem::ruleToItem))
                .filter(notPresentIn)
                .toList();
    }
}
