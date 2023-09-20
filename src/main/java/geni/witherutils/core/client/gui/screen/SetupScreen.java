package geni.witherutils.core.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import geni.witherutils.WitherUtils;
import geni.witherutils.core.common.math.Vector2i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("rawtypes")
public class SetupScreen extends Screen {

	WUTScreen parent;
	private final int leftPos;
	private final int topPos;
	private final int imageWidth;
	private final int imageHeight;
	
	public SetupScreen(WUTScreen parent)
    {
		super(Component.translatable(""));
		this.parent = parent;
		this.leftPos = parent.getGuiLeft();
		this.topPos = parent.getGuiTop();
        this.imageWidth = getBackgroundImageSize().x();
        this.imageHeight = getBackgroundImageSize().y();
	}

	@Override
    protected void init()
    {
        super.init();
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks)
    {
        gg.blit(getBackgroundImage(), leftPos, topPos, 0, 0, imageWidth, imageHeight);
        super.render(gg, mouseX, mouseY, partialTicks);
        renderBar(gg, mouseX, mouseY, partialTicks, true);
    }
    
    public void renderBar(GuiGraphics gui, int mouseX, int mouseY, float partialTicks, boolean redstone)
    {
        Minecraft mc = Minecraft.getInstance();
        gui.pose().pushPose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WitherUtils.loc("textures/gui/bareffect.png"));
        float time = mc.level.getGameTime() + partialTicks;
        double offset = Math.sin(time * 1.0D / 4.0D) / 10.0D;
        if (redstone == true)
            offset = Math.sin(time * 1.0D / 12.0D) / 10.0D;
        gui.pose().translate(0.0D + offset * 100, 0.0D, 0.0D);
        gui.blit(WitherUtils.loc("textures/gui/bareffect.png"), this.leftPos + 120, this.topPos + 7, 0, 0, 32, 16, 32, 16);
        gui.pose().popPose();
        gui.drawString(this.font, new String(getBarName()), this.leftPos + 8, this.topPos + 6, 16777215);
    }

    @SuppressWarnings("resource")
    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers)
    {
        if (pKeyCode == 256)
        {
            Minecraft.getInstance().player.closeContainer();
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }
    
    @Override
    public void removed()
    {
        super.removed();
    }
    
    protected String getBarName()
    {
        return "Setup";
    }
    
    public ResourceLocation getBackgroundImage()
    {
        return WitherUtils.loc("textures/gui/setup.png");
    }

    protected Vector2i getBackgroundImageSize()
    {
        return new Vector2i(176, 131);
    }
}
