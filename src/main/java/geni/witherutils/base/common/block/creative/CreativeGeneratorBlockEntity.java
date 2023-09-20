package geni.witherutils.base.common.block.creative;

import java.util.Optional;

import geni.witherutils.api.soulbank.FixedScalable;
import geni.witherutils.base.common.base.WitherMachineEnergyGenBlockEntity;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.base.common.io.item.MultiSlotAccess;
import geni.witherutils.core.common.sync.BooleanDataSlot;
import geni.witherutils.core.common.sync.SyncMode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;

public class CreativeGeneratorBlockEntity extends WitherMachineEnergyGenBlockEntity implements MenuProvider {

	public static final MultiSlotAccess INPUTS = new MultiSlotAccess();

    public static final FixedScalable CAPACITY = new FixedScalable(() -> 900000f);
    public static final FixedScalable TRANSFER = new FixedScalable(() -> 900000f / 4);
    public static final FixedScalable USAGE = new FixedScalable(() -> 0f);

    private boolean charging;
    
    public CreativeGeneratorBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        super(CAPACITY, TRANSFER, USAGE, WUTEntities.CREATIVE_GENERATOR.get(), worldPosition, blockState);
        addDataSlot(new BooleanDataSlot(this::isCharging, p -> charging = p, SyncMode.WORLD));
    }
    
    @Override
    public void serverTick()
    {
    	super.serverTick();
    	
    	charging = false;
    	
    	for (int i = 0; i < 4; i++)
    	{
            ItemStack chargeable = INPUTS.get(i).getItemStack(this);
            Optional<IEnergyStorage> energyHandlerCap = chargeable.getCapability(ForgeCapabilities.ENERGY).resolve();

            if (energyHandlerCap.isPresent())
            {
            	IEnergyStorage itemEnergyStorage = energyHandlerCap.get();

            	if (itemEnergyStorage.getEnergyStored() == itemEnergyStorage.getMaxEnergyStored())
            	{
            		charging = false;
            	}
            	else
            	{
                	charging = true;
                	
                    itemEnergyStorage.receiveEnergy(2000, false);
            	}
            }
    	}
    }
    
	@Override
	public boolean isGenerating()
	{
		return getGenerationRate() > 0;
	}
	@Override
	public int getGenerationRate()
	{
		return 5000;
	}
	@Override
	public boolean hasEfficiencyRate()
	{
		return false;
	}
	@Override
	public float getEfficiencyRate()
	{
		return 0;
	}
	
	public boolean isCharging()
	{
		return charging;
	}
	
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity)
    {
        return new CreativeGeneratorContainer(this, playerInventory, i);
    }
    
    @Override
    public MachineInventoryLayout getInventoryLayout()
    {
        return MachineInventoryLayout
            .builder()
            .setStackLimit(1)
            .inputSlot(4, (slot, stack) -> acceptItem(stack))
            .slotAccess(INPUTS)
            .build();
    }
    
    public boolean acceptItem(ItemStack item)
    {
        Optional<IEnergyStorage> energyHandlerCap = item.getCapability(ForgeCapabilities.ENERGY).resolve();
        return energyHandlerCap.isPresent();
    }
}
