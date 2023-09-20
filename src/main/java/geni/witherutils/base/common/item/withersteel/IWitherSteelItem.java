package geni.witherutils.base.common.item.withersteel;

import javax.annotation.Nonnull;

import geni.witherutils.base.common.init.WUTEnchants;
import geni.witherutils.core.common.util.EnergyUtil;
import net.minecraft.world.item.Item;
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

    default ItemStack createFullyUpgradedStack(Item item)
    {
        ItemStack is = new ItemStack(item);
        
        is.enchant(WUTEnchants.ENERGY.get(), 1);
        is.enchant(WUTEnchants.ENERGY.get(), 2);
        is.enchant(WUTEnchants.ENERGY.get(), 3);
        
        EnergyUtil.setFull(is);
        
        return is;
    }
}
