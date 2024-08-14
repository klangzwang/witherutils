package geni.witherutils.base.common.entity.cursed.spider;

import com.mojang.blaze3d.vertex.PoseStack;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.client.render.layer.ModelLayers;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CursedSpiderRenderer extends MobRenderer<CursedSpider, CursedSpiderModel<CursedSpider>> {

	private static final ResourceLocation TEX_LOCATION = WitherUtilsRegistry.loc("textures/model/entity/cursed/spider.png");
	private final RandomSource random = RandomSource.create();
	
	public CursedSpiderRenderer(EntityRendererProvider.Context context)
	{
		this(context, ModelLayers.CURSEDSPIDER);
	}
	public CursedSpiderRenderer(EntityRendererProvider.Context context, ModelLayerLocation layer)
	{
		super(context, new CursedSpiderModel<>(context.bakeLayer(layer)), 0.8F);
		this.addLayer(new CursedSpiderEyesLayer(this));
	}
	
	@Override
	public void render(CursedSpider creeper, float p_115456_, float p_115457_, PoseStack matrix, MultiBufferSource rtb, int light)
	{
		CursedSpiderModel<CursedSpider> cursedCreeperModel = this.getModel();
		cursedCreeperModel.creepy = creeper.isCreepy();
		super.render(creeper, p_115456_, p_115457_, matrix, rtb, light);
	}

	@Override
	public Vec3 getRenderOffset(CursedSpider p_114336_, float p_114337_)
	{
		if (p_114336_.isCreepy())
			return new Vec3(this.random.nextGaussian() * 0.02D, 0.0D, this.random.nextGaussian() * 0.02D);
		else
			return super.getRenderOffset(p_114336_, p_114337_);
	}
	
	@Override
	protected float getFlipDegrees(CursedSpider p_116011_)
	{
		return 180.0F;
	}
	
	@Override
	public ResourceLocation getTextureLocation(CursedSpider p_114334_)
	{
		return TEX_LOCATION;
	}
}