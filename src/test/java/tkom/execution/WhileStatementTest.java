package tkom.execution;

import org.junit.Test;
import tkom.ast.expression.NumberNode;
import tkom.errorHandler.InvalidTokenException;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.errorHandler.UnexpectedTokenException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class WhileStatementTest extends ExecutionTest {
    @Test
    public void falseCondition() throws RuntimeEnvironmentException, UnexpectedTokenException, InvalidTokenException, IOException {
        prepareEnvironmentWithVariable("var1", 1);
        String input = "while(var1 == 3) { var1 = 2; }";
        executeStatement(input);
        assertEquals(new NumberNode(1), environment.getVariableValue("var1"));
    }

    @Test
    public void checkReturnInBlock() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironmentWithVariable("var1", 1);
        String input = "while(var1 < 10) { return var1; }";
        executeStatement(input);
        assertEquals(new NumberNode(1), environment.getVariableValue("var1"));
    }

    @Test
    public void checkIterate() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironmentWithVariable("var1", 1);
        String input = "while(var1<10) { var1 = var1 + 1;}";
        executeStatement(input);
        assertEquals(new NumberNode(10), environment.getVariableValue("var1"));
    }

}
