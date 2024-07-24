package geni.witherutils.base.client.model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Supplier;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.client.model.armor.AbstractArmorModel;
import geni.witherutils.base.client.model.armor.ModelSteelBoots;
import geni.witherutils.base.client.model.armor.ModelSteelChest;
import geni.witherutils.base.client.model.armor.ModelSteelHelmet;
import geni.witherutils.base.client.model.armor.ModelSteelLeggings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@SuppressWarnings("rawtypes")
@EventBusSubscriber(modid = WitherUtilsRegistry.MODID, bus = Bus.MOD, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class ModelHandler {

	private static Map<ModelLayerLocation, Layer> layers = new HashMap<>();
	private static Map<Pair<ModelLayerLocation, EquipmentSlot>, AbstractArmorModel> cachedArmors = new HashMap<>();

	public static ModelLayerLocation steelarmor_head;
	public static ModelLayerLocation steelarmor_body;
	public static ModelLayerLocation steelarmor_legs;
	public static ModelLayerLocation steelarmor_boots;

	private static boolean modelsInitted = false;

	private static void initModels()
	{
		if(modelsInitted)
			return;

		steelarmor_head = addArmorModel("steelarmor_head", ModelSteelHelmet::createBodyLayer);
		steelarmor_body = addArmorModel("steelarmor_body", ModelSteelChest::createBodyLayer);
		steelarmor_legs = addArmorModel("steelarmor_legs", ModelSteelLeggings::createBodyLayer);
		steelarmor_boots = addArmorModel("steelarmor_boots", ModelSteelBoots::createBodyLayer);

		modelsInitted = true;
	}

	@SuppressWarnings("unused")
	private static ModelLayerLocation addModel(String name, Supplier<LayerDefinition> supplier, Function<ModelPart, EntityModel<?>> modelConstructor)
	{
		return addLayer(name, new Layer(supplier, modelConstructor));
	}
	private static ModelLayerLocation addArmorModel(String name, Supplier<LayerDefinition> supplier)
	{
		return addLayer(name, new Layer(supplier, AbstractArmorModel::new));
	}

	private static ModelLayerLocation addLayer(String name, Layer layer)
	{
		ModelLayerLocation loc = new ModelLayerLocation(WitherUtilsRegistry.loc(name), "main");
		layers.put(loc, layer);
		return loc;
	}

	@SubscribeEvent
	public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event)
	{
		initModels();

		for(ModelLayerLocation location : layers.keySet())
			event.registerLayerDefinition(location, layers.get(location).definition);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Mob, M extends EntityModel<T>> M model(ModelLayerLocation location)
	{
		initModels();
		
		Layer layer = layers.get(location);
		Minecraft mc = Minecraft.getInstance();
		
		return (M) layer.modelConstructor.apply(mc.getEntityModels().bakeLayer(location));
	}

	public static AbstractArmorModel armorModel(ModelLayerLocation location, EquipmentSlot slot, LivingEntity livingEntity)
	{
		Pair<ModelLayerLocation, EquipmentSlot> key = Pair.of(location, slot);
		if(cachedArmors.containsKey(key))
			return cachedArmors.get(key).withAnimations(livingEntity);

		initModels();

		Layer layer = layers.get(location);
		Minecraft mc = Minecraft.getInstance();
		AbstractArmorModel model = layer.armorModelConstructor.apply(mc.getEntityModels().bakeLayer(location), slot);
		cachedArmors.put(key, model);

		return model; 
	}
	
	private static class Layer
	{
		final Supplier<LayerDefinition> definition;
		final Function<ModelPart, EntityModel<?>> modelConstructor;
		final BiFunction<ModelPart, EquipmentSlot, AbstractArmorModel> armorModelConstructor;

		public Layer(Supplier<LayerDefinition> definition, Function<ModelPart, EntityModel<?>> modelConstructor)
		{
			this.definition = definition;
			this.modelConstructor = modelConstructor;
			this.armorModelConstructor = null;	
		}
		public Layer(Supplier<LayerDefinition> definition, BiFunction<ModelPart, EquipmentSlot, AbstractArmorModel> armorModelConstructor)
		{
			this.definition = definition;
			this.modelConstructor = null;
			this.armorModelConstructor = armorModelConstructor;
		}
	}
}
