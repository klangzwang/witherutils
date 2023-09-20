package geni.witherutils.base.common.plugin;

import java.util.List;

import geni.witherutils.base.common.init.WUTEnchants;
import geni.witherutils.base.common.init.WUTItems;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class WandRecipeCategory {

    private WandRecipeCategory()
    {
    }
    
    public static void addWandBasicEnergyRecipe(IRecipeRegistration registry, ItemStack leftItemL, ItemStack leftItemR)
    {
        IVanillaRecipeFactory factory = registry.getVanillaRecipeFactory();
        ItemStack wandstack = leftItemL.copy();
        ItemStack soulbankstack = leftItemR.copy();
        
        if(leftItemR.getItem() == WUTItems.SOULBANK_BASIC.get().asItem())
        {
            soulbankstack = WUTItems.SOULBANK_BASIC.get().getDefaultInstance();
            wandstack.enchant(WUTEnchants.ENERGY.get(), 1);

            registry.addRecipes(RecipeTypes.ANVIL, List.of(factory.createAnvilRecipe(leftItemL, List.of(soulbankstack), List.of(wandstack))));
            addWandAdvancedEnergyRecipe(registry, wandstack, WUTItems.SOULBANK_ADVANCED.get().getDefaultInstance());
        }
    }
    
    public static void addWandAdvancedEnergyRecipe(IRecipeRegistration registry, ItemStack leftItemL, ItemStack leftItemR)
    {
        IVanillaRecipeFactory factory = registry.getVanillaRecipeFactory();
        ItemStack wandstack = leftItemL.copy();
        ItemStack soulbankstack = leftItemR.copy();

        if(leftItemR.getItem() == WUTItems.SOULBANK_ADVANCED.get().asItem())
        {
        	wandstack.enchant(WUTEnchants.ENERGY.get(), 2);
            registry.addRecipes(RecipeTypes.ANVIL, List.of(factory.createAnvilRecipe(leftItemL, List.of(soulbankstack), List.of(wandstack))));
            addWandUltraEnergyRecipe(registry, wandstack, WUTItems.SOULBANK_ULTRA.get().getDefaultInstance());
        }
    }
    
    public static void addWandUltraEnergyRecipe(IRecipeRegistration registry, ItemStack leftItemL, ItemStack leftItemR)
    {
        IVanillaRecipeFactory factory = registry.getVanillaRecipeFactory();
        ItemStack wandstack = leftItemL.copy();
        ItemStack soulbankstack = leftItemR.copy();

        if(leftItemR.getItem() == WUTItems.SOULBANK_ULTRA.get().asItem())
        {
        	wandstack.enchant(WUTEnchants.ENERGY.get(), 3);
            registry.addRecipes(RecipeTypes.ANVIL, List.of(factory.createAnvilRecipe(leftItemL, List.of(soulbankstack), List.of(wandstack))));
        }
    }
    
    public static void addWandPearlPortRecipe(IRecipeRegistration registry, ItemStack leftItemL, ItemStack leftItemR)
    {
        IVanillaRecipeFactory factory = registry.getVanillaRecipeFactory();
        ItemStack wandstack = leftItemL.copy();
        ItemStack pearlstack = leftItemR.copy();

        if(leftItemR.getItem() == Items.ENDER_PEARL.asItem())
        {
        	wandstack.enchant(WUTEnchants.PEARL.get(), 1);
            registry.addRecipes(RecipeTypes.ANVIL, List.of(factory.createAnvilRecipe(leftItemL, List.of(pearlstack), List.of(wandstack))));
        }
    }
}
