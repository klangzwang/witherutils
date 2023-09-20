package geni.witherutils.core.common.sync;

import net.minecraft.nbt.CompoundTag;

import java.util.function.Consumer;
import java.util.function.Supplier;

import geni.witherutils.base.common.io.energy.IWitherEnergyStorage;
import geni.witherutils.base.common.io.energy.ImmutableWitherEnergyStorage;

public class EnergyDataSlot extends EnderDataSlot<IWitherEnergyStorage> {
    
    public EnergyDataSlot(Supplier<IWitherEnergyStorage> getter, Consumer<IWitherEnergyStorage> setter, SyncMode mode)
    {
        super(getter, setter, mode);
    }

    @Override
    public CompoundTag toFullNBT()
    {
        IWitherEnergyStorage storage = getter().get();
        CompoundTag tag = new CompoundTag();
        tag.putInt("Energy", storage.getEnergyStored());
        tag.putInt("MaxStored", storage.getMaxEnergyStored());
        tag.putInt("MaxTransfer", storage.getMaxEnergyTransfer());
        tag.putInt("MaxUse", storage.getMaxEnergyUse());
        return tag;
    }

    @Override
    protected IWitherEnergyStorage fromNBT(CompoundTag nbt)
    {
        int energy = nbt.getInt("Energy");
        int maxStored = nbt.getInt("MaxStored");
        int maxTransfer = nbt.getInt("MaxTransfer");
        int maxUse = nbt.getInt("MaxUse");
        return new ImmutableWitherEnergyStorage(energy, maxStored, maxTransfer, maxUse);
    }
}
