package tkom.ast.statement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tkom.ast.Node;
import tkom.ast.NodeType;
import tkom.ast.expression.Expression;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnStatement implements Statement, Node {
    private Expression returnedExpression;

    @Override
    public NodeType getNodeType() {
        return NodeType.RETURN_STATEMENT;
    }
}