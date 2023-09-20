package geni.witherutils.core.common.network;

import java.util.Optional;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent.Context;

public class PacketDirectionDash implements Packet {

    private long time;
    private byte dir;
    
    public PacketDirectionDash(long ti, byte direction)
    {
        this.time = ti;
        this.dir = direction;
    }
    
    public PacketDirectionDash(FriendlyByteBuf buf)
    {
        time = buf.readLong();
        dir = buf.readByte();
    }

    protected void write(FriendlyByteBuf writeInto)
    {
        writeInto.writeLong(time);
        writeInto.writeByte(dir);
    }
    
    @Override
    public boolean isValid(Context context)
    {
        return context.getDirection() == NetworkDirection.PLAY_TO_SERVER;
    }

    @Override
    public void handle(Context context)
    {
        ServerPlayer player = context.getSender();
        
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

    public static class Handler extends PacketHandler<PacketDirectionDash>
    {
        @Override
        public PacketDirectionDash fromNetwork(FriendlyByteBuf buf)
        {
            return new PacketDirectionDash(buf);
        }
        @Override
        public Optional<NetworkDirection> getDirection()
        {
            return Optional.of(NetworkDirection.PLAY_TO_SERVER);
        }
        @Override
        public void toNetwork(PacketDirectionDash packet, FriendlyByteBuf buf)
        {
            packet.write(buf);
        }
    }
}
