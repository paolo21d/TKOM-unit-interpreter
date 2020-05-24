package tkom.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Data;
import tkom.ast.Node;
import tkom.ast.Value;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.execution.Environment;

@Data
@AllArgsConstructor
public class Variable implements Expression, Node {
    private String identifier;

    @Override
    public Value evaluate(Environment environment) throws RuntimeEnvironmentException {
        return environment.getVariableValue(identifier);
    }
}
