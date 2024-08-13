package geni.witherutils.base.common.block.generator.solar;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.client.base.AbstractBlockEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.block.state.BlockState;

public class SolarPanelRenderer extends AbstractBlockEntityRenderer<SolarPanelBlockEntity> {

    public SolarPanelRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

	@Override
	public void render(SolarPanelBlockEntity te, float partialTick, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight)
	{
		if(te.getLevel() == null || te == null)
			return;

		BlockState state = te.getLevel().getBlockState(te.getBlockPos());

		if(!(state.getBlock() instanceof SolarPanelBlock solar))
			return;
	    if(solar.getType() == null)
	    	return;

		light = LevelRenderer.getLightColor(te.getLevel(), te.getBlockPos().above());

		matrix.pushPose();
		renderCuboidTop(te, partialTick, buffer, matrix, light, solar.getType().ordinal());
		matrix.popPose();

		matrix.pushPose();
		renderGlassTop(te, partialTick, buffer, matrix, light, overlayLight);
		matrix.popPose();
	}

	@SuppressWarnings({ "deprecation", "unused" })
	private void renderCuboidTop(SolarPanelBlockEntity tile, float partialTicks, MultiBufferSource getter, PoseStack matrixStack, int light, int type)
	{
		final boolean isPowered = tile.isSolarPowered(tile.getLevel(), tile.getBlockPos()) && tile.calculateLightRatio(tile.getLevel()) > 0;
		float xMax, zMax, xMin, zMin, yMin = 0;

		xMax = 2.0F;
		zMax = 2.0F;
		xMin = 0.0F;
		zMin = 0.0F;
		yMin = 0.025F;

		float height = 0.025F;
		float red = 0.2F;
		float green = 0.2F;
		float blue = 0.2F;
		float alpha = 1.0F;		

		if(isPowered)
		{
			switch(type)
			{
				case 1:
					red = 0.25F;
					green = 0.85F;
					blue = 1.0F;
					break;
				case 2:
					red = 0.25F;
					green = 1.0F;
					blue = 0.25F;
					break;
				case 3:
					red = 1.0F;
					green = 0.0F;
					blue = 0.0F;
					break;
			}
		}

		TextureAtlasSprite textureAtlasSprite = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS).getSprite(WitherUtilsRegistry.loc("block/solar/solar_nocolor"));
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, textureAtlasSprite.atlasLocation());
		VertexConsumer buffer = getter.getBuffer(RenderType.cutout());

		float uMin = textureAtlasSprite.getU0();
		float uMax = textureAtlasSprite.getU1();
		float vMin = textureAtlasSprite.getV0();
		float vMax = textureAtlasSprite.getV1();

		addVertexWithUV(buffer, matrixStack, xMax, height, zMax, uMax, vMin, red, green, blue, alpha, light);
		addVertexWithUV(buffer, matrixStack, xMax, height, zMin, uMin, vMin, red, green, blue, alpha, light);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMin, uMin, vMax, red, green, blue, alpha, light);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMax, uMax, vMax, red, green, blue, alpha, light);
	}

	@SuppressWarnings({ "deprecation", "unused" })
	private void renderGlassTop(SolarPanelBlockEntity tile, float partialTicks, MultiBufferSource getter, PoseStack matrixStack, int light, int combinedLight)
	{
		final boolean isPowered = tile.isSolarPowered(tile.getLevel(), tile.getBlockPos()) && tile.calculateLightRatio(tile.getLevel()) > 0;
		float xMax, zMax, xMin, zMin, yMin = 0;

		xMax = 2.0F;
		zMax = 2.0F;
		xMin = 0.0F;
		zMin = 0.0F;
		yMin = 0.045F;

		float time = tile.getLevel().getGameTime() + partialTicks;
		double offset = Math.sin((float)time * 1.0F / 8.0F) / 20.0F;

		float height = 0.045F;
		
		float red = 1.0F;
		float green = 1.0F;
		float blue = 1.0F;
		float alpha = 1.0F;
		
		if(isPowered && offset > 0)
		{
			alpha = 1.0F - (float)offset * 14;	
		}

		TextureAtlasSprite textureAtlasSprite = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS).getSprite(WitherUtilsRegistry.loc("block/solar/solar_glass"));
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, textureAtlasSprite.atlasLocation());
		VertexConsumer buffer = getter.getBuffer(RenderType.translucent());

		float uMin = textureAtlasSprite.getU0();
		float uMax = textureAtlasSprite.getU1();
		float vMin = textureAtlasSprite.getV0();
		float vMax = textureAtlasSprite.getV1();

		addVertexWithUV(buffer, matrixStack, xMax, height, zMax, uMax, vMin, red, green, blue, alpha, light);
		addVertexWithUV(buffer, matrixStack, xMax, height, zMin, uMin, vMin, red, green, blue, alpha, light);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMin, uMin, vMax, red, green, blue, alpha, light);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMax, uMax, vMax, red, green, blue, alpha, light);
	}

	private void addVertexWithUV(VertexConsumer buffer, PoseStack matrixStack, float x, float y, float z, float u, float v, float red, float green, float blue, float alpha, int combinedLight)
	{
		buffer.addVertex(matrixStack.last().pose(), x / 2f, y, z / 2f).setColor(red, green, blue, alpha).setUv(u, v).setUv2(combinedLight, 240).setNormal(1, 0, 0);
	}
}
