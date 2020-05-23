package tkom.ast.statement;

import lombok.Data;
import tkom.ast.Node;
import tkom.ast.condition.Condition;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.execution.Environment;

@Data
public class WhileStatement implements Statement, Node {
    private Condition condition;
    private Statement loopBlock;

    @Override
    public ExecuteOut execute(Environment environment) throws RuntimeEnvironmentException {
        ExecuteOut out = new ExecuteOut(ExecuteOut.ExecuteStatus.NORMAL);

        while(condition.checkIfTrue(environment)) {
            environment.createNewLocalScope();
            out = loopBlock.execute(environment);
            if(out.isReturnCall()) {
                environment.destroyScope();
                return out;
            }
            environment.destroyScope();
        }
        return out;
    }
}
