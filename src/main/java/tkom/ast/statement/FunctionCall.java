package tkom.ast.statement;

import lombok.Data;
import lombok.NonNull;
import tkom.ast.Node;
import tkom.ast.Value;
import tkom.ast.expression.Expression;
import tkom.ast.expression.ExpressionNode;
import tkom.ast.expression.NumberNode;
import tkom.ast.expression.StringNode;
import tkom.ast.function.FunctionDef;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.execution.Environment;

import java.util.ArrayList;
import java.util.List;

@Data
public class FunctionCall implements Node, Statement, Expression {
    List<Expression> arguments = new ArrayList<>();
    @NonNull
    private String identifier;

    public void addArgument(ExpressionNode expression) {
        arguments.add(expression);
    }

    @Override
    public Value evaluate(Environment environment) throws RuntimeException, RuntimeEnvironmentException {
        if (identifier.toUpperCase().equals("PRINT")) {
            executePrintFunction(environment);
            return new NumberNode(1.0);
        }

        FunctionDef function = environment.getFunction(identifier);
        return function.execute(environment, arguments);
    }

    @Override
    public ExecuteOut execute(Environment environment) throws RuntimeEnvironmentException {
        if (identifier.toUpperCase().equals("PRINT")) {
            executePrintFunction(environment);
            return new ExecuteOut(ExecuteOut.ExecuteStatus.NORMAL);
        }

        Value value = evaluate(environment);
        return new ExecuteOut(ExecuteOut.ExecuteStatus.NORMAL, value);
    }

    void executePrintFunction(Environment environment) throws RuntimeEnvironmentException {
        if (arguments.size() != 1) {
            throw new RuntimeEnvironmentException("PRINT function require 1 parameter!");
        }

        Expression argument = arguments.get(0);
        String valueToPrint;
        if (((ExpressionNode) argument).getOperands().get(0) instanceof StringNode) {
            valueToPrint = ((StringNode) ((ExpressionNode) argument).getOperands().get(0)).getValue();
        } else {
            valueToPrint = argument.evaluate(environment).toString();
        }
        System.out.println(valueToPrint);
    }


}
