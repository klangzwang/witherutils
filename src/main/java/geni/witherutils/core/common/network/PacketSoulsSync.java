package geni.witherutils.core.common.network;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.common.init.WUTAttachments;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketSoulsSync(int souls) implements CustomPacketPayload {

	public static final Type<PacketSoulsSync> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(WitherUtilsRegistry.MODID, "soulssync"));

	public static final StreamCodec<RegistryFriendlyByteBuf, PacketSoulsSync> CODEC = StreamCodec.composite(
        ByteBufCodecs.INT, PacketSoulsSync::souls,
        PacketSoulsSync::new
	);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return TYPE;
	}
	
	public static void handle(PacketSoulsSync message, IPayloadContext ctx)
	{
		ctx.enqueueWork(() -> {
			ctx.player().setData(WUTAttachments.SOULS_CONTROL, message.souls);
//			ClientSoulsData.setSouls(message.souls);
		});
    }
}