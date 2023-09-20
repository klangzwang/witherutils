package geni.witherutils.base.common.block.generator.water;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.type.WUTRenderType;
import geni.witherutils.base.common.base.AbstractBlockEntityRenderer;
import geni.witherutils.core.common.math.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;

public class WaterGeneratorRenderer extends AbstractBlockEntityRenderer<WaterGeneratorBlockEntity> {

    public WaterGeneratorRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(WaterGeneratorBlockEntity te, float partialTick, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight)
    {
        if (te.getLevel() == null)
            return;

    	matrix.pushPose();
    	
    	double x = Vector3.CENTER.x - 0.5F;
    	double y = Vector3.CENTER.y - 0.5F;
    	double z = Vector3.CENTER.z - 0.5F;
      
    	matrix.translate(x, y, z);
      
    	matrix.translate(0.5, 0.5, 0.5);
    	float rotation = te.prevFanRotation + (te.fanRotation - te.prevFanRotation) * partialTick;
    	matrix.mulPose(Axis.YP.rotationDegrees(rotation));
    	matrix.translate(-0.5, -0.5, -0.5);
      
    	renderSpecialFacingModel(SpecialModels.SHOVEL.getModel(), ItemDisplayContext.NONE, false, matrix, buffer, -1, light, OverlayTexture.NO_OVERLAY, WUTRenderType.solid(), null);
    	matrix.popPose();
    }
}
