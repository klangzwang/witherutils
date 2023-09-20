package geni.witherutils.base.data.generator.recipe;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.google.gson.JsonObject;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTRecipes;
import geni.witherutils.core.data.WitherRecipeProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

public class WitherCutterRecipeProvider extends WitherRecipeProvider {

    public WitherCutterRecipeProvider(PackOutput packOutput)
    {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer)
    {
        build(WUTBlocks.CTM_CONCRETE_A.get().asItem(), Ingredient.of(Items.GRAY_CONCRETE), finishedRecipeConsumer);
        build(WUTBlocks.CTM_CONCRETE_B.get().asItem(), Ingredient.of(Items.GRAY_CONCRETE), finishedRecipeConsumer);
        build(WUTBlocks.CTM_CONCRETE_C.get().asItem(), Ingredient.of(Items.GRAY_CONCRETE), finishedRecipeConsumer);
        build(WUTBlocks.CTM_METAL_A.get().asItem(), Ingredient.of(Items.IRON_BLOCK), finishedRecipeConsumer);
        build(WUTBlocks.CTM_STONE_A.get().asItem(), Ingredient.of(Items.STONE), finishedRecipeConsumer);
        build(WUTBlocks.CTM_GLASS_A.get().asItem(), Ingredient.of(Blocks.BLACK_STAINED_GLASS), finishedRecipeConsumer);
        build(WUTBlocks.CTM_GLASS_B.get().asItem(), Ingredient.of(Blocks.YELLOW_STAINED_GLASS), finishedRecipeConsumer);
        build(WUTBlocks.CTM_GLASS_C.get().asItem(), Ingredient.of(Blocks.WHITE_STAINED_GLASS), finishedRecipeConsumer);
        build(WUTBlocks.BRICKSDARK.get().asItem(), Ingredient.of(Blocks.STONE_BRICKS), finishedRecipeConsumer);
    }

    protected void build(Item result, Ingredient ingredient, Consumer<FinishedRecipe> finishedRecipeConsumer)
    {
        finishedRecipeConsumer.accept(new FinishedCutterRecipe(WitherUtils.loc("cutter/" + ForgeRegistries.ITEMS.getKey(result).getPath()), result, ingredient));
    }

    protected static class FinishedCutterRecipe extends WitherFinishedRecipe
    {
        private final Item result;
        private final Ingredient ingredient;
        
        public FinishedCutterRecipe(ResourceLocation id, Item result, Ingredient ingredient)
        {
            super(id);
            this.result = result;
            this.ingredient = ingredient;
        }

        @Override
        public void serializeRecipeData(JsonObject json)
        {
        	json.add("ingredient", ingredient.toJson());
            json.addProperty("result", ForgeRegistries.ITEMS.getKey(result).toString());
            
            super.serializeRecipeData(json);
        }

        @Override
        protected Set<String> getModDependencies()
        {
            Set<String> mods = new HashSet<>();
            mods.add(ForgeRegistries.ITEMS.getKey(result).getNamespace());
            return mods;
        }

        @Override
        public RecipeSerializer<?> getType()
        {
            return WUTRecipes.CUTTER_S.get();
        }
    }
}
