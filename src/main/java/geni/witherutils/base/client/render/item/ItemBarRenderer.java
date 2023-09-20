package geni.witherutils.base.client.render.item;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.GuiGraphics;

public class ItemBarRenderer {
	
	public static final int ITEM_BAR_RGB = 0xADD8E6;
	public static final int ENERGY_BAR_RGB = 0x00B168E4;
	public static final int FLUID_BAR_RGB = 0x99BD42;
	
    public static void renderBar(GuiGraphics guiGraphics, float fillRatio, int xOffset, int yOffset, int blitOffset, int color)
    {
        RenderSystem.disableBlend();

        int i = Math.round(13.0F - fillRatio * 13.0F);
        int x = xOffset + 2;
        int y = yOffset + 12;

        guiGraphics.fill(x, y, x + i, y + 1, blitOffset + 190, color);

        RenderSystem.enableBlend();
    }
}
