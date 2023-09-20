package geni.witherutils.base.common.block.scanner;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;

import geni.witherutils.base.common.base.MachineSlot;
import geni.witherutils.base.common.base.WitherMachineMenu;
import geni.witherutils.base.common.init.WUTMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ScannerContainer extends WitherMachineMenu<ScannerBlockEntity> {

	public ScannerContainer(ScannerBlockEntity blockEntity, Inventory inventory, int pContainerId)
	{
		super(blockEntity, inventory, WUTMenus.SCANNER.get(), pContainerId);
		if (blockEntity != null)
		{
			addSlot(new MachineSlot(blockEntity.getInventory(), ScannerBlockEntity.INPUT, 79, 24));
		}
		addInventorySlots(8, 103, true);
	}
	
    public static ScannerContainer factory(@Nullable MenuType<ScannerContainer> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf)
    {
        BlockEntity entity = inventory.player.level().getBlockEntity(buf.readBlockPos());
        if (entity instanceof ScannerBlockEntity castBlockEntity)
            return new ScannerContainer(castBlockEntity, inventory, pContainerId);
        LogManager.getLogger().warn("couldn't find BlockEntity");
        return new ScannerContainer(null, inventory, pContainerId);
    }
}
