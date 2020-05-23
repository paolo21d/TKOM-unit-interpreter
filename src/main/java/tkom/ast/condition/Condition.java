package tkom.ast.condition;

import lombok.Data;
import tkom.ast.Node;
import tkom.ast.Value;
import tkom.ast.expression.Expression;
import tkom.data.TokenType;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.execution.Environment;

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
    public Value evaluate(Environment environment) throws RuntimeException, RuntimeEnvironmentException {
        return null;
    }

    public boolean checkIfTrue(Environment environment) {
        return false;
    }

}
