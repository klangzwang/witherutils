package geni.witherutils.base.common.recipes;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import geni.witherutils.base.common.block.anvil.AnvilBlockEntity;
import geni.witherutils.base.common.init.WUTRecipes;
import geni.witherutils.core.common.recipes.WitherRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;

public class AnvilRecipe implements WitherRecipe<RecipeWrapper> {

    private final ResourceLocation id;
    private final ItemStack output;
	private Ingredient ingredient;
	private final int hitcounter;
	private final int experience;
	private final int bonuscount;
	
	private AnvilRecipe(ResourceLocation id, Ingredient ingredient, int hitcounter, int experience, int bonuscount, ItemStack output)
	{
		this.id = id;
		this.ingredient = ingredient;
	    this.hitcounter = hitcounter;
	    this.experience = experience;
		this.bonuscount = bonuscount;
		this.output = output;
	}

    @Override
    public NonNullList<Ingredient> getIngredients()
    {
        return NonNullList.of(Ingredient.EMPTY, ingredient);
    }
	
	@Nullable
	public static AnvilRecipe findRecipe(Level level, ItemStack input)
	{
		List<AnvilRecipe> recipes = level.getRecipeManager().getAllRecipesFor(WUTRecipes.ANVIL.get());
		for (AnvilRecipe recipe : recipes)
		{
			for (ItemStack stack : recipe.ingredient.getItems())
			{
				if (stack.getItem().toString() == input.getItem().toString())
				{
					return recipe;
				}
			}
		}
		return null;
	}

	public ItemStack getIngredientStack()
	{
		for (ItemStack stack : this.ingredient.getItems())
		{
			return stack;
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean matches(RecipeWrapper wrapper, Level level)
	{
		return ingredient.test(AnvilBlockEntity.INPUT.getItemStack(wrapper));
	}

	@Override
	public ItemStack assemble(RecipeWrapper p_44001_, RegistryAccess p_267165_)
	{
		return this.output.copy();
	}
	
    public ItemStack getOutput()
    {
        return output;
    }
    
	@Override
	public ResourceLocation getId()
	{
		return id;
	}
	
    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess)
    {
        return this.output;
    }
	
	@Override
	public RecipeType<?> getType()
	{
		return WUTRecipes.ANVIL.get();
	}
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return WUTRecipes.ANVIL_S.get();
	}
	
    @Override
    public boolean isSpecial()
    {
        return true;
    }

    public ItemStack getResultItem()
    {
        return output;
    }
	public int getHitCounter()
	{
		return this.hitcounter;
	}
	public int getExperience()
	{
		return this.experience;
	}
	public int getBonusCount()
	{
		return this.bonuscount;
	}
	
	public static class SerializeAnvil implements RecipeSerializer<AnvilRecipe>
	{
		@Override
		public AnvilRecipe fromJson(ResourceLocation recipeId, JsonObject json)
		{
			Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));
			String result = GsonHelper.getAsString(json, "result");
			int hitcounter = GsonHelper.getAsInt(json, "hitcounter");
			int experience = GsonHelper.getAsInt(json, "experience");
			int bonuscount = GsonHelper.getAsInt(json, "bonuscount");
			ItemStack itemstack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(result)), 1);
			
			return new AnvilRecipe(recipeId, ingredient, hitcounter, experience, bonuscount, itemstack);
		}

		@Override
		public @Nullable AnvilRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf)
		{
			Ingredient ingredient = Ingredient.fromNetwork(buf);
			int hitcounter = buf.readInt();
			int experience = buf.readInt();
			int bonuscount = buf.readInt();
			ItemStack itemstack = buf.readItem();
			return new AnvilRecipe(recipeId, ingredient, hitcounter, experience, bonuscount, itemstack);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, AnvilRecipe recipe)
		{
			recipe.ingredient.toNetwork(buf);
			buf.writeInt(recipe.hitcounter);
			buf.writeInt(recipe.experience);
			buf.writeInt(recipe.bonuscount);
			buf.writeItem(recipe.output);
		}
	}
}
