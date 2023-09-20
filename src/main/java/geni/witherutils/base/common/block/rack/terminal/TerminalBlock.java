package geni.witherutils.base.common.block.rack.terminal;

import javax.annotation.Nullable;

import geni.witherutils.api.block.BStateProperties;
import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.base.common.block.rack.IBlockMulti;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.core.common.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class TerminalBlock extends WitherAbstractBlock implements IBlockMulti, EntityBlock {
	
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty FORMED = BooleanProperty.create("formed");
    
    public TerminalBlock(BlockBehaviour.Properties props)
	{
		super(props);
        registerDefaultState(defaultBlockState().setValue(FORMED, false));
		this.setHasTooltip();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx)
    {
        BlockState state = super.getStateForPlacement(ctx);
        if (state != null)
        {
            Direction f = ctx.getNearestLookingDirection();
            state = state.setValue(FACING, reversePlacementRotation() ? f.getOpposite() : f);
        }
        return state;
    }
    protected boolean reversePlacementRotation()
    {
        return false;
    }

	@Override
	public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos)
	{
        Block block = state.getBlock();
        if(block != this)
        {
            return block.getLightEmission(state, level, pos);
        }
        return state.getValue(FORMED) ? 9 : 0;
	}
    
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity par5EntityLiving, ItemStack iStack)
    {
        super.setPlacedBy(level, pos, state, par5EntityLiving, iStack);
        if (!level.isClientSide && TerminalBlockEntity.checkForming(level, pos)) {};
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(BStateProperties.FORMED, FACING);
    }
    
    @Override
    public BlockState rotate(BlockState state, Rotation rotation)
    {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
	@Override
    public BlockState mirror(BlockState state, Mirror mirrorIn)
    {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    protected boolean rotateForgeWay()
    {
        return true;
    }
    protected boolean rotateCustom(Level world, BlockPos pos, BlockState state, Direction side)
    {
        return false;
    }
    
	@Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() != newState.getBlock())
        {
            invalidateMultiBlock(world, pos);
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    private void invalidateMultiBlock(Level world, BlockPos pos)
    {
        if (!world.isClientSide)
        {
            BlockEntityUtil.getTileEntityAt(world, pos, TerminalBlockEntity.class).ifPresent(teTerminal -> {
            	
                if (teTerminal.multiBlockSize > 0)
                {
                	teTerminal.onMultiBlockBreak();
                }
                else if (teTerminal.accessoryTerminals.size() > 0)
                {
                	teTerminal.accessoryTerminals.stream()
                            .filter(terminal -> terminal.multiBlockSize > 0)
                            .findFirst()
                            .ifPresent(TerminalBlockEntity::onMultiBlockBreak);
                }
            });
        }
    }

//    @Override
//    public void attack(BlockState state, Level worldIn, BlockPos pos, Player player)
//    {
//    	if(state.getValue(FORMED))
//    	BlockEntityUtil.getTileEntityAt(worldIn, pos.relative(state.getValue(FACING)),
//    			ControllerItemBlockEntity.class).ifPresent(tile -> tile.onLeftClicked(player));
//    }
//    @Override
//    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult brtr)
//    {
//        return world.getBlockEntity(pos.relative(state.getValue(FACING)), WUTEntities.RACKITEM_CONTROLLER.get()).map(te -> {
//        	if(state.getValue(FORMED))
//        	te.onRightClicked(player, hand);
//            return InteractionResult.SUCCESS;
//        }).orElse(InteractionResult.SUCCESS);
//    }
    
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new TerminalBlockEntity(pos, state);
    }
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> pBlockEntityType)
    {
        return createTickerHelper(pBlockEntityType, WUTEntities.RACK_TERMINAL.get(), TerminalBlockEntity::tick);
    }
}