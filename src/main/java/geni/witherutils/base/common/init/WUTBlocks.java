package geni.witherutils.base.common.init;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import geni.witherutils.api.lib.Names;
import geni.witherutils.base.common.base.WitherSimpleBlock;
import geni.witherutils.base.common.base.WitherSoundType;
import geni.witherutils.base.common.block.angel.AngelBlock;
import geni.witherutils.base.common.block.anvil.AnvilBlock;
import geni.witherutils.base.common.block.cauldron.CauldronBlock;
import geni.witherutils.base.common.block.collector.CollectorBlock;
import geni.witherutils.base.common.block.creative.CreativeEnergyBlock;
import geni.witherutils.base.common.block.deco.catwalk.CatwalkBlock;
import geni.witherutils.base.common.block.deco.cutter.CutterBlock;
import geni.witherutils.base.common.block.deco.cutter.CutterBlock.CutterBlockType;
import geni.witherutils.base.common.block.deco.door.DoorsBlock;
import geni.witherutils.base.common.block.deco.door.metal.MetalDoorBlock;
import geni.witherutils.base.common.block.deco.fan.FanBlock;
import geni.witherutils.base.common.block.deco.fire.SoulFireBlock;
import geni.witherutils.base.common.block.deco.lavabricks.BricksLavaBlock;
import geni.witherutils.base.common.block.deco.light.LightBlock;
import geni.witherutils.base.common.block.deco.pole.SteelPoleBlock;
import geni.witherutils.base.common.block.deco.railing.RailingBlock;
import geni.witherutils.base.common.block.deco.sliced.SlicedConcreteBlock;
import geni.witherutils.base.common.block.fakedriver.FakeDriverBlock;
import geni.witherutils.base.common.block.fakedriver.FakeDriverBlockEntity;
import geni.witherutils.base.common.block.fisher.FisherBlock;
import geni.witherutils.base.common.block.fluid.BlueLimboBlock;
import geni.witherutils.base.common.block.fluid.ColdSlushBlock;
import geni.witherutils.base.common.block.fluid.ExperienceBlock;
import geni.witherutils.base.common.block.fluid.FertilizerBlock;
import geni.witherutils.base.common.block.fluid.PortiumBlock;
import geni.witherutils.base.common.block.fluid.RedResinBlock;
import geni.witherutils.base.common.block.fluid.SoulfulBlock;
import geni.witherutils.base.common.block.fluid.WitherWaterBlock;
import geni.witherutils.base.common.block.generator.lava.LavaGeneratorBlock;
import geni.witherutils.base.common.block.generator.solar.SolarPanelBlock;
import geni.witherutils.base.common.block.generator.solar.SolarType;
import geni.witherutils.base.common.block.generator.water.WaterGeneratorBlock;
import geni.witherutils.base.common.block.generator.wind.WindGeneratorBlock;
import geni.witherutils.base.common.block.greenhouse.GreenhouseBlock;
import geni.witherutils.base.common.block.lilly.LillyBlock;
import geni.witherutils.base.common.block.nature.RottenEarth;
import geni.witherutils.base.common.block.nature.RottenRoots;
import geni.witherutils.base.common.block.nature.RottenSapling;
import geni.witherutils.base.common.block.nature.RottenSpike;
import geni.witherutils.base.common.block.nature.RottenWood;
import geni.witherutils.base.common.block.sensor.floor.FloorSensorBlock;
import geni.witherutils.base.common.block.sensor.wall.WallSensorBlock;
import geni.witherutils.base.common.block.smarttv.SmartTVBlock;
import geni.witherutils.base.common.block.spawner.SpawnerBlock;
import geni.witherutils.base.common.block.totem.TotemBlock;
import geni.witherutils.base.common.block.xpdrain.XpDrainBlock;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class WUTBlocks {

	public static final DeferredRegister.Blocks BLOCK_TYPES = DeferredRegister.createBlocks(Names.MODID);
    public static final DeferredRegister<Item> ITEMS = WUTItems.ITEM_TYPES;
    
    /*
     * 
     * TREEGROWER
     * 
     */
    public static final TreeGrower ROTTEN = new TreeGrower(
        "rotten",
        0.1F,
        Optional.empty(),
        Optional.empty(),
        Optional.of(TreeFeatures.OAK),
        Optional.of(TreeFeatures.FANCY_OAK),
        Optional.of(TreeFeatures.OAK_BEES_005),
        Optional.of(TreeFeatures.FANCY_OAK_BEES_005)
    );
    
    /*
     * 
     * PROPERTIES
     * 
     */
    public static Block.Properties soulFireProps()
    {
        return Block.Properties
                .of()
                .mapColor(MapColor.COLOR_LIGHT_BLUE)
                .replaceable()
                .noCollission()
                .instabreak()
                .lightLevel(p_50884_ -> 10)
                .sound(SoundType.WOOL)
                .pushReaction(PushReaction.DESTROY);
    }
    
    public static Block.Properties glassProps()
    {
        return Block.Properties
                .of()
                .mapColor(MapColor.WATER)
                .strength(0.3F, 2000.0F)
                .sound(SoundType.GLASS)
                .noOcclusion();
    }
	
    public static Block.Properties standardProps()
    {
        return Block.Properties
                .of()
                .mapColor(MapColor.STONE)
                .strength(3.0F, 2000.0F)
                .sound(WitherSoundType.STANDARD)
                .noOcclusion();
    }
    
    public static Block.Properties metalProps()
    {
        return Block.Properties
                .of()
                .mapColor(MapColor.METAL)
                .strength(1.5F, 2000.0F)
                .sound(WitherSoundType.SOULMETAL)
                .noOcclusion();
    }
    
    public static Block.Properties machineProps()
    {
        return Block.Properties
                .of()
                .mapColor(MapColor.METAL)
                .strength(3.0F, 2000.0F)
                .sound(WitherSoundType.SOULMETAL)
                .noOcclusion();
    }

    public static Block.Properties plantProps()
    {
        return Block.Properties
                .of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.CROP);
    }
    
    public static Block.Properties doorProps()
    {
        return Block.Properties
                .of()
                .mapColor(MapColor.METAL)
                .noOcclusion()
                .requiresCorrectToolForDrops()
                .strength(1.5F, 6.0F)
                .sound(WitherSoundType.SOULMETAL);
    }
    
    public static Block.Properties dirtProps()
    {
        return Block.Properties
                .of()
                .mapColor(MapColor.DIRT)
                .randomTicks()
                .requiresCorrectToolForDrops()
                .strength(0.5F)
                .sound(SoundType.GRASS);
    }
    
    public static Block.Properties woodProps()
    {
        return Block.Properties
                .of()
                .mapColor(MapColor.WOOD)
                .requiresCorrectToolForDrops()
                .sound(SoundType.WOOD);
    }
    
    private static Block.Properties fluidProps()
    {
        return Block.Properties.of()
			    .mapColor(MapColor.WATER)
			    .noCollission()
			    .strength(100f)
			    .noLootTable()
			    .replaceable();
    }

	/*
	 * 
	 * LOGICALBLOCK
	 *
	 */
    public static final DeferredBlock<Block> LINES = register("lines", () -> new WitherSimpleBlock(standardProps()));
    
	public static final DeferredBlock<Block> ANVIL = register("anvil", () -> new AnvilBlock(machineProps()));
	public static final DeferredBlock<Block> FAKE_DRIVER = register("fake_driver", () -> new FakeDriverBlock(FakeDriverBlockEntity::new));
	
	public static final DeferredBlock<Block> CREATIVE_GENERATOR = register("creative_generator", () -> new CreativeEnergyBlock(machineProps()));
	
	public static final DeferredBlock<Block> GREENHOUSE = register("greenhouse", () -> new GreenhouseBlock(machineProps()));
	public static final DeferredBlock<Block> CAULDRON = register("cauldron", () -> new CauldronBlock(machineProps()));
	public static final DeferredBlock<Block> COLLECTOR = register("collector", () -> new CollectorBlock(machineProps()));
	
	public static final DeferredBlock<Block> TOTEM = register("totem", () -> new TotemBlock(machineProps()));
	public static final DeferredBlock<Block> XPDRAIN = register("xpdrain", () -> new XpDrainBlock(machineProps()));

    public static final DeferredBlock<Block> LILLY = register("lilly", () -> new LillyBlock(plantProps()));

    public static final DeferredBlock<Block> LAVA_GENERATOR = register("lava_generator", () -> new LavaGeneratorBlock(machineProps()));
    public static final DeferredBlock<Block> WIND_GENERATOR = register("wind_generator", () -> new WindGeneratorBlock(machineProps()));
	public static final DeferredBlock<Block> WATER_GENERATOR = register("water_generator", () -> new WaterGeneratorBlock(machineProps()));
	
	public static final DeferredBlock<Block> SOLARCASE = register("solar_case", () -> new WitherSimpleBlock(glassProps()));
	public static final DeferredBlock<Block> SOLARBASIC = register("solar_basic", () -> new SolarPanelBlock(glassProps(), SolarType.BASIC));
	public static final DeferredBlock<Block> SOLARADV = register("solar_adv", () -> new SolarPanelBlock(glassProps(), SolarType.ADVANCED));
	public static final DeferredBlock<Block> SOLARULTRA = register("solar_ultra", () -> new SolarPanelBlock(glassProps(), SolarType.ULTRA));
    
	public static final DeferredBlock<Block> SMARTTV = register("smarttv", () -> new SmartTVBlock(machineProps()));
	
	public static final DeferredBlock<Block> FLOORSENSOR = register("floorsensor", () -> new FloorSensorBlock(machineProps()));
	public static final DeferredBlock<Block> WALLSENSOR = register("wallsensor", () -> new WallSensorBlock(machineProps()));
	
//	public static final DeferredBlock<Block> FARMER = register("farmer", () -> new FarmerBlock(machineProps()));
	public static final DeferredBlock<Block> SPAWNER = register("spawner", () -> new SpawnerBlock(machineProps()));
	public static final DeferredBlock<Block> FISHER = register("fisher", () -> new FisherBlock(machineProps()));
	
	/*
	 * 
	 * WITHERUTILSBLOCK
	 *
	 */
	public static final DeferredBlock<Block> ANGEL = register("angel", () -> new AngelBlock(standardProps()));
	
	/*
	 * 
	 * MATERIALS
	 * 
	 */
    public static final DeferredBlock<Block> CASE_SMALL = register("case_small", () -> new WitherSimpleBlock(machineProps()));
    public static final DeferredBlock<Block> CASE_BIG = register("case_big", () -> new WitherSimpleBlock(machineProps()));
    public static final DeferredBlock<Block> WITHERSTEEL_BLOCK = register("withersteel_block", () -> new WitherSimpleBlock(standardProps()));
    public static final DeferredBlock<Block> SOULISHED_BLOCK = register("soulished_block", () -> new WitherSimpleBlock(standardProps()));

	/*
	 * 
	 * DOORS
	 * 
	 */
	public static final DeferredBlock<Block> DOOR_CASED = register("cased_door", () -> new DoorsBlock(doorProps(), DoorsBlock.getPixeledAABB(15,0, 0, 16,16,16), DoorsBlock.getPixeledAABB( 0,0,13, 16,16,16), SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_DOOR_CLOSE));
	public static final DeferredBlock<Block> DOOR_CREEP = register("creep_door", () -> new DoorsBlock(doorProps(), DoorsBlock.getPixeledAABB(15,0, 0, 16,16,16), DoorsBlock.getPixeledAABB( 0,0,13, 16,16,16), SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_DOOR_CLOSE));
	public static final DeferredBlock<Block> DOOR_LIRON = register("liron_door", () -> new DoorsBlock(doorProps(), DoorsBlock.getPixeledAABB(15,0, 0, 16,16,16), DoorsBlock.getPixeledAABB( 0,0,13, 16,16,16), SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_DOOR_CLOSE));
	public static final DeferredBlock<Block> DOOR_STEEL = register("steel_door", () -> new DoorsBlock(doorProps(), DoorsBlock.getPixeledAABB(15,0, 0, 16,16,16), DoorsBlock.getPixeledAABB( 0,0,13, 16,16,16), SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_DOOR_CLOSE));
	public static final DeferredBlock<Block> DOOR_STRIP = register("striped_door", () -> new DoorsBlock(doorProps(), DoorsBlock.getPixeledAABB(15,0, 0, 16,16,16), DoorsBlock.getPixeledAABB( 0,0,13, 16,16,16), SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_DOOR_CLOSE));
	public static final DeferredBlock<Block> METALDOOR = register("metaldoor", () -> new MetalDoorBlock(doorProps(), new AABB[] { MetalDoorBlock.getPixeledAABB(0,0,0, 16,16,16), MetalDoorBlock.getPixeledAABB(0,0,0, 16,16,16) } ));
	
	/*
	 * 
	 * LIQUID 
	 *
	 */
    public static final DeferredBlock<LiquidBlock> BLUELIMBO = registerNoItem("bluelimbo", () -> new BlueLimboBlock(WUTFluids.BLUELIMBO.get(), fluidProps().mapColor(DyeColor.BLUE)));
    public static final DeferredBlock<LiquidBlock> COLDSLUSH = registerNoItem("coldslush", () -> new ColdSlushBlock(WUTFluids.COLDSLUSH.get(), fluidProps().mapColor(DyeColor.LIGHT_BLUE)));
    public static final DeferredBlock<LiquidBlock> EXPERIENCE = registerNoItem("experience", () -> new ExperienceBlock(WUTFluids.EXPERIENCE.get(), fluidProps().mapColor(DyeColor.YELLOW)));
    public static final DeferredBlock<LiquidBlock> FERTILIZER = registerNoItem("fertilizer", () -> new FertilizerBlock(WUTFluids.FERTILIZER.get(), fluidProps().mapColor(DyeColor.WHITE)));
    public static final DeferredBlock<LiquidBlock> PORTIUM = registerNoItem("portium", () -> new PortiumBlock(WUTFluids.PORTIUM.get(), fluidProps().mapColor(DyeColor.GREEN)));
    public static final DeferredBlock<LiquidBlock> REDRESIN = registerNoItem("redresin", () -> new RedResinBlock(WUTFluids.REDRESIN.get(), fluidProps().mapColor(DyeColor.RED)));
    public static final DeferredBlock<LiquidBlock> SOULFUL = registerNoItem("soulful", () -> new SoulfulBlock(WUTFluids.SOULFUL.get(), fluidProps().mapColor(DyeColor.CYAN)));
    public static final DeferredBlock<LiquidBlock> WITHERWATER = registerNoItem("witherwater", () -> new WitherWaterBlock(WUTFluids.WITHERWATER.get(), fluidProps().mapColor(DyeColor.PURPLE)));

	/*
	 * 
	 * CUTTERBLOCKS 
	 *
	 */
	public static List<CutterBlock> CUTTERBLOCKS = new ArrayList<>();
    
	/*
	 * CONCRETE
	 */
	public static final DeferredBlock<Block> CTM_CONCRETE_A = register("concretea", () -> new CutterBlock(standardProps(), Blocks.GRAY_CONCRETE, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_CONCRETE_B = register("concreteb", () -> new CutterBlock(standardProps(), Blocks.GRAY_CONCRETE, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_CONCRETE_C = register("concretec", () -> new CutterBlock(standardProps(), Blocks.GRAY_CONCRETE, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_CONCRETE_D = register("concreted", () -> new CutterBlock(standardProps(), Blocks.GRAY_CONCRETE, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_CONCRETE_E = register("concretee", () -> new CutterBlock(standardProps(), Blocks.GRAY_CONCRETE, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_CONCRETE_F = register("concretef", () -> new CutterBlock(standardProps(), Blocks.GRAY_CONCRETE, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_CONCRETE_G = register("concreteg", () -> new CutterBlock(standardProps(), Blocks.GRAY_CONCRETE, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_CONCRETE_H = register("concreteh", () -> new CutterBlock(standardProps(), Blocks.GRAY_CONCRETE, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_CONCRETE_I = register("concretei", () -> new CutterBlock(standardProps(), Blocks.GRAY_CONCRETE, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_CONCRETE_J = register("concretej", () -> new CutterBlock(standardProps(), Blocks.GRAY_CONCRETE, false, false, CutterBlockType.CONNECTED, 0));
	
	/*
	 * METAL
	 */
	public static final DeferredBlock<Block> CTM_METAL_0 = register("metal0", () -> new WitherSimpleBlock(metalProps()));
	public static final DeferredBlock<Block> CTM_METAL_A = register("metala", () -> new CutterBlock(metalProps(), WUTBlocks.CTM_METAL_0.get(), false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_METAL_B = register("metalb", () -> new CutterBlock(metalProps(), WUTBlocks.CTM_METAL_0.get(), false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_METAL_C = register("metalc", () -> new CutterBlock(metalProps(), WUTBlocks.CTM_METAL_0.get(), false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_METAL_D = register("metald", () -> new CutterBlock(metalProps(), WUTBlocks.CTM_METAL_0.get(), false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_METAL_E = register("metale", () -> new CutterBlock(metalProps(), WUTBlocks.CTM_METAL_0.get(), false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_METAL_F = register("metalf", () -> new CutterBlock(metalProps(), WUTBlocks.CTM_METAL_0.get(), false, false, CutterBlockType.UNCONNECTED, 0));
	public static final DeferredBlock<Block> CTM_METAL_G = register("metalg", () -> new CutterBlock(metalProps(), WUTBlocks.CTM_METAL_0.get(), false, false, CutterBlockType.UNCONNECTED, 0));
	public static final DeferredBlock<Block> CTM_METAL_H = register("metalh", () -> new CutterBlock(metalProps(), WUTBlocks.CTM_METAL_0.get(), false, false, CutterBlockType.UNCONNECTED, 0));
	public static final DeferredBlock<Block> CTM_METAL_I = register("metali", () -> new CutterBlock(metalProps(), WUTBlocks.CTM_METAL_0.get(), false, false, CutterBlockType.UNCONNECTED, 0));
	public static final DeferredBlock<Block> CTM_METAL_J = register("metalj", () -> new CutterBlock(metalProps(), WUTBlocks.CTM_METAL_0.get(), false, false, CutterBlockType.UNCONNECTED, 0));
	public static final DeferredBlock<Block> CTM_METAL_K = register("metalk", () -> new CutterBlock(metalProps(), WUTBlocks.CTM_METAL_0.get(), true, false, CutterBlockType.UNCONNECTED, 0));
	public static final DeferredBlock<Block> CTM_METAL_L = register("metall", () -> new CutterBlock(metalProps(), WUTBlocks.CTM_METAL_0.get(), true, false, CutterBlockType.UNCONNECTED, 0));
	public static final DeferredBlock<Block> CTM_METAL_M = register("metalm", () -> new CutterBlock(metalProps(), WUTBlocks.CTM_METAL_0.get(), true, false, CutterBlockType.UNCONNECTED, 0));

	/*
	 * STONE
	 */
	public static final DeferredBlock<Block> CTM_STONE_A = register("stonea", () -> new CutterBlock(standardProps(), Blocks.STONE, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_STONE_B = register("stoneb", () -> new CutterBlock(standardProps(), Blocks.STONE, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_STONE_C = register("stonec", () -> new CutterBlock(standardProps(), Blocks.STONE, false, false, CutterBlockType.CONNECTED, 0));

	/*
	 * GLASS
	 */
	public static final DeferredBlock<Block> CTM_GLASS_A = register("glassa", () -> new CutterBlock(glassProps(), Blocks.GLASS, true, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_GLASS_B = register("glassb", () -> new CutterBlock(glassProps(), Blocks.GLASS, true, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_GLASS_C = register("glassc", () -> new CutterBlock(glassProps(), Blocks.GLASS, true, true, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_GLASS_D = register("glassd", () -> new CutterBlock(glassProps(), Blocks.GLASS, true, false, CutterBlockType.UNCONNECTED, 0));
	public static final DeferredBlock<Block> CTM_GLASS_E = register("glasse", () -> new CutterBlock(glassProps(), Blocks.GLASS, true, false, CutterBlockType.UNCONNECTED, 0));
	public static final DeferredBlock<Block> CTM_GLASS_F = register("glassf", () -> new CutterBlock(glassProps(), Blocks.GLASS, true, false, CutterBlockType.UNCONNECTED, 0));
	public static final DeferredBlock<Block> CTM_GLASS_G = register("glassg", () -> new CutterBlock(glassProps(), Blocks.GLASS, true, false, CutterBlockType.UNCONNECTED, 0));
	public static final DeferredBlock<Block> CTM_GLASS_H = register("glassh", () -> new CutterBlock(glassProps(), Blocks.GLASS, true, false, CutterBlockType.UNCONNECTED, 0));
	public static final DeferredBlock<Block> CTM_GLASS_I = register("glassi", () -> new CutterBlock(glassProps(), Blocks.GLASS, true, false, CutterBlockType.UNCONNECTED, 0));

	/*
	 * DIRT
	 */
	public static final DeferredBlock<Block> CTM_DIRT_A = register("dirta", () -> new CutterBlock(dirtProps(), Blocks.DIRT, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_DIRT_B = register("dirtb", () -> new CutterBlock(dirtProps(), Blocks.DIRT, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_DIRT_C = register("dirtc", () -> new CutterBlock(dirtProps(), Blocks.DIRT, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_DIRT_D = register("dirtd", () -> new CutterBlock(dirtProps(), Blocks.DIRT, false, false, CutterBlockType.CONNECTED, 0));
	
	/*
	 * 
	 * DECO
	 * 
	 */
	public static final DeferredBlock<Block> BRICKSDARK = register("bricks_dark", () -> new WitherSimpleBlock(standardProps()));
	public static final DeferredBlock<Block> BRICKSLAVA = register("bricks_lava", () -> new BricksLavaBlock(Block.Properties.of().mapColor(MapColor.NETHER).requiresCorrectToolForDrops().strength(3.0F, 2000.0F).sound(WitherSoundType.STONE)));
	public static final DeferredBlock<Block> STEELPOLEHEAD = register("steel_pole_head", () -> new SteelPoleBlock(metalProps(), SteelPoleBlock.getPixeledAABB(5,5,0, 11,11,16)));
	public static final DeferredBlock<Block> STEELPOLE = register("steel_pole", () -> new SteelPoleBlock(metalProps(), SteelPoleBlock.getPixeledAABB(5,5,0, 11,11,16)));
	public static final DeferredBlock<Block> STEELRAILING = register("steel_railing", () -> new RailingBlock(metalProps(), RailingBlock.getPixeledAABB(0,0,0,0,0,0), RailingBlock.getPixeledAABB(0,0,0,16,15.9,1)));
	public static final DeferredBlock<Block> SLICEDCONCRETEBLACK = register("slicedconcrete_black", () -> new SlicedConcreteBlock(standardProps()));
	public static final DeferredBlock<Block> SLICEDCONCRETEGRAY = register("slicedconcrete_gray", () -> new SlicedConcreteBlock(standardProps()));
	public static final DeferredBlock<Block> SLICEDCONCRETEWHITE = register("slicedconcrete_white", () -> new SlicedConcreteBlock(standardProps()));
	public static final DeferredBlock<Block> CATWALK = register("catwalk", () -> new CatwalkBlock(metalProps()));
	public static final DeferredBlock<Block> LIGHT = register("light", () -> new LightBlock(glassProps(), RailingBlock.getPixeledAABB(0,0,0,0,0,0), RailingBlock.getPixeledAABB(2,0,0.43,14,0.48,1.57)));
	public static final DeferredBlock<Block> FAN0 = register("fan0", () -> new FanBlock(metalProps()));
	public static final DeferredBlock<Block> FAN1 = register("fan1", () -> new FanBlock(metalProps()));
	public static final DeferredBlock<Block> FAN2 = register("fan2", () -> new FanBlock(metalProps()));
	public static final DeferredBlock<Block> FAN3 = register("fan3", () -> new FanBlock(metalProps()));
	public static final DeferredBlock<Block> SOULFIRE = register("soulfire", () -> new SoulFireBlock(soulFireProps()));
	
	/*
	 * 
	 * ROTTEN
	 * 
	 */
    public static final DeferredBlock<Block> ROTTEN_EARTH = register("rotten_earth", () -> new RottenEarth());
	public static final DeferredBlock<Block> ROTTEN_SAPLING = register("rotten_sapling", () -> new RottenSapling(ROTTEN, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));
	public static final DeferredBlock<Block> ROTTEN_LEAVES = register("rotten_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.of().strength(0.2f).randomTicks().noOcclusion().sound(SoundType.GRASS)));
    public static final DeferredBlock<Block> ROTTEN_LOG = register("rotten_log", () -> new RottenWood(BlockBehaviour.Properties.of().strength(2F, 2F).sound(SoundType.WOOD)));
    public static final DeferredBlock<Block> ROTTEN_SPIKE = register("rotten_spike", () -> new RottenSpike());
    public static final DeferredBlock<Block> ROTTEN_ROOTS = register("rotten_roots", () -> new RottenRoots());
    public static final DeferredBlock<Block> ROTTEN_ROOTS_POT = register("rotten_roots_pot", () -> new RottenRoots());
    
	/*
	 * 
	 * REGISTERING
	 *
	 */
    private static <T extends Block> DeferredBlock<T> register(String name, Supplier<? extends T> sup)
    {
        return register(name, sup, WUTBlocks::itemDefault);
    }
    private static <T extends Block> DeferredBlock<T> register(String name, Supplier<? extends T> sup, Function<DeferredBlock<T>, Supplier<? extends Item>> itemCreator)
    {
        DeferredBlock<T> ret = registerNoItem(name, sup);
        ITEMS.register(name, itemCreator.apply(ret));
        return ret;
    }
    private static <T extends Block> DeferredBlock<T> registerNoItem(String name, Supplier<? extends T> sup)
    {
        return BLOCK_TYPES.register(name, sup);
    }

	/*
	 * 
	 * BLOCKITEMS
	 *
	 */
    private static Supplier<BlockItem> itemDefault(final DeferredBlock<? extends Block> blockSupplier)
    {
        return item(blockSupplier);
    }
    private static Supplier<BlockItem> item(final DeferredBlock<? extends Block> blockSupplier)
    {
        return () -> new BlockItem(blockSupplier.get(), WUTItems.defaultProps());
    }
}
