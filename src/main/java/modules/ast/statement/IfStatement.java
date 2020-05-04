package modules.ast.statement;

import lombok.Data;
import modules.ast.Node;
import modules.ast.NodeType;
import modules.ast.condition.Condition;

@Data
public class IfStatement implements Statement, Node {
    private Condition condition;
    private Statement trueStatement;
    private Statement falseStatement;

    @Override
    public NodeType getNodeType() {
        return NodeType.IF_STATEMENT;
    }
}
