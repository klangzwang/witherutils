package geni.witherutils.base.common.item.withersteel.armor.upgrades;

import geni.witherutils.base.common.base.WitherItem;
import net.minecraft.world.item.Item;

public abstract class SteelUpgradeItem extends WitherItem {

	public SteelUpgradeItem()
	{
		super(new Item.Properties().stacksTo(1));
	}
}
