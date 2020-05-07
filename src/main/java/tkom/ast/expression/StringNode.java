package tkom.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Data;
import tkom.ast.Node;
import tkom.ast.NodeType;

@Data
@AllArgsConstructor
public class StringNode implements Expression, Node {
    private String value;

    @Override
    public NodeType getNodeType() {
        return NodeType.STRING;
    }
}
