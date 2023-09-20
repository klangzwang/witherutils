package geni.witherutils.core.common.network;

import java.util.Optional;

import geni.witherutils.core.common.util.PlaceBlocksUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent.Context;

public class PacketRotateBlock implements Packet {

    private BlockPos pos;
    private Direction side;
    private InteractionHand hand;
    
    public PacketRotateBlock(BlockPos mouseover, Direction s, InteractionHand hand)
    {
        pos = mouseover;
        side = s;
        this.hand = hand;
    }

    public PacketRotateBlock(FriendlyByteBuf buf)
    {
        pos = buf.readBlockPos();
        side = Direction.values()[buf.readInt()];
        hand = InteractionHand.values()[buf.readInt()];
    }
    
    protected void write(FriendlyByteBuf writeInto)
    {
        writeInto.writeBlockPos(pos);
        writeInto.writeInt(side.ordinal());
        writeInto.writeInt(hand.ordinal());
    }
    
    @Override
    public boolean isValid(Context context)
    {
        return context.getDirection() == NetworkDirection.PLAY_TO_SERVER;
    }

    @Override
    public void handle(Context context)
    {
        Level world = context.getSender().level();
        PlaceBlocksUtil.rotateBlockValidState(world, pos, side);
    }

    public static class Handler extends PacketHandler<PacketRotateBlock>
    {
        @Override
        public PacketRotateBlock fromNetwork(FriendlyByteBuf buf)
        {
            return new PacketRotateBlock(buf);
        }
        @Override
        public Optional<NetworkDirection> getDirection()
        {
            return Optional.of(NetworkDirection.PLAY_TO_SERVER);
        }
        @Override
        public void toNetwork(PacketRotateBlock packet, FriendlyByteBuf buf)
        {
            packet.write(buf);
        }
    }
}
