package geni.witherutils.base.common.block.smarttv;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SmartTVBlock extends WitherAbstractBlock implements EntityBlock
{
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty MOUNTED = BooleanProperty.create("mounted");

	public SmartTVBlock(BlockBehaviour.Properties props)
	{
		super(props);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(MOUNTED, false).setValue(LIT, false));
        this.setHasTooltip();
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		Direction facing = context.getClickedFace();
        if (facing == Direction.DOWN || facing == Direction.UP)
        {
            facing = context.getHorizontalDirection().getOpposite();
        }
        return this.defaultBlockState().setValue(FACING, facing.getOpposite()).setValue(MOUNTED, context.getClickedFace().get2DDataValue() == -1 ? false : true);
	}
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, MOUNTED, LIT);
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
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos)
	{
		return true;
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos)
	{
		return 0;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context)
	{
		return Shapes.empty();
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context)
	{
		return switch (state.getValue(FACING))
		{
			default -> box(-11, 2, 11, 27, 22, 13);
			case NORTH -> box(-11, 2, 3, 27, 22, 5);
			case EAST -> box(11, 2, -11, 13, 22, 27);
			case WEST -> box(3, 2, -11, 5, 22, 27);
		};
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext selectionContext)
	{
		return switch (state.getValue(FACING))
		{
			default -> box(-11, 2, 11, 27, 22, 13);
			case NORTH -> box(-11, 2, 3, 27, 22, 5);
			case EAST -> box(11, 2, -11, 13, 22, 27);
			case WEST -> box(3, 2, -11, 5, 22, 27);
		};
	}

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new SmartTVBlockEntity(pos, state);
    }
}
