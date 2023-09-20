package geni.witherutils.core.common.recipes;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface MachineRecipe<C extends Container> extends WitherRecipe<C> {

    int getEnergyCost(C container);

    List<OutputStack> craft(C container, RegistryAccess registryAccess);
    List<OutputStack> getResultStacks(RegistryAccess registryAccess);

    @Override
    default ItemStack assemble(C container, RegistryAccess registryAccess)
    {
        return ItemStack.EMPTY;
    }

    @Override
    default ItemStack getResultItem(RegistryAccess registryAccess)
    {
        return ItemStack.EMPTY;
    }
}