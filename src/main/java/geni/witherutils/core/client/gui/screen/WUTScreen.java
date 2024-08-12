package geni.witherutils.core.client.gui.screen;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.core.common.math.Vector2i;
import geni.witherutils.core.common.util.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class WUTScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> implements IWitherScreen {

    private final boolean renderLabels;
    private final List<LateTooltipData> tooltips = new ArrayList<>();
	private boolean showHotbar = true;
	private int hotbarOffset = 88;

    protected WUTScreen(T pMenu, Inventory pPlayerInventory, Component pTitle)
    {
        this(pMenu, pPlayerInventory, pTitle, false);
    }
    protected WUTScreen(T pMenu, Inventory pPlayerInventory, Component pTitle, boolean renderLabels)
    {
        super(pMenu, pPlayerInventory, pTitle);
        this.renderLabels = renderLabels;
        this.imageWidth = getBackgroundImageSize().x();
        this.imageHeight = getBackgroundImageSize().y();
    }

    protected void drawBackground(GuiGraphics gui, ResourceLocation res)
    {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, res);
		int relX = (this.width - this.imageWidth) / 2;
		int relY = (this.height - this.imageHeight) / 2;
		gui.blit(res, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }
	
    public void setHotbarVisible(boolean value)
    {
    	showHotbar = value;
    }
    public void setHotbarOffset(int value)
    {
    	hotbarOffset = value;
    }
    
    @Override
    public void render(GuiGraphics gui, int pMouseX, int pMouseY, float pPartialTicks)
    {
        renderBackground(gui, pMouseX, pMouseY, pPartialTicks);
        super.render(gui, pMouseX, pMouseY, pPartialTicks);
        this.renderTooltip(gui, pMouseX, pMouseY);
        for (LateTooltipData tooltip : tooltips)
        {
            renderTooltip(gui, tooltip.getMouseX(), tooltip.getMouseY());
        }
    }

    protected void renderBg(GuiGraphics gui, float pPartialTick, int pMouseX, int pMouseY)
    {
        tooltips.clear();
        
        gui.blit(getBackgroundImage(), getGuiLeft(), getGuiTop(), 0, 0, imageWidth, imageHeight);
        
        if(showHotbar)
        	gui.blit(WitherUtilsRegistry.loc("textures/gui/hotbar.png"), getGuiLeft(), getGuiTop() + hotbarOffset, 0, 0, 179, 49, 179, 49);
		
        renderBar(gui, pMouseX, pMouseY, pPartialTick, true);
        
        if(!showHotbar)
        	return;
        
        for(int i = getMenu().slots.size() - 9; i < getMenu().slots.size(); ++i)
        {
            int slotX = getMenu().slots.get(i).x;
            int slotY = getMenu().slots.get(i).y;

            if(isHovering(slotX, slotY, 16, 16, pMouseX, pMouseY))
            {
                gui.pose().pushPose();
                float f = pPartialTick % 10F / 10F;
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(0.5f + f, 0.5f + f, 0.5f + f, 1.0f);
                gui.blit(WitherUtilsRegistry.loc("textures/gui/hbslot_hover.png"), leftPos - 4 + slotX, topPos - 4 + slotY, 0, 0, 24, 24, 24, 24);
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                gui.pose().popPose();
            }
        }
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
    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton)
    {
        for (GuiEventListener widget : children())
        {
            if (widget instanceof AbstractWidget abstractWidget && abstractWidget.isActive() && widget instanceof IFullScreenListener fullScreenListener)
            {
                fullScreenListener.onGlobalClick(pMouseX, pMouseY);
            }
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY)
    {
        if (getFocused() instanceof AbstractWidget abstractWidget && abstractWidget.isActive())
        {
            return abstractWidget.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        }
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }
    @Override
    protected void renderLabels(GuiGraphics gui, int pMouseX, int pMouseY)
    {
        if (renderLabels)
        {
            super.renderLabels(gui, pMouseX, pMouseY);
        }
    }
    @Override
    protected void containerTick()
    {
        super.containerTick();
    }

    public abstract ResourceLocation getBackgroundImage();
    protected abstract Vector2i getBackgroundImageSize();
    protected abstract String getBarName();

    @Override
    public void addTooltip(LateTooltipData data)
    {
        tooltips.add(data);
    }
    
    /*
     * 
     * SPECIAL
     * 
     */
    public void renderBar(GuiGraphics gui, int mouseX, int mouseY, float partialTicks, boolean redstone)
    {
        Minecraft mc = Minecraft.getInstance();
        gui.pose().pushPose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WitherUtilsRegistry.loc("textures/gui/bareffect.png"));
        float time = mc.level.getGameTime() + partialTicks;
        double offset = Math.sin(time * 1.0D / 4.0D) / 10.0D;
        if (redstone)
            offset = Math.sin(time * 1.0D / 12.0D) / 10.0D;
        gui.pose().translate(0.0D + offset * 100, 0.0D, 0.0D);
        gui.blit(WitherUtilsRegistry.loc("textures/gui/bareffect.png"), this.leftPos + 120, this.topPos + 7, 0, 0, 32, 16, 32, 16);
        gui.pose().popPose();
        drawTextWithScale(gui, Component.translatable("gui.witherutils.namesquares").append(getBarName()), this.leftPos + 9, this.topPos + 4, 0xFFCCCCCC, 0.749f, false);
        drawTextWithScale(gui, Component.translatable("gui.witherutils.blockheads"), this.leftPos + 32, this.topPos + 10, 0xFF00AAFF, 0.5f, false);
    }
    public void renderProgress(GuiGraphics gui, float partialTicks, int mouseX, int mouseY, boolean isworking, ResourceLocation texture)
    {
        Minecraft mc = Minecraft.getInstance();
        gui.pose().pushPose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        float time = mc.level.getGameTime() + partialTicks;

        double hoffset = Math.sin(time * 1.0D / 4.0D) / 15.0D;
        double voffset = Math.sin(time * 1.0D / 2.0D) / 15.0D;

        gui.pose().translate(0.0D + hoffset * mc.level.random.nextDouble() * 10, 0.0D + voffset * mc.level.random.nextDouble() * 10, 0.0D);
        RenderSystem.setShaderColor(1.0f + (float)hoffset, 0.95f + (float)hoffset * 2, 1.0f + (float)hoffset, 0.6f + (float)hoffset *4);
        gui.blit(texture, this.leftPos + 71, this.topPos + 40, 0, 0, 14, 26, 14, 26);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        gui.pose().popPose();
    }
    public void renderFadeProgress(GuiGraphics gui, float partialTicks, int x, int y, int mouseX, int mouseY, boolean isworking, ResourceLocation texture)
    {
        if(!isworking)
            return;
        
        Minecraft mc = Minecraft.getInstance();
        gui.pose().pushPose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);

        float time = mc.level.getGameTime() + partialTicks;
        double hoffset = Math.sin(time * 1.0D / 4.0D) / 15.0D;
        RenderSystem.setShaderColor(1.0f + (float)hoffset, 0.95f + (float)hoffset * 2, 1.0f + (float)hoffset, 0.6f + (float)hoffset *4);
        gui.blit(texture, this.leftPos + x - 18, this.topPos + y, 0, 0, 4, 24, 4, 24);
        gui.blit(texture, this.leftPos + x + 14, this.topPos + y, 0, 0, 4, 24, -4, 24);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        gui.pose().popPose();
    }
    public void renderHotBarColor(GuiGraphics gui, int mouseX, int mouseY)
    {
        for(int i = 0; i < 9; i++)
        {
            if(isHovering(7 + (i * 18), 150, 18, 18, mouseX, mouseY))
            {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, WitherUtilsRegistry.loc("textures/gui/slot2x2.png"));
                RenderSystem.setShaderColor(0.0f, 0.995f, 1.0f, 1.0f);
                gui.blit(WitherUtilsRegistry.loc("textures/gui/slot2x2.png"), leftPos + 7 + (i * 18), topPos + 167, 0, 0, 18, 3, 18, 18);
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }

    /*
     * 
     * SLOT
     * 
     */
    protected void drawSlotNormal(GuiGraphics ms, int x, int y, boolean blueborder, boolean redborder, boolean locked)
    {
        drawSlotNormal(ms, x, y, WitherUtilsRegistry.loc("textures/gui/slot/slot_normal.png"), 18, blueborder, redborder, locked);
    }
    protected void drawSlotNormal(GuiGraphics gg, int x, int y, ResourceLocation texture, int size, boolean blueborder, boolean redborder, boolean locked)
    {
        if(locked)
            gg.blit(WitherUtilsRegistry.loc("textures/gui/slot/slot_normal_locked.png"), leftPos + x, topPos + y, 0, 0, size, size, size, size);
        if(blueborder)
        	gg.blit(WitherUtilsRegistry.loc("textures/gui/slot/slot_normal_in.png"), leftPos + x, topPos + y, 0, 0, size, size, size, size);
        if(redborder)
        	gg.blit(WitherUtilsRegistry.loc("textures/gui/slot/slot_normal_out.png"), leftPos + x, topPos + y, 0, 0, size, size, size, size);
    }
    
    protected void drawSlotLarge(GuiGraphics ms, int x, int y, boolean blueborder, boolean redborder, boolean locked)
    {
        drawSlotLarge(ms, x, y, WitherUtilsRegistry.loc("textures/gui/slot/slot_large.png"), 22, blueborder, redborder, locked);
    }
    protected void drawSlotLarge(GuiGraphics gg, int x, int y, ResourceLocation texture, int size, boolean blueborder, boolean redborder, boolean locked)
    {
        if(locked)
            gg.blit(WitherUtilsRegistry.loc("textures/gui/slot/slot_large_locked.png"), leftPos + x, topPos + y, 0, 0, size, size, size, size);
        gg.blit(WitherUtilsRegistry.loc("textures/gui/slot/slot_large_out.png"), leftPos + x, topPos + y, 0, 0, size, size, size, size);
    }
    
    protected void drawLockedGreySlot(GuiGraphics gui, int x, int y, int width, int height, int mouseX, int mouseY, boolean hide)
    {
        if(hide) return;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WitherUtilsRegistry.loc("textures/gui/slot2x2.png"));
        RenderSystem.setShaderColor(0.6f, 0.6f, 0.6f, 1.0f);
        gui.blit(WitherUtilsRegistry.loc("textures/gui/slot2x2.png"), leftPos + x, topPos + y, 0, 0, 16, 16, 16, 16);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
    protected void drawHoverColoredSlot(GuiGraphics gui, int x, int y, int width, int height, int mouseX, int mouseY, boolean hovered, float red, float green, float blue, float alphaHover, float alphaUnhover)
    {
        final int inputsize = hovered ? 14 : 16;
        final float alpha = hovered ? alphaHover : alphaUnhover;
        
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WitherUtilsRegistry.loc("textures/gui/slot2x2.png"));
        RenderSystem.setShaderColor(red, green, blue, alpha);
        gui.blit(WitherUtilsRegistry.loc("textures/gui/slot2x2.png"), hovered ? leftPos + x + 1 : leftPos + x, hovered ? topPos + y + 1 : topPos + y, 0, 0, inputsize, inputsize, inputsize, inputsize);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
    protected void drawHoverBlueInputSlot(GuiGraphics gui, int x, int y, int width, int height, int mouseX, int mouseY, boolean hovered)
    {
        final int inputsize = hovered ? 14 : 16;
        
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WitherUtilsRegistry.loc("textures/gui/slot2x2.png"));
        RenderSystem.setShaderColor(0.392f, 0.521f, 0.627f, 1.0f);
        gui.blit(WitherUtilsRegistry.loc("textures/gui/slot2x2.png"), hovered ? leftPos + x + 1 : leftPos + x, hovered ? topPos + y + 1 : topPos + y, 0, 0, inputsize, inputsize, inputsize, inputsize);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
    protected void drawHoverRedOutputSlot(GuiGraphics gui, int x, int y, int width, int height, int mouseX, int mouseY, boolean hovered)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WitherUtilsRegistry.loc("textures/gui/slot2x2.png"));
        RenderSystem.setShaderColor(hovered ? 0.6f : 0.627f, hovered ? 0.4f : 0.423f, hovered ? 0.3f : 0.392f, 1.0f);
        gui.blit(WitherUtilsRegistry.loc("textures/gui/slot2x2.png"), leftPos + x, topPos + y, 0, 0, 24, 20, 20, 20);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
    protected void drawHoverGrayInventorySlot(GuiGraphics gui, int x, int y, int width, int height, int mouseX, int mouseY, boolean hovered)
    {
        final int inputsize = hovered ? 14 : 16;
        
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WitherUtilsRegistry.loc("textures/gui/slot2x2.png"));
        RenderSystem.setShaderColor(hovered ? 0.392f : 0.9f, hovered ? 0.521f : 0.95f, hovered ? 0.627f : 1.0f, 1.0f);
        gui.blit(WitherUtilsRegistry.loc("textures/gui/slot2x2.png"), hovered ? leftPos + x + 1 : leftPos + x, hovered ? topPos + y + 1 : topPos + y, 0, 0, inputsize, inputsize, inputsize, inputsize);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
    public void renderSlotColor(PoseStack ms, int mouseX, int mouseY, int yOffset)
    {
    }
    
    /*
     * 
     * SOUND
     * 
     */
    protected void playDownSound(SoundManager manager)
    {
        manager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
    protected void playDownSoundFail(SoundManager manager)
    {
        manager.play(SimpleSoundInstance.forUI(SoundEvents.WOODEN_BUTTON_CLICK_OFF, 1.0F));
    }
    
    /*
     * 
     * TEXT
     * 
     */
    public void renderMinecraftScaledText(GuiGraphics gg, int x, int y, int mouseX, int mouseY, float partialTicks, float maxScale, ChatFormatting leftFormatColor, String firsttext, ChatFormatting rightFormatColor, String appendText, float number, String appendFormatProvider, boolean shadow)
    {
        gg.pose().pushPose();
        NumberFormat fmt = NumberFormat.getInstance(Locale.ENGLISH);
        Component text = Component.translatable(leftFormatColor + firsttext + " ").append(rightFormatColor + appendText + " ").append(fmt.format(number) + " " + appendFormatProvider);
        float scale = Math.min(0.5f, maxScale / getStringWidth(text));
        drawScaledCenteredText(gg, text, leftPos + getXSize() / 2F + x, topPos + y, 0xFF404040, scale, shadow);
        gg.pose().popPose();
    }
    
    public void renderSoulieScaledText(GuiGraphics gg, int x, int y, int mouseX, int mouseY, float partialTicks, float maxScale, ChatFormatting leftFormatColor, String firsttext, ChatFormatting rightFormatColor, String appendText, float number, String appendFormatProvider, boolean shadow)
    {
        gg.pose().pushPose();
        NumberFormat fmt = NumberFormat.getInstance(Locale.ENGLISH);
        ResourceLocation SOULIEFONT = WitherUtilsRegistry.loc("soulie");
        Style STYLE = Style.EMPTY.withFont(SOULIEFONT);
        Component text = Component.translatable(leftFormatColor + firsttext + " ").append(rightFormatColor + appendText + " ").append(fmt.format(number) + " " + appendFormatProvider).setStyle(STYLE);
        float scale = Math.min(0.75f, maxScale / getStringWidth(text));
        drawScaledCenteredText(gg, text, leftPos + getXSize() / 2F + x, topPos + y, 0xFF404040, scale, shadow);
        gg.pose().popPose();
    }
    
    public int drawString(GuiGraphics gg, Component component, int x, int y, int color, boolean shadow)
    {
        return gg.drawString(font, component, x, y, color, shadow);
    }
    public int getStringWidth(Component component)
    {
        return font.width(component);
    }
    public void drawCenteredText(GuiGraphics gg, Component component, float x, float y, int color, boolean shadow)
    {
        drawCenteredText(gg, component, x, 0, y, color, shadow);
    }
    public void drawCenteredText(GuiGraphics gg, Component component, float xStart, float areaWidth, float y, int color, boolean shadow)
    {
        int textWidth = getStringWidth(component);
        float centerX = xStart + (areaWidth / 2F) - (textWidth / 2F);
        drawTextExact(gg, component, centerX, y, color, shadow);
    }
    public void drawTextExact(GuiGraphics gg, Component text, float x, float y, int color, boolean shadow)
    {
        gg.pose().pushPose();
        gg.pose().translate(x, y, 0);
        drawString(gg, text, 0, 0, color, shadow);
        gg.pose().popPose();
    }
    public void drawTitleText(GuiGraphics gg, Component text, float y, boolean shadow)
    {
        drawCenteredTextScaledBound(gg, text, getXSize() - 8, y, 0xFF404040, shadow);
    }
    public void drawCenteredTextScaledBound(GuiGraphics gg, Component text, float maxLength, float y, int color, boolean shadow)
    {
        drawCenteredTextScaledBound(gg, text, maxLength, 0, y, color, shadow);
    }
    public void drawCenteredTextScaledBound(GuiGraphics gg, Component text, float maxLength, float x, float y, int color, boolean shadow)
    {
        float scale = Math.min(1, maxLength / getStringWidth(text));
        drawScaledCenteredText(gg, text, x + getXSize() / 2F, y, color, scale, shadow);
    }
    public void drawScaledCenteredText(GuiGraphics gg, Component text, float left, float y, int color, float scale, boolean shadow)
    {
        int textWidth = getStringWidth(text);
        float centerX = left - (textWidth / 2F) * scale;
        drawTextWithScale(gg, text, centerX, y, color, scale, shadow);
    }
    public void drawTextWithScale(GuiGraphics gg, Component text, float x, float y, int color, float scale, boolean shadow)
    {
        prepTextScale(gg, m -> drawString(m, text, 0, 0, color, shadow), x, y, scale);
    }
    public void prepTextScale(GuiGraphics gg, Consumer<GuiGraphics> runnable, float x, float y, float scale)
    {
        float yAdd = 4 - (scale * 8) / 2F;
        gg.pose().pushPose();
        gg.pose().translate(x, y + yAdd, 0);
        gg.pose().scale(scale, scale, scale);
        runnable.accept(gg);
        gg.pose().popPose();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
    public void drawScaledCenteredTextScaledBound(GuiGraphics gg, Component text, float left, float y, int color, float maxX, float textScale, boolean shadow)
    {
        float width = getStringWidth(text) * textScale;
        float scale = Math.min(1, maxX / width) * textScale;
        drawScaledCenteredText(gg, text, left, y, color, scale, shadow);
    }
    public float getNeededScale(Component text, float maxLength)
    {
        int length = getStringWidth(text);
        return length <= maxLength ? 1 : maxLength / length;
    }
    public void drawTextScaledBound(GuiGraphics gg, String text, float x, float y, int color, float maxLength, boolean shadow)
    {
        drawTextScaledBound(gg, TextUtil.getString(text), x, y, color, maxLength, shadow);
    }
    public void drawTextScaledBound(GuiGraphics gg, Component component, float x, float y, int color, float maxLength, boolean shadow)
    {
        int length = getStringWidth(component);

        if (length <= maxLength) {
            drawTextExact(gg, component, x, y, color, shadow);
        } else {
            drawTextWithScale(gg, component, x, y, color, maxLength / length, shadow);
        }
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
    public void drawScaledTextScaledBound(GuiGraphics gg, Component text, float x, float y, int color, float maxX, float textScale, boolean shadow)
    {
        float width = getStringWidth(text) * textScale;
        float scale = Math.min(1, maxX / width) * textScale;
        drawTextWithScale(gg, text, x, y, color, scale, shadow);
    }
    public int drawWrappedTextWithScale(GuiGraphics gg, Component text, float x, float y, int color, float maxLength, float scale, boolean shadow)
    {
        return new WrappedTextRenderer(this.font, text).renderWithScale(gg, x, y, color, maxLength, scale, shadow);
    }
    public void drawWrappedCenteredText(GuiGraphics gg, Component text, float x, float y, int color, float maxLength, boolean shadow)
    {
        new WrappedTextRenderer(this.font, text).renderCentered(gg, x, y, color, maxLength, shadow);
    }
    
    class WrappedTextRenderer
    {
        private final List<LineData> linesToDraw = new ArrayList<>();
        private final Font font;
        private final String text;
        @Nullable
        private Font lastFont;
        private float lastMaxLength = -1;
        private float lineLength = 0;

        public WrappedTextRenderer(Font font, Component text) {
            this(font, text.getString());
        }

        public WrappedTextRenderer(Font font, String text) {
            this.font = font;
            this.text = text;
        }

        public void renderCentered(GuiGraphics gg, float x, float y, int color, float maxLength, boolean shadow) {
            calculateLines(maxLength);
            float startY = y;
            for (LineData line : linesToDraw)
            {
                drawTextExact(gg, line.component(), x - line.length() / 2, startY, color, shadow);
                startY += 9;
            }
        }

        public int renderWithScale(GuiGraphics gg, float x, float y, int color, float maxLength, float scale, boolean shadow) {
            calculateLines(maxLength / scale);
            prepTextScale(gg, m -> {
                int startY = 0;
                for (LineData line : linesToDraw) {
                    drawString(m, line.component(), 0, startY, color, shadow);
                    startY += 9;
                }
            }, x, y, scale);
            return linesToDraw.size();
        }

        void calculateLines(float maxLength) {
            Font font = this.font;
            if (font != null && (lastFont != font || lastMaxLength != maxLength)) {
                lastFont = font;
                lastMaxLength = maxLength;
                linesToDraw.clear();
                StringBuilder lineBuilder = new StringBuilder();
                StringBuilder wordBuilder = new StringBuilder();
                int spaceLength = lastFont.width(" ");
                int wordLength = 0;
                for (char c : text.toCharArray()) {
                    if (c == ' ') {
                        lineBuilder = addWord(lineBuilder, wordBuilder, maxLength, spaceLength, wordLength);
                        wordBuilder = new StringBuilder();
                        wordLength = 0;
                        continue;
                    }
                    wordBuilder.append(c);
                    wordLength += lastFont.width(Character.toString(c));
                }
                if (!wordBuilder.isEmpty()) {
                    lineBuilder = addWord(lineBuilder, wordBuilder, maxLength, spaceLength, wordLength);
                }
                if (!lineBuilder.isEmpty()) {
                    linesToDraw.add(new LineData(TextUtil.getString(lineBuilder.toString()), lineLength));
                }
            }
        }

        StringBuilder addWord(StringBuilder lineBuilder, StringBuilder wordBuilder, float maxLength, int spaceLength, int wordLength) {
            float spacingLength = lineBuilder.isEmpty() ? 0 : spaceLength;
            if (lineLength + spacingLength + wordLength > maxLength) {
                linesToDraw.add(new LineData(TextUtil.getString(lineBuilder.toString()), lineLength));
                lineBuilder = new StringBuilder(wordBuilder);
                lineLength = wordLength;
            } else {
                if (spacingLength > 0) {
                    lineBuilder.append(" ");
                }
                lineBuilder.append(wordBuilder);
                lineLength += spacingLength + wordLength;
            }
            return lineBuilder;
        }

        public static int calculateHeightRequired(Font font, Component text, int width, float maxLength)
        {
            return calculateHeightRequired(font, text.getString(), width, maxLength);
        }

        public static int calculateHeightRequired(Font font, String text, int width, float maxLength)
        {
            return 9 * 4;
        }

        private record LineData(Component component, float length) {}
    }
    
    /*
     * 
     * GRADIENT BOX
     * 
     */
    public static void renderGradientBox(GuiGraphics gg, int leftPos, int topPos, int leftOffset, int topOffset, int width, int height, int backgroundTop, int backgroundBottom, int borderTop, int borderBottom)
    {
        Vector2i pos = new Vector2i(0, 0);
        Vector2i pos2 = pos.add(new Vector2i(60, 60));
        renderTooltipBackground(gg, leftPos + leftOffset + pos.x(), topPos + topOffset + pos.y(), pos2.x(), pos2.y(), 0, backgroundTop, backgroundBottom, borderTop, borderBottom);
    }

    public static void renderTooltipBackground(GuiGraphics gg, int p_281901_, int p_281846_, int p_281559_, int p_283336_, int p_283422_, int backgroundTop, int backgroundBottom, int borderTop, int borderBottom)
    {
       int i = p_281901_ - 3;
       int j = p_281846_ - 3;
       int k = p_281559_ + 3 + 3;
       int l = p_283336_ + 3 + 3;
       renderHorizontalLine(gg, i, j - 1, k, p_283422_, backgroundTop);
       renderHorizontalLine(gg, i, j + l, k, p_283422_, backgroundBottom);
       renderRectangle(gg, i, j, k, l, p_283422_, backgroundTop, backgroundBottom);
       renderVerticalLineGradient(gg, i - 1, j, l, p_283422_, backgroundTop, backgroundBottom);
       renderVerticalLineGradient(gg, i + k, j, l, p_283422_, backgroundTop, backgroundBottom);
       renderFrameGradient(gg, i, j + 1, k, l, p_283422_, borderTop, borderBottom);
    }
    private static void renderFrameGradient(GuiGraphics p_282000_, int p_282055_, int p_281580_, int p_283284_, int p_282599_, int p_283432_, int p_282907_, int p_283153_)
    {
        renderVerticalLineGradient(p_282000_, p_282055_, p_281580_, p_282599_ - 2, p_283432_, p_282907_, p_283153_);
        renderVerticalLineGradient(p_282000_, p_282055_ + p_283284_ - 1, p_281580_, p_282599_ - 2, p_283432_, p_282907_, p_283153_);
        renderHorizontalLine(p_282000_, p_282055_, p_281580_ - 1, p_283284_, p_283432_, p_282907_);
        renderHorizontalLine(p_282000_, p_282055_, p_281580_ - 1 + p_282599_ - 1, p_283284_, p_283432_, p_283153_);
    }
    private static void renderHorizontalLine(GuiGraphics p_282981_, int p_282028_, int p_282141_, int p_281771_, int p_282734_, int p_281979_)
    {
        p_282981_.fill(p_282028_, p_282141_, p_282028_ + p_281771_, p_282141_ + 1, p_282734_, p_281979_);
    }
    private static void renderRectangle(GuiGraphics p_281392_, int p_282294_, int p_283353_, int p_282640_, int p_281964_, int p_283211_, int p_282349_, int colorTo)
    {
        p_281392_.fillGradient(p_282294_, p_283353_, p_282294_ + p_282640_, p_283353_ + p_281964_, p_283211_, p_282349_, colorTo);
    }
    private static void renderVerticalLineGradient(GuiGraphics p_282478_, int p_282583_, int p_283262_, int p_283161_, int p_283322_, int p_282624_, int p_282756_)
    {
        p_282478_.fillGradient(p_282583_, p_283262_, p_282583_ + 1, p_283262_ + p_283161_, p_283322_, p_282624_, p_282756_);
    }
}
