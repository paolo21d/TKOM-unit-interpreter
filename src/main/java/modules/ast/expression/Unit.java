package modules.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Data;
import modules.ast.Node;
import modules.ast.NodeType;

@Data
@AllArgsConstructor
public class Unit implements Expression, Node {
    private Double value;
    private String unitType;

    @Override
    public NodeType getNodeType() {
        return NodeType.UNIT;
    }
}
