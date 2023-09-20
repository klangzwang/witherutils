package geni.witherutils.base.common.base;

import com.mojang.blaze3d.vertex.PoseStack;

import geni.witherutils.core.common.blockentity.WitherBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public abstract class AbstractBlockEntityModelRenderer<T extends WitherBlockEntity> implements BlockEntityRenderer<T> {
	
    protected final BlockEntityRendererProvider.Context context;
    
	protected AbstractBlockEntityModelRenderer(BlockEntityRendererProvider.Context context)
	{
        this.context = context;
	}

    @Override
    public void render(T te, float partialTicks, PoseStack matrixStack, MultiBufferSource iRenderTypeBuffer, int combinedLightIn, int combinedOverlayIn)
    {
        matrixStack.pushPose();
        matrixStack.translate(0.5, 1.5, 0.5);
        matrixStack.scale(1f, -1f, -1f);
        renderModel(te, partialTicks, matrixStack, iRenderTypeBuffer, combinedLightIn, combinedOverlayIn);
        matrixStack.popPose();
        renderExtras(te, partialTicks, matrixStack, iRenderTypeBuffer, combinedLightIn, combinedOverlayIn);
    }

    protected void renderModel(T te, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
    }
    protected void renderExtras(T te, float partialTicks, PoseStack matrixStack, MultiBufferSource iRenderTypeBuffer, int combinedLightIn, int combinedOverlayIn)
    {
    }
}
