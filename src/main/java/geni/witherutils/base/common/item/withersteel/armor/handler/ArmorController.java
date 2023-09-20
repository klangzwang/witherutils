package geni.witherutils.base.common.item.withersteel.armor.handler;

import javax.annotation.Nonnull;

import geni.witherutils.base.common.init.WUTEnchants;
import geni.witherutils.base.common.item.withersteel.armor.SteelArmorItem;
import geni.witherutils.base.common.item.withersteel.armor.upgrades.featherfall.FeatherFallUpgrade;
import geni.witherutils.base.common.item.withersteel.armor.upgrades.jump.JumpingUpgrade;
import geni.witherutils.base.common.item.withersteel.armor.upgrades.sprinting.SprintingUpgrade;
import geni.witherutils.base.common.item.withersteel.armor.upgrades.squidring.SquidRingUpgrade;
import geni.witherutils.base.common.item.withersteel.armor.upgrades.vision.NightVisionUpgrade;
import geni.witherutils.core.common.util.NNList;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ArmorController {

	private static final @Nonnull NNList<EquipmentSlot> SLOTS = NNList.of(EquipmentSlot.class);
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		if (event.phase == Phase.START && !event.player.isSpectator() && !(event.player instanceof AbstractClientPlayer) && !(event.player instanceof FakePlayer))
		{
			doPlayerTick(event.player);
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onPlayerTickServer(TickEvent.PlayerTickEvent event)
	{
		if (event.phase == Phase.START && !event.player.isSpectator() && !(event.player instanceof FakePlayer))
		{
			doPlayerTick(event.player);
		}
	}

	private static void doPlayerTick(@Nonnull Player player)
	{
		SLOTS.apply(slot -> {
			ItemStack stack = player.getItemBySlot(slot);
			if (stack.getItem() instanceof SteelArmorItem steelArmor)
			{
				if(!steelArmor.isChargeable(stack) || !steelArmor.hasChargeMoreThen(stack, 0))
					return;

				switch(slot)
				{
					case CHEST :
						if (isFeatherFallUpgradeEquipped(stack))
						{
							FeatherFallUpgrade.onPlayerTick(stack, steelArmor, player);
						}
						break;
					case FEET :
						if (isDoubleJumpUpgradeEquipped(stack))
						{
							JumpingUpgrade.onPlayerTick(stack, steelArmor, player);
						}
						if (isSquidRingUpgradeEquipped(stack))
						{
							SquidRingUpgrade.onPlayerTick(stack, steelArmor, player);
						}
						break;
					case HEAD :
						if (isNightVisionUpgradeEquipped(stack))
						{
							NightVisionUpgrade.onPlayerTick(stack, steelArmor, player);
						}
						break;
					case LEGS :
						if (isSpeedUpgradeEquipped(stack))
						{
							SprintingUpgrade.onPlayerTick(stack, steelArmor, player);
						}
						break;
//					case MAINHAND :
//						break;
//					case OFFHAND :
//						break;
					default :
						break;
				}
			}
		});
	}
	
	public static boolean isNightVisionUpgradeEquipped(@Nonnull ItemStack stack)
	{
		return stack.getEnchantmentLevel(WUTEnchants.NIGHT_VISION.get()) > 0; 
	}
	public static boolean isFeatherFallUpgradeEquipped(@Nonnull ItemStack stack)
	{
		return stack.getEnchantmentLevel(WUTEnchants.FEATHER_FALL.get()) > 0; 
	}
	public static boolean isSpeedUpgradeEquipped(@Nonnull ItemStack stack)
	{
		return stack.getEnchantmentLevel(WUTEnchants.SPRINTING.get()) > 0; 
	}
	public static boolean isDoubleJumpUpgradeEquipped(@Nonnull ItemStack stack)
	{
		return stack.getEnchantmentLevel(WUTEnchants.JUMPING.get()) > 0; 
	}
	public static boolean isSquidRingUpgradeEquipped(@Nonnull ItemStack stack)
	{
		return stack.getEnchantmentLevel(WUTEnchants.SQUID_JUMP.get()) > 0; 
	}
}
