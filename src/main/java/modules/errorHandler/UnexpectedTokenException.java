package modules.errorHandler;

import modules.data.TokenType;

import java.util.List;

public class UnexpectedTokenException extends Exception { //TODO dodać lineConetnt

    public UnexpectedTokenException(int line,
                                    int position,
                                    String lineContent,
                                    TokenType expectedTokenType,
                                    TokenType providedTokenType) {
        super(String.format("Unexpected token at line %d position %d. Line content: %s\n" +
                        "Expected token type %s provided token %s",
                        line, position, lineContent, expectedTokenType, providedTokenType));
    }

    public UnexpectedTokenException(int line,
                                    int position,
                                    String lineContent,
                                    List<TokenType> expectedTokenTypes,
                                    TokenType providedTokenType) {
        super(String.format("Unexpected token at line %d position %d. Line content: %s\n" +
                        "Expected token types %s provided token %s",
                line, position, lineContent, expectedTokenTypes.toString(), providedTokenType));
    }
}
