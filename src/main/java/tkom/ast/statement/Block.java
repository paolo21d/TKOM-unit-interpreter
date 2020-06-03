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
        environment.createNewLocalScope();
        ExecuteOut blockOut = new ExecuteOut(ExecuteOut.ExecuteStatus.NORMAL);
        for (Statement statement : statements) {
            if (statement instanceof ReturnStatement) {
                blockOut = statement.execute(environment);
                break;
            }

            ExecuteOut out = statement.execute(environment);
            if (out.isReturnCall()) {
                blockOut = out;
                break;
            }
        }
        environment.destroyScope();
        return blockOut;
    }
}
