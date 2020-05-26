package tkom;

import tkom.ast.Program;
import tkom.data.KeyWords;
import tkom.data.UnitRatio;
import tkom.errorHandler.InvalidTokenException;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.errorHandler.UnexpectedTokenException;
import tkom.execution.Environment;
import tkom.lexer.Lexer;
import tkom.parser.Parser;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        KeyWords.addVariableType("long");
//        Lexer lexer = new Lexer(new FileReader("src/main/resources/example1.txt"));
/*        Lexer lexer = new Lexer(new StringReader("function DOUBLE main() { return 0.0; }"));
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

//        Parser parser = new Parser(new Lexer(new StringReader("function DOUBLE main() { return 0.0; }")));
        Parser parser = new Parser(new Lexer(new FileReader("src/main/resources/example2.txt")));
        Program program = parser.parseProgram();
        UnitRatio unitRatio = new UnitRatio();
        runProgram(program, unitRatio);
        System.out.println(program);
    }

    public static void runProgram(Program program, UnitRatio unitRatio) throws RuntimeEnvironmentException {
        Environment environment = new Environment(program.getFunctions(), unitRatio);
        environment.getFunction("main").execute(environment, new ArrayList<>());
    }
}
