package geni.witherutils.base.client.render.item;

import geni.witherutils.core.common.util.EnergyUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemDecorator;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class EnergyBarDecorator implements IItemDecorator {
	
    public static final EnergyBarDecorator INSTANCE = new EnergyBarDecorator();
    
    public static final int BAR_COLOR = 0x00FFFFFF;

    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset)
    {
        if(EnergyUtil.getMaxEnergyStored(stack) <= 0)
        {
            return false;
        }
        float fillRatio = stack
            .getCapability(ForgeCapabilities.ENERGY)
            .map(energyStorage -> 1.0f - (float) energyStorage.getEnergyStored() / (float) energyStorage.getMaxEnergyStored())
            .orElse(0f);
        ItemBarRenderer.renderBar(guiGraphics, fillRatio, xOffset, yOffset, 0, BAR_COLOR);
        return false;
    }
}
