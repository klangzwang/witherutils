package geni.witherutils.core.common.sync;

import net.minecraft.nbt.CompoundTag;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UUIDDataSlot extends EnderDataSlot<UUID> {

    public UUIDDataSlot(Supplier<UUID> getter, Consumer<UUID> setter, SyncMode mode)
    {
        super(getter, setter, mode);
    }

    @Override
    public CompoundTag toFullNBT()
    {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("value", getter().get());
        return tag;
    }

    @Override
    protected UUID fromNBT(CompoundTag nbt)
    {
        return nbt.getUUID("value");
    }
}
