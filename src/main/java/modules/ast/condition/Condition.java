package modules.ast.condition;

import lombok.Data;
import modules.ast.Node;
import modules.ast.NodeType;
import modules.ast.expression.Expression;
import modules.data.TokenType;

import java.util.ArrayList;
import java.util.List;

@Data
public class Condition implements Node, Expression {
    Boolean negated = false;
    private TokenType operator;
    private List<Expression> operands = new ArrayList<>();

    public void addOperand(Expression operand) {
        operands.add(operand);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.CONDITION;
    }
}
