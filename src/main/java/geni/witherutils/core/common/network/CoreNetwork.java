package geni.witherutils.core.common.network;

import java.util.ArrayList;
import java.util.List;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.common.data.PlayerData;
import geni.witherutils.base.common.data.WorldData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class CoreNetwork {
	
    public static final List<IDataHandler> DATA_HANDLERS = new ArrayList<>();
    private static final String PROTOCOL_VERSION = "1.0";

    @SubscribeEvent
	public static void register(final RegisterPayloadHandlersEvent event)
	{
		final PayloadRegistrar registrar = event.registrar(WitherUtilsRegistry.MODID)
			.versioned(PROTOCOL_VERSION)
			.optional();

        registrar.playToClient(ServerboundCDataSlotUpdate.TYPE, ServerboundCDataSlotUpdate.STREAM_CODEC, ClientPayloadHandler.getInstance()::handleDataSlotUpdate);
        registrar.playToServer(ClientboundDataSlotChange.TYPE, ClientboundDataSlotChange.STREAM_CODEC, ServerPayloadHandler.getInstance()::handleDataSlotChange);
        
		registrar.playToClient(PacketSoulsSync.TYPE, PacketSoulsSync.CODEC, PacketSoulsSync::handle);
		registrar.playToServer(PacketRotateBlock.TYPE, PacketRotateBlock.CODEC, PacketRotateBlock::handle);
		registrar.playToServer(PacketDirectionDash.TYPE, PacketDirectionDash.CODEC, PacketDirectionDash::handle);
        registrar.playToClient(PacketServerToClient.ID, PacketServerToClient.CODEC, PacketServerToClient::handle);
		
        DATA_HANDLERS.add(SYNC_PLAYER_DATA);
        DATA_HANDLERS.add(PLAYER_DATA_TO_SERVER);
    }
    
    public static final IDataHandler SYNC_PLAYER_DATA = new IDataHandler()
    {
        @Override
        public void handleData(CompoundTag compound, IPayloadContext context)
        {
            CompoundTag dataTag = compound.getCompound("Data");
            Player player = context.player();
            
            if (player != null)
            {
                PlayerData.getDataFromPlayer(player).readFromNBT(dataTag, false);
            }
        }
    };
    
    public static final IDataHandler PLAYER_DATA_TO_SERVER = (compound, context) -> {
    	
        if (context.player() != null)
        {
            Level level = context.player().getServer().getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.tryParse(compound.getString("World"))));
            Player player = level.getServer().getPlayerList().getPlayer(compound.getUUID("UUID"));
            
            if (player != null)
            {
                WorldData.get(level).setDirty();
            }
        }
    };
    
    public static void sendToAll(CustomPacketPayload message)
    {
		PacketDistributor.sendToAllPlayers(message);
    }

    public static void sendToPlayer(CustomPacketPayload message, ServerPlayer player)
    {
		PacketDistributor.sendToPlayer(player, message);
    }

	public static void sendToAllTracking(CustomPacketPayload message, Entity entity)
	{
		PacketDistributor.sendToPlayersTrackingEntity(entity, message);
	}

	public static void sendToAllTracking(CustomPacketPayload message, Level level, BlockPos pos)
	{
		if (level instanceof ServerLevel serverLevel)
		{
			PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(pos), message);
		}
	}

	public static void sendToAllTracking(CustomPacketPayload message, BlockEntity te)
	{
    	if (te.getLevel() != null)
    	{
    		sendToAllTracking(message, te.getLevel(), te.getBlockPos());
		}
    }

	public static void sendToDimension(CustomPacketPayload message, ServerLevel level)
	{
		PacketDistributor.sendToPlayersInDimension(level, message);
    }

    public static void sendToServer(CustomPacketPayload message)
    {
		PacketDistributor.sendToServer(message);
    }

	public static void sendNonLocal(CustomPacketPayload packet)
	{
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if (server != null)
		{
			if (server.isDedicatedServer())
			{
				sendToAll(packet);
			}
			else
			{
				for (ServerPlayer player : server.getPlayerList().getPlayers())
				{
					if (!player.server.isSingleplayerOwner(player.getGameProfile()))
					{
						sendToPlayer(packet, player);
					}
				}
			}
		}
	}

	public static void sendNonLocal(ServerPlayer player, CustomPacketPayload packet)
	{
		if (!player.server.isSingleplayerOwner(player.getGameProfile()))
		{
			sendToPlayer(packet, player);
		}
	}
}
