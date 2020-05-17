package tkom.ast.function;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tkom.ast.Node;
import tkom.ast.Signature;
import tkom.ast.statement.Block;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunctionDef extends Signature implements Node {
    private List<Signature> parameters = new ArrayList<>();
    private Block block;

    public FunctionDef(String type, String identifier) {
        super(type, identifier);
    }

    public void addParameter(Signature parameter) {
        parameters.add(parameter);
    }

    /*    public void setSignature(Signature signature) {
        this.type = signature.getType();
        this.identifier = signature.getIdentifier();
    }*/
}
