package modules.errorHandler;

import modules.data.TokenType;

import java.util.List;

public class UnexpectedTokenException extends Exception { //TODO dodać lineConetnt

    public UnexpectedTokenException(int line,
                                    int position,
                                    TokenType expectedTokenType,
                                    TokenType providedTokenType) {
        super(String.format("Unexpected token at line %d position %d. " +
                        "Expected token type %s provided token %s",
                        line, position, expectedTokenType, providedTokenType));
    }

    public UnexpectedTokenException(int line,
                                    int position,
                                    List<TokenType> expectedTokenTypes,
                                    TokenType providedTokenType) {
        super(String.format("Unexpected token at line %d position %d. " +
                        "Expected token types %s provided token %s",
                line, position, expectedTokenTypes.toString(), providedTokenType));
    }
}
