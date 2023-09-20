package geni.witherutils.core.client.gui.widgets;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import geni.witherutils.WitherUtils;
import geni.witherutils.core.client.gui.screen.WUTScreen;
import geni.witherutils.core.common.math.Vector2i;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.widget.ExtendedButton;

@SuppressWarnings({ "unused", "rawtypes" })
@OnlyIn(Dist.CLIENT)
public class ImgButton<U extends WUTScreen> extends ExtendedButton {

    private final U addedOn;
	private final ResourceLocation resourceLocation;
    private int leftPos;
    private int topPos;
    private int x;
    private int y;
    private int width;
    private int height;
    private final Font font;
    
    public ImgButton(U addedOn, Font font, ResourceLocation resourceLocation, int x, int y, int width, int height, Button.OnPress press)
    {
        super(x, y, width, height, Component.translatable(""), press);
        this.addedOn = addedOn;
        this.font = font;
        this.resourceLocation = resourceLocation;
        this.leftPos = addedOn.getGuiLeft();
        this.topPos = addedOn.getGuiTop();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    @Override
    public void renderWidget(GuiGraphics gg, int pMouseX, int pMouseY, float pPartialTick)
    {
//        Vector2i pos = new Vector2i(getX(), getY());
//        addedOn.renderSimpleArea(gg, pos, pos.add(new Vector2i(width, height)));
    	
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableDepthTest();
        gg.blit(WitherUtils.loc("textures/gui/io_config.png"), this.getX(), this.getY(), 0, 0, this.width, this.height, 16, 16);
        
        renderToolTip(gg, pMouseX, pMouseY);
    }

    public void renderToolTip(GuiGraphics gg, int mouseX, int mouseY)
    {
        if(isHovered)
        {
            List<Component> list = new ArrayList<>();
            list.add(Component.translatable(ChatFormatting.DARK_GRAY + "Setup"));
            gg.renderComponentTooltip(font, list, mouseX, mouseY); 
        }
    }

    @Override
    public void onClick(double pMouseX, double pMouseY)
    {
    }

    @Override
	public void updateWidgetNarration(NarrationElementOutput narration) {}
}