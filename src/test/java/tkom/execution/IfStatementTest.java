package tkom.execution;

import org.junit.Test;
import tkom.ast.expression.NumberNode;
import tkom.errorHandler.InvalidTokenException;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.errorHandler.UnexpectedTokenException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class IfStatementTest extends ExecutionTest {

    @Test
    public void checkExecuteTrueBlock() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironmentWithVariable("a", 5);
        executeStatement("if(a==5) { a = 1;} else { a = 0; }");
        assertEquals(new NumberNode(1), environment.getVariableValue("a"));
    }

    @Test
    public void checkExecuteFalseBlock() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironmentWithVariable("a", 4);
        executeStatement("if(a==5) { a = 1;} else { a = 0; }");
        assertEquals(new NumberNode(0), environment.getVariableValue("a"));
    }

    @Test
    public void checkExecuteTrueWithoutElse() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironmentWithVariable("a", 4);
        executeStatement("if(a==5) { a = 1;} ");
        assertEquals(new NumberNode(4), environment.getVariableValue("a"));
    }
}
