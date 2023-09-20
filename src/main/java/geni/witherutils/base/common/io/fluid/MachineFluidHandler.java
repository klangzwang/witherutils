package geni.witherutils.base.common.io.fluid;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import geni.witherutils.api.capability.IWitherCapabilityProvider;
import geni.witherutils.api.io.IIOConfig;

import java.util.EnumMap;
import java.util.List;

public class MachineFluidHandler implements IFluidHandler, IWitherCapabilityProvider<IFluidHandler> {
    
    private final IIOConfig config;

    private final List<IFluidTank> tanks;

    private final EnumMap<Direction, LazyOptional<Sided>> sideCache = new EnumMap<>(Direction.class);
    private LazyOptional<MachineFluidHandler> selfCache = LazyOptional.empty();

    public MachineFluidHandler(IIOConfig config, IFluidTank... tanks)
    {
        this.config = config;
        this.tanks = List.of(tanks);
    }

    public final IIOConfig getConfig()
    {
        return config;
    }

    public final IFluidTank getTank(int tank)
    {
        return tanks.get(tank);
    }

    @Override
    public int getTanks()
    {
        return tanks.size();
    }

    @Override
    public FluidStack getFluidInTank(int tank)
    {
        return tanks.get(tank).getFluid();
    }

    @Override
    public int getTankCapacity(int tank)
    {
        return tanks.get(tank).getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack)
    {
        return tanks.get(tank).isFluidValid(stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action)
    {
        if (resource.isEmpty())
            return 0;
        FluidStack resourceLeft = resource.copy();
        int totalFilled = 0;

        for (IFluidTank tank : tanks)
        {
            int filled = tank.fill(resourceLeft, action);
            resourceLeft.shrink(filled);
            totalFilled += filled;

            if (resourceLeft.isEmpty())
                break;
        }
        return totalFilled;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action)
    {
        for (IFluidTank tank : tanks)
        {
            if (tank.drain(resource, FluidAction.SIMULATE) != FluidStack.EMPTY)
            {
                return tank.drain(resource, action);
            }
        }

        return FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action)
    {
        for (IFluidTank tank : tanks)
        {
            if (tank.drain(maxDrain, FluidAction.SIMULATE) != FluidStack.EMPTY)
            {
                return tank.drain(maxDrain, action);
            }
        }
        return FluidStack.EMPTY;
    }

    @Override
    public Capability<IFluidHandler> getCapabilityType()
    {
        return ForgeCapabilities.FLUID_HANDLER;
    }

    @Override
    public LazyOptional<IFluidHandler> getCapability(@Nullable Direction side)
    {
        if (side == null)
        {
            if (!selfCache.isPresent())
                selfCache = LazyOptional.of(() -> this);
            return selfCache.cast();
        }
        if (!config.getMode(side).canConnect())
            return LazyOptional.empty();
        return sideCache.computeIfAbsent(side, dir -> LazyOptional.of(() -> new Sided(this, dir))).cast();
    }

    @Override
    public void invalidateSide(@Nullable Direction side)
    {
        if (side != null)
        {
            if (sideCache.containsKey(side))
            {
                sideCache.get(side).invalidate();
                sideCache.remove(side);
            }
        }
        else
        {
            selfCache.invalidate();
        }
    }

    @Override
    public void invalidateCaps()
    {
        for (LazyOptional<Sided> side : sideCache.values())
        {
            side.invalidate();
        }
        selfCache.invalidate();
    }

    private static class Sided implements IFluidHandler
    {
        private final MachineFluidHandler master;
        private final Direction direction;

        public Sided(MachineFluidHandler master, Direction direction)
        {
            this.master = master;
            this.direction = direction;
        }

        @Override
        public int getTanks()
        {
            return master.getTanks();
        }

        @Override
        public FluidStack getFluidInTank(int tank)
        {
            return master.getFluidInTank(tank);
        }

        @Override
        public int getTankCapacity(int tank)
        {
            return master.getTankCapacity(tank);
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack)
        {
            return master.isFluidValid(tank, stack);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action)
        {
            if (master.getConfig().getMode(direction).canInput())
                return master.fill(resource, action);
            return 0;
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action)
        {
            if (master.getConfig().getMode(direction).canOutput())
                return master.drain(resource, action);
            return FluidStack.EMPTY;
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action)
        {
            if (master.getConfig().getMode(direction).canOutput())
                return master.drain(maxDrain, action);
            return FluidStack.EMPTY;
        }
    }
}
