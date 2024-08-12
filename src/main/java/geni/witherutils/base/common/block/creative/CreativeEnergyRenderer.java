package geni.witherutils.base.common.block.creative;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.base.client.base.AbstractBlockEntityRenderer;
import geni.witherutils.core.common.util.TextureUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;

public class CreativeEnergyRenderer extends AbstractBlockEntityRenderer<CreativeEnergyBlockEntity> {

    public CreativeEnergyRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(CreativeEnergyBlockEntity te, float partialTick, PoseStack matrixStack, MultiBufferSource bufferIn, Minecraft mc, ClientLevel level, LocalPlayer player, int combinedLight, int overlayLight)
    {
    	if(te == null)
    		return;

    	
    	
    	
    	
//    	int fps = mc.getFps();
//    	System.out.println(fps);
    	
    	
    	
    	
    	
//        double progress = Math.min(1.0, Math.max(0, te.timer - (1 * 10) + 5 * partialTick) / 60.0);
//        double angle = (progress * progress) * 90F;
//        double rotation = 1.0 - Math.cos(Math.toRadians(angle));
//        System.out.println("TIMER: " + te.timer);
        
        
    	
        double slideProgress = te.getSlideProgress(partialTick);
//        System.out.println("SLIDEPROGRESS: " + slideProgress);
        
        
//        System.out.println("TIMER: " + Math.pow(slideProgress, 2) * ((1.5 + 1) * slideProgress - 1.5)); // Math.pow(te.timer, 4));
        

        
        
        
        
        matrixStack.translate(0.5, 0.5, 0.5);
    	matrixStack.mulPose(Axis.YP.rotationDegrees(360 * (float)slideProgress));
    	matrixStack.translate(-0.5, -0.5, -0.5);
    	
    	
    	
		float xMin, xMax, yMin, yMax, zMin, zMax = 0;
		
		xMax = 1.984375F;
		yMax = 0.984375F;
		zMax = 1.984375F;
		xMin = 0.015625F;
		zMin = 0.015625F;
		yMin = 0.015625F;

		float alpha = 0.5F;
		float red = (0xFFFFFFFF >> 16 & 0xFF) / 255.0F;
		float green = (0xFFFFFFFF >> 8 & 0xFF) / 255.0F;
		float blue = (0xFFFFFFFF & 0xFF) / 255.0F;
		
		VertexConsumer buffer = bufferIn.getBuffer(RenderType.translucent());
		TextureAtlasSprite textureAtlasSprite = TextureUtil.getBlockTexture(BuiltInRegistries.BLOCK.getKey(Blocks.MAGENTA_CONCRETE));
		
		float uMin = textureAtlasSprite.getU0();
		float uMax = textureAtlasSprite.getU1();
		float vMin = textureAtlasSprite.getV0();
		float vMax = textureAtlasSprite.getV1();

		float vHeight = vMax - vMin;

		addVertexWithUV(buffer, matrixStack, xMax, yMax, zMax, uMax, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMax, yMax, zMin, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, yMax, zMin, uMin, vMax, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, yMax, zMax, uMax, vMax, red, green, blue, alpha, combinedLight);

		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMin, uMax, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMin, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, yMax, zMin, uMin, vMin + (vHeight * yMax), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMax, yMax, zMin, uMax, vMin + (vHeight * yMax), red, green, blue, alpha, combinedLight);

		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMax, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMax, yMax, zMax, uMin, vMin + (vHeight * yMax), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, yMax, zMax, uMax, vMin + (vHeight * yMax), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMax, uMax, vMin, red, green, blue, alpha, combinedLight);

		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMin, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMax, yMax, zMin, uMin, vMin + (vHeight * yMax), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMax, yMax, zMax, uMax, vMin + (vHeight * yMax), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMax, uMax, vMin, red, green, blue, alpha, combinedLight);

		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMax, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, yMax, zMax, uMin, vMin + (vHeight * yMax), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, yMax, zMin, uMax, vMin + (vHeight * yMax), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMin, uMax, vMin, red, green, blue, alpha, combinedLight);

		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMin, uMax, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMax, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMax, uMin, vMax, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMin, uMax, vMax, red, green, blue, alpha, combinedLight);
    }

	private void addVertexWithUV(VertexConsumer buffer, PoseStack matrixStack, float x, float y, float z, float u, float v, float red, float green, float blue, float alpha, int combinedLight)
	{
		buffer.addVertex(matrixStack.last().pose(), x / 2f, y, z / 2f).setColor(red, green, blue, alpha).setUv(u, v).setUv2(combinedLight, 240).setNormal(1, 0, 0);
	}
    
    @Override
    public boolean shouldRenderOffScreen(CreativeEnergyBlockEntity p_188185_1_)
    {
        return true;
    }	
    @Override
    public int getViewDistance()
    {
        return 128;
    }
}