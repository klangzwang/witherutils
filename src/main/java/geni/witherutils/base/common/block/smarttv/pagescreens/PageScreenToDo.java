package geni.witherutils.base.common.block.smarttv.pagescreens;

import com.mojang.blaze3d.systems.RenderSystem;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.common.block.smarttv.SmartTVScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class PageScreenToDo extends Screen {

	private final SmartTVScreen screen;

    protected int imageWidth = 208;
    protected int imageHeight = 116;
    protected int leftPos;
    protected int topPos;
    
    public PageScreenToDo(SmartTVScreen screen)
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

	    this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (p_169249_) -> {
	    	this.minecraft.setScreen(this.screen);
	    }).bounds(this.width / 2 - 100, this.height / 6 + 168, 200, 20).build());
	}

    @SuppressWarnings("unused")
	private void configureTxtBox(EditBox txtEditBox)
    {
        txtEditBox.setMaxLength(21);
        txtEditBox.setBordered(false);
        txtEditBox.setCanLoseFocus(true);
        txtEditBox.setFGColor(0);
        txtEditBox.setValue("");
    }

	@Override
	public void render(GuiGraphics ms, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(ms, mouseX, mouseY, partialTicks);
		super.render(ms, mouseX, mouseY, partialTicks);
		drawBackground(ms, WitherUtilsRegistry.loc("textures/gui/smarttv.png"));
		drawBackground(ms, WitherUtilsRegistry.loc("textures/gui/smarttv_todo.png"));
	}

	@Override
    public void onClose()
    {
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
