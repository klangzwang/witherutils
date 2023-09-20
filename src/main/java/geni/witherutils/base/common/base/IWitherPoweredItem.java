package geni.witherutils.base.common.base;

import net.minecraft.world.item.ItemStack;

public interface IWitherPoweredItem {

	boolean consumeByActive();
	
    int getEnergyUse(ItemStack stack);

    int getMaxEnergy(ItemStack stack);
    
    int getMaxTransfer(ItemStack stack);
    
    boolean hasCharge(ItemStack stack);
    
    void consumeCharge(ItemStack stack);
    
    int getPowerLevel(ItemStack stack);
}
