package tkom.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Data;
import tkom.ast.Node;
import tkom.ast.Value;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.execution.Environment;

@Data
@AllArgsConstructor
public class Unit implements Expression, Node {
    private Double value;
    private String unitType;

    @Override
    public Value evaluate(Environment environment) throws RuntimeEnvironmentException {
        return null;
    }
}
