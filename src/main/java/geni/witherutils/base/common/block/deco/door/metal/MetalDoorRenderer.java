package geni.witherutils.base.common.block.deco.door.metal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.type.WUTRenderType;
import geni.witherutils.base.common.base.AbstractBlockEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Blocks;

public class MetalDoorRenderer extends AbstractBlockEntityRenderer<MetalDoorBlockEntity> {

    public MetalDoorRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(MetalDoorBlockEntity tile, float partialTick, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight)
    {
		if(tile == null || !tile.hasLevel())
			return;

		BlockPos pos = tile.getBlockPos();
		if(level.getBlockState(pos).getBlock() == Blocks.AIR)
			return;

		light = LevelRenderer.getLightColor(level, pos.above(255));

		renderDoorLower(tile, partialTick, matrix, buffer, light, overlayLight);
		renderDoorUpper(tile, partialTick, matrix, buffer, light, overlayLight);
    }
    
	public void renderDoorLower(MetalDoorBlockEntity te, float partialTicks, PoseStack matrix, MultiBufferSource getter, int light, int overlayLight)
	{
        if (te.getLevel() == null)
            return;

        Direction facing = te.getCurrentFacing();
        
    	matrix.pushPose();

    	matrix.translate(0.0F, -0.05f -te.getSlideProgress(partialTicks) * 1.2, 0.0F);
      
    	if(te.isDoorWideOpen())
    		renderSpecialFacingModel(SpecialModels.METALDOORTEETH.getModel(), ItemDisplayContext.NONE, false, matrix, getter, -1, light, OverlayTexture.NO_OVERLAY, WUTRenderType.cutout(), facing);
    	else
    		renderSpecialFacingModel(SpecialModels.METALDOOR.getModel(), ItemDisplayContext.NONE, false, matrix, getter, -1, light, OverlayTexture.NO_OVERLAY, WUTRenderType.cutout(), facing);
    	
    	matrix.popPose();
	}
	
	public void renderDoorUpper(MetalDoorBlockEntity te, float partialTicks, PoseStack matrix, MultiBufferSource getter, int light, int overlayLight)
	{
        if (te.getLevel() == null)
            return;

        Direction facing = te.getCurrentFacing();
        
    	matrix.pushPose();

    	matrix.translate(0.0F, 1.05F + te.getSlideProgress(partialTicks) * 1.2, 0.0F);

    	matrix.translate(0.5F, 0.5F, 0.5F);
    	matrix.mulPose(Axis.XP.rotationDegrees(180));
    	matrix.translate(-0.5F, -0.5F, -0.5F);
    	
    	if(te.isDoorWideOpen())
    	{
    		renderSpecialFacingModel(SpecialModels.METALDOORTEETH.getModel(), ItemDisplayContext.NONE, false, matrix, getter, -1, light, OverlayTexture.NO_OVERLAY, WUTRenderType.cutout(), facing);
    	}
    	else
    	{
    		renderSpecialFacingModel(SpecialModels.METALDOOR.getModel(), ItemDisplayContext.NONE, false, matrix, getter, -1, light, OverlayTexture.NO_OVERLAY, WUTRenderType.cutout(), facing);
    	}
    	
    	matrix.popPose();
	}
}
