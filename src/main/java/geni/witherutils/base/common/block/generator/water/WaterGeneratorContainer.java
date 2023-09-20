package geni.witherutils.base.common.block.generator.water;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;

import geni.witherutils.base.common.base.MachineSlot;
import geni.witherutils.base.common.base.WitherMachineMenu;
import geni.witherutils.base.common.init.WUTMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class WaterGeneratorContainer extends WitherMachineMenu<WaterGeneratorBlockEntity> {

	public WaterGeneratorContainer(WaterGeneratorBlockEntity blockEntity, Inventory inventory, int pContainerId)
	{
		super(blockEntity, inventory, WUTMenus.WATER_GENERATOR.get(), pContainerId);
		if (blockEntity != null)
		{
			if (blockEntity.requiresSoulBank())
			{
				addSlot(new MachineSlot(blockEntity.getInventory(), blockEntity.getSoulBankSlot(), 7, 64));
			}
			addSlot(new MachineSlot(blockEntity.getInventory(), WaterGeneratorBlockEntity.INPUT, 151, 27));
			addSlot(new MachineSlot(getBlockEntity().getInventory(), WaterGeneratorBlockEntity.OUTPUT, 151, 59));
		}
		addInventorySlots(8, 103, true);
	}
	
    public static WaterGeneratorContainer factory(@Nullable MenuType<WaterGeneratorContainer> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf)
    {
        BlockEntity entity = inventory.player.level().getBlockEntity(buf.readBlockPos());
        if (entity instanceof WaterGeneratorBlockEntity castBlockEntity)
            return new WaterGeneratorContainer(castBlockEntity, inventory, pContainerId);
        LogManager.getLogger().warn("couldn't find BlockEntity");
        return new WaterGeneratorContainer(null, inventory, pContainerId);
    }
}
