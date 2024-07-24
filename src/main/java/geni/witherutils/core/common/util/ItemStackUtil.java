package geni.witherutils.core.common.util;

import java.util.List;
import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.IItemHandler;

public class ItemStackUtil {

	private static final Random RANDOM_GENERATOR = new Random();

//    @Nonnull
//    public static ItemStack insertItemMultiSlot(MachineInventory inventory, @Nonnull ItemStack stack, MultiSlotAccess outputs)
//    {
//        if(inventory == null || stack.isEmpty())
//            return stack;
//
//        for (SingleSlotAccess access : outputs.getAccesses())
//        {
//        	ItemStack slot = inventory.getStackInSlot(access.getIndex());
//        	if(ItemHandlerHelper.insertItemStacked(slot, stack, false))
//        	{
//        		stack = inventory.insertItem(access.getIndex(), stack, false);
//        		if(stack.isEmpty())
//        		{
//        			break;
//        		}
//        	}
//        }
//        if(!stack.isEmpty())
//        {
//        	for (SingleSlotAccess access : outputs.getAccesses())
//            {
//                if(inventory.getStackInSlot(access.getIndex()).isEmpty())
//                {
//                    stack = inventory.insertItem(access.getIndex(), stack, false);
//                    if(stack.isEmpty())
//                    {
//                        break;
//                    }
//                }
//            }
//        }
//        return stack;
//    }

	public static void dropAll(IItemHandler items, Level world, BlockPos pos)
	{
		if(items == null)
		{
			return;
		}
		for(int i = 0; i < items.getSlots(); i++)
		{
			Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), items.getStackInSlot(i));
		}
	}

	public static void repairItem(ItemStack s)
	{
		repairItem(s, 1);
	}

	public static void repairItem(ItemStack s, int amount)
	{
		s.setDamageValue(Math.max(0, s.getDamageValue() - amount));
	}

	public static void damageItem(ItemStack s)
	{
		if(s.getItem() instanceof BlockItem)
			return;
		s.setDamageValue(s.getDamageValue() + 1);
		if(s.getDamageValue() >= s.getMaxDamage())
		{
			s.shrink(1);
		}
	}

	public static void damageItem(LivingEntity player, ItemStack stack)
	{
//		stack.hurtAndBreak(1, player, (p) -> {
//			p.broadcastBreakEvent(InteractionHand.MAIN_HAND);
//		});
		if(stack.getDamageValue() >= stack.getMaxDamage())
		{
			stack.setCount(0);
			stack = ItemStack.EMPTY;
		}
	}

	public static void damageItemRandomly(LivingEntity player, ItemStack stack)
	{
		if(player.level().getRandom().nextDouble() < 0.001)
		{
			damageItem(player, stack);
		}
	}

	public static void drop(Level world, BlockPos pos, Block drop)
	{
		if(!world.isClientSide)
		{
			world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(drop.asItem())));
		}
	}

	public static void drop(Level world, BlockPos pos, ItemStack drop)
	{
		if(!world.isClientSide)
		{
			double d0 = world.random.nextFloat() * 0.5F + 0.25D;
			double d1 = world.random.nextFloat() * 0.5F + 0.25D;
			double d2 = world.random.nextFloat() * 0.5F + 0.25D;

			ItemEntity entityitem = new ItemEntity(world, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, drop);

			entityitem.setDefaultPickUpDelay();
//			entityitem.hurt(world.damageSources().inFire(), -100);
//			entityitem.setSecondsOnFire(10);

			world.addFreshEntity(entityitem);
//			world.playSound((Player) null, pos, SoundEvents.FIREWORK_ROCKET_LARGE_BLAST, SoundSource.BLOCKS, 1.0F, world.random.nextFloat() * 0.4F + 0.8F);
		}
	}

	public static boolean matches(ItemStack current, ItemStack in)
	{
		return ItemStack.isSameItem(current, in) && ItemStack.matches(current, in);
	}

	public static void shrink(Player player, ItemStack stac)
	{
		if(!player.isCreative())
		{
			stac.shrink(1);
		}
	}

	public static void drop(Level world, BlockPos center, List<ItemStack> lootDrops)
	{
		for(ItemStack dropMe : lootDrops)
		{
			ItemStackUtil.drop(world, center, dropMe);
		}
	}

	public static void dropItemStackMotionless(Level world, BlockPos pos, ItemStack stack)
	{
		if(stack.isEmpty())
		{
			return;
		}
		if(world.isClientSide == false)
		{
			ItemEntity entityItem = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
			world.addFreshEntity(entityItem);
			entityItem.setDeltaMovement(0, 0, 0);
		}
	}

	public static void deleteTag(ItemStack itemstack)
	{
		int dmg = itemstack.getDamageValue();
//		itemstack.setTag(null);
		itemstack.setDamageValue(dmg);
	}

	public static void spawnDrops(final Level level, final BlockPos pos, final List<ItemStack> drops)
	{
		if(!level.isClientSide()) {
			for(final ItemStack i : drops) {
				if(!i.isEmpty() && i.getCount() > 0) {
					final double offset_x = (getRandomInt() % 32 - 16) / 82;
					final double offset_y = (getRandomInt() % 32 - 16) / 82;
					final double offset_z = (getRandomInt() % 32 - 16) / 82;
					final ItemEntity ei = new ItemEntity(level, 0.5 + offset_x + pos.getX(),
							0.5 + offset_y + pos.getY(), 0.2 + offset_z + pos.getZ(), i.copy());
					level.addFreshEntity(ei);
				}
			}
		}
	}

	public static int getRandomInt()
	{
		return Math.abs(RANDOM_GENERATOR.nextInt());
	}
}
