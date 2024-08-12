package geni.witherutils.base.common.item.upgrade;

import java.util.stream.Stream;

import geni.witherutils.api.upgrade.IUpgradeItem;
import geni.witherutils.api.upgrade.WUTUpgrade;
import geni.witherutils.base.common.base.CreativeTabStackProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class UpgradeItem extends Item implements IUpgradeItem, CreativeTabStackProvider {
	
    private final WUTUpgrade upgrade;
    private final int tier;

    public UpgradeItem(WUTUpgrade upgrade, int tier, Rarity rarity)
    {
        this(upgrade, tier, new Item.Properties().rarity(rarity));
    }

    public UpgradeItem(WUTUpgrade upgrade, int tier, Properties properties)
    {
        super(properties);
        this.upgrade = upgrade;
        this.tier = tier;
    }

    @Override
    public WUTUpgrade getUpgradeType()
    {
        return upgrade;
    }

    @Override
    public int getUpgradeTier()
    {
        return tier;
    }

    public static UpgradeItem of(ItemStack stack)
    {
        return (UpgradeItem) stack.getItem();
    }

	@Override
	public Stream<ItemStack> getStacksForItem()
	{
        return Stream.of(new ItemStack(this));
	}
}
