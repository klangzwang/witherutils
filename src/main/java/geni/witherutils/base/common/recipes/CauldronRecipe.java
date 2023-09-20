package geni.witherutils.base.common.recipes;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.block.cauldron.CauldronBlockEntity;
import geni.witherutils.base.common.init.WUTRecipes;
import geni.witherutils.core.common.recipes.WitherRecipe;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;

public class CauldronRecipe implements WitherRecipe<CauldronRecipe.Container> {

    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final int experience;
    private final int timer;
    private final FluidStack output;
    
	public CauldronRecipe(ResourceLocation id, Ingredient ingredient, int experience, int timer, FluidStack output)
	{
        this.id = id;
        this.ingredient = ingredient;
        this.output = output;
        this.experience = experience;
        this.timer = timer;
	}

    public Ingredient getInput()
    {
        return ingredient;
    }
    public FluidStack getOutput()
    {
        return output;
    }
    public FluidStack getFluid()
    {
        return output;
    }
    public int getExperience()
    {
        return experience;
    }
    public int getTimer()
    {
        return timer;
    }

	@Override
	public ItemStack getResultItem(RegistryAccess p_267052_)
	{
		return ItemStack.EMPTY;
	}
	public FluidStack getResultFluid(RegistryAccess p_267052_)
	{
		return output;
	}
	
    @Override
    public boolean matches(Container pContainer, Level pLevel)
    {
        return ingredient.test(CauldronBlockEntity.INPUT.getItemStack(pContainer));
    }
	@Override
	public ItemStack assemble(Container p_44001_, RegistryAccess p_267165_)
	{
		return null;
	}

	@Override
	public RecipeType<?> getType()
	{
		return WUTRecipes.CAULDRON.get();
	}
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return WUTRecipes.CAULDRON_S.get();
	}
	
    public static class Container extends RecipeWrapper
    {
        private final FluidTank fluidTank;

        public Container(IItemHandlerModifiable inv, FluidTank fluidTank)
        {
            super(inv);
            this.fluidTank = fluidTank;
        }

        public FluidTank getFluidTank()
        {
            return fluidTank;
        }
    }
    
	public static class SerializeCauldron implements RecipeSerializer<CauldronRecipe>
	{
        @Override
        public CauldronRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe)
        {
        	Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(serializedRecipe, "ingredient"));

			int experience = GsonHelper.getAsInt(serializedRecipe, "experience");
			int timer = GsonHelper.getAsInt(serializedRecipe, "timer");
			
            JsonObject fluidJson = serializedRecipe.get("result").getAsJsonObject();
            ResourceLocation fluidId = new ResourceLocation(fluidJson.get("fluid").getAsString());
            FluidStack fluid = new FluidStack(ForgeRegistries.FLUIDS.getValue(fluidId), fluidJson.get("amount").getAsInt());
            
            return new CauldronRecipe(recipeId, ingredient, experience, timer, fluid);
        }

        @Override
        public @Nullable CauldronRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
        {
            try
            {
                Ingredient input = Ingredient.fromNetwork(buffer);
    			int experience = buffer.readInt();
    			int timer = buffer.readInt();
                FluidStack fluid = FluidStack.readFromPacket(buffer);
                
                return new CauldronRecipe(recipeId, input, experience, timer, fluid);
            }
            catch (Exception ex)
            {
                WitherUtils.LOGGER.error("Error reading tank recipe from packet.", ex);
                throw ex;
            }
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CauldronRecipe recipe)
        {
            try
            {
    			recipe.ingredient.toNetwork(buffer);
    			buffer.writeInt(recipe.experience);
    			buffer.writeInt(recipe.timer);
    			recipe.output.writeToPacket(buffer);
            }
            catch (Exception ex)
            {
            	WitherUtils.LOGGER.error("Error writing tank recipe to packet.", ex);
                throw ex;
            }
        }
    }

	@Override
	public ResourceLocation getId()
	{
		return id;
	}
}
