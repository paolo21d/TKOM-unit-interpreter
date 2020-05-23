package tkom.ast.function;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tkom.ast.Node;
import tkom.ast.Signature;
import tkom.ast.Value;
import tkom.ast.expression.Expression;
import tkom.ast.statement.Block;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.execution.Environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunctionDef extends Signature implements Node {
    private List<Signature> parameters = new ArrayList<>();
    private Block block;

    private Value returnedValue = null;

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

    public Value execute(Environment environment, List<Expression> arguments) throws RuntimeEnvironmentException {
        checkParameters();
//        checkArguments();
        environment.createNewLocalScope();
        return null;
    }

    private void checkParameters() throws RuntimeEnvironmentException {
        Set<Signature> duplicates = parameters.stream()
                .filter(parameter -> Collections.frequency(parameters, parameter.getIdentifier()) > 1)
                .collect(Collectors.toSet());
        if (duplicates.size() != 0) {
            throw new RuntimeEnvironmentException("Redefinition of parameter " + duplicates.iterator().next().getIdentifier()
                    + "in function " + this.identifier);
        }
    }
}
