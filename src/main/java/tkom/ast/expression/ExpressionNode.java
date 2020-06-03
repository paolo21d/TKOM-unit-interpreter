package tkom.ast.expression;

import lombok.Data;
import tkom.ast.ArithmeticValue;
import tkom.ast.Node;
import tkom.ast.Value;
import tkom.data.TokenType;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.execution.Environment;

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
    public Value evaluate(Environment environment) throws RuntimeEnvironmentException {
        int operandIndex = 0;
        ArithmeticValue firstOperand = (ArithmeticValue) operands.get(operandIndex).evaluate(environment);

        for (TokenType operator : operators) {
            operandIndex++;
            ArithmeticValue secondOperand = (ArithmeticValue) operands.get(operandIndex).evaluate(environment);

            firstOperand = executeOperation(operator, firstOperand, secondOperand);
        }

        return firstOperand;
    }

    private ArithmeticValue executeOperation(TokenType operator, ArithmeticValue firstOperand, ArithmeticValue secondOperand) throws RuntimeEnvironmentException {
        switch (operator) {
            case Plus:
                return firstOperand.add(secondOperand);
            case Minus:
                return firstOperand.subtract(secondOperand);
            case Multiply:
                return firstOperand.multiply(secondOperand);
            case Divide:
                return firstOperand.divide(secondOperand);
        }
        return null;
    }
}
