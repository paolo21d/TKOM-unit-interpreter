package tkom.execution;

import org.junit.Test;
import tkom.ast.expression.NumberNode;
import tkom.ast.expression.Unit;
import tkom.ast.statement.ExecuteOut;
import tkom.ast.statement.ReturnStatement;
import tkom.errorHandler.InvalidTokenException;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.errorHandler.UnexpectedTokenException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReturnStatementTest extends ExecutionTest {

    @Test
    public void checkReturnNumberNode() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        ExecuteOut out = executeReturnStatement("return 10;");
        assertTrue(out.isReturnCall());
        assertEquals(new NumberNode(10), out.getReturnedValue());
    }

    @Test
    public void checkReturnUnit() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        executeStatement("KILO k = 10;");
        ExecuteOut out = executeReturnStatement("return k;");
        assertTrue(out.isReturnCall());
        assertUnit(new Unit(10, "KILO", environment.getUnitRatio().getUnitValue("KILO")), ((Unit) out.getReturnedValue()));
    }

    private ExecuteOut executeReturnStatement(String input) throws UnexpectedTokenException, InvalidTokenException, IOException, RuntimeEnvironmentException {
        initializeParser(input);
        ReturnStatement statement = ((ReturnStatement) parser.parseStatement());
        return statement.execute(environment);
    }
}
