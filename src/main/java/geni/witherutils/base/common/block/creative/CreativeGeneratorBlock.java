package geni.witherutils.base.common.block.creative;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.base.common.init.WUTEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class CreativeGeneratorBlock extends WitherAbstractBlock implements EntityBlock {
	
	public CreativeGeneratorBlock(BlockBehaviour.Properties props)
	{
		super(props);
		this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.valueOf(false)));
		this.setHasScreen();
		this.setHasTooltip();
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(LIT);
	}
	
	@Override
	public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos)
	{
        return 15;
	}
    
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new CreativeGeneratorBlockEntity(pos, state);
	}
	
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type)
	{
		return createTickerHelper(type, WUTEntities.CREATIVE_GENERATOR.get(), CreativeGeneratorBlockEntity::tick);
	}
}