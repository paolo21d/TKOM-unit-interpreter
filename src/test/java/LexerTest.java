import modules.Data.Token;
import modules.Data.TokenType;
import modules.ErrorHandler.InvalidLiteralNumber;
import modules.ErrorHandler.InvalidToken;
import modules.Lexer.Lexer;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class LexerTest {

    @Test
    public void emptyInput() throws IOException, InvalidToken {
        Lexer lexer = new Lexer(new StringReader(""));
        assertEquals(TokenType.EndOfFile, lexer.readNextToken().getType());
    }

    @Test
    public void checkIdentifierType() throws IOException, InvalidToken {
        Lexer lexer = new Lexer(new StringReader("id1 id_2 _id3"));
        for (int i = 0; i < 3; i++) {
            assertEquals(TokenType.Identifier, lexer.readNextToken().getType());
        }
    }

    @Test
    public void checkNumberToken() throws IOException, InvalidToken {
        List<Double> numbers = new ArrayList<Double>(Arrays.asList(0.1D, 1D, 8.888D, 999999D));
        StringBuilder inputValue = new StringBuilder();
        for (Double number : numbers) {
            inputValue.append(number.toString()).append(" ");
        }
        Lexer lexer = new Lexer(new StringReader(inputValue.toString()));
        for (Double number : numbers) {
            Token token = lexer.readNextToken();
            assertEquals(TokenType.NumberLiteral, token.getType());
            assertEquals(number, token.getLiteralNumber());
        }
    }

    @Test(expected = InvalidLiteralNumber.class)
    public void checkInvalidLiteralNumber() throws IOException, InvalidToken {
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
            } catch (InvalidToken e) {
                assertEquals(invalidToken, e.getInvalidText());
            }
        }
    }

}
