package tkom.data;

import tkom.errorHandler.RuntimeEnvironmentException;

import java.util.HashMap;
import java.util.Map;

public class UnitRatio {
    Map<String, Double> ratio = new HashMap<>();

    public UnitRatio() {
        ratio.put("KILO", 1000.0);
        ratio.put("MEGA", 1000000.0);
        ratio.put("GIGA", 1000000000.0);
        ratio.put("CENTY", 0.01);
        ratio.put("MILI", 0.001);
        ratio.put("BASE", 1.0);
    }

    public void addUnit(String identifier, Double value) {
        ratio.put(identifier, value);
    }

    public boolean isUnitDefined(String identifier) {
        return ratio.containsKey(identifier);
    }

    public double getUnitValue(String identifier) throws RuntimeEnvironmentException {
        if (ratio.containsKey(identifier)) {
            return ratio.get(identifier);
        }

        throw new RuntimeEnvironmentException("Undefined reference to unit " + identifier);
    }

    public double castUnits(String originalUnitType, String resultUnitType, double originalUnitValue) throws RuntimeEnvironmentException {
        return originalUnitValue * getUnitValue(originalUnitType) / getUnitValue(resultUnitType);
    }
}
