package geni.witherutils.base.common.block.creative;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class CreativeGeneratorBlockItem extends BlockItem {

	public CreativeGeneratorBlockItem(Block block, Properties prop)
	{
		super(block, prop);
	}
	
	@Override
	public boolean isFoil(ItemStack stack)
	{
		return true;
	}
}
