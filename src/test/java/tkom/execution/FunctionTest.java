package tkom.execution;

import org.junit.Test;
import tkom.ast.Value;
import tkom.ast.expression.NumberNode;
import tkom.ast.expression.Unit;
import tkom.ast.function.FunctionDef;
import tkom.ast.statement.ExecuteOut;
import tkom.ast.statement.FunctionCall;
import tkom.errorHandler.InvalidTokenException;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.errorHandler.UnexpectedTokenException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class FunctionTest extends ExecutionTest {

    @Test(expected = RuntimeEnvironmentException.class)
    public void checkWrongQuantityArgument() throws RuntimeEnvironmentException, UnexpectedTokenException, InvalidTokenException, IOException {
        prepareEnvironment();
        addFunction("function INTEGER foo(INTEGER arg1) { return arg1; }");
        executeCallFunction("foo(1, 2);");
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void checkWrongTypeOfArgument() throws RuntimeEnvironmentException, UnexpectedTokenException, InvalidTokenException, IOException {
        prepareEnvironment();
        addFunction("function INTEGER foo(INTEGER arg1) { return arg1; }");
        executeStatement("KILO k = 10;");
        executeCallFunction("foo(k);");

        prepareEnvironment();
        addFunction("function INTEGER foo(KILO arg1) { return arg1; }");
        executeStatement("INTEGER i = 10;");
        executeCallFunction("foo(i);");
    }

    @Test
    public void checkReturnDouble() throws RuntimeEnvironmentException, UnexpectedTokenException, InvalidTokenException, IOException {
        prepareEnvironment();
        addFunction("function DOUBLE foo(DOUBLE arg1) { return arg1; }");
        ExecuteOut out = executeCallFunction("foo(1.5);");
        assertFalse(out.isReturnCall());
        assertEquals(new NumberNode(1.5), out.getReturnedValue());
    }

    @Test
    public void checkReturnInteger() throws RuntimeEnvironmentException, UnexpectedTokenException, InvalidTokenException, IOException { //TODO moze ogarnac żeby integer nie przyjmowal ulamkow
        prepareEnvironment();
        addFunction("function INTEGER foo(INTEGER arg1) { return arg1; }");
        ExecuteOut out = executeCallFunction("foo(1);");
        assertFalse(out.isReturnCall());
        assertEquals(new NumberNode(1), out.getReturnedValue());
    }

    @Test
    public void checkReturnUnit() throws RuntimeEnvironmentException, UnexpectedTokenException, InvalidTokenException, IOException {
        prepareEnvironment();
        addFunction("function KILO foo(KILO arg1) { return arg1; }");
        executeStatement("KILO k = 10;");
        ExecuteOut out = executeCallFunction("foo(k);");
        assertFalse(out.isReturnCall());
        assertUnit(new Unit(10, "KILO", environment.getUnitRatio().getUnitValue("KILO")), (Unit) out.getReturnedValue());
    }

    @Test
    public void checkReturnUnitCasting() throws RuntimeEnvironmentException, UnexpectedTokenException, InvalidTokenException, IOException {
        prepareEnvironment();
        addFunction("function KILO foo(KILO arg1) { return arg1; }");
        executeStatement("MEGA m = 1;");
        ExecuteOut out = executeCallFunction("foo(m);");
        assertFalse(out.isReturnCall());
        assertUnit(new Unit(1000, "KILO", environment.getUnitRatio().getUnitValue("KILO")), (Unit) out.getReturnedValue());
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void checkCallNonExistingFunction() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        ExecuteOut out = executeCallFunction("foo(1);");
    }

    @Test
    public void checkPrintMethod() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        ExecuteOut out = executeCallFunction("print(1);");
    }

    //////
    private void addFunction(String functionText) throws IOException, InvalidTokenException, UnexpectedTokenException {
        initializeParser(functionText);
        FunctionDef functionDef = parser.parseFunctionDef();
        environment.addFunction(functionDef);
    }

    private ExecuteOut executeCallFunction(String callFunctionText) throws UnexpectedTokenException, InvalidTokenException, IOException, RuntimeEnvironmentException {
        initializeParser(callFunctionText);
        FunctionCall functionCall = (FunctionCall) parser.parseStatement();
        return functionCall.execute(environment);
    }

    private Value evaluateFunctionCall(String callFunctionText) throws UnexpectedTokenException, InvalidTokenException, IOException, RuntimeEnvironmentException {
        initializeParser(callFunctionText);
        FunctionCall functionCall = (FunctionCall) parser.parseStatement();
        return functionCall.evaluate(environment);
    }

}
