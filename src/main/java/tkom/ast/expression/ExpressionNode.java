package tkom.ast.expression;

import lombok.Data;
import tkom.ast.Node;
import tkom.ast.NodeType;
import tkom.data.TokenType;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExpressionNode implements Expression, Node {
    private List<TokenType> operators = new ArrayList<>();
    private List<Expression> operands = new ArrayList<>();

    public void addOperators(TokenType operationType) {
        operators.add(operationType);
    }

    public void addOperand(Expression operand) {
        operands.add(operand);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.EXPRESSION;
    }
}
