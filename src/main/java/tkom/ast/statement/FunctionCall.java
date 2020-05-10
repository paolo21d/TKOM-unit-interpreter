package tkom.ast.statement;

import lombok.Data;
import lombok.NonNull;
import tkom.ast.Node;
import tkom.ast.expression.Expression;
import tkom.ast.expression.ExpressionNode;

import java.util.ArrayList;
import java.util.List;

@Data
public class FunctionCall implements Node, Statement, Expression {
    @NonNull
    private String identifier;
    List<ExpressionNode> arguments = new ArrayList<>(); //TODO moze to powinien być ExpressionNode

    public void addArgument(ExpressionNode expression) {
        arguments.add(expression);
    }
}
