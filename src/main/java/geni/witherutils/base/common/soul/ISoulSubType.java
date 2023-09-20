package geni.witherutils.base.common.soul;

import java.util.Collection;

import net.minecraft.resources.ResourceLocation;

public interface ISoulSubType extends ISoul
{
    Collection<ResourceLocation> getTypes();
}