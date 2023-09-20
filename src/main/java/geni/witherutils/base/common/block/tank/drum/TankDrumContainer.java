package geni.witherutils.base.common.block.tank.drum;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;

import geni.witherutils.base.common.base.MachineSlot;
import geni.witherutils.base.common.base.WitherMachineMenu;
import geni.witherutils.base.common.init.WUTMenus;

public class TankDrumContainer extends WitherMachineMenu<TankDrumBlockEntity> {

    public TankDrumContainer(@Nullable TankDrumBlockEntity blockEntity, Inventory inventory, int pContainerId)
    {
        super(blockEntity, inventory, WUTMenus.TANKDRUM.get(), pContainerId);
        if (blockEntity != null)
        {
            addSlot(new MachineSlot(blockEntity.getInventory(), TankDrumBlockEntity.INPUTFILL, 44, 28));
            addSlot(new MachineSlot(blockEntity.getInventory(), TankDrumBlockEntity.OUTPUTFILL, 44, 59));
            addSlot(new MachineSlot(blockEntity.getInventory(), TankDrumBlockEntity.INPUTDRAIN, 116, 28));
            addSlot(new MachineSlot(blockEntity.getInventory(), TankDrumBlockEntity.OUTPUTDRAIN, 116, 59));
        }
        addInventorySlots(8, 123, true);
    }

    public static TankDrumContainer factory(@Nullable MenuType<TankDrumContainer> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf)
    {
        BlockEntity entity = inventory.player.level().getBlockEntity(buf.readBlockPos());
        if (entity instanceof TankDrumBlockEntity castBlockEntity)
            return new TankDrumContainer(castBlockEntity, inventory, pContainerId);
        LogManager.getLogger().warn("couldn't find BlockEntity");
        return new TankDrumContainer(null, inventory, pContainerId);
    }
}
