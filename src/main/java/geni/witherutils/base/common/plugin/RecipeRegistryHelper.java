//package geni.witherutils.base.common.plugin;
//
//import java.util.List;
//
//import geni.witherutils.base.common.init.WUTBlocks;
//import geni.witherutils.base.common.init.WUTEnchants;
//import geni.witherutils.base.common.init.WUTItems;
//import geni.witherutils.base.common.init.WUTUpgrades;
//import mezz.jei.api.constants.RecipeTypes;
//import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
//import mezz.jei.api.registration.IRecipeRegistration;
//import net.minecraft.world.item.ItemStack;
//
//public class RecipeRegistryHelper {
//
//    private RecipeRegistryHelper() {
//    }
//    
//    public static void addArmorBasicEnergyRecipe(IRecipeRegistration registry, ItemStack leftItemL, ItemStack leftItemR)
//    {
//        IVanillaRecipeFactory factory = registry.getVanillaRecipeFactory();
//        ItemStack armorstack = leftItemL.copy();
//        ItemStack soulbankstack = leftItemR.copy();
//
//		if(leftItemR.getItem() == WUTItems.SOULBANK_BASIC.get().asItem())
//		{
//	        soulbankstack = WUTItems.SOULBANK_BASIC.get().getDefaultInstance();
//	        armorstack.enchant(WUTEnchants.ENERGY.get(), 1);
//
//			registry.addRecipes(RecipeTypes.ANVIL, List.of(factory.createAnvilRecipe(leftItemL, List.of(soulbankstack), List.of(armorstack))));
//			addArmorAdvancedEnergyRecipe(registry, armorstack, WUTItems.SOULBANK_ADVANCED.get().getDefaultInstance());
//
//			if(leftItemL.getItem() == WUTItems.STEELARMOR_HELMET.get().asItem())
//			{
//				ItemStack inputstack = leftItemL.copy();
//				ItemStack outputstack = leftItemL.copy();
//				inputstack.enchant(WUTEnchants.ENERGY.get(), 1);
//				outputstack.enchant(WUTEnchants.ENERGY.get(), 1);
//				outputstack.enchant(WUTEnchants.SOLAR_POWER.get(), 1);
//				registry.addRecipes(RecipeTypes.ANVIL, List.of(factory.createAnvilRecipe(inputstack, List.of(WUTBlocks.SOLARBASIC.get().asItem().getDefaultInstance()), List.of(outputstack))));
//				inputstack.enchant(WUTEnchants.ENERGY.get(), 2);
//				outputstack.enchant(WUTEnchants.ENERGY.get(), 2);
//				outputstack.enchant(WUTEnchants.SOLAR_POWER.get(), 2);
//				registry.addRecipes(RecipeTypes.ANVIL, List.of(factory.createAnvilRecipe(inputstack, List.of(WUTBlocks.SOLARADV.get().asItem().getDefaultInstance()), List.of(outputstack))));
//				inputstack.enchant(WUTEnchants.ENERGY.get(), 3);
//				outputstack.enchant(WUTEnchants.ENERGY.get(), 3);
//				outputstack.enchant(WUTEnchants.SOLAR_POWER.get(), 3);
//				registry.addRecipes(RecipeTypes.ANVIL, List.of(factory.createAnvilRecipe(inputstack, List.of(WUTBlocks.SOLARULTRA.get().asItem().getDefaultInstance()), List.of(outputstack))));
//				
//				addHelmetVisionRecipe(registry, armorstack, new ItemStack(WUTUpgrades.UPGRADEVISION.get(), 1));
//			}
//			if(leftItemL.getItem() == WUTItems.STEELARMOR_CHEST.get().asItem())
//			{
//				addChestFeatherRecipe(registry, armorstack, new ItemStack(WUTUpgrades.UPGRADEFEATHER.get(), 1));
//			}
//			if(leftItemL.getItem() == WUTItems.STEELARMOR_LEGGINGS.get().asItem())
//			{
//				addLegSprintingRecipe(registry, armorstack, new ItemStack(WUTUpgrades.UPGRADESPEED.get(), 1));
//			}
//			if(leftItemL.getItem() == WUTItems.STEELARMOR_BOOTS.get().asItem())
//			{
//				addFeetJumpingRecipe(registry, armorstack, new ItemStack(WUTUpgrades.UPGRADEJUMP.get(), 1));
//				addFeetSquidJumpingRecipe(registry, armorstack, new ItemStack(WUTUpgrades.UPGRADESQUID.get(), 1));
//			}
//		}
//    }
//    public static void addArmorAdvancedEnergyRecipe(IRecipeRegistration registry, ItemStack leftItemL, ItemStack leftItemR)
//    {
//        IVanillaRecipeFactory factory = registry.getVanillaRecipeFactory();
//        ItemStack armorstack = leftItemL.copy();
//        ItemStack soulbankstack = leftItemR.copy();
//
//		if(leftItemR.getItem() == WUTItems.SOULBANK_ADVANCED.get().asItem())
//		{
//	        armorstack.enchant(WUTEnchants.ENERGY.get(), 2);
//			registry.addRecipes(RecipeTypes.ANVIL, List.of(factory.createAnvilRecipe(leftItemL, List.of(soulbankstack), List.of(armorstack))));
//			addArmorUltraEnergyRecipe(registry, armorstack, WUTItems.SOULBANK_ULTRA.get().getDefaultInstance());
//		}
//    }
//    public static void addArmorUltraEnergyRecipe(IRecipeRegistration registry, ItemStack leftItemL, ItemStack leftItemR)
//    {
//        IVanillaRecipeFactory factory = registry.getVanillaRecipeFactory();
//        ItemStack armorstack = leftItemL.copy();
//        ItemStack soulbankstack = leftItemR.copy();
//
//		if(leftItemR.getItem() == WUTItems.SOULBANK_ULTRA.get().asItem())
//		{
//	        armorstack.enchant(WUTEnchants.ENERGY.get(), 3);
//			registry.addRecipes(RecipeTypes.ANVIL, List.of(factory.createAnvilRecipe(leftItemL, List.of(soulbankstack), List.of(armorstack))));
//		}
//    }
//
//    public static void addHelmetBasicSolarRecipe(IRecipeRegistration registry, ItemStack leftItemL, ItemStack leftItemR)
//    {
//        IVanillaRecipeFactory factory = registry.getVanillaRecipeFactory();
//        ItemStack armorstack = leftItemL.copy();
//        ItemStack solarpanelstack = leftItemR.copy();
//
//		if(leftItemR.getItem() == WUTBlocks.SOLARBASIC.get().asItem())
//		{
//	        armorstack.enchant(WUTEnchants.SOLAR_POWER.get(), 1);
//			registry.addRecipes(RecipeTypes.ANVIL, List.of(factory.createAnvilRecipe(leftItemL, List.of(solarpanelstack), List.of(armorstack))));
//		}
//    }
//    public static void addHelmetAdvancedSolarRecipe(IRecipeRegistration registry, ItemStack leftItemL, ItemStack leftItemR)
//    {
//        IVanillaRecipeFactory factory = registry.getVanillaRecipeFactory();
//        ItemStack armorstack = leftItemL.copy();
//        ItemStack solarpanelstack = leftItemR.copy();
//
//		if(leftItemR.getItem() == WUTBlocks.SOLARADV.get().asItem())
//		{
//	        armorstack.enchant(WUTEnchants.SOLAR_POWER.get(), 2);
//			registry.addRecipes(RecipeTypes.ANVIL, List.of(factory.createAnvilRecipe(leftItemL, List.of(solarpanelstack), List.of(armorstack))));
//		}
//    }
//    public static void addHelmetUltraSolarRecipe(IRecipeRegistration registry, ItemStack leftItemL, ItemStack leftItemR)
//    {
//        IVanillaRecipeFactory factory = registry.getVanillaRecipeFactory();
//        ItemStack armorstack = leftItemL.copy();
//        ItemStack solarpanelstack = leftItemR.copy();
//
//		if(leftItemR.getItem() == WUTBlocks.SOLARULTRA.get().asItem())
//		{
//	        armorstack.enchant(WUTEnchants.SOLAR_POWER.get(), 3);
//			registry.addRecipes(RecipeTypes.ANVIL, List.of(factory.createAnvilRecipe(leftItemL, List.of(solarpanelstack), List.of(armorstack))));
//		}
//    }
//
//    public static void addHelmetVisionRecipe(IRecipeRegistration registry, ItemStack leftItemL, ItemStack leftItemR)
//    {
//        IVanillaRecipeFactory factory = registry.getVanillaRecipeFactory();
//
//        ItemStack armorstack = leftItemL.copy();
//        ItemStack visionstack = leftItemR.copy();
//
//		if(leftItemR.getItem() == WUTUpgrades.UPGRADEVISION.get())
//		{
//	        armorstack.enchant(WUTEnchants.NIGHT_VISION.get(), 1);
//			registry.addRecipes(RecipeTypes.ANVIL, List.of(factory.createAnvilRecipe(leftItemL, List.of(visionstack), List.of(armorstack))));
//		}
//    }
//    public static void addChestFeatherRecipe(IRecipeRegistration registry, ItemStack leftItemL, ItemStack leftItemR)
//    {
//        IVanillaRecipeFactory factory = registry.getVanillaRecipeFactory();
//        ItemStack armorstack = leftItemL.copy();
//        ItemStack featherstack = leftItemR.copy();
//
//		if(leftItemR.getItem() == WUTUpgrades.UPGRADEFEATHER.get())
//		{
//	        armorstack.enchant(WUTEnchants.FEATHER_FALL.get(), 1);
//			registry.addRecipes(RecipeTypes.ANVIL, List.of(factory.createAnvilRecipe(leftItemL, List.of(featherstack), List.of(armorstack))));
//		}
//    }
//    public static void addFeetJumpingRecipe(IRecipeRegistration registry, ItemStack leftItemL, ItemStack leftItemR)
//    {
//        IVanillaRecipeFactory factory = registry.getVanillaRecipeFactory();
//        ItemStack armorstack = leftItemL.copy();
//        ItemStack lilypadstack = leftItemR.copy();
//
//		if(leftItemR.getItem() == WUTUpgrades.UPGRADEJUMP.get())
//		{
//	        armorstack.enchant(WUTEnchants.JUMPING.get(), 1);
//			registry.addRecipes(RecipeTypes.ANVIL, List.of(factory.createAnvilRecipe(leftItemL, List.of(lilypadstack), List.of(armorstack))));
//		}
//    }
//    public static void addFeetSquidJumpingRecipe(IRecipeRegistration registry, ItemStack leftItemL, ItemStack leftItemR)
//    {
//        IVanillaRecipeFactory factory = registry.getVanillaRecipeFactory();
//        ItemStack armorstack = leftItemL.copy();
//        ItemStack lilypadstack = leftItemR.copy();
//
//		if(leftItemR.getItem() == WUTUpgrades.UPGRADESQUID.get())
//		{
//	        armorstack.enchant(WUTEnchants.SQUID_JUMP.get(), 1);
//			registry.addRecipes(RecipeTypes.ANVIL, List.of(factory.createAnvilRecipe(leftItemL, List.of(lilypadstack), List.of(armorstack))));
//		}
//    }
//    public static void addLegSprintingRecipe(IRecipeRegistration registry, ItemStack leftItemL, ItemStack leftItemR)
//    {
//        IVanillaRecipeFactory factory = registry.getVanillaRecipeFactory();
//        ItemStack armorstack = leftItemL.copy();
//        ItemStack redstonestack = leftItemR.copy();
//
//		if(leftItemR.getItem() == WUTUpgrades.UPGRADESPEED.get())
//		{
//	        armorstack.enchant(WUTEnchants.SPRINTING.get(), 1);
//			registry.addRecipes(RecipeTypes.ANVIL, List.of(factory.createAnvilRecipe(leftItemL, List.of(redstonestack), List.of(armorstack))));
//		}
//    }
//}