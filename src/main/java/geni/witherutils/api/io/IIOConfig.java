package geni.witherutils.api.io;

import geni.witherutils.api.UseOnly;
import geni.witherutils.api.capability.IWitherCapabilityProvider;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.LogicalSide;

public interface IIOConfig extends INBTSerializable<CompoundTag>, IWitherCapabilityProvider<ISideConfig> {

    IOMode getMode(Direction side);

    void setMode(Direction side, IOMode state);

    default void cycleMode(Direction side)
    {
        IOMode currentMode = getMode(side);

        int curOrd = currentMode.ordinal();
        int nextOrd = (curOrd + 1) % IOMode.values().length;

        while (nextOrd != curOrd)
        {
            IOMode next = IOMode.values()[nextOrd];
            if (supportsMode(side, next))
            {
                setMode(side, next);
                break;
            }
            nextOrd = (nextOrd + 1) % IOMode.values().length;
        }
    }

    boolean supportsMode(Direction side, IOMode state);

    @UseOnly(LogicalSide.CLIENT)
    boolean renderOverlay();
}
