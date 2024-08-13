package geni.witherutils.base.common.block.sensor.floor;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.core.common.block.WitherEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class FloorSensorBlock extends WitherAbstractBlock implements WitherEntityBlock {
	
    public static final BooleanProperty COVERED = BooleanProperty.create("covered");
    
	public FloorSensorBlock(BlockBehaviour.Properties props)
	{
		super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)).setValue(COVERED, Boolean.valueOf(false)));
	}

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return state.getValue(COVERED) ? RenderShape.INVISIBLE : RenderShape.MODEL;
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, LIT, COVERED);
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
            return state.getValue(LIT) && facing == Direction.DOWN ? 15 : 0;
	}
	@Override
	public boolean isSignalSource(BlockState state)
	{
		return true;
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
		return new FloorSensorBlockEntity(pos, state);
	}
}
