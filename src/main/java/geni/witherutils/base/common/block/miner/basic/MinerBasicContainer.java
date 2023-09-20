package geni.witherutils.base.common.block.miner.basic;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;

import geni.witherutils.base.common.base.MachineSlot;
import geni.witherutils.base.common.base.WitherMachineMenu;
import geni.witherutils.base.common.init.WUTMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class MinerBasicContainer extends WitherMachineMenu<MinerBasicBlockEntity> {

	public MinerBasicContainer(MinerBasicBlockEntity blockEntity, Inventory inventory, int pContainerId)
	{
		super(blockEntity, inventory, WUTMenus.MINERBASIC.get(), pContainerId);
		if (blockEntity != null)
		{
			if (blockEntity.requiresSoulBank())
			{
				addSlot(new MachineSlot(blockEntity.getInventory(), blockEntity.getSoulBankSlot(), 7, 64));
			}
			
			addSlot(new MachineSlot(blockEntity.getInventory(), MinerBasicBlockEntity.INPUTTOOL, 76, 30));
		}
		addInventorySlots(8, 103, true);
	}
	
    public static MinerBasicContainer factory(@Nullable MenuType<MinerBasicContainer> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf)
    {
        BlockEntity entity = inventory.player.level().getBlockEntity(buf.readBlockPos());
        if (entity instanceof MinerBasicBlockEntity castBlockEntity)
            return new MinerBasicContainer(castBlockEntity, inventory, pContainerId);
        LogManager.getLogger().warn("couldn't find BlockEntity");
        return new MinerBasicContainer(null, inventory, pContainerId);
    }
}
