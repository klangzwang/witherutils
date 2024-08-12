package geni.witherutils.base.common.item.remote;

import geni.witherutils.base.common.base.WitherItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class RemoteItem extends WitherItem {
	
	public RemoteItem()
	{
		super(new Item.Properties().stacksTo(1));
	}

	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context)
	{
		return activateTelevision(context.getLevel(), context.getPlayer());
	}

	public InteractionResult activateTelevision(Level worldIn, Player playerIn)
    {
        BlockHitResult result = getPlayerPOVHitResult(worldIn, playerIn, ClipContext.Fluid.SOURCE_ONLY);
        if(result != null && result.getType() == HitResult.Type.BLOCK)
        {
//			BlockPos pos = result.getBlockPos();
//			BlockEntity tileEntity = worldIn.getBlockEntity(pos);
//			if (tileEntity instanceof SmartTVBlockEntity)
//			{
//				SmartTVBlockEntity tvbe = (SmartTVBlockEntity) tileEntity;
//				if (!playerIn.isCrouching())
//				{
//					tvbe.onBlockUse(tvbe.getBlockState(), playerIn, playerIn.getUsedItemHand(), result);
//					tvbe.setPowered(!tvbe.isPowered());
//					worldIn.playSound(null, pos, WUTSounds.NOISE.get(), SoundSource.BLOCKS, 0.5F, 1.0F);
//				}
////				tvbe.nextChannel()
//				return InteractionResult.SUCCESS;
//			}
        }
        return InteractionResult.PASS;
	}

	@Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
    	return new InteractionResultHolder<>(activateTelevision(level, player), player.getItemInHand(hand));
    }

//    @SuppressWarnings("resource")
//	@Override
//    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag)
//    {
//    	super.appendHoverText(stack, world, list, flag);
//    	String info = I18n.get("Allows you to wirelessly control a TV");
//    	list.add(Component.literal(ChatFormatting.YELLOW + I18n.get(Minecraft.getInstance().font.plainSubstrByWidth(info, 150))));
//    	list.add(Component.literal(ChatFormatting.YELLOW + I18n.get("Remote")));
//    }
}