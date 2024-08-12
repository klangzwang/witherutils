package geni.witherutils.base.common.item.soulorb;

import java.util.List;

import geni.witherutils.base.common.base.WitherItem;
import geni.witherutils.base.common.config.common.LootConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class SoulOrbItem extends WitherItem {
	
    public SoulOrbItem()
	{
        super(new Item.Properties());
	}

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand)
    {
    	return super.use(pLevel, pPlayer, pUsedHand);
    }
    
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext pContext, List<Component> list, TooltipFlag flag)
    {
    	super.appendHoverText(stack, pContext, list, flag);

    	list.add(Component.empty());
    	list.add(Component.translatable(ChatFormatting.GRAY + "■-§9 §nDimensions: "));
    	
    	if(LootConfig.SOULORBDROPLIST.get().size() == 0)
    	{
    		list.add(Component.translatable(ChatFormatting.GREEN + "minecraft:the_nether"));
    	}
    	else
    	{
    		final List<? extends String> dimensionlist = LootConfig.SOULORBDROPLIST.get();
    		for (String key : dimensionlist)
    		{
    			list.add(Component.translatable(ChatFormatting.GREEN + key));
    		}
    	}
    }
}
