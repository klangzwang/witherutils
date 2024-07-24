package geni.witherutils.base.common.item.cutter;

import java.util.concurrent.CompletableFuture;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.common.init.WUTBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

public class CutterRecipeProvider extends RecipeProvider {

    public CutterRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(packOutput, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput)
    {
    	WUTBlocks.CUTTERBLOCKS.forEach(cutblock -> {
    		build(cutblock.asItem(), Ingredient.of(cutblock.getInputBlock().asItem()), cutblock.getDamageValue(), recipeOutput);
    	});
    	
        build(Blocks.ACACIA_PLANKS, Ingredient.of(ItemTags.ACACIA_LOGS), 6, recipeOutput);
    }

    protected void build(ItemLike output, Ingredient input, float damage, RecipeOutput recipeOutput)
    {
        build(output, input, damage, "", recipeOutput);
    }

    protected void build(ItemLike output, Ingredient input, float damage, String suffix, RecipeOutput recipeOutput)
    {
        recipeOutput.accept(
            WitherUtilsRegistry.loc("cutter/" + BuiltInRegistries.ITEM.getKey(output.asItem()).getPath() + suffix),
            new CutterRecipe(input, output.asItem().getDefaultInstance(), damage),
            null);
    }
}
