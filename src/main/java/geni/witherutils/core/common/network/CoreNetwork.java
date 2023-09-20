package geni.witherutils.core.common.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class CoreNetwork {
    
    private static final String PROTOCOL_VERSION = "1.0";
    private static SimpleChannel CHANNEL;

    private static int packetId = 0;

    public static void networkInit()
    {
        CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation("witherutils", "network"))
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .simpleChannel();

        registerPacket(new PacketClientToServerMenu.Handler<>(PacketSyncClientToServerMenu::new), PacketSyncClientToServerMenu.class);
        registerPacket(new EmitParticlePacket.Handler(), EmitParticlePacket.class);
        registerPacket(new EmitParticlesPacket.Handler(), EmitParticlesPacket.class);
        registerPacket(new PacketRotateBlock.Handler(), PacketRotateBlock.class);
        registerPacket(new PacketDirectionDash.Handler(), PacketDirectionDash.class);
        registerPacket(new PacketTravelEvent.Handler(), PacketTravelEvent.class);
        registerPacket(new PacketStartupShutdown.Handler(), PacketStartupShutdown.class);
        registerPacket(new PacketSyncServerSouls.Handler(), PacketSyncServerSouls.class);
        registerPacket(new PacketSoulInfo.Handler(), PacketSoulInfo.class);
    }

    public static <P extends Packet> void sendToServer(P packet)
    {
        CHANNEL.sendToServer(packet);
    }
    public static <P extends Packet> void sendToPlayer(ServerPlayer player, P packet)
    {
        send(PacketDistributor.PLAYER.with(() -> player), packet);
    }
    public static <P extends Packet> void sendToTracking(LevelChunk chunk, P packet)
    {
        send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), packet);
    }
    public static <P extends Packet> void send(PacketDistributor.PacketTarget target, P packet)
    {
        CHANNEL.send(target, packet);
    }
    public static <P extends Packet> void registerPacket(Packet.PacketHandler<P> handler, Class<P> clazz)
    {
        CHANNEL.registerMessage(id(), clazz, handler::toNetwork, handler::fromNetwork, handler, handler.getDirection());
    }
    private static int id()
    {
        return packetId++;
    }
}