package geni.witherutils.core.common.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.ItemHandlerHelper;

public class FluidsUtil {

	public static boolean tryFillPositionFromTank(Level world, BlockPos posSide, Direction sideOpp, IFluidHandler tankFrom, final int amount)
	{
		if (tankFrom == null)
		{
			return false;
		}
		try
		{
			IFluidHandler fluidTo = FluidUtil.getFluidHandler(world, posSide, sideOpp).orElse(null);
			if (fluidTo != null)
			{
				FluidStack wasDrained = tankFrom.drain(amount, FluidAction.SIMULATE);
				if (wasDrained == null)
				{
					return false;
				}
				int filled = fluidTo.fill(wasDrained, FluidAction.SIMULATE);
				if (wasDrained != null && wasDrained.getAmount() > 0 && filled > 0)
				{
					int realAmt = Math.min(filled, wasDrained.getAmount());
					wasDrained = tankFrom.drain(realAmt, FluidAction.EXECUTE);
					if (wasDrained == null)
					{
						return false;
					}
					int actuallyFilled = fluidTo.fill(wasDrained, FluidAction.EXECUTE);
					return actuallyFilled > 0;
				}
			}
			return false;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	public static void extractSourceWaterloggedCauldron(Level level, BlockPos posTarget, IFluidHandler tank)
	{
		BlockState targetState = level.getBlockState(posTarget);
		FluidState fluidState = level.getFluidState(posTarget);
		if (targetState.hasProperty(BlockStateProperties.WATERLOGGED)
				&& targetState.getValue(BlockStateProperties.WATERLOGGED)) {
			int simFill = tank.fill(new FluidStack(new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME).getFluid(),
					FluidType.BUCKET_VOLUME), FluidAction.SIMULATE);
			if (simFill == FluidType.BUCKET_VOLUME && level.setBlockAndUpdate(posTarget,
					targetState.setValue(BlockStateProperties.WATERLOGGED, false))) {
				tank.fill(new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME), FluidAction.EXECUTE);
			}
		} else if (targetState.getBlock() == Blocks.WATER_CAULDRON) {
			int simFill = tank.fill(new FluidStack(new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME).getFluid(),
					FluidType.BUCKET_VOLUME), FluidAction.SIMULATE);
			if (simFill == FluidType.BUCKET_VOLUME
					&& level.setBlockAndUpdate(posTarget, Blocks.CAULDRON.defaultBlockState())) {
				tank.fill(new FluidStack(new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME).getFluid(),
						FluidType.BUCKET_VOLUME), FluidAction.EXECUTE);
			}
		} else if (targetState.getBlock() == Blocks.LAVA_CAULDRON) {
			int simFill = tank.fill(new FluidStack(new FluidStack(Fluids.LAVA, FluidType.BUCKET_VOLUME).getFluid(),
					FluidType.BUCKET_VOLUME), FluidAction.SIMULATE);
			if (simFill == FluidType.BUCKET_VOLUME
					&& level.setBlockAndUpdate(posTarget, Blocks.CAULDRON.defaultBlockState())) {
				tank.fill(new FluidStack(new FluidStack(Fluids.LAVA, FluidType.BUCKET_VOLUME).getFluid(),
						FluidType.BUCKET_VOLUME), FluidAction.EXECUTE);
			}
		} else if (fluidState != null && fluidState.isSource() && fluidState.getType() != null) { // from ze world
			int simFill = tank.fill(new FluidStack(new FluidStack(fluidState.getType(), FluidType.BUCKET_VOLUME).getFluid(),
					FluidType.BUCKET_VOLUME), FluidAction.SIMULATE);
			if (simFill == FluidType.BUCKET_VOLUME
					&& level.setBlockAndUpdate(posTarget, Blocks.AIR.defaultBlockState())) {
				tank.fill(new FluidStack(fluidState.getType(), FluidType.BUCKET_VOLUME), FluidAction.EXECUTE);
			}
		}
	}

    public static String fluidToString(FluidStack fluid)
    {
        if (fluid == null)
        {
            return "null";
        }
        return fluid.getAmount() + "mb " + fluid.getFluid();
    }
    
    @SuppressWarnings("removal")
	public static List<FluidStack> mergeSameFluids(List<FluidStack> fluids)
    {
        List<FluidStack> stacks = new ArrayList<>();
        fluids.forEach(toAdd -> {
            boolean found = false;
            for (FluidStack stack : stacks)
            {
                if (stack.isFluidEqual(toAdd))
                {
                    stack.setAmount(toAdd.getAmount());
                    found = true;
                }
            }
            if (!found) {
                stacks.add(toAdd.copy());
            }
        });
        return stacks;
    }

    @SuppressWarnings("removal")
	public static boolean areFluidStackEqual(FluidStack a, FluidStack b)
    {
        return (a == null && b == null) || (a != null && a.isFluidEqual(b) && a.getAmount() == b.getAmount());
    }

    public static boolean areFluidsEqual(Fluid a, Fluid b)
    {
        if (a == null || b == null)
        {
            return a == b;
        }
        return a.toString().equals(b.toString());
    }

    @Nullable
    public static FluidStack move(IFluidHandler from, IFluidHandler to)
    {
        return move(from, to, Integer.MAX_VALUE);
    }

    @SuppressWarnings("removal")
	@Nullable
    public static FluidStack move(IFluidHandler from, IFluidHandler to, int max)
    {
        if (from == null || to == null)
        {
            return null;
        }
        FluidStack toDrainPotential;
        toDrainPotential = from.drain(max, FluidAction.EXECUTE);
        if (toDrainPotential == null)
        {
            return null;
        }
        int accepted = to.fill(toDrainPotential.copy(), FluidAction.EXECUTE);
        if (accepted <= 0)
        {
            return null;
        }
        FluidStack toDrain = new FluidStack(toDrainPotential.getFluid(), accepted);
        if (accepted < toDrainPotential.getAmount())
        {
            toDrainPotential = from.drain(toDrain, FluidAction.EXECUTE);
            if (toDrainPotential == null || toDrainPotential.getAmount() < accepted)
            {
                return null;
            }
        }
        FluidStack drained = from.drain(toDrain.copy(), FluidAction.SIMULATE);
        if (drained == null || toDrain.getAmount() != drained.getAmount() || !toDrain.isFluidEqual(drained))
        {
            String detail = "(To Drain = " + fluidToString(toDrain);
            detail += ",\npotential drain = " + fluidToString(toDrainPotential) + ")";
            detail += ",\nactually drained = " + fluidToString(drained) + ")";
            detail += ",\nIFluidHandler (from) = " + from.getClass() + "(" + from + ")";
            detail += ",\nIFluidHandler (to) = " + to.getClass() + "(" + to + ")";
            throw new IllegalStateException("Drained fluid did not equal expected fluid!\n" + detail);
        }
        int actuallyAccepted = to.fill(drained, FluidAction.SIMULATE);
        if (actuallyAccepted != accepted)
        {
            String detail = "(actually accepted = " + actuallyAccepted + ", accepted = " + accepted + ")";
            throw new IllegalStateException("Mismatched IFluidHandler implementations!\n" + detail);
        }
        return new FluidStack(drained.getFluid(), accepted);
    }

    @SuppressWarnings("unused")
	public static boolean onTankActivated(Player player, BlockPos pos, InteractionHand hand, IFluidHandler fluidHandler)
    {
        ItemStack held = player.getItemInHand(hand);
        if (held.isEmpty())
        {
            return false;
        }
        boolean replace = !player.isCreative();
        boolean single = held.getCount() == 1;
        IFluidHandlerItem flItem;
        if (replace && single)
        {
            flItem = FluidUtil.getFluidHandler(held).get();
        }
        else
        {
            ItemStack copy = held.copy();
            copy.setCount(1);
            flItem = FluidUtil.getFluidHandler(copy).get();
        }
        if (flItem == null)
        {
            return false;
        }
        Level world = player.level();
        if (world.isClientSide)
        {
            return true;
        }
        boolean changed = true;
        FluidStack moved;
        if ((moved = FluidsUtil.move(flItem, fluidHandler)) != null)
        {
//            UtilSound.playBucketEmpty(world, pos, moved);
        }
        else if ((moved = FluidsUtil.move(fluidHandler, flItem)) != null)
        {
//        	UtilSound.playBucketFill(world, pos, moved);
        }
        else
        {
            changed = false;
        }

        if (changed && replace)
        {
            if (single)
            {
                player.setItemInHand(hand, flItem.getContainer());
            }
            else
            {
                ItemHandlerHelper.giveItemToPlayer(player, flItem.getContainer());
            }
            player.getInventory().setChanged();
        }
        return true;
    }
}
