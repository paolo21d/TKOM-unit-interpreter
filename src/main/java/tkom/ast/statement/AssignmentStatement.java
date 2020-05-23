package tkom.ast.statement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tkom.ast.Node;
import tkom.ast.Value;
import tkom.ast.expression.Expression;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.execution.Environment;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentStatement implements Statement, Node {
    private String identifier;
    private Expression assignable;

    @Override
    public ExecuteOut execute(Environment environment) throws RuntimeEnvironmentException {
        Value variableValue = environment.getVariableValue(identifier);
        Value assignValue = assignable.evaluate(environment);

        //TODO trzeba tutaj zrobić environemt.setrVariable

        return null;
    }
}
