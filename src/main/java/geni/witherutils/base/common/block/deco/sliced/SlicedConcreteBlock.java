package geni.witherutils.base.common.block.deco.sliced;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class SlicedConcreteBlock extends MultifaceBlock implements SimpleWaterloggedBlock {
	
	public static final MapCodec<SlicedConcreteBlock> CODEC = simpleCodec(SlicedConcreteBlock::new);
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
    @Override
    public MapCodec<SlicedConcreteBlock> codec() {
        return CODEC;
    }
    
	public SlicedConcreteBlock(BlockBehaviour.Properties props)
	{
		super(props);
		this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(false)));
	}
	@Override
	public BlockState updateShape(BlockState p_222384_, Direction p_222385_, BlockState p_222386_, LevelAccessor p_222387_, BlockPos p_222388_, BlockPos p_222389_)
	{
		if (p_222384_.getValue(WATERLOGGED))
		{
			p_222387_.scheduleTick(p_222388_, Fluids.WATER, Fluids.WATER.getTickDelay(p_222387_));
		}
		return super.updateShape(p_222384_, p_222385_, p_222386_, p_222387_, p_222388_, p_222389_);
	}
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_222391_)
	{
		super.createBlockStateDefinition(p_222391_);
		p_222391_.add(WATERLOGGED);
	}
	@Override
	public boolean canBeReplaced(BlockState p_222381_, BlockPlaceContext p_222382_)
	{
		return !p_222382_.getItemInHand().is(Items.SCULK_VEIN) || super.canBeReplaced(p_222381_, p_222382_);
	}
	@Override
	public FluidState getFluidState(BlockState p_222394_)
	{
		return p_222394_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_222394_);
	}
	@Override
	public MultifaceSpreader getSpreader()
	{
		return null;
	}
}
