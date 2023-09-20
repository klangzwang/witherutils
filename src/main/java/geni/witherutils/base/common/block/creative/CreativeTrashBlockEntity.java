package geni.witherutils.base.common.block.creative;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.io.fluid.WitherFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class CreativeTrashBlockEntity extends WitherMachineBlockEntity {
	
	EnergyStorage energy = new EnergyStorage(10000);
    private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> this.energy);
	WitherFluidTank tank = new WitherFluidTank(this, 10000);
	LazyOptional<WitherFluidTank> fluidCap = LazyOptional.of(() -> tank);
    
    public CreativeTrashBlockEntity(BlockPos pos, BlockState state)
    {
        super(WUTEntities.CREATIVE_TRASH.get(), pos, state);
    }

    @Override
    public void serverTick()
    {
    	super.serverTick();

    	for(Direction facing : Direction.values())
    	{

    		BlockEntity be = level.getBlockEntity(worldPosition.relative(facing));
    		if(be != null)
    		{
                LazyOptional<IEnergyStorage> energyHandler = be.getCapability(ForgeCapabilities.ENERGY, facing);
                energyHandler.ifPresent((handler) -> {
                    if (handler.getEnergyStored() > 0)
                    {
                		int received = be.getCapability(ForgeCapabilities.ENERGY, null).map(cap -> cap.extractEnergy(this.energy.receiveEnergy(10000 / 80, false), false)).orElse(0);
                		if(received > 0)
                		{
//               	    		System.out.println(received);
                		}
//            	    	int received = storage.receiveEnergy(1000, false);
                    }
                });
    		}
    	}
    	if(energy.getEnergyStored() == energy.getMaxEnergyStored())
    		energy.extractEnergy(energy.getMaxEnergyStored(), false);
    }
    
    @Override
    public void clientTick()
    {
    	super.clientTick();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        if(cap == ForgeCapabilities.FLUID_HANDLER_ITEM)
            return fluidCap.cast();
        else if(cap == ForgeCapabilities.ENERGY)
            return energyCap.cast();
        return LazyOptional.empty();
    }
}
