package geni.witherutils.base.common.init;

import geni.witherutils.WitherUtils;
import geni.witherutils.base.common.base.GlassGlowingBlock;
import geni.witherutils.base.common.base.WitherBlockItem;
import geni.witherutils.base.common.base.WitherGlassBlock;
import geni.witherutils.base.common.base.WitherSimpleBlock;
import geni.witherutils.base.common.base.WitherSoundType;
import geni.witherutils.base.common.block.activator.ActivatorBlock;
import geni.witherutils.base.common.block.angel.AngelBlock;
import geni.witherutils.base.common.block.angel.AngelBlockItem;
import geni.witherutils.base.common.block.anvil.AnvilBlock;
import geni.witherutils.base.common.block.battery.core.CoreBlock;
import geni.witherutils.base.common.block.battery.pylon.PylonBlock;
import geni.witherutils.base.common.block.battery.stab.StabBlock;
import geni.witherutils.base.common.block.cauldron.CauldronBlock;
import geni.witherutils.base.common.block.clicker.ClickerBlock;
import geni.witherutils.base.common.block.collector.CollectorBlock;
import geni.witherutils.base.common.block.creative.CreativeGeneratorBlock;
import geni.witherutils.base.common.block.creative.CreativeGeneratorBlockItem;
import geni.witherutils.base.common.block.creative.CreativeTrashBlock;
import geni.witherutils.base.common.block.creative.CreativeTrashBlockItem;
import geni.witherutils.base.common.block.cutter.CutterBlock;
import geni.witherutils.base.common.block.deco.catwalk.CatwalkBlock;
import geni.witherutils.base.common.block.deco.catwalk.CatwalkBlockItem;
import geni.witherutils.base.common.block.deco.door.DoorsBlock;
import geni.witherutils.base.common.block.deco.door.metal.MetalDoorBlock;
import geni.witherutils.base.common.block.deco.lavabricks.BricksLavaBlock;
import geni.witherutils.base.common.block.deco.pole.SteelPoleBlock;
import geni.witherutils.base.common.block.deco.railing.RailingBlock;
import geni.witherutils.base.common.block.deco.sliced.SlicedConcreteBlock;
import geni.witherutils.base.common.block.farmer.FarmerBlock;
import geni.witherutils.base.common.block.fisher.FisherBlock;
import geni.witherutils.base.common.block.fisher.FisherBlockItem;
import geni.witherutils.base.common.block.floodgate.FloodgateBlock;
import geni.witherutils.base.common.block.fluid.ExperienceBlock;
import geni.witherutils.base.common.block.fluid.FertilizerBlock;
import geni.witherutils.base.common.block.furnace.alloy.AlloyFurnaceBlock;
import geni.witherutils.base.common.block.furnace.electro.ElectroFurnaceBlock;
import geni.witherutils.base.common.block.generator.lava.LavaGeneratorBlock;
import geni.witherutils.base.common.block.generator.solar.SolarPanelBlock;
import geni.witherutils.base.common.block.generator.solar.SolarPanelBlockItem;
import geni.witherutils.base.common.block.generator.solar.SolarType;
import geni.witherutils.base.common.block.generator.water.WaterGeneratorBlock;
import geni.witherutils.base.common.block.generator.wind.WindGeneratorBlock;
import geni.witherutils.base.common.block.greenhouse.GreenhouseBlock;
import geni.witherutils.base.common.block.lilly.LillyBlock;
import geni.witherutils.base.common.block.miner.advanced.MinerAdvancedBlock;
import geni.witherutils.base.common.block.miner.basic.MinerBasicBlock;
import geni.witherutils.base.common.block.nature.WitherEarthBlock;
import geni.witherutils.base.common.block.nature.rotten.RottenLeavesBlock;
import geni.witherutils.base.common.block.nature.rotten.RottenSaplingBlock;
import geni.witherutils.base.common.block.placer.PlacerBlock;
import geni.witherutils.base.common.block.rack.casing.CaseBlock;
import geni.witherutils.base.common.block.rack.controller.fluid.ControllerFluidBlock;
import geni.witherutils.base.common.block.rack.controller.fluid.ControllerFluidBlockItem;
import geni.witherutils.base.common.block.rack.controller.item.ControllerItemBlock;
import geni.witherutils.base.common.block.rack.controller.item.ControllerItemBlockItem;
import geni.witherutils.base.common.block.rack.terminal.TerminalBlock;
import geni.witherutils.base.common.block.scanner.ScannerBlock;
import geni.witherutils.base.common.block.sensor.floor.FloorSensorBlock;
import geni.witherutils.base.common.block.sensor.wall.WallSensorBlock;
import geni.witherutils.base.common.block.smarttv.SmartTVBlock;
import geni.witherutils.base.common.block.spawner.SpawnerBlock;
import geni.witherutils.base.common.block.tank.drum.TankDrumBlock;
import geni.witherutils.base.common.block.tank.drum.TankDrumBlockItem;
import geni.witherutils.base.common.block.tank.reservoir.TankReservoirBlock;
import geni.witherutils.base.common.block.totem.TotemBlock;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class WUTBlocks {

	public static final DeferredRegister<Block> BLOCK_TYPES = DeferredRegister.create(ForgeRegistries.BLOCKS, WitherUtils.MODID);
	public static final DeferredRegister<Item> BLOCKITEM_TYPES = DeferredRegister.create(ForgeRegistries.ITEMS, WitherUtils.MODID);
	   
    /*
     * 
     * PROPERTIES
     * 
     */
    public static Block.Properties glassProps =
    		BlockBehaviour.Properties
            .of()
            .mapColor(MapColor.WATER)
            .strength(0.3F, 2000.0F)
            .sound(SoundType.GLASS)
            .noOcclusion();
	
    public static Block.Properties standardProps =
    		BlockBehaviour.Properties
            .of()
            .mapColor(MapColor.STONE)
            .strength(3.0F, 2000.0F)
            .sound(SoundType.STONE)
            .noOcclusion();
    
    public static Block.Properties metalProps =
    		BlockBehaviour.Properties
            .of()
            .mapColor(MapColor.METAL)
            .strength(1.5F, 2000.0F)
            .sound(WitherSoundType.SOULMETAL)
            .noOcclusion();
    
    public static Block.Properties machineProps =
    		BlockBehaviour.Properties
            .of()
            .mapColor(MapColor.METAL)
            .strength(3.0F, 2000.0F)
            .sound(WitherSoundType.SOULMETAL)
            .noOcclusion();

    public static Block.Properties plantProps =
    		BlockBehaviour.Properties
            .of()
            .mapColor(MapColor.PLANT)
            .noCollission()
            .randomTicks()
            .instabreak()
            .sound(SoundType.CROP);
    
    public static Block.Properties doorProps =
    		BlockBehaviour.Properties
            .of()
            .mapColor(MapColor.METAL)
            .noOcclusion()
            .requiresCorrectToolForDrops()
            .strength(1.5F, 6.0F)
            .sound(WitherSoundType.SOULMETAL);
    
    public static Block.Properties dirtProps =
    		BlockBehaviour.Properties
            .of()
            .mapColor(MapColor.DIRT)
            .randomTicks()
            .requiresCorrectToolForDrops()
            .strength(0.5F)
            .sound(SoundType.GRASS);
    
    public static Block.Properties woodProps =
    		BlockBehaviour.Properties
            .of()
            .mapColor(MapColor.WOOD)
            .requiresCorrectToolForDrops()
            .sound(SoundType.WOOD);
	/*
	 * 
	 * BLOCKS
	 * 
	 */
    public static final RegistryObject<Block> LINES = BLOCK_TYPES.register("lines", () -> new WitherSimpleBlock(standardProps));
    public static final RegistryObject<BlockItem> LINES_BI = BLOCKITEM_TYPES.register("lines", () -> new BlockItem(LINES.get(), new Item.Properties()));
    
    public static final RegistryObject<Block> ALLOY_FURNACE = BLOCK_TYPES.register("alloy_furnace", () -> new AlloyFurnaceBlock(machineProps));
    public static final RegistryObject<BlockItem> ALLOY_FURNACE_BI = BLOCKITEM_TYPES.register("alloy_furnace", () -> new BlockItem(ALLOY_FURNACE.get(), new Item.Properties()));

    public static final RegistryObject<Block> ELECTRO_FURNACE = BLOCK_TYPES.register("electro_furnace", () -> new ElectroFurnaceBlock(machineProps));
	public static final RegistryObject<BlockItem> ELECTRO_FURNACE_BI = BLOCKITEM_TYPES.register("electro_furnace", () -> new BlockItem(ELECTRO_FURNACE.get(), new Item.Properties()));

	public static final RegistryObject<Block> ANVIL = BLOCK_TYPES.register("anvil", () -> new AnvilBlock(machineProps));
	public static final RegistryObject<BlockItem> ANVIL_BI = BLOCKITEM_TYPES.register("anvil", () -> new BlockItem(ANVIL.get(), new Item.Properties()));

    public static final RegistryObject<Block> CAULDRON = BLOCK_TYPES.register("cauldron", () -> new CauldronBlock(machineProps));
    public static final RegistryObject<BlockItem> CAULDRON_BI = BLOCKITEM_TYPES.register("cauldron", () -> new BlockItem(CAULDRON.get(), new Item.Properties()));

    public static final RegistryObject<Block> LAVA_GENERATOR = BLOCK_TYPES.register("lava_generator", () -> new LavaGeneratorBlock(machineProps));
    public static final RegistryObject<BlockItem> LAVA_GENERATOR_BI = BLOCKITEM_TYPES.register("lava_generator", () -> new BlockItem(LAVA_GENERATOR.get(), new Item.Properties()));
    public static final RegistryObject<Block> WIND_GENERATOR = BLOCK_TYPES.register("wind_generator", () -> new WindGeneratorBlock(machineProps));
    public static final RegistryObject<BlockItem> WIND_GENERATOR_BI = BLOCKITEM_TYPES.register("wind_generator", () -> new WitherBlockItem(WIND_GENERATOR.get(), new Item.Properties(), false, false, true, 45));
	public static final RegistryObject<Block> WATER_GENERATOR = BLOCK_TYPES.register("water_generator", () -> new WaterGeneratorBlock(machineProps));
	public static final RegistryObject<BlockItem> WATER_GENERATOR_BI = BLOCKITEM_TYPES.register("water_generator", () -> new WitherBlockItem(WATER_GENERATOR.get(), new Item.Properties(), false, false, true, 45));
	
	public static final RegistryObject<Block> SOLARCASE = BLOCK_TYPES.register("solar_case", () -> new WitherSimpleBlock(glassProps));
	public static final RegistryObject<BlockItem> SOLARCASE_BI = BLOCKITEM_TYPES.register("solar_case", () -> new BlockItem(SOLARCASE.get(), new Item.Properties()));
	public static final RegistryObject<Block> SOLARBASIC = BLOCK_TYPES.register("solar_basic", () -> new SolarPanelBlock(glassProps, SolarType.BASIC));
	public static final RegistryObject<BlockItem> SOLARBASIC_BI = BLOCKITEM_TYPES.register("solar_basic", () -> new SolarPanelBlockItem(SOLARBASIC.get(), new Item.Properties(), SolarType.BASIC));
	public static final RegistryObject<Block> SOLARADV = BLOCK_TYPES.register("solar_adv", () -> new SolarPanelBlock(glassProps, SolarType.ADVANCED));
	public static final RegistryObject<BlockItem> SOLARADV_BI = BLOCKITEM_TYPES.register("solar_adv", () -> new SolarPanelBlockItem(SOLARADV.get(), new Item.Properties(), SolarType.ADVANCED));
	public static final RegistryObject<Block> SOLARULTRA = BLOCK_TYPES.register("solar_ultra", () -> new SolarPanelBlock(glassProps, SolarType.ULTRA));
	public static final RegistryObject<BlockItem> SOLARULTRA_BI = BLOCKITEM_TYPES.register("solar_ultra", () -> new SolarPanelBlockItem(SOLARULTRA.get(), new Item.Properties(), SolarType.ULTRA));
	public static final RegistryObject<Block> CREATIVE_GENERATOR = BLOCK_TYPES.register("creative_generator", () -> new CreativeGeneratorBlock(machineProps));
	public static final RegistryObject<BlockItem> CREATIVE_GENERATOR_BI = BLOCKITEM_TYPES.register("creative_generator", () -> new CreativeGeneratorBlockItem(CREATIVE_GENERATOR.get(), new Item.Properties()));

	public static final RegistryObject<Block> RESERVOIR = BLOCK_TYPES.register("tankreservoir", () -> new TankReservoirBlock(glassProps));
	public static final RegistryObject<BlockItem> RESERVOIR_BI = BLOCKITEM_TYPES.register("tankreservoir", () -> new BlockItem(RESERVOIR.get(), new Item.Properties()));

	public static final RegistryObject<Block> TANKDRUM = BLOCK_TYPES.register("tankdrum", () -> new TankDrumBlock(glassProps));
	public static final RegistryObject<BlockItem> TANKDRUM_BI = BLOCKITEM_TYPES.register("tankdrum", () -> new TankDrumBlockItem(TANKDRUM.get(), new Item.Properties()));
		
	public static final RegistryObject<Block> CORE = BLOCK_TYPES.register("core", () -> new CoreBlock(machineProps));
	public static final RegistryObject<BlockItem> CORE_BI = BLOCKITEM_TYPES.register("core", () -> new BlockItem(CORE.get(), new Item.Properties()));
	public static final RegistryObject<Block> PYLON = BLOCK_TYPES.register("pylon", () -> new PylonBlock(machineProps));
	public static final RegistryObject<BlockItem> PYLON_BI = BLOCKITEM_TYPES.register("pylon", () -> new BlockItem(PYLON.get(), new Item.Properties()));
	public static final RegistryObject<Block> STAB = BLOCK_TYPES.register("stab", () -> new StabBlock(machineProps));
	public static final RegistryObject<BlockItem> STAB_BI = BLOCKITEM_TYPES.register("stab", () -> new BlockItem(STAB.get(), new Item.Properties()));
	
	public static final RegistryObject<Block> CREATIVE_TRASH = BLOCK_TYPES.register("creative_trash", () -> new CreativeTrashBlock(machineProps));
	public static final RegistryObject<BlockItem> CREATIVE_TRASH_BI = BLOCKITEM_TYPES.register("creative_trash", () -> new CreativeTrashBlockItem(CREATIVE_TRASH.get(), new Item.Properties()));

	public static final RegistryObject<Block> ANGEL = BLOCK_TYPES.register("angel", () -> new AngelBlock(standardProps));
	public static final RegistryObject<BlockItem> ANGEL_BI = BLOCKITEM_TYPES.register("angel", () -> new AngelBlockItem(ANGEL.get(), new Item.Properties()));

    public static final RegistryObject<Block> LILLY = BLOCK_TYPES.register("lilly", () -> new LillyBlock(plantProps));
	public static final RegistryObject<BlockItem> LILLY_BI = BLOCKITEM_TYPES.register("lilly", () -> new BlockItem(LILLY.get(), new Item.Properties()));

	public static final RegistryObject<Block> SMARTTV = BLOCK_TYPES.register("smarttv", () -> new SmartTVBlock(machineProps));
	public static final RegistryObject<BlockItem> SMARTTV_BI = BLOCKITEM_TYPES.register("smarttv", () -> new BlockItem(SMARTTV.get(), new Item.Properties()));
	
	public static final RegistryObject<Block> MINERBASIC = BLOCK_TYPES.register("miner_basic", () -> new MinerBasicBlock(machineProps));
	public static final RegistryObject<BlockItem> MINERBASIC_BI = BLOCKITEM_TYPES.register("miner_basic", () -> new BlockItem(MINERBASIC.get(), new Item.Properties()));
	public static final RegistryObject<Block> MINERADV = BLOCK_TYPES.register("miner_adv", () -> new MinerAdvancedBlock(machineProps));
	public static final RegistryObject<BlockItem> MINERADV_BI = BLOCKITEM_TYPES.register("miner_adv", () -> new BlockItem(MINERADV.get(), new Item.Properties()));

	public static final RegistryObject<Block> FLOORSENSOR = BLOCK_TYPES.register("floorsensor", () -> new FloorSensorBlock(machineProps));
	public static final RegistryObject<BlockItem> FLOORSENSOR_BI = BLOCKITEM_TYPES.register("floorsensor", () -> new BlockItem(FLOORSENSOR.get(), new Item.Properties()));

	public static final RegistryObject<Block> WALLSENSOR = BLOCK_TYPES.register("wallsensor", () -> new WallSensorBlock(machineProps));
	public static final RegistryObject<BlockItem> WALLSENSOR_BI = BLOCKITEM_TYPES.register("wallsensor", () -> new BlockItem(WALLSENSOR.get(), new Item.Properties()));

//	public static final RegistryObject<Block> XPWIRELESS = BLOCK_TYPES.register("xpwireless", () -> new XpWirelessBlock(machineProps));
//	public static final RegistryObject<BlockItem> XPWIRELESS_BI = BLOCKITEM_TYPES.register("xpwireless", () -> new BlockItem(XPWIRELESS.get(), new Item.Properties()));
//	public static final RegistryObject<Block> XPPLATE = BLOCK_TYPES.register("xpplate", () -> new XpPlateBlock(machineProps));
//	public static final RegistryObject<BlockItem> XPPLATE_BI = BLOCKITEM_TYPES.register("xpplate", () -> new BlockItem(XPPLATE.get(), new Item.Properties()));

	public static final RegistryObject<Block> GREENHOUSE = BLOCK_TYPES.register("greenhouse", () -> new GreenhouseBlock(glassProps));
	public static final RegistryObject<BlockItem> GREENHOUSE_BI = BLOCKITEM_TYPES.register("greenhouse", () -> new BlockItem(GREENHOUSE.get(), new Item.Properties()));

	public static final RegistryObject<Block> COLLECTOR = BLOCK_TYPES.register("collector", () -> new CollectorBlock(machineProps));
	public static final RegistryObject<BlockItem> COLLECTOR_BI = BLOCKITEM_TYPES.register("collector", () -> new BlockItem(COLLECTOR.get(), new Item.Properties()));

	public static final RegistryObject<Block> ACTIVATOR = BLOCK_TYPES.register("activator", () -> new ActivatorBlock(machineProps));
	public static final RegistryObject<BlockItem> ACTIVATOR_BI = BLOCKITEM_TYPES.register("activator", () -> new BlockItem(ACTIVATOR.get(), new Item.Properties()));
	public static final RegistryObject<Block> CLICKER = BLOCK_TYPES.register("clicker", () -> new ClickerBlock(machineProps));
	public static final RegistryObject<BlockItem> CLICKER_BI = BLOCKITEM_TYPES.register("clicker", () -> new BlockItem(CLICKER.get(), new Item.Properties()));
	public static final RegistryObject<Block> PLACER = BLOCK_TYPES.register("placer", () -> new PlacerBlock(machineProps));
	public static final RegistryObject<BlockItem> PLACER_BI = BLOCKITEM_TYPES.register("placer", () -> new BlockItem(PLACER.get(), new Item.Properties()));
	public static final RegistryObject<Block> SCANNER = BLOCK_TYPES.register("scanner", () -> new ScannerBlock(machineProps));
	public static final RegistryObject<BlockItem> SCANNER_BI = BLOCKITEM_TYPES.register("scanner", () -> new BlockItem(SCANNER.get(), new Item.Properties()));

	public static final RegistryObject<Block> FARMER = BLOCK_TYPES.register("farmer", () -> new FarmerBlock(machineProps));
	public static final RegistryObject<BlockItem> FARMER_BI = BLOCKITEM_TYPES.register("farmer", () -> new BlockItem(FARMER.get(), new Item.Properties()));
	public static final RegistryObject<Block> SPAWNER = BLOCK_TYPES.register("spawner", () -> new SpawnerBlock(machineProps));
	public static final RegistryObject<BlockItem> SPAWNER_BI = BLOCKITEM_TYPES.register("spawner", () -> new BlockItem(SPAWNER.get(), new Item.Properties()));
	public static final RegistryObject<Block> FISHER = BLOCK_TYPES.register("fisher", () -> new FisherBlock(machineProps));
	public static final RegistryObject<BlockItem> FISHER_BI = BLOCKITEM_TYPES.register("fisher", () -> new FisherBlockItem(FISHER.get(), new Item.Properties()));
	
	public static final RegistryObject<Block> TOTEM = BLOCK_TYPES.register("totem", () -> new TotemBlock(standardProps));
	public static final RegistryObject<BlockItem> TOTEM_BI = BLOCKITEM_TYPES.register("totem", () -> new BlockItem(TOTEM.get(), new Item.Properties()));
	
	public static final RegistryObject<Block> FLOODGATE = BLOCK_TYPES.register("floodgate", () -> new FloodgateBlock());
	public static final RegistryObject<BlockItem> FLOODGATE_BI = BLOCKITEM_TYPES.register("floodgate", () -> new BlockItem(FLOODGATE.get(), new Item.Properties()));
	
	public static final RegistryObject<Block> RACK_CASE = BLOCK_TYPES.register("rack_case", () -> new CaseBlock(machineProps));
	public static final RegistryObject<BlockItem> RACK_CASE_BI = BLOCKITEM_TYPES.register("rack_case", () -> new BlockItem(RACK_CASE.get(), new Item.Properties()));
	public static final RegistryObject<Block> RACK_TERMINAL = BLOCK_TYPES.register("rack_terminal", () -> new TerminalBlock(machineProps));
	public static final RegistryObject<BlockItem> RACK_TERMINAL_BI = BLOCKITEM_TYPES.register("rack_terminal", () -> new BlockItem(RACK_TERMINAL.get(), new Item.Properties()));
	public static final RegistryObject<Block> RACKITEM_CONTROLLER = BLOCK_TYPES.register("rackitem_controller", () -> new ControllerItemBlock(machineProps));
	public static final RegistryObject<BlockItem> RACKITEM_CONTROLLER_BI = BLOCKITEM_TYPES.register("rackitem_controller", () -> new ControllerItemBlockItem(RACKITEM_CONTROLLER.get(), new Item.Properties()));
	public static final RegistryObject<Block> RACKFLUID_CONTROLLER = BLOCK_TYPES.register("rackfluid_controller", () -> new ControllerFluidBlock(machineProps));
	public static final RegistryObject<BlockItem> RACKFLUID_CONTROLLER_BI = BLOCKITEM_TYPES.register("rackfluid_controller", () -> new ControllerFluidBlockItem(RACKFLUID_CONTROLLER.get(), new Item.Properties()));

	/*
	 * 
	 * DECO
	 * 
	 */
	public static final RegistryObject<Block> BRICKSDARK = BLOCK_TYPES.register("bricks_dark", () -> new WitherSimpleBlock(standardProps));
	public static final RegistryObject<BlockItem> BRICKSDARK_BI = BLOCKITEM_TYPES.register("bricks_dark", () -> new BlockItem(BRICKSDARK.get(), new Item.Properties()));
	public static final RegistryObject<Block> BRICKSLAVA = BLOCK_TYPES.register("bricks_lava", () -> new BricksLavaBlock(Block.Properties.of().mapColor(MapColor.NETHER).requiresCorrectToolForDrops().strength(3.0F, 2000.0F).sound(WitherSoundType.STONE)));
	public static final RegistryObject<BlockItem> BRICKSLAVA_BI = BLOCKITEM_TYPES.register("bricks_lava", () -> new BlockItem(BRICKSLAVA.get(), new Item.Properties()));
	
	public static final RegistryObject<Block> STEELPOLEHEAD = BLOCK_TYPES.register("steel_pole_head", () -> new SteelPoleBlock(metalProps, SteelPoleBlock.getPixeledAABB(5,5,0, 11,11,16)));
	public static final RegistryObject<BlockItem> STEELPOLEHEAD_BI = BLOCKITEM_TYPES.register("steel_pole_head", () -> new BlockItem(STEELPOLEHEAD.get(), new Item.Properties()));
	public static final RegistryObject<Block> STEELPOLE = BLOCK_TYPES.register("steel_pole", () -> new SteelPoleBlock(metalProps, SteelPoleBlock.getPixeledAABB(5,5,0, 11,11,16)));
	public static final RegistryObject<BlockItem> STEELPOLE_BI = BLOCKITEM_TYPES.register("steel_pole", () -> new BlockItem(STEELPOLE.get(), new Item.Properties()));
	
	public static final RegistryObject<Block> STEELRAILING = BLOCK_TYPES.register("steel_railing", () -> new RailingBlock(metalProps, RailingBlock.getPixeledAABB(0,0,0,0,0,0), RailingBlock.getPixeledAABB(0,0,0,16,15.9,1)));
	public static final RegistryObject<BlockItem> STEELRAILING_BI = BLOCKITEM_TYPES.register("steel_railing", () -> new BlockItem(STEELRAILING.get(), new Item.Properties()));
	
	public static final RegistryObject<Block> SLICEDCONCRETEBLACK = BLOCK_TYPES.register("slicedconcrete_black", () -> new SlicedConcreteBlock(standardProps));
	public static final RegistryObject<BlockItem> SLICEDCONCRETEBLACK_BI = BLOCKITEM_TYPES.register("slicedconcrete_black", () -> new BlockItem(SLICEDCONCRETEBLACK.get(), new Item.Properties()));
	public static final RegistryObject<Block> SLICEDCONCRETEGRAY = BLOCK_TYPES.register("slicedconcrete_gray", () -> new SlicedConcreteBlock(standardProps));
	public static final RegistryObject<BlockItem> SLICEDCONCRETEGRAY_BI = BLOCKITEM_TYPES.register("slicedconcrete_gray", () -> new BlockItem(SLICEDCONCRETEGRAY.get(), new Item.Properties()));
	public static final RegistryObject<Block> SLICEDCONCRETEWHITE = BLOCK_TYPES.register("slicedconcrete_white", () -> new SlicedConcreteBlock(standardProps));
	public static final RegistryObject<BlockItem> SLICEDCONCRETEWHITE_BI = BLOCKITEM_TYPES.register("slicedconcrete_white", () -> new BlockItem(SLICEDCONCRETEWHITE.get(), new Item.Properties()));

	public static final RegistryObject<Block> CATWALK = BLOCK_TYPES.register("catwalk", () -> new CatwalkBlock(metalProps));
	public static final RegistryObject<BlockItem> CATWALK_BI = BLOCKITEM_TYPES.register("catwalk", () -> new CatwalkBlockItem(CATWALK.get(), new Item.Properties()));

	/*
	 * 
	 * MATERIALS
	 * 
	 */
    public static final RegistryObject<Block> CASE_SMALL = BLOCK_TYPES.register("case_small", () -> new WitherSimpleBlock(machineProps));
    public static final RegistryObject<BlockItem> CASE_SMALL_BI = BLOCKITEM_TYPES.register("case_small", () -> new BlockItem(CASE_SMALL.get(), new Item.Properties()));
    
    public static final RegistryObject<Block> CASE_BIG = BLOCK_TYPES.register("case_big", () -> new WitherSimpleBlock(machineProps));
    public static final RegistryObject<BlockItem> CASE_BIG_BI = BLOCKITEM_TYPES.register("case_big", () -> new BlockItem(CASE_BIG.get(), new Item.Properties()));
	
    public static final RegistryObject<Block> WITHERSTEEL_BLOCK = BLOCK_TYPES.register("withersteel_block", () -> new WitherSimpleBlock(standardProps));
    public static final RegistryObject<BlockItem> WITHERSTEEL_BLOCK_BI = BLOCKITEM_TYPES.register("withersteel_block", () -> new BlockItem(WITHERSTEEL_BLOCK.get(), new Item.Properties()));
    
    public static final RegistryObject<Block> SOULISHED_BLOCK = BLOCK_TYPES.register("soulished_block", () -> new WitherSimpleBlock(standardProps));
    public static final RegistryObject<BlockItem> SOULISHED_BLOCK_BI = BLOCKITEM_TYPES.register("soulished_block", () -> new BlockItem(SOULISHED_BLOCK.get(), new Item.Properties()));
    
    /*
     * 
     * CTM
     * 
     */
	public static final RegistryObject<Block> CTM_CONCRETE_A = BLOCK_TYPES.register("ctm_concrete_a", () -> new CutterBlock(standardProps));
	public static final RegistryObject<BlockItem> CTM_CONCRETE_A_BI = BLOCKITEM_TYPES.register("ctm_concrete_a", () -> new BlockItem(CTM_CONCRETE_A.get(), new Item.Properties()));
	public static final RegistryObject<Block> CTM_CONCRETE_B = BLOCK_TYPES.register("ctm_concrete_b", () -> new CutterBlock(standardProps));
	public static final RegistryObject<BlockItem> CTM_CONCRETE_B_BI = BLOCKITEM_TYPES.register("ctm_concrete_b", () -> new BlockItem(CTM_CONCRETE_B.get(), new Item.Properties()));
	public static final RegistryObject<Block> CTM_CONCRETE_C = BLOCK_TYPES.register("ctm_concrete_c", () -> new CutterBlock(standardProps));
	public static final RegistryObject<BlockItem> CTM_CONCRETE_C_BI = BLOCKITEM_TYPES.register("ctm_concrete_c", () -> new BlockItem(CTM_CONCRETE_C.get(), new Item.Properties()));
	public static final RegistryObject<Block> CTM_METAL_A = BLOCK_TYPES.register("ctm_metal_a", () -> new CutterBlock(standardProps));
	public static final RegistryObject<BlockItem> CTM_METAL_A_BI = BLOCKITEM_TYPES.register("ctm_metal_a", () -> new BlockItem(CTM_METAL_A.get(), new Item.Properties()));
	public static final RegistryObject<Block> CTM_STONE_A = BLOCK_TYPES.register("ctm_stone_a", () -> new CutterBlock(standardProps));
	public static final RegistryObject<BlockItem> CTM_STONE_A_BI = BLOCKITEM_TYPES.register("ctm_stone_a", () -> new BlockItem(CTM_STONE_A.get(), new Item.Properties()));
	public static final RegistryObject<Block> CTM_GLASS_A = BLOCK_TYPES.register("ctm_glass_a", () -> new WitherGlassBlock(glassProps));
	public static final RegistryObject<BlockItem> CTM_GLASS_A_BI = BLOCKITEM_TYPES.register("ctm_glass_a", () -> new BlockItem(CTM_GLASS_A.get(), new Item.Properties()));
	public static final RegistryObject<Block> CTM_GLASS_B = BLOCK_TYPES.register("ctm_glass_b", () -> new WitherGlassBlock(glassProps));
	public static final RegistryObject<BlockItem> CTM_GLASS_B_BI = BLOCKITEM_TYPES.register("ctm_glass_b", () -> new BlockItem(CTM_GLASS_B.get(), new Item.Properties()));
	public static final RegistryObject<Block> CTM_GLASS_C = BLOCK_TYPES.register("ctm_glass_c", () -> new GlassGlowingBlock(glassProps));
	public static final RegistryObject<BlockItem> CTM_GLASS_C_BI = BLOCKITEM_TYPES.register("ctm_glass_c", () -> new BlockItem(CTM_GLASS_C.get(), new Item.Properties()));

	/*
	 * 
	 * DOORS
	 * 
	 */
	public static final RegistryObject<Block> CASED_DOOR = BLOCK_TYPES.register("cased_door", () -> new DoorsBlock(doorProps, DoorsBlock.getPixeledAABB(15,0, 0, 16,16,16), DoorsBlock.getPixeledAABB( 0,0,13, 16,16,16), SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_DOOR_CLOSE));
	public static final RegistryObject<BlockItem> CASED_DOOR_BI = BLOCKITEM_TYPES.register("cased_door", () -> new BlockItem(CASED_DOOR.get(), new Item.Properties()));
	public static final RegistryObject<Block> CREEP_DOOR = BLOCK_TYPES.register("creep_door", () -> new DoorsBlock(doorProps, DoorsBlock.getPixeledAABB(15,0, 0, 16,16,16), DoorsBlock.getPixeledAABB( 0,0,13, 16,16,16), SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_DOOR_CLOSE));
	public static final RegistryObject<BlockItem> CREEP_DOOR_BI = BLOCKITEM_TYPES.register("creep_door", () -> new BlockItem(CREEP_DOOR.get(), new Item.Properties()));
	public static final RegistryObject<Block> LIRON_DOOR = BLOCK_TYPES.register("liron_door", () -> new DoorsBlock(doorProps, DoorsBlock.getPixeledAABB(15,0, 0, 16,16,16), DoorsBlock.getPixeledAABB( 0,0,13, 16,16,16), SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_DOOR_CLOSE));
	public static final RegistryObject<BlockItem> LIRON_DOOR_BI = BLOCKITEM_TYPES.register("liron_door", () -> new BlockItem(LIRON_DOOR.get(), new Item.Properties()));
	public static final RegistryObject<Block> STEEL_DOOR = BLOCK_TYPES.register("steel_door", () -> new DoorsBlock(doorProps, DoorsBlock.getPixeledAABB(15,0, 0, 16,16,16), DoorsBlock.getPixeledAABB( 0,0,13, 16,16,16), SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_DOOR_CLOSE));
	public static final RegistryObject<BlockItem> STEEL_DOOR_BI = BLOCKITEM_TYPES.register("steel_door", () -> new BlockItem(STEEL_DOOR.get(), new Item.Properties()));
	public static final RegistryObject<Block> STRIPED_DOOR = BLOCK_TYPES.register("striped_door", () -> new DoorsBlock(doorProps, DoorsBlock.getPixeledAABB(15,0, 0, 16,16,16), DoorsBlock.getPixeledAABB( 0,0,13, 16,16,16), SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_DOOR_CLOSE));
	public static final RegistryObject<BlockItem> STRIPED_DOOR_BI = BLOCKITEM_TYPES.register("striped_door", () -> new BlockItem(STRIPED_DOOR.get(), new Item.Properties()));
	
	public static final RegistryObject<Block> METALDOOR = BLOCK_TYPES.register("metaldoor", () -> new MetalDoorBlock(doorProps, new AABB[] { MetalDoorBlock.getPixeledAABB(0,0,0, 16,16,16), MetalDoorBlock.getPixeledAABB(0,0,0, 16,16,16) } )); // (5,0,0, 11,16,16)
	public static final RegistryObject<BlockItem> METALDOOR_BI = BLOCKITEM_TYPES.register("metaldoor", () -> new CatwalkBlockItem(METALDOOR.get(), new Item.Properties()));
	
    /*
     * 
     * FLUIDBLOCKS
     * 
     */
	public static final RegistryObject<LiquidBlock> FERTILIZER_MOLTEN = BLOCK_TYPES.register("fertilizer_molten", () -> new FertilizerBlock());
	public static final RegistryObject<LiquidBlock> EXPERIENCE_MOLTEN = BLOCK_TYPES.register("experience_molten", () -> new ExperienceBlock());
	
	/*
	 * 
	 * ROTTEN
	 * 
	 */
    public static final RegistryObject<Block> WITHEREARTH = BLOCK_TYPES.register("witherearth", () -> new WitherEarthBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT)));
    public static final RegistryObject<BlockItem> WITHEREARTH_BI = BLOCKITEM_TYPES.register("witherearth", () -> new BlockItem(WITHEREARTH.get(), new Item.Properties()));
    public static final RegistryObject<Block> ROTTEN_LOG = BLOCK_TYPES.register("rotten_log", () -> WUTBlocks.log(DyeColor.BLACK, DyeColor.BLACK));
    public static final RegistryObject<BlockItem> ROTTEN_LOG_BI = BLOCKITEM_TYPES.register("rotten_log", () -> new BlockItem(ROTTEN_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Block> ROTTEN_LEAVES = BLOCK_TYPES.register("rotten_leaves", () -> new RottenLeavesBlock());
    public static final RegistryObject<BlockItem> ROTTEN_LEAVES_BI = BLOCKITEM_TYPES.register("rotten_leaves", () -> new BlockItem(ROTTEN_LEAVES.get(), new Item.Properties()));
	public static final RegistryObject<Block> ROTTEN_SAPLING = BLOCK_TYPES.register("rotten_sapling", () -> new RottenSaplingBlock());
	public static final RegistryObject<BlockItem> ROTTEN_SAPLING_BI = BLOCKITEM_TYPES.register("rotten_sapling", () -> new BlockItem(ROTTEN_SAPLING.get(), new Item.Properties()));

    private static RotatedPillarBlock log(DyeColor color1, DyeColor color2)
    {
        return new RotatedPillarBlock(BlockBehaviour.Properties.of().strength(2.0f).sound(SoundType.WOOD));
    }
}
