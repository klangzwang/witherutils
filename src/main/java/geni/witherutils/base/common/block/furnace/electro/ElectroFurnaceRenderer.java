package geni.witherutils.base.common.block.furnace.electro;

import com.mojang.blaze3d.vertex.PoseStack;

import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.common.base.AbstractBlockEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;

public class ElectroFurnaceRenderer extends AbstractBlockEntityRenderer<ElectroFurnaceBlockEntity> {

    public ElectroFurnaceRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(ElectroFurnaceBlockEntity te, float partialTick, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight)
    {
        if (te.getLevel() == null)
            return;
        
        matrix.pushPose();
        renderSpecialFacingModel(SpecialModels.EMFURNACE.getModel(), ItemDisplayContext.NONE, false, matrix, buffer,
        		-1, light, OverlayTexture.NO_OVERLAY, RenderType.entityTranslucentEmissive(
				new ResourceLocation("witherutils:textures/block/emissive/blue.png")), te.getCurrentFacing());
        matrix.popPose();
    }
}
