package geni.witherutils.base.common.item.cutter;

import geni.witherutils.base.common.base.WitherItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;

public class CutterItem extends WitherItem {

	public CutterItem()
	{
		super(new Item.Properties().stacksTo(1).durability(250));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn)
	{
		if(!worldIn.isClientSide && !playerIn.isCrouching())
		{
			NetworkHooks.openScreen((ServerPlayer) playerIn, new CutterContainerProvider(), playerIn.blockPosition());
		}
		return super.use(worldIn, playerIn, handIn);
	}

	@Override
	public int getEnchantmentValue(ItemStack stack)
	{
		return 0;
	}
	@Override
	public boolean isEnchantable(ItemStack p_41456_)
	{
		return false;
	}
	@Override
	public int getUseDuration(ItemStack itemstack)
	{
		return 0;
	}
	@Override
	public float getDestroySpeed(ItemStack par1ItemStack, BlockState par2Block)
	{
		return 1F;
	}
}
