package geni.witherutils.base.data.generator.recipe;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.init.WUTRecipes;
import geni.witherutils.base.common.io.energy.WitherEnergyIngredient;
import geni.witherutils.core.common.recipes.CountedIngredient;
import geni.witherutils.core.data.WitherRecipeProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

public class WitherAlloyRecipeProvider extends WitherRecipeProvider {

    public WitherAlloyRecipeProvider(PackOutput packOutput)
    {
        super(packOutput);
    }

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer)
	{
		build(new ItemStack(WUTItems.WITHERSTEEL_INGOT.get()),
				List.of(CountedIngredient.of(Tags.Items.INGOTS_IRON),
						CountedIngredient.of(Items.CHARCOAL),
						CountedIngredient.of(Blocks.OBSIDIAN)),
				new WitherEnergyIngredient(200, 100), 3, pFinishedRecipeConsumer);
		
		build(new ItemStack(WUTItems.SOULISHED_INGOT.get()),
				List.of(CountedIngredient.of(Tags.Items.INGOTS_IRON),
						CountedIngredient.of(WUTItems.SOULORB.get())),
				new WitherEnergyIngredient(100, 100), 3, pFinishedRecipeConsumer);
	}

    protected void build(ItemStack output, List<CountedIngredient> inputs, WitherEnergyIngredient energy, int experience, Consumer<FinishedRecipe> recipeConsumer)
    {
        build(WitherUtils.loc("alloyfurnace/" + ForgeRegistries.ITEMS.getKey(output.getItem()).getPath()), inputs, output, energy, experience, recipeConsumer);
    }
    protected void build(ItemStack output, String suffix, List<CountedIngredient> inputs, WitherEnergyIngredient energy, int experience, Consumer<FinishedRecipe> recipeConsumer)
    {
        build(WitherUtils.loc("alloyfurnace/" + ForgeRegistries.ITEMS.getKey(output.getItem()).getPath() + "_" + suffix), inputs, output, energy, experience, recipeConsumer);
    }
    protected void build(ResourceLocation id, List<CountedIngredient> inputs, ItemStack output, WitherEnergyIngredient energy, int experience, Consumer<FinishedRecipe> recipeConsumer)
    {
        recipeConsumer.accept(new FinishedAlloyingRecipe(id, inputs, output, energy, experience));
    }
    
    protected static class FinishedAlloyingRecipe extends WitherFinishedRecipe
    {
        private final List<CountedIngredient> inputs;
        private final ItemStack output;
        private final WitherEnergyIngredient energy;
        private final int experience;

        public FinishedAlloyingRecipe(ResourceLocation id, List<CountedIngredient> inputs, ItemStack output, WitherEnergyIngredient energy, int experience)
        {
            super(id);
            this.inputs = inputs;
            this.output = output;
            this.energy = energy;
            this.experience = experience;
        }

        @Override
        public void serializeRecipeData(JsonObject json)
        {
            JsonArray jsonInputs = new JsonArray(inputs.size());
            inputs.forEach(ing -> jsonInputs.add(ing.toJson()));

            json.add("inputs", jsonInputs);

            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("item", ForgeRegistries.ITEMS.getKey(output.getItem()).toString());
            
            if (output.getCount() > 1)
            {
                jsonobject.addProperty("count", output.getCount());
            }

            json.add("result", jsonobject);

            JsonObject jsonEnergy = new JsonObject();
            json.add("energy", jsonEnergy);
            jsonEnergy.addProperty("rfpertick", energy.getRfPertick());
            jsonEnergy.addProperty("ticks", energy.getTicks());

            json.addProperty("experience", experience);

            super.serializeRecipeData(json);
        }

        @Override
        protected Set<String> getModDependencies()
        {
            Set<String> mods = new HashSet<>();
            mods.add(ForgeRegistries.ITEMS.getKey(output.getItem()).getNamespace());
            return mods;
        }

        @Override
        public RecipeSerializer<?> getType()
        {
            return WUTRecipes.ALLOY_S.get();
        }
    }
}
