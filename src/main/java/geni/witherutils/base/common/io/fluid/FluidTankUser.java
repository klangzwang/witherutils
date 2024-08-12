package geni.witherutils.base.common.io.fluid;

import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public interface FluidTankUser {

    MachineTankLayout getTankLayout();
    MachineFluidHandler getMachineFluidHandler();
    MachineFluidHandler createMachineFluidHandler();

    default void saveTank(HolderLookup.Provider lookupProvider, CompoundTag pTag)
    {
        pTag.put("Fluids", getMachineFluidHandler().serializeNBT(lookupProvider));
    }

    default void loadTank(HolderLookup.Provider lookupProvider, CompoundTag pTag)
    {
    	getMachineFluidHandler().deserializeNBT(lookupProvider, pTag.getCompound("Fluids"));
    }

    ICapabilityProvider<WitherMachineBlockEntity, Direction, IFluidHandler> FLUID_HANDLER_PROVIDER =
        (be, side) -> {
            if (be instanceof FluidTankUser user) 
            {
                return user.getMachineFluidHandler().getForSide(side);
            }
            return null;
        };
}