package geni.witherutils.base.common.block.deco.door.metal;

import java.util.ArrayList;
import java.util.Arrays;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.core.common.block.WitherEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MetalDoorBlock extends WitherAbstractBlock implements WitherEntityBlock {

	protected final ArrayList<VoxelShape> collision_shapes_;
	
	public MetalDoorBlock(BlockBehaviour.Properties properties, AABB aabb)
	{
		this(properties, new AABB[]{aabb});
	}

	public MetalDoorBlock(BlockBehaviour.Properties properties, AABB[] aabbs)
	{
		super(properties);
		AABB[] caabbs = new AABB[aabbs.length];
		for (int i = 0; i < caabbs.length; ++i)
			caabbs[i] = aabbs[i].expandTowards(0, 1, 0);
		collision_shapes_ = new ArrayList<>(Arrays.asList(Shapes.block(), Shapes.block(),
						getUnionShape(getRotatedAABB(caabbs, Direction.NORTH, true)),
						getUnionShape(getRotatedAABB(caabbs, Direction.SOUTH, true)),
						getUnionShape(getRotatedAABB(caabbs, Direction.WEST, true)),
						getUnionShape(getRotatedAABB(caabbs, Direction.EAST, true)),
						Shapes.block(), Shapes.block()));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));
	}
	
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
//    	BlockPos blockpos = context.getClickedPos();
//        Level level = context.getLevel();
//        boolean flag = level.hasNeighborSignal(blockpos) || level.hasNeighborSignal(blockpos.above());
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
    
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, LIT);
	}
	
	public static VoxelShape getUnionShape(AABB... aabbs)
	{
		VoxelShape shape = Shapes.empty();
		for (AABB aabb : aabbs)
			shape = Shapes.joinUnoptimized(shape, Shapes.create(aabb), BooleanOp.OR);
		return shape;
	}
	public static AABB[] getRotatedAABB(AABB[] bbs, Direction new_facing, boolean horizontal_rotation)
	{
		final AABB[] transformed = new AABB[bbs.length];
		for (int i = 0; i < bbs.length; ++i)
			transformed[i] = getRotatedAABB(bbs[i], new_facing, horizontal_rotation);
		return transformed;
	}
	public static AABB getRotatedAABB(AABB bb, Direction new_facing, boolean horizontal_rotation)
	{
		if (!horizontal_rotation)
		{
			switch (new_facing.get3DDataValue()) {
				case 0 :
					return new AABB(1 - bb.maxX, bb.minZ, bb.minY, 1 - bb.minX,
							bb.maxZ, bb.maxY); // D
				case 1 :
					return new AABB(1 - bb.maxX, 1 - bb.maxZ, 1 - bb.maxY,
							1 - bb.minX, 1 - bb.minZ, 1 - bb.minY); // U
				case 2 :
					return new AABB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY,
							bb.maxZ); // N --> bb
				case 3 :
					return new AABB(1 - bb.maxX, bb.minY, 1 - bb.maxZ,
							1 - bb.minX, bb.maxY, 1 - bb.minZ); // S
				case 4 :
					return new AABB(bb.minZ, bb.minY, 1 - bb.maxX, bb.maxZ,
							bb.maxY, 1 - bb.minX); // W
				case 5 :
					return new AABB(1 - bb.maxZ, bb.minY, bb.minX, 1 - bb.minZ,
							bb.maxY, bb.maxX); // E
			}
		} else {
			switch (new_facing.get3DDataValue()) {
				case 0 :
					return new AABB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY,
							bb.maxZ); // D --> bb
				case 1 :
					return new AABB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY,
							bb.maxZ); // U --> bb
				case 2 :
					return new AABB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY,
							bb.maxZ); // N --> bb
				case 3 :
					return new AABB(1 - bb.maxX, bb.minY, 1 - bb.maxZ,
							1 - bb.minX, bb.maxY, 1 - bb.minZ); // S
				case 4 :
					return new AABB(bb.minZ, bb.minY, 1 - bb.maxX, bb.maxZ,
							bb.maxY, 1 - bb.minX); // W
				case 5 :
					return new AABB(1 - bb.maxZ, bb.minY, bb.minX, 1 - bb.minZ,
							bb.maxY, bb.maxX); // E
			}
		}
		return bb;
	}
	
	public static AABB getPixeledAABB(double x0, double y0, double z0, double x1, double y1, double z1)
	{ return new AABB(x0/16.0, y0/16.0, z0/16.0, x1/16.0, y1/16.0, z1/16.0); }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter source, BlockPos pos, CollisionContext selectionContext)
    {
    	return collision_shapes_.get(state.getValue(FACING).get3DDataValue() & 0x7);
    }
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext selectionContext)
	{
		boolean flag = false;
		if(world instanceof Level level)
		{
			flag = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
		}
		return state.getValue(LIT) || flag ? Shapes.empty() : collision_shapes_.get(state.getValue(FACING).get3DDataValue() & 0x7);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos pos, BlockPos facingPos)
	{
		return getInitialState(super.updateShape(state, facing, facingState, world, pos, facingPos), world, pos);
	}
	
	@Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult rayTraceResult)
	{
//		if ((rayTraceResult.getDirection() == Direction.UP) || (rayTraceResult.getDirection() == Direction.DOWN) && (player.getItemInHand(hand).getItem() == this.asItem()))
//			return InteractionResult.PASS;
//		if (world.isClientSide())
//			return InteractionResult.SUCCESS;
//		
//		final boolean open = !state.getValue(LIT);
//		world.setBlock(pos, state.setValue(LIT, open), 2 | 8 | 16);
//		
//		world.playSound(null, pos, open ? WUTSounds.HANGARDOOROPEN.get() : WUTSounds.HANGARDOORCLOSE.get(), SoundSource.BLOCKS, 0.7f, 1.4f);
//		
//		return InteractionResult.CONSUME;
		
		return super.useWithoutItem(state, world, pos, player, rayTraceResult);
	}
	
	private BlockState getInitialState(BlockState state, LevelAccessor world, BlockPos pos)
	{
		if (state.getBlock() == this)
			return state.setValue(LIT, state.getValue(LIT)).setValue(FACING, state.getValue(FACING));
		return state.setValue(LIT, false);
	}
	
//	@Override
//	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
//	{
//		if (world.isClientSide)
//			return;
//		
//		boolean powered = false;
//		boolean sound = false;
//		
//		if (powered != state.getValue(LIT))
//		{
//			world.setBlock(pos, state.setValue(LIT, powered), 2 | 8 | 16);
//			sound = true;
//		}
//		if ((state != null) && (powered != state.getValue(LIT)))
//		{
//			world.setBlock(pos, state.setValue(LIT, powered), 2 | 8 | 16);
//			sound = true;
//		}
//		if (sound)
//		{
//			world.playSound(null, pos, powered ? WUTSounds.HANGARDOOROPEN.get() : WUTSounds.HANGARDOORCLOSE.get(), SoundSource.BLOCKS, 0.7f, 1.4f);
//		}
//	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new MetalDoorBlockEntity(pos, state);
	}
}
