package geni.witherutils.base.common.block.fakedriver;

import java.util.Random;

import org.joml.Matrix4f;
import org.joml.Quaternionf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.base.client.ClientSetup;
import geni.witherutils.base.client.base.AbstractBlockEntityRenderer;
import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.RenderSettings;
import geni.witherutils.base.client.render.type.WUTRenderType;
import geni.witherutils.base.common.base.WitherMachineFakeBlockEntity;
import geni.witherutils.base.common.item.fakejob.IFakeJobItem;
import geni.witherutils.core.common.math.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class FakeDriverRenderer extends AbstractBlockEntityRenderer<FakeDriverBlockEntity> {

    public static final Vec3 START1 = new Vec3(.5, .5, .5);
    public static final Vec3 START2 = new Vec3(.5, .5, .5);
    
    private final Random random = new Random();
    
    public FakeDriverRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(FakeDriverBlockEntity tile, float partialTick, PoseStack matrixStack, MultiBufferSource bufferIn, Minecraft mc, ClientLevel level, LocalPlayer player, int combinedLight, int overlayLight)
    {
    	if(tile == null)
    		return;
    	
    	if(tile instanceof WitherMachineFakeBlockEntity te)
    		FakePlayerRenderer.render(te, partialTick, matrixStack, bufferIn, mc, level, player, combinedLight, overlayLight);
    	
    	renderSpecialFacingModel(SpecialModels.EMPROCESSOR.getModel(), ItemDisplayContext.NONE, false, matrixStack, bufferIn, -1, combinedLight, OverlayTexture.NO_OVERLAY, WUTRenderType.eyes(EMISSIVE), tile.getCurrentFacing(), 0, 0f);
    	
//    	VertexConsumer buf = bufferIn.getBuffer(RenderType.translucent());
//    	renderBlueLaser(tile, tile.getBlockPos(), matrixStack, buf);
//    	renderHalo(matrixStack, buf, tile.getCurrentFacing());
//    	renderLasers(tile, tile.getBlockPos(), matrixStack, buf);
    	
        matrixStack.pushPose();
        renderAdapterType(tile, partialTick, bufferIn, matrixStack, mc, combinedLight, bufferIn.getBuffer(RenderType.translucent()), tile.getCurrentFacing());
    	matrixStack.popPose();
    }

    /*
     * 
     * ADAPTERTYPE
     * 
     */
    public void renderAdapterType(FakeDriverBlockEntity tile, float partialTick, MultiBufferSource bufferIn, PoseStack matrixStack, Minecraft mc, int combinedLight, VertexConsumer vconsumer, Direction facing)
    {
		TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(ClientSetup.ADAPTER);

		matrixStack.pushPose();
		
        double x = Vector3.CENTER.x - 0.5F;
        double y = Vector3.CENTER.y - 0.5F;
        double z = Vector3.CENTER.z - 0.5F;

        matrixStack.translate(x, y, z);

        if(facing != null)
        {
            matrixStack.translate(0.5, 0.5, 0.5);
            if(facing == Direction.NORTH)
            {
            	matrixStack.translate(0.0, 0.0, -0.44 + tile.getSlideProgress(partialTick));
            }
            else if(facing == Direction.SOUTH)
            {
            	matrixStack.translate(0.0, 0.0, 0.44 - tile.getSlideProgress(partialTick));
                matrixStack.mulPose(Axis.YN.rotationDegrees(180));
            }
            else if(facing == Direction.EAST)
            {
            	matrixStack.translate(0.44 - tile.getSlideProgress(partialTick), 0.0, 0.0);
                matrixStack.mulPose(Axis.YN.rotationDegrees(90));
            }
            else if(facing == Direction.WEST)
            {
            	matrixStack.translate(-0.44 + tile.getSlideProgress(partialTick), 0.0, 0.0);
                matrixStack.mulPose(Axis.YP.rotationDegrees(90));
            }
            else if(facing == Direction.UP)
            {
            	matrixStack.translate(0.0, 0.44 - tile.getSlideProgress(partialTick), 0.0);
                matrixStack.mulPose(Axis.XP.rotationDegrees(90));
            }
            else if(facing == Direction.DOWN)
            {
            	matrixStack.translate(0.0, -0.44 + tile.getSlideProgress(partialTick), 0.0);
                matrixStack.mulPose(Axis.XN.rotationDegrees(90));
            }
            matrixStack.translate(-0.5, -0.5, -0.5);
        }

        renderSpecialModel(SpecialModels.ADAPTER.getModel(), ItemDisplayContext.NONE, false, matrixStack, bufferIn, -1, combinedLight, OverlayTexture.NO_OVERLAY, WUTRenderType.cutout());

        if(!tile.getInventory().getStackInSlot(0).isEmpty())
        {
            ItemStack stack = tile.getInventory().getStackInSlot(0);
            if (!stack.isEmpty() && stack.getItem() instanceof IFakeJobItem)
            {
        		Matrix4f matrix = matrixStack.last().pose();

            	RenderSettings settings = RenderSettings.builder()
                		.color(120)
                        .alpha(255)
                        .build();
                
                drawAdapterQuad(matrix, mc.renderBuffers().bufferSource().getBuffer(RenderType.translucent()), sprite, settings);
            }
        }
        
        matrixStack.popPose();
    }
	
    @SuppressWarnings("unused")
	private static void drawAdapterQuad(Matrix4f matrix, VertexConsumer buffer, TextureAtlasSprite sprite, RenderSettings settings)
    {
		int b1 = settings.brightness() >> 16 & 65535;
		int b2 = settings.brightness() & 65535;

		float xMax, yMax, zMax, xMin, yMin, zMin;
		
		xMin = 0.0F;
		xMax = 1.0F;
		yMin = 0.0F;
		yMax = 1.0F;
		zMin = 0.0F;
		zMax = 1.0F;
		
		float uMin = sprite.getU0();
		float uMax = sprite.getU1();
		float vMin = sprite.getV0();
		float vMax = sprite.getV1();
		
		vt(buffer, matrix, xMax, yMin, zMin, uMax, vMin, b1, b2, settings.r(), settings.g(), settings.b(), settings.a());
		vt(buffer, matrix, xMin, yMin, zMin, uMin, vMin, b1, b2, settings.r(), settings.g(), settings.b(), settings.a());
		vt(buffer, matrix, xMin, yMax, zMin, uMin, vMax, b1, b2, settings.r(), settings.g(), settings.b(), settings.a());
		vt(buffer, matrix, xMax, yMax, zMin, uMax, vMax, b1, b2, settings.r(), settings.g(), settings.b(), settings.a());
	}

//    public float[] getColor(ItemStack stack)
//    {
//    	if(stack.getItem() instanceof IFakeJobItem)
//    	{
//			FakeJobAbstractItem adapItem = (FakeJobAbstractItem) stack.getItem();
//			FakeJobAbstractItem adapter = adapItem.getFakeJob();
//			return adapter.getColor();
//		}
//    	return null;
//    }

    /*
     * 
     * JITTERLASERS
     * 
     */
    @SuppressWarnings({ "resource", "deprecation", "unused" })
	private void renderLasers(FakeDriverBlockEntity tile, BlockPos pos, PoseStack matrixStack, VertexConsumer builder)
    {
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(ClientSetup.LASERBEAMS[random.nextInt(4)]);

        Direction direction = tile.getCurrentFacing();
        
        float destX = 0.5f + (
        		tile.getCurrentFacing() == Direction.UP || tile.getCurrentFacing() == Direction.DOWN
        		? 0.0f : direction.getStepX() * 10.0f);
        float destY = 0.5f + (
        		tile.getCurrentFacing() == Direction.UP || tile.getCurrentFacing() == Direction.DOWN
        		? direction.getStepY() * 10.0f : 0.0f);
        float destZ = 0.5f + (
        		tile.getCurrentFacing() == Direction.UP || tile.getCurrentFacing() == Direction.DOWN
        		? 0.0f : direction.getStepZ() * 10.0f);

        int tex = pos.getX();
        int tey = pos.getY();
        int tez = pos.getZ();

        float dynamic = tile.getLevel().random.nextFloat() / 2;

        Vec3 player = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().add(-tex, -tey, -tez);
        Vec3 end = new Vec3(
        		destX - 0.25f + dynamic,
        		destY - 0.25f + dynamic,
        		destZ - 0.25f + dynamic);

        Vec3 middle = new Vec3(
        		jitter(0.05, new Vec3(0.25f + dynamic, 0.25f + dynamic, 0.25f + dynamic).x(), end.x()),
        		jitter(0.05, new Vec3(0.25f + dynamic, 0.25f + dynamic, 0.25f + dynamic).y(), end.y()),
        		jitter(0.05, new Vec3(0.25f + dynamic, 0.25f + dynamic, 0.25f + dynamic).z(), end.z()));

        RenderSettings settingsLaser = RenderSettings.builder()
        		.color(255, 255, 255)
                .width(.04f)
                .alpha(128)
                .build();
        
        drawBeam(matrixStack, builder, sprite, new Vec3(.5, .5, .5), middle, player, settingsLaser);
        drawBeam(matrixStack, builder, sprite, middle, end, player, settingsLaser);

        end = new Vec3(destX, destY, destZ);
        
        middle = new Vec3(jitter(0.0, new Vec3(.5, .5, .5).x(), end.x()), jitter(0.0, new Vec3(.5, .5, .5).y(), end.y()), jitter(0.0, START2.z(), end.z()));
        settingsLaser = RenderSettings.builder()
        		.color(255, 128, 255)
                .width(.24f)
                .alpha(196)
                .build();
        
        drawBeam(matrixStack, builder, sprite, new Vec3(.5, .5, .5), middle, player, settingsLaser);
        drawBeam(matrixStack, builder, sprite, middle, end, player, settingsLaser);
    }

    private double jitter(double startupFactor, double a1, double a2)
    {
        return (a1 + a2) / 2.0 + (random.nextDouble() * 2.0 - 1.0) * startupFactor;
    }

    /*
     * 
     * HALO
     * 
     */
    @SuppressWarnings("unused")
	private void renderHalo(PoseStack matrixStack, VertexConsumer buffer, Direction facing)
    {
        matrixStack.pushPose();
        
    	RenderSettings billBoardSettings = RenderSettings.builder()
        		.color(255, 255, 255)
                .alpha(235)
                .build();

        double x = Vector3.CENTER.x;
        double y = Vector3.CENTER.y;
        double z = Vector3.CENTER.z;

        matrixStack.translate(x, y, z);
    	
    	Vec3 offset = new Vec3(0, 0, 0);

        if(facing != null)
        {
            matrixStack.translate(0.5, 0.5, 0.5);
            if(facing == Direction.NORTH)
            {
            	offset = new Vec3(0.0, 0.0, -0.4);
            }
            else if(facing == Direction.SOUTH)
            {
            	offset = new Vec3(0.0, 0.0, 0.4);
            }
            else if(facing == Direction.EAST)
            {
            	offset = new Vec3(0.4, 0.0, 0.0);
            }
            else if(facing == Direction.WEST)
            {
            	offset = new Vec3(-0.4, 0.0, 0.0);
            }
            else if(facing == Direction.UP)
            {
            	offset = new Vec3(0.0, 0.4, 0.0);
            }
            else if(facing == Direction.DOWN)
            {
            	offset = new Vec3(0.0, -0.4, 0.0);
            }
            matrixStack.translate(-0.5, -0.5, -0.5);
        }

        renderSplitBillboard(matrixStack, buffer, 0.65f, offset, ClientSetup.REDHALO, billBoardSettings);
        
        matrixStack.popPose();
    }
    public static void renderSplitBillboard(PoseStack matrixStack, VertexConsumer buffer, float scale, Vec3 offset, ResourceLocation texture, RenderSettings settings)
    {
		int b1 = LightTexture.FULL_SKY;
		int b2 = LightTexture.FULL_BLOCK;
		
		TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);
		
		matrixStack.pushPose();

		matrixStack.translate(offset.x, offset.y, offset.z);
		
//		matrixStack.translate(
//				jitter(0.02, new Vec3(.5, .5, .5).x(), 0.5f),
//				jitter(0.02, new Vec3(.5, .5, .5).x(), 0.5f), 0.5 - offset.z);
		
		rotateToPlayer(matrixStack);
		
		Matrix4f matrix = matrixStack.last().pose();

		float u0 = sprite.getU0();
		float v0 = sprite.getV0();
		float u1 = sprite.getU1();
		float v1 = sprite.getV1();
		float um = (u0 + u1) / 2f;
		float vm = (v0 + v1) / 2f;

		buffer.addVertex(matrix, -scale, -scale, 0.0f).setColor(settings.r(), settings.g(), settings.b(), settings.a()).setUv(u0, v0).setUv2(b1, b2).setNormal(1, 0, 0);
		buffer.addVertex(matrix, -scale, 0.0f, 0.0f).setColor(settings.r(), settings.g(), settings.b(), settings.a()).setUv(u0, vm).setUv2(b1, b2).setNormal(1, 0, 0);
		buffer.addVertex(matrix, 0.0f, 0.0f, 0.0f).setColor(settings.r(), settings.g(), settings.b(), settings.a()).setUv(um, vm).setUv2(b1, b2).setNormal(1, 0, 0);
		buffer.addVertex(matrix, 0.0f, -scale, 0.0f).setColor(settings.r(), settings.g(), settings.b(), settings.a()).setUv(um, v0).setUv2(b1, b2).setNormal(1, 0, 0);

		buffer.addVertex(matrix, 0.0f, 0.0f, 0.0f).setColor(settings.r(), settings.g(), settings.b(), settings.a()).setUv(um, vm).setUv2(b1, b2).setNormal(1, 0, 0);
		buffer.addVertex(matrix, 0.0f, scale, 0.0f).setColor(settings.r(), settings.g(), settings.b(), settings.a()).setUv(um, v1).setUv2(b1, b2).setNormal(1, 0, 0);
		buffer.addVertex(matrix, scale, scale, 0.0f).setColor(settings.r(), settings.g(), settings.b(), settings.a()).setUv(u1, v1).setUv2(b1, b2).setNormal(1, 0, 0);
		buffer.addVertex(matrix, scale, 0.0f, 0.0f).setColor(settings.r(), settings.g(), settings.b(), settings.a()).setUv(u1, vm).setUv2(b1, b2).setNormal(1, 0, 0);

		buffer.addVertex(matrix, 0.0f, -scale, 0.0f).setColor(settings.r(), settings.g(), settings.b(), settings.a()).setUv(um, v0).setUv2(b1, b2).setNormal(1, 0, 0);
		buffer.addVertex(matrix, 0.0f, 0.0f, 0.0f).setColor(settings.r(), settings.g(), settings.b(), settings.a()).setUv(um, vm).setUv2(b1, b2).setNormal(1, 0, 0);
		buffer.addVertex(matrix, scale, 0.0f, 0.0f).setColor(settings.r(), settings.g(), settings.b(), settings.a()).setUv(u1, vm).setUv2(b1, b2).setNormal(1, 0, 0);
		buffer.addVertex(matrix, scale, -scale, 0.0f).setColor(settings.r(), settings.g(), settings.b(), settings.a()).setUv(u1, v0).setUv2(b1, b2).setNormal(1, 0, 0);

		buffer.addVertex(matrix, -scale, 0.0f, 0.0f).setColor(settings.r(), settings.g(), settings.b(), settings.a()).setUv(u0, vm).setUv2(b1, b2).setNormal(1, 0, 0);
		buffer.addVertex(matrix, -scale, scale, 0.0f).setColor(settings.r(), settings.g(), settings.b(), settings.a()).setUv(u0, v1).setUv2(b1, b2).setNormal(1, 0, 0);
		buffer.addVertex(matrix, 0.0f, scale, 0.0f).setColor(settings.r(), settings.g(), settings.b(), settings.a()).setUv(um, v1).setUv2(b1, b2).setNormal(1, 0, 0);
		buffer.addVertex(matrix, 0.0f, 0.0f, 0.0f).setColor(settings.r(), settings.g(), settings.b(), settings.a()).setUv(um, vm).setUv2(b1, b2).setNormal(1, 0, 0)
		;
		matrixStack.popPose();
    }
    @SuppressWarnings("resource")
	public static void rotateToPlayer(PoseStack poseStack)
    {
    	Quaternionf rotation = Minecraft.getInstance().gameRenderer.getMainCamera().rotation();
    	poseStack.mulPose(rotation);
	}
    
    /*
     * 
     * LASERBLUE
     * 
     */
    @SuppressWarnings({ "resource", "deprecation", "unused" })
	private void renderBlueLaser(FakeDriverBlockEntity tile, BlockPos pos, PoseStack matrixStack, VertexConsumer builder)
    {
        Direction direction = tile.getCurrentFacing();
        
        float destX = 0.5f + (
        		tile.getCurrentFacing() == Direction.UP || tile.getCurrentFacing() == Direction.DOWN
        		? 0.0f : direction.getStepX() * 10.0f);
        float destY = 0.5f + (
        		tile.getCurrentFacing() == Direction.UP || tile.getCurrentFacing() == Direction.DOWN
        		? direction.getStepY() * 10.0f : 0.0f);
        float destZ = 0.5f + (
        		tile.getCurrentFacing() == Direction.UP || tile.getCurrentFacing() == Direction.DOWN
        		? 0.0f : direction.getStepZ() * 10.0f);
        
        int tex = pos.getX();
        int tey = pos.getY();
        int tez = pos.getZ();
        
        Vec3 player = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().add(-tex, -tey, -tez);
        Vec3 end = new Vec3(destX, destY, destZ);

        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(ClientSetup.LASER);

        matrixStack.pushPose();
        
        RenderSettings settingsLaser = RenderSettings.builder()
        		.color(255, 0, 0)
                .width(.05f)
                .alpha(255)
                .build();
        
        drawBeam(matrixStack, builder, sprite, new Vec3(.5, .5, .5), end, player, settingsLaser);
        
        matrixStack.popPose();
    }
    
    public static void drawBeam(PoseStack matrix, VertexConsumer builder, TextureAtlasSprite sprite, Vec3 S, Vec3 E, Vec3 P, float width)
    {
        Vec3 PS = S.subtract(P);
        Vec3 SE = E.subtract(S);

        Vec3 normal = PS.cross(SE).normalize();

        Vec3 half = normal.multiply(width, width, width);
        Vec3 p1 = S.add(half);
        Vec3 p2 = S.subtract(half);
        Vec3 p3 = E.add(half);
        Vec3 p4 = E.subtract(half);

        RenderSettings defaultSettings = RenderSettings.builder()
        		.color(255, 255, 255)
                .alpha(128)
                .build();
        
        drawQuad(matrix.last().pose(), builder, sprite, p1, p3, p4, p2, defaultSettings);
    }
    
    public static void drawBeam(PoseStack poseStack, VertexConsumer buffer, TextureAtlasSprite sprite, Vec3 S, Vec3 E, Vec3 P, RenderSettings settings)
    {
        Vec3 PS = S.subtract(P);
        Vec3 SE = E.subtract(S);

        Vec3 normal = PS.cross(SE).normalize();

        Vec3 half = normal.multiply(settings.width(), settings.width(), settings.width());
        Vec3 p1 = S.add(half);
        Vec3 p2 = S.subtract(half);
        Vec3 p3 = E.add(half);
        Vec3 p4 = E.subtract(half);

        drawQuad(poseStack.last().pose(), buffer, sprite, p1, p3, p4, p2, settings);
    }
    
    private static void drawQuad(Matrix4f matrix, VertexConsumer buffer, TextureAtlasSprite sprite, Vec3 p1, Vec3 p2, Vec3 p3, Vec3 p4, RenderSettings settings)
    {
		int b1 = settings.brightness() >> 16 & 65535;
		int b2 = settings.brightness() & 65535;

		vt(buffer, matrix, (float) p1.x(), (float) p1.y(), (float) p1.z(), sprite.getU0(), sprite.getV0(), b1, b2, settings.r(), settings.g(), settings.b(), settings.a());
		vt(buffer, matrix, (float) p2.x(), (float) p2.y(), (float) p2.z(), sprite.getU1(), sprite.getV0(), b1, b2, settings.r(), settings.g(), settings.b(), settings.a());
		vt(buffer, matrix, (float) p3.x(), (float) p3.y(), (float) p3.z(), sprite.getU1(), sprite.getV1(), b1, b2, settings.r(), settings.g(), settings.b(), settings.a());
		vt(buffer, matrix, (float) p4.x(), (float) p4.y(), (float) p4.z(), sprite.getU0(), sprite.getV1(), b1, b2, settings.r(), settings.g(), settings.b(), settings.a());
	}

    private static void vt(VertexConsumer renderer, Matrix4f matrix, float x, float y, float z, float u, float v, int lu, int lv, int r, int g, int b, int a)
    {
    	renderer
    	.addVertex(matrix, x, y, z)
    	.setColor(r, g, b, a)
    	.setUv(u, v)
    	.setLight(lu)
    	.setNormal(1, 0, 0);
	}
    
    @Override
    public boolean shouldRenderOffScreen(FakeDriverBlockEntity p_188185_1_)
    {
        return true;
    }	
    @Override
    public int getViewDistance()
    {
        return 128;
    }
}
