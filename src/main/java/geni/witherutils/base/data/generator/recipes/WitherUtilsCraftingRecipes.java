package geni.witherutils.base.data.generator.recipes;

import java.util.concurrent.CompletableFuture;

import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

public class WitherUtilsCraftingRecipes extends RecipeProvider
{
    public WitherUtilsCraftingRecipes(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(packOutput, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeConsumer)
    {
        /*
         * HAMMER
         */
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, WUTItems.HAMMER.get())
                .pattern("  I")
                .pattern("SSI")
                .pattern("  I")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', Tags.Items.RODS)
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .save(recipeConsumer);

        /*
         * ANGELBLOCK
         */
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, WUTBlocks.ANGEL.get())
	        .pattern("FGF")
	        .pattern("GCG")
	        .pattern("FGF")
	        .define('G', Items.GOLD_NUGGET)
	        .define('F', Items.FEATHER)
	        .define('C', WUTBlocks.CASE_BIG.get())
	        .unlockedBy("root", has(WUTItems.HAMMER.get()))
	        .save(recipeConsumer);
        
        /*
         * CASE
         */
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, WUTBlocks.CASE_BIG.get())
            .pattern("III")
            .pattern("ICI")
            .pattern("III")
            .define('I', Items.IRON_INGOT)
            .define('C', Blocks.COAL_BLOCK)
	        .unlockedBy("root", has(WUTItems.HAMMER.get()))
            .save(recipeConsumer);

        /*
         * IRONGEAR
         */
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, WUTItems.IRON_GEAR.get())
                .pattern(" I ")
                .pattern("I I")
                .pattern(" I ")
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy("root", has(WUTItems.HAMMER.get()))
                .save(recipeConsumer);

        /*
         * REMOTE
         */
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, WUTItems.REMOTE.get())
                .pattern("X")
                .pattern("X")
                .pattern("P")
                .define('P', WUTItems.IRON_PLATE.get())
                .define('X', Blocks.GRAY_TERRACOTTA)
                .unlockedBy("root", has(WUTItems.HAMMER.get()))
                .save(recipeConsumer);
    }
}
