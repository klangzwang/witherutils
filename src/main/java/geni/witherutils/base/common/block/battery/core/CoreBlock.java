package geni.witherutils.base.common.block.battery.core;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.base.common.init.WUTEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class CoreBlock extends WitherAbstractBlock implements EntityBlock {

    public CoreBlock(BlockBehaviour.Properties props)
	{
		super(props);
		this.setHasTooltip();
    }

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new CoreBlockEntity(pos, state);
	}
	
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type)
	{
		return createTickerHelper(type, WUTEntities.CORE.get(), CoreBlockEntity::tick);
	}
}
