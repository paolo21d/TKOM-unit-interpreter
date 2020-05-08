package tkom.ast.statement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tkom.ast.Node;
import tkom.ast.expression.Expression;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentStatement implements Statement, Node {
    private String identifier;
    private Expression assignable;
}
