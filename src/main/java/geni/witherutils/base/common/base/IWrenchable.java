package geni.witherutils.base.common.base;

import javax.annotation.Nullable;

import geni.witherutils.api.UseOnly;
import net.minecraft.core.Direction;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.LogicalSide;

public interface IWrenchable {
    
    @UseOnly(LogicalSide.SERVER)
    ItemInteractionResult onWrenched(@Nullable Player player, @Nullable Direction side);
}
