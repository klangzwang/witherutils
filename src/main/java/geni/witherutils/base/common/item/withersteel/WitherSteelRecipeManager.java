package geni.witherutils.base.common.item.withersteel;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

public class WitherSteelRecipeManager {

	public static WitherSteelRecipeManager instance = new WitherSteelRecipeManager();

	public WitherSteelRecipeManager()
	{
    }

	@SubscribeEvent
	public void handleAnvilEvent(AnvilUpdateEvent evt)
	{
		if (evt.getLeft() == null || evt.getRight() == null)
		{
			return;
		}
//		if (evt.getLeft().getItem() instanceof IWitherSteelItem iwither)
//		{
//			if (evt.getRight().getItem() == WUTItems.WITHERSTEEL_INGOT.get())
//			{
//				handleRepair(evt);
//			}
//		}
	}

//	private void handleRepair(AnvilUpdateEvent evt)
//	{
//		if (evt.getLeft().getCount() == 1 && evt.getLeft().getItem() instanceof IWitherSteelItem)
//		{
//			final ItemStack targetStack = evt.getLeft();
//			final IWitherSteelItem item = (IWitherSteelItem) targetStack.getItem();
//			if (item.isItemForRepair(evt.getRight()))
//			{
//				final ItemStack ingots = evt.getRight();
//
//				final int maxIngots = item.getIngotsRequiredForFullRepair();
//				int ingouts = ingots.getCount();
//				final int damage = targetStack.getDamageValue();
//				final int maxDamage = targetStack.getMaxDamage();
//
//				final double damPerc = (double) damage / maxDamage;
//				int requiredIngots = (int) Math.ceil(damPerc * maxIngots);
//				if (ingouts > requiredIngots)
//				{
//					ingouts = requiredIngots;
//				}
//
//				final int damageAddedPerIngot = (int) Math.ceil((double) maxDamage / maxIngots);
//				final int totalDamageRemoved = damageAddedPerIngot * ingouts;
//
//				final ItemStack resultStack = targetStack.copy();
//				resultStack.setDamageValue(Math.max(0, damage - totalDamageRemoved));
//
//				evt.setOutput(resultStack);
//				evt.setCost(ingouts + (int) Math.ceil(getEnchantmentRepairCost(resultStack.copy()) / 2d));
//				evt.setMaterialCost(ingouts);
//			}
//		}
//	}
//
//	public static int getEnchantmentRepairCost(@Nonnull ItemStack itemStack)
//	{
//		int res = 0;
//		Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(itemStack);
//		Iterator<Enchantment> iter = map1.keySet().iterator();
//		while (iter.hasNext())
//		{
//			Enchantment i1 = iter.next();
//			Enchantment enchantment = i1;
//
//			int level = map1.get(enchantment).intValue();
//			if (enchantment.canApplyAtEnchantingTable(itemStack))
//			{
//				if (level > enchantment.getMaxLevel())
//				{
//					level = enchantment.getMaxLevel();
//				}
//				
//				int costPerLevel = 0;
//				
//				switch (enchantment.getRarity())
//				{
//					case VERY_RARE :
//						costPerLevel = 8;
//						break;
//					case RARE :
//						costPerLevel = 4;
//						break;
//					case UNCOMMON :
//						costPerLevel = 2;
//						break;
//					case COMMON :
//						costPerLevel = 1;
//				}
//				res += costPerLevel * level;
//			}
//		}
//		return res;
//	}
//
//	/*
//	 * 
//	 * Energy Enchantment
//	 * 
//	 */
//	@SubscribeEvent
//	public void handleEnergyEnchantment(AnvilUpdateEvent event)
//	{
//		ItemStack leftItem = event.getLeft();
//		ItemStack rightItem = event.getRight();
//		
//		if(leftItem.getItem() instanceof SteelArmorItem armor && rightItem.getItem() instanceof SoulBankItem bank)
//		{
//			ItemStack stack = leftItem.copy();
//			ItemStack bankStack = bank.getDefaultInstance();
//			armor.getPowerLevel(stack);
//			
//			if(armor.getLevel() == 0 && SoulBankUtil.getSoulBankData(bankStack).get() == DefaultSoulBankData.BASIC)
//			{
//				stack.enchant(WUTEnchants.ENERGY.get(), 1);
//
//				event.setCost(3);
//				event.setOutput(stack);
//				event.setMaterialCost(1);
//			}
//			if(armor.getLevel() == 1 && SoulBankUtil.getSoulBankData(bankStack).get() == DefaultSoulBankData.ADVANCED)
//			{
//				stack.enchant(WUTEnchants.ENERGY.get(), 2);
//
//				event.setCost(6);
//				event.setOutput(stack);
//				event.setMaterialCost(1);
//			}
//			if(armor.getLevel() == 2 && SoulBankUtil.getSoulBankData(bankStack).get() == DefaultSoulBankData.ULTRA)
//			{
//				stack.enchant(WUTEnchants.ENERGY.get(), 3);
//
//				event.setCost(12);
//				event.setOutput(stack);
//				event.setMaterialCost(1);
//			}
//			
//			if(!stack.isEmpty())
//			{
//				stack.resetHoverName();
//				stack.setHoverName(Component.literal(ChatFormatting.YELLOW + stack.getHoverName().getString() + " §r§f" + "■-§9Powered§r§f-■"));
//			}
//		}
//	}
//	
//	/*
//	 * 
//	 * NightVision Enchantment
//	 * 
//	 */
//	@SubscribeEvent
//	public void handleVisionEnchantment(AnvilUpdateEvent event)
//	{
//		ItemStack leftItem = event.getLeft();
//		ItemStack rightItem = event.getRight();
//
//		if(leftItem.getItem() == WUTItems.STEELARMOR_HELMET.get() && leftItem.getEnchantmentLevel(WUTEnchants.NIGHT_VISION.get()) <= 0 && leftItem.getEnchantmentLevel(WUTEnchants.ENERGY.get()) > 0)
//		{
//			ItemStack stack = leftItem.copy();
//			if(rightItem.getItem() == WUTUpgrades.UPGRADEVISION.get().asItem())
//			{
//				stack.enchant(WUTEnchants.NIGHT_VISION.get(), 1);
//				event.setCost(4);
//				event.setOutput(stack);
//				event.setMaterialCost(1);				
//			}
//		}
//	}
//	
//	/*
//	 * 
//	 * Sprinting Enchantment
//	 * 
//	 */
//	@SubscribeEvent
//	public void handleSprintingEnchantment(AnvilUpdateEvent event)
//	{
//		ItemStack leftItem = event.getLeft();
//		ItemStack rightItem = event.getRight();
//
//		if(leftItem.getItem() == WUTItems.STEELARMOR_LEGGINGS.get() && leftItem.getEnchantmentLevel(WUTEnchants.SPRINTING.get()) <= 0 && leftItem.getEnchantmentLevel(WUTEnchants.ENERGY.get()) > 0)
//		{
//			ItemStack stack = leftItem.copy();
//			if(rightItem.getItem() == WUTUpgrades.UPGRADESPEED.get().asItem())
//			{
//				stack.enchant(WUTEnchants.SPRINTING.get(), 1);
//				event.setCost(4);
//				event.setOutput(stack);
//				event.setMaterialCost(64);
//			}
//		}
//	}
//	
//	/*
//	 * 
//	 * SolarCharge Enchantment
//	 * 
//	 */
//	@SubscribeEvent
//	public void handleSolarEnchantment(AnvilUpdateEvent event)
//	{
//		ItemStack leftItem = event.getLeft();
//		ItemStack rightItem = event.getRight();
//
//		if(leftItem.getItem() == WUTItems.STEELARMOR_HELMET.get() && leftItem.getItem() instanceof SteelArmorItem armor)
//		{
//			ItemStack stack = leftItem.copy();
//			if(EnchantmentHelper.getEnchantments(leftItem).get(WUTEnchants.ENERGY.get()) != null)
//			{
//				if(rightItem.getItem() == WUTBlocks.SOLARBASIC.get().asItem() && armor.getSolarLevel(leftItem) == 0)
//				{
//					stack.enchant(WUTEnchants.SOLAR_POWER.get(), 1);
//
//					event.setCost(3);
//					event.setOutput(stack);
//					event.setMaterialCost(1);
//				}
//				if(rightItem.getItem() == WUTBlocks.SOLARADV.get().asItem() && armor.getSolarLevel(leftItem) == 1)
//				{
//					stack.enchant(WUTEnchants.SOLAR_POWER.get(), 2);
//
//					event.setCost(6);
//					event.setOutput(stack);
//					event.setMaterialCost(1);
//				}
//				if(rightItem.getItem() == WUTBlocks.SOLARULTRA.get().asItem() && armor.getSolarLevel(leftItem) == 2)
//				{
//					stack.enchant(WUTEnchants.SOLAR_POWER.get(), 3);
//
//					event.setCost(12);
//					event.setOutput(stack);
//					event.setMaterialCost(1);
//				}
//			}
//		}
//	}
//	
//	/*
//	 * 
//	 * DoubleJump Enchantment
//	 * 
//	 */
//	@SubscribeEvent
//	public void handleJumpingEnchantment(AnvilUpdateEvent event)
//	{
//		ItemStack leftItem = event.getLeft();
//		ItemStack rightItem = event.getRight();
//
//		if(leftItem.getItem() == WUTItems.STEELARMOR_BOOTS.get() && leftItem.getEnchantmentLevel(WUTEnchants.JUMPING.get()) <= 0 && leftItem.getEnchantmentLevel(WUTEnchants.ENERGY.get()) > 0)
//		{
//			ItemStack stack = leftItem.copy();
//			if(rightItem.getItem() == WUTUpgrades.UPGRADEJUMP.get().asItem())
//			{
//				stack.enchant(WUTEnchants.JUMPING.get(), 1);
//				event.setCost(4);
//				event.setOutput(stack);
//				event.setMaterialCost(12);
//			}
//		}
//	}
//	
//	/*
//	 * 
//	 * SquidJump Enchantment
//	 * 
//	 */
//	@SubscribeEvent
//	public void handleSquidJumpingEnchantment(AnvilUpdateEvent event)
//	{
//		ItemStack leftItem = event.getLeft();
//		ItemStack rightItem = event.getRight();
//
//		if(leftItem.getItem() == WUTItems.STEELARMOR_BOOTS.get() && leftItem.getEnchantmentLevel(WUTEnchants.SQUID_JUMP.get()) <= 0 && leftItem.getEnchantmentLevel(WUTEnchants.ENERGY.get()) > 0)
//		{
//			ItemStack stack = leftItem.copy();
//			if(rightItem.getItem() == WUTUpgrades.UPGRADESQUID.get().asItem())
//			{
//				stack.enchant(WUTEnchants.SQUID_JUMP.get(), 1);
//				event.setCost(4);
//				event.setOutput(stack);
//				event.setMaterialCost(12);
//			}
//		}
//	}
//	
//	/*
//	 * 
//	 * FeatherFall Enchantment
//	 * 
//	 */
//	@SubscribeEvent
//	public void handleFeatherFallEnchantment(AnvilUpdateEvent event)
//	{
//		ItemStack leftItem = event.getLeft();
//		ItemStack rightItem = event.getRight();
//
//		if(leftItem.getItem() == WUTItems.STEELARMOR_CHEST.get() && leftItem.getEnchantmentLevel(WUTEnchants.FEATHER_FALL.get()) <= 0 && leftItem.getEnchantmentLevel(WUTEnchants.ENERGY.get()) > 0)
//		{
//			ItemStack stack = leftItem.copy();
//			if(rightItem.getItem() == WUTUpgrades.UPGRADEFEATHER.get().asItem())
//			{
//				stack.enchant(WUTEnchants.FEATHER_FALL.get(), 1);
//				event.setCost(4);
//				event.setOutput(stack);
//				event.setMaterialCost(12);
//			}
//		}
//	}
//	
//	@SubscribeEvent
//	public void handleSwordEnergyEnchantment(AnvilUpdateEvent event)
//	{
//		ItemStack leftItem = event.getLeft();
//		ItemStack rightItem = event.getRight();
//
//		if(leftItem.getItem() instanceof SwordSteelItem && rightItem.getItem() instanceof SoulBankItem)
//		{
//			SwordSteelItem sword = (SwordSteelItem) leftItem.getItem();
//			ItemStack stack = leftItem.copy();
//			
//			float bankLevel = SoulBankUtil.getSoulBankData(rightItem).get().getBase();
//			int swordLevel = sword.getPowerLevel(stack);
//			
//			if(swordLevel == 0 && bankLevel == 1)
//			{
//				stack.enchant(WUTEnchants.ENERGY.get(), 1);
//				event.setCost(3);
//				event.setOutput(stack);
//				event.setMaterialCost(1);
//			}
//			else if(swordLevel == 1 && bankLevel == 2)
//			{
//				stack.enchant(WUTEnchants.ENERGY.get(), 2);
//				event.setCost(6);
//				event.setOutput(stack);
//				event.setMaterialCost(1);
//			}
//			else if(swordLevel == 2 && bankLevel == 3)
//			{
//				stack.enchant(WUTEnchants.ENERGY.get(), 3);
//				event.setCost(12);
//				event.setOutput(stack);
//				event.setMaterialCost(1);
//			}
//			
//			if(!stack.isEmpty())
//			{
//				stack.resetHoverName();
//				stack.setHoverName(Component.literal(ChatFormatting.YELLOW + stack.getHoverName().getString() + " §r§f" + "■-§9Powered§r§f-■"));
//			}
//		}
//	}
//	
//	@SubscribeEvent
//	public void handleWandEnergyEnchantment(AnvilUpdateEvent event)
//	{
//		ItemStack leftItem = event.getLeft();
//		ItemStack rightItem = event.getRight();
//		
//		if(leftItem.getItem() instanceof WandSteelItem)
//		{
//			WandSteelItem wand = (WandSteelItem) leftItem.getItem();
//			ItemStack stack = leftItem.copy();
//
//			if(rightItem.getItem() instanceof SoulBankItem)
//			{
//				float bankLevel = SoulBankUtil.getSoulBankData(rightItem).get().getBase();
//				int wandLevel = wand.getPowerLevel(stack);
//				
//				if(wandLevel == 0 && bankLevel == 1)
//				{
//					stack.enchant(WUTEnchants.ENERGY.get(), 1);
//					event.setCost(3);
//					event.setOutput(stack);
//					event.setMaterialCost(1);
//				}
//				else if(wandLevel == 1 && bankLevel == 2)
//				{
//					stack.enchant(WUTEnchants.ENERGY.get(), 2);
//					event.setCost(6);
//					event.setOutput(stack);
//					event.setMaterialCost(1);
//				}
//				else if(wandLevel == 2 && bankLevel == 3)
//				{
//					stack.enchant(WUTEnchants.ENERGY.get(), 3);
//					event.setCost(12);
//					event.setOutput(stack);
//					event.setMaterialCost(1);
//				}
//			}
//			else if(rightItem.getItem() instanceof EnderpearlItem)
//			{
//				int wandLevel = wand.getPowerLevel(stack);
//				int pearlLevel = stack.getEnchantmentLevel(WUTEnchants.PEARL.get());
//
//				if(wandLevel != 0 && pearlLevel == 0)
//				{
//					stack.enchant(WUTEnchants.PEARL.get(), 1);
//					event.setCost(3);
//					event.setOutput(stack);
//					event.setMaterialCost(1);
//				}
//			}
//			
//			if(!stack.isEmpty())
//			{
//				stack.resetHoverName();
//				stack.setHoverName(Component.literal(ChatFormatting.YELLOW + stack.getHoverName().getString() + " §r§f" + "■-§9Powered§r§f-■"));
//			}
//		}
//	}
//	
//	@SubscribeEvent
//	public void handleShieldEnergyEnchantment(AnvilUpdateEvent event)
//	{
//		ItemStack leftItem = event.getLeft();
//		ItemStack rightItem = event.getRight();
//
//		if(leftItem.getItem() instanceof ShieldPoweredItem)
//		{
//			ShieldPoweredItem shield = (ShieldPoweredItem) leftItem.getItem();
//			ItemStack stack = leftItem.copy();
//			
//			if(rightItem.getItem() instanceof SoulBankItem)
//			{
//				float bankLevel = SoulBankUtil.getSoulBankData(rightItem).get().getBase();
//				int shieldLevel = shield.getPowerLevel(stack);
//				
//				if(shieldLevel == 0 && bankLevel == 1)
//				{
//					stack.enchant(WUTEnchants.ENERGY.get(), 1);
//					event.setCost(3);
//					event.setOutput(stack);
//					event.setMaterialCost(1);
//				}
//				else if(shieldLevel == 1 && bankLevel == 2)
//				{
//					stack.enchant(WUTEnchants.ENERGY.get(), 2);
//					event.setCost(6);
//					event.setOutput(stack);
//					event.setMaterialCost(1);
//				}
//				else if(shieldLevel == 2 && bankLevel == 3)
//				{
//					stack.enchant(WUTEnchants.ENERGY.get(), 3);
//					event.setCost(12);
//					event.setOutput(stack);
//					event.setMaterialCost(1);
//				}
//			}
//			
//			if(!stack.isEmpty())
//			{
//				stack.resetHoverName();
//				stack.setHoverName(Component.literal(ChatFormatting.YELLOW + stack.getHoverName().getString() + " §r§f" + "■-§9Powered§r§f-■"));
//			}
//		}
//	}
}
