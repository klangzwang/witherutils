package geni.witherutils.base.common.block.sensor.wall;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.type.WUTRenderType;
import geni.witherutils.base.common.base.AbstractBlockEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class WallSensorRenderer extends AbstractBlockEntityRenderer<WallSensorBlockEntity> {

	public static final ResourceLocation EMISSIVE = new ResourceLocation("witherutils:textures/block/emissive/blue.png");
	
    public WallSensorRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(WallSensorBlockEntity te, float partialTick, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight)
    {
        if (te.getLevel() == null)
            return;
        
		matrix.pushPose();
		renderLight(te, partialTick, matrix, buffer, light, overlayLight);
        matrix.popPose();
    }
    
	public void renderLight(WallSensorBlockEntity tile, float v, PoseStack matrixStack, MultiBufferSource getter, int light, int combinedLight)
	{
		matrixStack.translate(0.5, 0.5, 0.5);
		Direction facing = tile.getCurrentFacing();
		switch(facing)
		{
			case EAST:
				matrixStack.mulPose(Axis.YP.rotationDegrees(-90));
				break;
			case WEST:
				matrixStack.mulPose(Axis.YP.rotationDegrees(90));
				break;
			case NORTH:
				break;
			case SOUTH:
				matrixStack.mulPose(Axis.YP.rotationDegrees(-180));
				break;
			case UP:
				matrixStack.mulPose(Axis.XP.rotationDegrees(90));
				break;
			case DOWN:
				matrixStack.mulPose(Axis.YP.rotationDegrees(-90));
				break;
			default :
				break;
		}	
		renderEmissiveModel(SpecialModels.EMWALLSENSOR.getModel(), ItemDisplayContext.NONE, false, matrixStack, getter, -1, combinedLight, OverlayTexture.NO_OVERLAY);
	}
	
    public static void renderEmissiveModel(BakedModel model, ItemDisplayContext transformType, boolean leftHanded, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int color, int lightTexture, int overlayTexture)
    {
        matrixStack.pushPose();
        net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrixStack, model, transformType, leftHanded);
        matrixStack.translate(-0.5, -0.5, -0.5);
        if(!model.isCustomRenderer())
        {
            VertexConsumer vertexBuilder = renderTypeBuffer.getBuffer(WUTRenderType.eyes(EMISSIVE));
            Minecraft.getInstance().getItemRenderer().renderModelLists(model, ItemStack.EMPTY, lightTexture, overlayTexture, matrixStack, vertexBuilder);
        }
        matrixStack.popPose();
    }
}
