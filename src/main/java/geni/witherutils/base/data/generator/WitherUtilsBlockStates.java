package geni.witherutils.base.data.generator;

import java.util.Map;
import java.util.function.Function;

import geni.witherutils.api.WitherUtilsRegistry;
import geni.witherutils.api.lib.Names;
import geni.witherutils.base.common.block.deco.cutter.CutterBlock;
import geni.witherutils.base.common.block.deco.cutter.CutterBlock.CutterBlockType;
import geni.witherutils.base.common.block.deco.fan.FanBlock;
import geni.witherutils.base.common.block.fisher.FisherBlock;
import geni.witherutils.base.common.block.fisher.FisherBlockType;
import geni.witherutils.base.common.block.nature.RottenEarth;
import geni.witherutils.base.common.block.sensor.floor.FloorSensorBlock;
import geni.witherutils.base.common.block.smarttv.SmartTVBlock;
import geni.witherutils.base.common.init.WUTBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.IGeneratedBlockState;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class WitherUtilsBlockStates extends BlockStateProvider {

    @SuppressWarnings("unused")
	private final PackOutput output;
    
    public WitherUtilsBlockStates(PackOutput output, ExistingFileHelper fileHelper)
    {
        super(output, Names.MODID, fileHelper);
        this.output = output;
    }

    @Override
    protected void registerStatesAndModels()
    {
    	registerBlock(WUTBlocks.CTM_METAL_0.get());
    	registerBlock(WUTBlocks.WITHERSTEEL_BLOCK.get());
    	registerBlock(WUTBlocks.SOULISHED_BLOCK.get());

        simpleBlock(WUTBlocks.ANGEL.get(), existingBlock(WUTBlocks.ANGEL));
        simpleBlock(WUTBlocks.LINES.get(), existingBlock(WUTBlocks.LINES));
        simpleBlock(WUTBlocks.BRICKSDARK.get(), existingBlock(WUTBlocks.BRICKSDARK));
        
        plantBlockWithSubDirectory(WUTBlocks.LILLY);

        registerCases();
        registerCutter();
        registerFluids();
        registerAnvil();
        registerGenerators();
//        registerBattery();
        registerSmartTV();
        registerSensors();
        registerXp();
        registerDeco();
        registerSpawner();
//        registerFarmer();
        registerFisher();
        registerCauldron();
        registerTotem();
//        registerFloodgate();
//        registerRack();
        registerRotten();
        registerFakeDriver();
        registerFans();
        registerSoulFire();
    }

    private void registerBlock(Block block)
    {
		String name = name(block);
        simpleBlock(block, models().getBuilder("block/" + name)
        		.parent(models().getExistingFile(mcLoc("block/cube_all")))
        		.texture("all", modLoc("block/" + name)));
    }
    
    private void registerCutter()
    {
    	for (CutterBlock cutterBlock : WUTBlocks.CUTTERBLOCKS)
        {
    		String name = name(cutterBlock);
            simpleBlock(cutterBlock, models().getBuilder("block/ctm/" + name)
            		.parent(models().getExistingFile(mcLoc("block/cube_all")))
            		.texture("all", modLoc("block/ctm/" + name +
            		(cutterBlock.getType() == CutterBlockType.CONNECTED ? "/particle" : "/" + name))));
        }
    }
    
    private void registerFisher()
    {
    	ModelFile master = models().getExistingFile(modLoc("block/fisher/fisher_master"));
    	ModelFile single = models().getExistingFile(modLoc("block/fisher/fisher_single"));
    	ModelFile empty = models().getExistingFile(modLoc("block/fisher/fisher_null"));
    	
        getVariantBuilder(WUTBlocks.FISHER.get()).forAllStates(state -> {
            
        	FisherBlockType type = state.getValue(FisherBlock.BLOCK_TYPE);
            
            return ConfiguredModel.builder()
                    .modelFile(type == FisherBlockType.MASTER ? master : type == FisherBlockType.SINGLE ? single : empty)
                    .build();
        });
    }
    
    private void registerSpawner()
    {
        getVariantBuilder(WUTBlocks.SPAWNER.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
            		.modelFile(lit ? models().getExistingFile(modLoc("block/spawner/spawner_on")) : models().getExistingFile(modLoc("block/spawner/spawner")))
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
    }
    
    private void registerSensors()
    {
        getVariantBuilder(WUTBlocks.FLOORSENSOR.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            boolean covered = state.getValue(FloorSensorBlock.COVERED);
            
            return ConfiguredModel.builder()
                    .modelFile(lit || covered ? models().getExistingFile(modLoc("block/sensor/floor/floorsensor")) : models().getExistingFile(modLoc("block/sensor/floor/floorsensor")))
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
        
        getVariantBuilder(WUTBlocks.WALLSENSOR.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
                    .modelFile(lit ? models().getExistingFile(modLoc("block/sensor/wall/wallsensor_on")) : models().getExistingFile(modLoc("block/sensor/wall/wallsensor")))
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
    }
    
    private void registerFluids()
    {
        simpleBlock(WUTBlocks.BLUELIMBO.get(), models().getBuilder("block/fluid/bluelimbo")
        		.texture("particle", modLoc("block/fluid/bluelimbo_still"))
        		.texture("particle2", modLoc("block/fluid/bluelimbo_flow"))
        		.renderType("translucent"));
        simpleBlock(WUTBlocks.COLDSLUSH.get(), models().getBuilder("block/fluid/coldslush")
        		.texture("particle", modLoc("block/fluid/coldslush_still"))
        		.texture("particle2", modLoc("block/fluid/coldslush_flow"))
        		.renderType("translucent"));
        simpleBlock(WUTBlocks.EXPERIENCE.get(), models().getBuilder("block/fluid/experience")
        		.texture("particle", modLoc("block/fluid/experience_still"))
        		.texture("particle2", modLoc("block/fluid/experience_flow"))
        		.renderType("translucent"));
        simpleBlock(WUTBlocks.FERTILIZER.get(), models().getBuilder("block/fluid/fertilizer")
        		.texture("particle", modLoc("block/fluid/fertilizer_still"))
        		.texture("particle2", modLoc("block/fluid/fertilizer_flow"))
        		.renderType("translucent"));
        simpleBlock(WUTBlocks.PORTIUM.get(), models().getBuilder("block/fluid/portium")
        		.texture("particle", modLoc("block/fluid/portium_still"))
        		.texture("particle2", modLoc("block/fluid/portium_flow"))
        		.renderType("translucent"));
        simpleBlock(WUTBlocks.REDRESIN.get(), models().getBuilder("block/fluid/redresin")
        		.texture("particle", modLoc("block/fluid/redresin_still"))
        		.texture("particle2", modLoc("block/fluid/redresin_flow"))
        		.renderType("translucent"));
        simpleBlock(WUTBlocks.SOULFUL.get(), models().getBuilder("block/fluid/soulful")
        		.texture("particle", modLoc("block/fluid/soulful_still"))
        		.texture("particle2", modLoc("block/fluid/soulful_flow"))
        		.renderType("translucent"));
        simpleBlock(WUTBlocks.WITHERWATER.get(), models().getBuilder("block/fluid/witherwater")
        		.texture("particle", modLoc("block/fluid/witherwater_still"))
        		.texture("particle2", modLoc("block/fluid/witherwater_flow"))
        		.renderType("translucent"));
    }
    
    private void registerAnvil()
    {
        getVariantBuilder(WUTBlocks.ANVIL.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
            		.modelFile(lit ? models().getExistingFile(modLoc("block/anvil/anvil_on")) : 
            			models().getExistingFile(modLoc("block/anvil/anvil")))
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
    }
    
    private void registerSmartTV()
    {
    	ModelFile mounted = models().getExistingFile(modLoc("block/smarttv/smarttv_mounted"));
    	ModelFile unmounted = models().getExistingFile(modLoc("block/smarttv/smarttv_unmounted"));

        getVariantBuilder(WUTBlocks.SMARTTV.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(SmartTVBlock.FACING);
            boolean mount = state.getValue(SmartTVBlock.MOUNTED);
            
            return ConfiguredModel.builder()
                    .modelFile(mount ? mounted : unmounted)
                    .rotationY(dir == Direction.NORTH ? 180 : dir == Direction.EAST ? 270 : dir == Direction.WEST ? 90 : 0)
                    .build();
        });
    }
    
    private void registerCases()
    {
        getVariantBuilder(WUTBlocks.CASE_BIG.get()).forAllStates(state -> {
            return ConfiguredModel.builder()
            		.modelFile(models().getExistingFile(modLoc("block/case/case_big")))
                    .build();
        });
        getVariantBuilder(WUTBlocks.CASE_SMALL.get()).forAllStates(state -> {
            return ConfiguredModel.builder()
            		.modelFile(models().getExistingFile(modLoc("block/case/case_small")))
                    .build();
        });
    }
    
    private void registerGenerators()
    {
        getVariantBuilder(WUTBlocks.LAVA_GENERATOR.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
            		.modelFile(lit ? models().getExistingFile(modLoc("block/generator/lava/lava_generator_on")) : 
            			models().getExistingFile(modLoc("block/generator/lava/lava_generator")))
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
        getVariantBuilder(WUTBlocks.WIND_GENERATOR.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
            		.modelFile(lit ? models().getExistingFile(modLoc("block/generator/wind/wind_generator_on")) : 
            			models().getExistingFile(modLoc("block/generator/wind/wind_generator")))
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
        getVariantBuilder(WUTBlocks.WATER_GENERATOR.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
            		.modelFile(lit ? models().getExistingFile(modLoc("block/generator/water/water_generator_on")) : 
            			models().getExistingFile(modLoc("block/generator/water/water_generator")))
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
        getVariantBuilder(WUTBlocks.CREATIVE_GENERATOR.get()).forAllStates(state -> {
            
            boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
            		.modelFile(lit ? models().getExistingFile(modLoc("block/generator/creative/creative_generator_on")) : 
            			models().getExistingFile(modLoc("block/generator/creative/creative_generator")))
                    .build();
        });
    }
    
    private void registerFakeDriver()
    {
        getVariantBuilder(WUTBlocks.FAKE_DRIVER.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(BlockStateProperties.FACING);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
            		.modelFile(lit ? models().getExistingFile(modLoc("block/fakedriver/fake_driver_on")) : 
            			models().getExistingFile(modLoc("block/fakedriver/fake_driver")))
                    .rotationX(dir == Direction.DOWN ? 90 : dir == Direction.UP ? -90 : 0)
                    .rotationY(dir == Direction.SOUTH ? 180 : dir == Direction.EAST ? 90 : dir == Direction.WEST ? 270 : 0)
                    .build();
        });
    }
    
    private void registerXp()
    {
        getVariantBuilder(WUTBlocks.XPDRAIN.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(BlockStateProperties.FACING);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
            		.modelFile(lit ? models().getExistingFile(modLoc("block/xpdrain/xpdrain_on")) : 
            			models().getExistingFile(modLoc("block/xpdrain/xpdrain")))
                    .rotationX(dir == Direction.DOWN ? 90 : dir == Direction.UP ? -90 : 0)
                    .rotationY(dir == Direction.SOUTH ? 180 : dir == Direction.EAST ? 90 : dir == Direction.WEST ? 270 : 0)
                    .build();
        });
    }
    
    private void registerDeco()
    {
        getVariantBuilder(WUTBlocks.BRICKSLAVA.get()).forAllStates(state -> {
            
        	boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
                    .modelFile(lit ? existingBlock(WUTBlocks.BRICKSLAVA, "on") : existingBlock(WUTBlocks.BRICKSLAVA, "off"))
                    .build();
        });
    }
    
    private void registerCauldron()
    {
        getVariantBuilder(WUTBlocks.CAULDRON.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
            		.modelFile(lit ? models().getExistingFile(modLoc("block/cauldron/cauldron_on")) : models().getExistingFile(modLoc("block/cauldron/cauldron")))
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
    }
    
    private void registerTotem()
    {
        getVariantBuilder(WUTBlocks.TOTEM.get()).forAllStates(state -> {

            Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
            		.modelFile(lit ? models().getExistingFile(modLoc("block/totem/totem_on")) : 
            			models().getExistingFile(modLoc("block/totem/totem")))
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
    }

    private void registerSoulFire()
    {
    	getVariantBuilder(WUTBlocks.SOULFIRE.get()).forAllStates(state -> {
    		
    		Integer age = state.getValue(BlockStateProperties.AGE_15);
            return ConfiguredModel.builder()
            		.modelFile(models().getExistingFile(WitherUtilsRegistry.loc("block/soulfire/" + WUTBlocks.SOULFIRE.getId().getPath() + "_" + age.toString())))
                    .build();
    	});
    }
    
    public void directionalLitAllWithUpModel(DeferredBlock<Block> block)
    {
        getVariantBuilder(block.get()).forAllStates(state -> {
                    Direction dir = state.getValue(DirectionalBlock.FACING);
                    boolean lit = state.getValue(BlockStateProperties.LIT);
                    return ConfiguredModel.builder()
                            .modelFile(
                            		lit && dir.getAxis().isVertical() ? existingBlock(block, "up_on") : lit && dir.getAxis().isHorizontal() ? existingBlock(block, "on") :
                            		dir.getAxis().isVertical() ? existingBlock(block, "up") : existingBlock(block))
                            .rotationX(dir == Direction.DOWN ? 90 : dir == Direction.UP ? 270 : 0)
                            .rotationY(dir == Direction.SOUTH ? 180 : dir == Direction.EAST ? 90 : dir == Direction.WEST ? 270 : 0)
                            .build();
                });
    }
    
    public void plantBlock(DeferredBlock<Block> block)
    {
    	getVariantBuilder(block.get()).forAllStates(state -> {
    		
    		Integer age = state.getValue(BlockStateProperties.AGE_7);
            return ConfiguredModel.builder()
                    .modelFile(existingBlock(block, age.toString()))
                    .build();
    	});
    }
    public void plantBlockWithSubDirectory(DeferredBlock<Block> block)
    {
    	getVariantBuilder(block.get()).forAllStates(state -> {
    		
    		Integer age = state.getValue(BlockStateProperties.AGE_7);
            return ConfiguredModel.builder()
            		.modelFile(existingBlockSubDirectory(block, age.toString()))
                    .build();
    	});
    }
    
    private void registerRotten()
    {
        getVariantBuilder(WUTBlocks.ROTTEN_LOG.get())
        .partialState().with(RotatedPillarBlock.AXIS, Axis.Y).modelForState().modelFile(models().getExistingFile(modLoc("block/rotten/rotten_log"))).addModel()
        .partialState().with(RotatedPillarBlock.AXIS, Axis.Z).modelForState().modelFile(models().getExistingFile(modLoc("block/rotten/rotten_log_horizontal"))).rotationX(90).addModel()
        .partialState().with(RotatedPillarBlock.AXIS, Axis.X).modelForState().modelFile(models().getExistingFile(modLoc("block/rotten/rotten_log_horizontal"))).rotationX(90).rotationY(90).addModel();
        
    	simpleBlock(WUTBlocks.ROTTEN_LEAVES.get(), models().getExistingFile(modLoc("block/rotten/rotten_leaves")));
    	simpleBlock(WUTBlocks.ROTTEN_SAPLING.get(), models().getExistingFile(modLoc("block/rotten/rotten_sapling")));
    	
    	simpleBlock(WUTBlocks.ROTTEN_ROOTS.get(), models().getExistingFile(modLoc("block/rotten/rotten_roots")));
    	simpleBlock(WUTBlocks.ROTTEN_ROOTS_POT.get(), models().getExistingFile(modLoc("block/rotten/rotten_roots_pot")));
    	
        getVariantBuilder(WUTBlocks.ROTTEN_EARTH.get()).forAllStates(state -> {
            
        	int decay = state.getValue(RottenEarth.DECAY);
        	
            return ConfiguredModel.builder()
            		.modelFile(decay < 9 ? models().getExistingFile(modLoc("block/rotten/rotten_earth")) : null)
                    .build();
        });
    }

	private void registerFans()
    {
		for (var entry : BuiltInRegistries.BLOCK.entrySet())
		{
			var block = entry.getValue();
    		if(block instanceof FanBlock)
    		{
        		final String blockNameId = BuiltInRegistries.BLOCK.getKey(block).getPath();
    	        getVariantBuilder(block).forAllStates(state -> {
    	            
    	            Direction dir = state.getValue(BlockStateProperties.FACING);
    	            
    	            return ConfiguredModel.builder()
    	                    .modelFile(blockNameId.contains("0") ? models().getExistingFile(modLoc("block/fan/fan0")) :
        	                    		blockNameId.contains("1") ? models().getExistingFile(modLoc("block/fan/fan1")) :
        	                    		blockNameId.contains("2") ? models().getExistingFile(modLoc("block/fan/fan2")) :
        	                    		blockNameId.contains("3") ? models().getExistingFile(modLoc("block/fan/fan3")) : null)
    	                    .rotationX(dir == Direction.DOWN ? 90 : dir == Direction.UP ? -90 : 0)
    	                    .rotationY(dir == Direction.SOUTH ? 180 : dir == Direction.EAST ? 90 : dir == Direction.WEST ? 270 : 0)
    	                    .build();
    	        });
    		}
		}
    }
    
    /*
     * 
     * SORTOF
     * 
     */
    private ResourceLocation key(Block block)
    {
        return BuiltInRegistries.BLOCK.getKey(block);
    }
    
    @SuppressWarnings("unused")
	private ResourceLocation key(DeferredBlock<Block> block)
    {
        return BuiltInRegistries.BLOCK.getKey(block.get());
    }

    @SuppressWarnings("unused")
	private BlockModelBuilder block(DeferredBlock<Block> block) { return block(block, ""); }

    private BlockModelBuilder block(DeferredBlock<Block> block, String suffix)
    {
        String name = block.getId().getPath();
        String path = "block/" + name;
        if (!suffix.isBlank())
        {
            path += "_" + suffix;
        }
        return models().getBuilder(path);
    }
    
    private ModelFile existingBlock(DeferredBlock<Block> block) { return existingBlock(block, ""); }

    private ModelFile existingBlock(DeferredBlock<Block> block, String suffix)
    {
        ResourceLocation name = block.getId();
        String path = "block/" + name.getPath();
        if (!suffix.isBlank())
        {
            path += "_" + suffix;
        }
        return models().getExistingFile(ResourceLocation.fromNamespaceAndPath(name.getNamespace(), path));
    }
    
	private ModelFile existingBlockSubDirectory(DeferredBlock<Block> block, String suffix)
    {
        ResourceLocation name = block.getId();
        String path = "block/" + name.getPath() + "/" + name.getPath();
        if (!suffix.isBlank())
        {
            path += "_" + suffix;
        }
        return models().getExistingFile(ResourceLocation.fromNamespaceAndPath(name.getNamespace(), path));
    }
    
    @SuppressWarnings("unused")
	private ItemModelBuilder simpleBlockWithItem(DeferredBlock<Block> block, ModelFile model, String itemRenderType)
    {
        return simpleBlockWithItem(block, model).renderType(itemRenderType);
    }

    private ItemModelBuilder simpleBlockWithItem(DeferredBlock<Block> block, ModelFile model)
    {
        simpleBlock(block.get(), model);
        return simpleBlockItem(block, model);
    }

    @SuppressWarnings("unused")
	private ItemModelBuilder simpleBlockItem(DeferredBlock<Block> block, ModelFile model, String renderType)
    {
        return simpleBlockItem(block, model).renderType(renderType);
    }

    private ItemModelBuilder simpleBlockItem(DeferredBlock<Block> block, ModelFile model)
    {
        return itemModels().getBuilder(block.getId().getPath()).parent(model);
    }

    @SuppressWarnings("unused")
	private ItemModelBuilder simpleItem(DeferredBlock<Block> block, String renderType)
    {
        return simpleItem(block.getId().getPath(), renderType);
    }

    @SuppressWarnings("unused")
	private ItemModelBuilder simpleItem(DeferredBlock<Block> block, String texture, String renderType)
    {
        return simpleItem(block.getId().getPath(), texture, renderType);
    }

    private ItemModelBuilder simpleItem(String name, String renderType)
    {
        return simpleItem(name, "item/" + name, renderType);
    }

    private ItemModelBuilder simpleItem(String name, String texture, String renderType)
    {
        return itemModels().singleTexture(name, mcLoc("item/generated"), "layer0", modLoc(texture)).renderType(renderType);
    }

    @SuppressWarnings("unused")
	private void dummyBlock(Block block)
    {
        ModelFile model = models()
                .withExistingParent("dummy", "block")
                .texture("particle", "minecraft:block/glass");
        simpleBlock(block, model);
    }

    public void directionalFromNorth(Block block, ModelFile model)
    {
        directionalFromNorth(block, model, 180);
    }

    public void directionalFromNorth(Block block, ModelFile model, int angleOffset)
    {
        directionalFromNorth(block, $ -> model, angleOffset);
    }

    public void directionalFromNorth(Block block, Function<BlockState, ModelFile> modelFunc)
    {
        directionalFromNorth(block, modelFunc, 180);
    }

    public void directionalFromNorth(Block block, Function<BlockState, ModelFile> modelFunc, int angleOffset)
    {
        getVariantBuilder(block)
                .forAllStates(state -> {
                    Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                    return ConfiguredModel.builder()
                            .modelFile(modelFunc.apply(state))
//                            .rotationX(dir == DOWN ? 90 : dir == UP ? -90 : 0)
                            .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + angleOffset) % 360)
                            .build();
                });
    }

    private String name(Block block)
    {
        return key(block).getPath();
    }
    
    public ResourceLocation customTexture(Block block, String folder)
    {
        ResourceLocation name = key(block);
        return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + folder + "/" + name.getPath());
    }
    
    public Map<Block, IGeneratedBlockState> getGeneratedBlockStates()
    {
        return registeredBlocks;
    }
    
    @Override
    public String getName()
    {
        return "WitherUtils Blockstates";
    }
}