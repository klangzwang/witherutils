package geni.witherutils.base.common.block.smarttv.pagescreens;

import java.util.ArrayList;

import com.mojang.blaze3d.systems.RenderSystem;

import geni.witherutils.base.common.block.smarttv.SmartTVScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

public class PageScreenDailyImage extends Screen {

	private final SmartTVScreen screen;

	private ArrayList<String> urlList;
	
    protected int imageWidth = 208;
    protected int imageHeight = 116;
    protected int leftPos;
    protected int topPos;
    
    public PageScreenDailyImage(SmartTVScreen screen)
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

		urlList = new ArrayList<String>();
		for(int j = 0; j < 6; j++)
		{
			addUrl(new String("https://picsum.photos/360/180?random=" + RandomSource.create().nextInt(100)));
		}
		int buttonIndex = 0;
		for(int j = 0; j < urlList.size(); j++)
		{
			String urlInfo = urlList.get(buttonIndex);
			this.addRenderableWidget(new RandomUrlButton(this.font, screen, urlInfo, buttonIndex, leftPos + 2, topPos + 10 + (j * 12) - ((int)Math.floor(j / 5) * 60)));
			buttonIndex++;
		}

	    this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (p_169249_) -> {
	    	this.minecraft.setScreen(this.screen);
	    }).bounds(this.width / 2 - 100, this.height / 6 + 168, 200, 20).build());
	}

	private void addUrl(String url)
	{
		this.urlList.add(url);
	}
	
	@SuppressWarnings("unused")
	@Override
	public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(gg, mouseX, mouseY, partialTicks);
		super.render(gg, mouseX, mouseY, partialTicks);

		int relX = (this.width - this.imageWidth) / 2;
		int relY = (this.height - this.imageHeight) / 2;
		
		for(int j = 0; j < urlList.size(); j++)
		{
			gg.drawString(this.font, Component.translatable("Random Wallpaper " + j),
					leftPos + 30, topPos + 12 + (j * 12) - ((int)Math.floor(j / 5) * 60), 16777215);
		}
	}

	@Override
    public void onClose()
    {
		this.urlList.clear();
        this.minecraft.setScreen(this.screen instanceof SmartTVScreen ? null : this.screen);
    }
	
	protected void drawBackground(GuiGraphics ms, ResourceLocation gui)
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, gui);
		int relX = (this.width - this.imageWidth) / 2;
		int relY = (this.height - this.imageHeight) / 2;
		ms.blit(gui, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
	}
	
	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
}
