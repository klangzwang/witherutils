package geni.witherutils.base.common.entity.portal;

import org.joml.Quaternionf;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.client.render.EffectLib;
import geni.witherutils.core.common.math.Vector3;
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

    private static final RandomSource rand = RandomSource.create();
    @SuppressWarnings("unused")
	private static final float[] randSet = new float[4096];
	public static final ResourceLocation TEXTURE = WitherUtilsRegistry.loc("textures/block/portal.png");

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
		double yOffset = gate.getBbHeight() / 2 + 0.5;

		matrix.translate(0, yOffset, 0);
        matrix.mulPose(new Quaternionf().rotationAxis(Mth.DEG_TO_RAD * 90, 0, 1, 0));
        matrix.mulPose(new Quaternionf().rotationAxis(Mth.DEG_TO_RAD * (180F - (float) angleOf(portal, playerV)), 0, 1, 0));
		matrix.scale(2, 1, 1);

		float time = partialTicks;
		int magic = 10;
		if (time < magic) {
			matrix.scale(Mth.lerp(time / magic, 1, 1.33F), 1, 1);
			matrix.scale(1, Mth.lerp(time / magic, 1, 1.33F), 1);
		} else if (time < 2 * magic) {
			time -= magic;
			matrix.scale(Mth.lerp(time / magic, 1.33F, 1F), 1, 1);
			matrix.scale(1, Mth.lerp(time / magic, 1.33F, 1F), 1);
		} else {
			float progress = (partialTicks % 80) / 80F;
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
		builder.addVertex(matrix.last().pose(), -1, -1, 0).setColor(r, g, b, 255).setUv(1, 1 - frame * frameHeight).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0, 1, 0);
		builder.addVertex(matrix.last().pose(), -1, 1, 0).setColor(r, g, b, 255).setUv(1, 8F / 9 - frame * frameHeight).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0, 1, 0);
		builder.addVertex(matrix.last().pose(), 1, 1, 0).setColor(r, g, b, 255).setUv(0, 8F / 9 - frame * frameHeight).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0, 1, 0);
		builder.addVertex(matrix.last().pose(), 1, -1, 0).setColor(r, g, b, 255).setUv(0, 1 - frame * frameHeight).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0, 1, 0);

		matrix.popPose();
        renderEffects(gate, yaw, partialTicks, matrix, buf, packedLight, 1);
	}

	public static double angleOf(Vec3 p1, Vec3 p2)
	{
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
        
		EffectLib.renderLightningP2PRotate(mStack, getter, posi2, posi1, 16, (long) partialTicks, 0.06F, 0.04F, false, 0, 0x6300BD);
		
        if(partialTicks % 5 == 0)
        {
            int pos = rand.nextInt(4);
            for(i = 0; i < 4; i++)
            {
                if (i != pos) continue;
                float loopOffset = ((i / 4F) * ((float) Math.PI * 2F)) + (partialTicks / 100F);
                float rot = ((7 / 64F) * (float) Math.PI * 2F) + (partialTicks / 10F) + loopOffset;
                double x = Mth.sin(rot) * 2;
                double z = Mth.cos(rot) * 2;
                double y = Mth.cos(rot + loopOffset) * 1;
                EffectLib.renderLightningP2PRotate(mStack, getter, new Vector3(x, y, z), Vector3.ZERO, 8, (long) ((partialTicks) / 12), 0.06F, 0.04F, false, 0, 0x6300BD);
            }
        }
    }
}
