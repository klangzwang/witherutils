package geni.witherutils.base.common.block.furnace.electro;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;

import geni.witherutils.base.common.base.MachineSlot;
import geni.witherutils.base.common.base.WitherMachineMenu;
import geni.witherutils.base.common.init.WUTMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ElectroFurnaceContainer extends WitherMachineMenu<ElectroFurnaceBlockEntity> {

    public ElectroFurnaceContainer(@Nullable ElectroFurnaceBlockEntity blockEntity, Inventory inventory, int pContainerId)
    {
        super(blockEntity, inventory, WUTMenus.ELECTRO_FURNACE.get(), pContainerId);
        if (blockEntity != null)
        {
            if (blockEntity.requiresSoulBank())
            {
                addSlot(new MachineSlot(blockEntity.getInventory(), blockEntity.getSoulBankSlot(), 7, 64));
            }
            addSlot(new MachineSlot(blockEntity.getInventory(), ElectroFurnaceBlockEntity.INPUT, 80, 28));
            addSlot(new MachineSlot(blockEntity.getInventory(), ElectroFurnaceBlockEntity.OUTPUT, 80, 59));
        }
        addInventorySlots(8, 103, true);
    }

    public static ElectroFurnaceContainer factory(@Nullable MenuType<ElectroFurnaceContainer> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf)
    {
        BlockEntity entity = inventory.player.level().getBlockEntity(buf.readBlockPos());
        if (entity instanceof ElectroFurnaceBlockEntity castBlockEntity)
            return new ElectroFurnaceContainer(castBlockEntity, inventory, pContainerId);
        LogManager.getLogger().warn("couldn't find BlockEntity");
        return new ElectroFurnaceContainer(null, inventory, pContainerId);
    }
}

