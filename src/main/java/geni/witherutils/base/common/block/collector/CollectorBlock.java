package geni.witherutils.base.common.block.collector;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.core.common.block.WitherEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class CollectorBlock extends WitherAbstractBlock implements WitherEntityBlock {

	public CollectorBlock(BlockBehaviour.Properties props)
	{
		super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));
        this.setHasScreen();
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
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos)
    {
        Block block = state.getBlock();
        if(block != this)
        {
            return block.getLightEmission(state, level, pos);
        }
        return state.getValue(LIT) ? 6 : 0;
    }
    
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new CollectorBlockEntity(pos, state);
    }
}