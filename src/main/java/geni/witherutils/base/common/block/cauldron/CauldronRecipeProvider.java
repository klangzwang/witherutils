package geni.witherutils.base.common.block.cauldron;

import java.util.concurrent.CompletableFuture;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.common.init.WUTFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.fluids.FluidStack;

public class CauldronRecipeProvider extends RecipeProvider {

    public CauldronRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(packOutput, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput finishedRecipeConsumer)
    {
//        build(Ingredient.of(Blocks.BONE_BLOCK), new FluidStack(WUTFluids.BLUELIMBO.get(), 1000), 3, 640, finishedRecipeConsumer);
//        build(Ingredient.of(Blocks.BONE_BLOCK), new FluidStack(WUTFluids.COLDSLUSH.get(), 1000), 3, 640, finishedRecipeConsumer);
        build(Ingredient.of(Blocks.BONE_BLOCK), new FluidStack(WUTFluids.FERTILIZER.get(), 1000), 3, 640, finishedRecipeConsumer);
//        build(Ingredient.of(Blocks.BONE_BLOCK), new FluidStack(WUTFluids.PORTIUM.get(), 1000), 3, 640, finishedRecipeConsumer);
//        build(Ingredient.of(Blocks.BONE_BLOCK), new FluidStack(WUTFluids.REDRESIN.get(), 1000), 3, 640, finishedRecipeConsumer);
//        build(Ingredient.of(Blocks.BONE_BLOCK), new FluidStack(WUTFluids.SOULFUL.get(), 1000), 3, 640, finishedRecipeConsumer);
//        build(Ingredient.of(Blocks.BONE_BLOCK), new FluidStack(WUTFluids.WITHERWATER.get(), 1000), 3, 640, finishedRecipeConsumer);
    }

    protected void build(Ingredient input, FluidStack output, int experience, int timer, RecipeOutput recipeOutput)
    {
        build(input, output, experience, timer, "", recipeOutput);
    }

    protected void build(Ingredient input, FluidStack output, int experience, int timer, String suffix, RecipeOutput recipeOutput)
    {
        recipeOutput.accept(
            WitherUtilsRegistry.loc("cauldron/" + BuiltInRegistries.FLUID.getKey(output.getFluid()).getPath() + suffix),
            new CauldronRecipe(input, output, experience, timer),
            null);
    }
}
