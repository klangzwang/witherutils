package geni.witherutils.base.common.fluid;

import geni.witherutils.api.WitherUtilsRegistry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

public class FluidRenderProps implements IClientFluidTypeExtensions {
	
    private final ResourceLocation still;
    private final ResourceLocation flowing;
    private final int colorTint;

    public FluidRenderProps(String still, String flowing)
    {
        this(still, flowing, 0xFFFFFFFF);
    }

    public FluidRenderProps(String still, String flowing, int colorTint)
    {
        this.still = still.indexOf(':') > 0 ? ResourceLocation.parse(still) : WitherUtilsRegistry.loc("block/fluid/" + still);
        this.flowing = flowing.indexOf(':') > 0 ? ResourceLocation.parse(flowing) : WitherUtilsRegistry.loc("block/fluid/" + flowing);
        this.colorTint = colorTint;
    }

    @Override
    public ResourceLocation getStillTexture()
    {
        return still;
    }

    @Override
    public ResourceLocation getFlowingTexture()
    {
        return flowing;
    }

    @Override
    public int getTintColor()
    {
        return colorTint;
    }
}
