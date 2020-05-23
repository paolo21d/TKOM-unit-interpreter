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
public class NumberNode implements Expression, ArithmeticValue, Node {
    private double value;

    @Override
    public Value evaluate(Environment environment) throws  RuntimeEnvironmentException {
        return null;
    }
}
