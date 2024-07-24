package geni.witherutils.base.common.base;

import java.util.Objects;

import javax.annotation.Nullable;

import geni.witherutils.base.common.io.item.MachineInventory;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.core.common.blockentity.WitherBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.items.IItemHandler;

public abstract class WitherMachineBlockEntity extends WitherBlockEntity implements Nameable {

    private Component customName = null;
    
    public static final ICapabilityProvider<WitherMachineBlockEntity, Direction, IItemHandler> ITEM_HANDLER_PROVIDER =
            (be, side) -> be.inventory != null ? be.inventory.getForSide(side) : null;

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

    @Override
    public void serverTick()
    {
        if (canActSlow())
            forceResources();
        
        super.serverTick();
    }

    public boolean canAct()
    {
        if (this.level == null)
            return false;
        return true;
    }

    public boolean canActSlow()
    {
        if (this.level == null)
            return false;
        return canAct() && this.level.getGameTime() % 5 == 0;
    }

    private void forceResources()
    {
        for (Direction direction : Direction.values())
        {
            moveItems(direction);
            moveFluids(direction);
        }
    }

    private void moveItems(Direction side)
    {
//        IItemHandler selfHandler = getSelfCapability(Capabilities.ItemHandler.BLOCK, side);
//        IItemHandler otherHandler = getNeighbouringCapability(Capabilities.ItemHandler.BLOCK, side);
    }
    private void moveFluids(Direction side)
    {
//        IFluidHandler selfHandler = getSelfCapability(Capabilities.FluidHandler.BLOCK, side);
//        IFluidHandler otherHandler = getNeighbouringCapability(Capabilities.FluidHandler.BLOCK, side);
    }

    private String getBlockTranslationKey()
    {
        String key = BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(getType())
                .map(rk -> rk.location().getPath())
                .orElse("unknown");
        return "block.witherutils." + key;
    }
    
    @Override
    public Component getName() {
        return customName == null ? Component.translatable(getBlockTranslationKey()) : customName;
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return customName;
    }

    public void setCustomName(Component customName) {
        this.customName = customName;
    }

    @Override
    public Component getDisplayName()
    {
        return getName();
    }

    public ItemInteractionResult onBlockEntityUsed(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public boolean canOpenMenu()
    {
        return true;
    }

    @SuppressWarnings("deprecation")
	public int getLightEmission()
    {
        return getBlockState().getLightEmission();
    }
    
    /*
     * 
     * LIT
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
}
