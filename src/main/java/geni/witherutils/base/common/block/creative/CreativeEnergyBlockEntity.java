package geni.witherutils.base.common.block.creative;

import org.jetbrains.annotations.Nullable;

import geni.witherutils.api.io.energy.EnergyIOMode;
import geni.witherutils.base.common.base.WitherMachineBlockEntity;
import geni.witherutils.base.common.block.LogicalBlockEntities;
import geni.witherutils.base.common.io.energy.WitherEnergyStorage;
import geni.witherutils.base.common.io.fluid.WitherFluidTank;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;

public class CreativeEnergyBlockEntity extends WitherMachineBlockEntity implements MenuProvider {

    private static final int BASE_FE_PRODUCTION = 4000;
    private final WitherEnergyStorage energy = new WitherEnergyStorage(EnergyIOMode.OUTPUT, () -> Integer.MAX_VALUE, () -> Integer.MAX_VALUE);
    private final WitherFluidTank tank = new WitherFluidTank(Integer.MAX_VALUE);
    
    private int rfPerTick;
    
	public CreativeEnergyBlockEntity(BlockPos pos, BlockState state)
	{
		super(LogicalBlockEntities.CREATIVEENERGY.get(), pos, state);
	}
	
    @Override
    public boolean hasItemCapability()
    {
        return false;
    }

    @Override
    public boolean hasEnergyCapability()
    {
        return true;
    }
    
    @Override
    public boolean hasFluidCapability()
    {
        return true;
    }
    
    @Override
    public IFluidHandler getFluidHandler(@Nullable Direction dir)
    {
        return dir == getCurrentFacing() ? null : tank;
    }
    
    @Override
    public IEnergyStorage getEnergyHandler(@Nullable Direction dir)
    {
        return dir == getCurrentFacing() ? null : energy;
    }
    
    @Override
    public void serverTick()
    {
        super.serverTick();

        if (level.getGameTime() % 5 == 0)
        {
            rfPerTick = (int) (BASE_FE_PRODUCTION);
        }
        if (energy.getEnergyStored() != Integer.MAX_VALUE)
        {
            energy.receiveEnergy(rfPerTick, false);
        }
        if (tank.getFluidAmount() != Integer.MAX_VALUE)
        {
        	tank.fill(new FluidStack(Fluids.LAVA, 1000), FluidAction.EXECUTE);
        }
        
        System.out.println(tank.getFluidAmount());
        System.out.println(energy.getEnergyStored());
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider)
    {
        super.saveAdditional(tag, provider);
        energy.serializeNBT(provider);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider)
    {
        super.loadAdditional(tag, provider);
        energy.deserializeNBT(provider, tag);
    }
    
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity)
    {
    	return new CreativeEnergyContainer(id, playerInventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(this.worldPosition));
    }
}
