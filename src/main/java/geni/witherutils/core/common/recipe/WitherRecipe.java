package geni.witherutils.core.common.recipe;

import java.util.List;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;

public interface WitherRecipe<T extends RecipeInput> extends Recipe<T> {

	double getBaseSaturationCost();
	
    default double getSaturationCost(T container)
    {
        return getBaseSaturationCost();
    }
	
    int getBaseEnergyCost();

    default int getEnergyCost(T container)
    {
        return getBaseEnergyCost();
    }

    List<OutputStack> craft(T container, RegistryAccess registryAccess);
    List<OutputStack> getResultStacks(RegistryAccess registryAccess);

    @Deprecated
    @Override
    default ItemStack assemble(T container, HolderLookup.Provider lookupProvider)
    {
        return ItemStack.EMPTY;
    }

    @Override
    default boolean canCraftInDimensions(int pWidth, int pHeight)
    {
        return true;
    }

    @Deprecated
    @Override
    default ItemStack getResultItem(HolderLookup.Provider lookupProvider)
    {
        return ItemStack.EMPTY;
    }

    @Override
    default boolean isSpecial()
    {
        return true;
    }
}
