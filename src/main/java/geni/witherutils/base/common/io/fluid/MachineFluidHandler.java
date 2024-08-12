package geni.witherutils.base.common.io.fluid;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntConsumer;

public class MachineFluidHandler implements IFluidHandler, INBTSerializable<CompoundTag> {

    public static final String TANK_INDEX = "Index";
    public static final String TANK_CONTENTS = "Contents";
    private final MachineTankLayout layout;
    private final Map<Integer, MachineFluidTank> tanks =  new HashMap<>();
    private final List<FluidStack> stacks;

    private IntConsumer changeListener = i -> {};

    public MachineFluidHandler(MachineTankLayout layout)
    {
        this.layout = layout;
        this.stacks = NonNullList.withSize(getTanks(), FluidStack.EMPTY);
    }

    public void addSlotChangedCallback(IntConsumer callback) {
        changeListener = changeListener.andThen(callback);
    }

    public MachineTankLayout getLayout() {
        return layout;
    }

    @Deprecated
    public final MachineFluidTank getTank(int tank)
    {
        if (tank > getTanks())
        {
            throw new IndexOutOfBoundsException("No tank found for index " + tank + " in range" + getTanks() + ".");
        }
        return tanks.computeIfAbsent(tank, i -> new MachineFluidTank(i, this));
    }

    @Override
    public int getTanks() {
        return layout.getTankCount();
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return stacks.get(tank);
    }

    public void setFluidInTank(int tank, FluidStack fluid) {
        stacks.set(tank, fluid);
    }

    @Override
    public int getTankCapacity(int tank) {
        return layout.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return layout.isFluidValid(tank, stack);
    }

    public boolean canInsert(int tank) {
        return layout.canInsert(tank);
    }

    public boolean canExtract(int tank) {
        return layout.canExtract(tank);
    }

    @Nullable
    public IFluidHandler getForSide(@Nullable Direction side) {
        if (side == null) {
            return this;
        }
        return null;
    }

    public int fill(int tank, FluidStack resource, FluidAction action) {
        FluidStack fluid = getFluidInTank(tank);
        int capacity = getTankCapacity(tank);
        if (resource.isEmpty()) {
            return 0;
        }

        if (!isFluidValid(tank, resource)) {
            return 0;
        }

        if (action.simulate()) {
            if (fluid.isEmpty()) {
                return Math.min(capacity, resource.getAmount());
            }

            if (!FluidStack.isSameFluidSameComponents(fluid, resource)) {
                return 0;
            }

            return Math.min(capacity - fluid.getAmount(), resource.getAmount());
        }

        if (fluid.isEmpty()) {
            fluid = new FluidStack(resource.getFluid(), Math.min(capacity, resource.getAmount()));
            setFluidInTank(tank, fluid);
            onContentsChanged(tank);
            return fluid.getAmount();
        }

        if (!FluidStack.isSameFluidSameComponents(fluid, resource)) {
            return 0;
        }

        int filled = capacity - fluid.getAmount();

        if (resource.getAmount() < filled) {
            fluid.grow(resource.getAmount());
            filled = resource.getAmount();
        } else {
            fluid.setAmount(capacity);
        }

        if (filled > 0) {
            onContentsChanged(tank);
        }

        return filled;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {

        if (resource.isEmpty()) {
            return 0;
        }

        FluidStack resourceLeft = resource.copy();
        int totalFilled = 0;

        for (int index = 0; index < getTanks(); index++) {
            if (!canInsert(index)) {
                continue;
            }

            int filled = fill(index, resourceLeft, action);
            resourceLeft.shrink(filled);
            totalFilled += filled;

            if (filled > 0) {
                onContentsChanged(index);
            }

            if (resourceLeft.isEmpty()) {
                break;
            }

        }
        return totalFilled;
    }

    public FluidStack drain(int tank, int maxDrain, FluidAction action) {
        FluidStack fluid = getFluidInTank(tank);
        int drained = maxDrain;
        if (fluid.getAmount() < drained) {
            drained = fluid.getAmount();
        }
        FluidStack stack = new FluidStack(fluid.getFluid(), drained);
        if (action.execute() && drained > 0) {
            fluid.shrink(drained);
            onContentsChanged(tank);
        }
        return stack;
    }

    @SuppressWarnings("removal")
	public FluidStack drain(int tank, FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || !isFluidValid(tank, resource)) {
            return FluidStack.EMPTY;
        }

        if (!getFluidInTank(tank).isEmpty() && !getFluidInTank(tank).isFluidEqual(resource)) {
            return FluidStack.EMPTY;
        }

        return drain(tank, resource.getAmount(), action);
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        for (int index = 0; index < getTanks(); index++) {
            if (canExtract(index)) {
                if (drain(index, resource, FluidAction.SIMULATE) != FluidStack.EMPTY) {
                    FluidStack drained = drain(index, resource, action);
                    if (!drained.isEmpty()) {
                        onContentsChanged(index);
                        changeListener.accept(index);
                    }
                    return drained;
                }
            }
        }

        return FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        for (int index = 0; index < getTanks(); index++) {
            if (canExtract(index)) {
                if (drain(index, maxDrain, FluidAction.SIMULATE) != FluidStack.EMPTY) {
                    FluidStack drained = drain(index, maxDrain, action);
                    if (!drained.isEmpty()) {
                        onContentsChanged(index);
                        changeListener.accept(index);
                    }
                    return drained;
                }
            }
        }

        return FluidStack.EMPTY;
    }

    protected void onContentsChanged(int slot) {}

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider lookupProvider) {
        ListTag nbtTagList = new ListTag();
        for (int i = 0; i < getTanks(); i++) {
            CompoundTag tankTag = new CompoundTag();
            tankTag.putInt(TANK_INDEX, i);
            tankTag.put(TANK_CONTENTS, stacks.get(i).saveOptional(lookupProvider));
            nbtTagList.add(tankTag);
        }
        CompoundTag nbt = new CompoundTag();
        nbt.put("Fluids", nbtTagList);
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
        ListTag tagList = nbt.getList("Tanks", Tag.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag tankTag = tagList.getCompound(i);
            int index = tankTag.getInt(TANK_INDEX);
            stacks.set(index, FluidStack.parseOptional(lookupProvider, tankTag.getCompound(TANK_CONTENTS)));
        }
    }
}
