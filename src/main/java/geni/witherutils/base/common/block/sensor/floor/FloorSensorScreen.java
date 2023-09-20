package geni.witherutils.base.common.block.sensor.floor;

import geni.witherutils.WitherUtils;
import geni.witherutils.core.client.gui.screen.WUTScreen;
import geni.witherutils.core.common.math.Vector2i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FloorSensorScreen extends WUTScreen<FloorSensorContainer> {

    public FloorSensorScreen(FloorSensorContainer pMenu, Inventory pPlayerInventory, Component pTitle)
    {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init()
    {
        super.init();
        setHotbarOffset(54);
    }
    
    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(gg);
        super.render(gg, mouseX, mouseY, partialTicks);
        this.renderTooltip(gg, mouseX, mouseY);
        
        drawTextWithScale(gg, Component.literal("CAMOUFLAGE"), leftPos + imageWidth - 58, topPos + 32, 0xFFBBBBBB, 0.6f, false);
        
    	String range = "";
		if(menu.getBlockEntity().getScanWidth() == 1)
			range = "RANGE x1";
		else if(menu.getBlockEntity().getScanWidth() == 2)
			range = "RANGE x2";
		else
			range = "RANGE x3";
    	drawTextWithScale(gg, Component.literal(range), leftPos + 23, topPos + 32, 0xFFBBBBBB, 0.6f, false);
    	
    	String trigger = "";
		if(menu.getBlockEntity().getTrigger() == 1)
			trigger = "PLAYERS";
		else if(menu.getBlockEntity().getTrigger() == 2)
			trigger = "HOSTILE";
		else
			trigger = "PASSIVE";
    	drawTextWithScale(gg, Component.literal(trigger), leftPos + 73, topPos + 32, 0xFFBBBBBB, 0.6f, false);
    }
    
	public ResourceLocation getScanWidthTexture()
	{
		if(menu.getBlockEntity().getScanWidth() == 1)
			return WitherUtils.loc("textures/gui/redstone_active.png");
		else if(menu.getBlockEntity().getScanWidth() == 2)
			return WitherUtils.loc("textures/gui/redstone_signal.png");
		else
			return WitherUtils.loc("textures/gui/speed_on.png");
	}
	public ResourceLocation getTriggerTexture()
	{
		if(menu.getBlockEntity().getTrigger() == 1)
			return WitherUtils.loc("textures/gui/redstone_active.png");
		else if(menu.getBlockEntity().getTrigger() == 2)
			return WitherUtils.loc("textures/gui/redstone_signal.png");
		else
			return WitherUtils.loc("textures/gui/speed_on.png");
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		if(isHovering(12, 29, 8, 15, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getScanWidth() == 1)
				menu.getBlockEntity().setScanWidth(2);
			else if(menu.getBlockEntity().getScanWidth() == 2)
				menu.getBlockEntity().setScanWidth(3);
			else if(menu.getBlockEntity().getScanWidth() == 3)
				menu.getBlockEntity().setScanWidth(1);
			
			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else if(isHovering(60, 29, 8, 15, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getTrigger() == 1)
				menu.getBlockEntity().setTrigger(2);
			else if(menu.getBlockEntity().getTrigger() == 2)
				menu.getBlockEntity().setTrigger(3);
			else if(menu.getBlockEntity().getTrigger() == 3)
				menu.getBlockEntity().setTrigger(1);

			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else
		{
			return super.mouseClicked(mouseX, mouseY, mouseButton);
		}
		return true;
	}
	
    @Override
    protected String getBarName()
    {
        return "Floor Sensor";
    }
    
    @Override
    public ResourceLocation getBackgroundImage()
    {
        return WitherUtils.loc("textures/gui/floorsensor.png");
    }

    @Override
    protected Vector2i getBackgroundImageSize()
    {
        return new Vector2i(176, 56);
    }
}
