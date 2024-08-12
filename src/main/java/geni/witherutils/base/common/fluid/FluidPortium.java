package geni.witherutils.base.common.fluid;

import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTFluids;
import geni.witherutils.base.common.init.WUTItems;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public abstract class FluidPortium {

    public static final FluidRenderProps RENDER_PROPS = new FluidRenderProps("portium_still", "portium_flow");

    private static BaseFlowingFluid.Properties props()
    {
        return new BaseFlowingFluid
        		.Properties(WUTFluids.PORTIUM_FLUID_TYPE, WUTFluids.PORTIUM, WUTFluids.PORTIUM_FLOWING)
        		.block(WUTBlocks.PORTIUM)
        		.bucket(WUTItems.PORTIUM_BUCKET)
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
