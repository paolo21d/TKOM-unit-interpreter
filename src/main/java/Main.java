import modules.Data.KeyWords;
import modules.Data.Token;
import modules.Data.TokenType;
import modules.ErrorHandler.InvalidToken;
import modules.Lexer.Lexer;

import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InvalidToken {
        KeyWords.addVariableType("long");
        Lexer lexer = new Lexer(new FileReader("src/main/resources/example2.txt"));
        Token token = lexer.readNextToken();
        while (token.getType() != TokenType.EndOfFile) {
            if (token.getType().equals(TokenType.Identifier) || token.getType().equals(TokenType.VariableType)) {
                System.out.println(token.getType().toString() + " " + token.getValue());
            } else if(token.getType().equals(TokenType.NumberLiteral)){
                System.out.println(token.getType().toString() + " " + token.getLiteralNumber());
            }
            else {
                System.out.println(token.getType().toString());
            }
            token = lexer.readNextToken();
        }
    }
}
