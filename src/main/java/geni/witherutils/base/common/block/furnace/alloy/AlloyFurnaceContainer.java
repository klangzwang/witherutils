package geni.witherutils.base.common.block.furnace.alloy;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;

import geni.witherutils.base.common.base.MachineSlot;
import geni.witherutils.base.common.base.WitherMachineMenu;
import geni.witherutils.base.common.init.WUTMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AlloyFurnaceContainer extends WitherMachineMenu<AlloyFurnaceBlockEntity> {

    public AlloyFurnaceContainer(@Nullable AlloyFurnaceBlockEntity blockEntity, Inventory inventory, int pContainerId)
    {
        super(blockEntity, inventory, WUTMenus.ALLOY_FURNACE.get(), pContainerId);
        if (blockEntity != null)
        {
            if (blockEntity.requiresSoulBank())
            {
                addSlot(new MachineSlot(blockEntity.getInventory(), blockEntity.getSoulBankSlot(), 7, 64));
            }
            addSlot(new MachineSlot(blockEntity.getInventory(), AlloyFurnaceBlockEntity.INPUTS.get(0), 59, 28));
            addSlot(new MachineSlot(blockEntity.getInventory(), AlloyFurnaceBlockEntity.INPUTS.get(1), 80, 28));
            addSlot(new MachineSlot(blockEntity.getInventory(), AlloyFurnaceBlockEntity.INPUTS.get(2), 101, 28));
            addSlot(new MachineSlot(blockEntity.getInventory(), AlloyFurnaceBlockEntity.OUTPUT, 80, 59));
        }
        addInventorySlots(8, 103, true);
    }

    public static AlloyFurnaceContainer factory(@Nullable MenuType<AlloyFurnaceContainer> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf)
    {
        BlockEntity entity = inventory.player.level().getBlockEntity(buf.readBlockPos());
        if (entity instanceof AlloyFurnaceBlockEntity castBlockEntity)
            return new AlloyFurnaceContainer(castBlockEntity, inventory, pContainerId);
        LogManager.getLogger().warn("couldn't find BlockEntity");
        return new AlloyFurnaceContainer(null, inventory, pContainerId);
    }
}

