package geni.witherutils.base.client.base;

import java.util.EnumMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.core.common.blockentity.WitherBlockEntity;
import geni.witherutils.core.common.math.Vector3;
import geni.witherutils.core.common.util.FacingUtil;
import geni.witherutils.core.common.util.TextureUtil;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public abstract class AbstractBlockEntityRenderer<T extends WitherBlockEntity> implements BlockEntityRenderer<T> {
	
	protected ItemRenderer itemRenderer;
	protected BlockRenderDispatcher blockRenderer;
    
    public static final ResourceLocation EMISSIVE = WitherUtilsRegistry.loc("textures/block/emissive/blue.png");
	
	private static final Map<Direction, Quaternionf> ROTATE_FOR_FACING = Util.make(
			new EnumMap<>(Direction.class), m -> {
				for(Direction facing : FacingUtil.BY_HORIZONTAL_INDEX)
					m.put(facing, new Quaternionf().rotateY(Mth.DEG_TO_RAD*(180-facing.toYRot())));
			}
	);

	protected static void rotateForFacingNoCentering(PoseStack stack, Direction facing)
	{
		stack.mulPose(ROTATE_FOR_FACING.get(facing));
	}

	protected static void rotateForFacing(PoseStack stack, Direction facing)
	{
		stack.translate(0.5, 0.5, 0.5);
		rotateForFacingNoCentering(stack, facing);
		stack.translate(-0.5, -0.5, -0.5);
	}
 
    protected final BlockEntityRendererProvider.Context context;
    
	protected AbstractBlockEntityRenderer(BlockEntityRendererProvider.Context context)
	{
        this.context = context;

        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
        this.blockRenderer = Minecraft.getInstance().getBlockRenderer();
	}

    @Override
    public final void render(T te, float partialTick, PoseStack matrix, MultiBufferSource buffer, int light, int overlayLight)
    {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        
        if (player != null)
        {
            render(te, partialTick, matrix, buffer, mc, player.clientLevel, player, light, overlayLight);
        }
    }

    public abstract void render(T te, float partialTick, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight);

	/*
	 * 
	 * NONFACING
	 * 
	 */
	public static void renderSpecialModel(BakedModel model, ItemDisplayContext transformType, boolean leftHanded, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int color, int lightTexture, int overlayTexture, RenderType renderType)
    {
        matrixStack.pushPose();
        ClientHooks.handleCameraTransforms(matrixStack, model, transformType, leftHanded);
        
        if(!model.isCustomRenderer())
        {
            VertexConsumer vertexBuilder = renderTypeBuffer.getBuffer(renderType);
            Minecraft.getInstance().getItemRenderer().renderModelLists(model, ItemStack.EMPTY, lightTexture, overlayTexture, matrixStack, vertexBuilder);
        }
        matrixStack.popPose();
    }

	/*
	 * 
	 * FACING
	 * 
	 */
    public static void renderSpecialFacingModel(BakedModel model, ItemDisplayContext transformType, boolean leftHanded, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int color, int lightTexture, int overlayTexture, RenderType renderType, @Nullable Direction facing)
    {
        ClientHooks.handleCameraTransforms(matrixStack, model, transformType, leftHanded);
        if(!model.isCustomRenderer())
        {
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
                }
                else if(facing == Direction.SOUTH)
                {
                    matrixStack.mulPose(Axis.YN.rotationDegrees(180));
                }
                else if(facing == Direction.EAST)
                {
                    matrixStack.mulPose(Axis.YN.rotationDegrees(90));
                }
                else if(facing == Direction.WEST)
                {
                    matrixStack.mulPose(Axis.YP.rotationDegrees(90));
                }
                else if(facing == Direction.UP)
                {
                    matrixStack.mulPose(Axis.XP.rotationDegrees(90));
                }
                else if(facing == Direction.DOWN)
                {
                    matrixStack.mulPose(Axis.XN.rotationDegrees(90));
                }
                matrixStack.translate(-0.5, -0.5, -0.5);
            }
            VertexConsumer vertexBuilder = renderTypeBuffer.getBuffer(renderType);
            Minecraft.getInstance().getItemRenderer().renderModelLists(model, ItemStack.EMPTY, lightTexture, overlayTexture, matrixStack, vertexBuilder);
            
            matrixStack.popPose();
        }
    }
    
	/*
	 * 
	 * FACING ROTATION OFFSET
	 * 
	 */
    public static void renderSpecialFacingModel(BakedModel model, ItemDisplayContext transformType, boolean leftHanded, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int color, int lightTexture, int overlayTexture, RenderType renderType, @Nullable Direction facing, float rotation, float offset)
    {
        ClientHooks.handleCameraTransforms(matrixStack, model, transformType, leftHanded);
        if(!model.isCustomRenderer())
        {
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
                	matrixStack.translate(0.0, 0.0, -offset);
                }
                else if(facing == Direction.SOUTH)
                {
                	matrixStack.translate(0.0, 0.0, offset);
                    matrixStack.mulPose(Axis.YN.rotationDegrees(180));
                }
                else if(facing == Direction.EAST)
                {
                	matrixStack.translate(offset, 0.0, 0.0);
                    matrixStack.mulPose(Axis.YN.rotationDegrees(90));
                }
                else if(facing == Direction.WEST)
                {
                	matrixStack.translate(-offset, 0.0, 0.0);
                    matrixStack.mulPose(Axis.YP.rotationDegrees(90));
                }
                else if(facing == Direction.UP)
                {
                	matrixStack.translate(0.0, offset, 0.0);
                    matrixStack.mulPose(Axis.XP.rotationDegrees(90));
                }
                else if(facing == Direction.DOWN)
                {
                	matrixStack.translate(0.0, -offset, 0.0);
                    matrixStack.mulPose(Axis.XN.rotationDegrees(90));
                }
                matrixStack.mulPose(Axis.XN.rotationDegrees(rotation));
                matrixStack.translate(-0.5, -0.5, -0.5);
            }
            VertexConsumer vertexBuilder = renderTypeBuffer.getBuffer(renderType);
            Minecraft.getInstance().getItemRenderer().renderModelLists(model, ItemStack.EMPTY, lightTexture, overlayTexture, matrixStack, vertexBuilder);
            
            matrixStack.popPose();
        }
    }
    
    @Override
    public boolean shouldRenderOffScreen(T te)
    {
        return true;
    }
    @Override
    public int getViewDistance()
    {
        return 128;
    }

    public static float getPartialTick()
    {
        return Minecraft.getInstance().getFps();
    }

    @SuppressWarnings("incomplete-switch")
	public static void rotate(PoseStack matrix, Direction facing, float north, float south, float west, float east)
    {
        switch (facing)
        {
            case NORTH -> matrix.mulPose(Axis.YP.rotationDegrees(north));
            case SOUTH -> matrix.mulPose(Axis.YP.rotationDegrees(south));
            case WEST -> matrix.mulPose(Axis.YP.rotationDegrees(west));
            case EAST -> matrix.mulPose(Axis.YP.rotationDegrees(east));
        }
    }
  
    public static float rotateMatrixForDirection(PoseStack matrixStack, Direction facing)
    {
        float yRotation;
        switch (facing)
        {
            case UP -> {
                yRotation = 0;
                matrixStack.mulPose(Axis.XP.rotationDegrees(90f));
                matrixStack.translate(0, -1, -1);
            }
            case DOWN -> {
                yRotation = 0;
                matrixStack.mulPose(Axis.XP.rotationDegrees(-90f));
                matrixStack.translate(0, -1, 1);
            }
            case NORTH -> yRotation = 0;
            case EAST -> yRotation = 90;
            case SOUTH -> yRotation = 180;
            default -> yRotation = 270;
        }
        matrixStack.mulPose(Axis.YP.rotationDegrees(yRotation));
        return yRotation;
    }
    
    /*
     * 
     * RENDER CUBE TRANSPARENT _ new float[]{1.0f, 0.f, 0.f} example - red
     * 
     */
    public void renderCubeColoredTransparent(PoseStack matrix, MultiBufferSource getter, int combinedLight, float[] colorIn, float alphaIn)
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
	
    /*
     * 
     * FLUIDTANK
     * 
     */
	public void renderFluidTank(IFluidHandler fluidHandler, BlockEntity te, PoseStack matrixStack, MultiBufferSource renderer, int combinedLight, float alpha, float minX, float minY, float maxX, float minZ, float maxZ, float maxY)
	{
		if(!fluidHandler.getFluidInTank(0).isEmpty())
		{
			float fluidLevel = fluidHandler.getFluidInTank(0).getAmount();
			if(fluidLevel > 0)
			{
				float height = (0.96875F / fluidHandler.getTankCapacity(0)) * fluidHandler.getFluidInTank(0).getAmount();
				render(te, matrixStack, renderer, combinedLight, fluidHandler.getFluidInTank(0), alpha, minX, minY, minZ, maxX, maxY, maxZ, height);
			}
		}
	}
	public void render(BlockEntity te, PoseStack matrixStack, MultiBufferSource renderer, int combinedLight, FluidStack fluidStack, float alpha, float minX, float minY, float minZ, float maxX, float maxHeight, float maxZ, float height)
	{
		var fluidExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
		int fluidColor = fluidExtensions.getTintColor();
		
		TextureAtlasSprite fluidStillSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidExtensions.getStillTexture());

		float red = (fluidColor >> 16 & 0xFF) / 255.0F;
		float green = (fluidColor >> 8 & 0xFF) / 255.0F;
		float blue = (fluidColor & 0xFF) / 255.0F;
		
		if(height >= maxHeight)
		{
			height = maxHeight;
		}

		float uMin = fluidStillSprite.getU0();
		float uMax = fluidStillSprite.getU1();
		float vMin = fluidStillSprite.getV0();
		float vMax = fluidStillSprite.getV1();

		float vHeight = vMax - vMin;
		
		VertexConsumer buffer = renderer.getBuffer(RenderType.translucent());
		
		addVertexWithUV(buffer, matrixStack, maxX, height, maxZ, uMax, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, maxX, height, minZ, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, minX, height, minZ, uMin, vMax, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, minX, height, maxZ, uMax, vMax, red, green, blue, alpha, combinedLight);

		addVertexWithUV(buffer, matrixStack, maxX, minY, minZ, uMax, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, minX, minY, minZ, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, minX, height, minZ, uMin, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, maxX, height, minZ, uMax, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);

		addVertexWithUV(buffer, matrixStack, maxX, minY, maxZ, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, maxX, height, maxZ, uMin, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, minX, height, maxZ, uMax, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, minX, minY, maxZ, uMax, vMin, red, green, blue, alpha, combinedLight);

		addVertexWithUV(buffer, matrixStack, maxX, minY, minZ, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, maxX, height, minZ, uMin, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, maxX, height, maxZ, uMax, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, maxX, minY, maxZ, uMax, vMin, red, green, blue, alpha, combinedLight);

		addVertexWithUV(buffer, matrixStack, minX, minY, maxZ, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, minX, height, maxZ, uMin, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, minX, height, minZ, uMax, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, minX, minY, minZ, uMax, vMin, red, green, blue, alpha, combinedLight);

		addVertexWithUV(buffer, matrixStack, maxX, minY, minZ, uMax, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, maxX, minY, maxZ, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, minX, minY, maxZ, uMin, vMax, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, minX, minY, minZ, uMax, vMax, red, green, blue, alpha, combinedLight);
	}

	/*
	 * 
	 * ADDVERTEX
	 * 
	 */
	private void addVertexWithUV(VertexConsumer buffer, PoseStack matrixStack, float x, float y, float z, float u, float v, float red, float green, float blue, float alpha, int combinedLight)
	{
		buffer.addVertex(matrixStack.last().pose(), x / 2f, y, z / 2f).setColor(red, green, blue, alpha).setUv(u, v).setUv2(combinedLight, 240).setNormal(1, 0, 0);
	}
}
