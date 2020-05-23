package tkom.ast.statement;

import lombok.Data;
import lombok.NonNull;
import tkom.ast.Node;
import tkom.ast.Value;
import tkom.ast.expression.Expression;
import tkom.ast.expression.ExpressionNode;
import tkom.ast.function.FunctionDef;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.execution.Environment;

import java.util.ArrayList;
import java.util.List;

@Data
public class FunctionCall implements Node, Statement, Expression {
    @NonNull
    private String identifier;
    List<Expression> arguments = new ArrayList<>();

    public void addArgument(ExpressionNode expression) {
        arguments.add(expression);
    }

    @Override
    public Value evaluate(Environment environment) throws RuntimeException, RuntimeEnvironmentException {
        FunctionDef function = environment.getFunction(identifier);
        return function.execute(environment, arguments);
    }

    @Override
    public ExecuteOut execute(Environment environment) throws RuntimeEnvironmentException {
        Value value = evaluate(environment);
        return new ExecuteOut(ExecuteOut.ExecuteStatus.NORMAL, value);
    }


}
