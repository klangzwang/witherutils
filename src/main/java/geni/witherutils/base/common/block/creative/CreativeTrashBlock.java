package geni.witherutils.base.common.block.creative;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.base.common.init.WUTEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class CreativeTrashBlock extends WitherAbstractBlock implements EntityBlock {
	
	public CreativeTrashBlock(BlockBehaviour.Properties props)
	{
		super(props);
		this.setHasTooltip();
	}
	
	@Override
	public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos)
	{
        return 15;
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new CreativeTrashBlockEntity(pos, state);
	}
	
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type)
	{
		return createTickerHelper(type, WUTEntities.CREATIVE_TRASH.get(), CreativeTrashBlockEntity::tick);
	}
}