package geni.witherutils.base.client.render.item;

import geni.witherutils.WitherUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemDecorator;

public class BetaCheckItemDecorator implements IItemDecorator {

    public static final BetaCheckItemDecorator INSTANCE = new BetaCheckItemDecorator();
    
	@Override
	public boolean render(GuiGraphics gg, Font font, ItemStack stack, int xOffset, int yOffset)
	{
		gg.blit(WitherUtils.loc("textures/gui/betachecked.png"), xOffset, yOffset, 0, 0, 6, 6, 6, 6);
		return false;
	}
}
