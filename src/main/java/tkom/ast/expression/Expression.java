package tkom.ast.expression;

import tkom.ast.Value;
import tkom.execution.Environment;

public interface Expression {
    Value evaluate(Environment environment) throws RuntimeException;
}
