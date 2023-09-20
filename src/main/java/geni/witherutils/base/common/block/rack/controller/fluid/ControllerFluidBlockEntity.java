package geni.witherutils.base.common.block.rack.controller.fluid;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import geni.witherutils.base.common.block.rack.casing.CaseBlock;
import geni.witherutils.base.common.block.rack.casing.CaseBlockEntity;
import geni.witherutils.base.common.config.common.BlocksConfig;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.io.fluid.WitherFluidTank;
import geni.witherutils.core.common.sync.FluidStackDataSlot;
import geni.witherutils.core.common.sync.IntegerDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import geni.witherutils.core.common.util.FacingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class ControllerFluidBlockEntity extends WitherMachineBlockEntity {

	static HashMap<UUID, Long> INTERACTION_LOGGER = new HashMap<>();
	
    private final WitherFluidTank fluidTank;
    private int fluidStored;
    private boolean isEmpty;

    private final LazyOptional<IFluidHandler> lazyFStorage;
    
    public static final int FLUIDCAPACITY = BlocksConfig.FLUIDCONTROLLERCAPACITY.get();
    
	public ControllerFluidBlockEntity(BlockPos pos, BlockState state)
	{
		super(WUTEntities.RACKFLUID_CONTROLLER.get(), pos, state);
        this.fluidTank = createFluidTank(FLUIDCAPACITY);
        this.lazyFStorage = LazyOptional.of(() -> fluidTank);
        addDataSlot(new FluidStackDataSlot(getFluidTank()::getFluid, getFluidTank()::setFluid, SyncMode.WORLD));
        add2WayDataSlot(new FluidStackDataSlot(fluidTank::getFluid, fluidTank::setFluid, SyncMode.GUI));
        addDataSlot(new IntegerDataSlot(this::getFluidStored, p -> fluidStored = p, SyncMode.WORLD));
        this.fluidStored = 0;
        isEmpty = true;
	}
	
    private WitherFluidTank createFluidTank(int capacity)
    {
        return new WitherFluidTank(this, capacity)
        {
            @Override
            protected void onContentsChanged()
            {
                super.onContentsChanged();
                setChanged();
            }
        };
    }
    
	@Override
	public void serverTick()
	{
		super.serverTick();
	}
	@Override
	public void clientTick()
	{
		super.clientTick();
		
		setLitProperty(fluidStored > 0);
		fluidStored = getFluidTank().getFluidAmount();
		
        if (isEmpty != (fluidTank.getFluidAmount() == 0))
            this.level.sendBlockUpdated(this.worldPosition, this.level.getBlockState(this.worldPosition), this.level.getBlockState(this.worldPosition), Block.UPDATE_ALL);
        isEmpty = fluidTank.getFluidAmount() == 0;
	}
	
	public boolean isFormed()
	{
		for(BlockPos aroundPos : FacingUtil.AROUND_ALL)
		{
			if(aroundPos != worldPosition)
			{
				BlockEntity te = level.getBlockEntity(worldPosition.offset(aroundPos));
				if (te instanceof CaseBlockEntity)
				{
					CaseBlockEntity caseBe = (CaseBlockEntity) te; 
					return CaseBlock.isFormed(caseBe.getBlockState());
				}
			}
		}
		return false;
	}
    
    @Nonnull
    @Override
    public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side)
    {
        if (cap == ForgeCapabilities.FLUID_HANDLER)
        {
            return lazyFStorage.cast();
        }
        return super.getCapability(cap, side);
    }
    
    public FluidStack getDisplayStack()
    {
        return fluidTank.getFluid();
    }

	public int getFluidStored()
	{
		return fluidStored;
	}
    public void setFluidStored(int stored)
    {
        this.fluidStored = stored;
    }
    
    public LazyOptional<IFluidHandler> getFLazyStorage()
	{
		return lazyFStorage;
	}
	
    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        this.fluidStored = tag.getInt("fluidStored");
        fluidTank.readFromNBT(tag.getCompound("fluid"));
    }

    @Override
    public void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        tag.putInt("fluidStored", fluidStored);
        tag.put("fluid", fluidTank.writeToNBT(new CompoundTag()));
    }
    
    public FluidTank getFluidTank()
    {
        return fluidTank;
    }
    
    public String getFormatedDisplayAmount()
    {
    	return fluidStored == 0 ? "0" : (fluidStored >= 1000 ? getFormatedBigNumber(fluidStored / 1000) + " x1000" : "") + (fluidStored >= 1000 && fluidStored % 1000 != 0 ? " + " : "") + (fluidStored % 1000 != 0 ? fluidStored % 1000 : "");
    }
    
    private static DecimalFormat formatterWithUnits = new DecimalFormat("####0.#");

    public static String getFormatedBigNumber(int number)
    {
        if (number >= 1000000000) {
            float numb = number / 1000000000F;
            return formatterWithUnits.format(numb) + "B";
        } else if (number >= 1000000) {
            float numb = number / 1000000F;
            if (number > 100000000) numb = Math.round(numb);
            return formatterWithUnits.format(numb) + "M";
        } else if (number >= 1000) {
            float numb = number / 1000F;
            if (number > 100000) numb = Math.round(numb);
            return formatterWithUnits.format(numb) + "K";
        }
        return String.valueOf(number);
    }
}
