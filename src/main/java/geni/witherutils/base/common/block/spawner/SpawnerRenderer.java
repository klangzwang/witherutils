package geni.witherutils.base.common.block.spawner;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.client.base.AbstractBlockEntityRenderer;
import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.type.WUTRenderType;
import geni.witherutils.core.common.util.FacingUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.client.ClientHooks;

public class SpawnerRenderer extends AbstractBlockEntityRenderer<SpawnerBlockEntity> {

	public static final ResourceLocation EMISSIVE = WitherUtilsRegistry.loc("textures/block/emissive/blue.png");
    
    public SpawnerRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(SpawnerBlockEntity tile, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int combinedLight, int combinedOverlay)
    {
        if (tile.getLevel() == null)
            return;
        
        if(tile.getShowSpawnerPos())
        {
    		VertexConsumer ivertexbuilder = buffer.getBuffer(RenderType.lines());
    		matrixStack.translate(-0.0005D, -0.0005D, -0.0005D);
    		matrixStack.scale(0.999F, 0.999F, 0.999F);
    		AABB thebox = new AABB(-8,0,-8,8,2,8);
    		LevelRenderer.renderLineBox(matrixStack, ivertexbuilder, thebox, 1F, 0.0F, 0.0F, 1.0F);
        }
        
		matrixStack.pushPose();
		matrixStack.translate(0.5, 0.5, 0.5);
        renderEmissiveModel(SpecialModels.EMSPAWNER.getModel(), ItemDisplayContext.NONE, false, matrixStack, buffer, -1, combinedLight, OverlayTexture.NO_OVERLAY);
        matrixStack.popPose();
		
		if(tile.getEntityToRender() != null)
		{
			Entity entity = tile.getEntityToRender();
			matrixStack.pushPose();
			matrixStack.translate(0.5D, 0.1D, 0.5D);
			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			matrixStack.mulPose(Axis.YP.rotationDegrees(partialTicks));
			matrixStack.scale(0.45F, 0.45F, 0.45F);
			Minecraft.getInstance().getEntityRenderDispatcher().render(entity, 0D, 0D, 0D, 0F, 0F, matrixStack, buffer, combinedLight);
			matrixStack.popPose();

			matrixStack.pushPose();
	        for(Direction facing : FacingUtil.FACES_AROUND_Y)
	        {
	        	String entityStr = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).getPath();
	        	renderText(tile, partialTicks, matrixStack, buffer, combinedLight, ChatFormatting.WHITE + entityStr, facing, 0.100f);
	        }
	        	
	        matrixStack.popPose();
		}
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
    
    @SuppressWarnings({ "resource", "incomplete-switch" })
	private void renderText(SpawnerBlockEntity tile, float partialTicks, PoseStack matrix, MultiBufferSource renderer, int overlayLight, String text, Direction side, float maxScale)
    {
        matrix.pushPose();
        matrix.translate(0, 0.6, 0);
        
        switch (side)
        {
            case SOUTH:
                matrix.translate(0, 1, 0.0001);
                matrix.mulPose(Axis.XP.rotationDegrees(90));
                break;
            case NORTH:
                matrix.translate(1, 1, 0.9999);
                matrix.mulPose(Axis.YP.rotationDegrees(180));
                matrix.mulPose(Axis.XP.rotationDegrees(90));
                break;
            case EAST:
                matrix.translate(0.0001, 1, 1);
                matrix.mulPose(Axis.YP.rotationDegrees(90));
                matrix.mulPose(Axis.XP.rotationDegrees(90));
                break;
            case WEST:
                matrix.translate(0.9999, 1, 0);
                matrix.mulPose(Axis.YP.rotationDegrees(-90));
                matrix.mulPose(Axis.XP.rotationDegrees(90));
                break;
        }

        float displayWidth = 1;
        float displayHeight = 1;
        matrix.translate(displayWidth / 2, 1, displayHeight / 2);
        matrix.mulPose(Axis.XP.rotationDegrees(-90));

        Font font = Minecraft.getInstance().font;

        int requiredWidth = Math.max(font.width(text), 1);
        int requiredHeight = font.lineHeight + 2;
        float scaler = 0.4F;
        float scaleX = displayWidth / requiredWidth;
        float scale = scaleX * scaler;
        if (maxScale > 0) {
            scale = Math.min(scale, maxScale);
        }

        matrix.scale(scale, -scale, scale);
        int realHeight = (int) Math.floor(displayHeight / scale);
        int realWidth = (int) Math.floor(displayWidth / scale);
        int offsetX = (realWidth - requiredWidth) / 2;
        int offsetY = (realHeight - requiredHeight) / 2;
        font.drawInBatch(Component.literal(text), offsetX - realWidth / 2f, 3 + offsetY - realHeight / 2f, overlayLight, false, matrix.last().pose(), renderer, Font.DisplayMode.NORMAL, 0, 0xF000F0);
    	
        matrix.popPose();
    }
    
    @Override
    public boolean shouldRenderOffScreen(SpawnerBlockEntity be)
    {
        return true;
    }	
    @Override
    public int getViewDistance()
    {
        return 128;
    }
}
