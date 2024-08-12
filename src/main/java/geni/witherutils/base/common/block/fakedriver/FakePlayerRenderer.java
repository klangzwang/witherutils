package geni.witherutils.base.common.block.fakedriver;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.base.common.base.WitherMachineFakeBlockEntity;
import geni.witherutils.core.common.fakeplayer.FakePlayerNotification;
import geni.witherutils.core.common.util.FacingUtil;
import geni.witherutils.core.common.util.TextureUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class FakePlayerRenderer {

	public static void render(WitherMachineFakeBlockEntity te, float partialTick, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight)
	{
		if(!te.isFakePlayerVisible())
			return;

        float scale = 1.0F;
        float yaw = te.lastLookYaw + (te.lookYaw - te.lastLookYaw) * partialTick;
        float pitch = te.lastLookPitch + (te.lookPitch - te.lastLookPitch) * partialTick;

        matrix.pushPose();
        matrix.translate(0.5, 1.5, 0.5);
        matrix.scale(scale, scale, scale);
        matrix.mulPose(Axis.YP.rotationDegrees(yaw));
        matrix.mulPose(Axis.XP.rotationDegrees(pitch - 14));
        mc.getItemRenderer().renderStatic(new ItemStack(Items.PLAYER_HEAD, 1), ItemDisplayContext.FIXED, light, overlayLight, matrix, buffer, te.getLevel(), (int) te.getBlockPos().asLong());
        matrix.popPose();

        
        
		float[] interactColor;
		
		if(te.canInteract())
			interactColor = new float[] {0.0f, 1.0f, 0.0f};
		else
			interactColor = new float[] {1.0f, 0.0f, 0.0f};

        matrix.pushPose();
		
        matrix.translate(0.5f, 0.5f, 0.5f);
        
        matrix.translate(0.0, 1.0, 0.0);
        matrix.scale(0.6f, 0.6f, 0.6f);

        matrix.mulPose(Axis.YP.rotationDegrees(yaw));
        matrix.mulPose(Axis.XP.rotationDegrees(pitch - 14));
        
        matrix.translate(-0.5f, -0.5f, -0.5f);
		renderCubeColoredTransparent(matrix, buffer, light, interactColor, te.canInteract() ? 0.4f : 0.2f);
        matrix.popPose();
        
        
        
        matrix.pushPose();
        for(Direction facing : FacingUtil.FACES_AROUND_Y)
        {
        	for (FakePlayerNotification note : te.getNotification())
        	{
                renderText(te, partialTick, matrix, buffer, overlayLight, Component.translatable(note.getName()).withStyle(ChatFormatting.WHITE), facing, 0.100f, yaw, pitch);
        	}
        }
        matrix.popPose();
	}
	
    @SuppressWarnings({ "resource", "incomplete-switch" })
	private static void renderText(WitherMachineFakeBlockEntity tile, float partialTicks, PoseStack matrix, MultiBufferSource renderer, int overlayLight, Component text, Direction side, float maxScale, float yaw, float pitch)
    {
        matrix.pushPose();
        matrix.translate(0, 0.5, 0);
        
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
        float scaler = 1.4F;
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
    
    /*
     * 
     * RENDER CUBE TRANSPARENT _ new float[]{1.0f, 0.f, 0.f} example - red
     * 
     */
    public static void renderCubeColoredTransparent(PoseStack matrix, MultiBufferSource getter, int combinedLight, float[] colorIn, float alphaIn)
    {
		float xMin, xMax, yMin, yMax, zMin, zMax = 0;
		
		xMax = 1.984375F;
		yMax = 0.984375F;
		zMax = 1.984375F;
		xMin = 0.015625F;
		zMin = 0.015625F;
		yMin = 0.015625F;

		float red = colorIn[0];
		float green = colorIn[1];
		float blue = colorIn[2];
		
		VertexConsumer buffer = getter.getBuffer(RenderType.translucent());
		TextureAtlasSprite textureAtlasSprite = TextureUtil.getBlockTexture(BuiltInRegistries.BLOCK.getKey(Blocks.MAGENTA_CONCRETE));
		
		float uMin = textureAtlasSprite.getU0();
		float uMax = textureAtlasSprite.getU1();
		float vMin = textureAtlasSprite.getV0();
		float vMax = textureAtlasSprite.getV1();

		float vHeight = vMax - vMin;

		addVertexWithUV(buffer, matrix, xMax, yMax, zMax, uMax, vMin, red, green, blue, alphaIn, combinedLight);
		addVertexWithUV(buffer, matrix, xMax, yMax, zMin, uMin, vMin, red, green, blue, alphaIn, combinedLight);
		addVertexWithUV(buffer, matrix, xMin, yMax, zMin, uMin, vMax, red, green, blue, alphaIn, combinedLight);
		addVertexWithUV(buffer, matrix, xMin, yMax, zMax, uMax, vMax, red, green, blue, alphaIn, combinedLight);

		addVertexWithUV(buffer, matrix, xMax, yMin, zMin, uMax, vMin, red, green, blue, alphaIn, combinedLight);
		addVertexWithUV(buffer, matrix, xMin, yMin, zMin, uMin, vMin, red, green, blue, alphaIn, combinedLight);
		addVertexWithUV(buffer, matrix, xMin, yMax, zMin, uMin, vMin + (vHeight * yMax), red, green, blue, alphaIn, combinedLight);
		addVertexWithUV(buffer, matrix, xMax, yMax, zMin, uMax, vMin + (vHeight * yMax), red, green, blue, alphaIn, combinedLight);

		addVertexWithUV(buffer, matrix, xMax, yMin, zMax, uMin, vMin, red, green, blue, alphaIn, combinedLight);
		addVertexWithUV(buffer, matrix, xMax, yMax, zMax, uMin, vMin + (vHeight * yMax), red, green, blue, alphaIn, combinedLight);
		addVertexWithUV(buffer, matrix, xMin, yMax, zMax, uMax, vMin + (vHeight * yMax), red, green, blue, alphaIn, combinedLight);
		addVertexWithUV(buffer, matrix, xMin, yMin, zMax, uMax, vMin, red, green, blue, alphaIn, combinedLight);

		addVertexWithUV(buffer, matrix, xMax, yMin, zMin, uMin, vMin, red, green, blue, alphaIn, combinedLight);
		addVertexWithUV(buffer, matrix, xMax, yMax, zMin, uMin, vMin + (vHeight * yMax), red, green, blue, alphaIn, combinedLight);
		addVertexWithUV(buffer, matrix, xMax, yMax, zMax, uMax, vMin + (vHeight * yMax), red, green, blue, alphaIn, combinedLight);
		addVertexWithUV(buffer, matrix, xMax, yMin, zMax, uMax, vMin, red, green, blue, alphaIn, combinedLight);

		addVertexWithUV(buffer, matrix, xMin, yMin, zMax, uMin, vMin, red, green, blue, alphaIn, combinedLight);
		addVertexWithUV(buffer, matrix, xMin, yMax, zMax, uMin, vMin + (vHeight * yMax), red, green, blue, alphaIn, combinedLight);
		addVertexWithUV(buffer, matrix, xMin, yMax, zMin, uMax, vMin + (vHeight * yMax), red, green, blue, alphaIn, combinedLight);
		addVertexWithUV(buffer, matrix, xMin, yMin, zMin, uMax, vMin, red, green, blue, alphaIn, combinedLight);

		addVertexWithUV(buffer, matrix, xMax, yMin, zMin, uMax, vMin, red, green, blue, alphaIn, combinedLight);
		addVertexWithUV(buffer, matrix, xMax, yMin, zMax, uMin, vMin, red, green, blue, alphaIn, combinedLight);
		addVertexWithUV(buffer, matrix, xMin, yMin, zMax, uMin, vMax, red, green, blue, alphaIn, combinedLight);
		addVertexWithUV(buffer, matrix, xMin, yMin, zMin, uMax, vMax, red, green, blue, alphaIn, combinedLight);
    }

	private static void addVertexWithUV(VertexConsumer buffer, PoseStack matrixStack, float x, float y, float z, float u, float v, float red, float green, float blue, float alpha, int combinedLight)
	{
		buffer.addVertex(matrixStack.last().pose(), x / 2f, y, z / 2f).setColor(red, green, blue, alpha).setUv(u, v).setUv2(combinedLight, 240).setNormal(1, 0, 0);
	}
}
