package geni.witherutils.core.common.network;

import java.util.Optional;

import javax.annotation.Nonnull;

import geni.witherutils.api.teleport.TeleportEntityEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent.Context;

public class PacketTravelEvent implements Packet {

	long pos;
	int powerUse;
	boolean conserveMotion;

    public PacketTravelEvent(BlockPos pos, int powerUse, boolean conserveMotion)
    {
		this.pos = pos.asLong();
		this.powerUse = powerUse;
		this.conserveMotion = conserveMotion;
    }

    public PacketTravelEvent(FriendlyByteBuf buf)
    {
		pos = buf.readLong();
		powerUse = buf.readInt();
		conserveMotion = buf.readBoolean();
    }
    
    protected void write(FriendlyByteBuf buf)
    {
		buf.writeLong(pos);
		buf.writeInt(powerUse);
		buf.writeBoolean(conserveMotion);
    }
    
    @Override
    public boolean isValid(Context context)
    {
        return context.getDirection() == NetworkDirection.PLAY_TO_SERVER;
    }

    @Override
    public void handle(Context context)
    {
		Entity toTp = context.getSender();
		doServerTeleport(toTp, BlockPos.of(pos), powerUse, conserveMotion);
    }

	private boolean doServerTeleport(@Nonnull Entity toTp, @Nonnull BlockPos pos, int powerUse, boolean conserveMotion)
	{
		Player player = toTp instanceof Player ? (Player) toTp : null;

		TeleportEntityEvent evt = new TeleportEntityEvent(toTp, pos);
		if (MinecraftForge.EVENT_BUS.post(evt))
		{
			return false;
		}
		pos = evt.getTarget();

//		SoundHelper.playSound(toTp.world, toTp, source.sound, 1.0F, 1.0F);

		if (player != null)
		{
			player.setPos(pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5);
		}
		else
		{
			toTp.setPos(pos.getX(), pos.getY(), pos.getZ());
		}

//		SoundHelper.playSound(toTp.world, toTp, source.sound, 1.0F, 1.0F);

		toTp.fallDistance = 0;

		if (player != null)
		{
//			if (conserveMotion)
//			{
//				Vector3d velocityVex = Utils.getLookVecWU(player);
//				SPacketEntityVelocity p = new SPacketEntityVelocity(toTp.getEntityId(), velocityVex.x, velocityVex.y, velocityVex.z);
//				((AbstractClientPlayer) player).connection.sendPacket(p);
//			}
//			if (powerUse > 0) {
//				ItemStack heldItem = player.getHeldItem(hand);
//				if (heldItem.getItem() instanceof IItemOfTravel) {
//					ItemStack item = heldItem.copy();
//					((IItemOfTravel) item.getItem()).extractInternal(item, powerUse);
//					player.setHeldItem(hand, item);
//				}
//			}
		}
		return true;
	}

    public static class Handler extends PacketHandler<PacketTravelEvent>
    {
        @Override
        public PacketTravelEvent fromNetwork(FriendlyByteBuf buf)
        {
            return new PacketTravelEvent(buf);
        }
        @Override
        public Optional<NetworkDirection> getDirection()
        {
            return Optional.of(NetworkDirection.PLAY_TO_SERVER);
        }
        @Override
        public void toNetwork(PacketTravelEvent packet, FriendlyByteBuf buf)
        {
            packet.write(buf);
        }
    }
}
