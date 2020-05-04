package modules.ast.statement;

import lombok.Getter;
import modules.ast.Node;
import modules.ast.NodeType;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Block implements Statement, Node {
    private List<Statement> statements = new ArrayList<>();

    public void addStatement(Statement statement) {
        statements.add(statement);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.BLOCK;
    }
}
