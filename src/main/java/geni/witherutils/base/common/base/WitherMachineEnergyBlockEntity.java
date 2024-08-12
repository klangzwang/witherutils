package geni.witherutils.base.common.base;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import geni.witherutils.api.io.energy.EnergyIOMode;
import geni.witherutils.api.misc.RedstoneControl;
import geni.witherutils.base.common.init.WUTAttachments;
import geni.witherutils.base.common.init.WUTComponents;
import geni.witherutils.base.common.io.energy.IWitherEnergyStorage;
import geni.witherutils.base.common.io.energy.ImmutableMachineEnergyStorage;
import geni.witherutils.base.common.io.energy.WitherEnergyStorage;
import geni.witherutils.core.common.network.NetworkDataSlot;
import geni.witherutils.core.common.sync.EnergySyncData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

public abstract class WitherMachineEnergyBlockEntity extends WitherMachineBlockEntity {

    private final WitherEnergyStorage energyStorage;
    protected IWitherEnergyStorage clientEnergyStorage = ImmutableMachineEnergyStorage.EMPTY;
    
    public WitherMachineEnergyBlockEntity(EnergyIOMode energyIOMode, Supplier<Integer> capacity, Supplier<Integer> usageRate, BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState)
    {
        super(type, worldPosition, blockState);
        this.energyStorage = createEnergyStorage(energyIOMode, capacity, usageRate);
        addDataSlot(createEnergyDataSlot());
    }

    public NetworkDataSlot<?> createEnergyDataSlot()
    {
        return EnergySyncData.DATA_SLOT_TYPE.create(() -> EnergySyncData.from(getEnergyHandler(null)), v -> clientEnergyStorage = v.toImmutableStorage());
    }
	
    protected WitherEnergyStorage createEnergyStorage(EnergyIOMode energyIOMode, Supplier<Integer> capacity, Supplier<Integer> usageRate)
    {
        return new WitherEnergyStorage(energyIOMode, capacity, usageRate)
        {
            @Override
            protected void onContentsChanged()
            {
                setChanged();
            }
        };
    }
    
    @Override
    public boolean hasEnergyCapability()
    {
        return true;
    }
    
    public final boolean hasEnergy()
    {
        return energyStorage.getEnergyStored() > 0;
    }
    
    @Override
    public IWitherEnergyStorage getEnergyHandler(@Nullable Direction dir)
    {
        if (level != null && level.isClientSide())
        {
            return clientEnergyStorage;
        }
        return energyStorage;
    }
	
    protected boolean moveEnergy(Direction facing, int energyAmount)
    {
        return moveEnergy(facing, worldPosition.relative(facing), energyAmount);
    }

    protected boolean moveEnergy(Direction facing, BlockPos targetPosition, int energyAmount)
    {
        if (this.level.isClientSide)
        {
            return false;
        }
        IEnergyStorage fromHandler = getSelfCapability(Capabilities.EnergyStorage.BLOCK, facing);
        if (fromHandler == null || fromHandler.getEnergyStored() == 0)
        {
            return false;
        }
        
        if (facing == null)
        {
            facing = Direction.UP;
        }
        
        Direction facingMe = facing.getOpposite();
        BlockEntity be = level.getBlockEntity(targetPosition);
        if (be == null)
        {
            return false;
        }
        
        IEnergyStorage toHandler = getNeighbouringCapability(Capabilities.EnergyStorage.BLOCK, facingMe);

        if (toHandler == null)
        {
            return false;
        }
        
        if (fromHandler != null && toHandler != null && fromHandler.canExtract() && toHandler.canReceive())
        {
            int drain = fromHandler.extractEnergy(energyAmount, true);
            if (drain > 0)
            {
                int filled = toHandler.receiveEnergy(drain, false);
                fromHandler.extractEnergy(filled, false);
                return filled > 0;
            }
        }
        return false;
    }

    @Override
    public void saveAdditional(CompoundTag pTag, HolderLookup.Provider lookupProvider)
    {
        super.saveAdditional(pTag, lookupProvider);
        var energyStorage = getEnergyHandler(null);
        if (energyStorage instanceof WitherEnergyStorage storage)
        {
            pTag.put("EnergyStored", storage.serializeNBT(lookupProvider));
        }
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider lookupProvider)
    {
        super.loadAdditional(pTag, lookupProvider);
        var energyStorage = getEnergyHandler(null);
        if (energyStorage instanceof WitherEnergyStorage storage && pTag.contains("EnergyStored"))
        {
            storage.deserializeNBT(lookupProvider, pTag.getCompound("EnergyStored"));
        }
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput components)
    {
        super.applyImplicitComponents(components);
        energyStorage.setEnergyStored(components.getOrDefault(WUTComponents.ENERGY, 0));
        setData(WUTAttachments.REDSTONE_CONTROL, components.getOrDefault(WUTComponents.REDSTONE_CONTROL, RedstoneControl.ALWAYS_ACTIVE));
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components)
    {
        super.collectImplicitComponents(components);
        components.set(WUTComponents.ENERGY, energyStorage.getEnergyStored());
        components.set(WUTComponents.REDSTONE_CONTROL, getData(WUTAttachments.REDSTONE_CONTROL));
    }
    
    @Override
    public void removeComponentsFromTag(CompoundTag tag)
    {
        super.removeComponentsFromTag(tag);
        tag.remove("EnergyStored");
        removeData(WUTAttachments.REDSTONE_CONTROL);
    }
}
