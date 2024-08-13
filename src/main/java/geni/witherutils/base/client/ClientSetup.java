package geni.witherutils.base.client;

import java.util.List;
import java.util.function.Supplier;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.base.client.particle.BlackSmokeParticle;
import geni.witherutils.base.client.particle.BubbleParticle;
import geni.witherutils.base.client.particle.EnergyCoreParticle;
import geni.witherutils.base.client.particle.EnergyParticle;
import geni.witherutils.base.client.particle.GuardianBeamParticle;
import geni.witherutils.base.client.particle.GuardianCloudParticle;
import geni.witherutils.base.client.particle.GuardianProjectileParticle;
import geni.witherutils.base.client.particle.LiquidSprayParticle;
import geni.witherutils.base.client.particle.RisingSoulParticle;
import geni.witherutils.base.client.particle.SoulFlakeParticle;
import geni.witherutils.base.client.particle.SoulFragParticle;
import geni.witherutils.base.client.particle.SoulOrbParticle;
import geni.witherutils.base.client.particle.WindParticle;
import geni.witherutils.base.client.particle.XpOrbParticle;
import geni.witherutils.base.common.block.anvil.AnvilRenderer;
import geni.witherutils.base.common.block.cauldron.CauldronRenderer;
import geni.witherutils.base.common.block.collector.CollectorRenderer;
import geni.witherutils.base.common.block.collector.CollectorScreen;
import geni.witherutils.base.common.block.creative.CreativeEnergyRenderer;
import geni.witherutils.base.common.block.deco.cutter.CutterBlock;
import geni.witherutils.base.common.block.deco.door.metal.MetalDoorRenderer;
import geni.witherutils.base.common.block.fakedriver.FakeDriverRenderer;
import geni.witherutils.base.common.block.generator.lava.LavaGeneratorRenderer;
import geni.witherutils.base.common.block.generator.lava.LavaGeneratorScreen;
import geni.witherutils.base.common.block.generator.solar.SolarPanelRenderer;
import geni.witherutils.base.common.block.generator.water.WaterGeneratorRenderer;
import geni.witherutils.base.common.block.generator.water.WaterGeneratorScreen;
import geni.witherutils.base.common.block.generator.wind.WindGeneratorRenderer;
import geni.witherutils.base.common.block.generator.wind.WindGeneratorScreen;
import geni.witherutils.base.common.block.sensor.floor.FloorSensorRenderer;
import geni.witherutils.base.common.block.sensor.floor.FloorSensorScreen;
import geni.witherutils.base.common.block.sensor.wall.WallSensorRenderer;
import geni.witherutils.base.common.block.smarttv.SmartTVRenderer;
import geni.witherutils.base.common.block.smarttv.SmartTVScreen;
import geni.witherutils.base.common.block.totem.TotemRenderer;
import geni.witherutils.base.common.block.totem.TotemScreen;
import geni.witherutils.base.common.data.WorldData;
import geni.witherutils.base.common.entity.bolt.CursedLightningBoltRenderer;
import geni.witherutils.base.common.entity.cursed.zombie.CursedZombieRenderer;
import geni.witherutils.base.common.entity.soulorb.SoulOrbProjectileRenderer;
import geni.witherutils.base.common.entity.soulorb.SoulOrbRenderer;
import geni.witherutils.base.common.entity.worm.WormRenderer;
import geni.witherutils.base.common.init.WUTBlockEntityTypes;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTFluids;
import geni.witherutils.base.common.init.WUTMenus;
import geni.witherutils.base.common.init.WUTParticles;
import geni.witherutils.base.common.item.card.CardScreen;
import geni.witherutils.base.common.item.cutter.CutterScreen;
import geni.witherutils.base.common.item.scaper.ScaperScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SuppressWarnings("deprecation")
	@SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event)
	{
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null)
		{
            WorldData.clear();
        }
    	
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.ANVIL.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WUTBlocks.CREATIVE_GENERATOR.get(), RenderType.cutout());
    	ItemBlockRenderTypes.setRenderLayer(WUTBlocks.ANGEL.get(), RenderType.cutout());
    	ItemBlockRenderTypes.setRenderLayer(WUTBlocks.CAULDRON.get(), RenderType.cutout());
    	
    	ItemBlockRenderTypes.setRenderLayer(WUTBlocks.GREENHOUSE.get(), RenderType.translucent());
    	
    	ItemBlockRenderTypes.setRenderLayer(WUTBlocks.CTM_METAL_A.get(), RenderType.cutout());
    	ItemBlockRenderTypes.setRenderLayer(WUTBlocks.CTM_METAL_K.get(), RenderType.cutout());
    	ItemBlockRenderTypes.setRenderLayer(WUTBlocks.CTM_METAL_L.get(), RenderType.cutout());
    	ItemBlockRenderTypes.setRenderLayer(WUTBlocks.CTM_METAL_M.get(), RenderType.cutout());
    	
    	ItemBlockRenderTypes.setRenderLayer(WUTBlocks.SOULFIRE.get(), RenderType.cutout());
    	
    	ItemBlockRenderTypes.setRenderLayer(WUTBlocks.WALLSENSOR.get(), RenderType.cutout());
    	
    	ItemBlockRenderTypes.setRenderLayer(WUTFluids.BLUELIMBO.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(WUTFluids.BLUELIMBO_FLOWING.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(WUTFluids.COLDSLUSH.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(WUTFluids.COLDSLUSH_FLOWING.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(WUTFluids.EXPERIENCE.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(WUTFluids.EXPERIENCE_FLOWING.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(WUTFluids.FERTILIZER.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(WUTFluids.FERTILIZER_FLOWING.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(WUTFluids.PORTIUM.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(WUTFluids.PORTIUM_FLOWING.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(WUTFluids.REDRESIN.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(WUTFluids.REDRESIN_FLOWING.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(WUTFluids.WITHERWATER.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(WUTFluids.WITHERWATER_FLOWING.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(WUTFluids.SOULFUL.get(), RenderType.translucent());
    	ItemBlockRenderTypes.setRenderLayer(WUTFluids.SOULFUL_FLOWING.get(), RenderType.translucent());
    	
    	for (CutterBlock cutterBlock : WUTBlocks.CUTTERBLOCKS)
        {
    		if(cutterBlock.isGlass())
    			ItemBlockRenderTypes.setRenderLayer(cutterBlock, RenderType.translucent());
        }
    }
    
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event)
	{
        event.register(WUTMenus.CUTTER.get(), CutterScreen::new);
        event.register(WUTMenus.SCAPER.get(), ScaperScreen::new);
        event.register(WUTMenus.BLOCKCARD.get(), CardScreen::new);
        event.register(WUTMenus.COLLECTOR.get(), CollectorScreen::new);
        event.register(WUTMenus.SMARTTV.get(), SmartTVScreen::new);
        event.register(WUTMenus.LAVA_GENERATOR.get(), LavaGeneratorScreen::new);
        event.register(WUTMenus.WATER_GENERATOR.get(), WaterGeneratorScreen::new);
        event.register(WUTMenus.WIND_GENERATOR.get(), WindGeneratorScreen::new);
        event.register(WUTMenus.TOTEM.get(), TotemScreen::new);
        event.register(WUTMenus.FLOORSENSOR.get(), FloorSensorScreen::new);
    }
    
    @SubscribeEvent
    public static void additionalModels(ModelEvent.RegisterAdditional event)
	{
        event.register(ModelResourceLocation.standalone(WitherUtilsRegistry.loc("item/wand_helper")));
        event.register(ModelResourceLocation.standalone(WitherUtilsRegistry.loc("item/iron_gear_helper")));
    }

    @SubscribeEvent
    public static void itemDecorators(RegisterItemDecorationsEvent event)
	{
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event)
	{
    }

    @SubscribeEvent
    public static void bakingCompleted(ModelEvent.BakingCompleted event)
	{
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
	    Minecraft.getInstance().particleEngine.register(WUTParticles.MONK_PROJECTILE.get(), GuardianProjectileParticle.Factory::new);
	    Minecraft.getInstance().particleEngine.register(WUTParticles.MONK_CLOUD.get(), GuardianCloudParticle.Factory::new);
	    Minecraft.getInstance().particleEngine.register(WUTParticles.MONK_BEAM.get(), GuardianBeamParticle.Factory::new);
    }

    @SubscribeEvent
    public static void modelInit(ModelEvent.RegisterGeometryLoaders event)
	{
    }
	
	@OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
	    event.registerBlockEntityRenderer(WUTBlockEntityTypes.ANVIL.get(), AnvilRenderer::new);
	    event.registerBlockEntityRenderer(WUTBlockEntityTypes.FAKE_DRIVER.get(), FakeDriverRenderer::new);
	    event.registerBlockEntityRenderer(WUTBlockEntityTypes.CREATIVE_GENERATOR.get(), CreativeEnergyRenderer::new);
	    event.registerBlockEntityRenderer(WUTBlockEntityTypes.CAULDRON.get(), CauldronRenderer::new);
	    event.registerBlockEntityRenderer(WUTBlockEntityTypes.COLLECTOR.get(), CollectorRenderer::new);
	    event.registerBlockEntityRenderer(WUTBlockEntityTypes.METALDOOR.get(), MetalDoorRenderer::new);
	    event.registerBlockEntityRenderer(WUTBlockEntityTypes.LAVA_GENERATOR.get(), LavaGeneratorRenderer::new);
	    event.registerBlockEntityRenderer(WUTBlockEntityTypes.WATER_GENERATOR.get(), WaterGeneratorRenderer::new);
	    event.registerBlockEntityRenderer(WUTBlockEntityTypes.WIND_GENERATOR.get(), WindGeneratorRenderer::new);
	    event.registerBlockEntityRenderer(WUTBlockEntityTypes.SOLARBASIC.get(), SolarPanelRenderer::new);
	    event.registerBlockEntityRenderer(WUTBlockEntityTypes.SOLARADV.get(), SolarPanelRenderer::new);
	    event.registerBlockEntityRenderer(WUTBlockEntityTypes.SOLARULTRA.get(), SolarPanelRenderer::new);
	    event.registerBlockEntityRenderer(WUTBlockEntityTypes.SMARTTV.get(), SmartTVRenderer::new);
	    event.registerBlockEntityRenderer(WUTBlockEntityTypes.TOTEM.get(), TotemRenderer::new);
	    event.registerBlockEntityRenderer(WUTBlockEntityTypes.FLOORSENSOR.get(), FloorSensorRenderer::new);
	    event.registerBlockEntityRenderer(WUTBlockEntityTypes.WALLSENSOR.get(), WallSensorRenderer::new);
		event.registerEntityRenderer(WUTEntities.WORM.get(), WormRenderer::new);
		event.registerEntityRenderer(WUTEntities.SOULORB.get(), SoulOrbRenderer::new);
		event.registerEntityRenderer(WUTEntities.SOULORBPRO.get(), SoulOrbProjectileRenderer::new);
		event.registerEntityRenderer(WUTEntities.CURSEDZOMBIE.get(), CursedZombieRenderer::new);
		event.registerEntityRenderer(WUTEntities.CURSEDBOLT.get(), CursedLightningBoltRenderer::new);
    }
	
    @SubscribeEvent
    public static void registerModelLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
	{
    }
    
    public static final IClientBlockExtensions PARTICLE_HANDLER = new IClientBlockExtensions()
    {
        @Override
        public boolean addDestroyEffects(BlockState state, Level Level, BlockPos pos, ParticleEngine manager)
        {
            state.getShape(Level, pos).forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
                double xDif = Math.min(1, maxX - minX);
                double yDif = Math.min(1, maxY - minY);
                double zDif = Math.min(1, maxZ - minZ);
                int xCount = Mth.ceil(xDif / 0.25);
                int yCount = Mth.ceil(yDif / 0.25);
                int zCount = Mth.ceil(zDif / 0.25);
                if (xCount > 0 && yCount > 0 && zCount > 0) {
                    for (int x = 0; x < xCount; x++) {
                        for (int y = 0; y < yCount; y++) {
                            for (int z = 0; z < zCount; z++) {
                                double d4 = (x + 0.5) / xCount;
                                double d5 = (y + 0.5) / yCount;
                                double d6 = (z + 0.5) / zCount;
                                double d7 = d4 * xDif + minX;
                                double d8 = d5 * yDif + minY;
                                double d9 = d6 * zDif + minZ;
                                manager.add(new TerrainParticle((ClientLevel) Level, pos.getX() + d7, pos.getY() + d8,
                                        pos.getZ() + d9, d4 - 0.5, d5 - 0.5, d6 - 0.5, state).updateSprite(state, pos));
                            }
                        }
                    }
                }
            });
            return true;
        }
    };

    /*
     * 
     * STITCH
     * 
     */
    public static void onTextureStitch(IEventBus bus, Supplier<List<ResourceLocation>> textures) {}
    public static final ResourceLocation REDHALO = WitherUtilsRegistry.loc("block/redhalo");
    public static final ResourceLocation HALO = WitherUtilsRegistry.loc("block/halo");
    public static final ResourceLocation LASER = WitherUtilsRegistry.loc("block/laserblue");
    public static final ResourceLocation ADAPTER = WitherUtilsRegistry.loc("block/processor/adapter_type");
    public static final ResourceLocation[] LASERBEAMS = new ResourceLocation[]
    {
    	WitherUtilsRegistry.loc("block/laserbeam1"),
    	WitherUtilsRegistry.loc("block/laserbeam2"),
    	WitherUtilsRegistry.loc("block/laserbeam3"),
    	WitherUtilsRegistry.loc("block/laserbeam4")
    };

    public static List<ResourceLocation> onTextureStitch()
    {
        return List.of(REDHALO, HALO, LASER, ADAPTER, LASERBEAMS[0], LASERBEAMS[1], LASERBEAMS[2], LASERBEAMS[3]);
    }
    
//	public static void onRenderLast(PoseStack matrix, float partialTick)
//  {
//  	Minecraft mc = Minecraft.getInstance();
//		MultiBufferSource getter = Minecraft.getInstance().renderBuffers().bufferSource();
//		int light = mc.level.getLightEmission(BlockPos.ZERO.above());
//		int packedLight = OverlayTexture.NO_OVERLAY;
//  	WUTBlocks.BLOCK_TYPES.getEntries().forEach(block -> {
//  		if(block.get() instanceof IRenderProvider provider)
//  		{
//  			provider.render(block.get().defaultBlockState(), partialTick, matrix, getter, mc, mc.level, mc.player, light, packedLight);
//  		}
//  	});
//  }
}
