package tkom.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Data;
import tkom.ast.Node;
import tkom.ast.NodeType;

@Data
@AllArgsConstructor
public class IntegerNode implements Expression, Node {
    private int value;

    @Override
    public NodeType getNodeType() {
        return NodeType.INTEGER;
    }
}
