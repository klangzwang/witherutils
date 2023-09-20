package geni.witherutils.base.common.base;

import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import geni.witherutils.api.io.IIOConfig;
import geni.witherutils.api.io.IOMode;
import geni.witherutils.api.io.ISideConfig;
import geni.witherutils.base.common.init.WUTCapabilities;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.io.IOConfig;
import geni.witherutils.base.common.io.item.MachineInventory;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.core.common.blockentity.WitherBlockEntity;
import geni.witherutils.core.common.sync.EnumDataSlot;
import geni.witherutils.core.common.sync.NBTSerializableDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.items.IItemHandler;

public abstract class WitherMachineBlockEntity extends WitherBlockEntity implements IWrenchable {

	protected boolean redstonePowered;
	
    private final IIOConfig ioConfig;

    public static final ModelProperty<IIOConfig> IO_CONFIG_PROPERTY = new ModelProperty<>();

    private ModelData modelData = ModelData.EMPTY;
    
    private RedstoneControl redstoneControl = RedstoneControl.ALWAYS_ACTIVE;
    
    protected final MachineInventory inventory;
    
    private final EnumMap<Direction, LazyOptional<IItemHandler>> itemHandlerCache = new EnumMap<>(Direction.class);
    private final EnumMap<Direction, LazyOptional<IFluidHandler>> fluidHandlerCache = new EnumMap<>(Direction.class);
    private boolean isCacheDirty = false;

    public WitherMachineBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState)
    {
        super(type, worldPosition, blockState);

        this.ioConfig = createIOConfig();
        addCapabilityProvider(ioConfig);
        
        MachineInventoryLayout slotLayout = getInventoryLayout();
        if (slotLayout != null)
        {
            inventory = createMachineInventory(slotLayout);
            addCapabilityProvider(inventory);
        }
        else
        {
            inventory = null;
        }

        if (supportsRedstoneControl())
        {
            add2WayDataSlot(new EnumDataSlot<>(this::getRedstoneControl, this::setRedstoneControl, SyncMode.GUI));
        }

        add2WayDataSlot(new NBTSerializableDataSlot<>(this::getIOConfig, SyncMode.WORLD, () -> {
            if (level.isClientSide)
            {
                onIOConfigChanged();
            }
        }));
    }
    
    public boolean supportsRedstoneControl()
    {
        return true;
    }
    public MachineInventoryLayout getInventoryLayout()
    {
        return null;
    }

    protected IIOConfig createIOConfig()
    {
        return new IOConfig()
        {
            @Override
            protected void onChanged(Direction side, IOMode oldMode, IOMode newMode)
            {
                setChanged();
                if (newMode == IOMode.DISABLED)
                {
                    invalidateSide(side);
                }
                level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
            }

            @Override
            protected Direction getBlockFacing()
            {
                BlockState state = getBlockState();
                if (state.hasProperty(WitherAbstractBlock.FACING))
                    return getBlockState().getValue(WitherAbstractBlock.FACING);
                return super.getBlockFacing();
            }

            @Override
            public boolean supportsMode(Direction side, IOMode mode)
            {
                return supportsIOMode(side, mode);
            }
        };
    }

    public final IIOConfig getIOConfig()
    {
        return this.ioConfig;
    }

    protected boolean supportsIOMode(Direction side, IOMode mode) 
    {
        return true;
    }

    @Override
    public @NotNull ModelData getModelData()
    {
        return getIOConfig().renderOverlay() ? modelData : ModelData.EMPTY;
    }

    private void onIOConfigChanged()
    {
        if (level.isClientSide && ioConfig.renderOverlay())
        {
            modelData = modelData.derive().with(IO_CONFIG_PROPERTY, ioConfig).build();
            requestModelDataUpdate();
        }
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public final MachineInventory getInventory()
    {
        return inventory;
    }
    
    protected final MachineInventory getInventoryNN()
    {
        return Objects.requireNonNull(inventory);
    }

    protected MachineInventory createMachineInventory(MachineInventoryLayout layout)
    {
        return new MachineInventory(getIOConfig(), layout)
        {
            @Override
            protected void onContentsChanged(int slot)
            {
                onInventoryContentsChanged(slot);
                setChanged();
            }
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
            {
            	if(slot == getInventory().getLayout().getSoulBankSlot())
            		SoundUtil.playSlotSound(level, worldPosition, WUTSounds.REACT.get(), 0.4f, 1.0f);
	      		return super.insertItem(slot, stack, simulate);
            }
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate)
            {
            	if(slot == getInventory().getLayout().getSoulBankSlot())
            		SoundUtil.playSlotSound(level, worldPosition, WUTSounds.SLOT.get(), 1.0f, 1.0f);
	      		return super.extractItem(slot, amount, simulate);
            }
        };
    }

    protected void onInventoryContentsChanged(int slot) {}

    @Override
    public void serverTick()
    {
        if (isCacheDirty)
        {
            updateCapabilityCache();
        }
        if (canActSlow())
        {
            forceResources();
        }
        super.serverTick();
    }

    public boolean canAct()
    {
        if (supportsRedstoneControl())
            return redstoneControl.isActive(level.hasNeighborSignal(worldPosition));
        return true;
    }

    public boolean canActSlow()
    {
        return canAct() && level.getGameTime() % 5 == 0;
    }

    private void forceResources()
    {
        for (Direction direction : Direction.values())
        {
            if (ioConfig.getMode(direction).canForce())
            {
//                moveItems(direction);
//                moveFluids(direction);
            }
        }
    }

//    private void moveItems(Direction side)
//    {
//        getCapability(ForgeCapabilities.ITEM_HANDLER, side).resolve().ifPresent(selfHandler -> {
//
//            Optional<IItemHandler> otherHandler = getNeighboringItemHandler(side).resolve();
//
//            if (otherHandler.isPresent())
//            {
//                IOMode mode = ioConfig.getMode(side);
//
//                if (mode.canPush())
//                {
//                    moveItems(selfHandler, otherHandler.get());
//                }
//
//                if (mode.canPull())
//                {
//                    moveItems(otherHandler.get(), selfHandler);
//                }
//            }
//        });
//    }
//
//    protected void moveItems(IItemHandler from, IItemHandler to)
//    {
//        for (int i = 0; i < from.getSlots(); i++)
//        {
//            ItemStack extracted = from.extractItem(i, 1, true);
//            if (!extracted.isEmpty())
//            {
//                for (int j = 0; j < to.getSlots(); j++)
//                {
//                    ItemStack inserted = to.insertItem(j, extracted, false);
//                    if (inserted.isEmpty())
//                    {
//                        from.extractItem(i, 1, false);
//                        return;
//                    }
//                }
//            }
//        }
//    }
//
//    private void moveFluids(Direction side)
//    {
//        getCapability(ForgeCapabilities.FLUID_HANDLER, side).resolve().ifPresent(selfHandler -> {
//
//            Optional<IFluidHandler> otherHandler = getNeighboringFluidHandler(side).resolve();
//
//            if (otherHandler.isPresent())
//            {
//                IOMode mode = ioConfig.getMode(side);
//                FluidStack stack = selfHandler.drain(100, FluidAction.SIMULATE);
//
//                if (stack.isEmpty() && mode.canPull())
//                {
//                    moveFluids(otherHandler.get(), selfHandler, 100);
//                }
//                else if (mode.canPush())
//                {
//                    moveFluids(selfHandler, otherHandler.get(), 100);
//                }
//            }
//        });
//    }

    protected int moveFluids(IFluidHandler from, IFluidHandler to, int maxDrain)
    {
        FluidStack stack = from.drain(maxDrain, FluidAction.SIMULATE);
        if(stack.isEmpty())
        {
            return 0;
        }
        int filled = to.fill(stack, FluidAction.EXECUTE);
        stack.setAmount(filled);
        from.drain(stack, FluidAction.EXECUTE);
        return filled;
    }

    protected LazyOptional<IItemHandler> getNeighboringItemHandler(Direction side)
    {
        if (!itemHandlerCache.containsKey(side))
            return LazyOptional.empty();
        return itemHandlerCache.get(side);
    }

    protected LazyOptional<IFluidHandler> getNeighboringFluidHandler(Direction side)
    {
        if (!fluidHandlerCache.containsKey(side))
            return LazyOptional.empty();
        return fluidHandlerCache.get(side);
    }

    protected <T> LazyOptional<T> addInvalidationListener(LazyOptional<T> capability)
    {
        if (capability.isPresent())
            capability.addListener(this::markCapabilityCacheDirty);
        return capability;
    }

    private <T> void markCapabilityCacheDirty(LazyOptional<T> capability)
    {
        isCacheDirty = true;
    }

    public void updateCapabilityCache()
    {
        clearCaches();
        for (Direction direction : Direction.values())
        {
            BlockEntity neighbor = level.getBlockEntity(worldPosition.relative(direction));
            populateCaches(direction, neighbor);
        }
    }

    protected void clearCaches()
    {
        itemHandlerCache.clear();
        fluidHandlerCache.clear();
    }

    protected void populateCaches(Direction direction, @Nullable BlockEntity neighbor)
    {
        if (neighbor != null)
        {
            itemHandlerCache.put(direction, addInvalidationListener(neighbor.getCapability(ForgeCapabilities.ITEM_HANDLER, direction.getOpposite())));
            fluidHandlerCache.put(direction, addInvalidationListener(neighbor.getCapability(ForgeCapabilities.FLUID_HANDLER, direction.getOpposite())));
        }
        else
        {
            itemHandlerCache.put(direction, LazyOptional.empty());
            fluidHandlerCache.put(direction, LazyOptional.empty());
        }
    }

    @Override
    public void saveAdditional(CompoundTag pTag)
    {
        super.saveAdditional(pTag);

        pTag.put("io_config", getIOConfig().serializeNBT());
        
        if (supportsRedstoneControl())
        {
            pTag.putInt("redstone", redstoneControl.ordinal());
        }
        if (inventory != null)
        {
            pTag.put("inventory", inventory.serializeNBT());
        }
    }

    @Override
    public void load(CompoundTag pTag)
    {
        ioConfig.deserializeNBT(pTag.getCompound("io_config"));

        if (supportsRedstoneControl())
        {
            redstoneControl = RedstoneControl.values()[pTag.getInt("redstone")];
        }
        
        if (inventory != null)
        {
            inventory.deserializeNBT(pTag.getCompound("inventory"));
        }

        if (level != null)
        {
            onIOConfigChanged();
        }

        isCacheDirty = true;

        super.load(pTag);
    }

    public Component getDisplayName()
    {
        return getBlockState().getBlock().getName();
    }

    public InteractionResult onBlockEntityUsed(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit){return InteractionResult.PASS;}

    public boolean stillValid(Player pPlayer)
    {
        if (this.level.getBlockEntity(this.worldPosition) != this)
            return false;
        return pPlayer.distanceToSqr(this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 0.5D, this.worldPosition.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public InteractionResult onWrenched(UseOnContext context)
    {
        Player player = context.getPlayer();
        if(player != null && level != null && player.isSecondaryUseActive())
        {
            BlockPos pos = context.getClickedPos();
            BlockState state = context.getLevel().getBlockState(pos);
            List<ItemStack> drops = Block.getDrops(state, (ServerLevel) level,pos, level.getBlockEntity(pos));
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
            player.swing(InteractionHand.MAIN_HAND);

            SoundType soundType = state.getBlock().getSoundType(state, level, pos, null);
            level.playSound(null, pos, soundType.getBreakSound(), SoundSource.BLOCKS, soundType.volume, soundType.pitch);
            
            for (ItemStack drop: drops)
            {
                if(!player.addItem(drop))
                {
                    level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop));
                }
            }
            return InteractionResult.CONSUME;
        }
        else
        {
            LazyOptional<ISideConfig> optSideConfig = getCapability(WUTCapabilities.SIDECONFIG, context.getClickedFace());
            if (optSideConfig.isPresent())
            {
                optSideConfig.ifPresent(ISideConfig::cycleMode);
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }
    
    /*
     * 
     * REDSTONE/LIT
     * 
     */
    public void setLitProperty(boolean lit)
    {
        BlockState st = this.getBlockState();
        if(!st.hasProperty(WitherAbstractBlock.LIT))
        {
            return;
        }
        boolean previous = st.getValue(WitherAbstractBlock.LIT);
        if(previous != lit)
        {
            this.level.setBlockAndUpdate(worldPosition, st.setValue(WitherAbstractBlock.LIT, lit));
        }
    }
    
    public boolean isPowered()
    {
        return this.getLevel().hasNeighborSignal(this.getBlockPos());
    }
    
    public RedstoneControl getRedstoneControl()
    {
        return redstoneControl;
    }
    public void setRedstoneControl(RedstoneControl redstoneControl)
    {
        this.redstoneControl = redstoneControl;
    }
}
