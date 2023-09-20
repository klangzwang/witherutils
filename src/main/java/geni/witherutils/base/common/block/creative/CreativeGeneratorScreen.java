package geni.witherutils.base.common.block.creative;

import geni.witherutils.WitherUtils;
import geni.witherutils.core.client.gui.screen.WUTScreen;
import geni.witherutils.core.common.math.Vector2i;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CreativeGeneratorScreen extends WUTScreen<CreativeGeneratorContainer> {
    
    public CreativeGeneratorScreen(CreativeGeneratorContainer pMenu, Inventory pPlayerInventory, Component pTitle)
    {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init()
    {
        super.init();
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(gg);
        super.render(gg, mouseX, mouseY, partialTicks);
        this.renderTooltip(gg, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics gui, float pPartialTick, int pMouseX, int pMouseY)
    {
    	super.renderBg(gui, pPartialTick, pMouseX, pMouseY);
    	renderAllSlots(gui);
    }
    
    public void renderAllSlots(GuiGraphics gg)
    {
        for(int i = 0; i < 4; i++)
        {
            Vector2i pos = new Vector2i(menu.getSlot(i).x + leftPos - 1, menu.getSlot(i).y + topPos - 1);
            Vector2i pos2 = pos.add(new Vector2i(18, 18));
        	renderSimpleDarkArea(gg, pos, pos2);
        }
    }
    
    @Override
    protected String getBarName()
    {
        return "Creative Generator";
    }
    
    @Override
    public ResourceLocation getBackgroundImage()
    {
        return WitherUtils.loc("textures/gui/setup.png");
    }

    @Override
    protected Vector2i getBackgroundImageSize()
    {
        return new Vector2i(176, 131);
    }
}
