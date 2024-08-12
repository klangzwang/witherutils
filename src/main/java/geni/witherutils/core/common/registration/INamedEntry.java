package geni.witherutils.core.common.registration;

import net.minecraft.resources.ResourceLocation;

public interface INamedEntry {

    default String getName() {
        return getId().getPath();
    }

    ResourceLocation getId();
}