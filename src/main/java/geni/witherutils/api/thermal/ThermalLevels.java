package geni.witherutils.api.thermal;

import java.util.Arrays;

public enum ThermalLevels {
	
    COLD(- Float.MAX_VALUE, 0.15f),
    TEMPERATE(0.15f, 1.5f),
    HOT(1.5f, Float.MAX_VALUE);

    private final float minValue;
    private final float maxValue;

    private ThermalLevels(float minValue, float maxValue)
    {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public static ThermalLevels from(float temp)
    {
        return Arrays.stream(ThermalLevels.values())
                .filter(range -> temp >= range.minValue && temp <= range.maxValue)
                .findFirst()
                .orElse(TEMPERATE);
    }
}
