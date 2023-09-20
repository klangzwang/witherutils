package geni.witherutils.base.client;

import java.util.function.Consumer;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexSorting;
import com.mojang.math.Axis;

import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.type.WUTRenderType;
import geni.witherutils.base.common.block.battery.core.CoreBlock;
import geni.witherutils.base.common.block.battery.core.CoreBlockEntity;
import geni.witherutils.core.common.helper.MathHelper;
import geni.witherutils.core.common.helper.RenderingHelper;
import geni.witherutils.core.common.math.Vector2i;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ForgeHooksClient;

@SuppressWarnings("resource")
public class ClientOverlayRenderer {

	public static void renderHUD(GuiGraphics graphics, float partialTicks)
    {
        double dist = 6.0;
        PoseStack matrixStack = graphics.pose();

        HitResult mouseOver = Minecraft.getInstance().hitResult;

        Player entity = Minecraft.getInstance().player;
        Vec3 start  = entity.getEyePosition(partialTicks);
        Vec3 vec31 = entity.getViewVector(partialTicks);
        Vec3 end = start.add(vec31.x * dist, vec31.y * dist, vec31.z * dist);

        ClipContext context = new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity);
        mouseOver = entity.getCommandSenderWorld().clip(context);
        
        if (mouseOver == null)
        {
            return;
        }
        
//        matrixStack.pushPose();
//        renderSoulFireOverlay(graphics, mouseOver, entity, partialTicks);
//        matrixStack.popPose();
        
        matrixStack.pushPose();
        renderCoreOverlay(graphics, mouseOver, partialTicks);
        matrixStack.popPose();
    }
    
    private static void setupOverlayRendering(float sw, float sh)
    {
        RenderSystem.clear(GL11.GL_DEPTH_BUFFER_BIT, true);
        Matrix4f ortho = (new Matrix4f()).setOrtho(0.0F, sw, sh, 0.0F, 1000.0F, ForgeHooksClient.getGuiFarPlane());
        RenderSystem.setProjectionMatrix(ortho, VertexSorting.ORTHOGRAPHIC_Z);
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.setIdentity();
        posestack.translate(0.0F, 0.0F, 1000.0F - ForgeHooksClient.getGuiFarPlane());
        RenderSystem.applyModelViewMatrix();
    }
	
	/*
	 * 
	 * SOUL FIRE OVERLAY
	 * 
	 */
//	private static void renderSoulFireOverlay(GuiGraphics gg, HitResult mouseOver, Player player, float partialTicks)
//    {
//		ItemStack heldStack = PlayerUtil.getPlayerItemIfHeld(player);
//
//		if (heldStack.getItem() instanceof WandSteelItem)
//		{
//			WandSteelItem wand = (WandSteelItem) heldStack.getItem();
//			
//			gg.pose().pushPose();
//
//	        float scale = 1.0f;
//			
//	        float sw = Minecraft.getInstance().getWindow().getGuiScaledWidth();
//	        float sh = Minecraft.getInstance().getWindow().getGuiScaledHeight();
//	        
//	        setupOverlayRendering(sw * scale, sh * scale);
//	        
//	        float time = player.level().getLevelData().getGameTime() + partialTicks;
//			double offset = Math.sin(time / 2 * 1 / 8.0D) / 30.0D;
//	        float oPlicator = (float) offset * 20 * 10;
//	        int flytoOutside = (int) oPlicator;
//
//	        gg.pose().pushPose();
//	        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, (float) offset * 8);
//	        gg.blit(WitherUtils.loc("textures/gui/overlay/souloverlay.png"), -80 + flytoOutside * 7, 0, 0, 0, 80, (int)sh, 80, 8);
//	        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//	        gg.pose().popPose();
//	        
//	        gg.pose().pushPose();
//	        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, (float) offset * 8);
//	        gg.blit(WitherUtils.loc("textures/gui/overlay/souloverlay.png"), (int) sw - flytoOutside * 7, 0, 0, 0, 80, (int)sh, 80, 8);
//	        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//	        gg.pose().popPose();
//	        
//	        setupOverlayRendering(sw, sh);
//
//			gg.pose().popPose();
//		}
//    }

	/*
	 * 
	 * BATTERY CORE OVERLAY
	 * 
	 */
	private static void renderCoreOverlay(GuiGraphics graphics, HitResult mouseOver, float partialTicks)
    {
        if (mouseOver.getType() == HitResult.Type.BLOCK)
        {
            float scale = 1.0f;

            float sw = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            float sh = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            setupOverlayRendering(sw * scale, sh * scale);
            renderHUDBlock(graphics, mouseOver, sw * scale, sh * scale, partialTicks);
            setupOverlayRendering(sw, sh);
        }
    }
	
	private static void renderHUDBlock(GuiGraphics graphics, HitResult mouseOver, double sw, double sh, float partialTicks)
    {
        if (!(mouseOver instanceof BlockHitResult))
        {
            return;
        }
        
        BlockPos blockPos = ((BlockHitResult) mouseOver).getBlockPos();
        if (blockPos == null)
        {
            return;
        }
        
        Player player = Minecraft.getInstance().player;
        if (player.getCommandSenderWorld().isEmptyBlock(blockPos))
        {
            return;
        }

        BlockState state = player.getCommandSenderWorld().getBlockState(blockPos);
        if (state.getBlock() instanceof CoreBlock)
        {
        	BlockEntity be = player.getCommandSenderWorld().getBlockEntity(blockPos);
        	if(be instanceof CoreBlockEntity core)
        	{
                renderElements(graphics, sw, sh, mouseOver, blockPos, player, partialTicks, core);
        	}
        }
    }

    public static void renderElements(GuiGraphics graphics, double sw, double sh, HitResult mouseOver, BlockPos blockPos, Player player, float partialTicks, CoreBlockEntity core)
    {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        if (!Minecraft.getInstance().isPaused())
        {
            RenderingHelper.rot += .5f;
        }
        
        int x = (int) sw / 2;
        int y = (int) sh;
        
        Vector2i pos = new Vector2i(x - 75, y - 100);
        Vector2i pos2 = pos.add(new Vector2i(150, 50));

//        int leftPos = (int) ((sw - pos2.x()) / 2);
//        int topPos = (int) (sh - pos2.y()) / 2;
        
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.5F);
        RenderingHelper.drawThickBeveledBox(graphics, pos.x(), pos.y(), pos2.x(), pos2.y(), 1, 0xFF118B8B, 0xFF118B8B, 0xFF373737);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

//        RenderHelper.drawHorizontalLine(graphics, pos.x() + 4, pos.y() + 20, pos.x() + 100 - 4, 0xFF8B8B8B);
        
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShaderTexture(0, WitherUtils.loc("textures/gui/io_config.png"));
//        RenderSystem.enableDepthTest();
//        graphics.blit(WitherUtils.loc("textures/gui/io_config.png"), pos.x(), pos.y(), 0, 0, 16, 16, 16, 16);

        ItemStack stack = requestBlockInfo(mouseOver, blockPos, player);
        if(stack != null)
        	renderNextPlacement(graphics.pose(), pos.x(), pos.y(), partialTicks, stack);

        graphics.pose().pushPose();
        RenderSystem.enableBlend();
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;

        String s = "Core Information";
        String s1 = "";
        String s2 = "";
        
        if(core.stabilizersValid)
        {
        	s2 = "" + ChatFormatting.GREEN + "Stabilizer found";
        }
        else
        {
        	s2 = "" + ChatFormatting.AQUA + "Stabilizer needed";
        }
        
        if (core.isControllerValid())
        {
            if (core.isSteelValid())
            {
                if (!core.isCoilValid())
                {
                	s1 = "" + ChatFormatting.RED + "Redstone Blocks needed";
                }
            }
            else
            {
            	s1 = "" + ChatFormatting.RED + "WitherSteel Blocks needed";
            }
        }
        else
        {
        	s1 = "" + ChatFormatting.AQUA + "Coal Blocks needed";
        }

        drawTextWithScale(graphics, Component.literal(s), x + 10 - (font.width(s) / 2), y - 90, 0xFFFFFFFF, 1.1f);
        drawTextWithScale(graphics, Component.literal(s1), x + 10 - (font.width(s) / 2), y - 80, 0xFFFFFFFF, 0.812f);
        drawTextWithScale(graphics, Component.literal(s2), x + 10 - (font.width(s) / 2), y - 70, 0xFFFFFFFF, 0.812f);
        
        RenderSystem.disableBlend();
        graphics.pose().popPose();
    }
    
    public static ItemStack requestBlockInfo(HitResult mouseOver, BlockPos blockPos, Player player)
    {
        Level world = player.getCommandSenderWorld();
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        return block.getCloneItemStack(blockState, mouseOver, world, blockPos, player);
    }
    
	public static void renderNextPlacement(PoseStack ms, int startX, int startY, float partialTicks, ItemStack stack)
	{
        partialTicks = Minecraft.getInstance().getFrameTime();

        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        {
            modelViewStack.translate(startX, startY + 25, 100);
            modelViewStack.scale(30F, -30F, 30F);
            modelViewStack.mulPose(Axis.XP.rotationDegrees(5F));
    		float rotation = (Minecraft.getInstance().player.tickCount + partialTicks) / 2F;
    		modelViewStack.mulPose(Axis.YP.rotationDegrees(rotation * (float) MathHelper.torad * 40));
    		modelViewStack.mulPose(Axis.ZP.rotationDegrees(rotation * (float) MathHelper.torad * 40 / 2));
    		
            RenderSystem.applyModelViewMatrix();
            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer vertexBuilder = buffer.getBuffer(WUTRenderType.solid());
            Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.SPHEREINNERSCREEN.getModel(), ItemStack.EMPTY, 255, OverlayTexture.NO_OVERLAY, ms, vertexBuilder);
            buffer.endBatch();
//            Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.FIXED, false, ms, buffer, 15728880, OverlayTexture.NO_OVERLAY, Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(stack));
       }
        modelViewStack.popPose();
        
        modelViewStack.pushPose();
        {
            modelViewStack.translate(startX, startY + 25, 100);
            modelViewStack.scale(32F, -32F, 32F);
            modelViewStack.mulPose(Axis.XP.rotationDegrees(5F));
    		float rotation = (Minecraft.getInstance().player.tickCount + partialTicks) / 2F;
    		modelViewStack.mulPose(Axis.YN.rotationDegrees(rotation * 0.5F * (float) MathHelper.torad * 60));
    		modelViewStack.mulPose(Axis.ZN.rotationDegrees(rotation * 0.5F * (float) MathHelper.torad * 60 / 2));

            RenderSystem.applyModelViewMatrix();
            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer vertexBuilder = buffer.getBuffer(WUTRenderType.translucent());
            Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.SPHEREOUTERSCREEN.getModel(), ItemStack.EMPTY, 255, OverlayTexture.NO_OVERLAY, ms, vertexBuilder);
            buffer.endBatch();
        }
        modelViewStack.popPose();
        
        RenderSystem.applyModelViewMatrix();
	}

    public static int drawString(GuiGraphics gg, Component component, int x, int y, int color)
    {
        return gg.drawString(Minecraft.getInstance().font, component, x, y, color);
    }
    public static void drawTextWithScale(GuiGraphics gg, Component text, float x, float y, int color, float scale)
    {
        prepTextScale(gg, m -> drawString(m, text, 0, 0, color), x, y, scale);
    }
    public static void prepTextScale(GuiGraphics gg, Consumer<GuiGraphics> runnable, float x, float y, float scale)
    {
        float yAdd = 4 - (scale * 8) / 2F;
        gg.pose().pushPose();
        gg.pose().translate(x, y + yAdd, 0);
        gg.pose().scale(scale, scale, scale);
        runnable.accept(gg);
        gg.pose().popPose();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
}
