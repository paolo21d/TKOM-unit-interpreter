package tkom.ast.condition;

import lombok.Data;
import tkom.ast.Node;
import tkom.ast.expression.Expression;
import tkom.data.TokenType;

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
}
