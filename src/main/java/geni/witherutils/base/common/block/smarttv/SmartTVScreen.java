package geni.witherutils.base.common.block.smarttv;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.common.block.smarttv.pagescreens.PageScreenDailyImage;
import geni.witherutils.base.common.block.smarttv.pagescreens.PageScreenDownload;
import geni.witherutils.base.common.block.smarttv.pagescreens.PageScreenToDo;
import geni.witherutils.core.client.gui.screen.WUTScreen;
import geni.witherutils.core.common.math.Vector2i;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;

public class SmartTVScreen extends WUTScreen<SmartTVContainer> {
	
	public boolean hasLogoPlayed;
	public float bgr;
	public float bgg;
	public float bgb;
	public float logoTick;

	private RandomSource random;
	
	public List<ResourceLocation> backgrounds = new ArrayList<>();
	
	@SuppressWarnings("resource")
	public SmartTVScreen(SmartTVContainer pMenu, Inventory pPlayerInventory, Component pTitle)
    {
        super(pMenu, pPlayerInventory, pTitle);
        this.random = menu.getBlockEntity().getLevel().random;
    }

    @Override
    protected void init()
    {
        super.init();
        this.setHotbarVisible(false);
		bgr = random.nextFloat();
		bgg = random.nextFloat();
		bgb = random.nextFloat();
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(gg, mouseX, mouseY, partialTicks);
        super.render(gg, mouseX, mouseY, partialTicks);
        this.renderTooltip(gg, mouseX, mouseY);
        
        renderTVBG(gg, partialTicks, mouseX, mouseY);
        
		if(hasLogoPlayed)
			gg.drawString(this.font, Component.translatable("Home Info Games").withStyle(ChatFormatting.DARK_AQUA), this.leftPos + 15, this.topPos + 26, 16777215);
    }
    
	protected void renderTVBG(GuiGraphics gg, float partialTicks, int mouseX, int mouseY)
	{
		PoseStack ms = gg.pose();
		
		int relX = (this.width - this.imageWidth) / 2;
		int relY = (this.height - this.imageHeight) / 2;
		
		if(hasLogoPlayed)
		{
			ms.pushPose();
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			gg.blit(WitherUtilsRegistry.loc("textures/gui/smarttv.png"), relX, relY, 0, 0, this.imageWidth, this.imageHeight);
			ms.popPose();
			
			ms.pushPose();
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(bgr, bgg, bgb, 1.0f);
			gg.blit(WitherUtilsRegistry.loc("textures/gui/smarttv_bg.png"), relX, relY, 0, 0, this.imageWidth, this.imageHeight);
			RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
			ms.popPose();
			
			ms.pushPose();
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			gg.blit(WitherUtilsRegistry.loc("textures/gui/smarttv_home.png"), relX, relY, 0, 0, this.imageWidth, this.imageHeight);
			ms.popPose();
		}
		else
		{
			ms.pushPose();
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			gg.blit(WitherUtilsRegistry.loc("textures/gui/smarttv.png"), relX, relY, 0, 0, this.imageWidth, this.imageHeight);
			ms.popPose();
			
			ms.pushPose();
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, logoTick);
			gg.blit(WitherUtilsRegistry.loc("textures/gui/smarttv_logo.png"), relX, relY, 0, 0, this.imageWidth, this.imageHeight);
			RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
			ms.popPose();
		}
	}

    @Override
    protected String getBarName()
    {
        return "SmartTV";
    }
    
    @Override
    public ResourceLocation getBackgroundImage()
    {
        return WitherUtilsRegistry.loc("textures/gui/smarttv.png");
    }

    @Override
    protected Vector2i getBackgroundImageSize()
    {
        return new Vector2i(208, 116);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
    {
    	if(isHovering(40, 81, 24, 24, mouseX, mouseY))
		{
    		this.minecraft.setScreen(new PageScreenDownload(this));
    		this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
    	else if(isHovering(65, 81, 24, 24, mouseX, mouseY))
		{
    		this.minecraft.setScreen(new PageScreenToDo(this));
    		this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
    	else if(isHovering(90, 81, 24, 24, mouseX, mouseY))
		{
    		this.minecraft.setScreen(new PageScreenDailyImage(this));
    		this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
    	else if(isHovering(90, 81, 24, 24, mouseX, mouseY))
		{
    		this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
    	else if(isHovering(115, 81, 24, 24, mouseX, mouseY))
		{
    		this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else
		{
			return super.mouseClicked(mouseX, mouseY, mouseButton);
		}
        return true;
    }

	@Override
	public void onClose()
	{
		hasLogoPlayed = false;
		super.onClose();
	}

	@Override
	protected void containerTick()
	{
		super.containerTick();

		if(logoTick < 1.0f)
			logoTick += 0.05f;
		else
		{
			hasLogoPlayed = true;
			logoTick = 1.0f;
		}
	}
}
