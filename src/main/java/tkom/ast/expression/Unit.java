package tkom.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Data;
import tkom.ast.Node;
import tkom.ast.NodeType;

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
