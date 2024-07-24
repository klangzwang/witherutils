package geni.witherutils.base.common.block.creative;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.core.client.gui.screen.WUTScreen;
import geni.witherutils.core.common.math.Vector2i;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CreativeEnergyScreen extends WUTScreen<CreativeEnergyContainer> {

    public CreativeEnergyScreen(CreativeEnergyContainer pMenu, Inventory pPlayerInventory, Component pTitle)
    {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init()
    {
        super.init();
        setHotbarVisible(false);
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(gg, mouseX, mouseY, partialTicks);
        super.render(gg, mouseX, mouseY, partialTicks);
        this.renderTooltip(gg, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics gui, float pPartialTick, int pMouseX, int pMouseY)
    {
    	super.renderBg(gui, pPartialTick, pMouseX, pMouseY);
    }

    @Override
    protected String getBarName()
    {
        return "Creative Generator";
    }
    
    @Override
    public ResourceLocation getBackgroundImage()
    {
        return WitherUtilsRegistry.loc("textures/gui/setup.png");
    }

    @Override
    protected Vector2i getBackgroundImageSize()
    {
        return new Vector2i(176, 131);
    }
}
