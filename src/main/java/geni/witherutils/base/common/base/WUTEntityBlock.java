package geni.witherutils.base.common.base;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

import geni.witherutils.core.common.blockentity.WitherBlockEntity;

public interface WUTEntityBlock extends EntityBlock {
	
    @Nullable
    @Override
    default <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType)
    {
        return (level1, blockPos, blockState, t) -> {
        	
            if (t instanceof WitherBlockEntity tickable)
            {
                WitherBlockEntity.tick(level1, blockPos, blockState, tickable);
                
                if (level1.isClientSide())
                {
                    tickable.clientTick();
                }
                else
                {
                    tickable.serverTick();
                }
            }
        };
    }
}
