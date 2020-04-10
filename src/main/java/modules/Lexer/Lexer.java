package modules.Lexer;

import modules.Data.KeyWords;
import modules.Data.Token;
import modules.Data.TokenType;
import modules.Reader.Reader;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Lexer {
    private Reader reader;

    public Lexer(String fileName) throws FileNotFoundException {
        reader = new Reader(fileName);
    }

    public Token getToken() throws IOException {
        StringBuilder buffer = new StringBuilder();
        Character sign = reader.getChar();
        while (Character.isWhitespace(sign) || sign == 13 || sign == 10) {
            sign = reader.getChar();
        }

        if (sign == 0) { //EOF
            return new Token(TokenType.EndOfLine);
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
    }

}
