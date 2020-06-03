package tkom.ast.statement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tkom.ast.Node;
import tkom.ast.expression.Expression;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.execution.Environment;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnStatement implements Statement, Node {
    private Expression returnedExpression;

    @Override
    public ExecuteOut execute(Environment environment) throws RuntimeEnvironmentException {
        return new ExecuteOut(ExecuteOut.ExecuteStatus.RETURN, returnedExpression.evaluate(environment));
    }
}
