package tkom;

import tkom.ast.Program;
import tkom.data.UnitRatio;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.execution.Environment;
import tkom.lexer.Lexer;
import tkom.parser.Parser;

import java.io.FileReader;
import java.util.ArrayList;

public class Run {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Program require 1 argument - path to file with code");
            System.exit(1);
        }

        String codeFile = args[0];

        try {
            Parser parser = new Parser(new Lexer(new FileReader(codeFile)));
            Program program = parser.parseProgram();
            UnitRatio unitRatio = new UnitRatio();
            runProgram(program, unitRatio);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void runProgram(Program program, UnitRatio unitRatio) throws RuntimeEnvironmentException {
        Environment environment = new Environment(program.getFunctions(), unitRatio);
        environment.getFunction("main").execute(environment, new ArrayList<>());
    }
}
