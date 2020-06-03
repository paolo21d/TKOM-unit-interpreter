package tkom.execution;

import org.junit.Test;
import tkom.ast.Signature;
import tkom.ast.expression.NumberNode;
import tkom.ast.expression.Unit;
import tkom.ast.function.FunctionDef;
import tkom.data.UnitRatio;
import tkom.errorHandler.RuntimeEnvironmentException;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class EnvironmentTest {

    @Test
    public void checkGetFunction() throws RuntimeEnvironmentException {
        FunctionDef functionDef = new FunctionDef();
        functionDef.setSignature(new Signature("DOUBLE", "func"));
        Environment environment = new Environment(Arrays.asList(functionDef), new UnitRatio());
        assertEquals(functionDef, environment.getFunction("func"));
    }

    @Test(expected = RuntimeEnvironmentException.class)
    public void checkGetFunctionException() throws RuntimeEnvironmentException {
        Environment environment = prepareEnvironment();
        environment.getFunction("other_func");
    }

    @Test
    public void checkGetVariableValue() throws RuntimeEnvironmentException {
        Environment environment = prepareEnvironment();
        environment.createNewLocalScope();
        environment.addVariable("var1", new NumberNode(1));
        assertEquals(new NumberNode(1), environment.getVariableValue("var1"));

        environment.createNewLocalScope();
        environment.createNewLocalScope();
        assertEquals(new NumberNode(1), environment.getVariableValue("var1"));
    }

    @Test
    public void checkSetVariableValue() throws RuntimeEnvironmentException {
        Environment environment = prepareEnvironment();
        environment.createNewLocalScope();
        environment.addVariable("var1", new NumberNode(1));

        environment.setVariableValue("var1", new NumberNode(2));
        assertEquals(new NumberNode(2), environment.getVariableValue("var1"));
    }

    @Test
    public void checkIsUnitDefined() throws RuntimeEnvironmentException {
        Environment environment = prepareEmptyEnvironment();

        assertTrue(environment.isUnitDefined("KILO"));
        assertTrue(environment.isUnitDefined("MEGA"));
        assertFalse(environment.isUnitDefined("UNDEFINED_UNIT"));
    }

    @Test
    public void checkCastUnitType() throws RuntimeEnvironmentException {
        Environment environment = prepareEmptyEnvironment();
        Unit originalUnit = new Unit(1000, "KILO", environment.getUnitRatio().getUnitValue("KILO"));
        Unit castedUnit = environment.castUnitType(originalUnit.getUnitType(), "MEGA", originalUnit.getValue());

        assertEquals("MEGA", castedUnit.getUnitType());
        assertEquals(Double.valueOf(1.0), Double.valueOf(castedUnit.getValue()));
        assertEquals(Double.valueOf(1000000.0), Double.valueOf(castedUnit.getValueInBase()));
    }


    /*    private Environment prepareEnvironment(List<FunctionDef> functions, UnitRatio unitRatio) {

        }*/
    private Environment prepareEmptyEnvironment() throws RuntimeEnvironmentException {
        return new Environment(new ArrayList<>(), new UnitRatio());
    }

    private Environment prepareEnvironment() throws RuntimeEnvironmentException {
        FunctionDef functionDef = new FunctionDef();
        functionDef.setSignature(new Signature("DOUBLE", "func"));
        return new Environment(Arrays.asList(functionDef), new UnitRatio());
    }
}
