package tkom.ast;

import tkom.ast.expression.Expression;
import tkom.ast.expression.NumberNode;
import tkom.ast.expression.Unit;

public interface Value {
    boolean isInitialized();

    static boolean isNumber(Expression value) {
        return value instanceof NumberNode;
    }

    static boolean isUnit(Expression value) {
        return value instanceof Unit;
    }

    static double getNumberValue(Value value) {
        return
    }
}
