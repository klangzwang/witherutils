package geni.witherutils.base.common.block.creative;

import geni.witherutils.core.common.item.ItemBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class CreativeEnergyBlockItem extends ItemBlock<Block> {

	public CreativeEnergyBlockItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}

	@Override
	public boolean isFoil(ItemStack pStack)
	{
		return true;
	}
}
