package geni.witherutils.base.common.fluid;

import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTFluids;
import geni.witherutils.base.common.init.WUTItems;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public abstract class FluidExperience {

    public static final FluidRenderProps RENDER_PROPS = new FluidRenderProps("experience_still", "experience_flow");

    private static BaseFlowingFluid.Properties props()
    {
        return new BaseFlowingFluid
        		.Properties(WUTFluids.EXPERIENCE_FLUID_TYPE, WUTFluids.EXPERIENCE, WUTFluids.EXPERIENCE_FLOWING)
        		.block(WUTBlocks.EXPERIENCE)
        		.bucket(WUTItems.EXPERIENCE_BUCKET)
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
