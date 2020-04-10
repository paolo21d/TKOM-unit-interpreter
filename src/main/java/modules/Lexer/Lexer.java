package modules.Lexer;

import lombok.Getter;
import modules.Data.KeyWords;
import modules.Data.Token;
import modules.Data.TokenType;
import modules.ErrorHandler.InvalidLiteralNumber;
import modules.ErrorHandler.InvalidToken;
import modules.Reader.InputManager;

import java.io.IOException;

public class Lexer {
    //private Reader reader;
    private InputManager reader;
    @Getter
    private Token token;

    /*public Lexer(String fileName) throws FileNotFoundException {
        reader = new Reader(fileName);
    }*/

    public Lexer(java.io.Reader reader) {
        this.reader = new InputManager(reader);
    }

   /* public Token getToken() throws IOException {
        StringBuilder buffer = new StringBuilder();
        Character sign = reader.getChar();
        while (Character.isWhitespace(sign) || sign == 13 || sign == 10) { //TODO zmienic na  to 13 i 10
            sign = reader.getChar();
        }

        if (sign == 0) { //EOF
            return new Token(TokenType.EndOfLine); //TODO ogarnac na EndOfFile
        }

        if (Character.isAlphabetic(sign) || sign.equals('_')) { //ID || keyWord
            do {
                buffer.append(Character.toLowerCase(sign));
                sign = reader.getChar();
            } while (Character.isAlphabetic(sign) || Character.isDigit(sign) || sign.equals('_'));
            reader.ungetChar();

            if (KeyWords.keywords.containsKey(buffer.toString())) { //keyword
                return new Token(KeyWords.keywords.get(buffer.toString()));
            } else if (KeyWords.variableTypes.containsKey(buffer.toString())) {
                return new Token(KeyWords.variableTypes.get(buffer.toString()), buffer.toString());
            } else { //identifier
                return new Token(TokenType.Identifier, buffer.toString());
            }
        } else if (Character.isDigit(sign)) { //number
            do {
                buffer.append(sign);
                sign = reader.getChar();
            } while (Character.isDigit(sign) || sign.equals('.'));
            reader.ungetChar();
            return new Token(TokenType.NumberLiteral, buffer.toString());
        } else { //special character
            switch (sign) {
                case '=':
                    return checkDualOperator('=', TokenType.Equality, TokenType.Assignment);
                case '<':
                    return checkDualOperator('=', TokenType.LessOrEqual, TokenType.Less);
                case '>':
                    return checkDualOperator('=', TokenType.GreaterOrEqual, TokenType.Greater);
                case '!':
                    return checkDualOperator('=', TokenType.Inequality, TokenType.Negation);
                case '&':
                    return checkDualOperator('&', TokenType.And, TokenType.Invalid);
                case '|':
                    return checkDualOperator('|', TokenType.Or, TokenType.Invalid);
                default:
                    return new Token(KeyWords.definedSigns.getOrDefault(sign.toString(), TokenType.Invalid));
            }
        }
    }

    private Token checkDualOperator(Character secondChar, TokenType typeIfDual, TokenType typeIfMono) throws IOException {
        Character nextSign = reader.getChar();
        if (nextSign.equals(secondChar)) {
            return new Token(typeIfDual);
        } else {
            reader.ungetChar();
            return new Token(typeIfMono);
        }
    }*/

    public Token readNextToken() throws IOException, InvalidToken {
        skipWhiteCharacters();
        //analyze EOF
        char sign = reader.peekChar();

        if (isStartingKeywordOrIdentifier(sign)) {
            token = getKeywordOrIdentifierToken();
        } else if (isStartingDigit(sign)) {
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

    private boolean isStartingDigit(char sign) {
        return Character.isDigit(sign);
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
            return new Token(KeyWords.keywords.get(word));
        } else if (KeyWords.variableTypes.containsKey(word)) {
            return new Token(KeyWords.variableTypes.get(word), word);
        } else { //identifier
            return new Token(TokenType.Identifier, word);
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
        return new Token(TokenType.NumberLiteral, number);
    }

    private Token getOtherToken() throws IOException, InvalidToken {
        char firstSign = reader.getNextChar();
        int tokenBeginSignPosition = reader.getSignPosition();
        char secondSign = reader.peekChar();
        String twoSigns = Character.toString(firstSign) + Character.toString(secondSign);
        if (KeyWords.doubleSigns.containsKey(twoSigns)) {
            return new Token(KeyWords.doubleSigns.get(twoSigns));
        } else if (KeyWords.singleSigns.containsKey(firstSign)) {
            return new Token(KeyWords.singleSigns.get(firstSign));
        } else {
            throw new InvalidToken(reader.getCurrentLine(), twoSigns, reader.getLineNumber(), tokenBeginSignPosition);
        }
    }
}
