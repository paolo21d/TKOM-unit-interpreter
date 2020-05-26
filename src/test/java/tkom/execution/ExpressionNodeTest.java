package tkom.execution;

import org.junit.Test;
import tkom.ast.Value;
import tkom.ast.expression.ExpressionNode;
import tkom.ast.expression.NumberNode;
import tkom.ast.expression.Unit;
import tkom.errorHandler.InvalidTokenException;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.errorHandler.UnexpectedTokenException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ExpressionNodeTest extends ExecutionTest {

    //ADD
    @Test
    public void checkAddNumberToNumber() throws InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException, IOException {
        checkNumberResult("DOUBLE a = 10;", "DOUBLE b = 5;", "a+b", 15);
    }

    @Test
    public void checkAddSameUnits() throws InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException, IOException {
        checkUnitResult("KILO a = 10;", "KILO b = 10;", "a+b", 20, "KILO");
    }

    @Test
    public void checkAddConvertedUnits() throws InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException, IOException {
        checkUnitResult("KILO a = 1000;", "MEGA b = 1;", "a+b", 2, "MEGA");
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void checkAddNumberToUnit() throws InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException, IOException {
        getExpressionResult("DOUBLE a = 10;", "KILO b = 20;", "a+b");
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void checkAddUnitToVariable() throws InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException, IOException {
        getExpressionResult("KILO a = 10;", "DOUBLE b = 20;", "a+b");
    }

    //SUBTRACT
    @Test
    public void checkSubtractNumberToNumber() throws InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException, IOException {
        checkNumberResult("DOUBLE a = 10;", "DOUBLE b = 5;", "a-b", 5);
    }

    @Test
    public void checkSubtractSameUnits() throws InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException, IOException {
        checkUnitResult("KILO a = 10;", "KILO b = 1;", "a-b", 9, "KILO");
    }

    @Test
    public void checkSubtractConvertedUnits() throws InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException, IOException {
        checkUnitResult("KILO a = 2000;", "MEGA b = 1;", "a-b", 1, "MEGA");
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void checkSubtractNumberToUnit() throws InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException, IOException {
        getExpressionResult("DOUBLE a = 10;", "KILO b = 20;", "a-b");
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void checkSubtractUnitToVariable() throws InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException, IOException {
        getExpressionResult("KILO a = 10;", "DOUBLE b = 20;", "a-b");
    }

    //MULTIPLY
    @Test
    public void checkMultiplyNumberToNumber() throws InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException, IOException {
        checkNumberResult("DOUBLE a = 10;", "DOUBLE b = 5;", "a*b", 50);
    }

    @Test
    public void checkMultiplySameUnits() throws InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException, IOException {
        checkUnitResult("KILO a = 10;", "KILO b = 2;", "a*b", 20000, "KILO");
    }

    @Test
    public void checkMultiplyConvertedUnits() throws InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException, IOException {
        checkUnitResult("KILO a = 2;", "MEGA b = 1;", "a*b", 2000, "MEGA");
    }

    @Test
    public void checkMultiplyNumberToUnit() throws InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException, IOException {
        checkUnitResult("DOUBLE a = 2;", "KILO b = 1;", "a*b", 2, "KILO");
    }

    @Test
    public void checkMultiplyUnitToVariable() throws InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException, IOException {
        checkUnitResult("KILO b = 1;", "DOUBLE a = 2;", "a*b", 2, "KILO");
    }

    //DIVIDE
    @Test
    public void checkDivideNumberToNumber() throws InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException, IOException {
        checkNumberResult("DOUBLE a = 10;", "DOUBLE b = 5;", "a/b", 2);
    }

    @Test
    public void checkDivideSameUnits() throws InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException, IOException {
        checkUnitResult("KILO a = 10;", "KILO b = 2;", "a/b", 0.005, "KILO");
    }

    @Test
    public void checkDivideConvertedUnits() throws InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException, IOException {
        checkUnitResult("MEGA a = 2;", "KILO b = 1;", "a/b", 2, "KILO");
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void checkDivideNumberToUnit() throws InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException, IOException {
        getExpressionResult("DOUBLE a = 2;", "KILO b = 1;", "a/b");
    }

    @Test
    public void checkDivideUnitToVariable() throws InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException, IOException {
        checkUnitResult("KILO a = 10;", "DOUBLE b = 2;", "a/b", 5, "KILO");
    }


    //PRIVATES
    private Value getExpressionResult(String firstArgument, String secondArgument, String operation) throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        executeStatement(firstArgument);
        executeStatement(secondArgument);
        return evaluateExpression(operation);
    }

    private void checkNumberResult(String firstArgument, String secondArgument, String operation, double expectedValue) throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        NumberNode result = (NumberNode) getExpressionResult(firstArgument, secondArgument, operation);
        assertEquals(new NumberNode(expectedValue), result);
    }

    private void checkUnitResult(String firstArgument, String secondArgument, String operation, double expectedUnitValue, String expectedUnitType) throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        Unit result = (Unit) getExpressionResult(firstArgument, secondArgument, operation);
        assertUnit(new Unit(expectedUnitValue, expectedUnitType, environment.getUnitRatio().getUnitValue(expectedUnitType)), result);
    }

    private ExpressionNode parseExpression(String expressionText) throws UnexpectedTokenException, InvalidTokenException, IOException {
        initializeParser(expressionText);
        return parser.parseExpression();
    }

    private Value evaluateExpression(String conditionText) throws IOException, InvalidTokenException, UnexpectedTokenException, RuntimeEnvironmentException {
        return parseExpression(conditionText).evaluate(environment);
    }
}
