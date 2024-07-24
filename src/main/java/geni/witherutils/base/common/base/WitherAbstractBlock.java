package geni.witherutils.base.common.base;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import geni.witherutils.base.client.ClientSetup;
import geni.witherutils.base.client.ClientTooltipHandler;
import geni.witherutils.core.common.blockentity.WitherBlockEntity;
import geni.witherutils.core.common.item.IBlock;
import geni.witherutils.core.common.item.ItemBlock;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;

@SuppressWarnings("unused")
public abstract class WitherAbstractBlock extends Block implements SimpleWaterloggedBlock, IBlock {
	
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    
	private boolean hasScreen = false;
	private boolean hasScreenExtra = false;
	private boolean hasLiquid = false;
	private boolean hasSoulBankSlot = false;
	private boolean hasTooltip = false;

    protected int lightOpacity;
	protected final boolean notNormalBlock;
	
	protected WitherAbstractBlock(Properties properties)
    {
        super(properties);
		lightOpacity = -1;
		this.notNormalBlock = !defaultBlockState().canOcclude();
		
        if (defaultBlockState().hasProperty(WATERLOGGED))
        {
            registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
        }
    }
	
	@Override
	public ItemBlock<?> getBlockItem(Item.Properties properties)
	{
		return new ItemBlock<>(this, properties);
	}
	
    @Override
    public void initializeClient(Consumer<IClientBlockExtensions> consumer)
    {
        consumer.accept(ClientSetup.PARTICLE_HANDLER);
    }

    @Override
    public boolean canPlaceLiquid(Player player, BlockGetter pLevel, BlockPos pPos, BlockState pState, Fluid pFluid)
    {
        return pState.hasProperty(WATERLOGGED) && SimpleWaterloggedBlock.super.canPlaceLiquid(player, pLevel, pPos, pState, pFluid);
    }

    @Override
    public boolean placeLiquid(LevelAccessor pLevel, BlockPos pPos, BlockState pState, FluidState pFluidState)
    {
        return pState.hasProperty(WATERLOGGED) && SimpleWaterloggedBlock.super.placeLiquid(pLevel, pPos, pState, pFluidState);
    }

    @Override
    public ItemStack pickupBlock(Player player, LevelAccessor pLevel, BlockPos pPos, BlockState pState)
    {
        return pState.hasProperty(WATERLOGGED) ? SimpleWaterloggedBlock.super.pickupBlock(player, pLevel, pPos, pState) : ItemStack.EMPTY;
    }

    @Override
    public FluidState getFluidState(BlockState state)
    {
        return state.hasProperty(WATERLOGGED) && state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    protected boolean isWaterloggable()
    {
        return false;
    }
    protected WitherAbstractBlock setLightOpacity(int opacity)
	{
		lightOpacity = opacity;
		return this;
	}
	protected WitherAbstractBlock setHasScreen()
	{
		this.hasScreen = true;
		return this;
	}
	protected WitherAbstractBlock setHasScreenExtra()
	{
	    this.hasScreenExtra = true;
	    return this;
	}
	protected WitherAbstractBlock setHasLiquid()
	{
		this.hasLiquid = true;
		return this;
	}
    protected WitherAbstractBlock setHasSoulBankSlot()
    {
        this.hasSoulBankSlot = true;
        return this;
    }
    protected WitherAbstractBlock setHasTooltip()
    {
        this.hasTooltip = true;
        return this;
    }
    
	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos)
	{
		if(this.lightOpacity != -1)
			return this.lightOpacity;
		else if(notNormalBlock)
			return 0;
		else
			return super.getLightBlock(state, worldIn, pos);
	}
	
	@Override
	public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos)
	{
		return notNormalBlock ? 1 : super.getShadeBrightness(state, world, pos);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos)
	{
		return notNormalBlock || super.propagatesSkylightDown(state, reader, pos);
	}
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState state = super.getStateForPlacement(context);
        if (state != null)
        {
            if (state.hasProperty(WATERLOGGED))
            {
                FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
                state = state.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
            }
        }
    	
        if(context.getLevel().getBlockState(context.getClickedPos()).hasProperty(BlockStateProperties.HORIZONTAL_FACING))
            return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
        
        else if(context.getLevel().getBlockState(context.getClickedPos()).hasProperty(HorizontalDirectionalBlock.FACING))
        {
            if (context.getClickedFace().getAxis() == Direction.Axis.Y)
                return this.defaultBlockState().setValue(FACING, Direction.NORTH);
            return this.defaultBlockState().setValue(FACING, context.getClickedFace());
        }
        
        else
            return super.defaultBlockState();
    }

    protected boolean reversePlacementRotation()
    {
        return false;
    }
    
    public BlockState rotate(BlockState state, Rotation rot)
    {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }
    @SuppressWarnings("deprecation")
	public BlockState mirror(BlockState state, Mirror mirrorIn)
    {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext pContext, List<Component> list, TooltipFlag flag)
    {
        if(hasTooltip)
            ClientTooltipHandler.Tooltip.addInformation(stack, pContext, list, flag, true);
    }
    
    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos)
    {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state)
    {
        return state.getBlock() instanceof EntityBlock;
    }

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if(state.getBlock() != newState.getBlock())
		{
			BlockEntity tileentity = worldIn.getBlockEntity(pos);
			if(tileentity instanceof WitherMachineBlockEntity wbe)
			{
				if(wbe != null && wbe.getInventory() != null)
				{
					for(int i = 0; i < wbe.getInventory().getSlots(); i++)
					{
						Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), wbe.getInventory().getStackInSlot(i));
					}
				}
				worldIn.updateNeighbourForOutputSignal(pos, this);
			}
		}
		super.onRemove(state, worldIn, pos, newState, isMoving);
	}

    @SuppressWarnings("deprecation")
	@Override
    public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int id, int type)
    {
        super.triggerEvent(state, level, pos, id, type);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity != null && blockEntity.triggerEvent(id, type);
    }

	@SuppressWarnings("unchecked")
	@Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> type1, BlockEntityType<E> type2, BlockEntityTicker<? super E> ticker)
    {
        return type2 == type1 ? (BlockEntityTicker<A>) ticker : null;
    }

//    @Override
//    public ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult)
//    {
//		if(this.hasScreen)
//		{
//			if(!pLevel.isClientSide)
//			{
//				BlockEntity tileEntity = pLevel.getBlockEntity(pPos);
//				if(tileEntity instanceof MenuProvider)
//				{
//	                if (pPlayer instanceof ServerPlayer serverPlayer)
//	                	serverPlayer.openMenu((MenuProvider) tileEntity, tileEntity.getBlockPos());
//				}
//				else
//				{
//					throw new IllegalStateException("Our named container provider is missing!");
//				}
//			}
//			return ItemInteractionResult.SUCCESS;
//		}
//		return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
//    }
//    
//    @Override
//    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult)
//    {
//        BlockEntity be = pLevel.getBlockEntity(pPos);
//        if (be instanceof WitherBlockEntity wbe)
//        	wbe.useWithoutItem(pState, pLevel, pPos, pPlayer, pHitResult);
//    	return super.useWithoutItem(pState, pLevel, pPos, pPlayer, pHitResult);
//    }
	
	@Override
	public InteractionResult useWithoutItem(BlockState blockstate, Level world, BlockPos pos, Player entity, BlockHitResult hit)
	{
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof WitherBlockEntity)
        {
            MenuProvider provider = new MenuProvider()
            {
                @Override
                public Component getDisplayName()
                {
                    return new ItemStack(WitherAbstractBlock.this).getHoverName();
                }
                @Nullable
                @Override
                public AbstractContainerMenu createMenu(int id, Inventory inventory, Player playerEntity)
                {
                    return getContainer(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(pos), pos);
                }
            };
            
            AbstractContainerMenu container = provider.createMenu(0, entity.getInventory(), entity);
            if (container != null)
            {
                if (entity instanceof ServerPlayer serverPlayer)
                {
                    serverPlayer.openMenu(provider, buffer -> {
                        buffer.writeBlockPos(pos);
                        additionalGuiData(buffer, blockstate, world, pos, entity, hit);
                    });
                }
                return InteractionResult.SUCCESS;
            }
            
        	System.out.println(provider.getDisplayName());
        }
        return super.useWithoutItem(blockstate, world, pos, entity, hit);
	}

    @Nullable
    public <T extends WitherBlockEntity> AbstractContainerMenu getContainer(int id, Inventory inventory, FriendlyByteBuf buffer, BlockPos pos)
    {
        return null;
    }

    protected void additionalGuiData(FriendlyByteBuf buffer, BlockState state, Level world, BlockPos pos, Player player, BlockHitResult result) {}
	
	@Override
	public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos)
	{
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		return tileEntity instanceof MenuProvider menuProvider ? menuProvider : null;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack)
	{
        super.setPlacedBy(level, pos, state, entity, stack);
		
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof WitherBlockEntity wbe)
        	wbe.onBlockPlacedBy(level, pos, state, entity, stack);
	}
	
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        super.onPlace(state, level, pos, oldState, isMoving);
    	
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof WitherBlockEntity wbe)
        	wbe.onAdded(level, state, oldState, isMoving);
    }
    
    @Override
    public boolean addLandingEffects(@Nonnull BlockState state, @Nonnull ServerLevel world, @Nonnull BlockPos bp, @Nonnull BlockState iblockstate, @Nonnull LivingEntity entity, int numberOfParticles)
    {
    	world.sendParticles(new DustParticleOptions(DustParticleOptions.REDSTONE_PARTICLE_COLOR, 1), bp.getX() + 0.5D, bp.getY() + 1.0D, bp.getZ() + 0.5D, numberOfParticles, 0.0D, 0.0D, 0.0D, 0.15000000596046448D);
    	return true;
    }
    
    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        if (stateIn.hasProperty(WATERLOGGED) && stateIn.getValue(WATERLOGGED))
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type)
    {
        return state.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).isEmpty();
    }
}