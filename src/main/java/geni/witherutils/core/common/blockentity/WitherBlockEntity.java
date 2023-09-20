package geni.witherutils.core.common.blockentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import geni.witherutils.api.UseOnly;
import geni.witherutils.api.capability.IWitherCapabilityProvider;
import geni.witherutils.core.common.sync.EnderDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.LogicalSide;

public class WitherBlockEntity extends BlockEntity implements Container {
	
    private final List<UUID> lastSyncedToPlayers = new ArrayList<>();
    private final List<EnderDataSlot<?>> dataSlots = new ArrayList<>();
    private final List<EnderDataSlot<?>> clientDecidingDataSlots = new ArrayList<>();
    private final Map<Capability<?>, IWitherCapabilityProvider<?>> capabilityProviders = new HashMap<>();

    public WitherBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState)
    {
        super(type, worldPosition, blockState);
    }
	
    public static void tick(Level level, BlockPos pos, BlockState state, WitherBlockEntity blockEntity)
    {
        if (level.isClientSide)
        {
            blockEntity.clientTick();
        }
        else
        {
            blockEntity.serverTick();
        }
    }
    public void serverTick()
    {
        if (level != null && !level.isClientSide)
        {
            sync();
            setChanged();
        }
    }
    public void clientTick()
    {
    }
    public boolean isClientSide()
    {
        if (level != null)
            return level.isClientSide;
        return false;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        return createUpdatePacket(false, SyncMode.WORLD);
    }
    
    @Nullable
    public ClientboundBlockEntityDataPacket createUpdatePacket(boolean fullUpdate, SyncMode mode)
    {
        CompoundTag nbt = new CompoundTag();
        ListTag listNBT = new ListTag();
        for (int i = 0; i < this.dataSlots.size(); i++)
        {
            EnderDataSlot<?> dataSlot = this.dataSlots.get(i);
            if (dataSlot.getSyncMode() == mode)
            {
                Optional<CompoundTag> optionalNBT = fullUpdate ? Optional.of(dataSlot.toFullNBT()) : dataSlot.toOptionalNBT();
                if (optionalNBT.isPresent())
                {
                    CompoundTag elementNBT = optionalNBT.get();
                    elementNBT.putInt("dataSlotIndex", i);
                    listNBT.add(elementNBT);
                }
            }
        }

        if (listNBT.isEmpty())
            return null;

        nbt.put("data", listNBT);
        return new ClientboundBlockEntityDataPacket(getBlockPos(), getType(), nbt);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
    {
        CompoundTag nbt = pkt.getTag();
        if (nbt != null && nbt.contains("data", Tag.TAG_LIST)) {
            ListTag listNBT = nbt.getList("data", Tag.TAG_COMPOUND);
            for (Tag tag : listNBT)
            {
                CompoundTag elementNBT = (CompoundTag) tag;
                int dataSlotIndex = elementNBT.getInt("dataSlotIndex");
                dataSlots.get(dataSlotIndex).handleNBT(elementNBT);
            }
        }
    }

    public void addDataSlot(EnderDataSlot<?> slot)
    {
        dataSlots.add(slot);
    }
    public void addClientDecidingDataSlot(EnderDataSlot<?> slot)
    {
        clientDecidingDataSlots.add(slot);
    }
    public void add2WayDataSlot(EnderDataSlot<?> slot)
    {
        addDataSlot(slot);
        addClientDecidingDataSlot(slot);
    }

    @UseOnly(LogicalSide.SERVER)
    private void sync()
    {
        ClientboundBlockEntityDataPacket fullUpdate = createUpdatePacket(true, SyncMode.WORLD);
        ClientboundBlockEntityDataPacket partialUpdate = getUpdatePacket();

        List<UUID> currentlyTracking = new ArrayList<>();

        getTrackingPlayers().forEach(serverPlayer -> {
            currentlyTracking.add(serverPlayer.getUUID());
            if (lastSyncedToPlayers.contains(serverPlayer.getUUID()))
            {
                sendPacket(serverPlayer, partialUpdate);
            }
            else
            {
                sendPacket(serverPlayer, fullUpdate);
            }
        });
        lastSyncedToPlayers.clear();
        lastSyncedToPlayers.addAll(currentlyTracking);
    }

    public void sendPacket(ServerPlayer player, @Nullable Packet<?> packet)
    {
        if (packet != null)
            player.connection.send(packet);
    }

    @SuppressWarnings("resource")
    @UseOnly(LogicalSide.SERVER)
    private List<ServerPlayer> getTrackingPlayers()
    {
        return ((ServerChunkCache)level.getChunkSource()).chunkMap.getPlayers(new ChunkPos(worldPosition), false);
    }

    public List<EnderDataSlot<?>> getDataSlots()
    {
        return dataSlots;
    }

    public List<EnderDataSlot<?>> getClientDecidingDataSlots()
    {
        return clientDecidingDataSlots;
    }

    public Map<Capability<?>, IWitherCapabilityProvider<?>> getCapabilityProviders()
    {
        return capabilityProviders;
    }
    public void addCapabilityProvider(IWitherCapabilityProvider<?> provider)
    {
        capabilityProviders.put(provider.getCapabilityType(), provider);
    }
    public void invalidateCaps(@Nullable Direction side)
    {
        for (IWitherCapabilityProvider<?> capProvider : capabilityProviders.values())
        {
            capProvider.invalidateCaps();
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side)
    {
        if (capabilityProviders.containsKey(cap))
        {
            return capabilityProviders.get(cap).getCapability(side).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps()
    {
        super.invalidateCaps();
        for (IWitherCapabilityProvider<?> provider : capabilityProviders.values())
        {
            provider.invalidateCaps();
        }
    }

    public void onNeighborBlockChange(BlockState state, Level level, BlockPos pos, Block block, BlockPos newpos, boolean value)
    {
    }
    public void onBlockPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
    }
    public void onReplaced(Level world, BlockPos pos, BlockState state, BlockState newstate)
    {
    }
    public void onRemoved(Level level, BlockState state, BlockState newstate, boolean isMoving)
    {
    }
    public void rotateBlock(Rotation axis)
    {
    }
    public void onAdded(Level world, BlockState state, BlockState oldState, boolean isMoving)
    {
    }
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        return InteractionResult.PASS;
    }
    
    public Direction getCurrentFacing()
    {
        if(this.getBlockState().hasProperty(BlockStateProperties.FACING))
        {
            return this.getBlockState().getValue(BlockStateProperties.FACING);
        }
        if(this.getBlockState().hasProperty(BlockStateProperties.HORIZONTAL_FACING))
        {
            return this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        }
        return null;
    }

    /**
     * 
     * RECIPE
     * 
     */
    @Deprecated
    @Override
    public int getContainerSize()
    {
        return 0;
    }
    @Deprecated
    @Override
    public boolean isEmpty()
    {
        return true;
    }
    @Deprecated
    @Override
    public ItemStack getItem(int index)
    {
        return ItemStack.EMPTY;
    }
    @Deprecated
    @Override
    public ItemStack removeItem(int index, int count)
    {
        return ItemStack.EMPTY;
    }
    @Deprecated
    @Override
    public ItemStack removeItemNoUpdate(int index)
    {
        return ItemStack.EMPTY;
    }
    @Deprecated
    @Override
    public void setItem(int index, ItemStack stack)
    {
    }
    @Deprecated
    @Override
    public void clearContent()
    {
    }
    
    public void popExperience(ServerPlayer serverplayer, int count) {}
    
    public boolean stillValid(Player pPlayer)
    {
        if (this.level.getBlockEntity(this.worldPosition) != this)
            return false;
        return pPlayer.distanceToSqr(this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 0.5D, this.worldPosition.getZ() + 0.5D) <= 64.0D;
    }
    
    @Override
    public CompoundTag getUpdateTag()
    {
        CompoundTag syncData = super.getUpdateTag();
        this.saveAdditional(syncData);
        return syncData;
    }
    
    public void setGhostSlotContents(int slot, ItemStack stack) {}
}
