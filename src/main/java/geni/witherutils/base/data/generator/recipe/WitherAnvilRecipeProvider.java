package geni.witherutils.base.data.generator.recipe;

import java.util.Set;
import java.util.function.Consumer;

import com.google.gson.JsonObject;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.init.WUTRecipes;
import geni.witherutils.core.data.WitherRecipeProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

public class WitherAnvilRecipeProvider extends WitherRecipeProvider {

    public WitherAnvilRecipeProvider(PackOutput packOutput)
    {
        super(packOutput);
    }

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer)
	{
		build(Items.BONE_MEAL, Ingredient.of(Items.BONE), 0, 2, 2, finishedRecipeConsumer);
		build(WUTItems.IRON_PLATE.get(), Ingredient.of(Tags.Items.INGOTS_IRON), 3, 8, 0, finishedRecipeConsumer);
		build(WUTItems.IRON_ROD.get(), Ingredient.of(Items.IRON_NUGGET), 1, 3, 0, finishedRecipeConsumer);
		build(WUTItems.WITHERSTEEL_PLATE.get(), Ingredient.of(WUTItems.WITHERSTEEL_INGOT.get()), 5, 14, 0, finishedRecipeConsumer);
	}

    protected void build(ItemLike output, Ingredient input, int experience, int hitcounter, int bonuscount, Consumer<FinishedRecipe> recipeConsumer)
    {
        build(output, input, experience, hitcounter, bonuscount, "", recipeConsumer);
    }
    protected void build(ItemLike output, Ingredient input, int experience, int hitcounter, int bonuscount, String suffix, Consumer<FinishedRecipe> recipeConsumer)
    {
        recipeConsumer.accept(new FinishedAnvilRecipe(WitherUtils.loc("anvil/" + ForgeRegistries.ITEMS.getKey(output.asItem()).getPath() + suffix), input, output.asItem(), experience, hitcounter, bonuscount));
    }
    protected static class FinishedAnvilRecipe extends WitherFinishedRecipe
    {
        private final Ingredient input;
        private final Item output;
        private final int experience;
    	private final int hitcounter;
        private final int bonuscount;
        
        public FinishedAnvilRecipe(ResourceLocation id, Ingredient input, Item output, int experience, int hitcounter, int bonuscount)
        {
            super(id);
            this.input = input;
            this.output = output;
            this.bonuscount = bonuscount;
            this.experience = experience;
            this.hitcounter = hitcounter;
        }

        @Override
        public void serializeRecipeData(JsonObject json)
        {
            json.add("ingredient", input.toJson());
            json.addProperty("result", ForgeRegistries.ITEMS.getKey(output).toString());
            
            json.addProperty("experience", experience);
            json.addProperty("hitcounter", hitcounter);
            json.addProperty("bonuscount", bonuscount);

            super.serializeRecipeData(json);
        }

        @Override
        protected Set<String> getModDependencies()
        {
            return Set.of(ForgeRegistries.ITEMS.getKey(output).getNamespace());
        }

        @Override
        public RecipeSerializer<?> getType()
        {
            return WUTRecipes.ANVIL_S.get();
        }
    }
}
