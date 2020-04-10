import modules.Data.KeyWords;
import modules.Data.Token;
import modules.Data.TokenType;
import modules.Lexer.Lexer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        KeyWords.addVariableType("long");
        Lexer lexer = new Lexer("src/main/resources/example1.txt");
        Token token = lexer.getToken();
        while (token.getType() != TokenType.EndOfLine) {
            if (token.getType().equals(TokenType.Identifier) || token.getType().equals(TokenType.NumberLiteral) || token.getType().equals(TokenType.VariableType)) {
                System.out.println(token.getType().toString() + " " + token.getValue());
            } else {
                System.out.println(token.getType().toString());
            }
            token = lexer.getToken();
        }
    }
}
