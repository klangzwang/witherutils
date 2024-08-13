package geni.witherutils.base.common.block.sensor.floor;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;

import geni.witherutils.base.common.init.WUTMenus;
import geni.witherutils.base.common.menu.GhostMachineSlot;
import geni.witherutils.base.common.menu.WitherMachineMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FloorSensorContainer extends WitherMachineMenu<FloorSensorBlockEntity> {

    public FloorSensorContainer(@Nullable FloorSensorBlockEntity blockEntity, Inventory inventory, int pContainerId)
    {
        super(WUTMenus.FLOORSENSOR.get(), pContainerId, inventory, blockEntity);
		if (blockEntity != null)
		{
			addSlot(new GhostMachineSlot(blockEntity.getInventory(), FloorSensorBlockEntity.GHOST, 157, 28));
		}
		addInventorySlots(8, 69, true);
    }
    
    public static FloorSensorContainer factory(@Nullable MenuType<FloorSensorContainer> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf)
    {
        BlockEntity entity = inventory.player.level().getBlockEntity(buf.readBlockPos());
        if (entity instanceof FloorSensorBlockEntity castBlockEntity)
            return new FloorSensorContainer(castBlockEntity, inventory, pContainerId);
        LogManager.getLogger().warn("couldn't find BlockEntity");
        return new FloorSensorContainer(null, inventory, pContainerId);
    }
}
