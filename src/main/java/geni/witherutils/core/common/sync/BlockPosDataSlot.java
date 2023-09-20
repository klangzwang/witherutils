package geni.witherutils.core.common.sync;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

import java.util.function.Consumer;
import java.util.function.Supplier;

import geni.witherutils.core.common.util.NBTUtil;

public class BlockPosDataSlot extends EnderDataSlot<BlockPos> {

    public BlockPosDataSlot(Supplier<BlockPos> getter, Consumer<BlockPos> setter, SyncMode mode)
    {
        super(getter, setter, mode);
    }

    @Override
    public CompoundTag toFullNBT()
    {
        CompoundTag tag = new CompoundTag();
        NBTUtil.putBlockPos(tag, getter().get());
        return tag;
    }

    @Override
    protected BlockPos fromNBT(CompoundTag nbt)
    {
    	return NBTUtil.getBlockPos(nbt);
    }
}
