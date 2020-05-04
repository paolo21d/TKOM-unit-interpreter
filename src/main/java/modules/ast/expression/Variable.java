package modules.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Data;
import modules.ast.Node;
import modules.ast.NodeType;

@Data
@AllArgsConstructor
public class Variable implements Expression, Node {
    private String identifier;

    @Override
    public NodeType getNodeType() {
        return NodeType.VARIABLE;
    }
}
