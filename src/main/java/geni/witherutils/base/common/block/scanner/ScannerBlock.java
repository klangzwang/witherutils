package geni.witherutils.base.common.block.scanner;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.core.common.util.BlockstatesUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ScannerBlock extends WitherAbstractBlock implements EntityBlock {

	public static final DirectionProperty FACING = BlockStateProperties.FACING;
    
	public ScannerBlock(BlockBehaviour.Properties props)
	{
		super(props);
	    this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));
	    this.setHasScreen();
		this.setHasTooltip();
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack)
	{
		if(entity != null)
			world.setBlock(pos, state.setValue(FACING, BlockstatesUtil.getFacingFromEntity(pos, entity)), 2);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		Player placer = context.getPlayer();
		Direction face = placer.getDirection().getOpposite();
		if (placer.getXRot() > 50) face = Direction.UP;
		else if (placer.getXRot() < -50) face = Direction.DOWN;
		return this.defaultBlockState().setValue(FACING, face);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(BlockStateProperties.FACING).add(LIT);
	}

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context)
    {
    	Direction direction = state.getValue(BlockStateProperties.FACING);
    	return direction == Direction.DOWN ? Shapes.or(Block.box(2, 5, 0, 14, 16, 16), Block.box(5.5, 0, 0, 10.5, 5, 16)) :
 		   direction.getAxis() == Direction.Axis.X ? Shapes.or(Block.box(0, 11, 5.5, 16, 16, 10.5), Block.box(0, 0, 2, 16, 11, 14)) : 
 			   										 Shapes.or(Block.box(2, 0, 0, 14, 11, 16), Block.box(5.5, 11, 0, 10.5, 16, 16));
    }
    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos)
    {
    	Direction direction = state.getValue(BlockStateProperties.FACING);
    	return direction == Direction.DOWN ? Shapes.or(Block.box(2, 5, 0, 14, 16, 16), Block.box(5.5, 0, 0, 10.5, 5, 16)) :
    		   direction.getAxis() == Direction.Axis.X ? Shapes.or(Block.box(0, 11, 5.5, 16, 16, 10.5), Block.box(0, 0, 2, 16, 11, 14)) : 
    			   										 Shapes.or(Block.box(2, 0, 0, 14, 11, 16), Block.box(5.5, 11, 0, 10.5, 16, 16));
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction facing)
    {
    	return state.getValue(BlockStateProperties.FACING) == facing && state.getValue(LIT) ? 15 : 0;
    }
    @Override
	public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction facing)
    {
    	return state.getValue(BlockStateProperties.FACING) == facing && state.getValue(LIT) ? 15 : 0;
	}
    @Override
    public boolean isSignalSource(BlockState state)
    {
        return true;
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
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new ScannerBlockEntity(pos, state);
	}
	
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type)
	{
		return createTickerHelper(type, WUTEntities.SCANNER.get(), ScannerBlockEntity::tick);
	}
}