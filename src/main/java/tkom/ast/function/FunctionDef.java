package tkom.ast.function;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tkom.ast.Node;
import tkom.ast.Signature;
import tkom.ast.Value;
import tkom.ast.expression.Expression;
import tkom.ast.expression.NumberNode;
import tkom.ast.expression.Unit;
import tkom.ast.statement.Block;
import tkom.ast.statement.ExecuteOut;
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
////////////////////////////////////////////////////////////////////

    public Value execute(Environment environment, List<Expression> arguments) throws RuntimeEnvironmentException {
        checkParameters();
        setUpEnvironmentForFunction(environment, arguments);
        ExecuteOut out = block.execute(environment);
        environment.destroyScope();

        if (!out.isReturnCall()) {
            throw new RuntimeEnvironmentException("Function " + identifier + "has no return statement!");
        }

        return castReturnType(environment, out.getReturnedValue());
    }

    private void setUpEnvironmentForFunction(Environment environment, List<Expression> arguments) throws RuntimeEnvironmentException {
        checkArgumentsQuantity(arguments);
        List<Value> argumentsValues = evaluateArguments(environment, arguments);
        environment.createNewLocalScope();

        for (int i = 0; i < parameters.size(); i++) {
            environment.addVariable(parameters.get(i).getIdentifier(), argumentsValues.get(i));
        }
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

    private void checkArgumentsQuantity(List<Expression> arguments) throws RuntimeEnvironmentException {
        if (parameters.size() != arguments.size()) {
            throw new RuntimeEnvironmentException("Invalid number of arguments in call function " + this.identifier);
        }
    }

    private List<Value> evaluateArguments(Environment environment, List<Expression> arguments) throws RuntimeEnvironmentException {
        List<Value> argumentsValues = new ArrayList<>();
        for (int i = 0; i < arguments.size(); i++) {
            Value value = arguments.get(i).evaluate(environment);
            validateArgument(parameters.get(i), value);
            argumentsValues.add(value);
        }
        return argumentsValues;
    }

    private void validateArgument(Signature parameter, Value argument) throws RuntimeEnvironmentException {
        if (parameter.isNumber() && argument instanceof NumberNode) {
            //valid argument
        } else if (!parameter.isNumber() && argument instanceof Unit) {
            //valid argument
        } else {
            throw new RuntimeEnvironmentException("Invalid argument type for parameter");
        }

    }

    private Value castReturnType(Environment environment, Value value) throws RuntimeEnvironmentException {
        if (isNumber() && value instanceof NumberNode) {
            return value;
        } else if (!isNumber() && value instanceof Unit) {
            return environment.castUnitType(((Unit) value).getUnitType(), getType(), ((Unit) value).getValue());
        } else {
            throw new RuntimeEnvironmentException("Cannot cast return type");
        }
    }

}
