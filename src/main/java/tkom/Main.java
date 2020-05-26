package tkom;

import tkom.ast.Program;
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
//        KeyWords.addVariableType("long");
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
