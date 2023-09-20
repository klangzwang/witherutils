package geni.witherutils.base.common.base;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public interface IInteractBlockEntity {

    @Deprecated
    default boolean onBlockActivated(BlockState state, Player player, InteractionHand hand, BlockHitResult hit)
    {
        return false;
    }
    default InteractionResult onBlockUse(BlockState state, Player player, InteractionHand hand, BlockHitResult hit)
    {
        return onBlockActivated(state, player, hand, hit) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
    }
    default InteractionResult useOnBlock(UseOnContext context)
    {
		return InteractionResult.SUCCESS;
    }
    default void onBlockAttack(BlockState state, Player player) {}
}
