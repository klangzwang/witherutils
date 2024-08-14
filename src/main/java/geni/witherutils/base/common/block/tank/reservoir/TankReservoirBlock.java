//package geni.witherutils.base.common.block.tank.reservoir;
//
//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//
//import geni.witherutils.base.common.base.WitherAbstractBlock;
//import geni.witherutils.base.common.base.WitherMachineBlockEntity;
//import geni.witherutils.base.common.init.WUTEntities;
//import geni.witherutils.core.common.block.WitherEntityBlock;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.world.item.context.BlockPlaceContext;
//import net.minecraft.world.level.BlockAndTintGetter;
//import net.minecraft.world.level.BlockGetter;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.LevelAccessor;
//import net.minecraft.world.level.LevelReader;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.EntityBlock;
//import net.minecraft.world.level.block.RenderShape;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.entity.BlockEntityTicker;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockBehaviour;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.state.StateDefinition;
//import net.minecraft.world.level.block.state.properties.BooleanProperty;
//import net.minecraft.world.level.material.FluidState;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.api.distmarker.OnlyIn;
//
//public class TankReservoirBlock extends WitherAbstractBlock implements WitherEntityBlock {
//
//    public static final BooleanProperty NORTH = BooleanProperty.create("north");
//    public static final BooleanProperty EAST = BooleanProperty.create("east");
//    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
//    public static final BooleanProperty WEST = BooleanProperty.create("west");
//    public static final BooleanProperty CORNER_NORTH_WEST = BooleanProperty.create("corner_north_west");
//    public static final BooleanProperty CORNER_NORTH_EAST = BooleanProperty.create("corner_north_east");
//    public static final BooleanProperty CORNER_SOUTH_EAST = BooleanProperty.create("corner_south_east");
//    public static final BooleanProperty CORNER_SOUTH_WEST = BooleanProperty.create("corner_south_west");
//
//    public TankReservoirBlock(BlockBehaviour.Properties props)
//	{
//		super(props);
//		this.registerDefaultState(this.getStateDefinition().any()
//				.setValue(NORTH, false)
//				.setValue(EAST, false)
//				.setValue(SOUTH, false)
//				.setValue(WEST, false)
//				.setValue(CORNER_NORTH_WEST, false)
//				.setValue(CORNER_NORTH_EAST, false)
//				.setValue(CORNER_SOUTH_EAST, false)
//				.setValue(CORNER_SOUTH_WEST, false)
//				);
//    }
//
//	@SuppressWarnings("unchecked")
//	public static @Nullable <Q> Q getAnyTileEntity(@Nonnull BlockGetter world, @Nonnull BlockPos pos, Class<Q> teClass)
//	{
//		BlockEntity te = world.getBlockEntity(pos);
//		if(teClass == null)
//		{
//			return (Q) te;
//		}
//		if (teClass.isInstance(te))
//		{
//			return teClass.cast(te);
//		}
//		return null;
//	}
//
//    @Override
//    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor level, BlockPos pos, BlockPos newPos)
//    {
//        return this.getPanelState(state, level, pos);
//    }
//
//    @Override
//    public BlockState getStateForPlacement(BlockPlaceContext context)
//    {
//        return this.getPanelState(super.getStateForPlacement(context), context.getLevel(), context.getClickedPos());
//    }
//    
//    private BlockState getPanelState(BlockState state, LevelAccessor level, BlockPos pos)
//    {
//        boolean north = level.getBlockState(pos.north()).getBlock() == this;
//        boolean east = level.getBlockState(pos.east()).getBlock() == this;
//        boolean south = level.getBlockState(pos.south()).getBlock() == this;
//        boolean west = level.getBlockState(pos.west()).getBlock() == this;
//        boolean cornerNorthWest = north && west && level.getBlockState(pos.north().west()).getBlock() != this;
//        boolean cornerNorthEast = north && east && level.getBlockState(pos.north().east()).getBlock() != this;
//        boolean cornerSouthEast = south && east && level.getBlockState(pos.south().east()).getBlock() != this;
//        boolean cornerSouthWest = south && west && level.getBlockState(pos.south().west()).getBlock() != this;
//        return state
//        		.setValue(NORTH, north)
//        		.setValue(EAST, east)
//        		.setValue(SOUTH, south)
//        		.setValue(WEST, west)
//        		.setValue(CORNER_NORTH_WEST, cornerNorthWest)
//        		.setValue(CORNER_NORTH_EAST, cornerNorthEast)
//        		.setValue(CORNER_SOUTH_EAST, cornerSouthEast)
//        		.setValue(CORNER_SOUTH_WEST, cornerSouthWest);
//    }
//
//    @Override
//    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
//    {
//        if(!state.is(newState.getBlock()))
//        {
//            super.onRemove(state, level, pos, newState, isMoving);
//        }
//    }
//
//    @Override
//    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
//    {
//        super.createBlockStateDefinition(builder);
//        builder.add(NORTH);
//        builder.add(EAST);
//        builder.add(SOUTH);
//        builder.add(WEST);
//        builder.add(CORNER_NORTH_WEST);
//        builder.add(CORNER_NORTH_EAST);
//        builder.add(CORNER_SOUTH_EAST);
//        builder.add(CORNER_SOUTH_WEST);
//    }
//
//    @Override
//    public RenderShape getRenderShape(BlockState pState)
//    {
//        return RenderShape.MODEL;
//    }
//
//	@Override
//    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving)
//    {
//        super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
//        updateBlockEntityCache(pLevel, pPos);
//    }
//
//    @Override
//    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor)
//    {
//        super.onNeighborChange(state, level, pos, neighbor);
//        updateBlockEntityCache(level, pos);
//    }
//
//    private void updateBlockEntityCache(LevelReader level, BlockPos pos)
//    {
//        BlockEntity blockEntity = level.getBlockEntity(pos);
//        if(blockEntity instanceof WitherMachineBlockEntity machineBlockEntity)
//        {
////            machineBlockEntity.updateCapabilityCache();
//        }
//    }
//    
//	@Override
//	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos)
//	{
//		return false;
//	}
//	@Override
//	@Deprecated
//	@OnlyIn(Dist.CLIENT)
//	public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos)
//	{
//		return 1.0F;
//	}
//	@Override
//	public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState)
//	{
//		return true;
//	}
//	@SuppressWarnings("deprecation")
//	@Override
//	@OnlyIn(Dist.CLIENT)
//	public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side)
//	{
//		return adjacentBlockState.is(this) || super.skipRendering(state, adjacentBlockState, side);
//	}
//
//	@Override
//	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
//	{
//    	if(level.getBlockState(pos.relative(Direction.DOWN)).getBlock() instanceof TankReservoirBlock
//    			|| level.getBlockState(pos.relative(Direction.UP)).getBlock() instanceof TankReservoirBlock)
//    		return false;
//    	else
//    		return true;
//	}
//	
//	@Override
//	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
//	{
//		return new TankReservoirBlockEntity(pos, state);
//	}
//}