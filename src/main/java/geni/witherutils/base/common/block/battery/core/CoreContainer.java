package geni.witherutils.base.common.block.battery.core;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;

import geni.witherutils.base.common.base.WitherMachineMenu;
import geni.witherutils.base.common.init.WUTMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CoreContainer extends WitherMachineMenu<CoreBlockEntity> {

    public CoreContainer(@Nullable CoreBlockEntity blockEntity, Inventory inventory, int pContainerId)
    {
        super(blockEntity, inventory, WUTMenus.CORE.get(), pContainerId);
    }
    
    public static CoreContainer factory(@Nullable MenuType<CoreContainer> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf)
    {
        BlockEntity entity = inventory.player.level().getBlockEntity(buf.readBlockPos());
        if (entity instanceof CoreBlockEntity castBlockEntity)
            return new CoreContainer(castBlockEntity, inventory, pContainerId);
        LogManager.getLogger().warn("couldn't find BlockEntity");
        return new CoreContainer(null, inventory, pContainerId);
    }
}
