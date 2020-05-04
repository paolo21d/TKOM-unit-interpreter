package modules.ast.statement;

import lombok.Data;
import modules.ast.Node;
import modules.ast.NodeType;
import modules.ast.condition.Condition;

@Data
public class WhileStatement implements Statement, Node {
    private Condition condition;
    private Statement loopBlock;

    @Override
    public NodeType getNodeType() {
        return NodeType.WHILE_STATEMENT;
    }
}
