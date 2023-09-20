package geni.witherutils.base.common.base;

import java.util.EnumMap;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import geni.witherutils.api.io.energy.EnergyIOMode;
import geni.witherutils.api.soulbank.ISoulBankData;
import geni.witherutils.api.soulbank.ISoulBankScalable;
import geni.witherutils.base.common.io.energy.IWitherEnergyStorage;
import geni.witherutils.base.common.io.energy.ImmutableWitherEnergyStorage;
import geni.witherutils.base.common.io.energy.WitherEnergyStorage;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.base.common.soulbank.DefaultSoulBankData;
import geni.witherutils.core.common.sync.EnergyDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import geni.witherutils.core.common.util.SoulBankUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class WitherMachineEnergyBlockEntity extends WitherMachineBlockEntity {

    protected final WitherEnergyStorage energyStorage;
    private IWitherEnergyStorage clientEnergyStorage = ImmutableWitherEnergyStorage.EMPTY;
    
    protected final LazyOptional<WitherEnergyStorage> energyStorageCap;
    
    private final EnumMap<Direction, LazyOptional<IEnergyStorage>> energyHandlerCache = new EnumMap<>(Direction.class);
    
    private ISoulBankData cachedSoulBankData = DefaultSoulBankData.NONE;
    private boolean soulBankCacheDirty;

    public WitherMachineEnergyBlockEntity(EnergyIOMode energyIOMode, ISoulBankScalable capacity, ISoulBankScalable transferRate, ISoulBankScalable usageRate, BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState)
    {
        super(type, worldPosition, blockState);

        this.energyStorage = createEnergyStorage(energyIOMode,
            capacity.scaleI(this::getSoulBankData),
            transferRate.scaleI(this::getSoulBankData),
            usageRate.scaleI(this::getSoulBankData));
        this.energyStorageCap = LazyOptional.of(() -> energyStorage);
        addCapabilityProvider(energyStorage);
        soulBankCacheDirty = true;
        
        addDataSlot(new EnergyDataSlot(this::getEnergyStorage, storage -> clientEnergyStorage = storage, SyncMode.GUI));
        addDataSlot(new EnergyDataSlot(this::getEnergyStorage, storage -> clientEnergyStorage = storage, SyncMode.WORLD));
    }

    @Override
    public void serverTick()
    {
        if (level.getGameTime() % 20 == 0)
        {
            energyStorage.takeEnergy(getEnergyLeakPerSecond());
        }
        if (canAct())
        {
            for(Direction facing : Direction.values())
            {
                if(!level.getBlockState(worldPosition.relative(facing)).isAir())
                {
                    getCapability(ForgeCapabilities.ENERGY, facing).resolve().ifPresent(selfHandler -> {
                        moveEnergy(facing, energyStorage.getMaxEnergyTransfer());
                    });
                }
            }
        }
        super.serverTick();
    }

    public final IWitherEnergyStorage getEnergyStorage()
    {
        if (isClientSide())
        {
            return clientEnergyStorage;
        }
        return energyStorage;
    }
    
    public int getEnergyLeakPerSecond()
    {
        return 0;
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
        IEnergyStorage fromHandler = this.getCapability(ForgeCapabilities.ENERGY, facing).orElse(null);
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
        
        IEnergyStorage toHandler = be.getCapability(ForgeCapabilities.ENERGY, facingMe).orElse(null);

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

    protected WitherEnergyStorage createEnergyStorage(EnergyIOMode energyIOMode, Supplier<Integer> capacity, Supplier<Integer> transferRate, Supplier<Integer> usageRate)
    {
        return new WitherEnergyStorage(getIOConfig(), energyIOMode, capacity, transferRate, usageRate)
        {
            @Override
            protected void onContentsChanged()
            {
                setChanged();
            }
        };
    }

    @Override
    protected void clearCaches()
    {
        super.clearCaches();
        energyHandlerCache.clear();
    }
    
    @Override
    protected void populateCaches(Direction direction, @Nullable BlockEntity neighbor)
    {
        super.populateCaches(direction, neighbor);

        if (neighbor != null)
        {
            energyHandlerCache.put(direction, addInvalidationListener(neighbor.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite())));
        }
        else
        {
            energyHandlerCache.put(direction, LazyOptional.empty());
        }
    }

    protected LazyOptional<IEnergyStorage> getNeighboringEnergyHandler(Direction side)
    {
        if (!energyHandlerCache.containsKey(side))
            return LazyOptional.empty();
        return energyHandlerCache.get(side);
    }
    
    public boolean requiresSoulBank()
    {
        MachineInventoryLayout layout = getInventoryLayout();
        if (layout == null)
            return false;
        return layout.supportsSoulBank();
    }

    public int getSoulBankSlot()
    {
        MachineInventoryLayout layout = getInventoryLayout();
        if (layout == null)
            return -1;
        return layout.getSoulBankSlot();
    }

    public boolean isSoulBankInstalled()
    {
        if (soulBankCacheDirty)
            cacheSoulBankData();
        return cachedSoulBankData != DefaultSoulBankData.NONE;
    }

    public ItemStack getSoulBankItem()
    {
        MachineInventoryLayout layout = getInventoryLayout();
        if (layout == null)
            return ItemStack.EMPTY;
        return getInventory().getStackInSlot(layout.getSoulBankSlot());
    }

    public ISoulBankData getSoulBankData()
    {
        if (soulBankCacheDirty)
            cacheSoulBankData();
        return cachedSoulBankData;
    }

    @Override
    protected void onInventoryContentsChanged(int slot)
    {
        if (getInventoryLayout().getSoulBankSlot() == slot)
        {
            soulBankCacheDirty = true;
        }
        super.onInventoryContentsChanged(slot);
    }

    private void cacheSoulBankData()
    {
        if (level == null)
        {
            return;
        }
    	
        soulBankCacheDirty = false;

        if (level.isClientSide())
        {
            return;
        }

        MachineInventoryLayout layout = getInventoryLayout();
        if (requiresSoulBank() && layout != null)
        {
            cachedSoulBankData = SoulBankUtil.getSoulBankData(getSoulBankItem()).orElse(DefaultSoulBankData.NONE);
        }
        else
        {
            cachedSoulBankData = DefaultSoulBankData.NONE;
        }
    }

    @Override
    public boolean canAct()
    {
        return super.canAct() && (!requiresSoulBank() || isSoulBankInstalled());
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side)
    {
        if (cap == ForgeCapabilities.ENERGY && side == null)
        {
            return energyStorageCap.cast();
        }
        return super.getCapability(cap, side);
    }
    
    @Override
    public void invalidateCaps()
    {
        super.invalidateCaps();
        energyStorageCap.invalidate();
    }

    @Override
    public void saveAdditional(CompoundTag pTag)
    {
        pTag.put("energy", energyStorage.serializeNBT());
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag)
    {
        energyStorage.deserializeNBT(pTag.getCompound("energy"));
        super.load(pTag);
    }
}
