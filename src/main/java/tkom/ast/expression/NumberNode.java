package tkom.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Data;
import tkom.ast.Node;

@Data
@AllArgsConstructor
public class NumberNode implements Expression, Node {
    private double value;
}
