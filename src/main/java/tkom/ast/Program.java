package tkom.ast;

import lombok.Getter;
import tkom.ast.function.FunctionDef;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Program implements Node {
    private List<FunctionDef> functions = new ArrayList<>();

    public void addFunction(FunctionDef funcDef) {
        functions.add(funcDef);
    }
}
