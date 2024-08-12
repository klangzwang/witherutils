package geni.witherutils.base.common.fluid;

import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTFluids;
import geni.witherutils.base.common.init.WUTItems;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public abstract class FluidSoulful {

    public static final FluidRenderProps RENDER_PROPS = new FluidRenderProps("soulful_still", "soulful_flow");

    private static BaseFlowingFluid.Properties props()
    {
        return new BaseFlowingFluid
        		.Properties(WUTFluids.SOULFUL_FLUID_TYPE, WUTFluids.SOULFUL, WUTFluids.SOULFUL_FLOWING)
        		.block(WUTBlocks.SOULFUL)
        		.bucket(WUTItems.SOULFUL_BUCKET)
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
