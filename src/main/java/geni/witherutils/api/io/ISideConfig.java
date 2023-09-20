package geni.witherutils.api.io;

public interface ISideConfig {

    IOMode getMode();

    void setMode(IOMode mode);

    void cycleMode();
}
