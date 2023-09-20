package geni.witherutils.base.common.block.rack.controller.item;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

@SuppressWarnings("unused")
public class ControllerItemBlockItemHandlerCapability implements IItemHandler, ICapabilityProvider {
	
	private final LazyOptional<IItemHandler> holder = LazyOptional.of(() -> this);
    public final ItemStack container;
    public final int slotLimit;

    public ControllerItemBlockItemHandlerCapability(ItemStack container, int slotLimit)
    {
        this.container = container;
		this.slotLimit = slotLimit;
    }

    @Override
    public int getSlots()
    {
        return 1;
    }
    
    public void setStack(ItemStack stack)
    {
        CompoundTag tag = getTag();
        if (tag == null)
        {
            CompoundTag compoundNBT = new CompoundTag();
            compoundNBT.put("BlockEntityTag", new CompoundTag());
            this.container.setTag(compoundNBT);
        }
        this.container.getTag().getCompound("BlockEntityTag").put("blStack", stack.serializeNBT());
    }
	
    public void setStored(int stored)
    {
        CompoundTag tag = getTag();
        if (tag == null)
        {
            CompoundTag compoundNBT = new CompoundTag();
            compoundNBT.put("BlockEntityTag", new CompoundTag());
            this.container.setTag(compoundNBT);
        }
        this.container.getTag().getCompound("BlockEntityTag").putInt("stored", stored);
    }
    
    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot)
    {
        ItemStack copied = getStack();
        copied.setCount(getStored());
        return copied;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
    {
    	if (isItemValid(slot, stack))
    	{
    		
    	}
    	return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
    	return extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return slotLimit;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
    {
    	return isItemValid(slot, stack);
    }
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        if (cap != null && cap.equals(ForgeCapabilities.ITEM_HANDLER))
        	return holder.cast();
        return LazyOptional.empty();
    }
    
    /*
     * 
     * TAG
     * 
     */
    public int getStored()
    {
        CompoundTag tag = getTag();
        if (tag != null && tag.contains("stored")) {
            return tag.getInt("stored");
        }
        return 0;
    }

    public ItemStack getStack()
    {
        CompoundTag tag = getTag();
        if (tag != null && tag.contains("blStack")) {
            return ItemStack.of(tag.getCompound("blStack"));
        }
        return ItemStack.EMPTY;
    }

    public boolean getVoid()
    {
        CompoundTag tag = getTag();
        if (tag != null && tag.contains("voidItems")) {
            return tag.getBoolean("voidItems");
        }
        return true;
    }

    private CompoundTag getTag()
    {
        if (container.hasTag() && container.getTag().contains("BlockEntityTag"))
            return container.getTag().getCompound("BlockEntityTag");
        return null;
    }
}
