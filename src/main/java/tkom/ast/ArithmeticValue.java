package tkom.ast;

import tkom.errorHandler.RuntimeEnvironmentException;

public interface ArithmeticValue extends Value {
    ArithmeticValue add(Value secondOperand) throws RuntimeEnvironmentException;

    ArithmeticValue subtract(Value secondOperand) throws RuntimeEnvironmentException;

    ArithmeticValue multiply(Value secondOperand) throws RuntimeEnvironmentException;

    ArithmeticValue divide(Value secondOperand) throws RuntimeEnvironmentException;

    ArithmeticValue isLess(Value secondOperand) throws RuntimeEnvironmentException;

    ArithmeticValue isLessOrEqual(Value secondOperand) throws RuntimeEnvironmentException;

    ArithmeticValue isGreater(Value secondOperand) throws RuntimeEnvironmentException;

    ArithmeticValue isGreaterOrEqual(Value secondOperand) throws RuntimeEnvironmentException;


}
