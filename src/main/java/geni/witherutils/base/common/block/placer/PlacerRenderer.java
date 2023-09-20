package geni.witherutils.base.common.block.placer;

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
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class PlacerRenderer extends AbstractBlockEntityRenderer<PlacerBlockEntity> {
	
	public static final ResourceLocation EMISSIVE = new ResourceLocation("witherutils:textures/block/emissive/blue.png");
	
    public PlacerRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(PlacerBlockEntity tile, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int combinedLight)
    {
        if (tile.getLevel() == null)
            return;

		matrixStack.pushPose();
		renderLight(tile, partialTicks, matrixStack, buffer, light, combinedLight);
        matrixStack.popPose();

		if(tile.getRender())
		{
			renderPreview(tile, partialTicks, matrixStack, buffer, light, combinedLight);
		}
		if(tile.getInventory().getStackInSlot(0) != null)
		{
			renderPlacement(tile, partialTicks, matrixStack, buffer, light, combinedLight, tile.getInventory().getStackInSlot(0));
		}
    }
    
	public void renderPreview(PlacerBlockEntity tile, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlaylight)
	{
		VertexConsumer ivertexbuilder = buffer.getBuffer(RenderType.lines());
		matrixStack.translate(-0.0005D, -0.0005D, -0.0005D);
		matrixStack.scale(0.999F, 0.999F, 0.999F);
		AABB thebox = new AABB(0,0,0,0,0,0);

		matrixStack.pushPose();
		if(tile.getCurrentFacing() == Direction.NORTH)
		{
			thebox = new AABB(0,0,-1,1,1,0);
			if(tile.getRange() == true)
			{
				thebox = new AABB(0,0,-5,1,1,0);
			}
		}
		matrixStack.popPose();

		matrixStack.pushPose();
		if(tile.getCurrentFacing() == Direction.SOUTH)
		{
			thebox = new AABB(0,0,2,1,1,1);
			if(tile.getRange() == true)
			{
				thebox = new AABB(0,0,6,1,1,1);
			}
		}
		matrixStack.popPose();

		matrixStack.pushPose();
		if(tile.getCurrentFacing() == Direction.EAST)
		{
			thebox = new AABB(2,0,0,1,1,1);
			if(tile.getRange() == true)
			{
				thebox = new AABB(6,0,0,1,1,1);
			}
		}
		matrixStack.popPose();

		matrixStack.pushPose();
		if(tile.getCurrentFacing() == Direction.WEST)
		{
			thebox = new AABB(-1,0,0,0,1,1);
			if(tile.getRange() == true)
			{
				thebox = new AABB(-5,0,0,0,1,1);
			}
		}
		matrixStack.popPose();

		matrixStack.pushPose();
		if(tile.getCurrentFacing() == Direction.UP)
		{
			thebox = new AABB(0,2,0,1,1,1);
			if(tile.getRange() == true)
			{
				thebox = new AABB(0,6,0,1,1,1);
			}
		}
		matrixStack.popPose();

		matrixStack.pushPose();
		if(tile.getCurrentFacing() == Direction.DOWN)
		{
			thebox = new AABB(0,-1,0,1,0,1);
			if(tile.getRange() == true)
			{
				thebox = new AABB(0,-5,0,1,0,1);
			}
		}
		matrixStack.popPose();

		LevelRenderer.renderLineBox(matrixStack, ivertexbuilder, thebox, 0F, 0.5F, 0.5F, 0.75F);
	}

	@SuppressWarnings("unused")
	public void renderPlacement(PlacerBlockEntity tile, float v, PoseStack matrixStack, MultiBufferSource getter, int light, int combinedLight, ItemStack stack)
	{
        double x = Vector3.CENTER.x;
        double y = Vector3.CENTER.y;
        double z = Vector3.CENTER.z;

        matrixStack.translate(x, y, z);
		
		float punchout = ((float)tile.getTimer() + 0.01f) / 250;

		Direction facing = tile.getCurrentFacing();
		switch (facing)
		{
			case UP:
				break;
			case DOWN:
				matrixStack.mulPose(Axis.XP.rotationDegrees(180));
				break;
			case NORTH:
				matrixStack.mulPose(Axis.XN.rotationDegrees(90));
				break;
			case SOUTH:
				matrixStack.mulPose(Axis.XP.rotationDegrees(90));
				break;
			case WEST:
				matrixStack.mulPose(Axis.YP.rotationDegrees(90));
				matrixStack.mulPose(Axis.XN.rotationDegrees(90));
				break;
			case EAST:
				matrixStack.mulPose(Axis.YP.rotationDegrees(90));
				matrixStack.mulPose(Axis.XP.rotationDegrees(90));
				break;
		}

//		matrixStack.translate(Vector3.CENTER.x + 0.0f, Vector3.CENTER.x + punchout, Vector3.CENTER.z + 0.0f);
		matrixStack.scale(0.6f, 0.6f, 0.6f);
//		matrixStack.translate(Vector3.CENTER.x + 0.0F, Vector3.CENTER.x + 0.2F, Vector3.CENTER.z + 0.0F);

		Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.NONE, light, OverlayTexture.NO_OVERLAY, matrixStack, getter, tile.getLevel(), 0);
	}
	
	public void renderLight(PlacerBlockEntity tile, float v, PoseStack matrixStack, MultiBufferSource getter, int light, int combinedLight)
	{
		matrixStack.translate(0.5, 0.5, 0.5);
		
		Direction facing = tile.getCurrentFacing();
		switch (facing)
		{
			case UP:
				matrixStack.mulPose(Axis.XP.rotationDegrees(90));
				break;
			case DOWN:
				matrixStack.mulPose(Axis.XP.rotationDegrees(-90));
				break;
			case NORTH:
				break;
			case SOUTH:
				matrixStack.mulPose(Axis.YP.rotationDegrees(180));
				break;
			case WEST:
				matrixStack.mulPose(Axis.YP.rotationDegrees(90));
				break;
			case EAST:
				matrixStack.mulPose(Axis.YP.rotationDegrees(-90));
				break;
		}

		renderEmissiveModel(SpecialModels.EMPLACER.getModel(), ItemDisplayContext.NONE, false, matrixStack, getter, -1, combinedLight, OverlayTexture.NO_OVERLAY);
	}

    public static void renderEmissiveModel(BakedModel model, ItemDisplayContext transformType, boolean leftHanded, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int color, int lightTexture, int overlayTexture)
    {
        matrixStack.pushPose();
        net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrixStack, model, transformType, leftHanded);
        matrixStack.translate(-0.5, -0.5, -0.5);
        if(!model.isCustomRenderer())
        {
            VertexConsumer vertexBuilder = renderTypeBuffer.getBuffer(WUTRenderType.eyes(EMISSIVE));
            Minecraft.getInstance().getItemRenderer().renderModelLists(model, ItemStack.EMPTY, lightTexture, overlayTexture, matrixStack, vertexBuilder);
        }
        matrixStack.popPose();
    }
}
