package tkom.execution;

import org.junit.Test;
import tkom.ast.expression.NumberNode;
import tkom.ast.expression.Unit;
import tkom.errorHandler.InvalidTokenException;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.errorHandler.UnexpectedTokenException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class InitStatementTest extends ExecutionTest {
    @Test
    public void checkInitNumber() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        executeStatement("DOUBLE a;");
        assertEquals(new NumberNode(0), environment.getVariableValue("a"));

        prepareEnvironment();
        executeStatement("DOUBLE a;");
        assertEquals(new NumberNode(0), environment.getVariableValue("a"));
    }

    @Test
    public void checkInitUnit() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        executeStatement("KILO a;");
        assertUnit(new Unit(0, "KILO", environment.getUnitRatio().getUnitValue("KILO")), ((Unit) environment.getVariableValue("a")));

        prepareEnvironment();
        executeStatement("KILO a = 5;");
        assertUnit(new Unit(5, "KILO", environment.getUnitRatio().getUnitValue("KILO")), ((Unit) environment.getVariableValue("a")));

        prepareEnvironment();
        executeStatement("DOUBLE i = 5;");
        executeStatement("KILO a = i;");
        assertUnit(new Unit(5, "KILO", environment.getUnitRatio().getUnitValue("KILO")), ((Unit) environment.getVariableValue("a")));
    }


/*    @Test(expected = RuntimeEnvironmentException.class)
    public void checkInitUnitWithNumberException() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        executeStatement("DOUBLE i = 5;");
        executeStatement("KILO a = i;");
    }*/

    @Test(expected = RuntimeEnvironmentException.class)
    public void checkInitNumberWithUnitException() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        executeStatement("KILO k = 5;");
        executeStatement("DOUBLE i = k;");
    }
}
