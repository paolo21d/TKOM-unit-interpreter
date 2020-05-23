package tkom.ast.statement;

import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.execution.Environment;

public interface Statement {
    ExecuteOut execute(Environment environment) throws RuntimeEnvironmentException;
}
