package geni.witherutils.core.common.fertilizer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import geni.witherutils.api.farm.IFertilizerResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class Bonemeal extends AbstractFertilizer {

	public Bonemeal(@Nonnull ItemStack stack)
	{
		super(stack);
	}

	public Bonemeal(@Nullable Block block)
	{
		super(block);
	}

	public Bonemeal(@Nullable Item item)
	{
		super(item);
	}

	@Override
	public IFertilizerResult apply(@Nonnull ItemStack stack, @Nonnull Player player, @Nonnull Level world, @Nonnull BlockPos bc)
	{
		ItemStack before = player.getItemInHand(InteractionHand.MAIN_HAND);
		player.setItemInHand(InteractionHand.MAIN_HAND, stack);
		InteractionResult res = stack.getItem().useOn(new UseOnContext(world, player, InteractionHand.MAIN_HAND, before, null));
		ItemStack after = player.getItemInHand(InteractionHand.MAIN_HAND);
		player.setItemInHand(InteractionHand.MAIN_HAND, before);
		return new FertilizerResult(after, res != InteractionResult.PASS);
	}
}
