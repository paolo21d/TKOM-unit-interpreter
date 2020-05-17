package tkom.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Data;
import tkom.ast.Node;

@Data
@AllArgsConstructor
public class Variable implements Expression, Node {
    private String identifier;
}
