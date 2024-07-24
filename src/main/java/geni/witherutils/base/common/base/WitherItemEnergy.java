package geni.witherutils.base.common.base;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;

public class WitherItemEnergy {

    public static int getMaxEnergyStored(ItemStack stack)
    {
        var energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        return energyStorage != null ? energyStorage.getMaxEnergyStored() : 0;
    }
    public static int getEnergyStored(ItemStack stack)
    {
        var energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        return energyStorage != null ? energyStorage.getEnergyStored() : 0;
    }
    public static boolean hasEnergy(ItemStack stack, int amount)
    {
        var energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        return energyStorage != null && energyStorage.getEnergyStored() >= amount;
    }
    public static void setFull(ItemStack stack)
    {
        var energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energyStorage != null) {
            energyStorage.receiveEnergy(energyStorage.getMaxEnergyStored(), false);
        }
    }
    public static void setEmpty(ItemStack stack)
    {
        var energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energyStorage != null)
        {
            energyStorage.extractEnergy(energyStorage.getEnergyStored(), false);
        }
    }
    public static void set(ItemStack stack, int energy)
    {
        var energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energyStorage != null)
        {
            int delta = energy - energyStorage.getEnergyStored();
            if (delta < 0)
            {
                energyStorage.extractEnergy(-delta, false);
            }
            else
            {
                energyStorage.receiveEnergy(delta, false);
            }
        }
    }
    public static int receiveEnergy(ItemStack stack, int maxReceive, boolean simulate)
    {
        var energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        return energyStorage != null ? energyStorage.receiveEnergy(maxReceive, simulate) : 0;
    }
    public static int extractEnergy(ItemStack stack, int maxExtract, boolean simulate)
    {
        var energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        return energyStorage != null ? energyStorage.extractEnergy(maxExtract, simulate) : 0;
    }
}
