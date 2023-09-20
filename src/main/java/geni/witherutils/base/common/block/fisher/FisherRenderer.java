package geni.witherutils.base.common.block.fisher;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.api.fisher.FisherNotification;
import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.common.base.AbstractBlockEntityRenderer;
import geni.witherutils.core.common.util.FacingUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class FisherRenderer extends AbstractBlockEntityRenderer<FisherBlockEntity> {
	
	public static final ResourceLocation EMISSIVE = new ResourceLocation("witherutils:textures/block/emissive/blue.png");
	
    public FisherRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(FisherBlockEntity tile, float partialTick, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight)
    {
		if(tile == null || !tile.hasLevel() || tile.getMaster() == null)
			return;
		
		if(tile.isMaster())
		{
			matrix.pushPose();
			renderPlatform(tile, partialTick, matrix, buffer, light, overlayLight);
			matrix.popPose();
			
			if(tile.getHookPosition() == null)
				return;

			matrix.pushPose();
			renderHook(tile, partialTick, matrix, buffer, light, overlayLight);
			matrix.popPose();
			
			matrix.pushPose();
			renderRodWire(tile, 0.0f, partialTick, matrix, buffer, light);
			matrix.popPose();

	        matrix.pushPose();
	        for(Direction facing : FacingUtil.FACES_AROUND_Y)
	        {
	        	for (FisherNotification note : tile.getNotification())
	        	{
	                renderText(tile, partialTick, matrix, buffer, overlayLight, Component.translatable(note.getName()).withStyle(ChatFormatting.WHITE), facing, 0.100f);
	        	}
	        }
	        matrix.popPose();
		}
    }
    
    public void renderPlatform(FisherBlockEntity tile, float partialTick, PoseStack matrix, MultiBufferSource buffer, int light, int overlayLight)
	{
    	if(tile.hasEnoughWater() > 42)
    	{
    		float time = tile.getLevel().getLevelData().getGameTime() + partialTick;
    		double offset = Math.sin(time * 0.1D) / 30.0D;
        	matrix.translate(0, offset, 0);
        	
        	offset = Math.sin(time * 0.1D) / 60.0D;

        	if(Math.sin(time * 0.1D) > 0) 
        		matrix.mulPose(Axis.ZN.rotationDegrees((float) offset * 60));
        	else
        		matrix.mulPose(Axis.ZP.rotationDegrees((float) offset * 60));
        	
        	if(Math.sin(time * 0.1D) > 0) 
        		matrix.mulPose(Axis.XN.rotationDegrees((float) offset * 60));
        	else
        		matrix.mulPose(Axis.XP.rotationDegrees((float) offset * 60));
    	}
        Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.FISHER_PLATFORM.getModel(), ItemStack.EMPTY, light, OverlayTexture.NO_OVERLAY, matrix, buffer.getBuffer(RenderType.cutout()));
	}
    
    public void renderHook(FisherBlockEntity tile, float partialTick, PoseStack matrix, MultiBufferSource buffer, int light, int overlayLight)
	{
		Vec3 sourcePos = new Vec3(tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
		Vec3 destPos = new Vec3(tile.getHookPosition().getX(), tile.getHookPosition().getY(), tile.getHookPosition().getZ());

		double x = sourcePos.subtract(destPos).x;
		double z = sourcePos.subtract(destPos).z;
		
		float time = tile.getLevel().getLevelData().getGameTime() + partialTick;
		double offset = Math.sin(time * 1.0D / 8.0D) / 10.0D;
		
    	matrix.translate(x, offset, z);
    	
        Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.FISHER_HOOK.getModel(), ItemStack.EMPTY, light, OverlayTexture.NO_OVERLAY, matrix, buffer.getBuffer(RenderType.cutout()));
	}
	
	public void renderRodWire(FisherBlockEntity tile, float value, float partialTick, PoseStack matrix, MultiBufferSource getter, int p_114710_)
	{
		Vec3 sourcePos = new Vec3(tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
		Vec3 destPos = new Vec3(tile.getHookPosition().getX(), tile.getHookPosition().getY(), tile.getHookPosition().getZ());

		matrix.translate(0.5f, 0, 0.5f);
		
		float f4 = (float) (sourcePos.subtract(destPos).x);
		float f5 = (float) (sourcePos.subtract(destPos).y);
		float f6 = (float) (sourcePos.subtract(destPos).z);
		
		VertexConsumer vertexconsumer = getter.getBuffer(RenderType.lineStrip());
		
		PoseStack.Pose pose = matrix.last();
		
		float time = tile.getLevel().getLevelData().getGameTime() + partialTick;
		double offset = Math.sin(time * 1.0D / 8.0D) / 10.0D;
		matrix.translate(f4, f5 - 1.25f + ((float)offset / 1), f6);
	
		for(int k = 0; k <= 16; ++k)
		{
			stringVertex(-f4, f5 + 0.5f - ((float)offset / 1), -f6, vertexconsumer, pose, fraction(k, 16), fraction(k + 1, 16));
		}
	}
	private static float fraction(int x, int y)
	{
		return (float) x / (float) y;
	}
	private static void stringVertex(float x, float y, float z, VertexConsumer buffer, PoseStack.Pose pose, float p_174124_, float p_174125_)
	{
		float f = x * p_174124_;
		float f1 = y * (p_174124_ * p_174124_ + p_174124_) * 0.5F + 0.25F;
		float f2 = z * p_174124_;
		float f3 = x * p_174125_ - f;
		float f4 = y * (p_174125_ * p_174125_ + p_174125_) * 0.5F + 0.25F - f1;
		float f5 = z * p_174125_ - f2;
		float f6 = Mth.sqrt(f3 * f3 + f4 * f4 + f5 * f5);
		f3 /= f6;
		f4 /= f6;
		f5 /= f6;
		buffer.vertex(pose.pose(), f, f1, f2).color(0, 0, 0, 255).normal(pose.normal(), f3, f4, f5).endVertex();
	}
    
	@SuppressWarnings({ "incomplete-switch", "resource" })
	private void renderText(FisherBlockEntity tile, float partialTicks, PoseStack matrix, MultiBufferSource renderer, int overlayLight, Component text, Direction side, float maxScale)
    {
        matrix.pushPose();
        matrix.translate(0, 0.3, 0);
        
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
    	
        matrix.popPose();
    }
    
    @Override
    public boolean shouldRenderOffScreen(FisherBlockEntity be)
    {
        return true;
    }	
    @Override
    public int getViewDistance()
    {
        return 128;
    }
}
