package geni.witherutils.base.common.block.miner.advanced;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.type.WUTRenderType;
import geni.witherutils.base.common.base.AbstractBlockEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.AABB;

public class MinerAdvancedRenderer extends AbstractBlockEntityRenderer<MinerAdvancedBlockEntity> {

    public MinerAdvancedRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(MinerAdvancedBlockEntity te, float partialTick, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight)
    {
        if (te.getLevel() == null)
            return;

        matrix.pushPose();
    	float rotation = te.prevFanRotation + (te.fanRotation - te.prevFanRotation) * partialTick;
    	renderSpecialFacingModel(SpecialModels.WHEEL.getModel(), ItemDisplayContext.NONE, false, matrix, buffer, -1, light, OverlayTexture.NO_OVERLAY, WUTRenderType.solid(), te.getCurrentFacing(), rotation, 0.15f);
    	renderSpecialFacingModel(SpecialModels.EMMINER.getModel(), ItemDisplayContext.NONE, false, matrix, buffer, -1, light, OverlayTexture.NO_OVERLAY, WUTRenderType.eyes(EMISSIVE), te.getCurrentFacing(), rotation, 0.15f);
    	renderSpecialFacingModel(SpecialModels.GLASSMINER.getModel(), ItemDisplayContext.NONE, false, matrix, buffer, -1, light, OverlayTexture.NO_OVERLAY, WUTRenderType.translucent(), te.getCurrentFacing());
    	matrix.popPose();

		if(te.getRender())
			renderPreview(te, partialTick, matrix, buffer, light, overlayLight);
    }
    
	public void renderPreview(MinerAdvancedBlockEntity tile, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlaylight)
	{
		if(!tile.getRender())
			return;
		
		RenderSystem.lineWidth(Math.max(2.5F, (float) Minecraft.getInstance().getWindow().getWidth() / 1920.0F * 2.5F));
        BlockPos blockpos = tile.getBlockPos();
        Color color = new Color(Math.abs(blockpos.getX() % 255), Math.abs(blockpos.getY() % 255), Math.abs(blockpos.getZ() % 255));
        
		matrixStack.translate(-0.0005D, -0.0005D, -0.0005D);
		matrixStack.scale(0.999F, 0.999F, 0.999F);

		VertexConsumer ivertexbuilder = buffer.getBuffer(RenderType.lines());
		matrixStack.translate(-0.0005D, -0.0005D, -0.0005D);
		matrixStack.scale(0.999F, 0.999F, 0.999F);
		AABB thebox = new AABB(0,0,0,0,0,0);

		matrixStack.pushPose();
		if(tile.getCurrentFacing() == Direction.NORTH)
		{
			thebox = new AABB(0,0,-tile.getRange(),1,1,0);
		}
		matrixStack.popPose();

		matrixStack.pushPose();
		if(tile.getCurrentFacing() == Direction.SOUTH)
		{
			thebox = new AABB(0,0,tile.getRange()+1,1,1,1);
		}
		matrixStack.popPose();

		matrixStack.pushPose();
		if(tile.getCurrentFacing() == Direction.EAST)
		{
			thebox = new AABB(tile.getRange()+1,0,0,1,1,1);
		}
		matrixStack.popPose();

		matrixStack.pushPose();
		if(tile.getCurrentFacing() == Direction.WEST)
		{
			thebox = new AABB(-tile.getRange(),0,0,0,1,1);
		}
		matrixStack.popPose();

		matrixStack.pushPose();
		if(tile.getCurrentFacing() == Direction.UP)
		{
			thebox = new AABB(0,tile.getRange()+1,0,1,1,1);
		}
		matrixStack.popPose();

		matrixStack.pushPose();
		if(tile.getCurrentFacing() == Direction.DOWN)
		{
			thebox = new AABB(0,-tile.getRange(),0,1,0,1);
		}
		matrixStack.popPose();

		LevelRenderer.renderLineBox(matrixStack, ivertexbuilder, thebox, (float) color.getRed() / 0f, (float) color.getGreen() / 255f, (float) color.getBlue() / 255f, 0.5F);
	}
}
