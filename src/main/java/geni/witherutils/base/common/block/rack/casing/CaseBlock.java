package geni.witherutils.base.common.block.rack.casing;

import static geni.witherutils.api.block.BStateProperties.FORMED;
import static geni.witherutils.api.block.BStateProperties.MBSTATE;

import org.jetbrains.annotations.Nullable;

import geni.witherutils.api.block.BStateProperties;
import geni.witherutils.api.block.MultiBlockState;
import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.base.common.block.rack.IBlockMulti;
import geni.witherutils.base.common.block.rack.terminal.TerminalBlockEntity;
import geni.witherutils.core.common.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CaseBlock extends WitherAbstractBlock implements IBlockMulti, EntityBlock {
	
    public CaseBlock(BlockBehaviour.Properties props)
	{
		super(props);
        registerDefaultState(defaultBlockState().setValue(BStateProperties.MBSTATE, MultiBlockState.NONE));
		this.setHasTooltip();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(BStateProperties.MBSTATE);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity par5EntityLiving, ItemStack iStack)
    {
        super.setPlacedBy(level, pos, state, par5EntityLiving, iStack);
        if (!level.isClientSide && TerminalBlockEntity.checkForming(level, pos)) {};
    }

	@Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() != newState.getBlock() && !world.isClientSide)
        	BlockEntityUtil.getTileEntityAt(world, pos, CaseBlockEntity.class).ifPresent(CaseBlockEntity::onBlockBreak);
        super.onRemove(state, world, pos, newState, isMoving);
    }

	public static boolean isFormed(BlockState state)
    {
        if (state.hasProperty(MBSTATE))
        {
            return state.getValue(MBSTATE) != MultiBlockState.NONE;
        }
        else if (state.hasProperty(FORMED))
        {
            return state.getValue(FORMED);
        }
        else
        {
            return false;
        }
    }
    
	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos)
	{
		return true;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side)
	{
		return adjacentBlockState.getBlock() == this;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos)
	{
		return 1.0F;
	}
	
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
    {
        return new CaseBlockEntity(pPos, pState);
    }
}
