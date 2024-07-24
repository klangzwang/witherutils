package geni.witherutils.base.common.entity.soulorb;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.api.WitherUtilsRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

@SuppressWarnings("rawtypes")
public class SoulOrbProjectileRenderer extends ThrownItemRenderer {
	
	private static final ResourceLocation SOULORB = WitherUtilsRegistry.loc("textures/block/model/entity/soulorb.png");
	private static final RenderType RENDER_TYPE = RenderType.itemEntityTranslucentCull(SOULORB);
	
	public SoulOrbProjectileRenderer(Context p_174414_)
	{
		super(p_174414_);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void render(Entity soulorb, float vone, float partialTicks, PoseStack matrix, MultiBufferSource bufferIn, int p_114604_)
	{
		matrix.pushPose();

		int i = RandomSource.create().nextInt(10);

		float f = (float) (i % 4 * 16 + 0) / 64.0F;
		float f1 = (float) (i % 4 * 16 + 16) / 64.0F;
		float f2 = (float) (i / 4 * 16 + 0) / 64.0F;
		float f3 = (float) (i / 4 * 16 + 16) / 64.0F;

		matrix.translate(0.0D, -0.2F, 0.0D);
		matrix.mulPose(this.entityRenderDispatcher.cameraOrientation());
		matrix.mulPose(Axis.YP.rotationDegrees(180.0F));

		int alpha = 255;
		if(soulorb instanceof SoulOrbProjectile orb)
		{
			alpha = orb.getAge();
			float f8 = ((float) orb.getAge() + partialTicks) / 255.0F;
			float scale = ((Mth.sin(f8 + 0.0F) + 1.0F) * 0.5F * 1.0F);
			matrix.scale(scale, scale, scale);
		}

		VertexConsumer vertexconsumer = bufferIn.getBuffer(RENDER_TYPE);
		PoseStack.Pose posestack$pose = matrix.last();

		Matrix4f matrix4f = posestack$pose.pose();
		Matrix3f matrix3f = posestack$pose.normal();

		int j = 255;
		int k = 255;
		int l = 255;

		vertex(vertexconsumer, matrix4f, matrix3f, -0.5F, -0.25F, j, k, l, f, f3, p_114604_, alpha);
		vertex(vertexconsumer, matrix4f, matrix3f, 0.5F, -0.25F, j, k, l, f1, f3, p_114604_, alpha);
		vertex(vertexconsumer, matrix4f, matrix3f, 0.5F, 0.75F, j, k, l, f1, f2, p_114604_, alpha);
		vertex(vertexconsumer, matrix4f, matrix3f, -0.5F, 0.75F, j, k, l, f, f2, p_114604_, alpha);

		matrix.popPose();

		super.render(soulorb, vone, partialTicks, matrix, bufferIn, p_114604_);
	}
	
	private static void vertex(VertexConsumer vertexconsumer, Matrix4f matrix4f, Matrix3f matrix3f, float x, float y, int r, int g, int b, float u, float v, int lightcolor, int alpha)
	{
		vertexconsumer.addVertex(matrix4f, x, y, 0.0F).setColor(r, g, b, alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightcolor).setNormal(0.0F, 1.0F, 0.0F);
	}
}
