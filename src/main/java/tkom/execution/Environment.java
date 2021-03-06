package tkom.execution;

import lombok.Getter;
import tkom.ast.Value;
import tkom.ast.expression.Unit;
import tkom.ast.function.FunctionDef;
import tkom.data.UnitRatio;
import tkom.errorHandler.RuntimeEnvironmentException;

import java.util.*;

@Getter
public class Environment {

    private final Map<String, FunctionDef> functions;
    private final UnitRatio unitRatio;
    Stack<FunctionDef> functionCallingStack = new Stack<>();
    private Stack<Scope> scopeStack = new Stack<>();

    public Environment(List<FunctionDef> functions, UnitRatio unitRatio) throws RuntimeEnvironmentException {
        this.functions = new HashMap<>();
        this.unitRatio = unitRatio;

        for (FunctionDef function : functions) {
            if (this.functions.containsKey(function.getIdentifier())) {
                throw new RuntimeEnvironmentException("Multiple function definition with the same identifier: " + function.getIdentifier());
            }
            this.functions.put(function.getIdentifier(), function);
        }
    }

    public FunctionDef getFunction(String identifier) throws RuntimeEnvironmentException {
        FunctionDef function = functions.get(identifier);
        if (function == null) {
            throw new RuntimeEnvironmentException("Undefined function call: " + identifier);
        }
        return function;
    }

    public void addFunction(FunctionDef function) {
        functions.put(function.getIdentifier(), function);
    }

    public void createNewLocalScope() {
        Scope parentScope;
        try {
            parentScope = scopeStack.peek();
        } catch (EmptyStackException e) {
            parentScope = null;
        }

        Scope localScope = new Scope(parentScope);
        scopeStack.push(localScope);
    }

    public void destroyScope() {
        scopeStack.pop();
    }

    public void addVariable(String identifier) throws RuntimeEnvironmentException {
        scopeStack.peek().addNewVariable(identifier);
    }

    public void addVariable(String identifier, Value value) throws RuntimeEnvironmentException {
        Scope scope = scopeStack.peek();
        scope.addNewVariable(identifier);
        scope.assignVariableValue(identifier, value);
    }

    public Value getVariableValue(String identifier) throws RuntimeEnvironmentException {
        Scope scope = scopeStack.peek();
        if (scope.isVariableDefined(identifier)) {
            return scope.getVariableValue(identifier);
        }

        while (scope.getParentScope() != null) {
            scope = scope.getParentScope();
            if (scope.isVariableDefined(identifier)) {
                return scope.getVariableValue(identifier);
            }
        }

        throw new RuntimeEnvironmentException("Undefined reference to variable " + identifier);
    }

    public void setVariableValue(String identifier, Value value) throws RuntimeEnvironmentException {
        Scope scope = scopeStack.peek();

        if (scope.isVariableDefined(identifier)) {
            scope.assignVariableValue(identifier, value);
            return;
        }

        while (scope.getParentScope() != null) {
            scope = scope.getParentScope();
            if (scope.isVariableDefined(identifier)) {
                scope.assignVariableValue(identifier, value);
                return;
            }
        }

        throw new RuntimeEnvironmentException("Undefined reference to variable " + identifier);
    }

    public boolean isUnitDefined(String identifier) {
        return unitRatio.isUnitDefined(identifier);
    }

    public Unit castUnitType(String originalUnitType, String resultUnitType, double originalUnitValue) throws RuntimeEnvironmentException {
        double resultValue = unitRatio.castUnits(originalUnitType, resultUnitType, originalUnitValue);
        return new Unit(resultValue, resultUnitType, unitRatio.getUnitValue(resultUnitType));
    }
}
