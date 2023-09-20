package geni.witherutils.base.common.block.creative;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.base.common.base.AbstractBlockEntityRenderer;
import geni.witherutils.core.common.util.TextureUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

public class CreativeGeneratorRenderer extends AbstractBlockEntityRenderer<CreativeGeneratorBlockEntity> {

    public CreativeGeneratorRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(CreativeGeneratorBlockEntity te, float partialTick, PoseStack matrixStack, MultiBufferSource bufferIn, Minecraft mc, ClientLevel level, LocalPlayer player, int combinedLight, int overlayLight)
    {
    	if(te == null)
    		return;
    	
    	if(!te.getInventory().getStackInSlot(0).isEmpty())
    	{
    		matrixStack.pushPose();
    		
    		float time = te.getLevel().getLevelData().getGameTime() + partialTick;
    		double offset = Math.sin(time * 1 / 8.0D) / 10.0D;
    		
    		BakedModel model = mc.getItemRenderer().getModel(te.getInventory().getStackInSlot(0), te.getLevel(), null, 0);

    		matrixStack.translate(0.5D, 1.4D + (te.isCharging() ? offset : 0), 0.5D);
        	if(te.isCharging())
        		matrixStack.mulPose(Axis.YP.rotationDegrees(time * 4.0F * 4.0F));
    		mc.getItemRenderer().render(te.getInventory().getStackInSlot(0), ItemDisplayContext.GROUND, false, matrixStack, bufferIn, combinedLight, overlayLight, model);
    		
    		matrixStack.popPose();
    	}

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
		TextureAtlasSprite textureAtlasSprite = TextureUtil.getBlockTexture(ForgeRegistries.BLOCKS.getKey(Blocks.MAGENTA_CONCRETE));
		
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
		buffer.vertex(matrixStack.last().pose(), x / 2f, y, z / 2f).color(red, green, blue, alpha).uv(u, v).uv2(combinedLight, 240).normal(1, 0, 0).endVertex();
	}
}