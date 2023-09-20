package geni.witherutils.base.common.entity.cursed.creeper;

import com.mojang.blaze3d.vertex.PoseStack;

import geni.witherutils.base.client.render.layer.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CursedCreeperRenderer extends MobRenderer<CursedCreeper, CursedCreeperModel<CursedCreeper>> {
	
	private static final ResourceLocation TEX_LOCATION = new ResourceLocation("witherutils:textures/model/entity/cursed/creeper.png");
	private final RandomSource random = RandomSource.create();
	
	public CursedCreeperRenderer(EntityRendererProvider.Context context)
	{
		super(context, new CursedCreeperModel<>(context.bakeLayer(ModelLayers.CURSEDCREEPER)), 1.0F);
		addLayer(new CursedCreeperEyesLayer(this));
	}

	@Override
	public void render(CursedCreeper creeper, float p_115456_, float p_115457_, PoseStack matrix, MultiBufferSource rtb, int light)
	{
		CursedCreeperModel<CursedCreeper> cursedCreeperModel = this.getModel();
		cursedCreeperModel.creepy = creeper.isCreepy();
		super.render(creeper, p_115456_, p_115457_, matrix, rtb, light);
	}

	@Override
	public Vec3 getRenderOffset(CursedCreeper p_114336_, float p_114337_)
	{
		if (p_114336_.isCreepy())
			return new Vec3(this.random.nextGaussian() * 0.02D, 0.0D, this.random.nextGaussian() * 0.02D);
		else
			return super.getRenderOffset(p_114336_, p_114337_);
	}
	   
	@Override
	public ResourceLocation getTextureLocation(CursedCreeper entity)
	{
		return TEX_LOCATION;
	}
}
