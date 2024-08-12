package geni.witherutils.core.common.network;

import geni.witherutils.api.WitherUtilsRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketDirectionDash(long time, byte dir) implements CustomPacketPayload {

	public static final Type<PacketDirectionDash> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(WitherUtilsRegistry.MODID, "directiondash"));

	public static final StreamCodec<RegistryFriendlyByteBuf, PacketDirectionDash> CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_LONG, PacketDirectionDash::time,
        ByteBufCodecs.BYTE, PacketDirectionDash::dir,
        PacketDirectionDash::new
	);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return TYPE;
	}
	
	public static void handle(PacketDirectionDash message, IPayloadContext ctx)
	{
		ctx.enqueueWork(() -> {
	        if (ctx.flow().isClientbound())
	        {
	            message.handleClientSide(ctx.player());
	        }
	        else if (ctx.player() instanceof ServerPlayer sp)
	        {
	            message.handleServerSide(sp);
	        }
		});
    }
    
    private void handleClientSide(Player player)
    {
    }

    private void handleServerSide(ServerPlayer player)
    {
        double strength = 1.3D;
        double velocity = 0.4D;
        
        Vec3 look = player.getLookAngle().multiply(strength, 0, strength).normalize();
        
        switch (dir)
        {
            case 0:
                player.push(look.x, velocity, look.z);
                break;
            case 1:
                player.push(-look.x, velocity, -look.z);
                break;
            case 2:
                player.push(look.z, velocity, -look.x);
                break;
            case 3:
                player.push(-look.z, velocity, look.x);
                break;
        }
        player.connection.send(new ClientboundSetEntityMotionPacket(player.getId(), player.getDeltaMovement()));
        player.getFoodData().addExhaustion((float) 0.4);
    }
}
