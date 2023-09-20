package geni.witherutils.base.common.init;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.item.withersteel.armor.upgrades.featherfall.FeatherFallUpgrade;
import geni.witherutils.base.common.item.withersteel.armor.upgrades.jump.JumpingUpgrade;
import geni.witherutils.base.common.item.withersteel.armor.upgrades.sprinting.SprintingUpgrade;
import geni.witherutils.base.common.item.withersteel.armor.upgrades.squidring.SquidRingUpgrade;
import geni.witherutils.base.common.item.withersteel.armor.upgrades.vision.NightVisionUpgrade;
import geni.witherutils.core.common.registration.impl.UpgradeDeferredRegister;
import geni.witherutils.core.common.registration.impl.UpgradeRegistryObject;
import net.minecraft.world.item.Item;

public class WUTUpgrades {

	public static final UpgradeDeferredRegister UPGRADES = new UpgradeDeferredRegister(WitherUtils.MODID);
	
	public static final UpgradeRegistryObject<Item> UPGRADEFEATHER = UPGRADES.register("upgrade_feather", FeatherFallUpgrade::new);
    public static final UpgradeRegistryObject<Item> UPGRADEVISION = UPGRADES.register("upgrade_vision", NightVisionUpgrade::new);
    public static final UpgradeRegistryObject<Item> UPGRADEJUMP = UPGRADES.register("upgrade_jump", JumpingUpgrade::new);
    public static final UpgradeRegistryObject<Item> UPGRADESPEED = UPGRADES.register("upgrade_speed", SprintingUpgrade::new);
    public static final UpgradeRegistryObject<Item> UPGRADESQUID = UPGRADES.register("upgrade_squid", SquidRingUpgrade::new);
}
