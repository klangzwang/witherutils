package geni.witherutils.base.common.item.worm;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public record WormRecipe(Block base, Block crusher, ItemStack input, ItemStack output) {
}
