package geni.witherutils.base.common.block.tank.drum;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import geni.witherutils.base.common.config.common.BlocksConfig;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.io.fluid.MachineFluidHandler;
import geni.witherutils.base.common.io.fluid.WitherFluidTank;
import geni.witherutils.base.common.io.item.MachineInventory;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.base.common.io.item.SingleSlotAccess;
import geni.witherutils.core.common.sync.FluidStackDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import geni.witherutils.core.common.util.FluidsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.network.NetworkHooks;

public class TankDrumBlockEntity extends WitherMachineBlockEntity implements MenuProvider {
	
    public static final SingleSlotAccess INPUTFILL = new SingleSlotAccess();
    public static final SingleSlotAccess INPUTDRAIN = new SingleSlotAccess();
    public static final SingleSlotAccess OUTPUTFILL = new SingleSlotAccess();
    public static final SingleSlotAccess OUTPUTDRAIN = new SingleSlotAccess();
	
	public static final int CAPACITY = BlocksConfig.TANKDRUMCAPACITY.get() * FluidType.BUCKET_VOLUME;
	
    private final WitherFluidTank fluidTank;
    private final MachineFluidHandler fluidHandler;

    public TankDrumBlockEntity(BlockPos pos, BlockState state)
    {
        super(WUTEntities.TANKDRUM.get(), pos, state);
        this.fluidTank = createFluidTank(CAPACITY);
        this.fluidHandler = new MachineFluidHandler(getIOConfig(), fluidTank);
        addCapabilityProvider(fluidHandler);

        addDataSlot(new FluidStackDataSlot(getFluidTank()::getFluid, getFluidTank()::setFluid, SyncMode.WORLD));
        add2WayDataSlot(new FluidStackDataSlot(fluidTank::getFluid, fluidTank::setFluid, SyncMode.GUI));
    }
    
    private WitherFluidTank createFluidTank(int capacity)
    {
        return new WitherFluidTank(this, capacity)
        {
            @Override
            protected void onContentsChanged()
            {
            	onTankContentsChanged();
                super.onContentsChanged();
                setChanged();
            }
        };
    }
    
    @Override
    protected MachineInventory createMachineInventory(MachineInventoryLayout layout)
    {
        return new MachineInventory(getIOConfig(), layout)
        {
        	@Override
        	protected void onContentsChanged(int slot)
        	{
        		if(slot == 0)
        			level.playLocalSound(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(),
        					WUTSounds.WATERHIT.get(), SoundSource.BLOCKS, 0.5f, 1.0f, false);
        		if(slot == 2)
        			level.playLocalSound(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(),
        					WUTSounds.WATERHIT.get(), SoundSource.BLOCKS, 0.5f, 1.0f, false);
        		if(slot == 1)
        			level.playLocalSound(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(),
        					WUTSounds.BUCKET.get(), SoundSource.BLOCKS, 0.8f, 1.0f, false);
        		if(slot == 3)
        			level.playLocalSound(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(),
        					WUTSounds.BUCKET.get(), SoundSource.BLOCKS, 0.8f, 1.0f, false);
        		setChanged();
        	}
        	
        	@Override
        	public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
        	{
    			if(stack.getItem() instanceof BucketItem)
    			{
    				if(slot == 1 || slot == 3)
    					return super.insertItem(slot, stack, simulate);

            		if(slot == 0)
            		{
                		if(fluidTank.getFluidAmount() < 28000)
                			return super.insertItem(slot, stack, simulate);
            		}
            		if(slot == 2)
            		{
                		if(fluidTank.getFluidAmount() > 0)
                			return super.insertItem(slot, stack, simulate);
            		}
    			}
				return stack;
        	}
        };
    }
    
    @Override
    public void serverTick()
    {
        super.serverTick();
        
        if(canActSlow())
        {
            fillInternal();
            drainInternal();
        }
        
        BlockEntity below = this.level.getBlockEntity(this.worldPosition.below());
        if (below != null && below instanceof TankDrumBlockEntity)
        {
        	FluidsUtil.tryFillPositionFromTank(level, this.worldPosition.below(), Direction.UP, fluidTank, FluidType.BUCKET_VOLUME / 20);
        }
    }
    
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if(!world.isClientSide)
        {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            
			ItemStack heldStack = player.getItemInHand(hand);
			if (!heldStack.isEmpty())
			{
				if (heldStack.getItem() == Items.GLASS_BOTTLE)
				{
					return super.use(state, world, pos, player, hand, hit);
				}
			}

            if(tileEntity instanceof MenuProvider)
            {
                NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) tileEntity, tileEntity.getBlockPos());
//                SoundUtil.playSoundFromServer((ServerPlayer) player, WUTSounds.BUCKET.get(), 0.05f, 1.0f);
            }
            else
            {
                throw new IllegalStateException("Our named container provider is missing!");
            }
        }
        return InteractionResult.SUCCESS;
    }
    
    private void fillInternal()
    {
        ItemStack inputItem = getInventory().getStackInSlot(0);
        ItemStack outputItem = getInventory().getStackInSlot(1);
        if(!inputItem.isEmpty())
        {
            if(inputItem.getItem() instanceof BucketItem filledBucket)
            {
                if(outputItem.isEmpty() || (outputItem.getItem() == Items.BUCKET && outputItem.getCount() < outputItem.getMaxStackSize()))
                {
                    int filled = fluidTank.fill(new FluidStack(filledBucket.getFluid(), FluidType.BUCKET_VOLUME), FluidAction.SIMULATE);
                    if(filled <= FluidType.BUCKET_VOLUME)
                    {
                        fluidTank.fill(new FluidStack(filledBucket.getFluid(), FluidType.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE);
                        inputItem.shrink(1);
                        getInventory().insertItem(1, Items.BUCKET.getDefaultInstance(), false);
                    }
                }
            }
            else
            {
                Optional<IFluidHandlerItem> fluidHandlerCap = inputItem.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve();
                if(fluidHandlerCap.isPresent() && outputItem.isEmpty())
                {
                    IFluidHandlerItem itemFluid = fluidHandlerCap.get();
                    int filled = moveFluids(itemFluid, fluidTank, fluidTank.getCapacity());
                    if(filled > 0)
                    {
                        getInventory().setStackInSlot(1, itemFluid.getContainer());
                        getInventory().setStackInSlot(0, ItemStack.EMPTY);
                    }
                }
            }
        }
    }

    private void drainInternal()
    {
        ItemStack inputItem = getInventory().getStackInSlot(2);
        ItemStack outputItem = getInventory().getStackInSlot(3);
        if(!inputItem.isEmpty())
        {
            if(inputItem.getItem() == Items.BUCKET)
            {
                if(!fluidTank.isEmpty())
                {
                    FluidStack stack = fluidTank.drain(FluidType.BUCKET_VOLUME, FluidAction.SIMULATE);
                    if(stack.getAmount() <= FluidType.BUCKET_VOLUME && (outputItem.isEmpty() || (outputItem.getItem() == stack.getFluid().getBucket() && outputItem.getCount() < outputItem.getMaxStackSize())))
                    {
                        fluidTank.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.EXECUTE);
                        inputItem.shrink(1);
                        if(outputItem.isEmpty())
                        {
                            getInventory().setStackInSlot(3, stack.getFluid().getBucket().getDefaultInstance());
                        }
                        else
                        {
                            outputItem.grow(1);
                        }
                    }
                }
            }
            else
            {
                Optional<IFluidHandlerItem> fluidHandlerCap = inputItem.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve();
                if(fluidHandlerCap.isPresent() && outputItem.isEmpty())
                {
                    IFluidHandlerItem itemFluid = fluidHandlerCap.get();
                    int filled = moveFluids(fluidTank, itemFluid, fluidTank.getFluidAmount());
                    if(filled > 0)
                    {
                        getInventory().setStackInSlot(3, itemFluid.getContainer());
                        getInventory().setStackInSlot(2, ItemStack.EMPTY);
                    }
                }
            }
        }
    }
    
    public FluidTank getFluidTank()
    {
        return fluidTank;
    }

    public void dump()
    {
    	fluidTank.drain(FluidType.BUCKET_VOLUME * BlocksConfig.TANKDRUMCAPACITY.get(),
    			IFluidHandler.FluidAction.EXECUTE);
    }
    
	public FluidStack getTankFluid()
	{
		return fluidTank.getFluid();
	}
	public void setFluid(FluidStack fluid)
	{
		fluidTank.setFluid(fluid);
	}
	
    @Override
    public void saveAdditional(CompoundTag pTag)
    {
        super.saveAdditional(pTag);
        pTag.put("fluid", fluidTank.writeToNBT(new CompoundTag()));
    }

    @Override
    public void load(CompoundTag pTag)
    {
        super.load(pTag);
        fluidTank.readFromNBT(pTag.getCompound("fluid"));
    }
    
    @Override
    public MachineInventoryLayout getInventoryLayout()
    {
        return MachineInventoryLayout
            .builder()
            .inputSlot()
            .slotAccess(INPUTFILL)
            .outputSlot()
            .slotAccess(OUTPUTFILL)
            .inputSlot()
            .slotAccess(INPUTDRAIN)
            .outputSlot()
            .slotAccess(OUTPUTDRAIN)
            .build();
    }
    
    private void onTankContentsChanged()
    {
        if (level != null)
        {
            if (!level.isClientSide())
            {
            }
        }
    }
    
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity)
    {
        return new TankDrumContainer(this, playerInventory, i);
    }
}
