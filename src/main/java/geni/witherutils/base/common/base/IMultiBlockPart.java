package geni.witherutils.base.common.base;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

public interface IMultiBlockPart {

    default InteractionResult handleRemoteClick(Player player, InteractionHand hand, BlockHitResult hit)
    {
        return InteractionResult.PASS;
    }
}