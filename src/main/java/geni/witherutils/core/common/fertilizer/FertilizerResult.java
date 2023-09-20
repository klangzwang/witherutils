package geni.witherutils.core.common.fertilizer;

import javax.annotation.Nonnull;

import geni.witherutils.api.farm.IFertilizerResult;
import net.minecraft.world.item.ItemStack;

public class FertilizerResult implements IFertilizerResult {

	private final @Nonnull ItemStack stack;
	private final boolean wasApplied;

	public FertilizerResult(@Nonnull ItemStack stack, boolean wasApplied)
	{
		this.stack = stack;
		this.wasApplied = wasApplied;
	}

	@Override
	public @Nonnull ItemStack getStack()
	{
		return stack;
	}

	@Override
	public boolean wasApplied()
	{
		return wasApplied;
	}
}
