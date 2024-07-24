package geni.witherutils.core.common.item;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class ItemBlock<B extends Block> extends BlockItem {
	
    private final B block;

    public ItemBlock(B block, Properties builder)
    {
        super(block, builder);
        this.block = block;
    }

    @Override
    public Component getName(ItemStack stack)
    {
        if (this.block instanceof WitherAbstractBlock)
            return ((WitherAbstractBlock) this.block).getName();
        return super.getName(stack);
    }

    @Override
    public B getBlock()
    {
        return this.block;
    }
}
