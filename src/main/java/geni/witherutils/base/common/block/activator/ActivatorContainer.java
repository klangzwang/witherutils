package geni.witherutils.base.common.block.activator;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;

import geni.witherutils.base.common.base.WitherMachineMenu;
import geni.witherutils.base.common.init.WUTMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ActivatorContainer extends WitherMachineMenu<ActivatorBlockEntity> {

	public ActivatorContainer(ActivatorBlockEntity blockEntity, Inventory inventory, int pContainerId)
	{
		super(blockEntity, inventory, WUTMenus.ACTIVATOR.get(), pContainerId);
	}
	
    public static ActivatorContainer factory(@Nullable MenuType<ActivatorContainer> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf)
    {
        BlockEntity entity = inventory.player.level().getBlockEntity(buf.readBlockPos());
        if (entity instanceof ActivatorBlockEntity castBlockEntity)
            return new ActivatorContainer(castBlockEntity, inventory, pContainerId);
        LogManager.getLogger().warn("couldn't find BlockEntity");
        return new ActivatorContainer(null, inventory, pContainerId);
    }
}
