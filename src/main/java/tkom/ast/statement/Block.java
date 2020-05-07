package tkom.ast.statement;

import lombok.Getter;
import tkom.ast.Node;
import tkom.ast.NodeType;

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
