package geni.witherutils.base.common.item.fakejob;

import geni.witherutils.core.common.lib.LogicSupport;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;

@SuppressWarnings("unused")
public class FakeJobActivatingItem extends FakeJobAbstractItem {

	protected final LogicSupport support = new LogicSupport();
	private	int power;

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
			tile.getFakeDriver().setLitProperty(true);
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

	public int getPower()
	{
		return power;
	}
	
    public void setRange(int power, IFakeDriver tile)
    {
    	this.power = power;
    	tile.getFakeDriver().setChanged();
    }

	@Override
	public int setInteractTimer()
	{
		return 20;
	}

	@SuppressWarnings("deprecation")
	public void checkRedstone(Level world, BlockPos pos, IFakeDriver tile)
	{
        support.checkRedstone(tile.getFakeDriver(), world, pos);
        if (loopDetector <= 0)
        {
            loopDetector++;
            BlockState state = world.getBlockState(pos);
            BlockPos offsetPos = pos.relative(LogicSupport.getFacing(state).getOpposite());
            if (world.hasChunkAt(offsetPos))
            {
                world.neighborChanged(offsetPos, state.getBlock(), pos);
            }
            loopDetector--;
        }		
	}
}
