package geni.witherutils.base.common.block.smarttv;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.base.client.base.AbstractBlockEntityRenderer;
import geni.witherutils.base.client.render.type.WUTRenderType;
import geni.witherutils.core.common.math.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;

public class SmartTVRenderer extends AbstractBlockEntityRenderer<SmartTVBlockEntity> {

    public SmartTVRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(SmartTVBlockEntity te, float partialTick, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight)
    {
		if(te == null || !te.hasLevel() || !te.isPowered())
			return;

	    BlockPos pos = te.getBlockPos();
	    BlockState state = te.getLevel().getBlockState(pos);
	    if(!state.getProperties().contains(SmartTVBlock.FACING))
	        return;
		
	    matrix.pushPose();
	    
		boolean northsouth = state.getValue(SmartTVBlock.FACING) == Direction.NORTH || state.getValue(SmartTVBlock.FACING) == Direction.SOUTH;
		boolean eastwest = state.getValue(SmartTVBlock.FACING) == Direction.EAST || state.getValue(SmartTVBlock.FACING) == Direction.WEST;

	    double x = Vector3.CENTER.x - 0.5F;
	    double y = Vector3.CENTER.y - 0.5F;
	    double z = Vector3.CENTER.z - 0.5F;

	    matrix.translate(x, y, z);
	    
	    if(northsouth)
	    {
			matrix.translate(0.5, 0.75, 0.5);
		    matrix.mulPose(Axis.ZP.rotationDegrees(180));
//			matrix.mulPose(new Quaternionf().rotateXYZ(0, 0, 180));
			matrix.translate(-0.5, -0.75, -0.5);
	    }
	    else if(eastwest)
	    {
			matrix.translate(0.5, 0.75, 0.5);
		    matrix.mulPose(Axis.XP.rotationDegrees(180));
//			matrix.mulPose(new Quaternionf().rotateXYZ(180, 0, 0));
			matrix.translate(-0.5, -0.75, -0.5);
	    }

		matrix.translate(0.5, 0.5, 0.5);
	    matrix.mulPose(Axis.YN.rotationDegrees(90));
//		matrix.mulPose(new Quaternionf().rotateXYZ(0, -90, 0));
		matrix.translate(-0.5, -0.5, -0.5);
		
	    te.tickTexture();
	    
//	    Direction direction = te.getBlockState().getValue(SmartTVBlock.FACING);
//	    SmartTVRenderer.applyDirection(matrixStack, direction);
	    
	    VertexConsumer builder = null;
	    if (te.shouldLoadTexture())
	    {
	        te.loadTexture();
	    }
	    
    	builder = buffer.getBuffer(WUTRenderType.eyes(te.textureLocation));
	    
	    float startX = 0.0f;
	    float startY = 0.0f;
	    float width = te.getWidth();
	    float height = te.getHeight();
	        
	    startX *= 0.0625;
	    startY *= 0.0625;
	    width *= 0.0625;
	    height *= 0.0625;
	        
	    matrix.translate(0.5, 0.5, 0.5);
	    matrix.mulPose(Axis.YP.rotationDegrees(90 - state.getValue(SmartTVBlock.FACING).toYRot()));
//	    matrix.mulPose(new Quaternionf().rotateXYZ(0, 90 - state.getValue(SmartTVBlock.FACING).toYRot(), 0));
	    matrix.translate(-0.5, -0.5, -0.5);

	    matrix.translate(8 * 0.0625, 8 * 0.0625, 8 * 0.0625);
	    matrix.translate(-te.getWidth() / 2 * 0.0625, 0, 0);
	    matrix.translate(0, -0.32, 0.218);
	    
//		this.add(builder, matrixStack, 0.0f, 0.0f, 0.5f, 1.0f, 1.0f, light);
//		this.add(builder, matrixStack, (float)te.width, 0.0f, 0.5f, 0.0f, 1.0f, light);
//		this.add(builder, matrixStack, (float)te.width, (float)te.height, 0.5f, 0.0f, 0.0f, light);
//		this.add(builder, matrixStack, 0.0f, (float)te.height, 0.5f, 1.0f, 0.0f, light);
	    this.add(builder, matrix, startX, startY, 0, 0.f, 0.f, light);
	    this.add(builder, matrix, startX, startY + height, 0, 0.f, 1.f, light);
	    this.add(builder, matrix, startX + width, startY + height, 0, 1.f, 1.f, light);
	    this.add(builder, matrix, startX + width, startY, 0, 1.f, 0.f, light);
	    
	    if (!te.textureWorker.loaded)
	    {
	        final Font fontrenderer = this.context.getFont();
	        matrix.translate(0.1f, 0.95f, -0.01);
	        matrix.scale(0.01f, 0.01f, 0.002f);

	        if (!te.textureWorker.error)
	        {
	            fontrenderer.drawInBatch("Loading...", 0.0f, 0.0f, DyeColor.CYAN.getTextColor(), false, matrix.last().pose(), buffer, DisplayMode.SEE_THROUGH, 0, light);
	        }
	        else
	        {
	            fontrenderer.drawInBatch("Error", 0.0f, 0.0f, DyeColor.RED.getTextColor(), false, matrix.last().pose(), buffer, DisplayMode.SEE_THROUGH, 0, light);
	        }
	    }
	    matrix.popPose();
    }

	private void add(final VertexConsumer renderer, final PoseStack stack, final float x, final float y, final float z, final float u, final float v, final int combinedLight)
	{
	    renderer.addVertex(stack.last().pose(), x, y, z).setColor(1.0f, 1.0f, 1.0f, 1.0f).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(combinedLight).setNormal(1.0f, 0.0f, 0.0f);
	}

	public static void applyDirection(PoseStack matrixStack, Direction direction)
	{
	    int rotation = switch (direction)
	    {
	        case EAST -> 0;
	        case NORTH -> 90;
	        case SOUTH -> 270;
	        case WEST -> 180;
	        default -> 0;
	    };
	    matrixStack.mulPose(Axis.YP.rotationDegrees((float)(rotation - 90)));
	}
	@Override
	public boolean shouldRenderOffScreen(SmartTVBlockEntity te)
	{
	    return te.width > 12 || te.height > 12;
	}
	@Override
	public int getViewDistance()
	{
		return 128;
	}
}
