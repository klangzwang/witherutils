package geni.witherutils.base.common.block.battery.pylon;

import java.util.Arrays;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.client.render.type.WUTRenderType;
import geni.witherutils.base.common.base.AbstractBlockEntityRenderer;
import geni.witherutils.base.common.block.battery.pylon.PylonBlock.Mode;
import geni.witherutils.core.common.util.ModelRenderUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class PylonRenderer extends AbstractBlockEntityRenderer<PylonBlockEntity> {

	public static final ResourceLocation BEAM_TEXTURE = new ResourceLocation("witherutils:textures/block/tile/beam.png");
	private static final RenderType RENDER_TYPE = WUTRenderType.entityBlendedNoDept(BEAM_TEXTURE);
    
    public PylonRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

	@Override
    public void render(PylonBlockEntity te, float partialTicks, PoseStack matrix, MultiBufferSource rtb, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight)
    {
        if (te.getLevel() == null)
        	return;
        
    	float red, green, blue, alpha;
    	red = te.getiOMode() == Mode.INPUT ? 0.8f : 0.0f;
    	green = te.getiOMode() == Mode.INPUT ? 0.2f : 0.5f;
    	blue = te.getiOMode() == Mode.INPUT ? 0.0f : 0.8f;
    	alpha = 1.0f;
        
        if(te.core == null || te.coreOffset == BlockPos.ZERO)
        {
        	red = 1.0f;
        	green = 1.0f;
        	blue = 1.0f;
        	alpha = 1.0f;
        	renderCuboid(te, matrix, mc, red, green, blue, alpha);
        	renderTextAboveCamera(matrix, rtb, light, Component.literal(ChatFormatting.WHITE + "NO CORE FOUND"), 1.0f, mc);
        	return;        	
        }

        if (te.getBlockPos() != null && mc.hitResult instanceof BlockHitResult hitResult && te.getBlockPos().equals(hitResult.getBlockPos()))
        {
            matrix.pushPose();
            renderBeams(te, matrix, rtb, partialTicks);
            matrix.popPose();
        }

    	matrix.pushPose();
    	renderCuboid(te, matrix, mc, red, green, blue, alpha);
    	matrix.popPose();
    	
    	matrix.pushPose();
    	renderTextAboveCamera(matrix, rtb, light, Component.literal(ChatFormatting.WHITE + te.getiOMode().name()), 1.0f, mc);
    	matrix.popPose();
	}
	
    private void renderCuboid(PylonBlockEntity te, PoseStack poseStack, Minecraft mc, float red, float green, float blue, float alpha)
    {
    	for(Direction side : Direction.values())
    	{
        	poseStack.pushPose();

        	RenderSystem.enableDepthTest();

        	poseStack.scale(0.45f, 0.45f, 0.45f);
            poseStack.translate(0.6f, 0.6f, 0.6f);
            
            Direction facing = te.getBlockState().getValue(PylonBlock.FACING);
            if(facing != null)
            {
                if(facing == Direction.NORTH)
                {
                    poseStack.translate(0, 0, -0.15f);
                }
                else if(facing == Direction.SOUTH)
                {
                    poseStack.translate(0, 0, 0.15f);
                }
                else if(facing == Direction.WEST)
                {
                    poseStack.translate(-0.15f, 0, 0);
                }
                else if(facing == Direction.EAST)
                {
                    poseStack.translate(0.15f, 0, 0);
                }
                else if(facing == Direction.UP)
                {
                    poseStack.translate(0, 0.15f, 0);
                }
                else if(facing == Direction.DOWN)
                {
                    poseStack.translate(0, -0.15f, 0);
                }
            }
            
        	BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        	RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        	TextureAtlasSprite tex = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation(WitherUtils.MODID, "block/solar/solar_nocolor"));
        	RenderSystem.setShaderTexture(0, tex.atlasLocation());
        	RenderSystem.setShaderColor(red, green, blue, alpha);

        	bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);

        	Vector3f[] vec = Arrays.stream(ModelRenderUtil.createQuadVerts(side, 0, 1, 1)).map(Vec3::toVector3f).toArray(Vector3f[]::new);

        	Matrix4f matrix4f = poseStack.last().pose();

        	bufferbuilder.vertex(matrix4f, vec[0].x(), vec[0].y(), vec[0].z()).color(1F, 1F, 1F, 1F).uv(tex.getU0(), tex.getV0()).endVertex();
        	bufferbuilder.vertex(matrix4f, vec[1].x(), vec[1].y(), vec[1].z()).color(1F, 1F, 1F, 1F).uv(tex.getU0(), tex.getV1()).endVertex();
        	bufferbuilder.vertex(matrix4f, vec[2].x(), vec[2].y(), vec[2].z()).color(1F, 1F, 1F, 1F).uv(tex.getU1(), tex.getV1()).endVertex();
        	bufferbuilder.vertex(matrix4f, vec[3].x(), vec[3].y(), vec[3].z()).color(1F, 1F, 1F, 1F).uv(tex.getU1(), tex.getV0()).endVertex();
      	  
        	BufferUploader.drawWithShader(bufferbuilder.end());
        	
        	RenderSystem.setShaderColor(1, 1, 1, 1);
        	
        	poseStack.popPose();
    	}
    }
    
	private void renderBeams(PylonBlockEntity te, PoseStack matrix, MultiBufferSource getter, float partialTicks)
	{
        if (te.core == null)
            return;

		Vec3 posone = new Vec3(te.getBlockPos().getX(), te.getBlockPos().getY(), te.getBlockPos().getZ());
		Vec3 postwo = new Vec3(te.getBlockPos().getX() - te.coreOffset.getX(),
							   te.getBlockPos().getY() - te.coreOffset.getY(),
							   te.getBlockPos().getZ() - te.coreOffset.getZ());
		renderBeam(te, partialTicks, matrix, getter, posone, postwo);
	}
	public static void renderBeam(BlockEntity tile, float pt, PoseStack matrix, MultiBufferSource rtb, Vec3 posone, Vec3 postwo)
	{
		matrix.pushPose();
		matrix.translate(0.5D, 0.5D, 0.5D);

		float f2 = 1.0F;
		float f3 = f2 * 0.5F % 1.0F;
		Vec3 vec3d2 = posone.subtract(postwo);
		double d0 = vec3d2.length();
		vec3d2 = vec3d2.normalize();

		float f5 = (float) Math.acos(Mth.clamp(vec3d2.y, -1.0, 1.0));
		float f6 = (float) Mth.atan2(vec3d2.z, vec3d2.x);

		matrix.mulPose(Axis.YP.rotationDegrees((((float) Math.PI / 2F) - f6) * (180F / (float) Math.PI)));
		matrix.mulPose(Axis.XP.rotationDegrees(f5 * (180F / (float) Math.PI)));

		float d1 = f2 * 0.0F;

		float d12 = Mth.cos((float) (d1 + Math.PI)) * 0.12F;
		float d13 = Mth.sin((float) (d1 + Math.PI)) * 0.12F;
		float d14 = Mth.cos(d1) * 0.12F;
		float d15 = Mth.sin(d1) * 0.12F;

		float d16 = Mth.cos((float) (d1 + (Math.PI / 2D))) * 0.12F;
		float d17 = Mth.sin((float) (d1 + (Math.PI / 2D))) * 0.12F;
		float d18 = Mth.cos((float) (d1 + (Math.PI * 1.5D))) * 0.12F;
		float d19 = Mth.sin((float) (d1 + (Math.PI * 1.5D))) * 0.12F;

		float d22 = (f3 - 1.0F);
		float d23 = (float) (d0 * 5.05D + d22);
		VertexConsumer builder = rtb.getBuffer(RENDER_TYPE);
		PoseStack.Pose last = matrix.last();
		Matrix4f matrix4f = last.pose();
		Matrix3f matrix3f = last.normal();

		int r = 0xFF & (0 >> 16);
		int g = 0xFF & (255 >> 8);
		int b = 0xFF & 255;

		pos(builder, matrix4f, matrix3f, d12, 0.0F, d13, r, g, b, 1, d23);
		pos(builder, matrix4f, matrix3f, d12, (float) -d0, d13, r, g, b, 1, d22);
		pos(builder, matrix4f, matrix3f, d14, (float) -d0, d15, r, g, b, 0.0F, d22);
		pos(builder, matrix4f, matrix3f, d14, 0.0F, d15, r, g, b, 0.0F, d23);

		pos(builder, matrix4f, matrix3f, d16, 0.0F, d17, r, g, b, 1, d23);
		pos(builder, matrix4f, matrix3f, d16, (float) -d0, d17, r, g, b, 1, d22);
		pos(builder, matrix4f, matrix3f, d18, (float) -d0, d19, r, g, b, 0.0F, d22);
		pos(builder, matrix4f, matrix3f, d18, 0.0F, d19, r, g, b, 0.0F, d23);

		matrix.popPose();
	}

	public static void pos(VertexConsumer builder, Matrix4f matrix4f, Matrix3f matrix3f, float x, float y, float z, int r, int g, int b, float u, float v)
	{
		builder.vertex(matrix4f, x, y, z).color(r, g, b, 255).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880 / 2).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
	}
    
    @Override
    public boolean shouldRenderOffScreen(PylonBlockEntity p_188185_1_)
    {
        return true;
    }	
    @Override
    public int getViewDistance()
    {
        return 128;
    }
}
