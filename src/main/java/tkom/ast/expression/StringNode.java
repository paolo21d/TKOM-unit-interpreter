package tkom.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Data;
import tkom.ast.ArithmeticValue;
import tkom.ast.Node;
import tkom.ast.Value;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.execution.Environment;

@Data
@AllArgsConstructor
public class StringNode implements Expression, Value, Node {
    private String value;

    @Override
    public Value evaluate(Environment environment) throws RuntimeEnvironmentException {
        return this;
    }

    @Override
    public ArithmeticValue isEqual(Value secondOperand) throws RuntimeEnvironmentException {
        if (secondOperand instanceof StringNode) {
            double resultValue = value.equals(((StringNode) secondOperand).getValue()) ? 1 : 0;
            return new NumberNode(resultValue);
        } else {
            throw new RuntimeEnvironmentException("Cannot compare to StringNode");
        }
    }

    @Override
    public String toString() {
        return "String: " + value;
    }
}
