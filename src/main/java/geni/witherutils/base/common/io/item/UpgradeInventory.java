package geni.witherutils.base.common.io.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UpgradeInventory extends ItemStackHandler {
	
    protected final BlockEntity te;

    public UpgradeInventory(int size)
    {
        this(null, size);
    }

    public UpgradeInventory(BlockEntity te, int size)
    {
        super(size);
        this.te = te;
    }

    @Override
    protected void onContentsChanged(int slot)
    {
        super.onContentsChanged(slot);
        if (te != null && te.getLevel() != null && !te.getLevel().isClientSide) te.setChanged();
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
    {
        return isItemValid(slot, stack) ? super.insertItem(slot, stack, simulate) : stack;
    }

    public ItemContainerContents toContainerContents()
    {
        return ItemContainerContents.fromItems(stacks);
    }

    public void loadContainerContents(@Nullable ItemContainerContents contents)
    {
        if (contents != null)
        {
            for (int i = 0; i < contents.getSlots() && i < stacks.size(); i++)
            {
                stacks.set(i, contents.getStackInSlot(i));
            }
            for (int i = contents.getSlots(); i < stacks.size(); i++)
            {
                stacks.set(i, ItemStack.EMPTY);
            }
        }
    }
}
