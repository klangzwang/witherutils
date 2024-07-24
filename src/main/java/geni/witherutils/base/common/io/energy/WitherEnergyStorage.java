package geni.witherutils.base.common.io.energy;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import geni.witherutils.api.io.energy.EnergyIOMode;
import geni.witherutils.api.lib.NBTKeys;

import java.util.function.Supplier;

public class WitherEnergyStorage implements IWitherEnergyStorage, INBTSerializable<CompoundTag> {
	
    private final EnergyIOMode ioMode;

    private int energyStored;

    private final Supplier<Integer> capacity;
    private final Supplier<Integer> usageRate;

    public WitherEnergyStorage(EnergyIOMode ioMode, Supplier<Integer> capacity, Supplier<Integer> usageRate) {
        this.ioMode = ioMode;
        this.capacity = capacity;
        this.usageRate = usageRate;
    }

    @Override
    public final EnergyIOMode getIOMode()
    {
        return ioMode;
    }

    // Override in BE
    protected void onContentsChanged() {}

    @Override
    public int getEnergyStored()
    {
        return Math.min(energyStored, getMaxEnergyStored());
    }

    public void setEnergyStored(int energy)
    {
        energyStored = Math.min(energy, getMaxEnergyStored());
        onContentsChanged();
    }

    @Override
    public int addEnergy(int energy)
    {
        return addEnergy(energy, false);
    }

    @Override
    public int addEnergy(int energy, boolean simulate)
    {
        int energyBefore = getEnergyStored();
        int newEnergyStored = Math.min(getEnergyStored() + energy, getMaxEnergyStored());
        if (!simulate)
        {
            setEnergyStored(newEnergyStored);
            onContentsChanged();
        }
        return newEnergyStored - energyBefore;
    }

    @Override
    public int takeEnergy(int energy)
    {
        int energyBefore = getEnergyStored();
        setEnergyStored(Math.max(getEnergyStored() - energy, 0));
        onContentsChanged();
        return energyBefore - getEnergyStored();
    }

    @Override
    public int consumeEnergy(int energy, boolean simulate)
    {
        int usableEnergy = Math.min(energy, getMaxEnergyUse());
        if (!simulate)
        {
            return takeEnergy(usableEnergy);
        }
        return usableEnergy;
    }

    @Override
    public int getMaxEnergyStored()
    {
        return capacity.get();
    }

    @Override
    public int getMaxEnergyUse()
    {
        return usageRate.get();
    }

    @Override
    public boolean canExtract()
    {
        return ioMode.canOutput();
    }

    @Override
    public boolean canReceive()
    {
        return ioMode.canInput();
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        if (!canReceive() || getMaxEnergyStored() == 0)
        {
            return 0;
        }
        int energyReceived = Math.min(getMaxEnergyStored() - getEnergyStored(), Math.min(getMaxEnergyUse() * 2, maxReceive));
        if (!simulate)
        {
            addEnergy(energyReceived);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        if (!canExtract())
        {
            return 0;
        }
        int energyExtracted = Math.min(getEnergyStored(), maxExtract);
        if (!simulate)
        {
            takeEnergy(energyExtracted);
        }
        return energyExtracted;
    }

    @Nullable
    public IEnergyStorage getForSide(@Nullable Direction side)
    {
        if (side == null)
            return this;
        return new Sided(this, side);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider lookupProvider)
    {
        CompoundTag tag = new CompoundTag();
        tag.putInt(NBTKeys.ENERGY_STORED, getEnergyStored());
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt)
    {
        energyStored = nbt.getInt(NBTKeys.ENERGY_STORED);
    }

    private record Sided(WitherEnergyStorage wrapped, Direction side) implements IEnergyStorage {

        @Override
        public int getEnergyStored()
        {
            return wrapped.getEnergyStored();
        }

        @Override
        public int getMaxEnergyStored()
        {
            return wrapped.getMaxEnergyStored();
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate)
        {
            if (!canReceive())
            {
                return 0;
            }
            return wrapped.receiveEnergy(maxReceive, simulate);
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate)
        {
            if (!canExtract())
            {
                return 0;
            }
            return wrapped.extractEnergy(maxExtract, simulate);
        }

        @Override
        public boolean canExtract()
        {
            if (wrapped.getIOMode().respectIOConfig())
            {
                return false;
            }
            return wrapped.canExtract();
        }
        
        @Override
        public boolean canReceive()
        {
            if (wrapped.getIOMode().respectIOConfig())
            {
                return false;
            }
            return wrapped.canReceive();
        }
    }
	
    public void produceEnergy(long energy)
    {
        this.energyStored += energy;
        if (this.energyStored < 0)
        {
            this.energyStored = 0;
        }
        else if (this.energyStored > capacity.get())
        {
            this.energyStored = capacity.get();
        }
        onContentsChanged();
    }
}
