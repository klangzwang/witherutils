package geni.witherutils.core.common.util;

import java.util.function.ToIntFunction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import geni.witherutils.base.common.io.energy.WitherEnergyStorage;
import geni.witherutils.core.common.helper.ItemNBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyUtil {
	
	public static final String ENERGY_KEY = "energy";
	
	@Nullable
	public static IEnergyStorage getEnergy(ItemStack stack) {
		if (stack == null) return null;
		return stack.getCapability(ForgeCapabilities.ENERGY).orElse(null);
	}
	@Nullable
	public static boolean hasEnergyHandler(ItemStack stack) {
		LazyOptional<IEnergyStorage> cap = stack.getCapability(ForgeCapabilities.ENERGY);
		return cap != null;
	}

    public static int getMaxEnergyStored(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.ENERGY).map(IEnergyStorage::getMaxEnergyStored).orElse(0);
    }
    public static int getEnergyStored(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
    }
    public static boolean hasEnergy(ItemStack stack, int amount) {
        return stack.getCapability(ForgeCapabilities.ENERGY).map(storage -> storage.getEnergyStored() >= amount).orElse(false);
    }
    public static void setFull(ItemStack stack) {
        stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(energyStorage -> energyStorage.receiveEnergy(energyStorage.getMaxEnergyStored(), false));
    }
    public static void setEmpty(ItemStack stack) {
        stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(energyStorage -> energyStorage.extractEnergy(energyStorage.getEnergyStored(), false));
    }

    public static void set(ItemStack stack, int energy) {
        stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(energyStorage -> {
            int delta = energy - energyStorage.getEnergyStored();
            if (delta < 0) {
                energyStorage.extractEnergy(-delta, false);
            } else {
                energyStorage.receiveEnergy(delta, false);
            }
        });
    }

    public static int receiveEnergy(ItemStack stack, int maxReceive, boolean simulate) {
        return stack.getCapability(ForgeCapabilities.ENERGY).map(energyStorage -> energyStorage.receiveEnergy(maxReceive, simulate)).orElse(0);
    }
    public static int extractEnergy(ItemStack stack, int maxExtract, boolean simulate) {
        return stack.getCapability(ForgeCapabilities.ENERGY).map(energyStorage -> energyStorage.extractEnergy(maxExtract, simulate)).orElse(0);
    }
    public static long calculateSunEnergy(Level level, BlockPos pos)
    {
    	long baseRatio = calculateLightRatio(level, pos);
        return calculateLocalLightRatio(level, pos, baseRatio);
    }
    static boolean isSolarPowered(@Nonnull Level level, BlockPos pos)
    {
    	return level.canSeeSky(pos.above());
    }
	static long calculateLocalLightRatio(@Nonnull Level level, BlockPos pos, long baseRatio)
	{
		long ratio = isSolarPowered(level, pos) ? baseRatio : 0;
		return ratio;
	}
	public static long calculateLightRatio(@Nonnull Level level, BlockPos pos)
	{
		long lightValue = level.getBrightness(LightLayer.SKY, pos) - level.getSkyDarken();
		float sunAngle = level.getSunAngle(1.0F);
		if(sunAngle < (float) Math.PI)
		{
			sunAngle += (0.0F - sunAngle) * 0.2F;
		}
		else
		{
			sunAngle += (((float) Math.PI * 2F) - sunAngle) * 0.2F;
		}
		lightValue = Math.round(lightValue * Mth.cos(sunAngle));
		lightValue = (long) Mth.clamp(lightValue, 0, 15);

		return lightValue / 4 * 2;
	}
    public static boolean isEnergyTE(BlockEntity te, @Nullable Direction side)
    {
        if (te == null)
        {
            return false;
        }
        return te.getCapability(ForgeCapabilities.ENERGY, side).isPresent();
    }
    public static int receiveEnergy(BlockEntity tileEntity, Direction from, int maxReceive)
    {
        if (tileEntity != null) {
            return tileEntity.getCapability(ForgeCapabilities.ENERGY, from).map(handler ->
                    handler.receiveEnergy(unsignedClampToInt(maxReceive), false)).orElse(0);
        }
        return 0;
    }
//    public static int receiveEnergy(ItemStack stack, int maxReceive)
//    {
//        Item item = stack.getItem();
//        if (item instanceof IEnergyItemProvider)
//        {
//            return ((IEnergyItemProvider)item).receiveEnergy(stack, maxReceive, false);
//        }
//        else
//        {
//            return stack.getCapability(ForgeCapabilities.ENERGY).map(handler ->
//                    handler.receiveEnergy(unsignedClampToInt(maxReceive), false)).orElse(0);
//        }
//    }
    public static int unsignedClampToInt(long l)
    {
        return l > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)l;
    }
    public static void handleSendingEnergy(Level world, BlockPos pos, int storedPower, float sendPerTick, WitherEnergyStorage storage)
    {
        for (Direction facing : FacingUtil.FACES_AROUND_X)
        {
            BlockPos p = pos.relative(facing);
            BlockEntity te = world.getBlockEntity(p);
            Direction opposite = facing.getOpposite();
            if (EnergyUtil.isEnergyTE(te, opposite))
            {
                int rfToGive = (int) Math.min(sendPerTick, storedPower);
                int received = EnergyUtil.receiveEnergy(te, opposite, rfToGive);
                storage.consumeEnergy(received, false);
                storedPower -= received;
                if (storedPower <= 0)
                {
                    break;
                }
            }
        }
    }
    
	public static class ItemEnergyStorage implements IEnergyStorage
	{
		private final ItemStack stack;
		private final ToIntFunction<ItemStack> getCapacity;

		public ItemEnergyStorage(ItemStack item, ToIntFunction<ItemStack> getCapacity)
		{
			this.stack = item;
			this.getCapacity = getCapacity;
		}

		@Override
		public int receiveEnergy(int maxReceive, boolean simulate)
		{
			int stored = getEnergyStored();
			int accepted = Math.min(maxReceive, getMaxEnergyStored()-stored);
			if(!simulate)
			{
				stored += accepted;
				ItemNBTHelper.putInt(stack, ENERGY_KEY, stored);
			}
			return accepted;
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate)
		{
			int stored = getEnergyStored();
			int extracted = Math.min(maxExtract, stored);
			if(!simulate)
			{
				stored -= extracted;
				ItemNBTHelper.putInt(stack, ENERGY_KEY, stored);
			}
			return extracted;
		}

		@Override
		public int getEnergyStored()
		{
			return ItemNBTHelper.getInt(stack, ENERGY_KEY);
		}

		@Override
		public int getMaxEnergyStored()
		{
			return getCapacity.applyAsInt(stack);
		}

		@Override
		public boolean canExtract()
		{
			return true;
		}

		@Override
		public boolean canReceive()
		{
			return true;
		}
	}
}
