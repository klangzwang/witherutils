package geni.witherutils.base.common.block.cauldron;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.base.client.base.AbstractBlockEntityRenderer;
import geni.witherutils.base.client.model.special.SpecialModels;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CauldronRenderer extends AbstractBlockEntityRenderer<CauldronBlockEntity> {

    public CauldronRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(CauldronBlockEntity te, float partialTicks, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight)
    {
        if (te.getLevel() == null)
            return;

		matrix.pushPose();
		renderFluidTank(te.getFluidHandler(null), te, matrix, buffer, light, 1.0f, 0.3F, 0.3F, 1.7F, 0.3F, 1.7F, 0.85F);
		matrix.popPose();
		
		if(te.getInventory().getStackInSlot(0).isEmpty())
			return;

		matrix.pushPose();
		matrix.translate(0.5F, 0.65F, 0.5F);
		matrix.scale(0.7F, 0.6F, 0.7F);
		Minecraft.getInstance().getItemRenderer().renderStatic(te.getFluidHandler(null).getFluidInTank(0).getAmount() == 0 ? te.getInventory().getStackInSlot(0) : ItemStack.EMPTY, ItemDisplayContext.NONE, light, OverlayTexture.NO_OVERLAY, matrix, buffer, te.getLevel(), 0);
		matrix.popPose();
		
		if(te.getTimer() == 0)
			return;

		matrix.pushPose();
		
			matrix.pushPose();
			renderSoulFire(partialTicks, matrix, buffer, mc, level, player, light, OverlayTexture.NO_OVERLAY, 0.0F, 0.85F, 1.0F, 0.85F, 0.0F, 0.9F, 0.0F);
			matrix.popPose();
		
			matrix.pushPose();
		    matrix.translate(0.5, 0.5, 0.5);
		    matrix.mulPose(Axis.YP.rotationDegrees(45));
		    matrix.translate(-0.5, -0.5, -0.5);
			renderSoulFire(partialTicks, matrix, buffer, mc, level, player, light, OverlayTexture.NO_OVERLAY, 1.0F, 0.65F, 1.25F, 0.65F, 0.0F, 0.9F, 0.0F);
			matrix.popPose();
		
		matrix.popPose();
    }
    
	/*
	 * 
	 * SOUL FIRE
	 * 
	 */
    @SuppressWarnings("unused")
	public void renderSoulFire(float partialTicks, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlay, float modulation, float scaleX, float scaleY, float scaleZ, float x, float y, float z)
    {
        matrix.pushPose();

		float time = level.getLevelData().getGameTime() + partialTicks;
		
		float translateFire = (float) Math.sin(time * modulation / 8.0F) / 10.0F;
		float scaleFire = (float) Math.sin(time * modulation / 8.0F) / 10.0F;
		
        matrix.translate(0.5, 0.5, 0.5);
        matrix.scale(scaleX, scaleY + scaleFire, scaleZ);
        matrix.translate(x, y, z);
        matrix.translate(-0.5, -0.5, -0.5);

        VertexConsumer vertexBuilder = buffer.getBuffer(RenderType.cutout());
        Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.SOUL_FIRE.getModel(), ItemStack.EMPTY, light, OverlayTexture.NO_OVERLAY, matrix, vertexBuilder);
        matrix.popPose();
	}
}
