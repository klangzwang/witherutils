package geni.witherutils.api.soulorb;

import geni.witherutils.api.nbt.INamedNBTSerializable;
import net.minecraft.nbt.Tag;

public interface ISoulOrbData extends INamedNBTSerializable<Tag> {

    int getSouls();
    
    void setSouls(int souls);
    
    int getMaxSouls();
    
    boolean canReceive();
    
    @Override
    default String getSerializedName()
    {
        return "SoulOrbData";
    }
}
