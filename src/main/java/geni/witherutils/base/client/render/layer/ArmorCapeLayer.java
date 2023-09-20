package geni.witherutils.base.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;

@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class ArmorCapeLayer extends RenderLayer {

	private RenderLayerParent parent;

	public ArmorCapeLayer(RenderLayerParent parent)
    {
        super(parent);
        this.parent = parent;
    }

    @Override
    public void render(PoseStack matrix, MultiBufferSource buffer, int packedLightIn, Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) 
    {
//		matrix.pushPose();
//		matrix.translate(0.0D, 0.0D, 0.125D);
//
//		double d0 = Mth.lerp((double) ageInTicks, entity.xo, entity.xOld) - Mth.lerp((double) ageInTicks, entity.xo, entity.getX());
//		double d1 = Mth.lerp((double) ageInTicks, entity.yo, entity.yOld) - Mth.lerp((double) ageInTicks, entity.yo, entity.getY());
//		double d2 = Mth.lerp((double) ageInTicks, entity.zo, entity.zOld) - Mth.lerp((double) ageInTicks, entity.zo, entity.getZ());
//
//		float f = entity.yRotO + (entity.yRot - entity.yRotO);
//		double d3 = (double) Mth.sin(f * ((float) Math.PI / 180F));
//		double d4 = (double) (-Mth.cos(f * ((float) Math.PI / 180F)));
//		float f1 = (float) d1 * 10.0F;
//		f1 = Mth.clamp(f1, -6.0F, 32.0F);
//		float f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
//		f2 = Mth.clamp(f2, 0.0F, 150.0F);
//		float f3 = (float) (d0 * d4 - d2 * d3) * 100.0F;
//		f3 = Mth.clamp(f3, -20.0F, 20.0F);
//
//		if (f2 < 0.0F)
//		{
//			f2 = 0.0F;
//		}

//		float f4 = Mth.lerp(ageInTicks, entity.oBob, monk.bob);
//		f1 += Mth.sin(Mth.lerp(ageInTicks, entity.walkDistO, entity.walkDist) * 6.0F) * 32.0F; // * f4;

//		matrix.mulPose(Vector3f.XP.rotationDegrees(6.0F + f2 / 2.0F + f1));
//		matrix.mulPose(Vector3f.ZP.rotationDegrees(f3 / 2.0F));
//		matrix.mulPose(Vector3f.YP.rotationDegrees(180.0F - f3 / 2.0F));

//		VertexConsumer vertexconsumer = buffer.getBuffer(WURenderType.COLLECTOR_PORTAL);
//		this.getParentModel().renderCloak(matrix, vertexconsumer, buffer, OverlayTexture.NO_OVERLAY);
//		this.getParentModel().renderToBuffer(matrix, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		
//		matrix.popPose();
	}
}