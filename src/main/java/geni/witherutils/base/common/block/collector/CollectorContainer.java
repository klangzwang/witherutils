package geni.witherutils.base.common.block.collector;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;

import geni.witherutils.base.common.base.MachineSlot;
import geni.witherutils.base.common.base.WitherMachineMenu;
import geni.witherutils.base.common.init.WUTMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CollectorContainer extends WitherMachineMenu<CollectorBlockEntity> {

    public CollectorContainer(@Nullable CollectorBlockEntity blockEntity, Inventory inventory, int pContainerId)
    {
        super(blockEntity, inventory, WUTMenus.COLLECTOR.get(), pContainerId);
        if (blockEntity != null)
        {
            addSlot(new MachineSlot(blockEntity.getInventory(), CollectorBlockEntity.INPUT, 68, 30));
    		addSlot(new MachineSlot(blockEntity.getInventory(), CollectorBlockEntity.OUTPUTS.get(0), 101, 30));
    		addSlot(new MachineSlot(blockEntity.getInventory(), CollectorBlockEntity.OUTPUTS.get(1), 119, 30));
    		addSlot(new MachineSlot(blockEntity.getInventory(), CollectorBlockEntity.OUTPUTS.get(2), 137, 30));
    		addSlot(new MachineSlot(blockEntity.getInventory(), CollectorBlockEntity.OUTPUTS.get(3), 155, 30));
    		addSlot(new MachineSlot(blockEntity.getInventory(), CollectorBlockEntity.OUTPUTS.get(4), 101, 48));
    		addSlot(new MachineSlot(blockEntity.getInventory(), CollectorBlockEntity.OUTPUTS.get(5), 119, 48));
    		addSlot(new MachineSlot(blockEntity.getInventory(), CollectorBlockEntity.OUTPUTS.get(6), 137, 48));
    		addSlot(new MachineSlot(blockEntity.getInventory(), CollectorBlockEntity.OUTPUTS.get(7), 155, 48));
        }
        addInventorySlots(8, 103, true);
    }

    public static CollectorContainer factory(@Nullable MenuType<CollectorContainer> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf)
    {
        BlockEntity entity = inventory.player.level().getBlockEntity(buf.readBlockPos());
        if (entity instanceof CollectorBlockEntity castBlockEntity)
            return new CollectorContainer(castBlockEntity, inventory, pContainerId);
        LogManager.getLogger().warn("couldn't find BlockEntity");
        return new CollectorContainer(null, inventory, pContainerId);
    }
}
