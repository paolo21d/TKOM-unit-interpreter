package tkom.ast.statement;

import lombok.Data;
import tkom.ast.Node;
import tkom.ast.NodeType;
import tkom.ast.condition.Condition;

@Data
public class WhileStatement implements Statement, Node {
    private Condition condition;
    private Statement loopBlock;

    @Override
    public NodeType getNodeType() {
        return NodeType.WHILE_STATEMENT;
    }
}
