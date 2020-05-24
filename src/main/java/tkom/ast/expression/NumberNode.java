package tkom.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Data;
import tkom.ast.ArithmeticValue;
import tkom.ast.Node;
import tkom.ast.Value;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.execution.Environment;

@Data
@AllArgsConstructor
public class NumberNode implements Expression, ArithmeticValue, Node {
    private double value;

    @Override
    public Value evaluate(Environment environment) throws RuntimeEnvironmentException {
        return this;
    }

    @Override
    public ArithmeticValue add(Value secondOperand) throws RuntimeEnvironmentException {
        if (secondOperand instanceof NumberNode) {
            return new NumberNode(value + ((NumberNode) secondOperand).value);
        } else {
            throw new RuntimeEnvironmentException("Cannot add to Number");
        }
    }

    @Override
    public ArithmeticValue subtract(Value secondOperand) throws RuntimeEnvironmentException {
        if (secondOperand instanceof NumberNode) {
            return new NumberNode(value - ((NumberNode) secondOperand).value);
        } else {
            throw new RuntimeEnvironmentException("Cannot substract from Number");
        }
    }

    @Override
    public ArithmeticValue multiply(Value secondOperand) throws RuntimeEnvironmentException {
        if (secondOperand instanceof NumberNode) {
            return new NumberNode(value * ((NumberNode) secondOperand).value);
        } else {
            throw new RuntimeEnvironmentException("Cannot multiply Number");
        }
    }

    @Override
    public ArithmeticValue divide(Value secondOperand) throws RuntimeEnvironmentException {
        if (secondOperand instanceof NumberNode) {
            return new NumberNode(value / ((NumberNode) secondOperand).value);
        } else {
            throw new RuntimeEnvironmentException("Cannot divide Number");
        }
    }

    @Override
    public ArithmeticValue isLess(Value secondOperand) throws RuntimeEnvironmentException {
        if (secondOperand instanceof NumberNode) {
            double resultValue = getValue() < ((NumberNode) secondOperand).getValue() ? 1 : 0;
            return new NumberNode(resultValue);
        } else {
            throw new RuntimeEnvironmentException("Cannot compare number and nonnumber type");
        }
    }

    @Override
    public ArithmeticValue isLessOrEqual(Value secondOperand) throws RuntimeEnvironmentException {
        if (secondOperand instanceof NumberNode) {
            double resultValue = getValue() <= ((NumberNode) secondOperand).getValue() ? 1 : 0;
            return new NumberNode(resultValue);
        } else {
            throw new RuntimeEnvironmentException("Cannot compare number and nonnumber type");
        }
    }

    @Override
    public ArithmeticValue isGreater(Value secondOperand) throws RuntimeEnvironmentException {
        if (secondOperand instanceof NumberNode) {
            double resultValue = getValue() > ((NumberNode) secondOperand).getValue() ? 1 : 0;
            return new NumberNode(resultValue);
        } else {
            throw new RuntimeEnvironmentException("Cannot compare number and nonnumber type");
        }
    }

    @Override
    public ArithmeticValue isGreaterOrEqual(Value secondOperand) throws RuntimeEnvironmentException {
        if (secondOperand instanceof NumberNode) {
            double resultValue = getValue() >= ((NumberNode) secondOperand).getValue() ? 1 : 0;
            return new NumberNode(resultValue);
        } else {
            throw new RuntimeEnvironmentException("Cannot compare number and nonnumber type");
        }
    }

    @Override
    public ArithmeticValue isEqual(Value secondOperand) throws RuntimeEnvironmentException {
        if (secondOperand instanceof NumberNode) {
            double resultValue = getValue() == ((NumberNode) secondOperand).getValue() ? 1 : 0;
            return new NumberNode(resultValue);
        } else {
            throw new RuntimeEnvironmentException("Cannot compare number and nonnumber type");
        }
    }

    @Override
    public String toString() {
        return "NUMBER: " + value;
    }

}
