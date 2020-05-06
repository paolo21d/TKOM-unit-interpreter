package modules.lexer;

import lombok.Getter;
import modules.data.KeyWords;
import modules.data.Token;
import modules.data.TokenType;
import modules.errorHandler.InvalidLiteralNumber;
import modules.errorHandler.InvalidToken;
import modules.inputManager.InputManager;

import java.io.IOException;
import java.io.Reader;

public class Lexer {
    private InputManager reader;
    @Getter
    private Token token;

    public Lexer(Reader reader) {
        this.reader = new InputManager(reader);
    }

    public Token readNextToken() throws IOException, InvalidToken {
        skipWhiteCharacters();

        char sign = reader.peekChar();
        if (isEndOfFile(sign)) {
            return new Token(TokenType.EndOfFile, reader.getLineNumber(), reader.getSignPosition(), reader.getCurrentLine());
        }

        if (isStartingKeywordOrIdentifier(sign)) {
            token = getKeywordOrIdentifierToken();
        } else if (isStartingNumber(sign)) {
            token = getNumberToken();
        } else {
            token = getOtherToken();
        }
        return token;
    }

    private void skipWhiteCharacters() throws IOException {
        char sign = reader.peekChar();
        while (isWhitespace(sign)) {
            reader.getNextChar();
            sign = reader.peekChar();
        }
    }

    private boolean isWhitespace(char sign) {
        return Character.isWhitespace(sign) || sign == '\n' || sign == '\r';
    }

    private boolean isStartingKeywordOrIdentifier(char sign) {
        return Character.isLetter(sign) || sign == '_';
    }

    private boolean isStartingNumber(char sign) {
        return Character.isDigit(sign);
    }

    private boolean isEndOfFile(char sign) {
        return sign == '\uFFFF';
    }

    private Token getKeywordOrIdentifierToken() throws IOException {
        StringBuilder buffer = new StringBuilder();
        char nextSign = reader.peekChar();
        while (Character.isLetterOrDigit(nextSign) || nextSign == '_') {
            buffer.append(reader.getNextChar());
            nextSign = reader.peekChar();
        }
        String word = buffer.toString();

        if (KeyWords.keywords.containsKey(word)) {
            return new Token(KeyWords.keywords.get(word), reader.getLineNumber(), reader.getSignPosition(), reader.getCurrentLine());
        } else if (KeyWords.variableTypes.containsKey(word)) {
            return new Token(KeyWords.variableTypes.get(word), word, reader.getLineNumber(), reader.getSignPosition(), reader.getCurrentLine());
        } else { //identifier
            return new Token(TokenType.Identifier, word, reader.getLineNumber(), reader.getSignPosition(), reader.getCurrentLine());
        }
    }

    private Token getNumberToken() throws IOException, InvalidLiteralNumber {
        StringBuilder buffer = new StringBuilder();
        char nextSign = reader.peekChar();
        int tokenBeginSignPosition = reader.getSignPosition() + 1;
        while (Character.isDigit(nextSign) || nextSign == '.') {
            buffer.append(reader.getNextChar());
            nextSign = reader.peekChar();
        }
        String word = buffer.toString();
        Double number = null;
        try {
            number = Double.parseDouble(word); //TODO ogarnac sytujacje 123>=x ORAZ sprawdzanie znaku po liczbie, zeby ogarnac sytuacje 123abc
        } catch (NumberFormatException exception) {
            throw new InvalidLiteralNumber(reader.getCurrentLine(), word, reader.getLineNumber(), tokenBeginSignPosition);
        }
        return new Token(TokenType.NumberLiteral, number, reader.getLineNumber(), reader.getSignPosition(), reader.getCurrentLine());
    }

    private Token getOtherToken() throws IOException, InvalidToken {
        String firstSign = Character.toString(reader.getNextChar());
        int tokenBeginSignPosition = reader.getSignPosition();
        String secondSign = Character.toString(reader.peekChar());
        String twoSigns = firstSign + secondSign;
        if (KeyWords.doubleSigns.containsKey(twoSigns)) {
            return new Token(KeyWords.doubleSigns.get(twoSigns), reader.getLineNumber(), reader.getSignPosition(), reader.getCurrentLine());
        } else if (KeyWords.singleSigns.containsKey(firstSign)) {
            return new Token(KeyWords.singleSigns.get(firstSign), reader.getLineNumber(), reader.getSignPosition(), reader.getCurrentLine());
        } else {
            reader.getNextChar(); //consume second sign
            throw new InvalidToken(reader.getCurrentLine(), twoSigns, reader.getLineNumber(), tokenBeginSignPosition);
        }
    }
}
