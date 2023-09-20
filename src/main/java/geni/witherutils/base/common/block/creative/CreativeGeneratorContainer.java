package geni.witherutils.base.common.block.creative;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;

import geni.witherutils.base.common.base.MachineSlot;
import geni.witherutils.base.common.base.WitherMachineMenu;
import geni.witherutils.base.common.init.WUTMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CreativeGeneratorContainer extends WitherMachineMenu<CreativeGeneratorBlockEntity> {

    public CreativeGeneratorContainer(@Nullable CreativeGeneratorBlockEntity blockEntity, Inventory inventory, int pContainerId)
    {
        super(blockEntity, inventory, WUTMenus.CREATIVEGEN.get(), pContainerId);
        if (blockEntity != null)
        {
            addSlot(new MachineSlot(blockEntity.getInventory(), CreativeGeneratorBlockEntity.INPUTS.get(0), 20, 30));
            addSlot(new MachineSlot(blockEntity.getInventory(), CreativeGeneratorBlockEntity.INPUTS.get(1), 20 + 18, 30));
            addSlot(new MachineSlot(blockEntity.getInventory(), CreativeGeneratorBlockEntity.INPUTS.get(2), 20 + 18 * 2, 30));
            addSlot(new MachineSlot(blockEntity.getInventory(), CreativeGeneratorBlockEntity.INPUTS.get(3), 20 + 18 * 3, 30));
        }
        addInventorySlots(8, 103, true);
    }

    public static CreativeGeneratorContainer factory(@Nullable MenuType<CreativeGeneratorContainer> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf)
    {
        BlockEntity entity = inventory.player.level().getBlockEntity(buf.readBlockPos());
        if (entity instanceof CreativeGeneratorBlockEntity castBlockEntity)
            return new CreativeGeneratorContainer(castBlockEntity, inventory, pContainerId);
        LogManager.getLogger().warn("couldn't find BlockEntity");
        return new CreativeGeneratorContainer(null, inventory, pContainerId);
    }
}
