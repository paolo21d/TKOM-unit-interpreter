import tkom.ast.Program;
import tkom.data.KeyWords;
import tkom.errorHandler.InvalidTokenException;
import tkom.errorHandler.UnexpectedTokenException;
import tkom.lexer.Lexer;
import tkom.parser.Parser;

import java.io.FileReader;
import java.io.IOException;

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

//        Parser parser = new Parser(new Lexer(new StringReader("function INTEGER main() { return 0.0; }")));
        Parser parser = new Parser(new Lexer(new FileReader("src/main/resources/example1.txt")));
        Program program = parser.parseProgram();
        System.out.println(program);
    }
}
