package geni.witherutils.api.farm;

import javax.annotation.Nonnull;

import net.minecraft.world.item.ItemStack;

public interface IFertilizerResult {

	@Nonnull
	ItemStack getStack();

	boolean wasApplied();
}
