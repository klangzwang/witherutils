package geni.witherutils.base.common.block.spawner;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4fStack;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.api.misc.RedstoneControl;
import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.type.WUTRenderType;
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
import net.minecraft.world.item.ItemStack;

public class SpawnerScreen extends WUTScreen<SpawnerContainer> {
    
    public SpawnerScreen(SpawnerContainer pMenu, Inventory pPlayerInventory, Component pTitle)
    {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init()
    {
        super.init();
        addRenderableOnly(new EnergyWidget(this, this.getMenu(), this.font, ()-> getMenu().getBlockEntity().getEnergyHandler(null), leftPos + 8, topPos + 23, 16, 40));
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(gg, mouseX, mouseY, partialTicks);
        super.render(gg, mouseX, mouseY, partialTicks);
        this.renderTooltip(gg, mouseX, mouseY);
        
		renderAround(gg.pose(), mouseX, mouseY, partialTicks);
        
    	gg.blit(getRedstoneTexture(), this.leftPos + 155, this.topPos + 26, 0, 0, 12, 12, 12, 12);
    	gg.blit(getPreviewTexture(), this.leftPos + 155, this.topPos + 60, 0, 0, 12, 12, 12, 12);
    	
    	renderSlotButtonToolTips(gg, mouseX, mouseY);
    }
    
	public ResourceLocation getRedstoneTexture()
	{
		if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ALWAYS_ACTIVE)
			return WitherUtilsRegistry.loc("textures/gui/redstone_active.png");
		else
			return WitherUtilsRegistry.loc("textures/gui/redstone_signal.png");
	}
	
	public ResourceLocation getPreviewTexture()
	{
		if(menu.getBlockEntity().getShowSpawnerPos() == true)
			return WitherUtilsRegistry.loc("textures/gui/showpos_on.png");
		else
			return WitherUtilsRegistry.loc("textures/gui/showpos_off.png");
	}
	
	@SuppressWarnings("resource")
	public void renderAround(PoseStack ms, int mouseX, int mouseY, float partialTicks)
	{
        partialTicks = Minecraft.getInstance().getFps();
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        
        int startX = this.leftPos;
        int startY = this.topPos;
        
    	float x = (float) Vector3.CENTER.x - 0.5F;
    	float y = (float) Vector3.CENTER.y - 0.5F;
    	float z = (float) Vector3.CENTER.z - 0.5F;
      
    	modelViewStack.translate(x, y, z);

        modelViewStack.pushMatrix();
        {
            modelViewStack.translate(startX + 78, startY + 70, 100);
            modelViewStack.scale(38F, -38F, 38F);
            
        	modelViewStack.translate(0.5f, 0.5f, 0.5f);
    		float rotation = (Minecraft.getInstance().player.tickCount * 8 + partialTicks) / 5F;
        	modelViewStack.rotate(Axis.YP.rotationDegrees(rotation));
        	modelViewStack.translate(-0.5f, -0.5f, -0.5f);
            
            RenderSystem.applyModelViewMatrix();
        	RenderSystem.enableCull();
        	
            BlockRenderDispatcher rendererDispatcher = Minecraft.getInstance().getBlockRenderer();
            BakedModel model = rendererDispatcher.getBlockModelShaper().getBlockModel(WUTBlocks.SPAWNER.get().defaultBlockState());
            
            RenderSystem.applyModelViewMatrix();
            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer vertexBuilder = buffer.getBuffer(WUTRenderType.cutout());
            Minecraft.getInstance().getItemRenderer().renderModelLists(model, ItemStack.EMPTY, 255, OverlayTexture.NO_OVERLAY, ms, vertexBuilder);
            buffer.endBatch();
        }
        modelViewStack.popMatrix();
        
        modelViewStack.pushMatrix();
        {
            modelViewStack.translate(startX + 78, startY + 70, 100);
            modelViewStack.scale(38F, -38F, 38F);
            
        	modelViewStack.translate(0.5f, 0.5f, 0.5f);
    		float rotation = (Minecraft.getInstance().player.tickCount * 8 + partialTicks) / 5F;
        	modelViewStack.rotate(Axis.YP.rotationDegrees(rotation));
        	modelViewStack.translate(-0.5f, -0.5f, -0.5f);

            RenderSystem.applyModelViewMatrix();
            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer vertexBuilder = buffer.getBuffer(WUTRenderType.translucent());
            Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.EMSPAWNER.getModel(), ItemStack.EMPTY, 255, OverlayTexture.NO_OVERLAY, ms, vertexBuilder);
            buffer.endBatch();
        }
        modelViewStack.popMatrix();
        
        RenderSystem.applyModelViewMatrix();
	}
	
    @Override
    protected String getBarName()
    {
        return "Spawner";
    }
    
    @Override
    public ResourceLocation getBackgroundImage()
    {
        return WitherUtilsRegistry.loc("textures/gui/spawner.png");
    }

    @Override
    protected Vector2i getBackgroundImageSize()
    {
        return new Vector2i(176, 131);
    }
    
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		if(isHovering(155, 26, 12, 12, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ALWAYS_ACTIVE)
				menu.getBlockEntity().setRedstoneControl(RedstoneControl.ACTIVE_WITH_SIGNAL);
			else
				menu.getBlockEntity().setRedstoneControl(RedstoneControl.ALWAYS_ACTIVE);

			this.playDownSound(Minecraft.getInstance().getSoundManager());
		}
		else if(isHovering(155, 60, 16, 10, mouseX, mouseY))
		{
			if(menu.getBlockEntity().getShowSpawnerPos())
				menu.getBlockEntity().setShowSpawnerPos(false);
			else
				menu.getBlockEntity().setShowSpawnerPos(true);

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

        if(isHovering(155, 23, 12, 12, mouseX, mouseY))
        {
            Component appendRedSt = Component.literal("");
            
            if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ALWAYS_ACTIVE)
            	appendRedSt = Component.literal(ChatFormatting.GREEN + "Always");
            else if(menu.getBlockEntity().getRedstoneControl() == RedstoneControl.ACTIVE_WITH_SIGNAL)
            	appendRedSt = Component.literal(ChatFormatting.RED + "withSignal");

            list.add(Component.translatable(ChatFormatting.GRAY + "Redstone Signal: ").append(appendRedSt));
        }
        else if(isHovering(155, 42, 12, 12, mouseX, mouseY))
        {
            if(!menu.getSlot(1).hasItem())
            {
            	list.add(Component.translatable(ChatFormatting.GRAY + "Filter: ").append(Component.literal("No Card found!")));
            }
        }
        else if(isHovering(155, 60, 12, 12, mouseX, mouseY))
        {
            Component appendAutofeed = Component.literal("");
            
            if(menu.getBlockEntity().getShowSpawnerPos() == true)
            	appendAutofeed = Component.literal(ChatFormatting.GREEN + "Enabled");
            else
            	appendAutofeed = Component.literal(ChatFormatting.RED + "Disabled");

            list.add(Component.translatable(ChatFormatting.GRAY + "Preview: ").append(appendAutofeed));
        }
        gg.renderComponentTooltip(font, list, mouseX, mouseY); 
    }
}
