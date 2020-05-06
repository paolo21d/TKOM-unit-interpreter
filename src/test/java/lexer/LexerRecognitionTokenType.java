package lexer;

import modules.data.KeyWords;
import modules.data.TokenType;
import modules.errorHandler.InvalidTokenException;
import modules.lexer.Lexer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class LexerRecognitionTokenType {
    private String tokenTypeString;
    private TokenType expectedTokenType;
    private Lexer lexer;

    public LexerRecognitionTokenType(String tokenTypeString, TokenType expectedTokenType) {
        this.tokenTypeString = tokenTypeString;
        this.expectedTokenType = expectedTokenType;
    }

    @Before
    public void initializeLexer() {
        lexer = new Lexer(new StringReader(tokenTypeString));
    }

    @Parameterized.Parameters
    public static Collection tokenTypes() {
        List tokenTypes = new ArrayList<Object[][]>();

        List<Map<String, TokenType>> maps = new ArrayList<Map<String, TokenType>>(
                Arrays.asList(KeyWords.keywords, KeyWords.singleSigns, KeyWords.doubleSigns, KeyWords.variableTypes)
        );

        //put all string with TokenTypes from class KeyWords to List tokenType
        for(Map<String, TokenType> map: maps){
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                tokenTypes.add(new Object[]{pair.getKey(), pair.getValue()});
            }
        }

        return tokenTypes;
    }

    @Test
    public void testLexerRecognitionTokenType() throws IOException, InvalidTokenException {
        assertEquals(expectedTokenType, lexer.readNextToken().getType());
    }
}
