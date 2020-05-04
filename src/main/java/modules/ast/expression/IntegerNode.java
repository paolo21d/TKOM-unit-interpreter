package modules.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Data;
import modules.ast.Node;
import modules.ast.NodeType;

@Data
@AllArgsConstructor
public class IntegerNode implements Expression, Node {
    private int value;

    @Override
    public NodeType getNodeType() {
        return NodeType.INTEGER;
    }
}
