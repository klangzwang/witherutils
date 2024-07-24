package geni.witherutils.base.common.base;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.energy.IEnergyStorage;

public interface IWitherPoweredItem {

	public ICapabilityProvider<ItemStack, Void, IEnergyStorage> initEnergyCap();
	
	boolean consumeByActive();
	
    int getEnergyUse(ItemStack stack);

    int getMaxEnergy(ItemStack stack);
    
    int getMaxTransfer(ItemStack stack);
    
    boolean hasCharge(ItemStack stack);
    
    void consumeCharge(ItemStack stack);
    
    int getPowerLevel(ItemStack stack);
}
