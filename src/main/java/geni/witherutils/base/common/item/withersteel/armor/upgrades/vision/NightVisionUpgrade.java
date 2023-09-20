package geni.witherutils.base.common.item.withersteel.armor.upgrades.vision;

import geni.witherutils.base.common.event.WitherKeyMappingHandler;
import geni.witherutils.base.common.item.withersteel.armor.SteelArmorItem;
import geni.witherutils.base.common.item.withersteel.armor.upgrades.SteelUpgradeItem;
import geni.witherutils.core.common.util.EnergyUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class NightVisionUpgrade extends SteelUpgradeItem {

	public static void onPlayerTick(ItemStack stack, SteelArmorItem item, Player player)
	{
		 if(item.hasChargeMoreThen(stack, 10) && WitherKeyMappingHandler.isVisionActive)
		 {
			 player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * 20, -1));
			 EnergyUtil.extractEnergy(stack, 10, false);
		 }
		 else
		 {
			 player.removeEffect(MobEffects.NIGHT_VISION);
		 }
	}
}
