package geni.witherutils.base.common.item.fakejob;

import geni.witherutils.core.common.util.FakePlayerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;

@SuppressWarnings("unused")
public class FakeJobPlacingItem extends FakeJobAbstractItem {

	private boolean range = false;

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
		
		if(fakePlayer != null && fakeTool != null)
		{
			if(!canBlockBeBroken(level, pos.relative(blockFacing, 1)))
			{
//				FakePlayerUtil.setupFakePlayerForUse(fakePlayer, pos, blockFacing, fakeTool.copy(), false);

				if(!range && fakeTool.getCount() > 0)
				{
					tile.getFakeDriver().setLitProperty(true);
					place(pos.relative(blockFacing, 1), fakeTool);
				}
				if(range && fakeTool.getCount() > 4)
				{
					for(int i = 0; i < 5; i++)
					{
						BlockPos coordsBlock = pos.relative(blockFacing, i + 1);
						place(coordsBlock, fakeTool);
					}
				}
			}
		}
	}

    public boolean canBlockBeBroken(Level world, BlockPos pos)
    {
        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, pos, world.getBlockState(pos), fakePlayer);
        NeoForge.EVENT_BUS.post(event);
        return event.isCanceled();
    }

    public boolean place(BlockPos pos, ItemStack stack)
    {
		if(trySetupFakeTool(stack))
			return CommonHooks.onPlaceItemIntoWorld(
					new UseOnContext(fakePlayer, InteractionHand.MAIN_HAND, new BlockHitResult(new Vec3(0, 0, 0), Direction.DOWN, pos, false))
					) == InteractionResult.SUCCESS;
		else
			return false;
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

	public boolean getRange()
	{
		return range;
	}
	
    public void setRange(boolean range, IFakeDriver tile)
    {
    	this.range = range;
    	tile.getFakeDriver().setChanged();
    }
    
	@Override
	public int setInteractTimer()
	{
		return 60;
	}

	@Override
	public void checkRedstone(Level world, BlockPos pos, IFakeDriver tile) {
		// TODO Auto-generated method stub
		
	}
}
