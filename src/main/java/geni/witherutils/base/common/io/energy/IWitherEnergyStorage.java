package geni.witherutils.base.common.io.energy;

import geni.witherutils.api.io.IIOConfig;
import geni.witherutils.api.io.energy.EnergyIOMode;
import net.minecraftforge.energy.IEnergyStorage;

public interface IWitherEnergyStorage extends IEnergyStorage {

    void setEnergyStored(int energy);

    int addEnergy(int energy);

    int takeEnergy(int energy);

    int consumeEnergy(int energy, boolean simulate);

    int getMaxEnergyTransfer();

    int getMaxEnergyUse();

    IIOConfig getConfig();

    EnergyIOMode getIOMode();
}
