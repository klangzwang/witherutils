package geni.witherutils.base.common.block.farmer;

import java.awt.Color;

import org.joml.Matrix4f;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;

import geni.witherutils.api.farm.FarmNotification;
import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.type.WUTRenderType;
import geni.witherutils.base.common.base.AbstractBlockEntityRenderer;
import geni.witherutils.core.common.math.Vector3;
import geni.witherutils.core.common.util.FacingUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;

public class FarmerRenderer extends AbstractBlockEntityRenderer<FarmerBlockEntity> {
    
    private static RenderType ATYPE = createRenderType();
	
    public static RenderType createRenderType()
    {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorShader))
                .setTransparencyState(new RenderStateShard.TransparencyStateShard("translucent_transparency", () -> {
                    RenderSystem.enableBlend();
                    RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                }, () -> {
                    RenderSystem.disableBlend();
                    RenderSystem.defaultBlendFunc();
                })).createCompositeState(true);
        return RenderType.create("working_area_render", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, true, state);
    }
	
    public FarmerRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(FarmerBlockEntity tile, float partialTicks, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight)
    {
        if (tile.getLevel() == null)
            return;

    	double x = Vector3.CENTER.x - 0.5F;
    	double y = Vector3.CENTER.y - 0.5F;
    	double z = Vector3.CENTER.z - 0.5F;

    	matrix.translate(x, y, z);

        matrix.pushPose();
        matrix.translate(0.0, tile.getSlideProgress(partialTicks) * 0.26, 0.0);
        renderColoredModel(SpecialModels.FARMERTOP.getModel(), ItemDisplayContext.NONE, false, matrix, buffer, -1, light, OverlayTexture.NO_OVERLAY);
        renderEmissiveModel(SpecialModels.EMFARMER.getModel(), ItemDisplayContext.NONE, false, matrix, buffer, -1, light, OverlayTexture.NO_OVERLAY);
        matrix.popPose();

        matrix.pushPose();
        for(Direction facing : FacingUtil.FACES_AROUND_Y)
        {
        	for (FarmNotification note : tile.getNotification())
        	{
                renderText(tile, partialTicks, matrix, buffer, overlayLight, Component.translatable(note.getName()).withStyle(ChatFormatting.WHITE), facing, 0.100f);
        	}
        }
        matrix.popPose();

        
        if(tile.getShowFarmingPos())
        {
            matrix.pushPose();
//    		VertexConsumer ivertexbuilder = buffer.getBuffer(RenderType.lines());
    		BlockPos farmOffset = tile.getFarmingPos().subtract(tile.getBlockPos());
    		matrix.translate(farmOffset.getX() *2 -0.0005D, farmOffset.getY() -0.0005D, farmOffset.getZ()*2 -0.0005D);
    		matrix.scale(0.999F, 0.999F, 0.999F);
    		AABB thebox = new AABB(0,0,0,1,1,1);
//    		LevelRenderer.renderLineBox(matrix, ivertexbuilder, thebox, 0F, 0.5F, 0.5F, 0.75F);

    		Color color = new Color(Math.abs(farmOffset.getX() % 255), Math.abs(farmOffset.getY() % 255), Math.abs(farmOffset.getZ() % 255));
    		renderFaces(matrix, buffer, Shapes.create(thebox).bounds(), (double) -farmOffset.getX(), (double) -farmOffset.getY(), (double) -farmOffset.getZ(), (float) color.getRed() / 1f, (float) color.getGreen() / 1f, (float) color.getBlue() / 255f, 0.2F);
    		
    		matrix.popPose();
        }
    }
    
    private void renderFaces(PoseStack stack, MultiBufferSource renderTypeBuffer, AABB pos, double x, double y, double z, float red, float green, float blue, float alpha)
    {
        float x1 = (float) (pos.minX + x);
        float x2 = (float) (pos.maxX + x);
        float y1 = (float) (pos.minY + y);
        float y2 = (float) (pos.maxY + y);
        float z1 = (float) (pos.minZ + z);
        float z2 = (float) (pos.maxZ + z);

        Matrix4f matrix = stack.last().pose();
        VertexConsumer buffer;

        buffer = renderTypeBuffer.getBuffer(ATYPE);

        buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha).endVertex();

        buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha).endVertex();

        buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha).endVertex();

        buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha).endVertex();

        buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha).endVertex();

        buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha).endVertex();
    }
    
    public static void renderColoredModel(BakedModel model, ItemDisplayContext transformType, boolean leftHanded, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int color, int lightTexture, int overlayTexture)
    {
        matrixStack.pushPose();
        net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrixStack, model, transformType, leftHanded);
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
        net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrixStack, model, transformType, leftHanded);
        if(!model.isCustomRenderer())
        {
            VertexConsumer vertexBuilder = renderTypeBuffer.getBuffer(WUTRenderType.eyes(EMISSIVE));
            Minecraft.getInstance().getItemRenderer().renderModelLists(model, ItemStack.EMPTY, lightTexture, overlayTexture, matrixStack, vertexBuilder);
        }
        matrixStack.popPose();
    }
    
    @SuppressWarnings({ "resource", "incomplete-switch" })
	private void renderText(FarmerBlockEntity tile, float partialTicks, PoseStack matrix, MultiBufferSource renderer, int overlayLight, Component text, Direction side, float maxScale)
    {
        matrix.pushPose();
        matrix.translate(0, 0.25 + tile.getSlideProgress(partialTicks) * 0.26, 0);
        
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
        font.drawInBatch(text, offsetX - realWidth / 2f, 3 + offsetY - realHeight / 2f, overlayLight, false, matrix.last().pose(), renderer, Font.DisplayMode.NORMAL, 0, 0xF000F0);

//    	for (FarmNotification note : tile.getNotification())
//    	{
//    		Component levelsString = Component.translatable(note.getName()).withStyle(ChatFormatting.WHITE);
//    		float opacity = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
//    		int j = (int) (opacity * 255.0F) << 24;
////    		float halfWidth = -font.width(levelsString) / 2;
//    		Matrix4f positionMatrix;
//
//    		matrix.pushPose();
//			positionMatrix = matrix.last().pose();
////			font.drawInBatch(levelsString, offsetX - realWidth / 2f, 3 + offsetY - realHeight / 2f, 553648127, false, positionMatrix, renderer, DisplayMode.SEE_THROUGH, j, 0xF000F0);
////			font.drawInBatch(levelsString, offsetX - realWidth / 2f, 3 + offsetY - realHeight / 2f, -1, false, positionMatrix, renderer, DisplayMode.NORMAL, 0, 0xF000F0);
//			matrix.popPose();
//    	}
    	
        matrix.popPose();
    }
    
    @Override
    public boolean shouldRenderOffScreen(FarmerBlockEntity be)
    {
        return true;
    }	
    @Override
    public int getViewDistance()
    {
        return 128;
    }
}
