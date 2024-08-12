package geni.witherutils.base.common.block.generator.wind;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import geni.witherutils.base.client.base.AbstractBlockEntityRenderer;
import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.type.WUTRenderType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;

public class WindGeneratorRenderer extends AbstractBlockEntityRenderer<WindGeneratorBlockEntity> {

    public WindGeneratorRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(WindGeneratorBlockEntity te, float partialTick, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight)
    {
        if (te.getLevel() == null)
            return;

        renderTranslated(te, partialTick, matrix, buffer, light);
    }
    
    private void renderTranslated(WindGeneratorBlockEntity te, float partialTick, PoseStack matrix, MultiBufferSource buffer, int light)
    {
        if (te.getLevel() == null)
            return;

        Direction facing = te.getCurrentFacing();
        
    	matrix.pushPose();

    	matrix.translate(0.5, 0.5, 0.5);
    	float rotation = te.prevFanRotation + (te.fanRotation - te.prevFanRotation) * partialTick;
        if(facing == Direction.WEST || facing == Direction.EAST)
        	matrix.mulPose(Axis.XP.rotationDegrees(rotation));
        else
        	matrix.mulPose(Axis.ZP.rotationDegrees(rotation));
    	matrix.translate(-0.5, -0.5, -0.5);
      
    	renderSpecialFacingModel(SpecialModels.FAN.getModel(), ItemDisplayContext.NONE, false, matrix, buffer, -1, light, OverlayTexture.NO_OVERLAY, WUTRenderType.cutout(), te.getCurrentFacing());
    	renderSpecialFacingModel(SpecialModels.FAN.getModel(), ItemDisplayContext.NONE, false, matrix, buffer, -1, light, OverlayTexture.NO_OVERLAY, WUTRenderType.cutout(), te.getCurrentFacing().getOpposite());
    	
    	matrix.popPose();
    }
}
