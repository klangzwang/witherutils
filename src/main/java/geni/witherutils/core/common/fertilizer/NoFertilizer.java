package geni.witherutils.core.common.fertilizer;

import javax.annotation.Nonnull;

import geni.witherutils.api.farm.IFertilizer;
import geni.witherutils.api.farm.IFertilizerResult;
import geni.witherutils.core.common.helper.NullHelper;
import geni.witherutils.core.common.util.NNList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class NoFertilizer extends AbstractFertilizer {

	static private IFertilizer NONE;

	public static @Nonnull IFertilizer getNone()
	{
		return NullHelper.notnull(NONE, "fertilizing before game has started error");
	}

	private NoFertilizer()
	{
		super(ItemStack.EMPTY);
	}

	@Override
	public IFertilizerResult apply(@Nonnull ItemStack stack, @Nonnull Player player, @Nonnull Level level, @Nonnull BlockPos bc)
	{
		return new FertilizerResult(stack, false);
	}

	@Override
	public boolean matches(@Nonnull ItemStack stack)
	{
		return false;
	}

	@Override
	@Nonnull
	public NonNullList<ItemStack> getGuiItem()
	{
		return NNList.emptyList();
	}
}
