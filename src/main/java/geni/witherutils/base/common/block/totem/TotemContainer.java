package geni.witherutils.base.common.block.totem;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;

import geni.witherutils.base.common.init.WUTMenus;
import geni.witherutils.base.common.menu.WitherMachineMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TotemContainer extends WitherMachineMenu<TotemBlockEntity> {

    public TotemContainer(@Nullable TotemBlockEntity blockEntity, Inventory inventory, int pContainerId)
    {
        super(WUTMenus.TOTEM.get(), pContainerId, inventory, blockEntity);
    }

    public static TotemContainer factory(@Nullable MenuType<TotemContainer> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf)
    {
        BlockEntity entity = inventory.player.level().getBlockEntity(buf.readBlockPos());
        if (entity instanceof TotemBlockEntity castBlockEntity)
            return new TotemContainer(castBlockEntity, inventory, pContainerId);
        LogManager.getLogger().warn("couldn't find BlockEntity");
        return new TotemContainer(null, inventory, pContainerId);
    }
}
