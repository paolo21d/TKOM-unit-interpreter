package modules.ast.statement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import modules.ast.Node;
import modules.ast.NodeType;
import modules.ast.expression.Expression;

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
