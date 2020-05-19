package tkom.data;

import tkom.errorHandler.RuntimeEnvironmentException;

import java.util.HashMap;
import java.util.Map;

public class UnitRatio {
    Map<String, Long> ratio = new HashMap<>();

    public void addUnit(String identifier, Long value) {
        ratio.put(identifier, value);
    }

    public boolean isUnitDefined(String identifier) {
        return ratio.containsKey(identifier);
    }

    public Long getUnitValue(String identifier) throws RuntimeEnvironmentException {
        if (ratio.containsKey(identifier)) {
            return ratio.get(identifier);
        }

        throw new RuntimeEnvironmentException("Undefined reference to unit " + identifier);
    }
}
