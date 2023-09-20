package geni.witherutils.base.common.block.deco.sliced;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public record SlicedConcreteRecipe(Block base, Block crusher, ItemStack input, ItemStack output) {
}
