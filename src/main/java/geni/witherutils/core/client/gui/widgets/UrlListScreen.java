package geni.witherutils.core.client.gui.widgets;

import java.util.ArrayList;
import java.util.List;

import geni.witherutils.base.common.block.smarttv.SmartTVScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
@SuppressWarnings("unused")
public class UrlListScreen extends Screen
{
	private final SmartTVScreen screen;
	

	private String url;
	
	private static final int buttonWidth = 165;
	private static final int buttonHeight = 12;
	
	private int u = 0;
	private int v = 131;
	
	private int totalPages;
	private int currentPage;
	
	public UrlListScreen(SmartTVScreen screen)
	{
		super(Component.empty());
		this.screen = screen;
		currentPage = 1;
	}

	@Override
	protected void init()
	{
		super.init();
		
		int leftPos = (this.width - 176) / 2;
	    int topPos = (this.height - 166) / 2;
		
		ArrayList<String> urlList = new ArrayList<String>(getImageUrls());
		totalPages = (int) Math.ceil(urlList.size() / 5.0D);
		int buttonIndex = 0;
		
		for(int j = 0; j < urlList.size(); j++)
		{
			String url = urlList.get(buttonIndex);
//			this.addRenderableWidget(new Button());
			buttonIndex++;
		}

//		this.addRenderableWidget(new PreviousPageButton(x + 5, y + 113, this));
//		this.addRenderableWidget(new NextPageButton(x + 158, y + 113, this));

	    this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (p_169249_) -> {
	    	this.minecraft.setScreen(this.screen);
	    }).bounds(this.width / 2 - 100, this.height / 6 + 168, 200, 20).build());
	}
	
	public List<String> getImageUrls()
	{
		List<String> urlList = new ArrayList<String>();
		
		urlList.add("");
		
		return urlList;
	}
	
	@Override
	public void render(GuiGraphics gg, int p_281550_, int p_282878_, float p_282465_)
	{
		this.renderBackground(gg);
		super.render(gg, p_281550_, p_282878_, p_282465_);
		
//		this.isHovered = mouseX >= getX() && mouseX < getX() + width && mouseY >= getY() && mouseY < getY() + height || this.isFocused();
//
//		this.v = 131;
//		if(this.isHovered)
//			this.v = 143;
//
//		this.active = true;
//		if(buttonIndex < (screen.getCurrentPage() * 5) - 5 || buttonIndex >= screen.getCurrentPage() * 5)
//		{
//			this.active = false;
//			this.v = 244;
//		}
//
//		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
//		gg.pose().pushPose();
//		RenderSystem.enableBlend();
//		gg.blit(new ResourceLocation(WitherUtils.MODID, "textures/gui/menus/advanced_online_detector_menu.png"), getX(), getY(), u, v, width, height);
//		RenderSystem.disableBlend();
//		gg.pose().popPose();
//		if(!(buttonIndex < (screen.getCurrentPage() * 5) - 5 || buttonIndex >= screen.getCurrentPage() * 5))
//		{
//			gg.drawString(font, playerInfo.getProfile().getName(), getX() + 12, getY() + 2, 0x000000);
//			gg.pose().pushPose();
//			gg.pose().translate(getX() + 1, getY() + 1, 0);
//			gg.pose().scale(1.25F, 1.25F, 1.0F);
//			RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
//			RenderSystem.setShaderTexture(0, playerInfo.getSkinLocation());
//			gg.blit(new ResourceLocation(WitherUtils.MODID, "textures/gui/menus/advanced_online_detector_menu.png"), 0, 0, 8, 8, 8.0F, 8, 8, 8, 64, 64);
//			gg.pose().popPose();
//		}
	}
	
	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
}