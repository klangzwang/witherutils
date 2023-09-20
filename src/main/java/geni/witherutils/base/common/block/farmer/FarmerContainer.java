package geni.witherutils.base.common.block.farmer;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;

import geni.witherutils.base.common.base.MachineSlotStack;
import geni.witherutils.base.common.base.WitherMachineMenu;
import geni.witherutils.base.common.init.WUTMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FarmerContainer extends WitherMachineMenu<FarmerBlockEntity> {

	public FarmerContainer(FarmerBlockEntity blockEntity, Inventory inventory, int pContainerId)
	{
		super(blockEntity, inventory, WUTMenus.FARMER.get(), pContainerId);
		if (blockEntity != null)
		{
	        if (blockEntity.requiresSoulBank())
	        {
	            addSlot(new MachineSlotStack(blockEntity.getInventory(), blockEntity.getSoulBankSlot(), 7, 64, new ItemStack(Items.ACACIA_BOAT)));
	        }
			addSlot(new MachineSlotStack(blockEntity.getInventory(), FarmerBlockEntity.TOOL, 44, 24, new ItemStack(Items.ACACIA_BOAT)));
			addSlot(new MachineSlotStack(blockEntity.getInventory(), FarmerBlockEntity.FERT, 62, 24, new ItemStack(Items.ACACIA_BOAT)));
			
			addSlot(new MachineSlotStack(blockEntity.getInventory(), FarmerBlockEntity.SEEDS1, 44, 46, new ItemStack(Items.ACACIA_BOAT))
			{
				@Override
				public boolean allowModification(Player player)
				{
					return !blockEntity.lockedSW;
				}
			});
			addSlot(new MachineSlotStack(blockEntity.getInventory(), FarmerBlockEntity.SEEDS2, 62, 46, new ItemStack(Items.ACACIA_BOAT))
			{
				@Override
				public boolean allowModification(Player player)
				{
					return !blockEntity.lockedSE;
				}
			});
			addSlot(new MachineSlotStack(blockEntity.getInventory(), FarmerBlockEntity.SEEDS3, 44, 64, new ItemStack(Items.ACACIA_BOAT))
			{
				@Override
				public boolean allowModification(Player player)
				{
					return !blockEntity.lockedNW;
				}
			});
			addSlot(new MachineSlotStack(blockEntity.getInventory(), FarmerBlockEntity.SEEDS4, 62, 64, new ItemStack(Items.ACACIA_BOAT))
			{
				@Override
				public boolean allowModification(Player player)
				{
					return !blockEntity.lockedNE;
				}
			});

			addSlot(new MachineSlotStack(blockEntity.getInventory(), FarmerBlockEntity.OUTPUTS.get(0), 98, 46, new ItemStack(Items.ACACIA_BOAT)));
			addSlot(new MachineSlotStack(blockEntity.getInventory(), FarmerBlockEntity.OUTPUTS.get(1), 116, 46, new ItemStack(Items.ACACIA_BOAT)));
			addSlot(new MachineSlotStack(blockEntity.getInventory(), FarmerBlockEntity.OUTPUTS.get(2), 98, 64, new ItemStack(Items.ACACIA_BOAT)));
			addSlot(new MachineSlotStack(blockEntity.getInventory(), FarmerBlockEntity.OUTPUTS.get(3), 116, 64, new ItemStack(Items.ACACIA_BOAT)));
		}
		addInventorySlots(8, 102, true);
		
		for(Slot slot : this.slots)
		{
    		if(slot instanceof MachineSlotStack)
    		{
    			MachineSlotStack mslot = (MachineSlotStack) slot;
    			mslot.setIsGrayOut();
    		}
		}
	}

    public static FarmerContainer factory(@Nullable MenuType<FarmerContainer> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf)
    {
        BlockEntity entity = inventory.player.level().getBlockEntity(buf.readBlockPos());
        if (entity instanceof FarmerBlockEntity castBlockEntity)
            return new FarmerContainer(castBlockEntity, inventory, pContainerId);
        LogManager.getLogger().warn("couldn't find BlockEntity");
        return new FarmerContainer(null, inventory, pContainerId);
    }
}
