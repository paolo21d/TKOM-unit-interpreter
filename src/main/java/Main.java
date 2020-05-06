import modules.ast.Program;
import modules.data.KeyWords;
import modules.errorHandler.InvalidTokenException;
import modules.errorHandler.UnexpectedTokenException;
import modules.lexer.Lexer;
import modules.parser.Parser;

import java.io.IOException;
import java.io.StringReader;

public class Main {
    public static void main(String[] args) throws IOException, InvalidTokenException, UnexpectedTokenException {
        KeyWords.addVariableType("long");
//        Lexer lexer = new Lexer(new FileReader("src/main/resources/example1.txt"));
/*        Lexer lexer = new Lexer(new StringReader("function INTEGER main() { return 0.0; }"));
        Token token = lexer.readNextToken();
        while (token.getType() != TokenType.EndOfFile) {
            if (token.getType().equals(TokenType.Identifier)) {
                System.out.println(token.getType().toString() + " " + token.getValue());
            } else if (token.getType().equals(TokenType.NumberLiteral)) {
                System.out.println(token.getType().toString() + " " + token.getLiteralNumber());
            } else {
                System.out.println(token.getType().toString());
            }
            token = lexer.readNextToken();
        }*/

        Parser parser = new Parser(new Lexer(new StringReader("function INTEGER main() { return 0.0; }")));
        Program program = parser.parseProgram();
        System.out.println(program);
    }
}
