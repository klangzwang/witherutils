package geni.witherutils.base.client.render;

import java.util.Random;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.core.common.math.Vector3;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;

public class EffectLib {

    protected static Random rand = new Random();
    private static final float[] r = new float[256];
    private static final float[] randSet = new float[4096];
    private static int randPos = 0;
    
    static
    {
        rand.setSeed(123);
        for (int i = 0; i < r.length; i++)
        {
            r[i] = rand.nextFloat();
        }
        for (int i = 0; i < randSet.length; i++)
        {
            randSet[i] = rand.nextFloat();
        }
    }
    
    protected static float nextFloat()
    {
        return randSet[randPos++ % randSet.length];
    }
    protected static void setRandSeed(int i)
    {
        randPos = i % randSet.length;
    }
    
    public static float interpolate(float a, float b, float d)
    {
        return a+(b-a)*d;
    }
    public static double interpolate(double a, double b, double d)
    {
        return a+(b-a)*d;
    }
    
    public static void renderLightningP2P(PoseStack mStack, MultiBufferSource getter, Vector3 startPos, Vector3 endPos, int segCount, long randSeed, float scaleMod, float deflectMod, boolean autoScale, float segTaper, int colour)
    {
        double height = endPos.y - startPos.y;
        float relScale = autoScale ? (float) height / 128F : 1F;
        float segHeight = (float) height / segCount;
        float[] segXOffset = new float[segCount + 1];
        float[] segZOffset = new float[segCount + 1];
        float xOffSum = 0;
        float zOffSum = 0;

        Random random = new Random(randSeed);
        for (int segment = 0; segment < segCount + 1; segment++) {
            segXOffset[segment] = xOffSum + (float) startPos.x;
            segZOffset[segment] = zOffSum + (float) startPos.z;

            if (segment < segCount) {
                xOffSum += (5 - (random.nextFloat() * 10)) * relScale * deflectMod;
                zOffSum += (5 - (random.nextFloat() * 10)) * relScale * deflectMod;
            }
        }

        xOffSum -= (float) (endPos.x - startPos.x);
        zOffSum -= (float) (endPos.z - startPos.z);

        VertexConsumer builder = getter.getBuffer(RenderType.lightning());
        Matrix4f matrix4f = mStack.last().pose();

        for (int layer = 0; layer < 4; ++layer)
        {
            float red = ((colour >> 16) & 0xFF) / 255F;
            float green = ((colour >> 8) & 0xFF) / 255F;
            float blue = (colour & 0xFF) / 255F;
            float alpha = 0.3F;
            if (layer == 0)
            {
                red = green = blue = alpha = 1;
            }

            for (int seg = 0; seg < segCount; seg++)
            {
                float pos = seg / (float) (segCount);
                float x = segXOffset[seg] - (xOffSum * pos);
                float z = segZOffset[seg] - (zOffSum * pos);

                float nextPos = (seg + 1) / (float) (segCount);
                float nextX = segXOffset[seg + 1] - (xOffSum * nextPos);
                float nextZ = segZOffset[seg + 1] - (zOffSum * nextPos);

                float layerOffsetA = (0.1F + (layer * 0.2F * (1F + segTaper))) * relScale * scaleMod;
                float layerOffsetB = (0.1F + (layer * 0.2F * (1F - segTaper))) * relScale * scaleMod;

                addSegmentQuad(matrix4f, builder, x, (float) startPos.y, z, seg, nextX, nextZ, red, green, blue, alpha, layerOffsetA, layerOffsetB, false, false, true, false, segHeight);    //North Side
                addSegmentQuad(matrix4f, builder, x, (float) startPos.y, z, seg, nextX, nextZ, red, green, blue, alpha, layerOffsetA, layerOffsetB, true, false, true, true, segHeight);      //East Side
                addSegmentQuad(matrix4f, builder, x, (float) startPos.y, z, seg, nextX, nextZ, red, green, blue, alpha, layerOffsetA, layerOffsetB, true, true, false, true, segHeight);      //South Side
                addSegmentQuad(matrix4f, builder, x, (float) startPos.y, z, seg, nextX, nextZ, red, green, blue, alpha, layerOffsetA, layerOffsetB, false, true, false, false, segHeight);    //West Side
            }
        }
    }

    public static double distance(Vector3 vec1, Vector3 vec2)
    {
        return Math.sqrt(distanceSq(vec1, vec2));
    }
    
    public static double distanceSq(Vector3 vec1, Vector3 vec2)
    {
        double dx = vec1.x - vec2.x;
        double dy = vec1.y - vec2.y;
        double dz = vec1.z - vec2.z;
        return dx * dx + dy * dy + dz * dz;
    }
    
    public static void renderLightningP2PRotate(PoseStack mStack, MultiBufferSource getter, Vector3 startPos, Vector3 endPos, int segCount, long randSeed, float scaleMod, float deflectMod, boolean autoScale, float segTaper, int colour)
    {
        mStack.pushPose();
        double length = distance(startPos, endPos);
        Vector3 virtualEndPos = startPos.copy().add(0, length, 0);
        Vector3 dirVec = endPos.copy();
        dirVec.subtract(startPos);
        dirVec.normalize();
        float dirVecXZDist = Mth.sqrt((float)dirVec.x * (float)dirVec.x + (float)dirVec.z * (float)dirVec.z);
        float yRot = (float) (Mth.atan2(dirVec.x, dirVec.z) * (double) (180F / (float) Math.PI));
        float xRot = (float) (Mth.atan2(dirVec.y, dirVecXZDist) * (double) (180F / (float) Math.PI));
        mStack.translate(startPos.x, startPos.y, startPos.z);
        mStack.mulPose(Axis.YP.rotationDegrees(yRot - 90));
        mStack.mulPose(Axis.ZP.rotationDegrees(xRot - 90));
        mStack.translate(-startPos.x, -startPos.y, -startPos.z);
        renderLightningP2P(mStack, getter, startPos, virtualEndPos, segCount, randSeed, scaleMod, deflectMod, autoScale, segTaper, colour);
        mStack.popPose();
    }
    private static void addSegmentQuad(Matrix4f matrix4f, VertexConsumer builder, float x1, float yOffset, float z1, int segIndex, float x2, float z2, float red, float green, float blue, float alpha, float offsetA, float offsetB, boolean invA, boolean invB, boolean invC, boolean invD, float segHeight)
    {
        builder.addVertex(matrix4f, x1 + (invA ? offsetB : -offsetB), yOffset + segIndex * segHeight, z1 + (invB ? offsetB : -offsetB)).setColor(red, green, blue, alpha);
        builder.addVertex(matrix4f, x2 + (invA ? offsetA : -offsetA), yOffset + (segIndex + 1F) * segHeight, z2 + (invB ? offsetA : -offsetA)).setColor(red, green, blue, alpha);
        builder.addVertex(matrix4f, x2 + (invC ? offsetA : -offsetA), yOffset + (segIndex + 1F) * segHeight, z2 + (invD ? offsetA : -offsetA)).setColor(red, green, blue, alpha);
        builder.addVertex(matrix4f, x1 + (invC ? offsetB : -offsetB), yOffset + segIndex * segHeight, z1 + (invD ? offsetB : -offsetB)).setColor(red, green, blue, alpha);
    }
    public static void drawParticle(PoseStack matrixStack, VertexConsumer buffer, TextureAtlasSprite textureAtlasSprite, float r, float g, float b, double x, double y, double z, float scale, int light)
    {
        float xMax, zMax, xMin, zMin, yMin = 0;
		xMax = 1.0F;
		zMax = 1.0F;
		xMin = -1.0F;
		zMin = -1.0F;
		yMin = -0.5F;
		float height = 0.5F;
		float alpha = 0.75F;
		float red = 1F;
		float green = 1F;
		float blue = 1F;
		
		float uMin = textureAtlasSprite.getU1();
		float uMax = textureAtlasSprite.getU0();
		float vMin = textureAtlasSprite.getV1();
		float vMax = textureAtlasSprite.getV0();
		float vHeight = vMax - vMin;

		// north
		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMin, uMax, vMin, red, green, blue, alpha, light);
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMin, uMin, vMin, red, green, blue, alpha, light);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMin, uMin, vMin + (vHeight * height), red, green, blue, alpha, light);
		addVertexWithUV(buffer, matrixStack, xMax, height, zMin, uMax, vMin + (vHeight * height), red, green, blue, alpha, light);

		// south
		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMax, uMin, vMin, red, green, blue, alpha, light);
		addVertexWithUV(buffer, matrixStack, xMax, height, zMax, uMin, vMin + (vHeight * height), red, green, blue, alpha, light);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMax, uMax, vMin + (vHeight * height), red, green, blue, alpha, light);
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMax, uMax, vMin, red, green, blue, alpha, light);

		// east
		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMin, uMin, vMin, red, green, blue, alpha, light);
		addVertexWithUV(buffer, matrixStack, xMax, height, zMin, uMin, vMin + (vHeight * height), red, green, blue, alpha, light);
		addVertexWithUV(buffer, matrixStack, xMax, height, zMax, uMax, vMin + (vHeight * height), red, green, blue, alpha, light);
		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMax, uMax, vMin, red, green, blue, alpha, light);

		// west
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMax, uMin, vMin, red, green, blue, alpha, light);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMax, uMin, vMin + (vHeight * height), red, green, blue, alpha, light);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMin, uMax, vMin + (vHeight * height), red, green, blue, alpha, light);
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMin, uMax, vMin, red, green, blue, alpha, light);
    }
	private static void addVertexWithUV(VertexConsumer buffer, PoseStack matrixStack, float x, float y, float z, float u, float v, float red, float green, float blue, float alpha, int combinedLight)
	{
		buffer.addVertex(matrixStack.last().pose(), x / 2f, y, z / 2f).setColor(red, green, blue, alpha).setUv(u, v).setUv2(combinedLight, 240).setNormal(1, 0, 0);
	}
}
