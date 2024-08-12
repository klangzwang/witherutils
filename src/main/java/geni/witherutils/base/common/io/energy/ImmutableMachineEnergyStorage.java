package geni.witherutils.base.common.io.energy;

import org.apache.commons.lang3.NotImplementedException;

import geni.witherutils.api.io.energy.EnergyIOMode;

public class ImmutableMachineEnergyStorage implements IWitherEnergyStorage {

    public static final ImmutableMachineEnergyStorage EMPTY = new ImmutableMachineEnergyStorage(0, 0, 0);

    private final int energyStored;
    private final int maxEnergyStored;
    private final int maxEnergyUse;

    public ImmutableMachineEnergyStorage(int energyStored, int maxEnergyStored, int maxEnergyUse)
    {
        this.energyStored = energyStored;
        this.maxEnergyStored = maxEnergyStored;
        this.maxEnergyUse = maxEnergyUse;
    }

    public ImmutableMachineEnergyStorage(IWitherEnergyStorage storage)
    {
        this(storage.getEnergyStored(), storage.getMaxEnergyStored(), storage.getMaxEnergyUse());
    }

    @Override
    public int getEnergyStored()
    {
        return energyStored;
    }

    @Override
    public int getMaxEnergyStored()
    {
        return maxEnergyStored;
    }

    @Override
    public int getMaxEnergyUse()
    {
        return maxEnergyUse;
    }

    @Override
    public EnergyIOMode getIOMode()
    {
        throw new NotImplementedException();
    }

    @Deprecated
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean canExtract()
    {
        return false;
    }

    @Override
    public boolean canReceive()
    {
        return false;
    }

    @Deprecated
    @Override
    public void setEnergyStored(int energy)
    {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public int addEnergy(int energy)
    {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public int addEnergy(int energy, boolean simulate)
    {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public int takeEnergy(int energy)
    {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public int consumeEnergy(int energy, boolean simulate)
    {
        throw new UnsupportedOperationException();
    }
}
