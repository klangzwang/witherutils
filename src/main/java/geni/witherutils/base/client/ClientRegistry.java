package geni.witherutils.base.client;

import java.util.List;
import java.util.function.Supplier;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.client.particle.BlackSmokeParticle;
import geni.witherutils.base.client.particle.BubbleParticle;
import geni.witherutils.base.client.particle.EnergyCoreParticle;
import geni.witherutils.base.client.particle.EnergyParticle;
import geni.witherutils.base.client.particle.LiquidSprayParticle;
import geni.witherutils.base.client.particle.RisingSoulParticle;
import geni.witherutils.base.client.particle.SoulFlakeParticle;
import geni.witherutils.base.client.particle.SoulFragParticle;
import geni.witherutils.base.client.particle.SoulOrbParticle;
import geni.witherutils.base.client.particle.WindParticle;
import geni.witherutils.base.client.particle.XpOrbParticle;
import geni.witherutils.base.client.render.item.EnergyBarDecorator;
import geni.witherutils.base.client.render.layer.ModelLayers;
import geni.witherutils.base.common.block.activator.ActivatorScreen;
import geni.witherutils.base.common.block.anvil.AnvilRenderer;
import geni.witherutils.base.common.block.battery.core.CoreRenderer;
import geni.witherutils.base.common.block.battery.core.CoreScreen;
import geni.witherutils.base.common.block.battery.pylon.PylonRenderer;
import geni.witherutils.base.common.block.battery.stab.StabRenderer;
import geni.witherutils.base.common.block.cauldron.CauldronRenderer;
import geni.witherutils.base.common.block.clicker.ClickerScreen;
import geni.witherutils.base.common.block.collector.CollectorRenderer;
import geni.witherutils.base.common.block.collector.CollectorScreen;
import geni.witherutils.base.common.block.creative.CreativeGeneratorRenderer;
import geni.witherutils.base.common.block.creative.CreativeGeneratorScreen;
import geni.witherutils.base.common.block.creative.CreativeTrashRenderer;
import geni.witherutils.base.common.block.deco.door.metal.MetalDoorRenderer;
import geni.witherutils.base.common.block.farmer.FarmerRenderer;
import geni.witherutils.base.common.block.farmer.FarmerScreen;
import geni.witherutils.base.common.block.fisher.FisherRenderer;
import geni.witherutils.base.common.block.fisher.FisherScreen;
import geni.witherutils.base.common.block.floodgate.FloodgateRenderer;
import geni.witherutils.base.common.block.floodgate.FloodgateScreen;
import geni.witherutils.base.common.block.furnace.alloy.AlloyFurnaceRenderer;
import geni.witherutils.base.common.block.furnace.alloy.AlloyFurnaceScreen;
import geni.witherutils.base.common.block.furnace.electro.ElectroFurnaceRenderer;
import geni.witherutils.base.common.block.furnace.electro.ElectroFurnaceScreen;
import geni.witherutils.base.common.block.generator.lava.LavaGeneratorRenderer;
import geni.witherutils.base.common.block.generator.lava.LavaGeneratorScreen;
import geni.witherutils.base.common.block.generator.solar.SolarPanelRenderer;
import geni.witherutils.base.common.block.generator.water.WaterGeneratorRenderer;
import geni.witherutils.base.common.block.generator.water.WaterGeneratorScreen;
import geni.witherutils.base.common.block.generator.wind.WindGeneratorRenderer;
import geni.witherutils.base.common.block.generator.wind.WindGeneratorScreen;
import geni.witherutils.base.common.block.miner.advanced.MinerAdvancedRenderer;
import geni.witherutils.base.common.block.miner.advanced.MinerAdvancedScreen;
import geni.witherutils.base.common.block.miner.basic.MinerBasicRenderer;
import geni.witherutils.base.common.block.miner.basic.MinerBasicScreen;
import geni.witherutils.base.common.block.placer.PlacerRenderer;
import geni.witherutils.base.common.block.placer.PlacerScreen;
import geni.witherutils.base.common.block.rack.casing.CaseRenderer;
import geni.witherutils.base.common.block.rack.controller.fluid.ControllerFluidRenderer;
import geni.witherutils.base.common.block.rack.controller.item.ControllerItemRenderer;
import geni.witherutils.base.common.block.scanner.ScannerScreen;
import geni.witherutils.base.common.block.sensor.floor.FloorSensorRenderer;
import geni.witherutils.base.common.block.sensor.floor.FloorSensorScreen;
import geni.witherutils.base.common.block.sensor.wall.WallSensorRenderer;
import geni.witherutils.base.common.block.smarttv.SmartTVRenderer;
import geni.witherutils.base.common.block.smarttv.SmartTVScreen;
import geni.witherutils.base.common.block.spawner.SpawnerRenderer;
import geni.witherutils.base.common.block.spawner.SpawnerScreen;
import geni.witherutils.base.common.block.tank.drum.TankDrumRenderer;
import geni.witherutils.base.common.block.tank.drum.TankDrumScreen;
import geni.witherutils.base.common.block.tank.reservoir.TankReservoirRenderer;
import geni.witherutils.base.common.block.totem.TotemRenderer;
import geni.witherutils.base.common.block.totem.TotemScreen;
import geni.witherutils.base.common.entity.cursed.creeper.CursedCreeperRenderer;
import geni.witherutils.base.common.entity.cursed.skeleton.CursedSkeletonRenderer;
import geni.witherutils.base.common.entity.cursed.spider.CursedSpiderRenderer;
import geni.witherutils.base.common.entity.cursed.zombie.CursedZombieRenderer;
import geni.witherutils.base.common.entity.naked.ChickenNakedRenderer;
import geni.witherutils.base.common.entity.portal.PortalRenderer;
import geni.witherutils.base.common.entity.soulorb.SoulOrbProjectileRenderer;
import geni.witherutils.base.common.entity.soulorb.SoulOrbRenderer;
import geni.witherutils.base.common.entity.worm.WormRenderer;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTFluids;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.base.common.init.WUTMenus;
import geni.witherutils.base.common.init.WUTParticles;
import geni.witherutils.base.common.item.card.CardScreen;
import geni.witherutils.base.common.item.cutter.CutterScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistry {

    @SuppressWarnings("deprecation")
    public static void setupClient(final FMLClientSetupEvent event)
	{
    	FMLJavaModLoadingContext.get().getModEventBus().addListener(ModelLayers::onAddLayers);
    	
    	ItemBlockRenderTypes.setRenderLayer(WUTBlocks.CREATIVE_TRASH.get(), RenderType.cutout());
    	ItemBlockRenderTypes.setRenderLayer(WUTBlocks.CREATIVE_GENERATOR.get(), RenderType.cutout());
    	
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.ALLOY_FURNACE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.ELECTRO_FURNACE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.ANVIL.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.LAVA_GENERATOR.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.WATER_GENERATOR.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.WIND_GENERATOR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(WUTBlocks.SOLARBASIC.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(WUTBlocks.SOLARADV.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(WUTBlocks.SOLARULTRA.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.CORE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.PYLON.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.ANGEL.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.LILLY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.MINERBASIC.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.MINERADV.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.SMARTTV.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.WALLSENSOR.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.TANKDRUM.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.CATWALK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.FARMER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.SPAWNER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.PLACER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.SCANNER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.CAULDRON.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.FISHER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.FLOODGATE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.RACK_CASE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.RACKITEM_CONTROLLER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.RACKFLUID_CONTROLLER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.ROTTEN_SAPLING.get(), RenderType.cutout());
        
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.ACTIVATOR.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.CLICKER.get(), RenderType.cutout());
        
    	ItemBlockRenderTypes.setRenderLayer(WUTBlocks.CTM_METAL_A.get(), RenderType.cutout());
    	
    	ItemBlockRenderTypes.setRenderLayer(WUTBlocks.CTM_GLASS_A.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(WUTBlocks.CTM_GLASS_B.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(WUTBlocks.CTM_GLASS_C.get(), RenderType.translucent());
    	
    	ItemBlockRenderTypes.setRenderLayer(WUTBlocks.GREENHOUSE.get(), RenderType.translucent());
    	
    	ItemBlockRenderTypes.setRenderLayer(WUTFluids.EXPERIENCE.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(WUTFluids.EXPERIENCE_FLOWING.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(WUTFluids.FERTILIZER.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(WUTFluids.FERTILIZER_FLOWING.get(), RenderType.translucent());
    	
        MenuScreens.register(WUTMenus.ALLOY_FURNACE.get(), AlloyFurnaceScreen::new);
        MenuScreens.register(WUTMenus.ELECTRO_FURNACE.get(), ElectroFurnaceScreen::new);
        MenuScreens.register(WUTMenus.LAVA_GENERATOR.get(), LavaGeneratorScreen::new);
        MenuScreens.register(WUTMenus.WATER_GENERATOR.get(), WaterGeneratorScreen::new);
        MenuScreens.register(WUTMenus.WIND_GENERATOR.get(), WindGeneratorScreen::new);
        MenuScreens.register(WUTMenus.CUTTER.get(), CutterScreen::new);
        MenuScreens.register(WUTMenus.CORE.get(), CoreScreen::new);
        MenuScreens.register(WUTMenus.SMARTTV.get(), SmartTVScreen::new);
        MenuScreens.register(WUTMenus.MINERBASIC.get(), MinerBasicScreen::new);
        MenuScreens.register(WUTMenus.MINERADV.get(), MinerAdvancedScreen::new);
        MenuScreens.register(WUTMenus.FLOORSENSOR.get(), FloorSensorScreen::new);
        MenuScreens.register(WUTMenus.TANKDRUM.get(), TankDrumScreen::new);
        MenuScreens.register(WUTMenus.COLLECTOR.get(), CollectorScreen::new);
        MenuScreens.register(WUTMenus.BLOCKCARD.get(), CardScreen::new);
        MenuScreens.register(WUTMenus.ACTIVATOR.get(), ActivatorScreen::new);
        MenuScreens.register(WUTMenus.CLICKER.get(), ClickerScreen::new);
        MenuScreens.register(WUTMenus.FARMER.get(), FarmerScreen::new);
        MenuScreens.register(WUTMenus.SPAWNER.get(), SpawnerScreen::new);
        MenuScreens.register(WUTMenus.PLACER.get(), PlacerScreen::new);
        MenuScreens.register(WUTMenus.SCANNER.get(), ScannerScreen::new);
        MenuScreens.register(WUTMenus.FISHER.get(), FisherScreen::new);
        MenuScreens.register(WUTMenus.FLOODGATE.get(), FloodgateScreen::new);
        MenuScreens.register(WUTMenus.TOTEM.get(), TotemScreen::new);
        MenuScreens.register(WUTMenus.CREATIVEGEN.get(), CreativeGeneratorScreen::new);
	}

	@SubscribeEvent
	public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerBlockEntityRenderer(WUTEntities.CREATIVE_GENERATOR.get(), CreativeGeneratorRenderer::new);
		event.registerBlockEntityRenderer(WUTEntities.CREATIVE_TRASH.get(), CreativeTrashRenderer::new);
		
	    event.registerBlockEntityRenderer(WUTEntities.ALLOY_FURNACE.get(), AlloyFurnaceRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.ELECTRO_FURNACE.get(), ElectroFurnaceRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.ANVIL.get(), AnvilRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.WATER_GENERATOR.get(), WaterGeneratorRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.WIND_GENERATOR.get(), WindGeneratorRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.LAVA_GENERATOR.get(), LavaGeneratorRenderer::new);
		event.registerBlockEntityRenderer(WUTEntities.SOLARBASIC.get(), SolarPanelRenderer::new);
		event.registerBlockEntityRenderer(WUTEntities.SOLARADV.get(), SolarPanelRenderer::new);
		event.registerBlockEntityRenderer(WUTEntities.SOLARULTRA.get(), SolarPanelRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.RESERVOIR.get(), TankReservoirRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.TANKDRUM.get(), TankDrumRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.CORE.get(), CoreRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.PYLON.get(), PylonRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.STAB.get(), StabRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.MINERBASIC.get(), MinerBasicRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.MINERADV.get(), MinerAdvancedRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.SMARTTV.get(), SmartTVRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.FLOORSENSOR.get(), FloorSensorRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.WALLSENSOR.get(), WallSensorRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.METALDOOR.get(), MetalDoorRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.COLLECTOR.get(), CollectorRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.FARMER.get(), FarmerRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.SPAWNER.get(), SpawnerRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.PLACER.get(), PlacerRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.FISHER.get(), FisherRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.CAULDRON.get(), CauldronRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.TOTEM.get(), TotemRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.FLOODGATE.get(), FloodgateRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.RACK_CASE.get(), CaseRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.RACKITEM_CONTROLLER.get(), ControllerItemRenderer::new);
	    event.registerBlockEntityRenderer(WUTEntities.RACKFLUID_CONTROLLER.get(), ControllerFluidRenderer::new);
	}

	@SuppressWarnings({ "resource", "deprecation" })
    @OnlyIn(Dist.CLIENT)
	@SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event)
    {
		Minecraft.getInstance().particleEngine.register(WUTParticles.SOULFLAKE.get(), sprite -> new SoulFlakeParticle.Provider(sprite));
		Minecraft.getInstance().particleEngine.register(WUTParticles.EXPERIENCE.get(), sprite -> new XpOrbParticle.Factory(sprite));
		
	    Minecraft.getInstance().particleEngine.register(WUTParticles.ENERGY.get(), EnergyParticle.Factory::new);
	    Minecraft.getInstance().particleEngine.register(WUTParticles.ENERGY_CORE.get(), EnergyCoreParticle.Factory::new);
		
	    Minecraft.getInstance().particleEngine.register(WUTParticles.BLACKSMOKE.get(), BlackSmokeParticle.Factory::new);
	    
	    Minecraft.getInstance().particleEngine.register(WUTParticles.BUBBLE.get(), BubbleParticle.Provider::new);
	    Minecraft.getInstance().particleEngine.register(WUTParticles.LIQUIDSPRAY.get(), LiquidSprayParticle.WaterSplashProvider::new);
	    Minecraft.getInstance().particleEngine.register(WUTParticles.FERTSPRAY.get(), LiquidSprayParticle.FertilizerSplashProvider::new);
	    Minecraft.getInstance().particleEngine.register(WUTParticles.SOULORB.get(), SoulOrbParticle.SmallNodeProvider::new);
	    Minecraft.getInstance().particleEngine.register(WUTParticles.WIND.get(), WindParticle.Provider::new);
	    Minecraft.getInstance().particleEngine.register(WUTParticles.RISINGSOUL.get(), RisingSoulParticle.Provider::new);
	    Minecraft.getInstance().particleEngine.register(WUTParticles.SOULFRAGSOFT.get(), SoulFragParticle.SoftProvider::new);
	    Minecraft.getInstance().particleEngine.register(WUTParticles.SOULFRAGHARD.get(), SoulFragParticle.HardProvider::new);
    }
    
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
	    event.registerEntityRenderer(WUTEntities.SOULORB.get(), SoulOrbRenderer::new);
		event.registerEntityRenderer(WUTEntities.SOULORBPRO.get(), SoulOrbProjectileRenderer::new);
		event.registerEntityRenderer(WUTEntities.WORM.get(), WormRenderer::new);
		event.registerEntityRenderer(WUTEntities.CHICKENNAKED.get(), ChickenNakedRenderer::new);
		event.registerEntityRenderer(WUTEntities.CURSEDCREEPER.get(), CursedCreeperRenderer::new);
		event.registerEntityRenderer(WUTEntities.CURSEDSKELETON.get(), CursedSkeletonRenderer::new);
		event.registerEntityRenderer(WUTEntities.CURSEDSPIDER.get(), CursedSpiderRenderer::new);
		event.registerEntityRenderer(WUTEntities.CURSEDZOMBIE.get(), CursedZombieRenderer::new);
		event.registerEntityRenderer(WUTEntities.PORTAL.get(), PortalRenderer::new);
	}

    @SubscribeEvent
    public static void additionalModels(ModelEvent.RegisterAdditional event)
    {
        event.register(WitherUtils.loc("item/iron_gear_helper"));
        event.register(WitherUtils.loc("item/withersteel_gear_helper"));
        event.register(WitherUtils.loc("item/shovel_helper"));
        event.register(WitherUtils.loc("item/fan_helper"));
        event.register(WitherUtils.loc("item/water_generator_helper"));
        event.register(WitherUtils.loc("item/wind_generator_helper"));
        event.register(WitherUtils.loc("item/wand_helper"));
        event.register(WitherUtils.loc("item/sword_helper"));
    }
    
    @SubscribeEvent
    public static void itemDecorators(RegisterItemDecorationsEvent event)
    {
//    	WUTCreativeTab.initItems(event);
//    	WUTCreativeTab.initBlocks(event);
//    	WUTCreativeTab.initDecos(event);
    	
        event.register(WUTItems.STEELARMOR_BOOTS.get(), EnergyBarDecorator.INSTANCE);
        event.register(WUTItems.STEELARMOR_CHEST.get(), EnergyBarDecorator.INSTANCE);
        event.register(WUTItems.STEELARMOR_HELMET.get(), EnergyBarDecorator.INSTANCE);
        event.register(WUTItems.STEELARMOR_LEGGINGS.get(), EnergyBarDecorator.INSTANCE);
    }
    
    /*
     * 
     * STITCH
     * 
     */
    public static void onTextureStitch(IEventBus bus, Supplier<List<ResourceLocation>> textures) {}
    public static final ResourceLocation REDHALO = new ResourceLocation(WitherUtils.MODID, "block/redhalo");
    public static List<ResourceLocation> onTextureStitch()
    {
        return List.of(REDHALO);
    }
}
