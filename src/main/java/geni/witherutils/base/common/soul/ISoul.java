package geni.witherutils.base.common.soul;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface ISoul {
	
    float getPower();
    
    IWorldSoulMultiplier getMultiplier();
    
    int frequency();
    
    void powerChanged(final boolean p0);
    
    @Nullable
    Level world();
    
    String getName();
    
    boolean isLoaded();
    
    @Nullable
    BlockPos getLocation();
}
