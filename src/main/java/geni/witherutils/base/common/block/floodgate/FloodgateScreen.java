package geni.witherutils.base.common.block.floodgate;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import geni.witherutils.WitherUtils;
import geni.witherutils.core.client.gui.screen.WUTScreen;
import geni.witherutils.core.common.math.Vector2i;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FloodgateScreen extends WUTScreen<FloodgateContainer> {
    
    public FloodgateScreen(FloodgateContainer pMenu, Inventory pPlayerInventory, Component pTitle)
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
        super.render(gg, mouseX, mouseY, partialTicks);
        this.renderTooltip(gg, mouseX, mouseY);
        
    	gg.blit(getRenderTexture(), this.leftPos + 4, this.topPos + 25, 0, 0, 12, 12, 12, 12);
    	renderSlotButtonToolTips(gg, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics gui, float pPartialTick, int pMouseX, int pMouseY)
    {
    	renderEffectChannel(gui);
    	super.renderBg(gui, pPartialTick, pMouseX, pMouseY);
		drawEffect(gui, pPartialTick);
    }
    
	protected void drawEffect(GuiGraphics gg, float partialTicks)
	{
		String OFFSETX = String.valueOf(menu.getBlockEntity().getOffsetAxis(Axis.X));
		String OFFSETY = String.valueOf(menu.getBlockEntity().getOffsetAxis(Axis.Y));
		String OFFSETZ = String.valueOf(menu.getBlockEntity().getOffsetAxis(Axis.Z));
		
		drawTextWithScale(gg, Component.literal("Up/Down"), leftPos + 36, topPos + 33, 0xFFFFFF00, 0.8f, false);
		gg.drawString(this.font, I18n.get(OFFSETY), this.leftPos + 74, this.topPos + 34, 5285857);
		drawTextWithScale(gg, Component.literal("Left/Right"), leftPos + 130, topPos + 27, 0xFFFFFF00, 0.8f, false);
		gg.drawString(this.font, I18n.get(OFFSETX), this.leftPos + 109, this.topPos + 27, 5285857);
		drawTextWithScale(gg, Component.literal("Fwd/Bwd"), leftPos + 130, topPos + 40, 0xFFFFFF00, 0.8f, false);
		gg.drawString(this.font, I18n.get(OFFSETZ), this.leftPos + 109, this.topPos + 40, 5285857);
	}
	
    public void renderEffectChannel(GuiGraphics gg)
    {
        Minecraft mc = Minecraft.getInstance();
        
        float sw = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        float sh = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        int x = (int) sw - (int) sw + leftPos;
        int y = (int) sh - (int) sh + topPos;
        
        gg.pose().pushPose();
        gg.pose().translate(x, y + 27, 0);
        gg.pose().scale(3f, 3f, 3f);
        gg.pose().mulPose(com.mojang.math.Axis.ZN.rotationDegrees((-360.0F / 180) * (mc.player.clientLevel.getGameTime() % 180)));
        RenderSystem.setShaderColor(1, 1, 1, 1);
        gg.blit(WitherUtils.loc("textures/item/withersteel_gear.png"), -10, -10, 0, 0, 20, 20, 20, 20);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        gg.pose().popPose();
    }
    
	public ResourceLocation getRenderTexture()
	{
		if(menu.getBlockEntity().getPreview())
			return WitherUtils.loc("textures/gui/render_on.png");
		else
			return WitherUtils.loc("textures/gui/render_off.png");
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		if(isHovering(4, 25, 12, 12, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getPreview() == false)
				menu.getBlockEntity().setPreview(true);
			else
				menu.getBlockEntity().setPreview(false);
			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else if(isHovering(18, 38, 13, 12, mouseX, mouseY))
		{
			int f = menu.getBlockEntity().getScaleY();
	        if(menu.getBlockEntity().getScaleY() > 1)
	        	menu.getBlockEntity().setScaleY(f -1);
			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else if(isHovering(18, 25, 13, 12, mouseX, mouseY))
		{
			int f = menu.getBlockEntity().getScaleY();
	        if(menu.getBlockEntity().getScaleY() < 10)
	        	menu.getBlockEntity().setScaleY(f +1);
			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else if(isHovering(97, 25, 15, 12, mouseX, mouseY))
		{
			int f = menu.getBlockEntity().getScaleX();
	        if(menu.getBlockEntity().getScaleX() > 1)
	        	menu.getBlockEntity().setScaleX(f -1);
			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else if(isHovering(112, 25, 15, 12, mouseX, mouseY))
		{
			int f = menu.getBlockEntity().getScaleX();
	        if(menu.getBlockEntity().getScaleX() < 10)
	        	menu.getBlockEntity().setScaleX(f +1);
			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}

		else if(isHovering(97, 38, 15, 12, mouseX, mouseY))
		{
			int f = menu.getBlockEntity().getScaleZ();
	        if(menu.getBlockEntity().getScaleZ() > 1)
	        	menu.getBlockEntity().setScaleZ(f -1);
			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else if(isHovering(112, 38, 15, 12, mouseX, mouseY))
		{
			int f = menu.getBlockEntity().getScaleZ();
	        if(menu.getBlockEntity().getScaleZ() < 10)
	        	menu.getBlockEntity().setScaleZ(f +1);
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
        return "Totem";
    }
    
    @Override
    public ResourceLocation getBackgroundImage()
    {
        return WitherUtils.loc("textures/gui/floodgate.png");
    }

    @Override
    protected Vector2i getBackgroundImageSize()
    {
        return new Vector2i(176, 59);
    }
	
    public void renderSlotButtonToolTips(GuiGraphics gg, int mouseX, int mouseY)
    {
        List<Component> list = new ArrayList<>();
    	
		if(isHovering(4, 25, 12, 12, mouseX, mouseY))
		{
            Component appendPreview = Component.literal("");
            if(menu.getBlockEntity().getPreview())
            	appendPreview = Component.literal(ChatFormatting.GREEN + "Show");
            else
            	appendPreview = Component.literal(ChatFormatting.RED + "Hide");
            list.add(Component.translatable(ChatFormatting.GRAY + "Preview: ").append(appendPreview));
		}
        gg.renderComponentTooltip(font, list, mouseX, mouseY); 
    }
}