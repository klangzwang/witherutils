package geni.witherutils.base.common.block.sensor.wall;

import java.util.List;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.core.common.util.ItemStackUtil;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WallSensorBlock extends WitherAbstractBlock implements EntityBlock {

	public WallSensorBlock(BlockBehaviour.Properties props)
	{
		super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));
        this.setHasTooltip();
	}
	
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
    
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, LIT);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context)
	{
		if (state.getValue(FACING) == Direction.WEST)
			return Block.box(14, 0, 0, 16, 4, 16);
		if (state.getValue(FACING) == Direction.EAST)
			return Block.box(0, 0, 0, 4, 4, 16);
		if (state.getValue(FACING) == Direction.NORTH)
			return Block.box(0, 0, 14, 16, 4, 16);
		if (state.getValue(FACING) == Direction.SOUTH)
			return Block.box(0, 0, 0, 16, 4, 2);
		if (state.getValue(FACING) == Direction.UP)
			return Block.box(0, 0, 0, 16, 4, 16);
		else
			return Block.box(0, 0, 0, 16, 4, 16);
	}

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos)
    {
        Block block = state.getBlock();
        if(block != this)
        {
            return block.getLightEmission(state, level, pos);
        }
        return state.getValue(LIT) ? 15 : 0;
    }
    
	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
	{
		if(state.getValue(LIT))
		{
			double d1 = (double) pos.getX() + random.nextDouble();
			double d2 = (double) pos.getY() + random.nextDouble();
			double d3 = (double) pos.getZ() + random.nextDouble();
			if(random.nextFloat() < 0.25F)
			{
				level.addParticle(new DustParticleOptions(DustParticleOptions.REDSTONE_PARTICLE_COLOR, 1.0F), d1, d2, d3, 0.0D, 0.1D, 0.0D);
			}
		}
	}

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
		for(Direction side : Direction.values())
			if(!level.isEmptyBlock(pos.relative(side.getOpposite())))
				return true;
		return false;
    }

    @Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block neighborBlock, BlockPos fromPos, boolean p_60514_)
	{
		Direction dir = state.getValue(FACING).getOpposite();
		if(world.isEmptyBlock(pos.relative(dir)))
		{
			ItemStackUtil.drop(world, pos, new ItemStack(WUTBlocks.WALLSENSOR.get().asItem(), 1));
			world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}
	}

    @Override
    public void destroy(LevelAccessor world, BlockPos pos, BlockState state)
    {
		if(isPowered(state))
			notifyPower((Level) world, pos, state);
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction facing)
    {
    	return state.getSignal(level, pos, facing);
    }
    @Override
	public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction facing)
    {
        if(!state.getValue(LIT))
            return 0;
        else
            return state.getValue(LIT) && state.getValue(FACING) == facing ? 15 : 0;
	}
	@Override
	public boolean isSignalSource(BlockState state)
	{
		return true;
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
			level.setBlockAndUpdate(pos, state.setValue(LIT, gettingPowered));
			notifyPower(level, pos, state);

			if(gettingPowered != false)
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
		return state.getValue(LIT);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean boolbool)
	{
		if(!boolbool && !state.is(oldState.getBlock()))
		{
			if(state.getValue(LIT))
			{
				this.notifyPower(level, pos, state);
			}
			super.onRemove(state, level, pos, oldState, boolbool);
		}
	}
	
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new WallSensorBlockEntity(pos, state);
    }
}
