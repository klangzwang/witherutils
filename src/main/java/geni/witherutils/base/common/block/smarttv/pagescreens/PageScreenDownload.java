package geni.witherutils.base.common.block.smarttv.pagescreens;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.block.smarttv.SmartTVScreen;
import geni.witherutils.core.client.gui.widgets.TextEditBox;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class PageScreenDownload extends Screen {

	private final SmartTVScreen screen;
	private TextEditBox textEditBox;
	
    protected int imageWidth = 208;
    protected int imageHeight = 116;
    protected int leftPos;
    protected int topPos;
	
	public PageScreenDownload(SmartTVScreen screen)
	{
		super(Component.translatable(""));
		this.screen = screen;
	}

	@Override
	public void init()
	{
		super.init();

		this.leftPos = (this.width - this.imageWidth) / 2;
		this.topPos = (this.height - this.imageHeight) / 2;
		
        textEditBox = new TextEditBox(this.font, leftPos + 39, topPos + 28, 148, mode -> screen.getMenu().getBlockEntity().setUrl(mode));
        textEditBox.setValue(screen.getMenu().getBlockEntity().getUrl());
        textEditBox.setBordered(false);
	    this.addRenderableWidget(textEditBox);
		
	    configureTxtBox(textEditBox);
	    
	    this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (p_169249_) -> {
	    	this.minecraft.setScreen(this.screen);
	    }).bounds(this.width / 2 - 100, this.height / 6 + 168, 200, 20).build());
	}
	
	@Override
	public void render(GuiGraphics gg, int p_96250_, int p_96251_, float p_96252_)
	{
		this.renderBackground(gg);
		super.render(gg, p_96250_, p_96251_, p_96252_);		
		
		drawBackground(gg, new ResourceLocation(WitherUtils.MODID, "textures/gui/smarttv.png"));
		drawBackground(gg, new ResourceLocation(WitherUtils.MODID, "textures/gui/smarttv_down.png"));
		
		textEditBox.render(gg, p_96250_, p_96251_, p_96252_);

		gg.drawString(this.font, Component.translatable("Media Downloader"), this.leftPos + 8, this.topPos + 68, 16777215);
		gg.drawString(this.font, new String("Enter Url and press Enter"), this.leftPos + 8, this.topPos + 98, 16777215);
	}
	   
    @Override
    public void onClose()
    {
        this.minecraft.setScreen(this.screen instanceof SmartTVScreen ? null : this.screen);
    }
    
	private void configureTxtBox(EditBox txtEditBox)
    {
    	textEditBox.setMaxLength(32767);
    	textEditBox.setBordered(false);
    	textEditBox.setCanLoseFocus(true);
    	textEditBox.setTextColor(Color.LIGHT_GRAY.getRGB());
    	textEditBox.setValue(screen.getMenu().getBlockEntity().getUrl());
    }
    
	@Override
	public boolean keyPressed(int key, int b, int c)
	{
		if(key == 257)
		{
			String current = textEditBox.getValue();
			screen.getMenu().getBlockEntity().setUrl(current);
			this.minecraft.player.closeContainer();
			return true;
		}
		if(key == 256)
		{
			this.minecraft.player.closeContainer();
			return true;
		}
		
		if(textEditBox.isFocused())
			return textEditBox.keyPressed(key, b, c);
		
		return super.keyPressed(key, b, c);
	}
	
	@Override
	public void tick()
	{
		textEditBox.tick();
		super.tick();
	}
	
	protected void drawBackground(GuiGraphics gg, ResourceLocation gui)
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		int relX = (this.width - this.imageWidth) / 2;
		int relY = (this.height - this.imageHeight) / 2;
		gg.blit(gui, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
	}
	
	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
}
