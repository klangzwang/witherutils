package geni.witherutils.base.common.block.fakedriver;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.base.common.base.WitherMachineFakeBlockEntity;
import geni.witherutils.base.common.init.WUTBlockEntityTypes;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.io.item.MachineInventory;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.base.common.io.item.SingleSlotAccess;
import geni.witherutils.base.common.item.fakejob.FakeJobAbstractItem;
import geni.witherutils.base.common.item.fakejob.IFakeDriver;
import geni.witherutils.base.common.item.fakejob.IFakeJobItem;
import geni.witherutils.core.common.lib.LogicSupport;
import geni.witherutils.core.common.network.NetworkDataSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class FakeDriverBlockEntity extends WitherMachineFakeBlockEntity implements IFakeDriver {

	public static final SingleSlotAccess FAKEJOB = new SingleSlotAccess();
	public static final SingleSlotAccess FAKETOOL = new SingleSlotAccess();
	
    private float maxProgress = 20.0f;
    private float slideProgress;
    private float prevSlideProgress;
    
    public FakeDriverBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
		super(WUTBlockEntityTypes.FAKE_DRIVER.get(), worldPosition, blockState);
        addDataSlot(NetworkDataSlot.INT.create(() -> interactTimer, p -> interactTimer = p));
        addDataSlot(NetworkDataSlot.INT.create(() -> interactTimerMax, p -> interactTimerMax = p));
        addDataSlot(NetworkDataSlot.BOOL.create(() -> fakePlayerReady, p -> fakePlayerReady = p));
	}
    
    @Override
    public void serverTick()
    {
        super.serverTick();

		if(fakePlayer == null)
            return;

//        System.out.println("SERVER: " + getInteractTimer());
		
    	if(FAKETOOL.getItemStack(getInventory()).isEmpty())
    	{
    		FAKETOOL.setStackInSlot(getInventory(), new ItemStack(Blocks.DIRT, 1));
    	}
		
    	callJob();
	}

    @Override
    public void clientTick()
    {
    	super.clientTick();

//    	System.out.println("CLIENT: " + getInteractTimer());
    	
    	calcSlideProgress();

//    	if(interactTimer == interactTimerMax - 10)
//    		SoundUtil.playDistanceSound(Minecraft.getInstance(), level, worldPosition, WUTSounds.LASER2.get(), 20);
//    	if(interactTimer == interactTimerMax / 4)
//    		SoundUtil.playDistanceSound(Minecraft.getInstance(), level, worldPosition, WUTSounds.LASER1.get(), 30);
    	if(interactTimer == 5)
    		setLitProperty(true);
    	if(interactTimer == 0)
    		setLitProperty(false);
    }
    
    @Override
    public int getRedstoneOutput(BlockState state, BlockGetter world, BlockPos pos, Direction side)
    {
        if (side == LogicSupport.getFacing(state))
            return powerLevel;
        else
            return 0;
    }

	@Override
    public void checkRedstone(Level world, BlockPos pos)
    {
		if(!FAKEJOB.getItemStack(getInventory()).isEmpty())
		{
			Item item = FAKEJOB.getItemStack(getInventory()).getItem();
			if(item instanceof IFakeJobItem)
			{
				FakeJobAbstractItem adapItem = (FakeJobAbstractItem) item;
		        if (canInteract())
		        	adapItem.checkRedstone(world, pos, this);
			}
		}
    }

    public void calcSlideProgress()
    {
        prevSlideProgress = slideProgress;
        if(getBlockState().getValue(WitherAbstractBlock.LIT) == true)
        {
            if(slideProgress < Math.max(0, maxProgress))
            {
                slideProgress += 5.0f;
            }
        }
        else if(slideProgress > 0)
        {
            slideProgress -= 0.5f;
        }
    }
    
    public float getSlideProgress(float partialTicks)
    {
        float partialSlideProgress = prevSlideProgress + (slideProgress - prevSlideProgress) * partialTicks;
        float normalProgress = partialSlideProgress / (float) maxProgress;
        return 0.44F * (1.0F - ((float) Math.sin(Math.toRadians(90.0 + 180.0 * normalProgress)) / 2.0F + 0.5F));
    }
    
	private void callJob()
    {
		if(!FAKEJOB.getItemStack(getInventory()).isEmpty())
		{
			Item item = FAKEJOB.getItemStack(getInventory()).getItem();
			if(item instanceof IFakeJobItem)
			{
				FakeJobAbstractItem adapItem = (FakeJobAbstractItem) item;
				
				if(interactTimerMax == -1)
					setInteractTimerMax(adapItem.setInteractTimer());

		        if (canInteract())
		        	adapItem.getFakeJob().call(this);
		        else
		        	setLitProperty(false);
			}
		}
    }
    
    @Override
    protected MachineInventory createMachineInventory(MachineInventoryLayout layout)
    {
        return new MachineInventory(layout)
        {
        	@Override
        	protected void onContentsChanged(int slot)
        	{
            	if(slot == 0)
	      		{
		      		FakeDriverBlockEntity.this.setChanged();
        			level.playLocalSound(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(),
        					WUTSounds.PICKSHEE.get(), SoundSource.BLOCKS, 0.5f, 1.0f, false);
	      		}
                onInventoryContentsChanged(slot);
        		setChanged();
        	}
        };
    }
	
    @Override
    public MachineInventoryLayout getInventoryLayout()
    {
        return MachineInventoryLayout.builder()
        		.setStackLimit(1)
        		.inputSlot().slotAccess(FAKEJOB)
        		.inputSlot().slotAccess(FAKETOOL)
        		.build();
    }
    
	@Override
	public FakeDriverBlockEntity getFakeDriver()
	{
		return this;
	}
    
    @Override
    public void saveAdditional(CompoundTag pTag, Provider lookupProvider)
    {
        super.saveAdditional(pTag, lookupProvider);
    }

    @Override
    public void loadAdditional(CompoundTag pTag, Provider lookupProvider)
    {
        super.loadAdditional(pTag, lookupProvider);
    }
}
