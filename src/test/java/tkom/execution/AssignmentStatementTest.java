package tkom.execution;

import org.junit.Test;
import tkom.ast.expression.NumberNode;
import tkom.ast.expression.Unit;
import tkom.errorHandler.InvalidTokenException;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.errorHandler.UnexpectedTokenException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class AssignmentStatementTest extends ExecutionTest {

    @Test
    public void checkNumberAssignment() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        String input = "INTEGER a = 5;";
        executeStatement(input);
        assertEquals(new NumberNode(5), environment.getVariableValue("a"));
        executeStatement("a=4;");
        assertEquals(new NumberNode(4), environment.getVariableValue("a"));

        prepareEnvironment();
        input = "DOUBLE b = 5.4;";
        executeStatement(input);
        assertEquals(new NumberNode(5.4), environment.getVariableValue("b"));
        executeStatement("b=3.1;");
        assertEquals(new NumberNode(3.1), environment.getVariableValue("b"));
    }

    @Test
    public void checkUnitAssignment() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        executeStatement("KILO k = 5;");
        assertUnit(new Unit(5, "KILO", environment.getUnitRatio().getUnitValue("KILO")), ((Unit) environment.getVariableValue("k")));
        executeStatement("MEGA x = k;");
        assertUnit(new Unit(0.005, "MEGA", environment.getUnitRatio().getUnitValue("MEGA")), ((Unit) environment.getVariableValue("x")));

        executeStatement("MEGA m = 1;");
        executeStatement("k = m;");
        assertUnit(new Unit(1000, "KILO", environment.getUnitRatio().getUnitValue("KILO")), ((Unit) environment.getVariableValue("k")));
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void assignUnitToNumberException() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        executeStatement("INTEGER a;");
        executeStatement("KILO k = 5;");
        executeStatement("a = k;");
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void assignNumberToUnitException() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        executeStatement("KILO x = 1;");
        executeStatement("INTEGER y = 5;");
        executeStatement("x = y;");
    }

}
