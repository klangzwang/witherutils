package geni.witherutils.base.common.fluid;

import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTFluids;
import geni.witherutils.base.common.init.WUTItems;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public abstract class FluidRedResin {

    public static final FluidRenderProps RENDER_PROPS = new FluidRenderProps("redresin_still", "redresin_flow");

    private static BaseFlowingFluid.Properties props()
    {
        return new BaseFlowingFluid
        		.Properties(WUTFluids.REDRESIN_FLUID_TYPE, WUTFluids.REDRESIN, WUTFluids.REDRESIN_FLOWING)
        		.block(WUTBlocks.REDRESIN)
        		.bucket(WUTItems.REDRESIN_BUCKET)
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
