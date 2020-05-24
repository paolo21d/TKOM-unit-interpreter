package tkom.ast.statement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tkom.ast.Node;
import tkom.ast.Signature;
import tkom.ast.Value;
import tkom.ast.expression.ExpressionNode;
import tkom.ast.expression.NumberNode;
import tkom.ast.expression.Unit;
import tkom.errorHandler.RuntimeEnvironmentException;
import tkom.execution.Environment;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitStatement extends Signature implements Statement, Node {
    private ExpressionNode assignable;

    @Override
    public ExecuteOut execute(Environment environment) throws RuntimeEnvironmentException {
        if (assignable == null) {
            environment.addVariable(getIdentifier(), getDefaultValue(environment));
        } else {
            Value assign = assignable.evaluate(environment);
            Value convertedValue = convert(environment, assign);
            environment.addVariable(getIdentifier(), convertedValue);
        }
        return new ExecuteOut(ExecuteOut.ExecuteStatus.NORMAL);
    }

    private Value getDefaultValue(Environment environment) throws RuntimeEnvironmentException {
        if (checkType("INTEGER") || checkType("DOUBLE")) {
            return new NumberNode(0.0);
        } else if (environment.isUnitDefined(getType())) {
            return new Unit(0.0, getType(), environment.getUnitRatio().getUnitValue(getType()));
        } else {
            throw new RuntimeEnvironmentException("Unexpected variable type: " + getType());
        }
    }

    private Value convert(Environment environment, Value assign) throws RuntimeEnvironmentException { //TODO tutaj moze byc problem!!
        if (isNumber() && assign instanceof NumberNode) {
            return assign;
        } else if (assign instanceof Unit && environment.isUnitDefined(getType())) {
/*            double inputUnitValue = ((Unit) assign).getValue();
            double inputUnitRatio = ((Unit) assign).getRatio();
            double resultUnitRatio = environment.getUnitRatio().getUnitValue(getType());
            double resultValue = inputUnitValue * inputUnitRatio / resultUnitRatio;
            return new Unit(resultValue, getType(), resultUnitRatio);*/
            return environment.castUnitType(((Unit) assign).getUnitType(), getType(), ((Unit) assign).getValue());
        } else {
            throw new RuntimeEnvironmentException("Cannot assign to " + getType().toUpperCase());
        }
    }


}
