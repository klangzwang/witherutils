package geni.witherutils.base.common.block.tank.drum;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import geni.witherutils.base.client.model.special.SpecialModels;
import geni.witherutils.base.client.render.type.WUTRenderType;
import geni.witherutils.base.common.base.AbstractBlockEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

public class TankDrumRenderer extends AbstractBlockEntityRenderer<TankDrumBlockEntity> {

    public TankDrumRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(TankDrumBlockEntity te, float partialTick, PoseStack matrix, MultiBufferSource buffer, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int overlayLight)
    {
        if (te.getLevel() == null)
            return;

        VertexConsumer vertexBuilder = buffer.getBuffer(WUTRenderType.eyes(EMISSIVE));
        Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.EMDRUM.getModel(), ItemStack.EMPTY, light, overlayLight, matrix, vertexBuilder);

        vertexBuilder = buffer.getBuffer(WUTRenderType.translucent());
        Minecraft.getInstance().getItemRenderer().renderModelLists(SpecialModels.GLASSDRUM.getModel(), ItemStack.EMPTY, light, overlayLight, matrix, vertexBuilder);

		if(te.getFluidTank().getFluid().isEmpty())
			return;
		float fluidLevel = te.getFluidTank().getFluidAmount();
		if(fluidLevel < 1)
			return;

		FluidStack fluidStack = new FluidStack(te.getFluidTank().getFluid(), 100);
		float height = (0.96875F / te.getFluidTank().getCapacity()) * te.getFluidTank().getFluidAmount();

		matrix.translate(0, 0.1, 0);
		
		renderCuboid(matrix, buffer, overlayLight, fluidStack, 0, 0, 0, height);
    }
    
	public static void renderCuboid(PoseStack matrixStack, MultiBufferSource renderer, int combinedLight, FluidStack fluidStack, float x, float y, float z, float height)
	{
		var fluidExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
		
		TextureAtlasSprite fluidStillSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidExtensions.getStillTexture());
		VertexConsumer buffer = renderer.getBuffer(RenderType.translucent());

		int fluidColor = fluidExtensions.getTintColor();

		float alpha = 1F;

		float red = (fluidColor >> 16 & 0xFF) / 255.0F;
		float green = (fluidColor >> 8 & 0xFF) / 255.0F;
		float blue = (fluidColor & 0xFF) / 255.0F;

		if(height >= 0.85f)
		{
			height = 0.85f;
		}

		float xMax, zMax, xMin, zMin, yMin = 0;
		
		xMin = 0.3F;
		xMax = 1.7F;
		zMin = 0.3F;
		zMax = 1.7F;
		
		yMin = 0.0F;
		
		float uMin = fluidStillSprite.getU0();
		float uMax = fluidStillSprite.getU1();
		float vMin = fluidStillSprite.getV0();
		float vMax = fluidStillSprite.getV1();

		float vHeight = vMax - vMin;

		// top
		addVertexWithUV(buffer, matrixStack, xMax, height, zMax, uMax, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMax, height, zMin, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMin, uMin, vMax, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMax, uMax, vMax, red, green, blue, alpha, combinedLight);

		// north
		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMin, uMax, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMin, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMin, uMin, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMax, height, zMin, uMax, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);

		// south
		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMax, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMax, height, zMax, uMin, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMax, uMax, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMax, uMax, vMin, red, green, blue, alpha, combinedLight);

		// east
		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMin, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMax, height, zMin, uMin, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMax, height, zMax, uMax, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMax, uMax, vMin, red, green, blue, alpha, combinedLight);

		// west
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMax, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMax, uMin, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, height, zMin, uMax, vMin + (vHeight * height), red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMin, uMax, vMin, red, green, blue, alpha, combinedLight);

		// down
		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMin, uMax, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMax, yMin, zMax, uMin, vMin, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMax, uMin, vMax, red, green, blue, alpha, combinedLight);
		addVertexWithUV(buffer, matrixStack, xMin, yMin, zMin, uMax, vMax, red, green, blue, alpha, combinedLight);
	}
	
	private static void addVertexWithUV(VertexConsumer buffer, PoseStack matrixStack, float x, float y, float z, float u, float v, float red, float green, float blue, float alpha, int combinedLight)
	{
		buffer.vertex(matrixStack.last().pose(), x / 2f, y, z / 2f).color(red, green, blue, alpha).uv(u, v).uv2(combinedLight, 240).normal(1, 0, 0).endVertex();
	}
}
