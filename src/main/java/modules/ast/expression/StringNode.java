package modules.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Data;
import modules.ast.Node;
import modules.ast.NodeType;

@Data
@AllArgsConstructor
public class StringNode implements Expression, Node {
    private String value;

    @Override
    public NodeType getNodeType() {
        return NodeType.STRING;
    }
}
