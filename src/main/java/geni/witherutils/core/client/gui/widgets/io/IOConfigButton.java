package geni.witherutils.core.client.gui.widgets.io;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import com.mojang.blaze3d.systems.RenderSystem;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.base.WitherMachineMenu;
import geni.witherutils.core.client.gui.screen.WUTScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;

public class IOConfigButton<U extends WUTScreen<?>, T extends AbstractWidget> extends AbstractWidget {
    
    private static final int RENDERER_HEIGHT = 80;
    private final IOConfigWidget<U> configRenderer;
    private final Supplier<Boolean> playerInvVisible;
    private final Function<Boolean, Boolean> setPlayerInvVisible;
    private final U addedOn;
    private final Font font;
    
    private final ImageButton upButton;
    
    public IOConfigButton(U addedOn, int x, int y, int width, int height, WitherMachineMenu<?> menu, Function<AbstractWidget, T> addRenderableWidget, Font font)
    {
        super(x, y, width, height, Component.empty());
        this.addedOn = addedOn;
        this.playerInvVisible = menu::getPlayerInvVisible;
        this.setPlayerInvVisible = menu::setPlayerInvVisible;

        var show = !playerInvVisible.get();
        configRenderer = new IOConfigWidget<>(addedOn, addedOn.getGuiLeft() + 5, addedOn.getGuiTop() + addedOn.getYSize() - RENDERER_HEIGHT - 5,
            addedOn.getXSize() - 10, RENDERER_HEIGHT, font, menu.getBlockEntity());
        configRenderer.visible = show;
        addRenderableWidget.apply(configRenderer);

        this.font = font;
        
        
        upButton = new ImageButton(addedOn.getGuiLeft() - 16, addedOn.getGuiTop() + addedOn.getYSize() - 5 - 16, 16, 16, 16, 0,
                0, WitherUtils.loc("textures/gui/io_config.png"), 48, 32, (b) -> configRenderer.cycleEnergyUp(), Component.literal(""));

        upButton.visible = show;
        addRenderableWidget.apply(upButton);
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
        var state = !setPlayerInvVisible.apply(!playerInvVisible.get());
        configRenderer.visible = state;
        addedOn.setHotbarVisible(playerInvVisible.get());
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {}
}
