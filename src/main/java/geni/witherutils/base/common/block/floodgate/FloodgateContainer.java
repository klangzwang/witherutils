package geni.witherutils.base.common.block.floodgate;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;

import geni.witherutils.base.common.base.WitherMachineMenu;
import geni.witherutils.base.common.init.WUTMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FloodgateContainer extends WitherMachineMenu<FloodgateBlockEntity> {

    public FloodgateContainer(@Nullable FloodgateBlockEntity blockEntity, Inventory inventory, int pContainerId)
    {
        super(blockEntity, inventory, WUTMenus.FLOODGATE.get(), pContainerId);
    }

    public static FloodgateContainer factory(@Nullable MenuType<FloodgateContainer> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf)
    {
        BlockEntity entity = inventory.player.level().getBlockEntity(buf.readBlockPos());
        if (entity instanceof FloodgateBlockEntity castBlockEntity)
            return new FloodgateContainer(castBlockEntity, inventory, pContainerId);
        LogManager.getLogger().warn("couldn't find BlockEntity");
        return new FloodgateContainer(null, inventory, pContainerId);
    }
}
