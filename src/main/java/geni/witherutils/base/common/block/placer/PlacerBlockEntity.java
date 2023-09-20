package geni.witherutils.base.common.block.placer;

import geni.witherutils.api.io.energy.EnergyIOMode;
import geni.witherutils.api.soulbank.QuadraticScalable;
import geni.witherutils.api.soulbank.SoulBankModifier;
import geni.witherutils.base.common.base.WitherMachineEnergyFakeBlockEntity;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.base.common.io.item.SingleSlotAccess;
import geni.witherutils.core.common.sync.BooleanDataSlot;
import geni.witherutils.core.common.sync.IntegerDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import geni.witherutils.core.common.util.FakePlayerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class PlacerBlockEntity extends WitherMachineEnergyFakeBlockEntity implements MenuProvider {

	public static final SingleSlotAccess INPUT = new SingleSlotAccess();

    public static final QuadraticScalable CAPACITY = new QuadraticScalable(SoulBankModifier.ENERGY_CAPACITY, () -> 16000);
    public static final QuadraticScalable TRANSFER = new QuadraticScalable(SoulBankModifier.ENERGY_TRANSFER, () -> 2000);
    public static final QuadraticScalable USAGE = new QuadraticScalable(SoulBankModifier.ENERGY_USE, () -> 200);
    
    private int timer;
    
	private boolean range = false;
	private boolean render = false;
	private boolean speed = false;
	
	public PlacerBlockEntity(BlockPos pos, BlockState state)
	{
		super(EnergyIOMode.Input, CAPACITY, TRANSFER, USAGE, WUTEntities.PLACER.get(), pos, state);
		addDataSlot(new IntegerDataSlot(this::getTimer, p -> timer = p, SyncMode.GUI));
		add2WayDataSlot(new BooleanDataSlot(this::getRange, p -> range = p, SyncMode.GUI));
        add2WayDataSlot(new BooleanDataSlot(this::getRender, p -> render = p, SyncMode.GUI));
        add2WayDataSlot(new BooleanDataSlot(this::getSpeed, p -> speed = p, SyncMode.GUI));
	}
    
	@Override
	public void serverTick()
	{
		super.serverTick();
		
		if(fakePlayer == null)
            this.fakePlayer = initFakePlayer((ServerLevel) level,
            ForgeRegistries.BLOCKS.getKey(getBlockState().getBlock()).getPath(), this);
		
        Direction facing = level.getBlockState(this.worldPosition).getValue(BlockStateProperties.FACING);
		BlockPos target = worldPosition.relative(facing);
		BlockState state = level.getBlockState(target);
		
		if(!canAct() || !state.isAir() || energyStorage.getEnergyStored() < 200 || INPUT.getItemStack(inventory).isEmpty())
		{
	    	resetFakeTimer();
	    	setLitProperty(false);
	        return;
		}
	    setLitProperty(true);

		if(timer-- > 0)
			return;
		else
		{
			FakePlayerUtil.setupFakePlayerForUse(fakePlayer.get(), this.worldPosition, facing, INPUT.getItemStack(inventory).copy(), false);
	
			if(!range && INPUT.getItemStack(inventory).getCount() > 0)
			{
				placeBlock(this.level, target, INPUT.getItemStack(inventory));
			}
			if(range && INPUT.getItemStack(inventory).getCount() > 4)
			{
				for(int i = 0; i < 5; i++)
				{
	                BlockPos coordsBlock = this.worldPosition.relative(facing, i + 1);
	                placeBlock(this.level, coordsBlock, INPUT.getItemStack(inventory));
				}
			}
			resetFakeTimer();
			
			energyStorage.takeEnergy(200);
		}
	}
	
    public boolean canBlockBeBroken(Level world, BlockPos pos)
    {
        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, pos, world.getBlockState(pos), fakePlayer.get());
        MinecraftForge.EVENT_BUS.post(event);
        return !event.isCanceled();
    }

	public int resetFakeTimer()
	{
		timer = 20;
		if(speed)
		timer = 1;
		return timer;
	}

	@Override
	public void load(CompoundTag tag)
	{
		render = tag.getBoolean("render");
		range = tag.getBoolean("range");
		speed = tag.getBoolean("speed");
		super.load(tag);
	}
	@Override
	public void saveAdditional(CompoundTag tag)
	{
		tag.putBoolean("render", render);
		tag.putBoolean("range", range);
		tag.putBoolean("speed", speed);
		super.saveAdditional(tag);
	}

	public int getTimer()
	{
		return timer;
	}
	public boolean getRange()
	{
		return range;
	}
    public void setRange(boolean range)
    {
    	this.range = range;
    	this.setChanged();
    }
	public boolean getRender()
	{
		return render;
	}
    public void setRender(boolean render)
    {
    	this.render = render;
    }
	public boolean getSpeed()
	{
		return speed;
	}
	public void setSpeed(boolean speed)
	{
		this.speed = speed;
		this.setChanged();
	}

    @Override
    public MachineInventoryLayout getInventoryLayout()
    {
        return MachineInventoryLayout.builder()
            .inputSlot((slot, stack) -> stack.getItem() instanceof BlockItem).slotAccess(INPUT).soulbank().build();
    }
    
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity)
    {
        return new PlacerContainer(this, playerInventory, i);
    }
    
	@Override
	public void resetFakeStamina()
	{
	}
}
