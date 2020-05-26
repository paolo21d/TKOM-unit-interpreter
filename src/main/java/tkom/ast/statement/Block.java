package tkom.ast.statement;

import lombok.Getter;
import tkom.ast.Node;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.execution.Environment;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Block implements Statement, Node {
    private List<Statement> statements = new ArrayList<>();

    public void addStatement(Statement statement) {
        statements.add(statement);
    }

    @Override
    public ExecuteOut execute(Environment environment) throws RuntimeEnvironmentException {
        for (Statement statement : statements) {
            if (statement instanceof ReturnStatement) {
                return statement.execute(environment);
            }

            ExecuteOut out = statement.execute(environment);
            if (out.isReturnCall()) {
                return out;
            }
        }
        return new ExecuteOut(ExecuteOut.ExecuteStatus.NORMAL);
    }
}
