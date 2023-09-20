package geni.witherutils.base.common.block.miner.basic;

import com.mojang.blaze3d.vertex.PoseStack;

import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.type.WUTRenderType;
import geni.witherutils.base.common.base.AbstractBlockEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;

public class MinerBasicRenderer extends AbstractBlockEntityRenderer<MinerBasicBlockEntity> {

    public MinerBasicRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(MinerBasicBlockEntity te, float partialTick, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight)
    {
        matrix.pushPose();
    	float rotation = te.prevFanRotation + (te.fanRotation - te.prevFanRotation) * partialTick;
    	renderSpecialFacingModel(SpecialModels.WHEEL.getModel(), ItemDisplayContext.NONE, false, matrix, buffer, -1, light, OverlayTexture.NO_OVERLAY, WUTRenderType.solid(), te.getCurrentFacing(), rotation, 0.15f);
    	renderSpecialFacingModel(SpecialModels.EMMINER.getModel(), ItemDisplayContext.NONE, false, matrix, buffer, -1, light, OverlayTexture.NO_OVERLAY, WUTRenderType.eyes(EMISSIVE), te.getCurrentFacing(), rotation, 0.15f);
    	matrix.popPose();
    }
}
