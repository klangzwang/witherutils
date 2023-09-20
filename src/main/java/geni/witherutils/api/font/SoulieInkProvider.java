package geni.witherutils.api.font;

import org.jetbrains.annotations.NotNull;

import geni.witherutils.base.common.init.WUTCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class SoulieInkProvider implements ISoulieTextProvider, ICapabilitySerializable<CompoundTag> {

    private boolean hasSoulieInk = false;

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, Direction facing)
    {
        return capability == WUTCapabilities.SOULIETEXT ? LazyOptional.of(() -> this).cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("ink", this.hasSoulieInk);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag)
    {
        this.hasSoulieInk = tag.getBoolean("ink");
    }

    @Override
    public boolean hasSoulieInk()
    {
        return this.hasSoulieInk;
    }

    @Override
    public void setSoulieInk(boolean hasInk)
    {
        this.hasSoulieInk = hasInk;
    }
}
