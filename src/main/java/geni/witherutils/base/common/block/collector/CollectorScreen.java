package geni.witherutils.base.common.block.collector;

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
import net.minecraft.world.item.ItemStack;

public class CollectorScreen extends WUTScreen<CollectorContainer> {
    
    public CollectorScreen(CollectorContainer pMenu, Inventory pPlayerInventory, Component pTitle)
    {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init()
    {
        super.init();
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(gg);
        super.render(gg, mouseX, mouseY, partialTicks);
        this.renderTooltip(gg, mouseX, mouseY);
        
		renderAround(gg.pose(), mouseX, mouseY, partialTicks);
		
    	gg.blit(getRedstoneTexture(), this.leftPos + 26, this.topPos + 66, 0, 0, 12, 12, 12, 12);
    	gg.blit(getRenderTexture(), this.leftPos + 10, this.topPos + 66, 0, 0, 12, 12, 12, 12);
    	gg.blit(getOverflowTexture(), this.leftPos + 43, this.topPos + 66, 0, 0, 12, 12, 12, 12);

    	renderSlotButtonToolTips(gg, mouseX, mouseY);
    }
    
	@SuppressWarnings("resource")
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
            modelViewStack.translate(startX + 10, startY + 75, 100);
            modelViewStack.scale(45F, -45F, 45F);
            
            RenderSystem.applyModelViewMatrix();
        	RenderSystem.enableCull();
        	
            BlockRenderDispatcher rendererDispatcher = Minecraft.getInstance().getBlockRenderer();
            BakedModel model = rendererDispatcher.getBlockModelShaper().getBlockModel(WUTBlocks.COLLECTOR.get().defaultBlockState());
            
            RenderSystem.applyModelViewMatrix();
            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer vertexBuilder = buffer.getBuffer(WUTRenderType.solid());
            Minecraft.getInstance().getItemRenderer().renderModelLists(model, ItemStack.EMPTY, 255, OverlayTexture.NO_OVERLAY, ms, vertexBuilder);
            buffer.endBatch();
        }
        modelViewStack.popPose();

        modelViewStack.pushPose();
        {
            modelViewStack.translate(startX + 10, startY + 75, 100);
            modelViewStack.scale(45F, -45F, 45F);

            RenderSystem.applyModelViewMatrix();
            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer vertexBuilder = buffer.getBuffer(WUTRenderType.solid());
            Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.COLLECTOR.getModel(), ItemStack.EMPTY, 255, OverlayTexture.NO_OVERLAY, ms, vertexBuilder);
            buffer.endBatch();
        }
        modelViewStack.popPose();
        
        modelViewStack.pushPose();
        {
            modelViewStack.translate(startX + 10, startY + 75, 100);
            modelViewStack.scale(45F, -45F, 45F);
        	
        	modelViewStack.translate(0.5, 0.5, 0.5);
    		float rotation = (Minecraft.getInstance().player.tickCount * 30 + partialTicks) / 2F;
        	modelViewStack.mulPose(Axis.YP.rotationDegrees(rotation));
        	modelViewStack.translate(-0.5, -0.5, -0.5);

            RenderSystem.applyModelViewMatrix();
            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer vertexBuilder = buffer.getBuffer(WUTRenderType.translucent());
            Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.COLLECTOR_ROTATE.getModel(), ItemStack.EMPTY, 255, OverlayTexture.NO_OVERLAY, ms, vertexBuilder);
            Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.COLLECTOR_EMM.getModel(), ItemStack.EMPTY, 255, OverlayTexture.NO_OVERLAY, ms, vertexBuilder);
            buffer.endBatch();
        }
        modelViewStack.popPose();
        
        RenderSystem.applyModelViewMatrix();
	}
	
    @Override
    protected String getBarName()
    {
        return "Collector";
    }
    
    @Override
    public ResourceLocation getBackgroundImage()
    {
        return WitherUtils.loc("textures/gui/collector.png");
    }

    @Override
    protected Vector2i getBackgroundImageSize()
    {
        return new Vector2i(176, 131);
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
			return WitherUtils.loc("textures/gui/render_on.png");
		else
			return WitherUtils.loc("textures/gui/render_off.png");
	}
	public ResourceLocation getOverflowTexture()
	{
		if(menu.getBlockEntity().getOverflow())
			return WitherUtils.loc("textures/gui/overflow_on.png");
		else
			return WitherUtils.loc("textures/gui/overflow_off.png");
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		if(isHovering(26, 66, 12, 12, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ALWAYS_ACTIVE)
				menu.getBlockEntity().setRedstoneControl(RedstoneControl.ACTIVE_WITH_SIGNAL);
			else
				menu.getBlockEntity().setRedstoneControl(RedstoneControl.ALWAYS_ACTIVE);

			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else if(isHovering(10, 66, 12, 12, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getRender() == false)
				menu.getBlockEntity().setRender(true);
			else
				menu.getBlockEntity().setRender(false);

			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else if(isHovering(43, 66, 16, 10, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getOverflow())
				menu.getBlockEntity().setOverflow(false);
			else
				menu.getBlockEntity().setOverflow(true);

			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else
		{
			return super.mouseClicked(mouseX, mouseY, mouseButton);
		}
		return true;
	}
	
    public void renderSlotButtonToolTips(GuiGraphics gg, int mouseX, int mouseY)
    {
        List<Component> list = new ArrayList<>();
    	
        if(isHovering(26, 66, 12, 12, mouseX, mouseY))
        {
            Component appendRedSt = Component.literal("");
            
            if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ALWAYS_ACTIVE)
            	appendRedSt = Component.literal(ChatFormatting.GREEN + "Always");
            else if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ACTIVE_WITH_SIGNAL)
            	appendRedSt = Component.literal(ChatFormatting.RED + "withSignal");

            list.add(Component.translatable(ChatFormatting.GRAY + "Redstone Signal: ").append(appendRedSt));
        }
        else if(isHovering(68, 30, 16, 16, mouseX, mouseY))
        {
            if(!menu.getSlot(0).hasItem())
            {
            	list.add(Component.translatable(ChatFormatting.GRAY + "Filter Card: ").append(Component.translatable(ChatFormatting.RED + "not found")));
            }
        }
        else if(isHovering(10, 66, 12, 12, mouseX, mouseY))
        {
            Component appendRender = Component.literal("");
            
            if(menu.getBlockEntity().getRender() == true)
            	appendRender = Component.literal(ChatFormatting.GREEN + "Show");
            else
            	appendRender = Component.literal(ChatFormatting.RED + "Hide");

            list.add(Component.translatable(ChatFormatting.GRAY + "Preview Range: ").append(appendRender));
        }
        else if(isHovering(43, 66, 12, 12, mouseX, mouseY))
        {
            Component appendOverflow = Component.literal("");
            
            if(menu.getBlockEntity().getOverflow() == true)
            	appendOverflow = Component.literal(ChatFormatting.GREEN + "Destroy");
            else
            	appendOverflow = Component.literal(ChatFormatting.RED + "DoNothing");

            list.add(Component.translatable(ChatFormatting.GRAY + "Overflow: ").append(appendOverflow));
        }
        gg.renderComponentTooltip(font, list, mouseX, mouseY); 
    }
}
