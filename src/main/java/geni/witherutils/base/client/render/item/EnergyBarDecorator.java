package geni.witherutils.base.client.render.item;

import geni.witherutils.base.common.base.WitherItemEnergy;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;

public class EnergyBarDecorator implements IItemDecorator {
	
    public static final EnergyBarDecorator INSTANCE = new EnergyBarDecorator();
    public static final int BAR_COLOR = 0x00B168E4;

    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset)
    {
        int maxEnergyStored = WitherItemEnergy.getMaxEnergyStored(stack);
        if (maxEnergyStored <= 0)
        {
            return false;
        }
        int energyStored = WitherItemEnergy.getEnergyStored(stack);
        float fillRatio = energyStored / (float)maxEnergyStored;
        ItemBarRenderer.renderBar(guiGraphics, fillRatio, xOffset, yOffset, 0, BAR_COLOR);
        return false;
    }
}


