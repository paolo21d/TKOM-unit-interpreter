package modules.ast;

import lombok.Getter;
import modules.ast.function.FunctionDef;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Program implements Node {
    private List<FunctionDef> functions = new ArrayList<>();

    public void addFunction(FunctionDef funcDef) {
        functions.add(funcDef);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.PROGRAM;
    }
}
