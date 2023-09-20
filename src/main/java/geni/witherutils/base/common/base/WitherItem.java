package geni.witherutils.base.common.base;

import java.util.List;

import geni.witherutils.base.client.ClientTooltipHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab.Output;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class WitherItem extends Item {

	public WitherItem(Properties properties)
	{
		super(properties.rarity(Rarity.create("NONEVIL", ChatFormatting.GRAY)));
	}
	
    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag)
    {
        ClientTooltipHandler.Tooltip.addInformation(stack, world, list, flag, true);
    }
    
	public void fillCreativeTab(Output out)
	{
		out.accept(this);
	}
}
