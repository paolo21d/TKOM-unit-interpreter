package tkom.ast.statement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tkom.ast.Node;
import tkom.ast.Signature;
import tkom.ast.Value;
import tkom.ast.expression.ExpressionNode;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.execution.Environment;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitStatement extends Signature implements Statement, Node {
    private ExpressionNode assignable;

    @Override
    public ExecuteOut execute(Environment environment) throws RuntimeEnvironmentException {
        if(assignable == null) {
            environment.addVariable();
        }
    }

    private Value getDefaultValue(Environment environment) {
        if(type.equals())
    }
}
