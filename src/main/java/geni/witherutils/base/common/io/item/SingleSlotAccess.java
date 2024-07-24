package geni.witherutils.base.common.io.item;

import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class SingleSlotAccess {
    private int index = Integer.MIN_VALUE;

    public ItemStack getItemStack(WitherMachineBlockEntity blockEntity) {
        return getItemStack(blockEntity.getInventory());
    }
    public ItemStack getItemStack(MachineInventory inventory) {
        return inventory.getStackInSlot(index);
    }

    public ItemStack getItemStack(Container container) {
        return container.getItem(index);
    }

    public ItemStack insertItem(MachineInventory inventory, ItemStack itemStack, boolean simulate) {
        return inventory.insertItem(index, itemStack, simulate);
    }
    public ItemStack insertItem(WitherMachineBlockEntity machine, ItemStack itemStack, boolean simulate) {
        return insertItem(machine.getInventory(), itemStack, simulate);
    }
    public void setStackInSlot(MachineInventory inventory, ItemStack itemStack) {
        inventory.setStackInSlot(index, itemStack);
    }
    public void setStackInSlot(WitherMachineBlockEntity machine, ItemStack itemStack) {
        setStackInSlot(machine.getInventory(), itemStack);
    }

    public boolean isSlot(int slot) {
        return this.index == slot;
    }

    public int getIndex() {
        return index;
    }

    void init(int i) {
        if (index == Integer.MIN_VALUE) {
            index = i;
        } else if (index != i) {
            throw new IllegalArgumentException("InventoryLayout changed dynamically from " + index + " to " + i + ", don't do that");
        }
    }
    public MultiSlotAccess wrapToMulti() {
        return MultiSlotAccess.wrap(this);
    }
}
