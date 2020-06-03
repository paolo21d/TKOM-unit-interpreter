package tkom.execution;

import org.junit.Test;
import tkom.ast.Value;
import tkom.ast.condition.Condition;
import tkom.ast.expression.NumberNode;
import tkom.errorHandler.InvalidTokenException;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.errorHandler.UnexpectedTokenException;

import java.io.IOException;

import static org.junit.Assert.*;

public class ConditionTest extends ExecutionTest {

    @Test(expected = RuntimeEnvironmentException.class)
    public void checkNegationUnitException() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        executeStatement("KILO a = 1;");
        evaluateCondition("!a;");
    }

    @Test
    public void checkTrueNegation() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        executeStatement("DOUBLE a = 1;");
        Value result = evaluateCondition("!a");
        assertFalseConditionResult(result);
    }

    @Test
    public void checkFalseNegation() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        executeStatement("DOUBLE a = 0;");
        Value result = evaluateCondition("!a");
        assertTrueConditionResult(result);
    }

    @Test
    public void checkIfTrueReturnTrue() throws RuntimeEnvironmentException, UnexpectedTokenException, InvalidTokenException, IOException {
        prepareEnvironment();
        Condition condition = parseCondition("1==1");
        assertTrue(condition.checkIfTrue(environment));
    }

    @Test
    public void checkIfTrueReturnFalse() throws RuntimeEnvironmentException, UnexpectedTokenException, InvalidTokenException, IOException {
        prepareEnvironment();
        Condition condition = parseCondition("1==2");
        assertFalse(condition.checkIfTrue(environment));
    }

    //EQUALITY
    @Test
    public void checkEqualityDOUBLEToDOUBLE() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        checkRelation("DOUBLE a = 1;", "DOUBLE b = 1;", "a == b");
    }

    @Test
    public void checkEqualityDoubleToDouble() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        checkRelation("DOUBLE a = 1;", "DOUBLE b = 1;", "a == b");
    }

    @Test
    public void checkEqualitySameUnit() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        checkRelation("KILO a = 1;", "KILO b = 1;", " a == b");
    }

    @Test
    public void checkEqualityConvertedUnit() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        checkRelation("KILO a = 1000;", "MEGA b = 1;", " a == b");
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void checkEqualityUnitToNumber() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        checkRelation("KILO a = 1000;", "DOUBLE b = 1;", " a == b");
    }

    //LESS
    @Test
    public void checkLessNumberToNumber() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        checkRelation("DOUBLE a = 1;", "DOUBLE b = 2;", " a < b");
    }

    @Test
    public void checkLessSameUnit() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        checkRelation("KILO a = 1;", "KILO b = 2;", "a < b");
    }

    @Test
    public void checkLessConvertedUnit() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        checkRelation("KILO a = 100;", "MEGA b = 1;", " a < b");
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void checkLessUnitToNumber() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        checkRelation("KILO a = 1000;", "DOUBLE b = 1;", "a < b");
    }

    //LESS OR EQUAL
    @Test
    public void checkLessOrEqualNumberToNumber() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        checkRelation("DOUBLE a = 1;", "DOUBLE b = 2;", " a <= b");
    }

    @Test
    public void checkLessOrEqualSameUnit() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        checkRelation("KILO a = 1;", "KILO b = 2;", "a <= b");
    }

    @Test
    public void checkLessOrEqualConvertedUnit() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        checkRelation("KILO a = 100;", "MEGA b = 1;", " a <= b");
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void checkLessOrEqualUnitToNumber() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        checkRelation("KILO a = 1000;", "DOUBLE b = 1;", "a <= b");
    }

    //GREATER
    @Test
    public void checkGreaterNumberToNumber() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        checkRelation("DOUBLE a = 2;", "DOUBLE b = 1;", " a > b");
    }

    @Test
    public void checkGreaterSameUnit() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        checkRelation("KILO a = 2;", "KILO b = 1;", "a > b");
    }

    @Test
    public void checkGreaterConvertedUnit() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        checkRelation("KILO a = 10000;", "MEGA b = 1;", " a > b");
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void checkGreaterUnitToNumber() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        checkRelation("KILO a = 1000;", "DOUBLE b = 1;", "a > b");
    }

    //GREATER OR EQUAL
    @Test
    public void checkGreaterOrEqualNumberToNumber() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        checkRelation("DOUBLE a = 2;", "DOUBLE b = 1;", " a >= b");
    }

    @Test
    public void checkGreaterOrEqualSameUnit() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        checkRelation("KILO a = 2;", "KILO b = 1;", "a >= b");
    }

    @Test
    public void checkGreaterOrEqualConvertedUnit() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        checkRelation("KILO a = 10000;", "MEGA b = 1;", " a >= b");
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void checkGreaterOrEqualUnitToNumber() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        checkRelation("KILO a = 1000;", "DOUBLE b = 1;", "a >= b");
    }

    //AND
    @Test
    public void checkAndConditionTrue() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        Value result = evaluateCondition("1 && 1");
        assertTrueConditionResult(result);
    }

    @Test
    public void checkAndConditionFalse() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        Value result = evaluateCondition("1 && 0");
        assertFalseConditionResult(result);
    }

    @Test
    public void checkAndMultiple() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        Value result = evaluateCondition("1 && 0 && 1");
        assertFalseConditionResult(result);

        result = evaluateCondition("1 && 1 && 1");
        assertTrueConditionResult(result);
    }

    //OR
    @Test
    public void checkOrConditionTrue() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        Value result = evaluateCondition("1 || 0");
        assertTrueConditionResult(result);

        result = evaluateCondition("0 || 1");
        assertTrueConditionResult(result);

        result = evaluateCondition("1 || 1");
        assertTrueConditionResult(result);
    }

    @Test
    public void checkOrConditionFalse() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        Value result = evaluateCondition("0 || 0");
        assertFalseConditionResult(result);
    }

    @Test
    public void checkOrMultiple() throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        Value result = evaluateCondition("0 || 0 || 0");
        assertFalseConditionResult(result);

        result = evaluateCondition("0 || 1 || 1");
        assertTrueConditionResult(result);
    }


    //PRIVATES
    private void checkRelation(String firstOperandInit, String secondOperandInit, String condition) throws RuntimeEnvironmentException, IOException, InvalidTokenException, UnexpectedTokenException {
        prepareEnvironment();
        executeStatement(firstOperandInit);
        executeStatement(secondOperandInit);

        Value result = evaluateCondition(condition);
        assertTrueConditionResult(result);
    }

    private void assertTrueConditionResult(Value value) {
        assertEquals(new NumberNode(1), value);
    }

    private void assertFalseConditionResult(Value value) {
        assertEquals(new NumberNode(0), value);
    }

    private Condition parseCondition(String conditionText) throws UnexpectedTokenException, InvalidTokenException, IOException {
        initializeParser(conditionText);
        return parser.parseCondition();
    }

    private Value evaluateCondition(String conditionText) throws UnexpectedTokenException, InvalidTokenException, IOException, RuntimeEnvironmentException {
        initializeParser(conditionText);
        Condition condition = parser.parseCondition();
        return condition.evaluate(environment);
    }
}
