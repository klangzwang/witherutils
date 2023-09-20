package geni.witherutils.core.common.sync;

import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class ItemStackDataSlot extends EnderDataSlot<ItemStack> {

    public ItemStackDataSlot(Supplier<ItemStack> getter, Consumer<ItemStack> setter, SyncMode syncMode)
    {
        super(getter, setter, syncMode);
    }

    @Override
    public CompoundTag toFullNBT()
    {
        return getter().get().save(new CompoundTag());
    }

    @Override
    public ItemStack fromNBT(CompoundTag nbt)
    {
        return ItemStack.of(nbt);
    }
}
