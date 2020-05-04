package modules.ast.statement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import modules.ast.Node;
import modules.ast.NodeType;
import modules.ast.Signature;
import modules.ast.expression.ExpressionNode;

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
