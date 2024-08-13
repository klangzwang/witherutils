package geni.witherutils.core.common.blockentity;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import geni.witherutils.api.UseOnly;
import geni.witherutils.base.common.io.energy.IWitherEnergyStorage;
import geni.witherutils.core.common.network.ClientboundDataSlotChange;
import geni.witherutils.core.common.network.NetworkDataSlot;
import geni.witherutils.core.common.network.ServerboundCDataSlotUpdate;
import io.netty.buffer.Unpooled;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;

public class WitherBlockEntity extends BlockEntity {
	
    public static final String DATA = "Data";
    public static final String INDEX = "Index";
    
    private final List<NetworkDataSlot<?>> dataSlots = new ArrayList<>();
    private final List<Runnable> afterDataSync = new ArrayList<>();

    private final Map<BlockCapability<?, ?>, EnumMap<Direction, BlockCapabilityCache<?, ?>>> selfCapabilities = new HashMap<>();
    private final Map<BlockCapability<?, ?>, EnumMap<Direction, BlockCapabilityCache<?, ?>>> neighbourCapabilities = new HashMap<>();
    
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
            level.blockEntityChanged(worldPosition);
        }
    }
    public void clientTick()
    {
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries)
    {
        ListTag dataList = new ListTag();
        for (int i = 0; i < dataSlots.size(); i++)
        {
            var slot = dataSlots.get(i);
            var nbt = slot.save(registries, true);

            CompoundTag slotTag = new CompoundTag();
            slotTag.putInt(INDEX, i);
            slotTag.put(DATA, nbt);

            dataList.add(slotTag);
        }

        CompoundTag data = new CompoundTag();
        data.put(DATA, dataList);
        return data;
    }

    @Override
    public void handleUpdateTag(CompoundTag syncData, HolderLookup.Provider lookupProvider)
    {
        if (syncData.contains(DATA, Tag.TAG_LIST))
        {
            ListTag dataList = syncData.getList(DATA, Tag.TAG_COMPOUND);

            for (Tag dataEntry : dataList)
            {
                if (dataEntry instanceof CompoundTag slotData)
                {
                    int slotIdx = slotData.getInt(INDEX);
                    dataSlots.get(slotIdx).parse(lookupProvider, Objects.requireNonNull(slotData.get(DATA)));
                }
            }

            for (Runnable task : afterDataSync)
            {
                task.run();
            }
        }
    }

	private byte @Nullable [] createBufferSlotUpdate()
    {
	    @SuppressWarnings("deprecation")
        RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), level.registryAccess());
        int amount = 0;
        for (int i = 0; i < dataSlots.size(); i++)
        {
            var networkDataSlot = dataSlots.get(i);
            if (networkDataSlot.doesNeedUpdate())
            {
                amount ++;
                buf.writeInt(i);
                networkDataSlot.write(buf);
            }
        }

        if (amount == 0)
        {
            return null;
        }

        FriendlyByteBuf result = new FriendlyByteBuf(Unpooled.buffer());
        result.writeInt(amount);
        result.writeBytes(buf.copy());
        return result.array();
    }

    public <T extends NetworkDataSlot<?>> T addDataSlot(T slot)
    {
        dataSlots.add(slot);
        return slot;
    }

    public void addAfterSyncRunnable(Runnable runnable)
    {
        afterDataSync.add(runnable);
    }

	@UseOnly(LogicalSide.CLIENT)
    public <T> void clientUpdateSlot(@Nullable NetworkDataSlot<T> slot, T value)
    {
        if (slot == null)
        {
            return;
        }
        if (dataSlots.contains(slot))
        {
            @SuppressWarnings("deprecation")
            RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), level.registryAccess());
            buf.writeInt(dataSlots.indexOf(slot));
            slot.write(buf, value);
            PacketDistributor.sendToServer(new ClientboundDataSlotChange(getBlockPos(), buf.array()));
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_NEIGHBORS);
        }
    }

	@UseOnly(LogicalSide.SERVER)
    public void sync()
	{
        var syncData = createBufferSlotUpdate();
        if (syncData != null && level instanceof ServerLevel serverLevel)
        {
            PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(getBlockPos()),
                new ServerboundCDataSlotUpdate(getBlockPos(), syncData));
        }
    }

	@UseOnly(LogicalSide.CLIENT)
    public void clientHandleBufferSync(RegistryFriendlyByteBuf buf)
	{
        for (int amount = buf.readInt(); amount > 0; amount--)
        {
            int index = buf.readInt();
            dataSlots.get(index).read(buf);
        }
        for (Runnable task : afterDataSync)
        {
            task.run();
        }
    }

	@UseOnly(LogicalSide.SERVER)
    public void serverHandleBufferChange(RegistryFriendlyByteBuf buf)
	{
        int index;
        try
        {
            index = buf.readInt();
        }
        catch (Exception e)
        {
            throw new IllegalStateException("Invalid buffer was passed over the network to the server.");
        }
        dataSlots.get(index).read(buf);
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
        return Direction.SOUTH;
    }
    
    /*
     * 
     * RENDERING
     * 
     */
	@SuppressWarnings("deprecation")
	@OnlyIn(Dist.CLIENT)
    public void renderBakedModel(final BlockGetter world, final MultiBufferSource renderer, final BlockRenderDispatcher blockRenderer, final BakedModel model, PoseStack matrixstack)
    {
    	matrixstack.translate(0, 1, 0);
    	VertexConsumer vconsumer = renderer.getBuffer(RenderType.solid());
    	int light = LevelRenderer.getLightColor(level, worldPosition.above());
        blockRenderer.getModelRenderer().renderModel(matrixstack.last(), vconsumer, world.getBlockState(worldPosition), model, 0f, 0f, 0f, light, OverlayTexture.NO_OVERLAY);
    }
	
    /**
     * 
     * BLOCK
     * 
     */
    public void onNeighborBlockChange(BlockState state, LevelReader level, BlockPos pos, Block block, BlockPos newpos, boolean value) {}
    public void onBlockPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {}
    public void onReplaced(Level world, BlockPos pos, BlockState state, BlockState newstate) {}
    public void onRemoved(Level level, BlockState state, BlockState newstate, boolean isMoving) {}
    public void rotateBlock(Rotation axis) {}
    public void onAdded(Level world, BlockState state, BlockState oldState, boolean isMoving) {}
    public InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) { return InteractionResult.PASS; }
    public ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) { return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION; }

    public int getRedstoneOutput(BlockState state, BlockGetter world, BlockPos pos, Direction side) { return -1; }
    
    public void popRecipeExperience(ServerPlayer serverplayer, float count) {}

    public boolean stillValid(Player pPlayer)
    {
        if (this.level == null)
        {
            return false;
        }
        if (this.level.getBlockEntity(this.worldPosition) != this)
        {
            return false;
        }
        return pPlayer.canInteractWithBlock(this.worldPosition, 1.5);
    }
  
    /**
     * 
     * CAPABILITY
     * 
     */
    @SuppressWarnings("unchecked")
	@Nullable
    protected <T> T getSelfCapability(BlockCapability<T, Direction> capability, Direction side)
    {
        if (level == null)
        {
            return null;
        }

        if (!selfCapabilities.containsKey(capability))
        {
            selfCapabilities.put(capability, new EnumMap<>(Direction.class));
            for (Direction direction : Direction.values())
            {
                populateSelfCachesFor(direction, capability);
            }
        }

        if (!selfCapabilities.get(capability).containsKey(side))
        {
            return null;
        }

        return (T) selfCapabilities.get(capability).get(side).getCapability();
    }

    private void populateSelfCachesFor(Direction direction, BlockCapability<?, Direction> capability)
    {
        if (level instanceof ServerLevel serverLevel)
        {
            selfCapabilities.get(capability).put(direction, BlockCapabilityCache.create(capability, serverLevel, getBlockPos(), direction));
        }
    }

    @SuppressWarnings("unchecked")
	@Nullable
    protected <T> T getNeighbouringCapability(BlockCapability<T, Direction> capability, Direction side)
    {
        if (level == null)
        {
            return null;
        }

        if (!neighbourCapabilities.containsKey(capability))
        {
            neighbourCapabilities.put(capability, new EnumMap<>(Direction.class));
            for (Direction direction : Direction.values())
            {
                populateNeighbourCachesFor(direction, capability);
            }
        }
        if (!neighbourCapabilities.get(capability).containsKey(side))
        {
            return null;
        }
        return (T) neighbourCapabilities.get(capability).get(side).getCapability();
    }

    private void populateNeighbourCachesFor(Direction direction, BlockCapability<?, Direction> capability)
    {
        if (level instanceof ServerLevel serverLevel)
        {
            BlockPos neighbourPos = getBlockPos().relative(direction);
            neighbourCapabilities.get(capability).put(direction, BlockCapabilityCache.create(capability, serverLevel, neighbourPos, direction.getOpposite()));
        }
    }
    
    public boolean hasItemCapability()
    {
        return false;
    }
    public boolean hasFluidCapability()
    {
        return false;
    }
    public boolean hasEnergyCapability()
    {
        return false;
    }
    public final IItemHandler getItemHandler()
    {
        return getItemHandler(null);
    }
    public final IFluidHandler getFluidHandler()
    {
        return getFluidHandler(null);
    }
    public final IEnergyStorage getEnergyHandler()
    {
        return getEnergyHandler(null);
    }
    public IItemHandler getItemHandler(@Nullable Direction dir)
    {
        return null;
    }
    public IFluidHandler getFluidHandler(@Nullable Direction dir)
    {
        return null;
    }
    public IWitherEnergyStorage getEnergyHandler(@Nullable Direction dir)
    {
        return null;
    }
}
