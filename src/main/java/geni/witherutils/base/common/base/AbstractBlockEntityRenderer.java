package geni.witherutils.base.common.base;

import java.util.EnumMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.core.common.blockentity.WitherBlockEntity;
import geni.witherutils.core.common.math.Vector3;
import geni.witherutils.core.common.util.FacingUtil;
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
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.ClientHooks;

public abstract class AbstractBlockEntityRenderer<T extends WitherBlockEntity> implements BlockEntityRenderer<T> {
	
	protected ItemRenderer itemRenderer;
	protected BlockRenderDispatcher blockRenderer;
    
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
}
