package com.petproject.slr.grammar;

import com.petproject.slr.grammar.exception.GrammarParseException;
import com.petproject.slr.grammar.token.NonTerminal;
import com.petproject.slr.grammar.token.Terminal;
import com.petproject.slr.grammar.token.Token;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GrammarParser {

    public static Grammar parseGrammar(List<String> grammarDefinitions) {
        return new GrammarTokenizer().parseGrammar(grammarDefinitions);
    }

    /**
     * @param grammarFilePath - absolute path to grammar file
     * @return Grammar
     */
    public static Grammar parseGrammar(Path grammarFilePath) {
        var grammarFile = grammarFilePath.toFile();
        if (!grammarFile.exists())
            throw new GrammarParseException("Can't find grammar file by path: " + grammarFilePath);

        try (Stream<String> stream = Files.lines(grammarFilePath)) {
            return parseGrammar(stream.collect(Collectors.toList()));
        } catch (IOException e) {
            throw new GrammarParseException("Can't parse grammar.", e);
        }
    }

    public static Grammar parseGrammar(String resourceFileName) {
        var is = GrammarParser.class.getClassLoader()
                .getResourceAsStream(resourceFileName);
        if (is == null)
            throw new GrammarParseException("Can't find resource file with name: " + resourceFileName);

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        try (Stream<String> stream = reader.lines()) {
            return parseGrammar(stream.collect(Collectors.toList()));
        }
    }

    private static class Tokenizer {

        private static final String KEY_VALUE_SEPARATOR = "[:=]";
        private static final String RULE_KEY_SEPARATOR = "[->]";
        private static final String RULE_VALUE_SEPARATOR = "[|]";

        record GrammarLine(GrammarLineType type, List<Token> payload) {

            public Token getRuleKey() {
                return payload.get(0);
            }

            public List<Token> getRuleValues() {
                return payload.subList(1, payload.size());
            }
        }

        enum GrammarLineType {
            Terminals, NonTerminals, Rule {
                @Override
                GrammarLine parse(String line, List<Terminal> terminals, List<NonTerminal> nonTerminals) {

                    var keySeparatorIndex = line.indexOf(RULE_KEY_SEPARATOR);
                    if (keySeparatorIndex == -1)
                        throw new GrammarParseException(
                                "Can't find index of RULE_KEY_SEPARATOR=" + RULE_KEY_SEPARATOR + " in line: " + line);
                    var ruleNonTerminal = line.substring(0, keySeparatorIndex);
                    var ruleValues = line.substring(keySeparatorIndex);
                    return null;
                }
            };

            GrammarLine parse(String line, List<Terminal> terminals, List<NonTerminal> nonTerminals) {
                var tokens = line
                        .replace(this.name().concat(KEY_VALUE_SEPARATOR), "")
                        .split(" ");


                return null;
//                return new GrammarLine(
//                        Terminals,
//                        Arrays.stream(tokens)
//                                .map(Terminal::new)
//                                .collect(Collectors.toList());
            }
        }



        private GrammarLine parseTokens(String grammarLine, int lineNumber) {
            var grammarLineType = Arrays.stream(GrammarLineType.values())
                    .filter(t -> grammarLine.startsWith(t.name()))
                    .findFirst();

            if (grammarLineType.isEmpty())
                throw new GrammarParseException("Can't distinct grammar line type. Line number: " + lineNumber);

            return grammarLineType.get().parse(grammarLine, null, null);
        }


    }
}
