package geni.witherutils.base.common.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.block.furnace.alloy.AlloyFurnaceBlockEntity;
import geni.witherutils.base.common.init.WUTRecipes;
import geni.witherutils.base.common.io.energy.WitherEnergyIngredient;
import geni.witherutils.core.common.recipes.CountedIngredient;
import geni.witherutils.core.common.recipes.MachineRecipe;
import geni.witherutils.core.common.recipes.OutputStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AlloyFurnaceRecipe implements MachineRecipe<AlloyFurnaceRecipe.Container> {

    private final ResourceLocation id;
    private final List<CountedIngredient> inputs;
    private final ItemStack output;
    private final WitherEnergyIngredient energy;
    private final int experience;

    public AlloyFurnaceRecipe(ResourceLocation id, List<CountedIngredient> inputs, ItemStack output, WitherEnergyIngredient energy, int experience)
    {
        this.id = id;
        this.inputs = inputs;
        this.output = output;
        this.energy = energy;
        this.experience = experience;
    }

    public List<CountedIngredient> getInputs()
    {
        return inputs;
    }

    public int getEnergyCost()
    {
        return this.energy.getEnergyTotal();
    }
    public int getExperience()
    {
        return this.experience;
    }

    @Override
    public int getEnergyCost(Container container)
    {
        return this.energy.getEnergyTotal();
    }
    public WitherEnergyIngredient getEnergyIngredient()
    {
        return energy;
    }

    @Override
    public boolean matches(Container container, Level level)
    {
        boolean[] matched = new boolean[3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (matched[j])
                    continue;

                if (j < inputs.size())
                {
                    if (inputs.get(j).test(AlloyFurnaceBlockEntity.INPUTS.get(i).getItemStack(container)))
                    {
                        matched[j] = true;
                    }
                }
                else if (container.getItem(i) == ItemStack.EMPTY)
                {
                    matched[j] = true;
                }
            }
        }
        for (int i = 0; i < 3; i++)
        {
            if (!matched[i])
                return false;
        }
        return true;
    }
    @Override
    public List<OutputStack> craft(Container container, RegistryAccess registryAccess)
    {
         return List.of(OutputStack.of(output.copy()));
    }
    @Override
    public List<OutputStack> getResultStacks(RegistryAccess registryAccess)
    {
        return List.of(OutputStack.of(output.copy()));
    }
    @Override
    public ResourceLocation getId()
    {
        return id;
    }
    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return WUTRecipes.ALLOY_S.get();
    }
    @Override
    public RecipeType<?> getType()
    {
        return WUTRecipes.ALLOY.get();
    }
    
    public static class Container extends RecipeWrapper
    {
        private int inputsTaken;

        public Container(IItemHandlerModifiable inv)
        {
            super(inv);
        }
        public int getInputsTaken()
        {
            return inputsTaken;
        }
        public void setInputsTaken(int inputsTaken)
        {
            this.inputsTaken = inputsTaken;
        }
    }

    public static class SerializeAlloyFurnace implements RecipeSerializer<AlloyFurnaceRecipe> {

        @Override
        public AlloyFurnaceRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe)
        {
            JsonArray jsonInputs = serializedRecipe.getAsJsonArray("inputs");
            List<CountedIngredient> inputs = new ArrayList<>(jsonInputs.size());
            for (int i = 0; i < jsonInputs.size(); i++)
            {
                inputs.add(i, CountedIngredient.fromJson(jsonInputs.get(i).getAsJsonObject()));
            }
            ItemStack result = CraftingHelper.getItemStack(serializedRecipe.getAsJsonObject("result"), false);
            int experience = serializedRecipe.get("experience").getAsInt();
            return new AlloyFurnaceRecipe(recipeId, inputs, result, new WitherEnergyIngredient(serializedRecipe), experience);
        }

        @Nullable
        @Override
        public AlloyFurnaceRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
        {
            try
            {
                List<CountedIngredient> ingredients = buffer.readList(CountedIngredient::fromNetwork);
                ItemStack result = buffer.readItem();
                int experience = buffer.readInt();
                return new AlloyFurnaceRecipe(recipeId, ingredients, result, new WitherEnergyIngredient(buffer.readInt(), buffer.readInt()), experience);
            }
            catch (Exception ex)
            {
                WitherUtils.LOGGER.error("Error reading alloy recipe from packet.", ex);
                throw ex;
            }
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, AlloyFurnaceRecipe recipe)
        {
            try
            {
                buffer.writeCollection(recipe.inputs, (buf, ing) -> ing.toNetwork(buf));
                buffer.writeItem(recipe.output);
                buffer.writeInt(recipe.energy.getRfPertick());
                buffer.writeInt(recipe.energy.getTicks());
                buffer.writeInt(recipe.experience);
            }
            catch (Exception ex)
            {
            	WitherUtils.LOGGER.error("Error writing alloy recipe to packet.", ex);
                throw ex;
            }
        }
    }
}
