package geni.witherutils.base.common.block.placer;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.type.WUTRenderType;
import geni.witherutils.base.common.base.RedstoneControl;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.core.client.gui.screen.WUTScreen;
import geni.witherutils.core.client.gui.widgets.EnergyWidget;
import geni.witherutils.core.common.math.Vector2i;
import geni.witherutils.core.common.math.Vector3;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class PlacerScreen extends WUTScreen<PlacerContainer> {

    public PlacerScreen(PlacerContainer pMenu, Inventory pPlayerInventory, Component pTitle)
    {
        super(pMenu, pPlayerInventory, pTitle);
    }

	@Override
    protected void init()
    {
        super.init();
        addRenderableOnly(new EnergyWidget(this, this.getMenu(), this.font, getMenu().getBlockEntity()::getEnergyStorage, leftPos + 8, topPos + 23, 16, 40));
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(gg);
        super.render(gg, mouseX, mouseY, partialTicks);
        this.renderTooltip(gg, mouseX, mouseY);

    	gg.blit(getRedstoneTexture(), this.leftPos + 151, this.topPos + 23, 0, 0, 12, 12, 12, 12);
    	gg.blit(getRenderTexture(), this.leftPos + 25, this.topPos + 23, 0, 0, 12, 12, 12, 12);
    	gg.blit(getRangeTexture(), this.leftPos + 25, this.topPos + 42, 0, 0, 12, 12, 12, 12);
    	gg.blit(getSpeedTexture(), this.leftPos + 25, this.topPos + 61, 0, 0, 12, 12, 12, 12);
    	
    	renderAround(gg.pose(), mouseX, mouseY, partialTicks);
    	renderNextPlacement(gg.pose(), mouseX, mouseY, partialTicks, menu.getBlockEntity().getInventory().getStackInSlot(0));
    	renderSlotButtonToolTips(gg, mouseX, mouseY);
    }
    
	public ResourceLocation getRedstoneTexture()
	{
		if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ALWAYS_ACTIVE)
			return WitherUtils.loc("textures/gui/redstone_active.png");
		else
			return WitherUtils.loc("textures/gui/redstone_signal.png");
	}
	public ResourceLocation getRenderTexture()
	{
		if(menu.getBlockEntity().getRender())
			return WitherUtils.loc("textures/gui/showpos_on.png");
		else
			return WitherUtils.loc("textures/gui/showpos_off.png");
	}
	public ResourceLocation getRangeTexture()
	{
		if(menu.getBlockEntity().getRange())
			return WitherUtils.loc("textures/gui/range_on.png");
		else
			return WitherUtils.loc("textures/gui/range_off.png");
	}
	public ResourceLocation getSpeedTexture()
	{
		if(menu.getBlockEntity().getSpeed())
			return WitherUtils.loc("textures/gui/speed_on.png");
		else
			return WitherUtils.loc("textures/gui/speed_off.png");
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		if(isHovering(151, 23, 12, 12, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ALWAYS_ACTIVE)
				menu.getBlockEntity().setRedstoneControl(RedstoneControl.ACTIVE_WITH_SIGNAL);
			else
				menu.getBlockEntity().setRedstoneControl(RedstoneControl.ALWAYS_ACTIVE);

			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else if(isHovering(25, 23, 12, 12, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getRender() == false)
				menu.getBlockEntity().setRender(true);
			else
				menu.getBlockEntity().setRender(false);

			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else if(isHovering(25, 42, 16, 10, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getRange())
				menu.getBlockEntity().setRange(false);
			else
				menu.getBlockEntity().setRange(true);

			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else if(isHovering(25, 61, 12, 12, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getSpeed())
				menu.getBlockEntity().setSpeed(false);
			else
				menu.getBlockEntity().setSpeed(true);

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
        return "Placer";
    }
    
    @Override
    public ResourceLocation getBackgroundImage()
    {
        return WitherUtils.loc("textures/gui/placer.png");
    }

    @Override
    protected Vector2i getBackgroundImageSize()
    {
        return new Vector2i(176, 131);
    }
    
    public void renderSlotButtonToolTips(GuiGraphics gg, int mouseX, int mouseY)
    {
        List<Component> list = new ArrayList<>();

        if(isHovering(151, 23, 12, 12, mouseX, mouseY))
        {
            Component appendRedSt = Component.literal("");
            
            if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ALWAYS_ACTIVE)
            	appendRedSt = Component.literal(ChatFormatting.GREEN + "Always");
            else if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ACTIVE_WITH_SIGNAL)
            	appendRedSt = Component.literal(ChatFormatting.RED + "withSignal");

            list.add(Component.translatable(ChatFormatting.GRAY + "Redstone Signal: ").append(appendRedSt));
        }
        else if(isHovering(152, 62, 16, 16, mouseX, mouseY))
        {
            if(!menu.getSlot(0).hasItem())
            {
            	list.add(Component.translatable(ChatFormatting.GRAY + "Block: ").append(Component.translatable(ChatFormatting.RED + "not found")));
            }
        }
        else if(isHovering(25, 23, 12, 12, mouseX, mouseY))
        {
            list.add(Component.translatable(ChatFormatting.GRAY + "Preview"));
        }
        else if(isHovering(25, 42, 12, 12, mouseX, mouseY))
        {
            list.add(Component.translatable(ChatFormatting.GRAY + "Range x1/x5"));
        }
        else if(isHovering(25, 61, 12, 12, mouseX, mouseY))
        {
            list.add(Component.translatable(ChatFormatting.GRAY + "Speed"));
        }
        gg.renderComponentTooltip(font, list, mouseX, mouseY); 
    }
    
	@SuppressWarnings("resource")
	public void renderNextPlacement(PoseStack ms, int mouseX, int mouseY, float partialTicks, ItemStack stack)
	{
        partialTicks = Minecraft.getInstance().getFrameTime();

        int startX = this.leftPos;
        int startY = this.topPos;

        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        {
            modelViewStack.translate(startX + 97, startY + 52, 160);
            modelViewStack.scale(48F, -48F, 48F);
            modelViewStack.mulPose(Axis.XP.rotationDegrees(5F));
            modelViewStack.mulPose(Axis.YP.rotationDegrees(Minecraft.getInstance().player.tickCount + partialTicks));
            RenderSystem.applyModelViewMatrix();
            MultiBufferSource.BufferSource buffer = this.minecraft.renderBuffers().bufferSource();
            Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.FIXED, false, ms, buffer, 15728880, OverlayTexture.NO_OVERLAY, Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(stack));
            buffer.endBatch();
        }
        modelViewStack.popPose();
        RenderSystem.applyModelViewMatrix();
	}
	
	public void renderAround(PoseStack ms, int mouseX, int mouseY, float partialTicks)
	{
        partialTicks = Minecraft.getInstance().getFrameTime();
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        
        int startX = this.leftPos;
        int startY = this.topPos;
        
    	double x = Vector3.CENTER.x - 0.5F;
    	double y = Vector3.CENTER.y - 0.5F;
    	double z = Vector3.CENTER.z - 0.5F;
      
    	modelViewStack.translate(x, y, z);

        modelViewStack.pushPose();
        {
            modelViewStack.translate(startX + 75, startY + 75, 100);
            modelViewStack.scale(45F, -45F, 45F);
            
        	modelViewStack.translate(0.5, 0.5, 0.5);
        	modelViewStack.mulPose(Axis.YP.rotationDegrees(180));
        	modelViewStack.translate(-0.5, -0.5, -0.5);
        	
            RenderSystem.applyModelViewMatrix();
        	RenderSystem.enableCull();
        	
            BlockRenderDispatcher rendererDispatcher = Minecraft.getInstance().getBlockRenderer();
            BakedModel model = rendererDispatcher.getBlockModelShaper().getBlockModel(WUTBlocks.PLACER.get().defaultBlockState());
            
            RenderSystem.applyModelViewMatrix();
            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer vertexBuilder = buffer.getBuffer(WUTRenderType.solid());
            Minecraft.getInstance().getItemRenderer().renderModelLists(model, ItemStack.EMPTY, 255, OverlayTexture.NO_OVERLAY, ms, vertexBuilder);
            Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.EMPLACER.getModel(), ItemStack.EMPTY, 255, OverlayTexture.NO_OVERLAY, ms, vertexBuilder);
            buffer.endBatch();
        }
        modelViewStack.popPose();

        RenderSystem.applyModelViewMatrix();
	}
}
