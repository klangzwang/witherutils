package geni.witherutils.api.nbt;

import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface INamedNBTSerializable<T extends Tag> extends INBTSerializable<T> {

    String getSerializedName();
}
