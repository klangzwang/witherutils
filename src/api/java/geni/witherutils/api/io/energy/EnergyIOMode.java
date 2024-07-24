package geni.witherutils.api.io.energy;

public enum EnergyIOMode {

    INPUT(true, false, false),
    OUTPUT(false, true, false),
    BOTH(true, true, true);

    private final boolean input;
    private final boolean output;
    private final boolean respectIOConfig;

    EnergyIOMode(boolean input, boolean output, boolean respectIOConfig) {
        this.input = input;
        this.output = output;
        this.respectIOConfig = respectIOConfig;
    }

    public boolean canInput()
    {
        return input;
    }

    public boolean canOutput()
    {
        return output;
    }

    public boolean respectIOConfig()
    {
        return respectIOConfig;
    }
}
