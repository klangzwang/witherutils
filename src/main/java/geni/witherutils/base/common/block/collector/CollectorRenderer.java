package geni.witherutils.base.common.block.collector;

import org.lwjgl.opengl.GL14;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.client.base.AbstractBlockEntityRenderer;
import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.type.WUTRenderType;
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
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.client.ClientHooks;

public class CollectorRenderer extends AbstractBlockEntityRenderer<CollectorBlockEntity> {

	public static final ResourceLocation SKY_LOCATION = ResourceLocation.withDefaultNamespace("textures/environment/end_sky.png");
	public static final ResourceLocation PORTAL_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/end_portal.png");
	public static final RenderStateShard.ShaderStateShard RENDERTYPE_PORTAL_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeEndPortalShader);
	public static RenderType CORE_PORTAL; 
	public static RenderStateShard.TransparencyStateShard CORE_TRANSPARENCY;

	public static final ResourceLocation EMISSIVE = WitherUtilsRegistry.loc("textures/block/emissive/blue.png");

    public CollectorRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(CollectorBlockEntity tile, float partialTick, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight)
    {
        if (tile.getLevel() == null)
            return;
        
        matrix.pushPose();
        matrix.translate(0.5, 0.5, 0.5);
        matrix.scale(0.75f, 1.0f, 0.75f);
        renderColoredModel(SpecialModels.COLLECTOR.getModel(), ItemDisplayContext.NONE, false, matrix, buffer, -1, light, OverlayTexture.NO_OVERLAY);
        matrix.popPose();
        
        matrix.pushPose();
		float time = tile.getLevel().getLevelData().getGameTime() + partialTick;
		double offset = Math.sin(time * 4.2 / 22.0D) / 30.0D;
        
        CORE_TRANSPARENCY = new TransparencyStateShard("core_transparency",
                () -> {
                    RenderSystem.enableBlend();
                    RenderSystem.blendFunc(GlStateManager.SourceFactor.CONSTANT_ALPHA, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
                    GL14.glBlendColor(1.0F, 1.0F, 1.0F, 0.4f + (float) offset * 4);
                },
                () -> {
                    GL14.glBlendColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.disableBlend();
                    RenderSystem.defaultBlendFunc();
                });
        
        CORE_PORTAL = RenderType.create("core_portal", DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
    			.setShaderState(RENDERTYPE_PORTAL_SHADER)
    			.setTextureState(RenderStateShard.MultiTextureStateShard.builder()
    					.add(WitherUtilsRegistry.loc("textures/block/emissive/blue.png"), false, false)
    					.add(SKY_LOCATION, false, false)
    					.add(PORTAL_LOCATION, false, false)
    					.build())
        		.setTransparencyState(CORE_TRANSPARENCY)
				.setWriteMaskState(RenderType.COLOR_WRITE)
				.createCompositeState(false));

        VertexConsumer vertexBuilder = buffer.getBuffer(this.renderType());
        Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.COLLECTOR.getModel(), ItemStack.EMPTY, light, OverlayTexture.NO_OVERLAY, matrix, vertexBuilder);
        matrix.popPose();

        matrix.pushPose();
        matrix.translate(0.5, 0.5, 0.5);
  	    float rotation = tile.prevFanRotation + (tile.fanRotation - tile.prevFanRotation) * partialTick;
  	    matrix.mulPose(Axis.YP.rotationDegrees(rotation));
        renderColoredModel(SpecialModels.COLLECTOR_ROTATE.getModel(), ItemDisplayContext.NONE, false, matrix, buffer, -1, light, OverlayTexture.NO_OVERLAY);
        renderEmissiveModel(SpecialModels.COLLECTOR_EMM.getModel(), ItemDisplayContext.NONE, false, matrix, buffer, -1, light, OverlayTexture.NO_OVERLAY);
        matrix.popPose();

        if(tile.render && tile.getRange() != 0)
  		{
  			renderPreview(tile, partialTick, matrix, buffer, light, overlayLight);
  		}
    }
    
	public void renderPreview(CollectorBlockEntity tile, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlaylight)
	{
		VertexConsumer ivertexbuilder = buffer.getBuffer(RenderType.lines());
		matrixStack.translate(-0.0005D, -0.0005D, -0.0005D);
		matrixStack.scale(0.999F, 0.999F, 0.999F);
		AABB thebox = new AABB(0,0,0,0,0,0);

		matrixStack.pushPose();
		thebox = new AABB(-tile.getRange(),0,-tile.getRange(),tile.getRange() + 1,1,tile.getRange() + 1);
		matrixStack.popPose();

		LevelRenderer.renderLineBox(matrixStack, ivertexbuilder, thebox, 0F, 0.5F, 0.5F, 0.75F);
	}

    public static void renderColoredModel(BakedModel model, ItemDisplayContext transformType, boolean leftHanded, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int color, int lightTexture, int overlayTexture)
    {
        matrixStack.pushPose();
        ClientHooks.handleCameraTransforms(matrixStack, model, transformType, leftHanded);
        matrixStack.translate(-0.5, -0.5, -0.5);
        if(!model.isCustomRenderer())
        {
            VertexConsumer vertexBuilder = renderTypeBuffer.getBuffer(RenderType.solid());
            Minecraft.getInstance().getItemRenderer().renderModelLists(model, ItemStack.EMPTY, lightTexture, overlayTexture, matrixStack, vertexBuilder);
        }
        matrixStack.popPose();
    }
    public static void renderEmissiveModel(BakedModel model, ItemDisplayContext transformType, boolean leftHanded, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int color, int lightTexture, int overlayTexture)
    {
        matrixStack.pushPose();
        ClientHooks.handleCameraTransforms(matrixStack, model, transformType, leftHanded);
        matrixStack.translate(-0.5, -0.5, -0.5);
        if(!model.isCustomRenderer())
        {
            VertexConsumer vertexBuilder = renderTypeBuffer.getBuffer(WUTRenderType.eyes(EMISSIVE));
            Minecraft.getInstance().getItemRenderer().renderModelLists(model, ItemStack.EMPTY, lightTexture, overlayTexture, matrixStack, vertexBuilder);
        }
        matrixStack.popPose();
    }
	
    @Override
    public boolean shouldRenderOffScreen(CollectorBlockEntity p_188185_1_)
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
