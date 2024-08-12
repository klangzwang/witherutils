package geni.witherutils.base.common.base;

import java.util.function.Supplier;

import geni.witherutils.api.io.energy.EnergyIOMode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class WitherMachineEnergyGenBlockEntity extends WitherMachineEnergyBlockEntity {
    
    public WitherMachineEnergyGenBlockEntity(EnergyIOMode energyIOMode, Supplier<Integer> capacity, Supplier<Integer> usageRate, BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState)
    {
		super(EnergyIOMode.OUTPUT, capacity, usageRate, type, worldPosition, blockState);
	}

	@Override
    public void serverTick()
    {
        if (isGenerating())
        {
        	if(!hasEfficiencyRate())
        		getEnergyHandler(null).addEnergy(getGenerationRate());
        	else
        	{
        		if(level.getGameTime() % 3 * getEfficiencyRate() == 0)
        			getEnergyHandler(null).addEnergy(getGenerationRate());
        	}
        }
        super.serverTick();
    }

    public abstract boolean isGenerating();

    public abstract int getGenerationRate();
    
    public abstract boolean hasEfficiencyRate();
    
    public abstract float getEfficiencyRate();
}