package geni.witherutils.base.common.block.miner.advanced;

import geni.witherutils.base.common.base.WitherAbstractBlock;
import geni.witherutils.base.common.init.WUTEntities;
import geni.witherutils.core.common.util.BlockstatesUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
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

public class MinerAdvancedBlock extends WitherAbstractBlock implements EntityBlock {

	public static final DirectionProperty FACING = BlockStateProperties.FACING;
    
	public MinerAdvancedBlock(BlockBehaviour.Properties props)
	{
		super(props);
	    this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));
	    this.setHasScreen();
		this.setHasTooltip();
	    this.setHasSoulBankSlot();
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		if(placer != null)
			world.setBlock(pos, state.setValue(FACING, BlockstatesUtil.getFacingFromEntity(pos, placer)), 2);
        BlockEntity te = world.getBlockEntity(pos);
        if (!world.isClientSide && te instanceof MinerAdvancedBlockEntity && placer instanceof Player)
        {
            ((MinerAdvancedBlockEntity) te).setPlayer((Player) placer);
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

	@SuppressWarnings({ "deprecation", "unused" })
	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
	{
		if(!state.getValue(LIT))
			return;

		if(true || (level.getGameTime() & 0x1) == 0)
		{
			SoundEvent sound = SoundEvents.WOOD_HIT;
			BlockState target_state = level.getBlockState(pos.relative(state.getValue(FACING)));
			SoundType stype = target_state.getBlock().getSoundType(target_state);
			if ((stype == SoundType.WOOL) || (stype == SoundType.GRASS) || (stype == SoundType.SNOW))
			{
				sound = SoundEvents.WOOL_HIT;
			}
			else if ((stype == SoundType.GRAVEL) || (stype == SoundType.SAND))
			{
				sound = SoundEvents.GRAVEL_HIT;
			}
			level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), sound, SoundSource.BLOCKS, 0.1f, 1.2f, false);
		}
		{
			final double rv = random.nextDouble();
			if (rv < 0.8)
			{
				final double x = 0.5 + pos.getX(), y = 0.5 + pos.getY(), z = 0.5 + pos.getZ();
				final double xc = 0.52, xr = random.nextDouble() * 0.4 - 0.2, yr = (y - 0.3 + random.nextDouble() * 0.2);
				switch (state.getValue(BlockStateProperties.FACING))
				{
					case WEST -> level.addParticle(ParticleTypes.SMOKE, x - xc, yr, z + xr, 0.0, 0.0, 0.0);
					case EAST -> level.addParticle(ParticleTypes.SMOKE, x + xc, yr, z + xr, 0.0, 0.0, 0.0);
					case NORTH -> level.addParticle(ParticleTypes.SMOKE, x + xr, yr, z - xc, 0.0, 0.0, 0.0);
					default -> level.addParticle(ParticleTypes.SMOKE, x + xr, yr, z + xc, 0.0, 0.0, 0.0);
				}
			}
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, LIT);
	}
	
    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos)
    {
        Block block = state.getBlock();
        if(block != this)
        {
            return block.getLightEmission(state, level, pos);
        }
        return state.getValue(LIT) ? 15 : 0;
    }
    
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new MinerAdvancedBlockEntity(pos, state);
	}
	
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type)
	{
		return createTickerHelper(type, WUTEntities.MINERADV.get(), MinerAdvancedBlockEntity::tick);
	}
}
