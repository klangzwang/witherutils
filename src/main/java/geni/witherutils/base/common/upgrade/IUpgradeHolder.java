package geni.witherutils.base.common.upgrade;

import net.neoforged.neoforge.items.IItemHandler;

@FunctionalInterface
public interface IUpgradeHolder {
	
    IItemHandler getUpgradeHandler();

    default void onUpgradesChanged() {}
}
