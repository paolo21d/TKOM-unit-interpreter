package tkom.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Data;
import tkom.ast.Node;
import tkom.ast.NodeType;

@Data
@AllArgsConstructor
public class Variable implements Expression, Node {
    private String identifier;

    @Override
    public NodeType getNodeType() {
        return NodeType.VARIABLE;
    }
}
