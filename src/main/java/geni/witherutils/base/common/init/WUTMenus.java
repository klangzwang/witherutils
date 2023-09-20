package geni.witherutils.base.common.init;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.block.activator.ActivatorBlockEntity;
import geni.witherutils.base.common.block.activator.ActivatorContainer;
import geni.witherutils.base.common.block.battery.core.CoreBlockEntity;
import geni.witherutils.base.common.block.battery.core.CoreContainer;
import geni.witherutils.base.common.block.clicker.ClickerBlockEntity;
import geni.witherutils.base.common.block.clicker.ClickerContainer;
import geni.witherutils.base.common.block.collector.CollectorBlockEntity;
import geni.witherutils.base.common.block.collector.CollectorContainer;
import geni.witherutils.base.common.block.creative.CreativeGeneratorBlockEntity;
import geni.witherutils.base.common.block.creative.CreativeGeneratorContainer;
import geni.witherutils.base.common.block.farmer.FarmerBlockEntity;
import geni.witherutils.base.common.block.farmer.FarmerContainer;
import geni.witherutils.base.common.block.fisher.FisherBlockEntity;
import geni.witherutils.base.common.block.fisher.FisherContainer;
import geni.witherutils.base.common.block.floodgate.FloodgateBlockEntity;
import geni.witherutils.base.common.block.floodgate.FloodgateContainer;
import geni.witherutils.base.common.block.furnace.alloy.AlloyFurnaceBlockEntity;
import geni.witherutils.base.common.block.furnace.alloy.AlloyFurnaceContainer;
import geni.witherutils.base.common.block.furnace.electro.ElectroFurnaceBlockEntity;
import geni.witherutils.base.common.block.furnace.electro.ElectroFurnaceContainer;
import geni.witherutils.base.common.block.generator.lava.LavaGeneratorBlockEntity;
import geni.witherutils.base.common.block.generator.lava.LavaGeneratorContainer;
import geni.witherutils.base.common.block.generator.water.WaterGeneratorBlockEntity;
import geni.witherutils.base.common.block.generator.water.WaterGeneratorContainer;
import geni.witherutils.base.common.block.generator.wind.WindGeneratorBlockEntity;
import geni.witherutils.base.common.block.generator.wind.WindGeneratorContainer;
import geni.witherutils.base.common.block.miner.advanced.MinerAdvancedBlockEntity;
import geni.witherutils.base.common.block.miner.advanced.MinerAdvancedContainer;
import geni.witherutils.base.common.block.miner.basic.MinerBasicBlockEntity;
import geni.witherutils.base.common.block.miner.basic.MinerBasicContainer;
import geni.witherutils.base.common.block.placer.PlacerBlockEntity;
import geni.witherutils.base.common.block.placer.PlacerContainer;
import geni.witherutils.base.common.block.scanner.ScannerBlockEntity;
import geni.witherutils.base.common.block.scanner.ScannerContainer;
import geni.witherutils.base.common.block.sensor.floor.FloorSensorBlockEntity;
import geni.witherutils.base.common.block.sensor.floor.FloorSensorContainer;
import geni.witherutils.base.common.block.smarttv.SmartTVBlockEntity;
import geni.witherutils.base.common.block.smarttv.SmartTVContainer;
import geni.witherutils.base.common.block.spawner.SpawnerBlockEntity;
import geni.witherutils.base.common.block.spawner.SpawnerContainer;
import geni.witherutils.base.common.block.tank.drum.TankDrumBlockEntity;
import geni.witherutils.base.common.block.tank.drum.TankDrumContainer;
import geni.witherutils.base.common.block.totem.TotemBlockEntity;
import geni.witherutils.base.common.block.totem.TotemContainer;
import geni.witherutils.base.common.item.card.CardContainer;
import geni.witherutils.base.common.item.cutter.CutterContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class WUTMenus {
    
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, WitherUtils.MODID);
    
    public static final RegistryObject<MenuType<AlloyFurnaceContainer>> ALLOY_FURNACE = CONTAINERS.register("alloy_furnace", () -> IForgeMenuType.create((windowId, inv, data) -> new AlloyFurnaceContainer((AlloyFurnaceBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));
    public static final RegistryObject<MenuType<ElectroFurnaceContainer>> ELECTRO_FURNACE = CONTAINERS.register("electro_furnace", () -> IForgeMenuType.create((windowId, inv, data) -> new ElectroFurnaceContainer((ElectroFurnaceBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));
    
    public static final RegistryObject<MenuType<LavaGeneratorContainer>> LAVA_GENERATOR = CONTAINERS.register("lava_generator", () -> IForgeMenuType.create((windowId, inv, data) -> new LavaGeneratorContainer((LavaGeneratorBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));
    public static final RegistryObject<MenuType<WaterGeneratorContainer>> WATER_GENERATOR = CONTAINERS.register("water_generator", () -> IForgeMenuType.create((windowId, inv, data) -> new WaterGeneratorContainer((WaterGeneratorBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));
    public static final RegistryObject<MenuType<WindGeneratorContainer>> WIND_GENERATOR = CONTAINERS.register("wind_generator", () -> IForgeMenuType.create((windowId, inv, data) -> new WindGeneratorContainer((WindGeneratorBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));

    public static final RegistryObject<MenuType<CoreContainer>> CORE = CONTAINERS.register("core", () -> IForgeMenuType.create((windowId, inv, data) -> new CoreContainer((CoreBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));
    
    public static final RegistryObject<MenuType<SmartTVContainer>> SMARTTV = CONTAINERS.register("smarttv", () -> IForgeMenuType.create((windowId, inv, data) -> new SmartTVContainer((SmartTVBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));

    public static final RegistryObject<MenuType<MinerBasicContainer>> MINERBASIC = CONTAINERS.register("miner_basic", () -> IForgeMenuType.create((windowId, inv, data) -> new MinerBasicContainer((MinerBasicBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));
    public static final RegistryObject<MenuType<MinerAdvancedContainer>> MINERADV = CONTAINERS.register("miner_adv", () -> IForgeMenuType.create((windowId, inv, data) -> new MinerAdvancedContainer((MinerAdvancedBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));

    public static final RegistryObject<MenuType<FloorSensorContainer>> FLOORSENSOR = CONTAINERS.register("floorsensor", () -> IForgeMenuType.create((windowId, inv, data) -> new FloorSensorContainer((FloorSensorBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));

    public static final RegistryObject<MenuType<TankDrumContainer>> TANKDRUM = CONTAINERS.register("tankdrum", () -> IForgeMenuType.create((windowId, inv, data) -> new TankDrumContainer((TankDrumBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));
    
    public static final RegistryObject<MenuType<CollectorContainer>> COLLECTOR = CONTAINERS.register("collector", () -> IForgeMenuType.create((windowId, inv, data) -> new CollectorContainer((CollectorBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));
    public static final RegistryObject<MenuType<FarmerContainer>> FARMER = CONTAINERS.register("farmer", () -> IForgeMenuType.create((windowId, inv, data) -> new FarmerContainer((FarmerBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));
    public static final RegistryObject<MenuType<SpawnerContainer>> SPAWNER = CONTAINERS.register("spawner", () -> IForgeMenuType.create((windowId, inv, data) -> new SpawnerContainer((SpawnerBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));
    
    public static final RegistryObject<MenuType<ActivatorContainer>> ACTIVATOR = CONTAINERS.register("activator", () -> IForgeMenuType.create((windowId, inv, data) -> new ActivatorContainer((ActivatorBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));
    public static final RegistryObject<MenuType<ClickerContainer>> CLICKER = CONTAINERS.register("clicker", () -> IForgeMenuType.create((windowId, inv, data) -> new ClickerContainer((ClickerBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));
    public static final RegistryObject<MenuType<PlacerContainer>> PLACER = CONTAINERS.register("placer", () -> IForgeMenuType.create((windowId, inv, data) -> new PlacerContainer((PlacerBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));
    public static final RegistryObject<MenuType<ScannerContainer>> SCANNER = CONTAINERS.register("scanner", () -> IForgeMenuType.create((windowId, inv, data) -> new ScannerContainer((ScannerBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));
    public static final RegistryObject<MenuType<FisherContainer>> FISHER = CONTAINERS.register("fisher", () -> IForgeMenuType.create((windowId, inv, data) -> new FisherContainer((FisherBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));
    public static final RegistryObject<MenuType<FloodgateContainer>> FLOODGATE = CONTAINERS.register("floodgate", () -> IForgeMenuType.create((windowId, inv, data) -> new FloodgateContainer((FloodgateBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));
    public static final RegistryObject<MenuType<TotemContainer>> TOTEM = CONTAINERS.register("totem", () -> IForgeMenuType.create((windowId, inv, data) -> new TotemContainer((TotemBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));
    
    public static final RegistryObject<MenuType<CreativeGeneratorContainer>> CREATIVEGEN = CONTAINERS.register("creative_generator", () -> IForgeMenuType.create((windowId, inv, data) -> new CreativeGeneratorContainer((CreativeGeneratorBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()), inv, windowId)));
    
    public static final RegistryObject<MenuType<CutterContainer>> CUTTER = CONTAINERS.register("cutter", () -> IForgeMenuType.create((windowId, inv, data) -> new CutterContainer(windowId, inv, inv.player)));
    public static final RegistryObject<MenuType<CardContainer>> BLOCKCARD = CONTAINERS.register("blockcard", () -> IForgeMenuType.create((windowId, inv, data) -> new CardContainer(windowId, inv, inv.player)));
}
