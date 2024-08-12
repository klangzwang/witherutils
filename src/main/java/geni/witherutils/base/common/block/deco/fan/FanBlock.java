package geni.witherutils.base.common.block.deco.fan;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class FanBlock extends WitherAbstractBlock {
	
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	
	public FanBlock(BlockBehaviour.Properties props)
	{
		super(props);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
		this.setHasTooltip();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		if(placer != null)
			world.setBlock(pos, state.setValue(FACING, getFacingFromEntity(pos, placer)), 2);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		Player placer = context.getPlayer();
		Direction face = placer.getDirection().getOpposite();
		if (placer.getXRot() > 50) face = Direction.UP;
		else if (placer.getXRot() < -50) face = Direction.DOWN;
		return this.defaultBlockState().setValue(FACING, face);
	}
	
//	@Override
//	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
//	{
//		if(level.getGameTime() % 20 == 0)
//		{
//			SoundUtil.playDistanceSound(Minecraft.getInstance(), level, pos, WUTSounds.LASER0.get(), 30);
//		}
//	}
}
