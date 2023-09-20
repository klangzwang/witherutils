package geni.witherutils.base.common.block.furnace.alloy;

import geni.witherutils.WitherUtils;
import geni.witherutils.core.client.gui.screen.WUTScreen;
import geni.witherutils.core.client.gui.widgets.EnergyWidget;
import geni.witherutils.core.client.gui.widgets.ProgressWidget;
import geni.witherutils.core.common.math.Vector2i;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AlloyFurnaceScreen extends WUTScreen<AlloyFurnaceContainer> {

    public AlloyFurnaceScreen(AlloyFurnaceContainer pMenu, Inventory pPlayerInventory, Component pTitle)
    {
        super(pMenu, pPlayerInventory, pTitle);
    }

	@Override
    protected void init()
    {
        super.init();
        addRenderableOnly(new EnergyWidget(this, this.getMenu(), this.font, getMenu().getBlockEntity()::getEnergyStorage, leftPos + 8, topPos + 23, 16, 40));
        addRenderableOnly(new ProgressWidget(this, this.getMenu(), this.font, getMenu().getBlockEntity()::getTimer, getMenu().getBlockEntity()::getBurnTimeMax, leftPos, topPos, leftPos + 28, topPos + 23, 6, 56));
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(gg);
        super.render(gg, mouseX, mouseY, partialTicks);
        this.renderTooltip(gg, mouseX, mouseY);
        
        renderFadeProgress(gg, partialTicks, menu.getSlot(4).x + 8, 55, mouseX, mouseY, getMenu().getBlockEntity().getTimer() > 0, WitherUtils.loc("textures/gui/smeltprogress.png"));
    }
    
    @Override
    protected String getBarName()
    {
        return "Alloy Furnace";
    }
    
    @Override
    public ResourceLocation getBackgroundImage()
    {
        return WitherUtils.loc("textures/gui/alloy_furnace.png");
    }

    @Override
    protected Vector2i getBackgroundImageSize()
    {
        return new Vector2i(176, 131);
    }
}
