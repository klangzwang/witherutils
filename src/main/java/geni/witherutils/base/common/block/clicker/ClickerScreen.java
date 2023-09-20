package geni.witherutils.base.common.block.clicker;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.core.client.gui.screen.WUTScreen;
import geni.witherutils.core.client.gui.widgets.GuiSliderInteger;
import geni.witherutils.core.common.math.Vector2i;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ClickerScreen extends WUTScreen<ClickerContainer> {

	private GuiSliderInteger speedSlider;
	private GuiSliderInteger powerSlider;
	
    public ClickerScreen(ClickerContainer pMenu, Inventory pPlayerInventory, Component pTitle)
    {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init()
    {
        super.init();
        setHotbarOffset(96);
        
        speedSlider = new GuiSliderInteger(leftPos + 52, topPos + 30, 100, 20, 0, 25, mode -> menu.getBlockEntity().setSpeed(mode));
        speedSlider.setSliderValueActual(menu.getBlockEntity().getSpeed());
	    this.addRenderableWidget(speedSlider);
	    
	    powerSlider = new GuiSliderInteger(leftPos + 52, topPos + 51, 100, 20, 0, 15, mode -> menu.getBlockEntity().setPower(mode));
	    powerSlider.setSliderValueActual(menu.getBlockEntity().getPower());
	    this.addRenderableWidget(powerSlider);
    }
    
    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(gg);
        super.render(gg, mouseX, mouseY, partialTicks);
        this.renderTooltip(gg, mouseX, mouseY);
        
        NumberFormat fmt = NumberFormat.getInstance(Locale.ENGLISH);
        
        drawTextWithScale(gg, Component.translatable(ChatFormatting.WHITE + "SPEED: ")
        		.append(fmt.format(getMenu().getBlockEntity().getSpeed())),
        		leftPos + 13, topPos + 32, 0xFF9999FF, 0.545f, false);

        drawTextWithScale(gg, Component.translatable(ChatFormatting.WHITE + "POWER: ")
        		.append(fmt.format(getMenu().getBlockEntity().getPower())),
        		leftPos + 13, topPos + 40, 0xFF9999FF, 0.545f, false);

        drawTextWithScale(gg, Component.translatable(ChatFormatting.BLACK + "TIMER: ")
        		.append(fmt.format(getMenu().getBlockEntity().getTimer())),
        		leftPos + 13, topPos + 82, 0xFF000000, 0.545f, false);
        
    	gg.blit(getRightClickTexture(), this.leftPos + 52, this.topPos + 80, 0, 0, 12, 12, 12, 12);
    	gg.blit(getSneakTexture(), this.leftPos + 64, this.topPos + 80, 0, 0, 12, 12, 12, 12);

        renderSlotButtonToolTips(gg, mouseX, mouseY);
    }

    @Override
    protected String getBarName()
    {
        return "Clicker";
    }
    
    @Override
    public ResourceLocation getBackgroundImage()
    {
        return WitherUtils.loc("textures/gui/clicker.png");
    }

    @Override
    protected Vector2i getBackgroundImageSize()
    {
        return new Vector2i(176, 96);
    }
    
	public ResourceLocation getRightClickTexture()
	{
		if(menu.getBlockEntity().getRightClick())
			return WitherUtils.loc("textures/gui/render_on.png");
		else
			return WitherUtils.loc("textures/gui/render_off.png");
	}
	public ResourceLocation getSneakTexture()
	{
		if(menu.getBlockEntity().getSneak())
			return WitherUtils.loc("textures/gui/render_on.png");
		else
			return WitherUtils.loc("textures/gui/render_off.png");
	}
    
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		if(isHovering(52, 80, 12, 12, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getRightClick() == false)
				menu.getBlockEntity().setRightClick(true);
			else
				menu.getBlockEntity().setRightClick(false);

			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else if(isHovering(64, 80, 12, 12, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getSneak())
				menu.getBlockEntity().setSneak(false);
			else
				menu.getBlockEntity().setSneak(true);

			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else
		{
			return super.mouseClicked(mouseX, mouseY, mouseButton);
		}
		return true;
	}
	
    public void renderSlotButtonToolTips(GuiGraphics gg, int mouseX, int mouseY)
    {
        List<Component> list = new ArrayList<>();
    	
        if(isHovering(52, 80, 12, 12, mouseX, mouseY))
        {
            Component appendRightClick = Component.literal("");
            
            if(menu.getBlockEntity().getRightClick() == true)
            	appendRightClick = Component.literal(ChatFormatting.GREEN + "On");
            else
            	appendRightClick = Component.literal(ChatFormatting.RED + "Off");

            list.add(Component.translatable(ChatFormatting.GRAY + "Right Click: ").append(appendRightClick));
        }
        else if(isHovering(64, 80, 12, 12, mouseX, mouseY))
        {
            Component appendSneak = Component.literal("");
            
            if(menu.getBlockEntity().getSneak() == true)
            	appendSneak = Component.literal(ChatFormatting.GREEN + "On");
            else
            	appendSneak = Component.literal(ChatFormatting.RED + "Off");

            list.add(Component.translatable(ChatFormatting.GRAY + "Sneak/Crouch: ").append(appendSneak));
        }
        gg.renderComponentTooltip(font, list, mouseX, mouseY); 
    }
    
    @Override
    public void onClose()
    {
        SoundUtil.playSound(menu.getBlockEntity().getLevel(), menu.getBlockEntity().getBlockPos(), WUTSounds.BUCKET.get(), 0.4f);
        super.onClose();
    }
}
