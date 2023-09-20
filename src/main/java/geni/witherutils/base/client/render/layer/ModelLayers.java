package geni.witherutils.base.client.render.layer;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.entity.cursed.creeper.CursedCreeperModel;
import geni.witherutils.base.common.entity.cursed.skeleton.CursedSkeletonModel;
import geni.witherutils.base.common.entity.cursed.spider.CursedSpiderModel;
import geni.witherutils.base.common.entity.cursed.zombie.CursedZombieModel;
import geni.witherutils.base.common.entity.naked.ChickenNakedModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ModelLayers {

    public static final ModelLayerLocation CHICKENNAKED = new ModelLayerLocation(new ResourceLocation(WitherUtils.MODID, "chickennaked"), "main");
    public static final ModelLayerLocation CURSEDCREEPER = new ModelLayerLocation(new ResourceLocation(WitherUtils.MODID, "cursedcreeper"), "main");
    public static final ModelLayerLocation CURSEDSKELETON = new ModelLayerLocation(new ResourceLocation(WitherUtils.MODID, "cursedskeleton"), "main");
    public static final ModelLayerLocation CURSEDZOMBIE = new ModelLayerLocation(new ResourceLocation(WitherUtils.MODID, "cursedzombie"), "main");
    public static final ModelLayerLocation CURSEDSPIDER = new ModelLayerLocation(new ResourceLocation(WitherUtils.MODID, "cursedspider"), "main");
    
    public static void init(IEventBus bus)
    {
        bus.addListener(ModelLayers::registerLayerDefinitions);
    }

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        event.registerLayerDefinition(CHICKENNAKED, ChickenNakedModel::createBodyLayer);
        event.registerLayerDefinition(CURSEDCREEPER, CursedCreeperModel::createBodyLayer);
        event.registerLayerDefinition(CURSEDSKELETON, CursedSkeletonModel::createBodyLayer);
        event.registerLayerDefinition(CURSEDZOMBIE, CursedZombieModel::createBodyLayer);
        event.registerLayerDefinition(CURSEDSPIDER, CursedSpiderModel::createBodyLayer);
    }
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	@SuppressWarnings("unchecked")
	public static void onAddLayers(EntityRenderersEvent.AddLayers event)
	{
		List<EntityType<? extends LivingEntity>> entityTypes = ImmutableList.copyOf(ForgeRegistries.ENTITY_TYPES.getValues()
				.stream().filter(DefaultAttributes::hasSupplier)
				.map(entityType -> (EntityType<? extends LivingEntity>) entityType).collect(Collectors.toList()));

		entityTypes.forEach((entityType -> {
			addLayerIfApplicable(entityType, event);
		}));

		for(String skinType : event.getSkins())
		{
			event.getSkin(skinType).addLayer(new ArmorHelmetLayer(event.getSkin(skinType)));
			event.getSkin(skinType).addLayer(new ArmorEyesLayer(event.getSkin(skinType)));
			event.getSkin(skinType).addLayer(new ArmorCapeLayer(event.getSkin(skinType)));
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void addLayerIfApplicable(EntityType<? extends LivingEntity> entityType, EntityRenderersEvent.AddLayers event)
	{
		LivingEntityRenderer renderer = null;
		if(entityType != EntityType.ENDER_DRAGON)
		{
			try
			{
				renderer = event.getRenderer(entityType);
			}
			catch(Exception e)
			{
				WitherUtils.LOGGER.warn("Could not apply layer to " + entityType.getDescriptionId() + ", has custom renderer that is not LivingEntityRenderer.");
			}
			if(renderer != null)
			{
				renderer.addLayer(new ArmorHelmetLayer(renderer));
				renderer.addLayer(new ArmorCapeLayer(renderer));
				renderer.addLayer(new ArmorEyesLayer(renderer));
			}
		}
	}
}
