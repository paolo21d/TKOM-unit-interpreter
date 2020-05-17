package tkom.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Data;
import tkom.ast.Node;

@Data
@AllArgsConstructor
public class Unit implements Expression, Node {
    private Double value;
    private String unitType;
}
