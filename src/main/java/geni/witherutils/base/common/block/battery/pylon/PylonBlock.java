package geni.witherutils.base.common.block.battery.pylon;

import java.util.Locale;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.base.common.init.WUTEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class PylonBlock extends WitherAbstractBlock implements EntityBlock {
	
	public static final EnumProperty<Mode> MODE = EnumProperty.create("mode", Mode.class);
	public static final DirectionProperty FACING = DirectionalBlock.FACING;
	
	public PylonBlock(BlockBehaviour.Properties props)
	{
		super(props);
	    this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP).setValue(MODE, Mode.OUTPUT));
		this.setHasTooltip();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, MODE);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		Direction facing = context.getClickedFace();;
		return this.defaultBlockState().setValue(FACING, facing);
	}
    
	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
	{
        return level.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).getBlock() != this;
	}
	
	@Override
	public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos)
	{
        return 8;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new PylonBlockEntity(pos, state);
	}
	
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type)
	{
		return createTickerHelper(type, WUTEntities.PYLON.get(), PylonBlockEntity::tick);
	}
	
    public enum Mode implements StringRepresentable
    {
        OUTPUT,
        INPUT;

        public boolean canReceive()
        {
            return this == INPUT;
        }
        public boolean canExtract()
        {
            return this == OUTPUT;
        }
        public Mode reverse()
        {
            return this == OUTPUT ? INPUT : OUTPUT;
        }
        @Override
        public String getSerializedName()
        {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
