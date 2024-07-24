package geni.witherutils.core.common.network;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.core.common.util.PlaceBlocksUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketRotateBlock(BlockPos pos, Direction side) implements CustomPacketPayload {

	public static final Type<PacketRotateBlock> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(WitherUtilsRegistry.MODID, "wrenchrotation"));

	public static final StreamCodec<RegistryFriendlyByteBuf, PacketRotateBlock> CODEC = StreamCodec.composite(
		BlockPos.STREAM_CODEC, PacketRotateBlock::pos,
		Direction.STREAM_CODEC, PacketRotateBlock::side,
        PacketRotateBlock::new
	);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return TYPE;
	}
	
	public static void handle(PacketRotateBlock message, IPayloadContext ctx)
	{
		ctx.enqueueWork(() -> {
	        Level world = ctx.player().level();
	        PlaceBlocksUtil.rotateBlockValidState(world, message.pos, message.side);
		});
    }
}
