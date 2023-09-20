package geni.witherutils.base.common.entity.cursed.zombie;

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
public class CursedZombieRenderer extends MobRenderer<CursedZombie, CursedZombieModel<CursedZombie>> {

	private static final ResourceLocation TEX_LOCATION = new ResourceLocation("witherutils:textures/model/entity/cursed/zombie.png");
	private final RandomSource random = RandomSource.create();
	
	public CursedZombieRenderer(EntityRendererProvider.Context context)
	{
		super(context, new CursedZombieModel<>(context.bakeLayer(ModelLayers.CURSEDZOMBIE)), 0.5F);
		this.addLayer(new CursedZombieEyesLayer(this));
	}
	
	@Override
	public void render(CursedZombie creeper, float p_115456_, float p_115457_, PoseStack matrix, MultiBufferSource rtb, int light)
	{
		CursedZombieModel<CursedZombie> cursedCreeperModel = this.getModel();
		cursedCreeperModel.creepy = creeper.isCreepy();
		super.render(creeper, p_115456_, p_115457_, matrix, rtb, light);
	}

	@Override
	public Vec3 getRenderOffset(CursedZombie p_114336_, float p_114337_)
	{
		if (p_114336_.isCreepy())
			return new Vec3(this.random.nextGaussian() * 0.02D, 0.0D, this.random.nextGaussian() * 0.02D);
		else
			return super.getRenderOffset(p_114336_, p_114337_);
	}
	
	@Override
	public ResourceLocation getTextureLocation(CursedZombie p_114334_)
	{
		return TEX_LOCATION;
	}
}