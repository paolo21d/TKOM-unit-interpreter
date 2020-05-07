package tkom.ast.statement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tkom.ast.Node;
import tkom.ast.NodeType;
import tkom.ast.Signature;
import tkom.ast.expression.ExpressionNode;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitStatement extends Signature implements Statement, Node {
    private ExpressionNode assignable;

    @Override
    public NodeType getNodeType() {
        return NodeType.INIT_STATEMENT;
    }
}