package geni.witherutils.base.common.block.spawner;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;

import geni.witherutils.base.common.init.WUTMenus;
import geni.witherutils.base.common.menu.MachineSlot;
import geni.witherutils.base.common.menu.WitherMachineMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SpawnerContainer extends WitherMachineMenu<SpawnerBlockEntity> {

    public SpawnerContainer(@Nullable SpawnerBlockEntity blockEntity, Inventory inventory, int pContainerId)
    {
        super(WUTMenus.SPAWNER.get(), pContainerId, inventory, blockEntity);
        if (blockEntity != null)
        {
    	    addSlot(new MachineSlot(blockEntity.getInventory(), SpawnerBlockEntity.INPUT, 153, 42));
        }
        addInventorySlots(8, 103, true);
    }

    public static SpawnerContainer factory(@Nullable MenuType<SpawnerContainer> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf)
    {
        BlockEntity entity = inventory.player.level().getBlockEntity(buf.readBlockPos());
        if (entity instanceof SpawnerBlockEntity castBlockEntity)
            return new SpawnerContainer(castBlockEntity, inventory, pContainerId);
        LogManager.getLogger().warn("couldn't find BlockEntity");
        return new SpawnerContainer(null, inventory, pContainerId);
    }
}
