package geni.witherutils.core.common.fertilizer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import geni.witherutils.api.farm.IFertilizer;
import geni.witherutils.api.farm.IFertilizerResult;
import geni.witherutils.core.common.util.NNList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class AbstractFertilizer implements IFertilizer {

	protected final @Nonnull ItemStack fertilizer;

	protected AbstractFertilizer(@Nonnull ItemStack stack)
	{
		fertilizer = stack;
		if(stack.is(fertilizer.getItem()))
		{
			ResourceKey.create(ForgeRegistries.ITEMS.getRegistryKey(), ForgeRegistries.ITEMS.getRegistryName());
		}
	}
	protected AbstractFertilizer(@Nullable Item item)
	{
		this(item == null ? ItemStack.EMPTY : new ItemStack(item));
	}
	protected AbstractFertilizer(@Nullable Block block)
	{
		this(block == null ? ItemStack.EMPTY : new ItemStack(block));
	}

	public boolean isValid()
	{
		return !fertilizer.isEmpty();
	}

	@Override
	public boolean matches(@Nonnull ItemStack stack)
	{
		return ItemStack.matches(fertilizer, stack);
	}

	@Override
	public abstract IFertilizerResult apply(@Nonnull ItemStack stack, @Nonnull Player player, @Nonnull Level world, @Nonnull BlockPos bc);

	@Override
	public boolean applyOnAir()
	{
		return false;
	}

	@Override
	public boolean applyOnPlant()
	{
		return true;
	}

	@Override
	@Nonnull
	public NonNullList<ItemStack> getGuiItem()
	{
		return new NNList<>(fertilizer);
	}
}
