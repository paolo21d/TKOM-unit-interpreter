package tkom.lexer;

import org.junit.Test;
import tkom.data.KeyWords;
import tkom.data.Token;
import tkom.data.TokenType;
import tkom.errorHandler.InvalidLiteralNumberException;
import tkom.errorHandler.InvalidStringException;
import tkom.errorHandler.InvalidTokenException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void emptyInput() throws IOException, InvalidTokenException {
        Lexer lexer = new Lexer(new StringReader(""));
        assertEquals(TokenType.EndOfFile, lexer.readNextToken().getType());
        assertEquals(TokenType.EndOfFile, lexer.readNextToken().getType());
    }

    @Test
    public void checkIdentifierType() throws IOException, InvalidTokenException {
        Lexer lexer = new Lexer(new StringReader("id1 id_2 _id3"));
        for (int i = 0; i < 3; i++) {
            assertEquals(TokenType.Identifier, lexer.readNextToken().getType());
        }
    }

    @Test
    public void checkNumberToken() throws IOException, InvalidTokenException {
        List<Double> numbers = new ArrayList<>(Arrays.asList(0.1D, 1D, 8.888D, 999999D));
        StringBuilder inputValue = new StringBuilder();
        for (Double number : numbers) {
            inputValue.append(number.toString()).append(" ");
        }
        Lexer lexer = new Lexer(new StringReader(inputValue.toString()));
        for (Double number : numbers) {
            Token token = lexer.readNextToken();
            assertEquals(TokenType.NumberLiteral, token.getType());
            assertEquals(number, Double.valueOf(token.getLiteralNumber()));
        }
    }

    @Test(expected = InvalidLiteralNumberException.class)
    public void checkInvalidLiteralNumber() throws IOException, InvalidTokenException {
        Lexer lexer = new Lexer(new StringReader("1.1.2"));
        lexer.readNextToken();
    }

    @Test
    public void checkInvalidTokens() throws IOException {
        List<String> invalidTokens = new ArrayList<>(Arrays.asList("&|", "|&"));
        StringBuilder stringBuilder = new StringBuilder();
        for (String invalidToken : invalidTokens) {
            stringBuilder.append(invalidToken).append(" ");
        }

        Lexer lexer = new Lexer(new StringReader(stringBuilder.toString()));
        for (String invalidToken : invalidTokens) {
            try {
                Token token = lexer.readNextToken();
                fail("Unrecognized invalid token: " + token.toString());
            } catch (InvalidTokenException e) {
                assertEquals(invalidToken, e.getInvalidText());
            }
        }
    }

    @Test
    public void checkNewVariableTypes() throws IOException, InvalidTokenException {
        List<String> newVariableTypes = new ArrayList<>(Arrays.asList("long", "newType1", "new_type_"));
        StringBuilder builder = new StringBuilder();
        for (String newVariableType : newVariableTypes) {
            KeyWords.addVariableType(newVariableType);
            builder.append(newVariableType).append(" ");
        }

        Lexer lexer = new Lexer(new StringReader(builder.toString()));
        for (String newVariableType : newVariableTypes) {
            Token token = lexer.readNextToken();
            assertEquals(TokenType.VariableType, token.getType());
            assertEquals(newVariableType, token.getValue());
        }

    }

    @Test
    public void checkStringToken() throws IOException, InvalidTokenException {
        Lexer lexer = new Lexer(new StringReader("\"String in my program\""));
        Token token = lexer.readNextToken();
        assertEquals(TokenType.String, token.getType());
        assertEquals("String in my program", token.getValue());
    }

    @Test
    public void checkInvalidStringToken() {
        Lexer lexer = new Lexer(new StringReader("\"String in my program"));
        assertThrows(InvalidStringException.class, lexer::readNextToken);
    }
}
