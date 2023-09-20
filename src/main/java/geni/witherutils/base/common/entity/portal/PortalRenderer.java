package geni.witherutils.base.common.entity.portal;

import org.joml.Quaternionf;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.client.render.EffectLib;
import geni.witherutils.core.common.math.Vector3;
import geni.witherutils.core.common.util.McTimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class PortalRenderer extends EntityRenderer<Portal> {

    private static RandomSource rand = RandomSource.create();
    @SuppressWarnings("unused")
	private static float[] randSet = new float[4096];
	public static final ResourceLocation TEXTURE = new ResourceLocation(WitherUtils.MODID, "textures/model/entity/portal.png");

	public PortalRenderer(EntityRendererProvider.Context mgr)
	{
		super(mgr);
	}

	@Override
	public ResourceLocation getTextureLocation(Portal entity)
	{
		return TEXTURE;
	}

	@SuppressWarnings("resource")
	@Override
	public void render(Portal gate, float yaw, float partialTicks, PoseStack matrix, MultiBufferSource buf, int packedLight)
	{
		matrix.pushPose();
		Player player = Minecraft.getInstance().player;
		Vec3 playerV = player.getEyePosition(partialTicks);
		Vec3 portal = gate.position();

		float baseScale = 1F;
		float scale = baseScale;
		double yOffset = gate.getBbHeight() / 2;

		matrix.translate(0, yOffset, 0);
		matrix.mulPose(new Quaternionf().rotateXYZ(0, 90, 0));
		matrix.mulPose(new Quaternionf().rotateYXZ(0, 180F - (float) angleOf(portal, playerV), 0));
		matrix.scale(2, 1, 1);

		float time = McTimerUtil.clientTimer + partialTicks;
		int magic = 10;
		if (time < magic) {
			matrix.scale(Mth.lerp(time / magic, 1, 1.33F), 1, 1);
			matrix.scale(1, Mth.lerp(time / magic, 1, 1.33F), 1);
		} else if (time < 2 * magic) {
			time -= magic;
			matrix.scale(Mth.lerp(time / magic, 1.33F, 1F), 1, 1);
			matrix.scale(1, Mth.lerp(time / magic, 1.33F, 1F), 1);
		} else {
			float progress = ((McTimerUtil.clientTimer + partialTicks - 20) % 80) / 80F;
			scale += (float) Math.sin(2 * Math.PI * progress) * baseScale / 6F;
		}

		matrix.scale(scale, scale, 1);

		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, this.getTextureLocation(gate));
		
		VertexConsumer builder = buf.getBuffer(RenderType.entityCutout(getTextureLocation(gate)));
		int r = 200 >> 16 & 255, g = 200 >> 8 & 255, b = 200 & 155;
		float frameHeight = 1 / 9F;
		int frame = gate.tickCount % 9;
		builder.vertex(matrix.last().pose(), -1, -1, 0).color(r, g, b, 255).uv(1, 1 - frame * frameHeight).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(matrix.last().normal(), 0, 1, 0).endVertex();
		builder.vertex(matrix.last().pose(), -1, 1, 0).color(r, g, b, 255).uv(1, 8F / 9 - frame * frameHeight).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(matrix.last().normal(), 0, 1, 0).endVertex();
		builder.vertex(matrix.last().pose(), 1, 1, 0).color(r, g, b, 255).uv(0, 8F / 9 - frame * frameHeight).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(matrix.last().normal(), 0, 1, 0).endVertex();
		builder.vertex(matrix.last().pose(), 1, -1, 0).color(r, g, b, 255).uv(0, 1 - frame * frameHeight).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(matrix.last().normal(), 0, 1, 0).endVertex();

		matrix.popPose();
        renderEffects(gate, yaw, partialTicks, matrix, buf, packedLight, 1);
	}

	public static double angleOf(Vec3 p1, Vec3 p2) {
		final double deltaY = p2.z - p1.z;
		final double deltaX = p2.x - p1.x;
		final double result = Math.toDegrees(Math.atan2(deltaY, deltaX));
		return result < 0 ? 360d + result : result;
	}
	
    private void renderEffects(Portal gate, float yaw, float partialTicks, PoseStack mStack, MultiBufferSource getter, int packedLight, int packetOverlay)
    {
        mStack.translate(0.0, 1.5, 0.0);
        int i = 0;
        final Vector3 posi1;
        final Vector3 posi2;
        posi1 = new Vector3(gate.getBlockX(), gate.getBlockY() + 2, gate.getBlockZ());
        posi2 = new Vector3(gate.getBlockX(), gate.getBlockY() + 2, gate.getBlockZ());
        
		EffectLib.renderLightningP2PRotate(mStack, getter, posi2, posi1, 16, (McTimerUtil.clientTimer / 2), 0.06F, 0.04F, false, 0, 0x6300BD);
		
        if(McTimerUtil.clientTimer % 5 == 0)
        {
            int pos = rand.nextInt(4);
            for(i = 0; i < 4; i++)
            {
                if (i != pos) continue;
                float loopOffset = ((i / 4F) * ((float) Math.PI * 2F)) + (McTimerUtil.clientTimer / 100F);
                float rot = ((7 / 64F) * (float) Math.PI * 2F) + (McTimerUtil.clientTimer / 10F) + loopOffset;
                double x = Mth.sin(rot) * 2;
                double z = Mth.cos(rot) * 2;
                double y = Mth.cos(rot + loopOffset) * 1;
                EffectLib.renderLightningP2PRotate(mStack, getter, new Vector3(x, y, z), Vector3.ZERO, 8, ((McTimerUtil.clientTimer) / 12), 0.06F, 0.04F, false, 0, 0x6300BD);
            }
        }
    }
}
