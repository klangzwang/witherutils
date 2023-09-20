package geni.witherutils.core.common.recipes;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

public interface WitherRecipe<C extends Container> extends Recipe<C> {

    @Override
    default boolean canCraftInDimensions(int width, int height)
    {
        return true;
    }

    @Override
    default boolean isSpecial()
    {
        return true;
    }
}
