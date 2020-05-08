package tkom.ast.statement;

import lombok.Data;
import tkom.ast.Node;
import tkom.ast.condition.Condition;

@Data
public class IfStatement implements Statement, Node {
    private Condition condition;
    private Statement trueStatement;
    private Statement falseStatement;
}
