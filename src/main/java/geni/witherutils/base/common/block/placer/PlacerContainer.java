package geni.witherutils.base.common.block.placer;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;

import geni.witherutils.base.common.base.MachineSlot;
import geni.witherutils.base.common.base.WitherMachineMenu;
import geni.witherutils.base.common.init.WUTMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PlacerContainer extends WitherMachineMenu<PlacerBlockEntity> {

	public PlacerContainer(PlacerBlockEntity blockEntity, Inventory inventory, int pContainerId)
	{
		super(blockEntity, inventory, WUTMenus.PLACER.get(), pContainerId);
		if (blockEntity != null)
		{
            if (blockEntity.requiresSoulBank())
            {
                addSlot(new MachineSlot(blockEntity.getInventory(), blockEntity.getSoulBankSlot(), 7, 64));
            }
    	    addSlot(new MachineSlot(blockEntity.getInventory(), PlacerBlockEntity.INPUT, 153, 42));
		}
        addInventorySlots(8, 90+13, true);
	}
	
    public static PlacerContainer factory(@Nullable MenuType<PlacerContainer> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf)
    {
        BlockEntity entity = inventory.player.level().getBlockEntity(buf.readBlockPos());
        if (entity instanceof PlacerBlockEntity castBlockEntity)
            return new PlacerContainer(castBlockEntity, inventory, pContainerId);
        LogManager.getLogger().warn("couldn't find BlockEntity");
        return new PlacerContainer(null, inventory, pContainerId);
    }
}
