package geni.witherutils.base.common.block.rack.controller.item;

import java.util.List;

import com.ibm.icu.text.DecimalFormat;

import geni.witherutils.base.client.render.item.ItemBarRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ControllerItemBlockItem extends BlockItem {

	public ControllerItemBlockItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder.stacksTo(1));
	}
	
	@Override
	public boolean isBarVisible(ItemStack stack)
	{
		ItemStack istack = getItemStack(stack);
		return istack != null && getItemStackStored(stack) > 0;
	}
	
	@Override
	public int getBarWidth(ItemStack stack)
	{
		try
		{
			float current = getItemStackStored(stack);
			float max = ControllerItemBlockEntity.ITEMCAPACITY;
			return Math.round(13.0F * current / max);
		}
		catch (Throwable e)
		{
		}
		return 1;
	}
	
	public static ItemStack getItemStack(ItemStack stack)
	{
		if (stack.getTag() != null)
		{
			ControllerItemBlockItemHandlerCapability handler = new ControllerItemBlockItemHandlerCapability(stack, ControllerItemBlockEntity.ITEMCAPACITY);
			return handler.getStack();
		}
		return null;
	}
	
	public static int getItemStackStored(ItemStack stack)
	{
		if (stack.getTag() != null)
		{
			ControllerItemBlockItemHandlerCapability handler = new ControllerItemBlockItemHandlerCapability(stack, ControllerItemBlockEntity.ITEMCAPACITY);
			return handler.getStored();
		}
		return 0;
	}
	
	@Override
	public int getBarColor(ItemStack stack)
	{
		return ItemBarRenderer.ITEM_BAR_RGB;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
	{
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        
        stack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
        	
            if (iItemHandler instanceof ControllerItemBlockItemHandlerCapability)
            {
                ItemStack contain = ((ControllerItemBlockItemHandlerCapability) iItemHandler).getStack();
                if (!contain.isEmpty())
                {
                    tooltip.add(Component.literal(ChatFormatting.GOLD + getString("Item") + ": " + ChatFormatting.WHITE + contain.getHoverName().getString()));
                    tooltip.add(Component.literal(ChatFormatting.GOLD + getString("Contains") + ": " + ChatFormatting.WHITE + new DecimalFormat().format(((ControllerItemBlockItemHandlerCapability) iItemHandler).getStored()) + ChatFormatting.DARK_AQUA + " " + getString("items")));
                }
            }
        });
        tooltip.add(Component.literal(ChatFormatting.GOLD + getString("Can Hold") + ": " + ChatFormatting.WHITE + new DecimalFormat().format(ControllerItemBlockEntity.ITEMCAPACITY) + ChatFormatting.DARK_AQUA + " " + getString("items")));
	}
	
    public static String getString(String string, Object... args)
    {
        return Component.translatable(string, args).getString();
    }
    
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, net.minecraft.nbt.CompoundTag nbt)
	{
		return new ControllerItemBlockItemHandlerCapability(stack, ControllerItemBlockEntity.ITEMCAPACITY);
	}
}
