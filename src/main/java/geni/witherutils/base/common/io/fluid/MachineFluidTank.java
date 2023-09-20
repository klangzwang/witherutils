package geni.witherutils.base.common.io.fluid;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

public class MachineFluidTank extends FluidTank {
	
    public boolean allowInput = true, allowOutput = true;
    private @Nullable BlockEntity parent = null;

    public MachineFluidTank(int capacity, BlockEntity parent) {
        super(capacity, (FluidStack stack) -> true);
        this.parent = parent;
    }

    public MachineFluidTank(BlockEntity parent) {
        super(8 * FluidType.BUCKET_VOLUME, (FluidStack stack) -> true);
        this.parent = parent;
    }

    public MachineFluidTank(int capacity, Predicate<FluidStack> validator, BlockEntity parent)
    {
        super(capacity, validator);
        this.parent = parent;
    }

    public int transferFrom(IFluidHandler from, int desiredAmount, boolean force)
    {
        FluidStack incomingStack = from.drain(desiredAmount, FluidAction.SIMULATE);
        int transferredAmount = fill(incomingStack, FluidAction.EXECUTE, force);
        from.drain(transferredAmount, FluidAction.EXECUTE);
        return transferredAmount;
    }

    public int transferTo(IFluidHandler to, int desiredAmount, boolean force)
    {
        FluidStack outStack = fluid.copy();
        int transferAmount = 0;
        if (!outStack.isEmpty()) {
            outStack.setAmount(drain(desiredAmount, FluidAction.SIMULATE, force));
            transferAmount = to.fill(outStack, FluidAction.EXECUTE);
        }
        drain(transferAmount, FluidAction.EXECUTE);
        return transferAmount;
    }

    public int fill(FluidStack source, FluidAction action, boolean force)
    {
        if (source.isEmpty())
            return 0;
        if (!fluid.isEmpty() && !fluid.isFluidEqual(source))
            return 0;
        if (!allowInput && !force)
            return 0;
        if (!isFluidValid(source) && !force)
            return 0;
        else if (action.simulate()) {
            return Math.min(capacity - fluid.getAmount(), source.getAmount());
        } else {
            if (fluid.isEmpty()) {
                fluid = new FluidStack(source, Math.min(capacity, source.getAmount()));
                onContentsChanged();
                return fluid.getAmount();
            } else {
                int availableSpace = capacity - fluid.getAmount();
                int sourceAmount = source.getAmount();
                int transferAmount = Math.min(availableSpace, sourceAmount);
                fluid.grow(transferAmount);

                if (transferAmount > 0)
                    onContentsChanged();
                return transferAmount;
            }
        }
    }

    public int fill(int desiredAmount, FluidAction action, boolean force)
    {
        if (fluid.isEmpty())
        {
            return 0;
        }
        return fill(new FluidStack(fluid.getFluid(), desiredAmount), action, force);
    }

    @Override
    public int fill(FluidStack source, FluidAction action)
    {
        return fill(source, action, false);
    }

    public int drain(int maxDrain, FluidAction action, boolean force)
    {
        if (maxDrain <= 0)
            return 0;
        if (!allowOutput && !force)
            return 0;
        int transferAmount = Math.min(maxDrain, fluid.getAmount());
        if (action == FluidAction.EXECUTE && transferAmount > 0) {
            fluid.shrink(transferAmount);
            onContentsChanged();
        }
        return transferAmount;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action)
    {
        if (resource.isEmpty() || !resource.isFluidEqual(fluid))
            return FluidStack.EMPTY;
        return drain(resource.getAmount(), action);
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action)
    {
        var storedFluid = fluid.getFluid();
        int amount = drain(maxDrain, action, false);
        if (amount == 0)
            return FluidStack.EMPTY;
        return new FluidStack(storedFluid, amount);
    }

    public InteractionResult onClickedWithPotentialFluidItem(Player player, InteractionHand hand)
    {
        if (!player.level().isClientSide() && player.hasItemInSlot(EquipmentSlot.MAINHAND) && hand == InteractionHand.MAIN_HAND) {
            ItemStack heldStack = player.getMainHandItem();
            Optional<IFluidHandlerItem> heldItemFH = heldStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve();
            if (heldStack.getItem() == Items.BUCKET && allowOutput) {
                if (getFluidAmount() >= FluidType.BUCKET_VOLUME) {
                    FluidStack outStack = drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.EXECUTE);
                    ItemStack filledBucket = new ItemStack(outStack.getFluid().getBucket(), 1);
                    if (heldStack.getCount() == 1) {
                        player.setItemInHand(hand, filledBucket);
                    } else {
                        heldStack.shrink(1);
                        if (!player.addItem(filledBucket)) {
                            player.drop(filledBucket, true);
                        }
                    }
                    return InteractionResult.CONSUME;
                }
            } else if (heldStack.getItem() instanceof BucketItem filledBucket && allowInput) {
                FluidStack bucketContent = new FluidStack(filledBucket.getFluid(), FluidType.BUCKET_VOLUME);
                if (fill(bucketContent, IFluidHandler.FluidAction.SIMULATE) == FluidType.BUCKET_VOLUME) {
                    fill(bucketContent, IFluidHandler.FluidAction.EXECUTE);
                    player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BUCKET, 1));
                    return InteractionResult.CONSUME;
                }
            } else if (heldItemFH.isPresent()) {
                IFluidHandlerItem itemFluid = heldItemFH.get();
                boolean hasTransferred = false;

                if (allowOutput) {
                    hasTransferred = transferTo(itemFluid, Integer.MAX_VALUE, false) > 0;
                }
                if (allowInput && !hasTransferred) {
                    hasTransferred = transferFrom(itemFluid, Integer.MAX_VALUE, false) > 0;
                }

                return hasTransferred ? InteractionResult.CONSUME : InteractionResult.PASS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void setFluid(FluidStack stack)
    {
        setFluid(stack, true);
    }

    public void setFluid(FluidStack stack, boolean force)
    {
        if (force || (allowInput && isFluidValid(stack)))
        {
            this.fluid = stack;
            onContentsChanged();
        }
    }

    @Override
    protected void onContentsChanged()
    {
        super.onContentsChanged();
        if (parent != null)
            parent.setChanged();
    }
}
