package geni.witherutils.base.common.block.smarttv.pagescreens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.block.smarttv.SmartTVScreen;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
@SuppressWarnings("unused")
public class RandomUrlButton extends Button
{
	private final Font font;
	private final int buttonIndex;
	private final int x;
	private final int y;
	private int u = 0;
	private int v = 54;
	private static final int buttonWidth = 165;
	private static final int buttonHeight = 12;

	private String url;
	private final SmartTVScreen screen;
	
	public RandomUrlButton(Font font, SmartTVScreen screen, String url, int buttonIndex, int x, int y)
	{
		super(x, y, buttonWidth, buttonHeight, Component.literal(""), null, DEFAULT_NARRATION);
		this.font = font;
		this.screen = screen;
		this.url = url;
		this.buttonIndex = buttonIndex;
		this.x = x;
		this.y = y;
	}
	
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
    
	@Override
	public void onPress()
	{
		String current = getUrl();
		screen.getMenu().getBlockEntity().setUrl(current);
	}
	
	@Override
	protected void renderWidget(GuiGraphics gg, int mouseX, int mouseY, float partialTick)
	{
		PoseStack poseStack = gg.pose();
		this.isHovered = mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height || this.isFocused();

		this.v = 54;
		if(this.isHovered)
			this.v = 66;

		this.active = true;

		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		poseStack.pushPose();
		RenderSystem.enableBlend();
		gg.blit(new ResourceLocation(WitherUtils.MODID, "textures/gui/buttons.png"), x, y, u, v, width, height);
		RenderSystem.disableBlend();
		poseStack.popPose();
		
//		gg.drawString(font, url, x + 12, y + 2, 0x000000);
//		poseStack.pushPose();
//		poseStack.translate(x + 1, y + 1, 0);
//		poseStack.scale(1.25F, 1.25F, 1.0F);
//		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
//		gg.blit(poseStack, 0, 0, 8, 8, 8.0F, 8, 8, 8, 64, 64);
//		poseStack.popPose();
	}
}