package geni.witherutils.core.common.network;

import geni.witherutils.core.common.blockentity.WitherBlockEntity;
import io.netty.buffer.Unpooled;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {
	
    private static final ClientPayloadHandler INSTANCE = new ClientPayloadHandler();

    public static ClientPayloadHandler getInstance()
    {
        return INSTANCE;
    }

    @SuppressWarnings("deprecation")
	public void handleDataSlotUpdate(ServerboundCDataSlotUpdate update, IPayloadContext context)
    {
        context.enqueueWork(() -> {
            var level = context.player().level();
            BlockEntity be = level.getBlockEntity(update.pos());
            if (be instanceof WitherBlockEntity witherBlockEntity)
            {
                var buf = new RegistryFriendlyByteBuf(Unpooled.wrappedBuffer(update.slotData()), level.registryAccess());
                witherBlockEntity.clientHandleBufferSync(buf);
            }
        });
    }
}
