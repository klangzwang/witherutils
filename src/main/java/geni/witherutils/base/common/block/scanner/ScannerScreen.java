package geni.witherutils.base.common.block.scanner;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.base.RedstoneControl;
import geni.witherutils.base.common.block.scanner.ScannerBlockEntity.SensorType;
import geni.witherutils.core.client.gui.screen.WUTScreen;
import geni.witherutils.core.common.math.Vector2i;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import geni.witherutils.core.common.helper.NamedEnum;

public class ScannerScreen extends WUTScreen<ScannerContainer> {

    public ScannerScreen(ScannerContainer pMenu, Inventory pPlayerInventory, Component pTitle)
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
        
        updateFields();
        
    	gg.blit(getRedstoneTexture(), this.leftPos + 160, this.topPos + 23, 0, 0, 12, 12, 12, 12);

        drawTextWithScale(gg, Component.translatable(ChatFormatting.BLACK + "SENSORTYPE: ")
        		.append(getMenu().getBlockEntity().getSensorType().getName()), leftPos + 78, topPos + 45, 0xFF000000, 0.645f, false);
        
        drawTextWithScale(gg, Component.translatable(ChatFormatting.BLACK + "AREATYPE: ")
        		.append(getMenu().getBlockEntity().getAreaType().getName()), leftPos + 78, topPos + 52, 0xFF000000, 0.645f, false);
        
        drawTextWithScale(gg, Component.translatable(ChatFormatting.BLACK + "GROUPTYPE: ")
        		.append(getMenu().getBlockEntity().getGroupType().getName()), leftPos + 78, topPos + 59, 0xFF000000, 0.645f, false);

        String typeName = menu.getBlockEntity().getSensorType().getName();
        drawTextWithScale(gg, Component.translatable(ChatFormatting.BLACK + typeName), leftPos + 29, topPos + 29, 0xFF000000, 0.85f, false);
    }
    
    private void updateFields()
    {
        SensorType sensorType = NamedEnum.getEnumByName("", SensorType.values());
        if (sensorType != null)
        {
        }
    }
    
	public ResourceLocation getRedstoneTexture()
	{
		if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ALWAYS_ACTIVE)
			return WitherUtils.loc("textures/gui/redstone_active.png");
		else
			return WitherUtils.loc("textures/gui/redstone_signal.png");
	}
	public ResourceLocation getSensorTypeTexture()
	{
		if(menu.getBlockEntity().getSensorType() == SensorType.SENSOR_BLOCK)
			return WitherUtils.loc("textures/gui/render_on.png");
		else if(menu.getBlockEntity().getSensorType() == SensorType.SENSOR_ENTITIES)
			return WitherUtils.loc("textures/gui/render_off.png");
		else if(menu.getBlockEntity().getSensorType() == SensorType.SENSOR_FLUID)
			return WitherUtils.loc("textures/gui/render_on.png");
		else if(menu.getBlockEntity().getSensorType() == SensorType.SENSOR_GROWTHLEVEL)
			return WitherUtils.loc("textures/gui/render_off.png");
		else if(menu.getBlockEntity().getSensorType() == SensorType.SENSOR_HOSTILE)
			return WitherUtils.loc("textures/gui/render_on.png");
		else if(menu.getBlockEntity().getSensorType() == SensorType.SENSOR_ITEMS)
			return WitherUtils.loc("textures/gui/render_off.png");
		else if(menu.getBlockEntity().getSensorType() == SensorType.SENSOR_PASSIVE)
			return WitherUtils.loc("textures/gui/render_on.png");
		else
			return WitherUtils.loc("textures/gui/render_off.png");
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		if(isHovering(160, 23, 12, 12, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ALWAYS_ACTIVE)
				menu.getBlockEntity().setRedstoneControl(RedstoneControl.ACTIVE_WITH_SIGNAL);
			else
				menu.getBlockEntity().setRedstoneControl(RedstoneControl.ALWAYS_ACTIVE);

			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else if(isHovering(9, 25, 64, 15, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getSensorType() == SensorType.SENSOR_BLOCK)
				menu.getBlockEntity().setSensorType(SensorType.SENSOR_ENTITIES);
			else if(menu.getBlockEntity().getSensorType() == SensorType.SENSOR_ENTITIES)
				menu.getBlockEntity().setSensorType(SensorType.SENSOR_FLUID);
			else if(menu.getBlockEntity().getSensorType() == SensorType.SENSOR_FLUID)
				menu.getBlockEntity().setSensorType(SensorType.SENSOR_GROWTHLEVEL);
			else if(menu.getBlockEntity().getSensorType() == SensorType.SENSOR_GROWTHLEVEL)
				menu.getBlockEntity().setSensorType(SensorType.SENSOR_HOSTILE);
			else if(menu.getBlockEntity().getSensorType() == SensorType.SENSOR_HOSTILE)
				menu.getBlockEntity().setSensorType(SensorType.SENSOR_ITEMS);
			else if(menu.getBlockEntity().getSensorType() == SensorType.SENSOR_ITEMS)
				menu.getBlockEntity().setSensorType(SensorType.SENSOR_PASSIVE);
			else if(menu.getBlockEntity().getSensorType() == SensorType.SENSOR_PASSIVE)
				menu.getBlockEntity().setSensorType(SensorType.SENSOR_PLAYERS);
			else if(menu.getBlockEntity().getSensorType() == SensorType.SENSOR_PLAYERS)
				menu.getBlockEntity().setSensorType(SensorType.SENSOR_BLOCK);
			
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
        return "Scanner";
    }
    
    @Override
    public ResourceLocation getBackgroundImage()
    {
        return WitherUtils.loc("textures/gui/scanner.png");
    }

    @Override
    protected Vector2i getBackgroundImageSize()
    {
        return new Vector2i(176, 173);
    }
}
