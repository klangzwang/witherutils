package geni.witherutils.base.common.block.deco.pole;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

import geni.witherutils.base.common.base.WitherAbstractBlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class SteelPoleBlock extends WitherAbstractBlock implements SimpleWaterloggedBlock {

	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final DirectionProperty FACING = DirectionalBlock.FACING;
	
	protected final Map<BlockState,VoxelShape> vshapes;
	
	public SteelPoleBlock(BlockBehaviour.Properties properties, final Function<List<BlockState>, Map<BlockState,VoxelShape>> shape_supplier)
	{
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP).setValue(WATERLOGGED, false));
		vshapes = shape_supplier.apply(getStateDefinition().getPossibleStates());
	}
    public SteelPoleBlock(BlockBehaviour.Properties properties, final Supplier<ArrayList<VoxelShape>> shape_supplier)
    {
		this(properties, (states) -> {
			final Map<BlockState, VoxelShape> vshapes = new HashMap<>();
			final ArrayList<VoxelShape> indexed_shapes = shape_supplier.get();
			for (BlockState state : states)
				vshapes.put(state, indexed_shapes.get(state.getValue(FACING).get3DDataValue()));
			return vshapes;
		});
    }
    public SteelPoleBlock(BlockBehaviour.Properties properties, final AABB[] unrotatedAABBs)
    {
		this(properties, (states) -> {
			Map<BlockState, VoxelShape> vshapes = new HashMap<>();
			for (BlockState state : states) {
				vshapes.put(state, getUnionShape(
						getRotatedAABB(unrotatedAABBs, state.getValue(FACING), false)));
			}
			return vshapes;
		});
    }
    public SteelPoleBlock(BlockBehaviour.Properties properties, final AABB unrotatedAABB)
    { this(properties, new AABB[]{unrotatedAABB}); }

    public static AABB getRotatedAABB(AABB bb, Direction new_facing)
    { return getRotatedAABB(bb, new_facing, false); }
    public static AABB[] getRotatedAABB(AABB[] bb, Direction new_facing)
    { return getRotatedAABB(bb, new_facing, false); }
    public static AABB getRotatedAABB(AABB bb, Direction new_facing, boolean horizontal_rotation)
    {
      if(!horizontal_rotation) {
        switch(new_facing.get3DDataValue()) {
          case 0: return new AABB(1-bb.maxX,   bb.minZ,   bb.minY, 1-bb.minX,   bb.maxZ,   bb.maxY); // D
          case 1: return new AABB(1-bb.maxX, 1-bb.maxZ, 1-bb.maxY, 1-bb.minX, 1-bb.minZ, 1-bb.minY); // U
          case 2: return new AABB(  bb.minX,   bb.minY,   bb.minZ,   bb.maxX,   bb.maxY,   bb.maxZ); // N --> bb
          case 3: return new AABB(1-bb.maxX,   bb.minY, 1-bb.maxZ, 1-bb.minX,   bb.maxY, 1-bb.minZ); // S
          case 4: return new AABB(  bb.minZ,   bb.minY, 1-bb.maxX,   bb.maxZ,   bb.maxY, 1-bb.minX); // W
          case 5: return new AABB(1-bb.maxZ,   bb.minY,   bb.minX, 1-bb.minZ,   bb.maxY,   bb.maxX); // E
        }
      } else {
        switch(new_facing.get3DDataValue()) {
          case 0: return new AABB(  bb.minX, bb.minY,   bb.minZ,   bb.maxX, bb.maxY,   bb.maxZ); // D --> bb
          case 1: return new AABB(  bb.minX, bb.minY,   bb.minZ,   bb.maxX, bb.maxY,   bb.maxZ); // U --> bb
          case 2: return new AABB(  bb.minX, bb.minY,   bb.minZ,   bb.maxX, bb.maxY,   bb.maxZ); // N --> bb
          case 3: return new AABB(1-bb.maxX, bb.minY, 1-bb.maxZ, 1-bb.minX, bb.maxY, 1-bb.minZ); // S
          case 4: return new AABB(  bb.minZ, bb.minY, 1-bb.maxX,   bb.maxZ, bb.maxY, 1-bb.minX); // W
          case 5: return new AABB(1-bb.maxZ, bb.minY,   bb.minX, 1-bb.minZ, bb.maxY,   bb.maxX); // E
        }
      }
      return bb;
    }
    public static AABB[] getRotatedAABB(AABB[] bbs, Direction new_facing, boolean horizontal_rotation)
    {
      final AABB[] transformed = new AABB[bbs.length];
      for(int i=0; i<bbs.length; ++i) transformed[i] = getRotatedAABB(bbs[i], new_facing, horizontal_rotation);
      return transformed;
    }
    public static VoxelShape getUnionShape(AABB ... aabbs)
    {
      VoxelShape shape = Shapes.empty();
      for(AABB aabb: aabbs) shape = Shapes.joinUnoptimized(shape, Shapes.create(aabb), BooleanOp.OR);
      return shape;
    }
    public static AABB getPixeledAABB(double x0, double y0, double z0, double x1, double y1, double z1)
    { return new AABB(x0/16.0, y0/16.0, z0/16.0, x1/16.0, y1/16.0, z1/16.0); }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter source, BlockPos pos, CollisionContext selectionContext)
    { return vshapes.get(state); }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext selectionContext)
    { return getShape(state, world, pos, selectionContext); }

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, WATERLOGGED);
	}
	
	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		Direction facing = context.getClickedFace();
		BlockState state = super.getStateForPlacement(context).setValue(FACING, facing);
		
		if(context.getPlayer() != null && (context.getPlayer().isShiftKeyDown()))
			facing = facing.getOpposite();

		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		if (world.getBlockState(pos.relative(facing.getOpposite())).getBlock() instanceof SteelPoleBlock)
		{
			state = state.setValue(FACING, state.getValue(FACING).getOpposite());
		}
		return state;
	}

	@SuppressWarnings("unused")
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		if ((hit.getDirection().getAxis() == state.getValue(FACING).getAxis()))
			return InteractionResult.PASS;
		
		final ItemStack held_stack = player.getItemInHand(hand);
		
		if ((held_stack.isEmpty()) || (!(held_stack.getItem() instanceof BlockItem)))
			return InteractionResult.PASS;
		
		if (!(((BlockItem) (held_stack.getItem())).getBlock() instanceof SteelPoleBlock))
			return InteractionResult.PASS;
		
		if (held_stack.getItem() != this.asItem())
			return InteractionResult.sidedSuccess(world.isClientSide());
		
		final Block held_block = ((BlockItem) (held_stack.getItem())).getBlock();
		
		final Direction block_direction = state.getValue(FACING);
		
		final Vec3 block_vec = Vec3.atLowerCornerOf(state.getValue(FACING).getNormal());
		
		final double colinearity = 1.0 - block_vec.cross(player.getLookAngle()).length();
		
		final Direction placement_direction = Arrays.stream(Direction.orderedByNearest(player))
				.filter(d -> d.getAxis() == block_direction.getAxis()).findFirst().orElse(Direction.NORTH);
		
		final BlockPos adjacent_pos = pos.relative(placement_direction);
		
		final BlockState adjacent = world.getBlockState(adjacent_pos);
		
		final BlockPlaceContext ctx = new DirectionalPlaceContext(world, adjacent_pos, placement_direction,
				player.getItemInHand(hand), placement_direction.getOpposite());

		if (!adjacent.canBeReplaced(ctx))
			return InteractionResult.sidedSuccess(world.isClientSide());
		
		final BlockState new_state = held_block.getStateForPlacement(ctx);
		
		if (new_state == null)
			return InteractionResult.FAIL;
		
		if (!world.setBlock(adjacent_pos, new_state, 1 | 2))
			return InteractionResult.FAIL;
		
		world.playSound(player, pos, SoundEvents.METAL_PLACE, SoundSource.BLOCKS, 1f, 1f);
		
		if (!player.isCreative())
		{
			held_stack.shrink(1);
			player.getInventory().removeItem(player.getInventory().selected, 1);
		}
		return InteractionResult.sidedSuccess(world.isClientSide());
	}

	/*
	 * 
	 * WATERLOGGABLE
	 * 
	 */
    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos)
    {
    	return !state.getValue(WATERLOGGED) && super.propagatesSkylightDown(state, reader, pos);
    }
    @Override
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state)
    {
    	return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos pos, BlockPos facingPos)
    {
    	if(state.getValue(WATERLOGGED)) world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
    	return state;
    }
    @Override
    public boolean canPlaceLiquid(BlockGetter world, BlockPos pos, BlockState state, Fluid fluid)
    { return SimpleWaterloggedBlock.super.canPlaceLiquid(world, pos, state, fluid); }
    @Override
    public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidState)
    { return SimpleWaterloggedBlock.super.placeLiquid(world, pos, state, fluidState); }
    @Override
    public ItemStack pickupBlock(LevelAccessor world, BlockPos pos, BlockState state)
    { return SimpleWaterloggedBlock.super.pickupBlock(world, pos, state); }
    @Override
    public Optional<SoundEvent> getPickupSound()
    { return SimpleWaterloggedBlock.super.getPickupSound(); }
}
