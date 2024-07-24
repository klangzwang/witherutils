package geni.witherutils.core.common.network;

import geni.witherutils.core.common.blockentity.WitherBlockEntity;
import io.netty.buffer.Unpooled;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {

    private static final ServerPayloadHandler INSTANCE = new ServerPayloadHandler();

    public static ServerPayloadHandler getInstance()
    {
        return INSTANCE;
    }

    @SuppressWarnings("deprecation")
	public void handleDataSlotChange(ClientboundDataSlotChange change, IPayloadContext context)
    {
        context.enqueueWork(() -> {
            var level = context.player().level();
            BlockEntity be = level.getBlockEntity(change.pos());
            if (be instanceof WitherBlockEntity witherBlockEntity)
            {
                var buf = new RegistryFriendlyByteBuf(Unpooled.wrappedBuffer(change.updateData()), level.registryAccess());
                witherBlockEntity.serverHandleBufferChange(buf);
            }
        });
    }
}
