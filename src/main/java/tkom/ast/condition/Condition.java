package tkom.ast.condition;

import lombok.Data;
import tkom.ast.ArithmeticValue;
import tkom.ast.Node;
import tkom.ast.Value;
import tkom.ast.expression.Expression;
import tkom.ast.expression.NumberNode;
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
        Value firstOperand = operands.get(0).evaluate(environment);

        for (int i = 1; i < operands.size(); i++) {
            Value secondOperand = operands.get(i).evaluate(environment);
            firstOperand = executeOperation(operator, firstOperand, secondOperand);
        }

        if (negated) {
            firstOperand = negation(firstOperand);
        }
        return firstOperand;
    }

    public boolean checkIfTrue(Environment environment) throws RuntimeEnvironmentException {
        Value value = this.evaluate(environment);
        if (value instanceof NumberNode) {
            return ((NumberNode) value).getValue() != 0.0;
        } else {
            throw new RuntimeEnvironmentException("Cannot evaluate");
        }
    }

    private Value executeOperation(TokenType operation, Value firstOperand, Value secondOperand) throws RuntimeEnvironmentException {
        switch (operation) {
            case Equality:
                return firstOperand.isEqual(secondOperand);
            case Inequality:
                return negation(firstOperand.isEqual(secondOperand));
            case Less:
                return ((ArithmeticValue) firstOperand).isLess(secondOperand);
            case LessOrEqual:
                return ((ArithmeticValue) firstOperand).isLessOrEqual(secondOperand);
            case Greater:
                return ((ArithmeticValue) firstOperand).isGreater(secondOperand);
            case GreaterOrEqual:
                return ((ArithmeticValue) firstOperand).isGreaterOrEqual(secondOperand);
            case And:
                return and(firstOperand, secondOperand);
            case Or:
                return or(firstOperand, secondOperand);
        }
        return null;
    }

    private Value negation(Value value) throws RuntimeEnvironmentException {
        if (value instanceof NumberNode) {
            if (((NumberNode) value).getValue() == 0.0) {
                return new NumberNode(1.0);
            } else {
                return new NumberNode(0.0);
            }
        } else {
            throw new RuntimeEnvironmentException("Cannot negate non number");
        }
    }

    private ArithmeticValue and(Value firstOperand, Value secondOperand) throws RuntimeEnvironmentException {
        if (firstOperand instanceof NumberNode && secondOperand instanceof NumberNode) {
            double result = ((NumberNode) firstOperand).getValue() == 1.0 && ((NumberNode) secondOperand).getValue() == 1.0 ? 1.0 : 0.0;
            return new NumberNode(result);
        } else {
            throw new RuntimeEnvironmentException("Cannot compare objects");
        }
    }

    private ArithmeticValue or(Value firstOperand, Value secondOperand) throws RuntimeEnvironmentException {

        if (firstOperand instanceof NumberNode && secondOperand instanceof NumberNode) {
            double result = ((NumberNode) firstOperand).getValue() == 1.0 || ((NumberNode) secondOperand).getValue() == 1.0 ? 1.0 : 0.0;
            return new NumberNode(result);
        } else {
            throw new RuntimeEnvironmentException("Cannot compare objects");
        }
    }

}
