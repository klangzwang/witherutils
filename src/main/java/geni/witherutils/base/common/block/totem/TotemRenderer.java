package geni.witherutils.base.common.block.totem;

import java.awt.Color;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.type.WUTRenderType;
import geni.witherutils.base.common.base.AbstractBlockEntityRenderer;
import geni.witherutils.core.common.helper.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

public class TotemRenderer extends AbstractBlockEntityRenderer<TotemBlockEntity> {

    public static final ResourceLocation EMISSIVEOFF = new ResourceLocation("witherutils:textures/block/emissive/red.png");
    public static final ResourceLocation EMISSIVEON = new ResourceLocation("witherutils:textures/block/emissive/green.png");
    
    public TotemRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(TotemBlockEntity te, float partialTick, PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight)
    {
        if (te.getLevel() == null)
            return;

		poseStack.pushPose();
		renderEmissive(te, partialTick, poseStack, buffer, light, overlayLight);
		poseStack.popPose();

		poseStack.pushPose();
		renderInventory(te, partialTick, poseStack, buffer, light, overlayLight);
		poseStack.popPose();
		
		poseStack.pushPose();
		renderPreview(te, partialTick, poseStack, buffer, light, overlayLight);
		poseStack.popPose();
		
//		poseStack.pushPose();
//		renderStack(te, partialTick, poseStack, buffer, light, overlayLight);
//		poseStack.popPose();
    }

    public void renderEmissive(TotemBlockEntity te, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int light, int overlayLight)
    {
    	boolean isEmpty = te.getInventory().getStackInSlot(0).isEmpty();
    	VertexConsumer consumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(WUTRenderType.eyes(isEmpty ? EMISSIVEOFF : EMISSIVEON));
        Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.EMTOTEM.getModel(), ItemStack.EMPTY, 255, OverlayTexture.NO_OVERLAY, poseStack, consumer);
    }

    public void renderStack(@Nonnull TotemBlockEntity tile, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int combinedLight, int combinedOverlay)
    {
        int progress = 0;
		float time = tile.getLevel().getLevelData().getGameTime() + partialTicks;
		progress = (int) (Math.sin(time * 1 / 8.0D) / 10.0D);
        
        matrixStack.pushPose();
        float scale = (float) (0.75f + 0.45f * (progress / 100.0f));
        float f = Math.floorMod(tile.getLevel().getGameTime(), 120);
        matrixStack.translate(0.5, 0.35 + (0.00 * progress), 0.5);
        RenderHelper.rotateYP(matrixStack, f * 3);
        matrixStack.scale(scale, scale, scale);
        RenderHelper.renderItemGround(matrixStack, buffer, RenderType.solid(), new ItemStack(Blocks.ACACIA_SLAB), RenderHelper.MAX_BRIGHTNESS / 2, combinedOverlay);
        matrixStack.popPose();
    }
    
    public void renderInventory(TotemBlockEntity te, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int light, int overlayLight)
    {
        if(te.getInventory().getStackInSlot(0).isEmpty())
        	return;
		
		BakedModel model = itemRenderer.getModel(te.getInventory().getStackInSlot(0), te.getLevel(), null, 0);

		float time = te.getLevel().getLevelData().getGameTime() + partialTick;
		double offset = Math.sin(time * 1 / 8.0D) / 10.0D;
		
		poseStack.translate(0.5D, 0.5D + offset, 0.5D);
		poseStack.mulPose(Axis.YP.rotationDegrees(time * 4.0F * 2.0f));
		itemRenderer.render(te.getInventory().getStackInSlot(0), ItemDisplayContext.GROUND, false, poseStack, buffer, light, overlayLight, model);
    }
    
	public void renderPreview(TotemBlockEntity te, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlaylight)
	{
		if(!te.getPreview())
			return;
		
		RenderSystem.lineWidth(Math.max(2.5F, (float) Minecraft.getInstance().getWindow().getWidth() / 1920.0F * 2.5F));
        BlockPos blockpos = te.getBlockPos();
        Color color = new Color(Math.abs(blockpos.getX() % 255), Math.abs(blockpos.getY() % 255), Math.abs(blockpos.getZ() % 255));
        
		VertexConsumer ivertexbuilder = buffer.getBuffer(RenderType.lines());
		matrixStack.translate(-0.0005D, -0.0005D, -0.0005D);
		matrixStack.scale(0.999F, 0.999F, 0.999F);
		
		AABB thebox = new AABB(-te.getScaleX(), 0, -te.getScaleZ(), te.getScaleX() +1, te.getScaleY(), te.getScaleZ() +1);
		LevelRenderer.renderLineBox(matrixStack, ivertexbuilder, thebox, (float) color.getRed() / 0f, (float) color.getGreen() / 255f, (float) color.getBlue() / 255f, 0.5F);
	}
}
