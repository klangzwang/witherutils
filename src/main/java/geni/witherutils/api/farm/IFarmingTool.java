package geni.witherutils.api.farm;

import javax.annotation.Nonnull;

import net.minecraft.world.item.ItemStack;

public interface IFarmingTool {

	public static class Tools
	{
		public static IFarmingTool HAND = null, HOE = null, AXE = null, TREETAP = null, SHEARS = null, NONE = null;
	}

	boolean itemMatches(@Nonnull ItemStack item);
}
