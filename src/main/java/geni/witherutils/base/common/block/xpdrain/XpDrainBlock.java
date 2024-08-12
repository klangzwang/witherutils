package geni.witherutils.base.common.block.xpdrain;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.core.common.block.WitherEntityBlock;
import geni.witherutils.core.common.util.ItemStackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;

public class XpDrainBlock extends WitherAbstractBlock implements WitherEntityBlock {

	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	
	public XpDrainBlock(BlockBehaviour.Properties props)
	{
		super(props);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));
		this.setHasTooltip();
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		if(placer != null)
			world.setBlock(pos, state.setValue(FACING, getFacingFromEntity(pos, placer)), 2);
        checkRedstone(world, pos);
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
		builder.add(FACING, LIT);
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos pos)
	{
    	if(levelReader instanceof Level level)
    		return level.getCapability(Capabilities.FluidHandler.BLOCK, pos, state.getValue(FACING).getOpposite()) != null;
        return false;
	}
    
	@Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
    {
        if(level.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).isAir())
        {
        	ItemStackUtil.drop(level, pos, this);
        	level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
        }
    }
	
	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos)
	{
		return true;
	}
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context)
	{
		return Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	}

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
    	return new XpDrainBlockEntity(pos, state);
    }
}
