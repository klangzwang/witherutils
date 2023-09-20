package geni.witherutils.core.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import geni.witherutils.core.client.gui.IIcon;
import geni.witherutils.core.common.math.Vector2i;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;

public interface IWitherScreen {

    default Screen getScreen()
    {
        return (Screen) this;
    }

    static void renderIcon(GuiGraphics gui, Vector2i pos, IIcon icon)
    {
        if (!icon.shouldRender())
            return;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, icon.getTextureLocation());
        RenderSystem.enableBlend();
        gui.blit(icon.getTextureLocation(), pos.x(), pos.y(), icon.getRenderSize().x(), icon.getRenderSize().y(), icon.getTexturePosition().x(), icon.getTexturePosition().y(), icon.getIconSize().x(),  icon.getIconSize().y(), icon.getTextureSize().x(), icon.getTextureSize().y());
    }

    default void renderSimpleArea(GuiGraphics gui, Vector2i pos, Vector2i pos2)
    {
        gui.fill(RenderType.guiOverlay(), pos.x(), pos.y(), pos2.x(), pos2.y(), 0xFF8B8B8B);
        gui.fill(RenderType.guiOverlay(), pos.x(), pos.y(), pos2.x() - 1, pos2.y() - 1, 0xFF373737);
        gui.fill(RenderType.guiOverlay(), pos.x() + 1, pos.y() + 1, pos2.x(), pos2.y(), 0xFFFFFFFF);
        gui.fill(RenderType.guiOverlay(), pos.x() + 1, pos.y() + 1, pos2.x() - 1, pos2.y() - 1, 0xFF8B8B8B);
    }
    
    default void renderSimpleDarkArea(GuiGraphics gui, Vector2i pos, Vector2i pos2)
    {
//        gui.fill(RenderType.guiOverlay(), pos.x(), pos.y(), pos2.x(), pos2.y(), 0xFF000000);
        gui.fill(RenderType.guiOverlay(), pos.x(), pos.y(), pos2.x() - 1, pos2.y() - 1, 0xFF000000);
        gui.fill(RenderType.guiOverlay(), pos.x() + 1, pos.y() + 1, pos2.x(), pos2.y(), 0xFF909090);
        gui.fill(RenderType.guiOverlay(), pos.x() + 1, pos.y() + 1, pos2.x() - 1, pos2.y() - 1, 0xFF474747);
    }
    
    default void renderIconBackground(GuiGraphics gui, Vector2i pos, IIcon icon)
    {
        renderSimpleArea(gui, pos, pos.add(icon.getRenderSize()).expand(2));
    }
    default void renderTooltipAfterEverything(PoseStack pPoseStack, Component pText, int pMouseX, int pMouseY)
    {
    	addTooltip(new LateTooltipData(pPoseStack, pText, pMouseX, pMouseY));
    }

    void addTooltip(LateTooltipData data);

    class LateTooltipData
    {
        private final PoseStack poseStack;
        private final Component text;
        private final int mouseX;
        private final int mouseY;

        LateTooltipData(PoseStack poseStack, Component text, int mouseX, int mouseY)
        {
            this.poseStack = poseStack;
            this.text = text;
            this.mouseX = mouseX;
            this.mouseY = mouseY;
        }
        public PoseStack getPoseStack()
        {
            return poseStack;
        }
        public Component getText()
        {
            return text;
        }
        public int getMouseX()
        {
            return mouseX;
        }
        public int getMouseY()
        {
            return mouseY;
        }
    }
}
