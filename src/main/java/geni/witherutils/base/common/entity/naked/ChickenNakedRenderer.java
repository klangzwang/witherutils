package geni.witherutils.base.common.entity.naked;

import geni.witherutils.base.client.render.layer.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChickenNakedRenderer extends MobRenderer<ChickenNaked, ChickenNakedModel<ChickenNaked>> {

	private static final ResourceLocation CHICKEN_LOCATION = new ResourceLocation("witherutils:textures/model/entity/chickennaked.png");
	
	public ChickenNakedRenderer(EntityRendererProvider.Context p_173952_)
	{
		super(p_173952_, new ChickenNakedModel<>(p_173952_.bakeLayer(ModelLayers.CHICKENNAKED)), 0.3F);
	}

	@Override
	public ResourceLocation getTextureLocation(ChickenNaked p_113998_)
	{
		return CHICKEN_LOCATION;
	}
}