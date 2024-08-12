package geni.witherutils.base.common.fluid;

import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTFluids;
import geni.witherutils.base.common.init.WUTItems;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public abstract class FluidFertilizer {

    public static final FluidRenderProps RENDER_PROPS = new FluidRenderProps("fertilizer_still", "fertilizer_flow");

    private static BaseFlowingFluid.Properties props()
    {
        return new BaseFlowingFluid
        		.Properties(WUTFluids.FERTILIZER_FLUID_TYPE, WUTFluids.FERTILIZER, WUTFluids.FERTILIZER_FLOWING)
        		.block(WUTBlocks.FERTILIZER)
        		.bucket(WUTItems.FERTILIZER_BUCKET)
        		.tickRate(10);
    }
    
    public static class Source extends BaseFlowingFluid.Source
    {
        public Source()
        {
            super(props());
        }
    }
    
    public static class Flowing extends BaseFlowingFluid.Flowing
    {
        public Flowing()
        {
            super(props());
        }
    }
}
