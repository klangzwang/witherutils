package geni.witherutils.base.common.block.anvil;

import java.util.concurrent.CompletableFuture;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.common.init.WUTItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

public class AnvilRecipeProvider extends RecipeProvider {

    public AnvilRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(packOutput, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput finishedRecipeConsumer)
    {
        build(Ingredient.of(Items.BONE), Items.BONE_MEAL, 3, 0, 3, 2, finishedRecipeConsumer);
        build(Ingredient.of(Tags.Items.INGOTS_IRON), WUTItems.IRON_PLATE.get(), 6, 2, 1, 0, finishedRecipeConsumer);
        build(Ingredient.of(Items.IRON_NUGGET), WUTItems.IRON_ROD, 3, 0, 1, 0, finishedRecipeConsumer);
        build(Ingredient.of(WUTItems.IRON_ROD), WUTItems.PICKAXEHEAD, 9, 2, 1, 0, finishedRecipeConsumer);
        build(Ingredient.of(WUTItems.IRON_PLATE.get()), Items.IRON_NUGGET, 6, 2, 6, 3, finishedRecipeConsumer);
    }

    protected void build(Ingredient input, ItemLike output, int hitCounter, float experience, int count, int bonus, RecipeOutput recipeOutput)
    {
        build(input, output, hitCounter, experience, count, bonus, "", recipeOutput);
    }

    protected void build(Ingredient input, ItemLike output, int hitCounter, float experience, int count, int bonus, String suffix, RecipeOutput recipeOutput)
    {
        recipeOutput.accept(
            WitherUtilsRegistry.loc("anvil/" + BuiltInRegistries.ITEM.getKey(output.asItem()).getPath() + suffix),
            new AnvilRecipe(input, output.asItem().getDefaultInstance(), hitCounter, experience, count, bonus),
            null);
    }
}
