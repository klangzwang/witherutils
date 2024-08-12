package geni.witherutils.base.common.base;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.items.IItemHandler;

public interface IWitherInventoryItem {

	ICapabilityProvider<ItemStack, Void, IItemHandler> initItemHandlerCap();
}
