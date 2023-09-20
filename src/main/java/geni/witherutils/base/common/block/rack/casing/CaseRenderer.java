package geni.witherutils.base.common.block.rack.casing;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.api.block.BStateProperties;
import geni.witherutils.api.block.MultiBlockState;
import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.type.WUTRenderType;
import geni.witherutils.base.common.base.AbstractBlockEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CaseRenderer extends AbstractBlockEntityRenderer<CaseBlockEntity> {
	
    public CaseRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(CaseBlockEntity te, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int combinedOverlayIn)
    {
        if (te.getLevel() == null)
            return;

        MultiBlockState mbstate = te.getBlockState().getValue(BStateProperties.MBSTATE);
        
        if(mbstate == MultiBlockState.YMAX_CENTER)
        {
        	renderCenterModel(te, Axis.XP, 0, false, partialTicks, matrixStackIn, bufferIn, light, combinedOverlayIn);
        }
        else if(mbstate == MultiBlockState.YMIN_CENTER)
        {
        	renderCenterModel(te, Axis.ZP, 180, false, partialTicks, matrixStackIn, bufferIn, light, combinedOverlayIn);
        }
        else if(mbstate == MultiBlockState.XMIN_CENTER)
        {
        	renderCenterModel(te, Axis.ZP, 90, true, partialTicks, matrixStackIn, bufferIn, light, combinedOverlayIn);
        }
        else if(mbstate == MultiBlockState.XMAX_CENTER)
        {
        	renderCenterModel(te, Axis.ZP, -90, true, partialTicks, matrixStackIn, bufferIn, light, combinedOverlayIn);
        }
        else if(mbstate == MultiBlockState.ZMIN_CENTER)
        {
        	renderCenterModel(te, Axis.XP, -90, false, partialTicks, matrixStackIn, bufferIn, light, combinedOverlayIn);
        }
        else if(mbstate == MultiBlockState.ZMAX_CENTER)
        {
        	renderCenterModel(te, Axis.XP, 90, false, partialTicks, matrixStackIn, bufferIn, light, combinedOverlayIn);
        }
        else if(mbstate == MultiBlockState.XMAX_ZMAX_YEDGE)
        {
        	renderLights(te, partialTicks, Axis.YP, 180, matrixStackIn, bufferIn, light, combinedOverlayIn);
        }
        else if(mbstate == MultiBlockState.XMAX_ZMIN_YEDGE)
        {
        	renderLights(te, partialTicks, Axis.YP, -90, matrixStackIn, bufferIn, light, combinedOverlayIn);
        }
        else if(mbstate == MultiBlockState.XMIN_ZMAX_YEDGE)
        {
        	renderLights(te, partialTicks, Axis.YP, 90, matrixStackIn, bufferIn, light, combinedOverlayIn);
        }
        else if(mbstate == MultiBlockState.XMIN_ZMIN_YEDGE)
        {
        	renderLights(te, partialTicks, Axis.YP, 0, matrixStackIn, bufferIn, light, combinedOverlayIn);
        }
    }
    
    public void renderCenterModel(CaseBlockEntity te, Axis axis, float degrees, boolean secondRotation, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int light, int combinedOverlayIn)
    {
        matrixStackIn.pushPose();
        
        matrixStackIn.translate(0.5, 0.5, 0.5);
    	matrixStackIn.mulPose(axis.rotationDegrees(degrees));
    	matrixStackIn.translate(-0.5, -0.5, -0.5);
    	
    	if(secondRotation)
    	{
            matrixStackIn.translate(0.5, 0.5, 0.5);
        	matrixStackIn.mulPose(Axis.YP.rotationDegrees(90));
        	matrixStackIn.translate(-0.5, -0.5, -0.5);
    	}
    	
        VertexConsumer vertexBuilder = bufferIn.getBuffer(RenderType.cutout());
        Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.MULTICENTER.getModel(), ItemStack.EMPTY, light, OverlayTexture.NO_OVERLAY, matrixStackIn, vertexBuilder);

        matrixStackIn.popPose();
    }
    
    public void renderLights(CaseBlockEntity te, float partialTick, Axis axis, float degrees, PoseStack matrixStackIn, MultiBufferSource bufferIn, int light, int combinedOverlayIn)
    {
		matrixStackIn.pushPose();

        matrixStackIn.translate(0.5, 0.5, 0.5);
    	matrixStackIn.mulPose(axis.rotationDegrees(degrees));
        matrixStackIn.translate(-0.5, -0.5, -0.5);
        
        float time = te.getLevel().getLevelData().getGameTime() + partialTick;
		double offset = Math.sin(time * 1 / 8.0D) / 10.0D;
        
		if(offset > 0)
			renderSpecialModel(SpecialModels.EMMULTI.getModel(), ItemDisplayContext.NONE, false, matrixStackIn, bufferIn, 0xFFFFFFFF, light, combinedOverlayIn, WUTRenderType.eyes(EMISSIVE));
		
    	matrixStackIn.popPose();
    }
}
