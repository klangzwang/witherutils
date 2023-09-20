package geni.witherutils.base.common.block.battery.stab;

import org.lwjgl.opengl.GL14;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.common.base.AbstractBlockEntityRenderer;
import geni.witherutils.core.common.math.Vector3;
import geni.witherutils.core.common.util.McTimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderStateShard.TransparencyStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class StabRenderer extends AbstractBlockEntityRenderer<StabBlockEntity> {

    private static TransparencyStateShard SPHERE_TRANSPARENCY;
    private static RenderType SPHERE;
    
    public StabRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(StabBlockEntity tile, float partialTicks, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlay)
    {
        if (tile.getLevel() == null)
        	return;
        if (tile.getBlockState().getValue(StabBlock.LIT) != true)
        	return;

        VertexConsumer vertexBuilder = buffer.getBuffer(RenderType.solid());
        
        double x = Vector3.CENTER.x - 0.0F;
        double y = Vector3.CENTER.y - 0.0F;
        double z = Vector3.CENTER.z - 0.0F;

        matrix.translate(x, y, z);
        matrix.scale(0.25f, 0.25f, 0.25f);
        
        matrix.pushPose();
        matrix.scale(tile.core.getEnergyStorage().getEnergyStored() > 0 ? 1.85f : 1.0f, tile.core.getEnergyStorage().getEnergyStored() > 0 ? 1.85f : 1.0f, tile.core.getEnergyStorage().getEnergyStored() > 0 ? 1.85f : 1.0f);
		matrix.mulPose(Axis.YP.rotationDegrees(1.0f * (float) ((McTimerUtil.clientTimer + partialTicks) * 2F)));
		matrix.mulPose(Axis.ZP.rotationDegrees(0.5f * (float) ((McTimerUtil.clientTimer + partialTicks) * 2F)));
        SPHERE = RenderType.create("pylon_sphere", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
        		.setLightmapState(RenderStateShard.LIGHTMAP)
                .setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(WitherUtils.MODID, "textures/block/tile/core_overlay_blue.png"), false, false))
                .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_SOLID_SHADER)
                .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                .createCompositeState(false)
        );
        vertexBuilder = buffer.getBuffer(RenderType.translucent());
        Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.SPHERESTAB.getModel(), ItemStack.EMPTY, light, overlay, matrix, vertexBuilder);
        matrix.popPose();
        
        matrix.pushPose();
        float f = ((McTimerUtil.clientTimer + partialTicks) % 30F) / 30F;
        SPHERE_TRANSPARENCY = new TransparencyStateShard("ghost_transparency",
                () -> {
                    RenderSystem.enableBlend();
                    RenderSystem.blendFunc(GlStateManager.SourceFactor.CONSTANT_ALPHA, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
                    GL14.glBlendColor(1.0F - f, 1.0F, 1.0F, 0.75F - f);
                },
                () -> {
                    GL14.glBlendColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.disableBlend();
                    RenderSystem.defaultBlendFunc();
                });
        SPHERE = RenderType.create("pylon_sphere", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
        		.setLightmapState(RenderStateShard.LIGHTMAP)
                .setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(WitherUtils.MODID, "textures/block/tile/core_overlay_blue.png"), false, false))
                .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                .setTransparencyState(SPHERE_TRANSPARENCY)
                .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                .createCompositeState(false)
        );

        if(tile.core.active)
        {
        	matrix.scale(tile.core.getEnergyStorage().getEnergyStored() > 0 ? 1.85f + f : 1.0f + f, tile.core.getEnergyStorage().getEnergyStored() > 0 ? 1.85f + f : 1.0f + f, tile.core.getEnergyStorage().getEnergyStored() > 0 ? 1.85f + f : 1.0f + f);
            vertexBuilder = buffer.getBuffer(SPHERE);
            Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.SPHERESTAB.getModel(), ItemStack.EMPTY, light, OverlayTexture.NO_OVERLAY, matrix, vertexBuilder);
        }
        matrix.popPose();
	}
	
    @Override
    public boolean shouldRenderOffScreen(StabBlockEntity p_188185_1_)
    {
        return true;
    }	
    @Override
    public int getViewDistance()
    {
        return 128;
    }
}
