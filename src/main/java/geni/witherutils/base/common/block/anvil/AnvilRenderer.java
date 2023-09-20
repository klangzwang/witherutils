package geni.witherutils.base.common.block.anvil;

import org.lwjgl.opengl.GL14;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;

import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.common.base.AbstractBlockEntityRenderer;
import geni.witherutils.core.common.math.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderStateShard.TransparencyStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class AnvilRenderer extends AbstractBlockEntityRenderer<AnvilBlockEntity> {

    private static TransparencyStateShard ANVIL_TRANSPARENCY;
    private static RenderType ANVIL;

    public AnvilRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

	@Override
    public void render(AnvilBlockEntity tile, float partialTick, PoseStack matrix, MultiBufferSource multibuffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight)
    {
    	if (tile.getLevel() == null)
            return;

        matrix.pushPose();
        
	        matrix.translate(0.5, 0.5, 0.5);
	        matrix.mulPose(Axis.XP.rotationDegrees(-90));
	        Direction facing = tile.getCurrentFacing();
	        if(facing == Direction.EAST || facing == Direction.WEST)
	            matrix.mulPose(Axis.ZP.rotationDegrees(-90));
	        matrix.translate(-0.5, -0.5, -0.5);

        matrix.popPose();

	    matrix.pushPose();
	    
	        if(tile.getRecipe() != null)
	        {
	            renderSpecialFacingModel(SpecialModels.SKULLEM.getModel(), ItemDisplayContext.NONE, false, matrix, multibuffer,
	            		-1, light, OverlayTexture.NO_OVERLAY, RenderType.entityTranslucentEmissive(
	    				new ResourceLocation("witherutils:textures/block/emissive/red.png")), tile.getCurrentFacing());
	        }
	        
        matrix.popPose();

        matrix.pushPose();
        
	        ItemStack stack = AnvilBlockEntity.INPUT.getItemStack(tile.getInventory());
	
	    	double x = Vector3.CENTER.x - 0.0F;
	    	double y = Vector3.CENTER.y - 0.0F;
	    	double z = Vector3.CENTER.z - 0.0F;
	    	
	    	matrix.translate(x, y, z);
	        matrix.translate(0, 0.5, 0);

	        rotateMatrixForDirection(matrix, tile.getCurrentFacing());
	        if (stack.getItem() instanceof BlockItem)
	        {
	            matrix.translate(0, 0.5f / 4d, 0);
	        }
	        else
	        {
	            matrix.translate(0, 0.025, 0);
	            matrix.mulPose(Axis.XP.rotationDegrees(90));
	        }
	        matrix.scale(0.5f, 0.5f, 0.5f);
	
	        if (!stack.isEmpty())
	        {
	            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
	            BakedModel bakedModel = itemRenderer.getModel(stack, mc.level, null, 0);
	            itemRenderer.render(stack, ItemDisplayContext.FIXED, true, matrix, multibuffer, light, overlayLight, bakedModel);
	        }
	        
        matrix.popPose();

        matrix.pushPose();
        
        	renderHotGlow(tile, partialTick, matrix, multibuffer, mc, level, player, light, overlayLight);
        	
        matrix.popPose();
    }

    public void renderHotGlow(AnvilBlockEntity tile, float partialTicks, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlay)
    {
        matrix.translate(0.5, 0.5, 0.5);
        matrix.mulPose(Axis.XP.rotationDegrees(-90));
        Direction facing = tile.getCurrentFacing();
        if(facing == Direction.EAST || facing == Direction.WEST)
            matrix.mulPose(Axis.ZP.rotationDegrees(-90));
        matrix.translate(-0.5, -0.5, -0.5);
    	
        VertexConsumer vertexBuilder = buffer.getBuffer(RenderType.solid());

        matrix.pushPose();
        ANVIL_TRANSPARENCY = new TransparencyStateShard("ghost_transparency",
                () -> {
                    RenderSystem.enableBlend();
                    RenderSystem.blendFunc(GlStateManager.SourceFactor.CONSTANT_ALPHA, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
                    GL14.glBlendColor(1.0F, 1.0F, 1.0F, tile.getHotCounter());
                },
                () -> {
                    GL14.glBlendColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.disableBlend();
                    RenderSystem.defaultBlendFunc();
                });
        ANVIL = RenderType.create("pylon_sphere", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
        		.setLightmapState(RenderStateShard.LIGHTMAP)
                .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                .setTransparencyState(ANVIL_TRANSPARENCY)
                .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                .createCompositeState(false)
        );

        vertexBuilder = buffer.getBuffer(ANVIL);
        Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.ANVIL_HOT.getModel(), ItemStack.EMPTY, light, OverlayTexture.NO_OVERLAY, matrix, vertexBuilder);

        matrix.popPose();
	}
}
