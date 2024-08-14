package geni.witherutils.base.common.entity.cursed.skeleton;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import geni.witherutils.api.WitherUtilsRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CursedSkeletonEyesLayer extends RenderLayer<CursedSkeleton, CursedSkeletonModel<CursedSkeleton>> {

	private static final RenderType SKELETON_EYES = RenderType.eyes(WitherUtilsRegistry.loc("textures/model/entity/cursed/skeleton_eyes.png"));

	public CursedSkeletonEyesLayer(RenderLayerParent<CursedSkeleton, CursedSkeletonModel<CursedSkeleton>> layer)
	{
		super(layer);
	}
	@Override
	public void render(PoseStack p_117349_, MultiBufferSource p_117350_, int p_117351_, CursedSkeleton p_117352_, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_)
	{
		VertexConsumer vertexconsumer = p_117350_.getBuffer(this.renderType());
		this.getParentModel().renderToBuffer(p_117349_, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY);
	}
	public RenderType renderType()
	{
		return SKELETON_EYES;
	}
}