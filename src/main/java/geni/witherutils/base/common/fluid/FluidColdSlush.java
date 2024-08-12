package geni.witherutils.base.common.fluid;

import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTFluids;
import geni.witherutils.base.common.init.WUTItems;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public abstract class FluidColdSlush {

    public static final FluidRenderProps RENDER_PROPS = new FluidRenderProps("coldslush_still", "coldslush_flow");

    private static BaseFlowingFluid.Properties props()
    {
        return new BaseFlowingFluid
        		.Properties(WUTFluids.COLDSLUSH_FLUID_TYPE, WUTFluids.COLDSLUSH, WUTFluids.COLDSLUSH_FLOWING)
        		.block(WUTBlocks.COLDSLUSH)
        		.bucket(WUTItems.COLDSLUSH_BUCKET)
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
