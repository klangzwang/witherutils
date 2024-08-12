package geni.witherutils.api.upgrade;

/**
 * Represents an item which can be used as a PneumaticCraft upgrade in machines or other items. You can implement this
 * interface on your own items, or you can use {@link IUpgradeRegistry#makeUpgradeItem(PNCUpgrade, int, net.minecraft.world.item.Rarity)} to create an
 * upgrade with default PneumaticCraft tooltip behaviour.
 * <p>
 * Items that you implement yourself should take a {@code Supplier&lt;PNCUpgrade&gt;} in their constructor, and
 * store that in a final field. A Supplier is needed because items are registered before PNCUpgrade objects.
 */
public interface IUpgradeItem {
    /**
     * Return the PNCUpgrade object associated with this item.
     *
     * @return the PNC upgrade
     */
    WUTUpgrade getUpgradeType();

    /**
     * Get the tier of this upgrade.
     *
     * @return the upgrade tier
     */
    default int getUpgradeTier()
    {
        return 1;
    }
}
