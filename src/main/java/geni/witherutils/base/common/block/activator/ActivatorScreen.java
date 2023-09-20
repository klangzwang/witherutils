package geni.witherutils.base.common.block.activator;

import java.text.NumberFormat;
import java.util.Locale;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.core.client.gui.screen.WUTScreen;
import geni.witherutils.core.client.gui.widgets.GuiSliderInteger;
import geni.witherutils.core.common.math.Vector2i;
import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ActivatorScreen extends WUTScreen<ActivatorContainer> {

	private GuiSliderInteger speedSlider;
	private GuiSliderInteger powerSlider;
	
    public ActivatorScreen(ActivatorContainer pMenu, Inventory pPlayerInventory, Component pTitle)
    {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init()
    {
        super.init();
        setHotbarVisible(false);
        
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
    }
	
    @Override
    protected String getBarName()
    {
        return "Activator";
    }
    
    @Override
    public ResourceLocation getBackgroundImage()
    {
        return WitherUtils.loc("textures/gui/activator.png");
    }

    @Override
    protected Vector2i getBackgroundImageSize()
    {
        return new Vector2i(176, 96);
    }
    
    @Override
    public void onClose()
    {
        SoundUtil.playSound(menu.getBlockEntity().getLevel(), menu.getBlockEntity().getBlockPos(), WUTSounds.BUCKET.get(), 0.4f);
        super.onClose();
    }
}
