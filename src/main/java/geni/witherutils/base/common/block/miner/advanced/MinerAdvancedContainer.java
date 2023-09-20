package geni.witherutils.base.common.block.miner.advanced;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;

import geni.witherutils.base.common.base.MachineSlot;
import geni.witherutils.base.common.base.WitherMachineMenu;
import geni.witherutils.base.common.init.WUTMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class MinerAdvancedContainer extends WitherMachineMenu<MinerAdvancedBlockEntity> {

	public MinerAdvancedContainer(MinerAdvancedBlockEntity blockEntity, Inventory inventory, int pContainerId)
	{
		super(blockEntity, inventory, WUTMenus.MINERADV.get(), pContainerId);
		
        if (blockEntity != null)
        {
            if (blockEntity.requiresSoulBank())
            {
                addSlot(new MachineSlot(blockEntity.getInventory(), blockEntity.getSoulBankSlot(), 7, 64));
            }
            
    		addSlot(new MachineSlot(blockEntity.getInventory(), MinerAdvancedBlockEntity.INPUTS.get(0), 76, 30));
    		addSlot(new MachineSlot(blockEntity.getInventory(), MinerAdvancedBlockEntity.INPUTS.get(1), 44, 30));

    		addSlot(new MachineSlot(blockEntity.getInventory(), MinerAdvancedBlockEntity.OUTPUTS.get(0), 101, 30));
    		addSlot(new MachineSlot(blockEntity.getInventory(), MinerAdvancedBlockEntity.OUTPUTS.get(1), 119, 30));
    		addSlot(new MachineSlot(blockEntity.getInventory(), MinerAdvancedBlockEntity.OUTPUTS.get(2), 137, 30));
    		addSlot(new MachineSlot(blockEntity.getInventory(), MinerAdvancedBlockEntity.OUTPUTS.get(3), 155, 30));
    		addSlot(new MachineSlot(blockEntity.getInventory(), MinerAdvancedBlockEntity.OUTPUTS.get(4), 101, 48));
    		addSlot(new MachineSlot(blockEntity.getInventory(), MinerAdvancedBlockEntity.OUTPUTS.get(5), 119, 48));
    		addSlot(new MachineSlot(blockEntity.getInventory(), MinerAdvancedBlockEntity.OUTPUTS.get(6), 137, 48));
    		addSlot(new MachineSlot(blockEntity.getInventory(), MinerAdvancedBlockEntity.OUTPUTS.get(7), 155, 48));
        }
        addInventorySlots(8, 103, true);
	}
	
    public static MinerAdvancedContainer factory(@Nullable MenuType<MinerAdvancedContainer> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf)
    {
        BlockEntity entity = inventory.player.level().getBlockEntity(buf.readBlockPos());
        if (entity instanceof MinerAdvancedBlockEntity castBlockEntity)
            return new MinerAdvancedContainer(castBlockEntity, inventory, pContainerId);
        LogManager.getLogger().warn("couldn't find BlockEntity");
        return new MinerAdvancedContainer(null, inventory, pContainerId);
    }
}
