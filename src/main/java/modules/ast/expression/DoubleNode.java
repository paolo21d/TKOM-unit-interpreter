package modules.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Data;
import modules.ast.Node;
import modules.ast.NodeType;

@Data
@AllArgsConstructor
public class DoubleNode implements Expression, Node {
    private double value;

    @Override
    public NodeType getNodeType() {
        return NodeType.DOUBLE;
    }
}
