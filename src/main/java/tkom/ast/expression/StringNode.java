package tkom.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Data;
import tkom.ast.Node;

@Data
@AllArgsConstructor
public class StringNode implements Expression, Node {
    private String value;
}
