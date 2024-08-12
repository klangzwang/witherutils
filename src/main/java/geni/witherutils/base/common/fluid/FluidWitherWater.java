package geni.witherutils.base.common.fluid;

import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTFluids;
import geni.witherutils.base.common.init.WUTItems;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public abstract class FluidWitherWater {

    public static final FluidRenderProps RENDER_PROPS = new FluidRenderProps("witherwater_still", "witherwater_flow");

    private static BaseFlowingFluid.Properties props()
    {
        return new BaseFlowingFluid
        		.Properties(WUTFluids.WITHERWATER_FLUID_TYPE, WUTFluids.WITHERWATER, WUTFluids.WITHERWATER_FLOWING)
        		.block(WUTBlocks.WITHERWATER)
        		.bucket(WUTItems.WITHERWATER_BUCKET)
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
