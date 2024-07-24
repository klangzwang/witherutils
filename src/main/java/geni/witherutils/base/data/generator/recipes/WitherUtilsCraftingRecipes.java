package geni.witherutils.base.data.generator.recipes;

import java.util.concurrent.CompletableFuture;

import geni.witherutils.base.common.init.WUTBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

public class WitherUtilsCraftingRecipes extends RecipeProvider
{
    public WitherUtilsCraftingRecipes(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(packOutput, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput)
    {
        /*
         * 
         * ANGELBLOCK
         * 
         */
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, WUTBlocks.ANGEL.get())
	        .pattern("FGF")
	        .pattern("GCG")
	        .pattern("FGF")
	        .define('G', Items.GOLD_NUGGET)
	        .define('F', Items.FEATHER)
	        .define('C', WUTBlocks.CASE.get())
	        .unlockedBy("has_case", has(WUTBlocks.CASE.get()))
	        .save(recipeOutput);
        
        /*
         * CASE
         */
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, WUTBlocks.CASE.get())
            .pattern("PPP")
            .pattern("P P")
            .pattern("PPP")
            .define('P', Items.IRON_INGOT)
            .unlockedBy("has_ironingot", has(Items.IRON_INGOT))
            .save(recipeOutput);
    }
}
