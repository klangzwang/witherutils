package geni.witherutils.base.common.base;

import java.util.Objects;

import javax.annotation.Nullable;

import geni.witherutils.api.misc.RedstoneControl;
import geni.witherutils.base.common.init.WUTAttachments;
import geni.witherutils.base.common.io.item.MachineInstallable;
import geni.witherutils.base.common.io.item.MachineInventory;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.core.common.blockentity.WitherBlockEntity;
import geni.witherutils.core.common.network.NetworkDataSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;

public abstract class WitherMachineBlockEntity extends WitherBlockEntity implements Nameable, MenuProvider, MachineInstallable {

    private Component customName = null;
	
    public static final NetworkDataSlot.CodecType<RedstoneControl> REDSTONE_CONTROL_DATA_SLOT_TYPE
    = new NetworkDataSlot.CodecType<>(RedstoneControl.CODEC, RedstoneControl.STREAM_CODEC.cast());

    private final NetworkDataSlot<RedstoneControl> redstoneControlDataSlot;
	protected int powerLevel = 0;
	
    @Nullable
    private final MachineInventory inventory;
    
    public WitherMachineBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState)
    {
        super(type, worldPosition, blockState);

        MachineInventoryLayout slotLayout = getInventoryLayout();
        
        if (slotLayout != null)
            inventory = createMachineInventory(slotLayout);
        else
            inventory = null;
        
        if (supportsRedstoneControl())
        {
            redstoneControlDataSlot = addDataSlot(REDSTONE_CONTROL_DATA_SLOT_TYPE.create(
                this::getRedstoneControl,
                this::internalSetRedstoneControl));
        }
        else
        {
            redstoneControlDataSlot = null;
        }
    }
    
    /*
     * 
     * ITEMINVENTORY
     * 
     */
    @Override
    public InteractionResult tryItemInstall(ItemStack stack, UseOnContext context, int slot)
    {
        MachineInventory inventory = getInventory();
        MachineInventoryLayout layout = getInventoryLayout();
        if (inventory != null && layout != null)
        {
        	if(inventory.getStackInSlot(slot).isEmpty())
                inventory.setStackInSlot(slot, stack.copyWithCount(1));            		
            stack.shrink(1);
            return InteractionResult.sidedSuccess(context.getLevel().isClientSide());
        }
        return InteractionResult.PASS;
    }
    
    @Override
    public boolean hasItemCapability()
    {
        return true;
    }
    
    @Override
    public IItemHandler getItemHandler(@Nullable Direction dir)
    {
    	return inventory;
    }
    
    @Nullable
    public MachineInventoryLayout getInventoryLayout()
    {
        return null;
    }

    @Nullable
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
        return new MachineInventory(layout)
        {
            @Override
            protected void onContentsChanged(int slot)
            {
                onInventoryContentsChanged(slot);
                setChanged();
            }
        };
    }

    protected void onInventoryContentsChanged(int slot) {}

    /*
     * 
     * LITREDSTONE
     * 
     */
    public void setLitProperty(boolean lit)
    {
        BlockState st = this.getBlockState();
        if(!st.hasProperty(WitherAbstractBlock.LIT))
            return;
        boolean previous = st.getValue(WitherAbstractBlock.LIT);
        if(previous != lit)
            this.level.setBlockAndUpdate(worldPosition, st.setValue(WitherAbstractBlock.LIT, lit));
    }
    
    public boolean isPowered()
    {
        return this.getLevel().hasNeighborSignal(this.getBlockPos());
    }

    public boolean supportsRedstoneControl()
    {
        return true;
    }

    public RedstoneControl getRedstoneControl()
    {
        if (!supportsRedstoneControl())
        {
            throw new IllegalStateException("This machine does not support redstone control.");
        }
        return getData(WUTAttachments.REDSTONE_CONTROL);
    }

    public void setRedstoneControl(RedstoneControl redstoneControl)
    {
        if (!supportsRedstoneControl())
        {
            throw new IllegalStateException("This machine does not support redstone control.");
        }
        if (level != null && level.isClientSide())
        {
            clientUpdateSlot(redstoneControlDataSlot, redstoneControl);
        }
        else
        {
            internalSetRedstoneControl(redstoneControl);
        }
    }

    private void internalSetRedstoneControl(RedstoneControl redstoneControl)
    {
        setData(WUTAttachments.REDSTONE_CONTROL, redstoneControl);
        setChanged();
    }
    
    protected boolean needsRedstoneMode() {
        return false;
    }

    public void checkRedstone(Level world, BlockPos pos)
    {
        int powered = world.getBestNeighborSignal(pos);
        setPowerInput(powered);
    }

    public void setPowerInput(int powered)
    {
        if (powerLevel != powered)
        {
            powerLevel = powered;
            setChanged();
        }
    }

    public int getPowerLevel()
    {
        return powerLevel;
    }
    
    /*
     * 
     * PACKET
     * 
     */
    @Override
    public void saveAdditional(CompoundTag pTag, HolderLookup.Provider lookupProvider)
    {
        super.saveAdditional(pTag, lookupProvider);
        if (this.inventory != null)
        {
            pTag.put("Items", inventory.serializeNBT(lookupProvider));
        }
        if (customName != null)
        {
        	pTag.putString("CustomName", Component.Serializer.toJson(customName, lookupProvider));
        }
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider lookupProvider)
    {
        if (this.inventory != null)
        {
            inventory.deserializeNBT(lookupProvider, pTag.getCompound("Items"));
        }
        if (this.level != null)
        {
        }
        if (pTag.contains("CustomName", Tag.TAG_STRING))
        {
            customName = Component.Serializer.fromJson(pTag.getString("CustomName"), lookupProvider);
        }
        super.loadAdditional(pTag, lookupProvider);
    }
    
    @Override
    protected void applyImplicitComponents(DataComponentInput components)
    {
        super.applyImplicitComponents(components);
        if (this.inventory != null)
        {
            this.inventory.copyFromItem(components.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
        }
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components)
    {
        super.collectImplicitComponents(components);
        if (this.inventory != null)
        {
            components.set(DataComponents.CONTAINER, this.inventory.toItemContents());
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void removeComponentsFromTag(CompoundTag tag)
    {
        super.removeComponentsFromTag(tag);
        tag.remove("Items");
	}
    
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player)
    {
        return null;
    }
    
    private String getBlockTranslationKey()
    {
        String key = BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(getType())
                .map(rk -> rk.location().getPath())
                .orElse("unknown");
        return "block.witherutils." + key;
    }
    
    @Override
    public Component getName()
    {
        return customName == null ? Component.translatable(getBlockTranslationKey()) : customName;
    }
    
	@Override
	public Component getDisplayName()
	{
		return getName();
	}
}
