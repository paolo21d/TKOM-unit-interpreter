package tkom.ast.statement;

import lombok.Data;
import tkom.ast.Node;
import tkom.ast.condition.Condition;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.execution.Environment;

@Data
public class IfStatement implements Statement, Node {
    private Condition condition;
    private Statement trueStatement;
    private Statement falseStatement;

    @Override
    public ExecuteOut execute(Environment environment) throws RuntimeEnvironmentException {
        environment.createNewLocalScope();
        ExecuteOut result = executeIfStatement(environment);
        environment.destroyScope();
        return result;
    }

    private ExecuteOut executeIfStatement(Environment environment) throws RuntimeEnvironmentException {
        if (condition.checkIfTrue(environment)) {
            trueStatement.execute(environment);
        } else if (falseStatement != null) {
            falseStatement.execute(environment);
        }

        return new ExecuteOut(ExecuteOut.ExecuteStatus.NORMAL);
    }
}
