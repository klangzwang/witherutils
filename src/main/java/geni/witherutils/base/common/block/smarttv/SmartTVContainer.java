package geni.witherutils.base.common.block.smarttv;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;

import geni.witherutils.base.common.base.WitherMachineMenu;
import geni.witherutils.base.common.init.WUTMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SmartTVContainer extends WitherMachineMenu<SmartTVBlockEntity> {

    public SmartTVContainer(@Nullable SmartTVBlockEntity blockEntity, Inventory inventory, int pContainerId)
    {
        super(blockEntity, inventory, WUTMenus.SMARTTV.get(), pContainerId);
    }

    public static SmartTVContainer factory(@Nullable MenuType<SmartTVContainer> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf)
    {
        BlockEntity entity = inventory.player.level().getBlockEntity(buf.readBlockPos());
        if (entity instanceof SmartTVBlockEntity castBlockEntity)
            return new SmartTVContainer(castBlockEntity, inventory, pContainerId);
        LogManager.getLogger().warn("couldn't find BlockEntity");
        return new SmartTVContainer(null, inventory, pContainerId);
    }
}

