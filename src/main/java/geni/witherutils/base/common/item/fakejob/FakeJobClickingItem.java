package geni.witherutils.base.common.item.fakejob;

import java.util.List;

import geni.witherutils.base.common.block.fakedriver.FakeDriverBlockEntity;
import geni.witherutils.core.common.util.FakePlayerUtil;
import geni.witherutils.core.common.util.ItemStackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

@SuppressWarnings("unused")
public class FakeJobClickingItem extends FakeJobAbstractItem {

	int speed = 0;
	int power = 0;
	int timer = 0;
	
	boolean rightClick = false;
	boolean sneak = false;
	
	@Override
	public void call(IFakeDriver tile)
	{
		Level level = tile.getFakeDriver().getLevel();
		BlockPos pos = tile.getFakeDriver().getBlockPos();

		fakePlayer = tile.getFakeDriver().getFakePlayer();
		inventory = tile.getFakeDriver().getInventory();
		
		fakeTool = inventory.getStackInSlot(1);
		
		Direction fakerFacing = fakePlayer.getDirection();
		Direction blockFacing = tile.getFakeDriver().getCurrentFacing();
		
		BlockPos target = pos.relative(tile.getFakeDriver().getCurrentFacing().getOpposite());
		
		if(fakePlayer != null && fakeTool != null)
		{
			AABB AABB = new AABB(target);
			List<LivingEntity> living = tile.getFakeDriver().getLevel().getEntitiesOfClass(LivingEntity.class, AABB);
			
			if(!rightClick)
			{
			    if(living.size() == 0)
			    {
			    	tile.getFakeDriver().setLitProperty(false);
			    	timer = 100;
			        return;
			    }
			}

		    if(speed <= 0 && power > 0)
		    {
		    	timer = 0;
		    	tile.getFakeDriver().setLitProperty(true);
		    	return;
		    }
		    
			timer -= speed;
			
			if(timer > 0)
			{
				tile.getFakeDriver().setLitProperty(false);
				return;
			}
			else
			{
				tile.getFakeDriver().setLitProperty(true);
				timer = 100;
			}

			try
			{
				var cooldowns = fakePlayer.getCooldowns();
				tile.getFakeDriver().tryEquipItem(fakeTool, InteractionHand.MAIN_HAND);
				var item = fakePlayer.getItemInHand(InteractionHand.MAIN_HAND).getItem();
				if (cooldowns.isOnCooldown(item))
					cooldowns.removeCooldown(item);
			}
			catch (Exception e) {}

			System.out.println("TEST");
			
//			if(rightClick)
//			{
//				try
//				{
//					ItemStack result = FAKETOOL.getItemStack(inventory);
//					if(result.isEmpty())
//						return;
//
//					if(result.getItem() instanceof BlockItem)
//					{
//						BlockEntity be = level.getBlockEntity(target);
//						if(be != null)
//						{
//							IItemHandler itemHandler =  level.getCapability(Capabilities.ItemHandler.BLOCK, tile.getFakeDriver().getBlockPos());
//							if(itemHandler != null)
//							{
//								ItemStack stack = inventory.extractItem(0, 1, false).split(1);
//								ItemHandlerHelper.insertItem(itemHandler, stack, false);
//							}
//						}
//					}
//					else if(result.getItem() instanceof Item)
//					{
//						FakePlayerUtil.setupFakePlayerForUse(fakePlayer, pos, tile.getFakeDriver().getCurrentFacing().getOpposite(), fakeTool.copy(), sneak);
//						result = FakePlayerUtil.rightClickInDirection(fakePlayer, level, pos, tile.getFakeDriver().getCurrentFacing().getOpposite(), level.getBlockState(pos));
//					}
//					
//					this.extinguishFires(target, facing);
//					
//					level.getEntities(fakePlayer, AABB, EntitySelector.NO_SPECTATORS).forEach((entityFound) -> {
//
//				        InteractionResult res = fakePlayer.interactOn(entityFound, InteractionHand.MAIN_HAND);
//				        if (res.consumesAction())
//				        {
////				        	WitherUtils.LOGGER.info(worldPosition + "| entity consume result detected " + res);
//				        }
//					});
//
//                    FakePlayerUtil.cleanupFakePlayerFromUse(fakePlayer, result, INPUT.getItemStack(inventory), this);
//                    tile.getFakeDriver().setChanged();
//				}
//				catch(Exception e)
//				{
//				}
//			}
//			else
//			{
//				try
//				{
//					FakePlayerUtil.setupFakePlayerForUse(this.fakePlayer, pos, tile.getFakeDriver().getCurrentFacing(), fakeTool.copy(), sneak);
//					ItemStack result = INPUT.getItemStack(inventory);
//					result = FakePlayerUtil.leftClickInDirection(fakePlayer, level, pos, tile.getFakeDriver().getCurrentFacing(), level.getBlockState(pos));
//					
//					level.getEntities(fakePlayer, AABB, EntitySelector.NO_SPECTATORS).forEach((entityFound) -> {
//						
////						WitherUtils.LOGGER.info(worldPosition + "| ??   " + fakePlayer.get().getMainHandItem());
////						WitherUtils.LOGGER.info(worldPosition + "| interactEntities ATTACK  " + entityFound);
//						
//						fakePlayer.attack(entityFound);
//					});
//					
//					ItemStackUtil.damageItem(inventory.getStackInSlot(0));
//					
////                    FakePlayerUtil.cleanupFakePlayerFromUse(fakePlayer, result, FakeDriverBlockEntity.FAKETOOL.getItemStack(inventory), tile.getFakeDriver());
//            		tile.getFakeDriver().setChanged();
//				}
//				catch(Exception e)
//				{
//				}
//			}
			
			fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
		}
	}
	
	public InteractionResult interactAtBlock(Block block, InteractionHand hand)
	{
		return InteractionResult.PASS;
	}
	
	public int getTimer()
	{
		return timer;
	}
	public int getPower()
	{
		return power;
	}
	public void setPower(int power, IFakeDriver tile)
	{
		this.power = power;
		tile.getFakeDriver().setChanged();
	}
	public boolean getRightClick()
	{
		return rightClick;
	}
	public void setRightClick(boolean rightClick, IFakeDriver tile)
	{
		this.rightClick = rightClick;
		tile.getFakeDriver().setChanged();
	}
	public boolean getSneak()
	{
		return sneak;
	}
	public void setSneak(boolean sneak, IFakeDriver tile)
	{
		this.sneak = sneak;
		tile.getFakeDriver().setChanged();
	}
	public int getSpeed()
	{
		return speed;
	}
	public void setSpeed(int speed, IFakeDriver tile)
	{
		this.speed = speed;
		tile.getFakeDriver().setChanged();
	}
	public boolean isCrouching()
	{
		return sneak;
	}
	public void setCrouching(boolean sneak, IFakeDriver tile)
	{
		this.sneak = sneak;
		tile.getFakeDriver().setChanged();
	}
	public boolean isRightClicking()
	{
		return rightClick;
	}
	public void setRightClicking(boolean rightClick, IFakeDriver tile)
	{
		this.rightClick = rightClick;
		tile.getFakeDriver().setChanged();
	}

	private void extinguishFires(Level level, BlockPos pos, Direction facing)
	{
		BlockState blockstate = level.getBlockState(pos);
		if (blockstate.is(BlockTags.FIRE))
		{
			level.removeBlock(pos, false);
		}
		else if(CampfireBlock.isLitCampfire(blockstate))
		{
			CampfireBlock.isLitCampfire(blockstate);
			level.setBlock(pos, blockstate.setValue(CampfireBlock.LIT, Boolean.valueOf(false)), 3);
		}
	}

	@Override
	public boolean trySetupFakeTool(ItemStack stack)
	{
		fakePlayer.setItemInHand(fakePlayer.getUsedItemHand(), stack);
		return fakePlayer.getItemInHand(fakePlayer.getUsedItemHand()) == stack;
	}
	
	@Override
	public FakeJobAbstractItem getFakeJob()
	{
		return this;
	}

	@Override
	public float[] getColor()
	{
        return new float[]{0F, 255F, 0F};
	}
	
	@Override
	public int setInteractTimer()
	{
		return 15;
	}

	@Override
	public void checkRedstone(Level world, BlockPos pos, IFakeDriver tile) {
		// TODO Auto-generated method stub
		
	}
}
