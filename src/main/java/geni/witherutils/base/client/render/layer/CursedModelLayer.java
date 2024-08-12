package geni.witherutils.base.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.type.WUTRenderType;
import geni.witherutils.core.common.math.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
public class CursedModelLayer extends RenderLayer {
	
	private final RenderLayerParent parent;
	
	public CursedModelLayer(RenderLayerParent parent)
    {
        super(parent);
        this.parent = parent;
    }

    @Override
    public void render(PoseStack matrix, MultiBufferSource buffer, int light, Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) 
    {
//        if(entity instanceof CursedDryHead)
//        {
//        	CursedDryHead dryHead = (CursedDryHead) entity;
//    		VertexConsumer vertex = buffer.getBuffer(WUTRenderType.cutout());
//
//    		if (!dryHead.isResting())
//    		{
//    			matrix.mulPose(Axis.XP.rotationDegrees(180F));
//    			matrix.mulPose(Axis.YP.rotationDegrees(180F));
//    		}
//    		else
//    		{
//    			matrix.mulPose(Axis.XP.rotationDegrees(((float)Math.PI / 4F) + Mth.cos(partialTicks * 0.1F) * 0.15F));
//    			matrix.mulPose(Axis.YP.rotationDegrees(0.0F));
//    		}
//    		renderHead(dryHead, matrix, buffer, light, netHeadYaw, headPitch, vertex);
//        }
    }

//    public void renderHead(CursedDryHead dryHead, PoseStack matrix, MultiBufferSource buffer, int light, float netHeadYaw, float headPitch, VertexConsumer vertex)
//    {
//		matrix.pushPose();
//		
//        double x = Vector3.CENTER.x - 1.0F;
//        double y = Vector3.CENTER.y - 1.0F;
//        double z = Vector3.CENTER.z - 1.0F;
//
//        matrix.translate(x, y + 1, z);
//		
//		matrix.mulPose(Axis.XP.rotationDegrees(headPitch *  ((float)Math.PI / 180F)));
//		matrix.mulPose(Axis.YP.rotationDegrees(netHeadYaw * ((float)Math.PI / 180F)));
//		matrix.mulPose(Axis.ZP.rotationDegrees(0.0F));
//		matrix.translate(0.0F, 0.0F, 0.0F);
//		
//	    renderBakedModelLists(SpecialModels.DRYHEAD_HEAD.getModel(), matrix, vertex, light);
//	    renderBakedModelLists(SpecialModels.DRYHEAD_HAIR.getModel(), matrix, vertex, light);
//	    
//		matrix.popPose();
//    }
//
//    public void renderBakedModelLists(BakedModel model, PoseStack matrix, VertexConsumer vertex, int light)
//    {
//		Minecraft.getInstance().getItemRenderer().renderModelLists(model, ItemStack.EMPTY, light, OverlayTexture.NO_OVERLAY, matrix, vertex);
//    }
}
