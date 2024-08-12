package geni.witherutils.base.common.item.card;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.core.client.gui.screen.WUTScreen;
import geni.witherutils.core.common.math.Vector2i;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CardScreen extends WUTScreen<CardContainer> {

	public CardScreen(CardContainer screenContainer, Inventory inv, Component titleIn)
	{
		super(screenContainer, inv, titleIn);
	}

    @Override
    protected void init()
    {
        super.init();
        setHotbarOffset(140);
    }
    
	@Override
	public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks)
	{
        this.renderBackground(gg, mouseX, mouseY, partialTicks);
        super.render(gg, mouseX, mouseY, partialTicks);
        this.renderTooltip(gg, mouseX, mouseY);
	}

    @Override
    protected String getBarName()
    {
        return "Card";
    }
    
    @Override
    public ResourceLocation getBackgroundImage()
    {
        return WitherUtilsRegistry.loc("textures/gui/card.png");
    }

    @Override
    protected Vector2i getBackgroundImageSize()
    {
    	return new Vector2i(176, 173);
    }
}
