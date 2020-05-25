package tkom.execution;

import org.junit.Test;
import tkom.ast.expression.NumberNode;
import tkom.ast.expression.Unit;
import tkom.ast.statement.ExecuteOut;
import tkom.errorHandler.InvalidTokenException;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.errorHandler.UnexpectedTokenException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BlockTest extends ExecutionTest {


    @Test
    public void checkExecuteEmptyBlock() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        ExecuteOut out = executeStatement("{}");
        assertEquals(ExecuteOut.ExecuteStatus.NORMAL, out.getStatus());
        assertFalse(out.isReturnCall());
    }

    @Test
    public void checkReturnNumber() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        ExecuteOut out = executeStatement("{return 0;}");
        assertReturnNumber(0, out);

        prepareEnvironment();
        out = executeStatement("{INTEGER a = 1; return a;}");
        assertReturnNumber(1, out);

        prepareEnvironment();
        out = executeStatement("{DOUBLE a = 2.5; return a;}");
        assertReturnNumber(2.5, out);
    }

    @Test
    public void checkReturnUnit() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        ExecuteOut out = executeStatement("{KILO k = 10; return k;}");
        assertEquals(ExecuteOut.ExecuteStatus.RETURN, out.getStatus());
        assertUnit(new Unit(10, "KILO", environment.getUnitRatio().getUnitValue("KILO")), ((Unit) out.getReturnedValue()));
    }

    @Test
    public void checkIfExecuteAfterReturn() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        ExecuteOut out = executeStatement("{return 5; return 6;}");
        assertReturnNumber(5, out);
    }

    @Test
    public void checkExpressionEvaluateInReturn() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        ExecuteOut out = executeStatement("{INTEGER a = 1; INTEGER b = 2; return a+b;}");
        assertReturnNumber(3, out);
    }

    private void assertReturnNumber(double expectedNumber, ExecuteOut out) {
        assertEquals(ExecuteOut.ExecuteStatus.RETURN, out.getStatus());
        assertEquals(new NumberNode(expectedNumber), out.getReturnedValue());
    }
}
