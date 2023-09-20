package geni.witherutils.api.io;

public enum IOMode {

    NONE(true, true, false, true),

    PUSH(false, true, true, true),

    PULL(true, false, true, true),

    BOTH(true, true, true, true),

    DISABLED(false, false, false, false);

    private final boolean input, output, force, canConnect;

    IOMode(boolean input, boolean output, boolean force, boolean canConnect)
    {
        this.input = input;
        this.output = output;
        this.force = force;
        this.canConnect = canConnect;
    }

    public boolean canInput()
    {
        return input;
    }
    public boolean canOutput()
    {
        return output;
    }
    public boolean canConnect()
    {
        return canConnect;
    }
    public boolean canPush()
    {
        return canOutput() && canForce();
    }
    public boolean canPull()
    {
        return canInput() && canForce();
    }
    public boolean canForce()
    {
        return force;
    }
}
