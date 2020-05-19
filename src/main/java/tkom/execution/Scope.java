package tkom.execution;

import lombok.Getter;
import lombok.Setter;
import tkom.ast.Value;
import tkom.errorHandler.RuntimeEnvironmentException;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Scope {
    @Setter
    Scope parentScope;
    Map<String, Value> definedVariables = new HashMap<>();

    public Scope(Scope scope) {
        parentScope = scope;
    }

    public void addNewVariable(String identifier) throws RuntimeEnvironmentException {
        if(definedVariables.containsKey(identifier)) {
            throw new RuntimeEnvironmentException("Variable " + identifier + " is already defined.");
        }
        definedVariables.put(identifier, null);
    }

    public void assignVariableValue(String identifier, Value value) throws RuntimeEnvironmentException {
        if (!definedVariables.containsKey(identifier)) {
            throw new RuntimeEnvironmentException("Undefined Reference to:" + identifier);
        }
        definedVariables.put(identifier, value);
    }

    public Value getVariableValue(String identifier) {
        return definedVariables.get(identifier);
    }

    public boolean isVariableDefined(String identifier) {
        return definedVariables.containsKey(identifier);
    }
}
