package geni.witherutils.base.common.io.fluid;

import org.jetbrains.annotations.Nullable;

import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class WitherFluidTank extends FluidTank {

    @SuppressWarnings("unused")
    private WitherMachineBlockEntity be;
    protected boolean canFill = true;
    protected boolean canDrain = true;
    
    public WitherFluidTank(WitherMachineBlockEntity be, int capacity)
    {
        super(capacity);
        this.be = be;
    }

    /**
     * Use this method to bypass the restrictions from {@link #canDrainFluidType(FluidStack)}
     * Meant for use by the owner of the tank when they have {@link #canDrain()} set to false}.
     */
    @Nullable
    public FluidStack drainInternal(int maxDrain, boolean doDrain)
    {
        if (fluid == null || maxDrain <= 0)
        {
            return null;
        }

        int drained = maxDrain;
        if (fluid.getAmount() < drained)
        {
            drained = fluid.getAmount();
        }

        FluidStack stack = new FluidStack(fluid, drained);
        if (doDrain)
        {
            fluid.setAmount(-drained);
            if (fluid.getAmount() <= 0)
            {
                fluid = null;
            }

            onContentsChanged();
        }
        return stack;
    }
    
    @Override
    protected void onContentsChanged()
    {
    }
    
    /**
     * Whether this tank can be filled with {@link IFluidHandler}
     *
     * @see IFluidTankProperties#canFill()
     */
    public boolean canFill()
    {
        return canFill;
    }

    /**
     * Whether this tank can be drained with {@link IFluidHandler}
     *
     * @see IFluidTankProperties#canDrain()
     */
    public boolean canDrain()
    {
        return canDrain;
    }

    /**
     * Set whether this tank can be filled with {@link IFluidHandler}
     *
     * @see IFluidTankProperties#canFill()
     */
    public void setCanFill(boolean canFill)
    {
        this.canFill = canFill;
    }

    /**
     * Set whether this tank can be drained with {@link IFluidHandler}
     *
     * @see IFluidTankProperties#canDrain()
     */
    public void setCanDrain(boolean canDrain)
    {
        this.canDrain = canDrain;
    }

    /**
     * Returns true if the tank can be filled with this type of fluid.
     * Used as a filter for fluid types.
     * Does not consider the current contents or capacity of the tank,
     * only whether it could ever fill with this type of fluid.
     *
     * @see IFluidTankProperties#canFillFluidType(FluidStack)
     */
    public boolean canFillFluidType(FluidStack fluid)
    {
        return canFill();
    }

    /**
     * Returns true if the tank can drain out this type of fluid.
     * Used as a filter for fluid types.
     * Does not consider the current contents or capacity of the tank,
     * only whether it could ever drain out this type of fluid.
     *
     * @see IFluidTankProperties#canDrainFluidType(FluidStack)
     */
    public boolean canDrainFluidType(@Nullable FluidStack fluid)
    {
        return fluid != null && canDrain();
    }
}