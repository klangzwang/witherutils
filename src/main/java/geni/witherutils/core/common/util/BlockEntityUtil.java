package geni.witherutils.core.common.util;

import java.util.List;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.items.IItemHandler;

public class BlockEntityUtil
{
    @SuppressWarnings("unchecked")
	public static <T extends BlockEntity> Optional<T> getTileEntityAt(BlockGetter w, BlockPos pos, Class<T> cls)
    {
        if (w != null && pos != null)
        {
            BlockEntity te = w.getBlockEntity(pos);
            if (te != null && cls.isAssignableFrom(te.getClass()))
            {
                return Optional.of((T) te);
            }
        }
        return Optional.empty();
    }
	
	@SuppressWarnings("unchecked")
	public static <T extends BlockEntity> T getBlockEntity(Class<T> type, BlockGetter world, BlockPos pos)
	{
		var tile = world.getBlockEntity(pos);
		return type.isInstance(tile) ? (T) tile : null;
	}

	public static void dropInventory(BlockEntity tile, IItemHandler inventory)
	{
		var pos = tile.getBlockPos();
		for (var i = 0; i < inventory.getSlots(); i++)
		{
			var stack = inventory.getStackInSlot(i);
			if (!stack.isEmpty())
				Containers.dropItemStack(tile.getLevel(), pos.getX(), pos.getY(), pos.getZ(), stack);
		}
	}

	@SuppressWarnings("resource")
	public static void sendBlockEntityToClients(BlockEntity tile)
	{
		var world = (ServerLevel) tile.getLevel();
		var entities = world.getChunkSource().chunkMap.getPlayers(new ChunkPos(tile.getBlockPos()), false);
		var packet = ClientboundBlockEntityDataPacket.create(tile, BlockEntity::saveWithoutMetadata);
		for (var e : entities)
			e.connection.send(packet);
	}

    public static void sendUpdatePacket(BlockEntity tileEntity)
    {
        Packet<ClientGamePacketListener> packet = tileEntity.getUpdatePacket();
        if(packet != null)
        {
            sendUpdatePacket(tileEntity.getLevel(), tileEntity.getBlockPos(), packet);
        }
    }

    public static void sendUpdatePacket(BlockEntity blockEntity, CompoundTag compound)
    {
        addIdAndPosition(blockEntity, compound);
        ClientboundBlockEntityDataPacket packet = ClientboundBlockEntityDataPacket.create(blockEntity, e -> compound);
        sendUpdatePacket(blockEntity.getLevel(), blockEntity.getBlockPos(), packet);
    }

    public static void sendUpdatePacketSimple(BlockEntity blockEntity, CompoundTag compound)
    {
        ResourceLocation id = BlockEntityType.getKey(blockEntity.getType());
        compound.putString("id", id.toString());
        compound.putInt("x", blockEntity.getBlockPos().getX());
        compound.putInt("y", blockEntity.getBlockPos().getY());
        compound.putInt("z", blockEntity.getBlockPos().getZ());
        ClientboundBlockEntityDataPacket packet = ClientboundBlockEntityDataPacket.create(blockEntity, e -> compound);
        sendUpdatePacket(blockEntity.getLevel(), blockEntity.getBlockPos(), packet);
    }

    @SuppressWarnings("resource")
	private static void sendUpdatePacket(Level level, BlockPos pos, Packet<ClientGamePacketListener> packet)
    {
        if(level instanceof ServerLevel server)
        {
            List<ServerPlayer> players = server.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false);
            players.forEach(player -> player.connection.send(packet));
        }
    }

    private static void addIdAndPosition(BlockEntity blockEntity, CompoundTag tag)
    {
        ResourceLocation id = BlockEntityType.getKey(blockEntity.getType());
        if(id != null)
        {
            tag.putString("id", id.toString());
            tag.putInt("x", blockEntity.getBlockPos().getX());
            tag.putInt("y", blockEntity.getBlockPos().getY());
            tag.putInt("z", blockEntity.getBlockPos().getZ());
        }
    }
}
