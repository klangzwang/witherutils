package geni.witherutils.base.common.block.clicker;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;

import geni.witherutils.base.common.base.MachineSlot;
import geni.witherutils.base.common.base.WitherMachineMenu;
import geni.witherutils.base.common.init.WUTMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ClickerContainer extends WitherMachineMenu<ClickerBlockEntity> {

	public ClickerContainer(ClickerBlockEntity blockEntity, Inventory inventory, int pContainerId)
	{
		super(blockEntity, inventory, WUTMenus.CLICKER.get(), pContainerId);
		if (blockEntity != null)
		{
			addSlot(new MachineSlot(blockEntity.getInventory(), ClickerBlockEntity.INPUT, 12, 53));
		}
		addInventorySlots(8, 96 + 15, true);
	}
	
    public static ClickerContainer factory(@Nullable MenuType<ClickerContainer> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf)
    {
        BlockEntity entity = inventory.player.level().getBlockEntity(buf.readBlockPos());
        if (entity instanceof ClickerBlockEntity castBlockEntity)
            return new ClickerContainer(castBlockEntity, inventory, pContainerId);
        LogManager.getLogger().warn("couldn't find BlockEntity");
        return new ClickerContainer(null, inventory, pContainerId);
    }
}
