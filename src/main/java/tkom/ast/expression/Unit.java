package tkom.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tkom.ast.ArithmeticValue;
import tkom.ast.Node;
import tkom.ast.Value;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.execution.Environment;

@Getter
@AllArgsConstructor
public class Unit implements Expression, ArithmeticValue, Node {
    private double value;
    private String unitType;
    private double ratio;

    public double getValueInBase() {
        return value * ratio;
    }

    @Override
    public Value evaluate(Environment environment) throws RuntimeEnvironmentException {
        return this;
    }

    @Override
    public ArithmeticValue add(Value secondOperand) throws RuntimeEnvironmentException {
        if (secondOperand instanceof Unit) {
            double resultValue = (getValueInBase() + ((Unit) secondOperand).getValueInBase()) / ((Unit) secondOperand).getRatio();
            return new Unit(resultValue, ((Unit) secondOperand).getUnitType(), ((Unit) secondOperand).getRatio());
        } else {
            throw new RuntimeEnvironmentException("Cannot add to Unit");
        }
    }

    @Override
    public ArithmeticValue subtract(Value secondOperand) throws RuntimeEnvironmentException {
        if (secondOperand instanceof Unit) {
            double resultValue = (getValueInBase() - ((Unit) secondOperand).getValueInBase()) / ((Unit) secondOperand).getRatio();
            return new Unit(resultValue, ((Unit) secondOperand).getUnitType(), ((Unit) secondOperand).getRatio());
        } else {
            throw new RuntimeEnvironmentException("Cannot substract from Unit");
        }
    }

    @Override
    public ArithmeticValue multiply(Value secondOperand) throws RuntimeEnvironmentException {
        if (secondOperand instanceof Unit) {
            double resultValue = (getValueInBase() * ((Unit) secondOperand).getValueInBase()) / ((Unit) secondOperand).getRatio();
            return new Unit(resultValue, ((Unit) secondOperand).getUnitType(), ((Unit) secondOperand).getRatio());
        } else {
            throw new RuntimeEnvironmentException("Cannot multiply Unit");
        }
    }

    @Override
    public ArithmeticValue divide(Value secondOperand) throws RuntimeEnvironmentException {
        if (secondOperand instanceof Unit) {
            double resultValue = (getValueInBase() / ((Unit) secondOperand).getValueInBase()) / ((Unit) secondOperand).getRatio();
            return new Unit(resultValue, ((Unit) secondOperand).getUnitType(), ((Unit) secondOperand).getRatio());
        } else {
            throw new RuntimeEnvironmentException("Cannot divide Unit");
        }
    }

    @Override
    public ArithmeticValue isLess(Value secondOperand) throws RuntimeEnvironmentException {
        if (secondOperand instanceof Unit) {
            double firstOperandValue = getValueInBase();
            double secondOperandValue = ((Unit) secondOperand).getValueInBase();
            double comparisonResult = firstOperandValue < secondOperandValue ? 1 : 0;
            return new NumberNode(comparisonResult);
        } else {
            throw new RuntimeEnvironmentException("Cannot compare unit and not unit type");
        }
    }

    @Override
    public ArithmeticValue isLessOrEqual(Value secondOperand) throws RuntimeEnvironmentException {
        if (secondOperand instanceof Unit) {
            double firstOperandValue = getValueInBase();
            double secondOperandValue = ((Unit) secondOperand).getValueInBase();
            double comparisonResult = firstOperandValue <= secondOperandValue ? 1 : 0;
            return new NumberNode(comparisonResult);
        } else {
            throw new RuntimeEnvironmentException("Cannot compare unit and not unit type");
        }
    }

    @Override
    public ArithmeticValue isGreater(Value secondOperand) throws RuntimeEnvironmentException {
        if (secondOperand instanceof Unit) {
            double firstOperandValue = getValueInBase();
            double secondOperandValue = ((Unit) secondOperand).getValueInBase();
            double comparisonResult = firstOperandValue > secondOperandValue ? 1 : 0;
            return new NumberNode(comparisonResult);
        } else {
            throw new RuntimeEnvironmentException("Cannot compare unit and not unit type");
        }
    }

    @Override
    public ArithmeticValue isGreaterOrEqual(Value secondOperand) throws RuntimeEnvironmentException {
        if (secondOperand instanceof Unit) {
            double firstOperandValue = getValueInBase();
            double secondOperandValue = ((Unit) secondOperand).getValueInBase();
            double comparisonResult = firstOperandValue >= secondOperandValue ? 1 : 0;
            return new NumberNode(comparisonResult);
        } else {
            throw new RuntimeEnvironmentException("Cannot compare unit and not unit type");
        }
    }

    @Override
    public ArithmeticValue isEqual(Value secondOperand) throws RuntimeEnvironmentException {
        if (secondOperand instanceof Unit) {
            double firstOperandValue = getValueInBase();
            double secondOperandValue = ((Unit) secondOperand).getValueInBase();
            double comparisonResult = firstOperandValue == secondOperandValue ? 1 : 0;
            return new NumberNode(comparisonResult);
        } else {
            throw new RuntimeEnvironmentException("Cannot compare unit and not unit type");
        }
    }

    @Override
    public String toString() {
        return "UNIT: " + value + " " + unitType;
    }
}
