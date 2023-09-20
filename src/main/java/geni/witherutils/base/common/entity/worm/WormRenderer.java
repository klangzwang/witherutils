package geni.witherutils.base.common.entity.worm;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import geni.witherutils.base.common.init.WUTItems;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WormRenderer extends EntityRenderer<Worm> {

    private ItemStack stack;
	
	public WormRenderer(EntityRendererProvider.Context manager)
	{
		super(manager);
        stack = new ItemStack(WUTItems.WORM.get());
	}

	@Override
	public void render(Worm entity, float partialTicks, float yaw, PoseStack matrix, MultiBufferSource buffer, int light)
	{
		matrix.pushPose();
		matrix.translate(0, 0.7F, 0);
		double boop = Util.getMillis() / 70D;
		matrix.mulPose(Axis.YP.rotationDegrees(-(float) (boop % 360)));
		matrix.translate(0, 0, 0.4);
		Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, matrix, buffer, entity.level(), 0);
		matrix.popPose();
	}

	@SuppressWarnings("deprecation")
	@Override
	public ResourceLocation getTextureLocation(Worm worm)
	{
		return TextureAtlas.LOCATION_BLOCKS;
	}
}
