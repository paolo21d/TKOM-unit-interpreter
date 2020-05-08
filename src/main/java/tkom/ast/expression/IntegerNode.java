package tkom.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Data;
import tkom.ast.Node;

@Data
@AllArgsConstructor
public class IntegerNode implements Expression, Node {
    private int value;
}
