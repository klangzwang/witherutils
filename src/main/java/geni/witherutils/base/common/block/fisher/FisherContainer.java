package geni.witherutils.base.common.block.fisher;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;

import geni.witherutils.base.common.init.WUTMenus;
import geni.witherutils.base.common.menu.MachineSlot;
import geni.witherutils.base.common.menu.WitherMachineMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FisherContainer extends WitherMachineMenu<FisherBlockEntity> {

    public FisherContainer(@Nullable FisherBlockEntity blockEntity, Inventory inventory, int pContainerId)
    {
        super(WUTMenus.FISHER.get(), pContainerId, inventory, blockEntity);
        if (blockEntity != null)
        {
    		addSlot(new MachineSlot(blockEntity.getInventory(), FisherBlockEntity.INPUTS.get(0), 8, 64));
    		addSlot(new MachineSlot(blockEntity.getInventory(), FisherBlockEntity.INPUTS.get(1), 26, 64));
    		addSlot(new MachineSlot(blockEntity.getInventory(), FisherBlockEntity.OUTPUTS.get(0), 80, 28));
    		addSlot(new MachineSlot(blockEntity.getInventory(), FisherBlockEntity.OUTPUTS.get(1), 80+18, 28));
    		addSlot(new MachineSlot(blockEntity.getInventory(), FisherBlockEntity.OUTPUTS.get(2), 80+18+18, 28));
    		addSlot(new MachineSlot(blockEntity.getInventory(), FisherBlockEntity.OUTPUTS.get(3), 80, 28+18));
    		addSlot(new MachineSlot(blockEntity.getInventory(), FisherBlockEntity.OUTPUTS.get(4), 80+18, 28+18));
    		addSlot(new MachineSlot(blockEntity.getInventory(), FisherBlockEntity.OUTPUTS.get(5), 80+18+18, 28+18));
    		addSlot(new MachineSlot(blockEntity.getInventory(), FisherBlockEntity.OUTPUTS.get(6), 80, 28+18+18));
    		addSlot(new MachineSlot(blockEntity.getInventory(), FisherBlockEntity.OUTPUTS.get(7), 80+18, 28+18+18));
    		addSlot(new MachineSlot(blockEntity.getInventory(), FisherBlockEntity.OUTPUTS.get(8), 80+18+18, 28+18+18));
        }
        addInventorySlots(8, 90 + 15, true);
    }

    public static FisherContainer factory(@Nullable MenuType<FisherContainer> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf)
    {
        BlockEntity entity = inventory.player.level().getBlockEntity(buf.readBlockPos());
        if (entity instanceof FisherBlockEntity castBlockEntity)
            return new FisherContainer(castBlockEntity, inventory, pContainerId);
        LogManager.getLogger().warn("couldn't find BlockEntity");
        return new FisherContainer(null, inventory, pContainerId);
    }
}
