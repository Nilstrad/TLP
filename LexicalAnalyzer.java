import java.io.*;
import java.util.*;

public class LexicalAnalyzer {
    private static final Set<String> KEYWORDS = Set.of("for", "do", "int");

    private final BufferedReader reader;
    private int lineNumber = 1;
    private int columnNumber = 0;
    private char currentChar;
    private boolean endOfFile = false;

    public LexicalAnalyzer(String filename) throws IOException {
        reader = new BufferedReader(new FileReader(filename));
        advance();
    }

    @SuppressWarnings("ConvertToTryWithResources")
    public List<Lexeme> analyze() throws IOException, LexicalException {
        List<Lexeme> lexemes = new ArrayList<>();
        StateType state = StateType.START;
        StringBuilder buffer = new StringBuilder();

        while (!endOfFile) {
            switch (state) {
                case START -> {
                    skipWhitespace();
                    buffer.setLength(0);

                    if (Character.isLetter(currentChar) || currentChar == '_') {
                        state = StateType.IDENTIFIER;
                    } else if ("XVI".indexOf(currentChar) >= 0) {
                        state = StateType.NUMBER;
                    } else if ("+-*/%".indexOf(currentChar) >= 0) {
                        state = StateType.ARITHMETIC;
                    } else if (currentChar == ':') {
                        state = StateType.ASSIGN;
                    } else if ("(){};".indexOf(currentChar) >= 0) {
                        state = StateType.SEPARATOR;
                    } else if ("<>=!".indexOf(currentChar) >= 0) {
                        state = StateType.CONDITIONS;
                    } else {
                        state = StateType.ERROR;
                    }
                }

                case IDENTIFIER -> {
                    while (Character.isLetterOrDigit(currentChar) || currentChar == '_') {
                        buffer.append(currentChar);
                        advance();
                    }
                    String lexeme = buffer.toString();
                    lexemes.add(new Lexeme(KEYWORDS.contains(lexeme) ? LexemeType.KEYWORD : LexemeType.IDENTIFIER, lexeme));
                    state = StateType.START;
                }

                case NUMBER -> {
                    while ("XVI".indexOf(currentChar) >= 0) {
                        buffer.append(currentChar);
                        advance();
                    }
                    lexemes.add(new Lexeme(LexemeType.NUMBER, buffer.toString()));
                    state = StateType.START;
                }

                case ASSIGN -> {
                    buffer.append(':');
                    advance();
                    if (currentChar == '=') {
                        buffer.append('=');
                        advance();
                        lexemes.add(new Lexeme(LexemeType.OPERATOR, buffer.toString()));
                    } else {
                        throw new LexicalException("Unexpected symbol ':' at line " + lineNumber + ", column " + columnNumber);
                    }
                    state = StateType.START;
                }

                case SEPARATOR -> {
                    lexemes.add(new Lexeme(LexemeType.SEPARATOR, String.valueOf(currentChar)));
                    advance();
                    state = StateType.START;
                }

                case CONDITIONS -> {
                    buffer.append(currentChar);
                    advance();
                    if (currentChar == '=') {
                        buffer.append('=');
                        advance();
                    }
                    lexemes.add(new Lexeme(LexemeType.OPERATOR, buffer.toString()));
                    state = StateType.START;
                }

                case ARITHMETIC -> {
                    lexemes.add(new Lexeme(LexemeType.OPERATOR, String.valueOf(currentChar)));
                    advance();
                    state = StateType.START;
                }

                case ERROR -> throw new LexicalException("Unexpected character '" + currentChar + "' at line " + lineNumber + ", column " + columnNumber);
            }
        }

        reader.close();
        return lexemes;
    }

    private void advance() throws IOException {
        int nextChar = reader.read();
        if (nextChar == -1) {
            endOfFile = true;
        } else {
            currentChar = (char) nextChar;
            columnNumber++;
            if (currentChar == '\n') {
                lineNumber++;
                columnNumber = 0;
            }
        }
    }

    private void skipWhitespace() throws IOException {
        while (Character.isWhitespace(currentChar) && !endOfFile) {
            advance();
        }
    }
}
