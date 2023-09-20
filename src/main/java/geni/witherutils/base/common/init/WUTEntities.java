package geni.witherutils.base.common.init;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.block.activator.ActivatorBlockEntity;
import geni.witherutils.base.common.block.anvil.AnvilBlockEntity;
import geni.witherutils.base.common.block.battery.core.CoreBlockEntity;
import geni.witherutils.base.common.block.battery.pylon.PylonBlockEntity;
import geni.witherutils.base.common.block.battery.stab.StabBlockEntity;
import geni.witherutils.base.common.block.cauldron.CauldronBlockEntity;
import geni.witherutils.base.common.block.clicker.ClickerBlockEntity;
import geni.witherutils.base.common.block.collector.CollectorBlockEntity;
import geni.witherutils.base.common.block.creative.CreativeGeneratorBlockEntity;
import geni.witherutils.base.common.block.creative.CreativeTrashBlockEntity;
import geni.witherutils.base.common.block.deco.door.metal.MetalDoorBlockEntity;
import geni.witherutils.base.common.block.farmer.FarmerBlockEntity;
import geni.witherutils.base.common.block.fisher.FisherBlockEntity;
import geni.witherutils.base.common.block.floodgate.FloodgateBlockEntity;
import geni.witherutils.base.common.block.furnace.alloy.AlloyFurnaceBlockEntity;
import geni.witherutils.base.common.block.furnace.electro.ElectroFurnaceBlockEntity;
import geni.witherutils.base.common.block.generator.lava.LavaGeneratorBlockEntity;
import geni.witherutils.base.common.block.generator.solar.SolarPanelBlockEntity;
import geni.witherutils.base.common.block.generator.water.WaterGeneratorBlockEntity;
import geni.witherutils.base.common.block.generator.wind.WindGeneratorBlockEntity;
import geni.witherutils.base.common.block.miner.advanced.MinerAdvancedBlockEntity;
import geni.witherutils.base.common.block.miner.basic.MinerBasicBlockEntity;
import geni.witherutils.base.common.block.placer.PlacerBlockEntity;
import geni.witherutils.base.common.block.rack.casing.CaseBlockEntity;
import geni.witherutils.base.common.block.rack.controller.fluid.ControllerFluidBlockEntity;
import geni.witherutils.base.common.block.rack.controller.item.ControllerItemBlockEntity;
import geni.witherutils.base.common.block.rack.terminal.TerminalBlockEntity;
import geni.witherutils.base.common.block.scanner.ScannerBlockEntity;
import geni.witherutils.base.common.block.sensor.floor.FloorSensorBlockEntity;
import geni.witherutils.base.common.block.sensor.wall.WallSensorBlockEntity;
import geni.witherutils.base.common.block.smarttv.SmartTVBlockEntity;
import geni.witherutils.base.common.block.spawner.SpawnerBlockEntity;
import geni.witherutils.base.common.block.tank.drum.TankDrumBlockEntity;
import geni.witherutils.base.common.block.tank.reservoir.TankReservoirBlockEntity;
import geni.witherutils.base.common.block.totem.TotemBlockEntity;
import geni.witherutils.base.common.entity.cursed.creeper.CursedCreeper;
import geni.witherutils.base.common.entity.cursed.seed.CursedSeed;
import geni.witherutils.base.common.entity.cursed.skeleton.CursedSkeleton;
import geni.witherutils.base.common.entity.cursed.spider.CursedSpider;
import geni.witherutils.base.common.entity.cursed.zombie.CursedZombie;
import geni.witherutils.base.common.entity.naked.ChickenNaked;
import geni.witherutils.base.common.entity.portal.Portal;
import geni.witherutils.base.common.entity.soulorb.SoulOrb;
import geni.witherutils.base.common.entity.soulorb.SoulOrbProjectile;
import geni.witherutils.base.common.entity.worm.Worm;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class WUTEntities {

	/*
	 * 
	 * BLOCK ENTITY
	 * 
	 */
	public static final DeferredRegister<BlockEntityType<?>> TILE_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, WitherUtils.MODID);

	public static final RegistryObject<BlockEntityType<AlloyFurnaceBlockEntity>> ALLOY_FURNACE = TILE_TYPES.register("alloy_furnace", () -> BlockEntityType.Builder.<AlloyFurnaceBlockEntity>of(AlloyFurnaceBlockEntity::new, WUTBlocks.ALLOY_FURNACE.get()).build(null));
	public static final RegistryObject<BlockEntityType<ElectroFurnaceBlockEntity>> ELECTRO_FURNACE = TILE_TYPES.register("electro_furnace", () -> BlockEntityType.Builder.<ElectroFurnaceBlockEntity>of(ElectroFurnaceBlockEntity::new, WUTBlocks.ELECTRO_FURNACE.get()).build(null));
	public static final RegistryObject<BlockEntityType<AnvilBlockEntity>> ANVIL = TILE_TYPES.register("anvil", () -> BlockEntityType.Builder.<AnvilBlockEntity>of(AnvilBlockEntity::new, WUTBlocks.ANVIL.get()).build(null));
	public static final RegistryObject<BlockEntityType<CauldronBlockEntity>> CAULDRON = TILE_TYPES.register("cauldron", () -> BlockEntityType.Builder.<CauldronBlockEntity>of(CauldronBlockEntity::new, WUTBlocks.CAULDRON.get()).build(null));

	public static final RegistryObject<BlockEntityType<LavaGeneratorBlockEntity>> LAVA_GENERATOR = TILE_TYPES.register("lava_generator", () -> BlockEntityType.Builder.<LavaGeneratorBlockEntity>of(LavaGeneratorBlockEntity::new, WUTBlocks.LAVA_GENERATOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<WindGeneratorBlockEntity>> WIND_GENERATOR = TILE_TYPES.register("wind_generator", () -> BlockEntityType.Builder.<WindGeneratorBlockEntity>of(WindGeneratorBlockEntity::new, WUTBlocks.WIND_GENERATOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<WaterGeneratorBlockEntity>> WATER_GENERATOR = TILE_TYPES.register("water_generator", () -> BlockEntityType.Builder.<WaterGeneratorBlockEntity>of(WaterGeneratorBlockEntity::new, WUTBlocks.WATER_GENERATOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<SolarPanelBlockEntity.Basic>> SOLARBASIC = TILE_TYPES.register("solarbasic", () -> BlockEntityType.Builder.<SolarPanelBlockEntity.Basic>of(SolarPanelBlockEntity.Basic::new, WUTBlocks.SOLARBASIC.get()).build(null));
	public static final RegistryObject<BlockEntityType<SolarPanelBlockEntity.Advanced>> SOLARADV = TILE_TYPES.register("solaradv", () -> BlockEntityType.Builder.<SolarPanelBlockEntity.Advanced>of(SolarPanelBlockEntity.Advanced::new, WUTBlocks.SOLARADV.get()).build(null));
	public static final RegistryObject<BlockEntityType<SolarPanelBlockEntity.Ultra>> SOLARULTRA = TILE_TYPES.register("solarultra", () -> BlockEntityType.Builder.<SolarPanelBlockEntity.Ultra>of(SolarPanelBlockEntity.Ultra::new, WUTBlocks.SOLARULTRA.get()).build(null));
	public static final RegistryObject<BlockEntityType<CreativeGeneratorBlockEntity>> CREATIVE_GENERATOR = TILE_TYPES.register("creative_generator", () -> BlockEntityType.Builder.<CreativeGeneratorBlockEntity>of(CreativeGeneratorBlockEntity::new, WUTBlocks.CREATIVE_GENERATOR.get()).build(null));

	public static final RegistryObject<BlockEntityType<TankReservoirBlockEntity>> RESERVOIR = TILE_TYPES.register("tankreservoir", () -> BlockEntityType.Builder.<TankReservoirBlockEntity>of(TankReservoirBlockEntity::new, WUTBlocks.RESERVOIR.get()).build(null));
	public static final RegistryObject<BlockEntityType<TankDrumBlockEntity>> TANKDRUM = TILE_TYPES.register("tankdrum", () -> BlockEntityType.Builder.<TankDrumBlockEntity>of(TankDrumBlockEntity::new, WUTBlocks.TANKDRUM.get()).build(null));

	public static final RegistryObject<BlockEntityType<CoreBlockEntity>> CORE = TILE_TYPES.register("core", () -> BlockEntityType.Builder.<CoreBlockEntity>of(CoreBlockEntity::new, WUTBlocks.CORE.get()).build(null));
	public static final RegistryObject<BlockEntityType<PylonBlockEntity>> PYLON = TILE_TYPES.register("pylon", () -> BlockEntityType.Builder.<PylonBlockEntity>of(PylonBlockEntity::new, WUTBlocks.PYLON.get()).build(null));
	public static final RegistryObject<BlockEntityType<StabBlockEntity>> STAB = TILE_TYPES.register("stab", () -> BlockEntityType.Builder.<StabBlockEntity>of(StabBlockEntity::new, WUTBlocks.STAB.get()).build(null));

	public static final RegistryObject<BlockEntityType<CreativeTrashBlockEntity>> CREATIVE_TRASH = TILE_TYPES.register("creative_trash", () -> BlockEntityType.Builder.<CreativeTrashBlockEntity>of(CreativeTrashBlockEntity::new, WUTBlocks.CREATIVE_TRASH.get()).build(null));

	public static final RegistryObject<BlockEntityType<MinerBasicBlockEntity>> MINERBASIC = TILE_TYPES.register("miner_basic", () -> BlockEntityType.Builder.<MinerBasicBlockEntity>of(MinerBasicBlockEntity::new, WUTBlocks.MINERBASIC.get()).build(null));
	public static final RegistryObject<BlockEntityType<MinerAdvancedBlockEntity>> MINERADV = TILE_TYPES.register("miner_adv", () -> BlockEntityType.Builder.<MinerAdvancedBlockEntity>of(MinerAdvancedBlockEntity::new, WUTBlocks.MINERADV.get()).build(null));

	public static final RegistryObject<BlockEntityType<SmartTVBlockEntity>> SMARTTV = TILE_TYPES.register("smarttv", () -> BlockEntityType.Builder.<SmartTVBlockEntity>of(SmartTVBlockEntity::new, WUTBlocks.SMARTTV.get()).build(null));

	public static final RegistryObject<BlockEntityType<FloorSensorBlockEntity>> FLOORSENSOR = TILE_TYPES.register("floorsensor", () -> BlockEntityType.Builder.<FloorSensorBlockEntity>of(FloorSensorBlockEntity::new, WUTBlocks.FLOORSENSOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<WallSensorBlockEntity>> WALLSENSOR = TILE_TYPES.register("wallsensor", () -> BlockEntityType.Builder.<WallSensorBlockEntity>of(WallSensorBlockEntity::new, WUTBlocks.WALLSENSOR.get()).build(null));

//	public static final RegistryObject<BlockEntityType<XpWirelessBlockEntity>> XPWIRELESS = TILE_TYPES.register("xpwireless", () -> BlockEntityType.Builder.<XpWirelessBlockEntity>of(XpWirelessBlockEntity::new, WUTBlocks.XPWIRELESS.get()).build(null));
//	public static final RegistryObject<BlockEntityType<XpPlateBlockEntity>> XPPLATE = TILE_TYPES.register("xpplate", () -> BlockEntityType.Builder.<XpPlateBlockEntity>of(XpPlateBlockEntity::new, WUTBlocks.XPPLATE.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<MetalDoorBlockEntity>> METALDOOR = TILE_TYPES.register("metaldoor", () -> BlockEntityType.Builder.<MetalDoorBlockEntity>of(MetalDoorBlockEntity::new, WUTBlocks.METALDOOR.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<CollectorBlockEntity>> COLLECTOR = TILE_TYPES.register("collector", () -> BlockEntityType.Builder.<CollectorBlockEntity>of(CollectorBlockEntity::new, WUTBlocks.COLLECTOR.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<SpawnerBlockEntity>> SPAWNER = TILE_TYPES.register("spawner", () -> BlockEntityType.Builder.<SpawnerBlockEntity>of(SpawnerBlockEntity::new, WUTBlocks.SPAWNER.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<ActivatorBlockEntity>> ACTIVATOR = TILE_TYPES.register("activator", () -> BlockEntityType.Builder.<ActivatorBlockEntity>of(ActivatorBlockEntity::new, WUTBlocks.ACTIVATOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<ClickerBlockEntity>> CLICKER = TILE_TYPES.register("clicker", () -> BlockEntityType.Builder.<ClickerBlockEntity>of(ClickerBlockEntity::new, WUTBlocks.CLICKER.get()).build(null));
	public static final RegistryObject<BlockEntityType<PlacerBlockEntity>> PLACER = TILE_TYPES.register("placer", () -> BlockEntityType.Builder.<PlacerBlockEntity>of(PlacerBlockEntity::new, WUTBlocks.PLACER.get()).build(null));
	public static final RegistryObject<BlockEntityType<ScannerBlockEntity>> SCANNER = TILE_TYPES.register("scanner", () -> BlockEntityType.Builder.<ScannerBlockEntity>of(ScannerBlockEntity::new, WUTBlocks.SCANNER.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<FarmerBlockEntity>> FARMER = TILE_TYPES.register("farmer", () -> BlockEntityType.Builder.<FarmerBlockEntity>of(FarmerBlockEntity::new, WUTBlocks.FARMER.get()).build(null));

	public static final RegistryObject<BlockEntityType<FisherBlockEntity>> FISHER = TILE_TYPES.register("fisher", () -> BlockEntityType.Builder.<FisherBlockEntity>of(FisherBlockEntity::new, WUTBlocks.FISHER.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<TotemBlockEntity>> TOTEM = TILE_TYPES.register("totem", () -> BlockEntityType.Builder.<TotemBlockEntity>of(TotemBlockEntity::new, WUTBlocks.TOTEM.get()).build(null));
	public static final RegistryObject<BlockEntityType<FloodgateBlockEntity>> FLOODGATE = TILE_TYPES.register("floodgate", () -> BlockEntityType.Builder.<FloodgateBlockEntity>of(FloodgateBlockEntity::new, WUTBlocks.FLOODGATE.get()).build(null));

	public static final RegistryObject<BlockEntityType<CaseBlockEntity>> RACK_CASE = TILE_TYPES.register("rack_case", () -> BlockEntityType.Builder.<CaseBlockEntity>of(CaseBlockEntity::new, WUTBlocks.RACK_CASE.get()).build(null));
	public static final RegistryObject<BlockEntityType<TerminalBlockEntity>> RACK_TERMINAL = TILE_TYPES.register("rack_terminal", () -> BlockEntityType.Builder.<TerminalBlockEntity>of(TerminalBlockEntity::new, WUTBlocks.RACK_TERMINAL.get()).build(null));
	public static final RegistryObject<BlockEntityType<ControllerItemBlockEntity>> RACKITEM_CONTROLLER = TILE_TYPES.register("rackitem_controller", () -> BlockEntityType.Builder.<ControllerItemBlockEntity>of(ControllerItemBlockEntity::new, WUTBlocks.RACKITEM_CONTROLLER.get()).build(null));
	public static final RegistryObject<BlockEntityType<ControllerFluidBlockEntity>> RACKFLUID_CONTROLLER = TILE_TYPES.register("rackfluid_controller", () -> BlockEntityType.Builder.<ControllerFluidBlockEntity>of(ControllerFluidBlockEntity::new, WUTBlocks.RACKFLUID_CONTROLLER.get()).build(null));

	/*
     * 
     * LIVING ENTITY
     * 
     */
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, WitherUtils.MODID);

	public static final RegistryObject<EntityType<CursedSeed>> CURSEDSEED = ENTITY_TYPES.register("cursedseed", () -> EntityType.Builder.of(CursedSeed::new, MobCategory.MONSTER)
			.setShouldReceiveVelocityUpdates(true)
			.setTrackingRange(64)
			.setUpdateInterval(3)
			.sized(0.8f, 3.0f)
			.fireImmune()
			.build(new ResourceLocation(WitherUtils.MODID, "cursedseed").toString()));
    
	public static final RegistryObject<EntityType<CursedZombie>> CURSEDZOMBIE = ENTITY_TYPES.register("cursedzombie", () -> EntityType.Builder.of(CursedZombie::new, MobCategory.MONSTER)
			.setShouldReceiveVelocityUpdates(true)
			.setTrackingRange(64)
			.setUpdateInterval(3)
			.sized(0.6F, 1.95F)
			.fireImmune()
			.build(new ResourceLocation(WitherUtils.MODID, "cursedzombie").toString()));
	
	public static final RegistryObject<EntityType<CursedCreeper>> CURSEDCREEPER = ENTITY_TYPES.register("cursedcreeper", () -> EntityType.Builder.of(CursedCreeper::new, MobCategory.MONSTER)
			.setShouldReceiveVelocityUpdates(true)
			.setTrackingRange(64)
			.setUpdateInterval(3)
			.sized(0.6F, 1.7F)
			.fireImmune()
			.build(new ResourceLocation(WitherUtils.MODID, "cursedcreeper").toString()));
	
	public static final RegistryObject<EntityType<CursedSkeleton>> CURSEDSKELETON = ENTITY_TYPES.register("cursedskeleton", () -> EntityType.Builder.of(CursedSkeleton::new, MobCategory.MONSTER)
			.setShouldReceiveVelocityUpdates(true)
			.setTrackingRange(64)
			.setUpdateInterval(3)
			.sized(0.6F, 1.95F)
			.fireImmune()
			.build(new ResourceLocation(WitherUtils.MODID, "cursedskeleton").toString()));
	
	public static final RegistryObject<EntityType<CursedSpider>> CURSEDSPIDER = ENTITY_TYPES.register("cursedspider", () -> EntityType.Builder.of(CursedSpider::new, MobCategory.MONSTER)
			.setShouldReceiveVelocityUpdates(true)
			.setTrackingRange(64)
			.setUpdateInterval(3)
			.sized(0.6F, 1.95F)
			.fireImmune()
			.build(new ResourceLocation(WitherUtils.MODID, "cursedspider").toString()));
	
    public static final RegistryObject<EntityType<SoulOrb>> SOULORB = ENTITY_TYPES.register("soulorb", () -> EntityType.Builder.<SoulOrb>of(SoulOrb::new, MobCategory.MISC)
    	.clientTrackingRange(10)
    	.sized(0.6F, 1.8F)
        .fireImmune()
        .build(new ResourceLocation(WitherUtils.MODID, "soulorb").toString()));
    
	public static final RegistryObject<EntityType<Worm>> WORM = ENTITY_TYPES.register("worm", () -> EntityType.Builder.of(Worm::new, MobCategory.MISC)
		.sized(0.6f, 1.8f)
		.setTrackingRange(64)
		.setUpdateInterval(1)
		.setShouldReceiveVelocityUpdates(false)
		.build(new ResourceLocation(WitherUtils.MODID, "worm").toString()));
	
	public static final RegistryObject<EntityType<SoulOrbProjectile>> SOULORBPRO = ENTITY_TYPES.register("soulorb_projectile", () -> EntityType.Builder.<SoulOrbProjectile> of(SoulOrbProjectile::new, MobCategory.MISC)
		.setShouldReceiveVelocityUpdates(true)
		.setUpdateInterval(1)
		.setTrackingRange(128)
		.sized(.6F, .6F)
		.build("soulorb_projectile"));
	
	public static final RegistryObject<EntityType<ChickenNaked>> CHICKENNAKED = ENTITY_TYPES.register("chickennaked", () -> EntityType.Builder.<ChickenNaked>of(ChickenNaked::new, MobCategory.CREATURE)
		.sized(0.4F, 0.7F)
		.setTrackingRange(80)
		.setUpdateInterval(1)
		.build(new ResourceLocation(WitherUtils.MODID, "chickennaked").toString()));
	
	public static final RegistryObject<EntityType<Portal>> PORTAL = ENTITY_TYPES.register("portal", () -> EntityType.Builder.<Portal>of(Portal::new, MobCategory.MISC)
			.setTrackingRange(5)
			.setUpdateInterval(20)
			.sized(2F, 3F)
			.build("portal"));
	
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event)
    {
        event.put(SOULORB.get(), SoulOrb.createAttributes().build());
        event.put(CHICKENNAKED.get(), ChickenNaked.createAttributes().build());
        event.put(CURSEDSEED.get(), CursedSeed.createAttributes().build());
        event.put(CURSEDZOMBIE.get(), CursedZombie.createAttributes().build());
        event.put(CURSEDCREEPER.get(), CursedCreeper.createAttributes().build());
        event.put(CURSEDSKELETON.get(), CursedSkeleton.createAttributes().build());
        event.put(CURSEDSPIDER.get(), CursedSpider.createAttributes().build());
    }

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
        	SoulOrb.init();
        	CursedZombie.init();
        	CursedCreeper.init();
        	CursedSkeleton.init();
        	CursedSpider.init();
        });
    }
}
