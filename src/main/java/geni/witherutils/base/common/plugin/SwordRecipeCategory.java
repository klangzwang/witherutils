//package geni.witherutils.base.common.plugin;
//
//import java.util.List;
//
//import geni.witherutils.base.common.init.WUTEnchants;
//import geni.witherutils.base.common.init.WUTItems;
//import mezz.jei.api.constants.RecipeTypes;
//import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
//import mezz.jei.api.registration.IRecipeRegistration;
//import net.minecraft.world.item.ItemStack;
//
//public class SwordRecipeCategory {
//
//    private SwordRecipeCategory()
//    {
//    }
//    
//    public static void addSwordBasicEnergyRecipe(IRecipeRegistration registry, ItemStack leftItemL, ItemStack leftItemR)
//    {
//        IVanillaRecipeFactory factory = registry.getVanillaRecipeFactory();
//        ItemStack swordstack = leftItemL.copy();
//        ItemStack soulbankstack = leftItemR.copy();
//        
//        if(leftItemR.getItem() == WUTItems.SOULBANK_BASIC.get().asItem())
//        {
//            soulbankstack = WUTItems.SOULBANK_BASIC.get().getDefaultInstance();
//            swordstack.enchant(WUTEnchants.ENERGY.get(), 1);
//
//            registry.addRecipes(RecipeTypes.ANVIL, List.of(factory.createAnvilRecipe(leftItemL, List.of(soulbankstack), List.of(swordstack))));
//            addSwordAdvancedEnergyRecipe(registry, swordstack, WUTItems.SOULBANK_ADVANCED.get().getDefaultInstance());
//        }
//    }
//    
//    public static void addSwordAdvancedEnergyRecipe(IRecipeRegistration registry, ItemStack leftItemL, ItemStack leftItemR)
//    {
//        IVanillaRecipeFactory factory = registry.getVanillaRecipeFactory();
//        ItemStack swordstack = leftItemL.copy();
//        ItemStack soulbankstack = leftItemR.copy();
//
//        if(leftItemR.getItem() == WUTItems.SOULBANK_ADVANCED.get().asItem())
//        {
//            swordstack.enchant(WUTEnchants.ENERGY.get(), 2);
//            registry.addRecipes(RecipeTypes.ANVIL, List.of(factory.createAnvilRecipe(leftItemL, List.of(soulbankstack), List.of(swordstack))));
//            addSwordUltraEnergyRecipe(registry, swordstack, WUTItems.SOULBANK_ULTRA.get().getDefaultInstance());
//        }
//    }
//    
//    public static void addSwordUltraEnergyRecipe(IRecipeRegistration registry, ItemStack leftItemL, ItemStack leftItemR)
//    {
//        IVanillaRecipeFactory factory = registry.getVanillaRecipeFactory();
//        ItemStack swordstack = leftItemL.copy();
//        ItemStack soulbankstack = leftItemR.copy();
//
//        if(leftItemR.getItem() == WUTItems.SOULBANK_ULTRA.get().asItem())
//        {
//            swordstack.enchant(WUTEnchants.ENERGY.get(), 3);
//            registry.addRecipes(RecipeTypes.ANVIL, List.of(factory.createAnvilRecipe(leftItemL, List.of(soulbankstack), List.of(swordstack))));
//        }
//    }
//}
