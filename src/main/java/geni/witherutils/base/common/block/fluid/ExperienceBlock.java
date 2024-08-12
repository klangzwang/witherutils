package geni.witherutils.base.common.block.fluid;

import geni.witherutils.base.common.init.WUTFluids;
import geni.witherutils.base.common.init.WUTParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class ExperienceBlock extends LiquidBlock {

    public ExperienceBlock(FlowingFluid supplier, Properties props)
    {
        super(supplier, props);
	}
    
	@Override
	public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos)
	{
		return WUTFluids.EXPERIENCE_FLUID_TYPE.get().getLightLevel();
	}
	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos)
	{
		return true;
	}

	@Override
	public void animateTick(BlockState state, Level worldIn, BlockPos pos, RandomSource random)
	{
		BlockPos blockpos = pos.above();
		BlockState blockstate = worldIn.getBlockState(blockpos);

		if(!blockstate.isFaceSturdy(worldIn, blockpos, Direction.UP))
		{
			double d1 = (double) pos.getX() + random.nextDouble();
			double d2 = (double) pos.getY() + 0.1F + state.getFluidState().getHeight(worldIn, blockpos);
			double d3 = (double) pos.getZ() + random.nextDouble();
			
			if(random.nextFloat() < 0.1F)
			{
				worldIn.addParticle(WUTParticles.BUBBLE.get(), d1, d2, d3, 0.0D, 0.0D, 0.0D);
			}
		}
	}
}