package geni.witherutils.base.common.base;

import net.minecraft.world.item.CreativeModeTab;

@FunctionalInterface
public interface CreativeTabVariants {

    void addAllVariants(CreativeModeTab.Output modifier);
}
