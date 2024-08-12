package geni.witherutils.base.common.item.bucket;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;

import geni.witherutils.base.common.init.WUTItems;

public class FluidBucketItem extends BucketItem implements IFluidCapProvider {
	
    public FluidBucketItem(Fluid fluid)
    {
        super(fluid, WUTItems.filledBucketProps());
    }

    @Override
    public IFluidHandlerItem provideFluidCapability(ItemStack stack)
    {
        return new FluidBucketWrapper(stack);
    }
}
