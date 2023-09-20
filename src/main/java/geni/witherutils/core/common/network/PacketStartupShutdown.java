package geni.witherutils.core.common.network;

import java.util.Optional;

import geni.witherutils.base.common.base.WitherMachineEnergyFakeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent.Context;

public class PacketStartupShutdown implements Packet {

    private BlockPos pos;
	private boolean field;
	
    public PacketStartupShutdown(BlockPos pos, boolean field)
    {
        this.pos = pos;
		this.field = field;
    }

    public PacketStartupShutdown(FriendlyByteBuf buf)
    {
		pos = buf.readBlockPos();
		field = buf.readBoolean();
    }
    
    protected void write(FriendlyByteBuf buf)
    {
    	buf.writeBlockPos(pos);
    	buf.writeBoolean(field);
    }
    
    @Override
    public boolean isValid(Context context)
    {
        return context.getDirection() == NetworkDirection.PLAY_TO_SERVER;
    }
    
	@Override
	public void handle(Context ctx)
	{
		ServerPlayer player = ctx.getSender();
		Level world = player.getCommandSenderWorld();
		BlockEntity tile = world.getBlockEntity(pos);
		
		if (tile instanceof WitherMachineEnergyFakeBlockEntity)
		{
			WitherMachineEnergyFakeBlockEntity base = (WitherMachineEnergyFakeBlockEntity) tile;
			base.setCanInteract(field);
			BlockState oldState = world.getBlockState(pos);
			world.sendBlockUpdated(pos, oldState, oldState, Block.UPDATE_ALL);
		}
	}

    public static class Handler extends PacketHandler<PacketStartupShutdown>
    {
        @Override
        public PacketStartupShutdown fromNetwork(FriendlyByteBuf buf)
        {
            return new PacketStartupShutdown(buf);
        }
        @Override
        public Optional<NetworkDirection> getDirection()
        {
            return Optional.of(NetworkDirection.PLAY_TO_SERVER);
        }
        @Override
        public void toNetwork(PacketStartupShutdown packet, FriendlyByteBuf buf)
        {
            packet.write(buf);
        }
    }
}
