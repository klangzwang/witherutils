package geni.witherutils.base.common.io.energy;

import geni.witherutils.api.io.energy.EnergyIOMode;
import net.neoforged.neoforge.energy.IEnergyStorage;

public interface IWitherEnergyStorage extends IEnergyStorage {

    void setEnergyStored(int energy);

    int addEnergy(int energy);
    
    int addEnergy(int energy, boolean simulate);

    int takeEnergy(int energy);

    int consumeEnergy(int energy, boolean simulate);

    int getMaxEnergyUse();
    
    EnergyIOMode getIOMode();
}
