package geni.witherutils.base.common.item.withersteel.armor.upgrades.sprinting;

import geni.witherutils.base.common.event.WitherKeyMappingHandler;
import geni.witherutils.base.common.item.withersteel.armor.SteelArmorItem;
import geni.witherutils.base.common.item.withersteel.armor.upgrades.SteelUpgradeItem;
import geni.witherutils.core.common.util.EnergyUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SprintingUpgrade extends SteelUpgradeItem {

	public static void onPlayerTick(ItemStack stack, SteelArmorItem item, Player player)
	{
		if (WitherKeyMappingHandler.isSpeedActive)
		{
			if(item.hasChargeMoreThen(stack, 10) && player.getDeltaMovement().horizontalDistance() > 0 && canTurboSprint(player))
			{
				player.setSprinting(true);
				EnergyUtil.extractEnergy(stack, 10, false);
				player.level().addParticle(ParticleTypes.CLOUD, player.getX(), player.getY() + 0.2D, player.getZ(), 0.0D, 0.02D, 0.0D);
			}
			else
			{
				player.setSprinting(false);
			}			
		}

	}

    private static boolean canTurboSprint(Player player)
	{
		return !player.getAbilities().flying && !player.isFallFlying() && !player.isVehicle() && !player.isInWater() && player.onGround() && !player.isInWater();
	}
}
