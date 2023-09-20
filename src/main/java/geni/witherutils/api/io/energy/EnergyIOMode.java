package geni.witherutils.api.io.energy;

public enum EnergyIOMode {

    Input(true, false, false),

    Output(false, true, false),

    Both(true, true, true);

    private final boolean input, output, respectIOConfig;

    EnergyIOMode(boolean input, boolean output, boolean respectIOConfig)
    {
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
