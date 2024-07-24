package geni.witherutils.base.common.item.withersteel;

import javax.annotation.Nonnull;

import net.minecraft.world.item.ItemStack;

public interface IWitherSteelItem {

	default int getIngotsRequiredForFullRepair()
	{
		return 9;
	}
	
	default boolean isItemForRepair(@Nonnull ItemStack right)
	{
		return false;
	}
}
