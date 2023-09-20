package geni.witherutils.base.common.block.battery.core;

import java.text.NumberFormat;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.type.WUTRenderType;
import geni.witherutils.base.common.io.energy.IWitherEnergyStorage;
import geni.witherutils.core.client.gui.screen.WUTScreen;
import geni.witherutils.core.client.gui.widgets.EnergyWidgetSimple;
import geni.witherutils.core.common.helper.MathHelper;
import geni.witherutils.core.common.math.Vector2i;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class CoreScreen extends WUTScreen<CoreContainer> {

    public CoreScreen(CoreContainer pMenu, Inventory pPlayerInventory, Component pTitle)
    {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init()
    {
        super.init();
        this.setHotbarVisible(false);
        addRenderableOnly(new EnergyWidgetSimple(this, this.getMenu(), this.font, getMenu().getBlockEntity()::getEnergyStorage, leftPos + 8, topPos + 23, 16, 40));
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(gg);
        super.render(gg, mouseX, mouseY, partialTicks);
        this.renderTooltip(gg, mouseX, mouseY);
        
		drawEnergyAmount(gg, mouseY, mouseY);
		renderSpheres(gg.pose(), mouseX, mouseY, partialTicks);

		gg.drawString(this.font, ChatFormatting.BOLD + "FE ", this.leftPos + 10, this.topPos + 68, 00707215);
		gg.blit(getCheckboxTexture(), this.leftPos + 7, this.topPos + 107, 0, 0, 12, 12, 12, 12);
		gg.drawString(this.font, Component.literal("BuildGuide"), this.leftPos + 22, this.topPos + 109, 16777215);
    }
    
    protected void drawEnergyAmount(GuiGraphics gg, int mouseX, int mouseY)
    {
        RenderSystem.enableBlend();
        IWitherEnergyStorage e = this.menu.getBlockEntity().getEnergyStorage();
        String s = addCommas(e.getEnergyStored()) + "/" + numFormat(e.getMaxEnergyStored()) + " FE";
        gg.drawString(this.font, s, this.leftPos + 42, this.topPos + 68, 16777215);
        gg.drawString(this.font, numFormat(e.getMaxEnergyTransfer()) + " FE/t", this.leftPos + 42, this.topPos + 86, 16777215);
        RenderSystem.disableBlend();
    }

	static final NavigableMap<Long, String> SUFFIXES = new TreeMap<>();
	static {
		SUFFIXES.put(1_000L, "k");
		SUFFIXES.put(1_000_000L, "M");
		SUFFIXES.put(1_000_000_000L, "B");
		SUFFIXES.put(1_000_000_000_000L, "T");
		SUFFIXES.put(1_000_000_000_000_000L, "P");
		SUFFIXES.put(1_000_000_000_000_000_000L, "E");
	}
	public static String numFormat(long value)
	{
		if (value == Long.MIN_VALUE)
			return numFormat(Long.MIN_VALUE + 1);
		if (value < 0)
			return "-" + numFormat(-value);
		if (value < 1000)
			return Long.toString(value);

		Map.Entry<Long, String> e = SUFFIXES.floorEntry(value);
		Long divideBy = e.getKey();
		String suffix = e.getValue();

		long truncated = value / (divideBy / 10);
		boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
		return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
	}
	public static String addCommas(long value)
	{
		return NumberFormat.getInstance().format(value);
	}
	
	public ResourceLocation getCheckboxTexture()
	{
		if(menu.getBlockEntity().isShowingBuildGuide())
			return WitherUtils.loc("textures/gui/checkbox_on.png");
		else
			return WitherUtils.loc("textures/gui/checkbox.png");
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		if(isHovering(7, 107, 12, 12, mouseX, mouseY))
		{
			if(menu.getBlockEntity().isShowingBuildGuide())
				menu.getBlockEntity().setShowingBuildGuide(false);
			else
				menu.getBlockEntity().setShowingBuildGuide(true);

			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else
		{
			return super.mouseClicked(mouseX, mouseY, mouseButton);
		}
		return true;
	}
    
    @Override
    protected String getBarName()
    {
        return "Battery";
    }
    
    @Override
    public ResourceLocation getBackgroundImage()
    {
        return WitherUtils.loc("textures/gui/battery.png");
    }

    @Override
    protected Vector2i getBackgroundImageSize()
    {
        return new Vector2i(176, 126);
    }

	@SuppressWarnings("resource")
	public void renderSpheres(PoseStack ms, int mouseX, int mouseY, float partialTicks)
	{
        partialTicks = Minecraft.getInstance().getFrameTime();
        
        int startX = this.leftPos;
        int startY = this.topPos;
        
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        {
            modelViewStack.translate(startX + 128, startY + 71, 100);
            modelViewStack.scale(30F, -30F, 30F);
            modelViewStack.mulPose(Axis.XP.rotationDegrees(5F));
    		float rotation = (Minecraft.getInstance().player.tickCount + partialTicks) / 2F;
    		modelViewStack.mulPose(Axis.YP.rotationDegrees(rotation * (float) MathHelper.torad * 40));
    		modelViewStack.mulPose(Axis.ZP.rotationDegrees(rotation * (float) MathHelper.torad * 40 / 2));
    		
            RenderSystem.applyModelViewMatrix();
            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer vertexBuilder = buffer.getBuffer(WUTRenderType.solid());
            Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.SPHEREINNERSCREEN.getModel(), ItemStack.EMPTY, 255, OverlayTexture.NO_OVERLAY, ms, vertexBuilder);
            buffer.endBatch();
        }
        modelViewStack.popPose();
        
        modelViewStack.pushPose();
        {
            modelViewStack.translate(startX + 128, startY + 71, 100);
            modelViewStack.scale(32F, -32F, 32F);
            modelViewStack.mulPose(Axis.XP.rotationDegrees(5F));
    		float rotation = (Minecraft.getInstance().player.tickCount + partialTicks) / 2F;
    		modelViewStack.mulPose(Axis.YN.rotationDegrees(rotation * 0.5F * (float) MathHelper.torad * 60));
    		modelViewStack.mulPose(Axis.ZN.rotationDegrees(rotation * 0.5F * (float) MathHelper.torad * 60 / 2));

            RenderSystem.applyModelViewMatrix();
            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer vertexBuilder = buffer.getBuffer(WUTRenderType.translucent());
            Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.SPHEREOUTERSCREEN.getModel(), ItemStack.EMPTY, 255, OverlayTexture.NO_OVERLAY, ms, vertexBuilder);
            buffer.endBatch();
        }
        modelViewStack.popPose();
        
        RenderSystem.applyModelViewMatrix();
	}
}
