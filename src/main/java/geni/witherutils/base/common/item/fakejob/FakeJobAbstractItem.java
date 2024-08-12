package geni.witherutils.base.common.item.fakejob;

import geni.witherutils.base.common.base.WitherItem;
import geni.witherutils.base.common.io.item.MachineInventory;
import geni.witherutils.core.common.fakeplayer.WUTFakePlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class FakeJobAbstractItem extends WitherItem implements IFakeJobItem {
	
	public WUTFakePlayer fakePlayer;
	public MachineInventory inventory;
	
	public ItemStack fakeTool;
	
	public int loopDetector = 0;
	
    public FakeJobAbstractItem()
    {
        super(new Properties().stacksTo(1));
    }
    
    public abstract void call(IFakeDriver tile);
    
    public abstract boolean trySetupFakeTool(ItemStack stack);
    
    public abstract FakeJobAbstractItem getFakeJob();

    public abstract float[] getColor();
    
    public abstract int setInteractTimer();
    
    public abstract void checkRedstone(Level world, BlockPos pos, IFakeDriver tile);
}
