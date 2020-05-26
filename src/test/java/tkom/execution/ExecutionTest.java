package tkom.execution;

import tkom.ast.Signature;
import tkom.ast.expression.NumberNode;
import tkom.ast.expression.Unit;
import tkom.ast.function.FunctionDef;
import tkom.ast.statement.ExecuteOut;
import tkom.ast.statement.Statement;
import tkom.data.UnitRatio;
import tkom.errorHandler.InvalidTokenException;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.errorHandler.UnexpectedTokenException;
import tkom.lexer.Lexer;
import tkom.parser.Parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ExecutionTest {
    Parser parser;
    Environment environment;
    UnitRatio unitRatio;

    protected void assertUnit(Unit expectedUnit, Unit actualUnit) {
        assertEquals(Double.valueOf(expectedUnit.getValue()), Double.valueOf(actualUnit.getValue()));
        assertEquals(expectedUnit.getUnitType(), expectedUnit.getUnitType());
        assertEquals(Double.valueOf(expectedUnit.getRatio()), Double.valueOf(actualUnit.getRatio()));
    }

    protected void prepareEnvironmentWithVariable(String variableName, double variableValue) throws RuntimeEnvironmentException {
        prepareEnvironment();
        environment.addVariable(variableName, new NumberNode(variableValue));
    }

    protected Parser initializeParser(String input) {
        Lexer lexer = new Lexer(new StringReader(input));
        parser = new Parser(lexer);
        return parser;
    }

    protected Environment prepareEnvironment() throws RuntimeEnvironmentException {
        FunctionDef functionDef = new FunctionDef();
        functionDef.setSignature(new Signature("DOUBLE", "main"));
        environment = new Environment(Arrays.asList(functionDef), new UnitRatio());
        environment.createNewLocalScope();
        return environment;
    }

    protected ExecuteOut executeStatement(String input) throws UnexpectedTokenException, InvalidTokenException, IOException, RuntimeEnvironmentException {
        initializeParser(input);
        Statement statement = parser.parseSingleLineOrBlockStatement();
        return statement.execute(environment);
    }
}
