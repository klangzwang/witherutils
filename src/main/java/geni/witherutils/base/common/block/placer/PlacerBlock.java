package geni.witherutils.base.common.block.placer;

import java.util.Random;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.core.common.util.BlockstatesUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PlacerBlock extends WitherAbstractBlock implements EntityBlock {

	public static final DirectionProperty FACING = BlockStateProperties.FACING;
    
	public PlacerBlock(BlockBehaviour.Properties props)
	{
		super(props);
	    this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));
	    this.setHasScreen();
		this.setHasTooltip();
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		if(placer != null)
			world.setBlock(pos, state.setValue(FACING, BlockstatesUtil.getFacingFromEntity(pos, placer)), 2);
        BlockEntity te = world.getBlockEntity(pos);
        if (!world.isClientSide && te instanceof PlacerBlockEntity && placer instanceof Player)
        {
            ((PlacerBlockEntity) te).setPlayer((Player) placer);
        }
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
	
	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos)
	{
		return true;
	}
	@Override
	public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState)
	{
		return true;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(BlockStateProperties.FACING).add(LIT);
	}

	@OnlyIn(Dist.CLIENT)
	@SuppressWarnings({ "deprecation", "unused" })
	public void animateTick(BlockState state, Level world, BlockPos pos, Random rnd) 
	{
		if(!state.getValue(LIT))
			return;

		if(true || (world.getGameTime() & 0x1) == 0)
		{
			SoundEvent sound = SoundEvents.WOOD_PLACE;
			BlockState target_state = world.getBlockState(pos.relative(state.getValue(BlockStateProperties.FACING)));
			SoundType stype = target_state.getBlock().getSoundType(target_state);
			if ((stype == SoundType.WOOL) || (stype == SoundType.GRASS) || (stype == SoundType.SNOW)) {
				sound = SoundEvents.WOOL_PLACE;
			} else if ((stype == SoundType.GRAVEL) || (stype == SoundType.SAND)) {
				sound = SoundEvents.GRAVEL_PLACE;
			}
			world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), sound, SoundSource.BLOCKS, 0.1f, 1.2f, false);
		}

		{
			final double rv = rnd.nextDouble();
			if (rv < 0.8) {
				final double x = 0.5 + pos.getX(), y = 0.5 + pos.getY(), z = 0.5 + pos.getZ();
				final double xc = 0.52, xr = rnd.nextDouble() * 0.4 - 0.2, yr = (y - 0.3 + rnd.nextDouble() * 0.2);
				switch (state.getValue(BlockStateProperties.FACING)) {
				case WEST -> world.addParticle(ParticleTypes.SMOKE, x - xc, yr, z + xr, 0.0, 0.0, 0.0);
				case EAST -> world.addParticle(ParticleTypes.SMOKE, x + xc, yr, z + xr, 0.0, 0.0, 0.0);
				case NORTH -> world.addParticle(ParticleTypes.SMOKE, x + xr, yr, z - xc, 0.0, 0.0, 0.0);
				default -> world.addParticle(ParticleTypes.SMOKE, x + xr, yr, z + xc, 0.0, 0.0, 0.0);
				}
			}
		}
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new PlacerBlockEntity(pos, state);
	}
	
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type)
	{
		return createTickerHelper(type, WUTEntities.PLACER.get(), PlacerBlockEntity::tick);
	}
}
