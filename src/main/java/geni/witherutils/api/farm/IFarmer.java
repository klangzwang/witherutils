package geni.witherutils.api.farm;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayer;

public interface IFarmer {

	@Nonnull
	ItemStack getSeedTypeInSuppliesFor(@Nonnull BlockPos pos);

	@Nonnull
	WeakReference<FakePlayer> getFakePlayer();

	@Nonnull
	Level getWorld();

	@Nonnull
	BlockPos getLocation();

	@Nonnull
	ItemStack takeSeedFromSupplies(@Nonnull ItemStack seeds, @Nonnull BlockPos pos, boolean simulate);

	default @Nonnull ItemStack takeSeedFromSupplies(@Nonnull BlockPos pos, boolean simulate) {
		return takeSeedFromSupplies(ItemStack.EMPTY, pos, simulate);
	}

	default @Nonnull ItemStack takeSeedFromSupplies(@Nonnull BlockPos pos) {
		return takeSeedFromSupplies(pos, false);
	}

	boolean hasTool(@Nonnull IFarmingTool tool);

	void setNotification(@Nonnull FarmNotification notification);

	int getLootingValue(@Nonnull IFarmingTool tool);

	@Nonnull
	BlockState getBlockState(@Nonnull BlockPos pos);

	@Nonnull
	ItemStack getTool(@Nonnull IFarmingTool tool);

	@Nonnull
	FakePlayer startUsingItem(@Nonnull ItemStack stack);

	@Nonnull
	FakePlayer startUsingItem(@Nonnull IFarmingTool tool);

	@Nonnull
	NonNullList<ItemStack> endUsingItem(boolean trashHandItem);

	@Nonnull
	NonNullList<ItemStack> endUsingItem(@Nonnull IFarmingTool tool);

	void handleExtraItems(@Nonnull NonNullList<ItemStack> items, @Nullable BlockPos pos);

	boolean checkAction(@Nonnull FarmingAction action, @Nonnull IFarmingTool tool);

	void registerAction(@Nonnull FarmingAction action, @Nonnull IFarmingTool tool);

	void registerAction(@Nonnull FarmingAction action, @Nonnull IFarmingTool tool, @Nonnull BlockState state, @Nonnull BlockPos pos);

	boolean hasSeed(@Nonnull ItemStack seeds, @Nonnull BlockPos pos);

	boolean tillBlock(@Nonnull BlockPos pos);

	int isLowOnSaplings(@Nonnull BlockPos pos);

	boolean isSlotLocked(@Nonnull BlockPos pos);

	int getFarmSize();

	void handleExtraItem(@Nonnull ItemStack stack, @Nullable BlockPos drop);

}
