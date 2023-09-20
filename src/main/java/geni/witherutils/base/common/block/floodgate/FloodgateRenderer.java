package geni.witherutils.base.common.block.floodgate;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.type.WUTRenderType;
import geni.witherutils.base.common.base.AbstractBlockEntityRenderer;
import geni.witherutils.core.common.math.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.AABB;

public class FloodgateRenderer extends AbstractBlockEntityRenderer<FloodgateBlockEntity> {

	public static final ResourceLocation EMISSIVE = new ResourceLocation("witherutils:textures/block/emissive/blue.png");
	
    public FloodgateRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(FloodgateBlockEntity te, float partialTick, PoseStack matrixStack, MultiBufferSource bufferIn, Minecraft mc, ClientLevel level, LocalPlayer player, int combinedLight, int overlayLight)
    {
    	if(te == null)
    		return;
    	
    	matrixStack.pushPose();
//		renderFaucets(te, partialTick, matrixStack, bufferIn, combinedLight, overlayLight);
		matrixStack.popPose();
		
    	matrixStack.pushPose();
		renderPreview(te, partialTick, matrixStack, bufferIn, combinedLight, overlayLight);
		matrixStack.popPose();
		
		matrixStack.pushPose();
		renderFaucetsFalling(te, partialTick, matrixStack, bufferIn, combinedLight, overlayLight);
		matrixStack.popPose();
    }
    
    public void renderFaucets(FloodgateBlockEntity te, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int light, int overlayLight)
    {
        double x = Vector3.CENTER.x - 0.5;
        double y = Vector3.CENTER.y - 0.5;
        double z = Vector3.CENTER.z - 0.5;

        poseStack.translate(x, y, z);
    	
    	
    	for(Direction facing : Direction.values())
    	{
    		if(facing != Direction.UP && facing != Direction.DOWN)
    		{
    			poseStack.pushPose();

//    			if(te.getFluidTank().getFluidAmount() > 0)
//    			{
                    if(facing == Direction.WEST)
                    {
            			poseStack.translate(0.5, 0.5, 0.5);
                    	poseStack.translate(-0.45F, 0.45F, 0.0F);
                        poseStack.mulPose(Axis.ZP.rotationDegrees(0F));
                    	poseStack.translate(-0.5, -0.5, -0.5);
                    }
                    else if(facing == Direction.EAST)
                    {
            			poseStack.translate(0.5, 0.5, 0.5);
                    	poseStack.translate(0.45F, 0.45F, 0.0F);
                        poseStack.mulPose(Axis.ZP.rotationDegrees(0F));
                    	poseStack.translate(-0.5, -0.5, -0.5);
                    }
                    else if(facing == Direction.NORTH)
                    {
            			poseStack.translate(0.5, 0.5, 0.5);
                    	poseStack.translate(0.0F, 0.45F, -0.45F);
                        poseStack.mulPose(Axis.ZP.rotationDegrees(0F));
                    	poseStack.translate(-0.5, -0.5, -0.5);
                    }
                    else if(facing == Direction.SOUTH)
                    {
            			poseStack.translate(0.5, 0.5, 0.5);
                    	poseStack.translate(0.0F, 0.45F, 0.45F);
                        poseStack.mulPose(Axis.ZP.rotationDegrees(0F));
                    	poseStack.translate(-0.5, -0.5, -0.5);
                    }
                
//    			}

    	    	renderSpecialFacingModel(SpecialModels.FAUCET.getModel(), ItemDisplayContext.NONE, false, poseStack, buffer, 0xFFFFFFFF, light, overlayLight, RenderType.solid(), facing);
    	    	renderSpecialFacingModel(SpecialModels.EMFLOODGATE.getModel(), ItemDisplayContext.NONE, false, poseStack, buffer, 0xFFFFFFFF, light, overlayLight, WUTRenderType.eyes(EMISSIVE), facing);
    	    	
    			poseStack.popPose();
    		}
    	}
    }
    
	public void renderPreview(FloodgateBlockEntity te, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlaylight)
	{
		if(!te.getPreview())
			return;
		
		RenderSystem.lineWidth(Math.max(2.5F, (float) Minecraft.getInstance().getWindow().getWidth() / 1920.0F * 2.5F));
        BlockPos blockpos = te.getBlockPos();
        Color color = new Color(Math.abs(blockpos.getX() % 255), Math.abs(blockpos.getY() % 255), Math.abs(blockpos.getZ() % 255));
        
		VertexConsumer ivertexbuilder = buffer.getBuffer(RenderType.lines());
		matrixStack.translate(-0.0005D, -0.0005D, -0.0005D);
		matrixStack.scale(0.999F, 0.999F, 0.999F);
		
		AABB thebox = new AABB(-te.getScaleX() +1, -te.getScaleY(), -te.getScaleZ() +1, te.getScaleX(), 0, te.getScaleZ());
		LevelRenderer.renderLineBox(matrixStack, ivertexbuilder, thebox, (float) color.getRed() / 255f, (float) color.getGreen() / 255f, (float) color.getBlue() / 0f, 0.5F);
	}
	
    public void renderFaucetsFalling(FloodgateBlockEntity crate, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay)
    {
    	if(crate.getLevel() == null)
    		return;
    	
        Direction facing = crate.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
        if(facing == null)
        	return;

        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.mulPose(Axis.YP.rotationDegrees(facing.get2DDataValue() * -90F + 180F));
        matrixStack.translate(-0.5, -0.5, -0.5);

        matrixStack.pushPose();

        light = LevelRenderer.getLightColor(crate.getLevel(), crate.getBlockPos().above());
        for(int i = 0; i < 4; i++)
        {
            matrixStack.pushPose();
            matrixStack.translate(0.5, 0.5, 0.5);
            matrixStack.mulPose(Axis.YP.rotationDegrees(90F * i));
            matrixStack.translate(0.0, 0.5, 8 * -0.0525);
            
            if(crate.isOpened())
            {
                double progress = Math.min(1.0, Math.max(0, crate.getTimer() - (i * 10) + 5 * partialTicks) / 60.0);
                double angle = (progress * progress) * 90F;
                double rotation = 1.0 - Math.cos(Math.toRadians(angle));
                matrixStack.mulPose(Axis.XP.rotationDegrees((float) rotation * -90F));
            }

            matrixStack.translate(-0.5, -0.525, -0.6);
            Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.FAUCET.getModel(), ItemStack.EMPTY, light, OverlayTexture.NO_OVERLAY, matrixStack, buffer.getBuffer(RenderType.solid()));
            matrixStack.translate(0.5, 0.5, 0.5);
            
            matrixStack.translate(-0.5, -0.5, -0.5);
            matrixStack.translate(0.0, 0.01, 0.0);
            Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.EMFLOODGATE.getModel(), ItemStack.EMPTY, light, OverlayTexture.NO_OVERLAY, matrixStack, buffer.getBuffer(WUTRenderType.eyes(EMISSIVE)));
            matrixStack.translate(0.5, 0.5, 0.5);

            matrixStack.popPose();
        }
        matrixStack.popPose();
    }
}
