package geni.witherutils.base.common.base;

import geni.witherutils.api.io.energy.EnergyIOMode;
import geni.witherutils.api.soulbank.ISoulBankScalable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class WitherMachineEnergyGenBlockEntity extends WitherMachineEnergyBlockEntity {
    
    public WitherMachineEnergyGenBlockEntity(ISoulBankScalable capacityKey, ISoulBankScalable transferKey, ISoulBankScalable consumptionKey, BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState)
    {
        super(EnergyIOMode.Output, capacityKey, transferKey, consumptionKey, type, worldPosition, blockState);
    }

    @Override
    public void serverTick()
    {
        if (isGenerating())
        {
        	if(!hasEfficiencyRate())
        		getEnergyStorage().addEnergy(getGenerationRate());
        	else
        	{
        		if(level.getGameTime() % 3 * getEfficiencyRate() == 0)
        			getEnergyStorage().addEnergy(getGenerationRate());
        	}
        }
        super.serverTick();
    }

    public abstract boolean isGenerating();

    public abstract int getGenerationRate();
    
    public abstract boolean hasEfficiencyRate();
    
    public abstract float getEfficiencyRate();
}