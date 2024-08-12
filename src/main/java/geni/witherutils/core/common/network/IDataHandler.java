package geni.witherutils.core.common.network;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface IDataHandler {

    void handleData(CompoundTag compound, IPayloadContext context);

}
