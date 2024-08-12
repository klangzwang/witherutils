package geni.witherutils.base.common.block.fluid;

import geni.witherutils.base.common.init.WUTFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class FertilizerBlock extends LiquidBlock {

    public FertilizerBlock(FlowingFluid supplier, Properties props)
    {
        super(supplier, props);
	}
    
	@Override
	public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos)
	{
		return WUTFluids.FERTILIZER_FLUID_TYPE.get().getLightLevel();
	}
}
