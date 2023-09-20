package geni.witherutils.base.data.generator;

import java.util.function.Function;

import geni.witherutils.WitherUtils;
import geni.witherutils.api.block.BStateProperties;
import geni.witherutils.api.block.MultiBlockState;
import geni.witherutils.base.common.block.battery.pylon.PylonBlock;
import geni.witherutils.base.common.block.battery.stab.StabBlock;
import geni.witherutils.base.common.block.fisher.FisherBlock;
import geni.witherutils.base.common.block.fisher.FisherBlockType;
import geni.witherutils.base.common.block.miner.advanced.MinerAdvancedBlock;
import geni.witherutils.base.common.block.nature.WitherEarthBlock;
import geni.witherutils.base.common.block.sensor.floor.FloorSensorBlock;
import geni.witherutils.base.common.block.smarttv.SmartTVBlock;
import geni.witherutils.base.common.block.tank.reservoir.TankReservoirBlock;
import geni.witherutils.base.common.init.WUTBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class WitherUtilsBlockStates extends BlockStateProvider {
    
    public WitherUtilsBlockStates(PackOutput gen, ExistingFileHelper fileHelper)
    {
        super(gen, WitherUtils.MODID, fileHelper);
    }

    @Override
    protected void registerStatesAndModels()
    {
        simpleBlock(WUTBlocks.WITHERSTEEL_BLOCK.get(), existingBlock(WUTBlocks.WITHERSTEEL_BLOCK));
        simpleBlock(WUTBlocks.SOULISHED_BLOCK.get(), existingBlock(WUTBlocks.SOULISHED_BLOCK));
        simpleBlock(WUTBlocks.CASE_BIG.get(), existingBlock(WUTBlocks.CASE_BIG));
        simpleBlock(WUTBlocks.CASE_SMALL.get(), existingBlock(WUTBlocks.CASE_SMALL));
        simpleBlock(WUTBlocks.CREATIVE_TRASH.get(), existingBlock(WUTBlocks.CREATIVE_TRASH));
        simpleBlock(WUTBlocks.ANGEL.get(), existingBlock(WUTBlocks.ANGEL));
        simpleBlock(WUTBlocks.LINES.get(), existingBlock(WUTBlocks.LINES));
        simpleBlock(WUTBlocks.BRICKSDARK.get(), existingBlock(WUTBlocks.BRICKSDARK));

        plantBlockWithSubDirectory(WUTBlocks.LILLY);
        
        registerAlloyFurnace();
        registerElectroFurnace();
        registerAnvil();
        registerGenerators();
        registerBattery();
        registerMiner();
        registerSmartTV();
        registerSensors();
        registerXp();
        registerDeco();
        registerActivator();
        registerClicker();
        registerPlacer();
        registerScanner();
        registerSpawner();
        registerFarmer();
        registerFisher();
        registerCauldron();
        registerWitherEarth();
        registerTotem();
        registerFloodgate();
        registerRack();
        registerRotten();
    }

    private void registerAlloyFurnace()
    {
        getVariantBuilder(WUTBlocks.ALLOY_FURNACE.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
                    .modelFile(lit ? existingBlock(WUTBlocks.ALLOY_FURNACE, "on") : existingBlock(WUTBlocks.ALLOY_FURNACE))
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
    }
    
    private void registerElectroFurnace()
    {
        getVariantBuilder(WUTBlocks.ELECTRO_FURNACE.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
                    .modelFile(lit ? existingBlock(WUTBlocks.ELECTRO_FURNACE, "on") : existingBlock(WUTBlocks.ELECTRO_FURNACE))
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
    }
    
    private void registerAnvil()
    {
        getVariantBuilder(WUTBlocks.ANVIL.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
                    .modelFile(lit ? existingBlock(WUTBlocks.ANVIL, "on") : existingBlock(WUTBlocks.ANVIL))
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
    }

    private void registerGenerators()
    {
        getVariantBuilder(WUTBlocks.LAVA_GENERATOR.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
                    .modelFile(lit ? existingBlock(WUTBlocks.LAVA_GENERATOR, "on") : existingBlock(WUTBlocks.LAVA_GENERATOR))
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
        getVariantBuilder(WUTBlocks.WIND_GENERATOR.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
                    .modelFile(lit ? existingBlock(WUTBlocks.WIND_GENERATOR, "on") : existingBlock(WUTBlocks.WIND_GENERATOR))
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
        getVariantBuilder(WUTBlocks.WATER_GENERATOR.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
                    .modelFile(lit ? existingBlock(WUTBlocks.WATER_GENERATOR, "on") : existingBlock(WUTBlocks.WATER_GENERATOR))
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
        getVariantBuilder(WUTBlocks.CREATIVE_GENERATOR.get()).forAllStates(state -> {
            
            boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
                    .modelFile(lit ? existingBlock(WUTBlocks.CREATIVE_GENERATOR) : existingBlock(WUTBlocks.CREATIVE_GENERATOR))
                    .build();
        });
    }
    
    private void registerBattery()
    {
        getVariantBuilder(WUTBlocks.CORE.get()).forAllStates(state -> {
            
            return ConfiguredModel.builder()
                    .modelFile(existingBlock(WUTBlocks.CORE))
                    .build();
        });
        
        getVariantBuilder(WUTBlocks.PYLON.get()).forAllStates(state -> {
            
            int xRot = 0;
            int yRot = 0;

            Direction dir = state.getValue(PylonBlock.FACING);
            geni.witherutils.base.common.block.battery.pylon.PylonBlock.Mode mode = state.getValue(PylonBlock.MODE);
            
            if(dir == Direction.UP)
            {
            	xRot = 270;
            }
            else if(dir == Direction.DOWN)
            {
            	xRot = 90;
            }
            else if(dir == Direction.NORTH)
            {
            	yRot = 0;
            }
            else if(dir == Direction.SOUTH)
            {
            	yRot = 180;
            }
            else if(dir == Direction.EAST)
            {
            	yRot = 90;
            }
            else if(dir == Direction.WEST)
            {
            	yRot = 270;
            }

            return ConfiguredModel.builder()
                    .modelFile(mode == geni.witherutils.base.common.block.battery.pylon.PylonBlock.Mode.OUTPUT ? existingBlock(WUTBlocks.PYLON) : existingBlock(WUTBlocks.PYLON))
                    .rotationX(xRot)
                    .rotationY(yRot)
                    .build();
        });
        
        getVariantBuilder(WUTBlocks.STAB.get()).forAllStates(state -> {
            
            int xRot = 0;
            int yRot = 0;

            Direction dir = state.getValue(StabBlock.FACING);
            boolean lit = state.getValue(StabBlock.LIT);
            
            if(dir == Direction.UP)
            {
            	xRot = 270;
            }
            else if(dir == Direction.DOWN)
            {
            	xRot = 90;
            }
            else if(dir == Direction.NORTH)
            {
            	yRot = 0;
            }
            else if(dir == Direction.SOUTH)
            {
            	yRot = 180;
            }
            else if(dir == Direction.EAST)
            {
            	yRot = 90;
            }
            else if(dir == Direction.WEST)
            {
            	yRot = 270;
            }

            return ConfiguredModel.builder()
                    .modelFile(lit ? existingBlock(WUTBlocks.STAB, "on") : existingBlock(WUTBlocks.STAB))
                    .rotationX(xRot)
                    .rotationY(yRot)
                    .build();
        });
    }
    
    private void registerMiner()
    {
        getVariantBuilder(WUTBlocks.MINERBASIC.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(MinerAdvancedBlock.FACING);
            boolean lit = state.getValue(MinerAdvancedBlock.LIT);
            
            return ConfiguredModel.builder()
                    .modelFile(lit ? existingBlock(WUTBlocks.MINERBASIC, "on") : existingBlock(WUTBlocks.MINERBASIC))
                    .rotationX(dir == Direction.DOWN ? 90 : dir == Direction.UP ? -90 : 0)
                    .rotationY(dir == Direction.SOUTH ? 180 : dir == Direction.EAST ? 90 : dir == Direction.WEST ? 270 : 0)
                    .build();
        });
        
        getVariantBuilder(WUTBlocks.MINERADV.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(MinerAdvancedBlock.FACING);
            boolean lit = state.getValue(MinerAdvancedBlock.LIT);
            
            return ConfiguredModel.builder()
                    .modelFile(lit ? existingBlock(WUTBlocks.MINERADV, "on") : existingBlock(WUTBlocks.MINERADV))
                    .rotationX(dir == Direction.DOWN ? 90 : dir == Direction.UP ? -90 : 0)
                    .rotationY(dir == Direction.SOUTH ? 180 : dir == Direction.EAST ? 90 : dir == Direction.WEST ? 270 : 0)
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
    
    private void registerTanks()
    {
        getVariantBuilder(WUTBlocks.RESERVOIR.get()).forAllStates(state -> {
        	
            ModelFile resmodel = null;
            
            boolean north = state.getValue(TankReservoirBlock.NORTH);
            boolean south = state.getValue(TankReservoirBlock.SOUTH);
            boolean east = state.getValue(TankReservoirBlock.EAST);
            boolean west = state.getValue(TankReservoirBlock.WEST);
            
            boolean northwest = state.getValue(TankReservoirBlock.CORNER_NORTH_WEST);
            boolean northeast = state.getValue(TankReservoirBlock.CORNER_NORTH_EAST);
            boolean southeast = state.getValue(TankReservoirBlock.CORNER_SOUTH_EAST);
            boolean southwest = state.getValue(TankReservoirBlock.CORNER_SOUTH_WEST);
            
            int yrotate = 0;
            
            if(!east && !north && !south && !west)
            	resmodel = models().getExistingFile(modLoc("block/tank/reservoir/reservoir_" + "bordered"));
            
            if(!east && north && !south && !west) {
            	yrotate = 0;
            	resmodel = models().getExistingFile(modLoc("block/tank/reservoir/reservoir_" + "north"));
            	}
            if(east && !north && !south && !west) {
            	yrotate = 90;
            	resmodel = models().getExistingFile(modLoc("block/tank/reservoir/reservoir_" + "north"));
            	}
            if(!east && !north && south && !west) {
            	yrotate = 180;
            	resmodel = models().getExistingFile(modLoc("block/tank/reservoir/reservoir_" + "north"));
            	}
            if(!east && !north && !south && west) {
            	yrotate = 270;
            	resmodel = models().getExistingFile(modLoc("block/tank/reservoir/reservoir_" + "north"));
            	}
            
            if(east && north && !south && !west) {
            	yrotate = 0;
            	resmodel = models().getExistingFile(modLoc("block/tank/reservoir/reservoir_" + "north_east"));
            	}
            if(east && !north && south && !west) {
            	yrotate = 90;
            	resmodel = models().getExistingFile(modLoc("block/tank/reservoir/reservoir_" + "north_east"));
            	}
            if(!east && !north && south && west) {
            	yrotate = 180;
            	resmodel = models().getExistingFile(modLoc("block/tank/reservoir/reservoir_" + "north_east"));
            	}
            if(!east && north && !south && west) {
            	yrotate = 270;
            	resmodel = models().getExistingFile(modLoc("block/tank/reservoir/reservoir_" + "north_east"));
            	}
            
            if(!east && north && south && !west) {
            	yrotate = 0;
            	resmodel = models().getExistingFile(modLoc("block/tank/reservoir/reservoir_" + "north_south"));
            	}
            if(east && !north && !south && west) {
            	yrotate = 90;
            	resmodel = models().getExistingFile(modLoc("block/tank/reservoir/reservoir_" + "north_south"));
            	}
            
            if(east && north && south && !west) {
            	yrotate = 0;
            	resmodel = models().getExistingFile(modLoc("block/tank/reservoir/reservoir_" + "north_east_south"));
            	}
            if(east && !north && south && west) {
            	yrotate = 90;
            	resmodel = models().getExistingFile(modLoc("block/tank/reservoir/reservoir_" + "north_east_south"));
            	}
            if(!east && north && south && west) {
            	yrotate = 180;
            	resmodel = models().getExistingFile(modLoc("block/tank/reservoir/reservoir_" + "north_east_south"));
            	}
            if(east && north && !south && west) {
            	yrotate = 270;
            	resmodel = models().getExistingFile(modLoc("block/tank/reservoir/reservoir_" + "north_east_south"));
            	}
            
            if(east && north && south && west)
            	resmodel = models().getExistingFile(modLoc("block/tank/reservoir/reservoir_" + "north_east_south_west"));
            
            if(northwest) {
            	yrotate = 0;
            	resmodel = models().getExistingFile(modLoc("block/tank/reservoir/reservoir_" + "reservoir_north_corner"));
            	}
            if(northeast) {
            	yrotate = 90;
            	resmodel = models().getExistingFile(modLoc("block/tank/reservoir/reservoir_" + "reservoir_north_corner"));
            	}
            if(southeast) {
            	yrotate = 180;
            	resmodel = models().getExistingFile(modLoc("block/tank/reservoir/reservoir_" + "reservoir_north_corner"));
            	}
            if(southwest) {
            	yrotate = 270;
            	resmodel = models().getExistingFile(modLoc("block/tank/reservoir/reservoir_" + "reservoir_north_corner"));
            	}
            				
            return ConfiguredModel.builder()
                    .modelFile(resmodel)
                    .rotationY(yrotate)
                    .build();
        });
    }

    private void registerXp()
    {
//        getVariantBuilder(WUTBlocks.XPWIRELESS.get()).forAllStates(state -> {
//            
//            int xRot = 0;
//            int yRot = 0;
//
//            Direction dir = state.getValue(XpWirelessBlock.FACING);
//            geni.witherutils.base.common.block.xp.wireless.XpWirelessBlock.Mode mode = state.getValue(XpWirelessBlock.MODE);
//            
//            if(dir == Direction.UP)
//            {
//            	xRot = 270;
//            }
//            else if(dir == Direction.DOWN)
//            {
//            	xRot = 90;
//            }
//            else if(dir == Direction.NORTH)
//            {
//            	yRot = 0;
//            }
//            else if(dir == Direction.SOUTH)
//            {
//            	yRot = 180;
//            }
//            else if(dir == Direction.EAST)
//            {
//            	yRot = 90;
//            }
//            else if(dir == Direction.WEST)
//            {
//            	yRot = 270;
//            }
//
//            return ConfiguredModel.builder()
//                    .modelFile(mode == geni.witherutils.base.common.block.xp.wireless.XpWirelessBlock.Mode.DRAIN ? existingBlock(WUTBlocks.XPWIRELESS) : existingBlock(WUTBlocks.XPWIRELESS))
//                    .rotationX(xRot)
//                    .rotationY(yRot)
//                    .build();
//        });
//        
//        getVariantBuilder(WUTBlocks.XPPLATE.get()).forAllStates(state -> {
//            
//            Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
//            
//            return ConfiguredModel.builder()
//                    .modelFile(existingBlock(WUTBlocks.XPPLATE))
//                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
//                    .build();
//        });
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
    
    private void registerActivator()
    {
        getVariantBuilder(WUTBlocks.ACTIVATOR.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(MinerAdvancedBlock.FACING);
            boolean lit = state.getValue(MinerAdvancedBlock.LIT);
            
            return ConfiguredModel.builder()
                    .modelFile(lit ? models().getExistingFile(modLoc("block/activator/activator_on")) : models().getExistingFile(modLoc("block/activator/activator")))
                    .rotationX(dir == Direction.DOWN ? 90 : dir == Direction.UP ? -90 : 0)
                    .rotationY(dir == Direction.SOUTH ? 180 : dir == Direction.EAST ? 90 : dir == Direction.WEST ? 270 : 0)
                    .build();
        });
    }
    
    private void registerClicker()
    {
        getVariantBuilder(WUTBlocks.CLICKER.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(MinerAdvancedBlock.FACING);
            boolean lit = state.getValue(MinerAdvancedBlock.LIT);
            
            return ConfiguredModel.builder()
            		.modelFile(lit ? models().getExistingFile(modLoc("block/clicker/clicker_on")) : models().getExistingFile(modLoc("block/clicker/clicker")))
                    .rotationX(dir == Direction.DOWN ? 90 : dir == Direction.UP ? -90 : 0)
                    .rotationY(dir == Direction.SOUTH ? 180 : dir == Direction.EAST ? 90 : dir == Direction.WEST ? 270 : 0)
                    .build();
        });
    }
    
    private void registerPlacer()
    {
        getVariantBuilder(WUTBlocks.PLACER.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(MinerAdvancedBlock.FACING);
            boolean lit = state.getValue(MinerAdvancedBlock.LIT);
            
            return ConfiguredModel.builder()
            		.modelFile(lit ? models().getExistingFile(modLoc("block/placer/placer_on")) : models().getExistingFile(modLoc("block/placer/placer")))
                    .rotationX(dir == Direction.DOWN ? 90 : dir == Direction.UP ? -90 : 0)
                    .rotationY(dir == Direction.SOUTH ? 180 : dir == Direction.EAST ? 90 : dir == Direction.WEST ? 270 : 0)
                    .build();
        });
    }
    
    private void registerScanner()
    {
        getVariantBuilder(WUTBlocks.SCANNER.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(MinerAdvancedBlock.FACING);
            boolean lit = state.getValue(MinerAdvancedBlock.LIT);
            
            return ConfiguredModel.builder()
            		.modelFile(lit ? models().getExistingFile(modLoc("block/scanner/scanner_on")) : models().getExistingFile(modLoc("block/scanner/scanner")))
                    .rotationX(dir == Direction.DOWN ? 90 : dir == Direction.UP ? -90 : 0)
                    .rotationY(dir == Direction.SOUTH ? 180 : dir == Direction.EAST ? 90 : dir == Direction.WEST ? 270 : 0)
                    .build();
        });
    }
    
    private void registerFarmer()
    {
        getVariantBuilder(WUTBlocks.FARMER.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
            		.modelFile(lit ? models().getExistingFile(modLoc("block/farmer/farmer")) : models().getExistingFile(modLoc("block/farmer/farmer")))
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
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
    
    private void registerWitherEarth()
    {
        getVariantBuilder(WUTBlocks.WITHEREARTH.get()).forAllStates(state -> {
            
        	int decay = state.getValue(WitherEarthBlock.DECAY);
        	
            return ConfiguredModel.builder()
            		.modelFile(decay < 9 ? models().getExistingFile(modLoc("block/witherearth")) : null)
                    .build();
        });
    }
    
    private void registerTotem()
    {
        getVariantBuilder(WUTBlocks.TOTEM.get()).forAllStates(state -> {

            Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
                    .modelFile(lit ? existingBlock(WUTBlocks.TOTEM) : existingBlock(WUTBlocks.TOTEM))
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
    }
    
    private void registerFloodgate()
    {
        getVariantBuilder(WUTBlocks.FLOODGATE.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
                    .modelFile(lit ? existingBlock(WUTBlocks.FLOODGATE) : existingBlock(WUTBlocks.FLOODGATE))
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
    }
    
    private void registerRack()
    {
        getVariantBuilder(WUTBlocks.RACKITEM_CONTROLLER.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
                    .modelFile(lit ? models().getExistingFile(modLoc("block/rack/controller/controller_on")) : models().getExistingFile(modLoc("block/rack/controller/controller")))
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
        
        getVariantBuilder(WUTBlocks.RACKFLUID_CONTROLLER.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            
            return ConfiguredModel.builder()
                    .modelFile(lit ? models().getExistingFile(modLoc("block/rack/controller/controller_on")) : models().getExistingFile(modLoc("block/rack/controller/controller")))
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
        });
        
        getVariantBuilder(WUTBlocks.RACK_TERMINAL.get()).forAllStates(state -> {
            
            Direction dir = state.getValue(BlockStateProperties.FACING);
            boolean formed = state.getValue(BStateProperties.FORMED);
            
            return ConfiguredModel.builder()
                    .modelFile(formed ? models().getExistingFile(modLoc("block/rack/terminal/terminal_formed")) : models().getExistingFile(modLoc("block/rack/terminal/terminal")))
                    .rotationX(dir == Direction.DOWN ? 270 : dir == Direction.UP ? 90 : 0)
                    .rotationY(dir == Direction.SOUTH ? 180 : dir == Direction.EAST ? 90 : dir == Direction.WEST ? 270 : 0)
                    .build();
        });
        
        getVariantBuilder(WUTBlocks.RACK_CASE.get()).forAllStates(state -> {
            
            MultiBlockState mbstate = state.getValue(BStateProperties.MBSTATE);
            
            return ConfiguredModel.builder()
                    .modelFile(getMBStateModel(mbstate))
                    .rotationY(getMBStateYRotation(mbstate))
                    .build();
        });
    }
    public ModelFile getMBStateModel(MultiBlockState mbstate)
    {
    	if(mbstate == MultiBlockState.NONE)
    	{
    		return models().getExistingFile(modLoc("block/rack/casing/case_none"));
    	}
    	else if(mbstate == MultiBlockState.XMAX_ZMAX_YEDGE ||
    	   mbstate == MultiBlockState.XMAX_ZMIN_YEDGE ||
    	   mbstate == MultiBlockState.XMIN_ZMAX_YEDGE ||
    	   mbstate == MultiBlockState.XMIN_ZMIN_YEDGE)
    	{
    		return models().getExistingFile(modLoc("block/rack/casing/case_corner"));
    	}
    	else
    	{
    		return models().getExistingFile(modLoc("block/rack/casing/case_null"));
    	}
    }
    public int getMBStateYRotation(MultiBlockState mbstate)
    {
    	if(mbstate == MultiBlockState.XMIN_ZMAX_YEDGE)
    	{
    		return -90;
    	}
    	else if(mbstate == MultiBlockState.XMAX_ZMIN_YEDGE)
    	{
    		return 90;
    	}
    	else if(mbstate == MultiBlockState.XMAX_ZMAX_YEDGE)
    	{
    		return 180;
    	}
    	else
    	{
        	return 0;
    	}
    }
    
    public void directionalLitAllWithUpModel(RegistryObject<Block> block)
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
    
    public void plantBlock(RegistryObject<Block> block)
    {
    	getVariantBuilder(block.get()).forAllStates(state -> {
    		
    		Integer age = state.getValue(BlockStateProperties.AGE_7);
            return ConfiguredModel.builder()
                    .modelFile(existingBlock(block, age.toString()))
                    .build();
    	});
    }
    public void plantBlockWithSubDirectory(RegistryObject<Block> block)
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
    }
    
//    private void registerTankReservoir()
//    {
//        ModelFile tr_bordered = models().getExistingFile(modLoc("block/reservoir/reservoir_bordered"));
//        ModelFile tr_north_corner = models().getExistingFile(modLoc("block/reservoir/reservoir_north_corner"));
//        ModelFile tr_north_east_south_west = models().getExistingFile(modLoc("block/reservoir/reservoir_north_east_south_west"));
//        ModelFile tr_north_east_south = models().getExistingFile(modLoc("block/reservoir/reservoir_north_east_south"));
//        ModelFile tr_north_east = models().getExistingFile(modLoc("block/reservoir/reservoir_north_east"));
//        ModelFile tr_north_south = models().getExistingFile(modLoc("block/reservoir/reservoir_north_south"));
//        ModelFile tr_north = models().getExistingFile(modLoc("block/reservoir/reservoir_north"));
//    	
//        getVariantBuilder(WUTBlocks.RESERVOIR.get()).forAllStates(state -> {
//            
//            boolean north = state.getValue(TankReservoirBlock.NORTH);
//            boolean east = state.getValue(TankReservoirBlock.EAST);
//            boolean south = state.getValue(TankReservoirBlock.SOUTH);
//            boolean west = state.getValue(TankReservoirBlock.WEST);
//            boolean corner_north_west = state.getValue(TankReservoirBlock.CORNER_NORTH_WEST);
//            boolean corner_north_east = state.getValue(TankReservoirBlock.CORNER_NORTH_EAST);
//            boolean corner_south_east = state.getValue(TankReservoirBlock.CORNER_SOUTH_EAST);
//            boolean corner_south_west = state.getValue(TankReservoirBlock.CORNER_SOUTH_WEST);
//            
//            return ConfiguredModel.builder()
//            		.modelFile(north || east || west || south ? tr_north : tr_north_south)
//                    .build();
//        });
//    }
    
    /*
     * 
     * SORTOF
     * 
     */
    private ResourceLocation key(Block block)
    {
        return ForgeRegistries.BLOCKS.getKey(block);
    }
    private ResourceLocation key(RegistryObject<Block> block)
    {
        return ForgeRegistries.BLOCKS.getKey(block.get());
    }

    private BlockModelBuilder block(RegistryObject<Block> block) { return block(block, ""); }

    private BlockModelBuilder block(RegistryObject<Block> block, String suffix)
    {
        String name = block.getId().getPath();
        String path = "block/" + name;
        if (!suffix.isBlank())
        {
            path += "_" + suffix;
        }
        return models().getBuilder(path);
    }
    
    private ModelFile existingBlock(RegistryObject<Block> block) { return existingBlock(block, ""); }

    private ModelFile existingBlock(RegistryObject<Block> block, String suffix)
    {
        ResourceLocation name = block.getId();
        String path = "block/" + name.getPath();
        if (!suffix.isBlank())
        {
            path += "_" + suffix;
        }
        return models().getExistingFile(new ResourceLocation(name.getNamespace(), path));
    }

    private ModelFile existingBlockSubDirectory(RegistryObject<Block> block, String suffix)
    {
        ResourceLocation name = block.getId();
        String path = "block/" + name.getPath() + "/" + name.getPath();
        if (!suffix.isBlank())
        {
            path += "_" + suffix;
        }
        return models().getExistingFile(new ResourceLocation(name.getNamespace(), path));
    }
    
    private ItemModelBuilder simpleBlockWithItem(RegistryObject<Block> block, ModelFile model, String itemRenderType)
    {
        return simpleBlockWithItem(block, model).renderType(itemRenderType);
    }

    private ItemModelBuilder simpleBlockWithItem(RegistryObject<Block> block, ModelFile model)
    {
        simpleBlock(block.get(), model);
        return simpleBlockItem(block, model);
    }

    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> block, ModelFile model, String renderType)
    {
        return simpleBlockItem(block, model).renderType(renderType);
    }

    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> block, ModelFile model)
    {
        return itemModels().getBuilder(block.getId().getPath()).parent(model);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Block> block, String renderType)
    {
        return simpleItem(block.getId().getPath(), renderType);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Block> block, String texture, String renderType)
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
    
    @Override
    public String getName()
    {
        return "WitherUtils Blockstates";
    }
}