package geni.witherutils.base.common.block.anvil;

import java.util.concurrent.CompletableFuture;

import geni.witherutils.api.WitherUtilsRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

public class AnvilRecipeProvider extends RecipeProvider {

    public AnvilRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(packOutput, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput)
    {
        build(Blocks.ACACIA_PLANKS, Ingredient.of(ItemTags.ACACIA_LOGS), 6, 1, 6, recipeOutput);
        build(Blocks.OAK_PLANKS, Ingredient.of(ItemTags.OAK_LOGS), 6, 1, 6, recipeOutput);
    }

    protected void build(ItemLike output, Ingredient input, int hitCounter, float experience, float satcost, RecipeOutput recipeOutput)
    {
        build(output, input, hitCounter, experience, satcost, "", recipeOutput);
    }

    protected void build(ItemLike output, Ingredient input, int hitCounter, float experience, float satcost, String suffix, RecipeOutput recipeOutput)
    {
        recipeOutput.accept(
            WitherUtilsRegistry.loc("anvil/" + BuiltInRegistries.ITEM.getKey(output.asItem()).getPath() + suffix),
            new AnvilRecipe(input, output.asItem().getDefaultInstance(), hitCounter, experience, satcost),
            null);
    }
}
