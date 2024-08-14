//package geni.witherutils.base.common.block.tank.reservoir;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import javax.annotation.Nonnull;
//
//import geni.witherutils.base.common.base.WitherMachineBlockEntity;
//import geni.witherutils.base.common.init.WUTEntities;
//import geni.witherutils.base.common.io.fluid.MachineFluidHandler;
//import geni.witherutils.base.common.io.fluid.WitherFluidTank;
//import geni.witherutils.core.common.sync.FluidStackDataSlot;
//import geni.witherutils.core.common.sync.SyncMode;
//import geni.witherutils.core.common.util.NNList;
//import geni.witherutils.core.common.util.NNList.NNIterator;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.material.Fluids;
//import net.minecraftforge.fluids.FluidStack;
//import net.minecraftforge.fluids.FluidType;
//import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
//import net.minecraftforge.fluids.capability.templates.FluidTank;
//
//public class TankReservoirBlockEntity extends WitherMachineBlockEntity {
//
//	public static final int CAPACITY = 2 * FluidType.BUCKET_VOLUME;
//	public static final int TRANSFER_FLUID_PER_TICK = FluidType.BUCKET_VOLUME / 2;
//	private final int checkOffset = (int) (Math.random() * 20);
//	
//    private final WitherFluidTank fluidTank;
//    private final MachineFluidHandler fluidHandler;
//
//    protected boolean canRefill = false;
//    protected boolean autoEject = false;
//    protected boolean tankDirty = false;
//
//    public TankReservoirBlockEntity(BlockPos pos, BlockState state)
//    {
//        super(WUTEntities.RESERVOIR.get(), pos, state);
//        this.fluidTank = createFluidTank(CAPACITY);
//        this.fluidHandler = new MachineFluidHandler(getIOConfig(), fluidTank);
//        addCapabilityProvider(fluidHandler);
//        addDataSlot(new FluidStackDataSlot(fluidTank::getFluid, fluidTank::setFluid, SyncMode.WORLD));
//    }
//    
//    private WitherFluidTank createFluidTank(int capacity)
//    {
//        return new WitherFluidTank(this, capacity)
//        {
//        	@Override
//        	public boolean isFluidValid(FluidStack stack)
//        	{
//        		if(stack.getFluid() != Fluids.WATER)
//        			return false;
//        		return true;
//        	}
//            @Override
//            protected void onContentsChanged()
//            {
//                setChanged();
//            }
//        };
//    }
//    
//    @Override
//    public void serverTick()
//    {
//        super.serverTick();
//        
//        doInfiniteSource();
//		if(shouldDoWorkThisTick(WORK_TICKS * 3, 1) && !fluidTank.isEmpty())
//		{
//			doLeak();
//			if(!fluidTank.isEmpty())
//			{
//				doEqualize();
//			}
//		}
//		if(shouldDoWorkThisTick(WORK_TICKS))
//		{
//			if(autoEject && allowExtracting())
//			{
//				doPush(WORK_TICKS);
//			}
//			if(tankDirty)
//			{
//				tankDirty = false;
//			}
//		}
//    }
//    
//	protected static final int WORK_TICKS = 5;
//
//	private boolean hasEnoughLiquid()
//	{
//		Set<TankReservoirBlockEntity> seen = new HashSet<TankReservoirBlockEntity>();
//		seen.add(this);
//		int got = fluidTank.getFluidAmount();
//		for(NNIterator<Direction> itr = NNList.FACING.fastIterator(); itr.hasNext();)
//		{
//			BlockPos pos1 = getBlockPos().relative(itr.next());
//			TankReservoirBlockEntity other = TankReservoirBlock.getAnyTileEntity(level, pos1, this.getClass());
//			if(other != null && !seen.contains(other))
//			{
//				seen.add(other);
//				got += other.fluidTank.getFluidAmount();
//				if(got >= FluidType.BUCKET_VOLUME * 2)
//				{
//					return true;
//				}
//				for(NNIterator<Direction> itr2 = NNList.FACING.fastIterator(); itr2.hasNext();)
//				{
//					BlockPos pos2 = pos1.relative(itr2.next());
//					TankReservoirBlockEntity other2 = TankReservoirBlock.getAnyTileEntity(level, pos2, this.getClass());
//					if(other2 != null && !seen.contains(other2))
//					{
//						seen.add(other2);
//						got += other2.fluidTank.getFluidAmount();
//						if(got >= FluidType.BUCKET_VOLUME * 2)
//						{
//							return true;
//						}
//					}
//				}
//			}
//		}
//		return false;
//	}
//
//	protected boolean allowExtracting()
//	{
//		return canRefill;
//	}
//
//	protected void doInfiniteSource()
//	{
//		if(shouldDoWorkThisTick(WORK_TICKS * 2))
//		{
//			if(tankDirty || fluidTank.getFluidAmount() < fluidTank.getCapacity() || !canRefill)
//			{
//				canRefill = hasEnoughLiquid();
//			}
//		}
//		else if(canRefill && fluidTank.getFluidAmount() < fluidTank.getCapacity() && shouldDoWorkThisTick(WORK_TICKS * 2, -1) && fluidTank.getFluid() != null)
//		{
//			fluidTank.fill(new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME / 2), FluidAction.EXECUTE);
//			setTanksDirty();
//		}
//	}
//
//	protected void doLeak()
//	{
//		BlockPos down = getBlockPos().below();
//		if(doLeak(down))
//		{
//			for(NNIterator<Direction> itr = NNList.FACING_HORIZONTAL.fastIterator(); itr.hasNext() && !fluidTank.isEmpty(); doLeak(down.relative(itr.next())))
//			{
//			}
//		}
//	}
//
//	protected boolean doLeak(@Nonnull BlockPos otherPos)
//	{
//		TankReservoirBlockEntity other = TankReservoirBlock.getAnyTileEntity(level, otherPos, this.getClass());
//		if(other != null)
//		{
//			if(other.fluidTank.isEmpty() && other.fluidTank.isFluidValid(fluidTank.getFluid()))
//			{
//				FluidStack canDrain = fluidTank.drain(other.fluidTank.getCapacity(), FluidAction.SIMULATE);
//				if (canDrain != null && canDrain.getAmount() > 0)
//				{
//					int fill = other.fluidTank.fill(canDrain, FluidAction.EXECUTE);
//					fluidTank.drain(fill, FluidAction.EXECUTE);
//				}
//			}
//			return true;
//		}
//		return false;
//	}
//
//	protected void doPush(int ticks)
//	{
//		if(!fluidTank.isEmpty())
//		{
//			for (NNIterator<Direction> itr = NNList.FACING.fastIterator(); itr.hasNext() && !fluidTank.isEmpty();)
//			{
//				final Direction dir = itr.next();
//				final BlockPos neighbor = getBlockPos().relative(dir);
//				if(!(level.getBlockState(neighbor).getBlock() instanceof TankReservoirBlock))
//				{
//					setTanksDirty();
//				}
//			}
//		}
//	}
//
//	private static final int IO_MB_TICK = 100;
//
//	protected void doEqualize()
//	{
//		for(NNIterator<Direction> itr = NNList.FACING_HORIZONTAL.fastIterator(); itr.hasNext() && !fluidTank.isEmpty();)
//		{
//			BlockPos pos1 = getBlockPos().relative(itr.next());
//			TankReservoirBlockEntity other = TankReservoirBlock.getAnyTileEntity(level, pos1, this.getClass());
//			if(other != null)
//			{
//				int toMove = (fluidTank.getFluidAmount() - other.fluidTank.getFluidAmount()) / 2;
//				if(toMove > 0)
//				{
//					FluidStack canDrain = fluidTank.drain(Math.min(toMove, IO_MB_TICK / 4), FluidAction.SIMULATE);
//					if(canDrain != null && canDrain.getAmount() > 0)
//					{
//						int fill = other.fluidTank.fill(canDrain, FluidAction.EXECUTE);
//						fluidTank.drain(fill, FluidAction.EXECUTE);
//						other.setTanksDirty();
//			            setTanksDirty();
//					}
//				}
//			}
//		}
//	}
//
//    public FluidTank getFluidTank()
//    {
//        return fluidTank;
//    }
//
//	public void setTanksDirty()
//	{
//		if(!tankDirty)
//		{
//			tankDirty = true;
//			setChanged();
//		}
//	}
//	
//	public void setAutoEject(boolean autoEject)
//	{
//		this.autoEject = autoEject;
//		setTanksDirty();
//	}
//	public boolean isAutoEject()
//	{
//		return autoEject;
//	}
//		  
//    public boolean shouldDoWorkThisTick(int interval)
//    {
//    	return shouldDoWorkThisTick(interval, 0);
//    }
//    
//    public boolean shouldDoWorkThisTick(int interval, int offset)
//    {
//    	return (level.getGameTime() + checkOffset + offset) % interval == 0;
//    }
//    
//    @Override
//    public void saveAdditional(CompoundTag pTag)
//    {
//        super.saveAdditional(pTag);
//        pTag.put("fluid", fluidTank.writeToNBT(new CompoundTag()));
//    }
//
//    @Override
//    public void load(CompoundTag pTag)
//    {
//        super.load(pTag);
//        fluidTank.readFromNBT(pTag.getCompound("fluid"));
//    }
//}
