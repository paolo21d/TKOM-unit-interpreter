package tkom.ast;

import tkom.ast.expression.Expression;
import tkom.ast.expression.NumberNode;
import tkom.ast.expression.Unit;
import tkom.errorHandler.RuntimeEnvironmentException;

public interface Value {

    ArithmeticValue isEqual(Value secondOperand) throws RuntimeEnvironmentException;

    static boolean isNumber(Expression value) {
        return value instanceof NumberNode;
    }

    static boolean isUnit(Expression value) {
        return value instanceof Unit;
    }

    static double getNumberValue(Value value) {
        return ((NumberNode) value).getValue();
    }

    static double getUnitValue(Value value) {
        return ((Unit) value).getValue();
    }
}
