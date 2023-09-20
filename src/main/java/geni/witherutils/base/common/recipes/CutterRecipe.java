package geni.witherutils.base.common.recipes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.init.WUTRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class CutterRecipe extends SingleItemRecipe {

	public static List<CutterRecipe> RECIPES = new ArrayList<>();
	private ItemStack result = ItemStack.EMPTY;
	private Ingredient ingredient;

	private CutterRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result)
	{
		super(WUTRecipes.CUTTER.get(), WUTRecipes.CUTTER_S.get(), id, group, ingredient, result);
		this.ingredient = ingredient;
		this.result = result;
	}

	@Override
	public boolean matches(Container inv, Level level)
	{
		return this.ingredient.test(inv.getItem(0));
	}

	@SuppressWarnings("unused")
	private static void initAllRecipes() {}
	private static Set<String> hashes = new HashSet<>();
	private static void addRecipe(CutterRecipe r)
	{
		ResourceLocation id = r.getId();
		if(hashes.contains(id.toString()))
		{
		    WitherUtils.LOGGER.error("Duplicate cutter recipe id " + id.toString());
		}
		RECIPES.add(r);
		hashes.add(id.toString());
	}
	
	public ItemStack[] ingredientAt(int slot)
	{
		Ingredient ing = at(slot);
		return ing.getItems();
	}
	
	public Ingredient at(int slot)
	{
		return ingredient;
	}
	@Override
	public NonNullList<Ingredient> getIngredients()
	{
		NonNullList<Ingredient> nonnulllist = NonNullList.create();
		nonnulllist.add(this.ingredient);
		return nonnulllist;
	}
	public ItemStack getIngredient()
	{
		for(Ingredient ing : this.getIngredients())
		{
			ItemStack[] ingstack = ing.getItems();
			for(ItemStack stack : ingstack)
			{
				return stack;
			}
		}
		return null;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess access)
	{
		return result.copy();
	}

	@Override
	public RecipeType<?> getType()
	{
		return WUTRecipes.CUTTER.get();
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return WUTRecipes.CUTTER_S.get();
	}

	public static class SerializeCutter implements RecipeSerializer<CutterRecipe>
	{
		@Override
		public CutterRecipe fromJson(ResourceLocation recipeId, JsonObject json)
		{
			CutterRecipe r = null;
			try
			{
				String group = GsonHelper.getAsString(json, "group", "");
				Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));
				String result = GsonHelper.getAsString(json, "result");
//				int i = GsonHelper.getAsInt(json, "count");
				ItemStack itemstack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(result)), 1);

				r = new CutterRecipe(recipeId, group, ingredient, itemstack);
				addRecipe(r);
			}
			catch (Exception e)
			{
				WitherUtils.LOGGER.error("Error loading recipe " + recipeId, e);
			}
			return r;
		}

		@Override
		public @Nullable CutterRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf)
		{
			String s = buf.readUtf(32767);
			Ingredient ingredient = Ingredient.fromNetwork(buf);
			ItemStack itemstack = buf.readItem();
			return new CutterRecipe(recipeId, s, ingredient, itemstack);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, CutterRecipe recipe)
		{
			buf.writeUtf(recipe.group);
			recipe.ingredient.toNetwork(buf);
			buf.writeItem(recipe.result);
		}
	}
}
