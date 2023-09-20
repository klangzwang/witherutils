package geni.witherutils.base.data.generator.recipe;

import java.util.Set;
import java.util.function.Consumer;

import com.google.gson.JsonObject;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.init.WUTFluids;
import geni.witherutils.base.common.init.WUTRecipes;
import geni.witherutils.core.data.WitherRecipeProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public class WitherCauldronRecipeProvider extends WitherRecipeProvider {

    public WitherCauldronRecipeProvider(PackOutput packOutput)
    {
        super(packOutput);
    }

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer)
	{
		build(Ingredient.of(Blocks.BONE_BLOCK), 3, 640, new FluidStack(WUTFluids.FERTILIZER.get(), 1000), finishedRecipeConsumer);
	}

    protected void build(Ingredient input, int experience, int timer, FluidStack output, Consumer<FinishedRecipe> recipeConsumer)
    {
        build(input, experience, timer, output, "", recipeConsumer);
    }
    protected void build(Ingredient input, int experience, int timer, FluidStack output, String suffix, Consumer<FinishedRecipe> recipeConsumer)
    {
        recipeConsumer.accept(new FinishedCauldronRecipe(WitherUtils.loc("cauldron/" + ForgeRegistries.FLUIDS.getKey(output.getFluid()).getPath() + suffix), input, experience, timer, output));
    }
    protected static class FinishedCauldronRecipe extends WitherFinishedRecipe
    {
        private final Ingredient ingredient;
        private final int experience;
    	private final int timer;
        private final FluidStack output;

        public FinishedCauldronRecipe(ResourceLocation id, Ingredient ingredient, int experience, int timer, FluidStack output)
        {
            super(id);
            this.ingredient = ingredient;
            this.experience = experience;
            this.timer = timer;
            this.output = output;
        }

        @Override
        public void serializeRecipeData(JsonObject json)
        {
            json.add("ingredient", ingredient.toJson());

            json.addProperty("experience", experience);
            json.addProperty("timer", timer);

			JsonObject jsonobject = new JsonObject();
			json.add("result", jsonobject);
			
			jsonobject.addProperty("fluid", ForgeRegistries.FLUIDS.getKey(output.getFluid()).toString());
			jsonobject.addProperty("amount", output.getAmount());
			
            super.serializeRecipeData(json);
        }

        @Override
        protected Set<String> getModDependencies()
        {
            return Set.of(ForgeRegistries.FLUIDS.getKey(output.getFluid()).getNamespace());
        }

        @Override
        public RecipeSerializer<?> getType()
        {
            return WUTRecipes.CAULDRON_S.get();
        }
    }
}
