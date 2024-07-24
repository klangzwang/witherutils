package geni.witherutils.base.common.base;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class WitherMachineMenu<T extends WitherMachineBlockEntity> extends AbstractContainerMenu {

    protected WitherMachineMenu(@Nullable MenuType<?> pMenuType, int pContainerId, @Nullable T blockEntity, Inventory inventory)
    {
        super(pMenuType, pContainerId);
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot)
    {
        return super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    public boolean canDragTo(Slot slot)
    {
        return super.canDragTo(slot);
    }
}
