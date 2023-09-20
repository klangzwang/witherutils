package geni.witherutils.api.farm;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IFertilizer {

	IFertilizerResult apply(@Nonnull ItemStack stack, @Nonnull Player player, @Nonnull Level level, @Nonnull BlockPos pos);

	boolean applyOnAir();

	boolean applyOnPlant();

	boolean matches(@Nonnull ItemStack stack);

	default @Nonnull NonNullList<ItemStack> getGuiItem()
	{
		return NonNullList.create();
	}
}
