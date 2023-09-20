package geni.witherutils.base.common.io;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

import geni.witherutils.api.io.IIOConfig;
import geni.witherutils.api.io.IOMode;
import geni.witherutils.api.io.ISideConfig;
import geni.witherutils.base.common.init.WUTCapabilities;

public final class FixedIOConfig implements IIOConfig {
	
    private final IOMode mode;

    public FixedIOConfig(IOMode mode)
    {
        this.mode = mode;
    }

    @Override
    public IOMode getMode(Direction side)
    {
        return mode;
    }

    @Override
    public void setMode(Direction side, IOMode mode) {}

    @Override
    public void cycleMode(Direction side) {}

    @Override
    public boolean supportsMode(Direction side, IOMode mode)
    {
        return mode == this.mode;
    }

    @Override
    public boolean renderOverlay()
    {
        return false;
    }

    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Mode", mode.ordinal());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {}
    
    @Override
    public Capability<ISideConfig> getCapabilityType()
    {
        return WUTCapabilities.SIDECONFIG;
    }
    
    @Override
    public LazyOptional<ISideConfig> getCapability(@Nullable Direction side)
    {
        return LazyOptional.empty();
    }

    @Override
    public void invalidateSide(@Nullable Direction side) {}

    @Override
    public void invalidateCaps() {}
}
