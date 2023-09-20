package geni.witherutils.base.common.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GlassGlowingBlock extends WitherAbstractBlock {

	public GlassGlowingBlock(BlockBehaviour.Properties props)
	{
		super(props);
		this.setHasTooltip();
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos)
	{
		return false;
	}
	
	@Override
	@Deprecated
	@OnlyIn(Dist.CLIENT)
	public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos)
	{
		return 1.0F;
	}
	
	@Override
	public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState)
	{
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side)
	{
		return adjacentBlockState.is(this) || super.skipRendering(state, adjacentBlockState, side);
	}

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos)
    {
        return 15;
    }
    
    @Override
    public int getLightBlock(BlockState pState, BlockGetter pLevel, BlockPos pPos)
    {
        return pLevel.getMaxLightLevel();
    }
}