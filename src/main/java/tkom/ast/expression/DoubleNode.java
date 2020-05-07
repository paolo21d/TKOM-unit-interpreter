package tkom.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Data;
import tkom.ast.Node;
import tkom.ast.NodeType;

@Data
@AllArgsConstructor
public class DoubleNode implements Expression, Node {
    private double value;

    @Override
    public NodeType getNodeType() {
        return NodeType.DOUBLE;
    }
}
