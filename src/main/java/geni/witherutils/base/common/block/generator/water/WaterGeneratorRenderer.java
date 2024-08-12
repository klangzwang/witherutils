package geni.witherutils.base.common.block.generator.water;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import geni.witherutils.base.client.base.AbstractBlockEntityRenderer;
import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.RenderSettings;
import geni.witherutils.base.client.render.type.WUTRenderType;
import geni.witherutils.base.common.block.generator.water.WaterGeneratorBlockEntity.Shovel;
import geni.witherutils.core.common.math.Vector3;
import geni.witherutils.core.common.util.FacingUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

public class WaterGeneratorRenderer extends AbstractBlockEntityRenderer<WaterGeneratorBlockEntity> {

    public WaterGeneratorRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @SuppressWarnings("incomplete-switch")
	@Override
    public void render(WaterGeneratorBlockEntity te, float partialTick, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight)
    {
        if (te.getLevel() == null || te.getShovelModul() == Shovel.NONE)
            return;

    	matrix.pushPose();
    	
    	double x = Vector3.CENTER.x - 0.5F;
    	double y = Vector3.CENTER.y - 0.5F;
    	double z = Vector3.CENTER.z - 0.5F;
      
    	matrix.translate(x, y, z);
      
    	matrix.translate(0.5, 0.5, 0.5);
    	float rotation = te.prevFanRotation + (te.fanRotation - te.prevFanRotation) * partialTick;
    	matrix.mulPose(Axis.YP.rotationDegrees(rotation * 6));
    	matrix.translate(-0.5, -0.5, -0.5);

    	switch(te.getShovelModul())
    	{
			case ADVANCED:
		    	renderSpecialFacingModel(SpecialModels.SHOVEL_ADVANCED.getModel(), ItemDisplayContext.NONE, false, matrix, buffer, -1, light, OverlayTexture.NO_OVERLAY, WUTRenderType.solid(), null);
				break;
			case BASIC:
		    	renderSpecialFacingModel(SpecialModels.SHOVEL_BASIC.getModel(), ItemDisplayContext.NONE, false, matrix, buffer, -1, light, OverlayTexture.NO_OVERLAY, WUTRenderType.solid(), null);
				break;
			case MASTER:
		    	renderSpecialFacingModel(SpecialModels.SHOVEL_MASTER.getModel(), ItemDisplayContext.NONE, false, matrix, buffer, -1, light, OverlayTexture.NO_OVERLAY, WUTRenderType.solid(), null);
				break;
    	}
    	matrix.popPose();
    	
    	matrix.pushPose();
    	renderWaterEffect(te, partialTick, matrix, buffer, mc, level, player, light, overlayLight);
    	matrix.popPose();
    }
    
    public void renderWaterEffect(WaterGeneratorBlockEntity te, float partialTick, PoseStack matrix, MultiBufferSource bufferIn, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight)
    {
        for (Direction facing : Direction.values())
        {
        	if(facing != Direction.UP || facing != Direction.DOWN)
        	{
        		for (BlockPos aroundPos : FacingUtil.getAroundAxis(net.minecraft.core.Direction.Axis.Y))
        		{
            		BlockPos pos = new BlockPos(
    				te.getBlockPos().getX() + aroundPos.getX(),
    				te.getBlockPos().getY() + aroundPos.getY(),
    				te.getBlockPos().getZ() + aroundPos.getZ());
            		
            		if(level.getBlockState(pos).getBlock() instanceof LiquidBlock)
            		{
            			LiquidBlock liquid = (LiquidBlock) level.getBlockState(pos).getBlock();
            			FluidState fluidState = level.getBlockState(pos).getFluidState();
            			float fluidLevel = fluidState.getAmount();
            			
            			if (fluidLevel < 1)
            				return;
            			
            			FluidStack fluidStack = new FluidStack(liquid.fluid, 100);
            			float height = fluidState.getHeight(level, pos);
            			
            			var fluidExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
            			for (RenderType renderType : RenderType.chunkBufferLayers())
            			{
            				if (ItemBlockRenderTypes.getRenderLayer(fluidStack.getFluid().defaultFluidState()) == renderType)
            				{
            					matrix.pushPose();
            					matrix.translate(0D, 0D, 0D);
            					
            					float xMax, zMax, xMin, zMin, yMin = 0;
            					
            					xMax = 1.998889F;
            					zMax = 1.998889F;
            					xMin = 0.002121F;
            					zMin = 0.002121F;
            					yMin = 0.002121F;
            					
            					TextureAtlasSprite fluidStillSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidExtensions.getStillTexture());
            					
            		            int color = fluidExtensions.getTintColor(fluidStack.getFluid().defaultFluidState(), te.getLevel(), te.getBlockPos());
            		            int luminosity = fluidStack.getFluid().getFluidType().getLightLevel();
            		            int brightness = LightTexture.pack(Math.max(luminosity, overlayLight), 0);
            		            
            		            RenderSettings settings = RenderSettings.builder()
            		            		.color(color)
            		            		.brightness(brightness)
            		            		.build();
            		            
            		            int b1 = settings.brightness() >> 16 & 65535;
            		            int b2 = settings.brightness() & 65535;

            					renderCuboid(bufferIn.getBuffer(RenderType.translucent()), matrix, xMax, xMin, yMin, height, zMin, zMax, fluidStillSprite, 0.0F, 0.5F, 1.0F, 0.25F, b1, b2);
            					matrix.popPose();
            				}
            			}
            		}
        		}
        	}
        }
	}

	private void renderCuboid(VertexConsumer buffer, PoseStack matrixStack, float xMax, float xMin, float yMin, float height, float zMin, float zMax, TextureAtlasSprite textureAtlasSprite, float red, float green, float blue, float alpha, int combinedLight, int combinedLight2)
	{
		float uMin = textureAtlasSprite.getU0();
		float uMax = textureAtlasSprite.getU1();
		float vMin = textureAtlasSprite.getV0();
		float vMax = textureAtlasSprite.getV1();

		float vHeight = vMax - vMin;

		// top
		addVertexWithUV(buffer, matrixStack, xMax, height, zMax, uMax, vMin, red, green, blue, alpha, combinedLight, combinedLight2);
		addVertexWithUV(buffer, matrixStack, xMax, height, zMin, uMin, vMin, red, green, blue, alpha, combinedLight, combinedLight2);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMin, uMin, vMax, red, green, blue, alpha, combinedLight, combinedLight2);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMax, uMax, vMax, red, green, blue, alpha, combinedLight, combinedLight2);

		// north
		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMin, uMax, vMin, red, green, blue, alpha, combinedLight, combinedLight2);
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMin, uMin, vMin, red, green, blue, alpha, combinedLight, combinedLight2);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMin, uMin, vMin + (vHeight * height), red, green, blue, alpha, combinedLight, combinedLight2);
		addVertexWithUV(buffer, matrixStack, xMax, height, zMin, uMax, vMin + (vHeight * height), red, green, blue, alpha, combinedLight, combinedLight2);

		// south
		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMax, uMin, vMin, red, green, blue, alpha, combinedLight, combinedLight2);
		addVertexWithUV(buffer, matrixStack, xMax, height, zMax, uMin, vMin + (vHeight * height), red, green, blue, alpha, combinedLight, combinedLight2);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMax, uMax, vMin + (vHeight * height), red, green, blue, alpha, combinedLight, combinedLight2);
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMax, uMax, vMin, red, green, blue, alpha, combinedLight, combinedLight2);

		// east
		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMin, uMin, vMin, red, green, blue, alpha, combinedLight, combinedLight2);
		addVertexWithUV(buffer, matrixStack, xMax, height, zMin, uMin, vMin + (vHeight * height), red, green, blue, alpha, combinedLight, combinedLight2);
		addVertexWithUV(buffer, matrixStack, xMax, height, zMax, uMax, vMin + (vHeight * height), red, green, blue, alpha, combinedLight, combinedLight2);
		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMax, uMax, vMin, red, green, blue, alpha, combinedLight, combinedLight2);

		// west
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMax, uMin, vMin, red, green, blue, alpha, combinedLight, combinedLight2);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMax, uMin, vMin + (vHeight * height), red, green, blue, alpha, combinedLight, combinedLight2);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMin, uMax, vMin + (vHeight * height), red, green, blue, alpha, combinedLight, combinedLight2);
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMin, uMax, vMin, red, green, blue, alpha, combinedLight, combinedLight2);

		// down
		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMin, uMax, vMin, red, green, blue, alpha, combinedLight, combinedLight2);
		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMax, uMin, vMin, red, green, blue, alpha, combinedLight, combinedLight2);
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMax, uMin, vMax, red, green, blue, alpha, combinedLight, combinedLight2);
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMin, uMax, vMax, red, green, blue, alpha, combinedLight, combinedLight2);
	}

	private void addVertexWithUV(VertexConsumer buffer, PoseStack matrixStack, float x, float y, float z, float u, float v, float red, float green, float blue, float alpha, int combinedLight, int combinedLight2)
	{
		buffer.addVertex(matrixStack.last().pose(), x / 2f, y, z / 2f).setColor(red, green, blue, alpha).setUv(u, v).setLight(combinedLight).setNormal(1, 0, 0);
	}
}
