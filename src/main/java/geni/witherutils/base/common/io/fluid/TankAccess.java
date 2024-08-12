package geni.witherutils.base.common.io.fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class TankAccess {

    private int index = Integer.MIN_VALUE;

    public MachineFluidTank getTank(FluidTankUser machine)
    {
        return getTank(machine.getMachineFluidHandler());
    }

    @SuppressWarnings("deprecation")
	public MachineFluidTank getTank(MachineFluidHandler fluidHandler)
    {
        return fluidHandler.getTank(index);
    }

    public int getCapacity(FluidTankUser machine)
    {
        return getCapacity(machine.getMachineFluidHandler());
    }

    public int getCapacity(MachineFluidHandler fluidHandler)
    {
        return fluidHandler.getTankCapacity(index);
    }

    public FluidStack getFluid(FluidTankUser machine)
    {
        return getFluid(machine.getMachineFluidHandler());
    }

    public FluidStack getFluid(MachineFluidHandler handler)
    {
        return handler.getFluidInTank(index);
    }

    public int getFluidAmount(FluidTankUser machine)
    {
        return getFluid(machine).getAmount();
    }

    public int getFluidAmount(MachineFluidHandler handler)
    {
        return getFluid(handler).getAmount();
    }

    public void setFluid(FluidTankUser machine, FluidStack fluid)
    {
        setFluid(machine.getMachineFluidHandler(), fluid);
    }

    public void setFluid(MachineFluidHandler handler, FluidStack fluid)
    {
        handler.setFluidInTank(index, fluid);
    }

    public boolean isFluidValid(FluidTankUser machine, FluidStack fluid)
    {
        return isFluidValid(machine.getMachineFluidHandler(), fluid);
    }

    public boolean isFluidValid(MachineFluidHandler handler, FluidStack fluid)
    {
        return handler.isFluidValid(index, fluid);
    }

    public boolean isEmpty(FluidTankUser machine)
    {
        return isEmpty(machine.getMachineFluidHandler());
    }

    public boolean isEmpty(MachineFluidHandler handler)
    {
        return getFluid(handler).isEmpty();
    }

    public boolean canInsert(FluidTankUser machine)
    {
        return canInsert(machine.getMachineFluidHandler());
    }

    public boolean canInsert(MachineFluidHandler handler)
    {
        return handler.canInsert(index);
    }

    public boolean canExtract(FluidTankUser machine)
    {
        return canExtract(machine.getMachineFluidHandler());
    }

    public boolean canExtract(MachineFluidHandler handler)
    {
        return handler.canExtract(index);
    }

    public int fill(MachineFluidHandler handler, FluidStack stack, IFluidHandler.FluidAction action)
    {
        return handler.fill(index, stack, action);
    }

    public int fill(FluidTankUser machine, FluidStack stack, IFluidHandler.FluidAction action)
    {
        return fill(machine.getMachineFluidHandler(), stack, action);
    }

    public FluidStack drain(MachineFluidHandler handler, FluidStack resource, IFluidHandler.FluidAction action)
    {
        return handler.drain(index, resource, action);
    }

    public FluidStack drain(FluidTankUser machine, FluidStack resource, IFluidHandler.FluidAction action)
    {
        return drain(machine.getMachineFluidHandler(), resource, action);
    }

    public FluidStack drain(MachineFluidHandler handler, int maxDrain, IFluidHandler.FluidAction action)
    {
        return handler.drain(index, maxDrain, action);
    }

    public FluidStack drain(FluidTankUser machine, int maxDrain, IFluidHandler.FluidAction action)
    {
        return drain(machine.getMachineFluidHandler(), maxDrain, action);
    }

    public boolean isTank(int slot)
    {
        return this.index == slot;
    }

    void init(int i)
    {
        if (index == Integer.MIN_VALUE)
        {
            index = i;
        }
        else if (index != i)
        {
            throw new IllegalArgumentException("TankLayout changed dynamically from " + index + " to " + i + ", don't do that");
        }
    }
}
