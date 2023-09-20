package geni.witherutils.base.common.block.battery.core;

import java.util.Arrays;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL14;

import com.mojang.blaze3d.platform.GlStateManager;
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
import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.EffectLib;
import geni.witherutils.base.client.render.type.WUTRenderType;
import geni.witherutils.base.common.base.AbstractBlockEntityRenderer;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.core.common.math.Vector3;
import geni.witherutils.core.common.util.McTimerUtil;
import geni.witherutils.core.common.util.ModelRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderStateShard.TransparencyStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class CoreRenderer extends AbstractBlockEntityRenderer<CoreBlockEntity> {

	public static final ResourceLocation SKY_LOCATION = new ResourceLocation("textures/environment/end_sky.png");
	public static final ResourceLocation PORTAL_LOCATION = new ResourceLocation("textures/entity/end_portal.png");
	public static final RenderStateShard.ShaderStateShard RENDERTYPE_PORTAL_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeEndPortalShader);
	public static RenderType CORE_PORTAL; 
	public static RenderStateShard.TransparencyStateShard CORE_TRANSPARENCY;
	
    private static final RenderType beam = RenderType.create("beam", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
            .setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(WitherUtils.MODID, "textures/block/tile/beam2.png"), false, false))
            .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionTexShader))
            .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
            .createCompositeState(false)
    );
	public static RenderType beam2;
//    public static TransparencyStateShard transparency;
//    public static RenderType buildguide;
    
    public CoreRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(CoreBlockEntity te, float partialTicks, PoseStack matrix, MultiBufferSource rtb, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int ov)
    {
        if (te.getLevel() == null)
        	return;

        if(!te.active && te.isShowingBuildGuide())
        {
            matrix.pushPose();
            renderBuildGuide(te, matrix, rtb, mc, light, partialTicks);
            matrix.popPose();
        }
        
        if(te.stabilizersValid)
        {
            matrix.pushPose();
            renderStabilizers(te, matrix, rtb, partialTicks);
            matrix.popPose(); 
        }
        
        matrix.pushPose();
        renderCore(te, partialTicks, matrix, rtb, light, ov);
        matrix.popPose();
        
        if(te.active && te.getEnergyStorage().getEnergyStored() > 0)
        {
            matrix.pushPose();
            renderEffects(te, matrix, rtb);
            matrix.popPose();
        }
	}
	
	@SuppressWarnings("unused")
	public void renderCore(CoreBlockEntity te, float partialTicks, PoseStack matrix, MultiBufferSource rtb, int light, int ov)
	{
        if(!te.active)
        	return;

        light = LevelRenderer.getLightColor(te.getLevel(), te.getBlockPos().above(255));
        
		float time = te.getLevel().getLevelData().getGameTime() + partialTicks;
		double offset = Math.sin(time * 4.2 / 22.0D) / 30.0D;
		float rotation = (McTimerUtil.renderTimer + partialTicks) / 2F;

        /*
         * 
         * SPHERE ONE
         * 
         */
		matrix.pushPose();
        matrix.translate(0.5, 0.5, 0.5);
        matrix.scale(3.33f, 3.33f, 3.33f);
        matrix.pushPose();
		matrix.mulPose(Axis.YP.rotationDegrees(1.0f * (float) ((McTimerUtil.clientTimer + partialTicks) * 0.75F)));
		matrix.mulPose(Axis.ZP.rotationDegrees(0.5f * (float) ((McTimerUtil.clientTimer + partialTicks) * 0.75F)));
        VertexConsumer vertexBuilder1 = rtb.getBuffer(WUTRenderType.GHOST);
        Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.SPHEREINNER.getModel(), ItemStack.EMPTY, light, OverlayTexture.NO_OVERLAY, matrix, vertexBuilder1);
        matrix.popPose();
        matrix.popPose();
        
        /*
         * 
         * SPHERE TWO
         * 
         */
		matrix.pushPose();
        matrix.translate(0.5, 0.5, 0.5);
        matrix.scale(3.33f, 3.33f, 3.33f);
        matrix.pushPose();
        
//        VertexConsumer vertexBuilder2 = rtb.getBuffer(WURenderType.getEyesAlphaEnabled(te.getLevel(), partialTicks, new ResourceLocation(WitherUtils.MODID, "textures/model/tile/core_overlayblue.png")));
        CORE_TRANSPARENCY = new TransparencyStateShard("core_transparency",
                () -> {
                    RenderSystem.enableBlend();
                    RenderSystem.blendFunc(GlStateManager.SourceFactor.CONSTANT_ALPHA, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
                    GL14.glBlendColor(1.0F, 1.0F, 1.0F, 0.6f + (float) offset * 4);
                },
                () -> {
                    GL14.glBlendColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.disableBlend();
                    RenderSystem.defaultBlendFunc();
                });
        
        CORE_PORTAL = RenderType.create("core_portal", DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
    			.setShaderState(RENDERTYPE_PORTAL_SHADER)
    			.setTextureState(RenderStateShard.MultiTextureStateShard.builder()
    					.add(new ResourceLocation(WitherUtils.MODID, "textures/block/tile/core_overlay_blue.png"), false, false)
    					.add(SKY_LOCATION, false, false)
    					.add(PORTAL_LOCATION, false, false)
    					.build())
        		.setTransparencyState(CORE_TRANSPARENCY)
				.setWriteMaskState(RenderType.COLOR_WRITE)
				.createCompositeState(false));

        VertexConsumer vertexBuilder2 = rtb.getBuffer(this.renderType());
        Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.SPHEREOUTER.getModel(), ItemStack.EMPTY, light, OverlayTexture.NO_OVERLAY, matrix, vertexBuilder2);
        matrix.popPose();
        matrix.popPose();
        
        /*
         * 
         * SPHERE THREE
         * 
         */
		matrix.pushPose();
        matrix.translate(0.5, 0.5, 0.5);
        matrix.scale(3.33f + (float) offset, 3.33f + (float) offset, 3.33f + (float) offset);
        matrix.pushPose();
		matrix.mulPose(Axis.YP.rotationDegrees(1.0f * (float) ((McTimerUtil.clientTimer + partialTicks) * 0.55F)));
		matrix.mulPose(Axis.ZP.rotationDegrees(0.5f * (float) ((McTimerUtil.clientTimer + partialTicks) * 0.55F)));
        VertexConsumer vertexBuilder3 = rtb.getBuffer(WUTRenderType.GHOST);
        Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.SPHEREINNER.getModel(), ItemStack.EMPTY, light, OverlayTexture.NO_OVERLAY, matrix, vertexBuilder3);
        matrix.popPose();
        matrix.popPose();
	}

	public void renderBuildGuide(CoreBlockEntity tile, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, int light, float partialTicks)
	{
        BlockPos pos = tile.getBlockPos();
        Level level = tile.getLevel();

//        transparency = new TransparencyStateShard("transparency",
//                () -> {
//                    RenderSystem.enableBlend();
//                    RenderSystem.blendFunc(GlStateManager.SourceFactor.CONSTANT_ALPHA, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
//                    GL14.glBlendColor(1.0F, 1.0F, 1.0F, 0.75F);
//                },
//                () -> {
//                    GL14.glBlendColor(1.0F, 1.0F, 1.0F, 1.0F);
//                    RenderSystem.disableBlend();
//                    RenderSystem.defaultBlendFunc();
//                });
//        buildguide = RenderType.create("buildguide", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 2097152, true, true, RenderType.CompositeState.builder()
//        		.setLightmapState(RenderStateShard.LIGHTMAP)
//        		.setTextureState(RenderStateShard.BLOCK_SHEET_MIPPED)
//                .setShaderState(RenderStateShard.RENDERTYPE_TRANSLUCENT_SHADER)
//                .setTransparencyState(transparency)
//                .setOutputState(RenderStateShard.TRANSLUCENT_TARGET)
//                .setCullState(RenderStateShard.CULL)
//                .createCompositeState(true)
//        );
        
        matrix.pushPose();
        matrix.translate(-pos.getX(), -pos.getY(), -pos.getZ());

        tile.getControllerList().forEach(aoePos -> {
            if (level != null && level.isEmptyBlock(aoePos)) {
                matrix.pushPose();
                matrix.translate(aoePos.getX(), aoePos.getY(), aoePos.getZ());
                renderGuideBlock(Blocks.COAL_BLOCK, 0.5F, matrix, buffer, mc);
//                Minecraft.getInstance().getBlockRenderer().renderSingleBlock(Blocks.COAL_BLOCK.defaultBlockState(), matrix, buffer, light, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, buildguide);
                matrix.popPose();
            }
        });
        if(tile.isControllerValid())
        {
            tile.getSteelList().forEach(aoePos -> {
                if (level != null && level.isEmptyBlock(aoePos)) {
                    matrix.pushPose();
                    matrix.translate(aoePos.getX(), aoePos.getY(), aoePos.getZ());
                    renderGuideBlock(WUTBlocks.WITHERSTEEL_BLOCK.get(), 0.75F, matrix, buffer, mc);
//                    Minecraft.getInstance().getBlockRenderer().renderSingleBlock(WUTBlocks.WITHERSTEEL_BLOCK.get().defaultBlockState(), matrix, buffer, light, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, buildguide);
                    matrix.popPose();
                }
            });
        }
        if(tile.isSteelValid())
        {
            tile.getCoilList().forEach(aoePos -> {
                if (level != null && level.isEmptyBlock(aoePos)) {
                    matrix.pushPose();
                    matrix.translate(aoePos.getX(), aoePos.getY(), aoePos.getZ());
                    renderGuideBlock(Blocks.REDSTONE_BLOCK, 0.75F, matrix, buffer, mc);
//                    Minecraft.getInstance().getBlockRenderer().renderSingleBlock(Blocks.REDSTONE_BLOCK.defaultBlockState(), matrix, buffer, light, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, buildguide);
                    matrix.popPose();
                }
            });
        }
        
        matrix.popPose();
	}
	
	public void renderGuideBlock(Block guideBlock, float alpha, PoseStack matrix, MultiBufferSource buffer, Minecraft mc)
	{
    	for(Direction side : Direction.values())
    	{
    		matrix.pushPose();

            RenderSystem.enableBlend();
            
        	BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        	RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        	ResourceLocation location = ForgeRegistries.BLOCKS.getKey(guideBlock);
        	TextureAtlasSprite tex = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation(location.getNamespace(), "block/" + location.getPath()));
            
        	RenderSystem.setShaderTexture(0, tex.atlasLocation());
        	RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);

        	bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);

        	Vector3f[] vec = Arrays.stream(ModelRenderUtil.createQuadVerts(side, 0, 1, 1)).map(Vec3::toVector3f).toArray(Vector3f[]::new);

        	Matrix4f matrix4f = matrix.last().pose();

        	bufferbuilder.vertex(matrix4f, vec[0].x(), vec[0].y(), vec[0].z()).color(1F, 1F, 1F, 1F).uv(tex.getU0(), tex.getV0()).endVertex();
        	bufferbuilder.vertex(matrix4f, vec[1].x(), vec[1].y(), vec[1].z()).color(1F, 1F, 1F, 1F).uv(tex.getU0(), tex.getV1()).endVertex();
        	bufferbuilder.vertex(matrix4f, vec[2].x(), vec[2].y(), vec[2].z()).color(1F, 1F, 1F, 1F).uv(tex.getU1(), tex.getV1()).endVertex();
        	bufferbuilder.vertex(matrix4f, vec[3].x(), vec[3].y(), vec[3].z()).color(1F, 1F, 1F, 1F).uv(tex.getU1(), tex.getV0()).endVertex();
      	  
        	BufferUploader.drawWithShader(bufferbuilder.end());

            RenderSystem.disableBlend();
            
            matrix.popPose();   
    	}
	}
	
	private void renderStabilizers(CoreBlockEntity te, PoseStack matrix, MultiBufferSource getter, float partialTick)
	{
        /*
         * 
         * X
         * 
         */
        if(te.stabilizerPositions[2].getX() > 0)
        {
            matrix.pushPose();
            matrix.mulPose(Axis.ZP.rotationDegrees(90));
            matrix.translate(0, -0.5, 0);
        	renderStabilizerBeam(te, matrix, getter, te.stabilizerPositions[2], partialTick);
            matrix.popPose();
        }
        if(te.stabilizerPositions[3].getX() < 0)
        {
            matrix.pushPose();
            matrix.mulPose(Axis.ZP.rotationDegrees(-90));
            matrix.translate(-1, 0.5, 0);
        	renderStabilizerBeam(te, matrix, getter, te.stabilizerPositions[3], partialTick);
            matrix.popPose();
        }
        
        /*
         * 
         * Y
         * 
         */
        if(te.stabilizerPositions[0].getY() > 0)
        {
            matrix.pushPose();
            matrix.mulPose(Axis.XP.rotationDegrees(180));
            matrix.translate(0, -0.5, -1);
        	renderStabilizerBeam(te, matrix, getter, te.stabilizerPositions[0], partialTick);
            matrix.popPose();
        }
        if(te.stabilizerPositions[1].getY() < 0)
        {
            matrix.pushPose();
            matrix.translate(0, 0.5, 0);
        	renderStabilizerBeam(te, matrix, getter, te.stabilizerPositions[1], partialTick);
            matrix.popPose();
        }
        
        /*
         * 
         * Z
         * 
         */
        if(te.stabilizerPositions[0].getZ() > 0)
        {
            matrix.pushPose();
        	matrix.mulPose(Axis.XP.rotationDegrees(-90));
        	matrix.translate(0, -0.5, 0);
        	renderStabilizerBeam(te, matrix, getter, te.stabilizerPositions[0], partialTick);
            matrix.popPose();
        }
        if(te.stabilizerPositions[1].getZ() < 0)
        {
            matrix.pushPose();
        	matrix.mulPose(Axis.XP.rotationDegrees(90));
        	matrix.translate(0, 0.5, -1);
        	renderStabilizerBeam(te, matrix, getter, te.stabilizerPositions[1], partialTick);
            matrix.popPose();
        }
        if(te.stabilizerPositions[2].getZ() > 0)
        {
            matrix.pushPose();
        	matrix.mulPose(Axis.XP.rotationDegrees(-90));
        	matrix.translate(0, -0.5, 0);
        	renderStabilizerBeam(te, matrix, getter, te.stabilizerPositions[2], partialTick);
            matrix.popPose();
        }
        if(te.stabilizerPositions[3].getZ() < 0)
        {
            matrix.pushPose();
        	matrix.mulPose(Axis.XP.rotationDegrees(90));
        	matrix.translate(0, 0.5, -1);
        	renderStabilizerBeam(te, matrix, getter, te.stabilizerPositions[3], partialTick);
            matrix.popPose();
        }
        
        for (BlockPos posOffset : te.stabilizerPositions)
        {
    		Vec3 posone = new Vec3(te.getBlockPos().getX(), te.getBlockPos().getY(), te.getBlockPos().getZ());
    		Vec3 postwo = new Vec3(te.getBlockPos().getX() - posOffset.getX(),
    							   te.getBlockPos().getY() - posOffset.getY(),
    							   te.getBlockPos().getZ() - posOffset.getZ());

            matrix.pushPose();
    		renderBeam(te, partialTick, matrix, getter, posone, postwo);
    		matrix.popPose();
        }
	}
	
    private void renderStabilizerBeam(CoreBlockEntity te, PoseStack matrix, MultiBufferSource getter, BlockPos vec, float partialTick)
    {
        VertexConsumer builder = getter.getBuffer(beam);

        float beamLength = Math.abs(vec.getX() + vec.getY() + vec.getZ()) - 0.5F;
        float time = McTimerUtil.renderTimer + partialTick;
        double rotation = (double) time * 0.275D * 0.01f * 10.0D;
        float beamMotion = time * 0.07F - (float) Math.floor(-time * 0.05F);

        float scale = 0.3F;
        float d7 = 0.5F + (float) Math.cos(rotation + 2.356194490192345F) * scale;
        float d9 = 0.5F + (float) Math.sin(rotation + 2.356194490192345F) * scale;
        float d11 = 0.5F + (float) Math.cos(rotation + (Math.PI / 4F)) * scale;
        float d13 = 0.5F + (float) Math.sin(rotation + (Math.PI / 4F)) * scale;
        float d15 = 0.5F + (float) Math.cos(rotation + 3.9269908169872414F) * scale;
        float d17 = 0.5F + (float) Math.sin(rotation + 3.9269908169872414F) * scale;
        float d19 = 0.5F + (float) Math.cos(rotation + 5.497787143782138F) * scale;
        float d21 = 0.5F + (float) Math.sin(rotation + 5.497787143782138F) * scale;
        float texXMin = 0.0F;
        float texXMax = 1.0F;
        float d28 = (-1.0F + beamMotion);
        float texHeight = beamLength * (0.5F / scale) + d28;
        
		PoseStack.Pose last = matrix.last();
		Matrix4f matrix4f = last.pose();

		builder.vertex(matrix4f, d7, beamLength, d9).uv(texXMax, texHeight).endVertex();
		builder.vertex(matrix4f, d7, 0, d9).uv(texXMax, d28).endVertex();
		builder.vertex(matrix4f, d11, 0, d13).uv(texXMin, d28).endVertex();
		builder.vertex(matrix4f, d11, beamLength, d13).uv(texXMin, texHeight).endVertex();

		builder.vertex(matrix4f, d19, beamLength, d21).uv(texXMax, texHeight).endVertex();
		builder.vertex(matrix4f, d19, 0, d21).uv(texXMax, d28).endVertex();
		builder.vertex(matrix4f, d15, 0, d17).uv(texXMin, d28).endVertex();
		builder.vertex(matrix4f, d15, beamLength, d17).uv(texXMin, texHeight).endVertex();

		builder.vertex(matrix4f, d11, beamLength, d13).uv(texXMax, texHeight).endVertex();
		builder.vertex(matrix4f, d11, 0, d13).uv(texXMax, d28).endVertex();
		builder.vertex(matrix4f, d19, 0, d21).uv(texXMin, d28).endVertex();
		builder.vertex(matrix4f, d19, beamLength, d21).uv(texXMin, texHeight).endVertex();

		builder.vertex(matrix4f, d15, beamLength, d17).uv(texXMax, texHeight).endVertex();
		builder.vertex(matrix4f, d15, 0, d17).uv(texXMax, d28).endVertex();
		builder.vertex(matrix4f, d7, 0, d9).uv(texXMin, d28).endVertex();
		builder.vertex(matrix4f, d7, beamLength, d9).uv(texXMin, texHeight).endVertex();

        scale = 0.3F;
        rotation += 0.77f;
        d7 = 0.5F + (float) Math.cos(rotation + 2.356194490192345F) * scale;
        d9 = 0.5F + (float) Math.sin(rotation + 2.356194490192345F) * scale;
        d11 = 0.5F + (float) Math.cos(rotation + (Math.PI / 4F)) * scale;
        d13 = 0.5F + (float) Math.sin(rotation + (Math.PI / 4F)) * scale;
        d15 = 0.5F + (float) Math.cos(rotation + 3.9269908169872414F) * scale;
        d17 = 0.5F + (float) Math.sin(rotation + 3.9269908169872414F) * scale;
        d19 = 0.5F + (float) Math.cos(rotation + 5.497787143782138F) * scale;
        d21 = 0.5F + (float) Math.sin(rotation + 5.497787143782138F) * scale;

        d28 = (-1F + (beamMotion * 0.1f));
        texHeight = beamLength * (0.5F / scale) + d28;

		builder.vertex(matrix4f, d7, beamLength, d9).uv(texXMax, texHeight).endVertex();
		builder.vertex(matrix4f, d7, 0, d9).uv(texXMax, d28).endVertex();
		builder.vertex(matrix4f, d11, 0, d13).uv(texXMin, d28).endVertex();
		builder.vertex(matrix4f, d11, beamLength, d13).uv(texXMin, texHeight).endVertex();

		builder.vertex(matrix4f, d19, beamLength, d21).uv(texXMax, texHeight).endVertex();
		builder.vertex(matrix4f, d19, 0, d21).uv(texXMax, d28).endVertex();
		builder.vertex(matrix4f, d15, 0, d17).uv(texXMin, d28).endVertex();
		builder.vertex(matrix4f, d15, beamLength, d17).uv(texXMin, texHeight).endVertex();

		builder.vertex(matrix4f, d11, beamLength, d13).uv(texXMax, texHeight).endVertex();
		builder.vertex(matrix4f, d11, 0, d13).uv(texXMax, d28).endVertex();
		builder.vertex(matrix4f, d19, 0, d21).uv(texXMin, d28).endVertex();
		builder.vertex(matrix4f, d19, beamLength, d21).uv(texXMin, texHeight).endVertex();

		builder.vertex(matrix4f, d15, beamLength, d17).uv(texXMax, texHeight).endVertex();
		builder.vertex(matrix4f, d15, 0, d17).uv(texXMax, d28).endVertex();
		builder.vertex(matrix4f, d7, 0, d9).uv(texXMin, d28).endVertex();
		builder.vertex(matrix4f, d7, beamLength, d9).uv(texXMin, texHeight).endVertex();
    }
    
	public static void renderBeam(CoreBlockEntity tile, float pt, PoseStack matrix, MultiBufferSource rtb, Vec3 posone, Vec3 postwo)
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
		beam2 = WUTRenderType.entityBlendedNoDept(new ResourceLocation("witherutils:textures/block/tile/beam.png"));
		VertexConsumer builder = rtb.getBuffer(beam2);
		PoseStack.Pose last = matrix.last();
		Matrix4f matrix4f = last.pose();
		Matrix3f matrix3f = last.normal();

		int r = 255;
		int g = 0;
		int b = 0;

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
	
	public static double angleOf(Vec3 p1, Vec3 p2)
	{
		final double deltaY = p2.z - p1.z;
		final double deltaX = p2.x - p1.x;
		final double result = Math.toDegrees(Math.atan2(deltaY, deltaX));
		return result < 0 ? 360d + result : result;
	}
	
    @SuppressWarnings("resource")
	private void renderEffects(CoreBlockEntity gate, PoseStack mStack, MultiBufferSource getter)
    {
    	RandomSource random = gate.getLevel().random;
    	
        mStack.translate(0.5, 0.5, 0.5);
        
        int i = 0;
        
        final Vector3 posi1;
        final Vector3 posi2;
        
        posi1 = new Vector3(gate.getBlockPos().getX(), gate.getBlockPos().getY() + 2, gate.getBlockPos().getZ());
        posi2 = new Vector3(gate.getBlockPos().getX(), gate.getBlockPos().getY() + 2, gate.getBlockPos().getZ());
//        		gate.getBlockPos().offset(-3 + random.nextInt(3), -3 + random.nextInt(3), -3 + random.nextInt(3)).getX(),
//        		gate.getBlockPos().offset(-3 + random.nextInt(3), -3 + random.nextInt(3), -3 + random.nextInt(3)).getY(),
//        		gate.getBlockPos().offset(-3 + random.nextInt(3), -3 + random.nextInt(3), -3 + random.nextInt(3)).getZ());
        
		EffectLib.renderLightningP2PRotate(mStack, getter, posi2, posi1, 16, (McTimerUtil.clientTimer / 2), 0.06F, 0.04F, false, 0, 0x6300BD);
		
        if(McTimerUtil.clientTimer % 5 == 0)
        {
            int pos = random.nextInt(4);
            for(i = 0; i < 4; i++)
            {
                if (i != pos) continue;
                float loopOffset = ((i / 4F) * ((float) Math.PI * 2F)) + (McTimerUtil.clientTimer / 100F);
                float rot = ((7 / 64F) * (float) Math.PI * 2F) + (McTimerUtil.clientTimer / 10F) + loopOffset;
                double x = Mth.sin(rot) * 2;
                double z = Mth.cos(rot) * 2;
                double y = Mth.cos(rot + loopOffset) * 1;
                EffectLib.renderLightningP2PRotate(mStack, getter, new Vector3(x-3 + random.nextInt(6), y, z-3 + random.nextInt(6)), Vector3.ZERO, 8, ((McTimerUtil.clientTimer) / 12), 0.06F, 0.04F, false, 0, 0x6300BD);
            }
        }
    }

    @Override
    public boolean shouldRenderOffScreen(CoreBlockEntity p_188185_1_)
    {
        return true;
    }	
    @Override
    public int getViewDistance()
    {
        return 128;
    }
    
	public static RenderType corePortal()
	{
		return CORE_PORTAL;
	}
	protected RenderType renderType()
	{
		return corePortal();
	}
}
