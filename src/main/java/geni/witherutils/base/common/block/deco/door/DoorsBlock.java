package geni.witherutils.base.common.block.deco.door;

import java.util.List;

import javax.annotation.Nullable;

import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DoorsBlock extends DoorBlock {
	
	protected final VoxelShape[][][][] shapes_;
	protected final SoundEvent open_sound_;
	protected final SoundEvent close_sound_;

	public DoorsBlock(BlockBehaviour.Properties properties, AABB[] open_aabbs_top,
			AABB[] open_aabbs_bottom, AABB[] closed_aabbs_top, AABB[] closed_aabbs_bottom, SoundEvent open_sound,
			SoundEvent close_sound)
	{
		super(BlockSetType.STONE, properties);
		VoxelShape[][][][] shapes = new VoxelShape[Direction.values().length][2][2][2];
		for (Direction facing : Direction.values())
		{
			for (boolean open : new boolean[] { false, true })
			{
				for (DoubleBlockHalf half : new DoubleBlockHalf[] { DoubleBlockHalf.UPPER, DoubleBlockHalf.LOWER })
				{
					for (boolean hinge_right : new boolean[] { false, true })
					{
						VoxelShape shape = Shapes.empty();
						if (facing.getAxis() == Direction.Axis.Y)
						{
							shape = Shapes.block();
						}
						else
						{
							final AABB[] aabbs = (open)
									? ((half == DoubleBlockHalf.UPPER) ? open_aabbs_top : open_aabbs_bottom)
									: ((half == DoubleBlockHalf.UPPER) ? closed_aabbs_top : closed_aabbs_bottom);
							for (AABB e : aabbs)
							{
								AABB aabb = getRotatedAABB(e, facing, true);
								if (!hinge_right)
									aabb = getMirroredAABB(aabb, facing.getClockWise().getAxis());
								shape = Shapes.join(shape, Shapes.create(aabb), BooleanOp.OR);
							}
						}
						shapes[facing.ordinal()][open ? 1 : 0][hinge_right ? 1 : 0][half == DoubleBlockHalf.UPPER ? 0
								: 1] = shape;
					}
				}
			}
		}
		shapes_ = shapes;
		open_sound_ = open_sound;
		close_sound_ = close_sound;
	}

	public DoorsBlock(BlockBehaviour.Properties properties, AABB open_aabb, AABB closed_aabb, SoundEvent open_sound, SoundEvent close_sound)
	{
		this(properties, new AABB[] { open_aabb }, new AABB[] { open_aabb }, new AABB[] { closed_aabb }, new AABB[] { closed_aabb }, open_sound, close_sound);
	}
	public DoorsBlock(BlockBehaviour.Properties properties, SoundEvent open_sound, SoundEvent close_sound)
	{
		this(properties, getPixeledAABB(13, 0, 0, 16, 16, 16), getPixeledAABB(0, 0, 13, 16, 16, 16), open_sound, close_sound);
	}
	public DoorsBlock(BlockBehaviour.Properties properties)
	{
		this(properties, getPixeledAABB(13, 0, 0, 16, 16, 16), getPixeledAABB(0, 0, 13, 16, 16, 16), SoundEvents.WOODEN_DOOR_OPEN, SoundEvents.WOODEN_DOOR_CLOSE);
	}

	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moving)
	{
		super.onPlace(state, world, pos, oldState, moving);
		world.scheduleTick(pos, this, 5);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{
		boolean powered = isPowered(state);
		
		if(level.isClientSide)
			return;

		level.scheduleTick(pos, this, 5);
		
		AABB bb = new AABB(pos).inflate(2, 1, 2);
		List<Player> nearbyPlayers = level.getEntitiesOfClass(Player.class, bb);
		boolean gettingPowered = nearbyPlayers.size() > 0;
		
		if(powered != gettingPowered)
		{
			level.setBlock(pos, state.setValue(POWERED, Boolean.valueOf(gettingPowered)).setValue(OPEN, Boolean.valueOf(gettingPowered)), 2);
			notifyPower(level, pos, state);

			if(gettingPowered)
			SoundUtil.playSoundFromServer(level, pos, WUTSounds.ELECTRODISTANT.get());
		}
	}
	
	private void notifyPower(Level level, BlockPos pos, BlockState state)
	{
		level.updateNeighborsAt(pos, this);
		level.updateNeighborsAt(pos.relative(state.getValue(FACING).getOpposite()), this);
	}
	
	public static boolean isPowered(BlockState state)
	{
		return state.getValue(POWERED);
	}
	
	protected void sound(BlockGetter world, BlockPos pos, boolean open)
	{
		if (world instanceof Level)
			((Level) world).playSound(null, pos, open ? open_sound_ : close_sound_, SoundSource.BLOCKS, 0.7f, 1f);
	}

	protected void actuate_adjacent_wing(BlockState state, BlockGetter world_ro, BlockPos pos, boolean open)
	{
		if (!(world_ro instanceof final Level world))
			return;
		final BlockPos adjecent_pos = pos
				.relative((state.getValue(HINGE) == DoorHingeSide.LEFT) ? (state.getValue(FACING).getClockWise())
						: (state.getValue(FACING).getCounterClockWise()));
		if (!world.isLoaded(adjecent_pos))
			return;
		BlockState adjacent_state = world.getBlockState(adjecent_pos);
		if (adjacent_state.getBlock() != this)
			return;
		if (adjacent_state.getValue(OPEN) == open)
			return;
		world.setBlock(adjecent_pos, adjacent_state.setValue(OPEN, open), 2 | 10);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context)
	{
		return shapes_[state.getValue(FACING).ordinal()][state.getValue(OPEN) ? 1
				: 0][state.getValue(HINGE) == DoorHingeSide.RIGHT ? 1 : 0][state.getValue(HALF) == DoubleBlockHalf.UPPER
						? 0
						: 1];
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult pHitResult)
	{
		setOpen(player, level, state, pos, !state.getValue(OPEN));
		return super.useWithoutItem(state, level, pos, player, pHitResult);
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
	{
		boolean powered = world.hasNeighborSignal(pos) || world.hasNeighborSignal(pos.relative(state.getValue(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN));

		if ((block == this) || (powered == state.getValue(POWERED)))
			return;
		
		world.setBlock(pos, state.setValue(POWERED, powered).setValue(OPEN, powered), 2);
		actuate_adjacent_wing(state, world, pos, powered);
		
		if (powered != state.getValue(OPEN))
			sound(world, pos, powered);
	}

	@Override
	public void setOpen(@Nullable Entity entity, Level world, BlockState state, BlockPos pos, boolean open)
	{
		if (!state.is(this) || (state.getValue(OPEN) == open))
			return;
		
		state = state.setValue(OPEN, open);
		world.setBlock(pos, state, 2 | 8);
		sound(world, pos, open);
		actuate_adjacent_wing(state, world, pos, open);
	}
	
	  public static AABB getPixeledAABB(double x0, double y0, double z0, double x1, double y1, double z1)
	  { return new AABB(x0/16.0, y0/16.0, z0/16.0, x1/16.0, y1/16.0, z1/16.0); }

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
	  public static AABB getMirroredAABB(AABB bb, Direction.Axis axis)
	  {
	    return switch (axis) {
	      case X -> new AABB(1 - bb.maxX, bb.minY, bb.minZ, 1 - bb.minX, bb.maxY, bb.maxZ);
	      case Y -> new AABB(bb.minX, 1 - bb.maxY, bb.minZ, bb.maxX, 1 - bb.minY, bb.maxZ);
	      case Z -> new AABB(bb.minX, bb.minY, 1 - bb.maxZ, bb.maxX, bb.maxY, 1 - bb.minZ);
	    };
	  }

	  public static AABB[] getMirroredAABB(AABB[] bbs, Direction.Axis axis)
	  {
	    final AABB[] transformed = new AABB[bbs.length];
	    for(int i=0; i<bbs.length; ++i) transformed[i] = getMirroredAABB(bbs[i], axis);
	    return transformed;
	  }
}
