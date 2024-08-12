package geni.witherutils.base.common.upgrade;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.api.upgrade.IUpgradeItem;
import geni.witherutils.api.upgrade.WUTUpgrade;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public class UpgradeCache {
	
    private byte[] countCache;
    private final IUpgradeHolder holder;

    public UpgradeCache(IUpgradeHolder holder)
    {
        this.holder = holder;
    }

    public void invalidateCache()
    {
        countCache = null;
    }

    public int getUpgrades(WUTUpgrade type)
    {
        validateCache();
        return countCache[type.getCacheId()];
    }

    public void validateCache()
    {
        if (countCache != null) return;

        countCache = new byte[largestID() + 1];
        IItemHandler handler = holder.getUpgradeHandler();

        for (int i = 0; i < handler.getSlots(); i++)
        {
            ItemStack stack = handler.getStackInSlot(i);
            if (stack.getItem() instanceof IUpgradeItem upgradeItem)
            {
                WUTUpgrade upgradeType = upgradeItem.getUpgradeType();
                if (countCache[upgradeType.getCacheId()] != 0)
                {
                    continue;
                }
                countCache[upgradeType.getCacheId()] = (byte)(stack.getCount() * upgradeItem.getUpgradeTier());
            }
            else if (!stack.isEmpty())
            {
                throw new IllegalStateException("found non-upgrade item in an upgrade handler! " + stack);
            }
        }
        holder.onUpgradesChanged();
    }

    private int largestID()
    {
        int max = 0;
        for (WUTUpgrade upgrade : WitherUtilsRegistry.getInstance().getUpgradeRegistry().getKnownUpgrades())
        {
            max = Math.max(max, upgrade.getCacheId());
        }
        return max;
    }
}
