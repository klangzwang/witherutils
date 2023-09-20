package geni.witherutils.core.common.sync;

import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BlockStateDataSlot extends EnderDataSlot<BlockState> {

    public BlockStateDataSlot(Supplier<BlockState> getter, Consumer<BlockState> setter, SyncMode syncMode)
    {
        super(getter, setter, syncMode);
    }

    @Override
    public CompoundTag toFullNBT()
    {
    	CompoundTag tag = new CompoundTag();
        if (getter().get() != null)
            tag.put("cover", NbtUtils.writeBlockState(getter().get()));
        return tag;
    }

    @SuppressWarnings({ "resource", "deprecation" })
	@Override
    public BlockState fromNBT(CompoundTag tag)
    {
    	Level level = Minecraft.getInstance().level;
    	return tag.contains("cover") ? NbtUtils.readBlockState(level != null ? level.holderLookup(Registries.BLOCK) :
    									BuiltInRegistries.BLOCK.asLookup(), tag.getCompound("cover")) : null;
    }
}
