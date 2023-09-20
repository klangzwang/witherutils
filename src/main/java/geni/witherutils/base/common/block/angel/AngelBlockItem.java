package geni.witherutils.base.common.block.angel;

import javax.annotation.Nonnull;

import geni.witherutils.core.common.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class AngelBlockItem extends BlockItem {

	public AngelBlockItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		ItemStack itemstack = player.getItemInHand(hand);
		if(level.isClientSide)
			return InteractionResultHolder.success(itemstack);

		int x = (int) (player.getX() + 3 * player.getLookAngle().x);
		int y = (int) (1.5 + player.getY() + 3 * player.getLookAngle().y);
		int z = (int) (player.getZ() + 3 * player.getLookAngle().z);

		BlockPos pos = new BlockPos(x, y, z);

		if(level.getBlockState(pos).isAir() || !level.getFluidState(pos).isEmpty())
		{
			level.setBlock(pos, getBlock().defaultBlockState(), 2);
	        if(!player.isCreative())
	        	itemstack.shrink(1);
	        SoundUtil.playSoundFromServer((ServerPlayer) player, SoundEvents.BONE_BLOCK_PLACE, 1.0f, 1.0f);
		}
		return InteractionResultHolder.success(itemstack);
	}
}
