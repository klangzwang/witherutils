package geni.witherutils.base.common.item.fakejob;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FakeJobScanningItem extends FakeJobAbstractItem {

	@Override
	public void call(IFakeDriver tile) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean trySetupFakeTool(ItemStack stack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public FakeJobAbstractItem getFakeJob() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float[] getColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int setInteractTimer() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void checkRedstone(Level world, BlockPos pos, IFakeDriver tile) {
		// TODO Auto-generated method stub
		
	}

}
