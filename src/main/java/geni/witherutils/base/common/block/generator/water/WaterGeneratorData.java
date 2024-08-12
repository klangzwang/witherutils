package geni.witherutils.base.common.block.generator.water;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import geni.witherutils.core.common.util.FacingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.LiquidBlock;

public class WaterGeneratorData {
	
	private Map<Direction, Supplier<Float>> powerLevelSides;
	private Map<Direction, Supplier<Float>> fluidLevelSides;
	private Map<Direction, Supplier<Float>> flowsLevelSides;

	public float outputPower;
	
    public WaterGeneratorData()
    {
        powerLevelSides = new ConcurrentHashMap<Direction, Supplier<Float>>();
        fluidLevelSides = new ConcurrentHashMap<Direction, Supplier<Float>>();
        flowsLevelSides = new ConcurrentHashMap<Direction, Supplier<Float>>();
        for (Direction facing : Direction.values())
        {
        	if(facing != Direction.UP || facing != Direction.DOWN)
        	{
        		powerLevelSides.put(facing, () -> 0.f);
        		fluidLevelSides.put(facing, () -> 0.f);
        		flowsLevelSides.put(facing, () -> 0.f);
        	}
        }
    }
    
    public void tick(WaterGeneratorBlockEntity tile)
    {
        if (tile == null)
            return;
    	
        if(tile.getLevel() instanceof ServerLevel level)
        {
            for (Direction facing : Direction.values())
            {
            	if(facing != Direction.UP || facing != Direction.DOWN)
            	{
            		for (BlockPos aroundPos : FacingUtil.getAroundAxis(Axis.Y))
            		{
                		BlockPos pos = new BlockPos(
        				tile.getBlockPos().getX() + aroundPos.getX(),
        				tile.getBlockPos().getY() + aroundPos.getY(),
        				tile.getBlockPos().getZ() + aroundPos.getZ());
                		
                		if(level.getBlockState(pos).getBlock() instanceof LiquidBlock)
                		{
//                			FluidState fluid = level.getBlockState(pos).getFluidState();
//                			tickPowerLevel(facing, level, fluid, pos);
                		}
            		}
            	}
            }
        }
    }
    
//    public void tickPowerLevel(Direction facing, ServerLevel level, FluidState fluid, BlockPos pos)
//    {
//    	powerLevelSides.put(facing, () -> fluid.getHeight(level, pos));
//
//    	powerLevelSides.forEach((fa, fl) -> {
////    		outputPower = outputPower + fl.get();
//    		System.out.println(fl.get());
//    	});
//    }
}        

//        int level;
//        
//        for (Direction facing : Direction.values())
//        {
//        	if(facing != Direction.UP || facing != Direction.DOWN)
//        	{
//            	for (BlockPos aroundPos : FacingUtil.getAroundAxis(Axis.Y))
//            	{

//            		
//                    if(tile.getLevel().getBlockState(pos).getBlock() instanceof LiquidBlock)
//                    {
//                        FluidState fluid = tile.getLevel().getBlockState(pos).getFluidState();
//                    	
//                    	
//                        System.out.println("EfficiencyData: CHECK: " + tile.getLevel().getBlockState(pos).getBlock());
//                    	
//                    	
//                        if (fluid != null && (level = fluid.getAmount()) > 0)
//                        {
//                            this.efficiencyFluidLevel -= Math.max(0, 6 - level) * 4;
//                        }
//                        
//                        if (fluid != null)
//                        {
//                        	System.out.println("EfficiencyData: GETAMOUNT: " + fluid.getAmount());
//                        	System.out.println("EfficiencyData: GETHEIGHT: " + fluid.getHeight(tile.getLevel(), tile.getBlockPos()));
//                        	System.out.println("EfficiencyData: GETOWNHEIGHT: " + fluid.getOwnHeight());
//                        	System.out.println("EfficiencyData: GETFLOW: " + fluid.getFlow(tile.getLevel(), pos));
//                        	System.out.println("EfficiencyData: GETFLUIDTYPE: " + fluid.getFluidType());
//                        	System.out.println("EfficiencyData: GETSHAPE: " + fluid.getShape(tile.getLevel(), pos));
//                        	System.out.println("EfficiencyData: GETTYPE: " + fluid.getType());
//                        	System.out.println("EfficiencyData: ISSOURCEOFTYPE: " + fluid.isSourceOfType(Fluids.FLOWING_WATER));
//                        	System.out.println("EfficiencyData: DENSITY: " + fluid.getType().getFluidType().getDensity());
//                        	System.out.println("EfficiencyData: DESCRIPTION: " + fluid.getType().getFluidType().getDescription());
//                        	System.out.println("EfficiencyData: LIGHTLEVEL: " + fluid.getType().getFluidType().getLightLevel());
//                        	System.out.println("EfficiencyData: TEMP: " + fluid.getType().getFluidType().getTemperature());
//                        	System.out.println("EfficiencyData: VISCOSITY: " + fluid.getType().getFluidType().getViscosity());
//                        	
//                        	if(tile.getLevel().getBlockState(pos).getBlock() instanceof LiquidBlock liquid)
//                        	{
//                        		if(fluid.hasProperty(BlockStateProperties.LEVEL))
//                        		{
//                            		Integer LEVEL = fluid.getValue(BlockStateProperties.LEVEL);
//                            		
//                            		System.out.println("EfficiencyData: LEVEL: " + LEVEL);
//                            		
//                            		fluid.trySetValue(BlockStateProperties.LEVEL, 1);
//                            		
////                            		liquid.fluid.getFlowing()
////                            		liquid.fluid.LEVEL;
//                            		
//                            		VoxelShape SHAPE = liquid.STABLE_SHAPE;
//                            		System.out.println("EfficiencyData: SHAPEMAXZ: " + SHAPE.bounds().maxZ);
//                            		
//                            		ImmutableList<Direction> FLOWDIR = liquid.POSSIBLE_FLOW_DIRECTIONS;
//                            		System.out.println("EfficiencyData: FLOWDIR: " + FLOWDIR);
//                        		}
//                        	}
//                        }
//                    }
//            	}
//        	}
//        }

//    public float getFluidLevelEfficiency()
//    {
//        return this.efficiencyFluidLevel;
//    }
//    public float getAdjacentBasesEfficiency()
//    {
//        return this.efficiencyAdjacentBases;
//    }
//    public float getTotalEfficiency()
//    {
//        return this.efficiencyFluidLevel + this.efficiencyAdjacentBases;
//    }
