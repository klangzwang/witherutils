package geni.witherutils.api.capability;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

public interface IWitherCapabilityProvider<T> {

    Capability<T> getCapabilityType();

    LazyOptional<T> getCapability(@Nullable Direction side);
    
    void invalidateSide(@Nullable Direction side);
    
    void invalidateCaps();
}
