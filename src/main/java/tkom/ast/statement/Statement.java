package tkom.ast.statement;

import tkom.execution.Environment;

public interface Statement {
    void execute(Environment environment) throws RuntimeException;
}
