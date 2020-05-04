package modules.ast.statement;

import lombok.Data;
import lombok.NonNull;
import modules.ast.Node;
import modules.ast.NodeType;
import modules.ast.expression.Expression;

import java.util.ArrayList;
import java.util.List;

@Data
public class FunctionCall implements Node, Statement, Expression {
    @NonNull
    private String identifier;
    List<Expression> arguments = new ArrayList<>(); //TODO moze to powinien być ExpressionNode

    public void addArgument(Expression expression) {
        arguments.add(expression);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.FUNCTION_CALL;
    }
}
