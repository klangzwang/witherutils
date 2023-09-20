package geni.witherutils.core.common.util;

import java.util.Collection;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public final class StackUtil {

    public static boolean isValid(ItemStack stack)
    {
        return stack != null && !stack.isEmpty();
    }

    public static ItemStack getEmpty()
    {
        return ItemStack.EMPTY;
    }

    public static CompoundTag getTagOrEmpty(ItemStack stack)
    {
    	CompoundTag nbt = stack.getTag();
        return nbt != null ? nbt : new CompoundTag();
    }

    public static boolean isEmpty(Collection<ItemStack> stacks) {
        if (stacks.isEmpty()) {
            return true;
        }
        for (ItemStack s : stacks) {
            if (!s.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    public static int findFirstFilled(IItemHandler inv) {
        for (int i = 0; i < inv.getSlots(); i++) {
            if (!inv.getStackInSlot(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }
    public static ItemStack grow(ItemStack s, int i) {
        s.grow(i);
        return s;
    }
    public static ItemStack shrink(ItemStack s, int i) {
        s.shrink(i);
        return s;
    }
    public static ItemStack shrinkForContainer(ItemStack s, int i) {
        ItemStack sc = s.copy();
        s.shrink(i);
        if (s.isEmpty()) {
            return sc.getItem().getCraftingRemainingItem(sc);
        }
        return s;
    }
}
