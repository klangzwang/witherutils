package geni.witherutils.base.common.entity.soulorb;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.api.WitherUtilsRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SoulOrbRenderer extends EntityRenderer<SoulOrb> {

	private static final ResourceLocation SOULORB = WitherUtilsRegistry.loc("textures/block/model/entity/soulorb.png");
	private static final RenderType RENDER_TYPE = RenderType.itemEntityTranslucentCull(SOULORB);

	public SoulOrbRenderer(EntityRendererProvider.Context context)
	{
		super(context);
		this.shadowRadius = 0.15F;
		this.shadowStrength = 0.75F;
	}

	protected int getBlockLightLevel(SoulOrb p_114606_, BlockPos p_114607_)
	{
		return Mth.clamp(super.getBlockLightLevel(p_114606_, p_114607_) + 7, 0, 15);
	}

	public void render(SoulOrb soulorb, float p_114600_, float partialTicks, PoseStack matrix, MultiBufferSource buffer, int p_114604_)
	{
		matrix.pushPose();

		int i = soulorb.getIcon();

		float f = (float) (i % 4 * 16) / 64.0F;
		float f1 = (float) (i % 4 * 16 + 16) / 64.0F;
		float f2 = (float) (i / 4 * 16) / 64.0F;
		float f3 = (float) (i / 4 * 16 + 16) / 64.0F;

		float f8 = ((float) soulorb.tickCount + partialTicks) / 2.0F;

		matrix.translate(0.0D, 0.1F, 0.0D);
		matrix.mulPose(this.entityRenderDispatcher.cameraOrientation());
		matrix.mulPose(Axis.YP.rotationDegrees(180.0F));

		int scale = (int) ((Mth.sin(f8 + 0.0F) + 1.0F) * 0.5F * 1.0F);
		matrix.scale(scale, scale, scale);

		VertexConsumer vertexconsumer = buffer.getBuffer(RENDER_TYPE);
		PoseStack.Pose posestack$pose = matrix.last();

		Matrix4f matrix4f = posestack$pose.pose();
		Matrix3f matrix3f = posestack$pose.normal();

		int j = 128;
		int k = 128;
		int l = 128;

		vertex(vertexconsumer, matrix4f, matrix3f, -0.5F, -0.25F, j, k, l, f, f3, p_114604_);
		vertex(vertexconsumer, matrix4f, matrix3f, 0.5F, -0.25F, j, k, l, f1, f3, p_114604_);
		vertex(vertexconsumer, matrix4f, matrix3f, 0.5F, 0.75F, j, k, l, f1, f2, p_114604_);
		vertex(vertexconsumer, matrix4f, matrix3f, -0.5F, 0.75F, j, k, l, f, f2, p_114604_);

		matrix.popPose();

		super.render(soulorb, p_114600_, partialTicks, matrix, buffer, p_114604_);
	}

	private static void vertex(VertexConsumer vertexconsumer, Matrix4f matrix4f, Matrix3f matrix3f, float x, float y, int r, int g, int b, float u, float v, int lightcolor)
	{
		vertexconsumer.addVertex(matrix4f, x, y, 0.0F).setColor(r, g, b, 255).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightcolor).setNormal(0.0F, 1.0F, 0.0F);
	}

	public ResourceLocation getTextureLocation(SoulOrb p_114597_)
	{
		return SOULORB;
	}
}