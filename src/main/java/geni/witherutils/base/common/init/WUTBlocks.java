package geni.witherutils.base.common.init;

import java.util.ArrayList;
import java.util.List;

import geni.witherutils.api.lib.Names;
import geni.witherutils.base.common.base.WitherSimpleBlock;
import geni.witherutils.base.common.base.WitherSoundType;
import geni.witherutils.base.common.block.angel.AngelBlock;
import geni.witherutils.base.common.block.cutter.CutterBlock;
import geni.witherutils.base.common.block.cutter.CutterBlock.CutterBlockType;
import geni.witherutils.base.common.block.deco.door.DoorsBlock;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class WUTBlocks {

	public static List<CutterBlock> CUTTERBLOCKS = new ArrayList<>();

	public static final DeferredRegister.Blocks BLOCK_TYPES = DeferredRegister.createBlocks(Names.MODID);

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
            .sound(WitherSoundType.STANDARD)
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
    
    public static Block.Properties fluidProps =
    		BlockBehaviour.Properties
    		.of()
    		.liquid()
    		.noCollission()
    		.replaceable()
    		.strength(100.0F)
    		.pushReaction(PushReaction.DESTROY)
    		.noLootTable();
    
	/*
	 * 
	 * WITHERUTILSBLOCK
	 *
	 */
	public static final DeferredBlock<Block> ANGEL = BLOCK_TYPES.register("angel", () -> new AngelBlock(standardProps));
	
	/*
	 * 
	 * WITHERSIMPLEBLOCK 
	 *
	 */
    public static final DeferredBlock<Block> CASE = BLOCK_TYPES.register("case", () -> new WitherSimpleBlock(standardProps));
    
	/*
	 * CONCRETE
	 */
	public static final DeferredBlock<Block> CTM_CONCRETE_A = BLOCK_TYPES.register("concretea", () -> new CutterBlock(standardProps, Blocks.GRAY_CONCRETE, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_CONCRETE_B = BLOCK_TYPES.register("concreteb", () -> new CutterBlock(standardProps, Blocks.GRAY_CONCRETE, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_CONCRETE_C = BLOCK_TYPES.register("concretec", () -> new CutterBlock(standardProps, Blocks.GRAY_CONCRETE, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_CONCRETE_D = BLOCK_TYPES.register("concreted", () -> new CutterBlock(standardProps, Blocks.GRAY_CONCRETE, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_CONCRETE_E = BLOCK_TYPES.register("concretee", () -> new CutterBlock(standardProps, Blocks.GRAY_CONCRETE, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_CONCRETE_F = BLOCK_TYPES.register("concretef", () -> new CutterBlock(standardProps, Blocks.GRAY_CONCRETE, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_CONCRETE_G = BLOCK_TYPES.register("concreteg", () -> new CutterBlock(standardProps, Blocks.GRAY_CONCRETE, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_CONCRETE_H = BLOCK_TYPES.register("concreteh", () -> new CutterBlock(standardProps, Blocks.GRAY_CONCRETE, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_CONCRETE_I = BLOCK_TYPES.register("concretei", () -> new CutterBlock(standardProps, Blocks.GRAY_CONCRETE, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_CONCRETE_J = BLOCK_TYPES.register("concretej", () -> new CutterBlock(standardProps, Blocks.GRAY_CONCRETE, false, false, CutterBlockType.CONNECTED, 0));
	
	/*
	 * METAL
	 */
	public static final DeferredBlock<Block> CTM_METAL_0 = BLOCK_TYPES.register("metal0", () -> new WitherSimpleBlock(metalProps));
	public static final DeferredBlock<Block> CTM_METAL_A = BLOCK_TYPES.register("metala", () -> new CutterBlock(metalProps, WUTBlocks.CTM_METAL_0.get(), false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_METAL_B = BLOCK_TYPES.register("metalb", () -> new CutterBlock(metalProps, WUTBlocks.CTM_METAL_0.get(), false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_METAL_C = BLOCK_TYPES.register("metalc", () -> new CutterBlock(metalProps, WUTBlocks.CTM_METAL_0.get(), false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_METAL_D = BLOCK_TYPES.register("metald", () -> new CutterBlock(metalProps, WUTBlocks.CTM_METAL_0.get(), false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_METAL_E = BLOCK_TYPES.register("metale", () -> new CutterBlock(metalProps, WUTBlocks.CTM_METAL_0.get(), false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_METAL_F = BLOCK_TYPES.register("metalf", () -> new CutterBlock(metalProps, WUTBlocks.CTM_METAL_0.get(), false, false, CutterBlockType.UNCONNECTED, 0));
	public static final DeferredBlock<Block> CTM_METAL_G = BLOCK_TYPES.register("metalg", () -> new CutterBlock(metalProps, WUTBlocks.CTM_METAL_0.get(), false, false, CutterBlockType.UNCONNECTED, 0));
	public static final DeferredBlock<Block> CTM_METAL_H = BLOCK_TYPES.register("metalh", () -> new CutterBlock(metalProps, WUTBlocks.CTM_METAL_0.get(), false, false, CutterBlockType.UNCONNECTED, 0));
	public static final DeferredBlock<Block> CTM_METAL_I = BLOCK_TYPES.register("metali", () -> new CutterBlock(metalProps, WUTBlocks.CTM_METAL_0.get(), false, false, CutterBlockType.UNCONNECTED, 0));
	public static final DeferredBlock<Block> CTM_METAL_J = BLOCK_TYPES.register("metalj", () -> new CutterBlock(metalProps, WUTBlocks.CTM_METAL_0.get(), false, false, CutterBlockType.UNCONNECTED, 0));
	public static final DeferredBlock<Block> CTM_METAL_K = BLOCK_TYPES.register("metalk", () -> new CutterBlock(metalProps, WUTBlocks.CTM_METAL_0.get(), true, false, CutterBlockType.UNCONNECTED, 0));
	public static final DeferredBlock<Block> CTM_METAL_L = BLOCK_TYPES.register("metall", () -> new CutterBlock(metalProps, WUTBlocks.CTM_METAL_0.get(), true, false, CutterBlockType.UNCONNECTED, 0));
	public static final DeferredBlock<Block> CTM_METAL_M = BLOCK_TYPES.register("metalm", () -> new CutterBlock(metalProps, WUTBlocks.CTM_METAL_0.get(), true, false, CutterBlockType.UNCONNECTED, 0));

	/*
	 * STONE
	 */
	public static final DeferredBlock<Block> CTM_STONE_A = BLOCK_TYPES.register("stonea", () -> new CutterBlock(standardProps, Blocks.STONE, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_STONE_B = BLOCK_TYPES.register("stoneb", () -> new CutterBlock(standardProps, Blocks.STONE, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_STONE_C = BLOCK_TYPES.register("stonec", () -> new CutterBlock(standardProps, Blocks.STONE, false, false, CutterBlockType.CONNECTED, 0));

	/*
	 * GLASS
	 */
	public static final DeferredBlock<Block> CTM_GLASS_A = BLOCK_TYPES.register("glassa", () -> new CutterBlock(glassProps, Blocks.GLASS, true, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_GLASS_B = BLOCK_TYPES.register("glassb", () -> new CutterBlock(glassProps, Blocks.GLASS, true, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_GLASS_C = BLOCK_TYPES.register("glassc", () -> new CutterBlock(glassProps, Blocks.GLASS, true, true, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_GLASS_D = BLOCK_TYPES.register("glassd", () -> new CutterBlock(glassProps, Blocks.GLASS, true, false, CutterBlockType.UNCONNECTED, 0));
	public static final DeferredBlock<Block> CTM_GLASS_E = BLOCK_TYPES.register("glasse", () -> new CutterBlock(glassProps, Blocks.GLASS, true, false, CutterBlockType.UNCONNECTED, 0));
	public static final DeferredBlock<Block> CTM_GLASS_F = BLOCK_TYPES.register("glassf", () -> new CutterBlock(glassProps, Blocks.GLASS, true, false, CutterBlockType.UNCONNECTED, 0));
	public static final DeferredBlock<Block> CTM_GLASS_G = BLOCK_TYPES.register("glassg", () -> new CutterBlock(glassProps, Blocks.GLASS, true, false, CutterBlockType.UNCONNECTED, 0));
	public static final DeferredBlock<Block> CTM_GLASS_H = BLOCK_TYPES.register("glassh", () -> new CutterBlock(glassProps, Blocks.GLASS, true, false, CutterBlockType.UNCONNECTED, 0));
	public static final DeferredBlock<Block> CTM_GLASS_I = BLOCK_TYPES.register("glassi", () -> new CutterBlock(glassProps, Blocks.GLASS, true, false, CutterBlockType.UNCONNECTED, 0));

	/*
	 * DIRT
	 */
	public static final DeferredBlock<Block> CTM_DIRT_A = BLOCK_TYPES.register("dirta", () -> new CutterBlock(dirtProps, Blocks.DIRT, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_DIRT_B = BLOCK_TYPES.register("dirtb", () -> new CutterBlock(dirtProps, Blocks.DIRT, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_DIRT_C = BLOCK_TYPES.register("dirtc", () -> new CutterBlock(dirtProps, Blocks.DIRT, false, false, CutterBlockType.CONNECTED, 0));
	public static final DeferredBlock<Block> CTM_DIRT_D = BLOCK_TYPES.register("dirtd", () -> new CutterBlock(dirtProps, Blocks.DIRT, false, false, CutterBlockType.CONNECTED, 0));
	
	/*
	 * 
	 * DOORS
	 * 
	 */
	public static final DeferredBlock<Block> DOOR_CASED = BLOCK_TYPES.register("cased_door", () -> new DoorsBlock(doorProps, DoorsBlock.getPixeledAABB(15,0, 0, 16,16,16), DoorsBlock.getPixeledAABB( 0,0,13, 16,16,16), SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_DOOR_CLOSE));
	public static final DeferredBlock<Block> DOOR_CREEP = BLOCK_TYPES.register("creep_door", () -> new DoorsBlock(doorProps, DoorsBlock.getPixeledAABB(15,0, 0, 16,16,16), DoorsBlock.getPixeledAABB( 0,0,13, 16,16,16), SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_DOOR_CLOSE));
	public static final DeferredBlock<Block> DOOR_LIRON = BLOCK_TYPES.register("liron_door", () -> new DoorsBlock(doorProps, DoorsBlock.getPixeledAABB(15,0, 0, 16,16,16), DoorsBlock.getPixeledAABB( 0,0,13, 16,16,16), SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_DOOR_CLOSE));
	public static final DeferredBlock<Block> DOOR_STEEL = BLOCK_TYPES.register("steel_door", () -> new DoorsBlock(doorProps, DoorsBlock.getPixeledAABB(15,0, 0, 16,16,16), DoorsBlock.getPixeledAABB( 0,0,13, 16,16,16), SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_DOOR_CLOSE));
	public static final DeferredBlock<Block> DOOR_STRIP = BLOCK_TYPES.register("striped_door", () -> new DoorsBlock(doorProps, DoorsBlock.getPixeledAABB(15,0, 0, 16,16,16), DoorsBlock.getPixeledAABB( 0,0,13, 16,16,16), SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_DOOR_CLOSE));
}
