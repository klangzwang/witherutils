package geni.witherutils.core.common.sync;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.function.Supplier;

public class NBTSerializableDataSlot<T extends INBTSerializable<CompoundTag>> extends EnderDataSlot<T> {

    private final Callback setterCallback;

    public NBTSerializableDataSlot(Supplier<T> getter, SyncMode syncMode)
    {
        this(getter, syncMode, () -> {});
    }

    public NBTSerializableDataSlot(Supplier<T> getter, SyncMode syncMode, Callback setterCallback)
    {
        super(getter, null, syncMode);
        this.setterCallback = setterCallback;
    }

    @Override
    public CompoundTag toFullNBT() {
        return getter().get().serializeNBT();
    }

    @Override
    protected T fromNBT(CompoundTag nbt)
    {
        return null;
    }

    @Override
    public void handleNBT(CompoundTag tag)
    {
        getter().get().deserializeNBT(tag);
        setterCallback.call();
    }

    public interface Callback
    {
        void call();
    }
}
