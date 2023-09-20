package geni.witherutils.core.common.network;

import java.util.Optional;

import geni.witherutils.base.common.soul.ClientSoul;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public class PacketSyncServerSouls implements Packet {

    float powerCreated;
    float powerUsed;

    public PacketSyncServerSouls(final float powerCreated, final float powerUsed)
    {
        this.powerCreated = powerCreated;
        this.powerUsed = powerUsed;
    }

    public PacketSyncServerSouls(FriendlyByteBuf buf)
    {
        this.powerCreated = buf.readFloat();
        this.powerUsed = buf.readFloat();
    }

    @Override
    public boolean isValid(NetworkEvent.Context context)
    {
        return context.getDirection() == NetworkDirection.PLAY_TO_CLIENT;
    }

    protected void write(FriendlyByteBuf buf)
    {
    	buf.writeFloat(this.powerCreated);
    	buf.writeFloat(this.powerUsed);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle(NetworkEvent.Context context)
    {
        ClientSoul.powerCreated = this.powerCreated;
        ClientSoul.powerDrained = this.powerUsed;
    }

    public static class Handler extends PacketHandler<PacketSyncServerSouls>
    {
        @Override
        public PacketSyncServerSouls fromNetwork(FriendlyByteBuf buf)
        {
            return new PacketSyncServerSouls(buf);
        }
        @Override
        public Optional<NetworkDirection> getDirection()
        {
            return Optional.of(NetworkDirection.PLAY_TO_CLIENT);
        }
        @Override
        public void toNetwork(PacketSyncServerSouls packet, FriendlyByteBuf buf)
        {
            packet.write(buf);
        }
    }
}
