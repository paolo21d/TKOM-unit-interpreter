package tkom.ast.statement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tkom.ast.Node;
import tkom.ast.Value;
import tkom.ast.expression.Expression;
import tkom.ast.expression.NumberNode;
import tkom.ast.expression.Unit;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.execution.Environment;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentStatement implements Statement, Node {
    private String identifier;
    private Expression assignable;

    @Override
    public ExecuteOut execute(Environment environment) throws RuntimeEnvironmentException {
        Value variableValue = environment.getVariableValue(identifier);
        Value assignValue = assignable.evaluate(environment);
        Value convertedValue = convert(environment, variableValue, assignValue);
        environment.setVariableValue(identifier, convertedValue);

        return new ExecuteOut(ExecuteOut.ExecuteStatus.NORMAL);
    }

/*    private Value convert(Value variableValue, Value assign) throws RuntimeEnvironmentException { //TODO tutaj moze byc problem!!
        if (variableValue instanceof NumberNode && assign instanceof NumberNode) {
            return assign;
        } else if (variableValue instanceof Unit && assign instanceof Unit) {
            double inputUnitValue = ((Unit) variableValue).getValue();
            double inputUnitRatio = ((Unit) variableValue).getRatio();
            double resultUnitRatio = ((Unit) assign).getRatio();
            double resultValue = inputUnitValue * inputUnitRatio / resultUnitRatio;
            return new Unit(resultValue, ((Unit) assign).getUnitType(), resultUnitRatio);
        } else {
            throw new RuntimeEnvironmentException("Cannot assign type");
        }
    }*/

    private Value convert(Environment environment, Value variableValue, Value assign) throws RuntimeEnvironmentException { //TODO tutaj moze byc problem!!
        if (variableValue instanceof NumberNode && assign instanceof NumberNode) {
            return assign;
        } else if (variableValue instanceof Unit && assign instanceof Unit) {
            return environment.castUnitType(((Unit) assign).getUnitType(), ((Unit) variableValue).getUnitType(), ((Unit) assign).getValue());
        } else {
            throw new RuntimeEnvironmentException("Cannot assign type");
        }
    }
}
