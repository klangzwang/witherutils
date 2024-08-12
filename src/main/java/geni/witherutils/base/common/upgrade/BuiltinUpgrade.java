package geni.witherutils.base.common.upgrade;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.api.upgrade.WUTUpgrade;
import geni.witherutils.base.common.init.WUTUpgrades;
import net.minecraft.world.item.Rarity;

public enum BuiltinUpgrade {
	
    VOLUME("volume"),
    DISPENSER("dispenser"),
    ITEM_LIFE("item_life"),
    ENTITY_TRACKER("entity_tracker"),
    BLOCK_TRACKER("block_tracker"),
    SPEED("speed"),
    SEARCH("search"),
    COORDINATE_TRACKER("coordinate_tracker"),
    RANGE("range"),
    SECURITY("security"),
    MAGNET("magnet"),
    CHARGING("charging"),
    ARMOR("armor"),
    JET_BOOTS("jet_boots", 5),
    NIGHT_VISION("night_vision"),
    SCUBA("scuba"),
    CREATIVE("creative"),
    INVENTORY("inventory"),
    JUMPING("jumping", 4),
    FLIPPERS("flippers"),
    STANDBY("standby"),
    MINIGUN("minigun"),
    GILDED("gilded"),
    ENDER_VISOR("ender_visor"),
    STOMP("stomp"),
    ELYTRA("elytra"),
    CHUNKLOADER("chunkloader");

    private final String name;
    private final int maxTier;
    private final String[] depModIds;

    BuiltinUpgrade(String name)
    {
        this(name, 1);
    }

    BuiltinUpgrade(String name, int maxTier, String... depModIds)
    {
        this.name = name;
        this.maxTier = maxTier;
        this.depModIds = depModIds;
    }

    public String getName()
    {
        return name;
    }

    public int getMaxTier()
    {
        return maxTier;
    }

    public Rarity getRarity()
    {
        return this == CREATIVE ? Rarity.EPIC : Rarity.COMMON;
    }

    public WUTUpgrade registerUpgrade()
    {
    	WUTUpgrade upgrade = WitherUtilsRegistry.getInstance().getUpgradeRegistry()
                .registerUpgrade(WitherUtilsRegistry.loc(name), maxTier, depModIds);
        return WUTUpgrades.registerBuiltin(this, upgrade);
    }
}